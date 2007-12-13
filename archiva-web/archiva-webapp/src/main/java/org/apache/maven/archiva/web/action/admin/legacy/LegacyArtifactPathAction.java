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
operator|.
name|legacy
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
name|ArrayList
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
name|LegacyArtifactPath
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
name|redback
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
name|redback
operator|.
name|xwork
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
name|redback
operator|.
name|xwork
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
name|redback
operator|.
name|xwork
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
name|Preparable
import|;
end_import

begin_comment
comment|/**  * Shows the LegacyArtifactPath Tab for the administrator.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="legacyArtifactPathAction"  */
end_comment

begin_class
specifier|public
class|class
name|LegacyArtifactPathAction
extends|extends
name|PlexusActionSupport
implements|implements
name|SecureAction
implements|,
name|ServletRequestAware
implements|,
name|Preparable
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|List
argument_list|<
name|LegacyArtifactPath
argument_list|>
name|legacyArtifactPaths
decl_stmt|;
comment|/**      * Used to construct the repository WebDAV URL in the repository action.      */
specifier|private
name|String
name|baseUrl
decl_stmt|;
specifier|public
name|void
name|setServletRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
comment|// TODO: is there a better way to do this?
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
name|prepare
parameter_list|()
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|legacyArtifactPaths
operator|=
operator|new
name|ArrayList
argument_list|<
name|LegacyArtifactPath
argument_list|>
argument_list|(
name|config
operator|.
name|getLegacyArtifactPaths
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|LegacyArtifactPath
argument_list|>
name|getLegacyArtifactPaths
parameter_list|()
block|{
return|return
name|legacyArtifactPaths
return|;
block|}
specifier|public
name|String
name|getBaseUrl
parameter_list|()
block|{
return|return
name|baseUrl
return|;
block|}
block|}
end_class

end_unit

