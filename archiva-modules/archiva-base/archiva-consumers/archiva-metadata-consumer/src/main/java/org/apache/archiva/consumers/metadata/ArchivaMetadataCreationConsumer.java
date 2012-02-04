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
name|ConfigurationNames
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
name|RepositoryStorage
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
name|RepositoryStorageMetadataInvalidException
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
name|RepositoryStorageMetadataNotFoundException
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
name|RepositoryStorageRuntimeException
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
comment|/**  * Take an artifact off of disk and put it into the metadata repository.  *  * @version $Id: ArtifactUpdateDatabaseConsumer.java 718864 2008-11-19 06:33:35Z brett $  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#create-archiva-metadata"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|ArchivaMetadataCreationConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
block|{
comment|/**      * default-value="create-archiva-metadata"      */
specifier|private
name|String
name|id
init|=
literal|"create-archiva-metadata"
decl_stmt|;
comment|/**      * default-value="Create basic metadata for Archiva to be able to reference the artifact"      */
specifier|private
name|String
name|description
init|=
literal|"Create basic metadata for Archiva to be able to reference the artifact"
decl_stmt|;
comment|/**      */
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      */
annotation|@
name|Inject
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|private
name|Date
name|whenGathered
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
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|/**      * FIXME: can be of other types      */
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
comment|/**      * FIXME: this needs to be configurable based on storage type - and could also be instantiated per repo. Change to a      * factory.      */
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryStorage#maven2"
argument_list|)
specifier|private
name|RepositoryStorage
name|repositoryStorage
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
specifier|private
name|String
name|repoId
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
name|ManagedRepository
name|repo
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|repoId
operator|=
name|repo
operator|.
name|getId
argument_list|()
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
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
try|try
block|{
comment|// note that we do minimal processing including checksums and POM information for performance of
comment|// the initial scan. Any request for this information will be intercepted and populated on-demand
comment|// or picked up by subsequent scans
name|ArtifactMetadata
name|artifact
init|=
name|repositoryStorage
operator|.
name|readArtifactMetadataFromPath
argument_list|(
name|repoId
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
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|setId
argument_list|(
name|artifact
operator|.
name|getProject
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
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|boolean
name|createVersionMetadata
init|=
literal|false
decl_stmt|;
comment|// FIXME: maybe not too efficient since it may have already been read and stored for this artifact
name|ProjectVersionMetadata
name|versionMetadata
init|=
literal|null
decl_stmt|;
try|try
block|{
name|versionMetadata
operator|=
name|repositoryStorage
operator|.
name|readProjectVersionMetadata
argument_list|(
name|repoId
argument_list|,
name|artifact
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|artifact
operator|.
name|getProject
argument_list|()
argument_list|,
name|projectVersion
argument_list|)
expr_stmt|;
name|createVersionMetadata
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryStorageMetadataNotFoundException
name|e
parameter_list|)
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
name|versionMetadata
operator|.
name|setIncomplete
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|createVersionMetadata
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryStorageMetadataInvalidException
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
comment|// read the metadata and update it if it is newer or doesn't exist
name|artifact
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|updateArtifact
argument_list|(
name|repoId
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
name|artifact
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
name|repoId
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
name|repoId
argument_list|,
name|project
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error occurred persisting metadata for artifact: "
operator|+
name|path
operator|+
literal|"; message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|revert
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryStorageRuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error occurred persisting metadata for artifact: "
operator|+
name|path
operator|+
literal|"; message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|revert
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
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
name|ConsumerException
block|{
name|processFile
argument_list|(
name|path
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
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|completeScan
argument_list|()
expr_stmt|;
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
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
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
parameter_list|()
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

