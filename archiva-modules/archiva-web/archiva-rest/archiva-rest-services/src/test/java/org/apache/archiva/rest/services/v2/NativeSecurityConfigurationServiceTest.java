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
operator|.
name|v2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|restassured
operator|.
name|response
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|AfterAll
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeAll
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|DisplayName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|MethodOrderer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Tag
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|TestInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|TestMethodOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|extension
operator|.
name|ExtendWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|SpringExtension
import|;
end_import

begin_import
import|import static
name|io
operator|.
name|restassured
operator|.
name|RestAssured
operator|.
name|given
import|;
end_import

begin_import
import|import static
name|io
operator|.
name|restassured
operator|.
name|http
operator|.
name|ContentType
operator|.
name|JSON
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|TestInstance
argument_list|(
name|TestInstance
operator|.
name|Lifecycle
operator|.
name|PER_CLASS
argument_list|)
annotation|@
name|Tag
argument_list|(
literal|"rest-native"
argument_list|)
annotation|@
name|TestMethodOrder
argument_list|(
name|MethodOrderer
operator|.
name|Random
operator|.
name|class
argument_list|)
annotation|@
name|DisplayName
argument_list|(
literal|"Native REST tests for V2 SecurityConfigurationService"
argument_list|)
specifier|public
class|class
name|NativeSecurityConfigurationServiceTest
extends|extends
name|AbstractNativeRestServices
block|{
annotation|@
name|Override
specifier|protected
name|String
name|getServicePath
parameter_list|( )
block|{
return|return
literal|"/security"
return|;
block|}
annotation|@
name|BeforeAll
name|void
name|setup
parameter_list|( )
throws|throws
name|Exception
block|{
name|super
operator|.
name|setupNative
argument_list|( )
expr_stmt|;
block|}
annotation|@
name|AfterAll
name|void
name|destroy
parameter_list|( )
throws|throws
name|Exception
block|{
name|super
operator|.
name|shutdownNative
argument_list|( )
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testGetConfiguration
parameter_list|()
block|{
name|String
name|token
init|=
name|getAdminToken
argument_list|( )
decl_stmt|;
name|Response
name|response
init|=
name|given
argument_list|( )
operator|.
name|spec
argument_list|(
name|getRequestSpec
argument_list|(
name|token
argument_list|)
argument_list|)
operator|.
name|contentType
argument_list|(
name|JSON
argument_list|)
operator|.
name|when
argument_list|( )
operator|.
name|get
argument_list|(
literal|"config"
argument_list|)
operator|.
name|prettyPeek
argument_list|()
operator|.
name|then
argument_list|( )
operator|.
name|statusCode
argument_list|(
literal|200
argument_list|)
operator|.
name|extract
argument_list|( )
operator|.
name|response
argument_list|( )
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jpa"
argument_list|,
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getString
argument_list|(
literal|"active_user_managers[0]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jpa"
argument_list|,
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getString
argument_list|(
literal|"active_rbac_managers[0]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"memory"
argument_list|,
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getString
argument_list|(
literal|"properties.\"authentication.jwt.keystoreType\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"10"
argument_list|,
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getString
argument_list|(
literal|"properties.\"security.policy.allowed.login.attempt\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getBoolean
argument_list|(
literal|"user_cache_enabled"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getBoolean
argument_list|(
literal|"ldap_active"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

