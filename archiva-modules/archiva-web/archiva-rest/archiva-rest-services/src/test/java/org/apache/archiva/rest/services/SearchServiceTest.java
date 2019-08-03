begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|UiConfiguration
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
name|maven2
operator|.
name|model
operator|.
name|Artifact
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ChecksumSearch
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|SearchRequest
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|SearchService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Collection
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
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|SearchServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO
init|=
literal|"test-repo"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|quickSearchOnArtifactId
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
comment|// START SNIPPET: quick-search
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|quickSearch
argument_list|(
literal|"commons-logging"
argument_list|)
decl_stmt|;
comment|// return all artifacts with groupId OR artifactId OR version OR packaging OR className
comment|// NOTE : only artifacts with classifier empty are returned
comment|// END SNIPPET: quick-search
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 6 results for commons-logging search but "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|":"
operator|+
name|artifacts
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
operator|==
literal|6
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts for commons-logging size {} search {}"
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
block|}
comment|/**      * same search but with Guest user      *      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|quickSearchOnArtifactIdGuest
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
literal|null
argument_list|)
decl_stmt|;
comment|// START SNIPPET: quick-search
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|quickSearch
argument_list|(
literal|"commons-logging"
argument_list|)
decl_stmt|;
comment|// return all artifacts with groupId OR artifactId OR version OR packaging OR className
comment|// NOTE : only artifacts with classifier empty are returned
comment|// END SNIPPET: quick-search
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 6 results for commons-logging search but "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|":"
operator|+
name|artifacts
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
operator|==
literal|6
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts for commons-logging size {} search {}"
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|searchArtifactVersions
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: searchservice-artifact-versions
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|getArtifactVersions
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
comment|// END SNIPPET: searchservice-artifact-versions
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 13 results for commons-logging search but "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|":"
operator|+
name|artifacts
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
operator|==
literal|13
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts for commons-logging size {} search {}"
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|artifacts
control|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"url: {}"
argument_list|,
name|artifact
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|version
init|=
name|artifact
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|artifact
operator|.
name|getUrl
argument_list|()
operator|.
name|contains
argument_list|(
name|version
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|searchWithSearchRequestGroupIdAndArtifactIdAndClassifier
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
comment|// START SNIPPET: searchservice-with-classifier
name|SearchRequest
name|searchRequest
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|searchRequest
operator|.
name|setGroupId
argument_list|(
literal|"commons-logging"
argument_list|)
expr_stmt|;
name|searchRequest
operator|.
name|setArtifactId
argument_list|(
literal|"commons-logging"
argument_list|)
expr_stmt|;
name|searchRequest
operator|.
name|setClassifier
argument_list|(
literal|"sources"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|searchArtifacts
argument_list|(
name|searchRequest
argument_list|)
decl_stmt|;
comment|// END SNIPPET: searchservice-with-classifier
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 2 results for commons-logging search but "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|":"
operator|+
name|artifacts
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts for commons-logging size {} search {}"
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|searchWithSearchRequestBundleSymbolicNameOneVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
comment|// START SNIPPET: searchservice-with-osgi
name|SearchRequest
name|searchRequest
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|searchRequest
operator|.
name|setBundleSymbolicName
argument_list|(
literal|"org.apache.karaf.features.command"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: searchservice-with-osgi
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|searchArtifacts
argument_list|(
name|searchRequest
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 1 results for Bundle Symbolic Name org.apache.karaf.features.command but "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|":"
operator|+
name|artifacts
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|searchWithSearchRequestBundleSymbolicNameTwoVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|UiConfiguration
name|uiConfiguration
init|=
operator|new
name|UiConfiguration
argument_list|()
decl_stmt|;
name|uiConfiguration
operator|.
name|setApplicationUrl
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|setUiConfiguration
argument_list|(
name|uiConfiguration
argument_list|)
expr_stmt|;
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|SearchRequest
name|searchRequest
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|searchRequest
operator|.
name|setBundleSymbolicName
argument_list|(
literal|"org.apache.karaf.features.core"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|searchArtifacts
argument_list|(
name|searchRequest
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|artifacts
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|artifacts
control|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"url: {}"
argument_list|,
name|artifact
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|version
init|=
name|artifact
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifact
operator|.
name|getUrl
argument_list|()
argument_list|)
comment|//
operator|.
name|isEqualTo
argument_list|(
literal|"http://localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
literal|"/repository/test-repo/org/apache/karaf/features/org.apache.karaf.features.core/"
operator|+
name|version
operator|+
literal|"/org.apache.karaf.features.core-"
operator|+
name|version
operator|+
literal|".jar"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|searchWithSearchRequestExportPackageOneVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|SearchRequest
name|searchRequest
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|searchRequest
operator|.
name|setBundleExportPackage
argument_list|(
literal|"org.apache.karaf.features.command.completers"
argument_list|)
expr_stmt|;
name|searchRequest
operator|.
name|setRepositories
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|searchArtifacts
argument_list|(
name|searchRequest
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 1 results for Bundle ExportPackage org.apache.karaf.features.command.completers but "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|":"
operator|+
name|artifacts
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifact url {}"
argument_list|,
name|artifacts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
comment|/**      * ensure we don't return response for an unknown repo      */
specifier|public
name|void
name|searchWithSearchUnknwownRepoId
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|SearchRequest
name|searchRequest
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|searchRequest
operator|.
name|setBundleExportPackage
argument_list|(
literal|"org.apache.karaf.features.command.completers"
argument_list|)
expr_stmt|;
name|searchRequest
operator|.
name|setRepositories
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"tototititata"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|searchArtifacts
argument_list|(
name|searchRequest
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 0 results for Bundle ExportPackage org.apache.karaf.features.command.completers but "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|":"
operator|+
name|artifacts
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
comment|/**      * ensure we revert to all observable repos in case of no repo in the request      */
specifier|public
name|void
name|searchWithSearchNoRepos
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|SearchRequest
name|searchRequest
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|searchRequest
operator|.
name|setBundleExportPackage
argument_list|(
literal|"org.apache.karaf.features.command.completers"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|searchArtifacts
argument_list|(
name|searchRequest
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 0 results for Bundle ExportPackage org.apache.karaf.features.command.completers but "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|":"
operator|+
name|artifacts
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifact url {}"
argument_list|,
name|artifacts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getAllGroupIds
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|groupIds
init|=
name|searchService
operator|.
name|getAllGroupIds
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|)
operator|.
name|getGroupIds
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"groupIds  {}"
argument_list|,
name|groupIds
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|groupIds
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|groupIds
operator|.
name|contains
argument_list|(
literal|"commons-cli"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|groupIds
operator|.
name|contains
argument_list|(
literal|"org.apache.felix"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
comment|/**      * test we don't return 2 artifacts pom + zip one      */
specifier|public
name|void
name|getSearchArtifactsWithOnlyClassifier
parameter_list|()
throws|throws
name|Exception
block|{
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|TEST_REPO
argument_list|,
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repo-with-classifier-only"
argument_list|)
argument_list|)
expr_stmt|;
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|SearchRequest
name|searchRequest
init|=
operator|new
name|SearchRequest
argument_list|(
literal|"org.foo"
argument_list|,
literal|"studio-all-update-site"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|searchArtifacts
argument_list|(
name|searchRequest
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifacts: {}"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * sha1 commons-logging 1.1 ba24d5de831911b684c92cd289ed5ff826271824      */
annotation|@
name|Test
specifier|public
name|void
name|search_with_sha1
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|getArtifactByChecksum
argument_list|(
operator|new
name|ChecksumSearch
argument_list|(
literal|null
argument_list|,
literal|"ba24d5de831911b684c92cd289ed5ff826271824"
argument_list|)
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifacts
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * md5 commons-logging 1.1 6b62417e77b000a87de66ee3935edbf5      */
annotation|@
name|Test
specifier|public
name|void
name|search_with_md5
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|getArtifactByChecksum
argument_list|(
operator|new
name|ChecksumSearch
argument_list|(
literal|null
argument_list|,
literal|"6b62417e77b000a87de66ee3935edbf5"
argument_list|)
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifacts
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|createRepo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// force guest user creation if not exists
if|if
condition|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getGuestUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|assertNotNull
argument_list|(
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createAndIndexRepo
argument_list|(
name|TEST_REPO
argument_list|,
name|getProjectDirectory
argument_list|( )
operator|.
name|resolve
argument_list|(
literal|"src/test/repo-with-osgi"
argument_list|)
argument_list|)
expr_stmt|;
name|waitForScanToComplete
argument_list|(
name|TEST_REPO
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|deleteRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestRepo
argument_list|(
name|TEST_REPO
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

