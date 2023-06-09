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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|RemoteRepositoriesService
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
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|Handler
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
name|server
operator|.
name|handler
operator|.
name|DefaultHandler
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
name|handler
operator|.
name|HandlerList
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
name|handler
operator|.
name|ResourceHandler
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
name|util
operator|.
name|Locale
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
specifier|public
class|class
name|RemoteRepositoryConnectivityCheckTest
extends|extends
name|AbstractDownloadTest
block|{
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
literal|"archiva-common-web_appsrv6_"
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
name|Test
specifier|public
name|void
name|checkRemoteConnectivity
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|Long
operator|.
name|toString
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|Path
name|srcRep
init|=
name|getProjectDirectory
argument_list|( )
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/test-repo"
argument_list|)
decl_stmt|;
name|Path
name|testRep
init|=
name|getBasedir
argument_list|( )
operator|.
name|resolve
argument_list|(
literal|"target"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"test-repo-"
operator|+
name|id
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|srcRep
operator|.
name|toFile
argument_list|( )
argument_list|,
name|testRep
operator|.
name|toFile
argument_list|( )
argument_list|)
expr_stmt|;
name|createdPaths
operator|.
name|add
argument_list|(
name|testRep
argument_list|)
expr_stmt|;
name|Server
name|repoServer
init|=
name|buildStaticServer
argument_list|(
name|testRep
argument_list|)
decl_stmt|;
name|ServerConnector
name|serverConnector
init|=
operator|new
name|ServerConnector
argument_list|(
name|repoServer
argument_list|,
operator|new
name|HttpConnectionFactory
argument_list|()
argument_list|)
decl_stmt|;
name|repoServer
operator|.
name|addConnector
argument_list|(
name|serverConnector
argument_list|)
expr_stmt|;
name|repoServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|RemoteRepositoriesService
name|service
init|=
name|getRemoteRepositoriesService
argument_list|()
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|repoServerPort
init|=
name|serverConnector
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
name|RemoteRepository
name|repo
init|=
name|getRemoteRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|repoServerPort
argument_list|)
expr_stmt|;
name|service
operator|.
name|addRemoteRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|service
operator|.
name|checkRemoteConnectivity
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isSuccess
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|deleteRemoteRepository
argument_list|(
literal|"id-new"
argument_list|)
expr_stmt|;
name|repoServer
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
name|checkRemoteConnectivityEmptyRemote
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|tmpDir
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|Server
name|repoServer
init|=
name|buildStaticServer
argument_list|(
name|tmpDir
argument_list|)
decl_stmt|;
name|ServerConnector
name|serverConnector
init|=
operator|new
name|ServerConnector
argument_list|(
name|repoServer
argument_list|,
operator|new
name|HttpConnectionFactory
argument_list|()
argument_list|)
decl_stmt|;
name|repoServer
operator|.
name|addConnector
argument_list|(
name|serverConnector
argument_list|)
expr_stmt|;
name|repoServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|RemoteRepositoriesService
name|service
init|=
name|getRemoteRepositoriesService
argument_list|()
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|repoServerPort
init|=
name|serverConnector
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
name|RemoteRepository
name|repo
init|=
name|getRemoteRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|repoServerPort
argument_list|)
expr_stmt|;
name|service
operator|.
name|addRemoteRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|service
operator|.
name|checkRemoteConnectivity
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isSuccess
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|deleteRemoteRepository
argument_list|(
literal|"id-new"
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
name|deleteQuietly
argument_list|(
name|tmpDir
argument_list|)
expr_stmt|;
name|repoServer
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
name|checkRemoteConnectivityFail
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepositoriesService
name|service
init|=
name|getRemoteRepositoriesService
argument_list|()
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
try|try
block|{
name|RemoteRepository
name|repo
init|=
name|getRemoteRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setUrl
argument_list|(
literal|"http://localhost:8956"
argument_list|)
expr_stmt|;
name|service
operator|.
name|addRemoteRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|service
operator|.
name|checkRemoteConnectivity
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isSuccess
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|deleteRemoteRepository
argument_list|(
literal|"id-new"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Server
name|buildStaticServer
parameter_list|(
name|Path
name|path
parameter_list|)
block|{
name|Server
name|repoServer
init|=
operator|new
name|Server
argument_list|(  )
decl_stmt|;
name|ResourceHandler
name|resourceHandler
init|=
operator|new
name|ResourceHandler
argument_list|()
decl_stmt|;
name|resourceHandler
operator|.
name|setDirectoriesListed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resourceHandler
operator|.
name|setWelcomeFiles
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"index.html"
block|}
argument_list|)
expr_stmt|;
name|resourceHandler
operator|.
name|setResourceBase
argument_list|(
name|path
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|HandlerList
name|handlers
init|=
operator|new
name|HandlerList
argument_list|()
decl_stmt|;
name|handlers
operator|.
name|setHandlers
argument_list|(
operator|new
name|Handler
index|[]
block|{
name|resourceHandler
block|,
operator|new
name|DefaultHandler
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|repoServer
operator|.
name|setHandler
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
return|return
name|repoServer
return|;
block|}
name|RemoteRepository
name|getRemoteRepository
parameter_list|()
block|{
return|return
operator|new
name|RemoteRepository
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|( )
argument_list|,
literal|"id-new"
argument_list|,
literal|"new one"
argument_list|,
literal|"http://foo.com"
argument_list|,
literal|"default"
argument_list|,
literal|"foo"
argument_list|,
literal|"foopassword"
argument_list|,
literal|120
argument_list|,
literal|"cool repo"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

