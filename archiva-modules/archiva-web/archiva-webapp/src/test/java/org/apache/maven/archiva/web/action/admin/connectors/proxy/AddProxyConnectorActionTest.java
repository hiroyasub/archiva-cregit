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
name|connectors
operator|.
name|proxy
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|policies
operator|.
name|CachedFailuresPolicy
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
name|policies
operator|.
name|ChecksumPolicy
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
name|policies
operator|.
name|PropagateErrorsDownloadPolicy
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
name|policies
operator|.
name|PropagateErrorsOnUpdateDownloadPolicy
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
name|policies
operator|.
name|ReleasesPolicy
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
name|policies
operator|.
name|SnapshotsPolicy
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * AddProxyConnectorActionTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|AddProxyConnectorActionTest
extends|extends
name|AbstractWebworkTestCase
block|{
specifier|private
name|AddProxyConnectorAction
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
specifier|public
name|void
name|testAddBlackListPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Prepare Test.
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ProxyConnectorConfiguration
name|connector
init|=
name|action
operator|.
name|getConnector
argument_list|()
decl_stmt|;
name|populateProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
comment|// Perform Test w/no values.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|addBlackListPattern
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
comment|// Should have returned an error, with no blacklist pattern added.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|connector
operator|.
name|getBlackListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Try again, but now with a pattern to add.
name|action
operator|.
name|setBlackListPattern
argument_list|(
literal|"**/*-javadoc.jar"
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|addBlackListPattern
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have no error, and 1 blacklist pattern added.
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|connector
operator|.
name|getBlackListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Prepare Test.
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ProxyConnectorConfiguration
name|connector
init|=
name|action
operator|.
name|getConnector
argument_list|()
decl_stmt|;
name|populateProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
comment|// Perform Test w/no values.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|addProperty
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
comment|// Should have returned an error, with no property pattern added.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|connector
operator|.
name|getProperties
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Try again, but now with a property key/value to add.
name|action
operator|.
name|setPropertyKey
argument_list|(
literal|"eat-a"
argument_list|)
expr_stmt|;
name|action
operator|.
name|setPropertyValue
argument_list|(
literal|"gramov-a-bits"
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|addProperty
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have no error, and 1 property added.
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|connector
operator|.
name|getProperties
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddProxyConnectorCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Prepare Test.
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ProxyConnectorConfiguration
name|connector
init|=
name|action
operator|.
name|getConnector
argument_list|()
decl_stmt|;
name|populateProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
comment|// forms will use an array
name|connector
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"eat-a"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"gramov-a-bits"
block|}
argument_list|)
expr_stmt|;
comment|// Create the input screen.
name|assertRequestStatus
argument_list|(
name|action
argument_list|,
name|Action
operator|.
name|SUCCESS
argument_list|,
literal|"commit"
argument_list|)
expr_stmt|;
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
comment|// Test configuration.
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|proxyConfigs
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|proxyConfigs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|proxyConfigs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ProxyConnectorConfiguration
name|actualConnector
init|=
name|proxyConfigs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|actualConnector
argument_list|)
expr_stmt|;
comment|// The use of "(direct connection)" should result in a proxyId which is<null>.
name|assertNull
argument_list|(
name|actualConnector
operator|.
name|getProxyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"corporate"
argument_list|,
name|actualConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"central"
argument_list|,
name|actualConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"gramov-a-bits"
argument_list|,
name|actualConnector
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"eat-a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddProxyConnectorInitialPage
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|3
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
name|ProxyConnectorConfiguration
name|configuration
init|=
name|action
operator|.
name|getConnector
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
name|getProxyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|configuration
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|configuration
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getPolicies
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getBlackListPatterns
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getWhiteListPatterns
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|input
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
name|testAddWhiteListPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Prepare Test.
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ProxyConnectorConfiguration
name|connector
init|=
name|action
operator|.
name|getConnector
argument_list|()
decl_stmt|;
name|populateProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
comment|// Perform Test w/no values.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|addWhiteListPattern
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
comment|// Should have returned an error, with no whitelist pattern added.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|connector
operator|.
name|getWhiteListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Try again, but now with a pattern to add.
name|action
operator|.
name|setWhiteListPattern
argument_list|(
literal|"**/*.jar"
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|addWhiteListPattern
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have no error, and 1 whitelist pattern added.
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|connector
operator|.
name|getWhiteListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveBlackListPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Prepare Test.
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ProxyConnectorConfiguration
name|connector
init|=
name|action
operator|.
name|getConnector
argument_list|()
decl_stmt|;
name|populateProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
comment|// Add some arbitrary blacklist patterns.
name|connector
operator|.
name|addBlackListPattern
argument_list|(
literal|"**/*-javadoc.jar"
argument_list|)
expr_stmt|;
name|connector
operator|.
name|addBlackListPattern
argument_list|(
literal|"**/*.war"
argument_list|)
expr_stmt|;
comment|// Perform Test w/no pattern value.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|removeBlackListPattern
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
comment|// Should have returned an error, with no blacklist pattern removed.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|connector
operator|.
name|getBlackListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Perform test w/invalid (non-existant) pattern value to remove.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setPattern
argument_list|(
literal|"**/*oops*"
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|removeBlackListPattern
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have returned an error, with no blacklist pattern removed.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|connector
operator|.
name|getBlackListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Try again, but now with a valid pattern to remove.
name|action
operator|.
name|setPattern
argument_list|(
literal|"**/*-javadoc.jar"
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|removeBlackListPattern
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have no error, and 1 blacklist pattern left.
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|connector
operator|.
name|getBlackListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have left 1 blacklist pattern"
argument_list|,
literal|"**/*.war"
argument_list|,
name|connector
operator|.
name|getBlackListPatterns
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Prepare Test.
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ProxyConnectorConfiguration
name|connector
init|=
name|action
operator|.
name|getConnector
argument_list|()
decl_stmt|;
name|populateProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
comment|// Add some arbitrary properties.
name|connector
operator|.
name|addProperty
argument_list|(
literal|"username"
argument_list|,
literal|"general-tso"
argument_list|)
expr_stmt|;
name|connector
operator|.
name|addProperty
argument_list|(
literal|"password"
argument_list|,
literal|"chicken"
argument_list|)
expr_stmt|;
comment|// Perform Test w/no property key.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|removeProperty
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
comment|// Should have returned an error, with no properties removed.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|connector
operator|.
name|getProperties
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Perform test w/invalid (non-existant) property key to remove.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setPropertyKey
argument_list|(
literal|"slurm"
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|removeProperty
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have returned an error, with no properties removed.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|connector
operator|.
name|getProperties
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Try again, but now with a valid property to remove.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setPropertyKey
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|removeProperty
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have no error, and 1 property left.
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|connector
operator|.
name|getProperties
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have left 1 property"
argument_list|,
literal|"general-tso"
argument_list|,
name|connector
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"username"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveWhiteListPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|expectConfigurationRequests
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Prepare Test.
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ProxyConnectorConfiguration
name|connector
init|=
name|action
operator|.
name|getConnector
argument_list|()
decl_stmt|;
name|populateProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
comment|// Add some arbitrary whitelist patterns.
name|connector
operator|.
name|addWhiteListPattern
argument_list|(
literal|"javax/**/*"
argument_list|)
expr_stmt|;
name|connector
operator|.
name|addWhiteListPattern
argument_list|(
literal|"com/sun/**/*"
argument_list|)
expr_stmt|;
comment|// Perform Test w/no pattern value.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|removeWhiteListPattern
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
comment|// Should have returned an error, with no whitelist pattern removed.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|connector
operator|.
name|getWhiteListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Perform test w/invalid (non-existant) pattern value to remove.
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|action
operator|.
name|setPattern
argument_list|(
literal|"**/*oops*"
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|removeWhiteListPattern
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have returned an error, with no whitelist pattern removed.
name|assertHasErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|connector
operator|.
name|getWhiteListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Try again, but now with a valid pattern to remove.
name|action
operator|.
name|setPattern
argument_list|(
literal|"com/sun/**/*"
argument_list|)
expr_stmt|;
name|preRequest
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|status
operator|=
name|action
operator|.
name|removeWhiteListPattern
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// Should have no error, and 1 whitelist pattern left.
name|assertNoErrors
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|connector
operator|.
name|getWhiteListPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have left 1 whitelist pattern"
argument_list|,
literal|"javax/**/*"
argument_list|,
name|connector
operator|.
name|getWhiteListPatterns
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
literal|3
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
literal|"corporate"
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
literal|"central"
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
return|return
name|config
return|;
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|requestConfigCount
condition|;
name|i
operator|++
control|)
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
name|config
argument_list|)
expr_stmt|;
block|}
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|populateProxyConnector
parameter_list|(
name|ProxyConnectorConfiguration
name|connector
parameter_list|)
block|{
name|connector
operator|.
name|setProxyId
argument_list|(
name|AbstractProxyConnectorFormAction
operator|.
name|DIRECT_CONNECTION
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setSourceRepoId
argument_list|(
literal|"corporate"
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setTargetRepoId
argument_list|(
literal|"central"
argument_list|)
expr_stmt|;
comment|// TODO: Set these options programatically via list of available policies.
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|policies
init|=
name|connector
operator|.
name|getPolicies
argument_list|()
decl_stmt|;
name|policies
operator|.
name|put
argument_list|(
literal|"releases"
argument_list|,
operator|new
name|ReleasesPolicy
argument_list|()
operator|.
name|getDefaultOption
argument_list|()
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
literal|"snapshots"
argument_list|,
operator|new
name|SnapshotsPolicy
argument_list|()
operator|.
name|getDefaultOption
argument_list|()
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
literal|"checksum"
argument_list|,
operator|new
name|ChecksumPolicy
argument_list|()
operator|.
name|getDefaultOption
argument_list|()
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
literal|"cache-failures"
argument_list|,
operator|new
name|CachedFailuresPolicy
argument_list|()
operator|.
name|getDefaultOption
argument_list|()
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
literal|"propagate-errors"
argument_list|,
operator|new
name|PropagateErrorsDownloadPolicy
argument_list|()
operator|.
name|getDefaultOption
argument_list|()
argument_list|)
expr_stmt|;
name|policies
operator|.
name|put
argument_list|(
literal|"propagate-errors-on-update"
argument_list|,
operator|new
name|PropagateErrorsOnUpdateDownloadPolicy
argument_list|()
operator|.
name|getDefaultOption
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|AddProxyConnectorAction
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
literal|"addProxyConnectorAction"
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
block|}
end_class

end_unit

