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
name|mock
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
name|model
operator|.
name|ArchivaArtifact
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
name|model
operator|.
name|ArtifactReference
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
name|model
operator|.
name|ProjectReference
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
name|model
operator|.
name|VersionedReference
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
name|ContentAccessException
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
name|ContentNotFoundException
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
name|LayoutException
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
name|ManagedRepositoryContent
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
name|content
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
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|ContentItem
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
name|content
operator|.
name|ItemNotFoundException
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
name|content
operator|.
name|ItemSelector
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
name|content
operator|.
name|Namespace
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
name|content
operator|.
name|Project
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
name|content
operator|.
name|Version
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
name|storage
operator|.
name|StorageAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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
name|Set
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
name|Stream
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"managedRepositoryContent#mock"
argument_list|)
specifier|public
class|class
name|ManagedRepositoryContentMock
implements|implements
name|ManagedRepositoryContent
block|{
specifier|private
name|ManagedRepository
name|repository
decl_stmt|;
annotation|@
name|Override
specifier|public
name|VersionedReference
name|toVersion
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|VersionedReference
name|toVersion
parameter_list|(
name|ArtifactReference
name|artifactReference
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ArtifactReference
name|toArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|classifier
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteItem
parameter_list|(
name|ContentItem
name|item
parameter_list|)
throws|throws
name|ItemNotFoundException
throws|,
name|ContentAccessException
block|{
block|}
annotation|@
name|Override
specifier|public
name|ContentItem
name|getItem
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Namespace
name|getNamespace
parameter_list|(
name|ItemSelector
name|namespaceSelector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Project
name|getProject
parameter_list|(
name|ItemSelector
name|projectSelector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteVersion
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|ContentAccessException
block|{
block|}
annotation|@
name|Override
specifier|public
name|Version
name|getVersion
parameter_list|(
name|ItemSelector
name|versionCoordinates
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteArtifact
parameter_list|(
name|ArtifactReference
name|artifactReference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|ContentAccessException
block|{
block|}
annotation|@
name|Override
specifier|public
name|Artifact
name|getArtifact
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|getArtifacts
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Stream
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|newArtifactStream
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Project
argument_list|>
name|getProjects
parameter_list|(
name|Namespace
name|namespace
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Project
argument_list|>
name|getProjects
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Version
argument_list|>
name|getVersions
parameter_list|(
name|Project
name|project
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Version
argument_list|>
name|getVersions
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|getArtifacts
parameter_list|(
name|ContentItem
name|item
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Stream
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|newArtifactStream
parameter_list|(
name|ContentItem
name|item
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasContent
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addArtifact
parameter_list|(
name|Path
name|sourceFile
parameter_list|,
name|Artifact
name|destination
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
block|}
annotation|@
name|Override
specifier|public
name|ContentItem
name|toItem
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ContentItem
name|toItem
parameter_list|(
name|StorageAsset
name|assetPath
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|ContentAccessException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteProject
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|ContentAccessException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteProject
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|ContentAccessException
block|{
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|( )
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactReference
argument_list|>
name|getRelatedArtifacts
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
throws|,
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactReference
argument_list|>
name|getRelatedArtifacts
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
throws|,
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|StorageAsset
argument_list|>
name|getRelatedAssets
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
throws|,
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactReference
argument_list|>
name|getArtifacts
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
throws|,
name|ContentAccessException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRepoRoot
parameter_list|( )
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|getRepository
parameter_list|( )
block|{
return|return
name|repository
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getVersions
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|ContentAccessException
throws|,
name|LayoutException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasContent
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasContent
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentAccessException
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setRepository
parameter_list|(
name|ManagedRepository
name|repo
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repo
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|toFile
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|toFile
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|toFile
parameter_list|(
name|ArchivaArtifact
name|reference
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toMetadataPath
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toMetadataPath
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toPath
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ItemSelector
name|toItemSelector
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toPath
parameter_list|(
name|ArchivaArtifact
name|reference
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

