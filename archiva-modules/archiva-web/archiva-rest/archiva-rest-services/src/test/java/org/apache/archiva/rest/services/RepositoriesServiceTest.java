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
name|beans
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
name|common
operator|.
name|utils
operator|.
name|FileUtil
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
name|ManagedRepositoriesService
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
name|io
operator|.
name|File
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
name|ManagedRepositoriesService
name|managedRepositoriesService
init|=
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|managedRepositoriesService
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
name|managedRepositoriesService
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
name|int
name|timeout
init|=
literal|20000
decl_stmt|;
while|while
condition|(
name|timeout
operator|>
literal|0
operator|&&
name|service
operator|.
name|alreadyScanning
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|timeout
operator|-=
literal|500
expr_stmt|;
block|}
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
block|}
specifier|protected
name|ManagedRepository
name|getTestManagedRepository
parameter_list|()
block|{
name|String
name|location
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target/test-repo"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
return|return
operator|new
name|ManagedRepository
argument_list|(
literal|"TEST"
argument_list|,
literal|"test"
argument_list|,
name|location
argument_list|,
literal|"default"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|"2 * * * * ?"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|80
argument_list|,
literal|80
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

