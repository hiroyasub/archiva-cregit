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
name|repositories
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
name|xwork2
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
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepositoryException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|stats
operator|.
name|RepositoryStatistics
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|stats
operator|.
name|RepositoryStatisticsManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|security
operator|.
name|common
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
name|ManagedRepositoryConfiguration
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
name|RemoteRepositoryConfiguration
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
name|functors
operator|.
name|RepositoryConfigurationComparator
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
name|AbstractActionSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
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
name|apache
operator|.
name|struts2
operator|.
name|interceptor
operator|.
name|ServletRequestAware
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
name|Collections
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
name|List
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

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
comment|/**  * Shows the Repositories Tab for the administrator.  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"repositoriesAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|RepositoriesAction
extends|extends
name|AbstractActionSupport
implements|implements
name|SecureAction
implements|,
name|ServletRequestAware
implements|,
name|Preparable
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|managedRepositories
decl_stmt|;
specifier|private
name|List
argument_list|<
name|RemoteRepositoryConfiguration
argument_list|>
name|remoteRepositories
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryStatistics
argument_list|>
name|repositoryStatistics
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|repositoryToGroupMap
decl_stmt|;
comment|/**      * Used to construct the repository WebDAV URL in the repository action.      */
specifier|private
name|String
name|baseUrl
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|remoteRepositories
operator|=
operator|new
name|ArrayList
argument_list|<
name|RemoteRepositoryConfiguration
argument_list|>
argument_list|(
name|config
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|managedRepositories
operator|=
operator|new
name|ArrayList
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
argument_list|(
name|config
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryToGroupMap
operator|=
name|config
operator|.
name|getRepositoryToGroupMap
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|managedRepositories
argument_list|,
operator|new
name|RepositoryConfigurationComparator
argument_list|()
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|remoteRepositories
argument_list|,
operator|new
name|RepositoryConfigurationComparator
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryStatistics
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|RepositoryStatistics
argument_list|>
argument_list|()
expr_stmt|;
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
try|try
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepositoryConfiguration
name|repo
range|:
name|managedRepositories
control|)
block|{
name|RepositoryStatistics
name|stats
init|=
literal|null
decl_stmt|;
try|try
block|{
name|stats
operator|=
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|metadataRepository
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Error retrieving statistics for repository "
operator|+
name|repo
operator|.
name|getId
argument_list|()
operator|+
literal|" - consult application logs"
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Error retrieving repository statistics: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|stats
operator|!=
literal|null
condition|)
block|{
name|repositoryStatistics
operator|.
name|put
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|stats
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|getManagedRepositories
parameter_list|()
block|{
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|managedRepositoriesList
init|=
operator|new
name|ArrayList
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepositoryConfiguration
name|repoConfig
range|:
name|managedRepositories
control|)
block|{
if|if
condition|(
operator|!
name|repoConfig
operator|.
name|getId
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"-stage"
argument_list|)
condition|)
block|{
name|managedRepositoriesList
operator|.
name|add
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|managedRepositoriesList
return|;
block|}
specifier|public
name|List
argument_list|<
name|RemoteRepositoryConfiguration
argument_list|>
name|getRemoteRepositories
parameter_list|()
block|{
return|return
name|remoteRepositories
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryStatistics
argument_list|>
name|getRepositoryStatistics
parameter_list|()
block|{
return|return
name|repositoryStatistics
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
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getRepositoryToGroupMap
parameter_list|()
block|{
return|return
name|repositoryToGroupMap
return|;
block|}
block|}
end_class

end_unit

