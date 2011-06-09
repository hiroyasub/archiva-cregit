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
comment|/**  * EditProxyConnectorAction   *  * @version $Id$  *   * plexus.component role="com.opensymphony.xwork2.Action" role-hint="editProxyConnectorAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"editProxyConnectorAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|EditProxyConnectorAction
extends|extends
name|AbstractProxyConnectorFormAction
block|{
comment|/**      * The proxy connector source id to edit. (used with {@link #target})      */
specifier|private
name|String
name|source
decl_stmt|;
comment|/**      * The proxy connector target id to edit. (used with {@link #source})      */
specifier|private
name|String
name|target
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|super
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|connector
operator|=
name|findProxyConnector
argument_list|(
name|source
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|input
parameter_list|()
block|{
if|if
condition|(
name|connector
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to edit non existant proxy connector with source ["
operator|+
name|source
operator|+
literal|"] and target ["
operator|+
name|target
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
if|if
condition|(
name|connector
operator|!=
literal|null
condition|)
block|{
comment|// MRM-1135
name|connector
operator|.
name|setBlackListPatterns
argument_list|(
name|escapePatterns
argument_list|(
name|connector
operator|.
name|getBlackListPatterns
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setWhiteListPatterns
argument_list|(
name|escapePatterns
argument_list|(
name|connector
operator|.
name|getWhiteListPatterns
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|commit
parameter_list|()
block|{
name|validateConnector
argument_list|()
expr_stmt|;
if|if
condition|(
name|hasActionErrors
argument_list|()
condition|)
block|{
return|return
name|INPUT
return|;
block|}
name|String
name|sourceId
init|=
name|connector
operator|.
name|getSourceRepoId
argument_list|()
decl_stmt|;
name|String
name|targetId
init|=
name|connector
operator|.
name|getTargetRepoId
argument_list|()
decl_stmt|;
name|ProxyConnectorConfiguration
name|otherConnector
init|=
name|findProxyConnector
argument_list|(
name|sourceId
argument_list|,
name|targetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|otherConnector
operator|!=
literal|null
condition|)
block|{
comment|// Remove the previous connector.
name|removeProxyConnector
argument_list|(
name|otherConnector
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasActionErrors
argument_list|()
condition|)
block|{
return|return
name|INPUT
return|;
block|}
comment|// MRM-1135
name|connector
operator|.
name|setBlackListPatterns
argument_list|(
name|unescapePatterns
argument_list|(
name|connector
operator|.
name|getBlackListPatterns
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setWhiteListPatterns
argument_list|(
name|unescapePatterns
argument_list|(
name|connector
operator|.
name|getWhiteListPatterns
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|addProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
return|return
name|saveConfiguration
argument_list|()
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
name|source
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
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
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
block|}
end_class

end_unit

