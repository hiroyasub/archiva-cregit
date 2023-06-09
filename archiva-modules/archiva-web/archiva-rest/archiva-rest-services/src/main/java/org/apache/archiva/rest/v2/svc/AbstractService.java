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
name|v2
operator|.
name|svc
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|AuditInformation
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
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|RedbackAuthenticationThreadLocal
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
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|RedbackRequestInformation
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
name|redback
operator|.
name|users
operator|.
name|User
import|;
end_import

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_comment
comment|/**  * @author Martin Schreier<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|AbstractService
block|{
annotation|@
name|Context
specifier|private
name|HttpServletRequest
name|httpServletRequest
decl_stmt|;
specifier|protected
name|AuditInformation
name|getAuditInformation
parameter_list|( )
block|{
name|RedbackRequestInformation
name|redbackRequestInformation
init|=
name|RedbackAuthenticationThreadLocal
operator|.
name|get
argument_list|( )
decl_stmt|;
name|User
name|user
decl_stmt|;
name|String
name|remoteAddr
decl_stmt|;
if|if
condition|(
name|redbackRequestInformation
operator|==
literal|null
condition|)
block|{
name|user
operator|=
literal|null
expr_stmt|;
name|remoteAddr
operator|=
name|httpServletRequest
operator|.
name|getRemoteAddr
argument_list|( )
expr_stmt|;
block|}
else|else
block|{
name|user
operator|=
name|redbackRequestInformation
operator|.
name|getUser
argument_list|( )
expr_stmt|;
name|remoteAddr
operator|=
name|redbackRequestInformation
operator|.
name|getRemoteAddr
argument_list|( )
expr_stmt|;
block|}
return|return
operator|new
name|AuditInformation
argument_list|(
name|user
argument_list|,
name|remoteAddr
argument_list|)
return|;
block|}
block|}
end_class

end_unit

