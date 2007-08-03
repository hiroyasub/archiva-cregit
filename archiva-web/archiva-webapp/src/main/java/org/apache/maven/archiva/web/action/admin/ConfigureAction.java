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
name|com
operator|.
name|opensymphony
operator|.
name|xwork
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
name|indexer
operator|.
name|RepositoryIndexException
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
name|indexer
operator|.
name|RepositoryIndexSearchException
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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Configures the application.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="configureAction"  */
end_comment

begin_class
specifier|public
class|class
name|ConfigureAction
extends|extends
name|PlexusActionSupport
implements|implements
name|ModelDriven
implements|,
name|Preparable
implements|,
name|Validateable
implements|,
name|SecureAction
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * The configuration.      */
specifier|private
name|Configuration
name|configuration
decl_stmt|;
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"validate()"
argument_list|)
expr_stmt|;
comment|//validate cron expression
block|}
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
throws|,
name|RepositoryIndexSearchException
throws|,
name|InvalidConfigurationException
throws|,
name|RegistryException
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"execute()"
argument_list|)
expr_stmt|;
comment|// TODO: if this didn't come from the form, go to configure.action instead of going through with re-saving what was just loaded
comment|// TODO: if this is changed, do we move the index or recreate it?
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
comment|// TODO: if the repository has changed, we need to check if indexing is needed!
name|addActionMessage
argument_list|(
literal|"Successfully saved configuration"
argument_list|)
expr_stmt|;
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
block|}
return|return
name|SUCCESS
return|;
block|}
comment|//    public String input()
comment|//    {
comment|////        String[] cronEx = configuration.getDataRefreshCronExpression().split( " " );
comment|//        String[] cronEx = new String[]{"0","0","*","*","*","*","*"};
comment|//        int i = 0;
comment|//
comment|//        while ( i< cronEx.length )
comment|//        {
comment|//            switch ( i )
comment|//            {
comment|//                case 0:
comment|//                    second = cronEx[i];
comment|//                    break;
comment|//                case 1:
comment|//                    minute = cronEx[i];
comment|//                    break;
comment|//                case 2:
comment|//                    hour = cronEx[i];
comment|//                    break;
comment|//                case 3:
comment|//                    dayOfMonth = cronEx[i];
comment|//                    break;
comment|//                case 4:
comment|//                    month = cronEx[i];
comment|//                    break;
comment|//                case 5:
comment|//                    dayOfWeek = cronEx[i];
comment|//                    break;
comment|//                case 6:
comment|//                    year = cronEx[i];
comment|//                    break;
comment|//            }
comment|//            i++;
comment|//        }
comment|//
comment|////        if ( activeRepositories.getLastDataRefreshTime() != 0 )
comment|////        {
comment|////            lastIndexingTime = new Date( activeRepositories.getLastDataRefreshTime() ).toString();
comment|////        }
comment|////        else
comment|//        {
comment|//            lastIndexingTime = "Never been run.";
comment|//        }
comment|//
comment|//        return INPUT;
comment|//    }
specifier|public
name|Object
name|getModel
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
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
block|}
comment|//    private String getCronExpression()
comment|//    {
comment|//        return ( second + " " + minute + " " + hour + " " + dayOfMonth + " " + month + " " + dayOfWeek + " " +
comment|//            year ).trim();
comment|//    }
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
block|}
end_class

end_unit

