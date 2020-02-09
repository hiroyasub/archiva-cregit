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
name|content
operator|.
name|maven2
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
name|common
operator|.
name|filelock
operator|.
name|FileLockManager
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
name|common
operator|.
name|utils
operator|.
name|PathUtil
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
name|common
operator|.
name|utils
operator|.
name|VersionUtil
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
name|configuration
operator|.
name|FileTypes
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
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
operator|.
name|ArtifactMappingProvider
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
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
operator|.
name|DefaultArtifactMappingProvider
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
name|EditableManagedRepository
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
name|RepositoryException
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
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Files
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
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Collectors
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
comment|/**  * ManagedDefaultRepositoryContent  */
end_comment

begin_class
specifier|public
class|class
name|ManagedDefaultRepositoryContent
extends|extends
name|AbstractDefaultRepositoryContent
implements|implements
name|ManagedRepositoryContent
block|{
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|public
name|void
name|setFileTypes
parameter_list|(
name|FileTypes
name|fileTypes
parameter_list|)
block|{
name|this
operator|.
name|filetypes
operator|=
name|fileTypes
expr_stmt|;
block|}
specifier|private
name|ManagedRepository
name|repository
decl_stmt|;
name|FileLockManager
name|lockManager
decl_stmt|;
specifier|public
name|ManagedDefaultRepositoryContent
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|FileTypes
name|fileTypes
parameter_list|,
name|FileLockManager
name|lockManager
parameter_list|)
block|{
name|super
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|DefaultArtifactMappingProvider
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setFileTypes
argument_list|(
name|fileTypes
argument_list|)
expr_stmt|;
name|this
operator|.
name|lockManager
operator|=
name|lockManager
expr_stmt|;
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ManagedDefaultRepositoryContent
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|ArtifactMappingProvider
argument_list|>
name|artifactMappingProviders
parameter_list|,
name|FileTypes
name|fileTypes
parameter_list|,
name|FileLockManager
name|lockManager
parameter_list|)
block|{
name|super
argument_list|(
name|artifactMappingProviders
operator|==
literal|null
condition|?
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|DefaultArtifactMappingProvider
argument_list|()
argument_list|)
else|:
name|artifactMappingProviders
argument_list|)
expr_stmt|;
name|setFileTypes
argument_list|(
name|fileTypes
argument_list|)
expr_stmt|;
name|this
operator|.
name|lockManager
operator|=
name|lockManager
expr_stmt|;
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a version reference from the coordinates      * @param groupId the group id      * @param artifactId the artifact id      * @param version the version      * @return the versioned reference object      */
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
name|VersionedReference
name|ref
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
return|return
name|ref
return|;
block|}
comment|/**      * Return the version the artifact is part of      * @param artifactReference      * @return      */
specifier|public
name|VersionedReference
name|toVersion
parameter_list|(
name|ArtifactReference
name|artifactReference
parameter_list|)
block|{
return|return
name|toVersion
argument_list|(
name|artifactReference
operator|.
name|getGroupId
argument_list|( )
argument_list|,
name|artifactReference
operator|.
name|getArtifactId
argument_list|( )
argument_list|,
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|artifactReference
operator|.
name|getVersion
argument_list|( )
argument_list|)
argument_list|)
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
name|ArtifactReference
name|ar
init|=
operator|new
name|ArtifactReference
argument_list|( )
decl_stmt|;
name|ar
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|ar
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|ar
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|ar
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|ar
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
return|return
name|ar
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
block|{
name|Path
name|projectDir
init|=
name|getRepoDir
argument_list|()
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|projectDir
argument_list|)
operator|&&
name|Files
operator|.
name|isDirectory
argument_list|(
name|projectDir
argument_list|)
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|projectDir
argument_list|)
expr_stmt|;
block|}
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
name|RepositoryException
block|{
name|ProjectReference
name|ref
init|=
operator|new
name|ProjectReference
argument_list|( )
decl_stmt|;
name|ref
operator|.
name|setGroupId
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setArtifactId
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|Path
name|projDirectory
init|=
name|getRepoDir
argument_list|( )
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|ref
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|projDirectory
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"cannot found project "
operator|+
name|namespace
operator|+
literal|":"
operator|+
name|projectId
argument_list|)
throw|;
block|}
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|projDirectory
argument_list|)
condition|)
block|{
try|try
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|projDirectory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"project {}:{} is not a directory"
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|)
expr_stmt|;
block|}
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
block|{
specifier|final
name|Path
name|repoDir
init|=
name|getRepoDir
argument_list|( )
decl_stmt|;
name|Path
name|filePath
init|=
name|repoDir
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|artifactReference
argument_list|)
argument_list|)
decl_stmt|;
name|Path
name|parentPath
init|=
name|filePath
operator|.
name|getParent
argument_list|( )
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|filePath
argument_list|)
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|filePath
argument_list|)
expr_stmt|;
block|}
name|Path
name|filePathmd5
init|=
name|parentPath
operator|.
name|resolve
argument_list|(
name|filePath
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|filePathmd5
argument_list|)
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|filePathmd5
argument_list|)
expr_stmt|;
block|}
name|Path
name|filePathsha1
init|=
name|parentPath
operator|.
name|resolve
argument_list|(
name|filePath
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|filePathsha1
argument_list|)
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|filePathsha1
argument_list|)
expr_stmt|;
block|}
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
block|{
name|Path
name|directory
init|=
name|getRepoDir
argument_list|( )
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|groupId
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|directory
argument_list|)
condition|)
block|{
try|try
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"skip error deleting directory {}:"
argument_list|,
name|directory
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|repository
operator|.
name|getId
argument_list|()
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
block|{
name|StorageAsset
name|artifactFile
init|=
name|toFile
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|StorageAsset
name|repoDir
init|=
name|artifactFile
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|repoDir
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get related artifacts using a non-existant directory: "
operator|+
name|repoDir
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|repoDir
operator|.
name|isContainer
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get related artifacts using a non-directory: "
operator|+
name|repoDir
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
comment|// First gather up the versions found as artifacts in the managed repository.
try|try
init|(
name|Stream
argument_list|<
name|StorageAsset
argument_list|>
name|stream
init|=
name|repoDir
operator|.
name|list
argument_list|()
operator|.
name|stream
argument_list|()
init|)
block|{
return|return
name|stream
operator|.
name|filter
argument_list|(
name|asset
lambda|->
operator|!
name|asset
operator|.
name|isContainer
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|path
lambda|->
block|{
lambda|try
block|{
name|ArtifactReference
name|artifact
init|=
name|toArtifactReference
argument_list|(
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|.
name|equals
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|&&
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|&&
name|artifact
operator|.
name|getVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|artifact
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Not processing file that is not an artifact: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block_content|)
block|.filter(Objects::nonNull
block|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
end_class

begin_function
unit|}     }
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
block|{
return|return
literal|null
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|String
name|getRepoRoot
parameter_list|()
block|{
return|return
name|convertUriToPath
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
specifier|private
name|String
name|convertUriToPath
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|.
name|getScheme
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
if|else if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
name|uri
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|uri
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
end_function

begin_comment
comment|/**      * Gather the Available Versions (on disk) for a specific Project Reference, based on filesystem      * information.      *      * @return the Set of available versions, based on the project reference.      * @throws LayoutException      */
end_comment

begin_function
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getVersions
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
block|{
specifier|final
name|Path
name|projDir
init|=
name|getRepoDir
argument_list|()
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|projDir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get Versions on a non-existant directory: "
operator|+
name|projDir
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|Files
operator|.
name|isDirectory
argument_list|(
name|projDir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get Versions on a non-directory: "
operator|+
name|projDir
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|String
name|groupId
init|=
name|reference
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
specifier|final
name|String
name|artifactId
init|=
name|reference
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
try|try
init|(
name|Stream
argument_list|<
name|Path
argument_list|>
name|stream
init|=
name|Files
operator|.
name|list
argument_list|(
name|projDir
argument_list|)
init|)
block|{
return|return
name|stream
operator|.
name|filter
argument_list|(
name|Files
operator|::
name|isDirectory
argument_list|)
operator|.
name|map
argument_list|(
name|p
lambda|->
name|toVersion
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|p
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|this
operator|::
name|hasArtifact
argument_list|)
operator|.
name|map
argument_list|(
name|ref
lambda|->
name|ref
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not read directory {}: {}"
argument_list|,
name|projDir
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|( )
operator|instanceof
name|LayoutException
condition|)
block|{
throw|throw
operator|(
name|LayoutException
operator|)
name|e
operator|.
name|getCause
argument_list|( )
throw|;
block|}
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
end_function

begin_function
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
block|{
try|try
init|(
name|Stream
argument_list|<
name|ArtifactReference
argument_list|>
name|stream
init|=
name|getArtifactStream
argument_list|(
name|reference
argument_list|)
init|)
block|{
return|return
name|stream
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|map
argument_list|(
name|ar
lambda|->
name|ar
operator|.
name|getVersion
argument_list|( )
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|( )
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|LayoutException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|boolean
name|hasContent
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
name|StorageAsset
name|artifactFile
init|=
name|toFile
argument_list|(
name|reference
argument_list|)
decl_stmt|;
return|return
name|artifactFile
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|artifactFile
operator|.
name|isContainer
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|boolean
name|hasContent
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
block|{
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|versions
init|=
name|getVersions
argument_list|(
name|reference
argument_list|)
decl_stmt|;
return|return
operator|!
name|versions
operator|.
name|isEmpty
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ContentNotFoundException
decl||
name|LayoutException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|boolean
name|hasContent
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|getFirstArtifact
argument_list|(
name|reference
argument_list|)
operator|!=
literal|null
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|LayoutException
decl||
name|ContentNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|void
name|setRepository
parameter_list|(
specifier|final
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
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|repository
operator|instanceof
name|EditableManagedRepository
condition|)
block|{
operator|(
operator|(
name|EditableManagedRepository
operator|)
name|repository
operator|)
operator|.
name|setContent
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_function

begin_function
specifier|private
name|Path
name|getRepoDir
parameter_list|()
block|{
return|return
name|repository
operator|.
name|getAsset
argument_list|(
literal|""
argument_list|)
operator|.
name|getFilePath
argument_list|( )
return|;
block|}
end_function

begin_comment
comment|/**      * Convert a path to an artifact reference.      *      * @param path the path to convert. (relative or full location path)      * @throws LayoutException if the path cannot be converted to an artifact reference.      */
end_comment

begin_function
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
name|String
name|repoPath
init|=
name|convertUriToPath
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|path
operator|!=
literal|null
operator|)
operator|&&
name|path
operator|.
name|startsWith
argument_list|(
name|repoPath
argument_list|)
operator|&&
name|repoPath
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|super
operator|.
name|toArtifactReference
argument_list|(
name|path
operator|.
name|substring
argument_list|(
name|repoPath
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|repoPath
operator|=
name|path
expr_stmt|;
if|if
condition|(
name|repoPath
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|repoPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|repoPath
operator|=
name|repoPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|super
operator|.
name|toArtifactReference
argument_list|(
name|repoPath
argument_list|)
return|;
block|}
block|}
end_function

begin_comment
comment|// The variant with runtime exception for stream usage
end_comment

begin_function
specifier|private
name|ArtifactReference
name|toArtifactRef
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
return|return
name|toArtifactReference
argument_list|(
name|path
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
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
name|repository
operator|.
name|getAsset
argument_list|(
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_function
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
name|repository
operator|.
name|getAsset
argument_list|(
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**      * Get the first Artifact found in the provided VersionedReference location.      *      * @param reference the reference to the versioned reference to search within      * @return the ArtifactReference to the first artifact located within the versioned reference. or null if      *         no artifact was found within the versioned reference.      * @throws java.io.IOException     if the versioned reference is invalid (example: doesn't exist, or isn't a directory)      * @throws LayoutException      */
end_comment

begin_function
specifier|private
name|ArtifactReference
name|getFirstArtifact
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
throws|,
name|IOException
block|{
try|try
init|(
name|Stream
argument_list|<
name|ArtifactReference
argument_list|>
name|stream
init|=
name|getArtifactStream
argument_list|(
name|reference
argument_list|)
init|)
block|{
return|return
name|stream
operator|.
name|findFirst
argument_list|( )
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
operator|.
name|getCause
argument_list|( )
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
specifier|private
name|Stream
argument_list|<
name|ArtifactReference
argument_list|>
name|getArtifactStream
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
throws|,
name|IOException
block|{
specifier|final
name|Path
name|repoBase
init|=
name|getRepoDir
argument_list|( )
decl_stmt|;
name|String
name|path
init|=
name|toMetadataPath
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|Path
name|versionDir
init|=
name|repoBase
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|versionDir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to gather the list of artifacts on a non-existant directory: "
operator|+
name|versionDir
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|Files
operator|.
name|isDirectory
argument_list|(
name|versionDir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to gather the list of snapshot versions on a non-directory: "
operator|+
name|versionDir
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|Files
operator|.
name|list
argument_list|(
name|versionDir
argument_list|)
operator|.
name|filter
argument_list|(
name|Files
operator|::
name|isRegularFile
argument_list|)
operator|.
name|map
argument_list|(
name|p
lambda|->
name|repoBase
operator|.
name|relativize
argument_list|(
name|p
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|filter
argument_list|(
name|p
lambda|->
operator|!
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
name|p
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|filetypes
operator|::
name|matchesArtifactPattern
argument_list|)
operator|.
name|map
argument_list|(
name|this
operator|::
name|toArtifactRef
argument_list|)
return|;
block|}
end_function

begin_function
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
block|{
try|try
init|(
name|Stream
argument_list|<
name|ArtifactReference
argument_list|>
name|stream
init|=
name|getArtifactStream
argument_list|(
name|reference
argument_list|)
init|)
block|{
return|return
name|stream
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|( )
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not access the repository files: "
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
specifier|private
name|boolean
name|hasArtifact
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
block|{
try|try
init|(
name|Stream
argument_list|<
name|ArtifactReference
argument_list|>
name|stream
init|=
name|getArtifactStream
argument_list|(
name|reference
argument_list|)
init|)
block|{
return|return
name|stream
operator|.
name|anyMatch
argument_list|(
name|e
lambda|->
literal|true
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ContentNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|LayoutException
decl||
name|IOException
name|e
parameter_list|)
block|{
comment|// We throw the runtime exception for better stream handling
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
specifier|public
name|void
name|setFiletypes
parameter_list|(
name|FileTypes
name|filetypes
parameter_list|)
block|{
name|this
operator|.
name|filetypes
operator|=
name|filetypes
expr_stmt|;
block|}
end_function

unit|}
end_unit

