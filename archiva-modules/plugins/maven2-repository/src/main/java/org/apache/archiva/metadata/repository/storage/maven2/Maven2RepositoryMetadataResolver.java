begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|maven2
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
name|MetadataResolver
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
name|MetadataResolverException
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
name|xml
operator|.
name|XMLException
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
name|model
operator|.
name|CiManagement
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
name|model
operator|.
name|Dependency
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
name|model
operator|.
name|IssueManagement
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
name|model
operator|.
name|License
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
name|model
operator|.
name|MailingList
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
name|model
operator|.
name|Model
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
name|model
operator|.
name|Organization
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
name|model
operator|.
name|Scm
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
name|model
operator|.
name|building
operator|.
name|DefaultModelBuildingRequest
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
name|model
operator|.
name|building
operator|.
name|ModelBuilder
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
name|model
operator|.
name|building
operator|.
name|ModelBuildingException
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
name|model
operator|.
name|building
operator|.
name|ModelBuildingRequest
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
comment|/**  * @plexus.component role="org.apache.archiva.metadata.repository.MetadataResolver" role-hint="maven2"  */
end_comment

begin_class
specifier|public
class|class
name|Maven2RepositoryMetadataResolver
implements|implements
name|MetadataResolver
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ModelBuilder
name|builder
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="maven2"      */
specifier|private
name|RepositoryPathTranslator
name|pathTranslator
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Maven2RepositoryMetadataResolver
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|ProjectMetadata
name|getProject
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|ProjectVersionMetadata
name|getProjectVersion
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
throws|throws
name|MetadataResolverException
block|{
name|ManagedRepositoryConfiguration
name|repositoryConfiguration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findManagedRepositoryById
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
name|String
name|artifactVersion
init|=
name|projectVersion
decl_stmt|;
name|File
name|basedir
init|=
operator|new
name|File
argument_list|(
name|repositoryConfiguration
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|projectVersion
argument_list|)
condition|)
block|{
name|File
name|metadataFile
init|=
name|pathTranslator
operator|.
name|toFile
argument_list|(
name|basedir
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
literal|"maven-metadata.xml"
argument_list|)
decl_stmt|;
try|try
block|{
name|MavenRepositoryMetadata
name|metadata
init|=
name|MavenRepositoryMetadataReader
operator|.
name|read
argument_list|(
name|metadataFile
argument_list|)
decl_stmt|;
comment|// re-adjust to timestamp if present, otherwise retain the original -SNAPSHOT filename
name|MavenRepositoryMetadata
operator|.
name|Snapshot
name|snapshotVersion
init|=
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|snapshotVersion
operator|!=
literal|null
condition|)
block|{
name|artifactVersion
operator|=
name|artifactVersion
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|artifactVersion
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
expr_stmt|;
comment|// remove SNAPSHOT from end
name|artifactVersion
operator|=
name|artifactVersion
operator|+
name|snapshotVersion
operator|.
name|getTimestamp
argument_list|()
operator|+
literal|"-"
operator|+
name|snapshotVersion
operator|.
name|getBuildNumber
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLException
name|e
parameter_list|)
block|{
comment|// unable to parse metadata - log it, and continue with the version as the original SNAPSHOT version
name|log
operator|.
name|warn
argument_list|(
literal|"Invalid metadata: "
operator|+
name|metadataFile
operator|+
literal|" - "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|File
name|file
init|=
name|pathTranslator
operator|.
name|toFile
argument_list|(
name|basedir
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|projectId
operator|+
literal|"-"
operator|+
name|artifactVersion
operator|+
literal|".pom"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// metadata could not be resolved
return|return
literal|null
return|;
block|}
name|ModelBuildingRequest
name|req
init|=
operator|new
name|DefaultModelBuildingRequest
argument_list|()
decl_stmt|;
name|req
operator|.
name|setProcessPlugins
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|req
operator|.
name|setPomFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|req
operator|.
name|setModelResolver
argument_list|(
operator|new
name|RepositoryModelResolver
argument_list|(
name|basedir
argument_list|,
name|pathTranslator
argument_list|)
argument_list|)
expr_stmt|;
name|req
operator|.
name|setValidationLevel
argument_list|(
name|ModelBuildingRequest
operator|.
name|VALIDATION_LEVEL_MINIMAL
argument_list|)
expr_stmt|;
name|Model
name|model
decl_stmt|;
try|try
block|{
name|model
operator|=
name|builder
operator|.
name|build
argument_list|(
name|req
argument_list|)
operator|.
name|getEffectiveModel
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ModelBuildingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MetadataResolverException
argument_list|(
literal|"Unable to build Maven POM to derive metadata from: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|ProjectVersionMetadata
name|metadata
init|=
operator|new
name|ProjectVersionMetadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setCiManagement
argument_list|(
name|convertCiManagement
argument_list|(
name|model
operator|.
name|getCiManagement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setDescription
argument_list|(
name|model
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setId
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setIssueManagement
argument_list|(
name|convertIssueManagement
argument_list|(
name|model
operator|.
name|getIssueManagement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setLicenses
argument_list|(
name|convertLicenses
argument_list|(
name|model
operator|.
name|getLicenses
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setMailingLists
argument_list|(
name|convertMailingLists
argument_list|(
name|model
operator|.
name|getMailingLists
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setDependencies
argument_list|(
name|convertDependencies
argument_list|(
name|model
operator|.
name|getDependencies
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setName
argument_list|(
name|model
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setOrganization
argument_list|(
name|convertOrganization
argument_list|(
name|model
operator|.
name|getOrganization
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setScm
argument_list|(
name|convertScm
argument_list|(
name|model
operator|.
name|getScm
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setUrl
argument_list|(
name|model
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|MavenProjectFacet
name|facet
init|=
operator|new
name|MavenProjectFacet
argument_list|()
decl_stmt|;
name|facet
operator|.
name|setGroupId
argument_list|(
name|model
operator|.
name|getGroupId
argument_list|()
operator|!=
literal|null
condition|?
name|model
operator|.
name|getGroupId
argument_list|()
else|:
name|model
operator|.
name|getParent
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|facet
operator|.
name|setArtifactId
argument_list|(
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|facet
operator|.
name|setPackaging
argument_list|(
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|model
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|MavenProjectParent
name|parent
init|=
operator|new
name|MavenProjectParent
argument_list|()
decl_stmt|;
name|parent
operator|.
name|setGroupId
argument_list|(
name|model
operator|.
name|getParent
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setArtifactId
argument_list|(
name|model
operator|.
name|getParent
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setVersion
argument_list|(
name|model
operator|.
name|getParent
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|facet
operator|.
name|setParent
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
name|metadata
operator|.
name|addFacet
argument_list|(
name|facet
argument_list|)
expr_stmt|;
return|return
name|metadata
return|;
block|}
specifier|private
name|List
argument_list|<
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
name|Dependency
argument_list|>
name|convertDependencies
parameter_list|(
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
parameter_list|)
block|{
name|List
argument_list|<
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
name|Dependency
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
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
name|Dependency
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Dependency
name|dependency
range|:
name|dependencies
control|)
block|{
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
name|Dependency
name|newDependency
init|=
operator|new
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
name|Dependency
argument_list|()
decl_stmt|;
name|newDependency
operator|.
name|setArtifactId
argument_list|(
name|dependency
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|newDependency
operator|.
name|setClassifier
argument_list|(
name|dependency
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|newDependency
operator|.
name|setGroupId
argument_list|(
name|dependency
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|newDependency
operator|.
name|setOptional
argument_list|(
name|dependency
operator|.
name|isOptional
argument_list|()
argument_list|)
expr_stmt|;
name|newDependency
operator|.
name|setScope
argument_list|(
name|dependency
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|newDependency
operator|.
name|setSystemPath
argument_list|(
name|dependency
operator|.
name|getSystemPath
argument_list|()
argument_list|)
expr_stmt|;
name|newDependency
operator|.
name|setType
argument_list|(
name|dependency
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|newDependency
operator|.
name|setVersion
argument_list|(
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|l
operator|.
name|add
argument_list|(
name|newDependency
argument_list|)
expr_stmt|;
block|}
return|return
name|l
return|;
block|}
specifier|private
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
name|Scm
name|convertScm
parameter_list|(
name|Scm
name|scm
parameter_list|)
block|{
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
name|Scm
name|newScm
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|scm
operator|!=
literal|null
condition|)
block|{
name|newScm
operator|=
operator|new
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
name|Scm
argument_list|()
expr_stmt|;
name|newScm
operator|.
name|setConnection
argument_list|(
name|scm
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|newScm
operator|.
name|setDeveloperConnection
argument_list|(
name|scm
operator|.
name|getDeveloperConnection
argument_list|()
argument_list|)
expr_stmt|;
name|newScm
operator|.
name|setUrl
argument_list|(
name|scm
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|newScm
return|;
block|}
specifier|private
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
name|Organization
name|convertOrganization
parameter_list|(
name|Organization
name|organization
parameter_list|)
block|{
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
name|Organization
name|org
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|organization
operator|!=
literal|null
condition|)
block|{
name|org
operator|=
operator|new
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
name|Organization
argument_list|()
expr_stmt|;
name|org
operator|.
name|setName
argument_list|(
name|organization
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|org
operator|.
name|setUrl
argument_list|(
name|organization
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|org
return|;
block|}
specifier|private
name|List
argument_list|<
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
name|License
argument_list|>
name|convertLicenses
parameter_list|(
name|List
argument_list|<
name|License
argument_list|>
name|licenses
parameter_list|)
block|{
name|List
argument_list|<
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
name|License
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
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
name|License
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|License
name|license
range|:
name|licenses
control|)
block|{
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
name|License
name|newLicense
init|=
operator|new
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
name|License
argument_list|()
decl_stmt|;
name|newLicense
operator|.
name|setName
argument_list|(
name|license
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|newLicense
operator|.
name|setUrl
argument_list|(
name|license
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|l
operator|.
name|add
argument_list|(
name|newLicense
argument_list|)
expr_stmt|;
block|}
return|return
name|l
return|;
block|}
specifier|private
name|List
argument_list|<
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
name|MailingList
argument_list|>
name|convertMailingLists
parameter_list|(
name|List
argument_list|<
name|MailingList
argument_list|>
name|mailingLists
parameter_list|)
block|{
name|List
argument_list|<
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
name|MailingList
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
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
name|MailingList
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|MailingList
name|mailingList
range|:
name|mailingLists
control|)
block|{
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
name|MailingList
name|newMailingList
init|=
operator|new
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
name|MailingList
argument_list|()
decl_stmt|;
name|newMailingList
operator|.
name|setName
argument_list|(
name|mailingList
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|newMailingList
operator|.
name|setMainArchiveUrl
argument_list|(
name|mailingList
operator|.
name|getArchive
argument_list|()
argument_list|)
expr_stmt|;
name|newMailingList
operator|.
name|setPostAddress
argument_list|(
name|mailingList
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
name|newMailingList
operator|.
name|setSubscribeAddress
argument_list|(
name|mailingList
operator|.
name|getSubscribe
argument_list|()
argument_list|)
expr_stmt|;
name|newMailingList
operator|.
name|setUnsubscribeAddress
argument_list|(
name|mailingList
operator|.
name|getUnsubscribe
argument_list|()
argument_list|)
expr_stmt|;
name|newMailingList
operator|.
name|setOtherArchives
argument_list|(
name|mailingList
operator|.
name|getOtherArchives
argument_list|()
argument_list|)
expr_stmt|;
name|l
operator|.
name|add
argument_list|(
name|newMailingList
argument_list|)
expr_stmt|;
block|}
return|return
name|l
return|;
block|}
specifier|private
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
name|IssueManagement
name|convertIssueManagement
parameter_list|(
name|IssueManagement
name|issueManagement
parameter_list|)
block|{
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
name|IssueManagement
name|im
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|issueManagement
operator|!=
literal|null
condition|)
block|{
name|im
operator|=
operator|new
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
name|IssueManagement
argument_list|()
expr_stmt|;
name|im
operator|.
name|setSystem
argument_list|(
name|issueManagement
operator|.
name|getSystem
argument_list|()
argument_list|)
expr_stmt|;
name|im
operator|.
name|setUrl
argument_list|(
name|issueManagement
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|im
return|;
block|}
specifier|private
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
name|CiManagement
name|convertCiManagement
parameter_list|(
name|CiManagement
name|ciManagement
parameter_list|)
block|{
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
name|CiManagement
name|ci
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ciManagement
operator|!=
literal|null
condition|)
block|{
name|ci
operator|=
operator|new
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
name|CiManagement
argument_list|()
expr_stmt|;
name|ci
operator|.
name|setSystem
argument_list|(
name|ciManagement
operator|.
name|getSystem
argument_list|()
argument_list|)
expr_stmt|;
name|ci
operator|.
name|setUrl
argument_list|(
name|ciManagement
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ci
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getArtifactVersions
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

