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
name|common
operator|.
name|utils
operator|.
name|BaseFile
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
name|configuration
operator|.
name|FileType
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
name|FileTypes
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
name|KnownRepositoryContentConsumer
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
name|core
operator|.
name|repository
operator|.
name|stubs
operator|.
name|LuceneRepositoryContentIndexFactoryStub
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
name|database
operator|.
name|ArchivaDatabaseException
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
name|scanner
operator|.
name|functors
operator|.
name|ConsumerWantsFilePredicate
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
name|util
operator|.
name|ArrayList
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
comment|/**  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryPurgeConsumerTest
extends|extends
name|AbstractRepositoryPurgeTest
block|{
specifier|public
name|void
name|testConsumption
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotConsumed
argument_list|(
literal|"org/apache/maven/plugins/maven-plugin-plugin/2.4.1/maven-metadata.xml"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConsumptionOfOtherMetadata
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotConsumed
argument_list|(
literal|"org/apache/maven/plugins/maven-plugin-plugin/2.4.1/maven-metadata-central.xml"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertNotConsumed
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|FileType
name|fileType
init|=
operator|(
name|FileType
operator|)
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getFileTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|,
name|fileType
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|fileType
operator|.
name|addPattern
argument_list|(
literal|"**/*.xml"
argument_list|)
expr_stmt|;
comment|// trigger reload
name|FileTypes
name|fileTypes
init|=
operator|(
name|FileTypes
operator|)
name|lookup
argument_list|(
name|FileTypes
operator|.
name|class
argument_list|)
decl_stmt|;
name|fileTypes
operator|.
name|afterConfigurationChange
argument_list|(
literal|null
argument_list|,
literal|"repositoryScanning.fileTypes"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|KnownRepositoryContentConsumer
name|repoPurgeConsumer
init|=
operator|(
name|KnownRepositoryContentConsumer
operator|)
name|lookup
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|,
literal|"repository-purge"
argument_list|)
decl_stmt|;
name|File
name|repoLocation
init|=
name|getTestFile
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/test-repo"
argument_list|)
decl_stmt|;
name|File
name|localFile
init|=
operator|new
name|File
argument_list|(
name|repoLocation
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|ConsumerWantsFilePredicate
name|predicate
init|=
operator|new
name|ConsumerWantsFilePredicate
argument_list|()
decl_stmt|;
name|BaseFile
name|baseFile
init|=
operator|new
name|BaseFile
argument_list|(
name|repoLocation
argument_list|,
name|localFile
argument_list|)
decl_stmt|;
name|predicate
operator|.
name|setBasefile
argument_list|(
name|baseFile
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|predicate
operator|.
name|evaluate
argument_list|(
name|repoPurgeConsumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setLastModified
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|File
index|[]
name|contents
init|=
name|dir
operator|.
name|listFiles
argument_list|()
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
name|contents
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|contents
index|[
name|i
index|]
operator|.
name|setLastModified
argument_list|(
literal|1179382029
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testConsumerByRetentionCount
parameter_list|()
throws|throws
name|Exception
block|{
name|KnownRepositoryContentConsumer
name|repoPurgeConsumer
init|=
operator|(
name|KnownRepositoryContentConsumer
operator|)
name|lookup
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|,
literal|"repo-purge-consumer-by-retention-count"
argument_list|)
decl_stmt|;
name|LuceneRepositoryContentIndexFactoryStub
name|indexFactory
init|=
operator|new
name|LuceneRepositoryContentIndexFactoryStub
argument_list|()
decl_stmt|;
name|indexFactory
operator|.
name|setExpectedRecordsSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
operator|(
operator|(
name|RepositoryPurgeConsumer
operator|)
name|repoPurgeConsumer
operator|)
operator|.
name|setRepositoryContentIndexFactory
argument_list|(
name|indexFactory
argument_list|)
expr_stmt|;
name|populateDbForRetentionCountTest
argument_list|()
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfiguration
init|=
name|getRepoConfiguration
argument_list|()
decl_stmt|;
name|repoConfiguration
operator|.
name|setDaysOlder
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// force days older off to allow retention count purge to execute.
name|repoConfiguration
operator|.
name|setRetentionCount
argument_list|(
name|TEST_RETENTION_COUNT
argument_list|)
expr_stmt|;
name|addRepoToConfiguration
argument_list|(
literal|"retention-count"
argument_list|,
name|repoConfiguration
argument_list|)
expr_stmt|;
name|repoPurgeConsumer
operator|.
name|beginScan
argument_list|(
name|repoConfiguration
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepo
argument_list|()
decl_stmt|;
name|repoPurgeConsumer
operator|.
name|processFile
argument_list|(
name|PATH_TO_BY_RETENTION_COUNT_ARTIFACT
argument_list|)
expr_stmt|;
name|String
name|versionRoot
init|=
name|repoRoot
operator|+
literal|"/org/jruby/plugins/jruby-rake-plugin/1.0RC1-SNAPSHOT"
decl_stmt|;
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
specifier|private
name|void
name|addRepoToConfiguration
parameter_list|(
name|String
name|configHint
parameter_list|,
name|ManagedRepositoryConfiguration
name|repoConfiguration
parameter_list|)
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
name|configHint
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|removeManagedRepository
argument_list|(
name|configuration
operator|.
name|findManagedRepositoryById
argument_list|(
name|repoConfiguration
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|repoConfiguration
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConsumerByDaysOld
parameter_list|()
throws|throws
name|Exception
block|{
name|populateDbForDaysOldTest
argument_list|()
expr_stmt|;
name|KnownRepositoryContentConsumer
name|repoPurgeConsumer
init|=
operator|(
name|KnownRepositoryContentConsumer
operator|)
name|lookup
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|,
literal|"repo-purge-consumer-by-days-old"
argument_list|)
decl_stmt|;
name|LuceneRepositoryContentIndexFactoryStub
name|indexFactory
init|=
operator|new
name|LuceneRepositoryContentIndexFactoryStub
argument_list|()
decl_stmt|;
name|indexFactory
operator|.
name|setExpectedRecordsSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
operator|(
operator|(
name|RepositoryPurgeConsumer
operator|)
name|repoPurgeConsumer
operator|)
operator|.
name|setRepositoryContentIndexFactory
argument_list|(
name|indexFactory
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfiguration
init|=
name|getRepoConfiguration
argument_list|()
decl_stmt|;
name|repoConfiguration
operator|.
name|setDaysOlder
argument_list|(
name|TEST_DAYS_OLDER
argument_list|)
expr_stmt|;
name|addRepoToConfiguration
argument_list|(
literal|"days-old"
argument_list|,
name|repoConfiguration
argument_list|)
expr_stmt|;
name|repoPurgeConsumer
operator|.
name|beginScan
argument_list|(
name|repoConfiguration
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepo
argument_list|()
decl_stmt|;
name|String
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/org/apache/maven/plugins/maven-install-plugin"
decl_stmt|;
name|setLastModified
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT"
argument_list|)
expr_stmt|;
name|repoPurgeConsumer
operator|.
name|processFile
argument_list|(
name|PATH_TO_BY_DAYS_OLD_ARTIFACT
argument_list|)
expr_stmt|;
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
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.pom.sha1"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the snapshot clean consumer on a repository set to NOT clean/delete snapshots based on released versions.      *      * @throws Exception      */
specifier|public
name|void
name|testReleasedSnapshotsWereNotCleaned
parameter_list|()
throws|throws
name|Exception
block|{
name|KnownRepositoryContentConsumer
name|repoPurgeConsumer
init|=
operator|(
name|KnownRepositoryContentConsumer
operator|)
name|lookup
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|,
literal|"repo-purge-consumer-by-retention-count"
argument_list|)
decl_stmt|;
name|populateDbForReleasedSnapshotsTest
argument_list|()
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfiguration
init|=
name|getRepoConfiguration
argument_list|()
decl_stmt|;
name|repoConfiguration
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Set to NOT delete released snapshots.
name|addRepoToConfiguration
argument_list|(
literal|"retention-count"
argument_list|,
name|repoConfiguration
argument_list|)
expr_stmt|;
name|repoPurgeConsumer
operator|.
name|beginScan
argument_list|(
name|repoConfiguration
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepo
argument_list|()
decl_stmt|;
name|repoPurgeConsumer
operator|.
name|processFile
argument_list|(
name|PATH_TO_RELEASED_SNAPSHOT
argument_list|)
expr_stmt|;
comment|// check if the snapshot wasn't removed
name|String
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/org/apache/maven/plugins/maven-plugin-plugin"
decl_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar.sha1"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom.md5"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
name|projectRoot
operator|+
literal|"/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom.sha1"
argument_list|)
expr_stmt|;
comment|// check if metadata file wasn't updated
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
literal|"<expected><versions><version>2.3-SNAPSHOT</version></versions></expected>"
decl_stmt|;
name|XMLAssert
operator|.
name|assertXpathEvaluatesTo
argument_list|(
literal|"2.3-SNAPSHOT"
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
name|testReleasedSnapshotsWereCleaned
parameter_list|()
throws|throws
name|Exception
block|{
name|KnownRepositoryContentConsumer
name|repoPurgeConsumer
init|=
operator|(
name|KnownRepositoryContentConsumer
operator|)
name|lookup
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|,
literal|"repo-purge-consumer-by-days-old"
argument_list|)
decl_stmt|;
name|populateDbForReleasedSnapshotsTest
argument_list|()
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfiguration
init|=
name|getRepoConfiguration
argument_list|()
decl_stmt|;
name|repoConfiguration
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addRepoToConfiguration
argument_list|(
literal|"days-old"
argument_list|,
name|repoConfiguration
argument_list|)
expr_stmt|;
name|repoPurgeConsumer
operator|.
name|beginScan
argument_list|(
name|repoConfiguration
argument_list|)
expr_stmt|;
name|String
name|repoRoot
init|=
name|prepareTestRepo
argument_list|()
decl_stmt|;
name|repoPurgeConsumer
operator|.
name|processFile
argument_list|(
name|PATH_TO_RELEASED_SNAPSHOT
argument_list|)
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
name|populateDbForRetentionCountTest
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.0RC1-20070504.153317-1"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.0RC1-20070504.160758-2"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.0RC1-20070505.090015-3"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.0RC1-20070506.090132-4"
argument_list|)
expr_stmt|;
name|populateDb
argument_list|(
literal|"org.jruby.plugins"
argument_list|,
literal|"jruby-rake-plugin"
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|populateDbForDaysOldTest
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"2.2-SNAPSHOT"
argument_list|)
expr_stmt|;
name|populateDb
argument_list|(
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-install-plugin"
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|populateDbForReleasedSnapshotsTest
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"2.3-SNAPSHOT"
argument_list|)
expr_stmt|;
name|populateDb
argument_list|(
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-plugin-plugin"
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

