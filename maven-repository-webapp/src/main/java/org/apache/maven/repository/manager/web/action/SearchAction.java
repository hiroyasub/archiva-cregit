begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|manager
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
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|ActionSupport
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
name|repository
operator|.
name|ArtifactRepository
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
name|repository
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
name|repository
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
name|repository
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
name|repository
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
name|repository
operator|.
name|indexing
operator|.
name|ArtifactRepositoryIndex
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
name|repository
operator|.
name|indexing
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndexSearchException
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndexSearchLayer
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndexingFactory
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
name|repository
operator|.
name|indexing
operator|.
name|query
operator|.
name|SinglePhraseQuery
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
name|net
operator|.
name|MalformedURLException
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

begin_comment
comment|/**  * Search all indexed fields by the given criteria.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="searchAction"  */
end_comment

begin_class
specifier|public
class|class
name|SearchAction
extends|extends
name|ActionSupport
block|{
comment|/**      * Query string.      */
specifier|private
name|String
name|q
decl_stmt|;
comment|/**      * The MD5 to search by.      */
specifier|private
name|String
name|md5
decl_stmt|;
comment|/**      * Search results.      */
specifier|private
name|List
name|searchResults
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryIndexingFactory
name|factory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryIndexSearchLayer
name|searchLayer
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
specifier|public
name|String
name|quickSearch
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|RepositoryIndexException
throws|,
name|RepositoryIndexSearchException
throws|,
name|ConfigurationStoreException
block|{
comment|// TODO: give action message if indexing is in progress
assert|assert
name|q
operator|!=
literal|null
operator|&&
name|q
operator|.
name|length
argument_list|()
operator|!=
literal|0
assert|;
name|ArtifactRepositoryIndex
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
name|indexExists
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
name|searchResults
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
name|q
argument_list|,
name|index
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|findArtifact
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|RepositoryIndexException
throws|,
name|RepositoryIndexSearchException
block|{
comment|// TODO: give action message if indexing is in progress
assert|assert
name|md5
operator|!=
literal|null
operator|&&
name|md5
operator|.
name|length
argument_list|()
operator|!=
literal|0
assert|;
name|ArtifactRepositoryIndex
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
name|indexExists
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
name|searchResults
operator|=
name|searchLayer
operator|.
name|searchAdvanced
argument_list|(
operator|new
name|SinglePhraseQuery
argument_list|(
literal|"md5"
argument_list|,
name|md5
argument_list|)
argument_list|,
name|index
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|ArtifactRepositoryIndex
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
name|ArtifactRepository
name|repository
init|=
name|repositoryFactory
operator|.
name|createRepository
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
return|;
block|}
specifier|public
name|String
name|doInput
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|getQ
parameter_list|()
block|{
return|return
name|q
return|;
block|}
specifier|public
name|void
name|setQ
parameter_list|(
name|String
name|q
parameter_list|)
block|{
name|this
operator|.
name|q
operator|=
name|q
expr_stmt|;
block|}
specifier|public
name|String
name|getMd5
parameter_list|()
block|{
return|return
name|md5
return|;
block|}
specifier|public
name|void
name|setMd5
parameter_list|(
name|String
name|md5
parameter_list|)
block|{
name|this
operator|.
name|md5
operator|=
name|md5
expr_stmt|;
block|}
specifier|public
name|List
name|getSearchResults
parameter_list|()
block|{
return|return
name|searchResults
return|;
block|}
block|}
end_class

end_unit

