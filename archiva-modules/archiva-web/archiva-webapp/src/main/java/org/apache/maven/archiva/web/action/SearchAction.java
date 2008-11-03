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
name|action
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|commons
operator|.
name|collections
operator|.
name|CollectionUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ArchivaConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDAO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|Constraint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|constraints
operator|.
name|ArtifactsByChecksumConstraint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryIndexException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryIndexSearchException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|search
operator|.
name|CrossRepositorySearch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
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
name|maven
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
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|AccessDeniedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|ArchivaSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|ArchivaXworkUser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|PrincipalNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|UserRepositories
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|ActionContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Preparable
import|;
end_import

begin_comment
comment|/**  * Search all indexed fields by the given criteria.  *  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="searchAction"  */
end_comment

begin_class
specifier|public
class|class
name|SearchAction
extends|extends
name|PlexusActionSupport
implements|implements
name|Preparable
block|{
comment|/**      * Query string.      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepositoryConfiguration
argument_list|>
name|managedRepositories
decl_stmt|;
specifier|private
name|String
name|q
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * The Search Results.      */
specifier|private
name|SearchResults
name|results
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|CrossRepositorySearch
name|crossRepoSearch
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|UserRepositories
name|userRepositories
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaXworkUser
name|archivaXworkUser
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESULTS
init|=
literal|"results"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT
init|=
literal|"artifact"
decl_stmt|;
specifier|private
name|List
name|databaseResults
decl_stmt|;
specifier|private
name|int
name|currentPage
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|totalPages
decl_stmt|;
specifier|private
name|boolean
name|searchResultsOnly
decl_stmt|;
specifier|private
name|String
name|completeQueryString
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMPLETE_QUERY_STRING_SEPARATOR
init|=
literal|";"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|managedRepositoryList
decl_stmt|;
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|className
decl_stmt|;
specifier|private
name|int
name|rowCount
init|=
literal|30
decl_stmt|;
specifier|private
name|String
name|repositoryId
decl_stmt|;
specifier|private
name|boolean
name|fromFilterSearch
decl_stmt|;
specifier|private
name|boolean
name|filterSearch
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|fromResultsPage
decl_stmt|;
specifier|private
name|int
name|num
decl_stmt|;
specifier|public
name|boolean
name|isFromResultsPage
parameter_list|()
block|{
return|return
name|fromResultsPage
return|;
block|}
specifier|public
name|void
name|setFromResultsPage
parameter_list|(
name|boolean
name|fromResultsPage
parameter_list|)
block|{
name|this
operator|.
name|fromResultsPage
operator|=
name|fromResultsPage
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFromFilterSearch
parameter_list|()
block|{
return|return
name|fromFilterSearch
return|;
block|}
specifier|public
name|void
name|setFromFilterSearch
parameter_list|(
name|boolean
name|fromFilterSearch
parameter_list|)
block|{
name|this
operator|.
name|fromFilterSearch
operator|=
name|fromFilterSearch
expr_stmt|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|managedRepositoryList
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|managedRepositoryList
operator|=
name|getObservableRepos
argument_list|()
expr_stmt|;
if|if
condition|(
name|managedRepositoryList
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|managedRepositoryList
operator|.
name|add
argument_list|(
literal|"all"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// advanced search MRM-90 -- filtered search
specifier|public
name|String
name|filteredSearch
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|RepositoryIndexException
throws|,
name|RepositoryIndexSearchException
block|{
name|fromFilterSearch
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|managedRepositoryList
argument_list|)
condition|)
block|{
return|return
name|GlobalResults
operator|.
name|ACCESS_TO_NO_REPOS
return|;
block|}
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
name|currentPage
argument_list|)
decl_stmt|;
name|limits
operator|.
name|setPageSize
argument_list|(
name|rowCount
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|repositoryId
operator|.
name|equals
argument_list|(
literal|"all"
argument_list|)
condition|)
block|{
name|selectedRepos
operator|=
name|getObservableRepos
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|selectedRepos
operator|.
name|add
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|selectedRepos
argument_list|)
condition|)
block|{
return|return
name|GlobalResults
operator|.
name|ACCESS_TO_NO_REPOS
return|;
block|}
name|results
operator|=
name|crossRepoSearch
operator|.
name|executeFilteredSearch
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|selectedRepos
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|className
argument_list|,
name|limits
argument_list|)
expr_stmt|;
if|if
condition|(
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|addActionError
argument_list|(
literal|"No results found"
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
name|totalPages
operator|=
name|results
operator|.
name|getTotalHits
argument_list|()
operator|/
name|limits
operator|.
name|getPageSize
argument_list|()
expr_stmt|;
if|if
condition|(
operator|(
name|results
operator|.
name|getTotalHits
argument_list|()
operator|%
name|limits
operator|.
name|getPageSize
argument_list|()
operator|)
operator|!=
literal|0
condition|)
block|{
name|totalPages
operator|=
name|totalPages
operator|+
literal|1
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|quickSearch
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|RepositoryIndexException
throws|,
name|RepositoryIndexSearchException
block|{
comment|/* TODO: give action message if indexing is in progress.          * This should be based off a count of 'unprocessed' artifacts.          * This (yet to be written) routine could tell the user that X (unprocessed) artifacts are not yet          * present in the full text search.          */
assert|assert
name|q
operator|!=
literal|null
operator|&&
name|q
operator|.
name|length
argument_list|()
operator|!=
literal|0
assert|;
name|fromFilterSearch
operator|=
literal|false
expr_stmt|;
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
name|currentPage
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
init|=
name|getObservableRepos
argument_list|()
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|selectedRepos
argument_list|)
condition|)
block|{
return|return
name|GlobalResults
operator|.
name|ACCESS_TO_NO_REPOS
return|;
block|}
if|if
condition|(
name|SearchUtil
operator|.
name|isBytecodeSearch
argument_list|(
name|q
argument_list|)
condition|)
block|{
name|results
operator|=
name|crossRepoSearch
operator|.
name|searchForBytecode
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|selectedRepos
argument_list|,
name|SearchUtil
operator|.
name|removeBytecodeKeyword
argument_list|(
name|q
argument_list|)
argument_list|,
name|limits
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|searchResultsOnly
operator|&&
operator|!
name|completeQueryString
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|results
operator|=
name|crossRepoSearch
operator|.
name|searchForTerm
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|selectedRepos
argument_list|,
name|q
argument_list|,
name|limits
argument_list|,
name|parseCompleteQueryString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|completeQueryString
operator|=
literal|""
expr_stmt|;
name|results
operator|=
name|crossRepoSearch
operator|.
name|searchForTerm
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|selectedRepos
argument_list|,
name|q
argument_list|,
name|limits
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|addActionError
argument_list|(
literal|"No results found"
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
name|totalPages
operator|=
name|results
operator|.
name|getTotalHits
argument_list|()
operator|/
name|limits
operator|.
name|getPageSize
argument_list|()
expr_stmt|;
if|if
condition|(
operator|(
name|results
operator|.
name|getTotalHits
argument_list|()
operator|%
name|limits
operator|.
name|getPageSize
argument_list|()
operator|)
operator|!=
literal|0
condition|)
block|{
name|totalPages
operator|=
name|totalPages
operator|+
literal|1
expr_stmt|;
block|}
comment|// TODO: filter / combine the artifacts by version? (is that even possible with non-artifact hits?)
comment|/* I don't think that we should, as I expect us to utilize the 'score' system in lucene in          * the future to return relevant links better.          * I expect the lucene scoring system to take multiple hits on different areas of a single document          * to result in a higher score.          *   - Joakim          */
if|if
condition|(
operator|!
name|isEqualToPreviousSearchTerm
argument_list|(
name|q
argument_list|)
condition|)
block|{
name|buildCompleteQueryString
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|findArtifact
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: give action message if indexing is in progress
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|q
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to search for a blank checksum"
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
name|Constraint
name|constraint
init|=
operator|new
name|ArtifactsByChecksumConstraint
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|databaseResults
operator|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|queryArtifacts
argument_list|(
name|constraint
argument_list|)
expr_stmt|;
if|if
condition|(
name|databaseResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|addActionError
argument_list|(
literal|"No results found"
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
if|if
condition|(
name|databaseResults
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// 1 hit? return it's information directly!
return|return
name|ARTIFACT
return|;
block|}
return|return
name|RESULTS
return|;
block|}
specifier|public
name|String
name|doInput
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|private
name|String
name|getPrincipal
parameter_list|()
block|{
return|return
name|archivaXworkUser
operator|.
name|getActivePrincipal
argument_list|(
name|ActionContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSession
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getObservableRepos
parameter_list|()
block|{
try|try
block|{
return|return
name|userRepositories
operator|.
name|getObservableRepositoryIds
argument_list|(
name|getPrincipal
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PrincipalNotFoundException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// TODO: pass this onto the screen.
block|}
catch|catch
parameter_list|(
name|ArchivaSecurityException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|private
name|void
name|buildCompleteQueryString
parameter_list|(
name|String
name|searchTerm
parameter_list|)
block|{
if|if
condition|(
name|searchTerm
operator|.
name|indexOf
argument_list|(
name|COMPLETE_QUERY_STRING_SEPARATOR
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|searchTerm
operator|=
name|StringUtils
operator|.
name|remove
argument_list|(
name|searchTerm
argument_list|,
name|COMPLETE_QUERY_STRING_SEPARATOR
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|completeQueryString
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|completeQueryString
argument_list|)
condition|)
block|{
name|completeQueryString
operator|=
name|searchTerm
expr_stmt|;
block|}
else|else
block|{
name|completeQueryString
operator|=
name|completeQueryString
operator|+
name|COMPLETE_QUERY_STRING_SEPARATOR
operator|+
name|searchTerm
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|parseCompleteQueryString
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|parsedCompleteQueryString
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|parsed
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|completeQueryString
argument_list|,
name|COMPLETE_QUERY_STRING_SEPARATOR
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|addAll
argument_list|(
name|parsedCompleteQueryString
argument_list|,
name|parsed
argument_list|)
expr_stmt|;
return|return
name|parsedCompleteQueryString
return|;
block|}
specifier|private
name|boolean
name|isEqualToPreviousSearchTerm
parameter_list|(
name|String
name|searchTerm
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|completeQueryString
argument_list|)
condition|)
block|{
name|String
index|[]
name|parsed
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|completeQueryString
argument_list|,
name|COMPLETE_QUERY_STRING_SEPARATOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|searchTerm
argument_list|,
name|parsed
index|[
name|parsed
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getQ
parameter_list|()
block|{
return|return
name|q
return|;
block|}
specifier|public
name|void
name|setQ
parameter_list|(
name|String
name|q
parameter_list|)
block|{
name|this
operator|.
name|q
operator|=
name|q
expr_stmt|;
block|}
specifier|public
name|SearchResults
name|getResults
parameter_list|()
block|{
return|return
name|results
return|;
block|}
specifier|public
name|List
name|getDatabaseResults
parameter_list|()
block|{
return|return
name|databaseResults
return|;
block|}
specifier|public
name|void
name|setCurrentPage
parameter_list|(
name|int
name|page
parameter_list|)
block|{
name|this
operator|.
name|currentPage
operator|=
name|page
expr_stmt|;
block|}
specifier|public
name|int
name|getCurrentPage
parameter_list|()
block|{
return|return
name|currentPage
return|;
block|}
specifier|public
name|int
name|getTotalPages
parameter_list|()
block|{
return|return
name|totalPages
return|;
block|}
specifier|public
name|void
name|setTotalPages
parameter_list|(
name|int
name|totalPages
parameter_list|)
block|{
name|this
operator|.
name|totalPages
operator|=
name|totalPages
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSearchResultsOnly
parameter_list|()
block|{
return|return
name|searchResultsOnly
return|;
block|}
specifier|public
name|void
name|setSearchResultsOnly
parameter_list|(
name|boolean
name|searchResultsOnly
parameter_list|)
block|{
name|this
operator|.
name|searchResultsOnly
operator|=
name|searchResultsOnly
expr_stmt|;
block|}
specifier|public
name|String
name|getCompleteQueryString
parameter_list|()
block|{
return|return
name|completeQueryString
return|;
block|}
specifier|public
name|void
name|setCompleteQueryString
parameter_list|(
name|String
name|completeQueryString
parameter_list|)
block|{
name|this
operator|.
name|completeQueryString
operator|=
name|completeQueryString
expr_stmt|;
block|}
specifier|public
name|ArchivaConfiguration
name|getArchivaConfiguration
parameter_list|()
block|{
return|return
name|archivaConfiguration
return|;
block|}
specifier|public
name|void
name|setArchivaConfiguration
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
block|{
name|this
operator|.
name|archivaConfiguration
operator|=
name|archivaConfiguration
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepositoryConfiguration
argument_list|>
name|getManagedRepositories
parameter_list|()
block|{
return|return
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
return|;
block|}
specifier|public
name|void
name|setManagedRepositories
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepositoryConfiguration
argument_list|>
name|managedRepositories
parameter_list|)
block|{
name|this
operator|.
name|managedRepositories
operator|=
name|managedRepositories
expr_stmt|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
name|rowCount
return|;
block|}
specifier|public
name|void
name|setRowCount
parameter_list|(
name|int
name|rowCount
parameter_list|)
block|{
name|this
operator|.
name|rowCount
operator|=
name|rowCount
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFilterSearch
parameter_list|()
block|{
return|return
name|filterSearch
return|;
block|}
specifier|public
name|void
name|setFilterSearch
parameter_list|(
name|boolean
name|filterSearch
parameter_list|)
block|{
name|this
operator|.
name|filterSearch
operator|=
name|filterSearch
expr_stmt|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getManagedRepositoryList
parameter_list|()
block|{
return|return
name|managedRepositoryList
return|;
block|}
specifier|public
name|void
name|setManagedRepositoryList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|managedRepositoryList
parameter_list|)
block|{
name|this
operator|.
name|managedRepositoryList
operator|=
name|managedRepositoryList
expr_stmt|;
block|}
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
specifier|public
name|void
name|setClassName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
block|}
block|}
end_class

end_unit

