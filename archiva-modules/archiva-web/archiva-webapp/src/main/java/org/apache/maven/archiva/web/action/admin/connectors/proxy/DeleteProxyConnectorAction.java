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
name|proxyconnector
operator|.
name|ProxyConnector
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

begin_comment
comment|/**  * DeleteProxyConnectorAction  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"deleteProxyConnectorAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|DeleteProxyConnectorAction
extends|extends
name|AbstractProxyConnectorAction
block|{
specifier|private
name|String
name|source
decl_stmt|;
specifier|private
name|String
name|target
decl_stmt|;
specifier|private
name|ProxyConnector
name|proxyConfig
decl_stmt|;
specifier|public
name|String
name|confirmDelete
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|this
operator|.
name|proxyConfig
operator|=
name|findProxyConnector
argument_list|(
name|source
argument_list|,
name|target
argument_list|)
expr_stmt|;
comment|// Not set? Then there is nothing to delete.
if|if
condition|(
name|this
operator|.
name|proxyConfig
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete proxy configuration, configuration with source ["
operator|+
name|source
operator|+
literal|"], and target ["
operator|+
name|target
operator|+
literal|"] does not exist."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
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
name|this
operator|.
name|proxyConfig
operator|=
name|findProxyConnector
argument_list|(
name|source
argument_list|,
name|target
argument_list|)
expr_stmt|;
comment|// Not set? Then there is nothing to delete.
if|if
condition|(
name|this
operator|.
name|proxyConfig
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete proxy configuration, configuration with source ["
operator|+
name|source
operator|+
literal|"], and target ["
operator|+
name|target
operator|+
literal|"] does not exist."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
if|if
condition|(
name|hasActionErrors
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|getProxyConnectorAdmin
argument_list|()
operator|.
name|deleteProxyConnector
argument_list|(
name|proxyConfig
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Successfully removed proxy connector ["
operator|+
name|source
operator|+
literal|" , "
operator|+
name|target
operator|+
literal|" ]"
argument_list|)
expr_stmt|;
name|setSource
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setTarget
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
specifier|public
name|void
name|setSource
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
specifier|public
name|void
name|setTarget
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|ProxyConnector
name|getProxyConfig
parameter_list|()
block|{
return|return
name|proxyConfig
return|;
block|}
block|}
end_class

end_unit

