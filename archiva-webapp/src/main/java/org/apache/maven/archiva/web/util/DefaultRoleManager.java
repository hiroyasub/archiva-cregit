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
comment|/*  * Copyright 2001-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|web
operator|.
name|ArchivaDefaults
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
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaDefaults
name|archivaDefaults
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
name|archivaDefaults
operator|.
name|ensureDefaultsExist
argument_list|()
expr_stmt|;
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
name|String
name|personalRoleName
init|=
literal|"Personal Role - "
operator|+
name|principal
decl_stmt|;
name|Role
name|userRole
init|=
name|manager
operator|.
name|createRole
argument_list|(
name|personalRoleName
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
name|addRoleName
argument_list|(
name|personalRoleName
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
name|addRoleName
argument_list|(
name|ArchivaDefaults
operator|.
name|SYSTEM_ADMINISTRATOR
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
name|ArchivaDefaults
operator|.
name|REPOSITORY_EDIT
operator|+
literal|" - "
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
name|ArchivaDefaults
operator|.
name|REPOSITORY_EDIT_OPERATION
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
name|ArchivaDefaults
operator|.
name|REPOSITORY_DELETE
operator|+
literal|" - "
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
name|ArchivaDefaults
operator|.
name|REPOSITORY_DELETE_OPERATION
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
name|Permission
name|accessRepo
init|=
name|manager
operator|.
name|createPermission
argument_list|(
name|ArchivaDefaults
operator|.
name|REPOSITORY_ACCESS
operator|+
literal|" - "
operator|+
name|repositoryName
argument_list|)
decl_stmt|;
name|accessRepo
operator|.
name|setOperation
argument_list|(
name|manager
operator|.
name|getOperation
argument_list|(
name|ArchivaDefaults
operator|.
name|REPOSITORY_ACCESS_OPERATION
argument_list|)
argument_list|)
expr_stmt|;
name|accessRepo
operator|.
name|setResource
argument_list|(
name|repoResource
argument_list|)
expr_stmt|;
name|accessRepo
operator|=
name|manager
operator|.
name|savePermission
argument_list|(
name|accessRepo
argument_list|)
expr_stmt|;
name|Permission
name|uploadRepo
init|=
name|manager
operator|.
name|createPermission
argument_list|(
name|ArchivaDefaults
operator|.
name|REPOSITORY_UPLOAD
operator|+
literal|" - "
operator|+
name|repositoryName
argument_list|)
decl_stmt|;
name|uploadRepo
operator|.
name|setOperation
argument_list|(
name|manager
operator|.
name|getOperation
argument_list|(
name|ArchivaDefaults
operator|.
name|REPOSITORY_UPLOAD_OPERATION
argument_list|)
argument_list|)
expr_stmt|;
name|uploadRepo
operator|.
name|setResource
argument_list|(
name|repoResource
argument_list|)
expr_stmt|;
name|uploadRepo
operator|=
name|manager
operator|.
name|savePermission
argument_list|(
name|uploadRepo
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
name|ArchivaDefaults
operator|.
name|REPORTS_ACCESS_PERMISSION
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
name|accessRepo
argument_list|)
expr_stmt|;
name|repositoryManager
operator|.
name|addPermission
argument_list|(
name|uploadRepo
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
name|ArchivaDefaults
operator|.
name|REPORTS_GENERATE_PERMISSION
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

