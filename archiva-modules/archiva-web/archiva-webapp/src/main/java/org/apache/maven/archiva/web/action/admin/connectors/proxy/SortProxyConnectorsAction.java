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
comment|/**  * SortProxyConnectorsAction -  *  * @version $Id$  *<p/>  *          plexus.component role="com.opensymphony.xwork2.Action" role-hint="sortProxyConnectorsAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"sortProxyConnectorsAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|SortProxyConnectorsAction
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
name|String
name|sortDown
parameter_list|()
block|{
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|connectors
init|=
name|createProxyConnectorMap
argument_list|()
operator|.
name|get
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|findTargetConnector
argument_list|(
name|connectors
argument_list|,
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|incrementConnectorOrder
argument_list|(
name|connectors
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|decrementConnectorOrder
argument_list|(
name|connectors
argument_list|,
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
specifier|public
name|String
name|sortUp
parameter_list|()
block|{
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|connectors
init|=
name|createProxyConnectorMap
argument_list|()
operator|.
name|get
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|findTargetConnector
argument_list|(
name|connectors
argument_list|,
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|decrementConnectorOrder
argument_list|(
name|connectors
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|incrementConnectorOrder
argument_list|(
name|connectors
argument_list|,
name|idx
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
specifier|private
name|void
name|decrementConnectorOrder
parameter_list|(
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|connectors
parameter_list|,
name|int
name|idx
parameter_list|)
block|{
if|if
condition|(
operator|!
name|validIndex
argument_list|(
name|connectors
argument_list|,
name|idx
argument_list|)
condition|)
block|{
comment|// Do nothing.
return|return;
block|}
name|int
name|order
init|=
name|connectors
operator|.
name|get
argument_list|(
name|idx
argument_list|)
operator|.
name|getOrder
argument_list|()
decl_stmt|;
name|connectors
operator|.
name|get
argument_list|(
name|idx
argument_list|)
operator|.
name|setOrder
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|order
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
name|findTargetConnector
parameter_list|(
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|connectors
parameter_list|,
name|String
name|targetRepoId
parameter_list|)
block|{
name|int
name|idx
init|=
operator|(
operator|-
literal|1
operator|)
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
name|connectors
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|targetRepoId
argument_list|,
name|connectors
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
condition|)
block|{
name|idx
operator|=
name|i
expr_stmt|;
break|break;
block|}
block|}
return|return
name|idx
return|;
block|}
specifier|private
name|void
name|incrementConnectorOrder
parameter_list|(
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|connectors
parameter_list|,
name|int
name|idx
parameter_list|)
block|{
if|if
condition|(
operator|!
name|validIndex
argument_list|(
name|connectors
argument_list|,
name|idx
argument_list|)
condition|)
block|{
comment|// Do nothing.
return|return;
block|}
name|int
name|order
init|=
name|connectors
operator|.
name|get
argument_list|(
name|idx
argument_list|)
operator|.
name|getOrder
argument_list|()
decl_stmt|;
name|connectors
operator|.
name|get
argument_list|(
name|idx
argument_list|)
operator|.
name|setOrder
argument_list|(
name|order
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|validIndex
parameter_list|(
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|connectors
parameter_list|,
name|int
name|idx
parameter_list|)
block|{
return|return
operator|(
name|idx
operator|>=
literal|0
operator|)
operator|&&
operator|(
name|idx
operator|<
name|connectors
operator|.
name|size
argument_list|()
operator|)
return|;
block|}
block|}
end_class

end_unit

