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
name|web
operator|.
name|actionmapper
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|webwork
operator|.
name|dispatcher
operator|.
name|mapper
operator|.
name|ActionMapping
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|webwork
operator|.
name|dispatcher
operator|.
name|mapper
operator|.
name|DefaultActionMapper
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryProxyActionMapper
extends|extends
name|DefaultActionMapper
block|{
comment|/**      * logger instance      */
specifier|protected
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RepositoryProxyActionMapper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|configFileName
init|=
literal|"maven-proxy-complete.conf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|defaultProxyAction
init|=
literal|"proxy"
decl_stmt|;
comment|/**      * the keyword that will be checked on the http request to determine if proxy      * is requested      *<p/>      * the default prefix is "/proxy/"      */
specifier|private
name|String
name|prefix
init|=
literal|"/proxy/"
decl_stmt|;
specifier|private
name|String
name|requestedArtifact
decl_stmt|;
name|String
name|configFile
init|=
literal|null
decl_stmt|;
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
specifier|public
name|String
name|getRequestedArtifact
parameter_list|()
block|{
return|return
name|requestedArtifact
return|;
block|}
specifier|public
name|ActionMapping
name|getDefaultActionMapping
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|ActionMapping
name|mapping
init|=
name|super
operator|.
name|getMapping
argument_list|(
name|request
argument_list|)
decl_stmt|;
return|return
name|mapping
return|;
block|}
specifier|public
name|void
name|setConfigfile
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|configFile
operator|=
name|fileName
expr_stmt|;
block|}
comment|/**      * only process the request that matches the prefix all other request      * will be hand over to the default action mapper      *<p/>      * if the configuration file is missing the request will also be channeled      * to the default action mapper      */
specifier|public
name|ActionMapping
name|getMapping
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|Properties
name|config
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|request
operator|.
name|getServletPath
argument_list|()
decl_stmt|;
name|URL
name|configURL
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|configFileName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|configURL
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|configFile
operator|==
literal|null
operator|)
condition|)
block|{
name|configFile
operator|=
name|configURL
operator|.
name|getFile
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|configFile
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|config
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|configFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"[config error] "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getDefaultActionMapping
argument_list|(
name|request
argument_list|)
return|;
block|}
if|if
condition|(
name|config
operator|.
name|getProperty
argument_list|(
literal|"prefix"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|prefix
operator|=
literal|"/"
operator|+
name|config
operator|.
name|getProperty
argument_list|(
literal|"prefix"
argument_list|)
operator|+
literal|"/"
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"prefix : "
operator|+
name|prefix
argument_list|)
expr_stmt|;
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|requestedArtifact
operator|=
name|uri
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|requestedArtifact
operator|==
literal|null
operator|)
operator|||
operator|(
name|requestedArtifact
operator|.
name|length
argument_list|()
operator|<
literal|0
operator|)
condition|)
block|{
return|return
name|getDefaultActionMapping
argument_list|(
name|request
argument_list|)
return|;
block|}
name|HashMap
name|parameterMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameterMap
operator|.
name|put
argument_list|(
literal|"requestedFile"
argument_list|,
name|requestedArtifact
argument_list|)
expr_stmt|;
name|parameterMap
operator|.
name|put
argument_list|(
literal|"configFile"
argument_list|,
name|configFile
argument_list|)
expr_stmt|;
return|return
operator|new
name|ActionMapping
argument_list|(
name|defaultProxyAction
argument_list|,
literal|"/"
argument_list|,
literal|""
argument_list|,
name|parameterMap
argument_list|)
return|;
block|}
return|return
name|getDefaultActionMapping
argument_list|(
name|request
argument_list|)
return|;
block|}
block|}
end_class

end_unit

