begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
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
name|rbac
operator|.
name|RBACManager
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
name|rbac
operator|.
name|RbacObjectNotFoundException
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
name|role
operator|.
name|RoleManager
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
name|users
operator|.
name|User
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
name|ManagedRepositoryConfiguration
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
name|rbac
operator|.
name|UserAssignment
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
name|system
operator|.
name|SecuritySystem
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
name|users
operator|.
name|UserManager
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
name|inject
operator|.
name|Named
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

begin_comment
comment|/**  * AbstractSecurityTest  *  */
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
literal|"classpath*:/spring-context.xml"
block|}
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractSecurityTest
extends|extends
name|TestCase
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
specifier|protected
specifier|static
specifier|final
name|String
name|USER_GUEST
init|=
literal|"guest"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|USER_ADMIN
init|=
literal|"admin"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|USER_ALPACA
init|=
literal|"alpaca"
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"securitySystem#testable"
argument_list|)
specifier|protected
name|SecuritySystem
name|securitySystem
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"rBACManager#memory"
argument_list|)
specifier|protected
name|RBACManager
name|rbacManager
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RoleManager
name|roleManager
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration#default"
argument_list|)
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|UserRepositories
name|userRepos
decl_stmt|;
specifier|protected
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
operator|new
name|File
argument_list|(
literal|"./target/test-repo/"
operator|+
name|repoId
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
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
block|}
comment|// Add repo roles to security.
name|userRepos
operator|.
name|createMissingRepositoryRoles
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
block|}
specifier|protected
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
specifier|protected
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
annotation|@
name|Before
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
name|File
name|srcConfig
init|=
operator|new
name|File
argument_list|(
literal|"./src/test/resources/repository-archiva.xml"
argument_list|)
decl_stmt|;
name|File
name|destConfig
init|=
operator|new
name|File
argument_list|(
literal|"./target/test-conf/archiva.xml"
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
name|getUsername
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
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|restoreGuestInitialValues
parameter_list|(
name|String
name|userId
parameter_list|)
throws|throws
name|Exception
block|{
name|UserAssignment
name|userAssignment
init|=
literal|null
decl_stmt|;
try|try
block|{
name|userAssignment
operator|=
name|rbacManager
operator|.
name|getUserAssignment
argument_list|(
name|userId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RbacObjectNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"ignore RbacObjectNotFoundException for id {} during restoreGuestInitialValues"
argument_list|,
name|userId
argument_list|)
expr_stmt|;
return|return;
block|}
name|userAssignment
operator|.
name|setRoleNames
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"Guest"
argument_list|)
argument_list|)
expr_stmt|;
name|rbacManager
operator|.
name|saveUserAssignment
argument_list|(
name|userAssignment
argument_list|)
expr_stmt|;
name|CacheManager
operator|.
name|getInstance
argument_list|()
operator|.
name|clearAll
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

