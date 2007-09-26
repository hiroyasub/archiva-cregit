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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|Configuration
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
comment|/**  * Abstract ManagedRepositories Action.  *   * Place for all generic methods used in Managed Repository Administration.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractManagedRepositoriesAction
extends|extends
name|AbstractRepositoriesAdminAction
block|{
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|protected
name|RoleManager
name|roleManager
decl_stmt|;
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
specifier|protected
name|void
name|addRepository
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|IOException
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
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|repository
operator|.
name|setLocation
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
block|}
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"unable to add repository - can not create the root directory: "
operator|+
name|file
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addRepositoryRoles
parameter_list|(
name|ManagedRepositoryConfiguration
name|newRepository
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|// TODO: double check these are configured on start up
comment|// TODO: belongs in the business logic
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
literal|"archiva-repository-manager"
argument_list|,
name|newRepository
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
name|newRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeContents
parameter_list|(
name|ManagedRepositoryConfiguration
name|existingRepository
parameter_list|)
throws|throws
name|IOException
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|existingRepository
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeRepository
parameter_list|(
name|String
name|repoId
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|toremove
init|=
name|configuration
operator|.
name|findManagedRepositoryById
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
name|configuration
operator|.
name|removeManagedRepository
argument_list|(
name|toremove
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|removeRepositoryRoles
parameter_list|(
name|ManagedRepositoryConfiguration
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
name|debug
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
block|}
end_class

end_unit

