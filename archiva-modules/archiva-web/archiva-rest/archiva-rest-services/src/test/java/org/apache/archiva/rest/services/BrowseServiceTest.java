begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|ProjectVersionMetadata
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ArtifactContentEntry
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|BrowseResult
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|BrowseResultEntry
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|Entry
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|VersionsList
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|BrowseService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fest
operator|.
name|assertions
operator|.
name|MapAssert
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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fest
operator|.
name|assertions
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|BrowseServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toMap
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|entries
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
name|entry
range|:
name|entries
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|metadatagetthenadd
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|metadatas
init|=
name|toMap
argument_list|(
name|browseService
operator|.
name|getMetadatas
argument_list|(
literal|"commons-cli"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|,
name|testRepoId
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|metadatas
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|browseService
operator|.
name|addMetadata
argument_list|(
literal|"commons-cli"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|,
literal|"wine"
argument_list|,
literal|"bordeaux"
argument_list|,
name|testRepoId
argument_list|)
expr_stmt|;
name|metadatas
operator|=
name|toMap
argument_list|(
name|browseService
operator|.
name|getMetadatas
argument_list|(
literal|"commons-cli"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|,
name|testRepoId
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|metadatas
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|includes
argument_list|(
name|MapAssert
operator|.
name|entry
argument_list|(
literal|"wine"
argument_list|,
literal|"bordeaux"
argument_list|)
argument_list|)
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|metadatagetthenaddthendelete
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|metadatas
init|=
name|toMap
argument_list|(
name|browseService
operator|.
name|getMetadatas
argument_list|(
literal|"commons-cli"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|,
name|testRepoId
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|metadatas
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|browseService
operator|.
name|addMetadata
argument_list|(
literal|"commons-cli"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|,
literal|"wine"
argument_list|,
literal|"bordeaux"
argument_list|,
name|testRepoId
argument_list|)
expr_stmt|;
name|metadatas
operator|=
name|toMap
argument_list|(
name|browseService
operator|.
name|getMetadatas
argument_list|(
literal|"commons-cli"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|,
name|testRepoId
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|metadatas
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|includes
argument_list|(
name|MapAssert
operator|.
name|entry
argument_list|(
literal|"wine"
argument_list|,
literal|"bordeaux"
argument_list|)
argument_list|)
expr_stmt|;
name|browseService
operator|.
name|deleteMetadata
argument_list|(
literal|"commons-cli"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|,
literal|"wine"
argument_list|,
name|testRepoId
argument_list|)
expr_stmt|;
name|metadatas
operator|=
name|toMap
argument_list|(
name|browseService
operator|.
name|getMetadatas
argument_list|(
literal|"commons-cli"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|,
name|testRepoId
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|metadatas
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|browserootGroups
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|BrowseResult
name|browseResult
init|=
name|browseService
operator|.
name|getRootGroups
argument_list|(
name|testRepoId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|browseResult
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|browseResult
operator|.
name|getBrowseResultEntries
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|BrowseResultEntry
argument_list|(
literal|"commons-cli"
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|BrowseResultEntry
argument_list|(
literal|"commons-logging"
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|BrowseResultEntry
argument_list|(
literal|"org.apache"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|browsegroupId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|BrowseResult
name|browseResult
init|=
name|browseService
operator|.
name|browseGroupId
argument_list|(
literal|"org.apache"
argument_list|,
name|testRepoId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|browseResult
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|browseResult
operator|.
name|getBrowseResultEntries
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|BrowseResultEntry
argument_list|(
literal|"org.apache.felix"
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|BrowseResultEntry
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|versionsList
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|VersionsList
name|versions
init|=
name|browseService
operator|.
name|getVersionsList
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
literal|"org.apache.karaf.features.core"
argument_list|,
name|testRepoId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|versions
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|versions
operator|.
name|getVersions
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
operator|.
name|contains
argument_list|(
literal|"2.2.1"
argument_list|,
literal|"2.2.2"
argument_list|)
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getProjectVersionMetadata
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ProjectVersionMetadata
name|metadata
init|=
name|browseService
operator|.
name|getProjectVersionMetadata
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
literal|"org.apache.karaf.features.core"
argument_list|,
name|testRepoId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|metadata
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readArtifactContentEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactContentEntry
argument_list|>
name|artifactContentEntries
init|=
name|browseService
operator|.
name|getArtifactContentEntries
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.1"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|testRepoId
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifactContentEntries: {}"
argument_list|,
name|artifactContentEntries
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactContentEntries
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
argument_list|,
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"META-INF"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readArtifactContentEntriesRootPath
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactContentEntry
argument_list|>
name|artifactContentEntries
init|=
name|browseService
operator|.
name|getArtifactContentEntries
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.1"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"org"
argument_list|,
name|testRepoId
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifactContentEntries: {}"
argument_list|,
name|artifactContentEntries
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactContentEntries
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org/apache"
argument_list|,
literal|false
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

