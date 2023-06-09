begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|remotedownload
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|redback
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|RoleManagementService
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
name|security
operator|.
name|common
operator|.
name|ArchivaRoleConstants
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
name|commons
operator|.
name|lang3
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
name|wagon
operator|.
name|providers
operator|.
name|http
operator|.
name|HttpWagon
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
name|wagon
operator|.
name|repository
operator|.
name|Repository
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
name|AfterClass
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
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|charset
operator|.
name|Charset
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
name|List
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
name|atomic
operator|.
name|AtomicReference
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
name|ZipEntry
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
name|ZipFile
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

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
name|DownloadArtifactsTest
extends|extends
name|AbstractDownloadTest
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DownloadArtifactsTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|Server
name|redirectServer
init|=
literal|null
decl_stmt|;
specifier|public
name|int
name|redirectPort
decl_stmt|;
specifier|public
name|Server
name|repoServer
init|=
literal|null
decl_stmt|;
specifier|public
name|int
name|repoServerPort
decl_stmt|;
specifier|private
specifier|static
name|Path
name|appServerBase
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setAppServerBase
parameter_list|()
throws|throws
name|IOException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Setting appserver base"
argument_list|)
expr_stmt|;
name|previousAppServerBase
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"appserver.base"
argument_list|)
expr_stmt|;
name|appServerBase
operator|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"archiva-common-web_appsrv2_"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|( )
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"appserver.base"
argument_list|,
name|appServerBase
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|resetAppServerBase
parameter_list|()
block|{
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|appServerBase
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|appServerBase
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|setProperty
argument_list|(
literal|"appserver.base"
argument_list|,
name|previousAppServerBase
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getSpringConfigLocation
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"AppserverBase: "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"appserver.base"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|"classpath*:META-INF/spring-context.xml classpath*:spring-context-test-common.xml classpath*:spring-context-artifacts-download.xml"
return|;
block|}
annotation|@
name|Override
annotation|@
name|Before
specifier|public
name|void
name|startServer
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|startServer
argument_list|()
expr_stmt|;
comment|// repo handler
name|this
operator|.
name|repoServer
operator|=
operator|new
name|Server
argument_list|(  )
expr_stmt|;
name|ServerConnector
name|repoServerConnector
init|=
operator|new
name|ServerConnector
argument_list|(
name|this
operator|.
name|repoServer
argument_list|,
operator|new
name|HttpConnectionFactory
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|repoServer
operator|.
name|addConnector
argument_list|(
name|repoServerConnector
argument_list|)
expr_stmt|;
name|ServletHolder
name|shRepo
init|=
operator|new
name|ServletHolder
argument_list|(
name|RepoServlet
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServletContextHandler
name|contextRepo
init|=
operator|new
name|ServletContextHandler
argument_list|()
decl_stmt|;
name|contextRepo
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|contextRepo
operator|.
name|addServlet
argument_list|(
name|shRepo
argument_list|,
literal|"/*"
argument_list|)
expr_stmt|;
name|repoServer
operator|.
name|setHandler
argument_list|(
name|contextRepo
argument_list|)
expr_stmt|;
name|repoServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|this
operator|.
name|repoServerPort
operator|=
name|repoServerConnector
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
comment|//redirect handler
name|this
operator|.
name|redirectServer
operator|=
operator|new
name|Server
argument_list|( )
expr_stmt|;
name|ServerConnector
name|redirectServerConnector
init|=
operator|new
name|ServerConnector
argument_list|(
name|this
operator|.
name|redirectServer
argument_list|,
operator|new
name|HttpConnectionFactory
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|redirectServer
operator|.
name|addConnector
argument_list|(
name|redirectServerConnector
argument_list|)
expr_stmt|;
name|ServletHolder
name|shRedirect
init|=
operator|new
name|ServletHolder
argument_list|(
name|RedirectServlet
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServletContextHandler
name|contextRedirect
init|=
operator|new
name|ServletContextHandler
argument_list|()
decl_stmt|;
name|contextRedirect
operator|.
name|setAttribute
argument_list|(
literal|"redirectToPort"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|this
operator|.
name|repoServerPort
argument_list|)
argument_list|)
expr_stmt|;
name|contextRedirect
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|contextRedirect
operator|.
name|addServlet
argument_list|(
name|shRedirect
argument_list|,
literal|"/*"
argument_list|)
expr_stmt|;
name|redirectServer
operator|.
name|setHandler
argument_list|(
name|contextRedirect
argument_list|)
expr_stmt|;
name|redirectServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|this
operator|.
name|redirectPort
operator|=
name|redirectServerConnector
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"redirect server port {}"
argument_list|,
name|redirectPort
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|redirectServer
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|redirectServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|downloadWithRemoteRedirect
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepository
name|remoteRepository
init|=
name|getRemoteRepositoriesService
argument_list|()
operator|.
name|getRemoteRepository
argument_list|(
literal|"central"
argument_list|)
decl_stmt|;
name|remoteRepository
operator|.
name|setUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|redirectPort
argument_list|)
expr_stmt|;
name|getRemoteRepositoriesService
argument_list|()
operator|.
name|updateRemoteRepository
argument_list|(
name|remoteRepository
argument_list|)
expr_stmt|;
name|RoleManagementService
name|roleManagementService
init|=
name|getRoleManagementService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|roleManagementService
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
literal|"internal"
argument_list|)
condition|)
block|{
name|roleManagementService
operator|.
name|createTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
block|}
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|createGuestUser
argument_list|()
expr_stmt|;
name|roleManagementService
operator|.
name|assignRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_GUEST
argument_list|,
literal|"guest"
argument_list|)
expr_stmt|;
name|roleManagementService
operator|.
name|assignTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
literal|"internal"
argument_list|,
literal|"guest"
argument_list|)
expr_stmt|;
name|getUserService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|removeFromCache
argument_list|(
literal|"guest"
argument_list|)
expr_stmt|;
name|Path
name|file
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/junit-4.9.jar"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|HttpWagon
name|httpWagon
init|=
operator|new
name|HttpWagon
argument_list|()
decl_stmt|;
name|httpWagon
operator|.
name|connect
argument_list|(
operator|new
name|Repository
argument_list|(
literal|"foo"
argument_list|,
literal|"http://localhost:"
operator|+
name|port
argument_list|)
argument_list|)
expr_stmt|;
name|httpWagon
operator|.
name|get
argument_list|(
literal|"repository/internal/junit/junit/4.9/junit-4.9.jar"
argument_list|,
name|file
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
name|ZipFile
name|zipFile
init|=
operator|new
name|ZipFile
argument_list|(
name|file
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|entries
init|=
name|getZipEntriesNames
argument_list|(
name|zipFile
argument_list|)
decl_stmt|;
name|ZipEntry
name|zipEntry
init|=
name|zipFile
operator|.
name|getEntry
argument_list|(
literal|"org/junit/runners/JUnit4.class"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"cannot find zipEntry org/junit/runners/JUnit4.class, entries: "
operator|+
name|entries
operator|+
literal|", content is: "
operator|+
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|file
operator|.
name|toFile
argument_list|()
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|,
name|zipEntry
argument_list|)
expr_stmt|;
name|zipFile
operator|.
name|close
argument_list|()
expr_stmt|;
name|file
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|RedirectServlet
extends|extends
name|HttpServlet
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
operator|.
name|info
argument_list|(
literal|"redirect servlet receive: {}"
argument_list|,
name|req
operator|.
name|getRequestURI
argument_list|()
argument_list|)
expr_stmt|;
name|resp
operator|.
name|setStatus
argument_list|(
literal|302
argument_list|)
expr_stmt|;
name|resp
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n"
operator|+
literal|"<html><head>\n"
operator|+
literal|"<title>302 Found</title>\n"
operator|+
literal|"</head><body>\n"
operator|+
literal|"<h1>Found</h1>\n"
operator|+
literal|"<p>The document has moved<a href=\"https://repo.maven.apache.org/maven2/junit/junit/4.9/junit-4.9.jar\">here</a>.</p>\n"
operator|+
literal|"</body></html>\n"
operator|+
literal|"<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n"
operator|+
literal|"<html><head>\n"
argument_list|)
expr_stmt|;
name|resp
operator|.
name|sendRedirect
argument_list|(
literal|"http://localhost:"
operator|+
name|getServletContext
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"redirectToPort"
argument_list|)
operator|+
literal|"/maven2/"
operator|+
name|req
operator|.
name|getRequestURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|RepoServlet
extends|extends
name|HttpServlet
block|{
specifier|private
name|AtomicReference
argument_list|<
name|Path
argument_list|>
name|projectDir
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|protected
name|Path
name|getProjectDirectory
parameter_list|()
block|{
if|if
condition|(
name|projectDir
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|propVal
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"mvn.project.base.dir"
argument_list|)
decl_stmt|;
name|Path
name|newVal
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|propVal
argument_list|)
condition|)
block|{
name|newVal
operator|=
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|newVal
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|propVal
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
expr_stmt|;
block|}
name|projectDir
operator|.
name|compareAndSet
argument_list|(
literal|null
argument_list|,
name|newVal
argument_list|)
expr_stmt|;
block|}
return|return
name|projectDir
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|Path
name|jar
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/junit-4.9.jar"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|jar
argument_list|,
name|resp
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

