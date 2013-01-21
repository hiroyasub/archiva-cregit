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
comment|/**  * @author Olivier Lamy  * @since 1.4-M4  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"redbackRuntimeConfiguration"
argument_list|)
specifier|public
class|class
name|RedbackRuntimeConfiguration
implements|implements
name|Serializable
block|{
comment|/**      * Field userManagerImpls.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|userManagerImpls
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Field rbacManagerImpls.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|rbacManagerImpls
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|LdapConfiguration
name|ldapConfiguration
decl_stmt|;
comment|/**      * flag to know if redback configuration has been checked/migrated.      */
specifier|private
name|boolean
name|migratedFromRedbackConfiguration
init|=
literal|false
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|configurationProperties
decl_stmt|;
comment|/**      * field to ease json mapping wrapper on<code>configurationProperties</code> field      */
specifier|private
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|configurationPropertiesEntries
decl_stmt|;
comment|/**      * flag to know if redback will use a cache to prevent      * searching users already found.      */
specifier|private
name|boolean
name|useUsersCache
init|=
literal|true
decl_stmt|;
specifier|private
name|CacheConfiguration
name|usersCacheConfiguration
decl_stmt|;
specifier|public
name|RedbackRuntimeConfiguration
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getUserManagerImpls
parameter_list|()
block|{
return|return
name|userManagerImpls
return|;
block|}
specifier|public
name|void
name|setUserManagerImpls
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|userManagerImpls
parameter_list|)
block|{
name|this
operator|.
name|userManagerImpls
operator|=
name|userManagerImpls
expr_stmt|;
block|}
specifier|public
name|LdapConfiguration
name|getLdapConfiguration
parameter_list|()
block|{
return|return
name|ldapConfiguration
return|;
block|}
specifier|public
name|void
name|setLdapConfiguration
parameter_list|(
name|LdapConfiguration
name|ldapConfiguration
parameter_list|)
block|{
name|this
operator|.
name|ldapConfiguration
operator|=
name|ldapConfiguration
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMigratedFromRedbackConfiguration
parameter_list|()
block|{
return|return
name|migratedFromRedbackConfiguration
return|;
block|}
specifier|public
name|void
name|setMigratedFromRedbackConfiguration
parameter_list|(
name|boolean
name|migratedFromRedbackConfiguration
parameter_list|)
block|{
name|this
operator|.
name|migratedFromRedbackConfiguration
operator|=
name|migratedFromRedbackConfiguration
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getConfigurationProperties
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|configurationProperties
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|configurationProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|configurationProperties
return|;
block|}
specifier|public
name|void
name|setConfigurationProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|configurationProperties
parameter_list|)
block|{
name|this
operator|.
name|configurationProperties
operator|=
name|configurationProperties
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|getConfigurationPropertiesEntries
parameter_list|()
block|{
name|configurationPropertiesEntries
operator|=
operator|new
name|ArrayList
argument_list|<
name|PropertyEntry
argument_list|>
argument_list|(
name|getConfigurationProperties
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
name|getConfigurationProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|configurationPropertiesEntries
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
name|configurationPropertiesEntries
return|;
block|}
specifier|public
name|void
name|setConfigurationPropertiesEntries
parameter_list|(
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|configurationPropertiesEntries
parameter_list|)
block|{
name|this
operator|.
name|configurationPropertiesEntries
operator|=
name|configurationPropertiesEntries
expr_stmt|;
if|if
condition|(
name|configurationPropertiesEntries
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|configurationProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|configurationPropertiesEntries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PropertyEntry
name|propertyEntry
range|:
name|configurationPropertiesEntries
control|)
block|{
name|this
operator|.
name|configurationProperties
operator|.
name|put
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
block|}
specifier|public
name|boolean
name|isUseUsersCache
parameter_list|()
block|{
return|return
name|useUsersCache
return|;
block|}
specifier|public
name|void
name|setUseUsersCache
parameter_list|(
name|boolean
name|useUsersCache
parameter_list|)
block|{
name|this
operator|.
name|useUsersCache
operator|=
name|useUsersCache
expr_stmt|;
block|}
specifier|public
name|CacheConfiguration
name|getUsersCacheConfiguration
parameter_list|()
block|{
return|return
name|usersCacheConfiguration
return|;
block|}
specifier|public
name|void
name|setUsersCacheConfiguration
parameter_list|(
name|CacheConfiguration
name|usersCacheConfiguration
parameter_list|)
block|{
name|this
operator|.
name|usersCacheConfiguration
operator|=
name|usersCacheConfiguration
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRbacManagerImpls
parameter_list|()
block|{
return|return
name|rbacManagerImpls
return|;
block|}
specifier|public
name|void
name|setRbacManagerImpls
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|rbacManagerImpls
parameter_list|)
block|{
name|this
operator|.
name|rbacManagerImpls
operator|=
name|rbacManagerImpls
expr_stmt|;
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
literal|"RedbackRuntimeConfiguration"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{userManagerImpls="
argument_list|)
operator|.
name|append
argument_list|(
name|userManagerImpls
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", rbacManagerImpls="
argument_list|)
operator|.
name|append
argument_list|(
name|rbacManagerImpls
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", ldapConfiguration="
argument_list|)
operator|.
name|append
argument_list|(
name|ldapConfiguration
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", migratedFromRedbackConfiguration="
argument_list|)
operator|.
name|append
argument_list|(
name|migratedFromRedbackConfiguration
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", configurationProperties="
argument_list|)
operator|.
name|append
argument_list|(
name|configurationProperties
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", configurationPropertiesEntries="
argument_list|)
operator|.
name|append
argument_list|(
name|configurationPropertiesEntries
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", useUsersCache="
argument_list|)
operator|.
name|append
argument_list|(
name|useUsersCache
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", usersCacheConfiguration="
argument_list|)
operator|.
name|append
argument_list|(
name|usersCacheConfiguration
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

