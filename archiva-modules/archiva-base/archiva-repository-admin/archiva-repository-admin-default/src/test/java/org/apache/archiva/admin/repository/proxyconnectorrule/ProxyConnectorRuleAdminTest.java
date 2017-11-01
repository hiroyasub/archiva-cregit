begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|proxyconnectorrule
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|beans
operator|.
name|ProxyConnector
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
name|ProxyConnectorRule
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
name|ProxyConnectorRuleType
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
name|AbstractRepositoryAdminTest
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|ProxyConnectorRuleAdminTest
extends|extends
name|AbstractRepositoryAdminTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|addProxyConnectorRule
parameter_list|()
throws|throws
name|Exception
block|{
name|ProxyConnector
name|proxyConnector
init|=
operator|new
name|ProxyConnector
argument_list|()
decl_stmt|;
name|proxyConnector
operator|.
name|setSourceRepoId
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setTargetRepoId
argument_list|(
literal|"central"
argument_list|)
expr_stmt|;
name|ProxyConnectorRule
name|rule
init|=
literal|null
decl_stmt|;
try|try
block|{
name|int
name|size
init|=
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|rule
operator|=
operator|new
name|ProxyConnectorRule
argument_list|(
literal|"org/apache/maven"
argument_list|,
name|ProxyConnectorRuleType
operator|.
name|BLACK_LIST
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|proxyConnector
argument_list|)
argument_list|)
expr_stmt|;
name|proxyConnectorRuleAdmin
operator|.
name|addProxyConnectorRule
argument_list|(
name|rule
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
literal|1
argument_list|,
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|rule
operator|=
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org/apache/maven"
argument_list|,
name|rule
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"snapshots"
argument_list|,
name|rule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"central"
argument_list|,
name|rule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ProxyConnectorRuleType
operator|.
name|BLACK_LIST
argument_list|,
name|rule
operator|.
name|getProxyConnectorRuleType
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|proxyConnectorRuleAdmin
operator|.
name|deleteProxyConnectorRule
argument_list|(
name|rule
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|deleteProxyConnector
argument_list|(
name|proxyConnector
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|addProxyConnectorRuleWithTwoProxyConnectors
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepository
name|remoteRepository
init|=
operator|new
name|RemoteRepository
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
decl_stmt|;
name|remoteRepository
operator|.
name|setId
argument_list|(
literal|"archiva"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setName
argument_list|(
literal|"archiva rocks"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setUrl
argument_list|(
literal|"http://wine.org"
argument_list|)
expr_stmt|;
name|remoteRepositoryAdmin
operator|.
name|addRemoteRepository
argument_list|(
name|remoteRepository
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|size
init|=
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|ProxyConnector
name|proxyConnector1
init|=
operator|new
name|ProxyConnector
argument_list|()
decl_stmt|;
name|proxyConnector1
operator|.
name|setSourceRepoId
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|proxyConnector1
operator|.
name|setTargetRepoId
argument_list|(
literal|"central"
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector1
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|ProxyConnector
name|proxyConnector2
init|=
operator|new
name|ProxyConnector
argument_list|()
decl_stmt|;
name|proxyConnector2
operator|.
name|setSourceRepoId
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|proxyConnector2
operator|.
name|setTargetRepoId
argument_list|(
literal|"archiva"
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector2
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|ProxyConnectorRule
name|rule
init|=
operator|new
name|ProxyConnectorRule
argument_list|(
literal|"org/apache/maven"
argument_list|,
name|ProxyConnectorRuleType
operator|.
name|BLACK_LIST
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|proxyConnector1
argument_list|,
name|proxyConnector2
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|proxyConnectorRuleAdmin
operator|.
name|addProxyConnectorRule
argument_list|(
name|rule
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
literal|1
argument_list|,
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|rule
operator|=
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org/apache/maven"
argument_list|,
name|rule
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ProxyConnectorRuleType
operator|.
name|BLACK_LIST
argument_list|,
name|rule
operator|.
name|getProxyConnectorRuleType
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|proxyConnectorRuleAdmin
operator|.
name|deleteProxyConnectorRule
argument_list|(
name|rule
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|deleteProxyConnector
argument_list|(
name|proxyConnector1
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|deleteProxyConnector
argument_list|(
name|proxyConnector2
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepositoryAdmin
operator|.
name|deleteRemoteRepository
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|updateProxyConnectorRuleWithTwoProxyConnectors
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepository
name|remoteRepository
init|=
operator|new
name|RemoteRepository
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|( )
argument_list|)
decl_stmt|;
name|remoteRepository
operator|.
name|setId
argument_list|(
literal|"archiva"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setName
argument_list|(
literal|"archiva rocks"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setUrl
argument_list|(
literal|"http://wine.org"
argument_list|)
expr_stmt|;
name|remoteRepositoryAdmin
operator|.
name|addRemoteRepository
argument_list|(
name|remoteRepository
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|size
init|=
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|ProxyConnector
name|proxyConnector1
init|=
operator|new
name|ProxyConnector
argument_list|()
decl_stmt|;
name|proxyConnector1
operator|.
name|setSourceRepoId
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|proxyConnector1
operator|.
name|setTargetRepoId
argument_list|(
literal|"central"
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector1
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|ProxyConnector
name|proxyConnector2
init|=
operator|new
name|ProxyConnector
argument_list|()
decl_stmt|;
name|proxyConnector2
operator|.
name|setSourceRepoId
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|proxyConnector2
operator|.
name|setTargetRepoId
argument_list|(
literal|"archiva"
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector2
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|ProxyConnectorRule
name|rule
init|=
operator|new
name|ProxyConnectorRule
argument_list|(
literal|"org/apache/maven"
argument_list|,
name|ProxyConnectorRuleType
operator|.
name|BLACK_LIST
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|proxyConnector1
argument_list|,
name|proxyConnector2
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|proxyConnectorRuleAdmin
operator|.
name|addProxyConnectorRule
argument_list|(
name|rule
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
literal|1
argument_list|,
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|rule
operator|=
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org/apache/maven"
argument_list|,
name|rule
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//assertEquals( "snapshots", rule.getProxyConnectors().get( 0 ).getSourceRepoId() );
comment|//assertEquals( "central", rule.getProxyConnectors().get( 0 ).getTargetRepoId() );
name|assertEquals
argument_list|(
name|ProxyConnectorRuleType
operator|.
name|BLACK_LIST
argument_list|,
name|rule
operator|.
name|getProxyConnectorRuleType
argument_list|()
argument_list|)
expr_stmt|;
name|rule
operator|.
name|setProxyConnectors
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|proxyConnector1
argument_list|)
argument_list|)
expr_stmt|;
name|proxyConnectorRuleAdmin
operator|.
name|updateProxyConnectorRule
argument_list|(
name|rule
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
literal|1
argument_list|,
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|rule
operator|=
name|proxyConnectorRuleAdmin
operator|.
name|getProxyConnectorRules
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org/apache/maven"
argument_list|,
name|rule
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"snapshots"
argument_list|,
name|rule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"central"
argument_list|,
name|rule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|proxyConnectorRuleAdmin
operator|.
name|deleteProxyConnectorRule
argument_list|(
name|rule
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|deleteProxyConnector
argument_list|(
name|proxyConnector1
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorAdmin
operator|.
name|deleteProxyConnector
argument_list|(
name|proxyConnector2
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepositoryAdmin
operator|.
name|deleteRemoteRepository
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

