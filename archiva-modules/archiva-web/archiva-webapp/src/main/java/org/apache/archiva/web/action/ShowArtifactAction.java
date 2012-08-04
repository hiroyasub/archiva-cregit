begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|generic
operator|.
name|GenericMetadataFacet
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
name|MetadataFacet
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
name|storage
operator|.
name|maven2
operator|.
name|ArtifactMetadataVersionComparator
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
name|maven2
operator|.
name|model
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
name|rest
operator|.
name|services
operator|.
name|utils
operator|.
name|ArtifactDownloadInfoBuilder
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
name|Controller
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
name|HashMap
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
comment|/**  * Browse the repository.  *<p/>  * TODO change name to ShowVersionedAction to conform to terminology.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Controller
argument_list|(
literal|"showArtifactAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|ShowArtifactAction
extends|extends
name|AbstractRepositoryBasedAction
implements|implements
name|Validateable
block|{
comment|/* .\ Not Exposed \._____________________________________________ */
annotation|@
name|Inject
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
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
name|Artifact
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
name|String
name|deleteItem
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|genericMetadata
decl_stmt|;
specifier|private
name|String
name|propertyName
decl_stmt|;
specifier|private
name|String
name|propertyValue
decl_stmt|;
comment|/**      * Show the versioned project information tab. TODO: Change name to 'project' - we are showing project versions      * here, not specific artifact information (though that is rendered in the download box).      */
specifier|public
name|String
name|artifact
parameter_list|()
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
return|return
name|handleArtifact
argument_list|(
name|repositorySession
argument_list|)
return|;
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
literal|"Unable to getProjectVersionMetadata: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"Unable to getProjectVersionMetadata - consult application logs."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
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
specifier|private
name|String
name|handleArtifact
parameter_list|(
name|RepositorySession
name|session
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|RepositoryException
block|{
comment|// In the future, this should be replaced by the repository grouping mechanism, so that we are only making
comment|// simple resource requests here and letting the resolver take care of it
name|ProjectVersionMetadata
name|versionMetadata
init|=
name|getProjectVersionMetadata
argument_list|(
name|session
argument_list|)
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
argument_list|(
literal|"Artifact metadata is incomplete."
argument_list|)
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
parameter_list|(
name|RepositorySession
name|session
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|RepositoryException
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
name|Artifact
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
name|MetadataResolver
name|metadataResolver
init|=
name|session
operator|.
name|getResolver
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
name|resolveProjectVersion
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|versionMetadata
operator|!=
literal|null
condition|)
block|{
name|MetadataFacet
name|repoProbFacet
decl_stmt|;
if|if
condition|(
operator|(
name|repoProbFacet
operator|=
name|versionMetadata
operator|.
name|getFacet
argument_list|(
name|RepositoryProblemFacet
operator|.
name|FACET_ID
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
name|addIncompleteModelWarning
argument_list|(
literal|"Artifact metadata is incomplete: "
operator|+
operator|(
operator|(
name|RepositoryProblemFacet
operator|)
name|repoProbFacet
operator|)
operator|.
name|getProblem
argument_list|()
argument_list|)
expr_stmt|;
comment|//set metadata to complete so that no additional 'Artifact metadata is incomplete' warning is logged
name|versionMetadata
operator|.
name|setIncomplete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|MetadataResolutionException
name|e
parameter_list|)
block|{
name|addIncompleteModelWarning
argument_list|(
literal|"Error resolving artifact metadata: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
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
decl_stmt|;
try|try
block|{
name|artifacts
operator|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|(
name|metadataResolver
operator|.
name|resolveArtifacts
argument_list|(
name|session
argument_list|,
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
catch|catch
parameter_list|(
name|MetadataResolutionException
name|e
parameter_list|)
block|{
name|addIncompleteModelWarning
argument_list|(
literal|"Error resolving artifact metadata: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|artifacts
argument_list|,
name|ArtifactMetadataVersionComparator
operator|.
name|INSTANCE
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
name|Artifact
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
name|Artifact
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
name|ArtifactDownloadInfoBuilder
name|builder
init|=
operator|new
name|ArtifactDownloadInfoBuilder
argument_list|()
operator|.
name|forArtifactMetadata
argument_list|(
name|artifact
argument_list|)
operator|.
name|withManagedRepositoryContent
argument_list|(
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repositoryId
argument_list|)
argument_list|)
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
name|builder
operator|.
name|build
argument_list|()
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
parameter_list|(
name|String
name|warningMessage
parameter_list|)
block|{
name|addActionError
argument_list|(
name|warningMessage
argument_list|)
expr_stmt|;
comment|//"The model may be incomplete due to a previous error in resolving information. Refer to the repository problem reports for more information." );
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
throws|throws
name|MetadataResolutionException
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
name|MetadataResolver
name|metadataResolver
init|=
name|repositorySession
operator|.
name|getResolver
argument_list|()
decl_stmt|;
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
name|resolveProjectReferences
argument_list|(
name|repositorySession
argument_list|,
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
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|dependees
operator|=
name|references
expr_stmt|;
comment|// TODO: may need to note on the page that references will be incomplete if the other artifacts are not yet
comment|// stored in the content repository
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
comment|// TODO: may need to note on the page that tree will be incomplete if the other artifacts are not yet stored in
comment|// the content repository
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
name|String
name|result
init|=
name|artifact
argument_list|()
decl_stmt|;
if|if
condition|(
name|model
operator|.
name|getFacet
argument_list|(
name|GenericMetadataFacet
operator|.
name|FACET_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|genericMetadata
operator|=
name|model
operator|.
name|getFacet
argument_list|(
name|GenericMetadataFacet
operator|.
name|FACET_ID
argument_list|)
operator|.
name|toProperties
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|genericMetadata
operator|==
literal|null
condition|)
block|{
name|genericMetadata
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|String
name|addMetadataProperty
parameter_list|()
block|{
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
name|ProjectVersionMetadata
name|projectMetadata
decl_stmt|;
try|try
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|projectMetadata
operator|=
name|getProjectVersionMetadata
argument_list|(
name|repositorySession
argument_list|)
expr_stmt|;
if|if
condition|(
name|projectMetadata
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
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
name|getFacet
argument_list|(
name|GenericMetadataFacet
operator|.
name|FACET_ID
argument_list|)
operator|==
literal|null
condition|)
block|{
name|genericMetadata
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|genericMetadata
operator|=
name|projectMetadata
operator|.
name|getFacet
argument_list|(
name|GenericMetadataFacet
operator|.
name|FACET_ID
argument_list|)
operator|.
name|toProperties
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|propertyName
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|propertyName
operator|.
name|trim
argument_list|()
argument_list|)
operator|||
name|propertyValue
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|propertyValue
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|model
operator|=
name|projectMetadata
expr_stmt|;
name|addActionError
argument_list|(
literal|"Property Name and Property Value are required."
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
name|genericMetadata
operator|.
name|put
argument_list|(
name|propertyName
argument_list|,
name|propertyValue
argument_list|)
expr_stmt|;
try|try
block|{
name|updateProjectMetadata
argument_list|(
name|projectMetadata
argument_list|,
name|metadataRepository
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
literal|"Unable to persist modified project metadata after adding entry: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"Unable to add metadata item to underlying content storage - consult application logs."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
comment|// TODO: why re-retrieve?
name|projectMetadata
operator|=
name|getProjectVersionMetadata
argument_list|(
name|repositorySession
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
literal|"Unable to getProjectVersionMetadata: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"Unable to getProjectVersionMetadata - consult application logs."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|genericMetadata
operator|=
name|projectMetadata
operator|.
name|getFacet
argument_list|(
name|GenericMetadataFacet
operator|.
name|FACET_ID
argument_list|)
operator|.
name|toProperties
argument_list|()
expr_stmt|;
name|model
operator|=
name|projectMetadata
expr_stmt|;
name|propertyName
operator|=
literal|""
expr_stmt|;
name|propertyValue
operator|=
literal|""
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
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|ProjectVersionMetadata
name|projectMetadata
init|=
name|getProjectVersionMetadata
argument_list|(
name|repositorySession
argument_list|)
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
name|getFacet
argument_list|(
name|GenericMetadataFacet
operator|.
name|FACET_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|genericMetadata
operator|=
name|projectMetadata
operator|.
name|getFacet
argument_list|(
name|GenericMetadataFacet
operator|.
name|FACET_ID
argument_list|)
operator|.
name|toProperties
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
condition|)
block|{
name|genericMetadata
operator|.
name|remove
argument_list|(
name|deleteItem
argument_list|)
expr_stmt|;
try|try
block|{
name|updateProjectMetadata
argument_list|(
name|projectMetadata
argument_list|,
name|metadataRepository
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
literal|"Unable to persist modified project metadata after removing entry: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"Unable to remove metadata item to underlying content storage - consult application logs."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
comment|// TODO: why re-retrieve?
name|projectMetadata
operator|=
name|getProjectVersionMetadata
argument_list|(
name|repositorySession
argument_list|)
expr_stmt|;
name|genericMetadata
operator|=
name|projectMetadata
operator|.
name|getFacet
argument_list|(
name|GenericMetadataFacet
operator|.
name|FACET_ID
argument_list|)
operator|.
name|toProperties
argument_list|()
expr_stmt|;
name|model
operator|=
name|projectMetadata
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Property successfully deleted."
argument_list|)
expr_stmt|;
block|}
name|deleteItem
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|addActionError
argument_list|(
literal|"No generic metadata facet for this artifact."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
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
literal|"Unable to getProjectVersionMetadata: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"Unable to getProjectVersionMetadata - consult application logs."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|void
name|updateProjectMetadata
parameter_list|(
name|ProjectVersionMetadata
name|projectMetadata
parameter_list|,
name|MetadataRepository
name|metadataRepository
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
name|GenericMetadataFacet
name|genericMetadataFacet
init|=
operator|new
name|GenericMetadataFacet
argument_list|()
decl_stmt|;
name|genericMetadataFacet
operator|.
name|fromProperties
argument_list|(
name|genericMetadata
argument_list|)
expr_stmt|;
name|projectMetadata
operator|.
name|addFacet
argument_list|(
name|genericMetadataFacet
argument_list|)
expr_stmt|;
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
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Artifact
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
name|boolean
name|isDependencyTree
parameter_list|()
block|{
return|return
name|dependencyTree
return|;
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getGenericMetadata
parameter_list|()
block|{
return|return
name|genericMetadata
return|;
block|}
specifier|public
name|void
name|setGenericMetadata
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|genericMetadata
parameter_list|)
block|{
name|this
operator|.
name|genericMetadata
operator|=
name|genericMetadata
expr_stmt|;
block|}
specifier|public
name|String
name|getPropertyName
parameter_list|()
block|{
return|return
name|propertyName
return|;
block|}
specifier|public
name|void
name|setPropertyName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|this
operator|.
name|propertyName
operator|=
name|propertyName
expr_stmt|;
block|}
specifier|public
name|String
name|getPropertyValue
parameter_list|()
block|{
return|return
name|propertyValue
return|;
block|}
specifier|public
name|void
name|setPropertyValue
parameter_list|(
name|String
name|propertyValue
parameter_list|)
block|{
name|this
operator|.
name|propertyValue
operator|=
name|propertyValue
expr_stmt|;
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
block|}
end_class

end_unit

