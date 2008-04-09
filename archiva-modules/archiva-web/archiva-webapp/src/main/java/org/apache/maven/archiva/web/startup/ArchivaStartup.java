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
name|javax
operator|.
name|servlet
operator|.
name|ServletContextEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContextListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
name|scheduled
operator|.
name|ArchivaTaskScheduler
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
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
name|spring
operator|.
name|PlexusToSpringUtils
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
name|taskqueue
operator|.
name|execution
operator|.
name|TaskQueueExecutor
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|context
operator|.
name|WebApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|context
operator|.
name|support
operator|.
name|WebApplicationContextUtils
import|;
end_import

begin_comment
comment|/**  * ArchivaStartup - the startup of all archiva features in a deterministic order.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaStartup
implements|implements
name|ServletContextListener
block|{
specifier|public
name|void
name|contextDestroyed
parameter_list|(
name|ServletContextEvent
name|arg0
parameter_list|)
block|{
block|}
specifier|public
name|void
name|contextInitialized
parameter_list|(
name|ServletContextEvent
name|arg0
parameter_list|)
block|{
name|WebApplicationContext
name|wac
init|=
name|WebApplicationContextUtils
operator|.
name|getRequiredWebApplicationContext
argument_list|(
name|arg0
operator|.
name|getServletContext
argument_list|()
argument_list|)
decl_stmt|;
name|SecuritySynchronization
name|securitySync
init|=
operator|(
name|SecuritySynchronization
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|SecuritySynchronization
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|ResolverFactoryInit
name|resolverFactory
init|=
operator|(
name|ResolverFactoryInit
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|ResolverFactoryInit
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|ArchivaTaskScheduler
name|taskScheduler
init|=
operator|(
name|ArchivaTaskScheduler
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|ArchivaTaskScheduler
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|TaskQueueExecutor
name|databaseUpdateQueue
init|=
operator|(
name|TaskQueueExecutor
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|TaskQueueExecutor
operator|.
name|class
argument_list|,
literal|"database-update"
argument_list|)
argument_list|)
decl_stmt|;
name|TaskQueueExecutor
name|repositoryScanningQueue
init|=
operator|(
name|TaskQueueExecutor
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|TaskQueueExecutor
operator|.
name|class
argument_list|,
literal|"repository-scanning"
argument_list|)
argument_list|)
decl_stmt|;
name|Banner
name|banner
init|=
operator|new
name|Banner
argument_list|()
decl_stmt|;
try|try
block|{
name|securitySync
operator|.
name|startup
argument_list|()
expr_stmt|;
name|resolverFactory
operator|.
name|startup
argument_list|()
expr_stmt|;
name|taskScheduler
operator|.
name|startup
argument_list|()
expr_stmt|;
name|banner
operator|.
name|display
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to properly startup archiva: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

