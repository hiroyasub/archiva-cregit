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
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|managed
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
name|commons
operator|.
name|lang
operator|.
name|time
operator|.
name|DateUtils
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
name|Calendar
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
name|List
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
block|{
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|dirPath
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
name|File
name|content
range|:
name|contents
control|)
block|{
name|content
operator|.
name|setLastModified
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
block|}
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
name|repoPurge
operator|=
operator|new
name|DaysOldRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|repoConfiguration
operator|.
name|getDaysOlder
argument_list|()
argument_list|,
name|repoConfiguration
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
literal|"/2.2-SNAPSHOT/"
argument_list|,
name|OLD_TIMESTAMP
argument_list|)
expr_stmt|;
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
literal|"maven-install-plugin"
argument_list|,
literal|"2.2-SNAPSHOT"
argument_list|,
literal|"maven-install-plugin-2.2-SNAPSHOT.jar"
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
literal|"2.2-20061118.060401-2"
argument_list|,
literal|"maven-install-plugin-2.2-20061118.060401-2.jar"
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
literal|"2.2-20061118.060401-2"
argument_list|,
literal|"maven-install-plugin-2.2-20061118.060401-2.pom"
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
name|PATH_TO_BY_DAYS_OLD_ARTIFACT
argument_list|)
expr_stmt|;
name|listenerControl
operator|.
name|verify
argument_list|()
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
name|repoPurge
operator|=
operator|new
name|DaysOldRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|repoConfiguration
operator|.
name|getDaysOlder
argument_list|()
argument_list|,
name|repoConfiguration
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
name|projectRoot
init|=
name|repoRoot
operator|+
literal|"/org/apache/maven/plugins/maven-assembly-plugin"
decl_stmt|;
name|setLastModified
argument_list|(
name|projectRoot
operator|+
literal|"/1.1.2-SNAPSHOT/"
argument_list|,
name|OLD_TIMESTAMP
argument_list|)
expr_stmt|;
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
literal|"1.1.2-20070427.065136-1"
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
literal|"1.1.2-20070427.065136-1"
argument_list|,
literal|"maven-assembly-plugin-1.1.2-20070427.065136-1.pom"
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
name|PATH_TO_TEST_ORDER_OF_DELETION
argument_list|)
expr_stmt|;
name|listenerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
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
name|repoPurge
operator|=
operator|new
name|DaysOldRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|repoConfiguration
operator|.
name|getDaysOlder
argument_list|()
argument_list|,
name|repoConfiguration
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
name|versionRoot
init|=
name|repoRoot
operator|+
literal|"/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT"
decl_stmt|;
name|Calendar
name|currentDate
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|DateUtils
operator|.
name|UTC_TIME_ZONE
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
name|File
name|jarFile
init|=
operator|new
name|File
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
name|jarFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|File
name|pomFile
init|=
operator|new
name|File
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
name|pomFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
comment|// set timestamp to older than 100 days for the first build, but ensure the filename timestamp is honoured instead
if|if
condition|(
name|i
operator|==
literal|5
condition|)
block|{
name|jarFile
operator|.
name|setLastModified
argument_list|(
name|OLD_TIMESTAMP
argument_list|)
expr_stmt|;
name|pomFile
operator|.
name|setLastModified
argument_list|(
name|OLD_TIMESTAMP
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-utils"
argument_list|,
literal|"1.4.3-20070113.163208-4"
argument_list|,
literal|"plexus-utils-1.4.3-20070113.163208-4.jar"
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
literal|"1.4.3-20070113.163208-4"
argument_list|,
literal|"plexus-utils-1.4.3-20070113.163208-4.pom"
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
name|PATH_TO_BY_DAYS_OLD_METADATA_DRIVEN_ARTIFACT
argument_list|)
expr_stmt|;
name|listenerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
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
annotation|@
name|After
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
block|}
end_class

end_unit

