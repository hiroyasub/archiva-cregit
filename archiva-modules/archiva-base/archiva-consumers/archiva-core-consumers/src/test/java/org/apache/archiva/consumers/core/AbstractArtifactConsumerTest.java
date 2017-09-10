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
name|core
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
name|FileType
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
name|IndexingContext
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

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
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractArtifactConsumerTest
block|{
specifier|private
name|Path
name|repoLocation
decl_stmt|;
specifier|protected
name|KnownRepositoryContentConsumer
name|consumer
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Inject
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|NexusIndexer
name|nexusIndexer
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|FileType
name|fileType
init|=
operator|(
name|FileType
operator|)
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getFileTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|,
name|fileType
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|fileType
operator|.
name|addPattern
argument_list|(
literal|"**/*.xml"
argument_list|)
expr_stmt|;
name|repoLocation
operator|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/test-repo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|IndexingContext
name|indexingContext
range|:
name|nexusIndexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|nexusIndexer
operator|.
name|removeIndexingContext
argument_list|(
name|indexingContext
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumption
parameter_list|()
block|{
name|Path
name|localFile
init|=
name|repoLocation
operator|.
name|resolve
argument_list|(
literal|"org/apache/maven/plugins/maven-plugin-plugin/2.4.1/maven-metadata.xml"
argument_list|)
decl_stmt|;
name|ConsumerWantsFilePredicate
name|predicate
init|=
operator|new
name|ConsumerWantsFilePredicate
argument_list|()
decl_stmt|;
name|BaseFile
name|baseFile
init|=
operator|new
name|BaseFile
argument_list|(
name|repoLocation
operator|.
name|toFile
argument_list|()
argument_list|,
name|localFile
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
name|predicate
operator|.
name|setBasefile
argument_list|(
name|baseFile
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|predicate
operator|.
name|evaluate
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumptionOfOtherMetadata
parameter_list|()
block|{
name|Path
name|localFile
init|=
name|repoLocation
operator|.
name|resolve
argument_list|(
literal|"org/apache/maven/plugins/maven-plugin-plugin/2.4.1/maven-metadata-central.xml"
argument_list|)
decl_stmt|;
name|ConsumerWantsFilePredicate
name|predicate
init|=
operator|new
name|ConsumerWantsFilePredicate
argument_list|()
decl_stmt|;
name|BaseFile
name|baseFile
init|=
operator|new
name|BaseFile
argument_list|(
name|repoLocation
operator|.
name|toFile
argument_list|()
argument_list|,
name|localFile
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
name|predicate
operator|.
name|setBasefile
argument_list|(
name|baseFile
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|predicate
operator|.
name|evaluate
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"."
argument_list|)
return|;
block|}
block|}
end_class

end_unit

