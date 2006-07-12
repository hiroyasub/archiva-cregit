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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepositoryFactory
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
name|layout
operator|.
name|ArtifactRepositoryLayout
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
comment|/**  * Searches for searchString in all indexed fields.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="quickSearchAction" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|QuickSearchAction
extends|extends
name|ActionSupport
block|{
comment|/**      * Query string.      */
specifier|private
name|String
name|q
decl_stmt|;
comment|/**      * Search results.      */
specifier|private
name|List
name|searchResult
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
name|ArtifactRepositoryFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout"      */
specifier|private
name|Map
name|repositoryLayouts
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
specifier|public
name|String
name|execute
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
comment|// TODO: return zero results if index doesn't yet exist
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
name|getDefaultRepository
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|ArtifactRepositoryIndex
name|index
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|searchResult
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
specifier|private
name|ArtifactRepository
name|getDefaultRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|MalformedURLException
block|{
comment|// TODO: [!] repository should only have been instantiated once
name|File
name|repositoryDirectory
init|=
operator|new
name|File
argument_list|(
name|configuration
operator|.
name|getRepositoryDirectory
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|repoDir
init|=
name|repositoryDirectory
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ArtifactRepositoryLayout
name|layout
init|=
operator|(
name|ArtifactRepositoryLayout
operator|)
name|repositoryLayouts
operator|.
name|get
argument_list|(
name|configuration
operator|.
name|getRepositoryLayout
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|repositoryFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"test"
argument_list|,
name|repoDir
argument_list|,
name|layout
argument_list|,
literal|null
argument_list|,
literal|null
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
name|List
name|getSearchResult
parameter_list|()
block|{
return|return
name|searchResult
return|;
block|}
block|}
end_class

end_unit

