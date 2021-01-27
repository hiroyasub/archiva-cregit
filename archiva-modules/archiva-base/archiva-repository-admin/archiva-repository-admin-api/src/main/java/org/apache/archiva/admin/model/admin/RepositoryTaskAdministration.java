begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|admin
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|RepositoryTaskInfo
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ScanStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|StopWatch
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
comment|/**  * Interface for managing repository scan tasks.  *  * @author Martin Stockhammer<martin_s@apache.org>  * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryTaskAdministration
block|{
comment|/**      * Schedules a full repository scan for the given repository. Metadata and Index are updated.      * All files are scanned, even if they were not modified since the last scan.      *      * @param repositoryId the repository identifier      * @throws RepositoryAdminException if it was not possible to schedule the scan      */
name|void
name|scheduleFullScan
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Schedules a scan that rebuilds the fulltext index of the repository.      *      * @param repositoryId the repository identifier      * @throws RepositoryAdminException if it was not possible to schedule the index scan      */
name|void
name|scheduleIndexFullScan
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Schedules a scan that rebuilds the fulltext index of the repository.      *      * @param repositoryId the repository identifier      * @param relativePath the path to the file to add to the index      * @throws RepositoryAdminException if it was not possible to schedule the index scan      */
name|void
name|scheduleIndexScan
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|relativePath
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Schedules a scan that rebuilds metadata of the repository      *      * @param repositoryId the repository identifier      * @throws RepositoryAdminException if it was not possible to schedule the index scan      */
name|void
name|scheduleMetadataFullScan
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Schedules a scan that rebuilds metadata of the repository but only for updated files.      *      * @param repositoryId the repository identifier      * @throws RepositoryAdminException if it was not possible to schedule the index scan      */
name|void
name|scheduleMetadataUpdateScan
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Returns information about currently running scans for the given repository.      *      * @return the status information      * @param repositoryId the repository identifier      * @throws RepositoryAdminException if there was an error retrieving the scan status      */
name|ScanStatus
name|getCurrentScanStatus
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Returns information about currently running scans for all repositories.      *      * @return the status information      * @throws RepositoryAdminException if there was an error retrieving the scan status      */
name|ScanStatus
name|getCurrentScanStatus
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Cancels the tasks either running or queued for the given repository.      * @param repositoryId the repository identifier      * @return a list of canceled tasks.      */
name|List
argument_list|<
name|RepositoryTaskInfo
argument_list|>
name|cancelTasks
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Cancels the metadata scan tasks either running or queued for the given repository.      * @param repositoryId the repository identifier      * @return a list of canceled tasks.      */
name|List
argument_list|<
name|RepositoryTaskInfo
argument_list|>
name|cancelScanTasks
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * Cancels the indexing tasks either running or queued for the given repository.      * @param repositoryId the repository identifier      * @return a list of canceled tasks.      */
name|List
argument_list|<
name|RepositoryTaskInfo
argument_list|>
name|cancelIndexTasks
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
block|}
end_interface

end_unit

