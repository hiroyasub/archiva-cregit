begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|reports
operator|.
name|consumers
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|consumers
operator|.
name|ConsumerException
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
name|metadata
operator|.
name|model
operator|.
name|ArtifactMetadata
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
name|metadata
operator|.
name|model
operator|.
name|MetadataFacet
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
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
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|RepositoryPathTranslator
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
name|metadata
operator|.
name|model
operator|.
name|facets
operator|.
name|RepositoryProblemFacet
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
name|BasicManagedRepository
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
name|mockito
operator|.
name|ArgumentCaptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Matchers
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
name|annotation
operator|.
name|DirtiesContext
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
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|NoSuchFileException
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
name|Arrays
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
name|Locale
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"ThrowableInstanceNeverThrown"
block|}
argument_list|)
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
annotation|@
name|DirtiesContext
argument_list|(
name|classMode
operator|=
name|DirtiesContext
operator|.
name|ClassMode
operator|.
name|AFTER_EACH_TEST_METHOD
argument_list|)
specifier|public
class|class
name|DuplicateArtifactsConsumerTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"knownRepositoryContentConsumer#duplicate-artifacts"
argument_list|)
specifier|private
name|DuplicateArtifactsConsumer
name|consumer
decl_stmt|;
specifier|private
name|BasicManagedRepository
name|config
decl_stmt|;
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO
init|=
literal|"test-repo"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CHECKSUM
init|=
literal|"edf5938e646956f445c6ecb719d44579cdeed974"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_PROJECT
init|=
literal|"test-artifact"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_NAMESPACE
init|=
literal|"com.example.test"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_FILE
init|=
literal|"com/example/test/test-artifact/1.0-SNAPSHOT/test-artifact-1.0-20100308.230825-1.jar"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VERSION
init|=
literal|"1.0-20100308.230825-1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ArtifactMetadata
name|TEST_METADATA
init|=
name|createMetadata
argument_list|(
name|TEST_VERSION
argument_list|)
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryPathTranslator#maven2"
argument_list|)
specifier|private
name|RepositoryPathTranslator
name|pathTranslator
decl_stmt|;
annotation|@
name|Inject
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
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
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|config
operator|=
operator|new
name|BasicManagedRepository
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_REPO
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
literal|"target"
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setLocation
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-repository"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|metadataRepository
operator|=
name|mock
argument_list|(
name|MetadataRepository
operator|.
name|class
argument_list|)
expr_stmt|;
name|RepositorySession
name|session
init|=
name|mock
argument_list|(
name|RepositorySession
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|session
operator|.
name|getRepository
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
name|RepositorySessionFactory
name|factory
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|RepositorySessionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//(RepositorySessionFactory) lookup( RepositorySessionFactory.class );
name|when
argument_list|(
name|factory
operator|.
name|createSession
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|pathTranslator
operator|.
name|getArtifactForPath
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_FILE
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|TEST_METADATA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumerArtifactNotDuplicated
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifactsByChecksum
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_CHECKSUM
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_METADATA
argument_list|)
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|(
name|config
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|TEST_FILE
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|addMetadataFacet
argument_list|(
name|eq
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|,
name|Matchers
operator|.
expr|<
name|MetadataFacet
operator|>
name|anyObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: Doesn't currently work
comment|//    public void testConsumerArtifactNotDuplicatedForOtherSnapshots()
comment|//        throws ConsumerException
comment|//    {
comment|//        when( metadataRepository.getArtifactsByChecksum( TEST_REPO, TEST_CHECKSUM ) ).thenReturn( Arrays.asList(
comment|//            TEST_METADATA, createMetadata( "1.0-20100309.002023-2" ) ) );
comment|//
comment|//        consumer.beginScan( config, new Date() );
comment|//        consumer.processFile( TEST_FILE );
comment|//        consumer.completeScan();
comment|//
comment|//        verify( metadataRepository, never() ).addMetadataFacet( eq( TEST_REPO ), Matchers.<MetadataFacet>anyObject() );
comment|//    }
annotation|@
name|Test
specifier|public
name|void
name|testConsumerArtifactDuplicated
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifactsByChecksum
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_CHECKSUM
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_METADATA
argument_list|,
name|createMetadata
argument_list|(
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|(
name|config
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|TEST_FILE
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|RepositoryProblemFacet
argument_list|>
name|argument
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|RepositoryProblemFacet
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|)
operator|.
name|addMetadataFacet
argument_list|(
name|eq
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|,
name|argument
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryProblemFacet
name|problem
init|=
name|argument
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertProblem
argument_list|(
name|problem
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumerArtifactDuplicatedButSelfNotInMetadataRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifactsByChecksum
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_CHECKSUM
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|createMetadata
argument_list|(
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|(
name|config
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|TEST_FILE
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|RepositoryProblemFacet
argument_list|>
name|argument
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|RepositoryProblemFacet
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|)
operator|.
name|addMetadataFacet
argument_list|(
name|eq
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|,
name|argument
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryProblemFacet
name|problem
init|=
name|argument
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertProblem
argument_list|(
name|problem
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumerArtifactFileNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|consumer
operator|.
name|beginScan
argument_list|(
name|config
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|consumer
operator|.
name|processFile
argument_list|(
literal|"com/example/test/test-artifact/2.0/test-artifact-2.0.jar"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed to find file"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConsumerException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|NoSuchFileException
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
block|}
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|addMetadataFacet
argument_list|(
name|eq
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|,
name|Matchers
operator|.
expr|<
name|MetadataFacet
operator|>
name|anyObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumerArtifactNotAnArtifactPathNoResults
parameter_list|()
throws|throws
name|Exception
block|{
name|consumer
operator|.
name|beginScan
argument_list|(
name|config
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
comment|// No exception unnecessarily for something we can't report on
name|consumer
operator|.
name|processFile
argument_list|(
literal|"com/example/invalid-artifact.txt"
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|addMetadataFacet
argument_list|(
name|eq
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|,
name|Matchers
operator|.
expr|<
name|MetadataFacet
operator|>
name|anyObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumerArtifactNotAnArtifactPathResults
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifactsByChecksum
argument_list|(
name|eq
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|,
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_METADATA
argument_list|,
name|createMetadata
argument_list|(
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// override, this feels a little overspecified though
name|when
argument_list|(
name|pathTranslator
operator|.
name|getArtifactForPath
argument_list|(
name|TEST_REPO
argument_list|,
literal|"com/example/invalid-artifact.txt"
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|(
name|config
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
comment|// No exception unnecessarily for something we can't report on
name|consumer
operator|.
name|processFile
argument_list|(
literal|"com/example/invalid-artifact.txt"
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|addMetadataFacet
argument_list|(
name|eq
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|,
name|Matchers
operator|.
expr|<
name|MetadataFacet
operator|>
name|anyObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|assertProblem
parameter_list|(
name|RepositoryProblemFacet
name|problem
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|TEST_REPO
argument_list|,
name|problem
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_NAMESPACE
argument_list|,
name|problem
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_PROJECT
argument_list|,
name|problem
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_VERSION
argument_list|,
name|problem
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_PROJECT
operator|+
literal|"-"
operator|+
name|TEST_VERSION
operator|+
literal|".jar"
argument_list|,
name|problem
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|problem
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"duplicate-artifact"
argument_list|,
name|problem
operator|.
name|getProblem
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|ArtifactMetadata
name|createMetadata
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|ArtifactMetadata
name|artifact
init|=
operator|new
name|ArtifactMetadata
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setId
argument_list|(
name|TEST_PROJECT
operator|+
literal|"-"
operator|+
name|version
operator|+
literal|".jar"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setNamespace
argument_list|(
name|TEST_NAMESPACE
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setProject
argument_list|(
name|TEST_PROJECT
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setProjectVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

