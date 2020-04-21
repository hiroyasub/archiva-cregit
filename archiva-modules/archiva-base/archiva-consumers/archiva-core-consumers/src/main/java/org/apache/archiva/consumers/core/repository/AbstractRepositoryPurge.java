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
operator|.
name|repository
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
name|facets
operator|.
name|AuditEvent
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
name|maven
operator|.
name|model
operator|.
name|MavenArtifactFacet
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
name|*
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
name|ArtifactReference
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
name|ContentNotFoundException
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
name|metadata
operator|.
name|audit
operator|.
name|RepositoryListener
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
name|ItemNotFoundException
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
name|util
operator|.
name|StorageUtil
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
name|lang3
operator|.
name|StringUtils
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
name|Collection
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
name|HashSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Base class for all repository purge tasks.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryPurge
implements|implements
name|RepositoryPurge
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|( )
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|ManagedRepositoryContent
name|repository
decl_stmt|;
specifier|protected
specifier|final
name|RepositorySession
name|repositorySession
decl_stmt|;
specifier|protected
specifier|final
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
decl_stmt|;
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"org.apache.archiva.AuditLog"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|DELIM
init|=
literal|' '
decl_stmt|;
specifier|public
name|AbstractRepositoryPurge
parameter_list|(
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|RepositorySession
name|repositorySession
parameter_list|,
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|repositorySession
operator|=
name|repositorySession
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
block|}
comment|/*      * We have to track namespace, project, project version, artifact version and classifier      * There is no metadata class that contains all these properties.      */
class|class
name|ArtifactInfo
block|{
specifier|final
name|String
name|namespace
decl_stmt|;
specifier|final
name|String
name|name
decl_stmt|;
specifier|final
name|String
name|projectVersion
decl_stmt|;
name|String
name|version
decl_stmt|;
name|String
name|classifier
decl_stmt|;
name|ArtifactInfo
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|projectVersion
operator|=
name|projectVersion
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
name|ArtifactInfo
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|projectVersion
operator|=
name|projectVersion
expr_stmt|;
block|}
comment|/*          * Creates a info object without version and classifier          */
name|ArtifactInfo
name|projectVersionLevel
parameter_list|( )
block|{
return|return
operator|new
name|ArtifactInfo
argument_list|(
name|this
operator|.
name|namespace
argument_list|,
name|this
operator|.
name|name
argument_list|,
name|this
operator|.
name|projectVersion
argument_list|)
return|;
block|}
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|( )
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|( )
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getProjectVersion
parameter_list|( )
block|{
return|return
name|projectVersion
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|( )
block|{
return|return
name|version
return|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|( )
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|boolean
name|hasClassifier
parameter_list|( )
block|{
return|return
name|classifier
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|classifier
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|( )
operator|!=
name|o
operator|.
name|getClass
argument_list|( )
condition|)
return|return
literal|false
return|;
name|ArtifactInfo
name|that
init|=
operator|(
name|ArtifactInfo
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|namespace
operator|.
name|equals
argument_list|(
name|that
operator|.
name|namespace
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|projectVersion
operator|.
name|equals
argument_list|(
name|that
operator|.
name|projectVersion
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
operator|(
name|version
operator|!=
literal|null
condition|?
name|version
operator|.
name|equals
argument_list|(
name|that
operator|.
name|version
argument_list|)
else|:
name|that
operator|.
name|version
operator|==
literal|null
operator|)
condition|)
return|return
literal|false
return|;
return|return
name|classifier
operator|!=
literal|null
condition|?
name|classifier
operator|.
name|equals
argument_list|(
name|that
operator|.
name|classifier
argument_list|)
else|:
name|that
operator|.
name|classifier
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
name|int
name|result
init|=
name|namespace
operator|.
name|hashCode
argument_list|( )
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|name
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|projectVersion
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|version
operator|!=
literal|null
condition|?
name|version
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|classifier
operator|!=
literal|null
condition|?
name|classifier
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|( )
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"ArtifactInfo{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"namespace='"
argument_list|)
operator|.
name|append
argument_list|(
name|namespace
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", name='"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", projectVersion='"
argument_list|)
operator|.
name|append
argument_list|(
name|projectVersion
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", version='"
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", classifier='"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|( )
return|;
block|}
block|}
comment|/**      * Purge the repo. Update db and index of removed artifacts.      *      * @param references      */
specifier|protected
name|void
name|purge
parameter_list|(
name|Set
argument_list|<
name|Artifact
argument_list|>
name|references
parameter_list|)
block|{
if|if
condition|(
name|references
operator|!=
literal|null
operator|&&
operator|!
name|references
operator|.
name|isEmpty
argument_list|( )
condition|)
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|( )
decl_stmt|;
name|Map
argument_list|<
name|ArtifactInfo
argument_list|,
name|ArtifactMetadata
argument_list|>
name|metaRemovalList
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|>
name|metaResolved
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
for|for
control|(
name|Artifact
name|reference
range|:
name|references
control|)
block|{
name|String
name|baseVersion
init|=
name|reference
operator|.
name|getVersion
argument_list|( )
operator|.
name|getVersion
argument_list|( )
decl_stmt|;
name|String
name|namespace
init|=
name|reference
operator|.
name|getVersion
argument_list|( )
operator|.
name|getProject
argument_list|( )
operator|.
name|getNamespace
argument_list|( )
operator|.
name|getNamespace
argument_list|( )
decl_stmt|;
comment|// Needed for tracking in the hashmap
name|String
name|metaBaseId
init|=
name|reference
operator|.
name|toKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|metaResolved
operator|.
name|containsKey
argument_list|(
name|metaBaseId
argument_list|)
condition|)
block|{
try|try
block|{
name|metaResolved
operator|.
name|put
argument_list|(
name|metaBaseId
argument_list|,
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repositorySession
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
argument_list|,
name|namespace
argument_list|,
name|reference
operator|.
name|getId
argument_list|( )
argument_list|,
name|baseVersion
argument_list|)
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
name|error
argument_list|(
literal|"Error during metadata retrieval {}: {}"
argument_list|,
name|metaBaseId
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
name|StorageAsset
name|artifactFile
init|=
name|reference
operator|.
name|getAsset
argument_list|()
decl_stmt|;
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
argument_list|,
name|namespace
argument_list|,
name|reference
operator|.
name|getId
argument_list|( )
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|( )
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifactFile
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reference
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|repository
operator|.
name|deleteItem
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ContentAccessException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error while trying to delete artifact {}: {}"
argument_list|,
name|reference
operator|.
name|toString
argument_list|( )
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ItemNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Asset deleted from background other thread: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|snapshotVersion
init|=
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|baseVersion
argument_list|)
decl_stmt|;
comment|// If this is a snapshot we have to search for artifacts with the same version. And remove all of them.
if|if
condition|(
name|snapshotVersion
condition|)
block|{
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
init|=
name|metaResolved
operator|.
name|get
argument_list|(
name|metaBaseId
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifacts
operator|!=
literal|null
condition|)
block|{
comment|// cleanup snapshots metadata
for|for
control|(
name|ArtifactMetadata
name|artifactMetadata
range|:
name|artifacts
control|)
block|{
comment|// Artifact metadata and reference version should match.
if|if
condition|(
name|artifactMetadata
operator|.
name|getVersion
argument_list|( )
operator|.
name|equals
argument_list|(
name|reference
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
condition|)
block|{
name|ArtifactInfo
name|info
init|=
operator|new
name|ArtifactInfo
argument_list|(
name|artifactMetadata
operator|.
name|getNamespace
argument_list|( )
argument_list|,
name|artifactMetadata
operator|.
name|getProject
argument_list|( )
argument_list|,
name|artifactMetadata
operator|.
name|getProjectVersion
argument_list|( )
argument_list|,
name|artifactMetadata
operator|.
name|getVersion
argument_list|( )
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|reference
operator|.
name|getClassifier
argument_list|( )
argument_list|)
condition|)
block|{
name|info
operator|.
name|setClassifier
argument_list|(
name|reference
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|metaRemovalList
operator|.
name|put
argument_list|(
name|info
argument_list|,
name|artifactMetadata
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// metadataRepository.removeTimestampedArtifact( artifactMetadata, baseVersion );
name|metaRemovalList
operator|.
name|put
argument_list|(
name|info
argument_list|,
name|artifactMetadata
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
else|else
comment|// otherwise we delete the artifact version
block|{
name|ArtifactInfo
name|info
init|=
operator|new
name|ArtifactInfo
argument_list|(
name|namespace
argument_list|,
name|reference
operator|.
name|getId
argument_list|( )
argument_list|,
name|baseVersion
argument_list|,
name|reference
operator|.
name|getArtifactVersion
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|metadata
range|:
name|metaResolved
operator|.
name|get
argument_list|(
name|metaBaseId
argument_list|)
control|)
block|{
name|metaRemovalList
operator|.
name|put
argument_list|(
name|info
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
block|}
name|triggerAuditEvent
argument_list|(
name|repository
operator|.
name|getRepository
argument_list|( )
operator|.
name|getId
argument_list|( )
argument_list|,
name|reference
operator|.
name|toKey
argument_list|()
argument_list|,
name|AuditEvent
operator|.
name|PURGE_ARTIFACT
argument_list|)
expr_stmt|;
comment|// purgeSupportFiles( artifactFile );
block|}
name|purgeMetadata
argument_list|(
name|metadataRepository
argument_list|,
name|metaRemovalList
argument_list|)
expr_stmt|;
try|try
block|{
name|repositorySession
operator|.
name|save
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
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
name|MetadataSessionException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
block|}
block|}
comment|/*      * Purges the metadata. First removes the artifacts. After that empty versions will be removed.      */
specifier|private
name|void
name|purgeMetadata
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|Map
argument_list|<
name|ArtifactInfo
argument_list|,
name|ArtifactMetadata
argument_list|>
name|dataList
parameter_list|)
block|{
name|Set
argument_list|<
name|ArtifactInfo
argument_list|>
name|projectLevelMetadata
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|( )
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ArtifactInfo
argument_list|,
name|ArtifactMetadata
argument_list|>
name|infoEntry
range|:
name|dataList
operator|.
name|entrySet
argument_list|( )
control|)
block|{
name|ArtifactInfo
name|info
init|=
name|infoEntry
operator|.
name|getKey
argument_list|( )
decl_stmt|;
try|try
block|{
name|removeArtifact
argument_list|(
name|metadataRepository
argument_list|,
name|info
argument_list|,
name|infoEntry
operator|.
name|getValue
argument_list|( )
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Removed artifact from MetadataRepository {}"
argument_list|,
name|info
argument_list|)
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
name|error
argument_list|(
literal|"Could not remove artifact from MetadataRepository {}: {}"
argument_list|,
name|info
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|projectLevelMetadata
operator|.
name|add
argument_list|(
name|info
operator|.
name|projectVersionLevel
argument_list|( )
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|repositorySession
operator|.
name|save
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataSessionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not save sesion {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
init|=
literal|null
decl_stmt|;
comment|// Get remaining artifacts and remove project if empty
for|for
control|(
name|ArtifactInfo
name|info
range|:
name|projectLevelMetadata
control|)
block|{
try|try
block|{
name|artifacts
operator|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repositorySession
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
argument_list|,
name|info
operator|.
name|getNamespace
argument_list|( )
argument_list|,
name|info
operator|.
name|getName
argument_list|( )
argument_list|,
name|info
operator|.
name|getProjectVersion
argument_list|( )
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifacts
operator|.
name|size
argument_list|( )
operator|==
literal|0
condition|)
block|{
name|metadataRepository
operator|.
name|removeProjectVersion
argument_list|(
name|repositorySession
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
argument_list|,
name|info
operator|.
name|getNamespace
argument_list|( )
argument_list|,
name|info
operator|.
name|getName
argument_list|( )
argument_list|,
name|info
operator|.
name|getProjectVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Removed project version from MetadataRepository {}"
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MetadataResolutionException
decl||
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not remove project version from MetadataRepository {}: {}"
argument_list|,
name|info
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|repositorySession
operator|.
name|save
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataSessionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not save sesion {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Removes the artifact from the metadataRepository. If a classifier is set, the facet will be removed.      */
specifier|private
name|void
name|removeArtifact
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|ArtifactInfo
name|artifactInfo
parameter_list|,
name|ArtifactMetadata
name|artifactMetadata
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
if|if
condition|(
name|artifactInfo
operator|.
name|hasClassifier
argument_list|( )
condition|)
block|{
comment|// cleanup facet which contains classifier information
name|MavenArtifactFacet
name|mavenArtifactFacet
init|=
operator|(
name|MavenArtifactFacet
operator|)
name|artifactMetadata
operator|.
name|getFacet
argument_list|(
name|MavenArtifactFacet
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|artifactInfo
operator|.
name|classifier
argument_list|,
name|mavenArtifactFacet
operator|.
name|getClassifier
argument_list|( )
argument_list|)
condition|)
block|{
name|artifactMetadata
operator|.
name|removeFacet
argument_list|(
name|MavenArtifactFacet
operator|.
name|FACET_ID
argument_list|)
expr_stmt|;
name|String
name|groupId
init|=
name|artifactInfo
operator|.
name|getNamespace
argument_list|( )
decl_stmt|,
name|artifactId
init|=
name|artifactInfo
operator|.
name|getName
argument_list|( )
decl_stmt|,
name|version
init|=
name|artifactInfo
operator|.
name|getProjectVersion
argument_list|( )
decl_stmt|;
name|MavenArtifactFacet
name|mavenArtifactFacetToCompare
init|=
operator|new
name|MavenArtifactFacet
argument_list|( )
decl_stmt|;
name|mavenArtifactFacetToCompare
operator|.
name|setClassifier
argument_list|(
name|artifactInfo
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|removeFacetFromArtifact
argument_list|(
name|repositorySession
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|mavenArtifactFacetToCompare
argument_list|)
expr_stmt|;
try|try
block|{
name|repositorySession
operator|.
name|save
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataSessionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not save session {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|metadataRepository
operator|.
name|removeTimestampedArtifact
argument_list|(
name|repositorySession
argument_list|,
name|artifactMetadata
argument_list|,
name|artifactInfo
operator|.
name|getProjectVersion
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|deleteSilently
parameter_list|(
name|StorageAsset
name|path
parameter_list|)
block|{
try|try
block|{
name|path
operator|.
name|getStorage
argument_list|()
operator|.
name|removeAsset
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|repository
operator|.
name|getRepository
argument_list|( )
operator|.
name|getId
argument_list|( )
argument_list|,
name|path
operator|.
name|toString
argument_list|( )
argument_list|,
name|AuditEvent
operator|.
name|PURGE_FILE
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
literal|"Error occured during file deletion {}: {} "
argument_list|,
name|path
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<p>      * This find support files for the artifactFile and deletes them.      *</p>      *<p>      * Support Files are things like ".sha1", ".md5", ".asc", etc.      *</p>      *      * @param artifactFile the file to base off of.      */
specifier|private
name|void
name|purgeSupportFiles
parameter_list|(
name|StorageAsset
name|artifactFile
parameter_list|)
block|{
name|StorageAsset
name|parentDir
init|=
name|artifactFile
operator|.
name|getParent
argument_list|( )
decl_stmt|;
if|if
condition|(
operator|!
name|parentDir
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|String
name|artifactName
init|=
name|artifactFile
operator|.
name|getName
argument_list|( )
decl_stmt|;
name|StorageUtil
operator|.
name|walk
argument_list|(
name|parentDir
argument_list|,
name|a
lambda|->
block|{
if|if
condition|(
operator|!
name|a
operator|.
name|isContainer
argument_list|()
operator|&&
name|a
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|artifactName
argument_list|)
condition|)
name|deleteSilently
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|String
name|msg
init|=
name|repoId
operator|+
name|DELIM
operator|+
literal|"<system-purge>"
operator|+
name|DELIM
operator|+
literal|"<system>"
operator|+
name|DELIM
operator|+
literal|'\"'
operator|+
name|resource
operator|+
literal|'\"'
operator|+
name|DELIM
operator|+
literal|'\"'
operator|+
name|action
operator|+
literal|'\"'
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

