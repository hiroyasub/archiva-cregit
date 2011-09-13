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
comment|/**  * DeleteRemoteRepositoryAction  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"deleteRemoteRepositoryAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|DeleteRemoteRepositoryAction
extends|extends
name|AbstractRemoteRepositoriesAction
implements|implements
name|Preparable
block|{
specifier|private
name|RemoteRepository
name|repository
decl_stmt|;
specifier|private
name|String
name|repoid
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|repoid
argument_list|)
condition|)
block|{
name|this
operator|.
name|repository
operator|=
name|getRemoteRepositoryAdmin
argument_list|()
operator|.
name|getRemoteRepository
argument_list|(
name|repoid
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
name|repoid
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete remote repository: repository id was blank."
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
name|String
name|result
init|=
name|SUCCESS
decl_stmt|;
name|RemoteRepository
name|existingRepository
init|=
name|repository
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
try|try
block|{
name|getRemoteRepositoryAdmin
argument_list|()
operator|.
name|deleteRemoteRepository
argument_list|(
name|existingRepository
operator|.
name|getId
argument_list|()
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
name|ERROR
expr_stmt|;
block|}
return|return
name|result
return|;
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
block|}
end_class

end_unit

