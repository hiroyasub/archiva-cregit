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
name|discovery
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|util
operator|.
name|Iterator
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
comment|/**  * Interface for discovering metadata files.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|MetadataDiscoverer
block|{
name|String
name|ROLE
init|=
name|MetadataDiscoverer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/** 	 * Search for metadata files in the repository. 	 *  	 * @param repositoryBase 	 *            The repository directory. 	 * @param blacklistedPatterns 	 *            Patterns that are to be excluded from the discovery process. 	 * @return 	 */
name|List
name|discoverMetadata
parameter_list|(
name|File
name|repositoryBase
parameter_list|,
name|String
name|blacklistedPatterns
parameter_list|)
function_decl|;
comment|/** 	 * Get the list of paths kicked out during the discovery process. 	 *  	 * @return the paths as Strings. 	 */
name|Iterator
name|getKickedOutPathsIterator
parameter_list|()
function_decl|;
comment|/** 	 * Get the list of paths excluded during the discovery process. 	 *  	 * @return the paths as Strings. 	 */
name|Iterator
name|getExcludedPathsIterator
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

