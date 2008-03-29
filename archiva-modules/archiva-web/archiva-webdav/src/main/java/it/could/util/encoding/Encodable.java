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
name|UnsupportedEncodingException
import|;
end_import

begin_comment
comment|/**  *<p>The {@link Encodable} interface describes an {@link Object} whose  * {@link String} representation can vary depending on the encoding used.</p>  *  * @author<a href="http://could.it/">Pier Fumagalli</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Encodable
extends|extends
name|EncodingAware
block|{
comment|/**      *<p>Return the {@link String} representation of this instance.</p>      *       *<p>This method is equivalent to a call to      * {@link #toString(String) toString}({@link EncodingAware#DEFAULT_ENCODING      * DEFAULT_ENCODING})</p>      */
specifier|public
name|String
name|toString
parameter_list|()
function_decl|;
comment|/**      *<p>Return the {@link String} representation of this instance given      * a specific character encoding.</p>      *       * @throws UnsupportedEncodingException if the specified encoding is not      *                                      supported by the platform.      */
specifier|public
name|String
name|toString
parameter_list|(
name|String
name|encoding
parameter_list|)
throws|throws
name|UnsupportedEncodingException
function_decl|;
block|}
end_interface

end_unit

