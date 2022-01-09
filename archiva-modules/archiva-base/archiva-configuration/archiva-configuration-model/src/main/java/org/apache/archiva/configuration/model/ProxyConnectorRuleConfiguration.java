begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Class ProxyConnectorRuleConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|ProxyConnectorRuleConfiguration
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      *       *             The type if this rule: whiteList, blackList      * etc..      *                 */
specifier|private
name|String
name|ruleType
decl_stmt|;
comment|/**      *       *             The pattern for this rule: whiteList, blackList      * etc..      *                 */
specifier|private
name|String
name|pattern
decl_stmt|;
comment|/**      * Field proxyConnectors.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|proxyConnectors
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method addProxyConnector.      *       * @param proxyConnectorConfiguration      */
specifier|public
name|void
name|addProxyConnector
parameter_list|(
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
parameter_list|)
block|{
name|getProxyConnectors
argument_list|()
operator|.
name|add
argument_list|(
name|proxyConnectorConfiguration
argument_list|)
expr_stmt|;
block|}
comment|//-- void addProxyConnector( ProxyConnectorConfiguration )
comment|/**      * Get the pattern for this rule: whiteList, blackList etc..      *       * @return String      */
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|this
operator|.
name|pattern
return|;
block|}
comment|//-- String getPattern()
comment|/**      * Method getProxyConnectors.      *       * @return List      */
specifier|public
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|ProxyConnectorConfiguration
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
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|proxyConnectors
return|;
block|}
comment|//-- java.util.List<ProxyConnectorConfiguration> getProxyConnectors()
comment|/**      * Get the type if this rule: whiteList, blackList etc..      *       * @return String      */
specifier|public
name|String
name|getRuleType
parameter_list|()
block|{
return|return
name|this
operator|.
name|ruleType
return|;
block|}
comment|//-- String getRuleType()
comment|/**      * Method removeProxyConnector.      *       * @param proxyConnectorConfiguration      */
specifier|public
name|void
name|removeProxyConnector
parameter_list|(
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
parameter_list|)
block|{
name|getProxyConnectors
argument_list|()
operator|.
name|remove
argument_list|(
name|proxyConnectorConfiguration
argument_list|)
expr_stmt|;
block|}
comment|//-- void removeProxyConnector( ProxyConnectorConfiguration )
comment|/**      * Set the pattern for this rule: whiteList, blackList etc..      *       * @param pattern      */
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
comment|//-- void setPattern( String )
comment|/**      * Set associated proxyConnectors configuration.      *       * @param proxyConnectors      */
specifier|public
name|void
name|setProxyConnectors
parameter_list|(
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|ProxyConnectorConfiguration
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
comment|//-- void setProxyConnectors( java.util.List )
comment|/**      * Set the type if this rule: whiteList, blackList etc..      *       * @param ruleType      */
specifier|public
name|void
name|setRuleType
parameter_list|(
name|String
name|ruleType
parameter_list|)
block|{
name|this
operator|.
name|ruleType
operator|=
name|ruleType
expr_stmt|;
block|}
comment|//-- void setRuleType( String )
block|}
end_class

end_unit

