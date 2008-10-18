begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|ArchivaRoleConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
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
name|codehaus
operator|.
name|plexus
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
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|authorization
operator|.
name|AuthorizationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|authorization
operator|.
name|AuthorizationResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|authorization
operator|.
name|UnauthorizedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
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
name|codehaus
operator|.
name|plexus
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
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|system
operator|.
name|DefaultSecuritySession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|system
operator|.
name|SecuritySession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|system
operator|.
name|SecuritySystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
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
name|codehaus
operator|.
name|plexus
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

begin_comment
comment|/**  * @version  * @plexus.component role="org.apache.maven.archiva.security.ServletAuthenticator" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaServletAuthenticator
implements|implements
name|ServletAuthenticator
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ArchivaServletAuthenticator
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|SecuritySystem
name|securitySystem
decl_stmt|;
specifier|public
name|boolean
name|isAuthenticated
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|AuthenticationResult
name|result
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|AccountLockedException
throws|,
name|MustChangePasswordException
block|{
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
operator|!
name|result
operator|.
name|isAuthenticated
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthenticationException
argument_list|(
literal|"User Credentials Invalid"
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|SecuritySession
name|securitySession
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|boolean
name|isWriteRequest
parameter_list|)
throws|throws
name|AuthorizationException
throws|,
name|UnauthorizedException
block|{
comment|// TODO: also check for permission to proxy the resource when MRM-579 is implemented
name|String
name|permission
init|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
decl_stmt|;
if|if
condition|(
name|isWriteRequest
condition|)
block|{
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
expr_stmt|;
block|}
name|AuthorizationResult
name|authzResult
init|=
name|securitySystem
operator|.
name|authorize
argument_list|(
name|securitySession
argument_list|,
name|permission
argument_list|,
name|repositoryId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|authzResult
operator|.
name|isAuthorized
argument_list|()
condition|)
block|{
if|if
condition|(
name|authzResult
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Authorization Denied [ip="
operator|+
name|request
operator|.
name|getRemoteAddr
argument_list|()
operator|+
literal|",isWriteRequest="
operator|+
name|isWriteRequest
operator|+
literal|",permission="
operator|+
name|permission
operator|+
literal|",repo="
operator|+
name|repositoryId
operator|+
literal|"] : "
operator|+
name|authzResult
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnauthorizedException
argument_list|(
literal|"Access denied for repository "
operator|+
name|repositoryId
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|UnauthorizedException
argument_list|(
literal|"User account is locked"
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|,
name|boolean
name|isWriteRequest
parameter_list|)
throws|throws
name|UnauthorizedException
block|{
try|try
block|{
name|String
name|permission
init|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
decl_stmt|;
if|if
condition|(
name|isWriteRequest
condition|)
block|{
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
expr_stmt|;
block|}
name|User
name|user
init|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
operator|.
name|findUser
argument_list|(
name|principal
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|.
name|isLocked
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnauthorizedException
argument_list|(
literal|"User account is locked."
argument_list|)
throw|;
block|}
name|AuthenticationResult
name|authn
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
name|principal
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SecuritySession
name|securitySession
init|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|authn
argument_list|,
name|user
argument_list|)
decl_stmt|;
return|return
name|securitySystem
operator|.
name|isAuthorized
argument_list|(
name|securitySession
argument_list|,
name|permission
argument_list|,
name|repoId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UserNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnauthorizedException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnauthorizedException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

