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
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|ActionSupport
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
name|proxy
operator|.
name|ProxyException
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
name|proxy
operator|.
name|ProxyManager
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
name|ResourceDoesNotExistException
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
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_comment
comment|/**  * Proxy functionality.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="proxyAction"  */
end_comment

begin_class
specifier|public
class|class
name|ProxyAction
extends|extends
name|ActionSupport
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ProxyManager
name|proxyManager
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
specifier|private
name|String
name|filename
decl_stmt|;
specifier|private
name|String
name|contentType
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NOT_FOUND
init|=
literal|"notFound"
decl_stmt|;
specifier|private
name|InputStream
name|artifactStream
decl_stmt|;
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|ProxyException
block|{
try|try
block|{
name|File
name|file
init|=
name|proxyManager
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|artifactStream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
comment|// TODO: could be better!
name|contentType
operator|=
literal|"application/octet-stream"
expr_stmt|;
name|filename
operator|=
name|file
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
comment|// TODO: set message?
return|return
name|NOT_FOUND
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
comment|// TODO: set message?
return|return
name|NOT_FOUND
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
name|filename
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
specifier|public
name|InputStream
name|getArtifactStream
parameter_list|()
block|{
return|return
name|artifactStream
return|;
block|}
block|}
end_class

end_unit

