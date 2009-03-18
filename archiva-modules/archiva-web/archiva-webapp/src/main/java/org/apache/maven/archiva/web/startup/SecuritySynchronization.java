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
name|startup
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|collections
operator|.
name|CollectionUtils
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
name|common
operator|.
name|ArchivaException
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
name|ConfigurationNames
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
name|RBACManager
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
name|redback
operator|.
name|rbac
operator|.
name|UserAssignment
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
name|role
operator|.
name|RoleManager
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
name|role
operator|.
name|RoleManagerException
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
name|system
operator|.
name|check
operator|.
name|EnvironmentCheck
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
name|users
operator|.
name|UserManager
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
name|Registry
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
name|RegistryListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * ConfigurationSynchronization  *  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.web.startup.SecuritySynchronization"  * role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|SecuritySynchronization
implements|implements
name|RegistryListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SecuritySynchronization
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|RoleManager
name|roleManager
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="cached"      */
specifier|private
name|RBACManager
name|rbacManager
decl_stmt|;
comment|/**      * @plexus.requirement role="org.codehaus.plexus.redback.system.check.EnvironmentCheck"      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|EnvironmentCheck
argument_list|>
name|checkers
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|ConfigurationNames
operator|.
name|isManagedRepositories
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|synchConfiguration
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|private
name|void
name|synchConfiguration
parameter_list|(
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|repos
parameter_list|)
block|{
comment|// NOTE: Remote Repositories do not have roles or security placed around them.
for|for
control|(
name|ManagedRepositoryConfiguration
name|repoConfig
range|:
name|repos
control|)
block|{
comment|// manage roles for repositories
try|try
block|{
if|if
condition|(
operator|!
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|roleManager
operator|.
name|verifyTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|roleManager
operator|.
name|createTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|roleManager
operator|.
name|verifyTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RoleManagerException
name|e
parameter_list|)
block|{
comment|// Log error.
name|log
operator|.
name|error
argument_list|(
literal|"Unable to create roles for configured repositories: "
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
block|}
block|}
specifier|public
name|void
name|startup
parameter_list|()
throws|throws
name|ArchivaException
block|{
name|executeEnvironmentChecks
argument_list|()
expr_stmt|;
name|synchConfiguration
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|archivaConfiguration
operator|.
name|isDefaulted
argument_list|()
condition|)
block|{
name|assignRepositoryObserverToGuestUser
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|executeEnvironmentChecks
parameter_list|()
throws|throws
name|ArchivaException
block|{
if|if
condition|(
operator|(
name|checkers
operator|==
literal|null
operator|)
operator|||
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|checkers
operator|.
name|values
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaException
argument_list|(
literal|"Unable to initialize the Redback Security Environment, "
operator|+
literal|"no Environment Check components found."
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|violations
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|EnvironmentCheck
argument_list|>
name|entry
range|:
name|checkers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|EnvironmentCheck
name|check
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|v
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|check
operator|.
name|validateEnvironment
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Environment Check: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" -> "
operator|+
name|v
operator|.
name|size
argument_list|()
operator|+
literal|" violation(s)"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|s
range|:
name|v
control|)
block|{
name|violations
operator|.
name|add
argument_list|(
literal|"["
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"] "
operator|+
name|s
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|violations
argument_list|)
condition|)
block|{
name|StringBuffer
name|msg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"EnvironmentCheck Failure.\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"======================================================================\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|" ENVIRONMENT FAILURE !! \n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|violation
range|:
name|violations
control|)
block|{
name|msg
operator|.
name|append
argument_list|(
name|violation
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"======================================================================"
argument_list|)
expr_stmt|;
name|log
operator|.
name|error
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaException
argument_list|(
literal|"Unable to initialize Redback Security Environment, ["
operator|+
name|violations
operator|.
name|size
argument_list|()
operator|+
literal|"] violation(s) encountered, See log for details."
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|assignRepositoryObserverToGuestUser
parameter_list|(
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|repos
parameter_list|)
block|{
for|for
control|(
name|ManagedRepositoryConfiguration
name|repoConfig
range|:
name|repos
control|)
block|{
name|String
name|repoId
init|=
name|repoConfig
operator|.
name|getId
argument_list|()
decl_stmt|;
name|String
name|principal
init|=
name|UserManager
operator|.
name|GUEST_USERNAME
decl_stmt|;
try|try
block|{
name|UserAssignment
name|ua
decl_stmt|;
if|if
condition|(
name|rbacManager
operator|.
name|userAssignmentExists
argument_list|(
name|principal
argument_list|)
condition|)
block|{
name|ua
operator|=
name|rbacManager
operator|.
name|getUserAssignment
argument_list|(
name|principal
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ua
operator|=
name|rbacManager
operator|.
name|createUserAssignment
argument_list|(
name|principal
argument_list|)
expr_stmt|;
block|}
name|ua
operator|.
name|addRoleName
argument_list|(
name|ArchivaRoleConstants
operator|.
name|toRepositoryObserverRoleName
argument_list|(
name|repoId
argument_list|)
argument_list|)
expr_stmt|;
name|rbacManager
operator|.
name|saveUserAssignment
argument_list|(
name|ua
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RbacManagerException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to add role ["
operator|+
name|ArchivaRoleConstants
operator|.
name|toRepositoryObserverRoleName
argument_list|(
name|repoId
argument_list|)
operator|+
literal|"] to "
operator|+
name|principal
operator|+
literal|" user."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

