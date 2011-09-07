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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|ProxyConnectorServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|getAllproxyConnectors
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|getProxyConnectorService
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getProxyConnectorService
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

