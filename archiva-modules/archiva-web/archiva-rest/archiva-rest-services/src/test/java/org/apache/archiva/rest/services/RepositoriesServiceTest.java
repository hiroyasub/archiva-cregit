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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ManagedRepository
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|RepositoriesService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|ServerWebApplicationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
specifier|public
class|class
name|RepositoriesServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
name|RepositoriesService
name|getRepositoriesService
parameter_list|()
block|{
return|return
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/services/archivaServices/"
argument_list|,
name|RepositoriesService
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ServerWebApplicationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|listRemoteRepositoriesKarmaFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoriesService
name|service
init|=
name|getRepositoriesService
argument_list|()
decl_stmt|;
try|try
block|{
name|assertFalse
argument_list|(
name|service
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServerWebApplicationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|403
argument_list|,
name|e
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|listRemoteRepositoriesKarma
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoriesService
name|service
init|=
name|getRepositoriesService
argument_list|()
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|300000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|repos
init|=
name|service
operator|.
name|getRemoteRepositories
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|repos
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"repos {}"
argument_list|,
name|repos
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ServerWebApplicationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|scanRepoKarmaFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoriesService
name|service
init|=
name|getRepositoriesService
argument_list|()
decl_stmt|;
try|try
block|{
name|service
operator|.
name|scanRepository
argument_list|(
literal|"id"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServerWebApplicationException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|403
argument_list|,
name|e
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|scanRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoriesService
name|service
init|=
name|getRepositoriesService
argument_list|()
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|300000
argument_list|)
expr_stmt|;
name|String
name|repoId
init|=
name|service
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|scanRepository
argument_list|(
name|repoId
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"sanRepo call ok "
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|alreadyScanning
argument_list|(
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|addManagedRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoriesService
name|service
init|=
name|getRepositoriesService
argument_list|()
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|300000
argument_list|)
expr_stmt|;
name|ManagedRepository
name|repo
init|=
name|getTestManagedRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|service
operator|.
name|getManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|service
operator|.
name|deleteManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service
operator|.
name|getManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|service
operator|.
name|getManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|updateManagedRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoriesService
name|service
init|=
name|getRepositoriesService
argument_list|()
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|300000
argument_list|)
expr_stmt|;
name|ManagedRepository
name|repo
init|=
name|getTestManagedRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|service
operator|.
name|getManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|service
operator|.
name|deleteManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service
operator|.
name|getManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|repo
operator|=
name|service
operator|.
name|getManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// toto is foo in French :-)
name|repo
operator|.
name|setName
argument_list|(
literal|"toto"
argument_list|)
expr_stmt|;
name|service
operator|.
name|updateManagedRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|repo
operator|=
name|service
operator|.
name|getManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toto"
argument_list|,
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ManagedRepository
name|getTestManagedRepository
parameter_list|()
block|{
return|return
operator|new
name|ManagedRepository
argument_list|(
literal|"TEST"
argument_list|,
literal|"test"
argument_list|,
literal|"foo"
argument_list|,
literal|"default"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|"2 * * * * ?"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

