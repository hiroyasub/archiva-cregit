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
name|test
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|LoginTest
extends|extends
name|AbstractArchivaTestCase
block|{
specifier|public
name|void
name|testBadLogin
parameter_list|()
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|submitLoginPage
argument_list|(
literal|"badUsername"
argument_list|,
literal|"badPassword"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You have entered an incorrect username and/or password"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUserLogin
parameter_list|()
block|{
name|createUser
argument_list|(
literal|"test-user"
argument_list|,
literal|"temp-pass"
argument_list|)
expr_stmt|;
name|goToLoginPage
argument_list|()
expr_stmt|;
name|submitLoginPage
argument_list|(
literal|"test-user"
argument_list|,
literal|"temp-pass"
argument_list|)
expr_stmt|;
comment|// change of password required for new users
if|if
condition|(
name|getTitle
argument_list|()
operator|.
name|equals
argument_list|(
name|getTitlePrefix
argument_list|()
operator|+
literal|"Change Password"
argument_list|)
condition|)
block|{
name|setFieldValue
argument_list|(
literal|"existingPassword"
argument_list|,
literal|"temp-pass"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newPassword"
argument_list|,
literal|"p4ssw0rd"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newPasswordConfirm"
argument_list|,
literal|"p4ssw0rd"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Change Password"
argument_list|)
expr_stmt|;
block|}
name|logout
argument_list|()
expr_stmt|;
name|deleteUser
argument_list|(
literal|"test-user"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createUser
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|submitLoginPage
argument_list|(
name|adminUsername
argument_list|,
name|adminPassword
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"User Management"
argument_list|)
expr_stmt|;
comment|//assertPage( "[Admin] User List" );
comment|//assertLinkNotPresent( username );
name|clickButtonWithValue
argument_list|(
literal|"Create New User"
argument_list|)
expr_stmt|;
comment|//assertPage( "[Admin] User Create" );
name|setFieldValue
argument_list|(
literal|"user.username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"user.fullName"
argument_list|,
name|username
operator|+
literal|" FullName"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"user.email"
argument_list|,
name|username
operator|+
literal|"@localhost.com"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"user.password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"user.confirmPassword"
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Create User"
argument_list|)
expr_stmt|;
name|waitPage
argument_list|()
expr_stmt|;
comment|//assertPage( "[Admin] User List" );
comment|//assertLinkPresent( username );
name|logout
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|deleteUser
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|submitLoginPage
argument_list|(
name|adminUsername
argument_list|,
name|adminPassword
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"User Management"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"[Admin] User List"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
name|username
argument_list|)
expr_stmt|;
comment|//this does not work bec the image is pointing to /archiva/archiva/images/pss/admin/delete.gif
comment|// when ran in selenium
comment|// clickLinkWithXPath( "//a[@href='/security/userdelete.action?username=" + username + "']" );
comment|//so instead we use this
name|open
argument_list|(
literal|"/archiva/security/userdelete.action?username="
operator|+
name|username
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"[Admin] User Delete"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"The following user will be deleted: "
operator|+
name|username
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Delete User"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"[Admin] User List"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

