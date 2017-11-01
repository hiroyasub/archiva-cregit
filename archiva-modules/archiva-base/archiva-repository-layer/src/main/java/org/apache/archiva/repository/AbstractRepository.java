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
name|repository
operator|.
name|features
operator|.
name|RepositoryFeature
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
block|{
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
specifier|private
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
specifier|public
name|URI
name|getAbsoluteLocation
parameter_list|()
block|{
return|return
name|baseUri
operator|.
name|resolve
argument_list|(
name|location
argument_list|)
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
name|URI
name|location
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|location
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
name|CronParser
name|parser
init|=
operator|new
name|CronParser
argument_list|(
name|CRON_DEFINITION
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|cronExpression
argument_list|)
operator|.
name|validate
argument_list|()
expr_stmt|;
name|this
operator|.
name|schedulingDefinition
operator|=
name|cronExpression
expr_stmt|;
block|}
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
block|}
end_class

end_unit
