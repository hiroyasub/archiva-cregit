begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
operator|.
name|util
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridgeException
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
name|index
operator|.
name|NexusIndexer
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
name|index
operator|.
name|context
operator|.
name|IndexingContext
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
name|context
operator|.
name|ApplicationContext
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
specifier|public
class|class
name|MavenIndexerCleaner
implements|implements
name|ServletContextListener
block|{
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|startup
parameter_list|()
block|{
name|plexusSisuBridge
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|PlexusSisuBridge
operator|.
name|class
argument_list|)
expr_stmt|;
name|cleanupIndex
argument_list|()
expr_stmt|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|cleanupIndex
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|contextInitialized
parameter_list|(
name|ServletContextEvent
name|servletContextEvent
parameter_list|)
block|{
try|try
block|{
name|WebApplicationContext
name|wacu
init|=
name|WebApplicationContextUtils
operator|.
name|getRequiredWebApplicationContext
argument_list|(
name|servletContextEvent
operator|.
name|getServletContext
argument_list|()
argument_list|)
decl_stmt|;
name|plexusSisuBridge
operator|=
name|wacu
operator|.
name|getBean
argument_list|(
name|PlexusSisuBridge
operator|.
name|class
argument_list|)
expr_stmt|;
name|cleanupIndex
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
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
specifier|public
name|void
name|contextDestroyed
parameter_list|(
name|ServletContextEvent
name|servletContextEvent
parameter_list|)
block|{
try|try
block|{
name|cleanupIndex
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
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
specifier|public
name|void
name|cleanupIndex
parameter_list|()
block|{
name|log
operator|.
name|info
argument_list|(
literal|"cleanup IndexingContext"
argument_list|)
expr_stmt|;
try|try
block|{
name|NexusIndexer
name|nexusIndexer
init|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|NexusIndexer
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|IndexingContext
name|context
range|:
name|nexusIndexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|nexusIndexer
operator|.
name|removeIndexingContext
argument_list|(
name|context
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"fail to cleanupIndex: {}"
argument_list|,
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
end_class

end_unit

