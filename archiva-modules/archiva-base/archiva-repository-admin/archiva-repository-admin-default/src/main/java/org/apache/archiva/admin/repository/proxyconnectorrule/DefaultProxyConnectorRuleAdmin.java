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
name|AuditInformation
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
name|proxyconnectorrule
operator|.
name|ProxyConnectorRuleAdmin
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
name|AbstractRepositoryAdmin
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
name|ProxyConnectorRuleConfiguration
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
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"proxyConnectorRuleAdmin#default"
argument_list|)
specifier|public
class|class
name|DefaultProxyConnectorRuleAdmin
extends|extends
name|AbstractRepositoryAdmin
implements|implements
name|ProxyConnectorRuleAdmin
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
name|List
argument_list|<
name|ProxyConnectorRule
argument_list|>
name|getProxyConnectorRules
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|ProxyConnectorRuleConfiguration
argument_list|>
name|proxyConnectorRuleConfigurations
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectorRuleConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
name|proxyConnectorRuleConfigurations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|ProxyConnectorRule
argument_list|>
name|proxyConnectorRules
init|=
operator|new
name|ArrayList
argument_list|<
name|ProxyConnectorRule
argument_list|>
argument_list|(
name|proxyConnectorRuleConfigurations
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ProxyConnectorRuleConfiguration
name|proxyConnectorRuleConfiguration
range|:
name|proxyConnectorRuleConfigurations
control|)
block|{
name|ProxyConnectorRule
name|proxyConnectorRule
init|=
operator|new
name|ProxyConnectorRule
argument_list|()
decl_stmt|;
name|proxyConnectorRule
operator|.
name|setPattern
argument_list|(
name|proxyConnectorRuleConfiguration
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorRule
operator|.
name|setProxyConnectorRuleType
argument_list|(
name|getProxyConnectorRuleType
argument_list|(
name|proxyConnectorRuleConfiguration
operator|.
name|getRuleType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
range|:
name|proxyConnectorRuleConfiguration
operator|.
name|getProxyConnectors
argument_list|()
control|)
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
name|proxyConnectorConfiguration
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setTargetRepoId
argument_list|(
name|proxyConnectorConfiguration
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorRule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|add
argument_list|(
name|proxyConnector
argument_list|)
expr_stmt|;
block|}
name|proxyConnectorRules
operator|.
name|add
argument_list|(
name|proxyConnectorRule
argument_list|)
expr_stmt|;
block|}
return|return
name|proxyConnectorRules
return|;
block|}
specifier|private
name|ProxyConnectorRuleType
name|getProxyConnectorRuleType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|ProxyConnectorRuleType
operator|.
name|WHITE_LIST
operator|.
name|getRuleType
argument_list|()
argument_list|,
name|type
argument_list|)
condition|)
block|{
return|return
name|ProxyConnectorRuleType
operator|.
name|WHITE_LIST
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|ProxyConnectorRuleType
operator|.
name|BLACK_LIST
operator|.
name|getRuleType
argument_list|()
argument_list|,
name|type
argument_list|)
condition|)
block|{
return|return
name|ProxyConnectorRuleType
operator|.
name|BLACK_LIST
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|addProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|ProxyConnectorRuleConfiguration
name|proxyConnectorRuleConfiguration
init|=
operator|new
name|ProxyConnectorRuleConfiguration
argument_list|()
decl_stmt|;
name|proxyConnectorRuleConfiguration
operator|.
name|setPattern
argument_list|(
name|proxyConnectorRule
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorRuleConfiguration
operator|.
name|setRuleType
argument_list|(
name|proxyConnectorRule
operator|.
name|getProxyConnectorRuleType
argument_list|()
operator|.
name|getRuleType
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ProxyConnector
name|proxyConnector
range|:
name|proxyConnectorRule
operator|.
name|getProxyConnectors
argument_list|()
control|)
block|{
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
init|=
operator|new
name|ProxyConnectorConfiguration
argument_list|()
decl_stmt|;
name|proxyConnectorConfiguration
operator|.
name|setSourceRepoId
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorConfiguration
operator|.
name|setTargetRepoId
argument_list|(
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorRuleConfiguration
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|add
argument_list|(
name|proxyConnectorConfiguration
argument_list|)
expr_stmt|;
block|}
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|getProxyConnectorRuleConfigurations
argument_list|()
operator|.
name|add
argument_list|(
name|proxyConnectorRuleConfiguration
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleteProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|// key is the pattern !!
comment|// recreate a list without the pattern
name|boolean
name|toSave
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|ProxyConnectorRuleConfiguration
argument_list|>
name|proxyConnectorRuleConfigurations
init|=
operator|new
name|ArrayList
argument_list|<
name|ProxyConnectorRuleConfiguration
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProxyConnectorRuleConfiguration
name|proxyConnectorRuleConfiguration
range|:
name|configuration
operator|.
name|getProxyConnectorRuleConfigurations
argument_list|()
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|proxyConnectorRuleConfiguration
operator|.
name|getPattern
argument_list|()
argument_list|,
name|proxyConnectorRule
operator|.
name|getPattern
argument_list|()
argument_list|)
condition|)
block|{
name|toSave
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|proxyConnectorRuleConfigurations
operator|.
name|add
argument_list|(
name|proxyConnectorRuleConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|toSave
condition|)
block|{
name|configuration
operator|.
name|setProxyConnectorRuleConfigurations
argument_list|(
name|proxyConnectorRuleConfigurations
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|updateProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
for|for
control|(
name|ProxyConnectorRuleConfiguration
name|proxyConnectorRuleConfiguration
range|:
name|configuration
operator|.
name|getProxyConnectorRuleConfigurations
argument_list|()
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|proxyConnectorRuleConfiguration
operator|.
name|getPattern
argument_list|()
argument_list|,
name|proxyConnectorRule
operator|.
name|getPattern
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|proxyConnectors
init|=
operator|new
name|ArrayList
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
argument_list|(
name|proxyConnectorRule
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ProxyConnector
name|proxyConnector
range|:
name|proxyConnectorRule
operator|.
name|getProxyConnectors
argument_list|()
control|)
block|{
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
init|=
operator|new
name|ProxyConnectorConfiguration
argument_list|()
decl_stmt|;
name|proxyConnectorConfiguration
operator|.
name|setSourceRepoId
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectorConfiguration
operator|.
name|setTargetRepoId
argument_list|(
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnectors
operator|.
name|add
argument_list|(
name|proxyConnectorConfiguration
argument_list|)
expr_stmt|;
block|}
name|proxyConnectorRuleConfiguration
operator|.
name|setProxyConnectors
argument_list|(
name|proxyConnectors
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

