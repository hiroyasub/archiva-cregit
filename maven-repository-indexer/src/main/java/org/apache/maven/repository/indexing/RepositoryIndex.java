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
name|lucene
operator|.
name|analysis
operator|.
name|Analyzer
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

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryIndex
block|{
name|String
name|ROLE
init|=
name|RepositoryIndex
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
index|[]
name|getIndexFields
parameter_list|()
function_decl|;
name|boolean
name|isOpen
parameter_list|()
function_decl|;
name|void
name|index
parameter_list|(
name|Object
name|obj
parameter_list|)
throws|throws
name|RepositoryIndexException
function_decl|;
name|void
name|close
parameter_list|()
throws|throws
name|RepositoryIndexException
function_decl|;
name|ArtifactRepository
name|getRepository
parameter_list|()
function_decl|;
name|void
name|optimize
parameter_list|()
throws|throws
name|RepositoryIndexException
function_decl|;
name|Analyzer
name|getAnalyzer
parameter_list|()
function_decl|;
name|String
name|getIndexPath
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

