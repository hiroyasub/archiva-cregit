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
name|admin
operator|.
name|model
operator|.
name|beans
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
name|layout
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
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|io
operator|.
name|IOException
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
name|Set
import|;
end_import

begin_comment
comment|/**  * ManagedDefaultRepositoryContent  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"managedRepositoryContent#default"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|ManagedDefaultRepositoryContent
extends|extends
name|AbstractDefaultRepositoryContent
implements|implements
name|ManagedRepositoryContent
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"fileTypes"
argument_list|)
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|private
name|ManagedRepository
name|repository
decl_stmt|;
specifier|public
name|ManagedDefaultRepositoryContent
parameter_list|()
block|{
comment|// default to use if there are none supplied as components
name|this
operator|.
name|artifactMappingProviders
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|DefaultArtifactMappingProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleteVersion
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
block|{
name|String
name|path
init|=
name|toMetadataPath
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|File
name|projectPath
init|=
operator|new
name|File
argument_list|(
name|getRepoRoot
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|File
name|projectDir
init|=
name|projectPath
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|projectDir
operator|.
name|exists
argument_list|()
operator|&&
name|projectDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|projectDir
argument_list|)
expr_stmt|;
block|}
block|}
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
throws|,
name|ContentNotFoundException
block|{
name|ArtifactReference
name|artifactReference
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|artifactReference
operator|.
name|setGroupId
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|artifactReference
operator|.
name|setArtifactId
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|toPath
argument_list|(
name|artifactReference
argument_list|)
decl_stmt|;
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|getRepoRoot
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|directory
operator|.
name|exists
argument_list|()
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
name|directory
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
try|try
block|{
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
specifier|public
name|void
name|deleteArtifact
parameter_list|(
name|ArtifactReference
name|artifactReference
parameter_list|)
block|{
name|String
name|path
init|=
name|toPath
argument_list|(
name|artifactReference
argument_list|)
decl_stmt|;
name|File
name|filePath
init|=
operator|new
name|File
argument_list|(
name|getRepoRoot
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|filePath
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|filePath
argument_list|)
expr_stmt|;
block|}
name|File
name|filePathmd5
init|=
operator|new
name|File
argument_list|(
name|getRepoRoot
argument_list|()
argument_list|,
name|path
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
if|if
condition|(
name|filePathmd5
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|filePathmd5
argument_list|)
expr_stmt|;
block|}
name|File
name|filePathsha1
init|=
operator|new
name|File
argument_list|(
name|getRepoRoot
argument_list|()
argument_list|,
name|path
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
if|if
condition|(
name|filePathsha1
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|filePathsha1
argument_list|)
expr_stmt|;
block|}
block|}
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
name|String
name|path
init|=
name|StringUtils
operator|.
name|replaceChars
argument_list|(
name|groupId
argument_list|,
literal|'.'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|getRepoRoot
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|directory
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
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
operator|.
name|getPath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
specifier|public
name|Set
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
block|{
name|File
name|artifactFile
init|=
name|toFile
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|File
name|repoDir
init|=
name|artifactFile
operator|.
name|getParentFile
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
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|repoDir
operator|.
name|isDirectory
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
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|ArtifactReference
argument_list|>
name|foundArtifacts
init|=
operator|new
name|HashSet
argument_list|<
name|ArtifactReference
argument_list|>
argument_list|()
decl_stmt|;
comment|// First gather up the versions found as artifacts in the managed repository.
name|File
name|repoFiles
index|[]
init|=
name|repoDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|repoFiles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|repoFiles
index|[
name|i
index|]
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Skip it. it's a directory.
continue|continue;
block|}
name|String
name|relativePath
init|=
name|PathUtil
operator|.
name|getRelative
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|repoFiles
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
name|relativePath
argument_list|)
condition|)
block|{
try|try
block|{
name|ArtifactReference
name|artifact
init|=
name|toArtifactReference
argument_list|(
name|relativePath
argument_list|)
decl_stmt|;
comment|// Test for related, groupId / artifactId / version must match.
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
name|foundArtifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
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
block|}
block|}
block|}
return|return
name|foundArtifacts
return|;
block|}
specifier|public
name|String
name|getRepoRoot
parameter_list|()
block|{
return|return
name|repository
operator|.
name|getLocation
argument_list|()
return|;
block|}
specifier|public
name|ManagedRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
comment|/**      * Gather the Available Versions (on disk) for a specific Project Reference, based on filesystem      * information.      *      * @return the Set of available versions, based on the project reference.      * @throws LayoutException      * @throws LayoutException      */
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
name|String
name|path
init|=
name|toMetadataPath
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|path
argument_list|)
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
literal|"Unable to get Versions on a non-existant directory: "
operator|+
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|repoDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get Versions on a non-directory: "
operator|+
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|foundVersions
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|VersionedReference
name|versionRef
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|versionRef
operator|.
name|setGroupId
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|versionRef
operator|.
name|setArtifactId
argument_list|(
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|repoFiles
index|[]
init|=
name|repoDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|repoFiles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|repoFiles
index|[
name|i
index|]
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Skip it. not a directory.
continue|continue;
block|}
comment|// Test if dir has an artifact, which proves to us that it is a valid version directory.
name|String
name|version
init|=
name|repoFiles
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
decl_stmt|;
name|versionRef
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasArtifact
argument_list|(
name|versionRef
argument_list|)
condition|)
block|{
comment|// Found an artifact, must be a valid version.
name|foundVersions
operator|.
name|add
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|foundVersions
return|;
block|}
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
name|String
name|path
init|=
name|toMetadataPath
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|path
argument_list|)
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
literal|"Unable to get versions on a non-existant directory: "
operator|+
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|repoDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get versions on a non-directory: "
operator|+
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|foundVersions
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// First gather up the versions found as artifacts in the managed repository.
name|File
name|repoFiles
index|[]
init|=
name|repoDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|repoFiles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|repoFiles
index|[
name|i
index|]
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Skip it. it's a directory.
continue|continue;
block|}
name|String
name|relativePath
init|=
name|PathUtil
operator|.
name|getRelative
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|repoFiles
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
name|relativePath
argument_list|)
condition|)
block|{
comment|// Skip it, it's metadata or similar
continue|continue;
block|}
if|if
condition|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
name|relativePath
argument_list|)
condition|)
block|{
try|try
block|{
name|ArtifactReference
name|artifact
init|=
name|toArtifactReference
argument_list|(
name|relativePath
argument_list|)
decl_stmt|;
name|foundVersions
operator|.
name|add
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
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
block|}
block|}
block|}
return|return
name|foundVersions
return|;
block|}
specifier|public
name|boolean
name|hasContent
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
name|File
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
name|artifactFile
operator|.
name|isFile
argument_list|()
return|;
block|}
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
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
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
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
comment|/**      * Convert a path to an artifact reference.      *      * @param path the path to convert. (relative or full location path)      * @throws LayoutException if the path cannot be converted to an artifact reference.      */
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
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
operator|&&
name|repository
operator|.
name|getLocation
argument_list|()
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
name|repository
operator|.
name|getLocation
argument_list|()
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
return|;
block|}
specifier|public
name|File
name|toFile
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|File
name|toFile
parameter_list|(
name|ArchivaArtifact
name|reference
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Get the first Artifact found in the provided VersionedReference location.      *      * @param reference the reference to the versioned reference to search within      * @return the ArtifactReference to the first artifact located within the versioned reference. or null if      *         no artifact was found within the versioned reference.      * @throws IOException     if the versioned reference is invalid (example: doesn't exist, or isn't a directory)      * @throws LayoutException      */
specifier|private
name|ArtifactReference
name|getFirstArtifact
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|LayoutException
throws|,
name|IOException
block|{
name|String
name|path
init|=
name|toMetadataPath
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|path
argument_list|)
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
name|IOException
argument_list|(
literal|"Unable to gather the list of snapshot versions on a non-existant directory: "
operator|+
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|repoDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to gather the list of snapshot versions on a non-directory: "
operator|+
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|File
name|repoFiles
index|[]
init|=
name|repoDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|repoFiles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|repoFiles
index|[
name|i
index|]
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Skip it. it's a directory.
continue|continue;
block|}
name|String
name|relativePath
init|=
name|PathUtil
operator|.
name|getRelative
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|repoFiles
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
name|relativePath
argument_list|)
condition|)
block|{
name|ArtifactReference
name|artifact
init|=
name|toArtifactReference
argument_list|(
name|relativePath
argument_list|)
decl_stmt|;
return|return
name|artifact
return|;
block|}
block|}
comment|// No artifact was found.
return|return
literal|null
return|;
block|}
specifier|private
name|boolean
name|hasArtifact
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|LayoutException
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
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
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
block|}
end_class

end_unit

