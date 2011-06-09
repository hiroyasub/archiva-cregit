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
name|appearance
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|IndeterminateConfigurationException
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
name|OrganisationInformation
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
name|registry
operator|.
name|RegistryException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|integration
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
name|redback
operator|.
name|integration
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
name|redback
operator|.
name|integration
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
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Controller
import|;
end_import

begin_comment
comment|/**  * @version $Id: ConfigurationAction.java 480950 2006-11-30 14:58:35Z evenisse $  * plexus.component role="com.opensymphony.xwork2.Action"  * role-hint="editOrganisationInfo"  * instantiation-strategy="per-lookup"  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"editOrganisationInfo"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|EditOrganisationInfoAction
extends|extends
name|AbstractAppearanceAction
implements|implements
name|SecureAction
implements|,
name|Validateable
block|{
annotation|@
name|Override
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|RegistryException
throws|,
name|IndeterminateConfigurationException
block|{
name|Configuration
name|config
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
name|OrganisationInformation
name|orgInfo
init|=
name|config
operator|.
name|getOrganisationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|orgInfo
operator|==
literal|null
condition|)
block|{
name|config
operator|.
name|setOrganisationInfo
argument_list|(
name|orgInfo
argument_list|)
expr_stmt|;
block|}
name|orgInfo
operator|.
name|setLogoLocation
argument_list|(
name|getOrganisationLogo
argument_list|()
argument_list|)
expr_stmt|;
name|orgInfo
operator|.
name|setName
argument_list|(
name|getOrganisationName
argument_list|()
argument_list|)
expr_stmt|;
name|orgInfo
operator|.
name|setUrl
argument_list|(
name|getOrganisationUrl
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
return|return
name|SUCCESS
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
name|validate
parameter_list|()
block|{
comment|// trim all unecessary trailing/leading white-spaces; always put this statement before the closing braces(after all validation).
name|trimAllRequestParameterValues
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|trimAllRequestParameterValues
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|super
operator|.
name|getOrganisationName
argument_list|()
argument_list|)
condition|)
block|{
name|super
operator|.
name|setOrganisationName
argument_list|(
name|super
operator|.
name|getOrganisationName
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|super
operator|.
name|getOrganisationUrl
argument_list|()
argument_list|)
condition|)
block|{
name|super
operator|.
name|setOrganisationUrl
argument_list|(
name|super
operator|.
name|getOrganisationUrl
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|super
operator|.
name|getOrganisationLogo
argument_list|()
argument_list|)
condition|)
block|{
name|super
operator|.
name|setOrganisationLogo
argument_list|(
name|super
operator|.
name|getOrganisationLogo
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

