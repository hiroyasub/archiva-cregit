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

begin_class
specifier|public
class|class
name|ArchivaAdminTest
extends|extends
name|AbstractArchivaTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testHome
parameter_list|()
block|{
name|loadPage
argument_list|(
name|baseUrl
argument_list|,
literal|30
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
literal|30
argument_list|)
decl_stmt|;
name|wait
operator|.
name|until
argument_list|(
name|ExpectedConditions
operator|.
name|titleContains
argument_list|(
literal|"Apache Archiva"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInitialRepositories
parameter_list|()
block|{
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
name|xpath
argument_list|(
literal|"//table[@id='managed-repositories-table']//td[contains(text(),'internal')]"
argument_list|)
argument_list|)
argument_list|,
literal|"Managed Repositories not activated"
argument_list|)
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
name|xpath
argument_list|(
literal|"//table[@id='managed-repositories-table']//td[contains(text(),'snapshots')]"
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
name|xpath
argument_list|(
literal|"//table[@id='remote-repositories-table']//td[contains(text(),'central')]"
argument_list|)
argument_list|)
argument_list|,
literal|"Remote Repositories View not available"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

