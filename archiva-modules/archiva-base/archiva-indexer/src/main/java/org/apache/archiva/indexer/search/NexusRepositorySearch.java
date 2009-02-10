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
name|search
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanClause
operator|.
name|Occur
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
name|Configuration
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|ArtifactInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|FlatSearchRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|FlatSearchResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|NexusIndexer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|context
operator|.
name|IndexContextInInconsistentStateException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|context
operator|.
name|IndexingContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|context
operator|.
name|UnsupportedExistingLuceneIndexException
import|;
end_import

begin_comment
comment|/**  * RepositorySearch implementation which uses the Nexus Indexer for searching.  */
end_comment

begin_class
specifier|public
class|class
name|NexusRepositorySearch
implements|implements
name|RepositorySearch
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|NexusRepositorySearch
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|NexusIndexer
name|indexer
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfig
decl_stmt|;
specifier|public
name|NexusRepositorySearch
parameter_list|(
name|NexusIndexer
name|indexer
parameter_list|,
name|ArchivaConfiguration
name|archivaConfig
parameter_list|)
block|{
name|this
operator|.
name|indexer
operator|=
name|indexer
expr_stmt|;
name|this
operator|.
name|archivaConfig
operator|=
name|archivaConfig
expr_stmt|;
block|}
comment|/**      * @see RepositorySearch#search(String, List, String, SearchResultLimits, List)      */
specifier|public
name|SearchResults
name|search
parameter_list|(
name|String
name|principal
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
parameter_list|,
name|String
name|term
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|previousSearchTerms
parameter_list|)
throws|throws
name|RepositorySearchException
block|{
name|addIndexingContexts
argument_list|(
name|selectedRepos
argument_list|)
expr_stmt|;
name|BooleanQuery
name|q
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|previousSearchTerms
operator|==
literal|null
operator|||
name|previousSearchTerms
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|constructQuery
argument_list|(
name|term
argument_list|,
name|q
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|String
name|previousTerm
range|:
name|previousSearchTerms
control|)
block|{
name|BooleanQuery
name|iQuery
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|constructQuery
argument_list|(
name|previousTerm
argument_list|,
name|iQuery
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|iQuery
argument_list|,
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
name|BooleanQuery
name|iQuery
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|constructQuery
argument_list|(
name|term
argument_list|,
name|iQuery
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|iQuery
argument_list|,
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
return|return
name|search
argument_list|(
name|limits
argument_list|,
name|q
argument_list|)
return|;
block|}
comment|/**      * @see RepositorySearch#search(String, SearchFields, SearchResultLimits)      */
specifier|public
name|SearchResults
name|search
parameter_list|(
name|String
name|principal
parameter_list|,
name|SearchFields
name|searchFields
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
throws|throws
name|RepositorySearchException
block|{
if|if
condition|(
name|searchFields
operator|.
name|getRepositories
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositorySearchException
argument_list|(
literal|"Repositories cannot be null."
argument_list|)
throw|;
block|}
name|addIndexingContexts
argument_list|(
name|searchFields
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|BooleanQuery
name|q
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|searchFields
operator|.
name|getGroupId
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|searchFields
operator|.
name|getGroupId
argument_list|()
argument_list|)
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|GROUP_ID
argument_list|,
name|searchFields
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|,
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|searchFields
operator|.
name|getArtifactId
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|searchFields
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|ARTIFACT_ID
argument_list|,
name|searchFields
operator|.
name|getArtifactId
argument_list|()
argument_list|)
argument_list|,
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|searchFields
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|searchFields
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|VERSION
argument_list|,
name|searchFields
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|,
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|searchFields
operator|.
name|getPackaging
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|searchFields
operator|.
name|getPackaging
argument_list|()
argument_list|)
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|PACKAGING
argument_list|,
name|searchFields
operator|.
name|getPackaging
argument_list|()
argument_list|)
argument_list|,
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|searchFields
operator|.
name|getClassName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|searchFields
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|NAMES
argument_list|,
name|searchFields
operator|.
name|getClassName
argument_list|()
argument_list|)
argument_list|,
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|q
operator|.
name|getClauses
argument_list|()
operator|==
literal|null
operator|||
name|q
operator|.
name|getClauses
argument_list|()
operator|.
name|length
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RepositorySearchException
argument_list|(
literal|"No search fields set."
argument_list|)
throw|;
block|}
return|return
name|search
argument_list|(
name|limits
argument_list|,
name|q
argument_list|)
return|;
block|}
specifier|private
name|SearchResults
name|search
parameter_list|(
name|SearchResultLimits
name|limits
parameter_list|,
name|BooleanQuery
name|q
parameter_list|)
throws|throws
name|RepositorySearchException
block|{
try|try
block|{
name|FlatSearchRequest
name|request
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|FlatSearchResponse
name|response
init|=
name|indexer
operator|.
name|searchFlat
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|==
literal|null
operator|||
name|response
operator|.
name|getTotalHits
argument_list|()
operator|==
literal|0
condition|)
block|{
name|SearchResults
name|results
init|=
operator|new
name|SearchResults
argument_list|()
decl_stmt|;
name|results
operator|.
name|setLimits
argument_list|(
name|limits
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
return|return
name|convertToSearchResults
argument_list|(
name|response
argument_list|,
name|limits
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IndexContextInInconsistentStateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositorySearchException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositorySearchException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|IndexingContext
argument_list|>
name|indexingContexts
init|=
name|indexer
operator|.
name|getIndexingContexts
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|indexingContexts
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
try|try
block|{
name|indexer
operator|.
name|removeIndexingContext
argument_list|(
name|indexingContexts
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Indexing context '"
operator|+
name|key
operator|+
literal|"' removed from search."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"IOException occurred while removing indexing content '"
operator|+
name|key
operator|+
literal|"'."
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|constructQuery
parameter_list|(
name|String
name|term
parameter_list|,
name|BooleanQuery
name|q
parameter_list|)
block|{
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|GROUP_ID
argument_list|,
name|term
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|ARTIFACT_ID
argument_list|,
name|term
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|VERSION
argument_list|,
name|term
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|PACKAGING
argument_list|,
name|term
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|NAMES
argument_list|,
name|term
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addIndexingContexts
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
parameter_list|)
block|{
for|for
control|(
name|String
name|repo
range|:
name|selectedRepos
control|)
block|{
try|try
block|{
name|Configuration
name|config
init|=
name|archivaConfig
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfig
init|=
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|repo
argument_list|)
decl_stmt|;
if|if
condition|(
name|repoConfig
operator|!=
literal|null
condition|)
block|{
name|String
name|indexDir
init|=
name|repoConfig
operator|.
name|getIndexDir
argument_list|()
decl_stmt|;
name|File
name|indexDirectory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|indexDir
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|indexDir
argument_list|)
condition|)
block|{
name|indexDirectory
operator|=
operator|new
name|File
argument_list|(
name|repoConfig
operator|.
name|getIndexDir
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|indexDirectory
operator|=
operator|new
name|File
argument_list|(
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
expr_stmt|;
block|}
name|IndexingContext
name|context
init|=
name|indexer
operator|.
name|addIndexingContext
argument_list|(
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|,
name|indexDirectory
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|NexusIndexer
operator|.
name|FULL_INDEX
argument_list|)
decl_stmt|;
name|context
operator|.
name|setSearchable
argument_list|(
name|repoConfig
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Repository '"
operator|+
name|repo
operator|+
literal|"' not found in configuration."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedExistingLuceneIndexException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error accessing index of repository '"
operator|+
name|repo
operator|+
literal|"' : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"IO error occured while accessing index of repository '"
operator|+
name|repo
operator|+
literal|"' : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
specifier|private
name|SearchResults
name|convertToSearchResults
parameter_list|(
name|FlatSearchResponse
name|response
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
block|{
name|SearchResults
name|results
init|=
operator|new
name|SearchResults
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ArtifactInfo
argument_list|>
name|artifactInfos
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactInfo
name|artifactInfo
range|:
name|artifactInfos
control|)
block|{
name|String
name|id
init|=
name|getHitId
argument_list|(
name|artifactInfo
operator|.
name|groupId
argument_list|,
name|artifactInfo
operator|.
name|artifactId
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|SearchResultHit
argument_list|>
name|hitsMap
init|=
name|results
operator|.
name|getHitsMap
argument_list|()
decl_stmt|;
name|SearchResultHit
name|hit
init|=
name|hitsMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|hit
operator|!=
literal|null
condition|)
block|{
name|hit
operator|.
name|addVersion
argument_list|(
name|artifactInfo
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|hit
operator|=
operator|new
name|SearchResultHit
argument_list|()
expr_stmt|;
name|hit
operator|.
name|setArtifactId
argument_list|(
name|artifactInfo
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|hit
operator|.
name|setGroupId
argument_list|(
name|artifactInfo
operator|.
name|groupId
argument_list|)
expr_stmt|;
comment|// do we still need to set the repository id even though we're merging everything?
comment|//hit.setRepositoryId( artifactInfo.repository );
name|hit
operator|.
name|setUrl
argument_list|(
name|artifactInfo
operator|.
name|repository
operator|+
literal|"/"
operator|+
name|artifactInfo
operator|.
name|fname
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hit
operator|.
name|getVersions
argument_list|()
operator|.
name|contains
argument_list|(
name|artifactInfo
operator|.
name|version
argument_list|)
condition|)
block|{
name|hit
operator|.
name|addVersion
argument_list|(
name|artifactInfo
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
block|}
name|results
operator|.
name|addHit
argument_list|(
name|id
argument_list|,
name|hit
argument_list|)
expr_stmt|;
block|}
name|results
operator|.
name|setTotalHits
argument_list|(
name|results
operator|.
name|getHitsMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|setLimits
argument_list|(
name|limits
argument_list|)
expr_stmt|;
if|if
condition|(
name|limits
operator|==
literal|null
operator|||
name|limits
operator|.
name|getSelectedPage
argument_list|()
operator|==
name|SearchResultLimits
operator|.
name|ALL_PAGES
condition|)
block|{
return|return
name|results
return|;
block|}
else|else
block|{
return|return
name|paginate
argument_list|(
name|results
argument_list|)
return|;
block|}
block|}
specifier|private
name|SearchResults
name|paginate
parameter_list|(
name|SearchResults
name|results
parameter_list|)
block|{
name|SearchResultLimits
name|limits
init|=
name|results
operator|.
name|getLimits
argument_list|()
decl_stmt|;
name|SearchResults
name|paginated
init|=
operator|new
name|SearchResults
argument_list|()
decl_stmt|;
name|int
name|fetchCount
init|=
name|limits
operator|.
name|getPageSize
argument_list|()
decl_stmt|;
name|int
name|offset
init|=
operator|(
name|limits
operator|.
name|getSelectedPage
argument_list|()
operator|*
name|limits
operator|.
name|getPageSize
argument_list|()
operator|)
decl_stmt|;
if|if
condition|(
name|fetchCount
operator|>
name|results
operator|.
name|getTotalHits
argument_list|()
condition|)
block|{
name|fetchCount
operator|=
name|results
operator|.
name|getTotalHits
argument_list|()
expr_stmt|;
block|}
comment|// Goto offset.
if|if
condition|(
name|offset
operator|<
name|results
operator|.
name|getTotalHits
argument_list|()
condition|)
block|{
comment|// only process if the offset is within the hit count.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fetchCount
condition|;
name|i
operator|++
control|)
block|{
comment|// Stop fetching if we are past the total # of available hits.
if|if
condition|(
name|offset
operator|+
name|i
operator|>=
name|results
operator|.
name|getHits
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
break|break;
block|}
name|SearchResultHit
name|hit
init|=
name|results
operator|.
name|getHits
argument_list|()
operator|.
name|get
argument_list|(
operator|(
name|offset
operator|+
name|i
operator|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|hit
operator|!=
literal|null
condition|)
block|{
name|String
name|id
init|=
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
argument_list|)
decl_stmt|;
name|paginated
operator|.
name|addHit
argument_list|(
name|id
argument_list|,
name|hit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
block|}
name|paginated
operator|.
name|setTotalHits
argument_list|(
name|results
operator|.
name|getTotalHits
argument_list|()
argument_list|)
expr_stmt|;
name|paginated
operator|.
name|setLimits
argument_list|(
name|limits
argument_list|)
expr_stmt|;
return|return
name|paginated
return|;
block|}
specifier|private
specifier|static
name|String
name|getHitId
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
block|{
return|return
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
return|;
block|}
block|}
end_class

end_unit

