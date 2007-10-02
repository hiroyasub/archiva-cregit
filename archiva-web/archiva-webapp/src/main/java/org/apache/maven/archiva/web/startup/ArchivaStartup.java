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
name|logging
operator|.
name|AbstractLogEnabled
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

begin_comment
comment|/**  * ArchivaStartup - the startup of all archiva features in a deterministic order.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component   *              role="org.apache.maven.archiva.web.startup.ArchivaStartup"  *              role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaStartup
extends|extends
name|AbstractLogEnabled
implements|implements
name|Initializable
block|{
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|ConfigurationSynchronization
name|configSync
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|ResolverFactoryInit
name|resolverFactory
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|ArchivaTaskScheduler
name|taskScheduler
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|Banner
operator|.
name|display
argument_list|(
name|getLogger
argument_list|()
argument_list|,
name|ArchivaVersion
operator|.
name|determineVersion
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|configSync
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
block|}
catch|catch
parameter_list|(
name|ArchivaException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InitializationException
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

