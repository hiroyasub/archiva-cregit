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
name|scheduled
operator|.
name|executors
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
name|index
operator|.
name|IndexReader
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
name|scheduled
operator|.
name|tasks
operator|.
name|ArtifactIndexingTask
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
name|taskqueue
operator|.
name|Task
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
name|execution
operator|.
name|TaskExecutionException
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
name|execution
operator|.
name|TaskExecutor
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
name|ArtifactContext
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
name|ArtifactContextProducer
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
name|DefaultArtifactContextProducer
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
name|IndexerEngine
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
name|packer
operator|.
name|IndexPacker
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
name|packer
operator|.
name|IndexPackingRequest
import|;
end_import

begin_comment
comment|/**  * ArchivaIndexingTaskExecutor Executes all indexing tasks. Adding, updating and removing artifacts from the index are  * all performed by this executor. Add and update artifact in index tasks are added in the indexing task queue by the  * NexusIndexerConsumer while remove artifact from index tasks are added by the LuceneCleanupRemoveIndexedConsumer.  *   * @todo Nexus specifics shouldn't be in the archiva-scheduled module  * @plexus.component role="org.codehaus.plexus.taskqueue.execution.TaskExecutor" role-hint="indexing"  *                   instantiation-strategy="singleton"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaIndexingTaskExecutor
implements|implements
name|TaskExecutor
implements|,
name|Initializable
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ArchivaIndexingTaskExecutor
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|IndexerEngine
name|indexerEngine
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|IndexPacker
name|indexPacker
decl_stmt|;
specifier|private
name|ArtifactContextProducer
name|artifactContextProducer
decl_stmt|;
specifier|public
name|void
name|executeTask
parameter_list|(
name|Task
name|task
parameter_list|)
throws|throws
name|TaskExecutionException
block|{
synchronized|synchronized
init|(
name|indexerEngine
init|)
block|{
name|ArtifactIndexingTask
name|indexingTask
init|=
operator|(
name|ArtifactIndexingTask
operator|)
name|task
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|indexingTask
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|IndexingContext
name|context
init|=
name|indexingTask
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|FINISH
operator|.
name|equals
argument_list|(
name|indexingTask
operator|.
name|getAction
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|context
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|File
name|managedRepository
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|File
name|indexLocation
init|=
operator|new
name|File
argument_list|(
name|managedRepository
argument_list|,
literal|".index"
argument_list|)
decl_stmt|;
name|IndexPackingRequest
name|request
init|=
operator|new
name|IndexPackingRequest
argument_list|(
name|context
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
name|log
operator|.
name|debug
argument_list|(
literal|"Index file packaged at '"
operator|+
name|indexLocation
operator|.
name|getPath
argument_list|()
operator|+
literal|"'."
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
name|error
argument_list|(
literal|"Error occurred while executing indexing task '"
operator|+
name|indexingTask
operator|+
literal|"'"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Error occurred while executing indexing task '"
operator|+
name|indexingTask
operator|+
literal|"'"
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|context
operator|.
name|close
argument_list|(
literal|false
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
name|error
argument_list|(
literal|"Error occurred while closing context: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Error occurred while closing context: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|context
operator|.
name|getIndexDirectory
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Trying to index an artifact but the context is already closed"
argument_list|)
throw|;
block|}
try|try
block|{
name|File
name|artifactFile
init|=
name|indexingTask
operator|.
name|getResourceFile
argument_list|()
decl_stmt|;
name|ArtifactContext
name|ac
init|=
name|artifactContextProducer
operator|.
name|getArtifactContext
argument_list|(
name|context
argument_list|,
name|artifactFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|ac
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|indexingTask
operator|.
name|getAction
argument_list|()
operator|.
name|equals
argument_list|(
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|)
condition|)
block|{
name|boolean
name|add
init|=
literal|true
decl_stmt|;
name|IndexReader
name|r
init|=
name|context
operator|.
name|getIndexReader
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|r
operator|.
name|numDocs
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|r
operator|.
name|isDeleted
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|Document
name|d
init|=
name|r
operator|.
name|document
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|uinfo
init|=
name|d
operator|.
name|get
argument_list|(
name|ArtifactInfo
operator|.
name|UINFO
argument_list|)
decl_stmt|;
if|if
condition|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|getUinfo
argument_list|()
operator|.
name|equals
argument_list|(
name|uinfo
argument_list|)
condition|)
block|{
name|add
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|add
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Adding artifact '"
operator|+
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|+
literal|"' to index.."
argument_list|)
expr_stmt|;
name|indexerEngine
operator|.
name|index
argument_list|(
name|context
argument_list|,
name|ac
argument_list|)
expr_stmt|;
name|context
operator|.
name|getIndexWriter
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Updating artifact '"
operator|+
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|+
literal|"' in index.."
argument_list|)
expr_stmt|;
name|indexerEngine
operator|.
name|update
argument_list|(
name|context
argument_list|,
name|ac
argument_list|)
expr_stmt|;
name|context
operator|.
name|getIndexWriter
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Removing artifact '"
operator|+
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|+
literal|"' from index.."
argument_list|)
expr_stmt|;
name|indexerEngine
operator|.
name|remove
argument_list|(
name|context
argument_list|,
name|ac
argument_list|)
expr_stmt|;
name|context
operator|.
name|getIndexWriter
argument_list|()
operator|.
name|commit
argument_list|()
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
name|error
argument_list|(
literal|"Error occurred while executing indexing task '"
operator|+
name|indexingTask
operator|+
literal|"'"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Error occurred while executing indexing task '"
operator|+
name|indexingTask
operator|+
literal|"'"
argument_list|)
throw|;
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
name|log
operator|.
name|info
argument_list|(
literal|"Initialized "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|artifactContextProducer
operator|=
operator|new
name|DefaultArtifactContextProducer
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setIndexerEngine
parameter_list|(
name|IndexerEngine
name|indexerEngine
parameter_list|)
block|{
name|this
operator|.
name|indexerEngine
operator|=
name|indexerEngine
expr_stmt|;
block|}
specifier|public
name|void
name|setIndexPacker
parameter_list|(
name|IndexPacker
name|indexPacker
parameter_list|)
block|{
name|this
operator|.
name|indexPacker
operator|=
name|indexPacker
expr_stmt|;
block|}
block|}
end_class

end_unit

