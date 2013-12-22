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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|RemoteRepository
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
name|admin
operator|.
name|model
operator|.
name|remote
operator|.
name|RemoteRepositoryAdmin
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
name|proxy
operator|.
name|common
operator|.
name|WagonFactory
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
name|proxy
operator|.
name|common
operator|.
name|WagonFactoryException
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
name|proxy
operator|.
name|common
operator|.
name|WagonFactoryRequest
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
name|RemoteRepositoriesService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|ResourceDoesNotExistException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|StreamWagon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|TransferFailedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|Wagon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|authorization
operator|.
name|AuthorizationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|providers
operator|.
name|http
operator|.
name|AbstractHttpClientWagon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|providers
operator|.
name|http
operator|.
name|HttpConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|providers
operator|.
name|http
operator|.
name|HttpMethodConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|providers
operator|.
name|http
operator|.
name|HttpWagon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|repository
operator|.
name|Repository
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"remoteRepositoriesService#rest"
argument_list|)
specifier|public
class|class
name|DefaultRemoteRepositoriesService
extends|extends
name|AbstractRestService
implements|implements
name|RemoteRepositoriesService
block|{
annotation|@
name|Inject
specifier|private
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|WagonFactory
name|wagonFactory
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|NetworkProxyAdmin
name|networkProxyAdmin
decl_stmt|;
specifier|public
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|getRemoteRepositories
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|remoteRepositories
init|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepositories
argument_list|()
decl_stmt|;
return|return
name|remoteRepositories
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|RemoteRepository
operator|>
name|emptyList
argument_list|()
else|:
name|remoteRepositories
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getFieldName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|RemoteRepository
name|getRemoteRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|remoteRepositories
init|=
name|getRemoteRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|RemoteRepository
name|repository
range|:
name|remoteRepositories
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|repositoryId
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|repository
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|deleteRemoteRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|remoteRepositoryAdmin
operator|.
name|deleteRemoteRepository
argument_list|(
name|repositoryId
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
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getFieldName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|addRemoteRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|remoteRepositoryAdmin
operator|.
name|addRemoteRepository
argument_list|(
name|remoteRepository
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
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getFieldName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|updateRemoteRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|remoteRepositoryAdmin
operator|.
name|updateRemoteRepository
argument_list|(
name|remoteRepository
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
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getFieldName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|checkRemoteConnectivity
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|RemoteRepository
name|remoteRepository
init|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepository
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
if|if
condition|(
name|remoteRepository
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"ignore scheduleDownloadRemote for repo with id {} as not exists"
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
name|NetworkProxy
name|networkProxy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|remoteRepository
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|()
argument_list|)
condition|)
block|{
name|networkProxy
operator|=
name|networkProxyAdmin
operator|.
name|getNetworkProxy
argument_list|(
name|remoteRepository
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|networkProxy
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"your remote repository is configured to download remote index trought a proxy we cannot find id:{}"
argument_list|,
name|remoteRepository
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|wagonProtocol
init|=
operator|new
name|URL
argument_list|(
name|remoteRepository
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
specifier|final
name|Wagon
name|wagon
init|=
name|wagonFactory
operator|.
name|getWagon
argument_list|(
operator|new
name|WagonFactoryRequest
argument_list|(
name|wagonProtocol
argument_list|,
name|remoteRepository
operator|.
name|getExtraHeaders
argument_list|()
argument_list|)
operator|.
name|networkProxy
argument_list|(
name|networkProxy
argument_list|)
argument_list|)
decl_stmt|;
name|wagon
operator|.
name|setReadTimeout
argument_list|(
name|remoteRepository
operator|.
name|getRemoteDownloadTimeout
argument_list|()
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|wagon
operator|.
name|setTimeout
argument_list|(
name|remoteRepository
operator|.
name|getTimeout
argument_list|()
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|HttpWagon
name|foo
decl_stmt|;
if|if
condition|(
name|wagon
operator|instanceof
name|AbstractHttpClientWagon
condition|)
block|{
name|HttpConfiguration
name|httpConfiguration
init|=
operator|new
name|HttpConfiguration
argument_list|()
decl_stmt|;
name|HttpMethodConfiguration
name|httpMethodConfiguration
init|=
operator|new
name|HttpMethodConfiguration
argument_list|()
decl_stmt|;
name|httpMethodConfiguration
operator|.
name|setUsePreemptive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|httpMethodConfiguration
operator|.
name|setReadTimeout
argument_list|(
name|remoteRepository
operator|.
name|getRemoteDownloadTimeout
argument_list|()
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|httpConfiguration
operator|.
name|setGet
argument_list|(
name|httpMethodConfiguration
argument_list|)
expr_stmt|;
name|AbstractHttpClientWagon
operator|.
name|class
operator|.
name|cast
argument_list|(
name|wagon
argument_list|)
operator|.
name|setHttpConfiguration
argument_list|(
name|httpConfiguration
argument_list|)
expr_stmt|;
block|}
name|wagon
operator|.
name|connect
argument_list|(
operator|new
name|Repository
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|remoteRepository
operator|.
name|getUrl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// we only check connectivity as remote repo can be empty
name|wagon
operator|.
name|getFileList
argument_list|(
literal|"/"
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
name|TransferFailedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"TransferFailedException :{}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
argument_list|,
name|Response
operator|.
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

