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
name|action
operator|.
name|admin
operator|.
name|repositories
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|ActionContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|Preparable
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
name|common
operator|.
name|utils
operator|.
name|PathUtil
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
name|InvalidConfigurationException
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
name|RepositoryConfiguration
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
name|authorization
operator|.
name|AuthorizationResult
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
name|xwork
operator|.
name|interceptor
operator|.
name|SecureAction
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
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionBundle
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
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionException
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
name|registry
operator|.
name|RegistryException
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
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Configures the application repositories.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="configureRepositoryAction"  */
end_comment

begin_class
specifier|public
class|class
name|ConfigureRepositoryAction
extends|extends
name|PlexusActionSupport
implements|implements
name|Preparable
implements|,
name|SecureAction
block|{
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|RoleManager
name|roleManager
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|SecuritySystem
name|securitySystem
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|String
name|repoid
decl_stmt|;
specifier|private
name|String
name|mode
decl_stmt|;
comment|/**      * The model for this action.      */
specifier|private
name|AdminRepositoryConfiguration
name|repository
decl_stmt|;
specifier|public
name|String
name|add
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".add()"
argument_list|)
expr_stmt|;
name|this
operator|.
name|mode
operator|=
literal|"add"
expr_stmt|;
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|confirm
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".confirm()"
argument_list|)
expr_stmt|;
if|if
condition|(
name|operationAllowed
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_DELETE_REPOSITORY
argument_list|,
name|getRepoid
argument_list|()
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"You do not have the appropriate permissions to delete the "
operator|+
name|getRepoid
argument_list|()
operator|+
literal|" repository."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|delete
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".delete()"
argument_list|)
expr_stmt|;
if|if
condition|(
name|operationAllowed
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_DELETE_REPOSITORY
argument_list|,
name|getRepoid
argument_list|()
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"You do not have the appropriate permissions to delete the "
operator|+
name|getRepoid
argument_list|()
operator|+
literal|" repository."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|mode
argument_list|,
literal|"delete-entry"
argument_list|)
operator|||
name|StringUtils
operator|.
name|equals
argument_list|(
name|mode
argument_list|,
literal|"delete-contents"
argument_list|)
condition|)
block|{
name|AdminRepositoryConfiguration
name|existingRepository
init|=
name|getRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|existingRepository
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"A repository with that id does not exist"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
comment|// TODO: remove from index too!
try|try
block|{
name|removeRepository
argument_list|(
name|getRepoid
argument_list|()
argument_list|)
expr_stmt|;
name|removeRepositoryRoles
argument_list|(
name|existingRepository
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|mode
argument_list|,
literal|"delete-contents"
argument_list|)
condition|)
block|{
name|removeContents
argument_list|(
name|existingRepository
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete repository: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RoleManagerException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete repository: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidConfigurationException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete repository: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete repository: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|edit
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".edit()"
argument_list|)
expr_stmt|;
name|this
operator|.
name|mode
operator|=
literal|"edit"
expr_stmt|;
if|if
condition|(
name|operationAllowed
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_EDIT_REPOSITORY
argument_list|,
name|getRepoid
argument_list|()
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"You do not have the appropriate permissions to edit the "
operator|+
name|getRepoid
argument_list|()
operator|+
literal|" repository."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|getMode
parameter_list|()
block|{
return|return
name|this
operator|.
name|mode
return|;
block|}
specifier|public
name|String
name|getRepoid
parameter_list|()
block|{
return|return
name|repoid
return|;
block|}
specifier|public
name|AdminRepositoryConfiguration
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|SecureActionBundle
name|getSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|SecureActionBundle
name|bundle
init|=
operator|new
name|SecureActionBundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|setRequiresAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|addRequiredAuthorization
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|,
name|Resource
operator|.
name|GLOBAL
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|getRepoid
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|repository
operator|=
operator|new
name|AdminRepositoryConfiguration
argument_list|()
expr_stmt|;
block|}
name|RepositoryConfiguration
name|repoconfig
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findRepositoryById
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|repoconfig
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|repository
operator|=
operator|new
name|AdminRepositoryConfiguration
argument_list|(
name|repoconfig
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|save
parameter_list|()
block|{
name|String
name|mode
init|=
name|getMode
argument_list|()
decl_stmt|;
name|String
name|repoId
init|=
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".save("
operator|+
name|mode
operator|+
literal|":"
operator|+
name|repoId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|addFieldError
argument_list|(
literal|"id"
argument_list|,
literal|"A repository with a blank id cannot be saved."
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"edit"
argument_list|,
name|mode
argument_list|)
condition|)
block|{
name|removeRepository
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|addRepository
argument_list|(
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"I/O Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RoleManagerException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Role Manager Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidConfigurationException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Invalid Configuration Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Configuration Registry Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|void
name|setMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
specifier|public
name|void
name|setRepoid
parameter_list|(
name|String
name|repoid
parameter_list|)
block|{
name|this
operator|.
name|repoid
operator|=
name|repoid
expr_stmt|;
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|AdminRepositoryConfiguration
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
specifier|private
name|void
name|addRepository
parameter_list|(
name|AdminRepositoryConfiguration
name|repository
parameter_list|)
throws|throws
name|IOException
throws|,
name|RoleManagerException
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".addRepository("
operator|+
name|repository
operator|+
literal|")"
argument_list|)
expr_stmt|;
comment|// Fix the URL entry (could possibly be a filesystem path)
name|String
name|rawUrlEntry
init|=
name|repository
operator|.
name|getUrl
argument_list|()
decl_stmt|;
name|repository
operator|.
name|setUrl
argument_list|(
name|PathUtil
operator|.
name|toUrl
argument_list|(
name|rawUrlEntry
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|repository
operator|.
name|isManaged
argument_list|()
condition|)
block|{
comment|// Normalize the path
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getDirectory
argument_list|()
argument_list|)
decl_stmt|;
name|repository
operator|.
name|setDirectory
argument_list|(
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
comment|// TODO: error handling when this fails, or is not a directory!
block|}
block|}
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
comment|// TODO: double check these are configured on start up
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
literal|"archiva-repository-manager"
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
literal|"archiva-repository-observer"
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|operationAllowed
parameter_list|(
name|String
name|permission
parameter_list|,
name|String
name|repoid
parameter_list|)
block|{
name|ActionContext
name|context
init|=
name|ActionContext
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|SecuritySession
name|securitySession
init|=
operator|(
name|SecuritySession
operator|)
name|context
operator|.
name|get
argument_list|(
name|SecuritySession
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|AuthorizationResult
name|authzResult
decl_stmt|;
try|try
block|{
name|authzResult
operator|=
name|securitySystem
operator|.
name|authorize
argument_list|(
name|securitySession
argument_list|,
name|permission
argument_list|,
name|repoid
argument_list|)
expr_stmt|;
return|return
name|authzResult
operator|.
name|isAuthorized
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Unable to authorize permission: "
operator|+
name|permission
operator|+
literal|" against repo: "
operator|+
name|repoid
operator|+
literal|" due to: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|void
name|removeContents
parameter_list|(
name|AdminRepositoryConfiguration
name|existingRepository
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|existingRepository
operator|.
name|isManaged
argument_list|()
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Removing "
operator|+
name|existingRepository
operator|.
name|getDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|existingRepository
operator|.
name|getDirectory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|removeRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".removeRepository()"
argument_list|)
expr_stmt|;
name|RepositoryConfiguration
name|toremove
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findRepositoryById
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
if|if
condition|(
name|toremove
operator|!=
literal|null
condition|)
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|removeRepository
argument_list|(
name|toremove
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|removeRepositoryRoles
parameter_list|(
name|RepositoryConfiguration
name|existingRepository
parameter_list|)
throws|throws
name|RoleManagerException
block|{
name|roleManager
operator|.
name|removeTemplatedRole
argument_list|(
literal|"archiva-repository-manager"
argument_list|,
name|existingRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|roleManager
operator|.
name|removeTemplatedRole
argument_list|(
literal|"archiva-repository-observer"
argument_list|,
name|existingRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"removed user roles associated with repository "
operator|+
name|existingRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|saveConfiguration
parameter_list|()
throws|throws
name|IOException
throws|,
name|InvalidConfigurationException
throws|,
name|RegistryException
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".saveConfiguration()"
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Successfully saved configuration"
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
block|}
end_class

end_unit

