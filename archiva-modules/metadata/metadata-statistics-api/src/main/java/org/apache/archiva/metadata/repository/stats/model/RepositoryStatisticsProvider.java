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
name|RepositorySession
import|;
end_import

begin_comment
comment|/**  *  * This interface is used for populating statistics data. It should be implemented  * by metadata store implementations in the MetadataRepository class, if the store  * implementation can provide a faster implementation than walking the tree.  *  * @author Martin Stockhammer  * @since 2.3  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryStatisticsProvider
block|{
comment|/**      * Populate the statistics object with the statistics data of this repository.      *      *      * @param repositorySession      * @param repository The current metadata repository implementation      * @param repositoryId The repository Id      * @param statistics The statistics object that should be filled.      * @throws MetadataRepositoryException Is thrown, if an error occurs while accessing the repository      */
name|void
name|populateStatistics
parameter_list|(
name|RepositorySession
name|repositorySession
parameter_list|,
name|MetadataRepository
name|repository
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|RepositoryStatistics
name|statistics
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
block|}
end_interface

end_unit

