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
name|xwork
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
name|IndeterminateConfigurationException
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
name|RemoteRepositoryConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|registry
operator|.
name|RegistryException
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

begin_comment
comment|/**  * Test the repositories action returns the correct data.  */
end_comment

begin_class
specifier|public
class|class
name|ConfigureRemoteRepositoryActionTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ConfigureRemoteRepositoryAction
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
specifier|private
specifier|static
specifier|final
name|String
name|REPO_ID
init|=
literal|"remote-repo-ident"
decl_stmt|;
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
comment|// TODO: purely to quiet logging - shouldn't be needed
name|String
name|appserverBase
init|=
name|getTestFile
argument_list|(
literal|"target/appserver-base"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"appserver.base"
argument_list|,
name|appserverBase
argument_list|)
expr_stmt|;
name|action
operator|=
operator|(
name|ConfigureRemoteRepositoryAction
operator|)
name|lookup
argument_list|(
name|Action
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"configureRemoteRepositoryAction"
argument_list|)
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
name|action
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
operator|new
name|Configuration
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|SecureActionBundle
name|bundle
init|=
name|action
operator|.
name|getSecureActionBundle
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|bundle
operator|.
name|requiresAuthentication
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bundle
operator|.
name|getAuthorizationTuples
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddRemoteRepositoryInitialPage
parameter_list|()
throws|throws
name|Exception
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
operator|new
name|Configuration
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getRepoid
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getMode
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|configuration
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|configuration
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|add
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
block|}
specifier|public
name|void
name|testAddRemoteRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
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
name|prepare
argument_list|()
expr_stmt|;
name|action
operator|.
name|setMode
argument_list|(
literal|"add"
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|populateRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|save
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
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|repository
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testEditRemoteRepositoryInitialPage
parameter_list|()
throws|throws
name|Exception
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
name|assertNull
argument_list|(
name|action
operator|.
name|getMode
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
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
name|edit
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
block|}
specifier|public
name|void
name|testEditRemoteRepository
parameter_list|()
throws|throws
name|Exception
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
name|prepare
argument_list|()
expr_stmt|;
name|action
operator|.
name|setMode
argument_list|(
literal|"edit"
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|populateRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setName
argument_list|(
literal|"new repo name"
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|save
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
name|RemoteRepositoryConfiguration
name|newRepository
init|=
name|createRepository
argument_list|()
decl_stmt|;
name|newRepository
operator|.
name|setName
argument_list|(
literal|"new repo name"
argument_list|)
expr_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|newRepository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|repository
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRemoteRepositoryConfirmation
parameter_list|()
block|{
name|RemoteRepositoryConfiguration
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
name|assertNull
argument_list|(
name|action
operator|.
name|getMode
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
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
name|confirm
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
name|configuration
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRemoteRepositoryKeepContent
parameter_list|()
throws|throws
name|RegistryException
throws|,
name|IndeterminateConfigurationException
block|{
name|Configuration
name|configuration
init|=
name|executeDeletionTest
argument_list|(
name|createRepository
argument_list|()
argument_list|)
decl_stmt|;
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
block|}
specifier|public
name|void
name|testDeleteRemoteRepositoryCancelled
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepositoryConfiguration
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
name|RemoteRepositoryConfiguration
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
name|RemoteRepositoryConfiguration
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
name|configuration
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Configuration
name|executeDeletionTest
parameter_list|(
name|RemoteRepositoryConfiguration
name|originalRepository
parameter_list|)
throws|throws
name|RegistryException
throws|,
name|IndeterminateConfigurationException
block|{
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
name|RemoteRepositoryConfiguration
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
return|return
name|configuration
return|;
block|}
specifier|private
name|void
name|assertRepositoryEquals
parameter_list|(
name|RemoteRepositoryConfiguration
name|expectedRepository
parameter_list|,
name|RemoteRepositoryConfiguration
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
name|Configuration
name|createConfigurationForEditing
parameter_list|(
name|RemoteRepositoryConfiguration
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
name|configuration
operator|.
name|addRemoteRepository
argument_list|(
name|repositoryConfiguration
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
specifier|private
name|RemoteRepositoryConfiguration
name|createRepository
parameter_list|()
block|{
name|RemoteRepositoryConfiguration
name|r
init|=
operator|new
name|RemoteRepositoryConfiguration
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
name|populateRepository
parameter_list|(
name|RemoteRepositoryConfiguration
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
comment|// TODO: test errors during add, other actions
comment|// TODO: what if there are proxy connectors attached to a deleted repository?
comment|// TODO: what about removing proxied content if a proxy is removed?
block|}
end_class

end_unit

