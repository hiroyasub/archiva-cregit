begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|repositories
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|Collections
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Action
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|Configuration
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
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|archiva
operator|.
name|security
operator|.
name|ArchivaRoleConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|role
operator|.
name|RoleManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|integration
operator|.
name|interceptor
operator|.
name|SecureActionBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|integration
operator|.
name|interceptor
operator|.
name|SecureActionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_comment
comment|/**  * AddManagedRepositoryActionTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|AddManagedRepositoryActionTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|AddManagedRepositoryAction
name|action
decl_stmt|;
specifier|private
name|RoleManager
name|roleManager
decl_stmt|;
specifier|private
name|MockControl
name|roleManagerControl
decl_stmt|;
specifier|private
name|MockControl
name|archivaConfigurationControl
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPO_ID
init|=
literal|"repo-ident"
decl_stmt|;
specifier|private
name|File
name|location
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
name|getPlexusConfigLocation
parameter_list|()
block|{
return|return
name|AbstractManagedRepositoriesAction
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"Test.xml"
return|;
block|}
annotation|@
name|Override
specifier|protected
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
name|action
operator|=
operator|new
name|AddManagedRepositoryAction
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|archivaConfigurationControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|action
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|RoleManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|roleManager
operator|=
operator|(
name|RoleManager
operator|)
name|roleManagerControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRoleManager
argument_list|(
name|roleManager
argument_list|)
expr_stmt|;
name|location
operator|=
name|getTestFile
argument_list|(
literal|"target/test/location"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
operator|new
name|Configuration
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|SecureActionBundle
name|bundle
init|=
name|action
operator|.
name|getSecureActionBundle
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|bundle
operator|.
name|requiresAuthentication
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bundle
operator|.
name|getAuthorizationTuples
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddRepositoryInitialPage
parameter_list|()
throws|throws
name|Exception
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
operator|new
name|Configuration
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ManagedRepositoryConfiguration
name|configuration
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|configuration
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// check all booleans are false
name|assertFalse
argument_list|(
name|configuration
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|configuration
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|configuration
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|configuration
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|input
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|INPUT
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// check defaults
name|assertFalse
argument_list|(
name|configuration
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|configuration
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|REPO_ID
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|.
name|setReturnValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|REPO_ID
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|.
name|setVoidCallable
argument_list|()
expr_stmt|;
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|REPO_ID
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|.
name|setReturnValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|REPO_ID
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|.
name|setVoidCallable
argument_list|()
expr_stmt|;
name|roleManagerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|populateRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|location
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|commit
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|location
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|repository
argument_list|)
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testAddRepositoryExistingLocation
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|location
operator|.
name|exists
argument_list|()
condition|)
block|{
name|location
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|populateRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|location
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|commit
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|AddManagedRepositoryAction
operator|.
name|CONFIRM
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|populateRepository
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
block|{
name|repository
operator|.
name|setId
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setName
argument_list|(
literal|"repo name"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setLocation
argument_list|(
name|location
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setRefreshCronExpression
argument_list|(
literal|"* 0/5 * * * ?"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setDaysOlder
argument_list|(
literal|31
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setRetentionCount
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setScanned
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// TODO: test errors during add, other actions
block|}
end_class

end_unit

