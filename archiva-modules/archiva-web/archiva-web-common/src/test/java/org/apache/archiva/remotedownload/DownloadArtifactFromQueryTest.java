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
name|Assert
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|DownloadArtifactFromQueryTest
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
name|DownloadArtifactFromQueryTest
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
name|Path
name|tmpIndexDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|,
literal|"tmpIndex"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|tmpIndexDir
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|tmpIndexDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|createAndScanRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|tmpIndexDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|,
literal|"tmpIndex"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|tmpIndexDir
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|tmpIndexDir
operator|.
name|toFile
argument_list|()
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
return|return
name|id
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|downloadFixedVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|createAndScanRepo
argument_list|()
decl_stmt|;
try|try
block|{
name|Response
name|response
init|=
name|getSearchService
argument_list|()
operator|.
name|redirectToArtifactFile
argument_list|(
literal|null
argument_list|,
literal|"org.apache.archiva"
argument_list|,
literal|"archiva-test"
argument_list|,
literal|"1.0"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|TEMPORARY_REDIRECT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|location
init|=
name|String
operator|.
name|class
operator|.
name|cast
argument_list|(
name|response
operator|.
name|getMetadata
argument_list|()
operator|.
name|get
argument_list|(
literal|"Location"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
comment|/// http://localhost:57168/repository/1400639145722/org/apache/archiva/archiva-test/1.0/archiva-test-1.0.jar
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/repository/"
operator|+
name|id
operator|+
literal|"/org/apache/archiva/archiva-test/1.0/archiva-test-1.0.jar"
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|getManagedRepositoriesService
argument_list|()
operator|.
name|deleteManagedRepository
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|downloadLatestVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|createAndScanRepo
argument_list|()
decl_stmt|;
try|try
block|{
name|Response
name|response
init|=
name|getSearchService
argument_list|()
operator|.
name|redirectToArtifactFile
argument_list|(
literal|null
argument_list|,
literal|"org.apache.archiva"
argument_list|,
literal|"archiva-test"
argument_list|,
literal|"LATEST"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|TEMPORARY_REDIRECT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|location
init|=
name|String
operator|.
name|class
operator|.
name|cast
argument_list|(
name|response
operator|.
name|getMetadata
argument_list|()
operator|.
name|get
argument_list|(
literal|"Location"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
comment|/// http://localhost:57168/repository/1400639145722/org/apache/archiva/archiva-test/1.0/archiva-test-1.0.jar
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/repository/"
operator|+
name|id
operator|+
literal|"/org/apache/archiva/archiva-test/2.0/archiva-test-2.0.jar"
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|getManagedRepositoriesService
argument_list|()
operator|.
name|deleteManagedRepository
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|download_no_content
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|createAndScanRepo
argument_list|()
decl_stmt|;
try|try
block|{
name|Response
name|response
init|=
name|getSearchService
argument_list|()
operator|.
name|redirectToArtifactFile
argument_list|(
literal|null
argument_list|,
literal|"org.apache.archiva.beer"
argument_list|,
literal|"archiva-wine"
argument_list|,
literal|"LATEST"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|NO_CONTENT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|getManagedRepositoriesService
argument_list|()
operator|.
name|deleteManagedRepository
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

