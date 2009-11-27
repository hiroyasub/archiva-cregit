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
name|LinkedHashSet
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
name|Set
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
name|maven2
operator|.
name|MavenProjectFacet
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

begin_comment
comment|/**  * Browse the repository.  *  * @todo cache browsing results.  * @todo implement repository selectors (all or specific repository)  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="browseAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|BrowseAction
extends|extends
name|PlexusActionSupport
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataResolver
name|metadataResolver
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|UserRepositories
name|userRepositories
decl_stmt|;
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
name|repositoryId
decl_stmt|;
specifier|private
name|ProjectVersionMetadata
name|sharedModel
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|namespaces
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|projectIds
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|projectVersions
decl_stmt|;
specifier|public
name|String
name|browse
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
init|=
name|getObservableRepos
argument_list|()
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|selectedRepos
argument_list|)
condition|)
block|{
return|return
name|GlobalResults
operator|.
name|ACCESS_TO_NO_REPOS
return|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|selectedRepos
control|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|rootNamespaces
init|=
name|metadataResolver
operator|.
name|getRootNamespaces
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
comment|// TODO: this logic should be optional, particularly remembering we want to keep this code simple
comment|//       it is located here to avoid the content repository implementation needing to do too much for what
comment|//       is essentially presentation code
for|for
control|(
name|String
name|n
range|:
name|rootNamespaces
control|)
block|{
comment|// TODO: check performance of this
name|namespaces
operator|.
name|add
argument_list|(
name|collapseNamespaces
argument_list|(
name|repoId
argument_list|,
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|namespaces
operator|=
name|getSortedList
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|String
name|collapseNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|n
parameter_list|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|subNamespaces
init|=
name|metadataResolver
operator|.
name|getNamespaces
argument_list|(
name|repoId
argument_list|,
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|subNamespaces
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
return|return
name|n
return|;
block|}
else|else
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|projects
init|=
name|metadataResolver
operator|.
name|getProjects
argument_list|(
name|repoId
argument_list|,
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|projects
operator|!=
literal|null
operator|&&
operator|!
name|projects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|n
return|;
block|}
else|else
block|{
return|return
name|collapseNamespaces
argument_list|(
name|repoId
argument_list|,
name|n
operator|+
literal|"."
operator|+
name|subNamespaces
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
specifier|public
name|String
name|browseGroup
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
comment|// TODO: i18n
name|addActionError
argument_list|(
literal|"You must specify a group ID to browse"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
init|=
name|getObservableRepos
argument_list|()
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|selectedRepos
argument_list|)
condition|)
block|{
return|return
name|GlobalResults
operator|.
name|ACCESS_TO_NO_REPOS
return|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|projects
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|selectedRepos
control|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|childNamespaces
init|=
name|metadataResolver
operator|.
name|getNamespaces
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
comment|// TODO: this logic should be optional, particularly remembering we want to keep this code simple
comment|//       it is located here to avoid the content repository implementation needing to do too much for what
comment|//       is essentially presentation code
for|for
control|(
name|String
name|n
range|:
name|childNamespaces
control|)
block|{
comment|// TODO: check performance of this
name|namespaces
operator|.
name|add
argument_list|(
name|collapseNamespaces
argument_list|(
name|repoId
argument_list|,
name|groupId
operator|+
literal|"."
operator|+
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|projects
operator|.
name|addAll
argument_list|(
name|metadataResolver
operator|.
name|getProjects
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|namespaces
operator|=
name|getSortedList
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectIds
operator|=
name|getSortedList
argument_list|(
name|projects
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|ArrayList
argument_list|<
name|String
argument_list|>
name|getSortedList
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|set
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
specifier|public
name|String
name|browseArtifact
parameter_list|()
throws|throws
name|MetadataResolverException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
comment|// TODO: i18n
name|addActionError
argument_list|(
literal|"You must specify a group ID to browse"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
comment|// TODO: i18n
name|addActionError
argument_list|(
literal|"You must specify a artifact ID to browse"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
init|=
name|getObservableRepos
argument_list|()
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|selectedRepos
argument_list|)
condition|)
block|{
return|return
name|GlobalResults
operator|.
name|ACCESS_TO_NO_REPOS
return|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|selectedRepos
control|)
block|{
name|versions
operator|.
name|addAll
argument_list|(
name|metadataResolver
operator|.
name|getProjectVersions
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO: sort by known version ordering method
name|this
operator|.
name|projectVersions
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|versions
argument_list|)
expr_stmt|;
name|populateSharedModel
argument_list|(
name|selectedRepos
argument_list|,
name|versions
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|void
name|populateSharedModel
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|selectedRepos
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|projectVersions
parameter_list|)
throws|throws
name|MetadataResolverException
block|{
name|sharedModel
operator|=
operator|new
name|ProjectVersionMetadata
argument_list|()
expr_stmt|;
name|MavenProjectFacet
name|mavenFacet
init|=
operator|new
name|MavenProjectFacet
argument_list|()
decl_stmt|;
name|mavenFacet
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|mavenFacet
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|sharedModel
operator|.
name|addFacet
argument_list|(
name|mavenFacet
argument_list|)
expr_stmt|;
name|boolean
name|isFirstVersion
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|version
range|:
name|projectVersions
control|)
block|{
name|ProjectVersionMetadata
name|versionMetadata
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|selectedRepos
control|)
block|{
if|if
condition|(
name|versionMetadata
operator|==
literal|null
condition|)
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
block|}
if|if
condition|(
name|versionMetadata
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|isFirstVersion
condition|)
block|{
name|sharedModel
operator|=
name|versionMetadata
expr_stmt|;
name|sharedModel
operator|.
name|setId
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MavenProjectFacet
name|versionMetadataMavenFacet
init|=
operator|(
name|MavenProjectFacet
operator|)
name|versionMetadata
operator|.
name|getFacet
argument_list|(
name|MavenProjectFacet
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|versionMetadataMavenFacet
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|mavenFacet
operator|.
name|getPackaging
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|mavenFacet
operator|.
name|getPackaging
argument_list|()
argument_list|,
name|versionMetadataMavenFacet
operator|.
name|getPackaging
argument_list|()
argument_list|)
condition|)
block|{
name|mavenFacet
operator|.
name|setPackaging
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sharedModel
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|sharedModel
operator|.
name|getName
argument_list|()
argument_list|,
name|versionMetadata
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|sharedModel
operator|.
name|setName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sharedModel
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|sharedModel
operator|.
name|getDescription
argument_list|()
argument_list|,
name|versionMetadata
operator|.
name|getDescription
argument_list|()
argument_list|)
condition|)
block|{
name|sharedModel
operator|.
name|setDescription
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sharedModel
operator|.
name|getIssueManagement
argument_list|()
operator|!=
literal|null
operator|&&
name|versionMetadata
operator|.
name|getIssueManagement
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|sharedModel
operator|.
name|getIssueManagement
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|,
name|versionMetadata
operator|.
name|getIssueManagement
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
condition|)
block|{
name|sharedModel
operator|.
name|setIssueManagement
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sharedModel
operator|.
name|getCiManagement
argument_list|()
operator|!=
literal|null
operator|&&
name|versionMetadata
operator|.
name|getCiManagement
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|sharedModel
operator|.
name|getCiManagement
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|,
name|versionMetadata
operator|.
name|getCiManagement
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
condition|)
block|{
name|sharedModel
operator|.
name|setCiManagement
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sharedModel
operator|.
name|getOrganization
argument_list|()
operator|!=
literal|null
operator|&&
name|versionMetadata
operator|.
name|getOrganization
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|sharedModel
operator|.
name|getOrganization
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|versionMetadata
operator|.
name|getOrganization
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|sharedModel
operator|.
name|setOrganization
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sharedModel
operator|.
name|getUrl
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|sharedModel
operator|.
name|getUrl
argument_list|()
argument_list|,
name|versionMetadata
operator|.
name|getUrl
argument_list|()
argument_list|)
condition|)
block|{
name|sharedModel
operator|.
name|setUrl
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|isFirstVersion
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getObservableRepos
parameter_list|()
block|{
try|try
block|{
return|return
name|userRepositories
operator|.
name|getObservableRepositoryIds
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
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|namespaces
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
name|ProjectVersionMetadata
name|getSharedModel
parameter_list|()
block|{
return|return
name|sharedModel
return|;
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
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjectIds
parameter_list|()
block|{
return|return
name|projectIds
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjectVersions
parameter_list|()
block|{
return|return
name|projectVersions
return|;
block|}
block|}
end_class

end_unit

