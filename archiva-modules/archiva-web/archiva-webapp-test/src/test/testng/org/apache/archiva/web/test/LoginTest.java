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
name|AfterTest
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
name|BeforeTest
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

begin_comment
comment|/*  * Bug in TestNG. TESTNG-285: @Test(sequential=true) works incorrectly for classes with inheritance  * http://code.google.com/p/testng/source/browse/trunk/CHANGES.txt  * Waiting 5.9 release. It's comming soon.  */
end_comment

begin_comment
comment|/**  * Based on LoginTest of Emmanuel Venisse test.  *   * @author JosÃ© Morales MartÃ­nez  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"login"
block|}
argument_list|,
name|dependsOnGroups
operator|=
block|{
literal|"about"
block|}
argument_list|)
specifier|public
class|class
name|LoginTest
extends|extends
name|AbstractArchivaTest
block|{
specifier|public
name|void
name|testWithBadUsername
parameter_list|()
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"loginForm_username"
argument_list|,
literal|"badUsername"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|waitForPageToLoad
argument_list|(
name|maxWaitTimeInMs
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You have entered an incorrect username and/or password"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testWithBadUsername"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testWithBadPassword
parameter_list|()
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"loginForm_username"
argument_list|,
name|getProperty
argument_list|(
literal|"ADMIN_USERNAME"
argument_list|)
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"loginForm_password"
argument_list|,
literal|"badPassword"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|click
argument_list|(
literal|"loginForm__login"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|waitForPageToLoad
argument_list|(
name|maxWaitTimeInMs
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You have entered an incorrect username and/or password"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testWithBadPassword"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testWithEmptyUsername
parameter_list|()
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"loginForm_password"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|waitForPageToLoad
argument_list|(
name|maxWaitTimeInMs
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"User Name is required"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testWithEmptyUsername"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testWithEmptyPassword
parameter_list|()
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"loginForm_username"
argument_list|,
name|getProperty
argument_list|(
literal|"ADMIN_USERNAME"
argument_list|)
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|waitForPageToLoad
argument_list|(
name|maxWaitTimeInMs
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You have entered an incorrect username and/or password"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"loginSuccess"
block|}
argument_list|,
name|dependsOnMethods
operator|=
block|{
literal|"testWithEmptyPassword"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testWithCorrectUsernamePassword
parameter_list|()
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"loginForm_username"
argument_list|,
name|getProperty
argument_list|(
literal|"ADMIN_USERNAME"
argument_list|)
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"loginForm_password"
argument_list|,
name|getProperty
argument_list|(
literal|"ADMIN_PASSWORD"
argument_list|)
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Login"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|waitForPageToLoad
argument_list|(
name|maxWaitTimeInMs
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Edit Details"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Logout"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|getProperty
argument_list|(
literal|"ADMIN_USERNAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BeforeTest
specifier|public
name|void
name|open
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|AfterTest
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

