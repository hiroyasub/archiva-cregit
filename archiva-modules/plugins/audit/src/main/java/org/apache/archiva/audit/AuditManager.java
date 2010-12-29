begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|audit
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
name|repository
operator|.
name|MetadataRepository
import|;
end_import

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
name|repository
operator|.
name|MetadataRepositoryException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_interface
specifier|public
interface|interface
name|AuditManager
block|{
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|getMostRecentAuditEvents
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|void
name|addAuditEvent
parameter_list|(
name|MetadataRepository
name|repository
parameter_list|,
name|AuditEvent
name|event
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|void
name|deleteAuditEvents
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Get all audit events from the given repositories that match a certain range      *      * @param metadataRepository      * @param repositoryIds      the repositories to retrieve events for      * @param startTime          find events only after this time      * @param endTime            find events only before this time      * @return the list of events found      */
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|getAuditEventsInRange
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Get all audit events from the given repositories that match a certain range and resource pattern      *      * @param metadataRepository      * @param repositoryIds      the repositories to retrieve events for      * @param resourcePattern    find all events whose resources start with this string      * @param startTime          find events only after this time      * @param endTime            find events only before this time      * @return the list of events found      */
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|getAuditEventsInRange
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|,
name|String
name|resourcePattern
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
block|}
end_interface

end_unit

