begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|api
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
name|group
operator|.
name|RepositoryGroupAdmin
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
name|managed
operator|.
name|ManagedRepositoryAdmin
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"dataValidatorService#rest"
argument_list|)
specifier|public
class|class
name|DefaultDataValidatorService
implements|implements
name|DataValidatorService
block|{
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|NetworkProxyAdmin
name|networkProxyAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryGroupAdmin
name|repositoryGroupAdmin
decl_stmt|;
specifier|public
name|Boolean
name|managedRepositoryIdNotExists
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
operator|!
name|idExist
argument_list|(
name|id
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
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|remoteRepositoryIdNotExists
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
operator|!
name|idExist
argument_list|(
name|id
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
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|networkProxyIdNotExists
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|networkProxyAdmin
operator|.
name|getNetworkProxy
argument_list|(
name|id
argument_list|)
operator|==
literal|null
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
comment|/**      * check if managedRepo, remoteRepo ou group exists with this id      *      * @param id      * @return true if something exists with this id.      */
specifier|private
name|Boolean
name|idExist
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
operator|(
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|id
argument_list|)
operator|!=
literal|null
operator|)
operator|||
operator|(
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepository
argument_list|(
name|id
argument_list|)
operator|!=
literal|null
operator|)
operator|||
operator|(
name|repositoryGroupAdmin
operator|.
name|getRepositoryGroup
argument_list|(
name|id
argument_list|)
operator|!=
literal|null
operator|)
return|;
block|}
block|}
end_class

end_unit
