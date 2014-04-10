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
literal|"ldapConfiguration"
argument_list|)
specifier|public
class|class
name|LdapConfiguration
implements|implements
name|Serializable
block|{
comment|/**      * The LDAP host.      */
specifier|private
name|String
name|hostName
decl_stmt|;
comment|/**      * The LDAP port.      */
specifier|private
name|int
name|port
decl_stmt|;
comment|/**      * ssl LDAP connection.      */
specifier|private
name|boolean
name|ssl
init|=
literal|false
decl_stmt|;
comment|/**      * The LDAP base dn.      */
specifier|private
name|String
name|baseDn
decl_stmt|;
comment|/**      * contextFactory to use.      */
specifier|private
name|String
name|contextFactory
decl_stmt|;
comment|/**      * The LDAP bind dn.      */
specifier|private
name|String
name|bindDn
decl_stmt|;
comment|/**      * The LDAP base dn for groups (if empty baseDn is used).      */
specifier|private
name|String
name|baseGroupsDn
decl_stmt|;
comment|/**      * The LDAP password.      */
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * The LDAP authenticationMethod.      */
specifier|private
name|String
name|authenticationMethod
decl_stmt|;
comment|/**      *      */
specifier|private
name|boolean
name|bindAuthenticatorEnabled
decl_stmt|;
comment|/**      * Will use role name as LDAP group.      */
specifier|private
name|boolean
name|useRoleNameAsGroup
init|=
literal|false
decl_stmt|;
comment|/**      * Field extraProperties.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * field to ease json mapping wrapper on<code>extraProperties</code> field      */
specifier|private
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|extraPropertiesEntries
decl_stmt|;
comment|/**      * LDAP writable.      */
specifier|private
name|boolean
name|writable
init|=
literal|false
decl_stmt|;
specifier|public
name|LdapConfiguration
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
specifier|public
name|void
name|setHostName
parameter_list|(
name|String
name|hostName
parameter_list|)
block|{
name|this
operator|.
name|hostName
operator|=
name|hostName
expr_stmt|;
block|}
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSsl
parameter_list|()
block|{
return|return
name|ssl
return|;
block|}
specifier|public
name|void
name|setSsl
parameter_list|(
name|boolean
name|ssl
parameter_list|)
block|{
name|this
operator|.
name|ssl
operator|=
name|ssl
expr_stmt|;
block|}
specifier|public
name|String
name|getBaseDn
parameter_list|()
block|{
return|return
name|baseDn
return|;
block|}
specifier|public
name|void
name|setBaseDn
parameter_list|(
name|String
name|baseDn
parameter_list|)
block|{
name|this
operator|.
name|baseDn
operator|=
name|baseDn
expr_stmt|;
block|}
specifier|public
name|String
name|getContextFactory
parameter_list|()
block|{
return|return
name|contextFactory
return|;
block|}
specifier|public
name|void
name|setContextFactory
parameter_list|(
name|String
name|contextFactory
parameter_list|)
block|{
name|this
operator|.
name|contextFactory
operator|=
name|contextFactory
expr_stmt|;
block|}
specifier|public
name|String
name|getBindDn
parameter_list|()
block|{
return|return
name|bindDn
return|;
block|}
specifier|public
name|void
name|setBindDn
parameter_list|(
name|String
name|bindDn
parameter_list|)
block|{
name|this
operator|.
name|bindDn
operator|=
name|bindDn
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
specifier|public
name|String
name|getAuthenticationMethod
parameter_list|()
block|{
return|return
name|authenticationMethod
return|;
block|}
specifier|public
name|void
name|setAuthenticationMethod
parameter_list|(
name|String
name|authenticationMethod
parameter_list|)
block|{
name|this
operator|.
name|authenticationMethod
operator|=
name|authenticationMethod
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraProperties
parameter_list|()
block|{
return|return
name|extraProperties
return|;
block|}
specifier|public
name|void
name|setExtraProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProperties
parameter_list|)
block|{
name|this
operator|.
name|extraProperties
operator|=
name|extraProperties
expr_stmt|;
block|}
specifier|public
name|boolean
name|isBindAuthenticatorEnabled
parameter_list|()
block|{
return|return
name|bindAuthenticatorEnabled
return|;
block|}
specifier|public
name|void
name|setBindAuthenticatorEnabled
parameter_list|(
name|boolean
name|bindAuthenticatorEnabled
parameter_list|)
block|{
name|this
operator|.
name|bindAuthenticatorEnabled
operator|=
name|bindAuthenticatorEnabled
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|getExtraPropertiesEntries
parameter_list|()
block|{
name|extraPropertiesEntries
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getExtraProperties
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
name|getExtraProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|extraPropertiesEntries
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
name|extraPropertiesEntries
return|;
block|}
specifier|public
name|void
name|setExtraPropertiesEntries
parameter_list|(
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|extraPropertiesEntries
parameter_list|)
block|{
name|this
operator|.
name|extraPropertiesEntries
operator|=
name|extraPropertiesEntries
expr_stmt|;
if|if
condition|(
name|extraPropertiesEntries
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PropertyEntry
name|propertyEntry
range|:
name|extraPropertiesEntries
control|)
block|{
name|this
operator|.
name|extraProperties
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
name|String
name|getBaseGroupsDn
parameter_list|()
block|{
return|return
name|baseGroupsDn
return|;
block|}
specifier|public
name|void
name|setBaseGroupsDn
parameter_list|(
name|String
name|baseGroupsDn
parameter_list|)
block|{
name|this
operator|.
name|baseGroupsDn
operator|=
name|baseGroupsDn
expr_stmt|;
block|}
specifier|public
name|boolean
name|isWritable
parameter_list|()
block|{
return|return
name|writable
return|;
block|}
specifier|public
name|void
name|setWritable
parameter_list|(
name|boolean
name|writable
parameter_list|)
block|{
name|this
operator|.
name|writable
operator|=
name|writable
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUseRoleNameAsGroup
parameter_list|()
block|{
return|return
name|useRoleNameAsGroup
return|;
block|}
specifier|public
name|void
name|setUseRoleNameAsGroup
parameter_list|(
name|boolean
name|useRoleNameAsGroup
parameter_list|)
block|{
name|this
operator|.
name|useRoleNameAsGroup
operator|=
name|useRoleNameAsGroup
expr_stmt|;
block|}
block|}
end_class

end_unit

