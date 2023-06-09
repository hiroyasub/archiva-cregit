begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|repository
operator|.
name|metadata
operator|.
name|storage
operator|.
name|mock
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
name|provider
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
name|model
operator|.
name|ArchivaRuntimeConfiguration
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
name|ConfigurationListener
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
name|FileType
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
name|RepositoryScanningConfiguration
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
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|HashSet
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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_comment
comment|/**  * MockConfiguration   *  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaConfiguration#mock"
argument_list|)
specifier|public
class|class
name|MockConfiguration
implements|implements
name|ArchivaConfiguration
block|{
specifier|private
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|RegistryListener
argument_list|>
name|registryListeners
init|=
operator|new
name|HashSet
argument_list|<
name|RegistryListener
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|ConfigurationListener
argument_list|>
name|configListeners
init|=
operator|new
name|HashSet
argument_list|<
name|ConfigurationListener
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Registry
name|registryMock
decl_stmt|;
specifier|public
name|MockConfiguration
parameter_list|()
block|{
name|registryMock
operator|=
name|mock
argument_list|(
name|Registry
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setArchivaRuntimeConfiguration
argument_list|(
operator|new
name|ArchivaRuntimeConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|addChecksumType
argument_list|(
literal|"sha1"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|addChecksumType
argument_list|(
literal|"sha256"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|addChecksumType
argument_list|(
literal|"md5"
argument_list|)
expr_stmt|;
name|RepositoryScanningConfiguration
name|rpsc
init|=
operator|new
name|RepositoryScanningConfiguration
argument_list|( )
decl_stmt|;
name|FileType
name|ft
init|=
operator|new
name|FileType
argument_list|( )
decl_stmt|;
name|ft
operator|.
name|setId
argument_list|(
literal|"artifacts"
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|plist
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|( )
decl_stmt|;
name|plist
operator|.
name|add
argument_list|(
literal|"**/*.jar"
argument_list|)
expr_stmt|;
name|plist
operator|.
name|add
argument_list|(
literal|"**/*.pom"
argument_list|)
expr_stmt|;
name|plist
operator|.
name|add
argument_list|(
literal|"**/*.war"
argument_list|)
expr_stmt|;
name|ft
operator|.
name|setPatterns
argument_list|(
name|plist
argument_list|)
expr_stmt|;
name|rpsc
operator|.
name|addFileType
argument_list|(
name|ft
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|FileType
argument_list|>
name|ftList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|( )
decl_stmt|;
name|ftList
operator|.
name|add
argument_list|(
name|ft
argument_list|)
expr_stmt|;
name|rpsc
operator|.
name|setFileTypes
argument_list|(
name|ftList
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRepositoryScanning
argument_list|(
name|rpsc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addChangeListener
parameter_list|(
name|RegistryListener
name|listener
parameter_list|)
block|{
name|registryListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeChangeListener
parameter_list|(
name|RegistryListener
name|listener
parameter_list|)
block|{
name|registryListeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|save
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RegistryException
block|{
comment|/* do nothing */
block|}
annotation|@
name|Override
specifier|public
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
block|{
comment|// do nothing
block|}
specifier|public
name|void
name|triggerChange
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
for|for
control|(
name|RegistryListener
name|listener
range|:
name|registryListeners
control|)
block|{
try|try
block|{
name|listener
operator|.
name|afterConfigurationChange
argument_list|(
name|registryMock
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addListener
parameter_list|(
name|ConfigurationListener
name|listener
parameter_list|)
block|{
name|configListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeListener
parameter_list|(
name|ConfigurationListener
name|listener
parameter_list|)
block|{
name|configListeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDefaulted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|reload
parameter_list|()
block|{
comment|// no op
block|}
annotation|@
name|Override
specifier|public
name|Locale
name|getDefaultLocale
parameter_list|( )
block|{
return|return
name|Locale
operator|.
name|getDefault
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Locale
operator|.
name|LanguageRange
argument_list|>
name|getLanguagePriorities
parameter_list|( )
block|{
return|return
name|Locale
operator|.
name|LanguageRange
operator|.
name|parse
argument_list|(
literal|"en,fr,de"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Path
name|getAppServerBaseDir
parameter_list|()
block|{
if|if
condition|(
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"appserver.base"
argument_list|)
condition|)
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"appserver.base"
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Path
name|getRepositoryBaseDir
parameter_list|()
block|{
return|return
name|getDataDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"repositories"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Path
name|getRemoteRepositoryBaseDir
parameter_list|()
block|{
return|return
name|getDataDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"remotes"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Path
name|getRepositoryGroupBaseDir
parameter_list|()
block|{
return|return
name|getDataDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"groups"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Path
name|getDataDirectory
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getDataDirectory
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
name|configuration
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getDataDirectory
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getAppServerBaseDir
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"data"
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Registry
name|getRegistry
parameter_list|( )
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

