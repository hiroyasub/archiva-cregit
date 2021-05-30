begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|v2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Schema
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
name|repository
operator|.
name|ManagedRepository
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
name|repository
operator|.
name|RepositoryType
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
name|repository
operator|.
name|features
operator|.
name|ArtifactCleanupFeature
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
name|repository
operator|.
name|features
operator|.
name|IndexCreationFeature
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
name|repository
operator|.
name|features
operator|.
name|StagingRepositoryFeature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Period
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"MavenManagedRepository"
argument_list|,
name|description
operator|=
literal|"A managed repository stores artifacts locally"
argument_list|)
specifier|public
class|class
name|MavenManagedRepository
extends|extends
name|Repository
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6853748886201905029L
decl_stmt|;
name|boolean
name|blocksRedeployments
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|releaseSchemes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(  )
decl_stmt|;
name|boolean
name|deleteSnapshotsOfRelease
init|=
literal|false
decl_stmt|;
specifier|private
name|Period
name|retentionPeriod
decl_stmt|;
specifier|private
name|int
name|retentionCount
decl_stmt|;
specifier|private
name|String
name|indexPath
decl_stmt|;
specifier|private
name|String
name|packedIndexPath
decl_stmt|;
specifier|private
name|boolean
name|skipPackedIndexCreation
decl_stmt|;
specifier|private
name|boolean
name|hasStagingRepository
decl_stmt|;
specifier|private
name|String
name|stagingRepository
decl_stmt|;
specifier|public
name|MavenManagedRepository
parameter_list|( )
block|{
name|super
operator|.
name|setCharacteristic
argument_list|(
name|Repository
operator|.
name|CHARACTERISTIC_MANAGED
argument_list|)
expr_stmt|;
name|super
operator|.
name|setType
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
operator|.
name|name
argument_list|( )
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|update
parameter_list|(
name|MavenManagedRepository
name|repo
parameter_list|,
name|ManagedRepository
name|beanRepo
parameter_list|)
block|{
name|repo
operator|.
name|setDescription
argument_list|(
name|beanRepo
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|beanRepo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setIndex
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLayout
argument_list|(
name|beanRepo
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setBlocksRedeployments
argument_list|(
name|beanRepo
operator|.
name|blocksRedeployments
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setReleaseSchemes
argument_list|(
name|beanRepo
operator|.
name|getActiveReleaseSchemes
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Objects
operator|::
name|toString
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|beanRepo
operator|.
name|getLocation
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|beanRepo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setScanned
argument_list|(
name|beanRepo
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setSchedulingDefinition
argument_list|(
name|beanRepo
operator|.
name|getSchedulingDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|ArtifactCleanupFeature
name|artifactCleanupFeature
init|=
name|beanRepo
operator|.
name|getFeature
argument_list|(
name|ArtifactCleanupFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|( )
decl_stmt|;
name|repo
operator|.
name|setDeleteSnapshotsOfRelease
argument_list|(
name|artifactCleanupFeature
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRetentionCount
argument_list|(
name|artifactCleanupFeature
operator|.
name|getRetentionCount
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRetentionPeriod
argument_list|(
name|artifactCleanupFeature
operator|.
name|getRetentionPeriod
argument_list|()
argument_list|)
expr_stmt|;
name|IndexCreationFeature
name|icf
init|=
name|beanRepo
operator|.
name|getFeature
argument_list|(
name|IndexCreationFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|( )
decl_stmt|;
name|repo
operator|.
name|setIndex
argument_list|(
name|icf
operator|.
name|hasIndex
argument_list|( )
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setIndexPath
argument_list|(
name|icf
operator|.
name|getIndexPath
argument_list|( )
operator|.
name|getPath
argument_list|( )
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setPackedIndexPath
argument_list|(
name|icf
operator|.
name|getPackedIndexPath
argument_list|( )
operator|.
name|getPath
argument_list|( )
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setSkipPackedIndexCreation
argument_list|(
name|icf
operator|.
name|isSkipPackedIndexCreation
argument_list|()
argument_list|)
expr_stmt|;
name|StagingRepositoryFeature
name|srf
init|=
name|beanRepo
operator|.
name|getFeature
argument_list|(
name|StagingRepositoryFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|( )
decl_stmt|;
name|repo
operator|.
name|setHasStagingRepository
argument_list|(
name|srf
operator|.
name|isStageRepoNeeded
argument_list|( )
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setStagingRepository
argument_list|(
name|srf
operator|.
name|getStagingRepository
argument_list|()
operator|!=
literal|null
condition|?
name|srf
operator|.
name|getStagingRepository
argument_list|()
operator|.
name|getId
argument_list|()
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|MavenManagedRepository
name|of
parameter_list|(
name|ManagedRepository
name|beanRepo
parameter_list|)
block|{
name|MavenManagedRepository
name|repo
init|=
operator|new
name|MavenManagedRepository
argument_list|( )
decl_stmt|;
name|update
argument_list|(
name|repo
argument_list|,
name|beanRepo
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"blocks_redeployments"
argument_list|,
name|description
operator|=
literal|"True, if redeployments to this repository are not allowed"
argument_list|)
specifier|public
name|boolean
name|isBlocksRedeployments
parameter_list|( )
block|{
return|return
name|blocksRedeployments
return|;
block|}
specifier|public
name|void
name|setBlocksRedeployments
parameter_list|(
name|boolean
name|blocksRedeployments
parameter_list|)
block|{
name|this
operator|.
name|blocksRedeployments
operator|=
name|blocksRedeployments
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"release_schemes"
argument_list|,
name|description
operator|=
literal|"The release schemes this repository is used for (e.g. RELEASE, SNAPSHOT)"
argument_list|)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getReleaseSchemes
parameter_list|( )
block|{
return|return
name|releaseSchemes
return|;
block|}
specifier|public
name|void
name|setReleaseSchemes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|releaseSchemes
parameter_list|)
block|{
name|this
operator|.
name|releaseSchemes
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|releaseSchemes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addReleaseScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|releaseSchemes
operator|.
name|contains
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|this
operator|.
name|releaseSchemes
operator|.
name|add
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"delete_snaphots_of_release"
argument_list|,
name|description
operator|=
literal|"True, if snapshots are deleted, after a version is released"
argument_list|)
specifier|public
name|boolean
name|isDeleteSnapshotsOfRelease
parameter_list|( )
block|{
return|return
name|deleteSnapshotsOfRelease
return|;
block|}
specifier|public
name|void
name|setDeleteSnapshotsOfRelease
parameter_list|(
name|boolean
name|deleteSnapshotsOfRelease
parameter_list|)
block|{
name|this
operator|.
name|deleteSnapshotsOfRelease
operator|=
name|deleteSnapshotsOfRelease
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"retention_period"
argument_list|,
name|description
operator|=
literal|"The period after which snapshots are deleted."
argument_list|)
specifier|public
name|Period
name|getRetentionPeriod
parameter_list|( )
block|{
return|return
name|retentionPeriod
return|;
block|}
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
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"retention_count"
argument_list|,
name|description
operator|=
literal|"Number of snapshot artifacts to keep."
argument_list|)
specifier|public
name|int
name|getRetentionCount
parameter_list|( )
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
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"index_path"
argument_list|,
name|description
operator|=
literal|"Path to the directory that contains the index, relative to the repository base directory"
argument_list|)
specifier|public
name|String
name|getIndexPath
parameter_list|( )
block|{
return|return
name|indexPath
return|;
block|}
specifier|public
name|void
name|setIndexPath
parameter_list|(
name|String
name|indexPath
parameter_list|)
block|{
name|this
operator|.
name|indexPath
operator|=
name|indexPath
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"packed_index_path"
argument_list|,
name|description
operator|=
literal|"Path to the directory that contains the packed index, relative to the repository base directory"
argument_list|)
specifier|public
name|String
name|getPackedIndexPath
parameter_list|( )
block|{
return|return
name|packedIndexPath
return|;
block|}
specifier|public
name|void
name|setPackedIndexPath
parameter_list|(
name|String
name|packedIndexPath
parameter_list|)
block|{
name|this
operator|.
name|packedIndexPath
operator|=
name|packedIndexPath
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"skip_packed_index_creation"
argument_list|,
name|description
operator|=
literal|"True, if packed index is not created during index update"
argument_list|)
specifier|public
name|boolean
name|isSkipPackedIndexCreation
parameter_list|( )
block|{
return|return
name|skipPackedIndexCreation
return|;
block|}
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
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"has_staging_repository"
argument_list|,
name|description
operator|=
literal|"True, if this repository has a staging repository assigned"
argument_list|)
specifier|public
name|boolean
name|isHasStagingRepository
parameter_list|( )
block|{
return|return
name|hasStagingRepository
return|;
block|}
specifier|public
name|void
name|setHasStagingRepository
parameter_list|(
name|boolean
name|hasStagingRepository
parameter_list|)
block|{
name|this
operator|.
name|hasStagingRepository
operator|=
name|hasStagingRepository
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"staging_repository"
argument_list|,
name|description
operator|=
literal|"The id of the assigned staging repository"
argument_list|)
specifier|public
name|String
name|getStagingRepository
parameter_list|( )
block|{
return|return
name|stagingRepository
return|;
block|}
specifier|public
name|void
name|setStagingRepository
parameter_list|(
name|String
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
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|( )
operator|!=
name|o
operator|.
name|getClass
argument_list|( )
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|super
operator|.
name|equals
argument_list|(
name|o
argument_list|)
condition|)
return|return
literal|false
return|;
name|MavenManagedRepository
name|that
init|=
operator|(
name|MavenManagedRepository
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|blocksRedeployments
operator|!=
name|that
operator|.
name|blocksRedeployments
condition|)
return|return
literal|false
return|;
return|return
name|releaseSchemes
operator|!=
literal|null
condition|?
name|releaseSchemes
operator|.
name|equals
argument_list|(
name|that
operator|.
name|releaseSchemes
argument_list|)
else|:
name|that
operator|.
name|releaseSchemes
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
name|int
name|result
init|=
name|super
operator|.
name|hashCode
argument_list|( )
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|blocksRedeployments
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|releaseSchemes
operator|!=
literal|null
condition|?
name|releaseSchemes
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|( )
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"ManagedRepository{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"blocksRedeployments="
argument_list|)
operator|.
name|append
argument_list|(
name|blocksRedeployments
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", releaseSchemes="
argument_list|)
operator|.
name|append
argument_list|(
name|releaseSchemes
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", id='"
argument_list|)
operator|.
name|append
argument_list|(
name|id
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
literal|", name='"
argument_list|)
operator|.
name|append
argument_list|(
name|name
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
literal|", description='"
argument_list|)
operator|.
name|append
argument_list|(
name|description
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
literal|", type='"
argument_list|)
operator|.
name|append
argument_list|(
name|type
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
literal|", location='"
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
literal|", schedulingDefinition='"
argument_list|)
operator|.
name|append
argument_list|(
name|schedulingDefinition
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
literal|", index="
argument_list|)
operator|.
name|append
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", layout='"
argument_list|)
operator|.
name|append
argument_list|(
name|layout
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
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|( )
return|;
block|}
block|}
end_class

end_unit

