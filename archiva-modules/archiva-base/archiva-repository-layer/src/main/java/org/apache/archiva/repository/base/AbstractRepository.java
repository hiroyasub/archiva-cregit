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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|cronutils
operator|.
name|model
operator|.
name|CronType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|cronutils
operator|.
name|model
operator|.
name|definition
operator|.
name|CronDefinition
import|;
end_import

begin_import
import|import
name|com
operator|.
name|cronutils
operator|.
name|model
operator|.
name|definition
operator|.
name|CronDefinitionBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|cronutils
operator|.
name|parser
operator|.
name|CronParser
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
name|RepositoryCapabilities
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
name|UnsupportedFeatureException
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
name|*
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
name|features
operator|.
name|StagingRepositoryFeature
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|nio
operator|.
name|channels
operator|.
name|ReadableByteChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|WritableByteChannel
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
name|CopyOption
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
name|HashSet
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
name|Map
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_comment
comment|/**  * Implementation of a repository with the necessary fields for a bare repository.  * No features are provided. Capabilities and features must be implemented by concrete classes.  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepository
implements|implements
name|EditableRepository
implements|,
name|EventHandler
argument_list|<
name|RepositoryEvent
argument_list|>
block|{
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractRepository
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|AtomicBoolean
name|openStatus
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|RepositoryType
name|type
decl_stmt|;
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Locale
argument_list|,
name|String
argument_list|>
name|names
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Locale
argument_list|,
name|String
argument_list|>
name|descriptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|Locale
name|primaryLocale
init|=
operator|new
name|Locale
argument_list|(
literal|"en_US"
argument_list|)
decl_stmt|;
specifier|protected
name|URI
name|location
decl_stmt|;
specifier|private
name|URI
name|baseUri
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|URI
argument_list|>
name|failoverLocations
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|URI
argument_list|>
name|uFailoverLocations
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|failoverLocations
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|scanned
init|=
literal|true
decl_stmt|;
name|String
name|schedulingDefinition
init|=
literal|"0 0 02 * * ?"
decl_stmt|;
specifier|private
name|String
name|layout
init|=
literal|"default"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|CronDefinition
name|CRON_DEFINITION
init|=
name|CronDefinitionBuilder
operator|.
name|instanceDefinitionFor
argument_list|(
name|CronType
operator|.
name|QUARTZ
argument_list|)
decl_stmt|;
specifier|private
name|RepositoryState
name|state
decl_stmt|;
specifier|private
specifier|final
name|EventManager
name|eventManager
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|RepositoryFeature
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|RepositoryFeature
argument_list|<
name|?
argument_list|>
argument_list|>
name|featureMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|ArchivaIndexingContext
name|indexingContext
decl_stmt|;
specifier|private
name|RepositoryStorage
name|storage
decl_stmt|;
specifier|public
name|AbstractRepository
parameter_list|(
name|RepositoryType
name|type
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|RepositoryStorage
name|repositoryStorage
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|names
operator|.
name|put
argument_list|(
name|primaryLocale
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|storage
operator|=
name|repositoryStorage
expr_stmt|;
name|this
operator|.
name|location
operator|=
name|repositoryStorage
operator|.
name|getLocation
argument_list|()
expr_stmt|;
name|this
operator|.
name|openStatus
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
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
block|}
specifier|public
name|AbstractRepository
parameter_list|(
name|Locale
name|primaryLocale
parameter_list|,
name|RepositoryType
name|type
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|RepositoryStorage
name|repositoryStorage
parameter_list|)
block|{
name|setPrimaryLocale
argument_list|(
name|primaryLocale
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|names
operator|.
name|put
argument_list|(
name|primaryLocale
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|storage
operator|=
name|repositoryStorage
expr_stmt|;
name|this
operator|.
name|location
operator|=
name|repositoryStorage
operator|.
name|getLocation
argument_list|()
expr_stmt|;
name|this
operator|.
name|openStatus
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
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
block|}
specifier|protected
name|void
name|setPrimaryLocale
parameter_list|(
name|Locale
name|locale
parameter_list|)
block|{
name|this
operator|.
name|primaryLocale
operator|=
name|locale
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|( )
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|( )
block|{
return|return
name|getName
argument_list|(
name|primaryLocale
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|(
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|names
operator|.
name|get
argument_list|(
name|locale
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|( )
block|{
return|return
name|getDescription
argument_list|(
name|primaryLocale
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|(
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|descriptions
operator|.
name|get
argument_list|(
name|primaryLocale
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryType
name|getType
parameter_list|( )
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|getLocation
parameter_list|( )
block|{
return|return
name|location
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|getRoot
parameter_list|( )
block|{
return|return
name|storage
operator|.
name|getRoot
argument_list|( )
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|URI
argument_list|>
name|getFailoverLocations
parameter_list|( )
block|{
return|return
name|uFailoverLocations
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isScanned
parameter_list|( )
block|{
return|return
name|scanned
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getSchedulingDefinition
parameter_list|( )
block|{
return|return
name|schedulingDefinition
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|boolean
name|hasIndex
parameter_list|( )
function_decl|;
annotation|@
name|Override
specifier|public
name|String
name|getLayout
parameter_list|( )
block|{
return|return
name|layout
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|RepositoryCapabilities
name|getCapabilities
parameter_list|( )
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
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
block|{
if|if
condition|(
name|featureMap
operator|.
name|containsKey
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
operator|)
name|featureMap
operator|.
name|get
argument_list|(
name|clazz
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedFeatureException
argument_list|(
literal|"Feature "
operator|+
name|clazz
operator|+
literal|" not supported"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
return|return
name|featureMap
operator|.
name|containsKey
argument_list|(
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Locale
name|getPrimaryLocale
parameter_list|( )
block|{
return|return
name|primaryLocale
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setName
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|names
operator|.
name|put
argument_list|(
name|locale
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDescription
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|descriptions
operator|.
name|put
argument_list|(
name|locale
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLocation
parameter_list|(
specifier|final
name|URI
name|location
parameter_list|)
block|{
if|if
condition|(
name|location
operator|!=
literal|null
operator|&&
operator|(
name|this
operator|.
name|location
operator|==
literal|null
operator|||
operator|!
name|this
operator|.
name|location
operator|.
name|equals
argument_list|(
name|location
argument_list|)
operator|)
condition|)
block|{
try|try
block|{
name|updateLocation
argument_list|(
name|location
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
literal|"Could not update location of repository {} to {}"
argument_list|,
name|getId
argument_list|()
argument_list|,
name|location
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateLocation
parameter_list|(
name|URI
name|newLocation
parameter_list|)
throws|throws
name|IOException
block|{
name|storage
operator|.
name|updateLocation
argument_list|(
name|newLocation
argument_list|)
expr_stmt|;
name|this
operator|.
name|location
operator|=
name|newLocation
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addFailoverLocation
parameter_list|(
name|URI
name|location
parameter_list|)
block|{
name|this
operator|.
name|failoverLocations
operator|.
name|add
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeFailoverLocation
parameter_list|(
name|URI
name|location
parameter_list|)
block|{
name|this
operator|.
name|failoverLocations
operator|.
name|remove
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearFailoverLocations
parameter_list|( )
block|{
name|this
operator|.
name|failoverLocations
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setScanned
parameter_list|(
name|boolean
name|scanned
parameter_list|)
block|{
name|this
operator|.
name|scanned
operator|=
name|scanned
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLayout
parameter_list|(
name|String
name|layout
parameter_list|)
block|{
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setBaseUri
parameter_list|(
name|URI
name|baseUri
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|baseUri
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setSchedulingDefinition
parameter_list|(
name|String
name|cronExpression
parameter_list|)
block|{
name|this
operator|.
name|schedulingDefinition
operator|=
name|cronExpression
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|void
name|addFeature
parameter_list|(
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
name|feature
parameter_list|)
block|{
name|featureMap
operator|.
name|put
argument_list|(
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|RepositoryFeature
argument_list|<
name|?
argument_list|>
argument_list|>
operator|)
name|feature
operator|.
name|getClass
argument_list|()
argument_list|,
name|feature
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setIndexingContext
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|indexingContext
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ArchivaIndexingContext
name|getIndexingContext
parameter_list|()
block|{
return|return
name|indexingContext
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|openStatus
operator|.
name|compareAndSet
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|ArchivaIndexingContext
name|ctx
init|=
name|getIndexingContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctx
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ctx
operator|.
name|close
argument_list|()
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
name|warn
argument_list|(
literal|"Error during index context close."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|indexingContext
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|supportsFeature
argument_list|(
name|StagingRepositoryFeature
operator|.
name|class
argument_list|)
condition|)
block|{
name|StagingRepositoryFeature
name|sf
init|=
name|getFeature
argument_list|(
name|StagingRepositoryFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|sf
operator|.
name|getStagingRepository
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sf
operator|.
name|getStagingRepository
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|setLastState
argument_list|(
name|RepositoryState
operator|.
name|CLOSED
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
name|openStatus
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|RepositoryEvent
name|event
parameter_list|)
block|{
comment|// We just rethrow the events
name|eventManager
operator|.
name|fireEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|Event
parameter_list|>
name|void
name|registerEventHandler
parameter_list|(
name|EventType
argument_list|<
name|T
argument_list|>
name|eventType
parameter_list|,
name|EventHandler
argument_list|<
name|?
super|super
name|T
argument_list|>
name|eventHandler
parameter_list|)
block|{
if|if
condition|(
operator|!
name|EventType
operator|.
name|isInstanceOf
argument_list|(
name|eventType
argument_list|,
name|RepositoryEvent
operator|.
name|ANY
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can only register RepositoryEvent Handlers"
argument_list|)
throw|;
block|}
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|eventType
argument_list|,
name|eventHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|Event
parameter_list|>
name|void
name|unregisterEventHandler
parameter_list|(
name|EventType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|EventHandler
argument_list|<
name|?
super|super
name|T
argument_list|>
name|eventHandler
parameter_list|)
block|{
name|eventManager
operator|.
name|unregisterEventHandler
argument_list|(
name|type
argument_list|,
name|eventHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|getAsset
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|storage
operator|.
name|getAsset
argument_list|(
name|path
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|addAsset
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|container
parameter_list|)
block|{
return|return
name|storage
operator|.
name|addAsset
argument_list|(
name|path
argument_list|,
name|container
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAsset
parameter_list|(
name|StorageAsset
name|asset
parameter_list|)
throws|throws
name|IOException
block|{
name|storage
operator|.
name|removeAsset
argument_list|(
name|asset
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|moveAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|String
name|destination
parameter_list|,
name|CopyOption
modifier|...
name|copyOptions
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|storage
operator|.
name|moveAsset
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|moveAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|StorageAsset
name|destination
parameter_list|,
name|CopyOption
modifier|...
name|copyOptions
parameter_list|)
throws|throws
name|IOException
block|{
name|storage
operator|.
name|moveAsset
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|,
name|copyOptions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|copyAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|String
name|destination
parameter_list|,
name|CopyOption
modifier|...
name|copyOptions
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|storage
operator|.
name|copyAsset
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|copyAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|StorageAsset
name|destination
parameter_list|,
name|CopyOption
modifier|...
name|copyOptions
parameter_list|)
throws|throws
name|IOException
block|{
name|storage
operator|.
name|copyAsset
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|,
name|copyOptions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|consumeData
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|InputStream
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|readLock
parameter_list|)
throws|throws
name|IOException
block|{
name|storage
operator|.
name|consumeData
argument_list|(
name|asset
argument_list|,
name|consumerFunction
argument_list|,
name|readLock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|consumeDataFromChannel
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|ReadableByteChannel
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|readLock
parameter_list|)
throws|throws
name|IOException
block|{
name|storage
operator|.
name|consumeDataFromChannel
argument_list|(
name|asset
argument_list|,
name|consumerFunction
argument_list|,
name|readLock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeData
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|OutputStream
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|writeLock
parameter_list|)
throws|throws
name|IOException
block|{
name|storage
operator|.
name|writeData
argument_list|(
name|asset
argument_list|,
name|consumerFunction
argument_list|,
name|writeLock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeDataToChannel
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|WritableByteChannel
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|writeLock
parameter_list|)
throws|throws
name|IOException
block|{
name|storage
operator|.
name|writeDataToChannel
argument_list|(
name|asset
argument_list|,
name|consumerFunction
argument_list|,
name|writeLock
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setStorage
parameter_list|(
name|RepositoryStorage
name|storage
parameter_list|)
block|{
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
block|}
specifier|protected
name|RepositoryStorage
name|getStorage
parameter_list|()
block|{
return|return
name|storage
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryState
name|getLastState
parameter_list|( )
block|{
return|return
name|this
operator|.
name|state
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLastState
parameter_list|(
name|RepositoryState
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
block|}
end_class

end_unit

