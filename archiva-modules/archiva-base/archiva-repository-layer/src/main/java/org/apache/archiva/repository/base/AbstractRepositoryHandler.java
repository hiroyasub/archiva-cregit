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
operator|.
name|base
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
name|configuration
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
name|IndeterminateConfigurationException
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
name|Event
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
name|EventManager
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
name|EventType
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
name|EditableRepository
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
name|Repository
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
name|RepositoryException
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
name|RepositoryHandler
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
name|RepositoryProvider
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
name|RepositoryState
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
name|RepositoryType
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
name|LifecycleEvent
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
name|CombinedValidator
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * Base abstract class for repository handlers.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryHandler
parameter_list|<
name|R
extends|extends
name|Repository
parameter_list|,
name|C
extends|extends
name|AbstractRepositoryConfiguration
parameter_list|>
implements|implements
name|RepositoryHandler
argument_list|<
name|R
argument_list|,
name|C
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractRepositoryHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|RepositoryType
argument_list|,
name|RepositoryProvider
argument_list|>
name|providerMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
specifier|private
name|CombinedValidator
argument_list|<
name|R
argument_list|>
name|combinedValidator
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|R
argument_list|>
name|repositoryClazz
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|C
argument_list|>
name|configurationClazz
decl_stmt|;
specifier|private
specifier|final
name|EventManager
name|eventManager
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|R
argument_list|>
name|repositoryMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
specifier|final
name|ConfigurationHandler
name|configurationHandler
decl_stmt|;
specifier|public
name|AbstractRepositoryHandler
parameter_list|(
name|Class
argument_list|<
name|R
argument_list|>
name|repositoryClazz
parameter_list|,
name|Class
argument_list|<
name|C
argument_list|>
name|configurationClazz
parameter_list|,
name|ConfigurationHandler
name|configurationHandler
parameter_list|)
block|{
name|this
operator|.
name|repositoryClazz
operator|=
name|repositoryClazz
expr_stmt|;
name|this
operator|.
name|configurationClazz
operator|=
name|configurationClazz
expr_stmt|;
name|this
operator|.
name|eventManager
operator|=
operator|new
name|EventManager
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|configurationHandler
operator|=
name|configurationHandler
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|RepositoryValidator
argument_list|<
name|R
argument_list|>
argument_list|>
name|initValidators
parameter_list|(
name|Class
argument_list|<
name|R
argument_list|>
name|clazz
parameter_list|,
name|List
argument_list|<
name|RepositoryValidator
argument_list|<
name|?
extends|extends
name|Repository
argument_list|>
argument_list|>
name|repositoryGroupValidatorList
parameter_list|)
block|{
if|if
condition|(
name|repositoryGroupValidatorList
operator|!=
literal|null
operator|&&
name|repositoryGroupValidatorList
operator|.
name|size
argument_list|( )
operator|>
literal|0
condition|)
block|{
return|return
name|repositoryGroupValidatorList
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|v
lambda|->
name|v
operator|.
name|isFlavour
argument_list|(
name|clazz
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|v
lambda|->
name|v
operator|.
name|narrowTo
argument_list|(
name|clazz
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|( )
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|( )
return|;
block|}
block|}
specifier|protected
name|CombinedValidator
argument_list|<
name|R
argument_list|>
name|getCombinedValidator
parameter_list|(
name|Class
argument_list|<
name|R
argument_list|>
name|clazz
parameter_list|,
name|List
argument_list|<
name|RepositoryValidator
argument_list|<
name|?
extends|extends
name|Repository
argument_list|>
argument_list|>
name|repositoryGroupValidatorList
parameter_list|)
block|{
return|return
operator|new
name|CombinedValidator
argument_list|<>
argument_list|(
name|clazz
argument_list|,
name|initValidators
argument_list|(
name|clazz
argument_list|,
name|repositoryGroupValidatorList
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|setLastState
parameter_list|(
name|R
name|repo
parameter_list|,
name|RepositoryState
name|state
parameter_list|)
block|{
name|RepositoryState
name|currentState
init|=
name|repo
operator|.
name|getLastState
argument_list|( )
decl_stmt|;
if|if
condition|(
name|repo
operator|instanceof
name|EditableRepository
condition|)
block|{
if|if
condition|(
name|state
operator|.
name|getOrderNumber
argument_list|( )
operator|>
name|repo
operator|.
name|getLastState
argument_list|( )
operator|.
name|getOrderNumber
argument_list|( )
condition|)
block|{
operator|(
operator|(
name|EditableRepository
operator|)
name|repo
operator|)
operator|.
name|setLastState
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Found a not editable repository instance: {}, {}"
argument_list|,
name|repo
operator|.
name|getId
argument_list|( )
argument_list|,
name|repo
operator|.
name|getClass
argument_list|( )
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|==
name|RepositoryState
operator|.
name|REGISTERED
operator|&&
name|state
operator|!=
name|currentState
condition|)
block|{
name|pushEvent
argument_list|(
name|LifecycleEvent
operator|.
name|REGISTERED
argument_list|,
name|repo
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|state
operator|==
name|RepositoryState
operator|.
name|UNREGISTERED
operator|&&
name|state
operator|!=
name|currentState
condition|)
block|{
name|pushEvent
argument_list|(
name|LifecycleEvent
operator|.
name|UNREGISTERED
argument_list|,
name|repo
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setRepositoryProviders
parameter_list|(
name|List
argument_list|<
name|RepositoryProvider
argument_list|>
name|providers
parameter_list|)
block|{
if|if
condition|(
name|providers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RepositoryProvider
name|provider
range|:
name|providers
control|)
block|{
for|for
control|(
name|RepositoryType
name|type
range|:
name|provider
operator|.
name|provides
argument_list|( )
control|)
block|{
name|providerMap
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|RepositoryProvider
name|getProvider
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
block|{
return|return
name|providerMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|this
operator|.
name|combinedValidator
operator|=
name|getCombinedValidator
argument_list|(
name|repositoryClazz
argument_list|,
name|repositoryValidatorList
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CombinedValidator
argument_list|<
name|R
argument_list|>
name|getCombinedValidator
parameter_list|()
block|{
return|return
name|this
operator|.
name|combinedValidator
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|R
argument_list|>
name|getVariant
parameter_list|( )
block|{
return|return
name|this
operator|.
name|repositoryClazz
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|C
argument_list|>
name|getConfigurationVariant
parameter_list|( )
block|{
return|return
name|this
operator|.
name|configurationClazz
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|processOtherVariantRemoval
parameter_list|(
name|Repository
name|repository
parameter_list|)
block|{
comment|// Default: do nothing
block|}
specifier|protected
name|void
name|pushEvent
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|eventManager
operator|.
name|fireEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|pushEvent
parameter_list|(
name|EventType
argument_list|<
name|?
extends|extends
name|LifecycleEvent
argument_list|>
name|event
parameter_list|,
name|R
name|repo
parameter_list|)
block|{
name|pushEvent
argument_list|(
operator|new
name|LifecycleEvent
argument_list|(
name|event
argument_list|,
name|this
argument_list|,
name|repo
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|R
argument_list|>
name|getRepositories
parameter_list|()
block|{
return|return
name|repositoryMap
return|;
block|}
specifier|protected
name|ConfigurationHandler
name|getConfigurationHandler
parameter_list|()
block|{
return|return
name|configurationHandler
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|activateRepository
parameter_list|(
name|R
name|repository
parameter_list|)
block|{
comment|//
block|}
annotation|@
name|Override
specifier|public
name|void
name|deactivateRepository
parameter_list|(
name|R
name|repository
parameter_list|)
block|{
name|repository
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|R
name|newInstance
parameter_list|(
name|C
name|repositoryConfiguration
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
annotation|@
name|Override
specifier|public
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
block|{
specifier|final
name|String
name|id
init|=
name|repositoryConfiguration
operator|.
name|getId
argument_list|( )
decl_stmt|;
name|R
name|currentRepository
init|=
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|R
name|managedRepository
init|=
name|newInstance
argument_list|(
name|repositoryConfiguration
argument_list|)
decl_stmt|;
name|CheckedResult
argument_list|<
name|R
argument_list|,
name|D
argument_list|>
name|result
decl_stmt|;
if|if
condition|(
name|currentRepository
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|checker
operator|.
name|apply
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|checker
operator|.
name|applyForUpdate
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|.
name|isValid
argument_list|( )
condition|)
block|{
name|put
argument_list|(
name|result
operator|.
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
specifier|abstract
name|C
name|findRepositoryConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|String
name|id
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|void
name|removeRepositoryConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|C
name|repoConfiguration
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|void
name|addRepositoryConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|C
name|repoConfiguration
parameter_list|)
function_decl|;
comment|/**      * Removes a repository group from the registry and configuration, if it exists.      * The change is saved to the configuration immediately.      *      * @param id the id of the repository group to remove      * @throws RepositoryException if an error occurs during configuration save      */
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|R
name|repo
init|=
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|repo
operator|=
name|getRepositories
argument_list|()
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|deactivateRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|this
operator|.
name|configurationHandler
operator|.
name|getBaseConfiguration
argument_list|( )
decl_stmt|;
name|C
name|cfg
init|=
name|findRepositoryConfiguration
argument_list|(
name|configuration
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfg
operator|!=
literal|null
condition|)
block|{
name|removeRepositoryConfiguration
argument_list|(
name|configuration
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|configurationHandler
operator|.
name|save
argument_list|(
name|configuration
argument_list|,
name|ConfigurationHandler
operator|.
name|REGISTRY_EVENT_TAG
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|repo
argument_list|,
name|RepositoryState
operator|.
name|UNREGISTERED
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RegistryException
decl||
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
comment|// Rollback
name|log
operator|.
name|error
argument_list|(
literal|"Could not save config after repository removal: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|getRepositories
argument_list|()
operator|.
name|put
argument_list|(
name|repo
operator|.
name|getId
argument_list|( )
argument_list|,
name|repo
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"Could not save configuration after repository removal: "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|R
name|repo
init|=
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|repo
operator|=
name|getRepositories
argument_list|()
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|deactivateRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
name|setLastState
argument_list|(
name|repo
argument_list|,
name|RepositoryState
operator|.
name|UNREGISTERED
argument_list|)
expr_stmt|;
block|}
name|C
name|cfg
init|=
name|findRepositoryConfiguration
argument_list|(
name|configuration
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfg
operator|!=
literal|null
condition|)
block|{
name|removeRepositoryConfiguration
argument_list|(
name|configuration
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|R
name|get
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
return|return
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|R
argument_list|>
name|getAll
parameter_list|( )
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|getRepositories
argument_list|( )
operator|.
name|values
argument_list|( )
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryValidator
argument_list|<
name|R
argument_list|>
name|getValidator
parameter_list|( )
block|{
return|return
name|getCombinedValidator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
return|return
name|getCombinedValidator
argument_list|(  )
operator|.
name|apply
argument_list|(
name|repository
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
return|return
name|getCombinedValidator
argument_list|()
operator|.
name|applyForUpdate
argument_list|(
name|repository
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasRepository
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|getRepositories
argument_list|()
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|( )
block|{
for|for
control|(
name|R
name|repository
range|:
name|getRepositories
argument_list|()
operator|.
name|values
argument_list|( )
control|)
block|{
try|try
block|{
name|deactivateRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not close repository {}: {}"
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
name|getRepositories
argument_list|()
operator|.
name|clear
argument_list|( )
expr_stmt|;
block|}
specifier|protected
name|void
name|registerNewRepository
parameter_list|(
name|C
name|repositoryGroupConfiguration
parameter_list|,
name|R
name|currentRepository
parameter_list|,
name|Configuration
name|configuration
parameter_list|,
name|boolean
name|updated
parameter_list|)
throws|throws
name|IndeterminateConfigurationException
throws|,
name|RegistryException
throws|,
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
name|repositoryGroupConfiguration
operator|.
name|getId
argument_list|( )
decl_stmt|;
name|getConfigurationHandler
argument_list|()
operator|.
name|save
argument_list|(
name|configuration
argument_list|,
name|ConfigurationHandler
operator|.
name|REGISTRY_EVENT_TAG
argument_list|)
expr_stmt|;
name|updateReferences
argument_list|(
name|currentRepository
argument_list|,
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|updated
condition|)
block|{
name|setLastState
argument_list|(
name|currentRepository
argument_list|,
name|RepositoryState
operator|.
name|REFERENCES_SET
argument_list|)
expr_stmt|;
block|}
name|activateRepository
argument_list|(
name|currentRepository
argument_list|)
expr_stmt|;
name|getRepositories
argument_list|()
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|currentRepository
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|currentRepository
argument_list|,
name|RepositoryState
operator|.
name|REGISTERED
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|rollback
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|R
name|oldRepository
parameter_list|,
name|Exception
name|e
parameter_list|,
name|C
name|oldCfg
parameter_list|)
throws|throws
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
name|oldRepository
operator|.
name|getId
argument_list|( )
decl_stmt|;
name|replaceOrAddRepositoryConfig
argument_list|(
name|oldCfg
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
try|try
block|{
name|getConfigurationHandler
argument_list|()
operator|.
name|save
argument_list|(
name|configuration
argument_list|,
name|ConfigurationHandler
operator|.
name|REGISTRY_EVENT_TAG
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndeterminateConfigurationException
decl||
name|RegistryException
name|indeterminateConfigurationException
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Fatal error, config save during rollback failed: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|updateReferences
argument_list|(
name|oldRepository
argument_list|,
name|oldCfg
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|oldRepository
argument_list|,
name|RepositoryState
operator|.
name|REFERENCES_SET
argument_list|)
expr_stmt|;
name|activateRepository
argument_list|(
name|oldRepository
argument_list|)
expr_stmt|;
name|getRepositories
argument_list|()
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|oldRepository
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|oldRepository
argument_list|,
name|RepositoryState
operator|.
name|REGISTERED
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|replaceOrAddRepositoryConfig
parameter_list|(
name|C
name|repositoryGroupConfiguration
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
block|{
name|C
name|oldCfg
init|=
name|findRepositoryConfiguration
argument_list|(
name|configuration
argument_list|,
name|repositoryGroupConfiguration
operator|.
name|getId
argument_list|( )
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldCfg
operator|!=
literal|null
condition|)
block|{
name|removeRepositoryConfiguration
argument_list|(
name|configuration
argument_list|,
name|oldCfg
argument_list|)
expr_stmt|;
block|}
name|addRepositoryConfiguration
argument_list|(
name|configuration
argument_list|,
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
