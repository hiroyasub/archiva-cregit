begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* ========================================================================== *  *         Copyright (C) 2004-2006, Pier Fumagalli<http://could.it/>         *  *                            All rights reserved.                            *  * ========================================================================== *  *                                                                            *  * Licensed under the  Apache License, Version 2.0  (the "License").  You may *  * not use this file except in compliance with the License.  You may obtain a *  * copy of the License at<http://www.apache.org/licenses/LICENSE-2.0>.       *  *                                                                            *  * Unless  required  by applicable  law or  agreed  to  in writing,  software *  * distributed under the License is distributed on an  "AS IS" BASIS, WITHOUT *  * WARRANTIES OR  CONDITIONS OF ANY KIND, either express or implied.  See the *  * License for the  specific language  governing permissions  and limitations *  * under the License.                                                         *  *                                                                            *  * ========================================================================== */
end_comment

begin_package
package|package
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|methods
package|;
end_package

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVException
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVMethod
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVResource
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVTransaction
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
comment|/**  *<p><a href="http://www.rfc-editor.org/rfc/rfc2518.txt">WebDAV</a>  *<code>MKCOL</code> metohd implementation.</p>   *  * @author<a href="http://could.it/">Pier Fumagalli</a>  */
end_comment

begin_class
specifier|public
class|class
name|MKCOL
implements|implements
name|DAVMethod
block|{
comment|/**      *<p>Create a new {@link MKCOL} instance.</p>      */
specifier|public
name|MKCOL
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      *<p>Process the<code>MKCOL</code> method.</p>      */
specifier|public
name|void
name|process
parameter_list|(
name|DAVTransaction
name|transaction
parameter_list|,
name|DAVResource
name|resource
parameter_list|)
throws|throws
name|IOException
block|{
comment|/* Unsupported media type, we don't want content */
if|if
condition|(
name|transaction
operator|.
name|hasRequestBody
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DAVException
argument_list|(
literal|415
argument_list|,
literal|"No request body allowed in request"
argument_list|)
throw|;
block|}
comment|/* Create the collection */
name|resource
operator|.
name|makeCollection
argument_list|()
expr_stmt|;
name|transaction
operator|.
name|setStatus
argument_list|(
literal|201
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

