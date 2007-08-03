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
name|commons
operator|.
name|collections
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
name|apache
operator|.
name|maven
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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaRepository
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
name|model
operator|.
name|RepositoryContentStatistics
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
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
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
name|maven
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
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
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
name|util
operator|.
name|DirectoryWalkListener
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * RepositoryScannerInstance   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryScannerInstance
implements|implements
name|DirectoryWalkListener
block|{
comment|/**      * Consumers that process known content.      */
specifier|private
name|List
name|knownConsumers
decl_stmt|;
comment|/**      * Consumers that process unknown/invalid content.      */
specifier|private
name|List
name|invalidConsumers
decl_stmt|;
name|ArchivaRepository
name|repository
decl_stmt|;
specifier|private
name|RepositoryContentStatistics
name|stats
decl_stmt|;
specifier|private
name|long
name|onlyModifiedAfterTimestamp
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
name|Logger
name|logger
decl_stmt|;
specifier|public
name|RepositoryScannerInstance
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|,
name|List
name|knownConsumerList
parameter_list|,
name|List
name|invalidConsumerList
parameter_list|,
name|Logger
name|logger
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
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|consumerProcessFile
operator|=
operator|new
name|ConsumerProcessFileClosure
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumerWantsFile
operator|=
operator|new
name|ConsumerWantsFilePredicate
argument_list|()
expr_stmt|;
name|stats
operator|=
operator|new
name|RepositoryContentStatistics
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
name|triggerBeginScan
init|=
operator|new
name|TriggerBeginScanClosure
argument_list|(
name|repository
argument_list|,
name|logger
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|knownConsumerList
argument_list|,
name|triggerBeginScan
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
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
name|RepositoryContentStatistics
name|getStatistics
parameter_list|()
block|{
return|return
name|stats
return|;
block|}
specifier|public
name|void
name|directoryWalkStarting
parameter_list|(
name|File
name|basedir
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Walk Started: ["
operator|+
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
operator|+
literal|"] "
operator|+
name|this
operator|.
name|repository
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|stats
operator|.
name|triggerStart
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|directoryWalkStep
parameter_list|(
name|int
name|percentage
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Walk Step: "
operator|+
name|percentage
operator|+
literal|", "
operator|+
name|file
argument_list|)
expr_stmt|;
name|stats
operator|.
name|increaseFileCount
argument_list|()
expr_stmt|;
comment|// Timestamp finished points to the last successful scan, not this current one.
if|if
condition|(
name|file
operator|.
name|lastModified
argument_list|()
operator|<
name|onlyModifiedAfterTimestamp
condition|)
block|{
comment|// Skip file as no change has occured.
name|logger
operator|.
name|debug
argument_list|(
literal|"Skipping, No Change: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|stats
operator|.
name|increaseNewFileCount
argument_list|()
expr_stmt|;
name|BaseFile
name|basefile
init|=
operator|new
name|BaseFile
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|file
argument_list|)
decl_stmt|;
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
name|processIfWanted
init|=
name|IfClosure
operator|.
name|getInstance
argument_list|(
name|consumerWantsFile
argument_list|,
name|consumerProcessFile
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
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
name|CollectionUtils
operator|.
name|forAllDo
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
specifier|public
name|void
name|directoryWalkFinished
parameter_list|()
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Walk Finished: ["
operator|+
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
operator|+
literal|"] "
operator|+
name|this
operator|.
name|repository
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|stats
operator|.
name|triggerFinished
argument_list|()
expr_stmt|;
block|}
specifier|public
name|long
name|getOnlyModifiedAfterTimestamp
parameter_list|()
block|{
return|return
name|onlyModifiedAfterTimestamp
return|;
block|}
specifier|public
name|void
name|setOnlyModifiedAfterTimestamp
parameter_list|(
name|long
name|onlyModifiedAfterTimestamp
parameter_list|)
block|{
name|this
operator|.
name|onlyModifiedAfterTimestamp
operator|=
name|onlyModifiedAfterTimestamp
expr_stmt|;
block|}
comment|/**      * Debug method from DirectoryWalker.      */
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Repository Scanner: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

