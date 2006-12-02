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
name|reporting
operator|.
name|processor
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
name|archiva
operator|.
name|layer
operator|.
name|RepositoryQueryLayer
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
name|layer
operator|.
name|RepositoryQueryLayerFactory
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
name|reporting
operator|.
name|processor
operator|.
name|MetadataReportProcessor
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
name|reporting
operator|.
name|database
operator|.
name|ReportingDatabase
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
name|Arrays
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This class will report on bad metadata files.  These include invalid version declarations and incomplete version  * information inside the metadata file.  Plugin metadata will be checked for validity of the latest plugin artifacts.  *  * @plexus.component role="org.apache.maven.archiva.reporting.processor.MetadataReportProcessor" role-hint="bad-metadata"  */
end_comment

begin_class
specifier|public
class|class
name|BadMetadataReportProcessor
implements|implements
name|MetadataReportProcessor
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryQueryLayerFactory
name|repositoryQueryLayerFactory
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ROLE_HINT
init|=
literal|"bad-metadata"
decl_stmt|;
comment|/**      * Process the metadata encountered in the repository and report all errors found, if any.      *      * @param metadata   the metadata to be processed.      * @param repository the repository where the metadata was encountered      * @param reporter   the ReportingDatabase to receive processing results      */
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
name|ReportingDatabase
name|reporter
parameter_list|)
block|{
if|if
condition|(
name|metadata
operator|.
name|storedInGroupDirectory
argument_list|()
condition|)
block|{
try|try
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
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addWarning
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|null
argument_list|,
literal|"Error getting plugin artifact directories versions: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
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
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|versioning
operator|!=
literal|null
condition|)
block|{
name|String
name|lastUpdated
init|=
name|versioning
operator|.
name|getLastUpdated
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastUpdated
operator|!=
literal|null
operator|&&
name|lastUpdated
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"missing-last-updated"
argument_list|,
literal|"Missing lastUpdated element inside the metadata."
argument_list|)
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
name|checkMetadataVersions
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
try|try
block|{
name|checkRepositoryVersions
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|String
name|reason
init|=
literal|"Error getting plugin artifact directories versions: "
operator|+
name|e
decl_stmt|;
name|addWarning
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|null
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|addWarning
parameter_list|(
name|ReportingDatabase
name|reporter
parameter_list|,
name|RepositoryMetadata
name|metadata
parameter_list|,
name|String
name|problem
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
comment|// TODO: reason could be an i18n key derived from the processor and the problem ID and the
name|reporter
operator|.
name|addWarning
argument_list|(
name|metadata
argument_list|,
name|ROLE_HINT
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method for processing a GroupRepositoryMetadata      *      * @param metadata   the metadata to be processed.      * @param repository the repository where the metadata was encountered      * @param reporter   the ReportingDatabase to receive processing results      */
specifier|private
name|void
name|checkPluginMetadata
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ReportingDatabase
name|reporter
parameter_list|)
throws|throws
name|IOException
block|{
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
name|List
name|pluginDirs
init|=
name|getArtifactIdFiles
argument_list|(
name|metadataDir
argument_list|)
decl_stmt|;
name|Map
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
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"missing-artifact-id:"
operator|+
name|plugin
operator|.
name|getPrefix
argument_list|()
argument_list|,
literal|"Missing or empty artifactId in group metadata for plugin "
operator|+
name|plugin
operator|.
name|getPrefix
argument_list|()
argument_list|)
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
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"missing-plugin-prefix:"
operator|+
name|artifactId
argument_list|,
literal|"Missing or empty plugin prefix for artifactId "
operator|+
name|artifactId
operator|+
literal|"."
argument_list|)
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
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"duplicate-plugin-prefix:"
operator|+
name|prefix
argument_list|,
literal|"Duplicate plugin prefix found: "
operator|+
name|prefix
operator|+
literal|"."
argument_list|)
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
operator|&&
name|artifactId
operator|.
name|length
argument_list|()
operator|>
literal|0
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
name|pluginDirs
operator|.
name|contains
argument_list|(
name|pluginDir
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"missing-plugin-from-repository:"
operator|+
name|artifactId
argument_list|,
literal|"Metadata plugin "
operator|+
name|artifactId
operator|+
literal|" not found in the repository"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pluginDirs
operator|.
name|remove
argument_list|(
name|pluginDir
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|pluginDirs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|Iterator
name|plugins
init|=
name|pluginDirs
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
name|File
name|plugin
init|=
operator|(
name|File
operator|)
name|plugins
operator|.
name|next
argument_list|()
decl_stmt|;
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"missing-plugin-from-metadata:"
operator|+
name|plugin
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Plugin "
operator|+
name|plugin
operator|.
name|getName
argument_list|()
operator|+
literal|" is present in the repository but "
operator|+
literal|"missing in the metadata."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Method for processing a SnapshotArtifactRepository      *      * @param metadata   the metadata to be processed.      * @param repository the repository where the metadata was encountered      * @param reporter   the ReportingDatabase to receive processing results      */
specifier|private
name|void
name|checkSnapshotMetadata
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ReportingDatabase
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
if|if
condition|(
name|versioning
operator|!=
literal|null
condition|)
block|{
name|Snapshot
name|snapshot
init|=
name|versioning
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|metadata
operator|.
name|getBaseVersion
argument_list|()
argument_list|,
name|Artifact
operator|.
name|SNAPSHOT_VERSION
argument_list|,
name|snapshot
operator|.
name|getTimestamp
argument_list|()
operator|+
literal|"-"
operator|+
name|snapshot
operator|.
name|getBuildNumber
argument_list|()
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createProjectArtifact
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
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|isSnapshot
argument_list|()
expr_stmt|;
comment|// trigger baseVersion correction
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
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"missing-snapshot-artifact-from-repository:"
operator|+
name|version
argument_list|,
literal|"Snapshot artifact "
operator|+
name|version
operator|+
literal|" does not exist."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Method for validating the versions declared inside an ArtifactRepositoryMetadata      *      * @param metadata   the metadata to be processed.      * @param repository the repository where the metadata was encountered      * @param reporter   the ReportingDatabase to receive processing results      */
specifier|private
name|void
name|checkMetadataVersions
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ReportingDatabase
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
if|if
condition|(
name|versioning
operator|!=
literal|null
condition|)
block|{
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
name|artifactFactory
operator|.
name|createProjectArtifact
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
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"missing-artifact-from-repository:"
operator|+
name|version
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
block|}
block|}
block|}
block|}
comment|/**      * Searches the artifact repository directory for all versions and verifies that all of them are listed in the      * ArtifactRepositoryMetadata      *      * @param metadata   the metadata to be processed.      * @param repository the repository where the metadata was encountered      * @param reporter   the ReportingDatabase to receive processing results      * @throws java.io.IOException if there is a problem reading from the file system      */
specifier|private
name|void
name|checkRepositoryVersions
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ReportingDatabase
name|reporter
parameter_list|)
throws|throws
name|IOException
block|{
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
name|List
name|metadataVersions
init|=
name|versioning
operator|!=
literal|null
condition|?
name|versioning
operator|.
name|getVersions
argument_list|()
else|:
name|Collections
operator|.
name|EMPTY_LIST
decl_stmt|;
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
comment|// TODO: I don't know how this condition can happen, but it was seen on the main repository.
comment|// Avoid hard failure
if|if
condition|(
name|versionsDir
operator|.
name|exists
argument_list|()
condition|)
block|{
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
name|metadataVersions
operator|.
name|contains
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|"missing-artifact-from-metadata:"
operator|+
name|version
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
block|}
block|}
block|}
else|else
block|{
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|metadata
argument_list|,
literal|null
argument_list|,
literal|"Metadata's directory did not exist: "
operator|+
name|versionsDir
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Used to gather artifactIds from a groupId directory.      *      * @param groupIdDir the directory of the group      * @return the list of artifact ID File objects for each directory      * @throws IOException if there was a failure to read the directories      */
specifier|private
name|List
name|getArtifactIdFiles
parameter_list|(
name|File
name|groupIdDir
parameter_list|)
throws|throws
name|IOException
block|{
name|List
name|artifactIdFiles
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|File
index|[]
name|files
init|=
name|groupIdDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|files
argument_list|)
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
name|artifactDir
init|=
operator|(
name|File
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifactDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|List
name|versions
init|=
name|FileUtils
operator|.
name|getFileNames
argument_list|(
name|artifactDir
argument_list|,
literal|"*/*.pom"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|versions
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|artifactIdFiles
operator|.
name|add
argument_list|(
name|artifactDir
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|artifactIdFiles
return|;
block|}
specifier|private
specifier|static
name|void
name|addFailure
parameter_list|(
name|ReportingDatabase
name|reporter
parameter_list|,
name|RepositoryMetadata
name|metadata
parameter_list|,
name|String
name|problem
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
comment|// TODO: reason could be an i18n key derived from the processor and the problem ID and the
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
name|ROLE_HINT
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

