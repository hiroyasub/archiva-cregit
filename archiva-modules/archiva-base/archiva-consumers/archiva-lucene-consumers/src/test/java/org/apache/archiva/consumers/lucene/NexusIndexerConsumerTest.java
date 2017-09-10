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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|FileTypes
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
name|redback
operator|.
name|components
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
name|apache
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|indexing
operator|.
name|ArtifactIndexingTask
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
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
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
name|IndexCreator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
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
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|HashSet
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
name|Set
import|;
end_import

begin_comment
comment|/**  * NexusIndexerConsumerTest  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath*:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|NexusIndexerConsumerTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|final
class|class
name|ArchivaTaskSchedulerStub
implements|implements
name|ArchivaTaskScheduler
argument_list|<
name|ArtifactIndexingTask
argument_list|>
block|{
name|Set
argument_list|<
name|Path
argument_list|>
name|indexed
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
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
switch|switch
condition|(
name|task
operator|.
name|getAction
argument_list|()
condition|)
block|{
case|case
name|ADD
case|:
name|indexed
operator|.
name|add
argument_list|(
name|task
operator|.
name|getResourceFile
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|DELETE
case|:
name|indexed
operator|.
name|remove
argument_list|(
name|task
operator|.
name|getResourceFile
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|FINISH
case|:
try|try
block|{
name|task
operator|.
name|getContext
argument_list|()
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
throw|throw
operator|new
name|TaskQueueException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
break|break;
block|}
block|}
block|}
specifier|private
name|NexusIndexerConsumer
name|nexusIndexerConsumer
decl_stmt|;
specifier|private
name|ManagedRepository
name|repositoryConfig
decl_stmt|;
specifier|private
name|ArchivaTaskSchedulerStub
name|scheduler
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|NexusIndexer
name|nexusIndexer
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|List
argument_list|<
name|IndexCreator
argument_list|>
name|indexCreators
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
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
name|scheduler
operator|=
operator|new
name|ArchivaTaskSchedulerStub
argument_list|()
expr_stmt|;
name|ArchivaConfiguration
name|configuration
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|FileTypes
name|filetypes
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|FileTypes
operator|.
name|class
argument_list|)
decl_stmt|;
name|nexusIndexerConsumer
operator|=
operator|new
name|NexusIndexerConsumer
argument_list|(
name|scheduler
argument_list|,
name|configuration
argument_list|,
name|filetypes
argument_list|,
name|indexCreators
argument_list|,
name|managedRepositoryAdmin
argument_list|,
name|nexusIndexer
argument_list|)
expr_stmt|;
comment|// initialize to set the file types to be processed
name|nexusIndexerConsumer
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|repositoryConfig
operator|=
operator|new
name|ManagedRepository
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
literal|"target/test-classes/test-repo"
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
block|}
annotation|@
name|Override
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// delete created index in the repository
name|Path
name|indexDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
decl_stmt|;
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
operator|.
name|deleteDirectory
argument_list|(
name|indexDir
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|indexDir
argument_list|)
argument_list|)
expr_stmt|;
name|indexDir
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index"
argument_list|)
expr_stmt|;
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
operator|.
name|deleteDirectory
argument_list|(
name|indexDir
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|indexDir
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIndexerIndexArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|artifactFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
comment|// begin scan
name|Date
name|now
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|nexusIndexerConsumer
operator|.
name|beginScan
argument_list|(
name|repositoryConfig
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|processFile
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|scheduler
operator|.
name|indexed
operator|.
name|contains
argument_list|(
name|artifactFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIndexerArtifactAlreadyIndexed
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|artifactFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
comment|// begin scan
name|Date
name|now
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|nexusIndexerConsumer
operator|.
name|beginScan
argument_list|(
name|repositoryConfig
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|processFile
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|scheduler
operator|.
name|indexed
operator|.
name|contains
argument_list|(
name|artifactFile
argument_list|)
argument_list|)
expr_stmt|;
comment|// scan and index again
name|now
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|beginScan
argument_list|(
name|repositoryConfig
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|processFile
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|scheduler
operator|.
name|indexed
operator|.
name|contains
argument_list|(
name|artifactFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIndexerIndexArtifactThenPom
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|artifactFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
comment|// begin scan
name|Date
name|now
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|nexusIndexerConsumer
operator|.
name|beginScan
argument_list|(
name|repositoryConfig
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|processFile
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|scheduler
operator|.
name|indexed
operator|.
name|contains
argument_list|(
name|artifactFile
argument_list|)
argument_list|)
expr_stmt|;
name|artifactFile
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/pom.xml"
argument_list|)
expr_stmt|;
comment|// scan and index again
name|now
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|beginScan
argument_list|(
name|repositoryConfig
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|processFile
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/pom.xml"
argument_list|)
expr_stmt|;
name|nexusIndexerConsumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|scheduler
operator|.
name|indexed
operator|.
name|contains
argument_list|(
name|artifactFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// MRM-1275 - Include other file types for the index consumer instead of just the indexable-content
annotation|@
name|Test
specifier|public
name|void
name|testIncludedFileTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
name|nexusIndexerConsumer
operator|.
name|getIncludes
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|".pom artifacts should be processed."
argument_list|,
name|includes
operator|.
name|contains
argument_list|(
literal|"**/*.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|".xml artifacts should be processed."
argument_list|,
name|includes
operator|.
name|contains
argument_list|(
literal|"**/*.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|".txt artifacts should be processed."
argument_list|,
name|includes
operator|.
name|contains
argument_list|(
literal|"**/*.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|".jar artifacts should be processed."
argument_list|,
name|includes
operator|.
name|contains
argument_list|(
literal|"**/*.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|".war artifacts should be processed."
argument_list|,
name|includes
operator|.
name|contains
argument_list|(
literal|"**/*.war"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|".zip artifacts should be processed."
argument_list|,
name|includes
operator|.
name|contains
argument_list|(
literal|"**/*.zip"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

