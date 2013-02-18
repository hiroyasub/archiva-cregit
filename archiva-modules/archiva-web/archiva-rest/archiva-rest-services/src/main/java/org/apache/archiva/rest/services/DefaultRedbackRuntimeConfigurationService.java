begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
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
name|beans
operator|.
name|PropertyEntry
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
name|beans
operator|.
name|RedbackRuntimeConfiguration
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
name|beans
operator|.
name|LdapConfiguration
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
name|runtime
operator|.
name|RedbackRuntimeConfigurationAdmin
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
name|redback
operator|.
name|authentication
operator|.
name|AuthenticationException
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
name|redback
operator|.
name|authentication
operator|.
name|Authenticator
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
name|redback
operator|.
name|common
operator|.
name|ldap
operator|.
name|user
operator|.
name|LdapUserMapper
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
name|redback
operator|.
name|common
operator|.
name|ldap
operator|.
name|connection
operator|.
name|LdapConnection
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
name|redback
operator|.
name|common
operator|.
name|ldap
operator|.
name|connection
operator|.
name|LdapConnectionConfiguration
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
name|redback
operator|.
name|common
operator|.
name|ldap
operator|.
name|connection
operator|.
name|LdapConnectionFactory
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
name|redback
operator|.
name|common
operator|.
name|ldap
operator|.
name|connection
operator|.
name|LdapException
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
name|redback
operator|.
name|components
operator|.
name|cache
operator|.
name|Cache
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
name|redback
operator|.
name|policy
operator|.
name|CookieSettings
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
name|redback
operator|.
name|policy
operator|.
name|PasswordRule
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
name|redback
operator|.
name|rbac
operator|.
name|RBACManager
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
name|redback
operator|.
name|users
operator|.
name|UserManager
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|RBACManagerImplementationInformation
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|UserManagerImplementationInformation
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|RedbackRuntimeConfigurationService
import|;
end_import

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
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
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
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InvalidNameException
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M4  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"redbackRuntimeConfigurationService#rest"
argument_list|)
specifier|public
class|class
name|DefaultRedbackRuntimeConfigurationService
extends|extends
name|AbstractRestService
implements|implements
name|RedbackRuntimeConfigurationService
block|{
annotation|@
name|Inject
specifier|private
name|RedbackRuntimeConfigurationAdmin
name|redbackRuntimeConfigurationAdmin
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"userManager#configurable"
argument_list|)
specifier|private
name|UserManager
name|userManager
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"ldapConnectionFactory#configurable"
argument_list|)
specifier|private
name|LdapConnectionFactory
name|ldapConnectionFactory
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"cache#users"
argument_list|)
specifier|private
name|Cache
name|usersCache
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|LdapUserMapper
name|ldapUserMapper
decl_stmt|;
specifier|public
name|RedbackRuntimeConfiguration
name|getRedbackRuntimeConfiguration
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|redbackRuntimeConfigurationAdmin
operator|.
name|getRedbackRuntimeConfiguration
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|updateRedbackRuntimeConfiguration
parameter_list|(
name|RedbackRuntimeConfiguration
name|redbackRuntimeConfiguration
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
comment|// has user manager impl changed ?
name|boolean
name|userManagerChanged
init|=
name|redbackRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
name|redbackRuntimeConfigurationAdmin
operator|.
name|getRedbackRuntimeConfiguration
argument_list|()
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|userManagerChanged
operator|=
name|userManagerChanged
operator|||
operator|(
name|redbackRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|!=
name|redbackRuntimeConfigurationAdmin
operator|.
name|getRedbackRuntimeConfiguration
argument_list|()
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|redbackRuntimeConfigurationAdmin
operator|.
name|updateRedbackRuntimeConfiguration
argument_list|(
name|redbackRuntimeConfiguration
argument_list|)
expr_stmt|;
if|if
condition|(
name|userManagerChanged
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"user managerImpls changed to {} so reload it"
argument_list|,
name|redbackRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
argument_list|)
expr_stmt|;
name|userManager
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
name|ldapConnectionFactory
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|PasswordRule
argument_list|>
name|passwordRules
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|PasswordRule
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|PasswordRule
name|passwordRule
range|:
name|passwordRules
control|)
block|{
name|passwordRule
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
name|Collection
argument_list|<
name|CookieSettings
argument_list|>
name|cookieSettingsList
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|CookieSettings
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|CookieSettings
name|cookieSettings
range|:
name|cookieSettingsList
control|)
block|{
name|cookieSettings
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
name|Collection
argument_list|<
name|Authenticator
argument_list|>
name|authenticators
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|Authenticator
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|Authenticator
name|authenticator
range|:
name|authenticators
control|)
block|{
name|authenticator
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
comment|// users cache
name|usersCache
operator|.
name|setTimeToIdleSeconds
argument_list|(
name|redbackRuntimeConfiguration
operator|.
name|getUsersCacheConfiguration
argument_list|()
operator|.
name|getTimeToIdleSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|usersCache
operator|.
name|setTimeToLiveSeconds
argument_list|(
name|redbackRuntimeConfiguration
operator|.
name|getUsersCacheConfiguration
argument_list|()
operator|.
name|getTimeToLiveSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|usersCache
operator|.
name|setMaxElementsInMemory
argument_list|(
name|redbackRuntimeConfiguration
operator|.
name|getUsersCacheConfiguration
argument_list|()
operator|.
name|getMaxElementsInMemory
argument_list|()
argument_list|)
expr_stmt|;
name|usersCache
operator|.
name|setMaxElementsOnDisk
argument_list|(
name|redbackRuntimeConfiguration
operator|.
name|getUsersCacheConfiguration
argument_list|()
operator|.
name|getMaxElementsOnDisk
argument_list|()
argument_list|)
expr_stmt|;
name|ldapUserMapper
operator|.
name|initialize
argument_list|()
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|AuthenticationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|UserManagerImplementationInformation
argument_list|>
name|getUserManagerImplementationInformations
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|UserManager
argument_list|>
name|beans
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|UserManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|beans
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|UserManagerImplementationInformation
argument_list|>
name|informations
init|=
operator|new
name|ArrayList
argument_list|<
name|UserManagerImplementationInformation
argument_list|>
argument_list|(
name|beans
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|UserManager
argument_list|>
name|entry
range|:
name|beans
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|UserManager
name|userManager
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|UserManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|userManager
operator|.
name|isFinalImplementation
argument_list|()
condition|)
block|{
name|UserManagerImplementationInformation
name|information
init|=
operator|new
name|UserManagerImplementationInformation
argument_list|()
decl_stmt|;
name|information
operator|.
name|setBeanId
argument_list|(
name|StringUtils
operator|.
name|substringAfter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
name|information
operator|.
name|setDescriptionKey
argument_list|(
name|userManager
operator|.
name|getDescriptionKey
argument_list|()
argument_list|)
expr_stmt|;
name|information
operator|.
name|setReadOnly
argument_list|(
name|userManager
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
name|informations
operator|.
name|add
argument_list|(
name|information
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|informations
return|;
block|}
specifier|public
name|List
argument_list|<
name|RBACManagerImplementationInformation
argument_list|>
name|getRbacManagerImplementationInformations
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|RBACManager
argument_list|>
name|beans
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|RBACManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|beans
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|RBACManagerImplementationInformation
argument_list|>
name|informations
init|=
operator|new
name|ArrayList
argument_list|<
name|RBACManagerImplementationInformation
argument_list|>
argument_list|(
name|beans
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|RBACManager
argument_list|>
name|entry
range|:
name|beans
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|RBACManager
name|rbacManager
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|RBACManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rbacManager
operator|.
name|isFinalImplementation
argument_list|()
condition|)
block|{
name|RBACManagerImplementationInformation
name|information
init|=
operator|new
name|RBACManagerImplementationInformation
argument_list|()
decl_stmt|;
name|information
operator|.
name|setBeanId
argument_list|(
name|StringUtils
operator|.
name|substringAfter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"#"
argument_list|)
argument_list|)
expr_stmt|;
name|information
operator|.
name|setDescriptionKey
argument_list|(
name|rbacManager
operator|.
name|getDescriptionKey
argument_list|()
argument_list|)
expr_stmt|;
name|information
operator|.
name|setReadOnly
argument_list|(
name|rbacManager
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
name|informations
operator|.
name|add
argument_list|(
name|information
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|informations
return|;
block|}
specifier|public
name|Boolean
name|checkLdapConnection
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
name|LdapConnection
name|ldapConnection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ldapConnection
operator|=
name|ldapConnectionFactory
operator|.
name|getConnection
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LdapException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"fail to get LdapConnection: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|ldapConnection
operator|!=
literal|null
condition|)
block|{
name|ldapConnection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
specifier|public
name|Boolean
name|checkLdapConnection
parameter_list|(
name|LdapConfiguration
name|ldapConfiguration
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|LdapConnection
name|ldapConnection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|LdapConnectionConfiguration
name|ldapConnectionConfiguration
init|=
operator|new
name|LdapConnectionConfiguration
argument_list|(
name|ldapConfiguration
operator|.
name|getHostName
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getPort
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getBaseDn
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getContextFactory
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getBindDn
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getPassword
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getAuthenticationMethod
argument_list|()
argument_list|,
name|toProperties
argument_list|(
name|ldapConfiguration
operator|.
name|getExtraProperties
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ldapConnection
operator|=
name|ldapConnectionFactory
operator|.
name|getConnection
argument_list|(
name|ldapConnectionConfiguration
argument_list|)
expr_stmt|;
name|ldapConnection
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// verify groups dn value too
name|ldapConnectionConfiguration
operator|=
operator|new
name|LdapConnectionConfiguration
argument_list|(
name|ldapConfiguration
operator|.
name|getHostName
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getPort
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getBaseGroupsDn
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getContextFactory
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getBindDn
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getPassword
argument_list|()
argument_list|,
name|ldapConfiguration
operator|.
name|getAuthenticationMethod
argument_list|()
argument_list|,
name|toProperties
argument_list|(
name|ldapConfiguration
operator|.
name|getExtraProperties
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ldapConnection
operator|=
name|ldapConnectionFactory
operator|.
name|getConnection
argument_list|(
name|ldapConnectionConfiguration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidNameException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"fail to get LdapConnection: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|LdapException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"fail to get LdapConnection: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|ldapConnection
operator|!=
literal|null
condition|)
block|{
name|ldapConnection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
specifier|private
name|Properties
name|toProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
operator|||
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|properties
return|;
block|}
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
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|properties
operator|.
name|put
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
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

