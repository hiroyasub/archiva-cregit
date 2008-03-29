begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|search
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
comment|/**  * Search across repositories in lucene indexes.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @todo add security to not perform search in repositories you don't have access to.  */
end_comment

begin_interface
specifier|public
interface|interface
name|CrossRepositorySearch
block|{
comment|/**      * Search for the specific term across all repositories.      *       * @param term the term to search for.      * @param limits the limits to apply to the search results.      * @return the results.      */
specifier|public
name|SearchResults
name|searchForTerm
parameter_list|(
name|String
name|principal
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
parameter_list|,
name|String
name|term
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
function_decl|;
comment|/**      * Search for the specific bytecode across all repositories.      *       * @param term the term to search for.      * @param limits the limits to apply to the search results.      * @return the results.      */
specifier|public
name|SearchResults
name|searchForBytecode
parameter_list|(
name|String
name|principal
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
parameter_list|,
name|String
name|term
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
function_decl|;
comment|/**      * Search for the specific checksum string across all repositories.      *       * @param checksum the checksum string to search for.      * @param limits the limits to apply to the search results.      * @return the results.      */
specifier|public
name|SearchResults
name|searchForChecksum
parameter_list|(
name|String
name|principal
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
parameter_list|,
name|String
name|checksum
parameter_list|,
name|SearchResultLimits
name|limits
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

