begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|indexing
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
name|scheduler
operator|.
name|ArchivaTaskScheduler
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
name|taskqueue
operator|.
name|TaskQueue
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
name|taskqueue
operator|.
name|TaskQueueException
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
name|inject
operator|.
name|Named
import|;
end_import

begin_comment
comment|/**  * Default implementation of a scheduling component for archiva.  *  * @todo TODO - consider just folding in, not really scheduled  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaTaskScheduler#indexing"
argument_list|)
specifier|public
class|class
name|IndexingArchivaTaskScheduler
implements|implements
name|ArchivaTaskScheduler
argument_list|<
name|ArtifactIndexingTask
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
name|IndexingArchivaTaskScheduler
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"taskQueue#indexing"
argument_list|)
specifier|private
name|TaskQueue
name|indexingQueue
decl_stmt|;
specifier|public
name|void
name|queueTask
parameter_list|(
name|ArtifactIndexingTask
name|task
parameter_list|)
throws|throws
name|TaskQueueException
block|{
name|indexingQueue
operator|.
name|put
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

