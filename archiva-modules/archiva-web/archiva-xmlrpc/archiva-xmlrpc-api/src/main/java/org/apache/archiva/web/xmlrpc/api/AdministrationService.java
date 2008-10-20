begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|xmlrpc
operator|.
name|api
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|beans
operator|.
name|RemoteRepository
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|xmlrpc
operator|.
name|ServiceObject
import|;
end_import

begin_interface
annotation|@
name|ServiceObject
argument_list|(
literal|"AdministrationService"
argument_list|)
specifier|public
interface|interface
name|AdministrationService
block|{
comment|/**      * Executes repository scanner on the given repository.      *        * @param repoId id of the repository to be scanned      * @return      * @throws Exception      */
specifier|public
name|Boolean
name|executeRepositoryScanner
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Executes the database scanner.      *       * @return      * @throws Exception      */
specifier|public
name|Boolean
name|executeDatabaseScanner
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets all available database consumers.      * @return      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAllDatabaseConsumers
parameter_list|()
function_decl|;
comment|/**      * Configures (enable or disable) database consumer.      *       * @param consumerId id of the database consumer      * @param enable flag whether to enable or disable the specified consumer      * @return      * @throws Exception      */
specifier|public
name|Boolean
name|configureDatabaseConsumer
parameter_list|(
name|String
name|consumerId
parameter_list|,
name|boolean
name|enable
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets all available repository consumers.      *       * @return      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAllRepositoryConsumers
parameter_list|()
function_decl|;
comment|// TODO should we already implement config of consumers per repository?
comment|/**      * Configures (enable or disable) repository consumer.      *       * @param repoId      * @param consumerId      * @param enable      * @return      * @throws Exception      */
specifier|public
name|Boolean
name|configureRepositoryConsumer
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|consumerId
parameter_list|,
name|boolean
name|enable
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets all managed repositories.      *       * @return      */
specifier|public
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getAllManagedRepositories
parameter_list|()
function_decl|;
comment|/**      * Gets all remote repositories.      *       * @return      */
specifier|public
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|getAllRemoteRepositories
parameter_list|()
function_decl|;
comment|/**      * Deletes given artifact from the specified repository.      *       * @param repoId id of the repository where the artifact to be deleted resides      * @param groupId groupId of the artifact to be deleted      * @param artifactId artifactId of the artifact to be deleted      * @param version version of the artifact to be deleted      * @return      * @throws Exception      */
specifier|public
name|Boolean
name|deleteArtifact
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|//TODO
comment|// consider the following as additional services:
comment|// - getAllConfiguredRepositoryConsumers( String repoId ) - list all enabled consumers for the repo
comment|// - getAllConfiguredDatabaseConsumers() - list all enabled db consumers
block|}
end_interface

end_unit

