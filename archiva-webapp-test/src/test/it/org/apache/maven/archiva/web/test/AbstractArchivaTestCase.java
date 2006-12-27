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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|shared
operator|.
name|web
operator|.
name|test
operator|.
name|AbstractSeleniumTestCase
import|;
end_import

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractArchivaTestCase
extends|extends
name|AbstractSeleniumTestCase
block|{
specifier|private
name|String
name|baseUrl
init|=
literal|"http://localhost:9595/archiva"
decl_stmt|;
specifier|protected
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
literal|"Archiva"
return|;
block|}
specifier|protected
name|String
name|getInceptionYear
parameter_list|()
block|{
return|return
literal|"2005"
return|;
block|}
specifier|protected
name|void
name|postAdminUserCreation
parameter_list|()
block|{
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
literal|"Configuration"
argument_list|)
condition|)
block|{
comment|//Add Managed Repository
name|setFieldValue
argument_list|(
literal|"id"
argument_list|,
literal|"web-ui"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"urlName"
argument_list|,
literal|"web-ui"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"name"
argument_list|,
literal|"Web UI Test Managed Repository"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"directory"
argument_list|,
name|getBasedir
argument_list|()
operator|+
literal|"target/web-ui-dir"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Add Repository"
argument_list|)
expr_stmt|;
comment|//Set Index location
name|assertPage
argument_list|(
literal|"Configuration"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"indexPath"
argument_list|,
name|getBasedir
argument_list|()
operator|+
literal|"target/web-ui-index"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Save Configuration"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Administration"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|assertHeader
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"banner is missing"
argument_list|,
name|getSelenium
argument_list|()
operator|.
name|isElementPresent
argument_list|(
literal|"xpath=//div[@id='banner']"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bannerLeft is missing"
argument_list|,
name|getSelenium
argument_list|()
operator|.
name|isElementPresent
argument_list|(
literal|"xpath=//div[@id='banner']"
operator|+
literal|"/span[@id='bannerLeft']"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bannerLeft link is missing"
argument_list|,
name|getSelenium
argument_list|()
operator|.
name|isElementPresent
argument_list|(
literal|"xpath=//div[@id='banner']"
operator|+
literal|"/span[@id='bannerLeft']/a[@href='http://maven.apache.org/archiva/']"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bannerLeft img is missing"
argument_list|,
name|getSelenium
argument_list|()
operator|.
name|isElementPresent
argument_list|(
literal|"xpath=//div[@id='banner']"
operator|+
literal|"/span[@id='bannerLeft']/a[@href='http://maven.apache.org/archiva/']"
operator|+
literal|"/img[@src='"
operator|+
name|getWebContext
argument_list|()
operator|+
literal|"/images/archiva.png']"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bannerRight is missing"
argument_list|,
name|getSelenium
argument_list|()
operator|.
name|isElementPresent
argument_list|(
literal|"xpath=//div[@id='banner']/span[@id='bannerRight']"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getTitlePrefix
parameter_list|()
block|{
return|return
literal|"Maven Archiva :: "
return|;
block|}
specifier|public
name|String
name|getBaseUrl
parameter_list|()
block|{
return|return
name|baseUrl
return|;
block|}
specifier|protected
name|String
name|getWebContext
parameter_list|()
block|{
return|return
literal|"/archiva"
return|;
block|}
block|}
end_class

end_unit

