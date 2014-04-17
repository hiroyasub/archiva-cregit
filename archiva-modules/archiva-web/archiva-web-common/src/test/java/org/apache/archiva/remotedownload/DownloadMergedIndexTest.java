begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|remotedownload
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
name|beans
operator|.
name|ProxyConnector
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
name|beans
operator|.
name|RepositoryGroup
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
name|maven2
operator|.
name|model
operator|.
name|Artifact
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
name|model
operator|.
name|SearchRequest
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
name|ManagedRepositoriesService
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
name|ProxyConnectorService
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
name|RepositoriesService
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
name|RepositoryGroupService
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
name|SearchService
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
name|archiva
operator|.
name|redback
operator|.
name|integration
operator|.
name|security
operator|.
name|role
operator|.
name|RedbackRoleConstants
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
name|services
operator|.
name|FakeCreateAdminService
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
name|Arrays
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
name|DownloadMergedIndexTest
extends|extends
name|AbstractDownloadTest
block|{
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"appserver.base"
argument_list|,
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
operator|+
literal|"/target/"
operator|+
name|DownloadMergedIndexTest
operator|.
name|class
operator|.
name|getName
argument_list|()
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
return|return
literal|"classpath*:META-INF/spring-context.xml classpath*:spring-context-test-common.xml classpath*:spring-context-merge-index-download.xml"
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|File
name|tmpIndexDir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
operator|+
literal|"/tmpIndex"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpIndexDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|tmpIndexDir
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|downloadMergedIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|tmpIndexDir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
operator|+
literal|"/tmpIndex"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpIndexDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|tmpIndexDir
argument_list|)
expr_stmt|;
block|}
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
name|ManagedRepository
name|managedRepository
init|=
operator|new
name|ManagedRepository
argument_list|()
decl_stmt|;
name|managedRepository
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setName
argument_list|(
literal|"name of "
operator|+
name|id
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setLocation
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
operator|+
literal|"/src/test/repositories/test-repo"
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setIndexDirectory
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
operator|+
literal|"/tmpIndex/"
operator|+
name|id
argument_list|)
expr_stmt|;
name|ManagedRepositoriesService
name|managedRepositoriesService
init|=
name|getManagedRepositoriesService
argument_list|()
decl_stmt|;
if|if
condition|(
name|managedRepositoriesService
operator|.
name|getManagedRepository
argument_list|(
name|id
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|managedRepositoriesService
operator|.
name|deleteManagedRepository
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|getManagedRepositoriesService
argument_list|()
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|RepositoriesService
name|repositoriesService
init|=
name|getRepositoriesService
argument_list|()
decl_stmt|;
name|repositoriesService
operator|.
name|scanRepositoryNow
argument_list|(
name|id
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// wait a bit to ensure index is finished
name|int
name|timeout
init|=
literal|20000
decl_stmt|;
while|while
condition|(
name|timeout
operator|>
literal|0
operator|&&
name|repositoriesService
operator|.
name|alreadyScanning
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|timeout
operator|-=
literal|500
expr_stmt|;
block|}
name|RepositoryGroupService
name|repositoryGroupService
init|=
name|getRepositoryGroupService
argument_list|()
decl_stmt|;
name|String
name|repoGroupId
init|=
literal|"test-group"
decl_stmt|;
if|if
condition|(
name|repositoryGroupService
operator|.
name|getRepositoryGroup
argument_list|(
name|repoGroupId
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|repositoryGroupService
operator|.
name|deleteRepositoryGroup
argument_list|(
name|repoGroupId
argument_list|)
expr_stmt|;
block|}
name|RepositoryGroup
name|repositoryGroup
init|=
operator|new
name|RepositoryGroup
argument_list|()
decl_stmt|;
name|repositoryGroup
operator|.
name|setId
argument_list|(
name|repoGroupId
argument_list|)
expr_stmt|;
name|repositoryGroup
operator|.
name|setRepositories
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|repositoryGroupService
operator|.
name|addRepositoryGroup
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
comment|// create a repo with a remote on the one with index
name|id
operator|=
name|Long
operator|.
name|toString
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|managedRepository
operator|=
operator|new
name|ManagedRepository
argument_list|()
expr_stmt|;
name|managedRepository
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setName
argument_list|(
literal|"name of "
operator|+
name|id
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setLocation
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
operator|+
literal|"/src/test/repositories/test-repo"
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setIndexDirectory
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
operator|+
literal|"/tmpIndex/"
operator|+
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|managedRepositoriesService
operator|.
name|getManagedRepository
argument_list|(
name|id
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|managedRepositoriesService
operator|.
name|deleteManagedRepository
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|getManagedRepositoriesService
argument_list|()
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|RemoteRepository
name|remoteRepository
init|=
operator|new
name|RemoteRepository
argument_list|()
decl_stmt|;
name|remoteRepository
operator|.
name|setId
argument_list|(
literal|"all-merged"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setName
argument_list|(
literal|"all-merged"
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
name|setUrl
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/repository/test-group"
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
literal|"/repository/test-group/.indexer"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setUserName
argument_list|(
name|RedbackRoleConstants
operator|.
name|ADMINISTRATOR_ACCOUNT_NAME
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setPassword
argument_list|(
name|FakeCreateAdminService
operator|.
name|ADMIN_TEST_PWD
argument_list|)
expr_stmt|;
if|if
condition|(
name|getRemoteRepositoriesService
argument_list|()
operator|.
name|getRemoteRepository
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|getRemoteRepositoriesService
argument_list|()
operator|.
name|deleteRemoteRepository
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getRemoteRepositoriesService
argument_list|()
operator|.
name|addRemoteRepository
argument_list|(
name|remoteRepository
argument_list|)
expr_stmt|;
name|ProxyConnectorService
name|proxyConnectorService
init|=
name|getProxyConnectorService
argument_list|()
decl_stmt|;
name|ProxyConnector
name|proxyConnector
init|=
operator|new
name|ProxyConnector
argument_list|()
decl_stmt|;
name|proxyConnector
operator|.
name|setProxyId
argument_list|(
literal|"foo-bar1"
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setSourceRepoId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setTargetRepoId
argument_list|(
literal|"all-merged"
argument_list|)
expr_stmt|;
name|proxyConnectorService
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector
argument_list|)
expr_stmt|;
name|repositoriesService
operator|.
name|scheduleDownloadRemoteIndex
argument_list|(
literal|"all-merged"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// wait a bit
name|timeout
operator|=
literal|20000
expr_stmt|;
while|while
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|timeout
operator|-=
literal|500
expr_stmt|;
block|}
name|SearchService
name|searchService
init|=
name|getSearchService
argument_list|()
decl_stmt|;
name|SearchRequest
name|request
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setRepositories
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setGroupId
argument_list|(
literal|"org.apache.felix"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
name|searchService
operator|.
name|searchArtifacts
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|artifacts
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

