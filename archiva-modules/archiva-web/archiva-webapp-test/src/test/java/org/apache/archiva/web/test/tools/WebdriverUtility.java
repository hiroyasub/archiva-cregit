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
name|tools
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebClient
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
name|openqa
operator|.
name|selenium
operator|.
name|*
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
name|htmlunit
operator|.
name|HtmlUnitDriver
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
name|remote
operator|.
name|DesiredCapabilities
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
name|remote
operator|.
name|RemoteWebDriver
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * Created by martin_s on 04.06.17.  */
end_comment

begin_class
specifier|public
class|class
name|WebdriverUtility
block|{
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|WebdriverUtility
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|WebDriver
name|newWebDriver
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
name|String
name|seleniumHost
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"seleniumHost"
argument_list|,
literal|"localhost"
argument_list|)
decl_stmt|;
name|int
name|seleniumPort
init|=
name|Integer
operator|.
name|getInteger
argument_list|(
literal|"seleniumPort"
argument_list|,
literal|4444
argument_list|)
decl_stmt|;
name|boolean
name|seleniumRemote
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"seleniumRemote"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|newWebDriver
argument_list|(
name|seleniumBrowser
argument_list|,
name|seleniumHost
argument_list|,
name|seleniumPort
argument_list|,
name|seleniumRemote
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|WebDriver
name|newWebDriver
parameter_list|(
name|String
name|seleniumBrowser
parameter_list|,
name|String
name|seleniumHost
parameter_list|,
name|int
name|seleniumPort
parameter_list|,
name|boolean
name|seleniumRemote
parameter_list|)
block|{
try|try
block|{
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
if|if
condition|(
name|seleniumRemote
condition|)
block|{
return|return
operator|new
name|RemoteWebDriver
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://"
operator|+
name|seleniumHost
operator|+
literal|":"
operator|+
name|seleniumPort
operator|+
literal|"/wd/hub"
argument_list|)
argument_list|,
name|DesiredCapabilities
operator|.
name|chrome
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ChromeDriver
argument_list|(  )
return|;
block|}
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
if|if
condition|(
name|seleniumRemote
condition|)
block|{
return|return
operator|new
name|RemoteWebDriver
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://"
operator|+
name|seleniumHost
operator|+
literal|":"
operator|+
name|seleniumPort
operator|+
literal|"/wd/hub"
argument_list|)
argument_list|,
name|DesiredCapabilities
operator|.
name|safari
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|SafariDriver
argument_list|()
return|;
block|}
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
if|if
condition|(
name|seleniumRemote
condition|)
block|{
return|return
operator|new
name|RemoteWebDriver
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://"
operator|+
name|seleniumHost
operator|+
literal|":"
operator|+
name|seleniumPort
operator|+
literal|"/wd/hub"
argument_list|)
argument_list|,
name|DesiredCapabilities
operator|.
name|internetExplorer
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
operator|new
name|InternetExplorerDriver
argument_list|(  )
expr_stmt|;
block|}
block|}
if|if
condition|(
name|StringUtils
operator|.
name|contains
argument_list|(
name|seleniumBrowser
argument_list|,
literal|"firefox"
argument_list|)
condition|)
block|{
if|if
condition|(
name|seleniumRemote
condition|)
block|{
return|return
operator|new
name|RemoteWebDriver
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://"
operator|+
name|seleniumHost
operator|+
literal|":"
operator|+
name|seleniumPort
operator|+
literal|"/wd/hub"
argument_list|)
argument_list|,
name|DesiredCapabilities
operator|.
name|firefox
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|FirefoxDriver
argument_list|()
return|;
block|}
block|}
if|if
condition|(
name|seleniumRemote
condition|)
block|{
return|return
operator|new
name|RemoteWebDriver
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://"
operator|+
name|seleniumHost
operator|+
literal|":"
operator|+
name|seleniumPort
operator|+
literal|"/wd/hub"
argument_list|)
argument_list|,
name|DesiredCapabilities
operator|.
name|htmlUnit
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
name|DesiredCapabilities
name|capabilities
init|=
name|DesiredCapabilities
operator|.
name|htmlUnit
argument_list|()
decl_stmt|;
name|capabilities
operator|.
name|setJavascriptEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|capabilities
operator|.
name|setVersion
argument_list|(
literal|"firefox-52"
argument_list|)
expr_stmt|;
name|HtmlUnitDriver
name|driver
init|=
operator|new
name|HtmlUnitDriver
argument_list|(
name|capabilities
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|WebClient
name|modifyWebClient
parameter_list|(
name|WebClient
name|client
parameter_list|)
block|{
name|client
operator|.
name|getOptions
argument_list|()
operator|.
name|setThrowExceptionOnFailingStatusCode
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|client
operator|.
name|getOptions
argument_list|()
operator|.
name|setThrowExceptionOnScriptError
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|client
operator|.
name|getOptions
argument_list|()
operator|.
name|setCssEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
block|}
decl_stmt|;
return|return
name|driver
return|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Initializion of remote driver failed"
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getBaseUrl
parameter_list|()
block|{
if|if
condition|(
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"baseUrl"
argument_list|)
condition|)
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"baseUrl"
argument_list|)
return|;
block|}
name|int
name|containerPort
init|=
literal|7777
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"container.http.port"
argument_list|)
condition|)
block|{
name|containerPort
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"container.http.port"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"container.propertiesPortFilePath"
argument_list|)
condition|)
block|{
name|Properties
name|portProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|inputStream
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"container.propertiesPortFilePath"
argument_list|)
argument_list|)
argument_list|)
init|)
block|{
name|portProperties
operator|.
name|load
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error during property loading with containger.propertiesPortFilePath"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|portProperties
operator|.
name|containsKey
argument_list|(
literal|"tomcat.maven.http.port"
argument_list|)
condition|)
block|{
name|containerPort
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portProperties
operator|.
name|getProperty
argument_list|(
literal|"tomcat.maven.http.port"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|containerPort
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portProperties
operator|.
name|getProperty
argument_list|(
literal|"container.http.port"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|"http://localhost:"
operator|+
name|containerPort
operator|+
literal|"/archiva"
return|;
block|}
specifier|public
specifier|static
name|Path
name|takeScreenShot
parameter_list|(
name|String
name|fileName
parameter_list|,
name|WebDriver
name|driver
parameter_list|)
block|{
name|Path
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Path
name|snapDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target"
argument_list|,
literal|"errorshtmlsnap"
argument_list|)
decl_stmt|;
name|Path
name|screenShotDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target"
argument_list|,
literal|"screenshots"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|snapDir
argument_list|)
condition|)
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|snapDir
argument_list|)
expr_stmt|;
block|}
name|Path
name|htmlFile
init|=
name|snapDir
operator|.
name|resolve
argument_list|(
name|fileName
operator|+
literal|".html"
argument_list|)
decl_stmt|;
name|Path
name|screenShotFile
init|=
name|screenShotDir
operator|.
name|resolve
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|String
name|pageSource
init|=
literal|null
decl_stmt|;
name|String
name|encoding
init|=
literal|"ISO-8859-1"
decl_stmt|;
try|try
block|{
name|pageSource
operator|=
operator|(
operator|(
name|JavascriptExecutor
operator|)
name|driver
operator|)
operator|.
name|executeScript
argument_list|(
literal|"return document.documentElement.outerHTML;"
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Could not create html source by javascript"
argument_list|)
expr_stmt|;
name|pageSource
operator|=
name|driver
operator|.
name|getPageSource
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|pageSource
operator|.
name|contains
argument_list|(
literal|"encoding=\""
argument_list|)
condition|)
block|{
name|encoding
operator|=
name|pageSource
operator|.
name|replaceFirst
argument_list|(
literal|".*encoding=\"([^\"]+)\".*"
argument_list|,
literal|"$1"
argument_list|)
expr_stmt|;
block|}
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|htmlFile
operator|.
name|toFile
argument_list|()
argument_list|,
name|pageSource
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
try|try
block|{
name|File
name|scrs
init|=
operator|(
operator|(
name|TakesScreenshot
operator|)
name|driver
operator|)
operator|.
name|getScreenshotAs
argument_list|(
name|OutputType
operator|.
name|FILE
argument_list|)
decl_stmt|;
name|result
operator|=
name|scrs
operator|.
name|toPath
argument_list|()
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|result
argument_list|,
name|screenShotFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Could not create screenshot: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Creating screenshot failed "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

