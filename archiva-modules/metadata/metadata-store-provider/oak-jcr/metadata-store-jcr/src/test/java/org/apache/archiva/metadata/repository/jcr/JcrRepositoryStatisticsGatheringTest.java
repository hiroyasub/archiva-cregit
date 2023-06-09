begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|jcr
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|metadata
operator|.
name|model
operator|.
name|MetadataFacetFactory
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
name|AbstractMetadataRepositoryTest
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
name|DefaultMetadataResolver
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
name|MetadataRepositoryException
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
name|MetadataService
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
name|stats
operator|.
name|model
operator|.
name|DefaultRepositoryStatistics
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|commons
operator|.
name|JcrUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|oak
operator|.
name|segment
operator|.
name|file
operator|.
name|InvalidFileStoreVersionException
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
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
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
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|ImportUUIDBehavior
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|RepositoryException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|nodetype
operator|.
name|NodeTypeManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|nodetype
operator|.
name|NodeTypeTemplate
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
name|io
operator|.
name|InputStream
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
name|List
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
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
name|JcrRepositoryStatisticsGatheringTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JcrRepositoryStatisticsGatheringTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TOTAL_FILE_COUNT
init|=
literal|1000
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|NEW_FILE_COUNT
init|=
literal|500
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO
init|=
literal|"test-repo"
decl_stmt|;
specifier|static
name|JcrMetadataRepository
name|repository
decl_stmt|;
specifier|static
name|JcrRepositorySessionFactory
name|sessionFactory
decl_stmt|;
name|Session
name|jcrSession
decl_stmt|;
specifier|private
specifier|static
name|Repository
name|jcrRepository
decl_stmt|;
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|int
name|assertRetrySleepMs
init|=
literal|500
decl_stmt|;
specifier|private
name|int
name|assertMaxTries
init|=
literal|5
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setupSpec
parameter_list|()
throws|throws
name|IOException
throws|,
name|InvalidFileStoreVersionException
block|{
name|Path
name|directory
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-repositories"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|directory
argument_list|)
condition|)
block|{
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
name|directory
argument_list|)
expr_stmt|;
block|}
name|directory
operator|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/jcr"
argument_list|)
expr_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|directory
argument_list|)
condition|)
block|{
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
name|directory
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|MetadataFacetFactory
argument_list|>
name|factories
init|=
name|AbstractMetadataRepositoryTest
operator|.
name|createTestMetadataFacetFactories
argument_list|()
decl_stmt|;
name|MetadataService
name|metadataService
init|=
operator|new
name|MetadataService
argument_list|( )
decl_stmt|;
name|metadataService
operator|.
name|setMetadataFacetFactories
argument_list|(
name|factories
argument_list|)
expr_stmt|;
name|JcrRepositorySessionFactory
name|jcrSessionFactory
init|=
operator|new
name|JcrRepositorySessionFactory
argument_list|()
decl_stmt|;
name|jcrSessionFactory
operator|.
name|setMetadataResolver
argument_list|(
operator|new
name|DefaultMetadataResolver
argument_list|()
argument_list|)
expr_stmt|;
name|jcrSessionFactory
operator|.
name|setMetadataService
argument_list|(
name|metadataService
argument_list|)
expr_stmt|;
name|jcrSessionFactory
operator|.
name|open
argument_list|()
expr_stmt|;
name|sessionFactory
operator|=
name|jcrSessionFactory
expr_stmt|;
name|repository
operator|=
name|jcrSessionFactory
operator|.
name|getMetadataRepository
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopSpec
parameter_list|()
block|{
try|try
block|{
name|repository
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
name|sessionFactory
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/*      * Used by tryAssert to allow to throw exceptions in the lambda expression.      */
annotation|@
name|FunctionalInterface
specifier|protected
interface|interface
name|AssertFunction
block|{
name|void
name|accept
parameter_list|( )
throws|throws
name|Exception
function_decl|;
block|}
specifier|protected
name|void
name|tryAssert
parameter_list|(
name|AssertFunction
name|func
parameter_list|)
throws|throws
name|Exception
block|{
name|tryAssert
argument_list|(
name|func
argument_list|,
name|assertMaxTries
argument_list|,
name|assertRetrySleepMs
argument_list|)
expr_stmt|;
block|}
comment|/*      * Runs the assert method until the assert is successful or the number of retries      * is reached. This is needed because the JCR Oak index update is asynchronous, so updates      * may not be visible immediately after the modification.      */
specifier|private
name|void
name|tryAssert
parameter_list|(
name|AssertFunction
name|func
parameter_list|,
name|int
name|retries
parameter_list|,
name|int
name|sleepMillis
parameter_list|)
throws|throws
name|Exception
block|{
name|Throwable
name|t
init|=
literal|null
decl_stmt|;
name|int
name|retry
init|=
name|retries
decl_stmt|;
while|while
condition|(
name|retry
operator|--
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|func
operator|.
name|accept
argument_list|( )
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|Exception
decl||
name|AssertionError
name|e
parameter_list|)
block|{
name|t
operator|=
name|e
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|( )
operator|.
name|sleep
argument_list|(
name|sleepMillis
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Retrying assert {}: {}"
argument_list|,
name|retry
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|warn
argument_list|(
literal|"Retries: {}, Exception: {}"
argument_list|,
name|retry
argument_list|,
name|t
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
if|if
condition|(
name|retry
operator|<=
literal|0
operator|&&
name|t
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|t
throw|;
block|}
if|else if
condition|(
name|t
operator|instanceof
name|Exception
condition|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|t
throw|;
block|}
if|else if
condition|(
name|t
operator|instanceof
name|Error
condition|)
block|{
throw|throw
operator|(
name|Error
operator|)
name|t
throw|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|registerMixinNodeType
parameter_list|(
name|NodeTypeManager
name|nodeTypeManager
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|NodeTypeTemplate
name|nodeType
init|=
name|nodeTypeManager
operator|.
name|createNodeTypeTemplate
argument_list|()
decl_stmt|;
name|nodeType
operator|.
name|setMixin
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|nodeType
operator|.
name|setName
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|nodeTypeManager
operator|.
name|registerNodeType
argument_list|(
name|nodeType
argument_list|,
literal|false
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
if|if
condition|(
name|repository
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|repository
operator|.
name|close
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//
block|}
block|}
if|if
condition|(
name|sessionFactory
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|sessionFactory
operator|.
name|close
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//
block|}
block|}
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
name|testJcrStatisticsQuery
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|RepositorySession
name|repSession
init|=
name|sessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|Date
name|endTime
init|=
name|cal
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|HOUR
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Date
name|startTime
init|=
name|cal
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|loadContentIntoRepo
argument_list|(
name|repSession
argument_list|,
name|TEST_REPO
argument_list|)
expr_stmt|;
name|loadContentIntoRepo
argument_list|(
name|repSession
argument_list|,
literal|"another-repo"
argument_list|)
expr_stmt|;
name|DefaultRepositoryStatistics
name|testedStatistics
init|=
operator|new
name|DefaultRepositoryStatistics
argument_list|()
decl_stmt|;
name|testedStatistics
operator|.
name|setNewFileCount
argument_list|(
name|NEW_FILE_COUNT
argument_list|)
expr_stmt|;
name|testedStatistics
operator|.
name|setTotalFileCount
argument_list|(
name|TOTAL_FILE_COUNT
argument_list|)
expr_stmt|;
name|testedStatistics
operator|.
name|setScanStartTime
argument_list|(
name|startTime
argument_list|)
expr_stmt|;
name|testedStatistics
operator|.
name|setScanEndTime
argument_list|(
name|endTime
argument_list|)
expr_stmt|;
name|DefaultRepositoryStatistics
name|expectedStatistics
init|=
operator|new
name|DefaultRepositoryStatistics
argument_list|()
decl_stmt|;
name|expectedStatistics
operator|.
name|setNewFileCount
argument_list|(
name|NEW_FILE_COUNT
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalFileCount
argument_list|(
name|TOTAL_FILE_COUNT
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setScanEndTime
argument_list|(
name|endTime
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setScanStartTime
argument_list|(
name|startTime
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalArtifactFileSize
argument_list|(
literal|95954585
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalArtifactCount
argument_list|(
literal|269
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalGroupCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalProjectCount
argument_list|(
literal|43
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalCountForType
argument_list|(
literal|"zip"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalCountForType
argument_list|(
literal|"gz"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// FIXME: should be tar.gz
name|expectedStatistics
operator|.
name|setTotalCountForType
argument_list|(
literal|"java-source"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalCountForType
argument_list|(
literal|"jar"
argument_list|,
literal|108
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalCountForType
argument_list|(
literal|"xml"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalCountForType
argument_list|(
literal|"war"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setTotalCountForType
argument_list|(
literal|"pom"
argument_list|,
literal|144
argument_list|)
expr_stmt|;
name|expectedStatistics
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO
argument_list|)
expr_stmt|;
name|tryAssert
argument_list|(
parameter_list|()
lambda|->
block|{
name|repository
operator|.
name|populateStatistics
argument_list|(
name|repSession
argument_list|,
name|repository
argument_list|,
name|TEST_REPO
argument_list|,
name|testedStatistics
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"getTotalCountForType: {}"
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NEW_FILE_COUNT
argument_list|,
name|testedStatistics
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TOTAL_FILE_COUNT
argument_list|,
name|testedStatistics
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endTime
argument_list|,
name|testedStatistics
operator|.
name|getScanEndTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|startTime
argument_list|,
name|testedStatistics
operator|.
name|getScanStartTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|269
argument_list|,
name|testedStatistics
operator|.
name|getTotalArtifactCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|testedStatistics
operator|.
name|getTotalGroupCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|43
argument_list|,
name|testedStatistics
operator|.
name|getTotalProjectCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|(
literal|"zip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|(
literal|"gz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|(
literal|"java-source"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|108
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|(
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|(
literal|"xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|(
literal|"war"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|144
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|(
literal|"pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|testedStatistics
operator|.
name|getTotalCountForType
argument_list|(
literal|"java-source"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|95954585
argument_list|,
name|testedStatistics
operator|.
name|getTotalArtifactFileSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|loadContentIntoRepo
parameter_list|(
name|RepositorySession
name|repoSession
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|RepositoryException
throws|,
name|IOException
throws|,
name|MetadataRepositoryException
block|{
name|jcrSession
operator|=
operator|(
operator|(
name|JcrRepositorySession
operator|)
name|repoSession
operator|)
operator|.
name|getJcrSession
argument_list|()
expr_stmt|;
name|Node
name|n
init|=
name|JcrUtils
operator|.
name|getOrAddNode
argument_list|(
name|jcrSession
operator|.
name|getRootNode
argument_list|( )
argument_list|,
literal|"repositories"
argument_list|)
decl_stmt|;
name|n
operator|=
name|JcrUtils
operator|.
name|getOrAddNode
argument_list|(
name|n
argument_list|,
name|repoId
argument_list|)
expr_stmt|;
name|n
operator|=
name|JcrUtils
operator|.
name|getOrAddNode
argument_list|(
name|n
argument_list|,
literal|"content"
argument_list|)
expr_stmt|;
name|n
operator|=
name|JcrUtils
operator|.
name|getOrAddNode
argument_list|(
name|n
argument_list|,
literal|"org"
argument_list|)
expr_stmt|;
name|n
operator|=
name|JcrUtils
operator|.
name|getOrAddNode
argument_list|(
name|n
argument_list|,
literal|"apache"
argument_list|)
expr_stmt|;
name|InputStream
name|inputStream
init|=
name|getClass
argument_list|( )
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifacts.xml"
argument_list|)
decl_stmt|;
name|jcrSession
operator|.
name|importXML
argument_list|(
name|n
operator|.
name|getPath
argument_list|( )
argument_list|,
name|inputStream
argument_list|,
name|ImportUUIDBehavior
operator|.
name|IMPORT_UUID_CREATE_NEW
argument_list|)
expr_stmt|;
name|jcrSession
operator|.
name|save
argument_list|( )
expr_stmt|;
block|}
block|}
end_class

end_unit

