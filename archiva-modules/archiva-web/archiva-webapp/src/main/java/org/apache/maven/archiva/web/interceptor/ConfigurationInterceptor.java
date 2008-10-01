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
name|interceptor
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
name|struts2
operator|.
name|ServletActionContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|ActionInvocation
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|interceptor
operator|.
name|Interceptor
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
name|configuration
operator|.
name|ArchivaConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_comment
comment|/**  * An interceptor that makes the configuration bits available, both to the application and the webapp  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @plexus.component role="com.opensymphony.xwork2.interceptor.Interceptor"  * role-hint="configurationInterceptor"  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationInterceptor
implements|implements
name|Interceptor
block|{
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|public
name|String
name|intercept
parameter_list|(
name|ActionInvocation
name|actionInvocation
parameter_list|)
throws|throws
name|Exception
block|{
comment|// populate webapp configuration bits into the session
name|ServletContext
name|applicationScope
init|=
name|ServletActionContext
operator|.
name|getServletContext
argument_list|()
decl_stmt|;
name|applicationScope
operator|.
name|setAttribute
argument_list|(
literal|"uiOptions"
argument_list|,
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWebapp
argument_list|()
operator|.
name|getUi
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|actionInvocation
operator|.
name|invoke
argument_list|()
return|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|// This space left intentionally blank
block|}
specifier|public
name|void
name|init
parameter_list|()
block|{
comment|// This space left intentionally blank
block|}
block|}
end_class

end_unit

