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
name|AbstractArtifactReportsTest
extends|extends
name|AbstractArchivaTest
block|{
comment|// Reports
specifier|public
name|void
name|goToReportsPage
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Reports"
argument_list|)
expr_stmt|;
name|assertReportsPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertReportsPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Reports"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Reports"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Statistics"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repositories To Be Compared"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"availableRepositories"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"v"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"^"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"<-"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"->"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"<<--"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"-->>"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"<*>"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"selectedRepositories"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"v"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"^"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Row Count"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"rowCount"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Start Date"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"startDate"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"End Date"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"endDate"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"View Statistics"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Health"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Row Count"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"rowCount"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Group ID"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"groupId"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository ID"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"repositoryId"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Show Report"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|compareRepositories
parameter_list|(
name|String
name|labelSelected
parameter_list|,
name|String
name|startDate
parameter_list|,
name|String
name|endDate
parameter_list|)
block|{
name|goToReportsPage
argument_list|()
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|removeSelection
argument_list|(
literal|"generateStatisticsReport_availableRepositories"
argument_list|,
name|labelSelected
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"->"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|type
argument_list|(
literal|"startDate"
argument_list|,
name|startDate
argument_list|)
expr_stmt|;
comment|// clickLinkWithLocator( "1" , false );
comment|// getSelenium().click( "endDate" );
name|getSelenium
argument_list|()
operator|.
name|type
argument_list|(
literal|"endDate"
argument_list|,
name|endDate
argument_list|)
expr_stmt|;
comment|// clickLinkWithLocator( "30" , false );
name|clickButtonWithValue
argument_list|(
literal|"View Statistics"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

