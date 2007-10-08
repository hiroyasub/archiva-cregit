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
name|collections
operator|.
name|Predicate
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
name|Transformer
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
name|document
operator|.
name|Document
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
name|queryParser
operator|.
name|MultiFieldQueryParser
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
name|queryParser
operator|.
name|ParseException
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
name|queryParser
operator|.
name|QueryParser
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
name|Hits
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
name|MultiSearcher
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
name|Searchable
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
name|ConfigurationNames
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
name|RepositoryContentIndex
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
name|bytecode
operator|.
name|BytecodeHandlers
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
name|filecontent
operator|.
name|FileContentHandlers
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
name|functors
operator|.
name|UserAllowedToSearchRepositoryPredicate
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
name|hashcodes
operator|.
name|HashcodesHandlers
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
name|hashcodes
operator|.
name|HashcodesKeys
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
name|lucene
operator|.
name|LuceneEntryConverter
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
name|lucene
operator|.
name|LuceneQuery
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
name|lucene
operator|.
name|LuceneRepositoryContentRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|registry
operator|.
name|RegistryListener
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
name|ArrayList
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
comment|/**  * DefaultCrossRepositorySearch  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.indexer.search.CrossRepositorySearch" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultCrossRepositorySearch
extends|extends
name|AbstractLogEnabled
implements|implements
name|CrossRepositorySearch
implements|,
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * @plexus.requirement role-hint="bytecode"      */
specifier|private
name|Transformer
name|bytecodeIndexTransformer
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="filecontent"      */
specifier|private
name|Transformer
name|filecontentIndexTransformer
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="hashcodes"      */
specifier|private
name|Transformer
name|hashcodesIndexTransformer
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="searchable"      */
specifier|private
name|Transformer
name|searchableTransformer
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="index-exists"      */
specifier|private
name|Predicate
name|indexExistsPredicate
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|List
name|localIndexedRepositories
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|public
name|SearchResults
name|searchForChecksum
parameter_list|(
name|String
name|checksum
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
block|{
name|List
name|indexes
init|=
name|getHashcodeIndexes
argument_list|()
decl_stmt|;
try|try
block|{
name|QueryParser
name|parser
init|=
operator|new
name|MultiFieldQueryParser
argument_list|(
operator|new
name|String
index|[]
block|{
name|HashcodesKeys
operator|.
name|MD5
block|,
name|HashcodesKeys
operator|.
name|SHA1
block|}
argument_list|,
operator|new
name|HashcodesHandlers
argument_list|()
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
decl_stmt|;
name|LuceneQuery
name|query
init|=
operator|new
name|LuceneQuery
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
name|checksum
argument_list|)
argument_list|)
decl_stmt|;
name|SearchResults
name|results
init|=
name|searchAll
argument_list|(
name|query
argument_list|,
name|limits
argument_list|,
name|indexes
argument_list|)
decl_stmt|;
name|results
operator|.
name|getRepositories
argument_list|()
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|localIndexedRepositories
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to parse query ["
operator|+
name|checksum
operator|+
literal|"]: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// empty results.
return|return
operator|new
name|SearchResults
argument_list|()
return|;
block|}
specifier|public
name|SearchResults
name|searchForBytecode
parameter_list|(
name|String
name|term
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
block|{
name|List
name|indexes
init|=
name|getHashcodeIndexes
argument_list|()
decl_stmt|;
try|try
block|{
name|QueryParser
name|parser
init|=
operator|new
name|BytecodeHandlers
argument_list|()
operator|.
name|getQueryParser
argument_list|()
decl_stmt|;
name|LuceneQuery
name|query
init|=
operator|new
name|LuceneQuery
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
name|term
argument_list|)
argument_list|)
decl_stmt|;
name|SearchResults
name|results
init|=
name|searchAll
argument_list|(
name|query
argument_list|,
name|limits
argument_list|,
name|indexes
argument_list|)
decl_stmt|;
name|results
operator|.
name|getRepositories
argument_list|()
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|localIndexedRepositories
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to parse query ["
operator|+
name|term
operator|+
literal|"]: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// empty results.
return|return
operator|new
name|SearchResults
argument_list|()
return|;
block|}
specifier|public
name|SearchResults
name|searchForTerm
parameter_list|(
name|String
name|term
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
block|{
name|List
name|indexes
init|=
name|getFileContentIndexes
argument_list|()
decl_stmt|;
try|try
block|{
name|QueryParser
name|parser
init|=
operator|new
name|FileContentHandlers
argument_list|()
operator|.
name|getQueryParser
argument_list|()
decl_stmt|;
name|LuceneQuery
name|query
init|=
operator|new
name|LuceneQuery
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
name|term
argument_list|)
argument_list|)
decl_stmt|;
name|SearchResults
name|results
init|=
name|searchAll
argument_list|(
name|query
argument_list|,
name|limits
argument_list|,
name|indexes
argument_list|)
decl_stmt|;
name|results
operator|.
name|getRepositories
argument_list|()
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|localIndexedRepositories
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to parse query ["
operator|+
name|term
operator|+
literal|"]: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// empty results.
return|return
operator|new
name|SearchResults
argument_list|()
return|;
block|}
specifier|private
name|SearchResults
name|searchAll
parameter_list|(
name|LuceneQuery
name|luceneQuery
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|,
name|List
name|indexes
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Query
name|specificQuery
init|=
name|luceneQuery
operator|.
name|getLuceneQuery
argument_list|()
decl_stmt|;
name|SearchResults
name|results
init|=
operator|new
name|SearchResults
argument_list|()
decl_stmt|;
if|if
condition|(
name|indexes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// No point going any further.
return|return
name|results
return|;
block|}
comment|// Setup the converter
name|LuceneEntryConverter
name|converter
init|=
literal|null
decl_stmt|;
name|RepositoryContentIndex
name|index
init|=
operator|(
name|RepositoryContentIndex
operator|)
name|indexes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|converter
operator|=
name|index
operator|.
name|getEntryConverter
argument_list|()
expr_stmt|;
comment|// Process indexes into an array of Searchables.
name|List
name|searchableList
init|=
operator|new
name|ArrayList
argument_list|(
name|indexes
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|transform
argument_list|(
name|searchableList
argument_list|,
name|searchableTransformer
argument_list|)
expr_stmt|;
name|Searchable
name|searchables
index|[]
init|=
operator|new
name|Searchable
index|[
name|searchableList
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|searchableList
operator|.
name|toArray
argument_list|(
name|searchables
argument_list|)
expr_stmt|;
name|MultiSearcher
name|searcher
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// Create a multi-searcher for looking up the information.
name|searcher
operator|=
operator|new
name|MultiSearcher
argument_list|(
name|searchables
argument_list|)
expr_stmt|;
comment|// Perform the search.
name|Hits
name|hits
init|=
name|searcher
operator|.
name|search
argument_list|(
name|specificQuery
argument_list|)
decl_stmt|;
name|int
name|hitCount
init|=
name|hits
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// Now process the limits.
name|results
operator|.
name|setLimits
argument_list|(
name|limits
argument_list|)
expr_stmt|;
name|results
operator|.
name|setTotalHits
argument_list|(
name|hitCount
argument_list|)
expr_stmt|;
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
name|fetchCount
operator|=
name|hitCount
expr_stmt|;
name|offset
operator|=
literal|0
expr_stmt|;
block|}
comment|// Goto offset.
if|if
condition|(
name|offset
operator|<
name|hitCount
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
operator|<=
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
name|hitCount
condition|)
block|{
break|break;
block|}
try|try
block|{
name|Document
name|doc
init|=
name|hits
operator|.
name|doc
argument_list|(
name|offset
operator|+
name|i
argument_list|)
decl_stmt|;
name|LuceneRepositoryContentRecord
name|record
init|=
name|converter
operator|.
name|convert
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|results
operator|.
name|addHit
argument_list|(
name|record
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|text
operator|.
name|ParseException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to parse document into record: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unable to setup multi-search: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|searcher
operator|!=
literal|null
condition|)
block|{
name|searcher
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ie
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unable to close index searcher: "
operator|+
name|ie
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ie
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
specifier|private
name|Predicate
name|getAllowedToSearchReposPredicate
parameter_list|()
block|{
return|return
operator|new
name|UserAllowedToSearchRepositoryPredicate
argument_list|()
return|;
block|}
specifier|public
name|List
name|getBytecodeIndexes
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|this
operator|.
name|localIndexedRepositories
init|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|CollectionUtils
operator|.
name|select
argument_list|(
name|this
operator|.
name|localIndexedRepositories
argument_list|,
name|getAllowedToSearchReposPredicate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|transform
argument_list|(
name|ret
argument_list|,
name|bytecodeIndexTransformer
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|filter
argument_list|(
name|ret
argument_list|,
name|indexExistsPredicate
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|List
name|getFileContentIndexes
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|this
operator|.
name|localIndexedRepositories
init|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|CollectionUtils
operator|.
name|select
argument_list|(
name|this
operator|.
name|localIndexedRepositories
argument_list|,
name|getAllowedToSearchReposPredicate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|transform
argument_list|(
name|ret
argument_list|,
name|filecontentIndexTransformer
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|filter
argument_list|(
name|ret
argument_list|,
name|indexExistsPredicate
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|List
name|getHashcodeIndexes
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|this
operator|.
name|localIndexedRepositories
init|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|CollectionUtils
operator|.
name|select
argument_list|(
name|this
operator|.
name|localIndexedRepositories
argument_list|,
name|getAllowedToSearchReposPredicate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|transform
argument_list|(
name|ret
argument_list|,
name|hashcodesIndexTransformer
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|filter
argument_list|(
name|ret
argument_list|,
name|indexExistsPredicate
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|ConfigurationNames
operator|.
name|isManagedRepositories
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initRepositories
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* Nothing to do here */
block|}
specifier|private
name|void
name|initRepositories
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
operator|.
name|localIndexedRepositories
init|)
block|{
name|this
operator|.
name|localIndexedRepositories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|repos
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepositoryConfiguration
name|repo
range|:
name|repos
control|)
block|{
if|if
condition|(
name|repo
operator|.
name|isScanned
argument_list|()
condition|)
block|{
name|localIndexedRepositories
operator|.
name|add
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|initRepositories
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

