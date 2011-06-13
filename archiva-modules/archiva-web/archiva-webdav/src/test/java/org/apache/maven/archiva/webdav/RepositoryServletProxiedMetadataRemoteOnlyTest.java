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
name|webdav
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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

begin_comment
comment|/**  * RepositoryServlet Tests, Proxied, Get of Metadata, exists on remote repository only.  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletProxiedMetadataRemoteOnlyTest
extends|extends
name|AbstractRepositoryServletProxiedMetadataTestCase
block|{
annotation|@
name|Before
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotVersionMetadataRemoteOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/archivatest-maven-plugin/4.0-alpha-1-SNAPSHOT/maven-metadata.xml"
decl_stmt|;
name|String
name|version
init|=
literal|"4.0-alpha-1-SNAPSHOT"
decl_stmt|;
name|String
name|timestamp
init|=
literal|"20040305.112233"
decl_stmt|;
name|String
name|buildNumber
init|=
literal|"2"
decl_stmt|;
name|String
name|lastUpdated
init|=
literal|"20040305112233"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createVersionMetadata
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"archivatest-maven-plugin"
argument_list|,
name|version
argument_list|,
name|timestamp
argument_list|,
name|buildNumber
argument_list|,
name|lastUpdated
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|remoteSnapshots
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteSnapshots
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remotePrivateSnapshots
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedPluginSnapshotVersionMetadataRemoteOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/maven/plugins/maven-assembly-plugin/2.2-beta-2-SNAPSHOT/maven-metadata.xml"
decl_stmt|;
name|String
name|version
init|=
literal|"2.2-beta-2-SNAPSHOT"
decl_stmt|;
name|String
name|timestamp
init|=
literal|"20071017.162810"
decl_stmt|;
name|String
name|buildNumber
init|=
literal|"20"
decl_stmt|;
name|String
name|lastUpdated
init|=
literal|"20071017162810"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createVersionMetadata
argument_list|(
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-assembly-plugin"
argument_list|,
name|version
argument_list|,
name|timestamp
argument_list|,
name|buildNumber
argument_list|,
name|lastUpdated
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|remoteSnapshots
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteSnapshots
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remotePrivateSnapshots
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedVersionMetadataRemoteOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/archivatest-maven-plugin/4.0-alpha-2/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createVersionMetadata
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"archivatest-maven-plugin"
argument_list|,
literal|"4.0-alpha-2"
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|remoteSnapshots
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteSnapshots
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remotePrivateSnapshots
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedProjectMetadataRemoteOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/archivatest-maven-plugin/maven-metadata.xml"
decl_stmt|;
name|String
name|latest
init|=
literal|"1.0-alpha-4"
decl_stmt|;
name|String
name|release
init|=
literal|"1.0-alpha-4"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createProjectMetadata
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"archivatest-maven-plugin"
argument_list|,
name|latest
argument_list|,
name|release
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0-alpha-4"
block|}
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|remoteSnapshots
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteSnapshots
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remotePrivateSnapshots
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedGroupMetadataRemoteOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createGroupMetadata
argument_list|(
literal|"org.apache.archiva"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archivatest-maven-plugin"
block|}
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|remoteSnapshots
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteSnapshots
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remotePrivateSnapshots
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

