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
name|util
operator|.
name|encoding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_comment
comment|/**  *<p>The {@link EncodingAware} interface describes an {@link Object} aware  * of multiple encodings existing withing the platform.</p>  *  * @author<a href="http://could.it/">Pier Fumagalli</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|EncodingAware
block|{
comment|/**<p>The default encoding is specified as being<code>UTF-8</code>.</p> */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ENCODING
init|=
literal|"UTF-8"
decl_stmt|;
comment|/**<p>The platform encoding is evaluated at runtime from the JVM.</p> */
specifier|public
specifier|static
specifier|final
name|String
name|PLATFORM_ENCODING
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
operator|.
name|getEncoding
argument_list|()
decl_stmt|;
block|}
end_interface

end_unit

