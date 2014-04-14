begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|Files
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
name|handler
operator|.
name|ContextHandlerCollection
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
name|File
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

begin_comment
comment|/**  * AbstractRepositoryServletProxiedTestCase  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryServletProxiedTestCase
extends|extends
name|AbstractRepositoryServletTestCase
block|{
class|class
name|RemoteRepoInfo
block|{
specifier|public
name|String
name|id
decl_stmt|;
specifier|public
name|String
name|url
decl_stmt|;
specifier|public
name|String
name|context
decl_stmt|;
specifier|public
name|Server
name|server
decl_stmt|;
specifier|public
name|File
name|root
decl_stmt|;
specifier|public
name|RemoteRepositoryConfiguration
name|config
decl_stmt|;
block|}
specifier|protected
specifier|static
specifier|final
name|long
name|ONE_SECOND
init|=
operator|(
literal|1000
comment|/* milliseconds */
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|ONE_MINUTE
init|=
operator|(
name|ONE_SECOND
operator|*
literal|60
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|ONE_HOUR
init|=
operator|(
name|ONE_MINUTE
operator|*
literal|60
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|ONE_DAY
init|=
operator|(
name|ONE_HOUR
operator|*
literal|24
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|OVER_ONE_HOUR
init|=
operator|(
name|ONE_HOUR
operator|+
name|ONE_MINUTE
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|OVER_ONE_DAY
init|=
operator|(
name|ONE_DAY
operator|+
name|ONE_HOUR
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|OLDER
init|=
operator|(
operator|-
literal|1
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|NEWER
init|=
literal|0
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|int
name|EXPECT_MANAGED_CONTENTS
init|=
literal|1
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|int
name|EXPECT_REMOTE_CONTENTS
init|=
literal|2
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|int
name|EXPECT_NOT_FOUND
init|=
literal|3
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|boolean
name|HAS_MANAGED_COPY
init|=
literal|true
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|boolean
name|NO_MANAGED_COPY
init|=
literal|false
decl_stmt|;
specifier|protected
name|RemoteRepoInfo
name|remoteCentral
decl_stmt|;
specifier|protected
name|RemoteRepoInfo
name|remoteSnapshots
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
name|startRepository
argument_list|()
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
name|shutdownServer
argument_list|(
name|remoteCentral
argument_list|)
expr_stmt|;
name|shutdownServer
argument_list|(
name|remoteSnapshots
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|RemoteRepoInfo
name|createServer
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|RemoteRepoInfo
name|repo
init|=
operator|new
name|RemoteRepoInfo
argument_list|()
decl_stmt|;
name|repo
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|repo
operator|.
name|context
operator|=
literal|"/"
operator|+
name|id
expr_stmt|;
name|repo
operator|.
name|root
operator|=
name|Files
operator|.
name|createTempDir
argument_list|()
expr_stmt|;
comment|// new File( System.getProperty( "basedir" ) + "target/remote-repos/" + id + "/" );
comment|// Remove exising root contents.
if|if
condition|(
name|repo
operator|.
name|root
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repo
operator|.
name|root
argument_list|)
expr_stmt|;
block|}
comment|// Establish root directory.
if|if
condition|(
operator|!
name|repo
operator|.
name|root
operator|.
name|exists
argument_list|()
condition|)
block|{
name|repo
operator|.
name|root
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|repo
operator|.
name|server
operator|=
operator|new
name|Server
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ContextHandlerCollection
name|contexts
init|=
operator|new
name|ContextHandlerCollection
argument_list|()
decl_stmt|;
name|repo
operator|.
name|server
operator|.
name|setHandler
argument_list|(
name|contexts
argument_list|)
expr_stmt|;
name|ServletContextHandler
name|context
init|=
operator|new
name|ServletContextHandler
argument_list|()
decl_stmt|;
name|context
operator|.
name|setContextPath
argument_list|(
name|repo
operator|.
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|setResourceBase
argument_list|(
name|repo
operator|.
name|root
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"dirAllowed"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
literal|"maxCacheSize"
argument_list|,
literal|0
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
name|contexts
operator|.
name|addHandler
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|repo
operator|.
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|int
name|port
init|=
name|repo
operator|.
name|server
operator|.
name|getConnectors
argument_list|()
index|[
literal|0
index|]
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
name|repo
operator|.
name|url
operator|=
literal|"http://localhost:"
operator|+
name|port
operator|+
name|repo
operator|.
name|context
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Remote HTTP Server started on {}"
argument_list|,
name|repo
operator|.
name|url
argument_list|)
expr_stmt|;
name|repo
operator|.
name|config
operator|=
name|createRemoteRepository
argument_list|(
name|repo
operator|.
name|id
argument_list|,
literal|"Testable ["
operator|+
name|repo
operator|.
name|id
operator|+
literal|"] Remote Repo"
argument_list|,
name|repo
operator|.
name|url
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|protected
name|void
name|assertServerSetupCorrectly
parameter_list|(
name|RemoteRepoInfo
name|remoteRepo
parameter_list|)
throws|throws
name|Exception
block|{
name|WebClient
name|client
init|=
name|newClient
argument_list|()
decl_stmt|;
name|int
name|status
init|=
name|client
operator|.
name|getPage
argument_list|(
name|remoteRepo
operator|.
name|url
argument_list|)
operator|.
name|getWebResponse
argument_list|()
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HttpServletResponse
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setupConnector
parameter_list|(
name|String
name|repoId
parameter_list|,
name|RemoteRepoInfo
name|remoteRepo
parameter_list|,
name|String
name|releasesPolicy
parameter_list|,
name|String
name|snapshotsPolicy
parameter_list|)
block|{
name|ProxyConnectorConfiguration
name|connector
init|=
operator|new
name|ProxyConnectorConfiguration
argument_list|()
decl_stmt|;
name|connector
operator|.
name|setSourceRepoId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setTargetRepoId
argument_list|(
name|remoteRepo
operator|.
name|id
argument_list|)
expr_stmt|;
name|connector
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_RELEASES
argument_list|,
name|releasesPolicy
argument_list|)
expr_stmt|;
name|connector
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_SNAPSHOTS
argument_list|,
name|snapshotsPolicy
argument_list|)
expr_stmt|;
name|connector
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_CHECKSUM
argument_list|,
name|ChecksumPolicy
operator|.
name|IGNORE
argument_list|)
expr_stmt|;
name|connector
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
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|shutdownServer
parameter_list|(
name|RemoteRepoInfo
name|remoteRepo
parameter_list|)
block|{
if|if
condition|(
name|remoteRepo
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|remoteRepo
operator|.
name|server
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|remoteRepo
operator|.
name|server
operator|.
name|isRunning
argument_list|()
condition|)
block|{
try|try
block|{
name|remoteRepo
operator|.
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// int graceful = remoteRepo.server.getGracefulShutdown();
comment|// System.out.println( "server set to graceful shutdown: " + graceful );
comment|// remoteRepo = null;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|protected
name|File
name|populateRepo
parameter_list|(
name|RemoteRepoInfo
name|remoteRepo
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|contents
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|remoteRepo
operator|.
name|root
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|destFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|destFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|destFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|destFile
argument_list|,
name|contents
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|destFile
return|;
block|}
specifier|protected
name|void
name|setupCentralRemoteRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|remoteCentral
operator|=
name|createServer
argument_list|(
literal|"central"
argument_list|)
expr_stmt|;
name|assertServerSetupCorrectly
argument_list|(
name|remoteCentral
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|remoteRepositoryConfiguration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRemoteRepositoriesAsMap
argument_list|()
operator|.
name|get
argument_list|(
name|remoteCentral
operator|.
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|remoteRepositoryConfiguration
operator|!=
literal|null
condition|)
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|removeRemoteRepository
argument_list|(
name|remoteRepositoryConfiguration
argument_list|)
expr_stmt|;
block|}
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addRemoteRepository
argument_list|(
name|remoteCentral
operator|.
name|config
argument_list|)
expr_stmt|;
name|setupCleanRepo
argument_list|(
name|remoteCentral
operator|.
name|root
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupConnector
parameter_list|(
name|String
name|repoId
parameter_list|,
name|RemoteRepoInfo
name|remoteRepo
parameter_list|)
block|{
name|setupConnector
argument_list|(
name|repoId
argument_list|,
name|remoteRepo
argument_list|,
name|ReleasesPolicy
operator|.
name|ALWAYS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupReleaseConnector
parameter_list|(
name|String
name|managedRepoId
parameter_list|,
name|RemoteRepoInfo
name|remoteRepo
parameter_list|,
name|String
name|releasePolicy
parameter_list|)
block|{
name|setupConnector
argument_list|(
name|managedRepoId
argument_list|,
name|remoteRepo
argument_list|,
name|releasePolicy
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupSnapshotConnector
parameter_list|(
name|String
name|managedRepoId
parameter_list|,
name|RemoteRepoInfo
name|remoteRepo
parameter_list|,
name|String
name|snapshotsPolicy
parameter_list|)
block|{
name|setupConnector
argument_list|(
name|managedRepoId
argument_list|,
name|remoteRepo
argument_list|,
name|ReleasesPolicy
operator|.
name|ALWAYS
argument_list|,
name|snapshotsPolicy
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupSnapshotsRemoteRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|remoteSnapshots
operator|=
name|createServer
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|assertServerSetupCorrectly
argument_list|(
name|remoteSnapshots
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|remoteRepositoryConfiguration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRemoteRepositoriesAsMap
argument_list|()
operator|.
name|get
argument_list|(
name|remoteSnapshots
operator|.
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|remoteRepositoryConfiguration
operator|!=
literal|null
condition|)
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|removeRemoteRepository
argument_list|(
name|remoteRepositoryConfiguration
argument_list|)
expr_stmt|;
block|}
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addRemoteRepository
argument_list|(
name|remoteSnapshots
operator|.
name|config
argument_list|)
expr_stmt|;
name|setupCleanRepo
argument_list|(
name|remoteSnapshots
operator|.
name|root
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

