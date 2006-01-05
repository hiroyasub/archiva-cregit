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

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryIndexingFactory
block|{
name|String
name|ROLE
init|=
name|RepositoryIndexingFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ArtifactRepositoryIndexSearcher
name|createArtifactRepositoryIndexSearcher
parameter_list|(
name|ArtifactRepositoryIndex
name|index
parameter_list|)
function_decl|;
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
function_decl|;
block|}
end_interface

end_unit

