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
comment|/**  * Base action for repository removal actions.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDeleteRepositoryAction
extends|extends
name|PlexusActionSupport
implements|implements
name|SecureAction
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * The repository ID to lookup when editing a repository.      */
specifier|protected
name|String
name|repoId
decl_stmt|;
comment|/**      * Which operation to select.      */
specifier|private
name|String
name|operation
init|=
literal|"unmodified"
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="archiva"      */
specifier|protected
name|RoleProfileManager
name|roleProfileManager
decl_stmt|;
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|IOException
throws|,
name|InvalidConfigurationException
throws|,
name|RegistryException
block|{
comment|// TODO: if this didn't come from the form, go to configure.action instead of going through with re-saving what was just loaded
if|if
condition|(
literal|"delete-entry"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
operator|||
literal|"delete-contents"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|//            AbstractRepositoryConfiguration existingRepository = getRepository( configuration );
comment|//            if ( existingRepository == null )
comment|//            {
comment|//                addActionError( "A repository with that id does not exist" );
comment|//                return ERROR;
comment|//            }
comment|//
comment|//            // TODO: remove from index too!
comment|//
comment|//            removeRepository( configuration, existingRepository );
comment|//
comment|//            archivaConfiguration.save( configuration );
comment|//
comment|//            if ( "delete-contents".equals( operation ) )
comment|//            {
comment|//                removeContents( existingRepository );
comment|//            }
block|}
return|return
name|SUCCESS
return|;
block|}
comment|//    protected abstract void removeContents( AbstractRepositoryConfiguration existingRepository )
comment|//        throws IOException;
comment|//
comment|//    protected abstract AbstractRepositoryConfiguration getRepository( Configuration configuration );
comment|//
comment|//    protected abstract void removeRepository( Configuration configuration,
comment|//                                              AbstractRepositoryConfiguration existingRepository );
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
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
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
if|if
condition|(
name|getRepoId
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// TODO: not right. We only care about this permission on managed repositories. Otherwise, it's configuration
name|bundle
operator|.
name|addRequiredAuthorization
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_DELETE_REPOSITORY
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

