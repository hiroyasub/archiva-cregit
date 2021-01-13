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
name|apache
operator|.
name|archiva
operator|.
name|components
operator|.
name|rest
operator|.
name|model
operator|.
name|PagedResult
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
name|rest
operator|.
name|model
operator|.
name|PropertyEntry
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
name|v2
operator|.
name|BeanInformation
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
name|v2
operator|.
name|CacheConfiguration
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
name|v2
operator|.
name|LdapConfiguration
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
annotation|@
name|Test
name|void
name|testGetConfigurationProperties
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
literal|"config/properties"
argument_list|)
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
name|PagedResult
argument_list|<
name|PropertyEntry
argument_list|>
name|result
init|=
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getObject
argument_list|(
literal|""
argument_list|,
name|PagedResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|propList
init|=
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getList
argument_list|(
literal|"data"
argument_list|,
name|PropertyEntry
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|result
operator|.
name|getPagination
argument_list|( )
operator|.
name|getLimit
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|result
operator|.
name|getPagination
argument_list|( )
operator|.
name|getOffset
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|47
argument_list|,
name|result
operator|.
name|getPagination
argument_list|( )
operator|.
name|getTotalCount
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"authentication.jwt.keystoreType"
argument_list|,
name|propList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|=
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
name|queryParam
argument_list|(
literal|"offset"
argument_list|,
literal|"3"
argument_list|)
operator|.
name|queryParam
argument_list|(
literal|"limit"
argument_list|,
literal|"5"
argument_list|)
operator|.
name|get
argument_list|(
literal|"config/properties"
argument_list|)
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
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|result
operator|=
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getObject
argument_list|(
literal|""
argument_list|,
name|PagedResult
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|result
operator|.
name|getPagination
argument_list|( )
operator|.
name|getLimit
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|47
argument_list|,
name|result
operator|.
name|getPagination
argument_list|( )
operator|.
name|getTotalCount
argument_list|( )
argument_list|)
expr_stmt|;
name|propList
operator|=
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getList
argument_list|(
literal|"data"
argument_list|,
name|PropertyEntry
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"authentication.jwt.refreshLifetimeMs"
argument_list|,
name|propList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testGetLdapConfiguration
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
literal|"config/ldap"
argument_list|)
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
literal|""
argument_list|,
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|get
argument_list|(
literal|"host_name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getMap
argument_list|(
literal|"properties"
argument_list|)
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testUpdateLdapConfiguration
parameter_list|()
block|{
name|String
name|token
init|=
name|getAdminToken
argument_list|( )
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jsonMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"host_name"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
literal|389
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"ssl_enabled"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"writable"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"base_dn"
argument_list|,
literal|"dc=apache,dc=org"
argument_list|)
expr_stmt|;
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
name|body
argument_list|(
name|jsonMap
argument_list|)
operator|.
name|put
argument_list|(
literal|"config/ldap"
argument_list|)
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
name|response
operator|=
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
literal|"config/ldap"
argument_list|)
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
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|LdapConfiguration
name|config
init|=
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getObject
argument_list|(
literal|""
argument_list|,
name|LdapConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|config
operator|.
name|getHostName
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|389
argument_list|,
name|config
operator|.
name|getPort
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|config
operator|.
name|isSslEnabled
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|config
operator|.
name|isWritable
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dc=apache,dc=org"
argument_list|,
name|config
operator|.
name|getBaseDn
argument_list|( )
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jsonMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"host_name"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"ssl_enabled"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"base_dn"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"writable"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
name|body
argument_list|(
name|jsonMap
argument_list|)
operator|.
name|put
argument_list|(
literal|"config/ldap"
argument_list|)
operator|.
name|then
argument_list|( )
operator|.
name|statusCode
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
name|void
name|testGetCacheConfiguration
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
literal|"config/cache"
argument_list|)
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
block|}
annotation|@
name|Test
name|void
name|testUpdateCacheConfiguration
parameter_list|()
block|{
name|String
name|token
init|=
name|getAdminToken
argument_list|( )
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jsonMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"time_to_idle_seconds"
argument_list|,
literal|1600
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"time_to_live_seconds"
argument_list|,
literal|12000
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"max_entries_in_memory"
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"max_entries_on_disk"
argument_list|,
literal|400
argument_list|)
expr_stmt|;
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
name|body
argument_list|(
name|jsonMap
argument_list|)
operator|.
name|put
argument_list|(
literal|"config/cache"
argument_list|)
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
name|response
operator|=
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
literal|"config/cache"
argument_list|)
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
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|CacheConfiguration
name|config
init|=
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getObject
argument_list|(
literal|""
argument_list|,
name|CacheConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1600
argument_list|,
name|config
operator|.
name|getTimeToIdleSeconds
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|12000
argument_list|,
name|config
operator|.
name|getTimeToLiveSeconds
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|config
operator|.
name|getMaxEntriesInMemory
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|400
argument_list|,
name|config
operator|.
name|getMaxEntriesOnDisk
argument_list|( )
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jsonMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"time_to_idle_seconds"
argument_list|,
literal|1800
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"time_to_live_seconds"
argument_list|,
literal|14400
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"max_entries_in_memory"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"max_entries_on_disk"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
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
name|body
argument_list|(
name|jsonMap
argument_list|)
operator|.
name|put
argument_list|(
literal|"config/cache"
argument_list|)
operator|.
name|then
argument_list|( )
operator|.
name|statusCode
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
name|void
name|testGetUserManagers
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
literal|"user_managers"
argument_list|)
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
name|List
argument_list|<
name|BeanInformation
argument_list|>
name|usrList
init|=
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getList
argument_list|(
literal|""
argument_list|,
name|BeanInformation
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|usrList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|usrList
operator|.
name|stream
argument_list|( )
operator|.
name|anyMatch
argument_list|(
name|bi
lambda|->
literal|"LDAP User Manager"
operator|.
name|equals
argument_list|(
name|bi
operator|.
name|getDisplayName
argument_list|( )
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|usrList
operator|.
name|stream
argument_list|( )
operator|.
name|anyMatch
argument_list|(
name|bi
lambda|->
literal|"Database User Manager"
operator|.
name|equals
argument_list|(
name|bi
operator|.
name|getDisplayName
argument_list|( )
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testGetRbacManagers
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
literal|"rbac_managers"
argument_list|)
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
name|List
argument_list|<
name|BeanInformation
argument_list|>
name|rbacList
init|=
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getList
argument_list|(
literal|""
argument_list|,
name|BeanInformation
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rbacList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rbacList
operator|.
name|stream
argument_list|( )
operator|.
name|anyMatch
argument_list|(
name|bi
lambda|->
literal|"Database RBAC Manager"
operator|.
name|equals
argument_list|(
name|bi
operator|.
name|getDisplayName
argument_list|( )
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rbacList
operator|.
name|stream
argument_list|( )
operator|.
name|anyMatch
argument_list|(
name|bi
lambda|->
literal|"LDAP RBAC Manager"
operator|.
name|equals
argument_list|(
name|bi
operator|.
name|getDisplayName
argument_list|( )
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testUpdateConfiguration
parameter_list|()
block|{
name|String
name|token
init|=
name|getAdminToken
argument_list|( )
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jsonAsMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|jsonAsMap
operator|.
name|put
argument_list|(
literal|"active_user_managers"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"jpa"
argument_list|,
literal|"ldap"
argument_list|)
argument_list|)
expr_stmt|;
name|jsonAsMap
operator|.
name|put
argument_list|(
literal|"active_rbac_managers"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"jpa"
argument_list|)
argument_list|)
expr_stmt|;
name|jsonAsMap
operator|.
name|put
argument_list|(
literal|"user_cache_enabled"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|jsonAsMap
operator|.
name|put
argument_list|(
literal|"ldap_active"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
name|body
argument_list|(
name|jsonAsMap
argument_list|)
operator|.
name|put
argument_list|(
literal|"config"
argument_list|)
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
name|response
operator|=
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
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|response
operator|.
name|getBody
argument_list|( )
operator|.
name|jsonPath
argument_list|( )
operator|.
name|getList
argument_list|(
literal|"active_user_managers"
argument_list|)
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jsonAsMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|jsonAsMap
operator|.
name|put
argument_list|(
literal|"active_user_managers"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"jpa"
argument_list|)
argument_list|)
expr_stmt|;
name|jsonAsMap
operator|.
name|put
argument_list|(
literal|"active_rbac_managers"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"jpa"
argument_list|)
argument_list|)
expr_stmt|;
name|jsonAsMap
operator|.
name|put
argument_list|(
literal|"user_cache_enabled"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|jsonAsMap
operator|.
name|put
argument_list|(
literal|"ldap_active"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
name|body
argument_list|(
name|jsonAsMap
argument_list|)
operator|.
name|put
argument_list|(
literal|"config"
argument_list|)
operator|.
name|then
argument_list|( )
operator|.
name|statusCode
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
name|void
name|testGetConfigProperty
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
literal|"config/properties/rest.csrffilter.absentorigin.deny"
argument_list|)
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
literal|"true"
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
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testUpdateConfigProperty
parameter_list|()
block|{
name|String
name|token
init|=
name|getAdminToken
argument_list|( )
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jsonMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"value"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
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
name|body
argument_list|(
name|jsonMap
argument_list|)
operator|.
name|put
argument_list|(
literal|"config/properties/rest.csrffilter.absentorigin.deny"
argument_list|)
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
name|response
operator|=
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
literal|"config/properties/rest.csrffilter.absentorigin.deny"
argument_list|)
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
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
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
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jsonMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"value"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
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
name|body
argument_list|(
name|jsonMap
argument_list|)
operator|.
name|put
argument_list|(
literal|"config/properties/rest.csrffilter.absentorigin.deny"
argument_list|)
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
block|}
block|}
block|}
end_class

end_unit
