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
name|RepositoryGroupConfiguration
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
name|repository
operator|.
name|audit
operator|.
name|AuditEvent
import|;
end_import

begin_comment
comment|/**  * DeleteRepositoryGroupAction  *   * @version  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="deleteRepositoryGroupAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|DeleteRepositoryGroupAction
extends|extends
name|AbstractRepositoriesAdminAction
implements|implements
name|Preparable
block|{
specifier|private
name|RepositoryGroupConfiguration
name|repositoryGroup
decl_stmt|;
specifier|private
name|String
name|repoGroupId
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|repoGroupId
argument_list|)
condition|)
block|{
name|this
operator|.
name|repositoryGroup
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findRepositoryGroupById
argument_list|(
name|repoGroupId
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|confirmDelete
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repoGroupId
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete repository group: repository id was blank."
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
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|RepositoryGroupConfiguration
name|group
init|=
name|config
operator|.
name|findRepositoryGroupById
argument_list|(
name|repoGroupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"A repository group with that id does not exist."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
name|config
operator|.
name|removeRepositoryGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|AuditEvent
operator|.
name|DELETE_REPO_GROUP
operator|+
literal|" "
operator|+
name|repoGroupId
argument_list|)
expr_stmt|;
return|return
name|saveConfiguration
argument_list|(
name|config
argument_list|)
return|;
block|}
specifier|public
name|RepositoryGroupConfiguration
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
name|RepositoryGroupConfiguration
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
block|}
end_class

end_unit

