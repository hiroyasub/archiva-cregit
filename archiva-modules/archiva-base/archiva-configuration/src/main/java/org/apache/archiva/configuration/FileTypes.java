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
name|common
operator|.
name|FileTypeUtils
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
name|functors
operator|.
name|FiletypeSelectionPredicate
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
name|io
operator|.
name|registry
operator|.
name|ConfigurationRegistryReader
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
name|redback
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
name|redback
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
name|redback
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
name|redback
operator|.
name|components
operator|.
name|registry
operator|.
name|commons
operator|.
name|CommonsConfigurationRegistry
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
name|collections4
operator|.
name|CollectionUtils
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
name|collections4
operator|.
name|Predicate
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
name|configuration
operator|.
name|CombinedConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|selectors
operator|.
name|SelectorUtils
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
name|inject
operator|.
name|Inject
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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * FileTypes  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"fileTypes"
argument_list|)
specifier|public
class|class
name|FileTypes
implements|implements
name|RegistryListener
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ARTIFACTS
init|=
literal|"artifacts"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTO_REMOVE
init|=
literal|"auto-remove"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INDEXABLE_CONTENT
init|=
literal|"indexable-content"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IGNORED
init|=
literal|"ignored"
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration#default"
argument_list|)
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * Map of default values for the file types.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|defaultTypeMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|artifactPatterns
decl_stmt|;
comment|/**      * Default exclusions from artifact consumers that are using the file types. Note that this is simplistic in the      * case of the support files (based on extension) as it is elsewhere - it may be better to match these to actual      * artifacts and exclude later during scanning.      *      * @deprecated      */
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_EXCLUSIONS
init|=
name|FileTypeUtils
operator|.
name|DEFAULT_EXCLUSIONS
decl_stmt|;
specifier|public
name|void
name|setArchivaConfiguration
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
block|{
name|this
operator|.
name|archivaConfiguration
operator|=
name|archivaConfiguration
expr_stmt|;
block|}
comment|/**      * Get the list of patterns for a specified filetype.      * You will always get a list.  In this order.      *<ul>      *<li>The Configured List</li>      *<li>The Default List</li>      *<li>A single item list of<code>&quot;**&#47;*&quot;</code></li>      *</ul>      *      * @param id the id to lookup.      * @return the list of patterns.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFileTypePatterns
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|Predicate
name|selectedFiletype
init|=
operator|new
name|FiletypeSelectionPredicate
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|RepositoryScanningConfiguration
name|repositoryScanningConfiguration
init|=
name|config
operator|.
name|getRepositoryScanning
argument_list|()
decl_stmt|;
if|if
condition|(
name|repositoryScanningConfiguration
operator|!=
literal|null
condition|)
block|{
name|FileType
name|filetype
init|=
operator|(
name|FileType
operator|)
name|CollectionUtils
operator|.
name|find
argument_list|(
name|config
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getFileTypes
argument_list|()
argument_list|,
name|selectedFiletype
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|filetype
operator|!=
literal|null
operator|)
operator|&&
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|filetype
operator|.
name|getPatterns
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|filetype
operator|.
name|getPatterns
argument_list|()
return|;
block|}
block|}
name|List
argument_list|<
name|String
argument_list|>
name|defaultPatterns
init|=
name|defaultTypeMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|defaultPatterns
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"**/*"
argument_list|)
return|;
block|}
return|return
name|defaultPatterns
return|;
block|}
specifier|public
specifier|synchronized
name|boolean
name|matchesArtifactPattern
parameter_list|(
name|String
name|relativePath
parameter_list|)
block|{
comment|// Correct the slash pattern.
name|relativePath
operator|=
name|relativePath
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifactPatterns
operator|==
literal|null
condition|)
block|{
name|artifactPatterns
operator|=
name|getFileTypePatterns
argument_list|(
name|ARTIFACTS
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|pattern
range|:
name|artifactPatterns
control|)
block|{
if|if
condition|(
name|SelectorUtils
operator|.
name|matchPath
argument_list|(
name|pattern
argument_list|,
name|relativePath
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// Found match
return|return
literal|true
return|;
block|}
block|}
comment|// No match.
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|matchesDefaultExclusions
parameter_list|(
name|String
name|relativePath
parameter_list|)
block|{
comment|// Correct the slash pattern.
name|relativePath
operator|=
name|relativePath
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|pattern
range|:
name|DEFAULT_EXCLUSIONS
control|)
block|{
if|if
condition|(
name|SelectorUtils
operator|.
name|matchPath
argument_list|(
name|pattern
argument_list|,
name|relativePath
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// Found match
return|return
literal|true
return|;
block|}
block|}
comment|// No match.
return|return
literal|false
return|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
comment|// TODO: why is this done by hand?
comment|// TODO: ideally, this would be instantiated by configuration instead, and not need to be a component
name|String
name|errMsg
init|=
literal|"Unable to load default archiva configuration for FileTypes: "
decl_stmt|;
try|try
block|{
name|CommonsConfigurationRegistry
name|commonsRegistry
init|=
operator|new
name|CommonsConfigurationRegistry
argument_list|()
decl_stmt|;
comment|// Configure commonsRegistry
name|Field
name|fld
init|=
name|commonsRegistry
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"configuration"
argument_list|)
decl_stmt|;
name|fld
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fld
operator|.
name|set
argument_list|(
name|commonsRegistry
argument_list|,
operator|new
name|CombinedConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|commonsRegistry
operator|.
name|addConfigurationFromResource
argument_list|(
literal|"org/apache/archiva/configuration/default-archiva.xml"
argument_list|)
expr_stmt|;
comment|// Read configuration as it was intended.
name|ConfigurationRegistryReader
name|configReader
init|=
operator|new
name|ConfigurationRegistryReader
argument_list|()
decl_stmt|;
name|Configuration
name|defaultConfig
init|=
name|configReader
operator|.
name|read
argument_list|(
name|commonsRegistry
argument_list|)
decl_stmt|;
name|initialiseTypeMap
argument_list|(
name|defaultConfig
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|errMsg
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|errMsg
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|errMsg
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|errMsg
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|errMsg
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|this
operator|.
name|archivaConfiguration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initialiseTypeMap
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
name|defaultTypeMap
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// Store the default file type declaration.
name|List
argument_list|<
name|FileType
argument_list|>
name|filetypes
init|=
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getFileTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|FileType
name|filetype
range|:
name|filetypes
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|patterns
init|=
name|defaultTypeMap
operator|.
name|get
argument_list|(
name|filetype
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|patterns
operator|==
literal|null
condition|)
block|{
name|patterns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|filetype
operator|.
name|getPatterns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|patterns
operator|.
name|addAll
argument_list|(
name|filetype
operator|.
name|getPatterns
argument_list|()
argument_list|)
expr_stmt|;
name|defaultTypeMap
operator|.
name|put
argument_list|(
name|filetype
operator|.
name|getId
argument_list|()
argument_list|,
name|patterns
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|propertyName
operator|.
name|contains
argument_list|(
literal|"fileType"
argument_list|)
condition|)
block|{
name|artifactPatterns
operator|=
literal|null
expr_stmt|;
name|initialiseTypeMap
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* nothing to do */
block|}
block|}
end_class

end_unit

