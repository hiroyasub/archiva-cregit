begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
operator|.
name|configuration
operator|.
name|ProxyConfiguration
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
name|PlexusContainer
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
name|codehaus
operator|.
name|plexus
operator|.
name|context
operator|.
name|Context
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
name|context
operator|.
name|ContextException
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
name|Contextualizable
import|;
end_import

begin_comment
comment|/**  * Factory class for creating ProxyManager instances.  The usage of a factory ensures that the created instance will  * have the necessary configuration  *  * @author Edwin Punzalan  * @plexus.component role="org.apache.maven.repository.proxy.ProxyManagerFactory"  */
end_comment

begin_class
specifier|public
class|class
name|ProxyManagerFactory
implements|implements
name|Contextualizable
block|{
specifier|public
specifier|static
name|String
name|ROLE
init|=
literal|"org.apache.maven.repository.proxy.ProxyManagerFactory"
decl_stmt|;
specifier|private
name|PlexusContainer
name|container
decl_stmt|;
comment|/**      * Used to create a ProxyManager instance of a certain type with a configuration to base its behavior      *      * @param proxy_type The ProxyManager repository type      * @param config     The ProxyConfiguration to describe the behavior of the proxy instance      * @return The ProxyManager instance of type proxy_type with ProxyConfiguration config      * @throws ComponentLookupException when the factory fails to create the ProxyManager instance      */
specifier|public
name|ProxyManager
name|getProxyManager
parameter_list|(
name|String
name|proxy_type
parameter_list|,
name|ProxyConfiguration
name|config
parameter_list|)
throws|throws
name|ComponentLookupException
block|{
name|ProxyManager
name|proxy
init|=
operator|(
name|ProxyManager
operator|)
name|container
operator|.
name|lookup
argument_list|(
name|ProxyManager
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|config
operator|.
name|setLayout
argument_list|(
name|proxy_type
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
name|proxy
return|;
block|}
comment|/**      * @see org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable#contextualize(org.codehaus.plexus.context.Context)      */
specifier|public
name|void
name|contextualize
parameter_list|(
name|Context
name|context
parameter_list|)
throws|throws
name|ContextException
block|{
name|container
operator|=
operator|(
name|PlexusContainer
operator|)
name|context
operator|.
name|get
argument_list|(
name|PlexusConstants
operator|.
name|PLEXUS_KEY
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

