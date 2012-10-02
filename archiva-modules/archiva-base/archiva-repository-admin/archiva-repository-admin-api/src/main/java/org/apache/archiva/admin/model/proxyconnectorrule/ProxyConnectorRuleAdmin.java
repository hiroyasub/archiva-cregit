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
name|proxyconnectorrule
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
name|ProxyConnectorRule
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProxyConnectorRuleAdmin
block|{
comment|/**      * @return      * @throws org.apache.archiva.admin.model.RepositoryAdminException      *      * @since 1.4-M3      */
name|List
argument_list|<
name|ProxyConnectorRule
argument_list|>
name|getProxyConnectorRules
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * @param proxyConnectorRule      * @throws RepositoryAdminException      * @since 1.4-M3      */
name|void
name|addProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * @param proxyConnectorRule      * @throws RepositoryAdminException      * @since 1.4-M3      */
name|void
name|deleteProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      *<b>only to update attached proxy connectors to update pattern delete then add</b>      * @param proxyConnectorRule      * @throws RepositoryAdminException      * @since 1.4-M3      */
name|void
name|updateProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
block|}
end_interface

end_unit

