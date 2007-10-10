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
name|consumers
operator|.
name|core
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
name|RepositoryContentFactory
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
name|RepositoryNotFoundException
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
name|MetadataTools
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
name|List
import|;
end_import

begin_comment
comment|/**  * MetadataUpdaterConsumer will create and update the metadata present within the repository.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  * role-hint="metadata-updater"  * instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|MetadataUpdaterConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * @plexus.configuration default-value="metadata-updater"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Update / Create maven-metadata.xml files"      */
specifier|private
name|String
name|description
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataTools
name|metadataTools
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
specifier|static
specifier|final
name|String
name|TYPE_METADATA_BAD_INTERNAL_REF
init|=
literal|"metadata-bad-internal-ref"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_METADATA_WRITE_FAILURE
init|=
literal|"metadata-write-failure"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_METADATA_IO
init|=
literal|"metadata-io-warning"
decl_stmt|;
specifier|private
name|ManagedRepositoryContent
name|repository
decl_stmt|;
specifier|private
name|File
name|repositoryDir
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
specifier|private
name|long
name|scanStartTimestamp
init|=
literal|0
decl_stmt|;
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setIncludes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|includes
parameter_list|)
block|{
name|this
operator|.
name|includes
operator|=
name|includes
expr_stmt|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepositoryConfiguration
name|repoConfig
parameter_list|)
throws|throws
name|ConsumerException
block|{
try|try
block|{
name|this
operator|.
name|repository
operator|=
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|repositoryDir
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getRepoRoot
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|scanStartTimestamp
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
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
catch|catch
parameter_list|(
name|RepositoryException
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
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing here */
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
literal|null
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
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
try|try
block|{
name|ArtifactReference
name|artifact
init|=
name|repository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|updateVersionMetadata
argument_list|(
name|artifact
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|updateProjectMetadata
argument_list|(
name|artifact
argument_list|,
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
literal|"Unable to convert to artifact reference: "
operator|+
name|path
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|updateProjectMetadata
parameter_list|(
name|ArtifactReference
name|artifact
parameter_list|,
name|String
name|path
parameter_list|)
block|{
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
name|String
name|metadataPath
init|=
name|this
operator|.
name|metadataTools
operator|.
name|toPath
argument_list|(
name|projectRef
argument_list|)
decl_stmt|;
name|File
name|projectMetadata
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
name|metadataPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectMetadata
operator|.
name|exists
argument_list|()
operator|&&
operator|(
name|projectMetadata
operator|.
name|lastModified
argument_list|()
operator|>=
name|this
operator|.
name|scanStartTimestamp
operator|)
condition|)
block|{
comment|// This metadata is up to date. skip it.
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Skipping uptodate metadata: "
operator|+
name|this
operator|.
name|metadataTools
operator|.
name|toPath
argument_list|(
name|projectRef
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|metadataTools
operator|.
name|updateMetadata
argument_list|(
name|this
operator|.
name|repository
argument_list|,
name|projectRef
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Updated metadata: "
operator|+
name|this
operator|.
name|metadataTools
operator|.
name|toPath
argument_list|(
name|projectRef
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|triggerConsumerWarning
argument_list|(
name|TYPE_METADATA_BAD_INTERNAL_REF
argument_list|,
literal|"Unable to convert path ["
operator|+
name|path
operator|+
literal|"] to an internal project reference: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|TYPE_METADATA_WRITE_FAILURE
argument_list|,
literal|"Unable to write project metadata for artifact ["
operator|+
name|path
operator|+
literal|"]: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|triggerConsumerWarning
argument_list|(
name|TYPE_METADATA_IO
argument_list|,
literal|"Project metadata not written due to IO warning: "
operator|+
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
name|triggerConsumerWarning
argument_list|(
name|TYPE_METADATA_IO
argument_list|,
literal|"Project metadata not written because no versions were found to update: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateVersionMetadata
parameter_list|(
name|ArtifactReference
name|artifact
parameter_list|,
name|String
name|path
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
try|try
block|{
name|String
name|metadataPath
init|=
name|this
operator|.
name|metadataTools
operator|.
name|toPath
argument_list|(
name|versionRef
argument_list|)
decl_stmt|;
name|File
name|projectMetadata
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
name|metadataPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectMetadata
operator|.
name|exists
argument_list|()
operator|&&
operator|(
name|projectMetadata
operator|.
name|lastModified
argument_list|()
operator|>=
name|this
operator|.
name|scanStartTimestamp
operator|)
condition|)
block|{
comment|// This metadata is up to date. skip it.
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Skipping uptodate metadata: "
operator|+
name|this
operator|.
name|metadataTools
operator|.
name|toPath
argument_list|(
name|versionRef
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|metadataTools
operator|.
name|updateMetadata
argument_list|(
name|this
operator|.
name|repository
argument_list|,
name|versionRef
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Updated metadata: "
operator|+
name|this
operator|.
name|metadataTools
operator|.
name|toPath
argument_list|(
name|versionRef
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|triggerConsumerWarning
argument_list|(
name|TYPE_METADATA_BAD_INTERNAL_REF
argument_list|,
literal|"Unable to convert path ["
operator|+
name|path
operator|+
literal|"] to an internal version reference: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|TYPE_METADATA_WRITE_FAILURE
argument_list|,
literal|"Unable to write version metadata for artifact ["
operator|+
name|path
operator|+
literal|"]: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|triggerConsumerWarning
argument_list|(
name|TYPE_METADATA_IO
argument_list|,
literal|"Version metadata not written due to IO warning: "
operator|+
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
name|triggerConsumerWarning
argument_list|(
name|TYPE_METADATA_IO
argument_list|,
literal|"Version metadata not written because no versions were found to update: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
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
comment|/* do nothing here */
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

