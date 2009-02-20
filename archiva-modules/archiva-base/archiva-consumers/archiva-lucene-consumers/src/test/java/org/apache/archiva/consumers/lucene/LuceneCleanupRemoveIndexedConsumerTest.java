begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|lucene
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
name|model
operator|.
name|ArchivaArtifact
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
name|ManagedRepositoryContent
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
name|RepositoryContentFactory
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
name|content
operator|.
name|ManagedDefaultRepositoryContent
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
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|MockClassControl
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
name|NexusIndexer
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
name|DefaultIndexingContext
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

begin_class
specifier|public
class|class
name|LuceneCleanupRemoveIndexedConsumerTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|LuceneCleanupRemoveIndexedConsumer
name|consumer
decl_stmt|;
specifier|private
name|MockControl
name|indexerControl
decl_stmt|;
specifier|private
name|NexusIndexer
name|indexer
decl_stmt|;
specifier|private
name|RepositoryContentFactory
name|repoFactory
decl_stmt|;
specifier|private
name|MockControl
name|repoFactoryControl
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|repositoryConfig
decl_stmt|;
specifier|private
name|MockControl
name|contextProducerControl
decl_stmt|;
specifier|private
name|ArtifactContextProducer
name|artifactContextProducer
decl_stmt|;
specifier|private
name|MockControl
name|acControl
decl_stmt|;
specifier|private
name|ArtifactContext
name|ac
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|indexerControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|NexusIndexer
operator|.
name|class
argument_list|)
expr_stmt|;
name|indexerControl
operator|.
name|setDefaultMatcher
argument_list|(
name|MockControl
operator|.
name|ALWAYS_MATCHER
argument_list|)
expr_stmt|;
name|indexer
operator|=
operator|(
name|NexusIndexer
operator|)
name|indexerControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|repoFactoryControl
operator|=
name|MockClassControl
operator|.
name|createControl
argument_list|(
name|RepositoryContentFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|repoFactory
operator|=
operator|(
name|RepositoryContentFactory
operator|)
name|repoFactoryControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|consumer
operator|=
operator|new
name|LuceneCleanupRemoveIndexedConsumer
argument_list|(
name|repoFactory
argument_list|,
name|indexer
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
expr_stmt|;
name|repositoryConfig
operator|.
name|setId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setLocation
argument_list|(
name|getBasedir
argument_list|()
operator|+
literal|"/target/test-classes/test-repo"
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setName
argument_list|(
literal|"Test Repository"
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setScanned
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setIndexDir
argument_list|(
name|getBasedir
argument_list|()
operator|+
literal|"/target/test-classes/test-repo/.cleanup-index"
argument_list|)
expr_stmt|;
name|contextProducerControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArtifactContextProducer
operator|.
name|class
argument_list|)
expr_stmt|;
name|artifactContextProducer
operator|=
operator|(
name|ArtifactContextProducer
operator|)
name|contextProducerControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|setArtifactContextProducer
argument_list|(
name|artifactContextProducer
argument_list|)
expr_stmt|;
name|acControl
operator|=
name|MockClassControl
operator|.
name|createControl
argument_list|(
name|ArtifactContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|ac
operator|=
operator|(
name|ArtifactContext
operator|)
name|acControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getIndexDir
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testProcessArtifactArtifactDoesNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
init|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"archiva-lucene-consumers"
argument_list|,
literal|"1.2"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
decl_stmt|;
name|ManagedRepositoryContent
name|repoContent
init|=
operator|new
name|ManagedDefaultRepositoryContent
argument_list|()
decl_stmt|;
name|repoContent
operator|.
name|setRepository
argument_list|(
name|repositoryConfig
argument_list|)
expr_stmt|;
name|IndexingContext
name|context
init|=
operator|new
name|DefaultIndexingContext
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getIndexDir
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|NexusIndexer
operator|.
name|FULL_INDEX
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-lucene-consumers/1.2/archiva-lucene-consumers-1.2.jar"
argument_list|)
decl_stmt|;
name|ArtifactInfo
name|ai
init|=
operator|new
name|ArtifactInfo
argument_list|(
literal|"test-repo"
argument_list|,
literal|"org.apache.archiva"
argument_list|,
literal|"archiva-lucene-consumers"
argument_list|,
literal|"1.2"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|repoFactoryControl
operator|.
name|expectAndReturn
argument_list|(
name|repoFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|repoContent
argument_list|)
expr_stmt|;
name|indexerControl
operator|.
name|expectAndReturn
argument_list|(
name|indexer
operator|.
name|addIndexingContext
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getIndexDir
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|NexusIndexer
operator|.
name|FULL_INDEX
argument_list|)
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|contextProducerControl
operator|.
name|expectAndReturn
argument_list|(
name|artifactContextProducer
operator|.
name|getArtifactContext
argument_list|(
name|context
argument_list|,
name|artifactFile
argument_list|)
argument_list|,
name|ac
argument_list|)
expr_stmt|;
name|acControl
operator|.
name|expectAndReturn
argument_list|(
name|ac
operator|.
name|getArtifactInfo
argument_list|()
argument_list|,
name|ai
argument_list|)
expr_stmt|;
name|repoFactoryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|indexerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|contextProducerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|acControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|processArchivaArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|repoFactoryControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|indexerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|contextProducerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|acControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testProcessArtifactArtifactExists
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
init|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-lucene-cleanup"
argument_list|,
literal|"1.0"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
decl_stmt|;
name|ManagedRepositoryContent
name|repoContent
init|=
operator|new
name|ManagedDefaultRepositoryContent
argument_list|()
decl_stmt|;
name|repoContent
operator|.
name|setRepository
argument_list|(
name|repositoryConfig
argument_list|)
expr_stmt|;
name|IndexingContext
name|context
init|=
operator|new
name|DefaultIndexingContext
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getIndexDir
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|NexusIndexer
operator|.
name|FULL_INDEX
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|repoFactoryControl
operator|.
name|expectAndReturn
argument_list|(
name|repoFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|repoContent
argument_list|)
expr_stmt|;
name|indexerControl
operator|.
name|expectAndReturn
argument_list|(
name|indexer
operator|.
name|addIndexingContext
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getIndexDir
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|NexusIndexer
operator|.
name|FULL_INDEX
argument_list|)
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|repoFactoryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|indexerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|processArchivaArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|repoFactoryControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|indexerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

