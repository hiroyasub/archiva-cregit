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
name|database
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
name|DatabaseScanningConfiguration
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
name|database
operator|.
name|updater
operator|.
name|DatabaseConsumers
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
name|scanning
operator|.
name|AdminRepositoryConsumerComparator
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
name|Collections
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

begin_comment
comment|/**  * DatabaseAction  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="databaseAction"  */
end_comment

begin_class
specifier|public
class|class
name|DatabaseAction
extends|extends
name|PlexusActionSupport
implements|implements
name|Preparable
implements|,
name|SecureAction
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|DatabaseConsumers
name|databaseConsumers
decl_stmt|;
specifier|private
name|String
name|cron
decl_stmt|;
comment|/**      * List of available {@link AdminDatabaseConsumer} objects for unprocessed artifacts.      */
specifier|private
name|List
name|unprocessedConsumers
decl_stmt|;
comment|/**      * List of enabled {@link AdminDatabaseConsumer} objects for unprocessed artifacts.      */
specifier|private
name|List
name|enabledUnprocessedConsumers
decl_stmt|;
comment|/**      * List of {@link AdminDatabaseConsumer} objects for "to cleanup" artifacts.      */
specifier|private
name|List
name|cleanupConsumers
decl_stmt|;
comment|/**      * List of enabled {@link AdminDatabaseConsumer} objects for "to cleanup" artifacts.      */
specifier|private
name|List
name|enabledCleanupConsumers
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|DatabaseScanningConfiguration
name|dbscanning
init|=
name|config
operator|.
name|getDatabaseScanning
argument_list|()
decl_stmt|;
name|this
operator|.
name|cron
operator|=
name|dbscanning
operator|.
name|getCronExpression
argument_list|()
expr_stmt|;
name|AddAdminDatabaseConsumerClosure
name|addAdminDbConsumer
decl_stmt|;
name|addAdminDbConsumer
operator|=
operator|new
name|AddAdminDatabaseConsumerClosure
argument_list|(
name|dbscanning
operator|.
name|getUnprocessedConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|databaseConsumers
operator|.
name|getAvailableUnprocessedConsumers
argument_list|()
argument_list|,
name|addAdminDbConsumer
argument_list|)
expr_stmt|;
name|this
operator|.
name|unprocessedConsumers
operator|=
name|addAdminDbConsumer
operator|.
name|getList
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|this
operator|.
name|unprocessedConsumers
argument_list|,
name|AdminRepositoryConsumerComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|addAdminDbConsumer
operator|=
operator|new
name|AddAdminDatabaseConsumerClosure
argument_list|(
name|dbscanning
operator|.
name|getCleanupConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|databaseConsumers
operator|.
name|getAvailableCleanupConsumers
argument_list|()
argument_list|,
name|addAdminDbConsumer
argument_list|)
expr_stmt|;
name|this
operator|.
name|cleanupConsumers
operator|=
name|addAdminDbConsumer
operator|.
name|getList
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|this
operator|.
name|cleanupConsumers
argument_list|,
name|AdminRepositoryConsumerComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|updateUnprocessedConsumers
parameter_list|()
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDatabaseScanning
argument_list|()
operator|.
name|setUnprocessedConsumers
argument_list|(
name|enabledUnprocessedConsumers
argument_list|)
expr_stmt|;
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
specifier|public
name|String
name|updateCleanupConsumers
parameter_list|()
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDatabaseScanning
argument_list|()
operator|.
name|setCleanupConsumers
argument_list|(
name|enabledCleanupConsumers
argument_list|)
expr_stmt|;
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
specifier|public
name|String
name|updateSchedule
parameter_list|()
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDatabaseScanning
argument_list|()
operator|.
name|setCronExpression
argument_list|(
name|cron
argument_list|)
expr_stmt|;
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
specifier|private
name|String
name|saveConfiguration
parameter_list|()
block|{
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Successfully saved configuration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"Error in saving configuration"
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
catch|catch
parameter_list|(
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
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
name|String
name|getCron
parameter_list|()
block|{
return|return
name|cron
return|;
block|}
specifier|public
name|void
name|setCron
parameter_list|(
name|String
name|cron
parameter_list|)
block|{
name|this
operator|.
name|cron
operator|=
name|cron
expr_stmt|;
block|}
specifier|public
name|List
name|getCleanupConsumers
parameter_list|()
block|{
return|return
name|cleanupConsumers
return|;
block|}
specifier|public
name|List
name|getUnprocessedConsumers
parameter_list|()
block|{
return|return
name|unprocessedConsumers
return|;
block|}
specifier|public
name|List
name|getEnabledUnprocessedConsumers
parameter_list|()
block|{
return|return
name|enabledUnprocessedConsumers
return|;
block|}
specifier|public
name|void
name|setEnabledUnprocessedConsumers
parameter_list|(
name|List
name|enabledUnprocessedConsumers
parameter_list|)
block|{
name|this
operator|.
name|enabledUnprocessedConsumers
operator|=
name|enabledUnprocessedConsumers
expr_stmt|;
block|}
specifier|public
name|List
name|getEnabledCleanupConsumers
parameter_list|()
block|{
return|return
name|enabledCleanupConsumers
return|;
block|}
specifier|public
name|void
name|setEnabledCleanupConsumers
parameter_list|(
name|List
name|enabledCleanupConsumers
parameter_list|)
block|{
name|this
operator|.
name|enabledCleanupConsumers
operator|=
name|enabledCleanupConsumers
expr_stmt|;
block|}
block|}
end_class

end_unit

