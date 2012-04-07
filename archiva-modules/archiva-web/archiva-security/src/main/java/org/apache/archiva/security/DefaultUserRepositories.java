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
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|UserNotFoundException
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
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|authentication
operator|.
name|AuthenticationResult
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
name|authorization
operator|.
name|AuthorizationException
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
name|role
operator|.
name|RoleManagerException
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
name|DefaultSecuritySession
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
name|SecuritySession
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
name|stereotype
operator|.
name|Service
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
comment|/**  * DefaultUserRepositories  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"userRepositories"
argument_list|)
specifier|public
class|class
name|DefaultUserRepositories
implements|implements
name|UserRepositories
block|{
annotation|@
name|Inject
specifier|private
name|SecuritySystem
name|securitySystem
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RoleManager
name|roleManager
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
specifier|private
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
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getObservableRepositoryIds
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|PrincipalNotFoundException
throws|,
name|AccessDeniedException
throws|,
name|ArchivaSecurityException
block|{
name|String
name|operation
init|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
decl_stmt|;
return|return
name|getAccessibleRepositoryIds
argument_list|(
name|principal
argument_list|,
name|operation
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getManagableRepositoryIds
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|PrincipalNotFoundException
throws|,
name|AccessDeniedException
throws|,
name|ArchivaSecurityException
block|{
name|String
name|operation
init|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
decl_stmt|;
return|return
name|getAccessibleRepositoryIds
argument_list|(
name|principal
argument_list|,
name|operation
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getAccessibleRepositoryIds
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|operation
parameter_list|)
throws|throws
name|ArchivaSecurityException
throws|,
name|AccessDeniedException
throws|,
name|PrincipalNotFoundException
block|{
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|managedRepositories
init|=
name|getAccessibleRepositories
argument_list|(
name|principal
argument_list|,
name|operation
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repoIds
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|managedRepositories
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ManagedRepository
name|managedRepository
range|:
name|managedRepositories
control|)
block|{
name|repoIds
operator|.
name|add
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|repoIds
return|;
block|}
specifier|public
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getAccessibleRepositories
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|ArchivaSecurityException
throws|,
name|AccessDeniedException
throws|,
name|PrincipalNotFoundException
block|{
return|return
name|getAccessibleRepositories
argument_list|(
name|principal
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getAccessibleRepositories
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|operation
parameter_list|)
throws|throws
name|ArchivaSecurityException
throws|,
name|AccessDeniedException
throws|,
name|PrincipalNotFoundException
block|{
name|SecuritySession
name|securitySession
init|=
name|createSession
argument_list|(
name|principal
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|managedRepositories
init|=
operator|new
name|ArrayList
argument_list|<
name|ManagedRepository
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
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
for|for
control|(
name|ManagedRepository
name|repo
range|:
name|repos
control|)
block|{
try|try
block|{
name|String
name|repoId
init|=
name|repo
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|securitySystem
operator|.
name|isAuthorized
argument_list|(
name|securitySession
argument_list|,
name|operation
argument_list|,
name|repoId
argument_list|)
condition|)
block|{
name|managedRepositories
operator|.
name|add
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
comment|// swallow.
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Not authorizing '{}' for repository '{}': {}"
argument_list|,
name|Lists
operator|.
expr|<
name|Object
operator|>
name|newArrayList
argument_list|(
name|principal
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|managedRepositories
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaSecurityException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|SecuritySession
name|createSession
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|ArchivaSecurityException
throws|,
name|AccessDeniedException
block|{
name|User
name|user
decl_stmt|;
try|try
block|{
name|user
operator|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
operator|.
name|findUser
argument_list|(
name|principal
argument_list|)
expr_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ArchivaSecurityException
argument_list|(
literal|"The security system had an internal error - please check your system logs"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|UserNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PrincipalNotFoundException
argument_list|(
literal|"Unable to find principal "
operator|+
name|principal
operator|+
literal|""
argument_list|)
throw|;
block|}
if|if
condition|(
name|user
operator|.
name|isLocked
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AccessDeniedException
argument_list|(
literal|"User "
operator|+
name|principal
operator|+
literal|"("
operator|+
name|user
operator|.
name|getFullName
argument_list|()
operator|+
literal|") is locked."
argument_list|)
throw|;
block|}
name|AuthenticationResult
name|authn
init|=
operator|new
name|AuthenticationResult
argument_list|(
literal|true
argument_list|,
name|principal
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
operator|new
name|DefaultSecuritySession
argument_list|(
name|authn
argument_list|,
name|user
argument_list|)
return|;
block|}
specifier|public
name|void
name|createMissingRepositoryRoles
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|ArchivaSecurityException
block|{
try|try
block|{
if|if
condition|(
operator|!
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
condition|)
block|{
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|repoId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
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
condition|)
block|{
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|repoId
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RoleManagerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaSecurityException
argument_list|(
literal|"Unable to create roles for configured repositories: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|isAuthorizedToUploadArtifacts
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|PrincipalNotFoundException
throws|,
name|ArchivaSecurityException
block|{
try|try
block|{
name|SecuritySession
name|securitySession
init|=
name|createSession
argument_list|(
name|principal
argument_list|)
decl_stmt|;
return|return
name|securitySystem
operator|.
name|isAuthorized
argument_list|(
name|securitySession
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
argument_list|,
name|repoId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaSecurityException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|isAuthorizedToDeleteArtifacts
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|ArchivaSecurityException
block|{
try|try
block|{
name|SecuritySession
name|securitySession
init|=
name|createSession
argument_list|(
name|principal
argument_list|)
decl_stmt|;
return|return
name|securitySystem
operator|.
name|isAuthorized
argument_list|(
name|securitySession
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_DELETE
argument_list|,
name|repoId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaSecurityException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|SecuritySystem
name|getSecuritySystem
parameter_list|()
block|{
return|return
name|securitySystem
return|;
block|}
specifier|public
name|void
name|setSecuritySystem
parameter_list|(
name|SecuritySystem
name|securitySystem
parameter_list|)
block|{
name|this
operator|.
name|securitySystem
operator|=
name|securitySystem
expr_stmt|;
block|}
specifier|public
name|RoleManager
name|getRoleManager
parameter_list|()
block|{
return|return
name|roleManager
return|;
block|}
specifier|public
name|void
name|setRoleManager
parameter_list|(
name|RoleManager
name|roleManager
parameter_list|)
block|{
name|this
operator|.
name|roleManager
operator|=
name|roleManager
expr_stmt|;
block|}
block|}
end_class

end_unit

