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
name|BeforeSuite
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
name|Optional
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
name|Parameters
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
literal|"about"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|ArchivaAdminTest
extends|extends
name|AbstractArchivaTest
block|{
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
annotation|@
name|Override
annotation|@
name|BeforeSuite
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
name|BeforeTest
annotation|@
name|Parameters
argument_list|(
block|{
literal|"baseUrl"
block|,
literal|"browser"
block|,
literal|"seleniumHost"
block|,
literal|"seleniumPort"
block|}
argument_list|)
specifier|public
name|void
name|initializeArchiva
parameter_list|(
name|String
name|baseUrl
parameter_list|,
name|String
name|browser
parameter_list|,
annotation|@
name|Optional
argument_list|(
literal|"localhost"
argument_list|)
name|String
name|seleniumHost
parameter_list|,
annotation|@
name|Optional
argument_list|(
literal|"4444"
argument_list|)
name|int
name|seleniumPort
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|open
argument_list|(
name|baseUrl
argument_list|,
name|browser
argument_list|,
name|seleniumHost
argument_list|,
name|seleniumPort
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
name|baseUrl
argument_list|)
expr_stmt|;
name|String
name|title
init|=
name|getSelenium
argument_list|()
operator|.
name|getTitle
argument_list|()
decl_stmt|;
if|if
condition|(
name|title
operator|.
name|equals
argument_list|(
literal|"Apache Archiva \\ Create Admin User"
argument_list|)
condition|)
block|{
name|assertCreateAdmin
argument_list|()
expr_stmt|;
name|String
name|fullname
init|=
name|getProperty
argument_list|(
literal|"ADMIN_FULLNAME"
argument_list|)
decl_stmt|;
name|String
name|username
init|=
name|getProperty
argument_list|(
literal|"ADMIN_USERNAME"
argument_list|)
decl_stmt|;
name|String
name|mail
init|=
name|getProperty
argument_list|(
literal|"ADMIN_EMAIL"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|getProperty
argument_list|(
literal|"ADMIN_PASSWORD"
argument_list|)
decl_stmt|;
name|submitAdminData
argument_list|(
name|fullname
argument_list|,
name|mail
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|assertAuthenticatedPage
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Logout"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

