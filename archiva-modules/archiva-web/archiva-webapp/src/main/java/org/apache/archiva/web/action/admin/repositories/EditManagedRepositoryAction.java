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
name|archiva
operator|.
name|redback
operator|.
name|components
operator|.
name|scheduler
operator|.
name|CronExpressionValidator
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * AddManagedRepositoryAction  *  *  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"editManagedRepositoryAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|EditManagedRepositoryAction
extends|extends
name|AbstractManagedRepositoriesAction
implements|implements
name|Preparable
implements|,
name|Validateable
block|{
specifier|private
name|ManagedRepository
name|repository
decl_stmt|;
specifier|private
name|ManagedRepository
name|stagingRepository
decl_stmt|;
specifier|private
name|String
name|repoid
decl_stmt|;
specifier|private
specifier|final
name|String
name|action
init|=
literal|"editRepository"
decl_stmt|;
specifier|private
name|boolean
name|stageNeeded
decl_stmt|;
comment|// FIXME better error message
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
name|repository
operator|=
name|getManagedRepositoryAdmin
argument_list|()
operator|.
name|getManagedRepository
argument_list|(
name|repoid
argument_list|)
expr_stmt|;
name|stagingRepository
operator|=
name|getManagedRepositoryAdmin
argument_list|()
operator|.
name|getManagedRepository
argument_list|(
name|repoid
operator|+
literal|"-stage"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|repository
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|setReleases
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setScanned
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|input
parameter_list|()
block|{
if|if
condition|(
name|repository
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"Edit failure, unable to edit a repository with a blank repository id."
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
name|confirmUpdate
parameter_list|()
block|{
comment|// location was changed
return|return
name|save
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
name|String
name|commit
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|ManagedRepository
name|existingConfig
init|=
name|getManagedRepositoryAdmin
argument_list|()
operator|.
name|getManagedRepository
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|resetStats
init|=
literal|false
decl_stmt|;
comment|// check if the location was changed
name|repository
operator|.
name|setLocation
argument_list|(
name|getRepositoryCommonValidator
argument_list|()
operator|.
name|removeExpressions
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|existingConfig
operator|.
name|getLocation
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|resetStats
operator|=
literal|true
expr_stmt|;
name|File
name|dir
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
if|if
condition|(
name|dir
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|CONFIRM
return|;
block|}
block|}
return|return
name|save
argument_list|(
name|resetStats
argument_list|)
return|;
block|}
specifier|private
name|String
name|save
parameter_list|(
name|boolean
name|resetStats
parameter_list|)
block|{
name|String
name|result
init|=
name|SUCCESS
decl_stmt|;
try|try
block|{
name|getManagedRepositoryAdmin
argument_list|()
operator|.
name|updateManagedRepository
argument_list|(
name|repository
argument_list|,
name|stageNeeded
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|,
name|resetStats
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
literal|"Repository Administration Exception: "
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
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|CronExpressionValidator
name|validator
init|=
operator|new
name|CronExpressionValidator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|validator
operator|.
name|validate
argument_list|(
name|repository
operator|.
name|getCronExpression
argument_list|()
argument_list|)
condition|)
block|{
name|addFieldError
argument_list|(
literal|"repository.cronExpression"
argument_list|,
literal|"Invalid cron expression."
argument_list|)
expr_stmt|;
block|}
name|trimAllRequestParameterValues
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|trimAllRequestParameterValues
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|repository
operator|.
name|setId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|repository
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|repository
operator|.
name|setName
argument_list|(
name|repository
operator|.
name|getName
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
condition|)
block|{
name|repository
operator|.
name|setLocation
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|repository
operator|.
name|getIndexDirectory
argument_list|()
argument_list|)
condition|)
block|{
name|repository
operator|.
name|setIndexDirectory
argument_list|(
name|repository
operator|.
name|getIndexDirectory
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|boolean
name|isStageNeeded
parameter_list|()
block|{
return|return
name|stageNeeded
return|;
block|}
specifier|public
name|void
name|setStageNeeded
parameter_list|(
name|boolean
name|stageNeeded
parameter_list|)
block|{
name|this
operator|.
name|stageNeeded
operator|=
name|stageNeeded
expr_stmt|;
block|}
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
specifier|public
name|ManagedRepository
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
name|ManagedRepository
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
name|ManagedRepository
name|getStagingRepository
parameter_list|()
block|{
return|return
name|stagingRepository
return|;
block|}
specifier|public
name|void
name|setStagingRepository
parameter_list|(
name|ManagedRepository
name|stagingRepository
parameter_list|)
block|{
name|this
operator|.
name|stagingRepository
operator|=
name|stagingRepository
expr_stmt|;
block|}
block|}
end_class

end_unit

