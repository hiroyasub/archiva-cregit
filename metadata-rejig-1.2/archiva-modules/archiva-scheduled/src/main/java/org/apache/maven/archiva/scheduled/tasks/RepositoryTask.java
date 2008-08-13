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
operator|.
name|tasks
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * DataRefreshTask - task for discovering changes in the repository   * and updating all associated data.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id: DataRefreshTask.java 525176 2007-04-03 15:21:33Z joakime $  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryTask
implements|implements
name|ArchivaTask
block|{
name|String
name|repositoryId
decl_stmt|;
name|String
name|name
decl_stmt|;
name|String
name|queuePolicy
decl_stmt|;
name|long
name|maxExecutionTime
decl_stmt|;
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|long
name|getMaxExecutionTime
parameter_list|()
block|{
return|return
name|maxExecutionTime
return|;
block|}
specifier|public
name|void
name|setMaxExecutionTime
parameter_list|(
name|long
name|maxExecutionTime
parameter_list|)
block|{
name|this
operator|.
name|maxExecutionTime
operator|=
name|maxExecutionTime
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getQueuePolicy
parameter_list|()
block|{
return|return
name|queuePolicy
return|;
block|}
specifier|public
name|void
name|setQueuePolicy
parameter_list|(
name|String
name|queuePolicy
parameter_list|)
block|{
name|this
operator|.
name|queuePolicy
operator|=
name|queuePolicy
expr_stmt|;
block|}
block|}
end_class

end_unit

