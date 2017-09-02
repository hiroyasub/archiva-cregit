begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|reports
operator|.
name|consumers
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
name|redback
operator|.
name|components
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
name|apache
operator|.
name|archiva
operator|.
name|redback
operator|.
name|components
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
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|facets
operator|.
name|RepositoryProblemFacet
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
name|collections
operator|.
name|CollectionUtils
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
name|Collection
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

begin_comment
comment|/**  * Search the artifact repository of known SHA1 Checksums for potential duplicate artifacts.  *<p>  * TODO: no need for this to be a scanner - we can just query the database / content repository to get a full list  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#duplicate-artifacts"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|DuplicateArtifactsConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DuplicateArtifactsConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|id
init|=
literal|"duplicate-artifacts"
decl_stmt|;
specifier|private
name|String
name|description
init|=
literal|"Check for Duplicate Artifacts via SHA1 Checksums"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
comment|/**      * FIXME: this could be multiple implementations and needs to be configured.      */
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
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
argument_list|()
decl_stmt|;
specifier|private
name|File
name|repositoryDir
decl_stmt|;
specifier|private
name|String
name|repoId
decl_stmt|;
comment|/**      * FIXME: needs to be selected based on the repository in question      */
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryPathTranslator#maven2"
argument_list|)
specifier|private
name|RepositoryPathTranslator
name|pathTranslator
decl_stmt|;
specifier|private
name|RepositorySession
name|repositorySession
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
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
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
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
name|repositoryDir
operator|=
operator|new
name|File
argument_list|(
name|repo
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repo
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
name|repo
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
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
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
comment|// TODO: would be quicker to somehow make sure it ran after the update database consumer, or as a part of that
comment|//  perhaps could use an artifact context that is retained for all consumers? First in can set the SHA-1
comment|//  alternatively this could come straight from the storage resolver, which could populate the artifact metadata
comment|//  in the later parse call with the desired checksum and use that
name|String
name|checksumSha1
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|artifactFile
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|checksumSha1
operator|=
name|checksummedFile
operator|.
name|calculateChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
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
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|results
decl_stmt|;
try|try
block|{
name|results
operator|=
name|metadataRepository
operator|.
name|getArtifactsByChecksum
argument_list|(
name|repoId
argument_list|,
name|checksumSha1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|results
argument_list|)
condition|)
block|{
name|ArtifactMetadata
name|originalArtifact
decl_stmt|;
try|try
block|{
name|originalArtifact
operator|=
name|pathTranslator
operator|.
name|getArtifactForPath
argument_list|(
name|repoId
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Not reporting problem for invalid artifact in checksum check: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|ArtifactMetadata
name|dupArtifact
range|:
name|results
control|)
block|{
name|String
name|id
init|=
name|path
operator|.
name|substring
argument_list|(
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|dupArtifact
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
operator|&&
name|dupArtifact
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|originalArtifact
operator|.
name|getNamespace
argument_list|()
argument_list|)
operator|&&
name|dupArtifact
operator|.
name|getProject
argument_list|()
operator|.
name|equals
argument_list|(
name|originalArtifact
operator|.
name|getProject
argument_list|()
argument_list|)
operator|&&
name|dupArtifact
operator|.
name|getVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|originalArtifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
comment|// Skip reference to itself.
name|log
operator|.
name|debug
argument_list|(
literal|"Not counting duplicate for artifact {} for path {}"
argument_list|,
name|dupArtifact
argument_list|,
name|path
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|RepositoryProblemFacet
name|problem
init|=
operator|new
name|RepositoryProblemFacet
argument_list|()
decl_stmt|;
name|problem
operator|.
name|setRepositoryId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setNamespace
argument_list|(
name|originalArtifact
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setProject
argument_list|(
name|originalArtifact
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setVersion
argument_list|(
name|originalArtifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
comment|// FIXME: need to get the right storage resolver for the repository the dupe artifact is in, it might be
comment|//       a different type
comment|// FIXME: we need the project version here, not the artifact version
name|problem
operator|.
name|setMessage
argument_list|(
literal|"Duplicate Artifact Detected: "
operator|+
name|path
operator|+
literal|"<--> "
operator|+
name|pathTranslator
operator|.
name|toPath
argument_list|(
name|dupArtifact
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|dupArtifact
operator|.
name|getProject
argument_list|()
argument_list|,
name|dupArtifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|dupArtifact
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setProblem
argument_list|(
literal|"duplicate-artifact"
argument_list|)
expr_stmt|;
try|try
block|{
name|metadataRepository
operator|.
name|addMetadataFacet
argument_list|(
name|repoId
argument_list|,
name|problem
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
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
name|ConsumerException
block|{
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
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
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|initIncludes
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

