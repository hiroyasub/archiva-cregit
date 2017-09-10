begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|repository
operator|.
name|model
package|;
end_package

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
name|taskqueue
operator|.
name|Task
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * DataRefreshTask - task for discovering changes in the repository  * and updating all associated data.  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryTask
implements|implements
name|Task
block|{
specifier|private
name|String
name|repositoryId
decl_stmt|;
specifier|private
name|Path
name|resourceFile
decl_stmt|;
specifier|private
name|boolean
name|updateRelatedArtifacts
decl_stmt|;
specifier|private
name|boolean
name|scanAll
decl_stmt|;
specifier|public
name|RepositoryTask
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|RepositoryTask
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
name|RepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|boolean
name|scanAll
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
name|this
operator|.
name|scanAll
operator|=
name|scanAll
expr_stmt|;
block|}
specifier|public
name|boolean
name|isScanAll
parameter_list|()
block|{
return|return
name|scanAll
return|;
block|}
specifier|public
name|void
name|setScanAll
parameter_list|(
name|boolean
name|scanAll
parameter_list|)
block|{
name|this
operator|.
name|scanAll
operator|=
name|scanAll
expr_stmt|;
block|}
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
annotation|@
name|Override
specifier|public
name|long
name|getMaxExecutionTime
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|Path
name|getResourceFile
parameter_list|()
block|{
return|return
name|resourceFile
return|;
block|}
specifier|public
name|void
name|setResourceFile
parameter_list|(
name|Path
name|resourceFile
parameter_list|)
block|{
name|this
operator|.
name|resourceFile
operator|=
name|resourceFile
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUpdateRelatedArtifacts
parameter_list|()
block|{
return|return
name|updateRelatedArtifacts
return|;
block|}
specifier|public
name|void
name|setUpdateRelatedArtifacts
parameter_list|(
name|boolean
name|updateRelatedArtifacts
parameter_list|)
block|{
name|this
operator|.
name|updateRelatedArtifacts
operator|=
name|updateRelatedArtifacts
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RepositoryTask [repositoryId="
operator|+
name|repositoryId
operator|+
literal|", resourceFile="
operator|+
name|resourceFile
operator|+
literal|", scanAll="
operator|+
name|scanAll
operator|+
literal|", updateRelatedArtifacts="
operator|+
name|updateRelatedArtifacts
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|repositoryId
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|repositoryId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|resourceFile
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|resourceFile
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|RepositoryTask
name|other
init|=
operator|(
name|RepositoryTask
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|repositoryId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|repositoryId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|repositoryId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|repositoryId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|resourceFile
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|resourceFile
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|resourceFile
operator|.
name|equals
argument_list|(
name|other
operator|.
name|resourceFile
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

