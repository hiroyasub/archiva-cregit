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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @author Maria Odea Ching  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositorySearcher
block|{
name|String
name|ROLE
init|=
name|RepositoryIndexer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/** 	 * Search the artifact that contains the query string in the specified 	 * search field. 	 *  	 * @param queryString 	 * @param searchField 	 * @return 	 */
specifier|public
name|List
name|searchArtifact
parameter_list|(
name|String
name|queryString
parameter_list|,
name|String
name|searchField
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

