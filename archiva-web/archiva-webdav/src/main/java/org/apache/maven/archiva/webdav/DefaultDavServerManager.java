begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|webdav
package|;
end_package

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
name|Collection
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
name|Map
import|;
end_import

begin_comment
comment|/**  * DefaultDavServerManager   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id: DefaultDavServerManager.java 7009 2007-10-25 23:34:43Z joakime $  *   * @plexus.component role="org.apache.maven.archiva.webdav.DavServerManager" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDavServerManager
implements|implements
name|DavServerManager
block|{
comment|/**      * @plexus.requirement role-hint="simple"      */
specifier|private
name|DavServerComponent
name|server
decl_stmt|;
specifier|private
name|Map
name|servers
decl_stmt|;
specifier|public
name|DefaultDavServerManager
parameter_list|()
block|{
name|servers
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
specifier|public
name|DavServerComponent
name|createServer
parameter_list|(
name|String
name|prefix
parameter_list|,
name|File
name|rootDirectory
parameter_list|)
throws|throws
name|DavServerException
block|{
if|if
condition|(
name|servers
operator|.
name|containsKey
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DavServerException
argument_list|(
literal|"Unable to create a new server on a pre-existing prefix ["
operator|+
name|prefix
operator|+
literal|"]"
argument_list|)
throw|;
block|}
name|server
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
if|if
condition|(
name|rootDirectory
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|setRootDirectory
argument_list|(
name|rootDirectory
argument_list|)
expr_stmt|;
block|}
name|servers
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|server
argument_list|)
expr_stmt|;
return|return
name|server
return|;
block|}
specifier|public
name|DavServerComponent
name|getServer
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
return|return
operator|(
name|DavServerComponent
operator|)
name|servers
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeServer
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|servers
operator|.
name|remove
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
name|getServers
parameter_list|()
block|{
return|return
name|servers
operator|.
name|values
argument_list|()
return|;
block|}
specifier|public
name|void
name|removeAllServers
parameter_list|()
block|{
name|servers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

