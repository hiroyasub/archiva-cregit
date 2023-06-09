begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|authorization
operator|.
name|AuthorizationException
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
name|authorization
operator|.
name|AuthorizationResult
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
name|keys
operator|.
name|KeyManager
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
name|keys
operator|.
name|memory
operator|.
name|MemoryKeyManager
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
name|DefaultUserSecurityPolicy
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
name|system
operator|.
name|DefaultSecuritySession
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
name|system
operator|.
name|DefaultSecuritySystem
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
name|system
operator|.
name|SecuritySession
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
name|system
operator|.
name|SecuritySystem
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
name|UserNotFoundException
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
name|memory
operator|.
name|MemoryUserManager
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

begin_comment
comment|/**  * BypassSecuritySystem - used to bypass the security system for testing reasons and allow  * for every request to respond as success / true.   *  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"securitySystem#bypass"
argument_list|)
specifier|public
class|class
name|BypassSecuritySystem
extends|extends
name|DefaultSecuritySystem
implements|implements
name|SecuritySystem
block|{
specifier|private
name|KeyManager
name|bypassKeyManager
decl_stmt|;
specifier|private
name|UserSecurityPolicy
name|bypassPolicy
decl_stmt|;
specifier|private
name|UserManager
name|bypassUserManager
decl_stmt|;
specifier|public
name|BypassSecuritySystem
parameter_list|()
block|{
name|bypassKeyManager
operator|=
operator|new
name|MemoryKeyManager
argument_list|()
expr_stmt|;
name|bypassPolicy
operator|=
operator|new
name|DefaultUserSecurityPolicy
argument_list|()
expr_stmt|;
name|bypassUserManager
operator|=
operator|new
name|MemoryUserManager
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|SecuritySession
name|authenticate
parameter_list|(
name|AuthenticationDataSource
name|source
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|UserNotFoundException
throws|,
name|AccountLockedException
block|{
name|AuthenticationResult
name|result
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
name|source
operator|.
name|getUsername
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
operator|new
name|DefaultSecuritySession
argument_list|(
name|result
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|AuthorizationResult
name|authorize
parameter_list|(
name|SecuritySession
name|session
parameter_list|,
name|String
name|permission
parameter_list|)
throws|throws
name|AuthorizationException
block|{
return|return
operator|new
name|AuthorizationResult
argument_list|(
literal|true
argument_list|,
name|session
operator|.
name|getUser
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|AuthorizationResult
name|authorize
parameter_list|(
name|SecuritySession
name|session
parameter_list|,
name|String
name|permission
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|AuthorizationException
block|{
return|return
operator|new
name|AuthorizationResult
argument_list|(
literal|true
argument_list|,
name|session
operator|.
name|getUser
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAuthenticatorId
parameter_list|()
block|{
return|return
literal|"bypass-authenticator"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAuthorizerId
parameter_list|()
block|{
return|return
literal|"bypass-authorizer"
return|;
block|}
annotation|@
name|Override
specifier|public
name|KeyManager
name|getKeyManager
parameter_list|()
block|{
return|return
name|bypassKeyManager
return|;
block|}
annotation|@
name|Override
specifier|public
name|UserSecurityPolicy
name|getPolicy
parameter_list|()
block|{
return|return
name|bypassPolicy
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getUserManagementId
parameter_list|()
block|{
return|return
literal|"bypass-managementid"
return|;
block|}
annotation|@
name|Override
specifier|public
name|UserManager
name|getUserManager
parameter_list|()
block|{
return|return
name|bypassUserManager
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAuthenticated
parameter_list|(
name|AuthenticationDataSource
name|source
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|UserNotFoundException
throws|,
name|AccountLockedException
block|{
comment|// Always true
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|SecuritySession
name|session
parameter_list|,
name|String
name|permission
parameter_list|)
throws|throws
name|AuthorizationException
block|{
comment|// Always true
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|SecuritySession
name|session
parameter_list|,
name|String
name|permission
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|AuthorizationException
block|{
comment|// Always true
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|userManagerReadOnly
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

