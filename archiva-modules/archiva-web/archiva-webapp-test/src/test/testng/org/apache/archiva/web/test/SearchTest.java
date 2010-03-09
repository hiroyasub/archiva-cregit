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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|AbstractSearchTest
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
literal|"search"
block|}
argument_list|,
name|dependsOnMethods
operator|=
block|{
literal|"testWithCorrectUsernamePassword"
block|}
argument_list|)
specifier|public
class|class
name|SearchTest
extends|extends
name|AbstractSearchTest
block|{
specifier|public
name|void
name|testSearchNonExistingArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|searchForArtifact
argument_list|(
name|getProperty
argument_list|(
literal|"SEARCH_BAD_ARTIFACT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"No results found"
argument_list|)
expr_stmt|;
block|}
comment|// TODO: make search tests more robust especially when comparing/asserting number of hits
specifier|public
name|void
name|testSearchExistingArtifact
parameter_list|()
block|{
name|searchForArtifact
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_ARTIFACTID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Hits: 1 to 1 of 1"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testViewSearchedArtifact
parameter_list|()
block|{
name|searchForArtifact
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_ARTIFACTID"
argument_list|)
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_ARTIFACTID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Browse Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_ARTIFACTID"
argument_list|)
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_VERSION"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Browse Repository"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSearchNonExistingArtifactInAdvancedSearch
parameter_list|()
block|{
name|searchForArtifactAdvancedSearch
argument_list|(
literal|null
argument_list|,
name|getProperty
argument_list|(
literal|"SEARCH_BAD_ARTIFACT"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"No results found"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSearchNoSearchCriteriaSpecifiedInAdvancedSearch
parameter_list|()
block|{
name|searchForArtifactAdvancedSearch
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Advanced Search - At least one search criteria must be provided."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSearchExistingArtifactUsingAdvancedSearchArtifactId
parameter_list|()
block|{
name|searchForArtifactAdvancedSearch
argument_list|(
literal|null
argument_list|,
name|getProperty
argument_list|(
literal|"ARTIFACT_ARTIFACTID"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|getProperty
argument_list|(
literal|"REPOSITORYID"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Hits: 1 to 1 of 1"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSearchExistingArtifactUsingAdvancedSearchGroupId
parameter_list|()
block|{
name|searchForArtifactAdvancedSearch
argument_list|(
name|getProperty
argument_list|(
literal|"GROUPID"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getProperty
argument_list|(
literal|"REPOSITORYID"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Hits: 1 to 1 of 1"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSearchExistingArtifactUsingAdvancedSearchNotInRepository
parameter_list|()
block|{
name|searchForArtifactAdvancedSearch
argument_list|(
literal|null
argument_list|,
name|getProperty
argument_list|(
literal|"ARTIFACT_ARTIFACTID"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|"snapshots"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"No results found"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"Hits: 1 to 1 of 1"
argument_list|)
expr_stmt|;
name|assertLinkNotPresent
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

