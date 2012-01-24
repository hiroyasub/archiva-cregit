begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|common
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
name|maven
operator|.
name|wagon
operator|.
name|Wagon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"wagonFactory"
argument_list|)
specifier|public
class|class
name|DefaultWagonFactory
implements|implements
name|WagonFactory
block|{
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|DebugTransferListener
name|debugTransferListener
init|=
operator|new
name|DebugTransferListener
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|DefaultWagonFactory
parameter_list|(
name|PlexusSisuBridge
name|plexusSisuBridge
parameter_list|,
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|plexusSisuBridge
operator|=
name|plexusSisuBridge
expr_stmt|;
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
specifier|public
name|Wagon
name|getWagon
parameter_list|(
name|String
name|protocol
parameter_list|)
throws|throws
name|WagonFactoryException
block|{
try|try
block|{
comment|// with sisu inject bridge hint is file or http
comment|// so remove wagon#
comment|//protocol = StringUtils.remove( protocol, "wagon#" );
comment|// spring beans will be named wagon#protocol (http, https, file )
name|protocol
operator|=
name|StringUtils
operator|.
name|startsWith
argument_list|(
name|protocol
argument_list|,
literal|"wagon#"
argument_list|)
condition|?
name|protocol
else|:
literal|"wagon#"
operator|+
name|protocol
expr_stmt|;
comment|//Wagon wagon = plexusSisuBridge.lookup( Wagon.class, protocol );
name|Wagon
name|wagon
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|protocol
argument_list|,
name|Wagon
operator|.
name|class
argument_list|)
decl_stmt|;
name|wagon
operator|.
name|addTransferListener
argument_list|(
name|debugTransferListener
argument_list|)
expr_stmt|;
return|return
name|wagon
return|;
block|}
comment|//catch ( PlexusSisuBridgeException e )
catch|catch
parameter_list|(
name|BeansException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WagonFactoryException
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
block|}
end_class

end_unit

