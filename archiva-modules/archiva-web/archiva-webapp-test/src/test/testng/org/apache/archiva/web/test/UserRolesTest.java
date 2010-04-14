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
name|web
operator|.
name|test
operator|.
name|parent
operator|.
name|AbstractArchivaTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|annotations
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"userroles"
block|}
argument_list|,
name|dependsOnMethods
operator|=
block|{
literal|"testWithCorrectUsernamePassword"
block|}
argument_list|)
specifier|public
class|class
name|UserRolesTest
extends|extends
name|AbstractArchivaTest
block|{
specifier|public
name|void
name|testBasicAddDeleteUser
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"GUEST_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"GUEST_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|deleteUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testBasicAddDeleteUser"
block|}
argument_list|)
specifier|public
name|void
name|testUserWithGuestRole
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"GUEST_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"GUEST_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit Roles"
argument_list|)
expr_stmt|;
name|checkUserRoleWithValue
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|username
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|)
expr_stmt|;
name|changePassword
argument_list|(
name|getUserRolePassword
argument_list|()
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password successfully changed"
argument_list|)
expr_stmt|;
name|assertLeftNavMenuWithRole
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testUserWithGuestRole"
block|}
argument_list|)
specifier|public
name|void
name|testUserWithRegisteredUserRole
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"REGISTERED_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"REGISTERED_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit Roles"
argument_list|)
expr_stmt|;
name|checkUserRoleWithValue
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|username
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|)
expr_stmt|;
name|changePassword
argument_list|(
name|getUserRolePassword
argument_list|()
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password successfully changed"
argument_list|)
expr_stmt|;
name|assertLeftNavMenuWithRole
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testUserWithRegisteredUserRole"
block|}
argument_list|)
specifier|public
name|void
name|testUserWithSysAdminUserRole
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"SYSAD_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"SYSAD_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit Roles"
argument_list|)
expr_stmt|;
name|checkUserRoleWithValue
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|username
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|)
expr_stmt|;
name|changePassword
argument_list|(
name|getUserRolePassword
argument_list|()
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password successfully changed"
argument_list|)
expr_stmt|;
name|assertLeftNavMenuWithRole
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testUserWithSysAdminUserRole"
block|}
argument_list|)
specifier|public
name|void
name|testUserWithUserAdminUserRole
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"USERADMIN_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"USERADMIN_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit Roles"
argument_list|)
expr_stmt|;
name|checkUserRoleWithValue
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|username
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|)
expr_stmt|;
name|changePassword
argument_list|(
name|getUserRolePassword
argument_list|()
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password successfully changed"
argument_list|)
expr_stmt|;
name|assertLeftNavMenuWithRole
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testUserWithUserAdminUserRole"
block|}
argument_list|)
specifier|public
name|void
name|testUserWithGlobalRepoManagerRole
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"GLOBALREPOMANAGER_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"GLOBALREPOMANAGER_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit Roles"
argument_list|)
expr_stmt|;
name|checkUserRoleWithValue
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|username
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|)
expr_stmt|;
name|changePassword
argument_list|(
name|getUserRolePassword
argument_list|()
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password successfully changed"
argument_list|)
expr_stmt|;
name|assertLeftNavMenuWithRole
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testUserWithGlobalRepoManagerRole"
block|}
argument_list|)
specifier|public
name|void
name|testUserWithGlobalRepoObserverRole
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"GLOBALREPOOBSERVER_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"GLOBALREPOOBSERVER_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit Roles"
argument_list|)
expr_stmt|;
name|checkUserRoleWithValue
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|username
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|)
expr_stmt|;
name|changePassword
argument_list|(
name|getUserRolePassword
argument_list|()
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password successfully changed"
argument_list|)
expr_stmt|;
name|assertLeftNavMenuWithRole
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testUserWithGlobalRepoObserverRole"
block|}
argument_list|)
specifier|public
name|void
name|testUserWithRepoManagerInternalRole
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"REPOMANAGER_INTERNAL_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"REPOMANAGER_INTERNAL_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit Roles"
argument_list|)
expr_stmt|;
name|checkResourceRoleWithValue
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|username
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|)
expr_stmt|;
name|changePassword
argument_list|(
name|getUserRolePassword
argument_list|()
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password successfully changed"
argument_list|)
expr_stmt|;
name|assertLeftNavMenuWithRole
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*      * @Test (dependsOnMethods = { "testUserWithRepoManagerInternalRole" } ) public void      * testUserWithRepoManagerSnapshotsRole() { username = getProperty("REPOMANAGER_SNAPSHOTS_USERNAME"); fullname =      * getProperty("REPOMANAGER_SNAPSHOTS_FULLNAME"); createUser(username, fullname, getUserEmail(),      * getUserRolePassword(), true); clickLinkWithText( username ); clickLinkWithText( "Edit Roles" );      * checkResourceRoleWithValue( fullname ); clickButtonWithValue( "Submit" ); clickLinkWithText("Logout");      * login(username, getUserRolePassword()); changePassword( getUserRolePassword(), getUserRoleNewPassword()); // this      * section will be removed if issue from redback after changing password will be fixed. getSelenium().goBack();      * clickLinkWithText("Logout"); //assertTextPresent("You are already logged in."); login(username,      * getUserRoleNewPassword()); assertLeftNavMenuWithRole( fullname ); clickLinkWithText("Logout"); login(      * getAdminUsername() , getAdminPassword() ); }      */
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testUserWithRepoManagerInternalRole"
block|}
argument_list|)
specifier|public
name|void
name|testUserWithRepoObserverInternalRole
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"REPOOBSERVER_INTERNAL_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"REPOOBSERVER_INTERNAL_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit Roles"
argument_list|)
expr_stmt|;
name|checkResourceRoleWithValue
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|username
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|)
expr_stmt|;
name|changePassword
argument_list|(
name|getUserRolePassword
argument_list|()
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
comment|// this section will be removed if issue from redback after changing password will be fixed.
name|getSelenium
argument_list|()
operator|.
name|goBack
argument_list|()
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
comment|// assertTextPresent("You are already logged in.");
name|login
argument_list|(
name|username
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertLeftNavMenuWithRole
argument_list|(
name|fullname
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*      * @Test (dependsOnMethods = { "testUserWithRepoObserverInternalRole" } ) public void      * testUserWithRepoObserverSnapshotsRole() { username = getProperty( "REPOOBSERVER_SNAPSHOTS_USERNAME" ); fullname =      * getProperty( "REPOOBSERVER_SNAPSHOTS_FULLNAME" ); createUser(username, fullname, getUserEmail(),      * getUserRolePassword(), true); clickLinkWithText( username ); clickLinkWithText( "Edit Roles" );      * checkResourceRoleWithValue( fullname ); clickButtonWithValue( "Submit" ); clickLinkWithText("Logout");      * login(username, getUserRolePassword()); changePassword( getUserRolePassword(), getUserRoleNewPassword()); // this      * section will be removed if issue from redback after changing password will be fixed. getSelenium().goBack();      * clickLinkWithText("Logout"); //assertTextPresent("You are already logged in."); login(username,      * getUserRoleNewPassword()); assertLeftNavMenuWithRole( fullname ); clickLinkWithText("Logout"); login(      * getAdminUsername() , getAdminPassword() ); }      */
block|}
end_class

end_unit

