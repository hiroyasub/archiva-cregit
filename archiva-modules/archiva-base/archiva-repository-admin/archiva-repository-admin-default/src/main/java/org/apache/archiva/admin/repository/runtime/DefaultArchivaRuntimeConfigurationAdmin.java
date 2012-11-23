begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
operator|.
name|runtime
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
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|beans
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
name|admin
operator|.
name|model
operator|.
name|runtime
operator|.
name|ArchivaRuntimeConfigurationAdmin
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
name|admin
operator|.
name|repository
operator|.
name|AbstractRepositoryAdmin
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
name|configuration
operator|.
name|RuntimeConfiguration
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
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M4  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaRuntimeConfigurationAdmin#default"
argument_list|)
specifier|public
class|class
name|DefaultArchivaRuntimeConfigurationAdmin
extends|extends
name|AbstractRepositoryAdmin
implements|implements
name|ArchivaRuntimeConfigurationAdmin
block|{
specifier|public
name|ArchivaRuntimeConfiguration
name|getArchivaRuntimeConfigurationAdmin
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
name|build
argument_list|(
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRuntimeConfiguration
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|updateArchivaRuntimeConfiguration
parameter_list|(
name|ArchivaRuntimeConfiguration
name|archivaRuntimeConfiguration
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|RuntimeConfiguration
name|runtimeConfiguration
init|=
name|build
argument_list|(
name|archivaRuntimeConfiguration
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setRuntimeConfiguration
argument_list|(
name|runtimeConfiguration
argument_list|)
expr_stmt|;
try|try
block|{
name|getArchivaConfiguration
argument_list|()
operator|.
name|save
argument_list|(
name|configuration
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
name|RepositoryAdminException
argument_list|(
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
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|ArchivaRuntimeConfiguration
name|build
parameter_list|(
name|RuntimeConfiguration
name|runtimeConfiguration
parameter_list|)
block|{
name|ArchivaRuntimeConfiguration
name|archivaRuntimeConfiguration
init|=
operator|new
name|ArchivaRuntimeConfiguration
argument_list|()
decl_stmt|;
name|archivaRuntimeConfiguration
operator|.
name|setUserManagerImpl
argument_list|(
name|runtimeConfiguration
operator|.
name|getUserManagerImpl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|archivaRuntimeConfiguration
return|;
block|}
specifier|private
name|RuntimeConfiguration
name|build
parameter_list|(
name|ArchivaRuntimeConfiguration
name|archivaRuntimeConfiguration
parameter_list|)
block|{
name|RuntimeConfiguration
name|runtimeConfiguration
init|=
operator|new
name|RuntimeConfiguration
argument_list|()
decl_stmt|;
name|runtimeConfiguration
operator|.
name|setUserManagerImpl
argument_list|(
name|archivaRuntimeConfiguration
operator|.
name|getUserManagerImpl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|runtimeConfiguration
return|;
block|}
block|}
end_class

end_unit

