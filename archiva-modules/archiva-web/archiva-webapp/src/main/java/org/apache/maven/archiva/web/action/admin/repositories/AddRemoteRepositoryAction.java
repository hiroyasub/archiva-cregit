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
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Preparable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Validateable
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
name|remote
operator|.
name|RemoteRepository
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
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
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
name|Controller
import|;
end_import

begin_comment
comment|/**  * AddRemoteRepositoryAction  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"addRemoteRepositoryAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|AddRemoteRepositoryAction
extends|extends
name|AbstractRemoteRepositoriesAction
implements|implements
name|Preparable
implements|,
name|Validateable
block|{
comment|/**      * The model for this action.      */
specifier|private
name|RemoteRepository
name|repository
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|this
operator|.
name|repository
operator|=
operator|new
name|RemoteRepository
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|input
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|commit
parameter_list|()
block|{
name|String
name|result
init|=
name|SUCCESS
decl_stmt|;
try|try
block|{
name|getRemoteRepositoryAdmin
argument_list|()
operator|.
name|addRemoteRepository
argument_list|(
name|repository
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"RepositoryAdminException: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|INPUT
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|// FIXME olamy dupe with admin repo component
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|String
name|repoId
init|=
name|repository
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
name|addFieldError
argument_list|(
literal|"repository.id"
argument_list|,
literal|"Unable to add new repository with id ["
operator|+
name|repoId
operator|+
literal|"], that id already exists as a managed repository."
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|config
operator|.
name|getRemoteRepositoriesAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
name|addFieldError
argument_list|(
literal|"repository.id"
argument_list|,
literal|"Unable to add new repository with id ["
operator|+
name|repoId
operator|+
literal|"], that id already exists as a remote repository."
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|config
operator|.
name|getRepositoryGroupsAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
name|addFieldError
argument_list|(
literal|"repository.id"
argument_list|,
literal|"Unable to add new repository with id ["
operator|+
name|repoId
operator|+
literal|"], that id already exists as a repository group."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|RemoteRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|RemoteRepository
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
block|}
end_class

end_unit

