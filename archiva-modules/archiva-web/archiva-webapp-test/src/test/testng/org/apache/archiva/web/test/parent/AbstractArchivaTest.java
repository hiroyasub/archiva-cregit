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
name|test
operator|.
name|parent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|web
operator|.
name|test
operator|.
name|XPathExpressionUtil
import|;
end_import

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractArchivaTest
extends|extends
name|AbstractSeleniumTest
block|{
specifier|protected
name|String
name|username
decl_stmt|;
specifier|protected
name|String
name|fullname
decl_stmt|;
specifier|public
name|String
name|getUserEmail
parameter_list|()
block|{
name|String
name|email
init|=
name|getProperty
argument_list|(
literal|"USERROLE_EMAIL"
argument_list|)
decl_stmt|;
return|return
name|email
return|;
block|}
specifier|public
name|String
name|getUserRolePassword
parameter_list|()
block|{
name|String
name|password
init|=
name|getProperty
argument_list|(
literal|"USERROLE_PASSWORD"
argument_list|)
decl_stmt|;
return|return
name|password
return|;
block|}
specifier|public
name|String
name|getUserRoleNewPassword
parameter_list|()
block|{
name|String
name|password_new
init|=
name|getProperty
argument_list|(
literal|"NEW_USERROLE_PASSWORD"
argument_list|)
decl_stmt|;
return|return
name|password_new
return|;
block|}
specifier|public
name|String
name|getBasedir
parameter_list|()
block|{
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|""
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
return|return
name|basedir
return|;
block|}
specifier|public
name|String
name|getAdminUsername
parameter_list|()
block|{
name|String
name|adminUsername
init|=
name|getProperty
argument_list|(
literal|"ADMIN_USERNAME"
argument_list|)
decl_stmt|;
return|return
name|adminUsername
return|;
block|}
specifier|public
name|String
name|getAdminPassword
parameter_list|()
block|{
name|String
name|adminPassword
init|=
name|getProperty
argument_list|(
literal|"ADMIN_PASSWORD"
argument_list|)
decl_stmt|;
return|return
name|adminPassword
return|;
block|}
specifier|public
name|void
name|assertCreateAdmin
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Create Admin User"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Username"
argument_list|)
expr_stmt|;
name|assertFieldValue
argument_list|(
literal|"admin"
argument_list|,
literal|"user.username"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Full Name*"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.fullName"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Email Address*"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.email"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password*"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.password"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Confirm Password*"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.confirmPassword"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Create Admin"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|submitAdminData
parameter_list|(
name|String
name|fullname
parameter_list|,
name|String
name|email
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|setFieldValue
argument_list|(
literal|"user.fullName"
argument_list|,
name|fullname
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"user.email"
argument_list|,
name|email
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
name|submit
argument_list|()
expr_stmt|;
block|}
comment|// Go to Login Page
specifier|public
name|void
name|goToLoginPage
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
name|baseUrl
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
name|assertLoginPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|submitUserData
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|rememberme
parameter_list|,
name|boolean
name|success
parameter_list|)
block|{
name|setFieldValue
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
if|if
condition|(
name|rememberme
condition|)
block|{
name|checkField
argument_list|(
literal|"rememberMe"
argument_list|)
expr_stmt|;
block|}
name|submit
argument_list|()
expr_stmt|;
if|if
condition|(
name|success
condition|)
block|{
name|assertUserLoggedIn
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertLoginPage
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|assertLoginPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Login Page"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Register"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Username"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"username"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Remember Me"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"rememberMe"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Need an Account? Register!"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Forgot your Password? Request a password reset."
argument_list|)
expr_stmt|;
block|}
comment|// User Management
specifier|public
name|void
name|goToUserManagementPage
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/security/userlist.action"
argument_list|)
expr_stmt|;
name|assertUserManagementPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertUserManagementPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ [Admin] User List"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"[Admin] List of Users in Role: Any"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Navigation"
argument_list|)
expr_stmt|;
name|assertImgWithAlt
argument_list|(
literal|"First"
argument_list|)
expr_stmt|;
name|assertImgWithAlt
argument_list|(
literal|"Prev"
argument_list|)
expr_stmt|;
name|assertImgWithAlt
argument_list|(
literal|"Next"
argument_list|)
expr_stmt|;
name|assertImgWithAlt
argument_list|(
literal|"Last"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Display Rows"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Username"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Full Name"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Email"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Permanent"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Validated"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Locked"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Tasks"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Tools"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Tasks"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"The following tools are available for administrators to manipulate the user list."
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Create New User"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Show Users In Role"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"roleName"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Reports"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Types"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"User List"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Roles Matrix"
argument_list|)
expr_stmt|;
block|}
comment|/*      * //User Role public void goToUserRolesPage() { clickLinkWithText( "User Roles" ); assertUserRolesPage(); }      */
specifier|public
name|void
name|assertUserRolesPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ [Admin] User Edit"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"[Admin] User Roles"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Username"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Full Name"
argument_list|)
expr_stmt|;
name|String
name|userRoles
init|=
literal|"Guest,Registered User,System Administrator,User Administrator,Global Repository Observer,Global Repository Manager,Repository Observer,Repository Manager,internal"
decl_stmt|;
name|String
index|[]
name|arrayRole
init|=
name|userRoles
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|userroles
range|:
name|arrayRole
control|)
name|assertTextPresent
argument_list|(
name|userroles
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertDeleteUserPage
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ [Admin] User Delete"
argument_list|)
expr_stmt|;
comment|// TODO
name|assertTextPresent
argument_list|(
literal|"[Admin] User Delete"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"The following user will be deleted:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Username: "
operator|+
name|username
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Delete User"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createUser
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|email
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|valid
parameter_list|)
block|{
name|createUser
argument_list|(
name|userName
argument_list|,
name|fullName
argument_list|,
name|email
argument_list|,
name|password
argument_list|,
name|password
argument_list|,
name|valid
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createUser
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|emailAd
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|confirmPassword
parameter_list|,
name|boolean
name|valid
parameter_list|)
block|{
comment|// login( getAdminUsername() , getAdminPassword() );
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/security/userlist.action"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Create New User"
argument_list|)
expr_stmt|;
name|assertCreateUserPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"user.username"
argument_list|,
name|userName
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"user.fullName"
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"user.email"
argument_list|,
name|emailAd
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
name|confirmPassword
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|assertUserRolesPage
argument_list|()
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
if|if
condition|(
name|valid
condition|)
block|{
name|String
index|[]
name|columnValues
init|=
block|{
name|userName
block|,
name|fullName
block|,
name|emailAd
block|}
decl_stmt|;
name|assertElementPresent
argument_list|(
name|XPathExpressionUtil
operator|.
name|getTableRow
argument_list|(
name|columnValues
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertCreateUserPage
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|deleteUser
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|emailAdd
parameter_list|)
block|{
name|deleteUser
argument_list|(
name|userName
argument_list|,
name|fullName
argument_list|,
name|emailAdd
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleteUser
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|emailAd
parameter_list|,
name|boolean
name|validated
parameter_list|,
name|boolean
name|locked
parameter_list|)
block|{
name|String
index|[]
name|columnValues
init|=
block|{
name|userName
block|,
name|fullName
block|,
name|emailAd
block|}
decl_stmt|;
comment|// clickLinkWithText( "userlist" );
name|clickLinkWithXPath
argument_list|(
literal|"//table[@id='ec_table']/tbody[2]/tr[3]/td[7]/a/img"
argument_list|)
expr_stmt|;
name|assertDeleteUserPage
argument_list|(
name|userName
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|assertElementNotPresent
argument_list|(
name|XPathExpressionUtil
operator|.
name|getTableRow
argument_list|(
name|columnValues
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|login
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|login
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
literal|true
argument_list|,
literal|"Login Page"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|login
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|valid
parameter_list|,
name|String
name|assertReturnPage
parameter_list|)
block|{
if|if
condition|(
name|isLinkPresent
argument_list|(
literal|"Login"
argument_list|)
condition|)
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|submitLoginPage
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
literal|false
argument_list|,
name|valid
argument_list|,
name|assertReturnPage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|valid
condition|)
block|{
name|assertUserLoggedIn
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|submitLoginPage
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|submitLoginPage
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|"Login Page"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|submitLoginPage
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|validUsernamePassword
parameter_list|)
block|{
name|submitLoginPage
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
literal|false
argument_list|,
name|validUsernamePassword
argument_list|,
literal|"Login Page"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|submitLoginPage
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|rememberMe
parameter_list|,
name|boolean
name|validUsernamePassword
parameter_list|,
name|String
name|assertReturnPage
parameter_list|)
block|{
name|assertLoginPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
if|if
condition|(
name|rememberMe
condition|)
block|{
name|checkField
argument_list|(
literal|"rememberMe"
argument_list|)
expr_stmt|;
block|}
name|clickButtonWithValue
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
if|if
condition|(
name|validUsernamePassword
condition|)
block|{
name|assertUserLoggedIn
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
literal|"Login Page"
operator|.
name|equals
argument_list|(
name|assertReturnPage
argument_list|)
condition|)
block|{
name|assertLoginPage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|assertPage
argument_list|(
name|assertReturnPage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|assertUserLoggedIn
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|assertTextPresent
argument_list|(
literal|"Current User:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Edit Details"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Logout"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
block|}
comment|// User Roles
specifier|public
name|void
name|assertUserRoleCheckBoxPresent
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|getSelenium
argument_list|()
operator|.
name|isElementPresent
argument_list|(
literal|"xpath=//input[@id='addRolesToUser_addNDSelectedRoles' and @name='addNDSelectedRoles' and @value='"
operator|+
name|value
operator|+
literal|"']"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertResourceRolesCheckBoxPresent
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|getSelenium
argument_list|()
operator|.
name|isElementPresent
argument_list|(
literal|"xpath=//input[@name='addDSelectedRoles' and @value='"
operator|+
name|value
operator|+
literal|"']"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|checkUserRoleWithValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|assertUserRoleCheckBoxPresent
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|click
argument_list|(
literal|"xpath=//input[@id='addRolesToUser_addNDSelectedRoles' and @name='addNDSelectedRoles' and @value='"
operator|+
name|value
operator|+
literal|"']"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|checkResourceRoleWithValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|assertResourceRolesCheckBoxPresent
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|click
argument_list|(
literal|"xpath=//input[@name='addDSelectedRoles' and @value='"
operator|+
name|value
operator|+
literal|"']"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|changePassword
parameter_list|(
name|String
name|oldPassword
parameter_list|,
name|String
name|newPassword
parameter_list|)
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Change Password"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"existingPassword"
argument_list|,
name|oldPassword
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newPassword"
argument_list|,
name|newPassword
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newPasswordConfirm"
argument_list|,
name|newPassword
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Change Password"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertCreateUserPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ [Admin] User Create"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"[Admin] User Create"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Username*:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.username"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Full Name*:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.fullName"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Email Address*:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.email"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password*:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.password"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Confirm Password*:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"user.confirmPassword"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Create User"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertLeftNavMenuWithRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
if|if
condition|(
name|role
operator|.
name|equals
argument_list|(
literal|"Guest"
argument_list|)
operator|||
name|role
operator|.
name|equals
argument_list|(
literal|"Registered User"
argument_list|)
operator|||
name|role
operator|.
name|equals
argument_list|(
literal|"Global Repository Observer"
argument_list|)
operator|||
name|role
operator|.
name|equals
argument_list|(
literal|"Repository Observer - internal"
argument_list|)
operator|||
name|role
operator|.
name|equals
argument_list|(
literal|"Repository Observer - snapshots"
argument_list|)
condition|)
block|{
name|assertTextPresent
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Find Artifact"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Browse"
argument_list|)
expr_stmt|;
name|assertLinkNotPresent
argument_list|(
literal|"Repositories"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|role
operator|.
name|equals
argument_list|(
literal|"User Administrator"
argument_list|)
condition|)
block|{
name|assertTextPresent
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Find Artifact"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Browse"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"User Management"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"User Roles"
argument_list|)
expr_stmt|;
name|assertLinkNotPresent
argument_list|(
literal|"Repositories"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|role
operator|.
name|equals
argument_list|(
literal|"Global Repository Manager"
argument_list|)
operator|||
name|role
operator|.
name|equals
argument_list|(
literal|"Repository Manager - internal"
argument_list|)
operator|||
name|role
operator|.
name|equals
argument_list|(
literal|"Repository Manager - snapshots"
argument_list|)
condition|)
block|{
name|assertTextPresent
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Find Artifact"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Browse"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Upload Artifact"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Delete Artifact"
argument_list|)
expr_stmt|;
name|assertLinkNotPresent
argument_list|(
literal|"Repositories"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTextPresent
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
name|String
name|navMenu
init|=
literal|"Find Artifact,Browse,Reports,User Management,User Roles,Appearance,Upload Artifact,Delete Artifact,Repository Groups,Repositories,Proxy Connectors,Legacy Support,Network Proxies,Repository Scanning"
decl_stmt|;
name|String
index|[]
name|arrayMenu
init|=
name|navMenu
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|navmenu
range|:
name|arrayMenu
control|)
name|assertLinkPresent
argument_list|(
name|navmenu
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Find Artifact
specifier|public
name|void
name|goToFindArtifactPage
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/findArtifact.action"
argument_list|)
expr_stmt|;
name|assertFindArtifactPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertFindArtifactPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Find Artifact"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Find Artifact"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Search for:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Checksum:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"q"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
block|}
comment|// Appearance
specifier|public
name|void
name|goToAppearancePage
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/configureAppearance.action"
argument_list|)
expr_stmt|;
name|assertAppearancePage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertAppearancePage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Configure Appearance"
argument_list|)
expr_stmt|;
name|String
name|appearance
init|=
literal|"Appearance,Organization Details,The logo in the top right of the screen is controlled by the following settings.,Organization Information,Name,URL,Logo URL"
decl_stmt|;
name|String
index|[]
name|arrayAppearance
init|=
name|appearance
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|appear
range|:
name|arrayAppearance
control|)
name|assertTextPresent
argument_list|(
name|appear
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Change your appearance"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addEditAppearance
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|logoUrl
parameter_list|)
block|{
name|setFieldValue
argument_list|(
literal|"organisationName"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"organisationUrl"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"organisationLogo"
argument_list|,
name|logoUrl
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Save"
argument_list|)
expr_stmt|;
block|}
comment|// Upload Artifact
specifier|public
name|void
name|goToAddArtifactPage
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/upload.action"
argument_list|)
expr_stmt|;
name|assertAddArtifactPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertAddArtifactPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Upload Artifact"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Upload Artifact"
argument_list|)
expr_stmt|;
name|String
name|artifact
init|=
literal|"Upload Artifact,Group Id*:,Artifact Id*:,Version*:,Packaging*:,Classifier:,Generate Maven 2 POM,Artifact File*:,POM File:,Repository Id:"
decl_stmt|;
name|String
index|[]
name|arrayArtifact
init|=
name|artifact
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|arrayartifact
range|:
name|arrayArtifact
control|)
name|assertTextPresent
argument_list|(
name|arrayartifact
argument_list|)
expr_stmt|;
name|String
name|artifactElements
init|=
literal|"upload_groupId,upload_artifactId,upload_version,upload_packaging,upload_classifier,upload_generatePom,upload_artifact,upload_pom,upload_repositoryId,upload_0"
decl_stmt|;
name|String
index|[]
name|arrayArtifactElements
init|=
name|artifactElements
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|artifactelements
range|:
name|arrayArtifactElements
control|)
name|assertElementPresent
argument_list|(
name|artifactelements
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|packaging
parameter_list|,
name|String
name|artifactFilePath
parameter_list|,
name|String
name|repositoryId
parameter_list|)
block|{
name|addArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|packaging
argument_list|,
literal|true
argument_list|,
name|artifactFilePath
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|packaging
parameter_list|,
name|boolean
name|generatePom
parameter_list|,
name|String
name|artifactFilePath
parameter_list|,
name|String
name|repositoryId
parameter_list|)
block|{
name|goToAddArtifactPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"packaging"
argument_list|,
name|packaging
argument_list|)
expr_stmt|;
if|if
condition|(
name|generatePom
condition|)
block|{
name|checkField
argument_list|(
literal|"generatePom"
argument_list|)
expr_stmt|;
block|}
name|String
name|path
decl_stmt|;
if|if
condition|(
name|artifactFilePath
operator|!=
literal|null
operator|&&
name|artifactFilePath
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|artifactFilePath
argument_list|)
decl_stmt|;
try|try
block|{
name|path
operator|=
name|f
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|path
operator|=
name|f
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|path
operator|=
name|artifactFilePath
expr_stmt|;
block|}
name|setFieldValue
argument_list|(
literal|"artifact"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"repositoryId"
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|goToRepositoriesPage
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getTitle
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Apache Archiva \\ Administration - Repositories"
argument_list|)
condition|)
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/repositories.action"
argument_list|)
expr_stmt|;
block|}
name|assertRepositoriesPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertRepositoriesPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Repositories"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Administration - Repositories"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Managed Repositories"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Remote Repositories"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addManagedRepository
parameter_list|(
name|String
name|identifier
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|directory
parameter_list|,
name|String
name|indexDirectory
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|cron
parameter_list|,
name|String
name|daysOlder
parameter_list|,
name|String
name|retentionCount
parameter_list|)
block|{
comment|// goToRepositoriesPage();
comment|// clickLinkWithText( "Add" );
name|setFieldValue
argument_list|(
literal|"repository.id"
argument_list|,
name|identifier
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.location"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.indexDir"
argument_list|,
name|indexDirectory
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"repository.layout"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.refreshCronExpression"
argument_list|,
name|cron
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.daysOlder"
argument_list|,
name|daysOlder
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.retentionCount"
argument_list|,
name|retentionCount
argument_list|)
expr_stmt|;
comment|// TODO
name|clickButtonWithValue
argument_list|(
literal|"Add Repository"
argument_list|)
expr_stmt|;
block|}
comment|// add managed repository and its staging repository
specifier|public
name|void
name|addStagingRepository
parameter_list|(
name|String
name|identifier
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|directory
parameter_list|,
name|String
name|indexDirectory
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|cron
parameter_list|,
name|String
name|daysOlder
parameter_list|,
name|String
name|retentionCount
parameter_list|)
block|{
name|setFieldValue
argument_list|(
literal|"repository.id"
argument_list|,
name|identifier
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.location"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.indexDir"
argument_list|,
name|indexDirectory
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"repository.layout"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.refreshCronExpression"
argument_list|,
name|cron
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.daysOlder"
argument_list|,
name|daysOlder
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repository.retentionCount"
argument_list|,
name|retentionCount
argument_list|)
expr_stmt|;
name|checkField
argument_list|(
literal|"stageNeeded"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Add Repository"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|logout
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Logout"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"Current User:"
argument_list|)
expr_stmt|;
name|assertLinkNotPresent
argument_list|(
literal|"Edit Details"
argument_list|)
expr_stmt|;
name|assertLinkNotPresent
argument_list|(
literal|"Logout"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

