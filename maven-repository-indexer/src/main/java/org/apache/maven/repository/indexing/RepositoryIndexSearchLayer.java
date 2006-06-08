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
name|repository
operator|.
name|indexing
operator|.
name|query
operator|.
name|Query
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
comment|/**  * Repository search layer.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryIndexSearchLayer
block|{
comment|/**      * The Plexus component role name.      */
name|String
name|ROLE
init|=
name|RepositoryIndexSearchLayer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Method for searching the keyword in all the fields in the index. "Query everything" search.      * The index fields will be retrieved and query objects will be constructed using the      * optional (OR) CompoundQuery.      *      * @param keyword      * @param index      * @return      * @throws RepositoryIndexSearchException      *      */
name|List
name|searchGeneral
parameter_list|(
name|String
name|keyword
parameter_list|,
name|RepositoryIndex
name|index
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
function_decl|;
comment|/**      * Method for "advanced search" of the index      *      * @param qry   the query object that will be used for searching the index      * @param index      * @return      * @throws RepositoryIndexSearchException      *      */
name|List
name|searchAdvanced
parameter_list|(
name|Query
name|qry
parameter_list|,
name|RepositoryIndex
name|index
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
function_decl|;
block|}
end_interface

end_unit

