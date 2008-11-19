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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|ArchivaRoleConstants
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
name|registry
operator|.
name|Registry
import|;
end_import

begin_comment
comment|/**  * ArchivaXworkUser   *  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.security.ArchivaXworkUser"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaXworkUser
block|{
comment|/**      * @plexus.requirement role-hint="commons-configuration"      */
specifier|private
name|Registry
name|registry
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"org.codehaus.plexus.redback"
decl_stmt|;
specifier|private
specifier|static
name|String
name|guest
decl_stmt|;
specifier|public
name|String
name|getActivePrincipal
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sessionMap
parameter_list|)
block|{
if|if
condition|(
name|sessionMap
operator|==
literal|null
condition|)
block|{
return|return
name|getGuest
argument_list|()
return|;
block|}
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
name|securitySession
operator|=
operator|(
name|SecuritySession
operator|)
name|sessionMap
operator|.
name|get
argument_list|(
name|SecuritySession
operator|.
name|ROLE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|securitySession
operator|==
literal|null
condition|)
block|{
return|return
name|getGuest
argument_list|()
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
name|getGuest
argument_list|()
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
specifier|public
name|String
name|getGuest
parameter_list|()
block|{
if|if
condition|(
name|guest
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|guest
argument_list|)
condition|)
block|{
name|Registry
name|subset
init|=
name|registry
operator|.
name|getSubset
argument_list|(
name|KEY
argument_list|)
decl_stmt|;
name|guest
operator|=
name|subset
operator|.
name|getString
argument_list|(
literal|"redback.default.guest"
argument_list|,
name|ArchivaRoleConstants
operator|.
name|PRINCIPAL_GUEST
argument_list|)
expr_stmt|;
block|}
return|return
name|guest
return|;
block|}
specifier|public
name|void
name|setGuest
parameter_list|(
name|String
name|guesT
parameter_list|)
block|{
name|guest
operator|=
name|guesT
expr_stmt|;
block|}
block|}
end_class

end_unit

