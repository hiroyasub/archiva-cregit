begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
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
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|BaseFile
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
name|utils
operator|.
name|PathUtil
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
name|consumers
operator|.
name|InvalidRepositoryContentConsumer
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|consumers
operator|.
name|RepositoryContentConsumer
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
name|consumers
operator|.
name|functors
operator|.
name|ConsumerWantsFilePredicate
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
name|repository
operator|.
name|scanner
operator|.
name|functors
operator|.
name|ConsumerProcessFileClosure
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
name|scanner
operator|.
name|functors
operator|.
name|TriggerBeginScanClosure
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
name|scanner
operator|.
name|functors
operator|.
name|TriggerScanCompletedClosure
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
name|Closure
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
name|collections4
operator|.
name|IterableUtils
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
name|functors
operator|.
name|IfClosure
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
name|SystemUtils
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
name|FileSystem
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
name|FileSystems
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
name|FileVisitResult
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
name|FileVisitor
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
name|nio
operator|.
name|file
operator|.
name|PathMatcher
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
name|attribute
operator|.
name|BasicFileAttributes
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * RepositoryScannerInstance  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryScannerInstance
implements|implements
name|FileVisitor
argument_list|<
name|Path
argument_list|>
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RepositoryScannerInstance
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Consumers that process known content.      */
specifier|private
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
decl_stmt|;
comment|/**      * Consumers that process unknown/invalid content.      */
specifier|private
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
decl_stmt|;
specifier|private
name|ManagedRepository
name|repository
decl_stmt|;
specifier|private
name|RepositoryScanStatistics
name|stats
decl_stmt|;
specifier|private
name|long
name|changesSince
init|=
literal|0
decl_stmt|;
specifier|private
name|ConsumerProcessFileClosure
name|consumerProcessFile
decl_stmt|;
specifier|private
name|ConsumerWantsFilePredicate
name|consumerWantsFile
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|consumerTimings
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|consumerCounts
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|fileNameIncludePattern
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|fileNameExcludePattern
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|PathMatcher
argument_list|>
name|includeMatcher
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|PathMatcher
argument_list|>
name|excludeMatcher
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|isRunning
init|=
literal|false
decl_stmt|;
name|Path
name|basePath
init|=
literal|null
decl_stmt|;
specifier|public
name|RepositoryScannerInstance
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumerList
parameter_list|,
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumerList
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|knownConsumers
operator|=
name|knownConsumerList
expr_stmt|;
name|this
operator|.
name|invalidConsumers
operator|=
name|invalidConsumerList
expr_stmt|;
name|addFileNameIncludePattern
argument_list|(
literal|"**/*"
argument_list|)
expr_stmt|;
name|consumerTimings
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|consumerCounts
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|consumerProcessFile
operator|=
operator|new
name|ConsumerProcessFileClosure
argument_list|()
expr_stmt|;
name|consumerProcessFile
operator|.
name|setExecuteOnEntireRepo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|consumerProcessFile
operator|.
name|setConsumerTimings
argument_list|(
name|consumerTimings
argument_list|)
expr_stmt|;
name|consumerProcessFile
operator|.
name|setConsumerCounts
argument_list|(
name|consumerCounts
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumerWantsFile
operator|=
operator|new
name|ConsumerWantsFilePredicate
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|stats
operator|=
operator|new
name|RepositoryScanStatistics
argument_list|()
expr_stmt|;
name|stats
operator|.
name|setRepositoryId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Closure
argument_list|<
name|RepositoryContentConsumer
argument_list|>
name|triggerBeginScan
init|=
operator|new
name|TriggerBeginScanClosure
argument_list|(
name|repository
argument_list|,
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|IterableUtils
operator|.
name|forEach
argument_list|(
name|knownConsumerList
argument_list|,
name|triggerBeginScan
argument_list|)
expr_stmt|;
name|IterableUtils
operator|.
name|forEach
argument_list|(
name|invalidConsumerList
argument_list|,
name|triggerBeginScan
argument_list|)
expr_stmt|;
if|if
condition|(
name|SystemUtils
operator|.
name|IS_OS_WINDOWS
condition|)
block|{
name|consumerWantsFile
operator|.
name|setCaseSensitive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|RepositoryScannerInstance
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownContentConsumers
parameter_list|,
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidContentConsumers
parameter_list|,
name|long
name|changesSince
parameter_list|)
block|{
name|this
argument_list|(
name|repository
argument_list|,
name|knownContentConsumers
argument_list|,
name|invalidContentConsumers
argument_list|)
expr_stmt|;
name|consumerWantsFile
operator|.
name|setChangesSince
argument_list|(
name|changesSince
argument_list|)
expr_stmt|;
name|this
operator|.
name|changesSince
operator|=
name|changesSince
expr_stmt|;
block|}
specifier|public
name|RepositoryScanStatistics
name|getStatistics
parameter_list|()
block|{
return|return
name|stats
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|getConsumerTimings
parameter_list|()
block|{
return|return
name|consumerTimings
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|getConsumerCounts
parameter_list|()
block|{
return|return
name|consumerCounts
return|;
block|}
specifier|public
name|ManagedRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|RepositoryScanStatistics
name|getStats
parameter_list|()
block|{
return|return
name|stats
return|;
block|}
specifier|public
name|long
name|getChangesSince
parameter_list|()
block|{
return|return
name|changesSince
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFileNameIncludePattern
parameter_list|()
block|{
return|return
name|fileNameIncludePattern
return|;
block|}
specifier|public
name|void
name|setFileNameIncludePattern
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|fileNamePattern
parameter_list|)
block|{
name|this
operator|.
name|fileNameIncludePattern
operator|=
name|fileNamePattern
expr_stmt|;
name|FileSystem
name|sys
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|this
operator|.
name|includeMatcher
operator|=
name|fileNamePattern
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ts
lambda|->
name|sys
operator|.
name|getPathMatcher
argument_list|(
literal|"glob:"
operator|+
name|ts
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addFileNameIncludePattern
parameter_list|(
name|String
name|fileNamePattern
parameter_list|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|fileNameIncludePattern
operator|.
name|contains
argument_list|(
name|fileNamePattern
argument_list|)
condition|)
block|{
name|this
operator|.
name|fileNameIncludePattern
operator|.
name|add
argument_list|(
name|fileNamePattern
argument_list|)
expr_stmt|;
name|this
operator|.
name|includeMatcher
operator|.
name|add
argument_list|(
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPathMatcher
argument_list|(
literal|"glob:"
operator|+
name|fileNamePattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFileNameExcludePattern
parameter_list|()
block|{
return|return
name|fileNameExcludePattern
return|;
block|}
specifier|public
name|void
name|setFileNameExcludePattern
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|fileNamePattern
parameter_list|)
block|{
name|this
operator|.
name|fileNameExcludePattern
operator|=
name|fileNamePattern
expr_stmt|;
name|FileSystem
name|sys
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|this
operator|.
name|excludeMatcher
operator|=
name|fileNamePattern
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ts
lambda|->
name|sys
operator|.
name|getPathMatcher
argument_list|(
literal|"glob:"
operator|+
name|ts
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addFileNameExcludePattern
parameter_list|(
name|String
name|fileNamePattern
parameter_list|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|fileNameExcludePattern
operator|.
name|contains
argument_list|(
name|fileNamePattern
argument_list|)
condition|)
block|{
name|this
operator|.
name|fileNameExcludePattern
operator|.
name|add
argument_list|(
name|fileNamePattern
argument_list|)
expr_stmt|;
name|this
operator|.
name|excludeMatcher
operator|.
name|add
argument_list|(
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPathMatcher
argument_list|(
literal|"glob:"
operator|+
name|fileNamePattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|preVisitDirectory
parameter_list|(
name|Path
name|dir
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|isRunning
condition|)
block|{
name|isRunning
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|basePath
operator|=
name|dir
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Walk Started: [{}] {}"
argument_list|,
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|this
operator|.
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|stats
operator|.
name|triggerStart
argument_list|()
expr_stmt|;
block|}
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|visitFile
parameter_list|(
name|Path
name|file
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|relativeFile
init|=
name|basePath
operator|.
name|relativize
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|excludeMatcher
operator|.
name|stream
argument_list|()
operator|.
name|noneMatch
argument_list|(
name|m
lambda|->
name|m
operator|.
name|matches
argument_list|(
name|relativeFile
argument_list|)
argument_list|)
operator|&&
name|includeMatcher
operator|.
name|stream
argument_list|()
operator|.
name|allMatch
argument_list|(
name|m
lambda|->
name|m
operator|.
name|matches
argument_list|(
name|relativeFile
argument_list|)
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Walk Step: {}, {}"
argument_list|,
name|file
argument_list|)
block|;
name|stats
operator|.
name|increaseFileCount
argument_list|()
empty_stmt|;
comment|// consume files regardless - the predicate will check the timestamp
name|Path
name|repoPath
init|=
name|PathUtil
operator|.
name|getPathFromUri
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|BaseFile
name|basefile
init|=
operator|new
name|BaseFile
argument_list|(
name|repoPath
operator|.
name|toString
argument_list|()
argument_list|,
name|file
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
comment|// Timestamp finished points to the last successful scan, not this current one.
if|if
condition|(
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|file
argument_list|)
operator|.
name|toMillis
argument_list|()
operator|>=
name|changesSince
condition|)
block|{
name|stats
operator|.
name|increaseNewFileCount
argument_list|()
expr_stmt|;
block|}
name|consumerProcessFile
operator|.
name|setBasefile
argument_list|(
name|basefile
argument_list|)
expr_stmt|;
name|consumerWantsFile
operator|.
name|setBasefile
argument_list|(
name|basefile
argument_list|)
expr_stmt|;
name|Closure
argument_list|<
name|RepositoryContentConsumer
argument_list|>
name|processIfWanted
init|=
name|IfClosure
operator|.
name|ifClosure
argument_list|(
name|consumerWantsFile
argument_list|,
name|consumerProcessFile
argument_list|)
decl_stmt|;
name|IterableUtils
operator|.
name|forEach
argument_list|(
name|this
operator|.
name|knownConsumers
argument_list|,
name|processIfWanted
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumerWantsFile
operator|.
name|getWantedFileCount
argument_list|()
operator|<=
literal|0
condition|)
block|{
comment|// Nothing known processed this file.  It is invalid!
name|IterableUtils
operator|.
name|forEach
argument_list|(
name|this
operator|.
name|invalidConsumers
argument_list|,
name|consumerProcessFile
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
end_class

begin_function
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|visitFileFailed
parameter_list|(
name|Path
name|file
parameter_list|,
name|IOException
name|exc
parameter_list|)
throws|throws
name|IOException
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error occured at {}: {}"
argument_list|,
name|file
argument_list|,
name|exc
operator|.
name|getMessage
argument_list|()
argument_list|,
name|exc
argument_list|)
expr_stmt|;
if|if
condition|(
name|basePath
operator|!=
literal|null
operator|&&
name|Files
operator|.
name|isSameFile
argument_list|(
name|file
argument_list|,
name|basePath
argument_list|)
condition|)
block|{
name|finishWalk
argument_list|()
expr_stmt|;
block|}
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|postVisitDirectory
parameter_list|(
name|Path
name|dir
parameter_list|,
name|IOException
name|exc
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|Files
operator|.
name|isSameFile
argument_list|(
name|dir
argument_list|,
name|basePath
argument_list|)
condition|)
block|{
name|finishWalk
argument_list|()
expr_stmt|;
block|}
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
end_function

begin_function
specifier|private
name|void
name|finishWalk
parameter_list|()
block|{
name|this
operator|.
name|isRunning
operator|=
literal|false
expr_stmt|;
name|TriggerScanCompletedClosure
name|scanCompletedClosure
init|=
operator|new
name|TriggerScanCompletedClosure
argument_list|(
name|repository
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|IterableUtils
operator|.
name|forEach
argument_list|(
name|knownConsumers
argument_list|,
name|scanCompletedClosure
argument_list|)
expr_stmt|;
name|IterableUtils
operator|.
name|forEach
argument_list|(
name|invalidConsumers
argument_list|,
name|scanCompletedClosure
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setConsumerTimings
argument_list|(
name|consumerTimings
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setConsumerCounts
argument_list|(
name|consumerCounts
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Walk Finished: [{}] {}"
argument_list|,
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|this
operator|.
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|stats
operator|.
name|triggerFinished
argument_list|()
expr_stmt|;
name|this
operator|.
name|basePath
operator|=
literal|null
expr_stmt|;
block|}
end_function

unit|}
end_unit

