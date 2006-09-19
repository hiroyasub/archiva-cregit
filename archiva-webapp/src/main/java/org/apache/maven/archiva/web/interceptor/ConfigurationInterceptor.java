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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
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
name|xwork
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
name|Configuration
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
name|ConfigurationStore
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
name|web
operator|.
name|util
operator|.
name|RoleManager
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
name|web
operator|.
name|ArchivaDefaults
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
name|logging
operator|.
name|AbstractLogEnabled
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
name|security
operator|.
name|rbac
operator|.
name|RBACManager
import|;
end_import

begin_comment
comment|/**  * An interceptor that makes the application configuration available  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @plexus.component role="com.opensymphony.xwork.interceptor.Interceptor" role-hint="configurationInterceptor"  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationInterceptor
extends|extends
name|AbstractLogEnabled
implements|implements
name|Interceptor
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RoleManager
name|roleManager
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RBACManager
name|rbacManager
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaDefaults
name|archivaDefaults
decl_stmt|;
comment|/**      *      * @param actionInvocation      * @return      * @throws Exception      */
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
name|archivaDefaults
operator|.
name|ensureDefaultsExist
argument_list|()
expr_stmt|;
comment|// determine if we need an admin account made
name|Configuration
name|configuration
init|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|configuration
operator|.
name|isValid
argument_list|()
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"No repositories were configured - forwarding to repository configuration page"
argument_list|)
expr_stmt|;
return|return
literal|"config-repository-needed"
return|;
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Configuration is incomplete - forwarding to configuration page"
argument_list|)
expr_stmt|;
return|return
literal|"config-needed"
return|;
block|}
block|}
else|else
block|{
return|return
name|actionInvocation
operator|.
name|invoke
argument_list|()
return|;
block|}
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

