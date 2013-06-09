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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fluentlenium
operator|.
name|adapter
operator|.
name|FluentTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fluentlenium
operator|.
name|core
operator|.
name|domain
operator|.
name|FluentList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fluentlenium
operator|.
name|core
operator|.
name|domain
operator|.
name|FluentWebElement
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fest
operator|.
name|assertions
operator|.
name|Assertions
operator|.
name|assertThat
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
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|WebDriver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|WebElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|chrome
operator|.
name|ChromeDriver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|firefox
operator|.
name|FirefoxDriver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|ie
operator|.
name|InternetExplorerDriver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|safari
operator|.
name|SafariDriver
import|;
end_import

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
name|FileInputStream
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fluentlenium
operator|.
name|core
operator|.
name|Fluent
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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|WebDriverBrowseTest
extends|extends
name|FluentTest
block|{
annotation|@
name|Override
specifier|public
name|Fluent
name|takeScreenShot
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
try|try
block|{
comment|// save html to have a minimum feedback if jenkins firefox not up
name|File
name|fileNameHTML
init|=
operator|new
name|File
argument_list|(
name|fileName
operator|+
literal|".html"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|fileNameHTML
argument_list|,
name|getDriver
argument_list|()
operator|.
name|getPageSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|super
operator|.
name|takeScreenShot
argument_list|(
name|fileName
argument_list|)
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|init
parameter_list|()
block|{
name|setSnapshotMode
argument_list|(
name|Mode
operator|.
name|TAKE_SNAPSHOT_ON_FAIL
argument_list|)
expr_stmt|;
name|setSnapshotPath
argument_list|(
operator|new
name|File
argument_list|(
literal|"target"
argument_list|,
literal|"errorshtmlsnap"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|simpletest
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|load
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|Properties
name|tomcatPortProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|tomcatPortProperties
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"tomcat.propertiesPortFilePath"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|tomcatPort
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|tomcatPortProperties
operator|.
name|getProperty
argument_list|(
literal|"tomcat.maven.http.port"
argument_list|)
argument_list|)
decl_stmt|;
name|goTo
argument_list|(
literal|"http://localhost:"
operator|+
name|tomcatPort
operator|+
literal|"/archiva/index.html?request_lang=en"
argument_list|)
expr_stmt|;
comment|// wait until topbar-menu-container is feeded
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
literal|"#topbar-menu"
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
name|FluentList
argument_list|<
name|FluentWebElement
argument_list|>
name|elements
init|=
name|find
argument_list|(
literal|"#create-admin-link-a"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|elements
operator|.
name|isEmpty
argument_list|()
operator|&&
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isDisplayed
argument_list|()
condition|)
block|{
name|WebElement
name|webElement
init|=
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Create Admin User"
argument_list|,
name|webElement
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|webElement
operator|.
name|click
argument_list|()
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
literal|"#user-create"
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|find
argument_list|(
literal|"#username"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|find
argument_list|(
literal|"#password"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|find
argument_list|(
literal|"#confirmPassword"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|find
argument_list|(
literal|"#email"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|fill
argument_list|(
literal|"#fullname"
argument_list|)
operator|.
name|with
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
literal|"ADMIN_FULLNAME"
argument_list|)
argument_list|)
expr_stmt|;
name|fill
argument_list|(
literal|"#email"
argument_list|)
operator|.
name|with
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
literal|"ADMIN_EMAIL"
argument_list|)
argument_list|)
expr_stmt|;
name|fill
argument_list|(
literal|"#password"
argument_list|)
operator|.
name|with
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
literal|"ADMIN_PASSWORD"
argument_list|)
argument_list|)
expr_stmt|;
name|fill
argument_list|(
literal|"#confirmPassword"
argument_list|)
operator|.
name|with
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
literal|"ADMIN_PASSWORD"
argument_list|)
argument_list|)
expr_stmt|;
name|find
argument_list|(
literal|"#user-create-form-register-button"
argument_list|)
operator|.
name|click
argument_list|()
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
literal|"#logout-link"
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
name|FluentList
argument_list|<
name|FluentWebElement
argument_list|>
name|elementss
init|=
name|find
argument_list|(
literal|"#menu-find-browse-a"
argument_list|)
decl_stmt|;
name|WebElement
name|webElsement
init|=
name|elementss
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|webElsement
operator|.
name|click
argument_list|()
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
literal|"#main_browse_result"
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
comment|// give me search page :( not  browse page
name|takeScreenShot
argument_list|(
literal|"search"
argument_list|)
expr_stmt|;
name|goTo
argument_list|(
literal|"http://localhost:"
operator|+
name|tomcatPort
operator|+
literal|"/archiva/index.html#browse?request_lang=en"
argument_list|)
expr_stmt|;
name|takeScreenShot
argument_list|(
literal|"browse"
argument_list|)
expr_stmt|;
comment|// give me a browse page
block|}
else|else
block|{
name|elements
operator|=
name|find
argument_list|(
literal|"#login-link-a"
argument_list|)
expr_stmt|;
name|WebElement
name|webElement
init|=
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"LOGIN"
argument_list|,
name|webElement
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|WebDriver
name|getDefaultDriver
parameter_list|()
block|{
name|String
name|seleniumBrowser
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"selenium.browser"
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|contains
argument_list|(
name|seleniumBrowser
argument_list|,
literal|"chrome"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ChromeDriver
argument_list|()
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|contains
argument_list|(
name|seleniumBrowser
argument_list|,
literal|"safari"
argument_list|)
condition|)
block|{
return|return
operator|new
name|SafariDriver
argument_list|()
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|contains
argument_list|(
name|seleniumBrowser
argument_list|,
literal|"iexplore"
argument_list|)
condition|)
block|{
return|return
operator|new
name|InternetExplorerDriver
argument_list|()
return|;
block|}
return|return
operator|new
name|FirefoxDriver
argument_list|()
return|;
block|}
block|}
end_class

end_unit
