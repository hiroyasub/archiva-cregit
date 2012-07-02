begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|repositories
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Action
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
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|RemoteRepository
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
name|admin
operator|.
name|repository
operator|.
name|remote
operator|.
name|DefaultRemoteRepositoryAdmin
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
name|archiva
operator|.
name|configuration
operator|.
name|IndeterminateConfigurationException
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
name|archiva
operator|.
name|configuration
operator|.
name|ProxyConnectorConfiguration
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
name|configuration
operator|.
name|RemoteRepositoryConfiguration
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
name|web
operator|.
name|action
operator|.
name|AbstractActionTestCase
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
comment|/**  * DeleteRemoteRepositoryActionTest  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DeleteRemoteRepositoryActionTest
extends|extends
name|AbstractActionTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|REPO_ID
init|=
literal|"remote-repo-ident"
decl_stmt|;
specifier|private
name|DeleteRemoteRepositoryAction
name|action
decl_stmt|;
specifier|private
name|MockControl
name|archivaConfigurationControl
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
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
comment|//action = (DeleteRemoteRepositoryAction) lookup( Action.class.getName(), "deleteRemoteRepositoryAction" );
name|action
operator|=
operator|(
name|DeleteRemoteRepositoryAction
operator|)
name|getActionProxy
argument_list|(
literal|"/admin/deleteRemoteRepository.action"
argument_list|)
operator|.
name|getAction
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|archivaConfigurationControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
operator|(
operator|(
name|DefaultRemoteRepositoryAdmin
operator|)
name|action
operator|.
name|getRemoteRepositoryAdmin
argument_list|()
operator|)
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteRemoteRepositoryConfirmation
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepository
name|originalRepository
init|=
name|createRepository
argument_list|()
decl_stmt|;
name|Configuration
name|configuration
init|=
name|createConfigurationForEditing
argument_list|(
name|originalRepository
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepoid
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO_ID
argument_list|,
name|action
operator|.
name|getRepoid
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteRepository
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|confirmDelete
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|repository
operator|=
name|action
operator|.
name|getRepository
argument_list|()
expr_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|originalRepository
argument_list|)
argument_list|,
name|action
operator|.
name|getRemoteRepositoryAdmin
argument_list|()
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteRemoteRepository
parameter_list|()
throws|throws
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|redback
operator|.
name|components
operator|.
name|registry
operator|.
name|RegistryException
throws|,
name|IndeterminateConfigurationException
throws|,
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|createConfigurationForEditing
argument_list|(
name|createRepository
argument_list|()
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
literal|"internal"
argument_list|,
literal|"target/repo/internal"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
literal|"snapshots"
argument_list|,
literal|"target/repo/snapshots"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addProxyConnector
argument_list|(
name|createProxyConnector
argument_list|(
literal|"internal"
argument_list|,
name|REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepoid
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO_ID
argument_list|,
name|action
operator|.
name|getRepoid
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteRepository
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|configuration
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|delete
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|configuration
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteRemoteRepositoryCancelled
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepository
name|originalRepository
init|=
name|createRepository
argument_list|()
decl_stmt|;
name|Configuration
name|configuration
init|=
name|createConfigurationForEditing
argument_list|(
name|originalRepository
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepoid
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO_ID
argument_list|,
name|action
operator|.
name|getRepoid
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteRepository
name|repositoryConfiguration
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repositoryConfiguration
argument_list|)
expr_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repositoryConfiguration
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|execute
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|RemoteRepository
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|originalRepository
argument_list|)
argument_list|,
name|action
operator|.
name|getRemoteRepositoryAdmin
argument_list|()
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Configuration
name|createConfigurationForEditing
parameter_list|(
name|RemoteRepository
name|repositoryConfiguration
parameter_list|)
block|{
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|RemoteRepositoryConfiguration
name|conf
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setId
argument_list|(
name|repositoryConfiguration
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setLayout
argument_list|(
name|repositoryConfiguration
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setUrl
argument_list|(
name|repositoryConfiguration
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setName
argument_list|(
name|repositoryConfiguration
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addRemoteRepository
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
specifier|private
name|RemoteRepository
name|createRepository
parameter_list|()
block|{
name|RemoteRepository
name|r
init|=
operator|new
name|RemoteRepository
argument_list|()
decl_stmt|;
name|r
operator|.
name|setId
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|populateRepository
argument_list|(
name|r
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|private
name|void
name|assertRepositoryEquals
parameter_list|(
name|RemoteRepository
name|expectedRepository
parameter_list|,
name|RemoteRepository
name|actualRepository
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getLayout
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getUrl
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getName
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|createManagedRepository
parameter_list|(
name|String
name|string
parameter_list|,
name|String
name|testPath
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|r
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|r
operator|.
name|setId
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|r
operator|.
name|setName
argument_list|(
literal|"repo name"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setLocation
argument_list|(
name|testPath
argument_list|)
expr_stmt|;
name|r
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setRefreshCronExpression
argument_list|(
literal|"* 0/5 * * * ?"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setDaysOlder
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|r
operator|.
name|setRetentionCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|r
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r
operator|.
name|setSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r
operator|.
name|setScanned
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|private
name|ProxyConnectorConfiguration
name|createProxyConnector
parameter_list|(
name|String
name|managedRepoId
parameter_list|,
name|String
name|remoteRepoId
parameter_list|)
block|{
name|ProxyConnectorConfiguration
name|connector
init|=
operator|new
name|ProxyConnectorConfiguration
argument_list|()
decl_stmt|;
name|connector
operator|.
name|setSourceRepoId
argument_list|(
name|managedRepoId
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setTargetRepoId
argument_list|(
name|remoteRepoId
argument_list|)
expr_stmt|;
return|return
name|connector
return|;
block|}
specifier|private
name|void
name|populateRepository
parameter_list|(
name|RemoteRepository
name|repository
parameter_list|)
block|{
name|repository
operator|.
name|setId
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setName
argument_list|(
literal|"repo name"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setUrl
argument_list|(
literal|"url"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
block|}
comment|// TODO: what about removing proxied content if a proxy is removed?
block|}
end_class

end_unit

