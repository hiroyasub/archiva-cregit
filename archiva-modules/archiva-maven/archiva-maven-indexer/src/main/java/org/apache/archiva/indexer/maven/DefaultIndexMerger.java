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
name|FileUtils
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
name|ArchivaIndexingContext
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
name|UnsupportedBaseContextException
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
name|merger
operator|.
name|IndexMerger
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
name|merger
operator|.
name|IndexMergerException
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
name|merger
operator|.
name|IndexMergerRequest
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
name|merger
operator|.
name|TemporaryGroupIndex
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
name|RepositoryRegistry
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
name|RepositoryType
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
name|time
operator|.
name|StopWatch
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
name|Indexer
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
name|ContextMemberProvider
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
name|IndexCreator
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
name|StaticContextMemberProvider
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
annotation|@
name|Inject
name|RepositoryRegistry
name|repositoryRegistry
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
specifier|private
specifier|final
name|IndexPacker
name|indexPacker
decl_stmt|;
specifier|private
name|Indexer
name|indexer
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|IndexCreator
argument_list|>
name|indexCreators
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|IndexingContext
argument_list|>
name|temporaryContextes
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|runningGroups
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|DefaultIndexMerger
parameter_list|(
name|Indexer
name|indexer
parameter_list|,
name|IndexPacker
name|indexPacker
parameter_list|,
name|List
argument_list|<
name|IndexCreator
argument_list|>
name|indexCreators
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
name|indexPacker
operator|=
name|indexPacker
expr_stmt|;
name|this
operator|.
name|indexCreators
operator|=
name|indexCreators
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ArchivaIndexingContext
name|buildMergedIndex
parameter_list|(
name|IndexMergerRequest
name|indexMergerRequest
parameter_list|)
throws|throws
name|IndexMergerException
block|{
name|String
name|groupId
init|=
name|indexMergerRequest
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
if|if
condition|(
name|runningGroups
operator|.
name|contains
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"skip build merge remote indexes for id: '{}' as already running"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|runningGroups
operator|.
name|add
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|StopWatch
name|stopWatch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|stopWatch
operator|.
name|reset
argument_list|()
expr_stmt|;
name|stopWatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|Path
name|mergedIndexDirectory
init|=
name|indexMergerRequest
operator|.
name|getMergedIndexDirectory
argument_list|()
decl_stmt|;
name|String
name|tempRepoId
init|=
name|mergedIndexDirectory
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
name|Path
name|indexLocation
init|=
name|mergedIndexDirectory
operator|.
name|resolve
argument_list|(
name|indexMergerRequest
operator|.
name|getMergedIndexPath
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|IndexingContext
argument_list|>
name|members
init|=
name|indexMergerRequest
operator|.
name|getRepositoriesIds
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|map
argument_list|(
name|id
lambda|->
name|repositoryRegistry
operator|.
name|getRepository
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|repo
lambda|->
name|repo
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|repo
lambda|->
block|{
lambda|try
block|{
return|return
name|repo
operator|.
name|getIndexingContext
argument_list|()
operator|.
name|getBaseContext
argument_list|(
name|IndexingContext
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedBaseContextException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
comment|// Ignore
block|}
block|}
block_content|)
block|.filter( Objects::nonNull
block|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
end_class

begin_decl_stmt
name|ContextMemberProvider
name|memberProvider
init|=
operator|new
name|StaticContextMemberProvider
argument_list|(
name|members
argument_list|)
decl_stmt|;
end_decl_stmt

begin_decl_stmt
name|IndexingContext
name|mergedCtx
init|=
name|indexer
operator|.
name|createMergedIndexingContext
argument_list|(
name|tempRepoId
argument_list|,
name|tempRepoId
argument_list|,
name|mergedIndexDirectory
operator|.
name|toFile
argument_list|()
argument_list|,
name|indexLocation
operator|.
name|toFile
argument_list|()
argument_list|,
literal|true
argument_list|,
name|memberProvider
argument_list|)
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|mergedCtx
operator|.
name|optimize
argument_list|()
expr_stmt|;
end_expr_stmt

begin_if_stmt
if|if
condition|(
name|indexMergerRequest
operator|.
name|isPackIndex
argument_list|()
condition|)
block|{
name|IndexPackingRequest
name|request
init|=
operator|new
name|IndexPackingRequest
argument_list|(
name|mergedCtx
argument_list|,
comment|//
name|mergedCtx
operator|.
name|acquireIndexSearcher
argument_list|()
operator|.
name|getIndexReader
argument_list|()
argument_list|,
comment|//
name|indexLocation
operator|.
name|toFile
argument_list|()
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
end_if_stmt

begin_if_stmt
if|if
condition|(
name|indexMergerRequest
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
name|temporaryGroupIndexes
operator|.
name|add
argument_list|(
operator|new
name|TemporaryGroupIndex
argument_list|(
name|mergedIndexDirectory
argument_list|,
name|tempRepoId
argument_list|,
name|groupId
argument_list|,
name|indexMergerRequest
operator|.
name|getMergedIndexTtl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|temporaryContextes
operator|.
name|add
argument_list|(
name|mergedCtx
argument_list|)
expr_stmt|;
block|}
end_if_stmt

begin_expr_stmt
name|stopWatch
operator|.
name|stop
argument_list|()
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|log
operator|.
name|info
argument_list|(
literal|"merged index for repos {} in {} s"
argument_list|,
name|indexMergerRequest
operator|.
name|getRepositoriesIds
argument_list|()
argument_list|,
name|stopWatch
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
operator|new
name|MavenIndexContext
argument_list|(
name|repositoryRegistry
operator|.
name|getRepositoryGroup
argument_list|(
name|groupId
argument_list|)
argument_list|,
name|mergedCtx
argument_list|)
return|;
end_return

begin_expr_stmt
unit|}         catch
operator|(
name|IOException
name|e
operator|)
block|{
throw|throw
argument_list|new
name|IndexMergerException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
block|;         }
end_expr_stmt

begin_finally
finally|finally
block|{
name|runningGroups
operator|.
name|remove
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
block|}
end_finally

begin_function
unit|}      @
name|Async
annotation|@
name|Override
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
name|Optional
argument_list|<
name|IndexingContext
argument_list|>
name|ctxOpt
init|=
name|temporaryContextes
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|ctx
lambda|->
name|ctx
operator|.
name|getId
argument_list|( )
operator|.
name|equals
argument_list|(
name|temporaryGroupIndex
operator|.
name|getIndexId
argument_list|( )
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|( )
decl_stmt|;
if|if
condition|(
name|ctxOpt
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|IndexingContext
name|ctx
init|=
name|ctxOpt
operator|.
name|get
argument_list|()
decl_stmt|;
name|indexer
operator|.
name|closeIndexingContext
argument_list|(
name|ctx
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|temporaryGroupIndexes
operator|.
name|remove
argument_list|(
name|temporaryGroupIndex
argument_list|)
expr_stmt|;
name|temporaryContextes
operator|.
name|remove
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|Path
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
name|Files
operator|.
name|exists
argument_list|(
name|directory
argument_list|)
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
block|}
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
end_function

begin_function
annotation|@
name|Override
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
end_function

unit|}
end_unit

