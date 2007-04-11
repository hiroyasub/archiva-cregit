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
name|Configuration
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
name|ArchivaDAO
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
name|web
operator|.
name|util
operator|.
name|VersionMerger
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
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|IOException
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
name|HashSet
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Browse the repository.  *  * TODO change name to ShowVersionedAction to conform to terminology.  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="showArtifactAction"  */
end_comment

begin_class
specifier|public
class|class
name|ShowArtifactAction
extends|extends
name|PlexusActionSupport
block|{
comment|/* .\ Not Exposed \._____________________________________________ */
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
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
comment|/**      * Show the versioned project information tab.      *       * TODO: Change name to 'project'      */
specifier|public
name|String
name|artifact
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|this
operator|.
name|model
operator|=
name|readProject
argument_list|()
expr_stmt|;
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
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|this
operator|.
name|model
operator|=
name|readProject
argument_list|()
expr_stmt|;
comment|// TODO: should this be the whole set of artifacts, and be more like the maven dependencies report?
comment|// this.dependencies = VersionMerger.wrap( project.getModel().getDependencies() );
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
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|this
operator|.
name|model
operator|=
name|readProject
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
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"#### In reports."
argument_list|)
expr_stmt|;
comment|// TODO: hook up reports on project - this.reports = artifactsDatabase.findArtifactResults( groupId, artifactId, version );
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
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|this
operator|.
name|model
operator|=
name|readProject
argument_list|()
expr_stmt|;
comment|// TODO: create depends on collector.
name|this
operator|.
name|dependees
operator|=
name|Collections
operator|.
name|EMPTY_LIST
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
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|this
operator|.
name|model
operator|=
name|readProject
argument_list|()
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|ArchivaProjectModel
name|readProject
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
return|return
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|getProjectModel
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|checkParameters
parameter_list|()
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
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
name|result
operator|=
literal|false
expr_stmt|;
block|}
if|else if
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
name|result
operator|=
literal|false
expr_stmt|;
block|}
if|else if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|version
argument_list|)
condition|)
block|{
comment|// TODO: i18n
name|addActionError
argument_list|(
literal|"You must specify a version to browse"
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|result
return|;
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
block|}
end_class

end_unit

