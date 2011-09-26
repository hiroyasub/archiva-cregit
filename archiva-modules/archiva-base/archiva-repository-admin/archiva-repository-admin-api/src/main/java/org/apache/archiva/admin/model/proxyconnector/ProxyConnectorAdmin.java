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
name|model
operator|.
name|proxyconnector
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
name|AuditInformation
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
name|ProxyConnector
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

begin_comment
comment|/**  *<b>No update method for changing source and target here as id is : sourceRepoId and targetRepoId, use delete then add.</b>  *  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProxyConnectorAdmin
block|{
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
name|ProxyConnector
name|getProxyConnector
parameter_list|(
name|String
name|sourceRepoId
parameter_list|,
name|String
name|targetRepoId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
name|Boolean
name|addProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
name|Boolean
name|deleteProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      *<b>only for enabled/disable or changing bean values except target/source</b>      *      * @param proxyConnector      * @param auditInformation      * @return      * @throws RepositoryAdminException      */
name|Boolean
name|updateProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * @return key/value : managed repo Id / list to proxy connector ordered      * @throws RepositoryAdminException      */
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|getProxyConnectorAsMap
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
block|}
end_interface

end_unit

