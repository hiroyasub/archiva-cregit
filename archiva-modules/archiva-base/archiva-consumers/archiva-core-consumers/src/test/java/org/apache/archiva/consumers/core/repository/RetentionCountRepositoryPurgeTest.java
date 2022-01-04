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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|Before
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
name|org
operator|.
name|mockito
operator|.
name|Mockito
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
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
comment|/**  * Test RetentionsCountRepositoryPurgeTest  */
end_comment

begin_class
specifier|public
class|class
name|RetentionCountRepositoryPurgeTest
extends|extends
name|AbstractRepositoryPurgeTest
block|{
annotation|@
name|Before
annotation|@
name|Override
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
name|ArtifactCleanupFeature
name|acf
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
name|Mockito
operator|.
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
name|Mockito
operator|.
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
name|RetentionCountRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|acf
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repositorySession
argument_list|,
name|listeners
argument_list|)
expr_stmt|;
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
block|}
comment|/**      * Test if the artifact to be processed was a jar.      */
annotation|@
name|Test
specifier|public
name|void
name|testIfAJarWasFound
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|repoRoot
init|=
name|prepareTestRepos
argument_list|()
decl_stmt|;
name|String
name|projectNs
init|=
literal|"org.jruby.plugins"
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
literal|"jruby-rake-plugin"
decl_stmt|;
name|String
name|projectVersion
init|=
literal|"1.0RC1-SNAPSHOT"
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
literal|"1.0RC1-20070504.153317-1"
argument_list|)
expr_stmt|;
name|deletedVersions
operator|.
name|add
argument_list|(
literal|"1.0RC1-20070504.160758-2"
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
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
literal|"1.0RC1-SNAPSHOT"
argument_list|,
literal|"jruby-rake-plugin-1.0RC1-20070504.153317-1.jar"
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
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
literal|"1.0RC1-SNAPSHOT"
argument_list|,
literal|"jruby-rake-plugin-1.0RC1-20070504.153317-1.pom"
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
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
literal|"1.0RC1-SNAPSHOT"
argument_list|,
literal|"jruby-rake-plugin-1.0RC1-20070504.160758-2.jar"
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
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
literal|"1.0RC1-SNAPSHOT"
argument_list|,
literal|"jruby-rake-plugin-1.0RC1-20070504.160758-2.pom"
operator|+
name|exts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
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
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
literal|"1.0RC1-SNAPSHOT"
argument_list|,
literal|"jruby-rake-plugin-1.0RC1-20070504.160758-2-javadoc.jar"
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
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
literal|"1.0RC1-SNAPSHOT"
argument_list|,
literal|"jruby-rake-plugin-1.0RC1-20070504.160758-2-javadoc.zip"
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
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
literal|"1.0RC1-SNAPSHOT"
argument_list|,
literal|"jruby-rake-plugin-1.0RC1-20070504.153317-1-javadoc.jar"
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
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
literal|"1.0RC1-SNAPSHOT"
argument_list|,
literal|"jruby-rake-plugin-1.0RC1-20070504.153317-1-javadoc.zip"
argument_list|)
expr_stmt|;
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
name|PATH_TO_BY_RETENTION_COUNT_ARTIFACT
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
comment|// assert if removed from repo
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.153317-1.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.153317-1.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.153317-1.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.153317-1.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.153317-1.pom.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.153317-1.pom.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.160758-2.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.160758-2.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.160758-2.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.160758-2.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.160758-2.pom.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070504.160758-2.pom.sha1"
argument_list|)
expr_stmt|;
comment|// assert if not removed from repo
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070505.090015-3.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070505.090015-3.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070505.090015-3.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070505.090015-3.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070505.090015-3.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070505.090015-3.pom.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070506.090132-4.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070506.090132-4.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070506.090132-4.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070506.090132-4.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070506.090132-4.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/jruby-rake-plugin-1.0RC1-20070506.090132-4.pom.sha1"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test if the artifact to be processed is a pom      */
annotation|@
name|Test
specifier|public
name|void
name|testIfAPomWasFound
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|repoRoot
init|=
name|prepareTestRepos
argument_list|()
decl_stmt|;
name|String
name|projectNs
init|=
literal|"org.codehaus.castor"
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
literal|"castor-anttasks"
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
name|String
name|versionRoot
init|=
name|projectRoot
operator|+
literal|"/"
operator|+
name|projectVersion
decl_stmt|;
comment|// test listeners for the correct artifacts
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
literal|"org.codehaus.castor"
argument_list|,
literal|"castor-anttasks"
argument_list|,
literal|"1.1.2-SNAPSHOT"
argument_list|,
literal|"castor-anttasks-1.1.2-20070427.065136-1.jar.md5"
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
literal|"org.codehaus.castor"
argument_list|,
literal|"castor-anttasks"
argument_list|,
literal|"1.1.2-SNAPSHOT"
argument_list|,
literal|"castor-anttasks-1.1.2-20070427.065136-1.jar.sha1"
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
literal|"org.codehaus.castor"
argument_list|,
literal|"castor-anttasks"
argument_list|,
literal|"1.1.2-SNAPSHOT"
argument_list|,
literal|"castor-anttasks-1.1.2-20070427.065136-1.jar"
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
literal|"org.codehaus.castor"
argument_list|,
literal|"castor-anttasks"
argument_list|,
literal|"1.1.2-SNAPSHOT"
argument_list|,
literal|"castor-anttasks-1.1.2-20070427.065136-1.pom.md5"
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
literal|"org.codehaus.castor"
argument_list|,
literal|"castor-anttasks"
argument_list|,
literal|"1.1.2-SNAPSHOT"
argument_list|,
literal|"castor-anttasks-1.1.2-20070427.065136-1.pom.sha1"
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
literal|"org.codehaus.castor"
argument_list|,
literal|"castor-anttasks"
argument_list|,
literal|"1.1.2-SNAPSHOT"
argument_list|,
literal|"castor-anttasks-1.1.2-20070427.065136-1.pom"
argument_list|)
expr_stmt|;
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
name|PATH_TO_BY_RETENTION_COUNT_POM
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
comment|// assert if removed from repo
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070427.065136-1.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070427.065136-1.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070427.065136-1.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070427.065136-1.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070427.065136-1.pom.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070427.065136-1.pom.sha1"
argument_list|)
expr_stmt|;
comment|// assert if not removed from repo
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3.pom.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3-sources.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3-sources.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070615.105019-3-sources.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2.pom.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2-sources.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2-sources.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/castor-anttasks-1.1.2-20070506.163513-2-sources.jar.sha1"
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
name|String
name|versionRoot
init|=
name|projectRoot
operator|+
literal|"/"
operator|+
name|projectVersion
decl_stmt|;
comment|// test listeners for the correct artifacts
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
literal|"maven-assembly-plugin-1.1.2-20070427.065136-1.jar.md5"
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
literal|"maven-assembly-plugin-1.1.2-20070427.065136-1.jar.sha1"
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
literal|"maven-assembly-plugin-1.1.2-20070427.065136-1.jar"
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
literal|"maven-assembly-plugin-1.1.2-20070427.065136-1.pom.md5"
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
literal|"maven-assembly-plugin-1.1.2-20070427.065136-1.pom.sha1"
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
argument_list|)
expr_stmt|;
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
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070427.065136-1.jar"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070427.065136-1.jar.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070427.065136-1.jar.md5"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070427.065136-1.pom"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070427.065136-1.pom.sha1"
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070427.065136-1.pom.md5"
argument_list|)
expr_stmt|;
comment|// the following should not have been deleted
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070506.163513-2.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070506.163513-2.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070506.163513-2.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070506.163513-2.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070506.163513-2.pom.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070506.163513-2.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070615.105019-3.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070615.105019-3.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070615.105019-3.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070615.105019-3.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070615.105019-3.pom.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|versionRoot
operator|+
literal|"/maven-assembly-plugin-1.1.2-20070615.105019-3.pom.md5"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

