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
name|net
operator|.
name|sf
operator|.
name|beanlib
operator|.
name|provider
operator|.
name|replicator
operator|.
name|BeanReplicator
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
operator|.
name|ProxyConnectorAdmin
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
name|ArchivaRestServiceException
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
name|ProxyConnectorService
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"proxyConnectorService#rest"
argument_list|)
specifier|public
class|class
name|DefaultProxyConnectorService
extends|extends
name|AbstractRestService
implements|implements
name|ProxyConnectorService
block|{
annotation|@
name|Inject
specifier|private
name|ProxyConnectorAdmin
name|proxyConnectorAdmin
decl_stmt|;
specifier|public
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|proxyConnectors
init|=
operator|new
name|ArrayList
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
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
name|proxyConnector
range|:
name|proxyConnectorAdmin
operator|.
name|getProxyConnectors
argument_list|()
control|)
block|{
name|proxyConnectors
operator|.
name|add
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|proxyConnector
argument_list|,
name|ProxyConnector
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|proxyConnectors
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
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
name|ArchivaRestServiceException
block|{
try|try
block|{
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
name|proxyConnector
init|=
name|proxyConnectorAdmin
operator|.
name|getProxyConnector
argument_list|(
name|sourceRepoId
argument_list|,
name|targetRepoId
argument_list|)
decl_stmt|;
return|return
name|proxyConnector
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|proxyConnector
argument_list|,
name|ProxyConnector
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|addProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|proxyConnector
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
try|try
block|{
return|return
name|proxyConnectorAdmin
operator|.
name|addProxyConnector
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|proxyConnector
argument_list|,
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
operator|.
name|class
argument_list|)
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|deleteProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|proxyConnector
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
try|try
block|{
return|return
name|proxyConnectorAdmin
operator|.
name|deleteProxyConnector
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|proxyConnector
argument_list|,
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
operator|.
name|class
argument_list|)
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|updateProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|proxyConnector
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
try|try
block|{
return|return
name|proxyConnectorAdmin
operator|.
name|updateProxyConnector
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|proxyConnector
argument_list|,
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
operator|.
name|class
argument_list|)
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|ProxyConnectorAdmin
name|getProxyConnectorAdmin
parameter_list|()
block|{
return|return
name|proxyConnectorAdmin
return|;
block|}
specifier|public
name|void
name|setProxyConnectorAdmin
parameter_list|(
name|ProxyConnectorAdmin
name|proxyConnectorAdmin
parameter_list|)
block|{
name|this
operator|.
name|proxyConnectorAdmin
operator|=
name|proxyConnectorAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

