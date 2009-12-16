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
name|reports
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
comment|/**  * Search the database of known SHA1 Checksums for potential duplicate artifacts.  *  * TODO: no need for this to be a scanner - we can just query the database / content repository to get a full list  *  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  * role-hint="duplicate-artifacts"  * instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|DuplicateArtifactsConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
implements|,
name|Initializable
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
comment|/**      * @plexus.configuration default-value="duplicate-artifacts"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Check for Duplicate Artifacts via SHA1 Checksums"      */
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
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
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
name|File
name|repositoryDir
decl_stmt|;
specifier|private
name|String
name|repoId
decl_stmt|;
specifier|private
name|ManagedRepositoryContent
name|repository
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
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
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
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
try|try
block|{
name|repoId
operator|=
name|repo
operator|.
name|getId
argument_list|()
expr_stmt|;
name|repository
operator|=
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repoId
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
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|results
init|=
name|metadataRepository
operator|.
name|getArtifactsByChecksum
argument_list|(
name|repoId
argument_list|,
name|checksumSha1
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|results
operator|.
name|size
argument_list|()
operator|<=
literal|1
condition|)
block|{
comment|// No duplicates detected.
name|log
operator|.
name|debug
argument_list|(
literal|"Found no duplicate artifact results on: "
operator|+
name|path
operator|+
literal|" (repository "
operator|+
name|repoId
operator|+
literal|")"
argument_list|)
expr_stmt|;
return|return;
block|}
name|ArtifactReference
name|artifactReference
decl_stmt|;
try|try
block|{
name|artifactReference
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
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to report problem for path: "
operator|+
name|path
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
literal|"/"
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
name|artifactReference
operator|.
name|getGroupId
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
name|artifactReference
operator|.
name|getArtifactId
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
name|artifactReference
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
comment|// Skip reference to itself.
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
name|artifactReference
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setProject
argument_list|(
name|artifactReference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setVersion
argument_list|(
name|artifactReference
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
comment|// TODO: proper path conversion for new metadata
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
name|dupArtifact
operator|.
name|getNamespace
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|dupArtifact
operator|.
name|getProject
argument_list|()
operator|+
literal|"/"
operator|+
name|dupArtifact
operator|.
name|getVersion
argument_list|()
operator|+
literal|"/"
operator|+
name|dupArtifact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setProblem
argument_list|(
literal|"duplicate-artifact"
argument_list|)
expr_stmt|;
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
block|}
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|// nothing to do
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

