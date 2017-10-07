begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|features
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
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
import|;
end_import

begin_comment
comment|/**  * This feature provides some information about staging repositories.  *  */
end_comment

begin_class
specifier|public
class|class
name|StagingRepositoryFeature
implements|implements
name|RepositoryFeature
argument_list|<
name|StagingRepositoryFeature
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|STAGING_REPO_POSTFIX
init|=
literal|"-stage"
decl_stmt|;
specifier|private
name|ManagedRepository
name|stagingRepository
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|stageRepoNeeded
init|=
literal|false
decl_stmt|;
specifier|public
name|StagingRepositoryFeature
parameter_list|()
block|{
block|}
specifier|public
name|StagingRepositoryFeature
parameter_list|(
name|ManagedRepository
name|stagingRepository
parameter_list|,
name|boolean
name|stageRepoNeeded
parameter_list|)
block|{
name|this
operator|.
name|stagingRepository
operator|=
name|stagingRepository
expr_stmt|;
name|this
operator|.
name|stageRepoNeeded
operator|=
name|stageRepoNeeded
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StagingRepositoryFeature
name|get
parameter_list|()
block|{
return|return
name|this
return|;
block|}
comment|/**      * Returns the staging repository, if it exists.      *      * @return The staging repository, null if not set.      *      */
specifier|public
name|ManagedRepository
name|getStagingRepository
parameter_list|()
block|{
return|return
name|stagingRepository
return|;
block|}
comment|/**      * Sets the staging repository.      *      * @param stagingRepository      */
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
comment|/**      * Returns true, if a staging repository is needed by this repository.      * @return True, if staging repository is needed, otherwise false.      */
specifier|public
name|boolean
name|isStageRepoNeeded
parameter_list|()
block|{
return|return
name|stageRepoNeeded
return|;
block|}
comment|/**      * Sets the flag for needed staging repository.      *      * @param stageRepoNeeded      */
specifier|public
name|void
name|setStageRepoNeeded
parameter_list|(
name|boolean
name|stageRepoNeeded
parameter_list|)
block|{
name|this
operator|.
name|stageRepoNeeded
operator|=
name|stageRepoNeeded
expr_stmt|;
block|}
block|}
end_class

end_unit

