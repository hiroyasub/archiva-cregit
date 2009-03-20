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
name|java
operator|.
name|util
operator|.
name|Map
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
name|SecuritySystemConstants
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
name|users
operator|.
name|User
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
name|users
operator|.
name|UserManager
import|;
end_import

begin_comment
comment|/**  * ArchivaXworkUser  *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ArchivaXworkUser
block|{
specifier|private
name|ArchivaXworkUser
parameter_list|()
block|{
comment|// no touchy
block|}
specifier|public
specifier|static
name|String
name|getActivePrincipal
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|sessionMap
parameter_list|)
block|{
name|SecuritySession
name|securitySession
init|=
operator|(
name|SecuritySession
operator|)
name|sessionMap
operator|.
name|get
argument_list|(
name|SecuritySystemConstants
operator|.
name|SECURITY_SESSION_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|securitySession
operator|==
literal|null
condition|)
block|{
return|return
name|UserManager
operator|.
name|GUEST_USERNAME
return|;
block|}
name|User
name|user
init|=
name|securitySession
operator|.
name|getUser
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
return|return
name|UserManager
operator|.
name|GUEST_USERNAME
return|;
block|}
return|return
operator|(
name|String
operator|)
name|user
operator|.
name|getPrincipal
argument_list|()
return|;
block|}
block|}
end_class

end_unit

