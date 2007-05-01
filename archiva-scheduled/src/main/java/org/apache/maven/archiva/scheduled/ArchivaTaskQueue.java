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
name|scheduled
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
name|tasks
operator|.
name|DatabaseTask
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
name|codehaus
operator|.
name|plexus
operator|.
name|taskqueue
operator|.
name|DefaultTaskQueue
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
name|Task
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
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * ArchivaTaskQueue   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.codehaus.plexus.taskqueue.TaskQueue"   *                   role-hint="archiva-task-queue"  *                   lifecycle-handler="plexus-configurable"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaTaskQueue
extends|extends
name|DefaultTaskQueue
block|{
specifier|public
name|ArchivaTaskQueue
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
comment|/* do nothing special */
block|}
specifier|public
name|boolean
name|hasDatabaseTaskInQueue
parameter_list|()
block|{
try|try
block|{
name|List
name|queue
init|=
name|getQueueSnapshot
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|queue
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Task
name|task
init|=
operator|(
name|Task
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|task
operator|instanceof
name|DatabaseTask
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|boolean
name|hasFilesystemTaskInQueue
parameter_list|()
block|{
try|try
block|{
name|List
name|queue
init|=
name|getQueueSnapshot
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|queue
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Task
name|task
init|=
operator|(
name|Task
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|task
operator|instanceof
name|RepositoryTask
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|boolean
name|hasRepositoryTaskInQueue
parameter_list|(
name|String
name|repoid
parameter_list|)
block|{
try|try
block|{
name|List
name|queue
init|=
name|getQueueSnapshot
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|queue
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Task
name|task
init|=
operator|(
name|Task
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|task
operator|instanceof
name|RepositoryTask
condition|)
block|{
name|RepositoryTask
name|rtask
init|=
operator|(
name|RepositoryTask
operator|)
name|task
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|repoid
argument_list|,
name|rtask
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

