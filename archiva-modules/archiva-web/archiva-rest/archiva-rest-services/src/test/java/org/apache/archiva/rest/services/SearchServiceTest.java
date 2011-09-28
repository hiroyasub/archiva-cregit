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
name|ManagedRepository
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
name|ManagedRepositoriesService
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
name|junit
operator|.
name|Test
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
name|util
operator|.
name|Date
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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|SearchServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|quickSearchOnArtifactId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
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
name|File
name|targetRepo
init|=
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|)
decl_stmt|;
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
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|,
name|targetRepo
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
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
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
name|File
name|targetRepo
init|=
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|)
decl_stmt|;
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
literal|" not 3 results for commons-logging search but "
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
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|,
name|targetRepo
argument_list|)
expr_stmt|;
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
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
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
name|File
name|targetRepo
init|=
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|)
decl_stmt|;
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
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|,
name|targetRepo
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
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
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
name|File
name|targetRepo
init|=
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|)
decl_stmt|;
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
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|,
name|targetRepo
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
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
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
name|File
name|targetRepo
init|=
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|)
decl_stmt|;
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
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|" not 2 results for Bundle Symbolic Name org.apache.karaf.features.core but "
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
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|,
name|targetRepo
argument_list|)
expr_stmt|;
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
name|String
name|testRepoId
init|=
literal|"test-repo"
decl_stmt|;
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
name|File
name|targetRepo
init|=
name|createAndIndexRepo
argument_list|(
name|testRepoId
argument_list|)
decl_stmt|;
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
literal|"artifcat url "
operator|+
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
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|,
name|targetRepo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|createAndIndexRepo
parameter_list|(
name|String
name|testRepoId
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|testRepoId
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|testRepoId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|File
name|targetRepo
init|=
operator|new
name|File
argument_list|(
literal|"src/test/repo-with-osgi"
argument_list|)
decl_stmt|;
name|ManagedRepository
name|managedRepository
init|=
operator|new
name|ManagedRepository
argument_list|()
decl_stmt|;
name|managedRepository
operator|.
name|setId
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setName
argument_list|(
literal|"test repo"
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setLocation
argument_list|(
name|targetRepo
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setIndexDirectory
argument_list|(
literal|"target/.index-"
operator|+
name|Long
operator|.
name|toString
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ManagedRepositoriesService
name|service
init|=
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|service
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|scanRepositoryNow
argument_list|(
name|testRepoId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|targetRepo
return|;
block|}
specifier|private
name|void
name|deleteTestRepo
parameter_list|(
name|String
name|id
parameter_list|,
name|File
name|targetRepo
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|id
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

