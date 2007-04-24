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

begin_comment
comment|//import org.apache.maven.archiva.configuration.AbstractRepositoryConfiguration;
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
name|InvalidConfigurationException
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
name|rbac
operator|.
name|profile
operator|.
name|RoleProfileException
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
name|rbac
operator|.
name|profile
operator|.
name|RoleProfileManager
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
name|plexus
operator|.
name|security
operator|.
name|rbac
operator|.
name|RbacManagerException
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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Base action for repository configuration actions.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractConfigureRepositoryAction
extends|extends
name|PlexusActionSupport
implements|implements
name|ModelDriven
implements|,
name|Preparable
implements|,
name|SecureAction
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="archiva"      */
specifier|protected
name|RoleProfileManager
name|roleProfileManager
decl_stmt|;
comment|/**      * The repository.      */
comment|//    private AbstractRepositoryConfiguration repository;
comment|/**      * The repository ID to lookup when editing a repository.      */
specifier|private
name|String
name|repoId
decl_stmt|;
comment|/**      * The previously read configuration.      */
specifier|protected
name|Configuration
name|configuration
decl_stmt|;
specifier|public
name|String
name|add
parameter_list|()
throws|throws
name|IOException
throws|,
name|InvalidConfigurationException
throws|,
name|RbacManagerException
throws|,
name|RoleProfileException
throws|,
name|RegistryException
block|{
comment|// TODO: if this didn't come from the form, go to configure.action instead of going through with re-saving what was just loaded
comment|//        AbstractRepositoryConfiguration existingRepository = getRepository( repository.getId() );
comment|//        if ( existingRepository != null )
comment|//        {
comment|//            addFieldError( "id", "A repository with that id already exists" );
comment|//            return INPUT;
comment|//        }
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
specifier|public
name|String
name|edit
parameter_list|()
throws|throws
name|IOException
throws|,
name|InvalidConfigurationException
throws|,
name|RbacManagerException
throws|,
name|RoleProfileException
throws|,
name|RegistryException
block|{
comment|// TODO: if this didn't come from the form, go to configure.action instead of going through with re-saving what was just loaded
comment|//        AbstractRepositoryConfiguration existingRepository = getRepository( repository.getId() );
comment|//        removeRepository( existingRepository );
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
comment|//    protected abstract void removeRepository( AbstractRepositoryConfiguration existingRepository );
comment|//
comment|//    protected abstract AbstractRepositoryConfiguration getRepository( String id );
specifier|private
name|String
name|saveConfiguration
parameter_list|()
throws|throws
name|IOException
throws|,
name|InvalidConfigurationException
throws|,
name|RbacManagerException
throws|,
name|RoleProfileException
throws|,
name|RegistryException
block|{
name|addRepository
argument_list|()
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
comment|// TODO: do we need to check if indexing is needed?
name|addActionMessage
argument_list|(
literal|"Successfully saved configuration"
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|addRepository
parameter_list|()
throws|throws
name|IOException
throws|,
name|RoleProfileException
function_decl|;
specifier|public
name|String
name|input
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|public
name|Object
name|getModel
parameter_list|()
block|{
return|return
operator|new
name|Object
argument_list|()
return|;
comment|//        return repository;
block|}
comment|//    protected abstract AbstractRepositoryConfiguration createRepository();
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
comment|//        if ( repository == null )
comment|//        {
comment|//            repository = getRepository( repoId );
comment|//        }
comment|//        if ( repository == null )
comment|//        {
comment|//            repository = createRepository();
comment|//        }
block|}
specifier|public
name|String
name|getRepoId
parameter_list|()
block|{
return|return
name|repoId
return|;
block|}
specifier|public
name|void
name|setRepoId
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
name|this
operator|.
name|repoId
operator|=
name|repoId
expr_stmt|;
block|}
comment|//    protected AbstractRepositoryConfiguration getRepository()
comment|//    {
comment|//        return repository;
comment|//    }
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
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
if|if
condition|(
name|getRepoId
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// TODO: this is not right. It needs to change based on method. But is this really the right way to restrict this area?
comment|// TODO: not right. We only care about this permission on managed repositories. Otherwise, it's configuration
name|bundle
operator|.
name|addRequiredAuthorization
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_EDIT_REPOSITORY
argument_list|,
name|getRepoId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
block|}
return|return
name|bundle
return|;
block|}
block|}
end_class

end_unit

