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
name|DaysOldRepositoryPurgeTest
extends|extends
name|AbstractRepositoryPurgeTest
block|{
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
name|repoPurge
operator|=
operator|new
name|DaysOldRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|getLayout
argument_list|()
argument_list|,
name|dao
argument_list|,
name|getRepoConfiguration
argument_list|()
operator|.
name|getDaysOlder
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setLastModified
parameter_list|(
name|String
name|dirPath
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
name|testByLastModified
parameter_list|()
throws|throws
name|Exception
block|{
name|populateDbForTestByLastModified
argument_list|()
expr_stmt|;
name|File
name|testDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectoryToDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test-classes/test-repo"
argument_list|)
argument_list|,
name|testDir
argument_list|)
expr_stmt|;
name|setLastModified
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-install-plugin/2.2-SNAPSHOT/"
argument_list|)
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|PATH_TO_BY_DAYS_OLD_ARTIFACT
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-install-plugin/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-install-plugin/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.jar.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-install-plugin/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-install-plugin/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-install-plugin/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.pom.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-install-plugin/2.2-SNAPSHOT/maven-install-plugin-2.2-SNAPSHOT.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|testDir
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMetadataDrivenSnapshots
parameter_list|()
throws|throws
name|Exception
block|{
name|populateDbForTestMetadataDrivenSnapshots
argument_list|()
expr_stmt|;
name|File
name|testDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectoryToDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test-classes/test-repo"
argument_list|)
argument_list|,
name|testDir
argument_list|)
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|PATH_TO_BY_DAYS_OLD_METADATA_DRIVEN_ARTIFACT
argument_list|)
expr_stmt|;
comment|// this should be deleted since the filename version (timestamp) is older than
comment|// 100 days even if the last modified date was<100 days ago
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070113.163208-4.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070113.163208-4.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070113.163208-4.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070113.163208-4.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// musn't be deleted since the filename version (timestamp) is not older than 100 days
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070618.102615-5.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070618.102615-5.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070618.102615-5.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070618.102615-5.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070630.113158-6.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070630.113158-6.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070630.113158-6.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070630.113158-6.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070707.122114-7.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070707.122114-7.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070707.122114-7.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070707.122114-7.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// mustn't be deleted since the last modified date is<100 days (this is not a timestamped version)
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-SNAPSHOT.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-SNAPSHOT.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|testDir
argument_list|)
expr_stmt|;
block|}
specifier|protected
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
specifier|private
name|void
name|populateDbForTestByLastModified
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|versions
init|=
operator|new
name|ArrayList
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
specifier|private
name|void
name|populateDbForTestMetadataDrivenSnapshots
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|versions
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.4.3-20070113.163208-4"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.4.3-20070618.102615-5"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.4.3-20070630.113158-6"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.4.3-20070707.122114-7"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.4.3-SNAPSHOT"
argument_list|)
expr_stmt|;
name|populateDb
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-utils"
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

