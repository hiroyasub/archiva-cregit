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
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|ObjectNotFoundException
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
name|database
operator|.
name|browsing
operator|.
name|BrowsingResults
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
name|database
operator|.
name|browsing
operator|.
name|RepositoryBrowsing
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
name|security
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * Browse the repository.  *  * @todo cache browsing results.  * @todo implement repository selectors (all or specific repository)  * @todo implement security around browse (based on repository id at first)  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="browseAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|BrowseAction
extends|extends
name|PlexusActionSupport
block|{
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|RepositoryBrowsing
name|repoBrowsing
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|UserRepositories
name|userRepositories
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaXworkUser
name|archivaXworkUser
decl_stmt|;
specifier|private
name|BrowsingResults
name|results
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
name|ArchivaProjectModel
name|sharedModel
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
name|this
operator|.
name|results
operator|=
name|repoBrowsing
operator|.
name|getRoot
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|selectedRepos
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
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
name|this
operator|.
name|results
operator|=
name|repoBrowsing
operator|.
name|selectGroupId
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|selectedRepos
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|browseArtifact
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
name|this
operator|.
name|results
operator|=
name|repoBrowsing
operator|.
name|selectArtifactId
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|selectedRepos
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|populateSharedModel
argument_list|()
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|void
name|populateSharedModel
parameter_list|()
block|{
name|sharedModel
operator|=
operator|new
name|ArchivaProjectModel
argument_list|()
expr_stmt|;
name|sharedModel
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|sharedModel
operator|.
name|setArtifactId
argument_list|(
name|artifactId
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
name|this
operator|.
name|results
operator|.
name|getVersions
argument_list|()
control|)
block|{
try|try
block|{
name|ArchivaProjectModel
name|model
init|=
name|repoBrowsing
operator|.
name|selectVersion
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|getObservableRepos
argument_list|()
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFirstVersion
condition|)
block|{
name|sharedModel
operator|=
name|model
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|sharedModel
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
name|sharedModel
operator|.
name|getPackaging
argument_list|()
argument_list|,
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
condition|)
block|{
name|sharedModel
operator|.
name|setPackaging
argument_list|(
literal|null
argument_list|)
expr_stmt|;
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
name|model
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
name|model
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
name|model
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
name|model
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
name|model
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
name|model
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
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
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
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
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
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|BrowsingResults
name|getResults
parameter_list|()
block|{
return|return
name|results
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
name|ArchivaProjectModel
name|getSharedModel
parameter_list|()
block|{
return|return
name|sharedModel
return|;
block|}
specifier|public
name|void
name|setSharedModel
parameter_list|(
name|ArchivaProjectModel
name|sharedModel
parameter_list|)
block|{
name|this
operator|.
name|sharedModel
operator|=
name|sharedModel
expr_stmt|;
block|}
block|}
end_class

end_unit

