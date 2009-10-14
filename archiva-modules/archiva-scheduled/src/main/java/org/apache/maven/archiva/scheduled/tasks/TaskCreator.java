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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|scheduled
operator|.
name|DefaultArchivaTaskScheduler
import|;
end_import

begin_comment
comment|/**  * TaskCreator Convenience class for creating Archiva tasks.  */
end_comment

begin_class
specifier|public
class|class
name|TaskCreator
block|{
specifier|public
specifier|static
name|RepositoryTask
name|createRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|taskNameSuffix
parameter_list|)
block|{
name|String
name|suffix
init|=
literal|""
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|taskNameSuffix
argument_list|)
condition|)
block|{
name|suffix
operator|=
literal|":"
operator|+
name|taskNameSuffix
expr_stmt|;
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
name|repositoryId
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
name|repositoryId
operator|+
name|suffix
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
return|return
name|task
return|;
block|}
specifier|public
specifier|static
name|RepositoryTask
name|createRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|taskNameSuffix
parameter_list|,
name|boolean
name|scanAll
parameter_list|)
block|{
name|RepositoryTask
name|task
init|=
name|createRepositoryTask
argument_list|(
name|repositoryId
argument_list|,
name|taskNameSuffix
argument_list|)
decl_stmt|;
name|task
operator|.
name|setScanAll
argument_list|(
name|scanAll
argument_list|)
expr_stmt|;
return|return
name|task
return|;
block|}
specifier|public
specifier|static
name|RepositoryTask
name|createRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|taskNameSuffix
parameter_list|,
name|File
name|resourceFile
parameter_list|,
name|boolean
name|updateRelatedArtifacts
parameter_list|)
block|{
name|RepositoryTask
name|task
init|=
name|createRepositoryTask
argument_list|(
name|repositoryId
argument_list|,
name|taskNameSuffix
argument_list|)
decl_stmt|;
name|task
operator|.
name|setResourceFile
argument_list|(
name|resourceFile
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUpdateRelatedArtifacts
argument_list|(
name|updateRelatedArtifacts
argument_list|)
expr_stmt|;
return|return
name|task
return|;
block|}
specifier|public
specifier|static
name|RepositoryTask
name|createRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|taskNameSuffix
parameter_list|,
name|File
name|resourceFile
parameter_list|,
name|boolean
name|updateRelatedArtifacts
parameter_list|,
name|boolean
name|scanAll
parameter_list|)
block|{
name|RepositoryTask
name|task
init|=
name|createRepositoryTask
argument_list|(
name|repositoryId
argument_list|,
name|taskNameSuffix
argument_list|,
name|resourceFile
argument_list|,
name|updateRelatedArtifacts
argument_list|)
decl_stmt|;
name|task
operator|.
name|setScanAll
argument_list|(
name|scanAll
argument_list|)
expr_stmt|;
return|return
name|task
return|;
block|}
specifier|public
specifier|static
name|ArtifactIndexingTask
name|createIndexingTask
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|File
name|resource
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|ArtifactIndexingTask
name|task
init|=
operator|new
name|ArtifactIndexingTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setRepositoryId
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|DefaultArchivaTaskScheduler
operator|.
name|INDEXING_JOB
operator|+
literal|":"
operator|+
name|repositoryId
decl_stmt|;
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|name
operator|+
literal|":"
operator|+
name|resource
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|name
operator|=
name|name
operator|+
literal|":"
operator|+
name|action
expr_stmt|;
name|task
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|task
operator|.
name|setAction
argument_list|(
name|action
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
name|task
operator|.
name|setResourceFile
argument_list|(
name|resource
argument_list|)
expr_stmt|;
return|return
name|task
return|;
block|}
block|}
end_class

end_unit

