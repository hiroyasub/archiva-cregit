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
comment|/*   * Copyright 2001-2005 The Apache Software Foundation.   *   * Licensed under the Apache License, Version 2.0 (the "License");   * you may not use this file except in compliance with the License.   * You may obtain a copy of the License at   *   *      http://www.apache.org/licenses/LICENSE-2.0   *   * Unless required by applicable law or agreed to in writing, software   * distributed under the License is distributed on an "AS IS" BASIS,   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   * See the License for the specific language governing permissions and   * limitations under the License.   */
end_comment

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
name|Iterator
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
name|factory
operator|.
name|ArtifactFactory
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
name|manager
operator|.
name|WagonManager
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
name|RepositoryMetadata
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
name|Versioning
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
name|repository
operator|.
name|RepositoryFileFilter
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
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|wagon
operator|.
name|TransferFailedException
import|;
end_import

begin_comment
comment|/**  * This class will report on bad metadata files.  These include invalid version declarations and incomplete version  * information inside the metadata file.  Plugin metadata will be checked for validity of the latest plugin artifacts.  *  */
end_comment

begin_class
specifier|public
class|class
name|BadMetadataReporter
implements|implements
name|MetadataReportProcessor
block|{
specifier|private
name|WagonManager
name|wagon
decl_stmt|;
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|public
name|void
name|processMetadata
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ArtifactReporter
name|reporter
parameter_list|)
block|{
name|boolean
name|hasFailures
init|=
literal|false
decl_stmt|;
name|String
name|lastUpdated
init|=
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|getVersioning
argument_list|()
operator|.
name|getLastUpdated
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastUpdated
operator|==
literal|null
operator|||
name|lastUpdated
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Missing lastUpdated element inside the metadata."
argument_list|)
expr_stmt|;
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|metadata
operator|.
name|storedInGroupDirectory
argument_list|()
condition|)
block|{
name|checkPluginMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|metadata
operator|.
name|storedInArtifactVersionDirectory
argument_list|()
condition|)
block|{
comment|//snapshot metadata
block|}
else|else
block|{
if|if
condition|(
operator|!
name|checkMetadataVersions
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
condition|)
name|hasFailures
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|checkRepositoryVersions
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
condition|)
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasFailures
condition|)
name|reporter
operator|.
name|addSuccess
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks the plugin metadata      */
specifier|public
name|boolean
name|checkPluginMetadata
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ArtifactReporter
name|reporter
parameter_list|)
block|{
name|boolean
name|hasFailures
init|=
literal|false
decl_stmt|;
return|return
name|hasFailures
return|;
block|}
comment|/**      * Checks the snapshot metadata      */
specifier|public
name|boolean
name|checkSnapshotMetadata
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ArtifactReporter
name|reporter
parameter_list|)
block|{
name|boolean
name|hasFailures
init|=
literal|false
decl_stmt|;
name|Snapshot
name|snapshot
init|=
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|getVersioning
argument_list|()
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
name|String
name|timestamp
init|=
name|snapshot
operator|.
name|getTimestamp
argument_list|()
decl_stmt|;
name|String
name|buildNumber
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|snapshot
operator|.
name|getBuildNumber
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|artifactName
init|=
name|metadata
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|timestamp
operator|+
literal|"-"
operator|+
name|buildNumber
operator|+
literal|".pom"
decl_stmt|;
comment|//@todo use wagon instead
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|snapshotFile
init|=
operator|new
name|File
argument_list|(
name|artifactFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|artifactName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|snapshotFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Snapshot artifact "
operator|+
name|artifactName
operator|+
literal|" does not exist."
argument_list|)
expr_stmt|;
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|hasFailures
return|;
block|}
comment|/**      * Checks the declared metadata versions if the artifacts are present in the repository      */
specifier|public
name|boolean
name|checkMetadataVersions
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ArtifactReporter
name|reporter
parameter_list|)
block|{
name|boolean
name|hasFailures
init|=
literal|false
decl_stmt|;
name|Versioning
name|versioning
init|=
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|getVersioning
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|versions
init|=
name|versioning
operator|.
name|getVersions
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|versions
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|version
init|=
operator|(
name|String
operator|)
name|versions
operator|.
name|next
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
name|metadata
argument_list|,
name|version
argument_list|)
decl_stmt|;
try|try
block|{
name|wagon
operator|.
name|getArtifact
argument_list|(
name|artifact
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransferFailedException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"An error occurred during the transfer of the artifact in "
operator|+
literal|"the repository."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
comment|//do nothing, will check later that this artifact has not been resolved
block|}
if|if
condition|(
operator|!
name|artifact
operator|.
name|isResolved
argument_list|()
condition|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Artifact version "
operator|+
name|version
operator|+
literal|" is present in metadata but "
operator|+
literal|"missing in the repository."
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hasFailures
condition|)
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|hasFailures
return|;
block|}
comment|/**      * Searches the artifact repository directory for all versions and verifies that all of them are listed in the       * metadata file.      */
specifier|public
name|boolean
name|checkRepositoryVersions
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ArtifactReporter
name|reporter
parameter_list|)
block|{
name|boolean
name|hasFailures
init|=
literal|false
decl_stmt|;
name|Versioning
name|versioning
init|=
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|getVersioning
argument_list|()
decl_stmt|;
name|String
name|repositoryPath
init|=
name|repository
operator|.
name|getBasedir
argument_list|()
decl_stmt|;
name|File
name|versionsDir
init|=
operator|new
name|File
argument_list|(
name|repositoryPath
argument_list|,
name|formatAsDirectory
argument_list|(
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|+
name|File
operator|.
name|pathSeparator
operator|+
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|)
decl_stmt|;
name|File
index|[]
name|versions
init|=
name|versionsDir
operator|.
name|listFiles
argument_list|(
operator|new
name|RepositoryFileFilter
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
name|versions
operator|.
name|length
condition|;
name|idx
operator|++
control|)
block|{
name|String
name|version
init|=
name|versions
index|[
name|idx
index|]
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|versioning
operator|.
name|getVersions
argument_list|()
operator|.
name|contains
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Artifact version "
operator|+
name|version
operator|+
literal|" found in the repository but "
operator|+
literal|"missing in the metadata."
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hasFailures
condition|)
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|hasFailures
return|;
block|}
comment|/**      * Formats an artifact groupId to the directory structure format used for storage in repositories      */
specifier|private
name|String
name|formatAsDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
return|return
name|directory
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
name|File
operator|.
name|pathSeparatorChar
argument_list|)
return|;
block|}
specifier|private
name|Artifact
name|createArtifact
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|)
block|{
return|return
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|metadata
operator|.
name|getBaseVersion
argument_list|()
argument_list|,
literal|"pom"
argument_list|)
return|;
block|}
specifier|private
name|Artifact
name|createArtifact
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|version
argument_list|,
literal|"pom"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

