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
name|ArchivaIndexManager
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
name|IndexCreationFailedException
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
name|Repository
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
name|storage
operator|.
name|StorageAsset
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
name|storage
operator|.
name|StorageUtil
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
name|time
operator|.
name|StopWatch
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
name|ArchivaIndexingContext
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
parameter_list|( )
block|{
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
try|try
block|{
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
name|StorageAsset
name|mergedIndexDirectory
init|=
name|indexMergerRequest
operator|.
name|getMergedIndexDirectory
argument_list|()
decl_stmt|;
name|Repository
name|destinationRepository
init|=
name|repositoryRegistry
operator|.
name|getRepository
argument_list|(
name|indexMergerRequest
operator|.
name|getGroupId
argument_list|()
argument_list|)
decl_stmt|;
name|ArchivaIndexManager
name|idxManager
init|=
name|repositoryRegistry
operator|.
name|getIndexManager
argument_list|(
name|destinationRepository
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArchivaIndexingContext
argument_list|>
name|sourceContexts
init|=
name|indexMergerRequest
operator|.
name|getRepositoriesIds
argument_list|()
operator|.
name|stream
argument_list|()
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
operator|.
name|getIndexingContext
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ArchivaIndexingContext
name|result
init|=
name|idxManager
operator|.
name|mergeContexts
argument_list|(
name|destinationRepository
argument_list|,
name|sourceContexts
argument_list|,
name|indexMergerRequest
operator|.
name|isPackIndex
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexMergerRequest
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
name|String
name|tempRepoId
init|=
name|destinationRepository
operator|.
name|getId
argument_list|()
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
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
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|IndexCreationFailedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IndexMergerException
argument_list|(
literal|"Index merging failed "
operator|+
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
finally|finally
block|{
name|stopWatch
operator|.
name|stop
argument_list|()
expr_stmt|;
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
name|runningGroups
operator|.
name|remove
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
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
name|ArchivaIndexingContext
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
name|ArchivaIndexingContext
name|ctx
init|=
name|ctxOpt
operator|.
name|get
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|close
argument_list|(
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
name|StorageAsset
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
name|StorageUtil
operator|.
name|deleteRecursively
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
block|}
end_class

end_unit

