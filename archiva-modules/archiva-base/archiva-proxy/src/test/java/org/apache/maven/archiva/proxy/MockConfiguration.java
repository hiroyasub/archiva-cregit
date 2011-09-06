begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|proxy
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
name|maven
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
name|maven
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileTypes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|RepositoryScanningConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
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
name|codehaus
operator|.
name|plexus
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
name|codehaus
operator|.
name|plexus
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
name|easymock
operator|.
name|MockControl
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
name|Set
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

begin_comment
comment|/**  * MockConfiguration  *  * @version $Id$  */
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
name|MockControl
name|registryControl
decl_stmt|;
specifier|private
name|Registry
name|registryMock
decl_stmt|;
specifier|public
name|MockConfiguration
parameter_list|()
block|{
name|registryControl
operator|=
name|MockControl
operator|.
name|createNiceControl
argument_list|(
name|Registry
operator|.
name|class
argument_list|)
expr_stmt|;
name|registryMock
operator|=
operator|(
name|Registry
operator|)
name|registryControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|Exception
block|{
name|configuration
operator|.
name|setRepositoryScanning
argument_list|(
operator|new
name|RepositoryScanningConfiguration
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|FileType
argument_list|>
name|getFileTypes
parameter_list|()
block|{
name|FileType
name|fileType
init|=
operator|new
name|FileType
argument_list|()
decl_stmt|;
name|fileType
operator|.
name|setId
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|)
expr_stmt|;
name|fileType
operator|.
name|setPatterns
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"**/*"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|fileType
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
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
specifier|public
name|boolean
name|isDefaulted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|reload
parameter_list|()
block|{
comment|// no op
block|}
block|}
end_class

end_unit

