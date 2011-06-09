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
name|audit
operator|.
name|AuditEvent
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
name|RemoteRepositoryConfiguration
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
name|IOException
import|;
end_import

begin_comment
comment|/**  * EditRemoteRepositoryAction  *  * @version $Id$  *<p/>  *          plexus.component role="com.opensymphony.xwork2.Action" role-hint="editRemoteRepositoryAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"editRemoteRepositoryAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|EditRemoteRepositoryAction
extends|extends
name|AbstractRemoteRepositoriesAction
implements|implements
name|Preparable
block|{
comment|/**      * The model for this action.      */
specifier|private
name|RemoteRepositoryConfiguration
name|repository
decl_stmt|;
comment|/**      * The repository id to edit.      */
specifier|private
name|String
name|repoid
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|String
name|id
init|=
name|repoid
decl_stmt|;
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
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findRemoteRepositoryById
argument_list|(
name|id
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
name|commit
parameter_list|()
block|{
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|// We are in edit mode, remove the old repository configuration.
name|removeRepository
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
comment|// Save the repository configuration.
name|String
name|result
decl_stmt|;
try|try
block|{
name|addRepository
argument_list|(
name|repository
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|MODIFY_REMOTE_REPO
argument_list|)
expr_stmt|;
name|result
operator|=
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
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
name|result
operator|=
name|INPUT
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
name|result
operator|=
name|INPUT
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|RemoteRepositoryConfiguration
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
name|RemoteRepositoryConfiguration
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

