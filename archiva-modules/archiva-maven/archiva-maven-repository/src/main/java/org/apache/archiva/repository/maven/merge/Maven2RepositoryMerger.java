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
name|maven
operator|.
name|merge
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|DefaultFileLockManager
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
name|archiva
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
name|maven
operator|.
name|metadata
operator|.
name|MavenMetadataReader
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
name|MetadataRepositoryException
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
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
name|RepositoryType
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
name|base
operator|.
name|RepositoryMetadataWriter
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
name|archiva
operator|.
name|repository
operator|.
name|storage
operator|.
name|fs
operator|.
name|FilesystemAsset
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
name|fs
operator|.
name|FilesystemStorage
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
name|stagerepository
operator|.
name|merge
operator|.
name|RepositoryMerger
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
name|stagerepository
operator|.
name|merge
operator|.
name|RepositoryMergerException
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|BufferedWriter
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
name|Comparator
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
name|java
operator|.
name|util
operator|.
name|TreeSet
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
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"metadataReader#maven"
argument_list|)
specifier|private
name|MavenMetadataReader
name|metadataReader
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Maven2RepositoryMerger
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|ArtifactMetadata
argument_list|>
name|META_COMPARATOR
init|=
name|Comparator
operator|.
name|comparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getNamespace
argument_list|)
operator|.
name|thenComparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getProject
argument_list|)
operator|.
name|thenComparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getId
argument_list|)
operator|.
name|thenComparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getVersion
argument_list|)
decl_stmt|;
comment|/**      *      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      *      */
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
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
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
annotation|@
name|Override
specifier|public
name|boolean
name|supportsRepository
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
block|{
return|return
name|RepositoryType
operator|.
name|MAVEN
operator|.
name|equals
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|RepositoryMergerException
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
init|)
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
name|session
argument_list|,
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
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryMergerException
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
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryMergerException
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
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryMergerException
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
comment|// TODO when UI needs a subset to merge
annotation|@
name|Override
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
name|RepositoryMergerException
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
init|)
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
name|session
argument_list|,
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
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryMergerException
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
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryMergerException
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
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryMergerException
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
name|Path
name|sourceArtifactFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|sourceRepoPath
argument_list|,
name|artifactPath
argument_list|)
decl_stmt|;
name|Path
name|targetArtifactFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|targetRepoPath
argument_list|,
name|artifactPath
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"artifactPath {}"
argument_list|,
name|artifactPath
argument_list|)
expr_stmt|;
name|int
name|lastIndex
init|=
name|artifactPath
operator|.
name|lastIndexOf
argument_list|(
name|RepositoryPathTranslator
operator|.
name|PATH_SEPARATOR
argument_list|)
decl_stmt|;
name|Path
name|targetFile
init|=
name|Paths
operator|.
name|get
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
name|Files
operator|.
name|exists
argument_list|(
name|targetFile
argument_list|)
condition|)
block|{
comment|// create the folder structure when it does not exist
name|Files
operator|.
name|createDirectories
argument_list|(
name|targetFile
argument_list|)
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
name|Path
name|sourcePomFile
init|=
name|Paths
operator|.
name|get
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
name|Path
name|targetPomFile
init|=
name|Paths
operator|.
name|get
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
name|Files
operator|.
name|exists
argument_list|(
name|targetPomFile
argument_list|)
operator|&&
name|Files
operator|.
name|exists
argument_list|(
name|sourcePomFile
argument_list|)
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
name|FilesystemStorage
name|fsStorage
init|=
operator|new
name|FilesystemStorage
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|sourceRepoPath
argument_list|)
argument_list|,
operator|new
name|DefaultFileLockManager
argument_list|()
argument_list|)
decl_stmt|;
name|StorageAsset
name|versionMetaDataFileInSourceRepo
init|=
name|pathTranslator
operator|.
name|toFile
argument_list|(
operator|new
name|FilesystemAsset
argument_list|(
name|fsStorage
argument_list|,
literal|""
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
name|sourceRepoPath
argument_list|)
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
comment|//Pattern quote for windows path
name|String
name|relativePathToVersionMetadataFile
init|=
name|getRelativeAssetPath
argument_list|(
name|versionMetaDataFileInSourceRepo
argument_list|)
decl_stmt|;
name|Path
name|versionMetaDataFileInTargetRepo
init|=
name|Paths
operator|.
name|get
argument_list|(
name|targetRepoPath
argument_list|,
name|relativePathToVersionMetadataFile
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|versionMetaDataFileInTargetRepo
argument_list|)
condition|)
block|{
name|copyFile
argument_list|(
name|versionMetaDataFileInSourceRepo
operator|.
name|getFilePath
argument_list|()
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
name|StorageAsset
name|projectDirectoryInSourceRepo
init|=
name|versionMetaDataFileInSourceRepo
operator|.
name|getParent
argument_list|()
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|StorageAsset
name|projectMetadataFileInSourceRepo
init|=
name|projectDirectoryInSourceRepo
operator|.
name|resolve
argument_list|(
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
name|getRelativeAssetPath
argument_list|(
name|projectMetadataFileInSourceRepo
argument_list|)
decl_stmt|;
name|Path
name|projectMetadataFileInTargetRepo
init|=
name|Paths
operator|.
name|get
argument_list|(
name|targetRepoPath
argument_list|,
name|relativePathToProjectMetadataFile
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|projectMetadataFileInTargetRepo
argument_list|)
condition|)
block|{
name|copyFile
argument_list|(
name|projectMetadataFileInSourceRepo
operator|.
name|getFilePath
argument_list|()
argument_list|,
name|projectMetadataFileInTargetRepo
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
name|String
name|getRelativeAssetPath
parameter_list|(
specifier|final
name|StorageAsset
name|asset
parameter_list|)
block|{
name|String
name|relPath
init|=
name|asset
operator|.
name|getPath
argument_list|()
decl_stmt|;
while|while
condition|(
name|relPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|relPath
operator|=
name|relPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|relPath
return|;
block|}
specifier|private
name|void
name|copyFile
parameter_list|(
name|Path
name|sourceFile
parameter_list|,
name|Path
name|targetFile
parameter_list|)
throws|throws
name|IOException
block|{
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|sourceFile
operator|.
name|toFile
argument_list|()
argument_list|,
name|targetFile
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updateProjectMetadata
parameter_list|(
name|Path
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
argument_list|<>
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
name|Files
operator|.
name|exists
argument_list|(
name|projectMetaDataFileIntargetRepo
argument_list|)
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
try|try
init|(
name|BufferedWriter
name|writer
init|=
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|projectMetaDataFileIntargetRepo
argument_list|)
init|)
block|{
name|RepositoryMetadataWriter
operator|.
name|write
argument_list|(
name|projectMetadata
argument_list|,
name|writer
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
name|RepositoryMetadataException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|updateVersionMetadata
parameter_list|(
name|Path
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
name|Files
operator|.
name|exists
argument_list|(
name|versionMetaDataFileInTargetRepo
argument_list|)
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
try|try
init|(
name|BufferedWriter
name|writer
init|=
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|versionMetaDataFileInTargetRepo
argument_list|)
init|)
block|{
name|RepositoryMetadataWriter
operator|.
name|write
argument_list|(
name|versionMetadata
argument_list|,
name|writer
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
name|RepositoryMetadataException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|ArchivaRepositoryMetadata
name|getMetadata
parameter_list|(
name|Path
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
name|Files
operator|.
name|exists
argument_list|(
name|metadataFile
argument_list|)
condition|)
block|{
name|metadata
operator|=
name|metadataReader
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
annotation|@
name|Override
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
name|RepositoryMergerException
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
name|TreeSet
argument_list|<
name|ArtifactMetadata
argument_list|>
name|targetArtifacts
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|META_COMPARATOR
argument_list|)
decl_stmt|;
name|targetArtifacts
operator|.
name|addAll
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|session
argument_list|,
name|targetRepo
argument_list|)
argument_list|)
expr_stmt|;
name|TreeSet
argument_list|<
name|ArtifactMetadata
argument_list|>
name|sourceArtifacts
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|META_COMPARATOR
argument_list|)
decl_stmt|;
name|sourceArtifacts
operator|.
name|addAll
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|session
argument_list|,
name|sourceRepo
argument_list|)
argument_list|)
expr_stmt|;
name|sourceArtifacts
operator|.
name|retainAll
argument_list|(
name|targetArtifacts
argument_list|)
expr_stmt|;
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|sourceArtifacts
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryMergerException
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
specifier|public
name|RepositorySessionFactory
name|getRepositorySessionFactory
parameter_list|( )
block|{
return|return
name|repositorySessionFactory
return|;
block|}
specifier|public
name|void
name|setRepositorySessionFactory
parameter_list|(
name|RepositorySessionFactory
name|repositorySessionFactory
parameter_list|)
block|{
name|this
operator|.
name|repositorySessionFactory
operator|=
name|repositorySessionFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

