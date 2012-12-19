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
name|stats
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|RepositorySessionFactory
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
name|core
operator|.
name|TransientRepository
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
name|NamespaceRegistry
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
name|SimpleCredentials
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Workspace
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
name|zip
operator|.
name|GZIPInputStream
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
name|ArchivaBlockJUnit4ClassRunner
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
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
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
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
decl_stmt|;
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
specifier|private
name|Session
name|session
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
name|File
name|confFile
init|=
operator|new
name|File
argument_list|(
literal|"src/test/repository.xml"
argument_list|)
decl_stmt|;
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
literal|"target/jcr"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|dir
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|confFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|TransientRepository
name|repository
init|=
operator|new
name|TransientRepository
argument_list|(
name|confFile
argument_list|,
name|dir
argument_list|)
decl_stmt|;
name|session
operator|=
name|repository
operator|.
name|login
argument_list|(
operator|new
name|SimpleCredentials
argument_list|(
literal|"username"
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: perhaps have an archiva-jcr-utils module shared by these plugins that does this and can contain
comment|//      structure information
name|Workspace
name|workspace
init|=
name|session
operator|.
name|getWorkspace
argument_list|()
decl_stmt|;
name|NamespaceRegistry
name|registry
init|=
name|workspace
operator|.
name|getNamespaceRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|registerNamespace
argument_list|(
literal|"archiva"
argument_list|,
literal|"http://archiva.apache.org/jcr/"
argument_list|)
expr_stmt|;
name|NodeTypeManager
name|nodeTypeManager
init|=
name|workspace
operator|.
name|getNodeTypeManager
argument_list|()
decl_stmt|;
name|registerMixinNodeType
argument_list|(
name|nodeTypeManager
argument_list|,
literal|"archiva:namespace"
argument_list|)
expr_stmt|;
name|registerMixinNodeType
argument_list|(
name|nodeTypeManager
argument_list|,
literal|"archiva:project"
argument_list|)
expr_stmt|;
name|registerMixinNodeType
argument_list|(
name|nodeTypeManager
argument_list|,
literal|"archiva:projectVersion"
argument_list|)
expr_stmt|;
name|registerMixinNodeType
argument_list|(
name|nodeTypeManager
argument_list|,
literal|"archiva:artifact"
argument_list|)
expr_stmt|;
name|registerMixinNodeType
argument_list|(
name|nodeTypeManager
argument_list|,
literal|"archiva:facet"
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
name|when
argument_list|(
name|metadataRepository
operator|.
name|canObtainAccess
argument_list|(
name|Session
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|metadataRepository
operator|.
name|obtainAccess
argument_list|(
name|Session
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|repositoryStatisticsManager
operator|=
operator|new
name|DefaultRepositoryStatisticsManager
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|logout
argument_list|()
expr_stmt|;
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
name|TEST_REPO
argument_list|)
expr_stmt|;
name|loadContentIntoRepo
argument_list|(
literal|"another-repo"
argument_list|)
expr_stmt|;
name|repositoryStatisticsManager
operator|.
name|addStatisticsAfterScan
argument_list|(
name|metadataRepository
argument_list|,
name|TEST_REPO
argument_list|,
name|startTime
argument_list|,
name|endTime
argument_list|,
name|TOTAL_FILE_COUNT
argument_list|,
name|NEW_FILE_COUNT
argument_list|)
expr_stmt|;
name|RepositoryStatistics
name|expectedStatistics
init|=
operator|new
name|RepositoryStatistics
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
name|verify
argument_list|(
name|metadataRepository
argument_list|)
operator|.
name|addMetadataFacet
argument_list|(
name|TEST_REPO
argument_list|,
name|expectedStatistics
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|loadContentIntoRepo
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|RepositoryException
throws|,
name|IOException
block|{
name|Node
name|n
init|=
name|JcrUtils
operator|.
name|getOrAddNode
argument_list|(
name|session
operator|.
name|getRootNode
argument_list|()
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
name|GZIPInputStream
name|inputStream
init|=
operator|new
name|GZIPInputStream
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifacts.xml.gz"
argument_list|)
argument_list|)
decl_stmt|;
name|session
operator|.
name|importXML
argument_list|(
name|n
operator|.
name|getPath
argument_list|()
argument_list|,
name|inputStream
argument_list|,
name|ImportUUIDBehavior
operator|.
name|IMPORT_UUID_CREATE_NEW
argument_list|)
expr_stmt|;
name|session
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

