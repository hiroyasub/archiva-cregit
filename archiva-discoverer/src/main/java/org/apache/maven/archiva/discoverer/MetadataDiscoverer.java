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
name|discoverer
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
name|archiva
operator|.
name|discoverer
operator|.
name|filter
operator|.
name|MetadataFilter
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Interface for discovering metadata files.  */
end_comment

begin_interface
specifier|public
interface|interface
name|MetadataDiscoverer
extends|extends
name|Discoverer
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
comment|/**      * Search for metadata files in the repository.      *      * @param repository          The repository.      * @param blacklistedPatterns Patterns that are to be excluded from the discovery process.      * @param metadataFilter      filter to use on the discovered metadata before returning      * @return the list of artifacts found      * @throws DiscovererException if there is a problem during the discovery process      */
name|List
name|discoverMetadata
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|List
name|blacklistedPatterns
parameter_list|,
name|MetadataFilter
name|metadataFilter
parameter_list|)
throws|throws
name|DiscovererException
function_decl|;
comment|/**      * Search for metadata files in the repository.      *      * @param repository          The repository.      * @param blacklistedPatterns Patterns that are to be excluded from the discovery process.      * @return the list of artifacts found      * @throws DiscovererException if there is a problem during the discovery process      */
name|List
name|discoverMetadata
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|List
name|blacklistedPatterns
parameter_list|)
throws|throws
name|DiscovererException
function_decl|;
block|}
end_interface

end_unit

