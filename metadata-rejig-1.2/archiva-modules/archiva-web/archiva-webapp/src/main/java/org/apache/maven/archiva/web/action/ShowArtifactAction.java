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
name|codehaus
operator|.
name|plexus
operator|.
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
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
name|xwork
operator|.
name|Validateable
import|;
end_import

begin_comment
comment|/**  * Browse the repository.   *   * TODO change name to ShowVersionedAction to conform to terminology.  *   * @plexus.component role="com.opensymphony.xwork.Action" role-hint="showArtifactAction"  */
end_comment

begin_class
specifier|public
class|class
name|ShowArtifactAction
extends|extends
name|PlexusActionSupport
implements|implements
name|Validateable
block|{
comment|/* .\ Not Exposed \._____________________________________________ */
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
comment|/* .\ Input Parameters \.________________________________________ */
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
comment|/* .\ Exposed Output Objects \.__________________________________ */
comment|/**      * The model of this versioned project.      */
specifier|private
name|ArchivaProjectModel
name|model
decl_stmt|;
comment|/**      * The list of artifacts that depend on this versioned project.      */
specifier|private
name|List
name|dependees
decl_stmt|;
comment|/**      * The reports associated with this versioned project.      */
specifier|private
name|List
name|reports
decl_stmt|;
specifier|private
name|List
name|mailingLists
decl_stmt|;
specifier|private
name|List
name|dependencies
decl_stmt|;
comment|/**      * Show the versioned project information tab. TODO: Change name to 'project'      */
specifier|public
name|String
name|artifact
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
try|try
block|{
name|this
operator|.
name|model
operator|=
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
expr_stmt|;
name|this
operator|.
name|repositoryId
operator|=
name|repoBrowsing
operator|.
name|getRepositoryId
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
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|oe
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to find project model for ["
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
literal|"]."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
comment|/**      * Show the artifact information tab.      */
specifier|public
name|String
name|dependencies
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|this
operator|.
name|model
operator|=
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
expr_stmt|;
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
name|SUCCESS
return|;
block|}
comment|/**      * Show the mailing lists information tab.      */
specifier|public
name|String
name|mailingLists
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|this
operator|.
name|model
operator|=
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
expr_stmt|;
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
name|SUCCESS
return|;
block|}
comment|/**      * Show the reports tab.      */
specifier|public
name|String
name|reports
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"#### In reports."
argument_list|)
expr_stmt|;
comment|// TODO: hook up reports on project - this.reports = artifactsDatabase.findArtifactResults( groupId, artifactId,
comment|// version );
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"#### Found "
operator|+
name|reports
operator|.
name|size
argument_list|()
operator|+
literal|" reports."
argument_list|)
expr_stmt|;
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
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|this
operator|.
name|model
operator|=
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
expr_stmt|;
name|this
operator|.
name|dependees
operator|=
name|repoBrowsing
operator|.
name|getUsedBy
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
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
comment|/**      * Show the dependencies of this versioned project tab.      */
specifier|public
name|String
name|dependencyTree
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|this
operator|.
name|model
operator|=
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
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|String
name|getPrincipal
parameter_list|()
block|{
return|return
name|ArchivaXworkUser
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
name|getLogger
argument_list|()
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
name|getLogger
argument_list|()
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
name|getLogger
argument_list|()
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
name|ArchivaProjectModel
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
name|getReports
parameter_list|()
block|{
return|return
name|reports
return|;
block|}
specifier|public
name|List
name|getMailingLists
parameter_list|()
block|{
return|return
name|mailingLists
return|;
block|}
specifier|public
name|List
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
specifier|public
name|List
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
block|}
end_class

end_unit

