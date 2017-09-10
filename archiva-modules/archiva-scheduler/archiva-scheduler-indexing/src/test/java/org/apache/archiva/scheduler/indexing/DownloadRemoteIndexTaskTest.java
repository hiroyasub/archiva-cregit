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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|RemoteRepository
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
name|remote
operator|.
name|RemoteRepositoryAdmin
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
name|utils
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
name|expr
operator|.
name|StringSearchExpression
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
name|index_shaded
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
name|maven
operator|.
name|index_shaded
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|HttpConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|ServerConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|DefaultServlet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletContextHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletHolder
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
name|scheduling
operator|.
name|concurrent
operator|.
name|ThreadPoolTaskScheduler
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
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
name|DownloadRemoteIndexTaskTest
block|{
specifier|private
name|Server
name|server
decl_stmt|;
specifier|private
name|ServerConnector
name|serverConnector
decl_stmt|;
specifier|private
name|int
name|port
decl_stmt|;
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
name|DefaultDownloadRemoteIndexScheduler
name|downloadRemoteIndexScheduler
decl_stmt|;
annotation|@
name|Inject
name|NexusIndexer
name|nexusIndexer
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|=
operator|new
name|Server
argument_list|( )
expr_stmt|;
name|serverConnector
operator|=
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|,
operator|new
name|HttpConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|addConnector
argument_list|(
name|serverConnector
argument_list|)
expr_stmt|;
name|createContext
argument_list|(
name|server
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|serverConnector
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"start server on port {}"
argument_list|,
name|this
operator|.
name|port
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createContext
parameter_list|(
name|Server
name|server
parameter_list|,
name|Path
name|repositoryDirectory
parameter_list|)
throws|throws
name|IOException
block|{
name|ServletContextHandler
name|context
init|=
operator|new
name|ServletContextHandler
argument_list|()
decl_stmt|;
name|context
operator|.
name|setResourceBase
argument_list|(
name|repositoryDirectory
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|ServletHolder
name|sh
init|=
operator|new
name|ServletHolder
argument_list|(
name|DefaultServlet
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|addServlet
argument_list|(
name|sh
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|context
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
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|downloadAndMergeRemoteIndexInEmptyIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepository
name|remoteRepository
init|=
name|getRemoteRepository
argument_list|()
decl_stmt|;
name|remoteRepositoryAdmin
operator|.
name|addRemoteRepository
argument_list|(
name|remoteRepository
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|downloadRemoteIndexScheduler
operator|.
name|startup
argument_list|()
expr_stmt|;
name|downloadRemoteIndexScheduler
operator|.
name|scheduleDownloadRemote
argument_list|(
literal|"test-repo"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|ThreadPoolTaskScheduler
operator|)
name|downloadRemoteIndexScheduler
operator|.
name|getTaskScheduler
argument_list|()
operator|)
operator|.
name|getScheduledExecutor
argument_list|()
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|remoteRepositoryAdmin
operator|.
name|deleteRemoteRepository
argument_list|(
literal|"test-repo"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// search
name|BooleanQuery
name|iQuery
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|iQuery
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
name|StringSearchExpression
argument_list|(
literal|"commons-logging"
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|FlatSearchRequest
name|rq
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|iQuery
argument_list|)
decl_stmt|;
name|rq
operator|.
name|setContexts
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|nexusIndexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|get
argument_list|(
literal|"remote-"
operator|+
name|getRemoteRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|FlatSearchResponse
name|response
init|=
name|nexusIndexer
operator|.
name|searchFlat
argument_list|(
name|rq
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"returned hit count:{}"
argument_list|,
name|response
operator|.
name|getReturnedHitsCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getReturnedHitsCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|8
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|RemoteRepository
name|getRemoteRepository
parameter_list|()
throws|throws
name|IOException
block|{
name|RemoteRepository
name|remoteRepository
init|=
operator|new
name|RemoteRepository
argument_list|()
decl_stmt|;
name|Path
name|indexDirectory
init|=
name|Paths
operator|.
name|get
argument_list|(
name|FileUtils
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target/index/test-"
operator|+
name|Long
operator|.
name|toString
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|indexDirectory
argument_list|)
expr_stmt|;
name|indexDirectory
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|remoteRepository
operator|.
name|setName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setIndexDirectory
argument_list|(
name|indexDirectory
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setDownloadRemoteIndex
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|port
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setRemoteIndexUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/index-updates/"
argument_list|)
expr_stmt|;
return|return
name|remoteRepository
return|;
block|}
block|}
end_class

end_unit

