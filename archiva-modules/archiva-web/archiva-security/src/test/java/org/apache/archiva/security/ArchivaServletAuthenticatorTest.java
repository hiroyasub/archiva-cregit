begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|security
operator|.
name|common
operator|.
name|ArchivaRoleConstants
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
name|UnauthorizedException
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
name|SecuritySession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_comment
comment|/**  * ArchivaServletAuthenticatorTest  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaServletAuthenticatorTest
extends|extends
name|AbstractSecurityTest
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"servletAuthenticator#test"
argument_list|)
specifier|private
name|ServletAuthenticator
name|servletAuth
decl_stmt|;
specifier|private
name|MockControl
name|httpServletRequestControl
decl_stmt|;
specifier|private
name|HttpServletRequest
name|request
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|httpServletRequestControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|=
operator|(
name|HttpServletRequest
operator|)
name|httpServletRequestControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|setupRepository
argument_list|(
literal|"corporate"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assignRepositoryManagerRole
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|Exception
block|{
name|roleManager
operator|.
name|assignTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|repoId
argument_list|,
name|principal
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthenticatedUserExists
parameter_list|()
throws|throws
name|Exception
block|{
name|AuthenticationResult
name|result
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
literal|"user"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|boolean
name|isAuthenticated
init|=
name|servletAuth
operator|.
name|isAuthenticated
argument_list|(
name|request
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|isAuthenticated
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthenticatedUserDoesNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|AuthenticationResult
name|result
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|false
argument_list|,
literal|"non-existing-user"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|servletAuth
operator|.
name|isAuthenticated
argument_list|(
name|request
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Authentication exception should have been thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthenticationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"User Credentials Invalid"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthorizedUserHasWriteAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|createUser
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"Al 'Archiva' Paca"
argument_list|)
expr_stmt|;
name|assignRepositoryManagerRole
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
name|UserManager
name|userManager
init|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
decl_stmt|;
name|User
name|user
init|=
name|userManager
operator|.
name|findUser
argument_list|(
name|USER_ALPACA
argument_list|)
decl_stmt|;
name|AuthenticationResult
name|result
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
name|USER_ALPACA
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SecuritySession
name|session
init|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|result
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|boolean
name|isAuthorized
init|=
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|request
argument_list|,
name|session
argument_list|,
literal|"corporate"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|isAuthorized
argument_list|)
expr_stmt|;
name|restoreGuestInitialValues
argument_list|(
name|USER_ALPACA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthorizedUserHasNoWriteAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|createUser
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"Al 'Archiva' Paca"
argument_list|)
expr_stmt|;
name|assignRepositoryObserverRole
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
name|httpServletRequestControl
operator|.
name|expectAndReturn
argument_list|(
name|request
operator|.
name|getRemoteAddr
argument_list|()
argument_list|,
literal|"192.168.111.111"
argument_list|)
expr_stmt|;
name|UserManager
name|userManager
init|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
decl_stmt|;
name|User
name|user
init|=
name|userManager
operator|.
name|findUser
argument_list|(
name|USER_ALPACA
argument_list|)
decl_stmt|;
name|AuthenticationResult
name|result
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
name|USER_ALPACA
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SecuritySession
name|session
init|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|result
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|httpServletRequestControl
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|request
argument_list|,
name|session
argument_list|,
literal|"corporate"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"UnauthorizedException should have been thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnauthorizedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Access denied for repository corporate"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|httpServletRequestControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|restoreGuestInitialValues
argument_list|(
name|USER_ALPACA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthorizedUserHasReadAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|createUser
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"Al 'Archiva' Paca"
argument_list|)
expr_stmt|;
name|assignRepositoryObserverRole
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
name|UserManager
name|userManager
init|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
decl_stmt|;
name|User
name|user
init|=
name|userManager
operator|.
name|findUser
argument_list|(
name|USER_ALPACA
argument_list|)
decl_stmt|;
name|AuthenticationResult
name|result
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
name|USER_ALPACA
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SecuritySession
name|session
init|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|result
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|boolean
name|isAuthorized
init|=
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|request
argument_list|,
name|session
argument_list|,
literal|"corporate"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|isAuthorized
argument_list|)
expr_stmt|;
name|restoreGuestInitialValues
argument_list|(
name|USER_ALPACA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthorizedUserHasNoReadAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|createUser
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"Al 'Archiva' Paca"
argument_list|)
expr_stmt|;
name|UserManager
name|userManager
init|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
decl_stmt|;
name|User
name|user
init|=
name|userManager
operator|.
name|findUser
argument_list|(
name|USER_ALPACA
argument_list|)
decl_stmt|;
name|AuthenticationResult
name|result
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
name|USER_ALPACA
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SecuritySession
name|session
init|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|result
argument_list|,
name|user
argument_list|)
decl_stmt|;
try|try
block|{
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|request
argument_list|,
name|session
argument_list|,
literal|"corporate"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"UnauthorizedException should have been thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnauthorizedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Access denied for repository corporate"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|restoreGuestInitialValues
argument_list|(
name|USER_ALPACA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthorizedGuestUserHasWriteAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|assignRepositoryManagerRole
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
name|boolean
name|isAuthorized
init|=
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|isAuthorized
argument_list|)
expr_stmt|;
comment|// cleanup previously add karma
name|restoreGuestInitialValues
argument_list|(
name|USER_GUEST
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthorizedGuestUserHasNoWriteAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|assignRepositoryObserverRole
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
name|boolean
name|isAuthorized
init|=
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|isAuthorized
argument_list|)
expr_stmt|;
comment|// cleanup previously add karma
name|restoreGuestInitialValues
argument_list|(
name|USER_GUEST
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthorizedGuestUserHasReadAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|assignRepositoryObserverRole
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
name|boolean
name|isAuthorized
init|=
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|isAuthorized
argument_list|)
expr_stmt|;
comment|// cleanup previously add karma
name|restoreGuestInitialValues
argument_list|(
name|USER_GUEST
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAuthorizedGuestUserHasNoReadAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|isAuthorized
init|=
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|isAuthorized
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

