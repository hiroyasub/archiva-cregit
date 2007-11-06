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
name|web
operator|.
name|util
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|ActionContext
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|ArchivaUser
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
name|users
operator|.
name|User
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * ArchivaXworkUser   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.security.ArchivaUser"  *                   role-hint="xwork"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaXworkUser
implements|implements
name|ArchivaUser
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getContextSession
parameter_list|()
block|{
name|ActionContext
name|context
init|=
name|ActionContext
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sessionMap
init|=
name|context
operator|.
name|getSession
argument_list|()
decl_stmt|;
if|if
condition|(
name|sessionMap
operator|==
literal|null
condition|)
block|{
name|sessionMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|sessionMap
return|;
block|}
specifier|private
name|SecuritySession
name|getSecuritySession
parameter_list|()
block|{
return|return
operator|(
name|SecuritySession
operator|)
name|getContextSession
argument_list|()
operator|.
name|get
argument_list|(
name|SecuritySession
operator|.
name|ROLE
argument_list|)
return|;
block|}
specifier|public
name|String
name|getActivePrincipal
parameter_list|()
block|{
name|SecuritySession
name|securitySession
init|=
name|getSecuritySession
argument_list|()
decl_stmt|;
if|if
condition|(
name|securitySession
operator|==
literal|null
condition|)
block|{
return|return
name|ArchivaRoleConstants
operator|.
name|PRINCIPAL_GUEST
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
name|ArchivaRoleConstants
operator|.
name|PRINCIPAL_GUEST
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

