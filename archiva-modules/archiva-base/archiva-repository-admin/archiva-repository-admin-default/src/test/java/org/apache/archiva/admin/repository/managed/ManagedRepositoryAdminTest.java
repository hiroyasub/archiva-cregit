begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
operator|.
name|managed
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
name|repository
operator|.
name|AbstractRepositoryAdminTest
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
name|metadata
operator|.
name|model
operator|.
name|facets
operator|.
name|AuditEvent
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
name|RepositoryRegistry
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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|nio
operator|.
name|file
operator|.
name|StandardCopyOption
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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|ManagedRepositoryAdminTest
extends|extends
name|AbstractRepositoryAdminTest
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STAGE_REPO_ID_END
init|=
name|DefaultManagedRepositoryAdmin
operator|.
name|STAGE_REPO_ID_END
decl_stmt|;
name|String
name|repoId
init|=
literal|"test-new-one"
decl_stmt|;
name|String
name|repoLocation
init|=
name|Paths
operator|.
name|get
argument_list|(
name|APPSERVER_BASE_PATH
argument_list|,
name|repoId
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
name|Path
name|archivaCfg1
init|=
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
argument_list|,
literal|".m2"
argument_list|,
literal|"archiva.xml"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|archivaCfg1
argument_list|)
expr_stmt|;
name|Path
name|archivaCfg2
init|=
name|Paths
operator|.
name|get
argument_list|(
name|APPSERVER_BASE_PATH
argument_list|,
literal|"conf/archiva.xml"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|archivaCfg2
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|archivaCfg2
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"default-archiva.xml"
argument_list|)
decl_stmt|;
name|Path
name|defaultCfg
init|=
name|Paths
operator|.
name|get
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|defaultCfg
argument_list|,
name|archivaCfg2
argument_list|,
name|StandardCopyOption
operator|.
name|REPLACE_EXISTING
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|()
expr_stmt|;
name|repositoryRegistry
operator|.
name|reload
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getAllManagedRepos
parameter_list|()
throws|throws
name|Exception
block|{
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|repos
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repos
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"repos {}"
argument_list|,
name|repos
argument_list|)
expr_stmt|;
comment|// check default internal
name|ManagedRepository
name|internal
init|=
name|findManagedRepoById
argument_list|(
name|repos
argument_list|,
literal|"internal"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|internal
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|internal
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|internal
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getById
parameter_list|()
throws|throws
name|Exception
block|{
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|ManagedRepository
name|repo
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
literal|"internal"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|addDeleteManagedRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|Path
name|repoDir
init|=
name|clearRepoLocation
argument_list|(
name|repoLocation
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|repos
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|int
name|initialSize
init|=
name|repos
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|initialSize
operator|>
literal|0
argument_list|)
expr_stmt|;
name|ManagedRepository
name|repo
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
name|repo
operator|.
name|setId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
literal|"test repo"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setCronExpression
argument_list|(
literal|"0 0 * * * ?"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setDescription
argument_list|(
literal|"cool repo"
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|,
literal|false
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|repos
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|+
literal|1
argument_list|,
name|repos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ManagedRepository
name|managedRepository
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test repo"
argument_list|,
name|managedRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cool repo"
argument_list|,
name|managedRepository
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertTemplateRoleExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|deleteManagedRepository
argument_list|(
name|repoId
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// deleteContents false
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|repoDir
argument_list|)
argument_list|)
expr_stmt|;
name|repos
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
argument_list|,
name|repos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTemplateRoleNotExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertAuditListenerCallAddAndDelete
argument_list|()
expr_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|updateDeleteManagedRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|repoDir
init|=
name|clearRepoLocation
argument_list|(
name|repoLocation
argument_list|)
decl_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|repos
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|int
name|initialSize
init|=
name|repos
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|initialSize
operator|>
literal|0
argument_list|)
expr_stmt|;
name|ManagedRepository
name|repo
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
name|repo
operator|.
name|setId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
literal|"test repo"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setCronExpression
argument_list|(
literal|"0 0 * * * ?"
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|,
literal|false
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertTemplateRoleExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|repos
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|+
literal|1
argument_list|,
name|repos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|newName
init|=
literal|"test repo update"
decl_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|String
name|description
init|=
literal|"so great repository"
decl_stmt|;
name|repo
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setCronExpression
argument_list|(
literal|"0 0 * * * ?"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setSkipPackedIndexCreation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|updateManagedRepository
argument_list|(
name|repo
argument_list|,
literal|false
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|repo
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newName
argument_list|,
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
argument_list|)
operator|.
name|normalize
argument_list|()
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
name|repo
operator|.
name|getLocation
argument_list|()
argument_list|)
operator|.
name|normalize
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|description
argument_list|,
name|repo
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|isSkipPackedIndexCreation
argument_list|()
argument_list|)
expr_stmt|;
name|assertTemplateRoleExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|deleteManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// check deleteContents false
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|repoDir
argument_list|)
argument_list|)
expr_stmt|;
name|assertTemplateRoleNotExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertAuditListenerCallAndUpdateAddAndDelete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|addDeleteManagedRepoWithStaged
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|repoDir
init|=
name|clearRepoLocation
argument_list|(
name|repoLocation
argument_list|)
decl_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|repos
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|int
name|initialSize
init|=
name|repos
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|initialSize
operator|>
literal|0
argument_list|)
expr_stmt|;
name|ManagedRepository
name|repo
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
name|repo
operator|.
name|setId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
literal|"test repo"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setCronExpression
argument_list|(
literal|"0 0 * * * ?"
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|,
literal|true
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|repos
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|+
literal|2
argument_list|,
name|repos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
name|assertTemplateRoleExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|repoDir
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|repoId
operator|+
name|STAGE_REPO_ID_END
argument_list|)
argument_list|)
expr_stmt|;
name|assertTemplateRoleExists
argument_list|(
name|repoId
operator|+
name|STAGE_REPO_ID_END
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
operator|+
name|STAGE_REPO_ID_END
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|deleteManagedRepository
argument_list|(
name|repoId
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|repoDir
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
operator|+
name|STAGE_REPO_ID_END
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTemplateRoleNotExists
argument_list|(
name|repoId
operator|+
name|STAGE_REPO_ID_END
argument_list|)
expr_stmt|;
name|repos
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
argument_list|,
name|repos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTemplateRoleNotExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertTemplateRoleNotExists
argument_list|(
name|repoId
operator|+
name|STAGE_REPO_ID_END
argument_list|)
expr_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|updateDeleteManagedRepoWithStagedRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|stageRepoLocation
init|=
name|Paths
operator|.
name|get
argument_list|(
name|APPSERVER_BASE_PATH
argument_list|,
name|repoId
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Path
name|repoDir
init|=
name|clearRepoLocation
argument_list|(
name|repoLocation
argument_list|)
decl_stmt|;
name|clearRepoLocation
argument_list|(
name|repoLocation
operator|+
name|STAGE_REPO_ID_END
argument_list|)
expr_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|repos
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|int
name|initialSize
init|=
name|repos
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|initialSize
operator|>
literal|0
argument_list|)
expr_stmt|;
name|ManagedRepository
name|repo
init|=
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
decl_stmt|;
name|managedRepositoryAdmin
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|,
literal|false
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertTemplateRoleExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
operator|+
name|STAGE_REPO_ID_END
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTemplateRoleNotExists
argument_list|(
name|repoId
operator|+
name|STAGE_REPO_ID_END
argument_list|)
expr_stmt|;
name|repos
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|+
literal|1
argument_list|,
name|repos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
operator|.
name|getIndexDirectory
argument_list|()
argument_list|,
name|repo
operator|.
name|getIndexDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|newName
init|=
literal|"test repo update"
decl_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|updateManagedRepository
argument_list|(
name|repo
argument_list|,
literal|true
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|repo
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newName
argument_list|,
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
name|repo
operator|.
name|getLocation
argument_list|()
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
operator|.
name|getCronExpression
argument_list|()
argument_list|,
name|repo
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
operator|.
name|getLayout
argument_list|()
argument_list|,
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
operator|.
name|getIndexDirectory
argument_list|()
argument_list|,
name|repo
operator|.
name|getIndexDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
operator|.
name|getRetentionPeriod
argument_list|()
argument_list|,
name|repo
operator|.
name|getRetentionPeriod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repo
operator|.
name|getRetentionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getTestManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoLocation
argument_list|)
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|,
name|repo
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|assertTemplateRoleExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|stageRepoLocation
operator|+
name|STAGE_REPO_ID_END
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTemplateRoleExists
argument_list|(
name|repoId
operator|+
name|STAGE_REPO_ID_END
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|deleteManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// check deleteContents false
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|repoDir
argument_list|)
argument_list|)
expr_stmt|;
name|assertTemplateRoleNotExists
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|stageRepoLocation
operator|+
name|STAGE_REPO_ID_END
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTemplateRoleNotExists
argument_list|(
name|repoId
operator|+
name|STAGE_REPO_ID_END
argument_list|)
expr_stmt|;
name|assertAuditListenerCallAndUpdateAddAndDelete
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
operator|+
name|STAGE_REPO_ID_END
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
operator|+
name|STAGE_REPO_ID_END
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//----------------------------------
comment|// utility methods
comment|//----------------------------------
specifier|private
name|void
name|assertTemplateRoleExists
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertTemplateRoleNotExists
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertAuditListenerCallAddAndDelete
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|ADD_MANAGED_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-localhost"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRemoteIP
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|DELETE_MANAGED_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertAuditListenerCallAndUpdateAddAndDelete
parameter_list|(
name|boolean
name|stageNeeded
parameter_list|)
block|{
if|if
condition|(
name|stageNeeded
condition|)
block|{
name|assertEquals
argument_list|(
literal|"not 4 audit events "
operator|+
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
argument_list|,
literal|4
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"not 3 audit events "
operator|+
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
argument_list|,
literal|3
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|ADD_MANAGED_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-localhost"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRemoteIP
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|stageNeeded
condition|)
block|{
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|ADD_MANAGED_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|MODIFY_MANAGED_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|DELETE_MANAGED_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|MODIFY_MANAGED_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|DELETE_MANAGED_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

