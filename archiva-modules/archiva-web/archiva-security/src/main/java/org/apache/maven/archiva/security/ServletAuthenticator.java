begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|authentication
operator|.
name|AuthenticationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|authentication
operator|.
name|AuthenticationResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
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
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|authorization
operator|.
name|UnauthorizedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|policy
operator|.
name|AccountLockedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|policy
operator|.
name|MustChangePasswordException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|system
operator|.
name|SecuritySession
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_interface
specifier|public
interface|interface
name|ServletAuthenticator
block|{
comment|/**      * Authentication check for users.      *       * @param request      * @param result      * @return      * @throws AuthenticationException      * @throws AccountLockedException      * @throws MustChangePasswordException      */
specifier|public
name|boolean
name|isAuthenticated
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|AuthenticationResult
name|result
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|AccountLockedException
throws|,
name|MustChangePasswordException
function_decl|;
comment|/**      * Authorization check for valid users.      *       * @param request      * @param securitySession      * @param repositoryId      * @param isWriteRequest      * @return      * @throws AuthorizationException      * @throws UnauthorizedException      */
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|SecuritySession
name|securitySession
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|boolean
name|isWriteRequest
parameter_list|)
throws|throws
name|AuthorizationException
throws|,
name|UnauthorizedException
function_decl|;
comment|/**      * Authorization check specific for user guest, which doesn't go through       * HttpBasicAuthentication#getAuthenticationResult( HttpServletRequest request, HttpServletResponse response )      * since no credentials are attached to the request.       *       * See also MRM-911      *       * @param principal      * @param repoId      * @param isWriteRequest      * @return      * @throws UnauthorizedException      */
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|,
name|boolean
name|isWriteRequest
parameter_list|)
throws|throws
name|UnauthorizedException
function_decl|;
block|}
end_interface

end_unit

