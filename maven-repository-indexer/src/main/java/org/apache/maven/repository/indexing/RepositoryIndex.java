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
comment|/**      * Method used to query the index status      *      * @return true if the index is open.      */
name|boolean
name|isOpen
parameter_list|()
function_decl|;
comment|/**      * Method to close open streams to the index directory      */
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
comment|/**      * Method to encapsulate the optimize() method for lucene      */
name|void
name|optimize
parameter_list|()
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Method to retrieve the lucene analyzer object used in creating the document fields for this index      *      * @return lucene Analyzer object used in creating the index fields      */
name|Analyzer
name|getAnalyzer
parameter_list|()
function_decl|;
comment|/**      * Method to retrieve the path where the index is made available      *      * @return the path where the index resides      */
name|String
name|getIndexPath
parameter_list|()
function_decl|;
comment|/**      * Tests an index field if it is a keyword field      *      * @param field the name of the index field to test      * @return true if the index field passed is a keyword, otherwise its false      */
name|boolean
name|isKeywordField
parameter_list|(
name|String
name|field
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

