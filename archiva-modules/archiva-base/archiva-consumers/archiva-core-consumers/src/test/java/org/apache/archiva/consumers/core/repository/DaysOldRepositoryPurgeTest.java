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
name|ArtifactMetadata
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
name|metadata
operator|.
name|audit
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
name|archiva
operator|.
name|repository
operator|.
name|features
operator|.
name|ArtifactCleanupFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|mockito
operator|.
name|ArgumentCaptor
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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
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

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|FileTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DaysOldRepositoryPurgeTest
extends|extends
name|AbstractRepositoryPurgeTest
block|{
specifier|private
specifier|static
specifier|final
name|int
name|OLD_TIMESTAMP
init|=
literal|1179382029
decl_stmt|;
specifier|private
name|void
name|setLastModified
parameter_list|(
name|String
name|dirPath
parameter_list|,
name|long
name|lastModified
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|dir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|dirPath
argument_list|)
decl_stmt|;
name|Path
index|[]
name|contents
init|=
name|Files
operator|.
name|list
argument_list|(
name|dir
argument_list|)
operator|.
name|toArray
argument_list|(
name|Path
index|[]
operator|::
operator|new
argument_list|)
decl_stmt|;
for|for
control|(
name|Path
name|content
range|:
name|contents
control|)
block|{
name|Files
operator|.
name|setLastModifiedTime
argument_list|(
name|content
argument_list|,
name|FileTime
operator|.
name|fromMillis
argument_list|(
name|lastModified
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|repoPurge
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testByLastModified
parameter_list|()
throws|throws
name|Exception
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|repoConfiguration
init|=
name|getRepoConfiguration
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|)
decl_stmt|;
name|ArtifactCleanupFeature
name|atf
init|=
name|repoConfiguration
operator|.
name|getFeature
argument_list|(
name|ArtifactCleanupFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|sessionFactory
operator|.
name|createSession
argument_list|( )
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|repositorySession
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|save
argument_list|()
expr_stmt|;
name|repoPurge
operator|=
operator|new
name|DaysOldRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|atf
operator|.
name|getRetentionPeriod
argument_list|()
operator|.
name|getDays
argument_list|()
argument_list|,
name|atf
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repositorySession
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepos
argument_list|()
decl_stmt|;
name|String
name|projectNs
init|=
literal|"org.apache.maven.plugins"
decl_stmt|;
name|String
name|projectPath
init|=
name|projectNs
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|projectName
init|=
literal|"maven-install-plugin"
decl_stmt|;
name|String
name|projectVersion
init|=
literal|"2.2-SNAPSHOT"
decl_stmt|;
name|String
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/"
operator|+
name|projectPath
operator|+
literal|"/"
operator|+
name|projectName
decl_stmt|;
name|Path
name|repo
init|=
name|getTestRepoRootPath
argument_list|()
decl_stmt|;
name|Path
name|vDir
init|=
name|repo
operator|.
name|resolve
argument_list|(
name|projectPath
argument_list|)
operator|.
name|resolve
argument_list|(
name|projectName
argument_list|)
operator|.
name|resolve
argument_list|(
name|projectVersion
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|deletedVersions
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|deletedVersions
operator|.
name|add
argument_list|(
literal|"2.2-SNAPSHOT"
argument_list|)
expr_stmt|;
name|deletedVersions
operator|.
name|add
argument_list|(
literal|"2.2-20061118.060401-2"
argument_list|)
expr_stmt|;
name|setLastModified
argument_list|(
name|projectRoot
operator|+
literal|"/"
operator|+
name|projectVersion
operator|+
literal|"/"
argument_list|,
name|OLD_TIMESTAMP
argument_list|)
expr_stmt|;
comment|// test listeners for the correct artifacts
name|String
index|[]
name|exts
init|=
block|{
literal|".md5"
block|,
literal|".sha1"
block|,
literal|""
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|exts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-install-plugin"
argument_list|,
literal|"2.2-SNAPSHOT"
argument_list|,
literal|"maven-install-plugin-2.2-SNAPSHOT.jar"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-install-plugin"
argument_list|,
literal|"2.2-SNAPSHOT"
argument_list|,
literal|"maven-install-plugin-2.2-SNAPSHOT.pom"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-install-plugin"
argument_list|,
literal|"2.2-SNAPSHOT"
argument_list|,
literal|"maven-install-plugin-2.2-20061118.060401-2.jar"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-install-plugin"
argument_list|,
literal|"2.2-SNAPSHOT"
argument_list|,
literal|"maven-install-plugin-2.2-20061118.060401-2.pom"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
comment|// Provide the metadata list
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|ml
init|=
name|getArtifactMetadataFromDir
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|projectName
argument_list|,
name|repo
operator|.
name|getParent
argument_list|()
argument_list|,
name|vDir
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repositorySession
argument_list|,
name|TEST_REPO_ID
argument_list|,
name|projectNs
argument_list|,
name|projectName
argument_list|,
name|projectVersion
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ml
argument_list|)
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|PATH_TO_BY_DAYS_OLD_ARTIFACT
argument_list|)
expr_stmt|;
comment|// Verify the metadataRepository invocations
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|removeProjectVersion
argument_list|(
name|eq
argument_list|(
name|repositorySession
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectNs
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectName
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectVersion
argument_list|)
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|ArtifactMetadata
argument_list|>
name|metadataArg
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|ArtifactMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|times
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|removeTimestampedArtifact
argument_list|(
name|eq
argument_list|(
name|repositorySession
argument_list|)
argument_list|,
name|metadataArg
operator|.
name|capture
argument_list|()
argument_list|,
name|eq
argument_list|(
name|projectVersion
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|metaL
init|=
name|metadataArg
operator|.
name|getAllValues
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|meta
range|:
name|metaL
control|)
block|{
name|assertTrue
argument_list|(
name|meta
operator|.
name|getId
argument_list|()
operator|.
name|startsWith
argument_list|(
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|deletedVersions
operator|.
name|contains
argument_list|(
name|meta
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.pom.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.pom.sha1"
argument_list|)
expr_stmt|;
comment|// shouldn't be deleted because even if older than 30 days (because retention count = 2)
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070513.034619-5.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070513.034619-5.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070513.034619-5.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070513.034619-5.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070513.034619-5.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070513.034619-5.pom.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070510.010101-4.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070510.010101-4.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070510.010101-4.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070510.010101-4.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070510.010101-4.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20070510.010101-4.pom.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.pom.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.pom.sha1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderOfDeletion
parameter_list|()
throws|throws
name|Exception
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|repoConfiguration
init|=
name|getRepoConfiguration
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|)
decl_stmt|;
name|ArtifactCleanupFeature
name|atf
init|=
name|repoConfiguration
operator|.
name|getFeature
argument_list|(
name|ArtifactCleanupFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|listener
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|sessionFactory
operator|.
name|createSession
argument_list|( )
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|repositorySession
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|save
argument_list|()
expr_stmt|;
name|repoPurge
operator|=
operator|new
name|DaysOldRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|atf
operator|.
name|getRetentionPeriod
argument_list|()
operator|.
name|getDays
argument_list|()
argument_list|,
name|atf
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repositorySession
argument_list|,
name|listeners
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepos
argument_list|()
decl_stmt|;
name|String
name|projectNs
init|=
literal|"org.apache.maven.plugins"
decl_stmt|;
name|String
name|projectPath
init|=
name|projectNs
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|projectName
init|=
literal|"maven-assembly-plugin"
decl_stmt|;
name|String
name|projectVersion
init|=
literal|"1.1.2-SNAPSHOT"
decl_stmt|;
name|String
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/"
operator|+
name|projectPath
operator|+
literal|"/"
operator|+
name|projectName
decl_stmt|;
name|Path
name|repo
init|=
name|getTestRepoRootPath
argument_list|()
decl_stmt|;
name|Path
name|vDir
init|=
name|repo
operator|.
name|resolve
argument_list|(
name|projectPath
argument_list|)
operator|.
name|resolve
argument_list|(
name|projectName
argument_list|)
operator|.
name|resolve
argument_list|(
name|projectVersion
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|deletedVersions
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|deletedVersions
operator|.
name|add
argument_list|(
literal|"1.1.2-20070427.065136-1"
argument_list|)
expr_stmt|;
name|setLastModified
argument_list|(
name|projectRoot
operator|+
literal|"/"
operator|+
name|projectVersion
operator|+
literal|"/"
argument_list|,
name|OLD_TIMESTAMP
argument_list|)
expr_stmt|;
comment|// test listeners for the correct artifacts
name|String
index|[]
name|exts
init|=
block|{
literal|".md5"
block|,
literal|".sha1"
block|,
literal|""
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|exts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-assembly-plugin"
argument_list|,
literal|"1.1.2-SNAPSHOT"
argument_list|,
literal|"maven-assembly-plugin-1.1.2-20070427.065136-1.jar"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-assembly-plugin"
argument_list|,
literal|"1.1.2-SNAPSHOT"
argument_list|,
literal|"maven-assembly-plugin-1.1.2-20070427.065136-1.pom"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
comment|// Provide the metadata list
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|ml
init|=
name|getArtifactMetadataFromDir
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|projectName
argument_list|,
name|repo
operator|.
name|getParent
argument_list|()
argument_list|,
name|vDir
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repositorySession
argument_list|,
name|TEST_REPO_ID
argument_list|,
name|projectNs
argument_list|,
name|projectName
argument_list|,
name|projectVersion
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ml
argument_list|)
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|PATH_TO_TEST_ORDER_OF_DELETION
argument_list|)
expr_stmt|;
comment|// Verify the metadataRepository invocations
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|removeProjectVersion
argument_list|(
name|eq
argument_list|(
name|repositorySession
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectNs
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectName
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectVersion
argument_list|)
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|ArtifactMetadata
argument_list|>
name|metadataArg
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|ArtifactMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|times
argument_list|(
name|deletedVersions
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
operator|.
name|removeTimestampedArtifact
argument_list|(
name|eq
argument_list|(
name|repositorySession
argument_list|)
argument_list|,
name|metadataArg
operator|.
name|capture
argument_list|()
argument_list|,
name|eq
argument_list|(
name|projectVersion
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|metaL
init|=
name|metadataArg
operator|.
name|getAllValues
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|meta
range|:
name|metaL
control|)
block|{
name|assertTrue
argument_list|(
name|meta
operator|.
name|getId
argument_list|()
operator|.
name|startsWith
argument_list|(
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|deletedVersions
operator|.
name|contains
argument_list|(
name|meta
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070427.065136-1.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070427.065136-1.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070427.065136-1.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070427.065136-1.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070427.065136-1.pom.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070427.065136-1.pom.md5"
argument_list|)
expr_stmt|;
comment|// the following should not have been deleted
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070506.163513-2.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070506.163513-2.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070506.163513-2.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070506.163513-2.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070506.163513-2.pom.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070506.163513-2.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070615.105019-3.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070615.105019-3.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070615.105019-3.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070615.105019-3.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070615.105019-3.pom.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070615.105019-3.pom.md5"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMetadataDrivenSnapshots
parameter_list|()
throws|throws
name|Exception
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|repoConfiguration
init|=
name|getRepoConfiguration
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|)
decl_stmt|;
name|ArtifactCleanupFeature
name|atf
init|=
name|repoConfiguration
operator|.
name|getFeature
argument_list|(
name|ArtifactCleanupFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|listener
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|sessionFactory
operator|.
name|createSession
argument_list|( )
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|repositorySession
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|save
argument_list|()
expr_stmt|;
name|repoPurge
operator|=
operator|new
name|DaysOldRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|atf
operator|.
name|getRetentionPeriod
argument_list|()
operator|.
name|getDays
argument_list|()
argument_list|,
name|atf
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repositorySession
argument_list|,
name|listeners
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepos
argument_list|()
decl_stmt|;
name|String
name|projectNs
init|=
literal|"org.codehaus.plexus"
decl_stmt|;
name|String
name|projectPath
init|=
name|projectNs
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|projectName
init|=
literal|"plexus-utils"
decl_stmt|;
name|String
name|projectVersion
init|=
literal|"1.4.3-SNAPSHOT"
decl_stmt|;
name|String
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/"
operator|+
name|projectPath
operator|+
literal|"/"
operator|+
name|projectName
decl_stmt|;
name|Path
name|repo
init|=
name|getTestRepoRootPath
argument_list|()
decl_stmt|;
name|Path
name|vDir
init|=
name|repo
operator|.
name|resolve
argument_list|(
name|projectPath
argument_list|)
operator|.
name|resolve
argument_list|(
name|projectName
argument_list|)
operator|.
name|resolve
argument_list|(
name|projectVersion
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|deletedVersions
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|deletedVersions
operator|.
name|add
argument_list|(
literal|"1.4.3-20070113.163208-4"
argument_list|)
expr_stmt|;
name|String
name|versionRoot
init|=
name|projectRoot
operator|+
literal|"/"
operator|+
name|projectVersion
decl_stmt|;
name|Calendar
name|currentDate
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
decl_stmt|;
name|setLastModified
argument_list|(
name|versionRoot
argument_list|,
name|currentDate
operator|.
name|getTimeInMillis
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|timestamp
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMdd.HHmmss"
argument_list|)
operator|.
name|format
argument_list|(
name|currentDate
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|5
init|;
name|i
operator|<=
literal|7
condition|;
name|i
operator|++
control|)
block|{
name|Path
name|jarFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|versionRoot
argument_list|,
literal|"/plexus-utils-1.4.3-"
operator|+
name|timestamp
operator|+
literal|"-"
operator|+
name|i
operator|+
literal|".jar"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createFile
argument_list|(
name|jarFile
argument_list|)
expr_stmt|;
name|Path
name|pomFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|versionRoot
argument_list|,
literal|"/plexus-utils-1.4.3-"
operator|+
name|timestamp
operator|+
literal|"-"
operator|+
name|i
operator|+
literal|".pom"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createFile
argument_list|(
name|pomFile
argument_list|)
expr_stmt|;
comment|// set timestamp to older than 100 days for the first build, but ensure the filename timestamp is honoured instead
if|if
condition|(
name|i
operator|==
literal|5
condition|)
block|{
name|Files
operator|.
name|setLastModifiedTime
argument_list|(
name|jarFile
argument_list|,
name|FileTime
operator|.
name|fromMillis
argument_list|(
name|OLD_TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|setLastModifiedTime
argument_list|(
name|pomFile
argument_list|,
name|FileTime
operator|.
name|fromMillis
argument_list|(
name|OLD_TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// test listeners for the correct artifacts
name|String
index|[]
name|exts
init|=
block|{
literal|".sha1"
block|,
literal|""
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|exts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-utils"
argument_list|,
literal|"1.4.3-SNAPSHOT"
argument_list|,
literal|"plexus-utils-1.4.3-20070113.163208-4.jar"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-utils"
argument_list|,
literal|"1.4.3-SNAPSHOT"
argument_list|,
literal|"plexus-utils-1.4.3-20070113.163208-4.pom"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
comment|// Provide the metadata list
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|ml
init|=
name|getArtifactMetadataFromDir
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|projectName
argument_list|,
name|repo
operator|.
name|getParent
argument_list|()
argument_list|,
name|vDir
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repositorySession
argument_list|,
name|TEST_REPO_ID
argument_list|,
name|projectNs
argument_list|,
name|projectName
argument_list|,
name|projectVersion
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ml
argument_list|)
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|PATH_TO_BY_DAYS_OLD_METADATA_DRIVEN_ARTIFACT
argument_list|)
expr_stmt|;
comment|// Verify the metadataRepository invocations
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|removeProjectVersion
argument_list|(
name|eq
argument_list|(
name|repositorySession
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectNs
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectName
argument_list|)
argument_list|,
name|eq
argument_list|(
name|projectVersion
argument_list|)
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|ArtifactMetadata
argument_list|>
name|metadataArg
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|ArtifactMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|times
argument_list|(
name|deletedVersions
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
operator|.
name|removeTimestampedArtifact
argument_list|(
name|eq
argument_list|(
name|repositorySession
argument_list|)
argument_list|,
name|metadataArg
operator|.
name|capture
argument_list|()
argument_list|,
name|eq
argument_list|(
name|projectVersion
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|metaL
init|=
name|metadataArg
operator|.
name|getAllValues
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|meta
range|:
name|metaL
control|)
block|{
name|assertTrue
argument_list|(
name|meta
operator|.
name|getId
argument_list|()
operator|.
name|startsWith
argument_list|(
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|deletedVersions
operator|.
name|contains
argument_list|(
name|meta
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// this should be deleted since the filename version (timestamp) is older than
comment|// 100 days even if the last modified date was<100 days ago
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/plexus-utils-1.4.3-20070113.163208-4.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/plexus-utils-1.4.3-20070113.163208-4.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/plexus-utils-1.4.3-20070113.163208-4.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/plexus-utils-1.4.3-20070113.163208-4.pom.sha1"
argument_list|)
expr_stmt|;
comment|// this should not be deleted because last modified date is<100 days ago
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/plexus-utils-1.4.3-SNAPSHOT.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/plexus-utils-1.4.3-SNAPSHOT.pom"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|5
init|;
name|i
operator|<=
literal|7
condition|;
name|i
operator|++
control|)
block|{
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/plexus-utils-1.4.3-"
operator|+
name|timestamp
operator|+
literal|"-"
operator|+
name|i
operator|+
literal|".jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/plexus-utils-1.4.3-"
operator|+
name|timestamp
operator|+
literal|"-"
operator|+
name|i
operator|+
literal|".pom"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

