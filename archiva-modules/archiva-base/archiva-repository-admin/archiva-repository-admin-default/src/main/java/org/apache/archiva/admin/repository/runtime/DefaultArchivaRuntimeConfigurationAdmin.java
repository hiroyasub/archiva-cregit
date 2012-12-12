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
name|repository
operator|.
name|runtime
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|beanlib
operator|.
name|provider
operator|.
name|replicator
operator|.
name|BeanReplicator
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
name|beans
operator|.
name|ArchivaRuntimeConfiguration
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
name|ArchivaRuntimeConfigurationAdmin
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|Configuration
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
name|configuration
operator|.
name|IndeterminateConfigurationException
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
name|configuration
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
name|redback
operator|.
name|components
operator|.
name|registry
operator|.
name|RegistryException
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
name|configuration
operator|.
name|UserConfiguration
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
name|configuration
operator|.
name|UserConfigurationException
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
name|configuration
operator|.
name|UserConfigurationKeys
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|annotation
operator|.
name|PostConstruct
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M4  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"userConfiguration#archiva"
argument_list|)
specifier|public
class|class
name|DefaultArchivaRuntimeConfigurationAdmin
implements|implements
name|ArchivaRuntimeConfigurationAdmin
implements|,
name|UserConfiguration
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"userConfiguration#redback"
argument_list|)
name|UserConfiguration
name|userConfiguration
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|UserConfigurationException
block|{
try|try
block|{
name|ArchivaRuntimeConfiguration
name|archivaRuntimeConfiguration
init|=
name|getArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
comment|// migrate or not data from redback
if|if
condition|(
operator|!
name|archivaRuntimeConfiguration
operator|.
name|isMigratedFromRedbackConfiguration
argument_list|()
condition|)
block|{
comment|// so migrate if available
name|String
name|userManagerImpl
init|=
name|userConfiguration
operator|.
name|getString
argument_list|(
name|UserConfigurationKeys
operator|.
name|USER_MANAGER_IMPL
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|userManagerImpl
argument_list|)
condition|)
block|{
name|archivaRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|add
argument_list|(
name|userManagerImpl
argument_list|)
expr_stmt|;
block|}
comment|// now ldap
name|LdapConfiguration
name|ldapConfiguration
init|=
name|archivaRuntimeConfiguration
operator|.
name|getLdapConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|ldapConfiguration
operator|==
literal|null
condition|)
block|{
name|ldapConfiguration
operator|=
operator|new
name|LdapConfiguration
argument_list|()
expr_stmt|;
name|archivaRuntimeConfiguration
operator|.
name|setLdapConfiguration
argument_list|(
name|ldapConfiguration
argument_list|)
expr_stmt|;
block|}
name|ldapConfiguration
operator|.
name|setHostName
argument_list|(
name|userConfiguration
operator|.
name|getString
argument_list|(
name|UserConfigurationKeys
operator|.
name|LDAP_HOSTNAME
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|ldapConfiguration
operator|.
name|setPort
argument_list|(
name|userConfiguration
operator|.
name|getInt
argument_list|(
name|UserConfigurationKeys
operator|.
name|LDAP_PORT
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ldapConfiguration
operator|.
name|setSsl
argument_list|(
name|userConfiguration
operator|.
name|getBoolean
argument_list|(
name|UserConfigurationKeys
operator|.
name|LDAP_SSL
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|ldapConfiguration
operator|.
name|setBaseDn
argument_list|(
name|userConfiguration
operator|.
name|getConcatenatedList
argument_list|(
name|UserConfigurationKeys
operator|.
name|LDAP_BASEDN
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|ldapConfiguration
operator|.
name|setContextFactory
argument_list|(
name|userConfiguration
operator|.
name|getString
argument_list|(
name|UserConfigurationKeys
operator|.
name|LDAP_CONTEX_FACTORY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|ldapConfiguration
operator|.
name|setBindDn
argument_list|(
name|userConfiguration
operator|.
name|getConcatenatedList
argument_list|(
name|UserConfigurationKeys
operator|.
name|LDAP_BINDDN
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|ldapConfiguration
operator|.
name|setPassword
argument_list|(
name|userConfiguration
operator|.
name|getString
argument_list|(
name|UserConfigurationKeys
operator|.
name|LDAP_PASSWORD
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|ldapConfiguration
operator|.
name|setAuthenticationMethod
argument_list|(
name|userConfiguration
operator|.
name|getString
argument_list|(
name|UserConfigurationKeys
operator|.
name|LDAP_AUTHENTICATION_METHOD
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|archivaRuntimeConfiguration
operator|.
name|setMigratedFromRedbackConfiguration
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|updateArchivaRuntimeConfiguration
argument_list|(
name|archivaRuntimeConfiguration
argument_list|)
expr_stmt|;
block|}
comment|// we must ensure userManagerImpls list is not empty if so put at least jdo one !
if|if
condition|(
name|archivaRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"archivaRuntimeConfiguration with empty userManagerImpls so force at least jdo implementation !"
argument_list|)
expr_stmt|;
name|archivaRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|add
argument_list|(
literal|"jdo"
argument_list|)
expr_stmt|;
name|updateArchivaRuntimeConfiguration
argument_list|(
name|archivaRuntimeConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UserConfigurationException
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
name|ArchivaRuntimeConfiguration
name|getArchivaRuntimeConfiguration
parameter_list|()
block|{
return|return
name|build
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRedbackRuntimeConfiguration
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|updateArchivaRuntimeConfiguration
parameter_list|(
name|ArchivaRuntimeConfiguration
name|archivaRuntimeConfiguration
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|RedbackRuntimeConfiguration
name|runtimeConfiguration
init|=
name|build
argument_list|(
name|archivaRuntimeConfiguration
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setRedbackRuntimeConfiguration
argument_list|(
name|runtimeConfiguration
argument_list|)
expr_stmt|;
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
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
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
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
specifier|private
name|ArchivaRuntimeConfiguration
name|build
parameter_list|(
name|RedbackRuntimeConfiguration
name|runtimeConfiguration
parameter_list|)
block|{
name|ArchivaRuntimeConfiguration
name|archivaRuntimeConfiguration
init|=
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|runtimeConfiguration
argument_list|,
name|ArchivaRuntimeConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|runtimeConfiguration
operator|.
name|getLdapConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|archivaRuntimeConfiguration
operator|.
name|setLdapConfiguration
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|runtimeConfiguration
operator|.
name|getLdapConfiguration
argument_list|()
argument_list|,
name|LdapConfiguration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|archivaRuntimeConfiguration
operator|.
name|getLdapConfiguration
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// prevent NPE
name|archivaRuntimeConfiguration
operator|.
name|setLdapConfiguration
argument_list|(
operator|new
name|LdapConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|archivaRuntimeConfiguration
return|;
block|}
specifier|private
name|RedbackRuntimeConfiguration
name|build
parameter_list|(
name|ArchivaRuntimeConfiguration
name|archivaRuntimeConfiguration
parameter_list|)
block|{
name|RedbackRuntimeConfiguration
name|redbackRuntimeConfiguration
init|=
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|archivaRuntimeConfiguration
argument_list|,
name|RedbackRuntimeConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|redbackRuntimeConfiguration
operator|.
name|setLdapConfiguration
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|archivaRuntimeConfiguration
operator|.
name|getLdapConfiguration
argument_list|()
argument_list|,
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|LdapConfiguration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|redbackRuntimeConfiguration
return|;
block|}
comment|// wrapper for UserConfiguration to intercept values (and store it not yet migrated
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|USER_MANAGER_IMPL
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|// possible false for others than archiva user manager
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
name|ArchivaRuntimeConfiguration
name|conf
init|=
name|getArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
name|String
name|value
init|=
name|userConfiguration
operator|.
name|getString
argument_list|(
name|key
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
literal|null
return|;
block|}
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
try|try
block|{
name|updateArchivaRuntimeConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"fail to save ArchivaRuntimeConfiguration: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
return|return
name|value
return|;
block|}
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|LDAP_HOSTNAME
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getLdapConfiguration
argument_list|()
operator|.
name|getHostName
argument_list|()
return|;
block|}
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|LDAP_CONTEX_FACTORY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getLdapConfiguration
argument_list|()
operator|.
name|getContextFactory
argument_list|()
return|;
block|}
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|LDAP_PASSWORD
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getLdapConfiguration
argument_list|()
operator|.
name|getPassword
argument_list|()
return|;
block|}
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|LDAP_AUTHENTICATION_METHOD
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getLdapConfiguration
argument_list|()
operator|.
name|getAuthenticationMethod
argument_list|()
return|;
block|}
name|ArchivaRuntimeConfiguration
name|conf
init|=
name|getArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
name|String
name|value
init|=
name|userConfiguration
operator|.
name|getString
argument_list|(
name|key
argument_list|,
name|defaultValue
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
literal|null
return|;
block|}
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
try|try
block|{
name|updateArchivaRuntimeConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"fail to save ArchivaRuntimeConfiguration: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
return|return
name|value
return|;
block|}
specifier|public
name|int
name|getInt
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|ArchivaRuntimeConfiguration
name|conf
init|=
name|getArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
name|int
name|value
init|=
name|userConfiguration
operator|.
name|getInt
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|updateArchivaRuntimeConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"fail to save ArchivaRuntimeConfiguration: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
return|return
name|value
return|;
block|}
specifier|public
name|int
name|getInt
parameter_list|(
name|String
name|key
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|LDAP_PORT
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getLdapConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
return|;
block|}
name|ArchivaRuntimeConfiguration
name|conf
init|=
name|getArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
name|int
name|value
init|=
name|userConfiguration
operator|.
name|getInt
argument_list|(
name|key
argument_list|,
name|defaultValue
argument_list|)
decl_stmt|;
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|updateArchivaRuntimeConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"fail to save ArchivaRuntimeConfiguration: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
return|return
name|value
return|;
block|}
specifier|public
name|boolean
name|getBoolean
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|ArchivaRuntimeConfiguration
name|conf
init|=
name|getArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
name|boolean
name|value
init|=
name|userConfiguration
operator|.
name|getBoolean
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|updateArchivaRuntimeConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"fail to save ArchivaRuntimeConfiguration: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
return|return
name|value
return|;
block|}
specifier|public
name|boolean
name|getBoolean
parameter_list|(
name|String
name|key
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|LDAP_SSL
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getLdapConfiguration
argument_list|()
operator|.
name|isSsl
argument_list|()
return|;
block|}
name|ArchivaRuntimeConfiguration
name|conf
init|=
name|getArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
name|boolean
name|value
init|=
name|userConfiguration
operator|.
name|getBoolean
argument_list|(
name|key
argument_list|,
name|defaultValue
argument_list|)
decl_stmt|;
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|updateArchivaRuntimeConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"fail to save ArchivaRuntimeConfiguration: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
return|return
name|value
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getList
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|value
init|=
name|userConfiguration
operator|.
name|getList
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|ArchivaRuntimeConfiguration
name|conf
init|=
name|getArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
comment|// TODO concat values
name|conf
operator|.
name|getConfigurationProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|""
argument_list|)
expr_stmt|;
try|try
block|{
name|updateArchivaRuntimeConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"fail to save ArchivaRuntimeConfiguration: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
return|return
name|value
return|;
block|}
specifier|public
name|String
name|getConcatenatedList
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|LDAP_BASEDN
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getLdapConfiguration
argument_list|()
operator|.
name|getBaseDn
argument_list|()
return|;
block|}
if|if
condition|(
name|UserConfigurationKeys
operator|.
name|LDAP_BINDDN
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getLdapConfiguration
argument_list|()
operator|.
name|getBindDn
argument_list|()
return|;
block|}
return|return
name|userConfiguration
operator|.
name|getConcatenatedList
argument_list|(
name|key
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
block|}
end_class

end_unit

