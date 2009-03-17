begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|dependencytree
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|ConsumerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|KnownRepositoryContentConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|spring
operator|.
name|PlexusContainerAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|XMLAssert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_class
specifier|public
class|class
name|DependencyTreeGeneratorConsumerTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|DependencyTreeGeneratorConsumer
name|consumer
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|repository
decl_stmt|;
specifier|private
name|File
name|repositoryLocation
decl_stmt|;
specifier|private
name|File
name|generatedRepositoryLocation
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|consumer
operator|=
operator|(
name|DependencyTreeGeneratorConsumer
operator|)
name|lookup
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|,
literal|"dependency-tree-generator"
argument_list|)
expr_stmt|;
name|repositoryLocation
operator|=
name|getTestFile
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/test-repo"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repositoryLocation
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|getTestFile
argument_list|(
literal|"target/test-classes/test-repo"
argument_list|)
argument_list|,
name|repositoryLocation
argument_list|)
expr_stmt|;
name|generatedRepositoryLocation
operator|=
name|getTestFile
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/generated-test-repo"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|generatedRepositoryLocation
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setGeneratedRepositoryLocation
argument_list|(
name|generatedRepositoryLocation
argument_list|)
expr_stmt|;
name|repository
operator|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
expr_stmt|;
name|repository
operator|.
name|setId
argument_list|(
literal|"dependency-tree"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setLocation
argument_list|(
name|repositoryLocation
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGenerateBasicTree
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConsumerException
throws|,
name|ParserConfigurationException
throws|,
name|SAXException
block|{
name|consumer
operator|.
name|beginScan
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/maven/maven-core/2.0/maven-core-2.0.pom"
decl_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|File
name|generatedFile
init|=
operator|new
name|File
argument_list|(
name|generatedRepositoryLocation
argument_list|,
name|path
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|XMLAssert
operator|.
name|assertXMLEqual
argument_list|(
name|IOUtils
operator|.
name|toString
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/test-data/maven-core-2.0-tree.xml"
argument_list|)
argument_list|)
argument_list|,
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|generatedFile
argument_list|)
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidCoordinate
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConsumerException
block|{
name|consumer
operator|.
name|beginScan
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|path
init|=
literal|"openejb/jaxb-xjc/2.0EA3/jaxb-xjc-2.0EA3.pom"
decl_stmt|;
try|try
block|{
name|consumer
operator|.
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should not have successfully processed the file"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConsumerException
name|e
parameter_list|)
block|{
name|File
name|generatedFile
init|=
operator|new
name|File
argument_list|(
name|generatedRepositoryLocation
argument_list|,
name|path
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|generatedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testProfiles
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConsumerException
throws|,
name|ParserConfigurationException
throws|,
name|SAXException
block|{
name|PlexusContainerAdapter
name|container
init|=
operator|new
name|PlexusContainerAdapter
argument_list|()
decl_stmt|;
name|container
operator|.
name|setApplicationContext
argument_list|(
name|getApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/maven/surefire/surefire-testng/2.0/surefire-testng-2.0.pom"
decl_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|File
name|generatedFile
init|=
operator|new
name|File
argument_list|(
name|generatedRepositoryLocation
argument_list|,
name|path
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|XMLAssert
operator|.
name|assertXMLEqual
argument_list|(
name|IOUtils
operator|.
name|toString
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/test-data/surefire-testng-2.0-tree.xml"
argument_list|)
argument_list|)
argument_list|,
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|generatedFile
argument_list|)
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

