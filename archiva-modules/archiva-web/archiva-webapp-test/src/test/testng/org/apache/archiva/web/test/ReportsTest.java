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
name|AbstractArtifactReportsTest
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
literal|"reports"
block|}
argument_list|,
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactValidValues"
block|}
argument_list|)
specifier|public
class|class
name|ReportsTest
extends|extends
name|AbstractArtifactReportsTest
block|{
comment|//TODO Tests for repository with defects
comment|//	@Test(dependsOnMethods = { "testAddArtifactValidValues" } )
specifier|public
name|void
name|testRepoStatisticsWithoutRepoCompared
parameter_list|()
block|{
name|goToReportsPage
argument_list|()
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"View Statistics"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Please select a repository (or repositories) from the list."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testRepoStatisticsWithoutRepoCompared"
block|}
argument_list|)
specifier|public
name|void
name|testRepositoryStatisticsWithoutDate
parameter_list|()
block|{
name|String
name|repositoryName
init|=
name|getProperty
argument_list|(
literal|"REPOSITORY_NAME"
argument_list|)
decl_stmt|;
name|compareRepositories
argument_list|(
literal|"label="
operator|+
name|repositoryName
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|//TODO
comment|//assertTextPresent( "Statistics Report" );
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testRepoStatisticsWithoutRepoCompared"
block|}
argument_list|)
specifier|public
name|void
name|testRepositoryStatisticsEndEarlierThanStart
parameter_list|()
block|{
name|String
name|repositoryName
init|=
name|getProperty
argument_list|(
literal|"REPOSITORY_NAME"
argument_list|)
decl_stmt|;
name|String
name|startDate
init|=
name|getProperty
argument_list|(
literal|"END_DATE"
argument_list|)
decl_stmt|;
name|String
name|endDate
init|=
name|getProperty
argument_list|(
literal|"START_DATE"
argument_list|)
decl_stmt|;
name|compareRepositories
argument_list|(
literal|"label="
operator|+
name|repositoryName
argument_list|,
name|startDate
argument_list|,
name|endDate
argument_list|)
expr_stmt|;
comment|//assertTextPresent( "Statistics for Repository '" + repositoryName + "'" );
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Reports"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Start Date must be earlier than the End Date"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactValidValues"
block|}
argument_list|)
specifier|public
name|void
name|testRepositoryStatistics
parameter_list|()
block|{
name|String
name|repositoryName
init|=
name|getProperty
argument_list|(
literal|"REPOSITORY_NAME"
argument_list|)
decl_stmt|;
name|String
name|startDate
init|=
name|getProperty
argument_list|(
literal|"START_DATE"
argument_list|)
decl_stmt|;
name|String
name|endDate
init|=
name|getProperty
argument_list|(
literal|"END_DATE"
argument_list|)
decl_stmt|;
name|compareRepositories
argument_list|(
literal|"label="
operator|+
name|repositoryName
argument_list|,
name|startDate
argument_list|,
name|endDate
argument_list|)
expr_stmt|;
comment|//assertTextPresent( "Statistics for Repository '" + repositoryName + "'" );
comment|//assertPage( "Apache Archiva \\ Reports" );
comment|//assertTextPresent( "Statistics Report" );
block|}
comment|/* @Test( dependsOnMethods = { "testRepositoryStatistics" } ) 	public void testRepositoriesStatisticComparisonReport() 	{ 		//goToReportsPage(); 		clickButtonWithValue( "-->>" , false ); 		clickButtonWithValue( "View Statistics" ); 		assertTextPresent( "Statistics Report" ); 	} 	 	@Test(dependsOnMethods = { "testAddArtifactValidValues" } ) 	public void testRepositoryHealthWithoutDefect() 	{ 		goToReportsPage(); 		String groupId = getProperty( "ARTIFACT_GROUPID" ); 		getSelenium().type( "generateReport_groupId" , groupId ); 		clickButtonWithValue( "Show Report" ); 		assertPage( "Apache Archiva \\ Reports" ); 		assertTextPresent( "The operation generated an empty report." ); 	} 	 	@Test(dependsOnMethods = { "testAddArtifactValidValues" } ) 	public void testRepositoryHealthWithoutGroupId() 	{ 		goToReportsPage(); 		clickButtonWithValue( "Show Report" ); 		assertPage( "Apache Archiva \\ Reports" ); 		assertTextPresent( "The operation generated an empty report." ); 		 		//TODO As of the creation of the tests, GroupId is not a required field in showing the reports of repository health. GroupId should be required I think. 	}*/
block|}
end_class

end_unit

