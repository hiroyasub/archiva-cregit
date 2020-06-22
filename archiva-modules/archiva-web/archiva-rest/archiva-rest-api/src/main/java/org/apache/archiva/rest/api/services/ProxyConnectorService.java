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
name|services
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ProxyConnector
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
name|model
operator|.
name|PolicyInformation
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
name|MediaType
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

begin_comment
comment|/**  *<b>No update method for changing source and target here as id is : sourceRepoId and targetRepoId, use delete then add.</b>  *  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/proxyConnectorService/"
argument_list|)
annotation|@
name|Tag
argument_list|(
name|name
operator|=
literal|"Proxy-Repository"
argument_list|)
specifier|public
interface|interface
name|ProxyConnectorService
block|{
annotation|@
name|Path
argument_list|(
literal|"getProxyConnectors"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
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
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getProxyConnector"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
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
name|ProxyConnector
name|getProxyConnector
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"sourceRepoId"
argument_list|)
name|String
name|sourceRepoId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"targetRepoId"
argument_list|)
name|String
name|targetRepoId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addProxyConnector"
argument_list|)
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
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
name|Boolean
name|addProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"deleteProxyConnector"
argument_list|)
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
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
name|Boolean
name|deleteProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
comment|/**      * @since 1.4-M3      */
annotation|@
name|Path
argument_list|(
literal|"removeProxyConnector"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
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
name|Boolean
name|removeProxyConnector
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"sourceRepoId"
argument_list|)
name|String
name|sourceRepoId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"targetRepoId"
argument_list|)
name|String
name|targetRepoId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
comment|/**      *<b>only for enabled/disable or changing bean values except target/source</b>      *      * @param proxyConnector      * @return      */
annotation|@
name|Path
argument_list|(
literal|"updateProxyConnector"
argument_list|)
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
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
name|Boolean
name|updateProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"allPolicies"
argument_list|)
annotation|@
name|GET
annotation|@
name|Consumes
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
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
name|List
argument_list|<
name|PolicyInformation
argument_list|>
name|getAllPolicyInformations
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
block|}
end_interface

end_unit

