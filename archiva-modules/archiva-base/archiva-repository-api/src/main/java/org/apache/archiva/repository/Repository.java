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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|ArchivaIndexingContext
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
name|repository
operator|.
name|events
operator|.
name|RepositoryEventSource
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
name|repository
operator|.
name|storage
operator|.
name|RepositoryStorage
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
name|repository
operator|.
name|features
operator|.
name|RepositoryFeature
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
name|repository
operator|.
name|storage
operator|.
name|StorageAsset
import|;
end_import

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  *  * Base interface for repositories.  *  * Created by Martin Stockhammer on 21.09.17.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Repository
extends|extends
name|RepositoryEventSource
extends|,
name|RepositoryStorage
block|{
comment|/**      * Return the identifier of the repository. Repository identifier should be unique at least      * for the same type.      * @return The identifier.      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * This is the display name of the repository. This string is presented in the user interface.      *      * @return The display name of the repository      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns the name in the given locale.      * @param locale      * @return      */
name|String
name|getName
parameter_list|(
name|Locale
name|locale
parameter_list|)
function_decl|;
comment|/**      * Returns a description of this repository.      * @return The description.      */
name|String
name|getDescription
parameter_list|()
function_decl|;
comment|/**      * Returns the description for the given locale.      * @param locale      * @return      */
name|String
name|getDescription
parameter_list|(
name|Locale
name|locale
parameter_list|)
function_decl|;
comment|/**      * This identifies the type of repository. Archiva does only support certain types of repositories.      *      * @return A unique identifier for the repository type.      */
name|RepositoryType
name|getType
parameter_list|()
function_decl|;
comment|/**      * Returns the location of this repository. For local repositories this might be a file URI, for      * remote repositories a http URL or some very repository specific schemes.      * Each repository has only one unique location.      *      * @return The repository location.      */
name|URI
name|getLocation
parameter_list|()
function_decl|;
comment|/**      * Returns a storage representation to the local data stored for this repository.      * The repository implementation may not store the real artifacts in this path. The directory structure      * is completely implementation dependant.      *      */
name|StorageAsset
name|getLocalPath
parameter_list|()
function_decl|;
comment|/**      * A repository may allow additional locations that can be used, if the primary location is not available.      * @return      */
name|Set
argument_list|<
name|URI
argument_list|>
name|getFailoverLocations
parameter_list|()
function_decl|;
comment|/**      * True, if this repository is scanned regularly.      */
name|boolean
name|isScanned
parameter_list|()
function_decl|;
comment|/**      * Returns the definition, when the repository jobs are executed.      * This must return a valid a cron string.      *      * @See http://www.quartz-scheduler.org/api/2.2.1/org/quartz/CronExpression.html      *      * @return      */
name|String
name|getSchedulingDefinition
parameter_list|()
function_decl|;
comment|/**      * Returns true, if this repository has a index available      * @return      */
name|boolean
name|hasIndex
parameter_list|()
function_decl|;
comment|/**      * Returns a layout definition. The returned string may be implementation specific and is not      * standardized.      *      * @return      */
name|String
name|getLayout
parameter_list|()
function_decl|;
comment|/**      * Returns the capabilities of the repository implementation.      * @return      */
name|RepositoryCapabilities
name|getCapabilities
parameter_list|()
function_decl|;
comment|/**      * Extension method that allows to provide different features that are not supported by all      * repository types.      *      * @param clazz The feature class that is requested      * @param<T> This is the class of the feature      * @return The feature implementation for this repository instance, if it is supported      * @throws UnsupportedFeatureException if the feature is not supported by this repository type      */
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
name|getFeature
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|UnsupportedFeatureException
function_decl|;
comment|/**      * Returns true, if the requested feature is supported by this repository.      *      * @param clazz The requested feature class      * @param<T> The requested feature class      * @return True, if the feature is supported, otherwise false.      */
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|boolean
name|supportsFeature
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Returns a indexing context.      * @return      * @throws UnsupportedOperationException      */
name|ArchivaIndexingContext
name|getIndexingContext
parameter_list|()
function_decl|;
comment|/**      * Closes all resources that are opened by this repository.      */
name|void
name|close
parameter_list|()
function_decl|;
comment|/**      * Returns the current status of this repository.      *      * @return<code>true</code>, if repository has not been closed, otherwise<code>false</code>      */
name|boolean
name|isOpen
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

