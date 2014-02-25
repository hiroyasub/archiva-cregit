begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|core
operator|.
name|repository
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
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|MetadataRepository
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
name|RepositorySession
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
name|events
operator|.
name|RepositoryListener
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
name|VersionComparator
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
name|RepositoryContentFactory
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
name|RepositoryNotFoundException
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
name|archiva
operator|.
name|repository
operator|.
name|metadata
operator|.
name|MetadataTools
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
name|metadata
operator|.
name|RepositoryMetadataException
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
name|ArrayList
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
name|List
import|;
end_import

begin_comment
comment|/**  *<p>  * This will look in a single managed repository, and purge any snapshots that are present  * that have a corresponding released version on the same repository.  *</p>  *<p>  * So, if you have the following (presented in the m2/default layout form) ...  *<pre>  *   /com/foo/foo-tool/1.0-SNAPSHOT/foo-tool-1.0-SNAPSHOT.jar  *   /com/foo/foo-tool/1.1-SNAPSHOT/foo-tool-1.1-SNAPSHOT.jar  *   /com/foo/foo-tool/1.2.1-SNAPSHOT/foo-tool-1.2.1-SNAPSHOT.jar  *   /com/foo/foo-tool/1.2.1/foo-tool-1.2.1.jar  *   /com/foo/foo-tool/2.0-SNAPSHOT/foo-tool-2.0-SNAPSHOT.jar  *   /com/foo/foo-tool/2.0/foo-tool-2.0.jar  *   /com/foo/foo-tool/2.1-SNAPSHOT/foo-tool-2.1-SNAPSHOT.jar  *</pre>  * then the current highest ranked released (non-snapshot) version is 2.0, which means  * the snapshots from 1.0-SNAPSHOT, 1.1-SNAPSHOT, 1.2.1-SNAPSHOT, and 2.0-SNAPSHOT can  * be purged.  Leaving 2.1-SNAPSHOT in alone.  */
end_comment

begin_class
specifier|public
class|class
name|CleanupReleasedSnapshotsRepositoryPurge
extends|extends
name|AbstractRepositoryPurge
block|{
specifier|private
name|MetadataTools
name|metadataTools
decl_stmt|;
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
specifier|private
name|RepositoryContentFactory
name|repoContentFactory
decl_stmt|;
specifier|public
name|CleanupReleasedSnapshotsRepositoryPurge
parameter_list|(
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|MetadataTools
name|metadataTools
parameter_list|,
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
parameter_list|,
name|RepositoryContentFactory
name|repoContentFactory
parameter_list|,
name|RepositorySession
name|repositorySession
parameter_list|,
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|super
argument_list|(
name|repository
argument_list|,
name|repositorySession
argument_list|,
name|listeners
argument_list|)
expr_stmt|;
name|this
operator|.
name|metadataTools
operator|=
name|metadataTools
expr_stmt|;
name|this
operator|.
name|managedRepositoryAdmin
operator|=
name|managedRepositoryAdmin
expr_stmt|;
name|this
operator|.
name|repoContentFactory
operator|=
name|repoContentFactory
expr_stmt|;
block|}
specifier|public
name|void
name|process
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|RepositoryPurgeException
block|{
try|try
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getRepoRoot
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactFile
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// Nothing to do here, file doesn't exist, skip it.
return|return;
block|}
name|ArtifactReference
name|artifactRef
init|=
name|repository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|artifactRef
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
comment|// Nothing to do here, not a snapshot, skip it.
return|return;
block|}
name|ProjectReference
name|reference
init|=
operator|new
name|ProjectReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
name|artifactRef
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|artifactRef
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Gether the released versions
name|List
argument_list|<
name|String
argument_list|>
name|releasedVersions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|repos
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepository
name|repo
range|:
name|repos
control|)
block|{
if|if
condition|(
name|repo
operator|.
name|isReleases
argument_list|()
condition|)
block|{
try|try
block|{
name|ManagedRepositoryContent
name|repoContent
init|=
name|repoContentFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|version
range|:
name|repoContent
operator|.
name|getVersions
argument_list|(
name|reference
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|releasedVersions
operator|.
name|add
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
comment|// swallow
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
comment|// swallow
block|}
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|releasedVersions
argument_list|,
name|VersionComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now clean out any version that is earlier than the highest released version.
name|boolean
name|needsMetadataUpdate
init|=
literal|false
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
name|artifactRef
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|versionRef
operator|.
name|setArtifactId
argument_list|(
name|artifactRef
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|releasedVersions
operator|.
name|contains
argument_list|(
name|VersionUtil
operator|.
name|getReleaseVersion
argument_list|(
name|artifactRef
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|versionRef
operator|.
name|setVersion
argument_list|(
name|artifactRef
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|repository
operator|.
name|deleteVersion
argument_list|(
name|versionRef
argument_list|)
expr_stmt|;
comment|// FIXME: looks incomplete, might not delete related metadata?
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|artifactRef
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifactRef
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifactRef
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifactFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|needsMetadataUpdate
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|needsMetadataUpdate
condition|)
block|{
name|updateMetadata
argument_list|(
name|artifactRef
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryPurgeException
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
catch|catch
parameter_list|(
name|ContentNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryPurgeException
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
specifier|private
name|void
name|updateMetadata
parameter_list|(
name|ArtifactReference
name|artifact
parameter_list|)
block|{
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
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|versionRef
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|versionRef
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectReference
name|projectRef
init|=
operator|new
name|ProjectReference
argument_list|()
decl_stmt|;
name|projectRef
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|projectRef
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|metadataTools
operator|.
name|updateMetadata
argument_list|(
name|repository
argument_list|,
name|versionRef
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ContentNotFoundException
name|e
parameter_list|)
block|{
comment|// Ignore. (Just means we have no snapshot versions left to reference).
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
comment|// Ignore.
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore.
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// Ignore.
block|}
try|try
block|{
name|metadataTools
operator|.
name|updateMetadata
argument_list|(
name|repository
argument_list|,
name|projectRef
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ContentNotFoundException
name|e
parameter_list|)
block|{
comment|// Ignore. (Just means we have no snapshot versions left to reference).
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
comment|// Ignore.
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore.
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// Ignore.
block|}
block|}
block|}
end_class

end_unit

