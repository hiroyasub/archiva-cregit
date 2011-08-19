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
name|services
operator|.
name|PingService
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
name|codehaus
operator|.
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|AbstractRestServicesTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
specifier|public
class|class
name|PingServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
name|PingService
name|getPingService
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
name|PingService
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|ping
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 1000000L
comment|//WebClient.getConfig( userService ).getHttpConduit().getClient().setReceiveTimeout(3000);
name|String
name|res
init|=
name|getPingService
argument_list|()
operator|.
name|ping
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Yeah Baby It rocks!"
argument_list|,
name|res
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
name|pingWithAuthzFailed
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|res
init|=
name|getPingService
argument_list|()
operator|.
name|pingWithAuthz
argument_list|()
decl_stmt|;
name|fail
argument_list|(
literal|"not in exception"
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
name|pingWithAuthz
parameter_list|()
throws|throws
name|Exception
block|{
name|PingService
name|service
init|=
name|getPingService
argument_list|()
decl_stmt|;
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
name|String
name|res
init|=
name|service
operator|.
name|pingWithAuthz
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Yeah Baby It rocks!"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"FIXME guest failed ???"
argument_list|)
specifier|public
name|void
name|pingWithAuthzGuest
parameter_list|()
throws|throws
name|Exception
block|{
name|PingService
name|service
init|=
name|getPingService
argument_list|()
decl_stmt|;
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
name|guestAuthzHeader
argument_list|)
expr_stmt|;
name|String
name|res
init|=
name|service
operator|.
name|pingWithAuthz
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Yeah Baby It rocks!"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

