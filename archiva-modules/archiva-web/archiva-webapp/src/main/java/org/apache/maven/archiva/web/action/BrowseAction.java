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

begin_comment
comment|/**  * Browse the repository.  *  * @todo implement repository selectors (all or specific repository)  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="browseAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|BrowseAction
extends|extends
name|AbstractRepositoryBasedAction
block|{
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
throws|throws
name|MetadataResolutionException
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
comment|// TODO: this logic should be optional, particularly remembering we want to keep this code simple
comment|//       it is located here to avoid the content repository implementation needing to do too much for what
comment|//       is essentially presentation code
name|Set
argument_list|<
name|String
argument_list|>
name|namespacesToCollapse
decl_stmt|;
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
name|namespacesToCollapse
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|selectedRepos
control|)
block|{
name|namespacesToCollapse
operator|.
name|addAll
argument_list|(
name|metadataResolver
operator|.
name|resolveRootNamespaces
argument_list|(
name|repositorySession
argument_list|,
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|n
range|:
name|namespacesToCollapse
control|)
block|{
comment|// TODO: check performance of this
name|namespaces
operator|.
name|add
argument_list|(
name|collapseNamespaces
argument_list|(
name|repositorySession
argument_list|,
name|metadataResolver
argument_list|,
name|selectedRepos
argument_list|,
name|n
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
name|RepositorySession
name|repositorySession
parameter_list|,
name|MetadataResolver
name|metadataResolver
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|repoIds
parameter_list|,
name|String
name|n
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|subNamespaces
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
name|repoIds
control|)
block|{
name|subNamespaces
operator|.
name|addAll
argument_list|(
name|metadataResolver
operator|.
name|resolveNamespaces
argument_list|(
name|repositorySession
argument_list|,
name|repoId
argument_list|,
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|n
operator|+
literal|" is not collapsible as it has sub-namespaces: "
operator|+
name|subNamespaces
argument_list|)
expr_stmt|;
block|}
return|return
name|n
return|;
block|}
else|else
block|{
for|for
control|(
name|String
name|repoId
range|:
name|repoIds
control|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|projects
init|=
name|metadataResolver
operator|.
name|resolveProjects
argument_list|(
name|repositorySession
argument_list|,
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
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|n
operator|+
literal|" is not collapsible as it has projects"
argument_list|)
expr_stmt|;
block|}
return|return
name|n
return|;
block|}
block|}
return|return
name|collapseNamespaces
argument_list|(
name|repositorySession
argument_list|,
name|metadataResolver
argument_list|,
name|repoIds
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
specifier|public
name|String
name|browseGroup
parameter_list|()
throws|throws
name|MetadataResolutionException
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
name|projects
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|namespaces
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
name|Set
argument_list|<
name|String
argument_list|>
name|namespacesToCollapse
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
name|namespacesToCollapse
operator|.
name|addAll
argument_list|(
name|metadataResolver
operator|.
name|resolveNamespaces
argument_list|(
name|repositorySession
argument_list|,
name|repoId
argument_list|,
name|groupId
argument_list|)
argument_list|)
expr_stmt|;
name|projects
operator|.
name|addAll
argument_list|(
name|metadataResolver
operator|.
name|resolveProjects
argument_list|(
name|repositorySession
argument_list|,
name|repoId
argument_list|,
name|groupId
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO: this logic should be optional, particularly remembering we want to keep this code simple
comment|//       it is located here to avoid the content repository implementation needing to do too much for what
comment|//       is essentially presentation code
name|namespaces
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|n
range|:
name|namespacesToCollapse
control|)
block|{
comment|// TODO: check performance of this
name|namespaces
operator|.
name|add
argument_list|(
name|collapseNamespaces
argument_list|(
name|repositorySession
argument_list|,
name|metadataResolver
argument_list|,
name|selectedRepos
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
name|MetadataResolutionException
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
name|resolveProjectVersions
argument_list|(
name|repositorySession
argument_list|,
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
name|repositorySession
argument_list|,
name|metadataResolver
argument_list|,
name|selectedRepos
argument_list|,
name|versions
argument_list|)
expr_stmt|;
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
name|populateSharedModel
parameter_list|(
name|RepositorySession
name|repositorySession
parameter_list|,
name|MetadataResolver
name|metadataResolver
parameter_list|,
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
try|try
block|{
name|versionMetadata
operator|=
name|metadataResolver
operator|.
name|resolveProjectVersion
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
literal|"Skipping invalid metadata while compiling shared model for "
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|" in repo "
operator|+
name|repoId
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
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

