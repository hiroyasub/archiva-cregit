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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_interface
specifier|public
interface|interface
name|Discoverer
block|{
comment|/**      * Get the list of paths kicked out during the discovery process.      *      * @return the paths as Strings.      */
name|Iterator
name|getKickedOutPathsIterator
parameter_list|()
function_decl|;
comment|/**      * Get the list of paths excluded during the discovery process.      *      * @return the paths as Strings.      */
name|Iterator
name|getExcludedPathsIterator
parameter_list|()
function_decl|;
comment|/**      * Reset the time in the repository that indicates the last time a check was performed.      *      * @param repository the location of the repository      * @param operation  the operation to record the timestamp for      * @throws java.io.IOException if there is a non-recoverable problem reading or writing the metadata      */
name|void
name|resetLastCheckedTime
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|operation
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Set the time in the repository that indicates the last time a check was performed.      *      * @param repository the location of the repository      * @param operation  the operation to record the timestamp for      * @param date       the date to set the last check to      * @throws java.io.IOException if there is a non-recoverable problem reading or writing the metadata      */
name|void
name|setLastCheckedTime
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|operation
parameter_list|,
name|Date
name|date
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

