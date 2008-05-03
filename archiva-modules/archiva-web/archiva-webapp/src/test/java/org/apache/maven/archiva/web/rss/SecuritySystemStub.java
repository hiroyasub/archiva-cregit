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
name|web
operator|.
name|rss
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|AuthenticationDataSource
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
name|keys
operator|.
name|KeyManager
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
name|UserSecurityPolicy
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
name|UserManager
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
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|users
operator|.
name|jdo
operator|.
name|JdoUser
import|;
end_import

begin_comment
comment|/**  * SecuritySystem stub used for testing.   *  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|SecuritySystemStub
implements|implements
name|SecuritySystem
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|users
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
name|List
argument_list|<
name|String
argument_list|>
name|repoIds
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|SecuritySystemStub
parameter_list|()
block|{
name|users
operator|.
name|put
argument_list|(
literal|"user1"
argument_list|,
literal|"password1"
argument_list|)
expr_stmt|;
name|users
operator|.
name|put
argument_list|(
literal|"user2"
argument_list|,
literal|"password2"
argument_list|)
expr_stmt|;
name|users
operator|.
name|put
argument_list|(
literal|"user3"
argument_list|,
literal|"password3"
argument_list|)
expr_stmt|;
name|repoIds
operator|.
name|add
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
block|}
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
literal|null
decl_stmt|;
name|SecuritySession
name|session
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|users
operator|.
name|get
argument_list|(
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
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
expr_stmt|;
name|User
name|user
init|=
operator|new
name|JdoUser
argument_list|()
decl_stmt|;
name|user
operator|.
name|setUsername
argument_list|(
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|user
operator|.
name|setPassword
argument_list|(
name|users
operator|.
name|get
argument_list|(
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|result
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
operator|new
name|AuthenticationResult
argument_list|(
literal|false
argument_list|,
name|source
operator|.
name|getPrincipal
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|session
operator|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|session
return|;
block|}
specifier|public
name|AuthorizationResult
name|authorize
parameter_list|(
name|SecuritySession
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
throws|throws
name|AuthorizationException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|AuthorizationResult
name|authorize
parameter_list|(
name|SecuritySession
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|,
name|Object
name|arg2
parameter_list|)
throws|throws
name|AuthorizationException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getAuthenticatorId
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getAuthorizerId
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|KeyManager
name|getKeyManager
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|UserSecurityPolicy
name|getPolicy
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getUserManagementId
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|UserManager
name|getUserManager
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isAuthenticated
parameter_list|(
name|AuthenticationDataSource
name|arg0
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|UserNotFoundException
throws|,
name|AccountLockedException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|SecuritySession
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
throws|throws
name|AuthorizationException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|SecuritySession
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|,
name|Object
name|arg2
parameter_list|)
throws|throws
name|AuthorizationException
block|{
if|if
condition|(
name|repoIds
operator|.
name|contains
argument_list|(
name|arg2
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

