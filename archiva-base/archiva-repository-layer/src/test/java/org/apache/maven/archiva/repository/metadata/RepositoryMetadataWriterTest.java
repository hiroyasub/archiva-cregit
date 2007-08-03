begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|metadata
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaRepositoryMetadata
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
name|PlexusTestCase
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
name|StringWriter
import|;
end_import

begin_comment
comment|/**  * RepositoryMetadataWriterTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryMetadataWriterTest
extends|extends
name|PlexusTestCase
block|{
specifier|public
name|void
name|testWriteSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|defaultRepoDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repositories/default-repository"
argument_list|)
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultRepoDir
argument_list|,
literal|"org/apache/maven/shared/maven-downloader/maven-metadata.xml"
argument_list|)
decl_stmt|;
name|String
name|expectedContent
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|expectedFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ArchivaRepositoryMetadata
name|metadata
init|=
operator|new
name|ArchivaRepositoryMetadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setGroupId
argument_list|(
literal|"org.apache.maven.shared"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setArtifactId
argument_list|(
literal|"maven-downloader"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setVersion
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setReleasedVersion
argument_list|(
literal|"1.1"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|add
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|add
argument_list|(
literal|"1.1"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setLastUpdated
argument_list|(
literal|"20061212214311"
argument_list|)
expr_stmt|;
name|StringWriter
name|actual
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|RepositoryMetadataWriter
operator|.
name|write
argument_list|(
name|metadata
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|XMLAssert
operator|.
name|assertXMLEqual
argument_list|(
literal|"XML Contents"
argument_list|,
name|expectedContent
argument_list|,
name|actual
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

