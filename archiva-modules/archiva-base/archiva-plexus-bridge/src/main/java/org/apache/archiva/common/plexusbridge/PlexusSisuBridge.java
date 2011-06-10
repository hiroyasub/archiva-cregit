begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|plexusbridge
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|DefaultContainerConfiguration
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
name|DefaultPlexusContainer
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
name|PlexusConstants
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
name|PlexusContainerException
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
name|classworlds
operator|.
name|ClassWorld
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
name|classworlds
operator|.
name|realm
operator|.
name|ClassRealm
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
name|component
operator|.
name|repository
operator|.
name|exception
operator|.
name|ComponentLookupException
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
name|stereotype
operator|.
name|Service
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
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

begin_comment
comment|/**  * Simple component which will initiate the plexus shim component  * to see plexus components inside a guice container.<br/>  * So move all of this here to be able to change quickly if needed.  *  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"plexusSisuBridge"
argument_list|)
specifier|public
class|class
name|PlexusSisuBridge
block|{
specifier|private
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
name|boolean
name|containerAutoWiring
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|containerClassPathScanning
init|=
name|PlexusConstants
operator|.
name|SCANNING_ON
decl_stmt|;
specifier|private
name|String
name|containerComponentVisibility
init|=
name|PlexusConstants
operator|.
name|REALM_VISIBILITY
decl_stmt|;
specifier|private
name|URL
name|overridingComponentsXml
decl_stmt|;
specifier|private
name|DefaultPlexusContainer
name|plexusContainer
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|PlexusSisuBridgeException
block|{
name|DefaultContainerConfiguration
name|conf
init|=
operator|new
name|DefaultContainerConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setAutoWiring
argument_list|(
name|containerAutoWiring
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setClassPathScanning
argument_list|(
name|containerClassPathScanning
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setComponentVisibility
argument_list|(
name|containerComponentVisibility
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setContainerConfigurationURL
argument_list|(
name|overridingComponentsXml
argument_list|)
expr_stmt|;
name|ClassWorld
name|classWorld
init|=
operator|new
name|ClassWorld
argument_list|()
decl_stmt|;
name|ClassLoader
name|tccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|ClassRealm
name|classRealm
init|=
operator|new
name|ClassRealm
argument_list|(
name|classWorld
argument_list|,
literal|"maven"
argument_list|,
name|tccl
argument_list|)
decl_stmt|;
comment|// olamy hackhish but plexus-sisu need a URLClassLoader with URL filled
if|if
condition|(
name|tccl
operator|instanceof
name|URLClassLoader
condition|)
block|{
name|URL
index|[]
name|urls
init|=
operator|(
operator|(
name|URLClassLoader
operator|)
name|tccl
operator|)
operator|.
name|getURLs
argument_list|()
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
name|urls
control|)
block|{
name|classRealm
operator|.
name|addURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
name|conf
operator|.
name|setRealm
argument_list|(
name|classRealm
argument_list|)
expr_stmt|;
comment|//conf.setClassWorld( classWorld );
name|ClassLoader
name|ori
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classRealm
argument_list|)
expr_stmt|;
name|plexusContainer
operator|=
operator|new
name|DefaultPlexusContainer
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PlexusContainerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PlexusSisuBridgeException
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
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|ori
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|URL
index|[]
name|getClassLoaderURLs
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
try|try
block|{
comment|// can be WebappClassLoader when using tomcat maven plugin
comment|//java.net.URL[] getURLs
name|Method
name|method
init|=
name|classLoader
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getURLs"
argument_list|,
operator|new
name|Class
index|[]
block|{ }
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|URL
index|[]
operator|)
name|method
operator|.
name|invoke
argument_list|(
name|classLoader
argument_list|,
literal|null
argument_list|)
return|;
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
name|info
argument_list|(
literal|"ignore issue trying to find url[] from classloader {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|URL
index|[]
block|{ }
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
try|try
block|{
return|return
name|plexusContainer
operator|.
name|lookup
argument_list|(
name|clazz
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ComponentLookupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PlexusSisuBridgeException
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
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|String
name|hint
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
try|try
block|{
return|return
name|plexusContainer
operator|.
name|lookup
argument_list|(
name|clazz
argument_list|,
name|hint
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ComponentLookupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PlexusSisuBridgeException
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
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|lookupList
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
try|try
block|{
return|return
name|plexusContainer
operator|.
name|lookupList
argument_list|(
name|clazz
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ComponentLookupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PlexusSisuBridgeException
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
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|lookupMap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
try|try
block|{
return|return
name|plexusContainer
operator|.
name|lookupMap
argument_list|(
name|clazz
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ComponentLookupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PlexusSisuBridgeException
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

