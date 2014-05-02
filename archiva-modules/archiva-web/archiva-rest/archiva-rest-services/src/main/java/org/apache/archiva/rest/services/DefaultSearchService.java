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
name|indexer
operator|.
name|search
operator|.
name|RepositorySearch
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
name|RepositorySearchException
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
name|SearchFields
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
name|Dependency
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
name|GroupIdList
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
name|model
operator|.
name|StringList
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
name|ArchivaRestServiceException
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
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|ListUtils
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
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"searchService#rest"
argument_list|)
specifier|public
class|class
name|DefaultSearchService
extends|extends
name|AbstractRestService
implements|implements
name|SearchService
block|{
annotation|@
name|Inject
specifier|private
name|RepositorySearch
name|repositorySearch
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|quickSearch
parameter_list|(
name|String
name|queryString
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|queryString
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|SearchResults
name|searchResults
init|=
name|repositorySearch
operator|.
name|search
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|getObservableRepos
argument_list|()
argument_list|,
name|queryString
argument_list|,
name|limits
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|getArtifacts
argument_list|(
name|searchResults
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositorySearchException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|quickSearchWithRepositories
parameter_list|(
name|SearchRequest
name|searchRequest
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|String
name|queryString
init|=
name|searchRequest
operator|.
name|getQueryTerms
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|queryString
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
name|searchRequest
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
if|if
condition|(
name|repositories
operator|==
literal|null
operator|||
name|repositories
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|repositories
operator|=
name|getObservableRepos
argument_list|()
expr_stmt|;
block|}
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
name|searchRequest
operator|.
name|getPageSize
argument_list|()
argument_list|,
name|searchRequest
operator|.
name|getSelectedPage
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|SearchResults
name|searchResults
init|=
name|repositorySearch
operator|.
name|search
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|repositories
argument_list|,
name|queryString
argument_list|,
name|limits
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|getArtifacts
argument_list|(
name|searchResults
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositorySearchException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|getArtifactVersions
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|packaging
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|groupId
argument_list|)
operator|||
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|SearchFields
name|searchField
init|=
operator|new
name|SearchFields
argument_list|()
decl_stmt|;
name|searchField
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|searchField
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|searchField
operator|.
name|setPackaging
argument_list|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|packaging
argument_list|)
condition|?
literal|"jar"
else|:
name|packaging
argument_list|)
expr_stmt|;
name|searchField
operator|.
name|setRepositories
argument_list|(
name|getObservableRepos
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|SearchResults
name|searchResults
init|=
name|repositorySearch
operator|.
name|search
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|searchField
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|getArtifacts
argument_list|(
name|searchResults
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositorySearchException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|searchArtifacts
parameter_list|(
name|SearchRequest
name|searchRequest
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|searchRequest
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|SearchFields
name|searchField
init|=
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|searchRequest
argument_list|,
name|SearchFields
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|limits
operator|.
name|setPageSize
argument_list|(
name|searchRequest
operator|.
name|getPageSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// if no repos set we use ones available for the user
if|if
condition|(
name|searchField
operator|.
name|getRepositories
argument_list|()
operator|==
literal|null
operator|||
name|searchField
operator|.
name|getRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|searchField
operator|.
name|setRepositories
argument_list|(
name|getObservableRepos
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|SearchResults
name|searchResults
init|=
name|repositorySearch
operator|.
name|search
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|searchField
argument_list|,
name|limits
argument_list|)
decl_stmt|;
return|return
name|getArtifacts
argument_list|(
name|searchResults
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositorySearchException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|GroupIdList
name|getAllGroupIds
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|observableRepos
init|=
name|getObservableRepos
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repos
init|=
name|ListUtils
operator|.
name|intersection
argument_list|(
name|observableRepos
argument_list|,
name|selectedRepos
argument_list|)
decl_stmt|;
if|if
condition|(
name|repos
operator|==
literal|null
operator|||
name|repos
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|GroupIdList
argument_list|(
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
try|try
block|{
return|return
operator|new
name|GroupIdList
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|repositorySearch
operator|.
name|getAllGroupIds
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|repos
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositorySearchException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|Dependency
argument_list|>
name|getDependencies
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|getArtifactByChecksum
parameter_list|(
name|String
name|checksum
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|StringList
name|getObservablesRepoIds
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
return|return
operator|new
name|StringList
argument_list|(
name|getObservableRepos
argument_list|()
argument_list|)
return|;
block|}
comment|//-------------------------------------
comment|// internal
comment|//-------------------------------------
specifier|protected
name|List
argument_list|<
name|Artifact
argument_list|>
name|getArtifacts
parameter_list|(
name|SearchResults
name|searchResults
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|searchResults
operator|==
literal|null
operator|||
name|searchResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|searchResults
operator|.
name|getReturnedHitsCount
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|SearchResultHit
name|hit
range|:
name|searchResults
operator|.
name|getHits
argument_list|()
control|)
block|{
comment|// duplicate Artifact one per available version
if|if
condition|(
name|hit
operator|.
name|getVersions
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|version
range|:
name|hit
operator|.
name|getVersions
argument_list|()
control|)
block|{
name|Artifact
name|versionned
init|=
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|hit
argument_list|,
name|Artifact
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|versionned
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|versionned
operator|.
name|setUrl
argument_list|(
name|getArtifactUrl
argument_list|(
name|versionned
argument_list|)
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|versionned
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|artifacts
return|;
block|}
block|}
end_class

end_unit

