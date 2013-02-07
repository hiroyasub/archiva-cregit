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
name|Scheduled
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
name|Date
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M2  */
end_comment

begin_class
annotation|@
name|Service
specifier|public
class|class
name|TemporaryGroupIndexCleaner
block|{
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
name|IndexMerger
name|indexMerger
decl_stmt|;
specifier|private
name|NexusIndexer
name|indexer
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|TemporaryGroupIndexCleaner
parameter_list|(
name|PlexusSisuBridge
name|plexusSisuBridge
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
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
block|}
comment|// 900000
annotation|@
name|Scheduled
argument_list|(
name|fixedDelay
operator|=
literal|900000
argument_list|)
specifier|public
name|void
name|cleanTemporaryIndex
parameter_list|()
block|{
name|log
operator|.
name|info
argument_list|(
literal|"cleanTemporaryIndex"
argument_list|)
expr_stmt|;
for|for
control|(
name|TemporaryGroupIndex
name|temporaryGroupIndex
range|:
name|indexMerger
operator|.
name|getTemporaryGroupIndexes
argument_list|()
control|)
block|{
comment|// cleanup files older than 60 minutes 3600000
if|if
condition|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
name|temporaryGroupIndex
operator|.
name|getCreationTime
argument_list|()
operator|>
literal|3600000
condition|)
block|{
name|indexMerger
operator|.
name|cleanTemporaryGroupIndex
argument_list|(
name|temporaryGroupIndex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

