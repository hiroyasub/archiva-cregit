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
name|java
operator|.
name|time
operator|.
name|Period
import|;
end_import

begin_comment
comment|/**  *  * This feature provides settings for artifact cleanup. This is meant mainly for snapshot artifacts,  * that should be deleted after a time period.  *  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactCleanupFeature
implements|implements
name|RepositoryFeature
argument_list|<
name|ArtifactCleanupFeature
argument_list|>
block|{
specifier|private
name|boolean
name|deleteReleasedSnapshots
init|=
literal|false
decl_stmt|;
specifier|private
name|Period
name|retentionPeriod
init|=
name|Period
operator|.
name|ofDays
argument_list|(
literal|100
argument_list|)
decl_stmt|;
specifier|private
name|int
name|retentionCount
init|=
literal|2
decl_stmt|;
specifier|public
name|ArtifactCleanupFeature
parameter_list|()
block|{
block|}
specifier|public
name|ArtifactCleanupFeature
parameter_list|(
name|boolean
name|deleteReleasedSnapshots
parameter_list|,
name|Period
name|retentionPeriod
parameter_list|,
name|int
name|retentionCount
parameter_list|)
block|{
name|this
operator|.
name|deleteReleasedSnapshots
operator|=
name|deleteReleasedSnapshots
expr_stmt|;
name|this
operator|.
name|retentionPeriod
operator|=
name|retentionPeriod
expr_stmt|;
name|this
operator|.
name|retentionCount
operator|=
name|retentionCount
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ArtifactCleanupFeature
name|get
parameter_list|()
block|{
return|return
name|this
return|;
block|}
comment|/**      * Returns true, if snapshot artifacts should be deleted, when artifacts with release version      * exist in one of the managed repositories.      * @return True, if artifacts should be deleted after release, otherwise false.      */
specifier|public
name|boolean
name|isDeleteReleasedSnapshots
parameter_list|()
block|{
return|return
name|deleteReleasedSnapshots
return|;
block|}
comment|/**      * Sets the flag for the deletion of released snapshot artifacts.      * @param deleteReleasedSnapshots      */
specifier|public
name|void
name|setDeleteReleasedSnapshots
parameter_list|(
name|boolean
name|deleteReleasedSnapshots
parameter_list|)
block|{
name|this
operator|.
name|deleteReleasedSnapshots
operator|=
name|deleteReleasedSnapshots
expr_stmt|;
block|}
comment|/**      * Returns the amount of time after that, the (snapshot) artifacts can be deleted.      *      * @return The time period after that the artifacts can be deleted.      */
specifier|public
name|Period
name|getRetentionPeriod
parameter_list|()
block|{
return|return
name|retentionPeriod
return|;
block|}
comment|/**      * Sets time period, after that artifacts can be deleted.      * @param retentionPeriod      */
specifier|public
name|void
name|setRetentionPeriod
parameter_list|(
name|Period
name|retentionPeriod
parameter_list|)
block|{
name|this
operator|.
name|retentionPeriod
operator|=
name|retentionPeriod
expr_stmt|;
block|}
comment|/**      * Sets the number of (snapshot) artifacts that should be kept, even if they are older      * than the retention time.      * @return The number of artifacts for a version that should be kept      */
specifier|public
name|int
name|getRetentionCount
parameter_list|()
block|{
return|return
name|retentionCount
return|;
block|}
comment|/**      * Sets the number of artifacts that should be kept and not be deleted.      *      * @param retentionCount      */
specifier|public
name|void
name|setRetentionCount
parameter_list|(
name|int
name|retentionCount
parameter_list|)
block|{
name|this
operator|.
name|retentionCount
operator|=
name|retentionCount
expr_stmt|;
block|}
block|}
end_class

end_unit
