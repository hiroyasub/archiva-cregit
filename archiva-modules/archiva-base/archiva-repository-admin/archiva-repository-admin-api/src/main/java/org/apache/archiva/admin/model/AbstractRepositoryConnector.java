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
name|PropertyEntry
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryConnector
implements|implements
name|Serializable
block|{
comment|/**      * The Repository Source for this connector.      */
specifier|private
name|String
name|sourceRepoId
decl_stmt|;
comment|/**      * The Repository Target for this connector.      */
specifier|private
name|String
name|targetRepoId
decl_stmt|;
comment|/**      * The network proxy ID to use for this connector.      */
specifier|private
name|String
name|proxyId
decl_stmt|;
comment|/**      * Field blackListPatterns.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|blackListPatterns
decl_stmt|;
comment|/**      * Field whiteListPatterns.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|whiteListPatterns
decl_stmt|;
comment|/**      * Field policies.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|policies
decl_stmt|;
comment|/**      * field to ease json mapping wrapper on<code>policies</code> field      *      * @since 1.4-M3      */
specifier|private
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|policiesEntries
decl_stmt|;
comment|/**      * Field properties.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
comment|/**      * field to ease json mapping wrapper on<code>properties</code> field      *      * @since 1.4-M3      */
specifier|private
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|propertiesEntries
decl_stmt|;
comment|/**      * If the the repository proxy connector is disabled or not      */
specifier|private
name|boolean
name|disabled
init|=
literal|false
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method addBlackListPattern.      *      * @param string      */
specifier|public
name|void
name|addBlackListPattern
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getBlackListPatterns
argument_list|()
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method addPolicy.      *      * @param key      * @param value      */
specifier|public
name|void
name|addPolicy
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|getPolicies
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method addProperty.      *      * @param key      * @param value      */
specifier|public
name|void
name|addProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method addWhiteListPattern.      *      * @param string      */
specifier|public
name|void
name|addWhiteListPattern
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getWhiteListPatterns
argument_list|()
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method getBlackListPatterns.      *      * @return List      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBlackListPatterns
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|blackListPatterns
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|blackListPatterns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|blackListPatterns
return|;
block|}
comment|/**      * Method getPolicies.      *      * @return Map      */
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
if|if
condition|(
name|this
operator|.
name|policies
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|policies
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|policies
return|;
block|}
comment|/**      * Method getProperties.      *      * @return Map      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|properties
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|properties
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|properties
return|;
block|}
comment|/**      * Get the network proxy ID to use for this connector.      *      * @return String      */
specifier|public
name|String
name|getProxyId
parameter_list|()
block|{
return|return
name|this
operator|.
name|proxyId
return|;
block|}
comment|/**      * Get the Repository Source for this connector.      *      * @return String      */
specifier|public
name|String
name|getSourceRepoId
parameter_list|()
block|{
return|return
name|this
operator|.
name|sourceRepoId
return|;
block|}
comment|/**      * Get the Repository Target for this connector.      *      * @return String      */
specifier|public
name|String
name|getTargetRepoId
parameter_list|()
block|{
return|return
name|this
operator|.
name|targetRepoId
return|;
block|}
comment|/**      * Method getWhiteListPatterns.      *      * @return List      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getWhiteListPatterns
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|whiteListPatterns
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|whiteListPatterns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|whiteListPatterns
return|;
block|}
comment|/**      * Get if the the repository proxy connector is disabled or not      * .      *      * @return boolean      */
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
name|this
operator|.
name|disabled
return|;
block|}
comment|/**      * Method removeBlackListPattern.      *      * @param string      */
specifier|public
name|void
name|removeBlackListPattern
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getBlackListPatterns
argument_list|()
operator|.
name|remove
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method removeWhiteListPattern.      *      * @param string      */
specifier|public
name|void
name|removeWhiteListPattern
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getWhiteListPatterns
argument_list|()
operator|.
name|remove
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the list of blacklisted patterns for this connector.      *      * @param blackListPatterns      */
specifier|public
name|void
name|setBlackListPatterns
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|blackListPatterns
parameter_list|)
block|{
name|this
operator|.
name|blackListPatterns
operator|=
name|blackListPatterns
expr_stmt|;
block|}
comment|/**      * Set if the the repository proxy connector is      * disabled or not      * .      *      * @param disabled      */
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
comment|/**      * Set policy configuration for the connector.      *      * @param policies      */
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
comment|/**      * Set configuration for the connector.      *      * @param properties      */
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
comment|/**      * Set the network proxy ID to use for this connector.      *      * @param proxyId      */
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
comment|/**      * Set the Repository Source for this connector.      *      * @param sourceRepoId      */
specifier|public
name|void
name|setSourceRepoId
parameter_list|(
name|String
name|sourceRepoId
parameter_list|)
block|{
name|this
operator|.
name|sourceRepoId
operator|=
name|sourceRepoId
expr_stmt|;
block|}
comment|/**      * Set the Repository Target for this connector.      *      * @param targetRepoId      */
specifier|public
name|void
name|setTargetRepoId
parameter_list|(
name|String
name|targetRepoId
parameter_list|)
block|{
name|this
operator|.
name|targetRepoId
operator|=
name|targetRepoId
expr_stmt|;
block|}
comment|/**      * Set      * The list of whitelisted patterns for this      * connector.      *      * @param whiteListPatterns      */
specifier|public
name|void
name|setWhiteListPatterns
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|whiteListPatterns
parameter_list|)
block|{
name|this
operator|.
name|whiteListPatterns
operator|=
name|whiteListPatterns
expr_stmt|;
block|}
comment|/**      * Obtain a specific policy from the underlying connector.      *      * @param policyId     the policy id to fetch.      * @param defaultValue the default value for the policy id.      * @return the configured policy value (or default value if not found).      */
specifier|public
name|String
name|getPolicy
parameter_list|(
name|String
name|policyId
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|getPolicies
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|value
init|=
name|this
operator|.
name|getPolicies
argument_list|()
operator|.
name|get
argument_list|(
name|policyId
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|value
return|;
block|}
specifier|public
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|getPoliciesEntries
parameter_list|()
block|{
name|policiesEntries
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getPolicies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
name|entry
range|:
name|getPolicies
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|policiesEntries
operator|.
name|add
argument_list|(
operator|new
name|PropertyEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|policiesEntries
return|;
block|}
specifier|public
name|void
name|setPoliciesEntries
parameter_list|(
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|policiesEntries
parameter_list|)
block|{
for|for
control|(
name|PropertyEntry
name|propertyEntry
range|:
name|policiesEntries
control|)
block|{
name|addPolicy
argument_list|(
name|propertyEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|propertyEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|getPropertiesEntries
parameter_list|()
block|{
name|propertiesEntries
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getProperties
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
name|entry
range|:
name|getProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|propertiesEntries
operator|.
name|add
argument_list|(
operator|new
name|PropertyEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|propertiesEntries
return|;
block|}
specifier|public
name|void
name|setPropertiesEntries
parameter_list|(
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|propertiesEntries
parameter_list|)
block|{
for|for
control|(
name|PropertyEntry
name|propertyEntry
range|:
name|propertiesEntries
control|)
block|{
name|addProperty
argument_list|(
name|propertyEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|propertyEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AbstractRepositoryConnector
name|that
init|=
operator|(
name|AbstractRepositoryConnector
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|sourceRepoId
operator|!=
literal|null
condition|?
operator|!
name|sourceRepoId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|sourceRepoId
argument_list|)
else|:
name|that
operator|.
name|sourceRepoId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|targetRepoId
operator|!=
literal|null
condition|?
operator|!
name|targetRepoId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|targetRepoId
argument_list|)
else|:
name|that
operator|.
name|targetRepoId
operator|!=
literal|null
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
name|sourceRepoId
operator|!=
literal|null
condition|?
name|sourceRepoId
operator|.
name|hashCode
argument_list|()
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|targetRepoId
operator|!=
literal|null
condition|?
name|targetRepoId
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
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
literal|"AbstractRepositoryConnector"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{sourceRepoId='"
argument_list|)
operator|.
name|append
argument_list|(
name|sourceRepoId
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
literal|", targetRepoId='"
argument_list|)
operator|.
name|append
argument_list|(
name|targetRepoId
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
literal|", proxyId='"
argument_list|)
operator|.
name|append
argument_list|(
name|proxyId
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
literal|", blackListPatterns="
argument_list|)
operator|.
name|append
argument_list|(
name|blackListPatterns
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", whiteListPatterns="
argument_list|)
operator|.
name|append
argument_list|(
name|whiteListPatterns
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", policies="
argument_list|)
operator|.
name|append
argument_list|(
name|policies
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", properties="
argument_list|)
operator|.
name|append
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", disabled="
argument_list|)
operator|.
name|append
argument_list|(
name|disabled
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

