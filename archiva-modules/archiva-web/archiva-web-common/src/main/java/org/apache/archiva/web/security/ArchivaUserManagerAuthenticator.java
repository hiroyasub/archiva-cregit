begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|security
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|redback
operator|.
name|authentication
operator|.
name|AuthenticationConstants
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
name|AuthenticationDataSource
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
name|AuthenticationResult
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
name|PasswordBasedAuthenticationDataSource
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
name|users
operator|.
name|UserManagerAuthenticator
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
name|AccountLockedException
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
name|MustChangePasswordException
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
name|PasswordEncoder
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
name|UserSecurityPolicy
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
name|User
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
name|redback
operator|.
name|users
operator|.
name|UserManagerException
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
name|UserNotFoundException
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
name|Service
argument_list|(
literal|"authenticator#archiva"
argument_list|)
specifier|public
class|class
name|ArchivaUserManagerAuthenticator
extends|extends
name|UserManagerAuthenticator
block|{
specifier|private
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
name|UserSecurityPolicy
name|securityPolicy
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ArchivaRuntimeConfigurationAdmin
name|archivaRuntimeConfigurationAdmin
decl_stmt|;
specifier|private
name|List
argument_list|<
name|UserManager
argument_list|>
name|userManagers
decl_stmt|;
annotation|@
name|PostConstruct
specifier|protected
name|void
name|initialize
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|userManagerImpls
init|=
name|archivaRuntimeConfigurationAdmin
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getUserManagerImpls
argument_list|()
decl_stmt|;
name|userManagers
operator|=
operator|new
name|ArrayList
argument_list|<
name|UserManager
argument_list|>
argument_list|(
name|userManagerImpls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|beanId
range|:
name|userManagerImpls
control|)
block|{
name|userManagers
operator|.
name|add
argument_list|(
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"userManager#"
operator|+
name|beanId
argument_list|,
name|UserManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|AuthenticationResult
name|authenticate
parameter_list|(
name|AuthenticationDataSource
name|ds
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|AccountLockedException
throws|,
name|MustChangePasswordException
block|{
name|boolean
name|authenticationSuccess
init|=
literal|false
decl_stmt|;
name|String
name|username
init|=
literal|null
decl_stmt|;
name|Exception
name|resultException
init|=
literal|null
decl_stmt|;
name|PasswordBasedAuthenticationDataSource
name|source
init|=
operator|(
name|PasswordBasedAuthenticationDataSource
operator|)
name|ds
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|authnResultExceptionsMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagers
control|)
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Authenticate: {} with userManager: {}"
argument_list|,
name|source
argument_list|,
name|userManager
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|User
name|user
init|=
name|userManager
operator|.
name|findUser
argument_list|(
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|)
decl_stmt|;
name|username
operator|=
name|user
operator|.
name|getUsername
argument_list|()
expr_stmt|;
if|if
condition|(
name|user
operator|.
name|isLocked
argument_list|()
condition|)
block|{
comment|//throw new AccountLockedException( "Account " + source.getPrincipal() + " is locked.", user );
name|AccountLockedException
name|e
init|=
operator|new
name|AccountLockedException
argument_list|(
literal|"Account "
operator|+
name|source
operator|.
name|getPrincipal
argument_list|()
operator|+
literal|" is locked."
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"{}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|resultException
operator|=
name|e
expr_stmt|;
name|authnResultExceptionsMap
operator|.
name|put
argument_list|(
name|AuthenticationConstants
operator|.
name|AUTHN_LOCKED_USER_EXCEPTION
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|user
operator|.
name|isPasswordChangeRequired
argument_list|()
operator|&&
name|source
operator|.
name|isEnforcePasswordChange
argument_list|()
condition|)
block|{
comment|//throw new MustChangePasswordException( "Password expired.", user );
name|MustChangePasswordException
name|e
init|=
operator|new
name|MustChangePasswordException
argument_list|(
literal|"Password expired."
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"{}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|resultException
operator|=
name|e
expr_stmt|;
name|authnResultExceptionsMap
operator|.
name|put
argument_list|(
name|AuthenticationConstants
operator|.
name|AUTHN_MUST_CHANGE_PASSWORD_EXCEPTION
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PasswordEncoder
name|encoder
init|=
name|securityPolicy
operator|.
name|getPasswordEncoder
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"PasswordEncoder: {}"
argument_list|,
name|encoder
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|isPasswordValid
init|=
name|encoder
operator|.
name|isPasswordValid
argument_list|(
name|user
operator|.
name|getEncodedPassword
argument_list|()
argument_list|,
name|source
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isPasswordValid
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"User {} provided a valid password"
argument_list|,
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|securityPolicy
operator|.
name|extensionPasswordExpiration
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|authenticationSuccess
operator|=
literal|true
expr_stmt|;
comment|//REDBACK-151 do not make unnessesary updates to the user object
if|if
condition|(
name|user
operator|.
name|getCountFailedLoginAttempts
argument_list|()
operator|>
literal|0
condition|)
block|{
name|user
operator|.
name|setCountFailedLoginAttempts
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|userManager
operator|.
name|updateUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MustChangePasswordException
name|e
parameter_list|)
block|{
name|user
operator|.
name|setPasswordChangeRequired
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|//throw e;
name|resultException
operator|=
name|e
expr_stmt|;
name|authnResultExceptionsMap
operator|.
name|put
argument_list|(
name|AuthenticationConstants
operator|.
name|AUTHN_MUST_CHANGE_PASSWORD_EXCEPTION
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Password is Invalid for user {}."
argument_list|,
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|authnResultExceptionsMap
operator|.
name|put
argument_list|(
name|AuthenticationConstants
operator|.
name|AUTHN_NO_SUCH_USER
argument_list|,
literal|"Password is Invalid for user "
operator|+
name|source
operator|.
name|getPrincipal
argument_list|()
operator|+
literal|"."
argument_list|)
expr_stmt|;
try|try
block|{
name|securityPolicy
operator|.
name|extensionExcessiveLoginAttempts
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|userManager
operator|.
name|updateUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
comment|//return new AuthenticationResult( false, source.getPrincipal(), null, authnResultExceptionsMap );
block|}
block|}
catch|catch
parameter_list|(
name|UserNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Login for user {} failed. user not found."
argument_list|,
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|resultException
operator|=
name|e
expr_stmt|;
name|authnResultExceptionsMap
operator|.
name|put
argument_list|(
name|AuthenticationConstants
operator|.
name|AUTHN_NO_SUCH_USER
argument_list|,
literal|"Login for user "
operator|+
name|source
operator|.
name|getPrincipal
argument_list|()
operator|+
literal|" failed. user not found."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UserManagerException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Login for user {} failed, message: {}"
argument_list|,
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|resultException
operator|=
name|e
expr_stmt|;
name|authnResultExceptionsMap
operator|.
name|put
argument_list|(
name|AuthenticationConstants
operator|.
name|AUTHN_RUNTIME_EXCEPTION
argument_list|,
literal|"Login for user "
operator|+
name|source
operator|.
name|getPrincipal
argument_list|()
operator|+
literal|" failed, message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|AuthenticationResult
argument_list|(
name|authenticationSuccess
argument_list|,
name|username
argument_list|,
name|resultException
argument_list|,
name|authnResultExceptionsMap
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"ArchivaUserManagerAuthenticator"
return|;
block|}
block|}
end_class

end_unit

