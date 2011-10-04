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
name|networkproxies
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
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Validateable
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
name|security
operator|.
name|common
operator|.
name|ArchivaRoleConstants
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
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|AbstractActionSupport
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
name|rbac
operator|.
name|Resource
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
name|SecureAction
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
name|redback
operator|.
name|integration
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
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
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
name|Controller
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

begin_comment
comment|/**  * ConfigureNetworkProxyAction  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"configureNetworkProxyAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|ConfigureNetworkProxyAction
extends|extends
name|AbstractActionSupport
implements|implements
name|SecureAction
implements|,
name|Preparable
implements|,
name|Validateable
block|{
annotation|@
name|Inject
specifier|private
name|NetworkProxyAdmin
name|networkProxyAdmin
decl_stmt|;
specifier|private
name|String
name|mode
decl_stmt|;
specifier|private
name|String
name|proxyid
decl_stmt|;
specifier|private
name|NetworkProxy
name|proxy
decl_stmt|;
specifier|public
name|String
name|add
parameter_list|()
block|{
name|this
operator|.
name|mode
operator|=
literal|"add"
expr_stmt|;
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|confirm
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|delete
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|String
name|id
init|=
name|getProxyid
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete network proxy with blank id."
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
name|NetworkProxy
name|networkProxy
init|=
name|getNetworkProxyAdmin
argument_list|()
operator|.
name|getNetworkProxy
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|networkProxy
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to remove network proxy, proxy with id ["
operator|+
name|id
operator|+
literal|"] not found."
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
name|getNetworkProxyAdmin
argument_list|()
operator|.
name|deleteNetworkProxy
argument_list|(
name|id
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Successfully removed network proxy ["
operator|+
name|id
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|edit
parameter_list|()
block|{
name|this
operator|.
name|mode
operator|=
literal|"edit"
expr_stmt|;
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
specifier|public
name|NetworkProxy
name|getProxy
parameter_list|()
block|{
return|return
name|proxy
return|;
block|}
specifier|public
name|String
name|getProxyid
parameter_list|()
block|{
return|return
name|proxyid
return|;
block|}
specifier|public
name|SecureActionBundle
name|getSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|SecureActionBundle
name|bundle
init|=
operator|new
name|SecureActionBundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|setRequiresAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|addRequiredAuthorization
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|,
name|Resource
operator|.
name|GLOBAL
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
specifier|public
name|String
name|input
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|getProxyid
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|proxy
operator|=
name|findNetworkProxy
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|proxy
operator|==
literal|null
condition|)
block|{
name|proxy
operator|=
operator|new
name|NetworkProxy
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|save
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|String
name|mode
init|=
name|getMode
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|getProxy
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"edit"
argument_list|,
name|mode
argument_list|)
condition|)
block|{
name|getNetworkProxyAdmin
argument_list|()
operator|.
name|updateNetworkProxy
argument_list|(
name|proxy
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getNetworkProxyAdmin
argument_list|()
operator|.
name|addNetworkProxy
argument_list|(
name|proxy
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
comment|// trim all unecessary trailing/leading white-spaces; always put this statement before the closing braces(after all validation).
name|trimAllRequestParameterValues
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
specifier|public
name|void
name|setProxy
parameter_list|(
name|NetworkProxy
name|proxy
parameter_list|)
block|{
name|this
operator|.
name|proxy
operator|=
name|proxy
expr_stmt|;
block|}
specifier|public
name|void
name|setProxyid
parameter_list|(
name|String
name|proxyid
parameter_list|)
block|{
name|this
operator|.
name|proxyid
operator|=
name|proxyid
expr_stmt|;
block|}
specifier|private
name|NetworkProxy
name|findNetworkProxy
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
name|getNetworkProxyAdmin
argument_list|()
operator|.
name|getNetworkProxy
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|private
name|void
name|trimAllRequestParameterValues
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|proxy
operator|.
name|setId
argument_list|(
name|proxy
operator|.
name|getId
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getHost
argument_list|()
argument_list|)
condition|)
block|{
name|proxy
operator|.
name|setHost
argument_list|(
name|proxy
operator|.
name|getHost
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getPassword
argument_list|()
argument_list|)
condition|)
block|{
name|proxy
operator|.
name|setPassword
argument_list|(
name|proxy
operator|.
name|getPassword
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|proxy
operator|.
name|setProtocol
argument_list|(
name|proxy
operator|.
name|getProtocol
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getUsername
argument_list|()
argument_list|)
condition|)
block|{
name|proxy
operator|.
name|setUsername
argument_list|(
name|proxy
operator|.
name|getUsername
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|NetworkProxyAdmin
name|getNetworkProxyAdmin
parameter_list|()
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

