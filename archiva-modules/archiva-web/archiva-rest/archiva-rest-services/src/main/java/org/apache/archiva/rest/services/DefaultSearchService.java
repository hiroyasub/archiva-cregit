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
name|common
operator|.
name|utils
operator|.
name|VersionComparator
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
name|maven
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
name|metadata
operator|.
name|model
operator|.
name|ArtifactMetadata
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepositoryException
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
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
name|collections4
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
name|lang3
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
specifier|private
specifier|static
specifier|final
name|String
name|LATEST_KEYWORD
init|=
literal|"LATEST"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositorySearch
name|repositorySearch
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
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
name|Artifact
argument_list|>
name|getArtifactByChecksum
parameter_list|(
name|ChecksumSearch
name|checksumSearch
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
comment|// if no repos set we use ones available for the user
if|if
condition|(
name|checksumSearch
operator|.
name|getRepositories
argument_list|()
operator|==
literal|null
operator|||
name|checksumSearch
operator|.
name|getRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|checksumSearch
operator|.
name|setRepositories
argument_list|(
name|getObservableRepos
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|RepositorySession
name|repositorySession
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Artifact
argument_list|>
name|artifactSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|String
name|repoId
range|:
name|checksumSearch
operator|.
name|getRepositories
argument_list|()
control|)
block|{
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactMetadatas
init|=
name|metadataRepository
operator|.
name|getArtifactsByChecksum
argument_list|(
name|repositorySession
argument_list|,
name|repoId
argument_list|,
name|checksumSearch
operator|.
name|getChecksum
argument_list|()
argument_list|)
decl_stmt|;
name|artifactSet
operator|.
name|addAll
argument_list|(
name|buildArtifacts
argument_list|(
name|artifactMetadatas
argument_list|,
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|artifactSet
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
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
finally|finally
block|{
name|repositorySession
operator|.
name|closeQuietly
argument_list|()
expr_stmt|;
block|}
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
annotation|@
name|Override
specifier|public
name|Response
name|redirectToArtifactFile
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|packaging
parameter_list|,
name|String
name|classifier
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
comment|// validate query
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
operator|new
name|Response
operator|.
name|StatusType
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
operator|.
name|Status
operator|.
name|Family
name|getFamily
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getFamily
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getReasonPhrase
parameter_list|()
block|{
return|return
literal|"groupId mandatory"
return|;
block|}
block|}
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|version
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
operator|new
name|Response
operator|.
name|StatusType
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
operator|.
name|Status
operator|.
name|Family
name|getFamily
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getFamily
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getReasonPhrase
parameter_list|()
block|{
return|return
literal|"version mandatory"
return|;
block|}
block|}
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
operator|new
name|Response
operator|.
name|StatusType
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
operator|.
name|Status
operator|.
name|Family
name|getFamily
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getFamily
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getReasonPhrase
parameter_list|()
block|{
return|return
literal|"artifactId mandatory"
return|;
block|}
block|}
argument_list|)
operator|.
name|build
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
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|version
argument_list|,
name|LATEST_KEYWORD
argument_list|)
condition|)
block|{
name|searchField
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
name|searchField
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|userRepos
init|=
name|getObservablesRepoIds
argument_list|()
operator|.
name|getStrings
argument_list|()
decl_stmt|;
name|searchField
operator|.
name|setRepositories
argument_list|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryId
argument_list|)
condition|?
name|userRepos
else|:
name|Arrays
operator|.
name|asList
argument_list|(
name|repositoryId
argument_list|)
argument_list|)
expr_stmt|;
name|searchField
operator|.
name|setExactSearch
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|getArtifacts
argument_list|(
name|searchResults
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifacts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
operator|new
name|Response
operator|.
name|StatusType
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|NO_CONTENT
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
operator|.
name|Status
operator|.
name|Family
name|getFamily
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|NO_CONTENT
operator|.
name|getFamily
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getReasonPhrase
parameter_list|()
block|{
return|return
literal|"your query doesn't return any artifact"
return|;
block|}
block|}
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|// TODO improve that with querying lucene with null value for classifier
comment|// so simple loop and retain only artifact with null classifier
if|if
condition|(
name|classifier
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|filteredArtifacts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|artifacts
control|)
block|{
if|if
condition|(
name|artifact
operator|.
name|getClassifier
argument_list|()
operator|==
literal|null
condition|)
block|{
name|filteredArtifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
name|artifacts
operator|=
name|filteredArtifacts
expr_stmt|;
block|}
comment|// TODO return json result of the query ?
if|if
condition|(
name|artifacts
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|&&
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|version
argument_list|,
name|LATEST_KEYWORD
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
operator|new
name|Response
operator|.
name|StatusType
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
operator|.
name|Status
operator|.
name|Family
name|getFamily
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getFamily
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getReasonPhrase
parameter_list|()
block|{
return|return
literal|"your query return more than one artifact"
return|;
block|}
block|}
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|// version is LATEST so we have to find the latest one from the result
if|if
condition|(
name|artifacts
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|version
argument_list|,
name|LATEST_KEYWORD
argument_list|)
condition|)
block|{
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Artifact
argument_list|>
name|artifactPerVersion
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|VersionComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|artifacts
control|)
block|{
name|artifactPerVersion
operator|.
name|put
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|temporaryRedirect
argument_list|(
operator|new
name|URI
argument_list|(
name|artifactPerVersion
operator|.
name|lastEntry
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
name|Artifact
name|artifact
init|=
name|artifacts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|temporaryRedirect
argument_list|(
operator|new
name|URI
argument_list|(
name|artifact
operator|.
name|getUrl
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
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

