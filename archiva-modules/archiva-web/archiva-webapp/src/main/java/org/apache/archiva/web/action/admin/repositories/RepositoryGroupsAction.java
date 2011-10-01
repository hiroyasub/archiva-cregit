begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|beans
operator|.
name|RepositoryGroup
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
name|group
operator|.
name|RepositoryGroupAdmin
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
name|web
operator|.
name|util
operator|.
name|ContextUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|interceptor
operator|.
name|ServletRequestAware
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * RepositoryGroupsAction  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"repositoryGroupsAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|RepositoryGroupsAction
extends|extends
name|AbstractRepositoriesAdminAction
implements|implements
name|ServletRequestAware
implements|,
name|Preparable
block|{
annotation|@
name|Inject
specifier|private
name|RepositoryGroupAdmin
name|repositoryGroupAdmin
decl_stmt|;
specifier|private
name|RepositoryGroup
name|repositoryGroup
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|repositoryGroups
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
name|managedRepositories
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|groupToRepositoryMap
decl_stmt|;
specifier|private
name|String
name|repoGroupId
decl_stmt|;
specifier|private
name|String
name|repoId
decl_stmt|;
comment|/**      * Used to construct the repository WebDAV URL in the repository action.      */
specifier|private
name|String
name|baseUrl
decl_stmt|;
specifier|public
name|void
name|setServletRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|this
operator|.
name|baseUrl
operator|=
name|ContextUtils
operator|.
name|getBaseURL
argument_list|(
name|request
argument_list|,
literal|"repository"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|repositoryGroup
operator|=
operator|new
name|RepositoryGroup
argument_list|()
expr_stmt|;
name|repositoryGroups
operator|=
name|getRepositoryGroupAdmin
argument_list|()
operator|.
name|getRepositoryGroupsAsMap
argument_list|()
expr_stmt|;
name|managedRepositories
operator|=
name|getManagedRepositoryAdmin
argument_list|()
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
expr_stmt|;
name|groupToRepositoryMap
operator|=
name|getRepositoryGroupAdmin
argument_list|()
operator|.
name|getGroupToRepositoryMap
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|addRepositoryGroup
parameter_list|()
block|{
try|try
block|{
name|getRepositoryGroupAdmin
argument_list|()
operator|.
name|addRepositoryGroup
argument_list|(
name|repositoryGroup
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|addRepositoryToGroup
parameter_list|()
block|{
try|try
block|{
name|getRepositoryGroupAdmin
argument_list|()
operator|.
name|addRepositoryToGroup
argument_list|(
name|repoGroupId
argument_list|,
name|repoId
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|removeRepositoryFromGroup
parameter_list|()
block|{
try|try
block|{
name|getRepositoryGroupAdmin
argument_list|()
operator|.
name|deleteRepositoryFromGroup
argument_list|(
name|repoGroupId
argument_list|,
name|repoId
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|RepositoryGroup
name|getRepositoryGroup
parameter_list|()
block|{
return|return
name|repositoryGroup
return|;
block|}
specifier|public
name|void
name|setRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
block|{
name|this
operator|.
name|repositoryGroup
operator|=
name|repositoryGroup
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|getRepositoryGroups
parameter_list|()
block|{
return|return
name|repositoryGroups
return|;
block|}
specifier|public
name|void
name|setRepositoryGroups
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|repositoryGroups
parameter_list|)
block|{
name|this
operator|.
name|repositoryGroups
operator|=
name|repositoryGroups
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
name|getManagedRepositories
parameter_list|()
block|{
return|return
name|managedRepositories
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getGroupToRepositoryMap
parameter_list|()
block|{
return|return
name|this
operator|.
name|groupToRepositoryMap
return|;
block|}
specifier|public
name|String
name|getRepoGroupId
parameter_list|()
block|{
return|return
name|repoGroupId
return|;
block|}
specifier|public
name|void
name|setRepoGroupId
parameter_list|(
name|String
name|repoGroupId
parameter_list|)
block|{
name|this
operator|.
name|repoGroupId
operator|=
name|repoGroupId
expr_stmt|;
block|}
specifier|public
name|String
name|getRepoId
parameter_list|()
block|{
return|return
name|repoId
return|;
block|}
specifier|public
name|void
name|setRepoId
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
name|this
operator|.
name|repoId
operator|=
name|repoId
expr_stmt|;
block|}
specifier|public
name|String
name|getBaseUrl
parameter_list|()
block|{
return|return
name|baseUrl
return|;
block|}
specifier|public
name|RepositoryGroupAdmin
name|getRepositoryGroupAdmin
parameter_list|()
block|{
return|return
name|repositoryGroupAdmin
return|;
block|}
specifier|public
name|void
name|setRepositoryGroupAdmin
parameter_list|(
name|RepositoryGroupAdmin
name|repositoryGroupAdmin
parameter_list|)
block|{
name|this
operator|.
name|repositoryGroupAdmin
operator|=
name|repositoryGroupAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

