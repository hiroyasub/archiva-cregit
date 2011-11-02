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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the Li  * cense is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|RepositoryAdminException
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
name|beans
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
name|lang
operator|.
name|StringUtils
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
name|maven
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
name|apache
operator|.
name|maven
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
name|apache
operator|.
name|maven
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
name|apache
operator|.
name|maven
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
name|apache
operator|.
name|maven
operator|.
name|index
operator|.
name|MAVEN
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
name|artifact
operator|.
name|IllegalArtifactCoordinateException
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
name|expr
operator|.
name|SourcedSearchExpression
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
name|List
import|;
end_import

begin_comment
comment|/**  * ArchivaIndexingTaskExecutor Executes all indexing tasks. Adding, updating and removing artifacts from the index are  * all performed by this executor. Add and update artifact in index tasks are added in the indexing task queue by the  * NexusIndexerConsumer while remove artifact from index tasks are added by the LuceneCleanupRemoveIndexedConsumer.  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"taskExecutor#indexing"
argument_list|)
specifier|public
class|class
name|ArchivaIndexingTaskExecutor
implements|implements
name|TaskExecutor
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
comment|/**      *      */
specifier|private
name|IndexPacker
name|indexPacker
decl_stmt|;
specifier|private
name|ArtifactContextProducer
name|artifactContextProducer
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|MavenIndexerUtils
name|mavenIndexerUtils
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
specifier|private
name|NexusIndexer
name|nexusIndexer
decl_stmt|;
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|IndexCreator
argument_list|>
name|allIndexCreators
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|PlexusSisuBridgeException
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Initialized {}"
argument_list|,
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
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|ArtifactContextProducer
operator|.
name|class
argument_list|)
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
name|nexusIndexer
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
name|allIndexCreators
operator|=
name|mavenIndexerUtils
operator|.
name|getAllIndexCreators
argument_list|()
expr_stmt|;
block|}
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
name|nexusIndexer
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
name|ManagedRepository
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
operator|&&
name|indexingTask
operator|.
name|isExecuteOnEntireRepo
argument_list|()
condition|)
block|{
try|try
block|{
name|nexusIndexer
operator|.
name|scan
argument_list|(
name|context
argument_list|,
literal|null
argument_list|,
name|indexingTask
operator|.
name|isOnlyUpdate
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Error scan repository "
operator|+
name|repository
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Finishing indexing task on repo: {}"
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|finishIndexingTask
argument_list|(
name|indexingTask
argument_list|,
name|repository
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// create context if not a repo scan request
if|if
condition|(
operator|!
name|indexingTask
operator|.
name|isExecuteOnEntireRepo
argument_list|()
condition|)
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Creating indexing context on resource: {}"
argument_list|,
name|indexingTask
operator|.
name|getResourceFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|=
name|managedRepositoryAdmin
operator|.
name|createIndexContext
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error occurred while creating context: "
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
literal|"Error occurred while creating context: "
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
if|if
condition|(
name|context
operator|==
literal|null
operator|||
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
comment|//IndexSearcher s = context.getIndexSearcher();
comment|//String uinfo = ac.getArtifactInfo().getUinfo();
comment|//TopDocs d = s.search( new TermQuery( new Term( ArtifactInfo.UINFO, uinfo ) ), 1 );
name|BooleanQuery
name|q
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|q
operator|.
name|add
argument_list|(
name|nexusIndexer
operator|.
name|constructQuery
argument_list|(
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|SourcedSearchExpression
argument_list|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|groupId
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|nexusIndexer
operator|.
name|constructQuery
argument_list|(
name|MAVEN
operator|.
name|ARTIFACT_ID
argument_list|,
operator|new
name|SourcedSearchExpression
argument_list|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|artifactId
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|nexusIndexer
operator|.
name|constructQuery
argument_list|(
name|MAVEN
operator|.
name|VERSION
argument_list|,
operator|new
name|SourcedSearchExpression
argument_list|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|version
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
if|if
condition|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|classifier
operator|!=
literal|null
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|nexusIndexer
operator|.
name|constructQuery
argument_list|(
name|MAVEN
operator|.
name|CLASSIFIER
argument_list|,
operator|new
name|SourcedSearchExpression
argument_list|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|classifier
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|packaging
operator|!=
literal|null
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|nexusIndexer
operator|.
name|constructQuery
argument_list|(
name|MAVEN
operator|.
name|PACKAGING
argument_list|,
operator|new
name|SourcedSearchExpression
argument_list|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|packaging
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
block|}
name|FlatSearchRequest
name|flatSearchRequest
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|q
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|FlatSearchResponse
name|flatSearchResponse
init|=
name|nexusIndexer
operator|.
name|searchFlat
argument_list|(
name|flatSearchRequest
argument_list|)
decl_stmt|;
if|if
condition|(
name|flatSearchResponse
operator|.
name|getResults
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Adding artifact '{}' to index.."
argument_list|,
name|ac
operator|.
name|getArtifactInfo
argument_list|()
argument_list|)
expr_stmt|;
name|nexusIndexer
operator|.
name|addArtifactToIndex
argument_list|(
name|ac
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Updating artifact '{}' in index.."
argument_list|,
name|ac
operator|.
name|getArtifactInfo
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO check if update exists !!
name|nexusIndexer
operator|.
name|deleteArtifactFromIndex
argument_list|(
name|ac
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|nexusIndexer
operator|.
name|addArtifactToIndex
argument_list|(
name|ac
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|updateTimestamp
argument_list|()
expr_stmt|;
comment|// close the context if not a repo scan request
if|if
condition|(
operator|!
name|indexingTask
operator|.
name|isExecuteOnEntireRepo
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Finishing indexing task on resource file : {}"
argument_list|,
name|indexingTask
operator|.
name|getResourceFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|finishIndexingTask
argument_list|(
name|indexingTask
argument_list|,
name|repository
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Removing artifact '{}' from index.."
argument_list|,
name|ac
operator|.
name|getArtifactInfo
argument_list|()
argument_list|)
expr_stmt|;
name|nexusIndexer
operator|.
name|deleteArtifactFromIndex
argument_list|(
name|ac
argument_list|,
name|context
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
name|error
argument_list|(
literal|"Error occurred while executing indexing task '"
operator|+
name|indexingTask
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
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
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalArtifactCoordinateException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error occurred while getting artifact context: "
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
literal|"Error occurred while getting artifact context."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|finishIndexingTask
parameter_list|(
name|ArtifactIndexingTask
name|indexingTask
parameter_list|,
name|ManagedRepository
name|repository
parameter_list|,
name|IndexingContext
name|context
parameter_list|)
throws|throws
name|TaskExecutionException
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
name|String
name|indexDirectory
init|=
name|repository
operator|.
name|getIndexDirectory
argument_list|()
decl_stmt|;
specifier|final
name|File
name|indexLocation
init|=
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|indexDirectory
argument_list|)
condition|?
operator|new
name|File
argument_list|(
name|managedRepository
argument_list|,
literal|".indexer"
argument_list|)
else|:
operator|new
name|File
argument_list|(
name|indexDirectory
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
name|context
operator|.
name|updateTimestamp
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Index file packaged at '{}'."
argument_list|,
name|indexLocation
operator|.
name|getPath
argument_list|()
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
literal|"': "
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
literal|"Error occurred while executing indexing task '"
operator|+
name|indexingTask
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
specifier|public
name|PlexusSisuBridge
name|getPlexusSisuBridge
parameter_list|()
block|{
return|return
name|plexusSisuBridge
return|;
block|}
specifier|public
name|void
name|setPlexusSisuBridge
parameter_list|(
name|PlexusSisuBridge
name|plexusSisuBridge
parameter_list|)
block|{
name|this
operator|.
name|plexusSisuBridge
operator|=
name|plexusSisuBridge
expr_stmt|;
block|}
block|}
end_class

end_unit

