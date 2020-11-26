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
operator|.
name|common
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaRoleConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DELIMITER
init|=
literal|" - "
decl_stmt|;
comment|// globalish roles
specifier|public
specifier|static
specifier|final
name|String
name|SYSTEM_ADMINISTRATOR_ROLE
init|=
literal|"System Administrator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_ADMINISTRATOR_ROLE
init|=
literal|"User Administrator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GLOBAL_REPOSITORY_MANAGER_ROLE
init|=
literal|"Global Repository Manager"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GLOBAL_REPOSITORY_OBSERVER_ROLE
init|=
literal|"Global Repository Observer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REGISTERED_USER_ROLE
init|=
literal|"Registered User"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GUEST_ROLE
init|=
literal|"Guest"
decl_stmt|;
comment|// dynamic role prefixes
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_MANAGER_ROLE_PREFIX
init|=
literal|"Repository Manager"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_OBSERVER_ROLE_PREFIX
init|=
literal|"Repository Observer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_OBSERVER_ROLE_ID_PREFIX
init|=
literal|"archiva-repository-observer"
decl_stmt|;
comment|// operations
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_MANAGE_USERS
init|=
literal|"archiva-manage-users"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_MANAGE_CONFIGURATION
init|=
literal|"archiva-manage-configuration"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_ACTIVE_GUEST
init|=
literal|"archiva-guest"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_RUN_INDEXER
init|=
literal|"archiva-run-indexer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_REGENERATE_INDEX
init|=
literal|"archiva-regenerate-index"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_ACCESS_REPORT
init|=
literal|"archiva-access-reports"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_ADD_REPOSITORY
init|=
literal|"archiva-add-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_REPOSITORY_ACCESS
init|=
literal|"archiva-read-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_DELETE_REPOSITORY
init|=
literal|"archiva-delete-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_EDIT_REPOSITORY
init|=
literal|"archiva-edit-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_REPOSITORY_UPLOAD
init|=
literal|"archiva-upload-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_FILE_UPLOAD
init|=
literal|"archiva-upload-file"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_REPOSITORY_DELETE
init|=
literal|"archiva-delete-artifact"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_MERGE_REPOSITORY
init|=
literal|"archiva-merge-repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_VIEW_AUDIT_LOG
init|=
literal|"archiva-view-audit-logs"
decl_stmt|;
comment|// Role templates
specifier|public
specifier|static
specifier|final
name|String
name|TEMPLATE_REPOSITORY_MANAGER
init|=
literal|"archiva-repository-manager"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEMPLATE_REPOSITORY_OBSERVER
init|=
literal|"archiva-repository-observer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEMPLATE_GLOBAL_REPOSITORY_OBSERVER
init|=
literal|"archiva-global-repository-observer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEMPLATE_SYSTEM_ADMIN
init|=
literal|"archiva-system-administrator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEMPLATE_GUEST
init|=
literal|"archiva-guest"
decl_stmt|;
specifier|public
specifier|static
name|String
name|toRepositoryObserverRoleName
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
return|return
name|REPOSITORY_OBSERVER_ROLE_PREFIX
operator|+
literal|" - "
operator|+
name|repoId
return|;
block|}
specifier|public
specifier|static
name|String
name|toRepositoryObserverRoleId
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
return|return
name|REPOSITORY_OBSERVER_ROLE_ID_PREFIX
operator|+
literal|"."
operator|+
name|repoId
return|;
block|}
block|}
end_class

end_unit

