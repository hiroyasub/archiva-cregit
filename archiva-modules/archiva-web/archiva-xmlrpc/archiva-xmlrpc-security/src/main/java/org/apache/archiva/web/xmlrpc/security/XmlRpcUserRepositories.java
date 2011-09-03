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
name|xmlrpc
operator|.
name|security
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|security
operator|.
name|ArchivaSecurityException
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
name|security
operator|.
name|PrincipalNotFoundException
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
name|security
operator|.
name|UserRepositories
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlrpc
operator|.
name|server
operator|.
name|AbstractReflectiveHandlerMapping
operator|.
name|AuthenticationHandler
import|;
end_import

begin_class
specifier|public
class|class
name|XmlRpcUserRepositories
block|{
specifier|private
name|UserRepositories
name|userRepositories
decl_stmt|;
specifier|private
name|AuthenticationHandler
name|authnHandler
decl_stmt|;
specifier|public
name|XmlRpcUserRepositories
parameter_list|(
name|UserRepositories
name|userRepositories
parameter_list|,
name|AuthenticationHandler
name|authnHandler
parameter_list|)
block|{
name|this
operator|.
name|userRepositories
operator|=
name|userRepositories
expr_stmt|;
name|this
operator|.
name|authnHandler
operator|=
name|authnHandler
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getObservableRepositories
parameter_list|()
throws|throws
name|PrincipalNotFoundException
throws|,
name|ArchivaSecurityException
block|{
name|XmlRpcAuthenticator
name|xmlRpcAuthn
init|=
operator|(
name|XmlRpcAuthenticator
operator|)
name|authnHandler
decl_stmt|;
return|return
name|userRepositories
operator|.
name|getObservableRepositoryIds
argument_list|(
name|xmlRpcAuthn
operator|.
name|getActiveUser
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

