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
name|web
operator|.
name|action
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Preparable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Validateable
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
name|audit
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
name|audit
operator|.
name|Auditable
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
name|repository
operator|.
name|events
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|AccessDeniedException
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
name|security
operator|.
name|ArchivaSecurityException
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
name|security
operator|.
name|PrincipalNotFoundException
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
name|security
operator|.
name|UserRepositories
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_comment
comment|/**  * Delete an artifact. Metadata will be updated if one exists, otherwise it would be created.  *  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="deleteArtifactAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|DeleteArtifactAction
extends|extends
name|PlexusActionSupport
implements|implements
name|Validateable
implements|,
name|Preparable
implements|,
name|Auditable
block|{
comment|/**      * The groupId of the artifact to be deleted.      */
specifier|private
name|String
name|groupId
decl_stmt|;
comment|/**      * The artifactId of the artifact to be deleted.      */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|/**      * The version of the artifact to be deleted.      */
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      * The repository where the artifact is to be deleted.      */
specifier|private
name|String
name|repositoryId
decl_stmt|;
comment|/**      * List of managed repositories to delete from.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|managedRepos
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|UserRepositories
name|userRepositories
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.archiva.repository.events.RepositoryListener"      */
specifier|private
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
decl_stmt|;
specifier|private
name|ChecksumAlgorithm
index|[]
name|algorithms
init|=
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|SHA1
block|,
name|ChecksumAlgorithm
operator|.
name|MD5
block|}
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getManagedRepos
parameter_list|()
block|{
return|return
name|managedRepos
return|;
block|}
specifier|public
name|void
name|setManagedRepos
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|managedRepos
parameter_list|)
block|{
name|this
operator|.
name|managedRepos
operator|=
name|managedRepos
expr_stmt|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|managedRepos
operator|=
name|getManagableRepos
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|input
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|private
name|void
name|reset
parameter_list|()
block|{
comment|// reset the fields so the form is clear when
comment|// the action returns to the jsp page
name|groupId
operator|=
literal|""
expr_stmt|;
name|artifactId
operator|=
literal|""
expr_stmt|;
name|version
operator|=
literal|""
expr_stmt|;
name|repositoryId
operator|=
literal|""
expr_stmt|;
block|}
specifier|public
name|String
name|doDelete
parameter_list|()
block|{
try|try
block|{
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
name|ManagedRepositoryConfiguration
name|repoConfig
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findManagedRepositoryById
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
name|VersionedReference
name|ref
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|ManagedRepositoryContent
name|repository
init|=
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|repository
operator|.
name|toMetadataPath
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|File
name|targetPath
init|=
operator|new
name|File
argument_list|(
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|targetPath
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ContentNotFoundException
argument_list|(
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
argument_list|)
throw|;
block|}
comment|// TODO: this should be in the storage mechanism so that it is all tied together
comment|// delete from file system
name|repository
operator|.
name|deleteVersion
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|File
name|metadataFile
init|=
name|getMetadata
argument_list|(
name|targetPath
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|ArchivaRepositoryMetadata
name|metadata
init|=
name|getMetadata
argument_list|(
name|metadataFile
argument_list|)
decl_stmt|;
name|updateMetadata
argument_list|(
name|metadata
argument_list|,
name|metadataFile
argument_list|,
name|lastUpdatedTimestamp
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
init|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repositoryId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|artifacts
control|)
block|{
comment|// TODO: mismatch between artifact (snapshot) version and project (base) version here
if|if
condition|(
name|artifact
operator|.
name|getVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|metadataRepository
operator|.
name|deleteArtifact
argument_list|(
name|artifact
operator|.
name|getRepositoryId
argument_list|()
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
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: move into the metadata repository proper - need to differentiate attachment of
comment|//       repository metadata to an artifact
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
name|repository
operator|.
name|getId
argument_list|()
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
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|triggerAuditEvent
argument_list|(
name|repositoryId
argument_list|,
name|path
argument_list|,
name|AuditEvent
operator|.
name|REMOVE_FILE
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|msg
init|=
literal|"Artifact \'"
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
operator|+
literal|"\' was successfully deleted from repository \'"
operator|+
name|repositoryId
operator|+
literal|"\'"
decl_stmt|;
name|addActionMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
catch|catch
parameter_list|(
name|ContentNotFoundException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Artifact does not exist: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Target repository cannot be found: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Repository exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
block|}
specifier|private
name|File
name|getMetadata
parameter_list|(
name|String
name|targetPath
parameter_list|)
block|{
name|String
name|artifactPath
init|=
name|targetPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|targetPath
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separatorChar
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|artifactPath
argument_list|,
name|MetadataTools
operator|.
name|MAVEN_METADATA
argument_list|)
return|;
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
comment|/**      * Update artifact level metadata. Creates one if metadata does not exist after artifact deletion.      *      * @param metadata      */
specifier|private
name|void
name|updateMetadata
parameter_list|(
name|ArchivaRepositoryMetadata
name|metadata
parameter_list|,
name|File
name|metadataFile
parameter_list|,
name|Date
name|lastUpdatedTimestamp
parameter_list|)
throws|throws
name|RepositoryMetadataException
block|{
name|List
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
literal|""
decl_stmt|;
if|if
condition|(
name|metadataFile
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|availableVersions
operator|=
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
expr_stmt|;
if|if
condition|(
name|availableVersions
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
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
name|availableVersions
operator|.
name|contains
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|availableVersions
operator|.
name|remove
argument_list|(
name|availableVersions
operator|.
name|indexOf
argument_list|(
name|version
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableVersions
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
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
block|}
block|}
block|}
if|if
condition|(
name|metadata
operator|.
name|getGroupId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|metadata
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|metadata
operator|.
name|getArtifactId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|metadata
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
condition|)
block|{
if|if
condition|(
name|metadata
operator|.
name|getReleasedVersion
argument_list|()
operator|!=
literal|null
operator|&&
name|metadata
operator|.
name|getReleasedVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|metadata
operator|.
name|setReleasedVersion
argument_list|(
name|latestVersion
argument_list|)
expr_stmt|;
block|}
block|}
name|metadata
operator|.
name|setLatestVersion
argument_list|(
name|latestVersion
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setLastUpdatedTimestamp
argument_list|(
name|lastUpdatedTimestamp
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setAvailableVersions
argument_list|(
name|availableVersions
argument_list|)
expr_stmt|;
name|RepositoryMetadataWriter
operator|.
name|write
argument_list|(
name|metadata
argument_list|,
name|metadataFile
argument_list|)
expr_stmt|;
name|ChecksummedFile
name|checksum
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|metadataFile
argument_list|)
decl_stmt|;
name|checksum
operator|.
name|fixChecksums
argument_list|(
name|algorithms
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|!
name|userRepositories
operator|.
name|isAuthorizedToDeleteArtifacts
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|repositoryId
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"User is not authorized to delete artifacts in repository '"
operator|+
name|repositoryId
operator|+
literal|"'."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|version
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
operator|&&
operator|(
operator|!
name|VersionUtil
operator|.
name|isVersion
argument_list|(
name|version
argument_list|)
operator|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Invalid version."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaSecurityException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getManagableRepos
parameter_list|()
block|{
try|try
block|{
return|return
name|userRepositories
operator|.
name|getManagableRepositoryIds
argument_list|(
name|getPrincipal
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PrincipalNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// TODO: pass this onto the screen.
block|}
catch|catch
parameter_list|(
name|ArchivaSecurityException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|getListeners
parameter_list|()
block|{
return|return
name|listeners
return|;
block|}
specifier|public
name|void
name|setRepositoryFactory
parameter_list|(
name|RepositoryContentFactory
name|repositoryFactory
parameter_list|)
block|{
name|this
operator|.
name|repositoryFactory
operator|=
name|repositoryFactory
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
name|setMetadataRepository
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|)
block|{
name|this
operator|.
name|metadataRepository
operator|=
name|metadataRepository
expr_stmt|;
block|}
block|}
end_class

end_unit

