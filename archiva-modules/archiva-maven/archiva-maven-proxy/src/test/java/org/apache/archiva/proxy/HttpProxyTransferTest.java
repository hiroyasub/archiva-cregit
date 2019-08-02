begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
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
name|NetworkProxyConfiguration
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
name|ProxyConnectorConfiguration
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
name|RemoteRepositoryConfiguration
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
name|model
operator|.
name|ArtifactReference
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
name|policies
operator|.
name|CachedFailuresPolicy
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
name|policies
operator|.
name|ChecksumPolicy
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
name|policies
operator|.
name|PropagateErrorsDownloadPolicy
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
name|policies
operator|.
name|PropagateErrorsOnUpdateDownloadPolicy
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
name|policies
operator|.
name|ReleasesPolicy
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
name|policies
operator|.
name|SnapshotsPolicy
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
name|proxy
operator|.
name|model
operator|.
name|RepositoryProxyHandler
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
name|*
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
name|storage
operator|.
name|StorageAsset
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
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
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
name|Request
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
name|AbstractHandler
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * Integration test for connecting over a HTTP proxy.  *  *  */
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
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|HttpProxyTransferTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PROXY_ID
init|=
literal|"proxy"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MANAGED_ID
init|=
literal|"default-managed-repository"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROXIED_ID
init|=
literal|"proxied1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROXIED_BASEDIR
init|=
literal|"src/test/repositories/proxied1"
decl_stmt|;
specifier|private
name|RepositoryProxyHandler
name|proxyHandler
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|config
decl_stmt|;
specifier|private
name|ManagedRepositoryContent
name|managedDefaultRepository
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
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
specifier|private
name|Server
name|server
decl_stmt|;
specifier|protected
name|ManagedRepositoryContent
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|layout
parameter_list|)
throws|throws
name|Exception
block|{
name|ManagedRepository
name|repo
init|=
name|BasicManagedRepository
operator|.
name|newFilesystemInstance
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
name|path
argument_list|)
operator|.
name|resolve
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|repositoryRegistry
operator|.
name|putRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
return|return
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|id
argument_list|)
operator|.
name|getContent
argument_list|()
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|proxyHandler
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"repositoryProxyConnectors#test"
argument_list|,
name|RepositoryProxyHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|config
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"archivaConfiguration#mock"
argument_list|,
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// clear from previous tests - TODO the spring context should be initialised per test instead, or the config
comment|// made a complete mock
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// Setup source repository (using default layout)
name|String
name|repoPath
init|=
literal|"target/test-repository/managed/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
name|Path
name|destRepoDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repoPath
argument_list|)
decl_stmt|;
comment|// Cleanout destination dirs.
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|destRepoDir
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|destRepoDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Make the destination dir.
name|Files
operator|.
name|createDirectories
argument_list|(
name|destRepoDir
argument_list|)
expr_stmt|;
name|Handler
name|handler
init|=
operator|new
name|AbstractHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|String
name|s
parameter_list|,
name|Request
name|request
parameter_list|,
name|HttpServletRequest
name|httpServletRequest
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|response
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|response
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|print
argument_list|(
literal|"get-default-layout-1.0.jar\n\n"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|request
operator|.
name|getHeader
argument_list|(
literal|"Proxy-Connection"
argument_list|)
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Request
operator|)
name|request
operator|)
operator|.
name|setHandled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handle
parameter_list|(
name|String
name|target
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|int
name|dispatch
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|response
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|response
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|print
argument_list|(
literal|"get-default-layout-1.0.jar\n\n"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|request
operator|.
name|getHeader
argument_list|(
literal|"Proxy-Connection"
argument_list|)
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Request
operator|)
name|request
operator|)
operator|.
name|setHandled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|server
operator|=
operator|new
name|Server
argument_list|(  )
expr_stmt|;
name|ServerConnector
name|serverConnector
init|=
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|,
operator|new
name|HttpConnectionFactory
argument_list|()
argument_list|)
decl_stmt|;
name|server
operator|.
name|addConnector
argument_list|(
name|serverConnector
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|int
name|port
init|=
name|serverConnector
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
name|NetworkProxyConfiguration
name|proxyConfig
init|=
operator|new
name|NetworkProxyConfiguration
argument_list|()
decl_stmt|;
name|proxyConfig
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|proxyConfig
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|proxyConfig
operator|.
name|setProtocol
argument_list|(
literal|"http"
argument_list|)
expr_stmt|;
name|proxyConfig
operator|.
name|setId
argument_list|(
name|PROXY_ID
argument_list|)
expr_stmt|;
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addNetworkProxy
argument_list|(
name|proxyConfig
argument_list|)
expr_stmt|;
operator|(
operator|(
name|MockConfiguration
operator|)
name|config
operator|)
operator|.
name|triggerChange
argument_list|(
literal|"networkProxies.networkProxy(0).host"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
comment|// Setup target (proxied to) repository.
name|RemoteRepositoryConfiguration
name|repoConfig
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repoConfig
operator|.
name|setId
argument_list|(
name|PROXIED_ID
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setName
argument_list|(
literal|"Proxied Repository 1"
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setUrl
argument_list|(
literal|"http://www.example.com/"
argument_list|)
expr_stmt|;
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addRemoteRepository
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|reload
argument_list|()
expr_stmt|;
name|managedDefaultRepository
operator|=
name|createRepository
argument_list|(
name|MANAGED_ID
argument_list|,
literal|"Default Managed Repository"
argument_list|,
name|repoPath
argument_list|,
literal|"default"
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
name|testGetOverHttpProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|Assertions
operator|.
name|assertThat
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyHost"
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPort"
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout/1.0/get-default-layout-1.0.jar"
decl_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|addConnector
argument_list|()
expr_stmt|;
name|Path
name|expectedFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepoRoot
argument_list|()
argument_list|)
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|ArtifactReference
name|artifact
init|=
name|managedDefaultRepository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|Path
name|sourceFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|PROXIED_BASEDIR
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected File should not be null."
argument_list|,
name|expectedFile
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Actual File should not be null."
argument_list|,
name|downloadedFile
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check actual file exists."
argument_list|,
name|Files
operator|.
name|exists
argument_list|(
name|downloadedFile
operator|.
name|getFilePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check filename path is appropriate."
argument_list|,
name|Files
operator|.
name|isSameFile
argument_list|(
name|expectedFile
argument_list|,
name|downloadedFile
operator|.
name|getFilePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file path matches."
argument_list|,
name|Files
operator|.
name|isSameFile
argument_list|(
name|expectedFile
argument_list|,
name|downloadedFile
operator|.
name|getFilePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|expectedContents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|sourceFile
operator|.
name|toFile
argument_list|()
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|actualContents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|downloadedFile
operator|.
name|getFilePath
argument_list|()
operator|.
name|toFile
argument_list|()
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file contents."
argument_list|,
name|expectedContents
argument_list|,
name|actualContents
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyHost"
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPort"
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|addConnector
parameter_list|()
block|{
name|ProxyConnectorConfiguration
name|connectorConfig
init|=
operator|new
name|ProxyConnectorConfiguration
argument_list|()
decl_stmt|;
name|connectorConfig
operator|.
name|setProxyId
argument_list|(
name|PROXY_ID
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|setSourceRepoId
argument_list|(
name|MANAGED_ID
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|setTargetRepoId
argument_list|(
name|PROXIED_ID
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_CHECKSUM
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_RELEASES
argument_list|,
name|ReleasesPolicy
operator|.
name|ONCE
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_SNAPSHOTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_CACHE_FAILURES
argument_list|,
name|CachedFailuresPolicy
operator|.
name|NO
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_PROPAGATE_ERRORS
argument_list|,
name|PropagateErrorsDownloadPolicy
operator|.
name|QUEUE
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_PROPAGATE_ERRORS_ON_UPDATE
argument_list|,
name|PropagateErrorsOnUpdateDownloadPolicy
operator|.
name|NOT_PRESENT
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addProxyConnector
argument_list|(
name|connectorConfig
argument_list|)
expr_stmt|;
comment|// Proper Triggering ...
name|String
name|prefix
init|=
literal|"proxyConnectors.proxyConnector("
operator|+
name|count
operator|+
literal|")"
decl_stmt|;
operator|(
operator|(
name|MockConfiguration
operator|)
name|config
operator|)
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".sourceRepoId"
argument_list|,
name|connectorConfig
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

