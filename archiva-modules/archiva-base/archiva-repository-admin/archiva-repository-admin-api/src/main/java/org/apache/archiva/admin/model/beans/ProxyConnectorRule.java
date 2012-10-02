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
name|model
operator|.
name|beans
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"proxyConnectorRule"
argument_list|)
specifier|public
class|class
name|ProxyConnectorRule
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|pattern
decl_stmt|;
comment|//FIXME: olamy possible tru rest ? or a String
specifier|private
name|ProxyConnectorRuleType
name|proxyConnectorRuleType
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|proxyConnectors
decl_stmt|;
specifier|public
name|ProxyConnectorRule
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ProxyConnectorRule
parameter_list|(
name|String
name|pattern
parameter_list|,
name|ProxyConnectorRuleType
name|proxyConnectorRuleType
parameter_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|proxyConnectors
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
name|this
operator|.
name|proxyConnectorRuleType
operator|=
name|proxyConnectorRuleType
expr_stmt|;
name|this
operator|.
name|proxyConnectors
operator|=
name|proxyConnectors
expr_stmt|;
block|}
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
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
name|ProxyConnectorRuleType
name|getProxyConnectorRuleType
parameter_list|()
block|{
return|return
name|proxyConnectorRuleType
return|;
block|}
specifier|public
name|void
name|setProxyConnectorRuleType
parameter_list|(
name|ProxyConnectorRuleType
name|proxyConnectorRuleType
parameter_list|)
block|{
name|this
operator|.
name|proxyConnectorRuleType
operator|=
name|proxyConnectorRuleType
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|proxyConnectors
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|proxyConnectors
operator|=
operator|new
name|ArrayList
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|proxyConnectors
return|;
block|}
specifier|public
name|void
name|setProxyConnectors
parameter_list|(
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|proxyConnectors
parameter_list|)
block|{
name|this
operator|.
name|proxyConnectors
operator|=
name|proxyConnectors
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|ProxyConnectorRule
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ProxyConnectorRule
name|that
init|=
operator|(
name|ProxyConnectorRule
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|pattern
operator|.
name|equals
argument_list|(
name|that
operator|.
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|proxyConnectorRuleType
operator|!=
name|that
operator|.
name|proxyConnectorRuleType
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|pattern
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|proxyConnectorRuleType
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
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
literal|"ProxyConnectorRule"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{pattern='"
argument_list|)
operator|.
name|append
argument_list|(
name|pattern
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", proxyConnectorRuleType="
argument_list|)
operator|.
name|append
argument_list|(
name|proxyConnectorRuleType
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", proxyConnectors="
argument_list|)
operator|.
name|append
argument_list|(
name|proxyConnectors
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

