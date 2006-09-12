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
name|util
package|;
end_package

begin_comment
comment|/* * Copyright 2005 The Apache Software Foundation. * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. * You may obtain a copy of the License at * *      http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
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
name|security
operator|.
name|rbac
operator|.
name|Operation
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
name|security
operator|.
name|rbac
operator|.
name|Permission
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
name|security
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
name|security
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
name|codehaus
operator|.
name|plexus
operator|.
name|security
operator|.
name|rbac
operator|.
name|RbacStoreException
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
name|security
operator|.
name|rbac
operator|.
name|Resource
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
name|security
operator|.
name|rbac
operator|.
name|Role
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
name|security
operator|.
name|rbac
operator|.
name|UserAssignment
import|;
end_import

begin_comment
comment|/**  * DefaultRoleManager:  *  * @author Jesse McConnell<jmcconnell@apache.org>  * @version $Id:$  * @plexus.component role="org.apache.maven.archiva.web.util.RoleManager"  * role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRoleManager
implements|implements
name|RoleManager
implements|,
name|Initializable
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|RBACManager
name|manager
decl_stmt|;
specifier|private
name|boolean
name|initialized
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
comment|// initialize the operations
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"add-repository"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"add-repository"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"edit-repository"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"edit-repository"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"delete-repository"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"delete-repository"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"edit-configuration"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"edit-configuration"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"run-indexer"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"run-indexer"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"regenerate-index"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"regenerate-index"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"access-reports"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"access-reports"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"generate-reports"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"generate-reports"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"edit-user"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"edit-user"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"edit-all-users"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"edit-all-users"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"grant-roles"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"grant-roles"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|operationExists
argument_list|(
literal|"remove-roles"
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|manager
operator|.
name|createOperation
argument_list|(
literal|"remove-roles"
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Edit Configuration"
argument_list|)
condition|)
block|{
name|Permission
name|editConfiguration
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Edit Configuration"
argument_list|,
literal|"edit-configuration"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|editConfiguration
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Run Indexer"
argument_list|)
condition|)
block|{
name|Permission
name|runIndexer
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Run Indexer"
argument_list|,
literal|"run-indexer"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|runIndexer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Add Repository"
argument_list|)
condition|)
block|{
name|Permission
name|runIndexer
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Add Repository"
argument_list|,
literal|"add-repository"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|runIndexer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Edit All Users"
argument_list|)
condition|)
block|{
name|Permission
name|editAllUsers
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Edit All Users"
argument_list|,
literal|"edit-all-users"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|editAllUsers
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Access Reports"
argument_list|)
condition|)
block|{
name|Permission
name|editAllUsers
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Access Reports"
argument_list|,
literal|"access-reports"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|editAllUsers
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Generate Reports"
argument_list|)
condition|)
block|{
name|Permission
name|editAllUsers
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Generate Reports"
argument_list|,
literal|"generate-reports"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|editAllUsers
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Grant Roles"
argument_list|)
condition|)
block|{
name|Permission
name|granRoles
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Grant Roles"
argument_list|,
literal|"grant-roles"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|granRoles
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Remove Roles"
argument_list|)
condition|)
block|{
name|Permission
name|removeRoles
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Remove Roles"
argument_list|,
literal|"remove-roles"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|removeRoles
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|permissionExists
argument_list|(
literal|"Regenerate Index"
argument_list|)
condition|)
block|{
name|Permission
name|regenIndex
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Regenerate Index"
argument_list|,
literal|"regenerate-index"
argument_list|,
name|manager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|.
name|savePermission
argument_list|(
name|regenIndex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|roleExists
argument_list|(
literal|"User Administrator"
argument_list|)
condition|)
block|{
name|Role
name|userAdmin
init|=
name|manager
operator|.
name|createRole
argument_list|(
literal|"User Administrator"
argument_list|)
decl_stmt|;
name|userAdmin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Edit All Users"
argument_list|)
argument_list|)
expr_stmt|;
name|userAdmin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Remove Roles"
argument_list|)
argument_list|)
expr_stmt|;
name|userAdmin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Grant Roles"
argument_list|)
argument_list|)
expr_stmt|;
name|userAdmin
operator|.
name|setAssignable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|manager
operator|.
name|saveRole
argument_list|(
name|userAdmin
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|manager
operator|.
name|roleExists
argument_list|(
literal|"System Administrator"
argument_list|)
condition|)
block|{
name|Role
name|admin
init|=
name|manager
operator|.
name|createRole
argument_list|(
literal|"System Administrator"
argument_list|)
decl_stmt|;
name|admin
operator|.
name|addChildRoleName
argument_list|(
name|manager
operator|.
name|getRole
argument_list|(
literal|"User Administrator"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Edit Configuration"
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Run Indexer"
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Add Repository"
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Access Reports"
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Generate Reports"
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Regenerate Index"
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|setAssignable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|manager
operator|.
name|saveRole
argument_list|(
name|admin
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RbacObjectNotFoundException
name|ne
parameter_list|)
block|{
name|ne
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|InitializationException
argument_list|(
literal|"error in role initialization"
argument_list|,
name|ne
argument_list|)
throw|;
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|addUser
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|RbacStoreException
block|{
comment|// make the resource
name|Resource
name|usernameResource
init|=
name|manager
operator|.
name|createResource
argument_list|(
name|principal
argument_list|)
decl_stmt|;
name|manager
operator|.
name|saveResource
argument_list|(
name|usernameResource
argument_list|)
expr_stmt|;
name|Permission
name|editUser
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Edit Myself - "
operator|+
name|principal
argument_list|,
literal|"edit-user"
argument_list|,
name|principal
argument_list|)
decl_stmt|;
name|editUser
operator|=
name|manager
operator|.
name|savePermission
argument_list|(
name|editUser
argument_list|)
expr_stmt|;
comment|// todo this one role a user will go away when we have expressions in the resources
name|Role
name|userRole
init|=
name|manager
operator|.
name|createRole
argument_list|(
literal|"Personal Role - "
operator|+
name|principal
argument_list|)
decl_stmt|;
name|userRole
operator|.
name|addPermission
argument_list|(
name|editUser
argument_list|)
expr_stmt|;
name|userRole
operator|=
name|manager
operator|.
name|saveRole
argument_list|(
name|userRole
argument_list|)
expr_stmt|;
name|UserAssignment
name|assignment
init|=
name|manager
operator|.
name|createUserAssignment
argument_list|(
name|principal
argument_list|)
decl_stmt|;
name|assignment
operator|.
name|addRole
argument_list|(
name|userRole
argument_list|)
expr_stmt|;
name|manager
operator|.
name|saveUserAssignment
argument_list|(
name|assignment
argument_list|)
expr_stmt|;
block|}
comment|/**      * helper method for just creating an admin user assignment      *      * @param principal      * @throws RbacStoreException      * @throws RbacObjectNotFoundException      */
specifier|public
name|void
name|addAdminUser
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|RbacStoreException
block|{
try|try
block|{
name|UserAssignment
name|assignment
init|=
name|manager
operator|.
name|createUserAssignment
argument_list|(
name|principal
argument_list|)
decl_stmt|;
name|assignment
operator|.
name|addRole
argument_list|(
name|manager
operator|.
name|getRole
argument_list|(
literal|"System Administrator"
argument_list|)
argument_list|)
expr_stmt|;
name|manager
operator|.
name|saveUserAssignment
argument_list|(
name|assignment
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RbacObjectNotFoundException
name|ne
parameter_list|)
block|{
throw|throw
operator|new
name|RbacStoreException
argument_list|(
literal|"unable to find administrator role, this of course is bad"
argument_list|,
name|ne
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|addRepository
parameter_list|(
name|String
name|repositoryName
parameter_list|)
throws|throws
name|RbacStoreException
block|{
try|try
block|{
comment|// make the resource
name|Resource
name|repoResource
init|=
name|manager
operator|.
name|createResource
argument_list|(
name|repositoryName
argument_list|)
decl_stmt|;
name|repoResource
operator|=
name|manager
operator|.
name|saveResource
argument_list|(
name|repoResource
argument_list|)
expr_stmt|;
comment|// make the permissions
name|Permission
name|editRepo
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Edit Repository - "
operator|+
name|repositoryName
argument_list|)
decl_stmt|;
name|editRepo
operator|.
name|setOperation
argument_list|(
name|manager
operator|.
name|getOperation
argument_list|(
literal|"edit-repository"
argument_list|)
argument_list|)
expr_stmt|;
name|editRepo
operator|.
name|setResource
argument_list|(
name|repoResource
argument_list|)
expr_stmt|;
name|editRepo
operator|=
name|manager
operator|.
name|savePermission
argument_list|(
name|editRepo
argument_list|)
expr_stmt|;
name|Permission
name|deleteRepo
init|=
name|manager
operator|.
name|createPermission
argument_list|(
literal|"Delete Repository - "
operator|+
name|repositoryName
argument_list|)
decl_stmt|;
name|deleteRepo
operator|.
name|setOperation
argument_list|(
name|manager
operator|.
name|getOperation
argument_list|(
literal|"delete-repository"
argument_list|)
argument_list|)
expr_stmt|;
name|deleteRepo
operator|.
name|setResource
argument_list|(
name|repoResource
argument_list|)
expr_stmt|;
name|deleteRepo
operator|=
name|manager
operator|.
name|savePermission
argument_list|(
name|deleteRepo
argument_list|)
expr_stmt|;
comment|// make the roles
name|Role
name|repositoryObserver
init|=
name|manager
operator|.
name|createRole
argument_list|(
literal|"Repository Observer - "
operator|+
name|repositoryName
argument_list|)
decl_stmt|;
name|repositoryObserver
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Access Reports"
argument_list|)
argument_list|)
expr_stmt|;
name|repositoryObserver
operator|.
name|setAssignable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repositoryObserver
operator|=
name|manager
operator|.
name|saveRole
argument_list|(
name|repositoryObserver
argument_list|)
expr_stmt|;
name|Role
name|repositoryManager
init|=
name|manager
operator|.
name|createRole
argument_list|(
literal|"Repository Manager - "
operator|+
name|repositoryName
argument_list|)
decl_stmt|;
name|repositoryManager
operator|.
name|addPermission
argument_list|(
name|editRepo
argument_list|)
expr_stmt|;
name|repositoryManager
operator|.
name|addPermission
argument_list|(
name|deleteRepo
argument_list|)
expr_stmt|;
name|repositoryManager
operator|.
name|addPermission
argument_list|(
name|manager
operator|.
name|getPermission
argument_list|(
literal|"Generate Reports"
argument_list|)
argument_list|)
expr_stmt|;
name|repositoryManager
operator|.
name|addChildRoleName
argument_list|(
name|repositoryObserver
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryManager
operator|.
name|setAssignable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|manager
operator|.
name|saveRole
argument_list|(
name|repositoryManager
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RbacObjectNotFoundException
name|ne
parameter_list|)
block|{
throw|throw
operator|new
name|RbacStoreException
argument_list|(
literal|"rbac object not found in repo role creation"
argument_list|,
name|ne
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|isInitialized
parameter_list|()
block|{
return|return
name|initialized
return|;
block|}
specifier|public
name|void
name|setInitialized
parameter_list|(
name|boolean
name|initialized
parameter_list|)
block|{
name|this
operator|.
name|initialized
operator|=
name|initialized
expr_stmt|;
block|}
block|}
end_class

end_unit

