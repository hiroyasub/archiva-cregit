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
name|component
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
name|maven
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|AbstractConfiguredAction
import|;
end_import

begin_comment
comment|// TODO import org.apache.maven.model.Model;
end_comment

begin_comment
comment|// import org.apache.maven.shared.app.company.CompanyPomHandler;
end_comment

begin_comment
comment|// import org.apache.maven.shared.app.configuration.MavenAppConfiguration;
end_comment

begin_comment
comment|/**  * Stores the company information for displaying on the page.  *  * @TODO plexus.component role="com.opensymphony.xwork.Action" role-hint="companyInfo"  */
end_comment

begin_class
specifier|public
class|class
name|CompanyInfoAction
extends|extends
name|AbstractConfiguredAction
block|{
specifier|private
name|String
name|companyLogo
decl_stmt|;
specifier|private
name|String
name|companyUrl
decl_stmt|;
specifier|private
name|String
name|companyName
decl_stmt|;
comment|/**      * @TODO plexus.requirement      */
comment|// private CompanyPomHandler handler;
comment|/**      * @TODO plexus.requirement      */
comment|// private MavenAppConfiguration appConfigurationStore;
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
comment|/* TODO         Model model = handler.getCompanyPomModel( appConfigurationStore.getConfiguration().getCompanyPom(),                                                   createLocalRepository() );          if ( model != null )         {             if ( model.getOrganization() != null )             {                 companyName = model.getOrganization().getName();                 companyUrl = model.getOrganization().getUrl();             }              companyLogo = model.getProperties().getProperty( "organization.logo" );         }*/
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|getCompanyLogo
parameter_list|()
block|{
return|return
name|companyLogo
return|;
block|}
specifier|public
name|String
name|getCompanyUrl
parameter_list|()
block|{
return|return
name|companyUrl
return|;
block|}
specifier|public
name|String
name|getCompanyName
parameter_list|()
block|{
return|return
name|companyName
return|;
block|}
block|}
end_class

end_unit

