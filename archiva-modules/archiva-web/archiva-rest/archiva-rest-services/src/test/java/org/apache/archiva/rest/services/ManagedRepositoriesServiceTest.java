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
name|apache
operator|.
name|maven
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
name|ManagedRepositoriesServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|addManagedRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoriesService
name|service
init|=
name|getManagedRepositoriesService
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
argument_list|,
literal|true
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
name|service
operator|.
name|deleteManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
literal|true
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
annotation|@
name|Test
specifier|public
name|void
name|updateManagedRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoriesService
name|service
init|=
name|getManagedRepositoriesService
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
argument_list|,
literal|true
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
name|service
operator|.
name|deleteManagedRepository
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
literal|true
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
block|}
end_class

end_unit

