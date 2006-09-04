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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|configuration
operator|.
name|ConfigurationStore
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
name|ConfigurationStoreException
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
name|ConfiguredRepositoryFactory
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
name|indexer
operator|.
name|RepositoryArtifactIndex
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
name|indexer
operator|.
name|RepositoryArtifactIndexFactory
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
name|indexer
operator|.
name|RepositoryIndexException
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
name|indexer
operator|.
name|RepositoryIndexSearchException
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
name|util
operator|.
name|StringUtils
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
name|Collections
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * Browse the repository.  *  * @todo cache should be a proper cache class that is a singleton requirement rather than static variables  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="browseAction"  */
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
name|RepositoryArtifactIndexFactory
name|factory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfiguredRepositoryFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
specifier|private
name|List
name|groups
decl_stmt|;
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_SEPARATOR
init|=
literal|"."
decl_stmt|;
specifier|private
name|List
name|artifactIds
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|List
name|versions
decl_stmt|;
specifier|private
specifier|static
name|GroupTreeNode
name|rootNode
decl_stmt|;
specifier|private
specifier|static
name|long
name|groupCacheTime
decl_stmt|;
specifier|public
name|String
name|browse
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|RepositoryIndexException
throws|,
name|IOException
block|{
name|RepositoryArtifactIndex
name|index
init|=
name|getIndex
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|index
operator|.
name|exists
argument_list|()
condition|)
block|{
name|addActionError
argument_list|(
literal|"The repository is not yet indexed. Please wait, and then try again."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
name|GroupTreeNode
name|rootNode
init|=
name|buildGroupTree
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|this
operator|.
name|groups
operator|=
name|collateGroups
argument_list|(
name|rootNode
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
throws|throws
name|ConfigurationStoreException
throws|,
name|RepositoryIndexException
throws|,
name|IOException
throws|,
name|RepositoryIndexSearchException
block|{
name|RepositoryArtifactIndex
name|index
init|=
name|getIndex
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|index
operator|.
name|exists
argument_list|()
condition|)
block|{
name|addActionError
argument_list|(
literal|"The repository is not yet indexed. Please wait, and then try again."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
name|GroupTreeNode
name|rootNode
init|=
name|buildGroupTree
argument_list|(
name|index
argument_list|)
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
return|return
name|ERROR
return|;
block|}
name|StringTokenizer
name|tok
init|=
operator|new
name|StringTokenizer
argument_list|(
name|groupId
argument_list|,
name|GROUP_SEPARATOR
argument_list|)
decl_stmt|;
while|while
condition|(
name|tok
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|part
init|=
name|tok
operator|.
name|nextToken
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|rootNode
operator|.
name|getChildren
argument_list|()
operator|.
name|containsKey
argument_list|(
name|part
argument_list|)
condition|)
block|{
comment|// TODO: i18n
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Can't find part: "
operator|+
name|part
operator|+
literal|" for groupId "
operator|+
name|groupId
operator|+
literal|" in children "
operator|+
name|rootNode
operator|.
name|getChildren
argument_list|()
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"The group specified was not found"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
else|else
block|{
name|rootNode
operator|=
operator|(
name|GroupTreeNode
operator|)
name|rootNode
operator|.
name|getChildren
argument_list|()
operator|.
name|get
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|groups
operator|=
name|collateGroups
argument_list|(
name|rootNode
argument_list|)
expr_stmt|;
name|this
operator|.
name|artifactIds
operator|=
name|index
operator|.
name|getArtifactIds
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|this
operator|.
name|artifactIds
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
throws|throws
name|ConfigurationStoreException
throws|,
name|RepositoryIndexException
throws|,
name|IOException
throws|,
name|RepositoryIndexSearchException
block|{
name|RepositoryArtifactIndex
name|index
init|=
name|getIndex
argument_list|()
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
name|versions
operator|=
name|index
operator|.
name|getVersions
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|this
operator|.
name|versions
argument_list|)
expr_stmt|;
if|if
condition|(
name|versions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// TODO: i18n
name|addActionError
argument_list|(
literal|"Could not find any artifacts with the given group and artifact ID"
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
specifier|private
name|GroupTreeNode
name|buildGroupTree
parameter_list|(
name|RepositoryArtifactIndex
name|index
parameter_list|)
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
block|{
comment|// TODO: give action message if indexing is in progress
name|long
name|lastUpdate
init|=
name|index
operator|.
name|getLastUpdatedTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|rootNode
operator|==
literal|null
operator|||
name|lastUpdate
operator|>
name|groupCacheTime
condition|)
block|{
name|List
name|groups
init|=
name|index
operator|.
name|getAllGroupIds
argument_list|()
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Loaded "
operator|+
name|groups
operator|.
name|size
argument_list|()
operator|+
literal|" groups from index"
argument_list|)
expr_stmt|;
name|rootNode
operator|=
operator|new
name|GroupTreeNode
argument_list|()
expr_stmt|;
comment|// build a tree structure
for|for
control|(
name|Iterator
name|i
init|=
name|groups
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|groupId
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|StringTokenizer
name|tok
init|=
operator|new
name|StringTokenizer
argument_list|(
name|groupId
argument_list|,
name|GROUP_SEPARATOR
argument_list|)
decl_stmt|;
name|GroupTreeNode
name|node
init|=
name|rootNode
decl_stmt|;
while|while
condition|(
name|tok
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|part
init|=
name|tok
operator|.
name|nextToken
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|getChildren
argument_list|()
operator|.
name|containsKey
argument_list|(
name|part
argument_list|)
condition|)
block|{
name|GroupTreeNode
name|newNode
init|=
operator|new
name|GroupTreeNode
argument_list|(
name|part
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|node
operator|.
name|addChild
argument_list|(
name|newNode
argument_list|)
expr_stmt|;
name|node
operator|=
name|newNode
expr_stmt|;
block|}
else|else
block|{
name|node
operator|=
operator|(
name|GroupTreeNode
operator|)
name|node
operator|.
name|getChildren
argument_list|()
operator|.
name|get
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|groupCacheTime
operator|=
name|lastUpdate
expr_stmt|;
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Loaded groups from cache"
argument_list|)
expr_stmt|;
block|}
return|return
name|rootNode
return|;
block|}
specifier|private
name|List
name|collateGroups
parameter_list|(
name|GroupTreeNode
name|rootNode
parameter_list|)
block|{
name|List
name|groups
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|rootNode
operator|.
name|getChildren
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|GroupTreeNode
name|node
init|=
operator|(
name|GroupTreeNode
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|.
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|node
operator|=
operator|(
name|GroupTreeNode
operator|)
name|node
operator|.
name|getChildren
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|groups
operator|.
name|add
argument_list|(
name|node
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|groups
return|;
block|}
specifier|private
name|RepositoryArtifactIndex
name|getIndex
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|RepositoryIndexException
block|{
name|Configuration
name|configuration
init|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
decl_stmt|;
name|File
name|indexPath
init|=
operator|new
name|File
argument_list|(
name|configuration
operator|.
name|getIndexPath
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createStandardIndex
argument_list|(
name|indexPath
argument_list|)
return|;
block|}
specifier|public
name|List
name|getGroups
parameter_list|()
block|{
return|return
name|groups
return|;
block|}
specifier|public
name|List
name|getArtifactIds
parameter_list|()
block|{
return|return
name|artifactIds
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
name|List
name|getVersions
parameter_list|()
block|{
return|return
name|versions
return|;
block|}
specifier|private
specifier|static
class|class
name|GroupTreeNode
block|{
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
specifier|final
name|String
name|fullName
decl_stmt|;
specifier|private
specifier|final
name|Map
name|children
init|=
operator|new
name|TreeMap
argument_list|()
decl_stmt|;
name|GroupTreeNode
parameter_list|()
block|{
name|name
operator|=
literal|null
expr_stmt|;
name|fullName
operator|=
literal|null
expr_stmt|;
block|}
name|GroupTreeNode
parameter_list|(
name|String
name|name
parameter_list|,
name|GroupTreeNode
name|parent
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|fullName
operator|=
name|parent
operator|.
name|fullName
operator|!=
literal|null
condition|?
name|parent
operator|.
name|fullName
operator|+
name|GROUP_SEPARATOR
operator|+
name|name
else|:
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getFullName
parameter_list|()
block|{
return|return
name|fullName
return|;
block|}
specifier|public
name|Map
name|getChildren
parameter_list|()
block|{
return|return
name|children
return|;
block|}
specifier|public
name|void
name|addChild
parameter_list|(
name|GroupTreeNode
name|newNode
parameter_list|)
block|{
name|children
operator|.
name|put
argument_list|(
name|newNode
operator|.
name|name
argument_list|,
name|newNode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

