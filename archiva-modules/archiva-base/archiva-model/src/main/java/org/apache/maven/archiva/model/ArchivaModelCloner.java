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
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Properties
import|;
end_import

begin_comment
comment|/**  * Utility methods for cloning various Archiva Model objects.   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaModelCloner
block|{
specifier|public
specifier|static
name|ArchivaProjectModel
name|clone
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
block|{
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ArchivaProjectModel
name|cloned
init|=
operator|new
name|ArchivaProjectModel
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setGroupId
argument_list|(
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setArtifactId
argument_list|(
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setVersion
argument_list|(
name|model
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setParentProject
argument_list|(
name|clone
argument_list|(
name|model
operator|.
name|getParentProject
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setName
argument_list|(
name|model
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setDescription
argument_list|(
name|model
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|model
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setPackaging
argument_list|(
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setOrigin
argument_list|(
name|model
operator|.
name|getOrigin
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setMailingLists
argument_list|(
name|cloneMailingLists
argument_list|(
name|model
operator|.
name|getMailingLists
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setCiManagement
argument_list|(
name|clone
argument_list|(
name|model
operator|.
name|getCiManagement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setIndividuals
argument_list|(
name|cloneIndividuals
argument_list|(
name|model
operator|.
name|getIndividuals
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setIssueManagement
argument_list|(
name|clone
argument_list|(
name|model
operator|.
name|getIssueManagement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setLicenses
argument_list|(
name|cloneLicenses
argument_list|(
name|model
operator|.
name|getLicenses
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setOrganization
argument_list|(
name|clone
argument_list|(
name|model
operator|.
name|getOrganization
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setScm
argument_list|(
name|clone
argument_list|(
name|model
operator|.
name|getScm
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setRepositories
argument_list|(
name|cloneRepositories
argument_list|(
name|model
operator|.
name|getRepositories
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setDependencies
argument_list|(
name|cloneDependencies
argument_list|(
name|model
operator|.
name|getDependencies
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setPlugins
argument_list|(
name|clonePlugins
argument_list|(
name|model
operator|.
name|getPlugins
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setReports
argument_list|(
name|cloneReports
argument_list|(
name|model
operator|.
name|getReports
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setDependencyManagement
argument_list|(
name|cloneDependencies
argument_list|(
name|model
operator|.
name|getDependencyManagement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|ArtifactReference
name|clone
parameter_list|(
name|ArtifactReference
name|artifactReference
parameter_list|)
block|{
if|if
condition|(
name|artifactReference
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ArtifactReference
name|cloned
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setGroupId
argument_list|(
name|artifactReference
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setArtifactId
argument_list|(
name|artifactReference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setVersion
argument_list|(
name|artifactReference
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setClassifier
argument_list|(
name|artifactReference
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setType
argument_list|(
name|artifactReference
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|CiManagement
name|clone
parameter_list|(
name|CiManagement
name|ciManagement
parameter_list|)
block|{
if|if
condition|(
name|ciManagement
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|CiManagement
name|cloned
init|=
operator|new
name|CiManagement
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setSystem
argument_list|(
name|ciManagement
operator|.
name|getSystem
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|ciManagement
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|Dependency
name|clone
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
if|if
condition|(
name|dependency
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Dependency
name|cloned
init|=
operator|new
name|Dependency
argument_list|()
decl_stmt|;
comment|// Identification
name|cloned
operator|.
name|setGroupId
argument_list|(
name|dependency
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setArtifactId
argument_list|(
name|dependency
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setVersion
argument_list|(
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setClassifier
argument_list|(
name|dependency
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setType
argument_list|(
name|dependency
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// The rest.
name|cloned
operator|.
name|setTransitive
argument_list|(
name|dependency
operator|.
name|isTransitive
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setScope
argument_list|(
name|dependency
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setOptional
argument_list|(
name|dependency
operator|.
name|isOptional
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setSystemPath
argument_list|(
name|dependency
operator|.
name|getSystemPath
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|dependency
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setExclusions
argument_list|(
name|cloneExclusions
argument_list|(
name|dependency
operator|.
name|getExclusions
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|IssueManagement
name|clone
parameter_list|(
name|IssueManagement
name|issueManagement
parameter_list|)
block|{
if|if
condition|(
name|issueManagement
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|IssueManagement
name|cloned
init|=
operator|new
name|IssueManagement
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setSystem
argument_list|(
name|issueManagement
operator|.
name|getSystem
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|issueManagement
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|MailingList
name|clone
parameter_list|(
name|MailingList
name|mailingList
parameter_list|)
block|{
if|if
condition|(
name|mailingList
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|MailingList
name|cloned
init|=
operator|new
name|MailingList
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setName
argument_list|(
name|mailingList
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setSubscribeAddress
argument_list|(
name|mailingList
operator|.
name|getSubscribeAddress
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUnsubscribeAddress
argument_list|(
name|mailingList
operator|.
name|getUnsubscribeAddress
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setPostAddress
argument_list|(
name|mailingList
operator|.
name|getPostAddress
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setMainArchiveUrl
argument_list|(
name|mailingList
operator|.
name|getMainArchiveUrl
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setOtherArchives
argument_list|(
name|cloneSimpleStringList
argument_list|(
name|mailingList
operator|.
name|getOtherArchives
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|Organization
name|clone
parameter_list|(
name|Organization
name|organization
parameter_list|)
block|{
if|if
condition|(
name|organization
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Organization
name|cloned
init|=
operator|new
name|Organization
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setFavicon
argument_list|(
name|organization
operator|.
name|getFavicon
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setName
argument_list|(
name|organization
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|organization
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|Properties
name|clone
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Properties
name|cloned
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|Enumeration
name|keys
init|=
name|properties
operator|.
name|propertyNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|cloned
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|Scm
name|clone
parameter_list|(
name|Scm
name|scm
parameter_list|)
block|{
if|if
condition|(
name|scm
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Scm
name|cloned
init|=
operator|new
name|Scm
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setConnection
argument_list|(
name|scm
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setDeveloperConnection
argument_list|(
name|scm
operator|.
name|getDeveloperConnection
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|scm
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|SnapshotVersion
name|clone
parameter_list|(
name|SnapshotVersion
name|snapshotVersion
parameter_list|)
block|{
if|if
condition|(
name|snapshotVersion
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SnapshotVersion
name|cloned
init|=
operator|new
name|SnapshotVersion
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setTimestamp
argument_list|(
name|snapshotVersion
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setBuildNumber
argument_list|(
name|snapshotVersion
operator|.
name|getBuildNumber
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|VersionedReference
name|clone
parameter_list|(
name|VersionedReference
name|versionedReference
parameter_list|)
block|{
if|if
condition|(
name|versionedReference
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|VersionedReference
name|cloned
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setGroupId
argument_list|(
name|versionedReference
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setArtifactId
argument_list|(
name|versionedReference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setVersion
argument_list|(
name|versionedReference
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneArtifactReferences
parameter_list|(
name|List
name|artifactReferenceList
parameter_list|)
block|{
if|if
condition|(
name|artifactReferenceList
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|artifactReferenceList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ArtifactReference
name|artifactReference
init|=
operator|(
name|ArtifactReference
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|clone
argument_list|(
name|artifactReference
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneDependencies
parameter_list|(
name|List
name|dependencies
parameter_list|)
block|{
if|if
condition|(
name|dependencies
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|dependencies
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Dependency
name|dep
init|=
operator|(
name|Dependency
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|dep
operator|==
literal|null
condition|)
block|{
comment|// Skip null dependency.
continue|continue;
block|}
name|ret
operator|.
name|add
argument_list|(
name|clone
argument_list|(
name|dep
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneExclusions
parameter_list|(
name|List
name|exclusions
parameter_list|)
block|{
if|if
condition|(
name|exclusions
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|exclusions
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Exclusion
name|exclusion
init|=
operator|(
name|Exclusion
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Exclusion
name|cloned
init|=
operator|new
name|Exclusion
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setGroupId
argument_list|(
name|exclusion
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setArtifactId
argument_list|(
name|exclusion
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|cloned
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneIndividuals
parameter_list|(
name|List
name|individuals
parameter_list|)
block|{
if|if
condition|(
name|individuals
operator|==
literal|null
condition|)
block|{
return|return
name|individuals
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|individuals
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Individual
name|individual
init|=
operator|(
name|Individual
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Individual
name|cloned
init|=
operator|new
name|Individual
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setPrincipal
argument_list|(
name|individual
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setEmail
argument_list|(
name|individual
operator|.
name|getEmail
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setName
argument_list|(
name|individual
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setOrganization
argument_list|(
name|individual
operator|.
name|getOrganization
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setOrganizationUrl
argument_list|(
name|individual
operator|.
name|getOrganizationUrl
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|individual
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setTimezone
argument_list|(
name|individual
operator|.
name|getTimezone
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setRoles
argument_list|(
name|cloneRoles
argument_list|(
name|individual
operator|.
name|getRoles
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setProperties
argument_list|(
name|clone
argument_list|(
name|individual
operator|.
name|getProperties
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|cloned
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneLicenses
parameter_list|(
name|List
name|licenses
parameter_list|)
block|{
if|if
condition|(
name|licenses
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|licenses
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|License
name|license
init|=
operator|(
name|License
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|License
name|cloned
init|=
operator|new
name|License
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setId
argument_list|(
name|license
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setName
argument_list|(
name|license
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|license
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setComments
argument_list|(
name|license
operator|.
name|getComments
argument_list|()
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|cloned
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneMailingLists
parameter_list|(
name|List
name|mailingLists
parameter_list|)
block|{
if|if
condition|(
name|mailingLists
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|mailingLists
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|MailingList
name|mailingList
init|=
operator|(
name|MailingList
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|mailingList
operator|==
literal|null
condition|)
block|{
comment|// Skip null mailing list.
continue|continue;
block|}
name|ret
operator|.
name|add
argument_list|(
name|clone
argument_list|(
name|mailingList
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|List
name|clonePlugins
parameter_list|(
name|List
name|plugins
parameter_list|)
block|{
return|return
name|cloneArtifactReferences
argument_list|(
name|plugins
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneReports
parameter_list|(
name|List
name|reports
parameter_list|)
block|{
return|return
name|cloneArtifactReferences
argument_list|(
name|reports
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneRepositories
parameter_list|(
name|List
name|repositories
parameter_list|)
block|{
if|if
condition|(
name|repositories
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|repositories
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectRepository
name|repository
init|=
operator|(
name|ProjectRepository
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ProjectRepository
name|cloned
init|=
operator|new
name|ProjectRepository
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|setId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setName
argument_list|(
name|repository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setUrl
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setLayout
argument_list|(
name|repository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setPlugins
argument_list|(
name|repository
operator|.
name|isPlugins
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setReleases
argument_list|(
name|repository
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setSnapshots
argument_list|(
name|repository
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|cloned
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneRoles
parameter_list|(
name|List
name|roles
parameter_list|)
block|{
return|return
name|cloneSimpleStringList
argument_list|(
name|roles
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|List
name|cloneSimpleStringList
parameter_list|(
name|List
name|simple
parameter_list|)
block|{
if|if
condition|(
name|simple
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|simple
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|txt
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|txt
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|List
name|cloneAvailableVersions
parameter_list|(
name|List
name|availableVersions
parameter_list|)
block|{
return|return
name|cloneSimpleStringList
argument_list|(
name|availableVersions
argument_list|)
return|;
block|}
block|}
end_class

end_unit

