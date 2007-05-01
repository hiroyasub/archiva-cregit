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
name|action
operator|.
name|admin
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
name|webwork
operator|.
name|interceptor
operator|.
name|ServletRequestAware
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
name|ModelDriven
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
name|Preparable
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
name|Validateable
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDAO
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
name|database
operator|.
name|constraints
operator|.
name|MostRecentRepositoryScanStatistics
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
name|model
operator|.
name|RepositoryContentStatistics
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
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|models
operator|.
name|AdminModel
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
name|action
operator|.
name|admin
operator|.
name|models
operator|.
name|AdminRepositoryConfiguration
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
name|ContextUtils
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
name|Resource
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
name|ui
operator|.
name|web
operator|.
name|interceptor
operator|.
name|SecureAction
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
name|ui
operator|.
name|web
operator|.
name|interceptor
operator|.
name|SecureActionBundle
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
name|ui
operator|.
name|web
operator|.
name|interceptor
operator|.
name|SecureActionException
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
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_comment
comment|/**  * Shows the Repositories Tab for the administrator.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="com.opensymphony.xwork.Action" role-hint="repositoriesAction"  */
end_comment

begin_class
specifier|public
class|class
name|RepositoriesAction
extends|extends
name|PlexusActionSupport
implements|implements
name|ModelDriven
implements|,
name|Preparable
implements|,
name|Validateable
implements|,
name|SecureAction
implements|,
name|ServletRequestAware
block|{
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|AdminModel
name|model
decl_stmt|;
specifier|private
name|String
name|baseUrl
decl_stmt|;
specifier|public
name|Object
name|getModel
parameter_list|()
block|{
return|return
name|getAdminModel
argument_list|()
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
name|model
operator|=
literal|null
expr_stmt|;
name|getModel
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|super
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|".execute()"
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|execute
argument_list|()
return|;
block|}
specifier|public
name|SecureActionBundle
name|getSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|SecureActionBundle
name|bundle
init|=
operator|new
name|SecureActionBundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|setRequiresAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|addRequiredAuthorization
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|,
name|Resource
operator|.
name|GLOBAL
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
specifier|public
name|void
name|setServletRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|this
operator|.
name|baseUrl
operator|=
name|ContextUtils
operator|.
name|getBaseURL
argument_list|(
name|request
argument_list|,
literal|"repository"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AdminModel
name|getAdminModel
parameter_list|()
block|{
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
name|model
operator|=
operator|new
name|AdminModel
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setBaseUrl
argument_list|(
name|baseUrl
argument_list|)
expr_stmt|;
name|updateLastIndexed
argument_list|(
name|model
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|model
return|;
block|}
specifier|private
name|void
name|updateLastIndexed
parameter_list|(
name|List
name|managedRepositories
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|managedRepositories
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|AdminRepositoryConfiguration
name|config
init|=
operator|(
name|AdminRepositoryConfiguration
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
name|results
init|=
name|dao
operator|.
name|query
argument_list|(
operator|new
name|MostRecentRepositoryScanStatistics
argument_list|(
name|config
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|RepositoryContentStatistics
name|stats
init|=
operator|(
name|RepositoryContentStatistics
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|config
operator|.
name|setStats
argument_list|(
name|stats
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getBaseUrlB
parameter_list|()
block|{
return|return
name|baseUrl
return|;
block|}
specifier|public
name|void
name|setBaseUrlB
parameter_list|(
name|String
name|baseUrl
parameter_list|)
block|{
name|this
operator|.
name|baseUrl
operator|=
name|baseUrl
expr_stmt|;
block|}
block|}
end_class

end_unit

