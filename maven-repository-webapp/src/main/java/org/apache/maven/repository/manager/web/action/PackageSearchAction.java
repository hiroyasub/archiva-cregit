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
name|Action
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
name|DefaultRepositoryIndexSearcher
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
name|query
operator|.
name|SinglePhraseQuery
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
name|manager
operator|.
name|web
operator|.
name|job
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
name|indexing
operator|.
name|RepositoryIndex
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
comment|/**  * TODO: Description.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="org.apache.maven.repository.manager.web.action.PackageSearchAction"  */
end_comment

begin_class
specifier|public
class|class
name|PackageSearchAction
implements|implements
name|Action
block|{
specifier|private
name|String
name|packageName
decl_stmt|;
specifier|private
name|String
name|md5
decl_stmt|;
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
name|ArtifactRepositoryFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|Configuration
name|configuration
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
block|{
name|String
name|searchTerm
decl_stmt|;
name|String
name|key
decl_stmt|;
if|if
condition|(
name|packageName
operator|!=
literal|null
operator|&&
name|packageName
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|searchTerm
operator|=
name|packageName
expr_stmt|;
name|key
operator|=
name|RepositoryIndex
operator|.
name|FLD_PACKAGES
expr_stmt|;
block|}
if|else if
condition|(
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
condition|)
block|{
name|searchTerm
operator|=
name|md5
expr_stmt|;
name|key
operator|=
literal|"md5"
expr_stmt|;
block|}
else|else
block|{
return|return
name|ERROR
return|;
block|}
comment|// TODO: better config
name|String
name|indexPath
init|=
name|configuration
operator|.
name|getIndexDirectory
argument_list|()
decl_stmt|;
comment|// TODO: reduce the amount of lookup?
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
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ArtifactRepository
name|repository
init|=
name|repositoryFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"repository"
argument_list|,
name|repoDir
argument_list|,
name|configuration
operator|.
name|getLayout
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
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
name|RepositoryIndexSearchLayer
name|searchLayer
init|=
name|factory
operator|.
name|createRepositoryIndexSearchLayer
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|searchResult
operator|=
name|searchLayer
operator|.
name|searchAdvanced
argument_list|(
operator|new
name|SinglePhraseQuery
argument_list|(
name|key
argument_list|,
name|searchTerm
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|void
name|setPackageName
parameter_list|(
name|String
name|packageName
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|packageName
expr_stmt|;
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

