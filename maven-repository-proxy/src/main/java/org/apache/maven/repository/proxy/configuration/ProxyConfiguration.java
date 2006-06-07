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
operator|.
name|configuration
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
name|repository
operator|.
name|ProxyRepository
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
name|proxy
operator|.
name|ProxyInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
comment|/**  * Class to represent the configuration file for the proxy  *  * @author Edwin Punzalan  * @plexus.component role="org.apache.maven.repository.proxy.configuration.ProxyConfiguration"  * @todo investigate how these should be set - probably plexus configuration [!]  */
end_comment

begin_class
specifier|public
class|class
name|ProxyConfiguration
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|ProxyConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
name|List
name|repositories
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|cachePath
decl_stmt|;
specifier|private
name|String
name|layout
decl_stmt|;
specifier|private
name|ProxyInfo
name|httpProxy
decl_stmt|;
comment|/**      * Used to set the location where the proxy should cache the configured repositories      *      * @param path      */
specifier|public
name|void
name|setRepositoryCachePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|cachePath
operator|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
comment|/**      * Used to retrieved the absolute path of the repository cache      *      * @return path to the proxy cache      */
specifier|public
name|String
name|getRepositoryCachePath
parameter_list|()
block|{
return|return
name|cachePath
return|;
block|}
specifier|public
name|void
name|setHttpProxy
parameter_list|(
name|ProxyInfo
name|httpProxy
parameter_list|)
block|{
name|this
operator|.
name|httpProxy
operator|=
name|httpProxy
expr_stmt|;
block|}
specifier|public
name|void
name|setHttpProxy
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|ProxyInfo
name|proxyInfo
init|=
operator|new
name|ProxyInfo
argument_list|()
decl_stmt|;
name|proxyInfo
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|httpProxy
operator|=
name|proxyInfo
expr_stmt|;
block|}
specifier|public
name|void
name|setHttpProxy
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|setHttpProxy
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|httpProxy
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|httpProxy
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setHttpProxy
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|ntlmHost
parameter_list|,
name|String
name|ntlmDomain
parameter_list|)
block|{
name|setHttpProxy
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|httpProxy
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|httpProxy
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|httpProxy
operator|.
name|setNtlmHost
argument_list|(
name|ntlmHost
argument_list|)
expr_stmt|;
name|httpProxy
operator|.
name|setNtlmDomain
argument_list|(
name|ntlmDomain
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ProxyInfo
name|getHttpProxy
parameter_list|()
block|{
return|return
name|httpProxy
return|;
block|}
comment|/**      * Used to add proxied repositories.      *      * @param repository the repository to be proxied      */
specifier|public
name|void
name|addRepository
parameter_list|(
name|ProxyRepository
name|repository
parameter_list|)
block|{
name|repositories
operator|.
name|add
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
comment|/**      * Used to retrieve an unmodifyable list of proxied repositories. They returned list determines the search sequence      * for retrieving artifacts.      *      * @return a list of ProxyRepository objects representing proxied repositories      */
specifier|public
name|List
name|getRepositories
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|repositories
argument_list|)
return|;
block|}
comment|/**      * Used to set the list of repositories to be proxied.  This replaces any repositories already added to this      * configuraion instance.  Useful for re-arranging an existing proxied list.      *      * @param repositories      */
specifier|public
name|void
name|setRepositories
parameter_list|(
name|List
name|repositories
parameter_list|)
block|{
name|this
operator|.
name|repositories
operator|=
name|repositories
expr_stmt|;
block|}
specifier|public
name|String
name|getLayout
parameter_list|()
block|{
if|if
condition|(
name|layout
operator|==
literal|null
condition|)
block|{
name|layout
operator|=
literal|"default"
expr_stmt|;
block|}
return|return
name|layout
return|;
block|}
specifier|public
name|void
name|setLayout
parameter_list|(
name|String
name|layout
parameter_list|)
block|{
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
block|}
block|}
end_class

end_unit

