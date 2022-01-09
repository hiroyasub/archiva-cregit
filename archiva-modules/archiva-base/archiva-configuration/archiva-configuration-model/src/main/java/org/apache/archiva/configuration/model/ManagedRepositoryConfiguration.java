begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Class ManagedRepositoryConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|ManagedRepositoryConfiguration
extends|extends
name|AbstractRepositoryConfiguration
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
implements|,
name|ConfigurationModel
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      *       *             The file system location for this repository.      *                 */
specifier|private
name|String
name|location
decl_stmt|;
comment|/**      * True if this repository contains release versioned artifacts.      */
specifier|private
name|boolean
name|releases
init|=
literal|true
decl_stmt|;
comment|/**      * True if re-deployment of artifacts already in the repository      * will be blocked.      */
specifier|private
name|boolean
name|blockRedeployments
init|=
literal|false
decl_stmt|;
comment|/**      * True if this repository contains snapshot versioned artifacts      */
specifier|private
name|boolean
name|snapshots
init|=
literal|false
decl_stmt|;
comment|/**      * True if this repository should be scanned and processed.      */
specifier|private
name|boolean
name|scanned
init|=
literal|true
decl_stmt|;
comment|/**      *       *             When to run the refresh task.      *             Default is every hour      *           .      */
specifier|private
name|String
name|refreshCronExpression
init|=
literal|"0 0 * * * ?"
decl_stmt|;
comment|/**      *       *             The total count of the artifact to be retained      * for each snapshot.      *                 */
specifier|private
name|int
name|retentionCount
init|=
literal|2
decl_stmt|;
comment|/**      *       *             The number of days after which snapshots will be      * removed.      *                 */
specifier|private
name|int
name|retentionPeriod
init|=
literal|100
decl_stmt|;
comment|/**      *       *             True if the released snapshots are to be removed      * from the repo during repository purge.      *                 */
specifier|private
name|boolean
name|deleteReleasedSnapshots
init|=
literal|false
decl_stmt|;
comment|/**      *       *             True to not generate packed index (note you      * won't be able to export your index.      *                 */
specifier|private
name|boolean
name|skipPackedIndexCreation
init|=
literal|false
decl_stmt|;
comment|/**      *       *             Need a staging repository      *           .      */
specifier|private
name|boolean
name|stageRepoNeeded
init|=
literal|false
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the file system location for this repository.      *       * @return String      */
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|this
operator|.
name|location
return|;
block|}
comment|//-- String getLocation()
comment|/**      * Get when to run the refresh task.      *             Default is every hour.      *       * @return String      */
specifier|public
name|String
name|getRefreshCronExpression
parameter_list|()
block|{
return|return
name|this
operator|.
name|refreshCronExpression
return|;
block|}
comment|//-- String getRefreshCronExpression()
comment|/**      * Get the total count of the artifact to be retained for each      * snapshot.      *       * @return int      */
specifier|public
name|int
name|getRetentionCount
parameter_list|()
block|{
return|return
name|this
operator|.
name|retentionCount
return|;
block|}
comment|//-- int getRetentionCount()
comment|/**      * Get the number of days after which snapshots will be      * removed.      *       * @return int      */
specifier|public
name|int
name|getRetentionPeriod
parameter_list|()
block|{
return|return
name|this
operator|.
name|retentionPeriod
return|;
block|}
comment|//-- int getRetentionPeriod()
comment|/**      * Get true if re-deployment of artifacts already in the      * repository will be blocked.      *       * @return boolean      */
specifier|public
name|boolean
name|isBlockRedeployments
parameter_list|()
block|{
return|return
name|this
operator|.
name|blockRedeployments
return|;
block|}
comment|//-- boolean isBlockRedeployments()
comment|/**      * Get true if the released snapshots are to be removed from      * the repo during repository purge.      *       * @return boolean      */
specifier|public
name|boolean
name|isDeleteReleasedSnapshots
parameter_list|()
block|{
return|return
name|this
operator|.
name|deleteReleasedSnapshots
return|;
block|}
comment|//-- boolean isDeleteReleasedSnapshots()
comment|/**      * Get true if this repository contains release versioned      * artifacts.      *       * @return boolean      */
specifier|public
name|boolean
name|isReleases
parameter_list|()
block|{
return|return
name|this
operator|.
name|releases
return|;
block|}
comment|//-- boolean isReleases()
comment|/**      * Get true if this repository should be scanned and processed.      *       * @return boolean      */
specifier|public
name|boolean
name|isScanned
parameter_list|()
block|{
return|return
name|this
operator|.
name|scanned
return|;
block|}
comment|//-- boolean isScanned()
comment|/**      * Get true to not generate packed index (note you won't be      * able to export your index.      *       * @return boolean      */
specifier|public
name|boolean
name|isSkipPackedIndexCreation
parameter_list|()
block|{
return|return
name|this
operator|.
name|skipPackedIndexCreation
return|;
block|}
comment|//-- boolean isSkipPackedIndexCreation()
comment|/**      * Get true if this repository contains snapshot versioned      * artifacts.      *       * @return boolean      */
specifier|public
name|boolean
name|isSnapshots
parameter_list|()
block|{
return|return
name|this
operator|.
name|snapshots
return|;
block|}
comment|//-- boolean isSnapshots()
comment|/**      * Get need a staging repository.      *       * @return boolean      */
specifier|public
name|boolean
name|isStageRepoNeeded
parameter_list|()
block|{
return|return
name|this
operator|.
name|stageRepoNeeded
return|;
block|}
comment|//-- boolean isStageRepoNeeded()
comment|/**      * Set true if re-deployment of artifacts already in the      * repository will be blocked.      *       * @param blockRedeployments      */
specifier|public
name|void
name|setBlockRedeployments
parameter_list|(
name|boolean
name|blockRedeployments
parameter_list|)
block|{
name|this
operator|.
name|blockRedeployments
operator|=
name|blockRedeployments
expr_stmt|;
block|}
comment|//-- void setBlockRedeployments( boolean )
comment|/**      * Set true if the released snapshots are to be removed from      * the repo during repository purge.      *       * @param deleteReleasedSnapshots      */
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
comment|//-- void setDeleteReleasedSnapshots( boolean )
comment|/**      * Set the file system location for this repository.      *       * @param location      */
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
block|}
comment|//-- void setLocation( String )
comment|/**      * Set when to run the refresh task.      *             Default is every hour.      *       * @param refreshCronExpression      */
specifier|public
name|void
name|setRefreshCronExpression
parameter_list|(
name|String
name|refreshCronExpression
parameter_list|)
block|{
name|this
operator|.
name|refreshCronExpression
operator|=
name|refreshCronExpression
expr_stmt|;
block|}
comment|//-- void setRefreshCronExpression( String )
comment|/**      * Set true if this repository contains release versioned      * artifacts.      *       * @param releases      */
specifier|public
name|void
name|setReleases
parameter_list|(
name|boolean
name|releases
parameter_list|)
block|{
name|this
operator|.
name|releases
operator|=
name|releases
expr_stmt|;
block|}
comment|//-- void setReleases( boolean )
comment|/**      * Set the total count of the artifact to be retained for each      * snapshot.      *       * @param retentionCount      */
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
comment|//-- void setRetentionCount( int )
comment|/**      * Set the number of days after which snapshots will be      * removed.      *       * @param retentionPeriod      */
specifier|public
name|void
name|setRetentionPeriod
parameter_list|(
name|int
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
comment|//-- void setRetentionPeriod( int )
comment|/**      * Set true if this repository should be scanned and processed.      *       * @param scanned      */
specifier|public
name|void
name|setScanned
parameter_list|(
name|boolean
name|scanned
parameter_list|)
block|{
name|this
operator|.
name|scanned
operator|=
name|scanned
expr_stmt|;
block|}
comment|//-- void setScanned( boolean )
comment|/**      * Set true to not generate packed index (note you won't be      * able to export your index.      *       * @param skipPackedIndexCreation      */
specifier|public
name|void
name|setSkipPackedIndexCreation
parameter_list|(
name|boolean
name|skipPackedIndexCreation
parameter_list|)
block|{
name|this
operator|.
name|skipPackedIndexCreation
operator|=
name|skipPackedIndexCreation
expr_stmt|;
block|}
comment|//-- void setSkipPackedIndexCreation( boolean )
comment|/**      * Set true if this repository contains snapshot versioned      * artifacts.      *       * @param snapshots      */
specifier|public
name|void
name|setSnapshots
parameter_list|(
name|boolean
name|snapshots
parameter_list|)
block|{
name|this
operator|.
name|snapshots
operator|=
name|snapshots
expr_stmt|;
block|}
comment|//-- void setSnapshots( boolean )
comment|/**      * Set need a staging repository.      *       * @param stageRepoNeeded      */
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
comment|//-- void setStageRepoNeeded( boolean )
block|}
end_class

end_unit

