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
name|consumers
operator|.
name|core
operator|.
name|repository
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
name|util
operator|.
name|Collections
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ArchivaConfiguration
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
name|Configuration
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
name|repository
operator|.
name|RepositoryContentFactory
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
name|repository
operator|.
name|events
operator|.
name|RepositoryListener
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
name|repository
operator|.
name|metadata
operator|.
name|MetadataTools
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
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|CleanupReleasedSnapshotsRepositoryPurgeTest
extends|extends
name|AbstractRepositoryPurgeTest
block|{
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_RELEASED_SNAPSHOT_IN_DIFF_REPO
init|=
literal|"org/apache/archiva/released-artifact-in-diff-repo/1.0-SNAPSHOT/released-artifact-in-diff-repo-1.0-SNAPSHOT.jar"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_HIGHER_SNAPSHOT_EXISTS_IN_SAME_REPO
init|=
literal|"org/apache/maven/plugins/maven-source-plugin/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.jar"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_RELEASED_SNAPSHOT_IN_SAME_REPO
init|=
literal|"org/apache/maven/plugins/maven-plugin-plugin/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar"
decl_stmt|;
annotation|@
name|Override
specifier|protected
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
name|MetadataTools
name|metadataTools
init|=
operator|(
name|MetadataTools
operator|)
name|lookup
argument_list|(
name|MetadataTools
operator|.
name|class
argument_list|)
decl_stmt|;
name|RepositoryContentFactory
name|factory
init|=
operator|(
name|RepositoryContentFactory
operator|)
name|lookup
argument_list|(
name|RepositoryContentFactory
operator|.
name|class
argument_list|,
literal|"cleanup-released-snapshots"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"cleanup-released-snapshots"
argument_list|)
expr_stmt|;
name|listenerControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|RepositoryListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
operator|=
operator|(
name|RepositoryListener
operator|)
name|listenerControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|repoPurge
operator|=
operator|new
name|CleanupReleasedSnapshotsRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|metadataTools
argument_list|,
name|archivaConfiguration
argument_list|,
name|factory
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReleasedSnapshotsExistsInSameRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|removeManagedRepository
argument_list|(
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|getRepoConfiguration
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepos
argument_list|()
decl_stmt|;
comment|// test listeners for the correct artifacts
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|createArtifact
argument_list|(
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-plugin-plugin"
argument_list|,
literal|"2.3-SNAPSHOT"
argument_list|,
literal|"maven-plugin"
argument_list|)
argument_list|)
expr_stmt|;
name|listenerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|CleanupReleasedSnapshotsRepositoryPurgeTest
operator|.
name|PATH_TO_RELEASED_SNAPSHOT_IN_SAME_REPO
argument_list|)
expr_stmt|;
name|listenerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|String
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/org/apache/maven/plugins/maven-plugin-plugin"
decl_stmt|;
comment|// check if the snapshot was removed
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom.sha1"
argument_list|)
expr_stmt|;
comment|// check if the released version was not removed
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3-sources.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3-sources.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3-sources.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3/maven-plugin-plugin-2.3.pom.sha1"
argument_list|)
expr_stmt|;
comment|// check if metadata file was updated
name|File
name|artifactMetadataFile
init|=
operator|new
name|File
argument_list|(
name|projectRoot
operator|+
literal|"/maven-metadata.xml"
argument_list|)
decl_stmt|;
name|String
name|metadataXml
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|artifactMetadataFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|expectedVersions
init|=
literal|"<expected><versions><version>2.2</version>"
operator|+
literal|"<version>2.3</version></versions></expected>"
decl_stmt|;
name|XMLAssert
operator|.
name|assertXpathEvaluatesTo
argument_list|(
literal|"2.3"
argument_list|,
literal|"//metadata/versioning/release"
argument_list|,
name|metadataXml
argument_list|)
expr_stmt|;
name|XMLAssert
operator|.
name|assertXpathEvaluatesTo
argument_list|(
literal|"2.3"
argument_list|,
literal|"//metadata/versioning/latest"
argument_list|,
name|metadataXml
argument_list|)
expr_stmt|;
name|XMLAssert
operator|.
name|assertXpathsEqual
argument_list|(
literal|"//expected/versions/version"
argument_list|,
name|expectedVersions
argument_list|,
literal|"//metadata/versioning/versions/version"
argument_list|,
name|metadataXml
argument_list|)
expr_stmt|;
name|XMLAssert
operator|.
name|assertXpathEvaluatesTo
argument_list|(
literal|"20070315032817"
argument_list|,
literal|"//metadata/versioning/lastUpdated"
argument_list|,
name|metadataXml
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReleasedSnapshotsExistsInDifferentRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|removeManagedRepository
argument_list|(
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|getRepoConfiguration
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|getRepoConfiguration
argument_list|(
name|RELEASES_TEST_REPO_ID
argument_list|,
name|RELEASES_TEST_REPO_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepos
argument_list|()
decl_stmt|;
comment|// test listeners for the correct artifacts
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"released-artifact-in-diff-repo"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
name|listenerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|PATH_TO_RELEASED_SNAPSHOT_IN_DIFF_REPO
argument_list|)
expr_stmt|;
name|listenerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|String
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/org/apache/archiva/released-artifact-in-diff-repo"
decl_stmt|;
comment|// check if the snapshot was removed
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.0-SNAPSHOT"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.0-SNAPSHOT/released-artifact-in-diff-repo-1.0-SNAPSHOT.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.0-SNAPSHOT/released-artifact-in-diff-repo-1.0-SNAPSHOT.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.0-SNAPSHOT/released-artifact-in-diff-repo-1.0-SNAPSHOT.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.0-SNAPSHOT/released-artifact-in-diff-repo-1.0-SNAPSHOT.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.0-SNAPSHOT/released-artifact-in-diff-repo-1.0-SNAPSHOT.pom.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.0-SNAPSHOT/released-artifact-in-diff-repo-1.0-SNAPSHOT.pom.sha1"
argument_list|)
expr_stmt|;
name|String
name|releasesProjectRoot
init|=
name|getTestFile
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/releases-test-repo-one"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/org/apache/archiva/released-artifact-in-diff-repo"
decl_stmt|;
comment|// check if the released version was not removed
name|assertExists
argument_list|(
name|releasesProjectRoot
operator|+
literal|"/1.0"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|releasesProjectRoot
operator|+
literal|"/1.0/released-artifact-in-diff-repo-1.0.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|releasesProjectRoot
operator|+
literal|"/1.0/released-artifact-in-diff-repo-1.0.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|releasesProjectRoot
operator|+
literal|"/1.0/released-artifact-in-diff-repo-1.0.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|releasesProjectRoot
operator|+
literal|"/1.0/released-artifact-in-diff-repo-1.0.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|releasesProjectRoot
operator|+
literal|"/1.0/released-artifact-in-diff-repo-1.0.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|releasesProjectRoot
operator|+
literal|"/1.0/released-artifact-in-diff-repo-1.0.pom.sha1"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testHigherSnapshotExistsInSameRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|removeManagedRepository
argument_list|(
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|getRepoConfiguration
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepos
argument_list|()
decl_stmt|;
comment|// test listeners for the correct artifacts - no deletions
name|listenerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|CleanupReleasedSnapshotsRepositoryPurgeTest
operator|.
name|PATH_TO_HIGHER_SNAPSHOT_EXISTS_IN_SAME_REPO
argument_list|)
expr_stmt|;
name|listenerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|String
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/org/apache/maven/plugins/maven-source-plugin"
decl_stmt|;
comment|// check if the snapshot was not removed
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.3-SNAPSHOT"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.pom.sha1"
argument_list|)
expr_stmt|;
comment|// check if the released version was not removed
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.4-SNAPSHOT"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.pom.sha1"
argument_list|)
expr_stmt|;
comment|// check if metadata file was not updated (because nothing was removed)
name|File
name|artifactMetadataFile
init|=
operator|new
name|File
argument_list|(
name|projectRoot
operator|+
literal|"/maven-metadata.xml"
argument_list|)
decl_stmt|;
name|String
name|metadataXml
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|artifactMetadataFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|expectedVersions
init|=
literal|"<expected><versions><version>2.0.3-SNAPSHOT</version>"
operator|+
literal|"<version>2.0.4-SNAPSHOT</version></versions></expected>"
decl_stmt|;
name|XMLAssert
operator|.
name|assertXpathEvaluatesTo
argument_list|(
literal|"2.0.4-SNAPSHOT"
argument_list|,
literal|"//metadata/versioning/latest"
argument_list|,
name|metadataXml
argument_list|)
expr_stmt|;
name|XMLAssert
operator|.
name|assertXpathsEqual
argument_list|(
literal|"//expected/versions/version"
argument_list|,
name|expectedVersions
argument_list|,
literal|"//metadata/versioning/versions/version"
argument_list|,
name|metadataXml
argument_list|)
expr_stmt|;
name|XMLAssert
operator|.
name|assertXpathEvaluatesTo
argument_list|(
literal|"20070427033345"
argument_list|,
literal|"//metadata/versioning/lastUpdated"
argument_list|,
name|metadataXml
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

