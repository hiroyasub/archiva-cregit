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
name|security
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|RepositoryAdminException
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
name|runtime
operator|.
name|RedbackRuntimeConfigurationAdmin
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
name|RbacManagerException
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
name|check
operator|.
name|EnvironmentCheck
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
name|apache
operator|.
name|archiva
operator|.
name|redback
operator|.
name|users
operator|.
name|UserManagerException
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
name|UserNotFoundException
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
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"environmentCheck#archiva-locked-admin-check"
argument_list|)
specifier|public
class|class
name|ArchivaLockedAdminEnvironmentCheck
implements|implements
name|EnvironmentCheck
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
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"rBACManager#cached"
argument_list|)
specifier|private
name|RBACManager
name|rbacManager
decl_stmt|;
comment|/**      * boolean detailing if this environment check has been executed      */
specifier|private
name|boolean
name|checked
init|=
literal|false
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
name|RedbackRuntimeConfigurationAdmin
name|redbackRuntimeConfigurationAdmin
decl_stmt|;
specifier|private
name|List
argument_list|<
name|UserManager
argument_list|>
name|userManagers
decl_stmt|;
annotation|@
name|PostConstruct
specifier|protected
name|void
name|initialize
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|userManagerImpls
init|=
name|redbackRuntimeConfigurationAdmin
operator|.
name|getRedbackRuntimeConfiguration
argument_list|()
operator|.
name|getUserManagerImpls
argument_list|()
decl_stmt|;
name|userManagers
operator|=
operator|new
name|ArrayList
argument_list|<
name|UserManager
argument_list|>
argument_list|(
name|userManagerImpls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|beanId
range|:
name|userManagerImpls
control|)
block|{
name|userManagers
operator|.
name|add
argument_list|(
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"userManager#"
operator|+
name|beanId
argument_list|,
name|UserManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This environment check will unlock system administrator accounts that are locked on the restart of the      * application when the environment checks are processed.      *      * @param violations      */
specifier|public
name|void
name|validateEnvironment
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|violations
parameter_list|)
block|{
if|if
condition|(
operator|!
name|checked
condition|)
block|{
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagers
control|)
block|{
if|if
condition|(
name|userManager
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|roles
operator|.
name|add
argument_list|(
name|RedbackRoleConstants
operator|.
name|SYSTEM_ADMINISTRATOR_ROLE
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|UserAssignment
argument_list|>
name|systemAdminstrators
decl_stmt|;
try|try
block|{
name|systemAdminstrators
operator|=
name|rbacManager
operator|.
name|getUserAssignmentsForRoles
argument_list|(
name|roles
argument_list|)
expr_stmt|;
for|for
control|(
name|UserAssignment
name|userAssignment
range|:
name|systemAdminstrators
control|)
block|{
try|try
block|{
name|User
name|admin
init|=
name|userManager
operator|.
name|findUser
argument_list|(
name|userAssignment
operator|.
name|getPrincipal
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|admin
operator|.
name|isLocked
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Unlocking system administrator: {}"
argument_list|,
name|admin
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|admin
operator|.
name|setLocked
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|userManager
operator|.
name|updateUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UserNotFoundException
name|ne
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Dangling UserAssignment -> {}"
argument_list|,
name|userAssignment
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UserManagerException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"fail to find user {}Â for admin unlock check: {}"
argument_list|,
name|userAssignment
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RbacManagerException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Exception when checking for locked admin user: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|checked
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit
