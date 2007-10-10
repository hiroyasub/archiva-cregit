begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
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
name|commons
operator|.
name|collections
operator|.
name|CollectionUtils
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
name|apache
operator|.
name|maven
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
name|maven
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|maven
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
name|maven
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
name|maven
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
name|maven
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
name|maven
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
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|SelectorUtils
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
name|ArrayList
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
name|Iterator
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

begin_comment
comment|/**  * ManagedLegacyRepositoryContent   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component   *      role="org.apache.maven.archiva.repository.ManagedRepositoryContent"  *      role-hint="legacy"  *      instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ManagedLegacyRepositoryContent
extends|extends
name|AbstractLegacyRepositoryContent
implements|implements
name|ManagedRepositoryContent
implements|,
name|Initializable
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|repository
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|artifactPatterns
decl_stmt|;
specifier|public
name|void
name|deleteVersion
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
block|{
name|File
name|groupDir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|groupDir
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get versions using a non-existant groupId directory: "
operator|+
name|groupDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|groupDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get versions using a non-directory: "
operator|+
name|groupDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
comment|// First gather up the versions found as artifacts in the managed repository.
name|File
name|typeDirs
index|[]
init|=
name|groupDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|typeDir
range|:
name|typeDirs
control|)
block|{
if|if
condition|(
operator|!
name|typeDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Skip it, we only care about directories.
continue|continue;
block|}
if|if
condition|(
operator|!
name|typeDir
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"s"
argument_list|)
condition|)
block|{
comment|// Skip it, we only care about directories that end in "s".
block|}
name|deleteVersions
argument_list|(
name|typeDir
argument_list|,
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|deleteVersions
parameter_list|(
name|File
name|typeDir
parameter_list|,
name|VersionedReference
name|reference
parameter_list|)
block|{
name|File
name|repoFiles
index|[]
init|=
name|typeDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|repoFile
range|:
name|repoFiles
control|)
block|{
if|if
condition|(
name|repoFile
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
name|repoFile
argument_list|)
decl_stmt|;
if|if
condition|(
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
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|repoFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|deleteSupportFiles
argument_list|(
name|repoFile
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
comment|/* don't fail the process if there is a bad artifact within the directory. */
block|}
block|}
block|}
block|}
specifier|private
name|void
name|deleteSupportFiles
parameter_list|(
name|File
name|repoFile
parameter_list|)
block|{
name|deleteSupportFile
argument_list|(
name|repoFile
argument_list|,
literal|".sha1"
argument_list|)
expr_stmt|;
name|deleteSupportFile
argument_list|(
name|repoFile
argument_list|,
literal|".md5"
argument_list|)
expr_stmt|;
name|deleteSupportFile
argument_list|(
name|repoFile
argument_list|,
literal|".asc"
argument_list|)
expr_stmt|;
name|deleteSupportFile
argument_list|(
name|repoFile
argument_list|,
literal|".gpg"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|deleteSupportFile
parameter_list|(
name|File
name|repoFile
parameter_list|,
name|String
name|supportExtension
parameter_list|)
block|{
name|File
name|supportFile
init|=
operator|new
name|File
argument_list|(
name|repoFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
name|supportExtension
argument_list|)
decl_stmt|;
if|if
condition|(
name|supportFile
operator|.
name|exists
argument_list|()
operator|&&
name|supportFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|supportFile
operator|.
name|delete
argument_list|()
expr_stmt|;
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
throws|,
name|LayoutException
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
name|projectParentDir
init|=
name|repoDir
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|File
name|typeDirs
index|[]
init|=
name|projectParentDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|typeDir
range|:
name|typeDirs
control|)
block|{
if|if
condition|(
operator|!
name|typeDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Skip it, we only care about directories.
continue|continue;
block|}
if|if
condition|(
operator|!
name|typeDir
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"s"
argument_list|)
condition|)
block|{
comment|// Skip it, we only care about directories that end in "s".
block|}
name|getRelatedArtifacts
argument_list|(
name|typeDir
argument_list|,
name|reference
argument_list|,
name|foundArtifacts
argument_list|)
expr_stmt|;
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
name|ManagedRepositoryConfiguration
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
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
block|{
name|File
name|groupDir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|groupDir
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get versions using a non-existant groupId directory: "
operator|+
name|groupDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|groupDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get versions using a non-directory: "
operator|+
name|groupDir
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
name|typeDirs
index|[]
init|=
name|groupDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|typeDir
range|:
name|typeDirs
control|)
block|{
if|if
condition|(
operator|!
name|typeDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Skip it, we only care about directories.
continue|continue;
block|}
if|if
condition|(
operator|!
name|typeDir
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"s"
argument_list|)
condition|)
block|{
comment|// Skip it, we only care about directories that end in "s".
block|}
name|getProjectVersions
argument_list|(
name|typeDir
argument_list|,
name|reference
argument_list|,
name|foundVersions
argument_list|)
expr_stmt|;
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
name|File
name|groupDir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|groupDir
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get versions using a non-existant groupId directory: "
operator|+
name|groupDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|groupDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
literal|"Unable to get versions using a non-directory: "
operator|+
name|groupDir
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
name|typeDirs
index|[]
init|=
name|groupDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|typeDir
range|:
name|typeDirs
control|)
block|{
if|if
condition|(
operator|!
name|typeDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// Skip it, we only care about directories.
continue|continue;
block|}
if|if
condition|(
operator|!
name|typeDir
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"s"
argument_list|)
condition|)
block|{
comment|// Skip it, we only care about directories that end in "s".
block|}
name|getVersionedVersions
argument_list|(
name|typeDir
argument_list|,
name|reference
argument_list|,
name|foundVersions
argument_list|)
expr_stmt|;
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
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|versions
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
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|versions
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
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|this
operator|.
name|artifactPatterns
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|initVariables
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|ManagedRepositoryConfiguration
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
comment|/**      * Convert a path to an artifact reference.      *       * @param path the path to convert. (relative or full location path)      * @throws LayoutException if the path cannot be converted to an artifact reference.      */
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
name|String
name|toMetadataPath
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
block|{
comment|// No metadata present in legacy repository.
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|toMetadataPath
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
block|{
comment|// No metadata present in legacy repository.
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|getProjectVersions
parameter_list|(
name|File
name|typeDir
parameter_list|,
name|ProjectReference
name|reference
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|foundVersions
parameter_list|)
block|{
name|File
name|repoFiles
index|[]
init|=
name|typeDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|repoFile
range|:
name|repoFiles
control|)
block|{
if|if
condition|(
name|repoFile
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
name|repoFile
argument_list|)
decl_stmt|;
if|if
condition|(
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
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
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
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* don't fail the process if there is a bad artifact within the directory. */
block|}
block|}
block|}
block|}
specifier|private
name|void
name|getRelatedArtifacts
parameter_list|(
name|File
name|typeDir
parameter_list|,
name|ArtifactReference
name|reference
parameter_list|,
name|Set
argument_list|<
name|ArtifactReference
argument_list|>
name|foundArtifacts
parameter_list|)
block|{
name|File
name|repoFiles
index|[]
init|=
name|typeDir
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
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
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
name|startsWith
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
comment|/* don't fail the process if there is a bad artifact within the directory. */
block|}
block|}
block|}
block|}
specifier|private
name|void
name|getVersionedVersions
parameter_list|(
name|File
name|typeDir
parameter_list|,
name|VersionedReference
name|reference
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|foundVersions
parameter_list|)
block|{
name|File
name|repoFiles
index|[]
init|=
name|typeDir
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
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
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
name|startsWith
argument_list|(
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
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
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* don't fail the process if there is a bad artifact within the directory. */
block|}
block|}
block|}
block|}
specifier|private
name|void
name|initVariables
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
operator|.
name|artifactPatterns
init|)
block|{
name|this
operator|.
name|artifactPatterns
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|artifactPatterns
operator|.
name|addAll
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|matchesArtifactPattern
parameter_list|(
name|String
name|relativePath
parameter_list|)
block|{
comment|// Correct the slash pattern.
name|relativePath
operator|=
name|relativePath
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|this
operator|.
name|artifactPatterns
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|pattern
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|SelectorUtils
operator|.
name|matchPath
argument_list|(
name|pattern
argument_list|,
name|relativePath
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// Found match
return|return
literal|true
return|;
block|}
block|}
comment|// No match.
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

