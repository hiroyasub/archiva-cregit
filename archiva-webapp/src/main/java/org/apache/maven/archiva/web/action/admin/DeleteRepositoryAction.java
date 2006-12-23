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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|configuration
operator|.
name|AbstractRepositoryConfiguration
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
name|RepositoryConfiguration
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
name|rbac
operator|.
name|profile
operator|.
name|RoleProfileException
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
name|util
operator|.
name|FileUtils
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
comment|/**  * Configures the application repositories.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="deleteRepositoryAction"  */
end_comment

begin_class
specifier|public
class|class
name|DeleteRepositoryAction
extends|extends
name|AbstractDeleteRepositoryAction
block|{
specifier|protected
name|AbstractRepositoryConfiguration
name|getRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
return|return
name|configuration
operator|.
name|getRepositoryById
argument_list|(
name|repoId
argument_list|)
return|;
block|}
specifier|protected
name|void
name|removeRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|AbstractRepositoryConfiguration
name|existingRepository
parameter_list|)
block|{
name|configuration
operator|.
name|removeRepository
argument_list|(
operator|(
name|RepositoryConfiguration
operator|)
name|existingRepository
argument_list|)
expr_stmt|;
try|try
block|{
name|removeRepositoryRoles
argument_list|(
name|existingRepository
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RoleProfileException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Error removing user roles associated with repository "
operator|+
name|existingRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|removeContents
parameter_list|(
name|AbstractRepositoryConfiguration
name|existingRepository
parameter_list|)
throws|throws
name|IOException
block|{
name|RepositoryConfiguration
name|repository
init|=
operator|(
name|RepositoryConfiguration
operator|)
name|existingRepository
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Removing "
operator|+
name|repository
operator|.
name|getDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repository
operator|.
name|getDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Remove user roles associated with the repository      *      * @param existingRepository      * @throws RoleProfileException      */
specifier|private
name|void
name|removeRepositoryRoles
parameter_list|(
name|AbstractRepositoryConfiguration
name|existingRepository
parameter_list|)
throws|throws
name|RoleProfileException
block|{
name|roleProfileManager
operator|.
name|deleteDynamicRole
argument_list|(
literal|"archiva-repository-manager"
argument_list|,
name|existingRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|roleProfileManager
operator|.
name|deleteDynamicRole
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
block|}
end_class

end_unit

