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
name|api
operator|.
name|v2
operator|.
name|svc
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|Operation
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|enums
operator|.
name|ParameterIn
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|ArraySchema
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Content
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|responses
operator|.
name|ApiResponse
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|security
operator|.
name|SecurityRequirement
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|tags
operator|.
name|Tag
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
name|redback
operator|.
name|authorization
operator|.
name|RedbackAuthorization
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
name|v2
operator|.
name|model
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
name|v2
operator|.
name|model
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
name|v2
operator|.
name|model
operator|.
name|LdapConfiguration
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
name|v2
operator|.
name|model
operator|.
name|SecurityConfiguration
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
name|security
operator|.
name|common
operator|.
name|ArchivaRoleConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DefaultValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PUT
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
operator|.
name|APPLICATION_JSON
import|;
end_import

begin_import
import|import static
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
name|v2
operator|.
name|svc
operator|.
name|RestConfiguration
operator|.
name|DEFAULT_PAGE_LIMIT
import|;
end_import

begin_comment
comment|/**  *  * Service for configuration of redback and security related settings.  *  * @author Martin Stockhammer<martin_s@apache.org>  * @since 3.0  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/security"
argument_list|)
annotation|@
name|Tag
argument_list|(
name|name
operator|=
literal|"v2"
argument_list|)
annotation|@
name|Tag
argument_list|(
name|name
operator|=
literal|"v2/Security"
argument_list|)
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
literal|"BearerAuth"
argument_list|)
specifier|public
interface|interface
name|SecurityConfigurationService
block|{
annotation|@
name|Path
argument_list|(
literal|"config"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Returns the security configuration that is currently active."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the configuration could be retrieved"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|SecurityConfiguration
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to gather the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|SecurityConfiguration
name|getConfiguration
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config"
argument_list|)
annotation|@
name|PUT
annotation|@
name|Consumes
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Updates the security configuration."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the configuration was updated"
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"422"
argument_list|,
name|description
operator|=
literal|"Invalid content data"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to update the configuration"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|SecurityConfiguration
name|updateConfiguration
parameter_list|(
name|SecurityConfiguration
name|newConfiguration
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config/properties"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Returns all configuration properties. The result is paged."
argument_list|,
name|parameters
operator|=
block|{
annotation|@
name|Parameter
argument_list|(
name|name
operator|=
literal|"q"
argument_list|,
name|description
operator|=
literal|"Search term"
argument_list|)
block|,
annotation|@
name|Parameter
argument_list|(
name|name
operator|=
literal|"offset"
argument_list|,
name|description
operator|=
literal|"The offset of the first element returned"
argument_list|)
block|,
annotation|@
name|Parameter
argument_list|(
name|name
operator|=
literal|"limit"
argument_list|,
name|description
operator|=
literal|"Maximum number of items to return in the response"
argument_list|)
block|,
annotation|@
name|Parameter
argument_list|(
name|name
operator|=
literal|"orderBy"
argument_list|,
name|description
operator|=
literal|"List of attribute used for sorting (key, value)"
argument_list|)
block|,
annotation|@
name|Parameter
argument_list|(
name|name
operator|=
literal|"order"
argument_list|,
name|description
operator|=
literal|"The sort order. Either ascending (asc) or descending (desc)"
argument_list|)
block|}
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the list could be returned"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|PagedResult
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to gather the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|PagedResult
argument_list|<
name|PropertyEntry
argument_list|>
name|getConfigurationProperties
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"q"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|""
argument_list|)
name|String
name|searchTerm
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"offset"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|"0"
argument_list|)
name|Integer
name|offset
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"limit"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
name|value
operator|=
name|DEFAULT_PAGE_LIMIT
argument_list|)
name|Integer
name|limit
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"orderBy"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|"key"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|orderBy
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"order"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|"asc"
argument_list|)
name|String
name|order
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config/properties/{propertyName}"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Returns a single configuration property value."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|parameters
operator|=
block|{
annotation|@
name|Parameter
argument_list|(
name|in
operator|=
name|ParameterIn
operator|.
name|PATH
argument_list|,
name|name
operator|=
literal|"propertyName"
argument_list|,
name|description
operator|=
literal|"The name of the property to get the value for"
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the configuration could be retrieved"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|PropertyEntry
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"404"
argument_list|,
name|description
operator|=
literal|"The given property name does not exist"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to gather the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|PropertyEntry
name|getConfigurationProperty
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"propertyName"
argument_list|)
name|String
name|propertyName
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config/properties/{propertyName}"
argument_list|)
annotation|@
name|PUT
annotation|@
name|Consumes
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Updates a single property value of the security configuration."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|parameters
operator|=
block|{
annotation|@
name|Parameter
argument_list|(
name|in
operator|=
name|ParameterIn
operator|.
name|PATH
argument_list|,
name|name
operator|=
literal|"propertyName"
argument_list|,
name|description
operator|=
literal|"The name of the property to update"
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the property value was updated."
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"400"
argument_list|,
name|description
operator|=
literal|"The body data is not valid"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"404"
argument_list|,
name|description
operator|=
literal|"The given property name does not exist"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to gather the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|Response
name|updateConfigurationProperty
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"propertyName"
argument_list|)
name|String
name|propertyName
parameter_list|,
name|PropertyEntry
name|propertyValue
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config/ldap"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Returns the LDAP configuration that is currently active."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the configuration could be retrieved"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|LdapConfiguration
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to gather the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|LdapConfiguration
name|getLdapConfiguration
parameter_list|( )
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config/ldap"
argument_list|)
annotation|@
name|PUT
annotation|@
name|Consumes
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Updates the LDAP configuration that is currently active."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the configuration was updated"
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to update the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|LdapConfiguration
name|updateLdapConfiguration
parameter_list|(
name|LdapConfiguration
name|configuration
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config/ldap/verify"
argument_list|)
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Checks the given LDAP configuration."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the check was successful"
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"400"
argument_list|,
name|description
operator|=
literal|"If the check was not successful"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to update the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|Response
name|verifyLdapConfiguration
parameter_list|(
name|LdapConfiguration
name|configuration
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config/cache"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Returns the cache configuration that is currently active."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the configuration could be retrieved"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|CacheConfiguration
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to gather the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|CacheConfiguration
name|getCacheConfiguration
parameter_list|( )
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"config/cache"
argument_list|)
annotation|@
name|PUT
annotation|@
name|Consumes
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Updates the LDAP configuration that is currently active."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the configuration was updated"
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to update the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|CacheConfiguration
name|updateCacheConfiguration
parameter_list|(
name|CacheConfiguration
name|cacheConfiguration
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"user_managers"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Returns the available user manager implementations."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the list could be retrieved"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|array
operator|=
annotation|@
name|ArraySchema
argument_list|(
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|BeanInformation
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to gather the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|List
argument_list|<
name|BeanInformation
argument_list|>
name|getAvailableUserManagers
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"rbac_managers"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
annotation|@
name|Operation
argument_list|(
name|summary
operator|=
literal|"Returns the available RBAC manager implementations."
argument_list|,
name|security
operator|=
block|{
annotation|@
name|SecurityRequirement
argument_list|(
name|name
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
block|}
argument_list|,
name|responses
operator|=
block|{
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"200"
argument_list|,
name|description
operator|=
literal|"If the list could be retrieved"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|array
operator|=
annotation|@
name|ArraySchema
argument_list|(
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|BeanInformation
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
argument_list|)
block|,
annotation|@
name|ApiResponse
argument_list|(
name|responseCode
operator|=
literal|"403"
argument_list|,
name|description
operator|=
literal|"Authenticated user is not permitted to gather the information"
argument_list|,
name|content
operator|=
annotation|@
name|Content
argument_list|(
name|mediaType
operator|=
name|APPLICATION_JSON
argument_list|,
name|schema
operator|=
annotation|@
name|Schema
argument_list|(
name|implementation
operator|=
name|ArchivaRestError
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
block|}
argument_list|)
name|List
argument_list|<
name|BeanInformation
argument_list|>
name|getAvailableRbacManagers
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
block|}
end_interface

end_unit

