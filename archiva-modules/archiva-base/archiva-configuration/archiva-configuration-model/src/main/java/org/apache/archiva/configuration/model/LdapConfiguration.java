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
comment|/**  *   *         The LDAP configuration.  *         *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|LdapConfiguration
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
comment|/**      * The LDAP host.      */
specifier|private
name|String
name|hostName
decl_stmt|;
comment|/**      * The LDAP port.      */
specifier|private
name|int
name|port
init|=
literal|0
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
comment|/**      * The LDAP base dn for groups (if empty baseDn is used).      */
specifier|private
name|String
name|baseGroupsDn
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
comment|/**      * The LDAP authenticator enabled.      */
specifier|private
name|boolean
name|bindAuthenticatorEnabled
init|=
literal|false
decl_stmt|;
comment|/**      * LDAP writable.      */
specifier|private
name|boolean
name|writable
init|=
literal|false
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
name|java
operator|.
name|util
operator|.
name|Map
name|extraProperties
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method addExtraProperty.      *       * @param key      * @param value      */
specifier|public
name|void
name|addExtraProperty
parameter_list|(
name|Object
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|getExtraProperties
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
comment|//-- void addExtraProperty( Object, String )
comment|/**      * Get the LDAP authenticationMethod.      *       * @return String      */
specifier|public
name|String
name|getAuthenticationMethod
parameter_list|()
block|{
return|return
name|this
operator|.
name|authenticationMethod
return|;
block|}
comment|//-- String getAuthenticationMethod()
comment|/**      * Get the LDAP base dn.      *       * @return String      */
specifier|public
name|String
name|getBaseDn
parameter_list|()
block|{
return|return
name|this
operator|.
name|baseDn
return|;
block|}
comment|//-- String getBaseDn()
comment|/**      * Get the LDAP base dn for groups (if empty baseDn is used).      *       * @return String      */
specifier|public
name|String
name|getBaseGroupsDn
parameter_list|()
block|{
return|return
name|this
operator|.
name|baseGroupsDn
return|;
block|}
comment|//-- String getBaseGroupsDn()
comment|/**      * Get the LDAP bind dn.      *       * @return String      */
specifier|public
name|String
name|getBindDn
parameter_list|()
block|{
return|return
name|this
operator|.
name|bindDn
return|;
block|}
comment|//-- String getBindDn()
comment|/**      * Get contextFactory to use.      *       * @return String      */
specifier|public
name|String
name|getContextFactory
parameter_list|()
block|{
return|return
name|this
operator|.
name|contextFactory
return|;
block|}
comment|//-- String getContextFactory()
comment|/**      * Method getExtraProperties.      *       * @return Map      */
specifier|public
name|java
operator|.
name|util
operator|.
name|Map
name|getExtraProperties
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|extraProperties
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|extraProperties
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|HashMap
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|extraProperties
return|;
block|}
comment|//-- java.util.Map getExtraProperties()
comment|/**      * Get the LDAP host.      *       * @return String      */
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|this
operator|.
name|hostName
return|;
block|}
comment|//-- String getHostName()
comment|/**      * Get the LDAP password.      *       * @return String      */
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|this
operator|.
name|password
return|;
block|}
comment|//-- String getPassword()
comment|/**      * Get the LDAP port.      *       * @return int      */
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|this
operator|.
name|port
return|;
block|}
comment|//-- int getPort()
comment|/**      * Get the LDAP authenticator enabled.      *       * @return boolean      */
specifier|public
name|boolean
name|isBindAuthenticatorEnabled
parameter_list|()
block|{
return|return
name|this
operator|.
name|bindAuthenticatorEnabled
return|;
block|}
comment|//-- boolean isBindAuthenticatorEnabled()
comment|/**      * Get ssl LDAP connection.      *       * @return boolean      */
specifier|public
name|boolean
name|isSsl
parameter_list|()
block|{
return|return
name|this
operator|.
name|ssl
return|;
block|}
comment|//-- boolean isSsl()
comment|/**      * Get will use role name as LDAP group.      *       * @return boolean      */
specifier|public
name|boolean
name|isUseRoleNameAsGroup
parameter_list|()
block|{
return|return
name|this
operator|.
name|useRoleNameAsGroup
return|;
block|}
comment|//-- boolean isUseRoleNameAsGroup()
comment|/**      * Get lDAP writable.      *       * @return boolean      */
specifier|public
name|boolean
name|isWritable
parameter_list|()
block|{
return|return
name|this
operator|.
name|writable
return|;
block|}
comment|//-- boolean isWritable()
comment|/**      * Set the LDAP authenticationMethod.      *       * @param authenticationMethod      */
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
comment|//-- void setAuthenticationMethod( String )
comment|/**      * Set the LDAP base dn.      *       * @param baseDn      */
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
comment|//-- void setBaseDn( String )
comment|/**      * Set the LDAP base dn for groups (if empty baseDn is used).      *       * @param baseGroupsDn      */
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
comment|//-- void setBaseGroupsDn( String )
comment|/**      * Set the LDAP authenticator enabled.      *       * @param bindAuthenticatorEnabled      */
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
comment|//-- void setBindAuthenticatorEnabled( boolean )
comment|/**      * Set the LDAP bind dn.      *       * @param bindDn      */
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
comment|//-- void setBindDn( String )
comment|/**      * Set contextFactory to use.      *       * @param contextFactory      */
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
comment|//-- void setContextFactory( String )
comment|/**      * Set additional properties to use for ldap connection.      *       * @param extraProperties      */
specifier|public
name|void
name|setExtraProperties
parameter_list|(
name|java
operator|.
name|util
operator|.
name|Map
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
comment|//-- void setExtraProperties( java.util.Map )
comment|/**      * Set the LDAP host.      *       * @param hostName      */
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
comment|//-- void setHostName( String )
comment|/**      * Set the LDAP password.      *       * @param password      */
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
comment|//-- void setPassword( String )
comment|/**      * Set the LDAP port.      *       * @param port      */
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
comment|//-- void setPort( int )
comment|/**      * Set ssl LDAP connection.      *       * @param ssl      */
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
comment|//-- void setSsl( boolean )
comment|/**      * Set will use role name as LDAP group.      *       * @param useRoleNameAsGroup      */
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
comment|//-- void setUseRoleNameAsGroup( boolean )
comment|/**      * Set lDAP writable.      *       * @param writable      */
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
comment|//-- void setWritable( boolean )
block|}
end_class

end_unit

