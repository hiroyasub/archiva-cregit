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
name|Dependency
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
name|License
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
name|MailingList
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
name|model
operator|.
name|ProjectVersionReference
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
name|MetadataResolutionException
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
name|storage
operator|.
name|maven2
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|artifact
operator|.
name|versioning
operator|.
name|DefaultArtifactVersion
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DecimalFormat
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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_comment
comment|/**  * Browse the repository.  *  * TODO change name to ShowVersionedAction to conform to terminology.  *  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="showArtifactAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ShowArtifactAction
extends|extends
name|AbstractRepositoryBasedAction
implements|implements
name|Validateable
block|{
comment|/* .\ Not Exposed \._____________________________________________ */
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataResolver
name|metadataResolver
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
comment|/* .\ Exposed Output Objects \.__________________________________ */
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|repositoryId
decl_stmt|;
comment|/**      * The model of this versioned project.      */
specifier|private
name|ProjectVersionMetadata
name|model
decl_stmt|;
comment|/**      * The list of artifacts that depend on this versioned project.      */
specifier|private
name|List
argument_list|<
name|ProjectVersionReference
argument_list|>
name|dependees
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MailingList
argument_list|>
name|mailingLists
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ArtifactDownloadInfo
argument_list|>
argument_list|>
name|artifacts
decl_stmt|;
specifier|private
name|boolean
name|dependencyTree
init|=
literal|false
decl_stmt|;
specifier|private
name|ProjectVersionMetadata
name|projectMetadata
decl_stmt|;
specifier|private
name|String
name|deleteItem
decl_stmt|;
specifier|private
name|String
name|itemValue
decl_stmt|;
comment|/**      * Show the versioned project information tab.      * TODO: Change name to 'project' - we are showing project versions here, not specific artifact information (though      * that is rendered in the download box).      */
specifier|public
name|String
name|artifact
parameter_list|()
block|{
comment|// In the future, this should be replaced by the repository grouping mechanism, so that we are only making
comment|// simple resource requests here and letting the resolver take care of it
name|String
name|errorMsg
init|=
literal|null
decl_stmt|;
name|ProjectVersionMetadata
name|versionMetadata
init|=
name|getProjectVersionMetadata
argument_list|()
decl_stmt|;
if|if
condition|(
name|versionMetadata
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
name|errorMsg
operator|!=
literal|null
condition|?
name|errorMsg
else|:
literal|"Artifact not found"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
if|if
condition|(
name|versionMetadata
operator|.
name|isIncomplete
argument_list|()
condition|)
block|{
name|addIncompleteModelWarning
argument_list|()
expr_stmt|;
block|}
name|model
operator|=
name|versionMetadata
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|ProjectVersionMetadata
name|getProjectVersionMetadata
parameter_list|()
block|{
name|ProjectVersionMetadata
name|versionMetadata
init|=
literal|null
decl_stmt|;
name|artifacts
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ArtifactDownloadInfo
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repos
init|=
name|getObservableRepos
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|repos
control|)
block|{
if|if
condition|(
name|versionMetadata
operator|==
literal|null
condition|)
block|{
comment|// we don't want the implementation being that intelligent - so another resolver to do the
comment|// "just-in-time" nature of picking up the metadata (if appropriate for the repository type) is used
try|try
block|{
name|versionMetadata
operator|=
name|metadataResolver
operator|.
name|getProjectVersion
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataResolutionException
name|e
parameter_list|)
block|{
name|addIncompleteModelWarning
argument_list|()
expr_stmt|;
comment|// TODO: need a consistent way to construct this - same in ArchivaMetadataCreationConsumer
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
name|version
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|versionMetadata
operator|!=
literal|null
condition|)
block|{
name|repositoryId
operator|=
name|repoId
expr_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|(
name|metadataResolver
operator|.
name|getArtifacts
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|artifacts
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ArtifactMetadata
name|o1
parameter_list|,
name|ArtifactMetadata
name|o2
parameter_list|)
block|{
comment|// sort by version (reverse), then ID
comment|// TODO: move version sorting into repository handling (maven2 specific), and perhaps add a
comment|//       way to get latest instead
name|int
name|result
init|=
operator|new
name|DefaultArtifactVersion
argument_list|(
name|o2
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|compareTo
argument_list|(
operator|new
name|DefaultArtifactVersion
argument_list|(
name|o1
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|result
operator|!=
literal|0
condition|?
name|result
else|:
name|o1
operator|.
name|getId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|artifacts
control|)
block|{
name|List
argument_list|<
name|ArtifactDownloadInfo
argument_list|>
name|l
init|=
name|this
operator|.
name|artifacts
operator|.
name|get
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|l
operator|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactDownloadInfo
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|artifacts
operator|.
name|put
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
name|l
operator|.
name|add
argument_list|(
operator|new
name|ArtifactDownloadInfo
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|versionMetadata
return|;
block|}
specifier|private
name|void
name|addIncompleteModelWarning
parameter_list|()
block|{
name|addActionMessage
argument_list|(
literal|"The model may be incomplete due to a previous error in resolving information. Refer to the repository problem reports for more information."
argument_list|)
expr_stmt|;
block|}
comment|/**      * Show the artifact information tab.      */
specifier|public
name|String
name|dependencies
parameter_list|()
block|{
name|String
name|result
init|=
name|artifact
argument_list|()
decl_stmt|;
name|this
operator|.
name|dependencies
operator|=
name|model
operator|.
name|getDependencies
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Show the mailing lists information tab.      */
specifier|public
name|String
name|mailingLists
parameter_list|()
block|{
name|String
name|result
init|=
name|artifact
argument_list|()
decl_stmt|;
name|this
operator|.
name|mailingLists
operator|=
name|model
operator|.
name|getMailingLists
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Show the reports tab.      */
specifier|public
name|String
name|reports
parameter_list|()
block|{
comment|// TODO: hook up reports on project
return|return
name|SUCCESS
return|;
block|}
comment|/**      * Show the dependees (other artifacts that depend on this project) tab.      */
specifier|public
name|String
name|dependees
parameter_list|()
block|{
name|List
argument_list|<
name|ProjectVersionReference
argument_list|>
name|references
init|=
operator|new
name|ArrayList
argument_list|<
name|ProjectVersionReference
argument_list|>
argument_list|()
decl_stmt|;
comment|// TODO: what if we get duplicates across repositories?
for|for
control|(
name|String
name|repoId
range|:
name|getObservableRepos
argument_list|()
control|)
block|{
comment|// TODO: what about if we want to see this irrespective of version?
name|references
operator|.
name|addAll
argument_list|(
name|metadataResolver
operator|.
name|getProjectReferences
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|dependees
operator|=
name|references
expr_stmt|;
comment|// TODO: may need to note on the page that references will be incomplete if the other artifacts are not yet stored in the content repository
comment|// (especially in the case of pre-population import)
return|return
name|artifact
argument_list|()
return|;
block|}
comment|/**      * Show the dependencies of this versioned project tab.      */
specifier|public
name|String
name|dependencyTree
parameter_list|()
block|{
comment|// temporarily use this as we only need the model for the tag to perform, but we should be resolving the
comment|// graph here instead
comment|// TODO: may need to note on the page that tree will be incomplete if the other artifacts are not yet stored in the content repository
comment|// (especially in the case of pre-population import)
comment|// TODO: a bit ugly, should really be mapping all these results differently now
name|this
operator|.
name|dependencyTree
operator|=
literal|true
expr_stmt|;
return|return
name|artifact
argument_list|()
return|;
block|}
specifier|public
name|String
name|projectMetadata
parameter_list|()
block|{
name|projectMetadata
operator|=
name|getProjectVersionMetadata
argument_list|()
expr_stmt|;
name|String
name|errorMsg
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|projectMetadata
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
name|errorMsg
operator|!=
literal|null
condition|?
name|errorMsg
else|:
literal|"Artifact not found"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
if|if
condition|(
name|projectMetadata
operator|.
name|isIncomplete
argument_list|()
condition|)
block|{
name|addIncompleteModelWarning
argument_list|()
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|updateProjectMetadata
parameter_list|()
block|{
name|metadataRepository
operator|.
name|updateProjectVersion
argument_list|(
name|repositoryId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|projectMetadata
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|deleteMetadataEntry
parameter_list|()
block|{
name|projectMetadata
operator|=
name|getProjectVersionMetadata
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|deleteItem
argument_list|)
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|itemValue
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"dependency"
operator|.
name|equals
argument_list|(
name|deleteItem
argument_list|)
condition|)
block|{
name|removeDependency
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
literal|"mailingList"
operator|.
name|equals
argument_list|(
name|deleteItem
argument_list|)
condition|)
block|{
name|removeMailingList
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
literal|"license"
operator|.
name|equals
argument_list|(
name|deleteItem
argument_list|)
condition|)
block|{
name|removeLicense
argument_list|()
expr_stmt|;
block|}
name|deleteItem
operator|=
literal|""
expr_stmt|;
name|itemValue
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|updateProjectMetadata
argument_list|()
return|;
block|}
specifier|private
name|void
name|removeDependency
parameter_list|()
block|{
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
init|=
name|projectMetadata
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Dependency
argument_list|>
name|newDependencies
init|=
operator|new
name|ArrayList
argument_list|<
name|Dependency
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|dependencies
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Dependency
name|dependency
range|:
name|dependencies
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|itemValue
argument_list|,
name|dependency
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
name|newDependencies
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|projectMetadata
operator|.
name|setDependencies
argument_list|(
name|newDependencies
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|removeMailingList
parameter_list|()
block|{
name|List
argument_list|<
name|MailingList
argument_list|>
name|mailingLists
init|=
name|projectMetadata
operator|.
name|getMailingLists
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MailingList
argument_list|>
name|newMailingLists
init|=
operator|new
name|ArrayList
argument_list|<
name|MailingList
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|mailingLists
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|MailingList
name|mailingList
range|:
name|mailingLists
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|itemValue
argument_list|,
name|mailingList
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|newMailingLists
operator|.
name|add
argument_list|(
name|mailingList
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|projectMetadata
operator|.
name|setMailingLists
argument_list|(
name|newMailingLists
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|removeLicense
parameter_list|()
block|{
name|List
argument_list|<
name|License
argument_list|>
name|licenses
init|=
name|projectMetadata
operator|.
name|getLicenses
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|License
argument_list|>
name|newLicenses
init|=
operator|new
name|ArrayList
argument_list|<
name|License
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|licenses
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|License
name|license
range|:
name|licenses
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|itemValue
argument_list|,
name|license
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|newLicenses
operator|.
name|add
argument_list|(
name|license
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|projectMetadata
operator|.
name|setLicenses
argument_list|(
name|newLicenses
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"You must specify a group ID to browse"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"You must specify a artifact ID to browse"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"You must specify a version to browse"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ProjectVersionMetadata
name|getModel
parameter_list|()
block|{
return|return
name|model
return|;
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
name|List
argument_list|<
name|MailingList
argument_list|>
name|getMailingLists
parameter_list|()
block|{
return|return
name|mailingLists
return|;
block|}
specifier|public
name|List
argument_list|<
name|Dependency
argument_list|>
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
specifier|public
name|List
argument_list|<
name|ProjectVersionReference
argument_list|>
name|getDependees
parameter_list|()
block|{
return|return
name|dependees
return|;
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
name|MetadataResolver
name|getMetadataResolver
parameter_list|()
block|{
return|return
name|metadataResolver
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ArtifactDownloadInfo
argument_list|>
argument_list|>
name|getArtifacts
parameter_list|()
block|{
return|return
name|artifacts
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getSnapshotVersions
parameter_list|()
block|{
return|return
name|artifacts
operator|.
name|keySet
argument_list|()
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
name|boolean
name|isDependencyTree
parameter_list|()
block|{
return|return
name|dependencyTree
return|;
block|}
specifier|public
name|ProjectVersionMetadata
name|getProjectMetadata
parameter_list|()
block|{
return|return
name|projectMetadata
return|;
block|}
specifier|public
name|void
name|setProjectMetadata
parameter_list|(
name|ProjectVersionMetadata
name|projectMetadata
parameter_list|)
block|{
name|this
operator|.
name|projectMetadata
operator|=
name|projectMetadata
expr_stmt|;
block|}
specifier|public
name|void
name|setDeleteItem
parameter_list|(
name|String
name|deleteItem
parameter_list|)
block|{
name|this
operator|.
name|deleteItem
operator|=
name|deleteItem
expr_stmt|;
block|}
specifier|public
name|void
name|setItemValue
parameter_list|(
name|String
name|itemValue
parameter_list|)
block|{
name|this
operator|.
name|itemValue
operator|=
name|itemValue
expr_stmt|;
block|}
comment|// TODO: move this into the artifact metadata itself via facets where necessary
specifier|public
class|class
name|ArtifactDownloadInfo
block|{
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|namespace
decl_stmt|;
specifier|private
name|String
name|project
decl_stmt|;
specifier|private
name|String
name|size
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|String
name|repositoryId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
specifier|public
name|ArtifactDownloadInfo
parameter_list|(
name|ArtifactMetadata
name|artifact
parameter_list|)
block|{
name|repositoryId
operator|=
name|artifact
operator|.
name|getRepositoryId
argument_list|()
expr_stmt|;
comment|// TODO: use metadata resolver capability instead - maybe the storage path could be stored in the metadata
comment|//       though keep in mind the request may not necessarily need to reflect the storage
name|ManagedRepositoryContent
name|repo
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repositoryId
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
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|ArtifactReference
name|ref
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|path
operator|=
name|repo
operator|.
name|toPath
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
operator|+
literal|1
argument_list|)
operator|+
name|artifact
operator|.
name|getId
argument_list|()
expr_stmt|;
comment|// TODO: need to accommodate Maven 1 layout too. Non-maven repository formats will need to generate this
comment|//       facet (perhaps on the fly) if wanting to display the Maven 2 elements on the Archiva pages
name|String
name|type
init|=
literal|null
decl_stmt|;
name|MavenArtifactFacet
name|facet
init|=
operator|(
name|MavenArtifactFacet
operator|)
name|artifact
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
name|facet
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|facet
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|namespace
operator|=
name|artifact
operator|.
name|getNamespace
argument_list|()
expr_stmt|;
name|project
operator|=
name|artifact
operator|.
name|getProject
argument_list|()
expr_stmt|;
comment|// TODO: find a reusable formatter for this
name|double
name|s
init|=
name|artifact
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|String
name|symbol
init|=
literal|"b"
decl_stmt|;
if|if
condition|(
name|s
operator|>
literal|1024
condition|)
block|{
name|symbol
operator|=
literal|"K"
expr_stmt|;
name|s
operator|/=
literal|1024
expr_stmt|;
if|if
condition|(
name|s
operator|>
literal|1024
condition|)
block|{
name|symbol
operator|=
literal|"M"
expr_stmt|;
name|s
operator|/=
literal|1024
expr_stmt|;
if|if
condition|(
name|s
operator|>
literal|1024
condition|)
block|{
name|symbol
operator|=
literal|"G"
expr_stmt|;
name|s
operator|/=
literal|1024
expr_stmt|;
block|}
block|}
block|}
name|size
operator|=
operator|new
name|DecimalFormat
argument_list|(
literal|"#,###.##"
argument_list|)
operator|.
name|format
argument_list|(
name|s
argument_list|)
operator|+
literal|" "
operator|+
name|symbol
expr_stmt|;
name|id
operator|=
name|artifact
operator|.
name|getId
argument_list|()
expr_stmt|;
name|version
operator|=
name|artifact
operator|.
name|getVersion
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
specifier|public
name|String
name|getSize
parameter_list|()
block|{
return|return
name|size
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
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
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
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
block|}
block|}
end_class

end_unit

