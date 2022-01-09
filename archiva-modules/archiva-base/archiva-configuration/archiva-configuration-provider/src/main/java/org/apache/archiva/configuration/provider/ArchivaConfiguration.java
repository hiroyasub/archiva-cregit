begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|provider
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
name|components
operator|.
name|registry
operator|.
name|Registry
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
name|components
operator|.
name|registry
operator|.
name|RegistryException
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
name|components
operator|.
name|registry
operator|.
name|RegistryListener
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
name|configuration
operator|.
name|model
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|Locale
import|;
end_import

begin_comment
comment|/**  * Configuration holder for the model read from the registry.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArchivaConfiguration
block|{
name|String
name|USER_CONFIG_PROPERTY
init|=
literal|"archiva.user.configFileName"
decl_stmt|;
name|String
name|USER_CONFIG_ENVVAR
init|=
literal|"ARCHIVA_USER_CONFIG_FILE"
decl_stmt|;
comment|/**      * Get the configuration.      *      * @return the configuration      */
name|Configuration
name|getConfiguration
parameter_list|()
function_decl|;
comment|/**      * Save any updated configuration.      *      * @param configuration the configuration to save      * @throws org.apache.archiva.components.registry.RegistryException      *          if there is a problem saving the registry data      * @throws IndeterminateConfigurationException      *          if the configuration cannot be saved because it was read from two sources      */
name|void
name|save
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RegistryException
throws|,
name|IndeterminateConfigurationException
function_decl|;
comment|/**      * Save any updated configuration. This method allows to add a tag to the thrown event.      * This allows to verify the origin if the caller is the same as the listener.      *      * @param configuration the configuration to save      * @param eventTag the tag to add to the thrown event      * @throws org.apache.archiva.components.registry.RegistryException      *          if there is a problem saving the registry data      * @throws IndeterminateConfigurationException      *          if the configuration cannot be saved because it was read from two sources      */
name|void
name|save
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|String
name|eventTag
parameter_list|)
throws|throws
name|RegistryException
throws|,
name|IndeterminateConfigurationException
function_decl|;
comment|/**      * Determines if the configuration in use was as a result of a defaulted configuration.      *      * @return true if the configuration was created from the default-archiva.xml as opposed      *         to being loaded from the usual locations of ${user.home}/.m2/archiva.xml or      *         ${appserver.base}/conf/archiva.xml      */
name|boolean
name|isDefaulted
parameter_list|()
function_decl|;
comment|/**      * Add a configuration listener to notify of changes to the configuration.      *      * @param listener the listener      */
name|void
name|addListener
parameter_list|(
name|ConfigurationListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Remove a configuration listener to stop notifications of changes to the configuration.      *      * @param listener the listener      */
name|void
name|removeListener
parameter_list|(
name|ConfigurationListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Add a registry listener to notify of events in spring-registry.      *      * @param listener the listener      *                 TODO: Remove in future.      */
name|void
name|addChangeListener
parameter_list|(
name|RegistryListener
name|listener
parameter_list|)
function_decl|;
name|void
name|removeChangeListener
parameter_list|(
name|RegistryListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * reload configuration from file included registry      *      * @since 1.4-M1      */
name|void
name|reload
parameter_list|()
function_decl|;
specifier|public
name|Locale
name|getDefaultLocale
parameter_list|()
function_decl|;
specifier|public
name|List
argument_list|<
name|Locale
operator|.
name|LanguageRange
argument_list|>
name|getLanguagePriorities
parameter_list|()
function_decl|;
specifier|public
name|Path
name|getAppServerBaseDir
parameter_list|()
function_decl|;
comment|/**      * Returns the base directory for repositories that have a relative location path set.      * @return      */
specifier|public
name|Path
name|getRepositoryBaseDir
parameter_list|()
function_decl|;
comment|/**      * Returns the base directory for remote repositories      * @return      */
specifier|public
name|Path
name|getRemoteRepositoryBaseDir
parameter_list|()
function_decl|;
comment|/**      * Returns the base directory for repository group files.      * @return      */
specifier|public
name|Path
name|getRepositoryGroupBaseDir
parameter_list|()
function_decl|;
comment|/**      * Returns the data directory where repositories and metadata reside      * @return      */
specifier|public
name|Path
name|getDataDirectory
parameter_list|()
function_decl|;
comment|/**      * Return the used configuration registry      * @return      */
name|Registry
name|getRegistry
parameter_list|( )
function_decl|;
block|}
end_interface

end_unit

