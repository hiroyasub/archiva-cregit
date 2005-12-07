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
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|versioning
operator|.
name|VersionRange
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
name|versioning
operator|.
name|ArtifactVersion
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
name|versioning
operator|.
name|OverConstrainedVersionException
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
name|handler
operator|.
name|ArtifactHandler
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
name|resolver
operator|.
name|filter
operator|.
name|ArtifactFilter
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
name|metadata
operator|.
name|ArtifactMetadata
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Collection
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
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|MockArtifact
implements|implements
name|Artifact
block|{
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|version
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|hasClassifier
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
block|}
specifier|public
name|String
name|getBaseVersion
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setBaseVersion
parameter_list|(
name|String
name|s
parameter_list|)
block|{
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getDependencyConflictId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|addMetadata
parameter_list|(
name|ArtifactMetadata
name|artifactMetadata
parameter_list|)
block|{
block|}
specifier|public
name|Collection
name|getMetadataList
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|ArtifactRepository
name|artifactRepository
parameter_list|)
block|{
block|}
specifier|public
name|ArtifactRepository
name|getRepository
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|updateVersion
parameter_list|(
name|String
name|s
parameter_list|,
name|ArtifactRepository
name|artifactRepository
parameter_list|)
block|{
block|}
specifier|public
name|String
name|getDownloadUrl
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setDownloadUrl
parameter_list|(
name|String
name|s
parameter_list|)
block|{
block|}
specifier|public
name|ArtifactFilter
name|getDependencyFilter
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setDependencyFilter
parameter_list|(
name|ArtifactFilter
name|artifactFilter
parameter_list|)
block|{
block|}
specifier|public
name|ArtifactHandler
name|getArtifactHandler
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
name|getDependencyTrail
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setDependencyTrail
parameter_list|(
name|List
name|list
parameter_list|)
block|{
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|s
parameter_list|)
block|{
block|}
specifier|public
name|VersionRange
name|getVersionRange
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setVersionRange
parameter_list|(
name|VersionRange
name|versionRange
parameter_list|)
block|{
block|}
specifier|public
name|void
name|selectVersion
parameter_list|(
name|String
name|s
parameter_list|)
block|{
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|groupId
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|artifactId
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSnapshot
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setResolved
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|isResolved
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setResolvedVersion
parameter_list|(
name|String
name|s
parameter_list|)
block|{
block|}
specifier|public
name|void
name|setArtifactHandler
parameter_list|(
name|ArtifactHandler
name|artifactHandler
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|isRelease
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setRelease
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
block|}
specifier|public
name|List
name|getAvailableVersions
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setAvailableVersions
parameter_list|(
name|List
name|list
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|ArtifactVersion
name|getSelectedVersion
parameter_list|()
throws|throws
name|OverConstrainedVersionException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isSelectedVersionKnown
parameter_list|()
throws|throws
name|OverConstrainedVersionException
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|int
name|compareTo
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

