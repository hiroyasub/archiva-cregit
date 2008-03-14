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
name|DAVMultiStatus
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_comment
comment|/**  *<p><a href="http://www.rfc-editor.org/rfc/rfc2518.txt">WebDAV</a>  *<code>MOVE</code> metohd implementation.</p>   *  * @author<a href="http://could.it/">Pier Fumagalli</a>  */
end_comment

begin_class
specifier|public
class|class
name|MOVE
implements|implements
name|DAVMethod
block|{
comment|/**      *<p>Create a new {@link MOVE} instance.</p>      */
specifier|public
name|MOVE
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      *<p>Process the<code>MOVE</code> method.</p>      */
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
name|URI
name|target
init|=
name|transaction
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
throw|throw
operator|new
name|DAVException
argument_list|(
literal|412
argument_list|,
literal|"No destination"
argument_list|)
throw|;
name|DAVResource
name|dest
init|=
name|resource
operator|.
name|getRepository
argument_list|()
operator|.
name|getResource
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|int
name|depth
init|=
name|transaction
operator|.
name|getDepth
argument_list|()
decl_stmt|;
name|boolean
name|recursive
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|depth
operator|==
literal|0
condition|)
block|{
name|recursive
operator|=
literal|false
expr_stmt|;
block|}
if|else if
condition|(
name|depth
operator|==
name|DAVTransaction
operator|.
name|INFINITY
condition|)
block|{
name|recursive
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|DAVException
argument_list|(
literal|412
argument_list|,
literal|"Invalid Depth specified"
argument_list|)
throw|;
block|}
try|try
block|{
name|int
name|status
decl_stmt|;
if|if
condition|(
operator|!
name|dest
operator|.
name|isNull
argument_list|()
operator|&&
operator|!
name|transaction
operator|.
name|getOverwrite
argument_list|()
condition|)
block|{
name|status
operator|=
literal|412
expr_stmt|;
comment|// MOVE-on-existing should fail with 412
block|}
else|else
block|{
name|resource
operator|.
name|move
argument_list|(
name|dest
argument_list|,
name|transaction
operator|.
name|getOverwrite
argument_list|()
argument_list|,
name|recursive
argument_list|)
expr_stmt|;
if|if
condition|(
name|transaction
operator|.
name|getOverwrite
argument_list|()
condition|)
block|{
name|status
operator|=
literal|204
expr_stmt|;
comment|// No Content
block|}
else|else
block|{
name|status
operator|=
literal|201
expr_stmt|;
comment|// Created
block|}
block|}
name|transaction
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DAVMultiStatus
name|multistatus
parameter_list|)
block|{
name|multistatus
operator|.
name|write
argument_list|(
name|transaction
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

