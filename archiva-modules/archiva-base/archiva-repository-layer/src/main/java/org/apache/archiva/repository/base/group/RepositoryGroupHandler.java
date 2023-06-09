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
operator|.
name|group
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
name|configuration
operator|.
name|provider
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
name|configuration
operator|.
name|model
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
name|model
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
name|indexer
operator|.
name|merger
operator|.
name|MergedRemoteIndexesScheduler
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
name|EditableRepositoryGroup
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
name|RepositoryGroup
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
name|RepositoryHandlerManager
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
name|base
operator|.
name|AbstractRepositoryHandler
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
name|base
operator|.
name|ArchivaRepositoryRegistry
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
name|base
operator|.
name|ConfigurationHandler
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
name|event
operator|.
name|RepositoryEvent
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
name|IndexCreationFeature
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
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
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
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|concurrent
operator|.
name|locks
operator|.
name|ReentrantReadWriteLock
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|ArchivaIndexManager
operator|.
name|DEFAULT_INDEX_PATH
import|;
end_import

begin_comment
comment|/**  * This class manages repository groups for the RepositoryRegistry.  * It is tightly coupled with the {@link ArchivaRepositoryRegistry}.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryGroupHandler#default"
argument_list|)
specifier|public
class|class
name|RepositoryGroupHandler
extends|extends
name|AbstractRepositoryHandler
argument_list|<
name|RepositoryGroup
argument_list|,
name|RepositoryGroupConfiguration
argument_list|>
implements|implements
name|RepositoryHandler
argument_list|<
name|RepositoryGroup
argument_list|,
name|RepositoryGroupConfiguration
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
name|RepositoryGroupHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|RepositoryHandlerManager
name|repositoryRegistry
decl_stmt|;
specifier|private
specifier|final
name|MergedRemoteIndexesScheduler
name|mergedRemoteIndexesScheduler
decl_stmt|;
specifier|private
name|Path
name|groupsDirectory
decl_stmt|;
comment|/**      * Creates a new instance. All dependencies are injected on the constructor.      *      * @param repositoryRegistry           the registry. To avoid circular dependencies via DI, this class registers itself on the registry.      * @param configurationHandler         the configuration handler is used to retrieve and save configuration.      * @param mergedRemoteIndexesScheduler the index scheduler is used for merging the indexes from all group members      */
specifier|public
name|RepositoryGroupHandler
parameter_list|(
name|RepositoryHandlerManager
name|repositoryRegistry
parameter_list|,
name|ConfigurationHandler
name|configurationHandler
parameter_list|,
annotation|@
name|Named
argument_list|(
literal|"mergedRemoteIndexesScheduler#default"
argument_list|)
name|MergedRemoteIndexesScheduler
name|mergedRemoteIndexesScheduler
parameter_list|)
block|{
name|super
argument_list|(
name|RepositoryGroup
operator|.
name|class
argument_list|,
name|RepositoryGroupConfiguration
operator|.
name|class
argument_list|,
name|configurationHandler
argument_list|)
expr_stmt|;
name|this
operator|.
name|mergedRemoteIndexesScheduler
operator|=
name|mergedRemoteIndexesScheduler
expr_stmt|;
name|this
operator|.
name|repositoryRegistry
operator|=
name|repositoryRegistry
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|PostConstruct
specifier|public
name|void
name|init
parameter_list|( )
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Initializing repository group handler "
operator|+
name|repositoryRegistry
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
name|initializeStorage
argument_list|( )
expr_stmt|;
comment|// We are registering this class on the registry. This is necessary to avoid circular dependencies via injection.
name|this
operator|.
name|repositoryRegistry
operator|.
name|registerHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initializeFromConfig
parameter_list|( )
block|{
name|getRepositories
argument_list|()
operator|.
name|clear
argument_list|( )
expr_stmt|;
name|getRepositories
argument_list|()
operator|.
name|putAll
argument_list|(
name|newInstancesFromConfig
argument_list|( )
argument_list|)
expr_stmt|;
for|for
control|(
name|RepositoryGroup
name|group
range|:
name|getRepositories
argument_list|()
operator|.
name|values
argument_list|( )
control|)
block|{
name|activateRepository
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initializeStorage
parameter_list|( )
block|{
name|Path
name|baseDir
init|=
name|this
operator|.
name|getConfigurationHandler
argument_list|()
operator|.
name|getArchivaConfiguration
argument_list|( )
operator|.
name|getRepositoryGroupBaseDir
argument_list|( )
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|baseDir
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|baseDir
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not create group base directory: {}"
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
block|}
name|this
operator|.
name|groupsDirectory
operator|=
name|baseDir
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|activateRepository
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
block|{
name|StorageAsset
name|indexDirectory
init|=
name|getMergedIndexDirectory
argument_list|(
name|repositoryGroup
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|indexDirectory
operator|.
name|exists
argument_list|( )
condition|)
block|{
try|try
block|{
name|indexDirectory
operator|.
name|create
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not create index directory {} for group {}: {}"
argument_list|,
name|indexDirectory
argument_list|,
name|repositoryGroup
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
name|Path
name|groupPath
init|=
name|groupsDirectory
operator|.
name|resolve
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|( )
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|groupPath
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|groupPath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not create repository group directory {}"
argument_list|,
name|groupPath
argument_list|)
expr_stmt|;
block|}
block|}
name|mergedRemoteIndexesScheduler
operator|.
name|schedule
argument_list|(
name|repositoryGroup
argument_list|,
name|indexDirectory
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|repositoryGroup
argument_list|,
name|RepositoryState
operator|.
name|INITIALIZED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deactivateRepository
parameter_list|(
name|RepositoryGroup
name|repository
parameter_list|)
block|{
name|mergedRemoteIndexesScheduler
operator|.
name|unschedule
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|repository
operator|.
name|close
argument_list|()
expr_stmt|;
name|setLastState
argument_list|(
name|repository
argument_list|,
name|RepositoryState
operator|.
name|DEACTIVATED
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StorageAsset
name|getMergedIndexDirectory
parameter_list|(
name|RepositoryGroup
name|group
parameter_list|)
block|{
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
return|return
name|group
operator|.
name|getFeature
argument_list|(
name|IndexCreationFeature
operator|.
name|class
argument_list|)
operator|.
name|getLocalIndexPath
argument_list|( )
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|newInstancesFromConfig
parameter_list|( )
block|{
try|try
block|{
name|List
argument_list|<
name|RepositoryGroupConfiguration
argument_list|>
name|repositoryGroupConfigurations
init|=
name|this
operator|.
name|getConfigurationHandler
argument_list|()
operator|.
name|getBaseConfiguration
argument_list|( )
operator|.
name|getRepositoryGroups
argument_list|( )
decl_stmt|;
if|if
condition|(
name|repositoryGroupConfigurations
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|( )
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|repositoryGroupMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|repositoryGroupConfigurations
operator|.
name|size
argument_list|( )
argument_list|)
decl_stmt|;
for|for
control|(
name|RepositoryGroupConfiguration
name|repoConfig
range|:
name|repositoryGroupConfigurations
control|)
block|{
name|RepositoryType
name|repositoryType
init|=
name|RepositoryType
operator|.
name|valueOf
argument_list|(
name|repoConfig
operator|.
name|getType
argument_list|( )
argument_list|)
decl_stmt|;
if|if
condition|(
name|super
operator|.
name|providerMap
operator|.
name|containsKey
argument_list|(
name|repositoryType
argument_list|)
condition|)
block|{
try|try
block|{
name|RepositoryGroup
name|repo
init|=
name|createNewRepositoryGroup
argument_list|(
name|providerMap
operator|.
name|get
argument_list|(
name|repositoryType
argument_list|)
argument_list|,
name|repoConfig
argument_list|)
decl_stmt|;
name|repositoryGroupMap
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
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not create repository group {}: {}"
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|( )
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
block|}
block|}
return|return
name|repositoryGroupMap
return|;
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
literal|"Could not initialize repositories from config: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyMap
argument_list|( )
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|newInstance
parameter_list|(
specifier|final
name|RepositoryType
name|type
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|RepositoryProvider
name|provider
init|=
name|getProvider
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|RepositoryGroupConfiguration
name|config
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|( )
decl_stmt|;
name|config
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
name|createNewRepositoryGroup
argument_list|(
name|provider
argument_list|,
name|config
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|newInstance
parameter_list|(
specifier|final
name|RepositoryGroupConfiguration
name|repositoryConfiguration
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|RepositoryType
name|type
init|=
name|RepositoryType
operator|.
name|valueOf
argument_list|(
name|repositoryConfiguration
operator|.
name|getType
argument_list|( )
argument_list|)
decl_stmt|;
name|RepositoryProvider
name|provider
init|=
name|getProvider
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
name|createNewRepositoryGroup
argument_list|(
name|provider
argument_list|,
name|repositoryConfiguration
argument_list|)
return|;
block|}
specifier|private
name|RepositoryGroup
name|createNewRepositoryGroup
parameter_list|(
name|RepositoryProvider
name|provider
parameter_list|,
name|RepositoryGroupConfiguration
name|config
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|RepositoryGroup
name|repositoryGroup
init|=
name|provider
operator|.
name|createRepositoryGroup
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|updateReferences
argument_list|(
name|repositoryGroup
argument_list|,
name|config
argument_list|)
expr_stmt|;
if|if
condition|(
name|repositoryGroup
operator|instanceof
name|EditableRepository
condition|)
block|{
operator|(
operator|(
name|EditableRepository
operator|)
name|repositoryGroup
operator|)
operator|.
name|setLastState
argument_list|(
name|RepositoryState
operator|.
name|REFERENCES_SET
argument_list|)
expr_stmt|;
block|}
name|repositoryGroup
operator|.
name|registerEventHandler
argument_list|(
name|RepositoryEvent
operator|.
name|ANY
argument_list|,
name|repositoryRegistry
argument_list|)
expr_stmt|;
return|return
name|repositoryGroup
return|;
block|}
comment|/**      * Adds a new repository group to the current list, or replaces the repository group definition with      * the same id, if it exists already.      * The change is saved to the configuration immediately.      *      * @param repositoryGroup the new repository group.      * @throws RepositoryException if the new repository group could not be saved to the configuration.      */
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|put
parameter_list|(
specifier|final
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
name|repositoryGroup
operator|.
name|getId
argument_list|( )
decl_stmt|;
name|RepositoryGroup
name|originRepoGroup
init|=
name|getRepositories
argument_list|()
operator|.
name|remove
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|originRepoGroup
operator|!=
literal|null
operator|&&
name|originRepoGroup
operator|!=
name|repositoryGroup
condition|)
block|{
name|deactivateRepository
argument_list|(
name|originRepoGroup
argument_list|)
expr_stmt|;
name|pushEvent
argument_list|(
operator|new
name|LifecycleEvent
argument_list|(
name|LifecycleEvent
operator|.
name|UNREGISTERED
argument_list|,
name|this
argument_list|,
name|originRepoGroup
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|RepositoryProvider
name|provider
init|=
name|getProvider
argument_list|(
name|repositoryGroup
operator|.
name|getType
argument_list|( )
argument_list|)
decl_stmt|;
name|RepositoryGroupConfiguration
name|newCfg
init|=
name|provider
operator|.
name|getRepositoryGroupConfiguration
argument_list|(
name|repositoryGroup
argument_list|)
decl_stmt|;
name|ReentrantReadWriteLock
operator|.
name|WriteLock
name|configLock
init|=
name|this
operator|.
name|getConfigurationHandler
argument_list|()
operator|.
name|getLock
argument_list|( )
operator|.
name|writeLock
argument_list|( )
decl_stmt|;
name|configLock
operator|.
name|lock
argument_list|( )
expr_stmt|;
try|try
block|{
name|Configuration
name|configuration
init|=
name|this
operator|.
name|getConfigurationHandler
argument_list|()
operator|.
name|getBaseConfiguration
argument_list|( )
decl_stmt|;
name|updateReferences
argument_list|(
name|repositoryGroup
argument_list|,
name|newCfg
argument_list|)
expr_stmt|;
name|RepositoryGroupConfiguration
name|oldCfg
init|=
name|configuration
operator|.
name|findRepositoryGroupById
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldCfg
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|removeRepositoryGroup
argument_list|(
name|oldCfg
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|.
name|addRepositoryGroup
argument_list|(
name|newCfg
argument_list|)
expr_stmt|;
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
name|setLastState
argument_list|(
name|repositoryGroup
argument_list|,
name|RepositoryState
operator|.
name|SAVED
argument_list|)
expr_stmt|;
name|activateRepository
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|configLock
operator|.
name|unlock
argument_list|( )
expr_stmt|;
block|}
name|getRepositories
argument_list|()
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|repositoryGroup
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|repositoryGroup
argument_list|,
name|RepositoryState
operator|.
name|REGISTERED
argument_list|)
expr_stmt|;
return|return
name|repositoryGroup
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Rollback
if|if
condition|(
name|originRepoGroup
operator|!=
literal|null
condition|)
block|{
name|getRepositories
argument_list|()
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|originRepoGroup
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getRepositories
argument_list|()
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|error
argument_list|(
literal|"Exception during configuration update {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"Could not save the configuration"
operator|+
operator|(
name|e
operator|.
name|getMessage
argument_list|( )
operator|==
literal|null
condition|?
literal|""
else|:
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
operator|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Adds a new repository group or updates the repository with the same id, if it exists already.      * The configuration is saved immediately.      *      * @param repositoryGroupConfiguration the repository configuration      * @return the updated or created repository      * @throws RepositoryException if an error occurs, or the configuration is not valid.      */
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|put
parameter_list|(
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
parameter_list|)
throws|throws
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
specifier|final
name|RepositoryType
name|repositoryType
init|=
name|RepositoryType
operator|.
name|valueOf
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getType
argument_list|( )
argument_list|)
decl_stmt|;
specifier|final
name|RepositoryProvider
name|provider
init|=
name|getProvider
argument_list|(
name|repositoryType
argument_list|)
decl_stmt|;
name|RepositoryGroup
name|currentRepository
decl_stmt|;
name|ReentrantReadWriteLock
operator|.
name|WriteLock
name|configLock
init|=
name|this
operator|.
name|getConfigurationHandler
argument_list|()
operator|.
name|getLock
argument_list|( )
operator|.
name|writeLock
argument_list|( )
decl_stmt|;
name|configLock
operator|.
name|lock
argument_list|( )
expr_stmt|;
try|try
block|{
name|Configuration
name|configuration
init|=
name|this
operator|.
name|getConfigurationHandler
argument_list|()
operator|.
name|getBaseConfiguration
argument_list|( )
decl_stmt|;
name|currentRepository
operator|=
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|RepositoryGroup
name|oldRepository
init|=
name|currentRepository
operator|==
literal|null
condition|?
literal|null
else|:
name|clone
argument_list|(
name|currentRepository
argument_list|,
name|id
argument_list|)
decl_stmt|;
try|try
block|{
name|boolean
name|updated
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|currentRepository
operator|==
literal|null
condition|)
block|{
name|currentRepository
operator|=
name|put
argument_list|(
name|repositoryGroupConfiguration
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setRepositoryGroupDefaults
argument_list|(
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
name|provider
operator|.
name|updateRepositoryGroupInstance
argument_list|(
operator|(
name|EditableRepositoryGroup
operator|)
name|currentRepository
argument_list|,
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
name|pushEvent
argument_list|(
name|LifecycleEvent
operator|.
name|UPDATED
argument_list|,
name|currentRepository
argument_list|)
expr_stmt|;
block|}
name|registerNewRepository
argument_list|(
name|repositoryGroupConfiguration
argument_list|,
name|currentRepository
argument_list|,
name|configuration
argument_list|,
name|updated
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndeterminateConfigurationException
decl||
name|RegistryException
decl||
name|RepositoryException
name|e
parameter_list|)
block|{
comment|// Trying a rollback
if|if
condition|(
name|oldRepository
operator|!=
literal|null
condition|)
block|{
name|RepositoryGroupConfiguration
name|oldCfg
init|=
name|provider
operator|.
name|getRepositoryGroupConfiguration
argument_list|(
name|oldRepository
argument_list|)
decl_stmt|;
name|provider
operator|.
name|updateRepositoryGroupInstance
argument_list|(
operator|(
name|EditableRepositoryGroup
operator|)
name|currentRepository
argument_list|,
name|oldCfg
argument_list|)
expr_stmt|;
name|rollback
argument_list|(
name|configuration
argument_list|,
name|oldRepository
argument_list|,
name|e
argument_list|,
name|oldCfg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getRepositories
argument_list|()
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|error
argument_list|(
literal|"Could not save the configuration for repository group {}: {}"
argument_list|,
name|id
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|RepositoryException
condition|)
block|{
throw|throw
operator|(
name|RepositoryException
operator|)
name|e
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"Could not save the configuration for repository group "
operator|+
name|id
operator|+
literal|": "
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
finally|finally
block|{
name|configLock
operator|.
name|unlock
argument_list|( )
expr_stmt|;
block|}
return|return
name|currentRepository
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|put
parameter_list|(
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
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
specifier|final
name|RepositoryType
name|repoType
init|=
name|RepositoryType
operator|.
name|valueOf
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getType
argument_list|( )
argument_list|)
decl_stmt|;
name|RepositoryGroup
name|repo
decl_stmt|;
name|setRepositoryGroupDefaults
argument_list|(
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
if|if
condition|(
name|getRepositories
argument_list|()
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|repo
operator|=
name|clone
argument_list|(
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|,
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|repo
operator|instanceof
name|EditableRepositoryGroup
condition|)
block|{
name|getProvider
argument_list|(
name|repoType
argument_list|)
operator|.
name|updateRepositoryGroupInstance
argument_list|(
operator|(
name|EditableRepositoryGroup
operator|)
name|repo
argument_list|,
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"The repository is not editable "
operator|+
name|id
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|repo
operator|=
name|getProvider
argument_list|(
name|repoType
argument_list|)
operator|.
name|createRepositoryGroup
argument_list|(
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|repo
argument_list|,
name|RepositoryState
operator|.
name|CREATED
argument_list|)
expr_stmt|;
block|}
name|replaceOrAddRepositoryConfig
argument_list|(
name|repositoryGroupConfiguration
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|updateReferences
argument_list|(
name|repo
argument_list|,
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|repo
argument_list|,
name|RepositoryState
operator|.
name|REFERENCES_SET
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|private
name|void
name|setRepositoryGroupDefaults
parameter_list|(
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getMergedIndexPath
argument_list|( )
argument_list|)
condition|)
block|{
name|repositoryGroupConfiguration
operator|.
name|setMergedIndexPath
argument_list|(
name|DEFAULT_INDEX_PATH
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|repositoryGroupConfiguration
operator|.
name|getMergedIndexTtl
argument_list|( )
operator|<=
literal|0
condition|)
block|{
name|repositoryGroupConfiguration
operator|.
name|setMergedIndexTtl
argument_list|(
literal|300
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getCronExpression
argument_list|( )
argument_list|)
condition|)
block|{
name|repositoryGroupConfiguration
operator|.
name|setCronExpression
argument_list|(
literal|"0 0 03 ? * MON"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|processOtherVariantRemoval
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
if|if
condition|(
name|repo
operator|instanceof
name|ManagedRepository
condition|)
block|{
name|getRepositories
argument_list|()
operator|.
name|values
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|repoGroup
lambda|->
name|repoGroup
operator|instanceof
name|EditableRepository
argument_list|)
operator|.
name|map
argument_list|(
name|repoGroup
lambda|->
operator|(
name|EditableRepositoryGroup
operator|)
name|repoGroup
argument_list|)
operator|.
name|forEach
argument_list|(
name|repoGroup
lambda|->
name|repoGroup
operator|.
name|removeRepository
argument_list|(
operator|(
name|ManagedRepository
operator|)
name|repo
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|clone
parameter_list|(
name|RepositoryGroup
name|repo
parameter_list|,
name|String
name|newId
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|RepositoryProvider
name|provider
init|=
name|getProvider
argument_list|(
name|repo
operator|.
name|getType
argument_list|( )
argument_list|)
decl_stmt|;
name|RepositoryGroupConfiguration
name|cfg
init|=
name|provider
operator|.
name|getRepositoryGroupConfiguration
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|setId
argument_list|(
name|newId
argument_list|)
expr_stmt|;
name|RepositoryGroup
name|cloned
init|=
name|provider
operator|.
name|createRepositoryGroup
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|cloned
operator|.
name|registerEventHandler
argument_list|(
name|RepositoryEvent
operator|.
name|ANY
argument_list|,
name|repositoryRegistry
argument_list|)
expr_stmt|;
name|setLastState
argument_list|(
name|cloned
argument_list|,
name|RepositoryState
operator|.
name|CREATED
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateReferences
parameter_list|(
name|RepositoryGroup
name|repo
parameter_list|,
name|RepositoryGroupConfiguration
name|repositoryConfiguration
parameter_list|)
block|{
if|if
condition|(
name|repo
operator|instanceof
name|EditableRepositoryGroup
operator|&&
name|repositoryConfiguration
operator|!=
literal|null
condition|)
block|{
name|EditableRepositoryGroup
name|eGroup
init|=
operator|(
name|EditableRepositoryGroup
operator|)
name|repo
decl_stmt|;
name|RepositoryHandler
argument_list|<
name|ManagedRepository
argument_list|,
name|ManagedRepositoryConfiguration
argument_list|>
name|managedHandler
init|=
name|repositoryRegistry
operator|.
name|getHandler
argument_list|(
name|ManagedRepository
operator|.
name|class
argument_list|,
name|ManagedRepositoryConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|eGroup
operator|.
name|setRepositories
argument_list|(
name|repositoryConfiguration
operator|.
name|getRepositories
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|map
argument_list|(
name|managedHandler
operator|::
name|get
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|PreDestroy
specifier|private
name|void
name|destroy
parameter_list|( )
block|{
name|this
operator|.
name|close
argument_list|( )
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|RepositoryGroupConfiguration
name|findRepositoryConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|String
name|id
parameter_list|)
block|{
return|return
name|configuration
operator|.
name|findRepositoryGroupById
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|removeRepositoryConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|RepositoryGroupConfiguration
name|cfg
parameter_list|)
block|{
name|configuration
operator|.
name|removeRepositoryGroup
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|addRepositoryConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|RepositoryGroupConfiguration
name|repoConfiguration
parameter_list|)
block|{
name|configuration
operator|.
name|addRepositoryGroup
argument_list|(
name|repoConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

