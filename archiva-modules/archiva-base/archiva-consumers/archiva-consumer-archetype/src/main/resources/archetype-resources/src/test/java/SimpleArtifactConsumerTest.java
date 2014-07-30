begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|$package
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|util
operator|.
name|Date
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

begin_comment
comment|/**  *<code>SimpleArtifactConsumerTest</code>  */
end_comment

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
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|SimpleArtifactConsumerTest
block|{
annotation|@
name|Inject
specifier|private
name|SimpleArtifactConsumer
name|consumer
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
specifier|private
name|ManagedRepository
name|testRepository
decl_stmt|;
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SimpleArtifactConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|MetadataRepository
name|metadataRepository
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
name|setUpMockRepository
argument_list|()
expr_stmt|;
name|RepositorySession
name|repositorySession
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
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|repositorySession
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
name|repositorySession
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
block|}
specifier|private
name|void
name|setUpMockRepository
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test-consumer-repo"
argument_list|)
decl_stmt|;
name|repoDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|repoDir
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|testRepository
operator|=
operator|new
name|ManagedRepository
argument_list|()
expr_stmt|;
name|testRepository
operator|.
name|setName
argument_list|(
literal|"Test-Consumer-Repository"
argument_list|)
expr_stmt|;
name|testRepository
operator|.
name|setId
argument_list|(
literal|"test-consumer-repository"
argument_list|)
expr_stmt|;
name|testRepository
operator|.
name|setLocation
argument_list|(
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|testRepository
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testRepository
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBeginScan
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Beginning scan of repository [test-consumer-repository]"
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|(
name|testRepository
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessFile
parameter_list|()
throws|throws
name|Exception
block|{
name|consumer
operator|.
name|beginScan
argument_list|(
name|testRepository
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
literal|"org/simple/test/testartifact/testartifact/1.0/testartifact-1.0.pom"
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
literal|"org/simple/test/testartifact/testartifact/1.1/testartifact-1.1.pom"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|)
operator|.
name|getArtifacts
argument_list|(
name|testRepository
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.simple.test.testartifact"
argument_list|,
literal|"testartifact"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|)
operator|.
name|getArtifacts
argument_list|(
name|testRepository
operator|.
name|getId
argument_list|()
argument_list|,
literal|"org.simple.test.testartifact"
argument_list|,
literal|"testartifact"
argument_list|,
literal|"1.1"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

