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
name|Date
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
name|ArchivaProjectModel
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
name|SnapshotVersion
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
name|scanner
operator|.
name|RepositoryContentConsumers
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
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|audit
operator|.
name|AuditListener
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
name|repository
operator|.
name|project
operator|.
name|ProjectModelException
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
name|project
operator|.
name|ProjectModelWriter
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
name|project
operator|.
name|writers
operator|.
name|ProjectModel400Writer
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
name|ArchivaXworkUser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|ServletActionContext
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
name|ActionContext
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
name|commons
operator|.
name|io
operator|.
name|FilenameUtils
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
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Upload an artifact using Jakarta file upload in webwork. If set by the user a pom will also be generated. Metadata  * will also be updated if one exists, otherwise it would be created.  *   * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="uploadAction"  */
end_comment

begin_class
specifier|public
class|class
name|UploadAction
extends|extends
name|PlexusActionSupport
implements|implements
name|Validateable
implements|,
name|Preparable
implements|,
name|Auditable
block|{
comment|/**       * @plexus.requirement       */
specifier|private
name|RepositoryContentConsumers
name|consumers
decl_stmt|;
comment|/**       * @plexus.requirement       */
specifier|private
name|ArchivaXworkUser
name|archivaXworkUser
decl_stmt|;
comment|/**      * The groupId of the artifact to be deployed.      */
specifier|private
name|String
name|groupId
decl_stmt|;
comment|/**      * The artifactId of the artifact to be deployed.      */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|/**      * The version of the artifact to be deployed.      */
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      * The packaging of the artifact to be deployed.      */
specifier|private
name|String
name|packaging
decl_stmt|;
comment|/**      * The classifier of the artifact to be deployed.      */
specifier|private
name|String
name|classifier
decl_stmt|;
comment|/**      * The temporary file representing the artifact to be deployed.      */
specifier|private
name|File
name|artifactFile
decl_stmt|;
comment|/**      * The content type of the artifact to be deployed.      */
specifier|private
name|String
name|artifactContentType
decl_stmt|;
comment|/**      * The original filename of the uploaded artifact file.      */
specifier|private
name|String
name|artifactFilename
decl_stmt|;
comment|/**      * The temporary file representing the pom to be deployed alongside the artifact.      */
specifier|private
name|File
name|pomFile
decl_stmt|;
comment|/**      * The content type of the pom file.      */
specifier|private
name|String
name|pomContentType
decl_stmt|;
comment|/**      * The original filename of the uploaded pom file.      */
specifier|private
name|String
name|pomFilename
decl_stmt|;
comment|/**      * The repository where the artifact is to be deployed.      */
specifier|private
name|String
name|repositoryId
decl_stmt|;
comment|/**      * Flag whether to generate a pom for the artifact or not.      */
specifier|private
name|boolean
name|generatePom
decl_stmt|;
comment|/**      * List of managed repositories to deploy to.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|managedRepoIdList
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
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.repository.audit.AuditListener"      */
specifier|private
name|List
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
init|=
operator|new
name|ArrayList
argument_list|<
name|AuditListener
argument_list|>
argument_list|()
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
specifier|private
name|ProjectModelWriter
name|pomWriter
init|=
operator|new
name|ProjectModel400Writer
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setArtifact
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|artifactFile
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|artifactContentType
operator|=
name|contentType
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactFileName
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|this
operator|.
name|artifactFilename
operator|=
name|filename
expr_stmt|;
block|}
specifier|public
name|void
name|setPom
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|pomFile
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|void
name|setPomContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|pomContentType
operator|=
name|contentType
expr_stmt|;
block|}
specifier|public
name|void
name|setPomFileName
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|this
operator|.
name|pomFilename
operator|=
name|filename
expr_stmt|;
block|}
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
name|getPackaging
parameter_list|()
block|{
return|return
name|packaging
return|;
block|}
specifier|public
name|void
name|setPackaging
parameter_list|(
name|String
name|packaging
parameter_list|)
block|{
name|this
operator|.
name|packaging
operator|=
name|packaging
expr_stmt|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
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
name|boolean
name|isGeneratePom
parameter_list|()
block|{
return|return
name|generatePom
return|;
block|}
specifier|public
name|void
name|setGeneratePom
parameter_list|(
name|boolean
name|generatePom
parameter_list|)
block|{
name|this
operator|.
name|generatePom
operator|=
name|generatePom
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getManagedRepoIdList
parameter_list|()
block|{
return|return
name|managedRepoIdList
return|;
block|}
specifier|public
name|void
name|setManagedRepoIdList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|managedRepoIdList
parameter_list|)
block|{
name|this
operator|.
name|managedRepoIdList
operator|=
name|managedRepoIdList
expr_stmt|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|managedRepoIdList
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
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
name|packaging
operator|=
literal|""
expr_stmt|;
name|classifier
operator|=
literal|""
expr_stmt|;
name|artifactFile
operator|=
literal|null
expr_stmt|;
name|artifactContentType
operator|=
literal|""
expr_stmt|;
name|artifactFilename
operator|=
literal|""
expr_stmt|;
name|pomFile
operator|=
literal|null
expr_stmt|;
name|pomContentType
operator|=
literal|""
expr_stmt|;
name|pomFilename
operator|=
literal|""
expr_stmt|;
name|repositoryId
operator|=
literal|""
expr_stmt|;
name|generatePom
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|String
name|doUpload
parameter_list|()
block|{
try|try
block|{
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
name|ArtifactReference
name|artifactReference
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|artifactReference
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|artifactReference
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|artifactReference
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|artifactReference
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
name|artifactReference
operator|.
name|setType
argument_list|(
name|packaging
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
name|artifactPath
init|=
name|repository
operator|.
name|toPath
argument_list|(
name|artifactReference
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
name|int
name|newBuildNumber
init|=
operator|-
literal|1
decl_stmt|;
name|String
name|timestamp
init|=
literal|null
decl_stmt|;
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
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
condition|)
block|{
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
name|timestamp
operator|=
name|fmt
operator|.
name|format
argument_list|(
name|lastUpdatedTimestamp
argument_list|)
expr_stmt|;
if|if
condition|(
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|newBuildNumber
operator|=
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|.
name|getBuildNumber
argument_list|()
operator|+
literal|1
expr_stmt|;
block|}
else|else
block|{
name|metadata
operator|.
name|setSnapshotVersion
argument_list|(
operator|new
name|SnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
name|newBuildNumber
operator|=
literal|1
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|targetPath
operator|.
name|exists
argument_list|()
condition|)
block|{
name|targetPath
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|String
name|filename
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
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|filename
operator|=
name|filename
operator|.
name|replaceAll
argument_list|(
literal|"SNAPSHOT"
argument_list|,
name|timestamp
operator|+
literal|"-"
operator|+
name|newBuildNumber
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|copyFile
argument_list|(
name|artifactFile
argument_list|,
name|targetPath
argument_list|,
name|filename
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|executeConsumers
argument_list|(
name|repoConfig
argument_list|,
name|repository
operator|.
name|toFile
argument_list|(
name|artifactReference
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ie
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Error encountered while uploading file: "
operator|+
name|ie
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
name|String
name|pomFilename
init|=
name|filename
decl_stmt|;
if|if
condition|(
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
condition|)
block|{
name|pomFilename
operator|=
name|StringUtils
operator|.
name|remove
argument_list|(
name|pomFilename
argument_list|,
literal|"-"
operator|+
name|classifier
argument_list|)
expr_stmt|;
block|}
name|pomFilename
operator|=
name|FilenameUtils
operator|.
name|removeExtension
argument_list|(
name|pomFilename
argument_list|)
operator|+
literal|".pom"
expr_stmt|;
if|if
condition|(
name|generatePom
condition|)
block|{
try|try
block|{
name|File
name|generatedPomFile
init|=
name|createPom
argument_list|(
name|targetPath
argument_list|,
name|pomFilename
argument_list|)
decl_stmt|;
name|consumers
operator|.
name|executeConsumers
argument_list|(
name|repoConfig
argument_list|,
name|generatedPomFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ie
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Error encountered while writing pom file: "
operator|+
name|ie
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
name|ProjectModelException
name|pe
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Error encountered while generating pom file: "
operator|+
name|pe
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
if|if
condition|(
name|pomFile
operator|!=
literal|null
operator|&&
name|pomFile
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|copyFile
argument_list|(
name|pomFile
argument_list|,
name|targetPath
argument_list|,
name|pomFilename
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|executeConsumers
argument_list|(
name|repoConfig
argument_list|,
operator|new
name|File
argument_list|(
name|targetPath
argument_list|,
name|pomFilename
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ie
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Error encountered while uploading pom file: "
operator|+
name|ie
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
name|updateMetadata
argument_list|(
name|metadata
argument_list|,
name|metadataFile
argument_list|,
name|lastUpdatedTimestamp
argument_list|,
name|timestamp
argument_list|,
name|newBuildNumber
argument_list|)
expr_stmt|;
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
literal|"\' was successfully deployed to repository \'"
operator|+
name|repositoryId
operator|+
literal|"\'"
decl_stmt|;
name|triggerAuditEvent
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|repositoryId
argument_list|,
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
argument_list|,
name|AuditEvent
operator|.
name|UPLOAD_FILE
argument_list|)
expr_stmt|;
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
name|RepositoryNotFoundException
name|re
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Target repository cannot be found: "
operator|+
name|re
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
name|rep
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Repository exception: "
operator|+
name|rep
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
name|String
name|getPrincipal
parameter_list|()
block|{
return|return
name|archivaXworkUser
operator|.
name|getActivePrincipal
argument_list|(
name|ActionContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSession
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|void
name|copyFile
parameter_list|(
name|File
name|sourceFile
parameter_list|,
name|File
name|targetPath
parameter_list|,
name|String
name|targetFilename
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
operator|new
name|File
argument_list|(
name|targetPath
argument_list|,
name|targetFilename
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|FileInputStream
name|input
init|=
operator|new
name|FileInputStream
argument_list|(
name|sourceFile
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|i
operator|=
name|input
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|File
name|createPom
parameter_list|(
name|File
name|targetPath
parameter_list|,
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
throws|,
name|ProjectModelException
block|{
name|ArchivaProjectModel
name|projectModel
init|=
operator|new
name|ArchivaProjectModel
argument_list|()
decl_stmt|;
name|projectModel
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|projectModel
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|projectModel
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|projectModel
operator|.
name|setPackaging
argument_list|(
name|packaging
argument_list|)
expr_stmt|;
name|File
name|pomFile
init|=
operator|new
name|File
argument_list|(
name|targetPath
argument_list|,
name|filename
argument_list|)
decl_stmt|;
name|pomWriter
operator|.
name|write
argument_list|(
name|projectModel
argument_list|,
name|pomFile
argument_list|)
expr_stmt|;
return|return
name|pomFile
return|;
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
comment|/**      * Update artifact level metadata. If it does not exist, create the metadata.      *       * @param metadata      */
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
parameter_list|,
name|String
name|timestamp
parameter_list|,
name|int
name|buildNumber
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
name|version
decl_stmt|;
if|if
condition|(
name|metadataFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|availableVersions
operator|=
name|metadata
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
name|version
argument_list|)
condition|)
block|{
name|availableVersions
operator|.
name|add
argument_list|(
name|version
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
name|version
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
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
name|metadata
operator|.
name|setReleasedVersion
argument_list|(
name|latestVersion
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|.
name|setBuildNumber
argument_list|(
name|buildNumber
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|.
name|setTimestamp
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
block|}
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
comment|// is this enough check for the repository permission?
if|if
condition|(
operator|!
name|userRepositories
operator|.
name|isAuthorizedToUploadArtifacts
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
literal|"User is not authorized to upload in repository "
operator|+
name|repositoryId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifactFile
operator|==
literal|null
operator|||
name|artifactFile
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|addActionError
argument_list|(
literal|"Please add a file to upload."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|VersionUtil
operator|.
name|isVersion
argument_list|(
name|version
argument_list|)
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
name|PrincipalNotFoundException
name|pe
parameter_list|)
block|{
name|addActionError
argument_list|(
name|pe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaSecurityException
name|ae
parameter_list|)
block|{
name|addActionError
argument_list|(
name|ae
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addAuditListener
parameter_list|(
name|AuditListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearAuditListeners
parameter_list|()
block|{
name|this
operator|.
name|auditListeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|removeAuditListener
parameter_list|(
name|AuditListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|AuditEvent
name|event
init|=
operator|new
name|AuditEvent
argument_list|(
name|repositoryId
argument_list|,
name|user
argument_list|,
name|resource
argument_list|,
name|action
argument_list|)
decl_stmt|;
name|event
operator|.
name|setRemoteIP
argument_list|(
name|ServletActionContext
operator|.
name|getRequest
argument_list|()
operator|.
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditListener
name|listener
range|:
name|auditListeners
control|)
block|{
name|listener
operator|.
name|auditEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

