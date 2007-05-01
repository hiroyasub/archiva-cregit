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
name|scheduled
operator|.
name|ArchivaTaskScheduler
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
name|scheduled
operator|.
name|DefaultArchivaTaskScheduler
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
name|scheduled
operator|.
name|tasks
operator|.
name|ArchivaTask
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
name|scheduled
operator|.
name|tasks
operator|.
name|RepositoryTask
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
name|security
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
name|security
operator|.
name|ui
operator|.
name|web
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
name|security
operator|.
name|ui
operator|.
name|web
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
name|security
operator|.
name|ui
operator|.
name|web
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
name|taskqueue
operator|.
name|TaskQueueException
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

begin_comment
comment|/**  * Configures the application.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="indexRepositoryAction"  */
end_comment

begin_class
specifier|public
class|class
name|IndexRepositoryAction
extends|extends
name|PlexusActionSupport
implements|implements
name|SecureAction
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaTaskScheduler
name|taskScheduler
decl_stmt|;
specifier|private
name|String
name|repoid
decl_stmt|;
specifier|public
name|String
name|run
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
literal|"Cannot run indexer on blank repository id."
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
name|RepositoryTask
name|task
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setRepositoryId
argument_list|(
name|repoid
argument_list|)
expr_stmt|;
name|task
operator|.
name|setName
argument_list|(
name|DefaultArchivaTaskScheduler
operator|.
name|REPOSITORY_JOB
operator|+
literal|":"
operator|+
name|repoid
argument_list|)
expr_stmt|;
name|task
operator|.
name|setQueuePolicy
argument_list|(
name|ArchivaTask
operator|.
name|QUEUE_POLICY_WAIT
argument_list|)
expr_stmt|;
name|boolean
name|scheduleTask
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|taskScheduler
operator|.
name|getTaskQueue
argument_list|()
operator|.
name|hasFilesystemTaskInQueue
argument_list|()
condition|)
block|{
if|if
condition|(
name|taskScheduler
operator|.
name|getTaskQueue
argument_list|()
operator|.
name|hasRepositoryTaskInQueue
argument_list|(
name|repoid
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Repository ["
operator|+
name|repoid
operator|+
literal|"] task was already queued."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scheduleTask
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|scheduleTask
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|scheduleTask
condition|)
block|{
try|try
block|{
name|taskScheduler
operator|.
name|getTaskQueue
argument_list|()
operator|.
name|put
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Your request to have repository ["
operator|+
name|repoid
operator|+
literal|"] be indexed has been queued."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to queue your request to have repository ["
operator|+
name|repoid
operator|+
literal|"] be indexed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Return to the repositories screen.
return|return
name|SUCCESS
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
name|OPERATION_RUN_INDEXER
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

