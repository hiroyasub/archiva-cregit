begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|reporting
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|Metadata
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|Snapshot
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|CachedRepositoryQueryLayer
extends|extends
name|AbstractRepositoryQueryLayer
block|{
specifier|private
name|Cache
name|cache
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|double
name|CACHE_HIT_RATIO
init|=
literal|0.5
decl_stmt|;
specifier|public
name|CachedRepositoryQueryLayer
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|cache
operator|=
operator|new
name|Cache
argument_list|(
name|CACHE_HIT_RATIO
argument_list|)
expr_stmt|;
block|}
specifier|public
name|double
name|getCacheHitRate
parameter_list|()
block|{
return|return
name|cache
operator|.
name|getHitRate
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|containsArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|boolean
name|artifactFound
init|=
literal|true
decl_stmt|;
name|String
name|artifactPath
init|=
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
literal|"/"
operator|+
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|.
name|get
argument_list|(
name|artifactPath
argument_list|)
operator|==
literal|null
condition|)
block|{
name|artifactFound
operator|=
name|super
operator|.
name|containsArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifactFound
condition|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|artifactPath
argument_list|,
name|artifactPath
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|artifactFound
return|;
block|}
specifier|public
name|boolean
name|containsArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Snapshot
name|snapshot
parameter_list|)
block|{
name|boolean
name|artifactFound
init|=
literal|true
decl_stmt|;
name|String
name|path
init|=
name|getSnapshotArtifactRepositoryPath
argument_list|(
name|artifact
argument_list|,
name|snapshot
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|.
name|get
argument_list|(
name|path
argument_list|)
operator|==
literal|null
condition|)
block|{
name|artifactFound
operator|=
name|super
operator|.
name|containsArtifact
argument_list|(
name|artifact
argument_list|,
name|snapshot
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifactFound
condition|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|artifactFound
return|;
block|}
comment|/**      * Override method to utilize the cache      */
specifier|protected
name|Metadata
name|getMetadata
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|RepositoryQueryLayerException
block|{
name|Metadata
name|metadata
init|=
operator|(
name|Metadata
operator|)
name|cache
operator|.
name|get
argument_list|(
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadata
operator|==
literal|null
condition|)
block|{
name|metadata
operator|=
name|super
operator|.
name|getMetadata
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|artifact
operator|.
name|getId
argument_list|()
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
return|return
name|metadata
return|;
block|}
block|}
end_class

end_unit

