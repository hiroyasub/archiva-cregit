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

begin_comment
comment|/**  * Browse the repository.  *  * @todo cache browsing results.  * @todo implement repository selectors (all or specific repository)  * @todo implement security around browse (based on repository id at first)  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="browseAction"  */
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
specifier|public
name|String
name|browse
parameter_list|()
block|{
name|this
operator|.
name|results
operator|=
name|repoBrowsing
operator|.
name|getRoot
argument_list|()
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
name|this
operator|.
name|results
operator|=
name|repoBrowsing
operator|.
name|selectGroupId
argument_list|(
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
name|this
operator|.
name|results
operator|=
name|repoBrowsing
operator|.
name|selectArtifactId
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
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
block|}
end_class

end_unit

