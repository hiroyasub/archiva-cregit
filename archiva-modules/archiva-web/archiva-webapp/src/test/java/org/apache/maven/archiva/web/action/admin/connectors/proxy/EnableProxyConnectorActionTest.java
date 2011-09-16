begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Copyright 2008 jdumay.  *   *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

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
name|connectors
operator|.
name|proxy
package|;
end_package

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
name|repository
operator|.
name|managed
operator|.
name|DefaultManagedRepositoryAdmin
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
name|proxyconnector
operator|.
name|DefaultProxyConnectorAdmin
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
name|maven
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|AbstractWebworkTestCase
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
name|codehaus
operator|.
name|redback
operator|.
name|integration
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
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_class
specifier|public
class|class
name|EnableProxyConnectorActionTest
extends|extends
name|AbstractWebworkTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_TARGET_ID
init|=
literal|"central"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SOURCE_ID
init|=
literal|"corporate"
decl_stmt|;
specifier|private
name|EnableProxyConnectorAction
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
name|action
operator|=
operator|(
name|EnableProxyConnectorAction
operator|)
name|getActionProxy
argument_list|(
literal|"/admin/enableProxyConnector.action"
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
name|DefaultManagedRepositoryAdmin
operator|)
name|action
operator|.
name|getManagedRepositoryAdmin
argument_list|()
operator|)
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
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
operator|(
operator|(
name|DefaultProxyConnectorAdmin
operator|)
name|action
operator|.
name|getProxyConnectorAdmin
argument_list|()
operator|)
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|expectConfigurationRequests
parameter_list|(
name|int
name|requestConfigCount
parameter_list|)
throws|throws
name|RegistryException
throws|,
name|IndeterminateConfigurationException
block|{
name|Configuration
name|config
init|=
name|createInitialConfiguration
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
name|config
argument_list|,
name|requestConfigCount
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConfirmDeleteBadSourceOrTarget
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Attempt to show the confirm enable screen, but provide
comment|// a bad source id or target id to actually enable
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSource
argument_list|(
literal|"bad-source"
argument_list|)
expr_stmt|;
comment|// id doesn't exist.
name|action
operator|.
name|setTarget
argument_list|(
literal|"bad-target"
argument_list|)
expr_stmt|;
comment|// id doesn't exist.
name|String
name|status
init|=
name|action
operator|.
name|confirmEnable
argument_list|()
decl_stmt|;
comment|// Should have resulted in an error.
name|assertEquals
argument_list|(
name|Action
operator|.
name|ERROR
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSource
argument_list|(
literal|"bad"
argument_list|)
expr_stmt|;
comment|// Bad doesn't exist.
name|action
operator|.
name|setTarget
argument_list|(
name|TEST_TARGET_ID
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|confirmEnable
argument_list|()
expr_stmt|;
comment|// Should have resulted in an error.
name|assertEquals
argument_list|(
name|Action
operator|.
name|ERROR
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSource
argument_list|(
name|TEST_SOURCE_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTarget
argument_list|(
literal|"bad"
argument_list|)
expr_stmt|;
comment|// Bad doesn't exist.
name|status
operator|=
name|action
operator|.
name|confirmEnable
argument_list|()
expr_stmt|;
comment|// Should have resulted in an error.
name|assertEquals
argument_list|(
name|Action
operator|.
name|ERROR
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConfirmEnableNoSourceOrTarget
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Attempt to show the confirm enable screen, but don't provide
comment|// the source id or target id to actually delete
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSource
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// No source Id.
name|action
operator|.
name|setTarget
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// No target Id.
name|String
name|status
init|=
name|action
operator|.
name|confirmEnable
argument_list|()
decl_stmt|;
comment|// Should have resulted in an error.
name|assertEquals
argument_list|(
name|Action
operator|.
name|ERROR
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSource
argument_list|(
name|TEST_SOURCE_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTarget
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// No target Id.
name|status
operator|=
name|action
operator|.
name|confirmEnable
argument_list|()
expr_stmt|;
comment|// Should have resulted in an error.
name|assertEquals
argument_list|(
name|Action
operator|.
name|ERROR
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSource
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// No source Id.
name|action
operator|.
name|setTarget
argument_list|(
name|TEST_TARGET_ID
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|confirmEnable
argument_list|()
expr_stmt|;
comment|// Should have resulted in an error.
name|assertEquals
argument_list|(
name|Action
operator|.
name|ERROR
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEnable
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Show the confirm the enable of proxy connector screen.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSource
argument_list|(
name|TEST_SOURCE_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTarget
argument_list|(
name|TEST_TARGET_ID
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|confirmEnable
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
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
comment|// Perform the delete.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|enable
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertHasMessages
argument_list|(
name|action
argument_list|)
expr_stmt|;
comment|// Test the configuration.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ProxyConnectorConfiguration
name|config
init|=
operator|(
name|ProxyConnectorConfiguration
operator|)
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|config
operator|.
name|isDisabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSecureActionBundle
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
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
name|testConfirmEnable
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Show the confirm the enable of proxy connector screen.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSource
argument_list|(
name|TEST_SOURCE_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTarget
argument_list|(
name|TEST_TARGET_ID
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|confirmEnable
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
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Configuration
name|createInitialConfiguration
parameter_list|()
block|{
name|Configuration
name|config
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|managedRepo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|managedRepo
operator|.
name|setId
argument_list|(
name|TEST_SOURCE_ID
argument_list|)
expr_stmt|;
name|managedRepo
operator|.
name|setLayout
argument_list|(
literal|"${java.io.tmpdir}/archiva-test/managed-repo"
argument_list|)
expr_stmt|;
name|managedRepo
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|managedRepo
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|remoteRepo
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|()
decl_stmt|;
name|remoteRepo
operator|.
name|setId
argument_list|(
name|TEST_TARGET_ID
argument_list|)
expr_stmt|;
name|remoteRepo
operator|.
name|setUrl
argument_list|(
literal|"http://repo1.maven.org/maven2/"
argument_list|)
expr_stmt|;
name|config
operator|.
name|addRemoteRepository
argument_list|(
name|remoteRepo
argument_list|)
expr_stmt|;
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
name|TEST_SOURCE_ID
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setTargetRepoId
argument_list|(
name|TEST_TARGET_ID
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setDisabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|addProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

