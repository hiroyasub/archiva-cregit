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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|codehaus
operator|.
name|plexus
operator|.
name|redback
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
name|UserNotFoundException
import|;
end_import

begin_comment
comment|/**  * DefaultUserRepositories  *   * @version $Id$  * @plexus.component role="org.apache.maven.archiva.security.UserRepositories" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultUserRepositories
implements|implements
name|UserRepositories
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|SecuritySystem
name|securitySystem
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="cached"      */
specifier|private
name|RBACManager
name|rbacManager
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|RoleManager
name|roleManager
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
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
try|try
block|{
name|User
name|user
init|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
operator|.
name|findUser
argument_list|(
name|principal
argument_list|)
decl_stmt|;
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
name|SecuritySession
name|securitySession
init|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|authn
argument_list|,
name|user
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
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|repos
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepositoryConfiguration
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
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
argument_list|,
name|repoId
argument_list|)
condition|)
block|{
name|repoIds
operator|.
name|add
argument_list|(
name|repoId
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
block|}
block|}
return|return
name|repoIds
return|;
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
name|User
name|user
init|=
name|securitySystem
operator|.
name|getUserManager
argument_list|()
operator|.
name|findUser
argument_list|(
name|principal
argument_list|)
decl_stmt|;
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
name|SecuritySession
name|securitySession
init|=
operator|new
name|DefaultSecuritySession
argument_list|(
name|authn
argument_list|,
name|user
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
name|RbacManagerException
throws|,
name|RbacObjectNotFoundException
block|{
name|boolean
name|isAuthorized
init|=
literal|false
decl_stmt|;
name|String
name|delimiter
init|=
literal|" - "
decl_stmt|;
try|try
block|{
name|Collection
name|roleList
init|=
name|rbacManager
operator|.
name|getEffectivelyAssignedRoles
argument_list|(
name|principal
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|roleList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Role
name|role
init|=
operator|(
name|Role
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|roleName
init|=
name|role
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|roleName
operator|.
name|startsWith
argument_list|(
name|ArchivaRoleConstants
operator|.
name|REPOSITORY_MANAGER_ROLE_PREFIX
argument_list|)
condition|)
block|{
name|int
name|delimiterIndex
init|=
name|roleName
operator|.
name|indexOf
argument_list|(
name|delimiter
argument_list|)
decl_stmt|;
name|String
name|resourceName
init|=
name|roleName
operator|.
name|substring
argument_list|(
name|delimiterIndex
operator|+
name|delimiter
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceName
operator|.
name|equals
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
name|isAuthorized
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RbacObjectNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RbacObjectNotFoundException
argument_list|(
literal|"Unable to find user "
operator|+
name|principal
operator|+
literal|""
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RbacManagerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RbacManagerException
argument_list|(
literal|"Unable to get roles for user "
operator|+
name|principal
operator|+
literal|""
argument_list|)
throw|;
block|}
return|return
name|isAuthorized
return|;
block|}
block|}
end_class

end_unit

