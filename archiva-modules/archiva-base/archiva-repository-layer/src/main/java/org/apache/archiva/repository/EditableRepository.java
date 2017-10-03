begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * This is the editable part of a repository.  * Normally a repository should also implement this interface but it is not  * required.  *  * Capabilities and features are a integral part of the implementation and not  * provided here by the interface.  * Feature setting methods are provided by the features itself.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|EditableRepository
extends|extends
name|Repository
block|{
comment|/**      * Returns the primary locale used for setting the default values for      * name and description.      *      * @return The locale used for name and description when they are not set      */
name|Locale
name|getPrimaryLocale
parameter_list|()
function_decl|;
comment|/**      * Sets the name for the given locale      *      * @param locale the locale for which the name is set      * @param name The name value in the language that matches the locale      */
name|void
name|setName
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Sets the description for the given locale      *      * @param locale the locale for which the description is set      * @param description The description in the language that matches the locale.      */
name|void
name|setDescription
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|String
name|description
parameter_list|)
function_decl|;
comment|/**      * Sets the location of the repository. May be a URI that is suitable for the      * repository implementation. Not all implementations will accept the same URI schemes.      * @param location the location URI      * @throws UnsupportedURIException if the URI scheme is not supported by the repository type.      */
name|void
name|setLocation
parameter_list|(
name|URI
name|location
parameter_list|)
throws|throws
name|UnsupportedURIException
function_decl|;
comment|/**      * Adds a failover location for the repository.      *      * @param location The location that should be used as failover.      * @throws UnsupportedURIException if the URI scheme is not supported by the repository type.      */
name|void
name|addFailoverLocation
parameter_list|(
name|URI
name|location
parameter_list|)
throws|throws
name|UnsupportedURIException
function_decl|;
comment|/**      * Removes a failover location from the set.      *      * @param location the location uri to remove      */
name|void
name|removeFailoverLocation
parameter_list|(
name|URI
name|location
parameter_list|)
function_decl|;
comment|/**      * Clears the failover location set.      */
name|void
name|clearFailoverLocations
parameter_list|()
function_decl|;
comment|/**      * Sets the flag for scanning the repository. If true, the repository will be scanned.      * You have to set the scheduling times, if you set this to true.      *      * @param scanned if true, the repository is scanned regulary.      */
name|void
name|setScanned
parameter_list|(
name|boolean
name|scanned
parameter_list|)
function_decl|;
comment|/**      * Adds a scheduling time at the given index.      * @param index the index where time should be set      * @param scheduleDefinition the scheduling definition to add      */
name|void
name|addSchedulingTime
parameter_list|(
name|int
name|index
parameter_list|,
name|ScheduleDefinition
name|scheduleDefinition
parameter_list|)
function_decl|;
comment|/**      * Adds a scheduling time to the end of the list.      * @param scheduleDefinition the scheduling definition to set      */
name|void
name|addSchedulingTime
parameter_list|(
name|ScheduleDefinition
name|scheduleDefinition
parameter_list|)
function_decl|;
comment|/**      * Removes the scheduling time definition from the list.      * @param scheduleDefinition the scheduling definition to remove.      */
name|void
name|removeSchedulingTime
parameter_list|(
name|ScheduleDefinition
name|scheduleDefinition
parameter_list|)
function_decl|;
comment|/**      * Removes the scheduling time definition add the given index.      *      * @param index the index to remove      */
name|void
name|removeSchedulingTime
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
comment|/**      * Clears the list of scheduling definitions.      */
name|void
name|clearSchedulingTimes
parameter_list|()
function_decl|;
comment|/**      * Set true, if the repository has indexes stored.      * @param hasIndex true, if the repository has indexes, otherwise false.      */
name|void
name|setIndex
parameter_list|(
name|boolean
name|hasIndex
parameter_list|)
function_decl|;
comment|/**      * Sets the path to the index directory. May be a relative or absolute URI.      * @param indexPath the path      */
name|void
name|setIndexPath
parameter_list|(
name|URI
name|indexPath
parameter_list|)
function_decl|;
comment|/**      * Sets the layout string.      * @param layout      */
name|void
name|setLayout
parameter_list|(
name|String
name|layout
parameter_list|)
function_decl|;
comment|/**      * Adds an active release scheme. Release schemes may be combined.      * @param scheme the scheme to add.      */
name|void
name|addActiveReleaseScheme
parameter_list|(
name|ReleaseScheme
name|scheme
parameter_list|)
function_decl|;
comment|/**      * Removes an active release scheme from the set.      * @param scheme the scheme to remove.      */
name|void
name|removeActiveReleaseScheme
parameter_list|(
name|ReleaseScheme
name|scheme
parameter_list|)
function_decl|;
comment|/**      * Clears all active release schemes.      */
name|void
name|clearActiveReleaseSchemes
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

