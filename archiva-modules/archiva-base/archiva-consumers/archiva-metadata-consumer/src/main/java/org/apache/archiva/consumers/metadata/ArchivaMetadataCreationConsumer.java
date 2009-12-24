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
name|metadata
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|checksum
operator|.
name|ChecksumAlgorithm
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
name|checksum
operator|.
name|ChecksummedFile
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
name|model
operator|.
name|ProjectMetadata
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
name|ProjectVersionMetadata
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
name|MetadataResolutionException
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
name|StorageMetadataResolver
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
name|ConfigurationNames
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
name|consumers
operator|.
name|AbstractMonitoredConsumer
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
name|consumers
operator|.
name|ConsumerException
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryListener
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

begin_comment
comment|/**  * Take an artifact off of disk and put it into the metadata repository.  *  * @version $Id: ArtifactUpdateDatabaseConsumer.java 718864 2008-11-19 06:33:35Z brett $  * @plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  * role-hint="create-archiva-metadata" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaMetadataCreationConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * @plexus.configuration default-value="create-archiva-metadata"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Create basic metadata for Archiva to be able to reference the artifact"      */
specifier|private
name|String
name|description
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|private
name|Date
name|whenGathered
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ManagedRepositoryContent
name|repository
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
comment|/**      * FIXME: this needs to be configurable based on storage type, and availability of proxy module      * ... could be a different type since we need methods to modify the storage metadata, which would also allow more      * appropriate methods to pass in the already determined repository configuration, for example, instead of the ID      *      * @plexus.requirement role-hint="maven2"      */
specifier|private
name|StorageMetadataResolver
name|storageResolver
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
name|ArchivaMetadataCreationConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
name|getDefaultArtifactExclusions
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|this
operator|.
name|includes
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepositoryConfiguration
name|repo
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|this
operator|.
name|repository
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|this
operator|.
name|whenGathered
operator|=
name|whenGathered
expr_stmt|;
block|}
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
comment|// note that we do minimal processing including checksums and POM information for performance of
comment|// the initial scan. Any request for this information will be intercepted and populated on-demand
comment|// or picked up by subsequent scans
name|ArtifactReference
name|artifact
decl_stmt|;
try|try
block|{
name|artifact
operator|=
name|repository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
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
name|File
name|file
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
name|ProjectMetadata
name|project
init|=
operator|new
name|ProjectMetadata
argument_list|()
decl_stmt|;
name|project
operator|.
name|setNamespace
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|setId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|projectVersion
init|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: maybe not too efficient since it may have already been read and stored for this artifact
name|ProjectVersionMetadata
name|versionMetadata
init|=
literal|null
decl_stmt|;
try|try
block|{
name|versionMetadata
operator|=
name|storageResolver
operator|.
name|getProjectVersion
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|projectVersion
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataResolutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error occurred resolving POM for artifact: "
operator|+
name|path
operator|+
literal|"; message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|createVersionMetadata
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|versionMetadata
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Missing or invalid POM for artifact: "
operator|+
name|path
operator|+
literal|"; creating empty metadata"
argument_list|)
expr_stmt|;
name|versionMetadata
operator|=
operator|new
name|ProjectVersionMetadata
argument_list|()
expr_stmt|;
name|versionMetadata
operator|.
name|setId
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
name|createVersionMetadata
operator|=
literal|true
expr_stmt|;
block|}
name|ArtifactMetadata
name|artifactMeta
init|=
operator|new
name|ArtifactMetadata
argument_list|()
decl_stmt|;
name|artifactMeta
operator|.
name|setRepositoryId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|artifactMeta
operator|.
name|setNamespace
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|artifactMeta
operator|.
name|setProject
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|artifactMeta
operator|.
name|setId
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|artifactMeta
operator|.
name|setFileLastModified
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
name|artifactMeta
operator|.
name|setSize
argument_list|(
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|artifactMeta
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|artifactMeta
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|artifactMeta
operator|.
name|setMd5
argument_list|(
name|checksummedFile
operator|.
name|calculateChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|)
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
name|error
argument_list|(
literal|"Error attempting to get MD5 checksum for "
operator|+
name|file
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|artifactMeta
operator|.
name|setSha1
argument_list|(
name|checksummedFile
operator|.
name|calculateChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
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
name|error
argument_list|(
literal|"Error attempting to get SHA-1 checksum for "
operator|+
name|file
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: transaction
comment|// read the metadata and update it if it is newer or doesn't exist
name|metadataRepository
operator|.
name|updateArtifact
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|project
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|project
operator|.
name|getId
argument_list|()
argument_list|,
name|projectVersion
argument_list|,
name|artifactMeta
argument_list|)
expr_stmt|;
if|if
condition|(
name|createVersionMetadata
condition|)
block|{
name|metadataRepository
operator|.
name|updateProjectVersion
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|project
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|project
operator|.
name|getId
argument_list|()
argument_list|,
name|versionMetadata
argument_list|)
expr_stmt|;
block|}
name|metadataRepository
operator|.
name|updateProject
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|project
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|ConfigurationNames
operator|.
name|isRepositoryScanning
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|private
name|void
name|initIncludes
parameter_list|()
block|{
name|includes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|includes
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
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

