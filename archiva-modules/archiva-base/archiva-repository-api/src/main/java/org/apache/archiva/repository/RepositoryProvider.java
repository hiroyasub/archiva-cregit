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
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|RemoteRepositoryConfiguration
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
name|RepositoryGroupConfiguration
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
name|event
operator|.
name|EventHandler
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
name|event
operator|.
name|RepositoryEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
comment|/**  *  * This interface must be implemented by the repository implementations. The repository provider knows all  * about the repositories and should be the only part that uses the repository specific classes and libraries  * (e.g. the maven libraries).  *  * Newly created instances should always be filled with default values that make sense. null values should  * be avoided.  *  * References like staging repositories must not be set.  *  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryProvider
extends|extends
name|EventHandler
block|{
comment|/**      * Returns the types of repositories this provider can handle.      *      * @return the set of supported repository types      */
name|Set
argument_list|<
name|RepositoryType
argument_list|>
name|provides
parameter_list|()
function_decl|;
comment|/**      * Creates a editable managed repository instance. The provider must not check the uniqueness of the      * id parameter and must not track the already created instances. Each call to this method will create      * a new instance.      *      * @param id the repository identifier      * @param name the repository name      * @return a new created managed repository instance      */
name|EditableManagedRepository
name|createManagedInstance
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Creates a editable remote repository instance. The provider must not check the uniqueness of the      * id parameter and must not track the already created instances. Each call to this method will create      * a new instance.      *      * @param id the repository identifier      * @param name the repository name      * @return a new created remote repository instance      */
name|EditableRemoteRepository
name|createRemoteInstance
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Creates a editable repository group. . The provider must not check the uniqueness of the      * id parameter and must not track the already created instances. Each call to this method will create      * a new instance.      *      * @param id the repository identifier      * @param name the repository name      * @return A new instance of the repository group implementation      */
name|EditableRepositoryGroup
name|createRepositoryGroup
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Creates a new managed repository instance from the given configuration. All attributes are filled from the      * provided configuration object.      *      * @param configuration the repository configuration that contains the repository data      * @return a new created managed repository instance      * @throws RepositoryException if some of the configuration values are not valid      */
name|ManagedRepository
name|createManagedInstance
parameter_list|(
name|ManagedRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Updates the given managed repository instance from the given configuration. All attributes are filled from the      * provided configuration object.      *      * @param repo the repository instance that should be updated      * @param configuration the repository configuration that contains the repository data      * @throws RepositoryException if some of the configuration values are not valid      */
name|void
name|updateManagedInstance
parameter_list|(
name|EditableManagedRepository
name|repo
parameter_list|,
name|ManagedRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Creates a new managed staging repository instance from the given configuration. All attributes are filled from the      * provided configuration object.      *      * @param baseConfiguration the repository configuration of the base repository that references the staging repository      * @return a new created managed staging repository instance      * @throws RepositoryException if some of the configuration values are not valid      */
name|ManagedRepository
name|createStagingInstance
parameter_list|(
name|ManagedRepositoryConfiguration
name|baseConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Creates a new remote repository instance from the given configuration. All attributes are filled from the      * provided configuration object.      *      * @param configuration the repository configuration that contains the repository data      * @return a new created remote repository instance      * @throws RepositoryException if some of the configuration values are not valid      */
name|RemoteRepository
name|createRemoteInstance
parameter_list|(
name|RemoteRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Updates the given remote repository instance from the given configuration. All attributes are filled from the      * provided configuration object.      *      * @param repo the repository instance that should be updated      * @param configuration the repository configuration that contains the repository data      * @throws RepositoryException if some of the configuration values are not valid      */
name|void
name|updateRemoteInstance
parameter_list|(
name|EditableRemoteRepository
name|repo
parameter_list|,
name|RemoteRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Creates a new repository group instance from the given configuration. All attributes are filled from the      * provided configuration object.      *      * @param configuration the repository group configuration      * @return a new created repository group instance      * @throws RepositoryException if some of the configuration values are not valid      */
name|RepositoryGroup
name|createRepositoryGroup
parameter_list|(
name|RepositoryGroupConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Updates the given remote repository instance from the given configuration. All attributes are filled from the      * provided configuration object.      *      * @param repositoryGroup the repository group instance that should be updated      * @param configuration the repository group configuration that contains the group data      * @throws RepositoryException if some of the configuration values are not valid      */
name|void
name|updateRepositoryGroupInstance
parameter_list|(
name|EditableRepositoryGroup
name|repositoryGroup
parameter_list|,
name|RepositoryGroupConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Returns a configuration object from the given remote repository instance.      *      * @param remoteRepository the remote repository instance      * @return the repository configuration with all the data that is stored in the repository instance      * @throws RepositoryException if the data cannot be converted      */
name|RemoteRepositoryConfiguration
name|getRemoteConfiguration
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Returns a configuration object from the given managed repository instance.      *      * @param managedRepository the managed repository instance      * @return the repository configuration with all the data that is stored in the repository instance      * @throws RepositoryException if the data cannot be converted      */
name|ManagedRepositoryConfiguration
name|getManagedConfiguration
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Returns a configuration object from the given repository group instance.      *      * @param repositoryGroup the repository group      * @return the repository group configuration with all the data that is stored in the repository instance      * @throws RepositoryException if the data cannot be converted      */
name|RepositoryGroupConfiguration
name|getRepositoryGroupConfiguration
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * This event handler is registered to all newly created repositories immediately after creation. This may be needed by      * some components to get events right after creation of the instance.      *      * @param eventHandler the event handler instance      */
name|void
name|addRepositoryEventHandler
parameter_list|(
name|EventHandler
argument_list|<
name|?
super|super
name|RepositoryEvent
argument_list|>
name|eventHandler
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

