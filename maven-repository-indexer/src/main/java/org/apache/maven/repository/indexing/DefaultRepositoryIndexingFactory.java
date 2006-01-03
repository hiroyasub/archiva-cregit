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
name|indexing
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0    *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|digest
operator|.
name|Digester
import|;
end_import

begin_comment
comment|/**  *  * @author Edwin Punzalan  * @plexus.component role="org.apache.maven.repository.indexing.RepositoryIndexingFactory"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRepositoryIndexingFactory
implements|implements
name|RepositoryIndexingFactory
block|{
comment|/** @plexus.requirement */
specifier|private
name|Digester
name|digester
decl_stmt|;
specifier|public
name|ArtifactRepositoryIndexSearcher
name|createArtifactRepositoryIndexSearcher
parameter_list|(
name|ArtifactRepositoryIndex
name|index
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ArtifactRepositoryIndex
name|createArtifactRepositoryIndex
parameter_list|(
name|String
name|indexPath
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
return|return
operator|new
name|ArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|,
name|digester
argument_list|)
return|;
block|}
block|}
end_class

end_unit

