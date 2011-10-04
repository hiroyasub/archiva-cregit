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
name|Preparable
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
name|NetworkProxy
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
name|networkproxy
operator|.
name|NetworkProxyAdmin
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
name|policies
operator|.
name|DownloadErrorPolicy
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
name|policies
operator|.
name|Policy
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
name|policies
operator|.
name|PostDownloadPolicy
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
name|policies
operator|.
name|PreDownloadPolicy
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
name|StringEscapeUtils
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
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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
name|Collection
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
name|HashMap
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
comment|/**  * AbstractProxyConnectorFormAction - generic fields and methods for either add or edit actions related with the  * Proxy Connector.  *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractProxyConnectorFormAction
extends|extends
name|AbstractProxyConnectorAction
implements|implements
name|Preparable
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PreDownloadPolicy
argument_list|>
name|preDownloadPolicyMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PostDownloadPolicy
argument_list|>
name|postDownloadPolicyMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DownloadErrorPolicy
argument_list|>
name|downloadErrorPolicyMap
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|proxyIdOptions
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|managedRepoIdList
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|remoteRepoIdList
decl_stmt|;
comment|/**      * The map of policies that are available to be set.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Policy
argument_list|>
name|policyMap
decl_stmt|;
comment|/**      * The property key to add or remove.      */
specifier|private
name|String
name|propertyKey
decl_stmt|;
comment|/**      * The property value to add.      */
specifier|private
name|String
name|propertyValue
decl_stmt|;
comment|/**      * The blacklist pattern to add.      */
specifier|private
name|String
name|blackListPattern
decl_stmt|;
comment|/**      * The whitelist pattern to add.      */
specifier|private
name|String
name|whiteListPattern
decl_stmt|;
comment|/**      * The pattern to add or remove (black or white).      */
specifier|private
name|String
name|pattern
decl_stmt|;
comment|/**      * The model for this action.      */
specifier|protected
name|ProxyConnector
name|connector
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|NetworkProxyAdmin
name|networkProxyAdmin
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|( )
block|{
name|super
operator|.
name|initialize
argument_list|( )
expr_stmt|;
name|this
operator|.
name|preDownloadPolicyMap
operator|=
name|getBeansOfType
argument_list|(
name|PreDownloadPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|postDownloadPolicyMap
operator|=
name|getBeansOfType
argument_list|(
name|PostDownloadPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|downloadErrorPolicyMap
operator|=
name|getBeansOfType
argument_list|(
name|DownloadErrorPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|addBlackListPattern
parameter_list|( )
block|{
name|String
name|pattern
init|=
name|getBlackListPattern
argument_list|( )
decl_stmt|;
comment|//pattern = StringEscapeUtils.unescapeJavaScript( pattern );
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Cannot add a blank black list pattern."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasActionErrors
argument_list|( )
condition|)
block|{
name|getConnector
argument_list|( )
operator|.
name|getBlackListPatterns
argument_list|( )
operator|.
name|add
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|setBlackListPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|INPUT
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|String
name|addProperty
parameter_list|( )
block|{
name|String
name|key
init|=
name|getPropertyKey
argument_list|( )
decl_stmt|;
name|String
name|value
init|=
name|getPropertyValue
argument_list|( )
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to add property with blank key."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to add property with blank value."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasActionErrors
argument_list|( )
condition|)
block|{
name|getConnector
argument_list|( )
operator|.
name|getProperties
argument_list|( )
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|setPropertyKey
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setPropertyValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|addWhiteListPattern
parameter_list|( )
block|{
name|String
name|pattern
init|=
name|getWhiteListPattern
argument_list|( )
decl_stmt|;
comment|//pattern = StringEscapeUtils.unescapeJavaScript( pattern );
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Cannot add a blank white list pattern."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasActionErrors
argument_list|( )
condition|)
block|{
name|getConnector
argument_list|( )
operator|.
name|getWhiteListPatterns
argument_list|( )
operator|.
name|add
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|setWhiteListPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|getBlackListPattern
parameter_list|( )
block|{
return|return
name|blackListPattern
return|;
block|}
specifier|public
name|ProxyConnector
name|getConnector
parameter_list|( )
block|{
return|return
name|connector
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getManagedRepoIdList
parameter_list|( )
block|{
return|return
name|managedRepoIdList
return|;
block|}
specifier|public
name|String
name|getPattern
parameter_list|( )
block|{
return|return
name|pattern
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Policy
argument_list|>
name|getPolicyMap
parameter_list|( )
block|{
return|return
name|policyMap
return|;
block|}
specifier|public
name|String
name|getPropertyKey
parameter_list|( )
block|{
return|return
name|propertyKey
return|;
block|}
specifier|public
name|String
name|getPropertyValue
parameter_list|( )
block|{
return|return
name|propertyValue
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getProxyIdOptions
parameter_list|( )
block|{
return|return
name|proxyIdOptions
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRemoteRepoIdList
parameter_list|( )
block|{
return|return
name|remoteRepoIdList
return|;
block|}
specifier|public
name|String
name|getWhiteListPattern
parameter_list|( )
block|{
return|return
name|whiteListPattern
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|( )
throws|throws
name|RepositoryAdminException
block|{
name|proxyIdOptions
operator|=
name|createNetworkProxyOptions
argument_list|( )
expr_stmt|;
name|managedRepoIdList
operator|=
name|createManagedRepoOptions
argument_list|( )
expr_stmt|;
name|remoteRepoIdList
operator|=
name|createRemoteRepoOptions
argument_list|( )
expr_stmt|;
name|policyMap
operator|=
name|createPolicyMap
argument_list|( )
expr_stmt|;
block|}
specifier|public
name|String
name|removeBlackListPattern
parameter_list|( )
block|{
name|String
name|pattern
init|=
name|getPattern
argument_list|( )
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Cannot remove a blank black list pattern."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getConnector
argument_list|( )
operator|.
name|getBlackListPatterns
argument_list|( )
operator|.
name|contains
argument_list|(
name|pattern
argument_list|)
operator|&&
operator|!
name|getConnector
argument_list|( )
operator|.
name|getBlackListPatterns
argument_list|( )
operator|.
name|contains
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Non-existant black list pattern ["
operator|+
name|pattern
operator|+
literal|"], no black list pattern removed."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasActionErrors
argument_list|( )
condition|)
block|{
name|getConnector
argument_list|( )
operator|.
name|getBlackListPatterns
argument_list|( )
operator|.
name|remove
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
name|setBlackListPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|removeProperty
parameter_list|( )
block|{
name|String
name|key
init|=
name|getPropertyKey
argument_list|( )
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to remove property with blank key."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getConnector
argument_list|( )
operator|.
name|getProperties
argument_list|( )
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Non-existant property key ["
operator|+
name|pattern
operator|+
literal|"], no property was removed."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasActionErrors
argument_list|( )
condition|)
block|{
name|getConnector
argument_list|( )
operator|.
name|getProperties
argument_list|( )
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|setPropertyKey
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setPropertyValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|removeWhiteListPattern
parameter_list|( )
block|{
name|String
name|pattern
init|=
name|getPattern
argument_list|( )
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Cannot remove a blank white list pattern."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getConnector
argument_list|( )
operator|.
name|getWhiteListPatterns
argument_list|( )
operator|.
name|contains
argument_list|(
name|pattern
argument_list|)
operator|&&
operator|!
name|getConnector
argument_list|( )
operator|.
name|getWhiteListPatterns
argument_list|( )
operator|.
name|contains
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Non-existant white list pattern ["
operator|+
name|pattern
operator|+
literal|"], no white list pattern removed."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasActionErrors
argument_list|( )
condition|)
block|{
name|getConnector
argument_list|( )
operator|.
name|getWhiteListPatterns
argument_list|( )
operator|.
name|remove
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
name|setWhiteListPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
specifier|public
name|void
name|setBlackListPattern
parameter_list|(
name|String
name|blackListPattern
parameter_list|)
block|{
name|this
operator|.
name|blackListPattern
operator|=
name|blackListPattern
expr_stmt|;
block|}
specifier|public
name|void
name|setConnector
parameter_list|(
name|ProxyConnector
name|connector
parameter_list|)
block|{
name|this
operator|.
name|connector
operator|=
name|connector
expr_stmt|;
block|}
specifier|public
name|void
name|setManagedRepoIdList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|managedRepoIdList
parameter_list|)
block|{
name|this
operator|.
name|managedRepoIdList
operator|=
name|managedRepoIdList
expr_stmt|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
specifier|public
name|void
name|setPolicyMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Policy
argument_list|>
name|policyMap
parameter_list|)
block|{
name|this
operator|.
name|policyMap
operator|=
name|policyMap
expr_stmt|;
block|}
specifier|public
name|void
name|setPropertyKey
parameter_list|(
name|String
name|propertyKey
parameter_list|)
block|{
name|this
operator|.
name|propertyKey
operator|=
name|propertyKey
expr_stmt|;
block|}
specifier|public
name|void
name|setPropertyValue
parameter_list|(
name|String
name|propertyValue
parameter_list|)
block|{
name|this
operator|.
name|propertyValue
operator|=
name|propertyValue
expr_stmt|;
block|}
specifier|public
name|void
name|setProxyIdOptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|proxyIdOptions
parameter_list|)
block|{
name|this
operator|.
name|proxyIdOptions
operator|=
name|proxyIdOptions
expr_stmt|;
block|}
specifier|public
name|void
name|setRemoteRepoIdList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|remoteRepoIdList
parameter_list|)
block|{
name|this
operator|.
name|remoteRepoIdList
operator|=
name|remoteRepoIdList
expr_stmt|;
block|}
specifier|public
name|void
name|setWhiteListPattern
parameter_list|(
name|String
name|whiteListPattern
parameter_list|)
block|{
name|this
operator|.
name|whiteListPattern
operator|=
name|whiteListPattern
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|createManagedRepoOptions
parameter_list|( )
throws|throws
name|RepositoryAdminException
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|getManagedRepositoryAdmin
argument_list|( )
operator|.
name|getManagedRepositoriesAsMap
argument_list|( )
operator|.
name|keySet
argument_list|( )
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|createNetworkProxyOptions
parameter_list|( )
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|( )
decl_stmt|;
name|options
operator|.
name|add
argument_list|(
name|DIRECT_CONNECTION
argument_list|)
expr_stmt|;
name|options
operator|.
name|addAll
argument_list|(
name|getNetworkProxiesKeys
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|options
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|getNetworkProxiesKeys
parameter_list|( )
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|NetworkProxy
argument_list|>
name|networkProxies
init|=
name|networkProxyAdmin
operator|.
name|getNetworkProxies
argument_list|( )
decl_stmt|;
if|if
condition|(
name|networkProxies
operator|==
literal|null
operator|||
name|networkProxies
operator|.
name|isEmpty
argument_list|( )
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|( )
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|networkProxies
operator|.
name|size
argument_list|( )
argument_list|)
decl_stmt|;
for|for
control|(
name|NetworkProxy
name|networkProxy
range|:
name|networkProxies
control|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|networkProxy
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
block|}
return|return
name|keys
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Policy
argument_list|>
name|createPolicyMap
parameter_list|( )
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Policy
argument_list|>
name|policyMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Policy
argument_list|>
argument_list|( )
decl_stmt|;
name|policyMap
operator|.
name|putAll
argument_list|(
name|preDownloadPolicyMap
argument_list|)
expr_stmt|;
name|policyMap
operator|.
name|putAll
argument_list|(
name|postDownloadPolicyMap
argument_list|)
expr_stmt|;
name|policyMap
operator|.
name|putAll
argument_list|(
name|downloadErrorPolicyMap
argument_list|)
expr_stmt|;
return|return
name|policyMap
return|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|createRemoteRepoOptions
parameter_list|( )
throws|throws
name|RepositoryAdminException
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|getRemoteRepositoryAdmin
argument_list|( )
operator|.
name|getRemoteRepositoriesAsMap
argument_list|( )
operator|.
name|keySet
argument_list|( )
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|void
name|validateConnector
parameter_list|( )
block|{
if|if
condition|(
name|connector
operator|.
name|getPolicies
argument_list|( )
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"Policies must be set."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Validate / Fix policy settings arriving from browser.
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Policy
argument_list|>
name|entry
range|:
name|getPolicyMap
argument_list|( )
operator|.
name|entrySet
argument_list|( )
control|)
block|{
name|String
name|policyId
init|=
name|entry
operator|.
name|getKey
argument_list|( )
decl_stmt|;
name|Policy
name|policy
init|=
name|entry
operator|.
name|getValue
argument_list|( )
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
name|policy
operator|.
name|getOptions
argument_list|( )
decl_stmt|;
if|if
condition|(
operator|!
name|connector
operator|.
name|getPolicies
argument_list|( )
operator|.
name|containsKey
argument_list|(
name|policyId
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Policy ["
operator|+
name|policyId
operator|+
literal|"] must be set (missing id)."
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
name|connector
operator|.
name|getProperties
argument_list|( )
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry2
range|:
name|properties
operator|.
name|entrySet
argument_list|( )
control|)
block|{
name|Object
name|value
init|=
name|entry2
operator|.
name|getValue
argument_list|( )
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|( )
operator|.
name|isArray
argument_list|( )
condition|)
block|{
name|String
index|[]
name|arr
init|=
operator|(
name|String
index|[]
operator|)
name|value
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|entry2
operator|.
name|getKey
argument_list|( )
argument_list|,
name|arr
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Ugly hack to compensate for ugly browsers.
name|Object
name|o
init|=
name|connector
operator|.
name|getPolicies
argument_list|( )
operator|.
name|get
argument_list|(
name|policyId
argument_list|)
decl_stmt|;
name|String
name|value
decl_stmt|;
if|if
condition|(
name|o
operator|.
name|getClass
argument_list|( )
operator|.
name|isArray
argument_list|( )
condition|)
block|{
name|String
name|arr
index|[]
init|=
operator|(
name|String
index|[]
operator|)
name|o
decl_stmt|;
name|value
operator|=
name|arr
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
operator|(
name|String
operator|)
name|o
expr_stmt|;
block|}
name|connector
operator|.
name|getPolicies
argument_list|( )
operator|.
name|put
argument_list|(
name|policyId
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Policy ["
operator|+
name|policyId
operator|+
literal|"] must be set (missing value)."
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|options
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Value of ["
operator|+
name|value
operator|+
literal|"] is invalid for policy ["
operator|+
name|policyId
operator|+
literal|"], valid values: "
operator|+
name|options
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
block|}
specifier|public
name|NetworkProxyAdmin
name|getNetworkProxyAdmin
parameter_list|( )
block|{
return|return
name|networkProxyAdmin
return|;
block|}
specifier|public
name|void
name|setNetworkProxyAdmin
parameter_list|(
name|NetworkProxyAdmin
name|networkProxyAdmin
parameter_list|)
block|{
name|this
operator|.
name|networkProxyAdmin
operator|=
name|networkProxyAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

