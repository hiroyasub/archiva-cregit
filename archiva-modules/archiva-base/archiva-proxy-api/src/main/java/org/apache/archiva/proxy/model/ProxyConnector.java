begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|model
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
name|repository
operator|.
name|ManagedRepositoryContent
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
name|repository
operator|.
name|RemoteRepositoryContent
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
name|repository
operator|.
name|connector
operator|.
name|RepositoryConnector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * This represents a connector for a repository to repository proxy.  */
end_comment

begin_class
specifier|public
class|class
name|ProxyConnector
implements|implements
name|RepositoryConnector
block|{
specifier|private
name|ManagedRepositoryContent
name|sourceRepository
decl_stmt|;
specifier|private
name|RemoteRepositoryContent
name|targetRepository
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|blacklist
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|whitelist
decl_stmt|;
specifier|private
name|String
name|proxyId
decl_stmt|;
specifier|private
name|int
name|order
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|policies
decl_stmt|;
specifier|private
name|boolean
name|disabled
decl_stmt|;
specifier|public
name|ProxyConnector
parameter_list|()
block|{
comment|// no op
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
name|disabled
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDisabled
parameter_list|(
name|boolean
name|disabled
parameter_list|)
block|{
name|this
operator|.
name|disabled
operator|=
name|disabled
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBlacklist
parameter_list|()
block|{
return|return
name|blacklist
return|;
block|}
specifier|public
name|void
name|setBlacklist
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|blacklist
parameter_list|)
block|{
name|this
operator|.
name|blacklist
operator|=
name|blacklist
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepositoryContent
name|getSourceRepository
parameter_list|()
block|{
return|return
name|sourceRepository
return|;
block|}
specifier|public
name|void
name|setSourceRepository
parameter_list|(
name|ManagedRepositoryContent
name|sourceRepository
parameter_list|)
block|{
name|this
operator|.
name|sourceRepository
operator|=
name|sourceRepository
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteRepositoryContent
name|getTargetRepository
parameter_list|()
block|{
return|return
name|targetRepository
return|;
block|}
specifier|public
name|void
name|setTargetRepository
parameter_list|(
name|RemoteRepositoryContent
name|targetRepository
parameter_list|)
block|{
name|this
operator|.
name|targetRepository
operator|=
name|targetRepository
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getWhitelist
parameter_list|()
block|{
return|return
name|whitelist
return|;
block|}
specifier|public
name|void
name|setWhitelist
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|whitelist
parameter_list|)
block|{
name|this
operator|.
name|whitelist
operator|=
name|whitelist
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPolicies
parameter_list|()
block|{
return|return
name|policies
return|;
block|}
specifier|public
name|void
name|setPolicies
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|policies
parameter_list|)
block|{
name|this
operator|.
name|policies
operator|=
name|policies
expr_stmt|;
block|}
specifier|public
name|String
name|getProxyId
parameter_list|()
block|{
return|return
name|proxyId
return|;
block|}
specifier|public
name|void
name|setProxyId
parameter_list|(
name|String
name|proxyId
parameter_list|)
block|{
name|this
operator|.
name|proxyId
operator|=
name|proxyId
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"ProxyConnector[\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"  source: [managed] "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|sourceRepository
operator|.
name|getRepoRoot
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"  target: [remote] "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|targetRepository
operator|.
name|getRepository
argument_list|()
operator|.
name|getLocation
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"  proxyId:"
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|proxyId
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|this
operator|.
name|policies
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|keys
operator|.
name|next
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"  policy["
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"]:"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|this
operator|.
name|policies
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|setPolicy
parameter_list|(
name|String
name|policyId
parameter_list|,
name|String
name|policySetting
parameter_list|)
block|{
name|this
operator|.
name|policies
operator|.
name|put
argument_list|(
name|policyId
argument_list|,
name|policySetting
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
return|return
name|order
return|;
block|}
specifier|public
name|void
name|setOrder
parameter_list|(
name|int
name|order
parameter_list|)
block|{
name|this
operator|.
name|order
operator|=
name|order
expr_stmt|;
block|}
block|}
end_class

end_unit

