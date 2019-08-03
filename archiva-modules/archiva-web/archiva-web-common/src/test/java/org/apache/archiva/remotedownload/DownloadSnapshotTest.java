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
name|Locale
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
name|DownloadSnapshotTest
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
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Path
name|appServerBase
decl_stmt|;
specifier|private
name|Path
name|indexDir
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
literal|"archiva-common-web_appsrv5_"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
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
name|Before
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|IOException
block|{
name|indexDir
operator|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"archiva-web-common-index"
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|indexDir
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|downloadSNAPSHOT
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
literal|"src/test/repositories/snapshot-repo"
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
literal|"snapshot-repo-"
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
name|ManagedRepository
name|managedRepository
init|=
operator|new
name|ManagedRepository
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
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
name|testRep
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setIndexDirectory
argument_list|(
name|indexDir
operator|.
name|resolve
argument_list|(
literal|"index-"
operator|+
name|id
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setPackedIndexDirectory
argument_list|(
name|indexDir
operator|.
name|resolve
argument_list|(
literal|"indexpacked-"
operator|+
name|id
argument_list|)
operator|.
name|toString
argument_list|()
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
name|id
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
name|id
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
name|id
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
literal|"target/archiva-model-1.4-M4-SNAPSHOT.jar"
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
literal|"/repository/"
operator|+
name|id
operator|+
literal|"/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-SNAPSHOT.jar"
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
literal|"org/apache/archiva/model/ArchivaArtifact.class"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"cannot find zipEntry org/apache/archiva/model/ArchivaArtifact.class, entries: "
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
block|}
end_class

end_unit

