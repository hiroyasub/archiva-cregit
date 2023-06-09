begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
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
name|beans
operator|.
name|RedbackRuntimeConfiguration
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|RBACManagerImplementationInformation
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|UserManagerImplementationInformation
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|RedbackRuntimeConfigurationService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|RedbackRuntimeConfigurationServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|nonnullConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|RedbackRuntimeConfiguration
name|redbackRuntimeConfiguration
init|=
name|getRedbackRuntimeConfigurationService
argument_list|()
operator|.
name|getRedbackRuntimeConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jpa"
argument_list|,
name|redbackRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|update
parameter_list|()
throws|throws
name|Exception
block|{
name|RedbackRuntimeConfiguration
name|redbackRuntimeConfiguration
init|=
name|getRedbackRuntimeConfigurationService
argument_list|()
operator|.
name|getRedbackRuntimeConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jpa"
argument_list|,
name|redbackRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|redbackRuntimeConfiguration
operator|.
name|setUserManagerImpls
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|getRedbackRuntimeConfigurationService
argument_list|()
operator|.
name|updateRedbackRuntimeConfiguration
argument_list|(
name|redbackRuntimeConfiguration
argument_list|)
expr_stmt|;
name|redbackRuntimeConfiguration
operator|=
name|getRedbackRuntimeConfigurationService
argument_list|()
operator|.
name|getRedbackRuntimeConfiguration
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|redbackRuntimeConfiguration
operator|.
name|getUserManagerImpls
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|usermanagersinformations
parameter_list|()
throws|throws
name|Exception
block|{
name|RedbackRuntimeConfigurationService
name|service
init|=
name|getRedbackRuntimeConfigurationService
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|UserManagerImplementationInformation
argument_list|>
name|infos
init|=
name|service
operator|.
name|getUserManagerImplementationInformations
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|infos
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserManagerImplementationInformation
argument_list|(
literal|"jpa"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|rbacmanagersinformations
parameter_list|()
throws|throws
name|Exception
block|{
name|RedbackRuntimeConfigurationService
name|service
init|=
name|getRedbackRuntimeConfigurationService
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RBACManagerImplementationInformation
argument_list|>
name|infos
init|=
name|service
operator|.
name|getRbacManagerImplementationInformations
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|infos
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|RBACManagerImplementationInformation
argument_list|(
literal|"jpa"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

