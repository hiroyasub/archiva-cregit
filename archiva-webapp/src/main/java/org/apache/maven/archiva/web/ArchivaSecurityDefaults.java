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
name|security
operator|.
name|user
operator|.
name|User
import|;
end_import

begin_comment
comment|/**  * ArchivaSecurityDefaults  *  * NOTE: this is targeted for removal with the forth coming rbac role templating   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArchivaSecurityDefaults
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|ArchivaSecurityDefaults
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GUEST_ROLE
init|=
literal|"Guest Role"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GUEST_USERNAME
init|=
literal|"guest"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONFIGURATION_EDIT_OPERATION
init|=
literal|"edit-configuration"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONFIGURATION_EDIT_PERMISSION
init|=
literal|"Edit Configuration"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INDEX_REGENERATE_OPERATION
init|=
literal|"regenerate-index"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INDEX_REGENERATE_PERMISSION
init|=
literal|"Regenerate Index"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INDEX_RUN_OPERATION
init|=
literal|"run-indexer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INDEX_RUN_PERMISSION
init|=
literal|"Run Indexer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPORTS_ACCESS_OPERATION
init|=
literal|"access-reports"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPORTS_ACCESS_PERMISSION
init|=
literal|"Access Reports"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPORTS_GENERATE_OPERATION
init|=
literal|"generate-reports"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPORTS_GENERATE_PERMISSION
init|=
literal|"Generate Reports"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_ACCESS
init|=
literal|"Access Repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_ACCESS_OPERATION
init|=
literal|"read-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_ADD_OPERATION
init|=
literal|"add-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_ADD_PERMISSION
init|=
literal|"Add Repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_DELETE
init|=
literal|"Delete Repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_DELETE_OPERATION
init|=
literal|"delete-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_EDIT
init|=
literal|"Edit Repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_EDIT_OPERATION
init|=
literal|"edit-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_MANAGER
init|=
literal|"Repository Manager"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_OBSERVER
init|=
literal|"Repository Observer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_UPLOAD
init|=
literal|"Repository Upload"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_UPLOAD_OPERATION
init|=
literal|"upload-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLES_GRANT_OPERATION
init|=
literal|"grant-roles"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLES_GRANT_PERMISSION
init|=
literal|"Grant Roles"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLES_REMOVE_OPERATION
init|=
literal|"remove-roles"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLES_REMOVE_PERMISSION
init|=
literal|"Remove Roles"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SYSTEM_ADMINISTRATOR
init|=
literal|"System Administrator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_ADMINISTRATOR
init|=
literal|"User Administrator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_EDIT_OPERATION
init|=
literal|"edit-user"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USERS_EDIT_ALL_OPERATION
init|=
literal|"edit-all-users"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USERS_EDIT_ALL_PERMISSION
init|=
literal|"Edit All Users"
decl_stmt|;
specifier|public
name|void
name|ensureDefaultsExist
parameter_list|()
function_decl|;
specifier|public
name|User
name|getGuestUser
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

