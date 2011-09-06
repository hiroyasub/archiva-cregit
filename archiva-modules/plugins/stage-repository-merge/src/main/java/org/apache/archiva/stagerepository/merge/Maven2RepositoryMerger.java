begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|stagerepository
operator|.
name|merge
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
name|metadata
operator|.
name|model
operator|.
name|ArtifactMetadata
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
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|IncludesFilter
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
name|RepositoryPathTranslator
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
name|IOUtils
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
name|VersionComparator
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
name|VersionUtil
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
name|ArchivaConfiguration
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
name|Configuration
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
name|ArchivaRepositoryMetadata
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
name|RepositoryException
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
name|metadata
operator|.
name|RepositoryMetadataException
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
name|metadata
operator|.
name|RepositoryMetadataReader
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
name|metadata
operator|.
name|RepositoryMetadataWriter
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Calendar
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
name|Date
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
name|TimeZone
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

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryMerger#maven2"
argument_list|)
specifier|public
class|class
name|Maven2RepositoryMerger
implements|implements
name|RepositoryMerger
block|{
comment|/**      * plexus.requirement role-hint="default"      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      * plexus.requirement role-hint="maven2"      */
specifier|private
name|RepositoryPathTranslator
name|pathTranslator
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|METADATA_FILENAME
init|=
literal|"maven-metadata.xml"
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|Maven2RepositoryMerger
parameter_list|(
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration#default"
argument_list|)
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|,
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryPathTranslator#maven2"
argument_list|)
name|RepositoryPathTranslator
name|repositoryPathTranslator
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|archivaConfiguration
expr_stmt|;
name|this
operator|.
name|pathTranslator
operator|=
name|repositoryPathTranslator
expr_stmt|;
block|}
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ArchivaConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
specifier|public
name|void
name|merge
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|sourceRepoId
parameter_list|,
name|String
name|targetRepoId
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsInSourceRepo
init|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|sourceRepoId
argument_list|)
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|artifactMetadata
range|:
name|artifactsInSourceRepo
control|)
block|{
name|artifactMetadata
operator|.
name|setRepositoryId
argument_list|(
name|targetRepoId
argument_list|)
expr_stmt|;
name|createFolderStructure
argument_list|(
name|sourceRepoId
argument_list|,
name|targetRepoId
argument_list|,
name|artifactMetadata
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO when UI needs a subset to merge
specifier|public
name|void
name|merge
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|sourceRepoId
parameter_list|,
name|String
name|targetRepoId
parameter_list|,
name|Filter
argument_list|<
name|ArtifactMetadata
argument_list|>
name|filter
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|sourceArtifacts
init|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|sourceRepoId
argument_list|)
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|metadata
range|:
name|sourceArtifacts
control|)
block|{
if|if
condition|(
name|filter
operator|.
name|accept
argument_list|(
name|metadata
argument_list|)
condition|)
block|{
name|createFolderStructure
argument_list|(
name|sourceRepoId
argument_list|,
name|targetRepoId
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|createFolderStructure
parameter_list|(
name|String
name|sourceRepoId
parameter_list|,
name|String
name|targetRepoId
parameter_list|,
name|ArtifactMetadata
name|artifactMetadata
parameter_list|)
throws|throws
name|IOException
throws|,
name|RepositoryException
block|{
name|Configuration
name|config
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|targetRepoConfig
init|=
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|targetRepoId
argument_list|)
decl_stmt|;
name|ManagedRepositoryConfiguration
name|sourceRepoConfig
init|=
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|sourceRepoId
argument_list|)
decl_stmt|;
name|Date
name|lastUpdatedTimestamp
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|TimeZone
name|timezone
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
decl_stmt|;
name|DateFormat
name|fmt
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMdd.HHmmss"
argument_list|)
decl_stmt|;
name|fmt
operator|.
name|setTimeZone
argument_list|(
name|timezone
argument_list|)
expr_stmt|;
name|String
name|timestamp
init|=
name|fmt
operator|.
name|format
argument_list|(
name|lastUpdatedTimestamp
argument_list|)
decl_stmt|;
name|String
name|targetRepoPath
init|=
name|targetRepoConfig
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|String
name|sourceRepoPath
init|=
name|sourceRepoConfig
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|String
name|artifactPath
init|=
name|pathTranslator
operator|.
name|toPath
argument_list|(
name|artifactMetadata
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|artifactMetadata
operator|.
name|getProject
argument_list|()
argument_list|,
name|artifactMetadata
operator|.
name|getProjectVersion
argument_list|()
argument_list|,
name|artifactMetadata
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|sourceArtifactFile
init|=
operator|new
name|File
argument_list|(
name|sourceRepoPath
argument_list|,
name|artifactPath
argument_list|)
decl_stmt|;
name|File
name|targetArtifactFile
init|=
operator|new
name|File
argument_list|(
name|targetRepoPath
argument_list|,
name|artifactPath
argument_list|)
decl_stmt|;
name|int
name|lastIndex
init|=
name|artifactPath
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|targetRepoPath
argument_list|,
name|artifactPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastIndex
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|targetFile
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// create the folder structure when it does not exist
name|targetFile
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|// artifact copying
name|copyFile
argument_list|(
name|sourceArtifactFile
argument_list|,
name|targetArtifactFile
argument_list|)
expr_stmt|;
comment|// pom file copying
name|String
name|fileName
init|=
name|artifactMetadata
operator|.
name|getProject
argument_list|()
operator|+
literal|"-"
operator|+
name|artifactMetadata
operator|.
name|getVersion
argument_list|()
operator|+
literal|".pom"
decl_stmt|;
comment|// pom file copying
comment|// TODO need to use path translator to get the pom file path
comment|//        String fileName = artifactMetadata.getProject() + "-" + artifactMetadata.getVersion() + ".pom";
comment|//
comment|//        File sourcePomFile =
comment|//            pathTranslator.toFile( new File( sourceRepoPath ), artifactMetadata.getId(), artifactMetadata.getProject(),
comment|//                                   artifactMetadata.getVersion(), fileName );
comment|//
comment|//        String relativePathToPomFile = sourcePomFile.getAbsolutePath().split( sourceRepoPath )[1];
comment|//        File targetPomFile = new File( targetRepoPath, relativePathToPomFile );
comment|//pom file copying  (file path is taken with out using path translator)
name|String
name|index
init|=
name|artifactPath
operator|.
name|substring
argument_list|(
name|lastIndex
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|last
init|=
name|index
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|File
name|sourcePomFile
init|=
operator|new
name|File
argument_list|(
name|sourceRepoPath
argument_list|,
name|artifactPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastIndex
argument_list|)
operator|+
literal|"/"
operator|+
name|artifactPath
operator|.
name|substring
argument_list|(
name|lastIndex
operator|+
literal|1
argument_list|)
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|last
argument_list|)
operator|+
literal|".pom"
argument_list|)
decl_stmt|;
name|File
name|targetPomFile
init|=
operator|new
name|File
argument_list|(
name|targetRepoPath
argument_list|,
name|artifactPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastIndex
argument_list|)
operator|+
literal|"/"
operator|+
name|artifactPath
operator|.
name|substring
argument_list|(
name|lastIndex
operator|+
literal|1
argument_list|)
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|last
argument_list|)
operator|+
literal|".pom"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|targetPomFile
operator|.
name|exists
argument_list|()
operator|&&
name|sourcePomFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|copyFile
argument_list|(
name|sourcePomFile
argument_list|,
name|targetPomFile
argument_list|)
expr_stmt|;
block|}
comment|// explicitly update only if metadata-updater consumer is not enabled!
if|if
condition|(
operator|!
name|config
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getKnownContentConsumers
argument_list|()
operator|.
name|contains
argument_list|(
literal|"metadata-updater"
argument_list|)
condition|)
block|{
comment|// updating version metadata files
name|File
name|versionMetaDataFileInSourceRepo
init|=
name|pathTranslator
operator|.
name|toFile
argument_list|(
operator|new
name|File
argument_list|(
name|sourceRepoPath
argument_list|)
argument_list|,
name|artifactMetadata
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|artifactMetadata
operator|.
name|getProject
argument_list|()
argument_list|,
name|artifactMetadata
operator|.
name|getVersion
argument_list|()
argument_list|,
name|METADATA_FILENAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|versionMetaDataFileInSourceRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|String
name|relativePathToVersionMetadataFile
init|=
name|versionMetaDataFileInSourceRepo
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|split
argument_list|(
name|sourceRepoPath
argument_list|)
index|[
literal|1
index|]
decl_stmt|;
name|File
name|versionMetaDataFileInTargetRepo
init|=
operator|new
name|File
argument_list|(
name|targetRepoPath
argument_list|,
name|relativePathToVersionMetadataFile
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|versionMetaDataFileInTargetRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|copyFile
argument_list|(
name|versionMetaDataFileInSourceRepo
argument_list|,
name|versionMetaDataFileInTargetRepo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateVersionMetadata
argument_list|(
name|versionMetaDataFileInTargetRepo
argument_list|,
name|artifactMetadata
argument_list|,
name|lastUpdatedTimestamp
argument_list|)
expr_stmt|;
block|}
block|}
comment|// updating project meta data file
name|String
name|projectDirectoryInSourceRepo
init|=
operator|new
name|File
argument_list|(
name|versionMetaDataFileInSourceRepo
operator|.
name|getParent
argument_list|()
argument_list|)
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|File
name|projectMetadataFileInSourceRepo
init|=
operator|new
name|File
argument_list|(
name|projectDirectoryInSourceRepo
argument_list|,
name|METADATA_FILENAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectMetadataFileInSourceRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|String
name|relativePathToProjectMetadataFile
init|=
name|projectMetadataFileInSourceRepo
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|split
argument_list|(
name|sourceRepoPath
argument_list|)
index|[
literal|1
index|]
decl_stmt|;
name|File
name|projectMetadataFileInTargetRepo
init|=
operator|new
name|File
argument_list|(
name|targetRepoPath
argument_list|,
name|relativePathToProjectMetadataFile
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|projectMetadataFileInTargetRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|copyFile
argument_list|(
name|projectMetadataFileInSourceRepo
argument_list|,
name|projectMetadataFileInSourceRepo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateProjectMetadata
argument_list|(
name|projectMetadataFileInTargetRepo
argument_list|,
name|artifactMetadata
argument_list|,
name|lastUpdatedTimestamp
argument_list|,
name|timestamp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|copyFile
parameter_list|(
name|File
name|sourceFile
parameter_list|,
name|File
name|targetFile
parameter_list|)
throws|throws
name|IOException
block|{
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|targetFile
argument_list|)
decl_stmt|;
name|FileInputStream
name|input
init|=
operator|new
name|FileInputStream
argument_list|(
name|sourceFile
argument_list|)
decl_stmt|;
comment|// IOUtils internally buffers the streams
try|try
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|input
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateProjectMetadata
parameter_list|(
name|File
name|projectMetaDataFileIntargetRepo
parameter_list|,
name|ArtifactMetadata
name|artifactMetadata
parameter_list|,
name|Date
name|lastUpdatedTimestamp
parameter_list|,
name|String
name|timestamp
parameter_list|)
throws|throws
name|RepositoryMetadataException
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|availableVersions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|latestVersion
init|=
name|artifactMetadata
operator|.
name|getProjectVersion
argument_list|()
decl_stmt|;
name|ArchivaRepositoryMetadata
name|projectMetadata
init|=
name|getMetadata
argument_list|(
name|projectMetaDataFileIntargetRepo
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectMetaDataFileIntargetRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|availableVersions
operator|=
operator|(
name|ArrayList
argument_list|<
name|String
argument_list|>
operator|)
name|projectMetadata
operator|.
name|getAvailableVersions
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|availableVersions
argument_list|,
name|VersionComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|availableVersions
operator|.
name|contains
argument_list|(
name|artifactMetadata
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|availableVersions
operator|.
name|add
argument_list|(
name|artifactMetadata
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|latestVersion
operator|=
name|availableVersions
operator|.
name|get
argument_list|(
name|availableVersions
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|availableVersions
operator|.
name|add
argument_list|(
name|artifactMetadata
operator|.
name|getProjectVersion
argument_list|()
argument_list|)
expr_stmt|;
name|projectMetadata
operator|.
name|setGroupId
argument_list|(
name|artifactMetadata
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|projectMetadata
operator|.
name|setArtifactId
argument_list|(
name|artifactMetadata
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectMetadata
operator|.
name|getGroupId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|projectMetadata
operator|.
name|setGroupId
argument_list|(
name|artifactMetadata
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectMetadata
operator|.
name|getArtifactId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|projectMetadata
operator|.
name|setArtifactId
argument_list|(
name|artifactMetadata
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|projectMetadata
operator|.
name|setLatestVersion
argument_list|(
name|latestVersion
argument_list|)
expr_stmt|;
name|projectMetadata
operator|.
name|setAvailableVersions
argument_list|(
name|availableVersions
argument_list|)
expr_stmt|;
name|projectMetadata
operator|.
name|setLastUpdated
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
name|projectMetadata
operator|.
name|setLastUpdatedTimestamp
argument_list|(
name|lastUpdatedTimestamp
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|artifactMetadata
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|projectMetadata
operator|.
name|setReleasedVersion
argument_list|(
name|latestVersion
argument_list|)
expr_stmt|;
block|}
name|RepositoryMetadataWriter
operator|.
name|write
argument_list|(
name|projectMetadata
argument_list|,
name|projectMetaDataFileIntargetRepo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updateVersionMetadata
parameter_list|(
name|File
name|versionMetaDataFileInTargetRepo
parameter_list|,
name|ArtifactMetadata
name|artifactMetadata
parameter_list|,
name|Date
name|lastUpdatedTimestamp
parameter_list|)
throws|throws
name|RepositoryMetadataException
block|{
name|ArchivaRepositoryMetadata
name|versionMetadata
init|=
name|getMetadata
argument_list|(
name|versionMetaDataFileInTargetRepo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|versionMetaDataFileInTargetRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|versionMetadata
operator|.
name|setGroupId
argument_list|(
name|artifactMetadata
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setArtifactId
argument_list|(
name|artifactMetadata
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setVersion
argument_list|(
name|artifactMetadata
operator|.
name|getProjectVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|versionMetadata
operator|.
name|setLastUpdatedTimestamp
argument_list|(
name|lastUpdatedTimestamp
argument_list|)
expr_stmt|;
name|RepositoryMetadataWriter
operator|.
name|write
argument_list|(
name|versionMetadata
argument_list|,
name|versionMetaDataFileInTargetRepo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ArchivaRepositoryMetadata
name|getMetadata
parameter_list|(
name|File
name|metadataFile
parameter_list|)
throws|throws
name|RepositoryMetadataException
block|{
name|ArchivaRepositoryMetadata
name|metadata
init|=
operator|new
name|ArchivaRepositoryMetadata
argument_list|()
decl_stmt|;
if|if
condition|(
name|metadataFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|metadata
operator|=
name|RepositoryMetadataReader
operator|.
name|read
argument_list|(
name|metadataFile
argument_list|)
expr_stmt|;
block|}
return|return
name|metadata
return|;
block|}
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getConflictingArtifacts
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|sourceRepo
parameter_list|,
name|String
name|targetRepo
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|targetArtifacts
init|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|targetRepo
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|sourceArtifacts
init|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|sourceRepo
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|conflictsArtifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|targetArtifact
range|:
name|targetArtifacts
control|)
block|{
for|for
control|(
name|ArtifactMetadata
name|sourceArtifact
range|:
name|sourceArtifacts
control|)
block|{
if|if
condition|(
name|isEquals
argument_list|(
name|targetArtifact
argument_list|,
name|sourceArtifact
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|conflictsArtifacts
operator|.
name|contains
argument_list|(
name|sourceArtifact
argument_list|)
condition|)
block|{
name|conflictsArtifacts
operator|.
name|add
argument_list|(
name|sourceArtifact
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|sourceArtifacts
operator|.
name|removeAll
argument_list|(
name|conflictsArtifacts
argument_list|)
expr_stmt|;
name|Filter
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsWithOutConflicts
init|=
operator|new
name|IncludesFilter
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|(
name|sourceArtifacts
argument_list|)
decl_stmt|;
comment|//        merge( sourceRepo, targetRepo, artifactsWithOutConflicts );
return|return
name|conflictsArtifacts
return|;
block|}
specifier|private
name|boolean
name|isEquals
parameter_list|(
name|ArtifactMetadata
name|sourceArtifact
parameter_list|,
name|ArtifactMetadata
name|targetArtifact
parameter_list|)
block|{
name|boolean
name|isSame
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|(
name|sourceArtifact
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|targetArtifact
operator|.
name|getNamespace
argument_list|()
argument_list|)
operator|)
operator|&&
operator|(
name|sourceArtifact
operator|.
name|getProject
argument_list|()
operator|.
name|equals
argument_list|(
name|targetArtifact
operator|.
name|getProject
argument_list|()
argument_list|)
operator|)
operator|&&
operator|(
name|sourceArtifact
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|targetArtifact
operator|.
name|getId
argument_list|()
argument_list|)
operator|)
operator|&&
operator|(
name|sourceArtifact
operator|.
name|getProjectVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|targetArtifact
operator|.
name|getProjectVersion
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|isSame
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|isSame
return|;
block|}
block|}
end_class

end_unit

