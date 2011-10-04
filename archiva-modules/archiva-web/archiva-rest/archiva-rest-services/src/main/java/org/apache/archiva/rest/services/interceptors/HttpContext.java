begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
operator|.
name|interceptors
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|Serializable
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
specifier|public
class|class
name|HttpContext
implements|implements
name|Serializable
block|{
specifier|private
name|HttpServletRequest
name|httpServletRequest
decl_stmt|;
specifier|public
name|HttpContext
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|HttpServletRequest
name|getHttpServletRequest
parameter_list|()
block|{
return|return
name|httpServletRequest
return|;
block|}
specifier|public
name|HttpContext
name|setHttpServletRequest
parameter_list|(
name|HttpServletRequest
name|httpServletRequest
parameter_list|)
block|{
name|this
operator|.
name|httpServletRequest
operator|=
name|httpServletRequest
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

