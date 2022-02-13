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
name|configuration
operator|.
name|model
operator|.
name|AbstractRepositoryConfiguration
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
name|RepositoryChecker
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
name|RepositoryValidator
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
comment|/**  * This is the generic interface that handles different repository flavours, currently for  * ManagedRepository, RemoteRepository and RepositoryGroup  *  * Lifecycle/states of a repository:  *<ul>  *<li>Instance created: This state is reached by the newInstance-methods. The instance is created, filled with the  *     corresponding attribute data and references are updated. References are object references to other repositories, if they exist.  *     The instance is not registered on the registry (stored) and configuration is not updated.</li>  *<li>Instance registered: Instances added/updated by the put()-methods are created and registered on the registry.  *     If all goes well, the configuration is updated.</li>  *<li>Instance initialized:</li>  *</ul>  *  * The repository handler are not thread safe. Synchronization is done by registry if necessary.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryHandler
parameter_list|<
name|R
extends|extends
name|Repository
parameter_list|,
name|C
extends|extends
name|AbstractRepositoryConfiguration
parameter_list|>
block|{
comment|/**      * Initializes the current state from the configuration      */
name|void
name|initializeFromConfig
parameter_list|( )
function_decl|;
comment|/**      * Initializes the repository. E.g. starts scheduling and activate additional processes.      * @param repository the repository to initialize      */
name|void
name|activateRepository
parameter_list|(
name|R
name|repository
parameter_list|)
function_decl|;
comment|/**      * Reset the repository. E.g. stops scheduling.      * @param repository      */
name|void
name|deactivateRepository
parameter_list|(
name|R
name|repository
parameter_list|)
function_decl|;
comment|/**      * Creates new instances from the archiva configuration. The instances are not registered in the registry.      *      * @return A map of (repository id, Repository) pairs      */
name|Map
argument_list|<
name|String
argument_list|,
name|R
argument_list|>
name|newInstancesFromConfig
parameter_list|( )
function_decl|;
comment|/**      * Creates a new instance without registering and without updating the archiva configuration      *      * @param type the repository type      * @param id   the repository identifier      * @return the repository instance      * @throws RepositoryException if the creation failed      */
name|R
name|newInstance
parameter_list|(
name|RepositoryType
name|type
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Creates a new instance based on the given configuration instance. The instance is not activated and not registered.      *      * @param repositoryConfiguration the configuration instance      * @return a newly created instance      * @throws RepositoryException if the creation failed      */
name|R
name|newInstance
parameter_list|(
name|C
name|repositoryConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds the given repository to the registry or replaces a already existing repository in the registry.      * If an error occurred during the update, it will revert to the old repository status.      *      * @param repository the repository      * @return the created or updated repository instance      * @throws RepositoryException if the update or creation failed      */
name|R
name|put
parameter_list|(
name|R
name|repository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds the repository to the registry, based on the given configuration.      * If there is a repository registered with the given id, it is updated.      * The archiva configuration is updated. The status is not defined, if an error occurs during update. The      * The repository instance is registered and initialized if no error occurs      *      * @param repositoryConfiguration the repository configuration      * @return the updated or created repository instance      * @throws RepositoryException if the update or creation failed      */
name|R
name|put
parameter_list|(
name|C
name|repositoryConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds a repository from the given repository configuration. The changes are stored in      * the configuration object. The archiva registry is not updated.      * The returned repository instance is a clone of the registered repository instance. It is not registered      * and not initialized. References are not updated.      *      * @param repositoryConfiguration the repository configuration      * @param configuration           the configuration instance      * @return the repository instance that was created or updated      * @throws RepositoryException if the update or creation failed      */
name|R
name|put
parameter_list|(
name|C
name|repositoryConfiguration
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates a repository from the given configuration data. The resulting repository is      * checked by the repository checker and the result is returned.      * If the checker returns a valid result, the registry is updated and configuration is saved.      *      * @param repositoryConfiguration the repository configuration      * @param checker                 the checker that validates the repository data      * @return the repository and the check result      * @throws RepositoryException if the creation or update failed      */
parameter_list|<
name|D
parameter_list|>
name|CheckedResult
argument_list|<
name|R
argument_list|,
name|D
argument_list|>
name|putWithCheck
parameter_list|(
name|C
name|repositoryConfiguration
parameter_list|,
name|RepositoryChecker
argument_list|<
name|R
argument_list|,
name|D
argument_list|>
name|checker
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Adds or updates a repository from the given configuration data. The resulting repository is      * checked by the default repository checker of the handler instance and the result is returned.      * If the checker returns a valid result, the registry is updated and configuration is saved.      *      * @param repositoryConfiguration the repository configuration      * @return the repository and the check result as map of attributes -> list of validation errors      * @throws RepositoryException if the creation or update failed      */
name|CheckedResult
argument_list|<
name|R
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
name|putWithCheck
parameter_list|(
name|C
name|repositoryConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given repository from the registry and updates references and saves the new configuration.      *      * @param id The repository identifier      * @throws RepositoryException if the repository could not be removed      */
name|void
name|remove
parameter_list|(
specifier|final
name|String
name|id
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Removes the given repository from the registry and updates only the given configuration instance.      * The archiva registry is not updated      *      * @param id            the repository identifier      * @param configuration the configuration to update      * @throws RepositoryException if the repository could not be removed      */
name|void
name|remove
parameter_list|(
name|String
name|id
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Returns the repository with the given identifier or<code>null</code>, if no repository is registered      * with the given id.      *      * @param id the repository id      * @return the repository instance or<code>null</code>      */
name|R
name|get
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Clones a given repository without registering.      *      * @param repo the repository that should be cloned      * @param newId the new identifier of the cloned instance      * @return a newly created instance with the same repository data      */
name|R
name|clone
parameter_list|(
name|R
name|repo
parameter_list|,
name|String
name|newId
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Updates the references and stores updates in the given<code>configuration</code> instance.      * The references that are updated depend on the concrete repository subclass<code>R</code>.      * This method may register/unregister repositories depending on the implementation. That means there is no simple      * way to roll back, if an error occurs.      *      * @param repo                    the repository for which references are updated      * @param repositoryConfiguration the repository configuration      */
name|void
name|updateReferences
parameter_list|(
name|R
name|repo
parameter_list|,
name|C
name|repositoryConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Returns all registered repositories.      *      * @return the list of repositories      */
name|Collection
argument_list|<
name|R
argument_list|>
name|getAll
parameter_list|( )
function_decl|;
comment|/**      * Returns a validator that can be used to validate repository data      *      * @return a validator instance      */
name|RepositoryValidator
argument_list|<
name|R
argument_list|>
name|getValidator
parameter_list|( )
function_decl|;
comment|/**      * Validates the set attributes of the given repository instance and returns the validation result.      * The repository registry uses all available validators and applies their validateRepository method to the given      * repository. Validation results will be merged per field.      *      * @param repository the repository to validate against      * @return the result of the validation.      */
name|CheckedResult
argument_list|<
name|R
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
name|validateRepository
parameter_list|(
name|R
name|repository
parameter_list|)
function_decl|;
comment|/**      * Validates the set attributes of the given repository instance for a repository update and returns the validation result.      * The repository registry uses all available validators and applies their validateRepositoryForUpdate method to the given      * repository. Validation results will be merged per field.      *      * @param repository the repository to validate against      * @return the result of the validation.      */
name|CheckedResult
argument_list|<
name|R
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
name|validateRepositoryForUpdate
parameter_list|(
name|R
name|repository
parameter_list|)
function_decl|;
comment|/**      * Returns<code>true</code>, if the repository is registered with the given id, otherwise<code>false</code>      *      * @param id the repository identifier      * @return<code>true</code>, if it is registered, otherwise<code>false</code>      */
name|boolean
name|hasRepository
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * This is called, when another variant repository was removed. This is needed only for certain variants.      *      * @param repository      */
name|void
name|processOtherVariantRemoval
parameter_list|(
name|Repository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Initializes the handler. This method must be called before using the repository handler.      */
name|void
name|init
parameter_list|( )
function_decl|;
comment|/**      * Closes the handler. After closing, the repository handler instance is not usable anymore.      */
name|void
name|close
parameter_list|( )
function_decl|;
comment|/**      * Sets the repository provider list      * @param providers      */
name|void
name|setRepositoryProviders
parameter_list|(
name|List
argument_list|<
name|RepositoryProvider
argument_list|>
name|providers
parameter_list|)
function_decl|;
comment|/**      * Sets the list of repository validators      * @param repositoryValidatorList      */
name|void
name|setRepositoryValidator
parameter_list|(
name|List
argument_list|<
name|RepositoryValidator
argument_list|<
name|?
extends|extends
name|Repository
argument_list|>
argument_list|>
name|repositoryValidatorList
parameter_list|)
function_decl|;
comment|/**      * Returns the repository variant, this handler manages.      * @return the concrete variant class      */
name|Class
argument_list|<
name|R
argument_list|>
name|getFlavour
parameter_list|()
function_decl|;
comment|/**      * Returns the repository configuration variant, this handler manages.      * @return the concrete configuration variant class      */
name|Class
argument_list|<
name|C
argument_list|>
name|getConfigurationFlavour
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

