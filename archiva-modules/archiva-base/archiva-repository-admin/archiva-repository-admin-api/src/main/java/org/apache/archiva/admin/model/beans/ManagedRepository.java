begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"managedRepository"
argument_list|)
specifier|public
class|class
name|ManagedRepository
extends|extends
name|AbstractRepository
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|location
decl_stmt|;
specifier|private
name|boolean
name|snapshots
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|releases
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|blockRedeployments
init|=
literal|false
decl_stmt|;
comment|/**      * not need when creating the repo : only available when reading      */
specifier|private
name|ManagedRepository
name|stagingRepository
decl_stmt|;
specifier|private
name|boolean
name|scanned
init|=
literal|false
decl_stmt|;
comment|/**      * default model value      */
specifier|private
name|int
name|daysOlder
init|=
literal|100
decl_stmt|;
comment|/**      * default model value      */
specifier|private
name|int
name|retentionCount
init|=
literal|2
decl_stmt|;
specifier|private
name|boolean
name|deleteReleasedSnapshots
decl_stmt|;
specifier|private
name|boolean
name|stageRepoNeeded
decl_stmt|;
specifier|private
name|boolean
name|resetStats
decl_stmt|;
specifier|public
name|ManagedRepository
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ManagedRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|layout
parameter_list|,
name|boolean
name|snapshots
parameter_list|,
name|boolean
name|releases
parameter_list|,
name|boolean
name|blockRedeployments
parameter_list|,
name|String
name|cronExpression
parameter_list|,
name|String
name|indexDir
parameter_list|,
name|boolean
name|scanned
parameter_list|,
name|int
name|daysOlder
parameter_list|,
name|int
name|retentionCount
parameter_list|,
name|boolean
name|deleteReleasedSnapshots
parameter_list|,
name|boolean
name|stageRepoNeeded
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|layout
argument_list|)
expr_stmt|;
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
name|this
operator|.
name|snapshots
operator|=
name|snapshots
expr_stmt|;
name|this
operator|.
name|releases
operator|=
name|releases
expr_stmt|;
name|this
operator|.
name|blockRedeployments
operator|=
name|blockRedeployments
expr_stmt|;
name|this
operator|.
name|setCronExpression
argument_list|(
name|cronExpression
argument_list|)
expr_stmt|;
name|this
operator|.
name|setIndexDirectory
argument_list|(
name|indexDir
argument_list|)
expr_stmt|;
name|this
operator|.
name|scanned
operator|=
name|scanned
expr_stmt|;
name|this
operator|.
name|daysOlder
operator|=
name|daysOlder
expr_stmt|;
name|this
operator|.
name|retentionCount
operator|=
name|retentionCount
expr_stmt|;
name|this
operator|.
name|deleteReleasedSnapshots
operator|=
name|deleteReleasedSnapshots
expr_stmt|;
name|this
operator|.
name|stageRepoNeeded
operator|=
name|stageRepoNeeded
expr_stmt|;
name|this
operator|.
name|resetStats
operator|=
name|resetStats
expr_stmt|;
block|}
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
comment|/**      * Get null      */
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
specifier|public
name|boolean
name|isBlockRedeployments
parameter_list|()
block|{
return|return
name|blockRedeployments
return|;
block|}
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
specifier|public
name|boolean
name|isScanned
parameter_list|()
block|{
return|return
name|scanned
return|;
block|}
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
specifier|public
name|int
name|getDaysOlder
parameter_list|()
block|{
return|return
name|daysOlder
return|;
block|}
specifier|public
name|void
name|setDaysOlder
parameter_list|(
name|int
name|daysOlder
parameter_list|)
block|{
name|this
operator|.
name|daysOlder
operator|=
name|daysOlder
expr_stmt|;
block|}
specifier|public
name|int
name|getRetentionCount
parameter_list|()
block|{
return|return
name|retentionCount
return|;
block|}
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
specifier|public
name|boolean
name|isDeleteReleasedSnapshots
parameter_list|()
block|{
return|return
name|deleteReleasedSnapshots
return|;
block|}
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
specifier|public
name|boolean
name|isStageRepoNeeded
parameter_list|()
block|{
return|return
name|stageRepoNeeded
return|;
block|}
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
specifier|public
name|boolean
name|isResetStats
parameter_list|()
block|{
return|return
name|resetStats
return|;
block|}
specifier|public
name|void
name|setResetStats
parameter_list|(
name|boolean
name|resetStats
parameter_list|)
block|{
name|this
operator|.
name|resetStats
operator|=
name|resetStats
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|super
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"ManagedRepository"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{location='"
argument_list|)
operator|.
name|append
argument_list|(
name|location
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", snapshots="
argument_list|)
operator|.
name|append
argument_list|(
name|snapshots
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", releases="
argument_list|)
operator|.
name|append
argument_list|(
name|releases
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", blockRedeployments="
argument_list|)
operator|.
name|append
argument_list|(
name|blockRedeployments
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", stagingRepository="
argument_list|)
operator|.
name|append
argument_list|(
name|stagingRepository
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", scanned="
argument_list|)
operator|.
name|append
argument_list|(
name|scanned
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", daysOlder="
argument_list|)
operator|.
name|append
argument_list|(
name|daysOlder
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", retentionCount="
argument_list|)
operator|.
name|append
argument_list|(
name|retentionCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", deleteReleasedSnapshots="
argument_list|)
operator|.
name|append
argument_list|(
name|deleteReleasedSnapshots
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", stageRepoNeeded="
argument_list|)
operator|.
name|append
argument_list|(
name|stageRepoNeeded
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", resetStats="
argument_list|)
operator|.
name|append
argument_list|(
name|resetStats
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

