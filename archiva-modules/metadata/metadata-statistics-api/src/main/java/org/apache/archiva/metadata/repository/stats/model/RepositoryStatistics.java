begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|stats
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|MetadataFacet
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  *  * Provides statistics data of metadata repositories.  *  * @since 2.3  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryStatistics
extends|extends
name|MetadataFacet
block|{
name|String
name|FACET_ID
init|=
literal|"org.apache.archiva.metadata.repository.stats"
decl_stmt|;
name|String
name|getRepositoryId
parameter_list|( )
function_decl|;
name|Date
name|getScanEndTime
parameter_list|( )
function_decl|;
name|Date
name|getScanStartTime
parameter_list|( )
function_decl|;
name|long
name|getTotalArtifactCount
parameter_list|( )
function_decl|;
name|void
name|setTotalArtifactCount
parameter_list|(
name|long
name|totalArtifactCount
parameter_list|)
function_decl|;
name|long
name|getTotalArtifactFileSize
parameter_list|( )
function_decl|;
name|void
name|setTotalArtifactFileSize
parameter_list|(
name|long
name|totalArtifactFileSize
parameter_list|)
function_decl|;
name|long
name|getTotalFileCount
parameter_list|( )
function_decl|;
name|void
name|setTotalFileCount
parameter_list|(
name|long
name|totalFileCount
parameter_list|)
function_decl|;
name|long
name|getTotalGroupCount
parameter_list|( )
function_decl|;
name|void
name|setTotalGroupCount
parameter_list|(
name|long
name|totalGroupCount
parameter_list|)
function_decl|;
name|long
name|getTotalProjectCount
parameter_list|( )
function_decl|;
name|void
name|setTotalProjectCount
parameter_list|(
name|long
name|totalProjectCount
parameter_list|)
function_decl|;
name|void
name|setNewFileCount
parameter_list|(
name|long
name|newFileCount
parameter_list|)
function_decl|;
name|long
name|getNewFileCount
parameter_list|( )
function_decl|;
name|long
name|getDuration
parameter_list|( )
function_decl|;
comment|/**      * Statistics data by artifact type.      *      * @return A list of data keys and values      */
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|getTotalCountForType
parameter_list|( )
function_decl|;
comment|/**      * Returns the value for the given artifact type.      *      * @param type The artifact type      * @return The count value.      */
name|long
name|getTotalCountForType
parameter_list|(
name|String
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets the value for the given artifact type.      * @param type The artifact type.      * @param count The count value.      */
name|void
name|setTotalCountForType
parameter_list|(
name|String
name|type
parameter_list|,
name|long
name|count
parameter_list|)
function_decl|;
comment|/**      * Reads custom statistic values that are store implementation      * specific.      *      * @param fieldName A unique field name.      */
name|long
name|getCustomValue
parameter_list|(
name|String
name|fieldName
parameter_list|)
function_decl|;
comment|/**      * Saves custom statistic values that are store implementation      * specific. The field name should be unique (e.g. prefixed by the      * package name of the data provider).      *      * @param fieldName A unique field name.      * @param count The statistic counter value      */
name|void
name|setCustomValue
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|long
name|count
parameter_list|)
function_decl|;
comment|/**      * Returns the list of field names of custom values stored in this instance.      * @return the list of stored custom values      */
name|List
argument_list|<
name|String
argument_list|>
name|getAvailableCustomValues
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

