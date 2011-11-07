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
name|merger
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|Files
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
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|common
operator|.
name|plexusbridge
operator|.
name|MavenIndexerUtils
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridgeException
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
name|io
operator|.
name|FileUtils
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
name|math
operator|.
name|NumberUtils
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
name|index
operator|.
name|NexusIndexer
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
name|apache
operator|.
name|maven
operator|.
name|index
operator|.
name|context
operator|.
name|UnsupportedExistingLuceneIndexException
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
name|index
operator|.
name|packer
operator|.
name|IndexPacker
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
name|index
operator|.
name|packer
operator|.
name|IndexPackingRequest
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
name|springframework
operator|.
name|scheduling
operator|.
name|annotation
operator|.
name|Async
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
name|annotation
operator|.
name|PostConstruct
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
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M2  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"indexMerger#default"
argument_list|)
specifier|public
class|class
name|DefaultIndexMerger
implements|implements
name|IndexMerger
block|{
comment|/**      * default tmp created group index ttl in minutes      */
specifier|static
specifier|final
name|int
name|DEFAULT_GROUP_INDEX_TTL
init|=
literal|30
decl_stmt|;
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
specifier|private
name|MavenIndexerUtils
name|mavenIndexerUtils
decl_stmt|;
specifier|private
name|NexusIndexer
name|indexer
decl_stmt|;
specifier|private
name|IndexPacker
name|indexPacker
decl_stmt|;
specifier|private
name|List
argument_list|<
name|TemporaryGroupIndex
argument_list|>
name|temporaryGroupIndexes
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|TemporaryGroupIndex
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|int
name|defaultGroupIndexTtl
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|DefaultIndexMerger
parameter_list|(
name|PlexusSisuBridge
name|plexusSisuBridge
parameter_list|,
name|MavenIndexerUtils
name|mavenIndexerUtils
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
name|this
operator|.
name|indexer
operator|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|NexusIndexer
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|mavenIndexerUtils
operator|=
name|mavenIndexerUtils
expr_stmt|;
name|indexPacker
operator|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|IndexPacker
operator|.
name|class
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|intialize
parameter_list|()
block|{
name|String
name|ttlStr
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|IndexMerger
operator|.
name|TMP_GROUP_INDEX_SYS_KEY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|DEFAULT_GROUP_INDEX_TTL
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|defaultGroupIndexTtl
operator|=
name|NumberUtils
operator|.
name|toInt
argument_list|(
name|ttlStr
argument_list|,
name|DEFAULT_GROUP_INDEX_TTL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IndexingContext
name|buildMergedIndex
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoriesIds
parameter_list|,
name|boolean
name|packIndex
parameter_list|)
throws|throws
name|IndexMergerException
block|{
name|File
name|tempRepoFile
init|=
name|Files
operator|.
name|createTempDir
argument_list|()
decl_stmt|;
name|tempRepoFile
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|String
name|tempRepoId
init|=
name|tempRepoFile
operator|.
name|getName
argument_list|()
decl_stmt|;
try|try
block|{
name|File
name|indexLocation
init|=
operator|new
name|File
argument_list|(
name|tempRepoFile
argument_list|,
literal|".indexer"
argument_list|)
decl_stmt|;
name|IndexingContext
name|indexingContext
init|=
name|indexer
operator|.
name|addIndexingContext
argument_list|(
name|tempRepoId
argument_list|,
name|tempRepoId
argument_list|,
name|tempRepoFile
argument_list|,
name|indexLocation
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|mavenIndexerUtils
operator|.
name|getAllIndexCreators
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|repositoriesIds
control|)
block|{
name|IndexingContext
name|idxToMerge
init|=
name|indexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|get
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
if|if
condition|(
name|idxToMerge
operator|!=
literal|null
condition|)
block|{
name|indexingContext
operator|.
name|merge
argument_list|(
name|idxToMerge
operator|.
name|getIndexDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|indexingContext
operator|.
name|optimize
argument_list|()
expr_stmt|;
if|if
condition|(
name|packIndex
condition|)
block|{
name|IndexPackingRequest
name|request
init|=
operator|new
name|IndexPackingRequest
argument_list|(
name|indexingContext
argument_list|,
name|indexLocation
argument_list|)
decl_stmt|;
name|indexPacker
operator|.
name|packIndex
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
name|temporaryGroupIndexes
operator|.
name|add
argument_list|(
operator|new
name|TemporaryGroupIndex
argument_list|(
name|tempRepoFile
argument_list|,
name|tempRepoId
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|indexingContext
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IndexMergerException
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
catch|catch
parameter_list|(
name|UnsupportedExistingLuceneIndexException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IndexMergerException
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
name|Async
specifier|public
name|void
name|cleanTemporaryGroupIndex
parameter_list|(
name|TemporaryGroupIndex
name|temporaryGroupIndex
parameter_list|)
block|{
if|if
condition|(
name|temporaryGroupIndex
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|IndexingContext
name|indexingContext
init|=
name|indexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|get
argument_list|(
name|temporaryGroupIndex
operator|.
name|getIndexId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexingContext
operator|!=
literal|null
condition|)
block|{
name|indexer
operator|.
name|removeIndexingContext
argument_list|(
name|indexingContext
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|File
name|directory
init|=
name|temporaryGroupIndex
operator|.
name|getDirectory
argument_list|()
decl_stmt|;
if|if
condition|(
name|directory
operator|!=
literal|null
operator|&&
name|directory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
name|temporaryGroupIndexes
operator|.
name|remove
argument_list|(
name|temporaryGroupIndex
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
literal|"fail to delete temporary group index {}"
argument_list|,
name|temporaryGroupIndex
operator|.
name|getIndexId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Collection
argument_list|<
name|TemporaryGroupIndex
argument_list|>
name|getTemporaryGroupIndexes
parameter_list|()
block|{
return|return
name|this
operator|.
name|temporaryGroupIndexes
return|;
block|}
specifier|public
name|int
name|getDefaultGroupIndexTtl
parameter_list|()
block|{
return|return
name|this
operator|.
name|defaultGroupIndexTtl
return|;
block|}
block|}
end_class

end_unit

