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
name|junit
operator|.
name|Assert
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
name|By
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
name|JavascriptExecutor
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
name|support
operator|.
name|ui
operator|.
name|ExpectedCondition
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
name|support
operator|.
name|ui
operator|.
name|ExpectedConditions
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
name|support
operator|.
name|ui
operator|.
name|WebDriverWait
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

begin_comment
comment|/**  * Based on LoginTest of Emmanuel Venisse test.  *  * @author skygo  *  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryAdminTest
extends|extends
name|AbstractArchivaTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testManagedRepository
parameter_list|()
block|{
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
name|WebDriverWait
name|wait
init|=
operator|new
name|WebDriverWait
argument_list|(
name|getWebDriver
argument_list|()
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|WebElement
name|el
decl_stmt|;
name|el
operator|=
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|elementToBeClickable
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"menu-repositories-list-a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|tryClick
argument_list|(
name|el
argument_list|,
name|ExpectedConditions
operator|.
name|presenceOfElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"managed-repositories-view-a"
argument_list|)
argument_list|)
argument_list|,
literal|"Managed Repositories not activated"
argument_list|)
expr_stmt|;
name|el
operator|=
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|elementToBeClickable
argument_list|(
name|By
operator|.
name|xpath
argument_list|(
literal|"//a[@href='#remote-repositories-content']"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|tryClick
argument_list|(
name|el
argument_list|,
name|ExpectedConditions
operator|.
name|visibilityOfElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"remote-repositories-view-a"
argument_list|)
argument_list|)
argument_list|,
literal|"Remote Repositories View not available"
argument_list|)
expr_stmt|;
name|el
operator|=
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|elementToBeClickable
argument_list|(
name|By
operator|.
name|xpath
argument_list|(
literal|"//a[@href='#remote-repository-edit']"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|el
operator|=
name|tryClick
argument_list|(
name|el
argument_list|,
name|ExpectedConditions
operator|.
name|visibilityOfElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"remote-repository-save-button"
argument_list|)
argument_list|)
argument_list|,
literal|"Repository Save Button not available"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"id"
argument_list|,
literal|"myrepoid"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"name"
argument_list|,
literal|"My repo name"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"url"
argument_list|,
literal|"http://www.repo.org"
argument_list|)
expr_stmt|;
name|el
operator|.
name|click
argument_list|()
expr_stmt|;
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|visibilityOfElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"remote-repositories-view-a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|el
operator|=
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|elementToBeClickable
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"menu-proxy-connectors-list-a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|el
operator|.
name|click
argument_list|()
expr_stmt|;
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|visibilityOfElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"proxy-connectors-view-tabs-a-network-proxies-grid"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|textToBePresentInElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"main-content"
argument_list|)
argument_list|,
literal|"Proxy Connectors"
argument_list|)
argument_list|)
expr_stmt|;
comment|// proxy connect
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|textToBePresentInElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"proxy-connectors-view"
argument_list|)
argument_list|,
literal|"central"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"myrepoid"
argument_list|)
expr_stmt|;
name|el
operator|=
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|elementToBeClickable
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"proxy-connectors-view-tabs-a-edit"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|el
operator|.
name|click
argument_list|()
expr_stmt|;
name|el
operator|=
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|visibilityOfElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"proxy-connector-btn-save"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"sourceRepoId"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
comment|// Workaround
comment|// TODO: Check after upgrade of htmlunit, bootstrap or jquery
comment|// TODO: Check whats wrong here
operator|(
operator|(
name|JavascriptExecutor
operator|)
name|getWebDriver
argument_list|()
operator|)
operator|.
name|executeScript
argument_list|(
literal|"$('#targetRepoId').show();"
argument_list|)
expr_stmt|;
comment|// End of Workaround
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|visibilityOfElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"targetRepoId"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"targetRepoId"
argument_list|,
literal|"myrepoid"
argument_list|)
expr_stmt|;
name|el
operator|.
name|click
argument_list|()
expr_stmt|;
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|textToBePresentInElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"user-messages"
argument_list|)
argument_list|,
literal|"ProxyConnector added"
argument_list|)
argument_list|)
expr_stmt|;
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|textToBePresentInElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"proxy-connectors-view"
argument_list|)
argument_list|,
literal|"central"
argument_list|)
argument_list|)
expr_stmt|;
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|textToBePresentInElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"proxy-connectors-view"
argument_list|)
argument_list|,
literal|"myrepoid"
argument_list|)
argument_list|)
expr_stmt|;
name|clickLinkWithXPath
argument_list|(
literal|"//i[contains(@class,'icon-resize-vertical')]//ancestor::a"
argument_list|)
expr_stmt|;
comment|// This is needed here for HTMLUnit Tests. Currently do not know why, wait is not working for the
comment|// list entries down
name|waitPage
argument_list|()
expr_stmt|;
name|el
operator|=
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|presenceOfElementLocated
argument_list|(
name|By
operator|.
name|id
argument_list|(
literal|"proxy-connector-edit-order-div"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|WebElement
argument_list|>
name|repos
init|=
name|el
operator|.
name|findElements
argument_list|(
name|By
operator|.
name|xpath
argument_list|(
literal|"./div"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"First repo is myrepo"
argument_list|,
name|repos
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getText
argument_list|()
operator|.
name|contains
argument_list|(
literal|"myrepoid"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Second repo is central"
argument_list|,
name|repos
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getText
argument_list|()
operator|.
name|contains
argument_list|(
literal|"central"
argument_list|)
argument_list|)
expr_stmt|;
comment|// works until this point
comment|/*getSelenium().mouseDown( "xpath=//div[@id='proxy-connector-edit-order-div']/div[1]" );         getSelenium().mouseMove( "xpath=//div[@id='proxy-connector-edit-order-div']/div[2]" );         getSelenium().mouseUp( "xpath=//div[@id='proxy-connector-edit-order-div']/div[last()]" );         Assert.assertTrue( "Second repo is myrepo", getSelenium().getText("xpath=//div[@id='proxy-connector-edit-order-div']/div[2]" ).contains( "myrepoid" ));         Assert.assertTrue( "First repo is central", getSelenium().getText("xpath=//div[@id='proxy-connector-edit-order-div']/div[1]" ).contains( "central" ));         */
block|}
block|}
end_class

end_unit

