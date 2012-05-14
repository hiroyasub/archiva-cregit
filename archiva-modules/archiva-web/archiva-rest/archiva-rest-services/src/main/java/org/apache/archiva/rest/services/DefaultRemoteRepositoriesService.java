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
name|Exception
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
specifier|public
name|Boolean
name|addRemoteRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|)
throws|throws
name|Exception
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
specifier|public
name|Boolean
name|updateRemoteRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|)
throws|throws
name|Exception
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
block|}
end_class

end_unit

