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
name|security
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
name|Arrays
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
comment|/**  * ServiceMethodsPermissionsMapping  *  * Used by the XmlRpcAuthenticationHandler to check the permissions specific to the requested service method.  * New methods in exposed services must be registered in the appropriate operation below.  *  * @version $Id: ServiceMethodsPermissionsMapping.java  */
end_comment

begin_class
specifier|public
class|class
name|ServiceMethodsPermissionsMapping
block|{
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|SERVICE_METHODS_FOR_OPERATION_MANAGE_CONFIGURATION
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"AdministrationService.configureRepositoryConsumer"
argument_list|,
literal|"AdministrationService.configureDatabaseConsumer"
argument_list|,
literal|"AdministrationService.executeDatabaseScanner"
argument_list|,
literal|"AdministrationService.getAllManagedRepositories"
argument_list|,
literal|"AdministrationService.getAllRemoteRepositories"
argument_list|,
literal|"AdministrationService.getAllDatabaseConsumers"
argument_list|,
literal|"AdministrationService.getAllRepositoryConsumers"
argument_list|,
literal|"AdministrationService.deleteArtifact"
argument_list|,
literal|"AdministrationService.addManagedRepository"
argument_list|,
literal|"AdministrationService.deleteManagedRepository"
argument_list|,
literal|"AdministrationService.getManagedRepository"
argument_list|,
literal|"AdministrationService.merge"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|SERVICE_METHODS_FOR_OPERATION_RUN_INDEXER
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"AdministrationService.executeRepositoryScanner"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|SERVICE_METHODS_FOR_OPERATION_REPOSITORY_ACCESS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"SearchService.quickSearch"
argument_list|,
literal|"SearchService.getArtifactByChecksum"
argument_list|,
literal|"SearchService.getArtifactVersions"
argument_list|,
literal|"SearchService.getArtifactVersionsByDate"
argument_list|,
literal|"SearchService.getDependencies"
argument_list|,
literal|"SearchService.getDependencyTree"
argument_list|,
literal|"SearchService.getDependees"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PING
init|=
literal|"PingService.ping"
decl_stmt|;
block|}
end_class

end_unit

