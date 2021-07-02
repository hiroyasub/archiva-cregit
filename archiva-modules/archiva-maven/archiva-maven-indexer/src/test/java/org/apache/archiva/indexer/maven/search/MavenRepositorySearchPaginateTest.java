begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|maven
operator|.
name|search
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|indexer
operator|.
name|search
operator|.
name|SearchResultHit
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
name|indexer
operator|.
name|search
operator|.
name|SearchResultLimits
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
name|indexer
operator|.
name|search
operator|.
name|SearchResults
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
name|indexer
operator|.
name|util
operator|.
name|SearchUtil
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
name|repository
operator|.
name|base
operator|.
name|ArchivaRepositoryRegistry
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
name|repository
operator|.
name|base
operator|.
name|group
operator|.
name|RepositoryGroupHandler
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
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|MavenRepositorySearchPaginateTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
name|ArchivaRepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Inject
name|RepositoryGroupHandler
name|repositoryGroupHandler
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|endTests
parameter_list|()
block|{
assert|assert
name|repositoryRegistry
operator|!=
literal|null
assert|;
name|repositoryRegistry
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|nonPaginatedResult
parameter_list|()
throws|throws
name|Exception
block|{
name|MavenRepositorySearch
name|search
init|=
operator|new
name|MavenRepositorySearch
argument_list|()
decl_stmt|;
name|SearchResults
name|searchResults
init|=
name|build
argument_list|(
literal|10
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|searchResults
operator|=
name|search
operator|.
name|paginate
argument_list|(
name|searchResults
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|searchResults
operator|.
name|getReturnedHitsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|nonPaginatedHugeResult
parameter_list|()
throws|throws
name|Exception
block|{
name|MavenRepositorySearch
name|search
init|=
operator|new
name|MavenRepositorySearch
argument_list|()
decl_stmt|;
name|SearchResults
name|origSearchResults
init|=
name|build
argument_list|(
literal|63
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|SearchResults
name|searchResults
init|=
name|search
operator|.
name|paginate
argument_list|(
name|origSearchResults
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|searchResults
operator|.
name|getReturnedHitsCount
argument_list|()
argument_list|)
expr_stmt|;
name|origSearchResults
operator|=
name|build
argument_list|(
literal|63
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|searchResults
operator|=
name|search
operator|.
name|paginate
argument_list|(
name|origSearchResults
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|searchResults
operator|.
name|getReturnedHitsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|paginatedResult
parameter_list|()
throws|throws
name|Exception
block|{
name|MavenRepositorySearch
name|search
init|=
operator|new
name|MavenRepositorySearch
argument_list|()
decl_stmt|;
name|SearchResults
name|searchResults
init|=
name|build
argument_list|(
literal|32
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|searchResults
operator|=
name|search
operator|.
name|paginate
argument_list|(
name|searchResults
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|searchResults
operator|.
name|getReturnedHitsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|SearchResults
name|build
parameter_list|(
name|int
name|number
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
block|{
name|SearchResults
name|searchResults
init|=
operator|new
name|SearchResults
argument_list|()
decl_stmt|;
name|searchResults
operator|.
name|setLimits
argument_list|(
name|limits
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|number
condition|;
name|i
operator|++
control|)
block|{
name|SearchResultHit
name|hit
init|=
operator|new
name|SearchResultHit
argument_list|()
decl_stmt|;
name|hit
operator|.
name|setGroupId
argument_list|(
literal|"commons-foo"
argument_list|)
expr_stmt|;
name|hit
operator|.
name|setArtifactId
argument_list|(
literal|"commons-bar-"
operator|+
name|i
argument_list|)
expr_stmt|;
name|hit
operator|.
name|setPackaging
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
name|hit
operator|.
name|setVersions
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|id
init|=
name|SearchUtil
operator|.
name|getHitId
argument_list|(
name|hit
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|hit
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|hit
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|hit
operator|.
name|getPackaging
argument_list|()
argument_list|)
decl_stmt|;
name|searchResults
operator|.
name|addHit
argument_list|(
name|id
argument_list|,
name|hit
argument_list|)
expr_stmt|;
block|}
name|searchResults
operator|.
name|setTotalHits
argument_list|(
name|number
argument_list|)
expr_stmt|;
return|return
name|searchResults
return|;
block|}
block|}
end_class

end_unit

