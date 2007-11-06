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
name|security
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
name|List
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
name|lang
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
name|ManagedRepositoryConfiguration
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
name|PlexusTestCase
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
name|rbac
operator|.
name|RBACManager
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
name|redback
operator|.
name|system
operator|.
name|SecuritySystem
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
name|users
operator|.
name|User
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
name|users
operator|.
name|UserManager
import|;
end_import

begin_comment
comment|/**  * DefaultUserRepositoriesTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DefaultUserRepositoriesTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|USER_GUEST
init|=
literal|"guest"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USER_ADMIN
init|=
literal|"admin"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USER_ALPACA
init|=
literal|"alpaca"
decl_stmt|;
specifier|private
name|SecuritySystem
name|securitySystem
decl_stmt|;
specifier|private
name|RBACManager
name|rbacManager
decl_stmt|;
specifier|private
name|RoleManager
name|roleManager
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|UserRepositories
name|userRepos
decl_stmt|;
specifier|public
name|void
name|testGetObservableRepositoryIds
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create some users.
name|createUser
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"Al 'Archiva' Paca"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected users"
argument_list|,
literal|3
argument_list|,
name|securitySystem
operator|.
name|getUserManager
argument_list|()
operator|.
name|getUsers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// some unassigned repo observer roles.
name|setupRepository
argument_list|(
literal|"central"
argument_list|)
expr_stmt|;
name|setupRepository
argument_list|(
literal|"corporate"
argument_list|)
expr_stmt|;
name|setupRepository
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|setupRepository
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|setupRepository
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
comment|// some assigned repo observer roles.
name|assignRepositoryObserverRole
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
name|assignRepositoryObserverRole
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"central"
argument_list|)
expr_stmt|;
name|assignRepositoryObserverRole
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
comment|// the global repo observer role.
name|assignGlobalRepositoryObserverRole
argument_list|(
name|USER_ADMIN
argument_list|)
expr_stmt|;
name|assertRepoIds
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"central"
block|,
literal|"corporate"
block|}
argument_list|,
name|userRepos
operator|.
name|getObservableRepositoryIds
argument_list|(
name|USER_ALPACA
argument_list|)
argument_list|)
expr_stmt|;
name|assertRepoIds
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"coporate"
block|}
argument_list|,
name|userRepos
operator|.
name|getObservableRepositoryIds
argument_list|(
name|USER_GUEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertRepoIds
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"central"
block|,
literal|"internal"
block|,
literal|"corporate"
block|,
literal|"snapshots"
block|,
literal|"secret"
block|}
argument_list|,
name|userRepos
operator|.
name|getObservableRepositoryIds
argument_list|(
name|USER_ADMIN
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertRepoIds
parameter_list|(
name|String
index|[]
name|expectedRepoIds
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Observable Repository Ids cannot be null."
argument_list|,
name|observableRepositoryIds
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedRepoIds
operator|.
name|length
operator|!=
name|observableRepositoryIds
operator|.
name|size
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Size of Observable Repository Ids wrong, expected<"
operator|+
name|expectedRepoIds
operator|.
name|length
operator|+
literal|"> but got<"
operator|+
name|observableRepositoryIds
operator|.
name|size
argument_list|()
operator|+
literal|"> instead. \nExpected: ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|expectedRepoIds
argument_list|,
literal|","
argument_list|)
operator|+
literal|"]\nActual: ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|observableRepositoryIds
operator|.
name|iterator
argument_list|()
argument_list|,
literal|","
argument_list|)
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setupRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Add repo to configuration.
name|ManagedRepositoryConfiguration
name|repoConfig
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repoConfig
operator|.
name|setId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setName
argument_list|(
literal|"Testable repo<"
operator|+
name|repoId
operator|+
literal|">"
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLocation
argument_list|(
name|getTestPath
argument_list|(
literal|"target/test-repo/"
operator|+
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addManagedRepository
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
comment|// Add repo roles to security.
name|userRepos
operator|.
name|createMissingRepositoryRoles
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assignGlobalRepositoryObserverRole
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|Exception
block|{
name|roleManager
operator|.
name|assignRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_GLOBAL_REPOSITORY_OBSERVER
argument_list|,
name|principal
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assignRepositoryObserverRole
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|Exception
block|{
name|roleManager
operator|.
name|assignTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|repoId
argument_list|,
name|principal
argument_list|)
expr_stmt|;
block|}
specifier|private
name|User
name|createUser
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|fullname
parameter_list|)
block|{
name|UserManager
name|userManager
init|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
decl_stmt|;
name|User
name|user
init|=
name|userManager
operator|.
name|createUser
argument_list|(
name|principal
argument_list|,
name|fullname
argument_list|,
name|principal
operator|+
literal|"@testable.archiva.apache.org"
argument_list|)
decl_stmt|;
name|securitySystem
operator|.
name|getPolicy
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|userManager
operator|.
name|addUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|securitySystem
operator|.
name|getPolicy
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|user
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
name|File
name|srcConfig
init|=
name|getTestFile
argument_list|(
literal|"src/test/resources/repository-archiva.xml"
argument_list|)
decl_stmt|;
name|File
name|destConfig
init|=
name|getTestFile
argument_list|(
literal|"target/test-conf/archiva.xml"
argument_list|)
decl_stmt|;
name|destConfig
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|destConfig
operator|.
name|delete
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|srcConfig
argument_list|,
name|destConfig
argument_list|)
expr_stmt|;
name|securitySystem
operator|=
operator|(
name|SecuritySystem
operator|)
name|lookup
argument_list|(
name|SecuritySystem
operator|.
name|class
argument_list|,
literal|"testable"
argument_list|)
expr_stmt|;
name|rbacManager
operator|=
operator|(
name|RBACManager
operator|)
name|lookup
argument_list|(
name|RBACManager
operator|.
name|class
argument_list|,
literal|"memory"
argument_list|)
expr_stmt|;
name|roleManager
operator|=
operator|(
name|RoleManager
operator|)
name|lookup
argument_list|(
name|RoleManager
operator|.
name|class
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|userRepos
operator|=
operator|(
name|UserRepositories
operator|)
name|lookup
argument_list|(
name|UserRepositories
operator|.
name|class
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Some basic asserts.
name|assertNotNull
argument_list|(
name|securitySystem
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|rbacManager
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|roleManager
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|userRepos
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
comment|// Setup Admin User.
name|User
name|adminUser
init|=
name|createUser
argument_list|(
name|USER_ADMIN
argument_list|,
literal|"Admin User"
argument_list|)
decl_stmt|;
name|roleManager
operator|.
name|assignRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_SYSTEM_ADMIN
argument_list|,
name|adminUser
operator|.
name|getPrincipal
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Setup Guest User.
name|User
name|guestUser
init|=
name|createUser
argument_list|(
name|USER_GUEST
argument_list|,
literal|"Guest User"
argument_list|)
decl_stmt|;
name|roleManager
operator|.
name|assignRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_GUEST
argument_list|,
name|guestUser
operator|.
name|getPrincipal
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

