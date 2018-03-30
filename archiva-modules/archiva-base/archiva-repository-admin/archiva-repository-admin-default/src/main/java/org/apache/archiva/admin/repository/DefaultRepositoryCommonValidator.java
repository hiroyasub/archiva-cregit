begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
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
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|RepositoryCommonValidator
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|AbstractRepository
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|archiva
operator|.
name|redback
operator|.
name|components
operator|.
name|registry
operator|.
name|Registry
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
name|redback
operator|.
name|components
operator|.
name|scheduler
operator|.
name|CronExpressionValidator
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
name|commons
operator|.
name|validator
operator|.
name|GenericValidator
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
name|Service
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
name|inject
operator|.
name|Named
import|;
end_import

begin_comment
comment|/**  * apply basic repository validation : id and name.  * Check if already exists.  *  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|Service
specifier|public
class|class
name|DefaultRepositoryCommonValidator
implements|implements
name|RepositoryCommonValidator
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"commons-configuration"
argument_list|)
specifier|private
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|redback
operator|.
name|components
operator|.
name|registry
operator|.
name|Registry
name|registry
decl_stmt|;
comment|/**      * @param abstractRepository      * @param update             in update mode if yes already exists won't be check      * @throws RepositoryAdminException      */
annotation|@
name|Override
specifier|public
name|void
name|basicValidation
parameter_list|(
name|AbstractRepository
name|abstractRepository
parameter_list|,
name|boolean
name|update
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|String
name|repoId
init|=
name|abstractRepository
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|update
condition|)
block|{
if|if
condition|(
name|config
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add new repository with id ["
operator|+
name|repoId
operator|+
literal|"], that id already exists as a managed repository."
argument_list|)
throw|;
block|}
if|else if
condition|(
name|config
operator|.
name|getRepositoryGroupsAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add new repository with id ["
operator|+
name|repoId
operator|+
literal|"], that id already exists as a repository group."
argument_list|)
throw|;
block|}
if|else if
condition|(
name|config
operator|.
name|getRemoteRepositoriesAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add new repository with id ["
operator|+
name|repoId
operator|+
literal|"], that id already exists as a remote repository."
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Repository ID cannot be empty."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|GenericValidator
operator|.
name|matchRegexp
argument_list|(
name|repoId
argument_list|,
name|REPOSITORY_ID_VALID_EXPRESSION
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid repository ID. Identifier must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
throw|;
block|}
name|String
name|name
init|=
name|abstractRepository
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"repository name cannot be empty"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|GenericValidator
operator|.
name|matchRegexp
argument_list|(
name|name
argument_list|,
name|REPOSITORY_NAME_VALID_EXPRESSION
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid repository name. Repository Name must only contain alphanumeric characters, white-spaces(' '), "
operator|+
literal|"forward-slashes(/), open-parenthesis('('), close-parenthesis(')'),  underscores(_), dots(.), and dashes(-)."
argument_list|)
throw|;
block|}
block|}
comment|/**      * validate cronExpression and location format      *      * @param managedRepository      * @since 1.4-M2      */
annotation|@
name|Override
specifier|public
name|void
name|validateManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|String
name|cronExpression
init|=
name|managedRepository
operator|.
name|getCronExpression
argument_list|()
decl_stmt|;
comment|// FIXME : olamy can be empty to avoid scheduled scan ?
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|cronExpression
argument_list|)
condition|)
block|{
name|CronExpressionValidator
name|validator
init|=
operator|new
name|CronExpressionValidator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|validator
operator|.
name|validate
argument_list|(
name|cronExpression
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid cron expression."
argument_list|,
literal|"cronExpression"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Cron expression cannot be empty."
argument_list|)
throw|;
block|}
name|String
name|repoLocation
init|=
name|removeExpressions
argument_list|(
name|managedRepository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|GenericValidator
operator|.
name|matchRegexp
argument_list|(
name|repoLocation
argument_list|,
name|ManagedRepositoryAdmin
operator|.
name|REPOSITORY_LOCATION_VALID_EXPRESSION
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid repository location. Directory must only contain alphanumeric characters, equals(=), question-marks(?), "
operator|+
literal|"exclamation-points(!), ampersands(&amp;), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|,
literal|"location"
argument_list|)
throw|;
block|}
block|}
comment|/**      * replace some interpolations ${appserver.base} with correct values      *      * @param directory      * @return      */
annotation|@
name|Override
specifier|public
name|String
name|removeExpressions
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|String
name|value
init|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|directory
argument_list|,
literal|"${appserver.base}"
argument_list|,
name|getRegistry
argument_list|()
operator|.
name|getString
argument_list|(
literal|"appserver.base"
argument_list|,
literal|"${appserver.base}"
argument_list|)
argument_list|)
decl_stmt|;
name|value
operator|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|value
argument_list|,
literal|"${appserver.home}"
argument_list|,
name|getRegistry
argument_list|()
operator|.
name|getString
argument_list|(
literal|"appserver.home"
argument_list|,
literal|"${appserver.home}"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
specifier|public
name|ArchivaConfiguration
name|getArchivaConfiguration
parameter_list|()
block|{
return|return
name|archivaConfiguration
return|;
block|}
specifier|public
name|void
name|setArchivaConfiguration
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
block|{
name|this
operator|.
name|archivaConfiguration
operator|=
name|archivaConfiguration
expr_stmt|;
block|}
specifier|public
name|Registry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
specifier|public
name|void
name|setRegistry
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|redback
operator|.
name|components
operator|.
name|registry
operator|.
name|Registry
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
block|}
end_class

end_unit

