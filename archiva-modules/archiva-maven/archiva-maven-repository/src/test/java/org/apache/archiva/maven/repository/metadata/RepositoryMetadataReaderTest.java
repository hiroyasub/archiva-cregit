begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|repository
operator|.
name|metadata
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|metadata
operator|.
name|MavenMetadataReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
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
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|metadata
operator|.
name|RepositoryMetadataException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|test
operator|.
name|utils
operator|.
name|ArchivaBlockJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_comment
comment|/**  * RepositoryMetadataReaderTest  *  *  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|RepositoryMetadataReaderTest
extends|extends
name|TestCase
block|{
specifier|private
name|Path
name|getRepositoryPath
parameter_list|(
name|String
name|repoName
parameter_list|)
block|{
try|try
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|( )
operator|.
name|getContextClassLoader
argument_list|( )
operator|.
name|getResource
argument_list|(
literal|"repositories/"
operator|+
name|repoName
argument_list|)
operator|.
name|toURI
argument_list|( )
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not resolve repository path "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadSimple
parameter_list|()
throws|throws
name|RepositoryMetadataException
block|{
name|Path
name|defaultRepoDir
init|=
name|getRepositoryPath
argument_list|(
literal|"default-repository"
argument_list|)
decl_stmt|;
name|Path
name|metadataFile
init|=
name|defaultRepoDir
operator|.
name|resolve
argument_list|(
literal|"org/apache/maven/shared/maven-downloader/maven-metadata.xml"
argument_list|)
decl_stmt|;
name|MavenMetadataReader
name|metadataReader
init|=
operator|new
name|MavenMetadataReader
argument_list|( )
decl_stmt|;
name|ArchivaRepositoryMetadata
name|metadata
init|=
name|metadataReader
operator|.
name|read
argument_list|(
name|metadataFile
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Group Id"
argument_list|,
literal|"org.apache.maven.shared"
argument_list|,
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artifact Id"
argument_list|,
literal|"maven-downloader"
argument_list|,
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Released Version"
argument_list|,
literal|"1.1"
argument_list|,
name|metadata
operator|.
name|getReleasedVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"List of Available Versions"
argument_list|,
literal|2
argument_list|,
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Available version 1.0"
argument_list|,
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|contains
argument_list|(
literal|"1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Available version 1.1"
argument_list|,
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|contains
argument_list|(
literal|"1.1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadComplex
parameter_list|()
throws|throws
name|RepositoryMetadataException
block|{
name|Path
name|defaultRepoDir
init|=
name|getRepositoryPath
argument_list|(
literal|"default-repository"
argument_list|)
decl_stmt|;
name|Path
name|metadataFile
init|=
name|defaultRepoDir
operator|.
name|resolve
argument_list|(
literal|"org/apache/maven/samplejar/maven-metadata.xml"
argument_list|)
decl_stmt|;
name|MavenMetadataReader
name|metadataReader
init|=
operator|new
name|MavenMetadataReader
argument_list|( )
decl_stmt|;
name|ArchivaRepositoryMetadata
name|metadata
init|=
name|metadataReader
operator|.
name|read
argument_list|(
name|metadataFile
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Group Id"
argument_list|,
literal|"org.apache.maven"
argument_list|,
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artifact Id"
argument_list|,
literal|"samplejar"
argument_list|,
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Released Version"
argument_list|,
literal|"2.0"
argument_list|,
name|metadata
operator|.
name|getReleasedVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Latest Version"
argument_list|,
literal|"6.0-SNAPSHOT"
argument_list|,
name|metadata
operator|.
name|getLatestVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"List of Available Versions"
argument_list|,
literal|18
argument_list|,
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Available version 6.0-20060311.183228-10"
argument_list|,
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|contains
argument_list|(
literal|"6.0-20060311.183228-10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Available version 6.0-SNAPSHOT"
argument_list|,
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|contains
argument_list|(
literal|"6.0-SNAPSHOT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

