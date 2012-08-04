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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|common
operator|.
name|utils
operator|.
name|FileUtil
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
name|maven2
operator|.
name|model
operator|.
name|Artifact
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
name|ManagedRepositoriesService
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
name|RepositoriesService
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
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|ServerWebApplicationException
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
name|api
operator|.
name|Assertions
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|RepositoriesServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ServerWebApplicationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|scanRepoKarmaFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoriesService
name|service
init|=
name|getRepositoriesService
argument_list|()
decl_stmt|;
try|try
block|{
name|service
operator|.
name|scanRepository
argument_list|(
literal|"id"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServerWebApplicationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|403
argument_list|,
name|e
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|scanRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoriesService
name|service
init|=
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|ManagedRepositoriesService
name|managedRepositoriesService
init|=
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|String
name|repoId
init|=
name|managedRepositoriesService
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
decl_stmt|;
name|int
name|timeout
init|=
literal|20000
decl_stmt|;
while|while
condition|(
name|timeout
operator|>
literal|0
operator|&&
name|service
operator|.
name|alreadyScanning
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|timeout
operator|-=
literal|500
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|service
operator|.
name|scanRepository
argument_list|(
name|repoId
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ServerWebApplicationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|deleteArtifactKarmaFailed
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
literal|"commons-logging"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
literal|"commons-logging"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
literal|"1.0.1"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setPackaging
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setContext
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|repositoriesService
operator|.
name|deleteArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServerWebApplicationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|403
argument_list|,
name|e
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ServerWebApplicationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|deleteWithRepoNull
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
literal|"commons-logging"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
literal|"commons-logging"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
literal|"1.0.1"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setPackaging
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
name|repositoriesService
operator|.
name|deleteArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServerWebApplicationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"not http 400 status"
argument_list|,
literal|400
argument_list|,
name|e
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|deleteArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|initSourceTargetRepo
argument_list|()
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
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|browseService
operator|.
name|getArtifactDownloadInfos
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
literal|"org.apache.karaf.features.core"
argument_list|,
literal|"2.2.2"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts: {}"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifacts
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
expr_stmt|;
name|VersionsList
name|versionsList
init|=
name|browseService
operator|.
name|getVersionsList
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
literal|"org.apache.karaf.features.core"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|versionsList
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
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts.size: {}"
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
literal|"target/test-origin-repo/org/apache/karaf/features/org.apache.karaf.features.core/2.2.2/org.apache.karaf.features.core-2.2.2.jar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"artifact not exists:"
operator|+
name|artifactFile
operator|.
name|getPath
argument_list|()
argument_list|,
name|artifactFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
literal|"org.apache.karaf.features"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
literal|"org.apache.karaf.features.core"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
literal|"2.2.2"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setPackaging
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setContext
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|repositoriesService
operator|.
name|deleteArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"artifact not deleted exists:"
operator|+
name|artifactFile
operator|.
name|getPath
argument_list|()
argument_list|,
name|artifactFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|browseService
operator|.
name|getArtifactDownloadInfos
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
literal|"org.apache.karaf.features.core"
argument_list|,
literal|"2.2.2"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifacts
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|versionsList
operator|=
name|browseService
operator|.
name|getVersionsList
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
literal|"org.apache.karaf.features.core"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|versionsList
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
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|cleanRepos
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|deleteArtifactWithClassifier
parameter_list|()
throws|throws
name|Exception
block|{
name|initSourceTargetRepo
argument_list|()
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
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|browseService
operator|.
name|getArtifactDownloadInfos
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.0.1"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifacts
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
expr_stmt|;
name|VersionsList
name|versionsList
init|=
name|browseService
operator|.
name|getVersionsList
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|versionsList
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
literal|6
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts.size: {}"
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
literal|"target/test-origin-repo/commons-logging/commons-logging/1.0.1/commons-logging-1.0.1-javadoc.jar"
argument_list|)
decl_stmt|;
name|File
name|artifactFilemd5
init|=
operator|new
name|File
argument_list|(
literal|"target/test-origin-repo/commons-logging/commons-logging/1.0.1/commons-logging-1.0.1-javadoc.jar.md5"
argument_list|)
decl_stmt|;
name|File
name|artifactFilesha1
init|=
operator|new
name|File
argument_list|(
literal|"target/test-origin-repo/commons-logging/commons-logging/1.0.1/commons-logging-1.0.1-javadoc.jar.sha1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"artifact not exists:"
operator|+
name|artifactFile
operator|.
name|getPath
argument_list|()
argument_list|,
name|artifactFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"md5 not exists:"
operator|+
name|artifactFilemd5
operator|.
name|getPath
argument_list|()
argument_list|,
name|artifactFilemd5
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"sha1 not exists:"
operator|+
name|artifactFilesha1
operator|.
name|getPath
argument_list|()
argument_list|,
name|artifactFilesha1
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
literal|"commons-logging"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
literal|"commons-logging"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
literal|"1.0.1"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setClassifier
argument_list|(
literal|"javadoc"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setPackaging
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setContext
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|repositoriesService
operator|.
name|deleteArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"artifact not deleted exists:"
operator|+
name|artifactFile
operator|.
name|getPath
argument_list|()
argument_list|,
name|artifactFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"md5 still exists:"
operator|+
name|artifactFilemd5
operator|.
name|getPath
argument_list|()
argument_list|,
name|artifactFilemd5
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"sha1 still exists:"
operator|+
name|artifactFilesha1
operator|.
name|getPath
argument_list|()
argument_list|,
name|artifactFilesha1
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|browseService
operator|.
name|getArtifactDownloadInfos
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.0.1"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifact: {}"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifacts
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
expr_stmt|;
name|versionsList
operator|=
name|browseService
operator|.
name|getVersionsList
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"versionsList: {}"
argument_list|,
name|versionsList
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|versionsList
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
literal|6
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|cleanRepos
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|deleteGroupId
parameter_list|()
throws|throws
name|Exception
block|{
name|initSourceTargetRepo
argument_list|()
expr_stmt|;
try|try
block|{
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
literal|"org.apache.karaf.features"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|browseResult
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"browseResult: {}"
argument_list|,
name|browseResult
argument_list|)
expr_stmt|;
name|Assertions
operator|.
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
name|contains
argument_list|(
operator|new
name|BrowseResultEntry
argument_list|(
literal|"org.apache.karaf.features.org.apache.karaf.features.command"
argument_list|,
literal|true
argument_list|)
argument_list|,
operator|new
name|BrowseResultEntry
argument_list|(
literal|"org.apache.karaf.features.org.apache.karaf.features.core"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
literal|"target/test-origin-repo/org/apache/karaf/features/org.apache.karaf.features.command"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"directory not exists"
argument_list|,
name|directory
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|repositoriesService
operator|.
name|deleteGroupId
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"directory not exists"
argument_list|,
name|directory
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|browseResult
operator|=
name|browseService
operator|.
name|browseGroupId
argument_list|(
literal|"org.apache.karaf.features"
argument_list|,
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|browseResult
argument_list|)
expr_stmt|;
name|Assertions
operator|.
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
name|isEmpty
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"browseResult: {}"
argument_list|,
name|browseResult
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|cleanRepos
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|authorizedToDeleteArtifacts
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepository
name|managedRepository
init|=
name|getTestManagedRepository
argument_list|(
literal|"SOURCE_REPO_ID"
argument_list|,
literal|"SOURCE_REPO_ID"
argument_list|)
decl_stmt|;
try|try
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|repositoriesService
operator|.
name|isAuthorizedToDeleteArtifacts
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|cleanQuietlyRepo
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|notAuthorizedToDeleteArtifacts
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepository
name|managedRepository
init|=
name|getTestManagedRepository
argument_list|(
literal|"SOURCE_REPO_ID"
argument_list|,
literal|"SOURCE_REPO_ID"
argument_list|)
decl_stmt|;
try|try
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|(
name|guestAuthzHeader
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|repositoriesService
operator|.
name|isAuthorizedToDeleteArtifacts
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|cleanQuietlyRepo
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|cleanQuietlyRepo
parameter_list|(
name|String
name|id
parameter_list|)
block|{
try|try
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|id
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"ignore issue deleting test repo: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|deleteSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|targetRepo
init|=
name|initSnapshotRepo
argument_list|()
decl_stmt|;
try|try
block|{
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
comment|//repositoriesService.scanRepositoryDirectoriesNow( SNAPSHOT_REPO_ID );
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
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|browseService
operator|.
name|getArtifactDownloadInfos
argument_list|(
literal|"org.apache.archiva.redback.components"
argument_list|,
literal|"spring-quartz"
argument_list|,
literal|"2.0-SNAPSHOT"
argument_list|,
name|SNAPSHOT_REPO_ID
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts: {}"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifacts
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
literal|10
argument_list|)
expr_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|targetRepo
argument_list|,
literal|"org/apache/archiva/redback/components/spring-quartz/2.0-SNAPSHOT/spring-quartz-2.0-20120618.214127-1.jar"
argument_list|)
decl_stmt|;
name|File
name|artifactFilemd5
init|=
operator|new
name|File
argument_list|(
name|targetRepo
argument_list|,
literal|"org/apache/archiva/redback/components/spring-quartz/2.0-SNAPSHOT/spring-quartz-2.0-20120618.214127-1.jar.md5"
argument_list|)
decl_stmt|;
name|File
name|artifactFilepom
init|=
operator|new
name|File
argument_list|(
name|targetRepo
argument_list|,
literal|"org/apache/archiva/redback/components/spring-quartz/2.0-SNAPSHOT/spring-quartz-2.0-20120618.214127-1.pom"
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifactFile
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifactFilemd5
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifactFilepom
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
comment|// we delete only one snapshot
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|(
literal|"org.apache.archiva.redback.components"
argument_list|,
literal|"spring-quartz"
argument_list|,
literal|"2.0-20120618.214127-1"
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setPackaging
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setRepositoryId
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setContext
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|)
expr_stmt|;
name|repositoriesService
operator|.
name|deleteArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|browseService
operator|.
name|getArtifactDownloadInfos
argument_list|(
literal|"org.apache.archiva.redback.components"
argument_list|,
literal|"spring-quartz"
argument_list|,
literal|"2.0-SNAPSHOT"
argument_list|,
name|SNAPSHOT_REPO_ID
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts: {}"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifacts
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
literal|8
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifactFile
argument_list|)
operator|.
name|doesNotExist
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifactFilemd5
argument_list|)
operator|.
name|doesNotExist
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifactFilepom
argument_list|)
operator|.
name|doesNotExist
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|cleanSnapshotRepo
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|File
name|initSnapshotRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|targetRepo
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target/repo-with-snapshots"
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|targetRepo
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|targetRepo
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectoryToDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-snapshots"
argument_list|)
argument_list|,
name|targetRepo
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ManagedRepository
name|managedRepository
init|=
name|getTestManagedRepository
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|,
literal|"repo-with-snapshots"
argument_list|)
decl_stmt|;
comment|/*managedRepository.setId( SNAPSHOT_REPO_ID );         managedRepository.setLocation( );         managedRepository.setCronExpression( "* * * * * ?" );*/
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|targetRepo
return|;
block|}
specifier|protected
name|void
name|cleanSnapshotRepo
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SNAPSHOT_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"skip issue while cleaning test repository: this can cause test failure"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|ManagedRepository
name|getTestManagedRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|String
name|location
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target/"
operator|+
name|path
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
return|return
operator|new
name|ManagedRepository
argument_list|(
name|id
argument_list|,
name|id
argument_list|,
name|location
argument_list|,
literal|"default"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|"2 * * * * ?"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|80
argument_list|,
literal|80
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|ManagedRepository
name|getTestManagedRepository
parameter_list|()
block|{
return|return
name|getTestManagedRepository
argument_list|(
literal|"TEST"
argument_list|,
literal|"test-repo"
argument_list|)
return|;
block|}
specifier|static
specifier|final
name|String
name|SNAPSHOT_REPO_ID
init|=
literal|"snapshot-repo"
decl_stmt|;
block|}
end_class

end_unit

