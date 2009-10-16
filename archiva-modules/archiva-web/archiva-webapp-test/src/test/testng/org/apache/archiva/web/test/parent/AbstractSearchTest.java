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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSearchTest
extends|extends
name|AbstractArchivaTest
block|{
comment|//Search
specifier|public
name|void
name|goToSearchPage
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
name|assertSearchPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertSearchPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Quick Search"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Search for"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"quickSearch_q"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
comment|//assertLinkPresent( "Advanced Search" );
name|assertTextPresent
argument_list|(
literal|"Enter your search terms. A variety of data will be searched for your keywords."
argument_list|)
expr_stmt|;
comment|//assertButtonWithDivIdPresent( "searchHint" );
block|}
specifier|public
name|void
name|searchForArtifact
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"Apache Archiva \\ Quick Search"
operator|.
name|equals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTitle
argument_list|()
argument_list|)
condition|)
block|{
name|clickLinkWithText
argument_list|(
literal|"Search"
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
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Quick Search"
argument_list|)
expr_stmt|;
block|}
name|getSelenium
argument_list|()
operator|.
name|type
argument_list|(
literal|"dom=document.forms[1].elements[0]"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

