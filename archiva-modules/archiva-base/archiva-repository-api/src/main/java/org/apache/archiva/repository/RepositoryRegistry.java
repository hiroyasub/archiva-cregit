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
name|ArchivaConfiguration
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
name|Configuration
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
name|EventSource
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
name|indexer
operator|.
name|ArchivaIndexManager
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
name|indexer
operator|.
name|IndexUpdateFailedException
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
name|metadata
operator|.
name|MetadataReader
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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|validation
operator|.
name|CheckedResult
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
name|validation
operator|.
name|ValidationError
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
name|validation
operator|.
name|ValidationResponse
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  *  Registry for repositories. This is the central entry point for repositories. It provides methods for  *  retrieving, adding and removing repositories.  *<p>  *  The modification methods putXX and removeXX without configuration object persist the changes immediately to the archiva configuration. If the  *  configuration save fails the changes are rolled back.  *</p>  *<p>  *    The modification methods with configuration object do only update the given configuration. The configuration is not saved.  *</p>  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
annotation|@
name|SuppressWarnings
argument_list|(
literal|"UnusedReturnValue"
argument_list|)
specifier|public
interface|interface
name|RepositoryRegistry
extends|extends
name|EventSource
extends|,
name|RepositoryHandlerManager
block|{
comment|/**      * Set the configuration for the registry      * @param archivaConfiguration the archiva configuration instance      */
name|void
name|setArchivaConfiguration
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
function_decl|;
comment|/**      * Return the index manager for the given repository type      * @param type the repository type      * @return the index manager, if it exists      */
name|ArchivaIndexManager
name|getIndexManager
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
function_decl|;
comment|/**      * Returns the metadata reader for the given repository type      * @param type the repository type      * @return the metadata reader instance      */
name|MetadataReader
name|getMetadataReader
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
throws|throws
name|UnsupportedRepositoryTypeException
function_decl|;
comment|/**      * Returns all registered repositories      * @return the list of repositories      */
name|Collection
argument_list|<
name|Repository
argument_list|>
name|getRepositories
parameter_list|( )
function_decl|;
comment|/**      * Returns all managed repositories      * @return the list of managed repositories      */
name|Collection
argument_list|<
name|ManagedRepository
argument_list|>
name|getManagedRepositories
parameter_list|( )
function_decl|;
comment|/**      * Returns a collection of all registered remote repositories      * @return the collection of remote repositories      */
name|Collection
argument_list|<
name|RemoteRepository
argument_list|>
name|getRemoteRepositories
parameter_list|( )
function_decl|;
comment|/**      * Returns a collection of all registered repository groups.      *      * @return the collection of repository groups      */
name|Collection
argument_list|<
name|RepositoryGroup
argument_list|>
name|getRepositoryGroups
parameter_list|( )
function_decl|;
comment|/**      * Returns the repository (managed, remote, group) with the given id      * @param repoId the id of the repository      * @return the repository or<code>null</code> if no repository with this ID is registered.      */
name|Repository
name|getRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
function_decl|;
comment|/**      * Returns the managed repository with the given id      * @param repoId the id of the repository      * @return the managed repository instance or<code>null</code>, if no managed repository with this ID is registered.      */
name|ManagedRepository
name|getManagedRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
function_decl|;
comment|/**      * Returns the remote repository with the given id      * @param repoId the id of the repository      * @return the remote repository instance or<code>null</code>, if no remote repository with this ID is registered.      */
name|RemoteRepository
name|getRemoteRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
function_decl|;
comment|/**      * Returns the repository group with the given id      * @param groupId the id of the repository group      * @return the repository group instance or<code>null</code>, if no repository group with this ID is registered.      */
name|RepositoryGroup
name|getRepositoryGroup
parameter_list|(
name|String
name|groupId
parameter_list|)
function_decl|;
comment|/**      * Returns<code>true</code>, if a repository with the given ID is registered, otherwise<code>false</code>      * @param repoId the ID of the repository      * @return<code>true</code>, if a repository with the given ID is registered, otherwise<code>false</code>      */
name|boolean
name|hasRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
function_decl|;
comment|/**      * Returns<code>true</code>, if a managed repository with the given ID is registered, otherwise<code>false</code>      * @param repoId the id of the managed repository      * @return<code>true</code>, if a managed repository with the given ID is registered, otherwise<code>false</code>      */
name|boolean
name|hasManagedRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
function_decl|;
comment|/**      * Returns<code>true</code>, if a remote repository with the given ID is registered, otherwise<code>false</code>      * @param repoId the id of the remote repository      * @return<code>true</code>, if a remote repository with the given ID is registered, otherwise<code>false</code>      */
name|boolean
name|hasRemoteRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
function_decl|;
comment|/**      * Returns<code>true</code>, if a repository group with the given ID is registered, otherwise<code>false</code>      * @param groupId the id of the repository group      * @return<code>true</code>, if a repository group with the given ID is registered, otherwise<code>false</code>      */
name|boolean
name|hasRepositoryGroup
parameter_list|(
name|String
name|groupId
parameter_list|)
function_decl|;
comment|/**      * Adds or updates the given managed repository. If a managed repository with the given id exists already, it is updated      * from the data of the given instance. Otherwise a new repository is created and updated by the data of the given instance.      *      * The archiva configuration is updated and saved after updating the registered repository instance.      *      * @param managedRepository the managed repository      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|ManagedRepository
name|putRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates the given managed repository. If a managed repository with the given id exists already, it is updated      * from the data of the given configuration. Otherwise a new repository is created and updated by the data of the given configuration.      *      * The archiva configuration is updated and saved after updating the registered repository instance.      *      * @param managedRepositoryConfiguration the managed repository configuration      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|ManagedRepository
name|putRepository
parameter_list|(
name|ManagedRepositoryConfiguration
name|managedRepositoryConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates the given managed repository. If a managed repository with the given id exists already, it is updated      * from the data of the given configuration. Otherwise a new repository is created and updated by the data of the given configuration.      *      * This method can be used, if the archiva configuration should not be saved. It will only update the given configuration object.      *      * @param managedRepositoryConfiguration the managed repository configuration      * @param configuration the archiva configuration that is updated      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|ManagedRepository
name|putRepository
parameter_list|(
name|ManagedRepositoryConfiguration
name|managedRepositoryConfiguration
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates the given repository group. If a repository group with the given id exists already, it is updated      * from the data of the given instance. Otherwise a new repository is created and updated by the data of the given instance.      *      * The archiva configuration is updated and saved after updating the registered repository instance.      *      * @param repositoryGroup the repository group      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|RepositoryGroup
name|putRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates the given repository group. If a repository group with the given id exists already, it is updated      * from the data of the given configuration. Otherwise a new repository is created and updated by the data of the given configuration.      *      * The archiva configuration is updated and saved after updating the registered repository instance.      *      * @param repositoryGroupConfiguration the repository group configuration      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|RepositoryGroup
name|putRepositoryGroup
parameter_list|(
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * This method creates or updates a repository by the given configuration. It uses the<code>validator</code> to check the      * result. If the validation is not successful, the repository will not be saved.      *      * @param configuration the repository configuration      * @return the result      */
name|CheckedResult
argument_list|<
name|RepositoryGroup
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
argument_list|>
argument_list|>
name|putRepositoryGroupAndValidate
parameter_list|(
name|RepositoryGroupConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates the given repository group. If a repository group with the given id exists already, it is updated      * from the data of the given configuration. Otherwise a new repository is created and updated by the data of the given configuration.      *      * This method can be used, if the archiva configuration should not be saved. It will only update the given configuration object.      *      * @param repositoryGroupConfiguration the repository group configuration      * @param configuration the archiva configuration that is updated      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|RepositoryGroup
name|putRepositoryGroup
parameter_list|(
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates the given remote repository. If a remote repository with the given id exists already, it is updated      * from the data of the given instance. Otherwise a new repository is created and updated by the data of the given instance.      *      * The archiva configuration is updated and saved after updating the registered repository instance.      *      * @param remoteRepository the remote repository      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|RemoteRepository
name|putRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates the given remote repository. If a remote repository with the given id exists already, it is updated      * from the data of the given configuration. Otherwise a new repository is created and updated by the data of the given configuration.      *      * The archiva configuration is updated and saved after updating the registered repository instance.      *      * @param remoteRepositoryConfiguration the remote repository configuration      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|RemoteRepository
name|putRepository
parameter_list|(
name|RemoteRepositoryConfiguration
name|remoteRepositoryConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates the given remote repository. If a remote repository with the given id exists already, it is updated      * from the data of the given configuration. Otherwise a new repository is created and updated by the data of the given configuration.      *      * This method can be used, if the archiva configuration should not be saved. It will only update the given configuration object.      *      * @param remoteRepositoryConfiguration the remote repository configuration      * @param configuration the archiva configuration where the updated data is stored into      * @return the repository instance, that was created or updated      * @throws RepositoryException if an error occurred while creating or updating the instance      */
name|RemoteRepository
name|putRepository
parameter_list|(
name|RemoteRepositoryConfiguration
name|remoteRepositoryConfiguration
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the repository or repository group with the given id, if it exists. Otherwise, it will do nothing.      *      * The configuration is updated and saved, if the deletion was successful      *      * @param repoId the id of the repository or repository group to delete      * @throws RepositoryException if the repository deletion failed      */
name|void
name|removeRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given repository.      *      * The configuration is updated and saved, if the deletion was successful      *      * @param repo the repository instance that should be deleted      * @throws RepositoryException if the repository deletion failed      */
name|void
name|removeRepository
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given managed repository.      *      * The configuration is updated and saved, if the deletion was successful      *      * @param managedRepository the managed repository to remove      * @throws RepositoryException if the repository deletion failed      */
name|void
name|removeRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given managed repository. The given configuration instance is updated, but the      * archiva configuration is not saved.      *      * @param managedRepository the managed repository to remove      * @param configuration the configuration instance to update      * @throws RepositoryException if the repository deletion failed      */
name|void
name|removeRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given repository group.      *      * The configuration is updated and saved, if the deletion was successful      *      * @param repositoryGroup the repository group to remove      * @throws RepositoryException if the repository deletion failed      */
name|void
name|removeRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given repository group. The given configuration instance is updated, but the      * archiva configuration is not saved.      *      * @param repositoryGroup the repository group to remove      * @param configuration the configuration instance to update      * @throws RepositoryException if the repository deletion failed      */
name|void
name|removeRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given remote repository.      *      * The configuration is updated and saved, if the deletion was successful      *      * @param remoteRepository the remote repository to remove      * @throws RepositoryException if the repository deletion failed      */
name|void
name|removeRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given remote repository. The given configuration instance is updated, but the      * archiva configuration is not saved.      *      * @param remoteRepository the remote repository to remove      * @param configuration the configuration instance to update      * @throws RepositoryException if the repository deletion failed      */
name|void
name|removeRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Reloads all repositories and groups from the configuration      */
name|void
name|reload
parameter_list|( )
function_decl|;
name|void
name|resetIndexingContext
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|IndexUpdateFailedException
function_decl|;
comment|/**      * Creates a new repository based on the given repository and with the given new id.      * @param repo the repository to copy from      * @param newId the new repository id      * @param<T> the type of the repository (Manage, Remote or RepositoryGroup)      * @return the newly created repository      * @throws RepositoryException if the repository could not be created      */
parameter_list|<
name|T
extends|extends
name|Repository
parameter_list|>
name|T
name|clone
parameter_list|(
name|T
name|repo
parameter_list|,
name|String
name|newId
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Return the repository that stores the given asset.      * @param asset the asset      * @return the repository or<code>null</code> if no matching repository is found      */
name|Repository
name|getRepositoryOfAsset
parameter_list|(
name|StorageAsset
name|asset
parameter_list|)
function_decl|;
comment|/**      * Validates the set attributes of the given repository instance and returns the validation result.      * The repository registry uses all available validators and applies their validateRepository method to the given      * repository. Validation results will be merged per field.      *      * @param repository the repository to validate against      * @return the result of the validation.      */
parameter_list|<
name|R
extends|extends
name|Repository
parameter_list|>
name|ValidationResponse
argument_list|<
name|R
argument_list|>
name|validateRepository
parameter_list|(
name|R
name|repository
parameter_list|)
function_decl|;
comment|/**      * Validates the set attributes of the given repository instance for a repository update and returns the validation result.      * The repository registry uses all available validators and applies their validateRepositoryForUpdate method to the given      * repository. Validation results will be merged per field.      *      * @param repository the repository to validate against      * @return the result of the validation.      */
parameter_list|<
name|R
extends|extends
name|Repository
parameter_list|>
name|ValidationResponse
argument_list|<
name|R
argument_list|>
name|validateRepositoryForUpdate
parameter_list|(
name|R
name|repository
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

