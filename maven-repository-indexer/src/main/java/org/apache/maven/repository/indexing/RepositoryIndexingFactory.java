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
comment|/**      * Method to create an instance of the ArtifactRepositoryIndex      *      * @param indexPath  the path where the index will be created/updated      * @param repository the repository where the indexed artifacts are located      * @return the ArtifactRepositoryIndex instance      * @throws RepositoryIndexException      */
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
comment|/**      * Method to create an instance of the PomRepositoryIndex      *      * @param indexPath  the path where the index will be created/updated      * @param repository the repository where the indexed poms are located      * @return the PomRepositoryIndex instance      * @throws RepositoryIndexException      */
name|PomRepositoryIndex
name|createPomRepositoryIndex
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
comment|/**      * Method to create instance of the MetadataRepositoryIndex      *      * @param indexPath  the path where the index will be created/updated      * @param repository the repository where the indexed metadata are located      * @return the MetadataRepositoryIndex instance      * @throws RepositoryIndexException      */
name|MetadataRepositoryIndex
name|createMetadataRepositoryIndex
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

