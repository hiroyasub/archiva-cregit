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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaConfiguration#mocked"
argument_list|)
specifier|public
class|class
name|StubConfiguration
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
name|StubConfiguration
parameter_list|()
block|{
name|configuration
operator|.
name|setRepositoryScanning
argument_list|(
operator|new
name|RepositoryScanningConfiguration
argument_list|()
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
throws|,
name|IndeterminateConfigurationException
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
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
name|addListener
parameter_list|(
name|ConfigurationListener
name|listener
parameter_list|)
block|{
comment|// throw new UnsupportedOperationException();
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
comment|// throw new UnsupportedOperationException();
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
parameter_list|( )
block|{
return|return
name|getDataDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"group"
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
name|configuration
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
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
name|Path
name|dataDir
init|=
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
decl_stmt|;
if|if
condition|(
name|dataDir
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
return|return
name|dataDir
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
name|dataDir
argument_list|)
return|;
block|}
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
block|}
end_class

end_unit

