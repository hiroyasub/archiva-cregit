begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|appearance
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
name|admin
operator|.
name|repository
operator|.
name|admin
operator|.
name|DefaultArchivaAdministration
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
name|web
operator|.
name|action
operator|.
name|AbstractWebworkTestCase
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
name|junit
operator|.
name|Before
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOrganizationInfoActionTest
extends|extends
name|AbstractWebworkTestCase
block|{
specifier|protected
name|MockControl
name|archivaConfigurationControl
decl_stmt|;
specifier|protected
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|protected
name|AbstractAppearanceAction
name|action
decl_stmt|;
specifier|protected
name|Configuration
name|config
decl_stmt|;
specifier|protected
specifier|abstract
name|AbstractAppearanceAction
name|getAction
parameter_list|()
function_decl|;
annotation|@
name|Before
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|config
operator|=
operator|new
name|Configuration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|archivaConfigurationControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|config
argument_list|,
literal|1
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setVoidCallable
argument_list|(
literal|1
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|DefaultArchivaAdministration
name|defaultArchivaAdministration
init|=
operator|new
name|DefaultArchivaAdministration
argument_list|()
decl_stmt|;
name|defaultArchivaAdministration
operator|.
name|setArchivaConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|getAction
argument_list|()
operator|.
name|setArchivaAdministration
argument_list|(
name|defaultArchivaAdministration
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|reloadAction
parameter_list|()
block|{
name|action
operator|=
name|getAction
argument_list|()
expr_stmt|;
operator|(
operator|(
name|DefaultArchivaAdministration
operator|)
name|action
operator|.
name|getArchivaAdministration
argument_list|()
operator|)
operator|.
name|setArchivaConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

