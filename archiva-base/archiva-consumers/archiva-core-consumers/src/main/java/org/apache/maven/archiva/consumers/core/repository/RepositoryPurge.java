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
name|consumers
operator|.
name|core
operator|.
name|repository
package|;
end_package

begin_comment
comment|/* * Licensed to the Apache Software Foundation (ASF) under one * or more contributor license agreements.  See the NOTICE file * distributed with this work for additional information * regarding copyright ownership.  The ASF licenses this file * to you under the Apache License, Version 2.0 (the * "License"); you may not use this file except in compliance * with the License.  You may obtain a copy of the License at * *  http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, * software distributed under the License is distributed on an * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY * KIND, either express or implied.  See the License for the * specific language governing permissions and limitations * under the License. */
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
name|configuration
operator|.
name|Configuration
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
name|archiva
operator|.
name|model
operator|.
name|ArchivaRepository
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
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|BidirectionalRepositoryLayout
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
name|archiva
operator|.
name|database
operator|.
name|ArchivaDAO
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
name|archiva
operator|.
name|database
operator|.
name|ArtifactDAO
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
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryContentIndex
import|;
end_import

begin_comment
comment|/**  *  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryPurge
block|{
comment|/**      * Perform checking on artifact for repository purge      *      * @param path          path to the scanned artifact      * @param configuration the configuration for the repository currently being scanned      */
specifier|public
name|void
name|process
parameter_list|(
name|String
name|path
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryPurgeException
function_decl|;
comment|/**      * Set the repository to be purged      *      * @param repository      */
specifier|public
name|void
name|setRepository
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Set the layout of the repository to be purged      *      * @param layout      */
specifier|public
name|void
name|setLayout
parameter_list|(
name|BidirectionalRepositoryLayout
name|layout
parameter_list|)
function_decl|;
comment|/**      * Set the index of the repository       *      * @param index      */
specifier|public
name|void
name|setIndex
parameter_list|(
name|RepositoryContentIndex
name|index
parameter_list|)
function_decl|;
comment|/**      * Set the artifact dao used for updating the database of the changes in the repo      *      * @param artifactDao      */
specifier|public
name|void
name|setArtifactDao
parameter_list|(
name|ArtifactDAO
name|artifactDao
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

