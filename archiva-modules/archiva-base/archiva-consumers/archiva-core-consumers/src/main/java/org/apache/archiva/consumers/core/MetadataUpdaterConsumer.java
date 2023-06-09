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
name|configuration
operator|.
name|provider
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
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|BaseRepositoryContentLayout
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
name|content
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
name|RepositoryRegistry
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
name|content
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
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|Project
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
name|storage
operator|.
name|StorageAsset
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
name|annotation
operator|.
name|PostConstruct
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

begin_comment
comment|/**  * MetadataUpdaterConsumer will create and update the metadata present within the repository.  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#metadata-updater"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|MetadataUpdaterConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
comment|// it's prototype bean so we assume configuration won't change during a run
comment|//, RegistryListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MetadataUpdaterConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * default-value="metadata-updater"      */
specifier|private
name|String
name|id
init|=
literal|"metadata-updater"
decl_stmt|;
specifier|private
name|String
name|description
init|=
literal|"Update / Create maven-metadata.xml files"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|MetadataTools
name|metadataTools
decl_stmt|;
annotation|@
name|Inject
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
name|StorageAsset
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
argument_list|<>
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|private
name|long
name|scanStartTimestamp
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|( )
block|{
return|return
name|description
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|( )
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
annotation|@
name|Override
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repoConfig
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
try|try
block|{
name|ManagedRepository
name|repo
init|=
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|repoConfig
operator|.
name|getId
argument_list|( )
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
literal|"Repository not found: "
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|repository
operator|=
name|repo
operator|.
name|getContent
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|repository
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
literal|"Repository content not found: "
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|repositoryDir
operator|=
name|repository
operator|.
name|getRepository
argument_list|()
operator|.
name|getRoot
argument_list|()
expr_stmt|;
name|this
operator|.
name|scanStartTimestamp
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|( )
expr_stmt|;
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
argument_list|( )
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|beginScan
argument_list|(
name|repository
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|( )
block|{
comment|/* do nothing here */
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|completeScan
argument_list|( )
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|( )
block|{
return|return
name|getDefaultArtifactExclusions
argument_list|( )
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|( )
block|{
return|return
name|this
operator|.
name|includes
return|;
block|}
annotation|@
name|Override
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
comment|// Ignore paths like .index etc
if|if
condition|(
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
try|try
block|{
name|BaseRepositoryContentLayout
name|layout
init|=
name|repository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
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
name|log
operator|.
name|info
argument_list|(
literal|"Not processing path that is not an artifact: {} ({})"
argument_list|,
name|path
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|Exception
block|{
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updateProjectMetadata
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|path
parameter_list|)
block|{
try|try
block|{
name|Project
name|proj
init|=
name|artifact
operator|.
name|getProject
argument_list|( )
decl_stmt|;
name|String
name|metadataPath
init|=
name|repository
operator|.
name|toPath
argument_list|(
name|proj
argument_list|)
decl_stmt|;
name|StorageAsset
name|projectMetadata
init|=
name|this
operator|.
name|repositoryDir
operator|.
name|resolve
argument_list|(
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
name|getModificationTime
argument_list|()
operator|.
name|toEpochMilli
argument_list|()
operator|>=
name|this
operator|.
name|scanStartTimestamp
operator|)
condition|)
block|{
comment|// This metadata is up to date. skip it.
name|log
operator|.
name|debug
argument_list|(
literal|"Skipping uptodate metadata: {}"
argument_list|,
name|metadataPath
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
name|metadataPath
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Updated metadata: {}"
argument_list|,
name|metadataPath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to write project metadat for artifact [{}]:"
argument_list|,
name|path
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateVersionMetadata
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|path
parameter_list|)
block|{
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
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|StorageAsset
name|projectMetadata
init|=
name|this
operator|.
name|repositoryDir
operator|.
name|resolve
argument_list|(
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
name|getModificationTime
argument_list|()
operator|.
name|toEpochMilli
argument_list|()
operator|>=
name|this
operator|.
name|scanStartTimestamp
operator|)
condition|)
block|{
comment|// This metadata is up to date. skip it.
name|log
operator|.
name|debug
argument_list|(
literal|"Skipping uptodate metadata: {}"
argument_list|,
name|metadataPath
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
name|metadataPath
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Updated metadata: {}"
argument_list|,
name|metadataPath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to write version metadata for artifact [{}]: "
argument_list|,
name|path
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initIncludes
parameter_list|( )
block|{
name|includes
operator|=
operator|new
name|ArrayList
argument_list|<>
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
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|( )
block|{
comment|//configuration.addChangeListener( this );
name|initIncludes
argument_list|( )
expr_stmt|;
block|}
block|}
end_class

end_unit

