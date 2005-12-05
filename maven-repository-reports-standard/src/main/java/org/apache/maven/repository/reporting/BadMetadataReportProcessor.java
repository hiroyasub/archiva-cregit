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
name|Plugin
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
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
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
name|HashMap
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

begin_comment
comment|/**  * This class will report on bad metadata files.  These include invalid version declarations and incomplete version  * information inside the metadata file.  Plugin metadata will be checked for validity of the latest plugin artifacts.  */
end_comment

begin_class
specifier|public
class|class
name|BadMetadataReportProcessor
implements|implements
name|MetadataReportProcessor
block|{
comment|// plexus components
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
name|RepositoryQueryLayerFactory
name|repositoryQueryLayerFactory
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
throws|throws
name|ReportProcessorException
block|{
name|boolean
name|hasFailures
init|=
literal|false
decl_stmt|;
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
else|else
block|{
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
name|storedInArtifactVersionDirectory
argument_list|()
condition|)
block|{
name|checkSnapshotMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
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
block|{
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
try|try
block|{
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
block|{
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ReportProcessorException
argument_list|(
literal|"Error getting versions"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|hasFailures
condition|)
block|{
name|reporter
operator|.
name|addSuccess
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Checks the plugin metadata      */
specifier|protected
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
name|File
name|metadataDir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOfRemoteRepositoryMetadata
argument_list|(
name|metadata
argument_list|)
argument_list|)
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|HashMap
name|prefixes
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|plugins
init|=
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|plugins
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Plugin
name|plugin
init|=
operator|(
name|Plugin
operator|)
name|plugins
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|artifactId
init|=
name|plugin
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifactId
operator|==
literal|null
operator|||
name|artifactId
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
literal|"Missing or empty artifactId in group metadata."
argument_list|)
expr_stmt|;
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
name|String
name|prefix
init|=
name|plugin
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
operator|||
name|prefix
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
literal|"Missing or empty plugin prefix for artifactId "
operator|+
name|artifactId
argument_list|)
expr_stmt|;
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|prefixes
operator|.
name|containsKey
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Duplicate plugin prefix found: "
operator|+
name|prefix
operator|+
literal|"."
argument_list|)
expr_stmt|;
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|prefixes
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|plugin
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|artifactId
operator|!=
literal|null
condition|)
block|{
name|File
name|pluginDir
init|=
operator|new
name|File
argument_list|(
name|metadataDir
argument_list|,
name|artifactId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pluginDir
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
literal|"Metadata plugin "
operator|+
name|artifactId
operator|+
literal|" is not present in the repository"
argument_list|)
expr_stmt|;
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
return|return
name|hasFailures
return|;
block|}
comment|/**      * Checks the snapshot metadata      */
specifier|protected
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
name|RepositoryQueryLayer
name|repositoryQueryLayer
init|=
name|repositoryQueryLayerFactory
operator|.
name|createRepositoryQueryLayer
argument_list|(
name|repository
argument_list|)
decl_stmt|;
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
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|repositoryQueryLayer
operator|.
name|containsArtifact
argument_list|(
name|artifact
argument_list|,
name|snapshot
argument_list|)
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
name|timestamp
operator|+
literal|"-"
operator|+
name|buildNumber
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
specifier|protected
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
name|RepositoryQueryLayer
name|repositoryQueryLayer
init|=
name|repositoryQueryLayerFactory
operator|.
name|createRepositoryQueryLayer
argument_list|(
name|repository
argument_list|)
decl_stmt|;
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
if|if
condition|(
operator|!
name|repositoryQueryLayer
operator|.
name|containsArtifact
argument_list|(
name|artifact
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
block|{
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
return|return
name|hasFailures
return|;
block|}
comment|/**      * Searches the artifact repository directory for all versions and verifies that all of them are listed in the      * metadata file.      */
specifier|protected
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
throws|throws
name|IOException
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
comment|// TODO: change this to look for repository artifacts. It needs to centre around that I think, currently this is hardwired to the default layout
name|File
name|versionsDir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOfRemoteRepositoryMetadata
argument_list|(
name|metadata
argument_list|)
argument_list|)
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|List
name|versions
init|=
name|FileUtils
operator|.
name|getFileNames
argument_list|(
name|versionsDir
argument_list|,
literal|"*/*.pom"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|versions
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|File
name|path
init|=
operator|new
name|File
argument_list|(
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|path
operator|.
name|getParentFile
argument_list|()
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
block|{
name|hasFailures
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
return|return
name|hasFailures
return|;
block|}
comment|/**      * Used to create an artifact object from a metadata base version      */
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
comment|/**      * Used to create an artifact object with a specified version      */
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

