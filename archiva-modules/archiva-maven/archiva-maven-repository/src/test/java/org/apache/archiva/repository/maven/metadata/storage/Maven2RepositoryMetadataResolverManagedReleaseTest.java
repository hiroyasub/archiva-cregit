begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|maven
operator|.
name|metadata
operator|.
name|storage
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|filter
operator|.
name|AllFilter
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
name|filter
operator|.
name|Filter
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
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|ReadMetadataRequest
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
name|repository
operator|.
name|storage
operator|.
name|RepositoryStorageRuntimeException
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
name|common
operator|.
name|proxy
operator|.
name|WagonFactory
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
name|RepositoryRegistry
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
name|base
operator|.
name|RepositoryHandlerDependencies
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_class
specifier|public
class|class
name|Maven2RepositoryMetadataResolverManagedReleaseTest
extends|extends
name|Maven2RepositoryMetadataResolverTest
block|{
specifier|private
specifier|static
specifier|final
name|Filter
argument_list|<
name|String
argument_list|>
name|ALL
init|=
operator|new
name|AllFilter
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"repositoryStorage#maven2"
argument_list|)
specifier|private
name|Maven2RepositoryStorage
name|storage
decl_stmt|;
annotation|@
name|Inject
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|Inject
name|RepositoryHandlerDependencies
name|repositoryHandlerDependencies
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"test"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REMOTE_REPO_ID
init|=
literal|"central"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ASF_SCM_CONN_BASE
init|=
literal|"scm:svn:http://svn.apache.org/repos/asf/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ASF_SCM_DEV_CONN_BASE
init|=
literal|"scm:svn:https://svn.apache.org/repos/asf/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ASF_SCM_VIEWVC_BASE
init|=
literal|"http://svn.apache.org/viewvc/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SCM_CONN_BASE
init|=
literal|"scm:svn:http://svn.example.com/repos/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SCM_DEV_CONN_BASE
init|=
literal|"scm:svn:https://svn.example.com/repos/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SCM_URL_BASE
init|=
literal|"http://svn.example.com/repos/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_MD5
init|=
literal|"d41d8cd98f00b204e9800998ecf8427e"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_SHA1
init|=
literal|"da39a3ee5e6b4b0d3255bfef95601890afd80709"
decl_stmt|;
specifier|private
name|WagonFactory
name|wagonFactory
decl_stmt|;
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
name|testRepo
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|setSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|save
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|reload
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Override
specifier|public
name|void
name|testModelWithJdkProfileActivation
parameter_list|()
throws|throws
name|Exception
block|{
comment|// skygo IMHO must fail because TEST_REPO_ID ( is snap ,no release) and we seek for a snapshot
name|ReadMetadataRequest
name|readMetadataRequest
init|=
operator|new
name|ReadMetadataRequest
argument_list|()
operator|.
name|repositoryId
argument_list|(
name|TEST_REPO_ID
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"org.apache.maven"
argument_list|)
operator|.
name|projectId
argument_list|(
literal|"maven-archiver"
argument_list|)
operator|.
name|projectVersion
argument_list|(
literal|"2.4.1"
argument_list|)
decl_stmt|;
name|ProjectVersionMetadata
name|metadata
init|=
name|storage
operator|.
name|readProjectVersionMetadata
argument_list|(
name|readMetadataRequest
argument_list|)
decl_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryStorageRuntimeException
operator|.
name|class
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|testGetProjectVersionMetadataForTimestampedSnapshotMissingMetadata
parameter_list|()
throws|throws
name|Exception
block|{
name|ReadMetadataRequest
name|readMetadataRequest
init|=
operator|new
name|ReadMetadataRequest
argument_list|()
operator|.
name|repositoryId
argument_list|(
name|TEST_REPO_ID
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"com.example.test"
argument_list|)
operator|.
name|projectId
argument_list|(
literal|"missing-metadata"
argument_list|)
operator|.
name|projectVersion
argument_list|(
literal|"1.0-SNAPSHOT"
argument_list|)
decl_stmt|;
name|storage
operator|.
name|readProjectVersionMetadata
argument_list|(
name|readMetadataRequest
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryStorageRuntimeException
operator|.
name|class
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|testGetProjectVersionMetadataForTimestampedSnapshotMalformedMetadata
parameter_list|()
throws|throws
name|Exception
block|{
name|ReadMetadataRequest
name|readMetadataRequest
init|=
operator|new
name|ReadMetadataRequest
argument_list|()
operator|.
name|repositoryId
argument_list|(
name|TEST_REPO_ID
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"com.example.test"
argument_list|)
operator|.
name|projectVersion
argument_list|(
literal|"malformed-metadata"
argument_list|)
operator|.
name|projectVersion
argument_list|(
literal|"1.0-SNAPSHOT"
argument_list|)
decl_stmt|;
name|storage
operator|.
name|readProjectVersionMetadata
argument_list|(
name|readMetadataRequest
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryStorageRuntimeException
operator|.
name|class
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|testGetProjectVersionMetadataForTimestampedSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|testGetProjectVersionMetadataForTimestampedSnapshot
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RepositoryStorageRuntimeException
operator|.
name|class
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|testGetProjectVersionMetadataForTimestampedSnapshotIncompleteMetadata
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|testGetProjectVersionMetadataForTimestampedSnapshotIncompleteMetadata
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

