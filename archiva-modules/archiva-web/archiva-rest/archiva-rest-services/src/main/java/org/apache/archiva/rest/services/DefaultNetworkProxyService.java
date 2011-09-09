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
name|repository
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
name|repository
operator|.
name|networkproxy
operator|.
name|NetworkProxyAdmin
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
name|NetworkProxy
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
name|NetworkProxyService
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
literal|"networkProxyService#rest"
argument_list|)
specifier|public
class|class
name|DefaultNetworkProxyService
extends|extends
name|AbstractRestService
implements|implements
name|NetworkProxyService
block|{
annotation|@
name|Inject
specifier|private
name|NetworkProxyAdmin
name|networkProxyAdmin
decl_stmt|;
specifier|public
name|List
argument_list|<
name|NetworkProxy
argument_list|>
name|getNetworkProxies
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|List
argument_list|<
name|NetworkProxy
argument_list|>
name|networkProxies
init|=
operator|new
name|ArrayList
argument_list|<
name|NetworkProxy
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
name|repository
operator|.
name|networkproxy
operator|.
name|NetworkProxy
name|networkProxy
range|:
name|networkProxyAdmin
operator|.
name|getNetworkProxies
argument_list|()
control|)
block|{
name|networkProxies
operator|.
name|add
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|networkProxy
argument_list|,
name|NetworkProxy
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|networkProxies
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
name|NetworkProxy
name|getNetworkProxy
parameter_list|(
name|String
name|networkProxyId
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
name|repository
operator|.
name|networkproxy
operator|.
name|NetworkProxy
name|networkProxy
init|=
name|networkProxyAdmin
operator|.
name|getNetworkProxy
argument_list|(
name|networkProxyId
argument_list|)
decl_stmt|;
return|return
name|networkProxy
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
name|networkProxy
argument_list|,
name|NetworkProxy
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
name|void
name|addNetworkProxy
parameter_list|(
name|NetworkProxy
name|networkProxy
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
if|if
condition|(
name|networkProxy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|getNetworkProxyAdmin
argument_list|()
operator|.
name|addNetworkProxy
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|networkProxy
argument_list|,
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
name|networkproxy
operator|.
name|NetworkProxy
operator|.
name|class
argument_list|)
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
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
name|void
name|updateNetworkProxy
parameter_list|(
name|NetworkProxy
name|networkProxy
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|networkProxy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|getNetworkProxyAdmin
argument_list|()
operator|.
name|updateNetworkProxy
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|networkProxy
argument_list|,
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
name|networkproxy
operator|.
name|NetworkProxy
operator|.
name|class
argument_list|)
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
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
name|deleteNetworkProxy
parameter_list|(
name|String
name|networkProxyId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|getNetworkProxyAdmin
argument_list|()
operator|.
name|deleteNetworkProxy
argument_list|(
name|networkProxyId
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
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
name|NetworkProxyAdmin
name|getNetworkProxyAdmin
parameter_list|()
block|{
return|return
name|networkProxyAdmin
return|;
block|}
specifier|public
name|void
name|setNetworkProxyAdmin
parameter_list|(
name|NetworkProxyAdmin
name|networkProxyAdmin
parameter_list|)
block|{
name|this
operator|.
name|networkProxyAdmin
operator|=
name|networkProxyAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

