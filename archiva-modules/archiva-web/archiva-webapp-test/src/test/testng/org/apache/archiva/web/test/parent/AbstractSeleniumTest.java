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

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|selenium
operator|.
name|DefaultSelenium
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|selenium
operator|.
name|Selenium
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
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|Assert
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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

begin_comment
comment|/**  * @author<a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>  * @version $Id: AbstractSeleniumTestCase.java 761154 2009-04-02 03:31:19Z wsmoak $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSeleniumTest
block|{
specifier|public
specifier|static
name|String
name|baseUrl
decl_stmt|;
specifier|public
specifier|static
name|String
name|maxWaitTimeInMs
decl_stmt|;
specifier|private
specifier|static
name|ThreadLocal
argument_list|<
name|Selenium
argument_list|>
name|selenium
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Selenium
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|Properties
name|p
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|PROPERTIES_SEPARATOR
init|=
literal|"="
decl_stmt|;
specifier|public
name|void
name|open
parameter_list|()
throws|throws
name|Exception
block|{
name|p
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
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
literal|"testng.properties"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Initialize selenium      */
specifier|public
name|void
name|open
parameter_list|(
name|String
name|baseUrl
parameter_list|,
name|String
name|browser
parameter_list|,
name|String
name|seleniumHost
parameter_list|,
name|int
name|seleniumPort
parameter_list|,
name|String
name|maxWaitTimeInMs
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|AbstractSeleniumTest
operator|.
name|baseUrl
operator|=
name|baseUrl
expr_stmt|;
name|AbstractSeleniumTest
operator|.
name|maxWaitTimeInMs
operator|=
name|maxWaitTimeInMs
expr_stmt|;
if|if
condition|(
name|getSelenium
argument_list|()
operator|==
literal|null
condition|)
block|{
name|DefaultSelenium
name|s
init|=
operator|new
name|DefaultSelenium
argument_list|(
name|seleniumHost
argument_list|,
name|seleniumPort
argument_list|,
name|browser
argument_list|,
name|baseUrl
argument_list|)
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
name|s
operator|.
name|setTimeout
argument_list|(
name|maxWaitTimeInMs
argument_list|)
expr_stmt|;
name|selenium
operator|.
name|set
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// yes
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
block|}
specifier|public
specifier|static
name|Selenium
name|getSelenium
parameter_list|()
block|{
return|return
name|selenium
operator|==
literal|null
condition|?
literal|null
else|:
name|selenium
operator|.
name|get
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|p
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getEscapeProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|InputStream
name|input
init|=
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
literal|"testng.properties"
argument_list|)
decl_stmt|;
name|String
name|value
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|lines
decl_stmt|;
try|try
block|{
name|lines
operator|=
name|IOUtils
operator|.
name|readLines
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|lines
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|String
name|l
range|:
name|lines
control|)
block|{
if|if
condition|(
name|l
operator|!=
literal|null
operator|&&
name|l
operator|.
name|startsWith
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|int
name|indexSeparator
init|=
name|l
operator|.
name|indexOf
argument_list|(
name|PROPERTIES_SEPARATOR
argument_list|)
decl_stmt|;
name|value
operator|=
name|l
operator|.
name|substring
argument_list|(
name|indexSeparator
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
return|return
name|value
return|;
block|}
comment|/**      * Close selenium session. Called from AfterSuite method of sub-class      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|getSelenium
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getSelenium
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
name|selenium
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|// *******************************************************
comment|// Auxiliar methods. This method help us and simplify test.
comment|// *******************************************************
specifier|public
name|void
name|assertFieldValue
parameter_list|(
name|String
name|fieldValue
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
name|assertElementPresent
argument_list|(
name|fieldName
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|fieldValue
argument_list|,
name|getSelenium
argument_list|()
operator|.
name|getValue
argument_list|(
name|fieldName
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertPage
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getTitle
argument_list|()
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
comment|// Collapse spaces
return|return
name|getSelenium
argument_list|()
operator|.
name|getTitle
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"[ \n\r]+"
argument_list|,
literal|" "
argument_list|)
return|;
block|}
specifier|public
name|String
name|getHtmlContent
parameter_list|()
block|{
return|return
name|getSelenium
argument_list|()
operator|.
name|getHtmlSource
argument_list|()
return|;
block|}
specifier|public
name|String
name|getText
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
return|return
name|getSelenium
argument_list|()
operator|.
name|getText
argument_list|(
name|locator
argument_list|)
return|;
block|}
specifier|public
name|void
name|assertTextPresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|isTextPresent
argument_list|(
name|text
argument_list|)
argument_list|,
literal|"'"
operator|+
name|text
operator|+
literal|"' isn't present."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertTextNotPresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|isTextPresent
argument_list|(
name|text
argument_list|)
argument_list|,
literal|"'"
operator|+
name|text
operator|+
literal|"' is present."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertElementPresent
parameter_list|(
name|String
name|elementLocator
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|isElementPresent
argument_list|(
name|elementLocator
argument_list|)
argument_list|,
literal|"'"
operator|+
name|elementLocator
operator|+
literal|"' isn't present."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertElementNotPresent
parameter_list|(
name|String
name|elementLocator
parameter_list|)
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|isElementPresent
argument_list|(
name|elementLocator
argument_list|)
argument_list|,
literal|"'"
operator|+
name|elementLocator
operator|+
literal|"' is present."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertLinkPresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|isElementPresent
argument_list|(
literal|"link="
operator|+
name|text
argument_list|)
argument_list|,
literal|"The link '"
operator|+
name|text
operator|+
literal|"' isn't present."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertLinkNotPresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|isElementPresent
argument_list|(
literal|"link="
operator|+
name|text
argument_list|)
argument_list|,
literal|"The link('"
operator|+
name|text
operator|+
literal|"' is present."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertImgWithAlt
parameter_list|(
name|String
name|alt
parameter_list|)
block|{
name|assertElementPresent
argument_list|(
literal|"/Â¯img[@alt='"
operator|+
name|alt
operator|+
literal|"']"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertImgWithAltAtRowCol
parameter_list|(
name|boolean
name|isALink
parameter_list|,
name|String
name|alt
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|String
name|locator
init|=
literal|"//tr["
operator|+
name|row
operator|+
literal|"]/td["
operator|+
name|column
operator|+
literal|"]/"
decl_stmt|;
name|locator
operator|+=
name|isALink
condition|?
literal|"a/"
else|:
literal|""
expr_stmt|;
name|locator
operator|+=
literal|"img[@alt='"
operator|+
name|alt
operator|+
literal|"']"
expr_stmt|;
name|assertElementPresent
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertImgWithAltNotPresent
parameter_list|(
name|String
name|alt
parameter_list|)
block|{
name|assertElementNotPresent
argument_list|(
literal|"/Â¯img[@alt='"
operator|+
name|alt
operator|+
literal|"']"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertCellValueFromTable
parameter_list|(
name|String
name|expected
parameter_list|,
name|String
name|tableElement
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|getCellValueFromTable
argument_list|(
name|tableElement
argument_list|,
name|row
argument_list|,
name|column
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isTextPresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|getSelenium
argument_list|()
operator|.
name|isTextPresent
argument_list|(
name|text
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isLinkPresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|isElementPresent
argument_list|(
literal|"link="
operator|+
name|text
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isElementPresent
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
return|return
name|getSelenium
argument_list|()
operator|.
name|isElementPresent
argument_list|(
name|locator
argument_list|)
return|;
block|}
specifier|public
name|void
name|waitPage
parameter_list|()
block|{
comment|// TODO define a smaller maxWaitTimeJsInMs for wait javascript response for browser side validation
comment|//getSelenium().waitForPageToLoad( maxWaitTimeInMs );
comment|// http://jira.openqa.org/browse/SRC-302
name|getSelenium
argument_list|()
operator|.
name|waitForCondition
argument_list|(
literal|"selenium.isElementPresent('document.body');"
argument_list|,
name|maxWaitTimeInMs
argument_list|)
expr_stmt|;
comment|/*         try         {             Thread.sleep( 1000 );         }         catch ( InterruptedException e )         {             throw new RuntimeException( "issue on Thread.sleep : " + e.getMessage(), e );         }*/
block|}
specifier|public
name|String
name|getFieldValue
parameter_list|(
name|String
name|fieldName
parameter_list|)
block|{
return|return
name|getSelenium
argument_list|()
operator|.
name|getValue
argument_list|(
name|fieldName
argument_list|)
return|;
block|}
specifier|public
name|String
name|getCellValueFromTable
parameter_list|(
name|String
name|tableElement
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
return|return
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
name|tableElement
operator|+
literal|"."
operator|+
name|row
operator|+
literal|"."
operator|+
name|column
argument_list|)
return|;
block|}
specifier|public
name|void
name|selectValue
parameter_list|(
name|String
name|locator
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|getSelenium
argument_list|()
operator|.
name|select
argument_list|(
name|locator
argument_list|,
literal|"label="
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertOptionPresent
parameter_list|(
name|String
name|selectField
parameter_list|,
name|String
index|[]
name|options
parameter_list|)
block|{
name|assertElementPresent
argument_list|(
name|selectField
argument_list|)
expr_stmt|;
name|String
index|[]
name|optionsPresent
init|=
name|getSelenium
argument_list|()
operator|.
name|getSelectOptions
argument_list|(
name|selectField
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expected
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|present
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|optionsPresent
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|present
operator|.
name|containsAll
argument_list|(
name|expected
argument_list|)
argument_list|,
literal|"Options expected are not included in present options"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertSelectedValue
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
name|assertElementPresent
argument_list|(
name|fieldName
argument_list|)
expr_stmt|;
name|String
name|optionsPresent
init|=
name|getSelenium
argument_list|()
operator|.
name|getSelectedLabel
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|optionsPresent
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|submit
parameter_list|()
block|{
name|clickLinkWithXPath
argument_list|(
literal|"//input[@type='submit']"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertButtonWithValuePresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|isButtonWithValuePresent
argument_list|(
name|text
argument_list|)
argument_list|,
literal|"'"
operator|+
name|text
operator|+
literal|"' button isn't present"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertButtonWithIdPresent
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|isButtonWithIdPresent
argument_list|(
name|id
argument_list|)
argument_list|,
literal|"'Button with id ="
operator|+
name|id
operator|+
literal|"' isn't present"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertButtonWithValueNotPresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|isButtonWithValuePresent
argument_list|(
name|text
argument_list|)
argument_list|,
literal|"'"
operator|+
name|text
operator|+
literal|"' button is present"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isButtonWithValuePresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|isElementPresent
argument_list|(
literal|"//button[@value='"
operator|+
name|text
operator|+
literal|"']"
argument_list|)
operator|||
name|isElementPresent
argument_list|(
literal|"//input[@value='"
operator|+
name|text
operator|+
literal|"']"
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isButtonWithIdPresent
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|isElementPresent
argument_list|(
literal|"//button[@id='"
operator|+
name|text
operator|+
literal|"']"
argument_list|)
operator|||
name|isElementPresent
argument_list|(
literal|"//input[@id='"
operator|+
name|text
operator|+
literal|"']"
argument_list|)
return|;
block|}
specifier|public
name|void
name|clickButtonWithValue
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|clickButtonWithValue
argument_list|(
name|text
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickButtonWithValue
parameter_list|(
name|String
name|text
parameter_list|,
name|boolean
name|wait
parameter_list|)
block|{
name|assertButtonWithValuePresent
argument_list|(
name|text
argument_list|)
expr_stmt|;
if|if
condition|(
name|isElementPresent
argument_list|(
literal|"//button[@value='"
operator|+
name|text
operator|+
literal|"']"
argument_list|)
condition|)
block|{
name|clickLinkWithXPath
argument_list|(
literal|"//button[@value='"
operator|+
name|text
operator|+
literal|"']"
argument_list|,
name|wait
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clickLinkWithXPath
argument_list|(
literal|"//input[@value='"
operator|+
name|text
operator|+
literal|"']"
argument_list|,
name|wait
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|clickSubmitWithLocator
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
name|clickLinkWithLocator
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickSubmitWithLocator
parameter_list|(
name|String
name|locator
parameter_list|,
name|boolean
name|wait
parameter_list|)
block|{
name|clickLinkWithLocator
argument_list|(
name|locator
argument_list|,
name|wait
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickImgWithAlt
parameter_list|(
name|String
name|alt
parameter_list|)
block|{
name|clickLinkWithLocator
argument_list|(
literal|"//img[@alt='"
operator|+
name|alt
operator|+
literal|"']"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickLinkWithText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|clickLinkWithText
argument_list|(
name|text
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickLinkWithText
parameter_list|(
name|String
name|text
parameter_list|,
name|boolean
name|wait
parameter_list|)
block|{
name|clickLinkWithLocator
argument_list|(
literal|"link="
operator|+
name|text
argument_list|,
name|wait
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickLinkWithXPath
parameter_list|(
name|String
name|xpath
parameter_list|)
block|{
name|clickLinkWithXPath
argument_list|(
name|xpath
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickLinkWithXPath
parameter_list|(
name|String
name|xpath
parameter_list|,
name|boolean
name|wait
parameter_list|)
block|{
name|clickLinkWithLocator
argument_list|(
literal|"xpath="
operator|+
name|xpath
argument_list|,
name|wait
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickLinkWithLocator
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
name|clickLinkWithLocator
argument_list|(
name|locator
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickLinkWithLocator
parameter_list|(
name|String
name|locator
parameter_list|,
name|boolean
name|wait
parameter_list|)
block|{
name|assertElementPresent
argument_list|(
name|locator
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|click
argument_list|(
name|locator
argument_list|)
expr_stmt|;
if|if
condition|(
name|wait
condition|)
block|{
name|waitPage
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|clickButtonWithLocator
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
name|clickButtonWithLocator
argument_list|(
name|locator
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clickButtonWithLocator
parameter_list|(
name|String
name|locator
parameter_list|,
name|boolean
name|wait
parameter_list|)
block|{
name|assertElementPresent
argument_list|(
name|locator
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|click
argument_list|(
name|locator
argument_list|)
expr_stmt|;
if|if
condition|(
name|wait
condition|)
block|{
name|waitPage
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setFieldValues
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|entries
init|=
name|fieldMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|entries
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|entry
operator|=
name|entries
operator|.
name|next
argument_list|()
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|type
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setFieldValue
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|getSelenium
argument_list|()
operator|.
name|type
argument_list|(
name|fieldName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|checkField
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
name|getSelenium
argument_list|()
operator|.
name|check
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|uncheckField
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
name|getSelenium
argument_list|()
operator|.
name|uncheck
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isChecked
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
return|return
name|getSelenium
argument_list|()
operator|.
name|isChecked
argument_list|(
name|locator
argument_list|)
return|;
block|}
specifier|public
name|void
name|assertIsChecked
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|isChecked
argument_list|(
name|locator
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertIsNotChecked
parameter_list|(
name|String
name|locator
parameter_list|)
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|isChecked
argument_list|(
name|locator
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertXpathCount
parameter_list|(
name|String
name|locator
parameter_list|,
name|int
name|expectedCount
parameter_list|)
block|{
name|int
name|count
init|=
name|getSelenium
argument_list|()
operator|.
name|getXpathCount
argument_list|(
name|locator
argument_list|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|count
argument_list|,
name|expectedCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertElementValue
parameter_list|(
name|String
name|locator
parameter_list|,
name|String
name|expectedValue
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getValue
argument_list|(
name|locator
argument_list|)
argument_list|,
name|expectedValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

