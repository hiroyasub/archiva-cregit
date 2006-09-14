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
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
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
name|user
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
name|security
operator|.
name|user
operator|.
name|UserManager
import|;
end_import

begin_comment
comment|/**  * DefaultArchivaDefaults   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.web.ArchivaDefaults"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArchivaDefaults
extends|extends
name|AbstractLogEnabled
implements|implements
name|ArchivaDefaults
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|RBACManager
name|rbacManager
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|UserManager
name|userManager
decl_stmt|;
specifier|private
name|boolean
name|initialized
decl_stmt|;
specifier|private
name|User
name|guestUser
decl_stmt|;
specifier|public
name|void
name|ensureDefaultsExist
parameter_list|()
block|{
if|if
condition|(
name|initialized
condition|)
block|{
return|return;
block|}
name|ensureOperationsExist
argument_list|()
expr_stmt|;
name|ensurePermissionsExist
argument_list|()
expr_stmt|;
name|ensureRolesExist
argument_list|()
expr_stmt|;
name|ensureUsersExist
argument_list|()
expr_stmt|;
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
specifier|private
name|void
name|ensureOperationExists
parameter_list|(
name|String
name|operationName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|rbacManager
operator|.
name|operationExists
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
name|Operation
name|operation
init|=
name|rbacManager
operator|.
name|createOperation
argument_list|(
name|operationName
argument_list|)
decl_stmt|;
name|rbacManager
operator|.
name|saveOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|ensureOperationsExist
parameter_list|()
block|{
name|ensureOperationExists
argument_list|(
name|REPOSITORY_ADD_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|REPOSITORY_EDIT_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|REPOSITORY_DELETE_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|CONFIGURATION_EDIT_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|INDEX_RUN_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|INDEX_REGENERATE_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|REPORTS_ACCESS_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|REPORTS_GENERATE_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|USER_EDIT_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|USERS_EDIT_ALL_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|ROLES_GRANT_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|ROLES_REMOVE_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|REPOSITORY_ACCESS_OPERATION
argument_list|)
expr_stmt|;
name|ensureOperationExists
argument_list|(
name|REPOSITORY_UPLOAD_OPERATION
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|ensurePermissionExists
parameter_list|(
name|String
name|permissionName
parameter_list|,
name|String
name|operationName
parameter_list|,
name|String
name|resourceIdentifier
parameter_list|)
block|{
if|if
condition|(
operator|!
name|rbacManager
operator|.
name|permissionExists
argument_list|(
name|permissionName
argument_list|)
condition|)
block|{
name|Permission
name|editConfiguration
init|=
name|rbacManager
operator|.
name|createPermission
argument_list|(
name|permissionName
argument_list|,
name|operationName
argument_list|,
name|resourceIdentifier
argument_list|)
decl_stmt|;
name|rbacManager
operator|.
name|savePermission
argument_list|(
name|editConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|ensurePermissionsExist
parameter_list|()
block|{
name|String
name|globalResource
init|=
name|rbacManager
operator|.
name|getGlobalResource
argument_list|()
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
name|ensurePermissionExists
argument_list|(
name|USERS_EDIT_ALL_PERMISSION
argument_list|,
name|USERS_EDIT_ALL_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|CONFIGURATION_EDIT_PERMISSION
argument_list|,
name|CONFIGURATION_EDIT_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|ROLES_GRANT_PERMISSION
argument_list|,
name|ROLES_GRANT_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|ROLES_REMOVE_PERMISSION
argument_list|,
name|ROLES_REMOVE_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|REPORTS_ACCESS_PERMISSION
argument_list|,
name|REPORTS_ACCESS_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|REPORTS_GENERATE_PERMISSION
argument_list|,
name|REPORTS_GENERATE_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|INDEX_RUN_PERMISSION
argument_list|,
name|INDEX_RUN_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|INDEX_REGENERATE_PERMISSION
argument_list|,
name|INDEX_REGENERATE_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|REPOSITORY_ADD_PERMISSION
argument_list|,
name|REPOSITORY_ADD_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|REPOSITORY_ACCESS
argument_list|,
literal|"access-repository"
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
name|ensurePermissionExists
argument_list|(
name|REPOSITORY_UPLOAD
argument_list|,
name|REPOSITORY_UPLOAD_OPERATION
argument_list|,
name|globalResource
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|ensureRolesExist
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|!
name|rbacManager
operator|.
name|roleExists
argument_list|(
name|USER_ADMINISTRATOR
argument_list|)
condition|)
block|{
name|Role
name|userAdmin
init|=
name|rbacManager
operator|.
name|createRole
argument_list|(
name|USER_ADMINISTRATOR
argument_list|)
decl_stmt|;
name|userAdmin
operator|.
name|addPermission
argument_list|(
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|USERS_EDIT_ALL_PERMISSION
argument_list|)
argument_list|)
expr_stmt|;
name|userAdmin
operator|.
name|addPermission
argument_list|(
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|ROLES_REMOVE_PERMISSION
argument_list|)
argument_list|)
expr_stmt|;
name|userAdmin
operator|.
name|addPermission
argument_list|(
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|ROLES_GRANT_PERMISSION
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
name|rbacManager
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
name|rbacManager
operator|.
name|roleExists
argument_list|(
name|SYSTEM_ADMINISTRATOR
argument_list|)
condition|)
block|{
name|Role
name|admin
init|=
name|rbacManager
operator|.
name|createRole
argument_list|(
name|SYSTEM_ADMINISTRATOR
argument_list|)
decl_stmt|;
name|admin
operator|.
name|addChildRoleName
argument_list|(
name|rbacManager
operator|.
name|getRole
argument_list|(
name|USER_ADMINISTRATOR
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
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|CONFIGURATION_EDIT_PERMISSION
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|INDEX_RUN_PERMISSION
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|REPOSITORY_ADD_PERMISSION
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|REPORTS_ACCESS_PERMISSION
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|REPORTS_GENERATE_PERMISSION
argument_list|)
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addPermission
argument_list|(
name|rbacManager
operator|.
name|getPermission
argument_list|(
name|INDEX_REGENERATE_PERMISSION
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
name|rbacManager
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
name|getLogger
argument_list|()
operator|.
name|fatalError
argument_list|(
literal|"Unable to initialize Roles!"
argument_list|,
name|ne
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"All Mandatory Defaults do not Exist!"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|ensureUsersExist
parameter_list|()
block|{
if|if
condition|(
operator|!
name|userManager
operator|.
name|userExists
argument_list|(
name|GUEST_USERNAME
argument_list|)
condition|)
block|{
name|this
operator|.
name|guestUser
operator|=
name|userManager
operator|.
name|createUser
argument_list|(
name|GUEST_USERNAME
argument_list|,
literal|"Guest User"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|this
operator|.
name|guestUser
operator|=
name|userManager
operator|.
name|addUser
argument_list|(
name|this
operator|.
name|guestUser
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|User
name|getGuestUser
parameter_list|()
block|{
return|return
name|this
operator|.
name|guestUser
return|;
block|}
block|}
end_class

end_unit

