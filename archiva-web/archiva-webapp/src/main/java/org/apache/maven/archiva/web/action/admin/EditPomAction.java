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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|AbstractConfiguredAction
import|;
end_import

begin_comment
comment|//import org.apache.maven.artifact.installer.ArtifactInstallationException;
end_comment

begin_comment
comment|//import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
end_comment

begin_comment
comment|//import org.apache.maven.model.Model;
end_comment

begin_comment
comment|//import org.apache.maven.project.ProjectBuildingException;
end_comment

begin_comment
comment|//import org.apache.maven.shared.app.company.CompanyPomHandler;
end_comment

begin_comment
comment|//import org.apache.maven.shared.app.configuration.CompanyPom;
end_comment

begin_comment
comment|//import org.apache.maven.shared.app.configuration.Configuration;
end_comment

begin_comment
comment|//import org.apache.maven.shared.app.configuration.MavenAppConfiguration;
end_comment

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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @version $Id: ConfigurationAction.java 480950 2006-11-30 14:58:35Z evenisse $  * @TODO plexus.component role="com.opensymphony.xwork.Action"  * role-hint="editPom"  */
end_comment

begin_class
specifier|public
class|class
name|EditPomAction
extends|extends
name|AbstractConfiguredAction
implements|implements
name|ModelDriven
implements|,
name|SecureAction
implements|,
name|Preparable
block|{
comment|/**      * @plexus.requirement      */
comment|// private MavenAppConfiguration appConfigurationStore;
comment|/**      * The configuration.      */
comment|//    private Configuration configuration;
comment|/**      * @plexus.requirement      */
comment|// private CompanyPomHandler companyPomHandler;
comment|// private Model companyModel;
specifier|public
name|String
name|execute
parameter_list|()
comment|//        throws IOException, ArtifactInstallationException
block|{
comment|// TODO: hack for passed in String[]
comment|//        String[] logo = (String[]) companyModel.getProperties().get( "organization.logo" );
comment|//        if ( logo != null )
comment|//        {
comment|//            companyModel.getProperties().put( "organization.logo", logo[0] );
comment|//        }
comment|//
comment|//        companyPomHandler.save( companyModel, createLocalRepository() );
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|Object
name|getModel
parameter_list|()
block|{
comment|//        return companyModel;
return|return
operator|new
name|Object
argument_list|()
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
comment|//     throws ProjectBuildingException, ArtifactMetadataRetrievalException
block|{
comment|//        configuration = appConfigurationStore.getConfiguration();
comment|//
comment|//        CompanyPom companyPom = configuration.getCompanyPom();
comment|//        companyModel = companyPomHandler.getCompanyPomModel( companyPom, createLocalRepository() );
comment|//
comment|//        if ( companyModel == null )
comment|//        {
comment|//            companyModel = new Model();
comment|//            companyModel.setModelVersion( "4.0.0" );
comment|//            companyModel.setPackaging( "pom" );
comment|//
comment|//            if ( companyPom != null )
comment|//            {
comment|//                companyModel.setGroupId( companyPom.getGroupId() );
comment|//                companyModel.setArtifactId( companyPom.getArtifactId() );
comment|//            }
comment|//        }
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
comment|//    public Model getCompanyModel()
comment|//    {
comment|//        return companyModel;
comment|//    }
block|}
end_class

end_unit

