begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|util
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|PageContext
import|;
end_import

begin_comment
comment|/**  * ContextUtils   *  *  */
end_comment

begin_class
specifier|public
class|class
name|ContextUtils
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|defaultSchemePortMap
decl_stmt|;
static|static
block|{
name|defaultSchemePortMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|defaultSchemePortMap
operator|.
name|put
argument_list|(
literal|"http"
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
literal|80
argument_list|)
argument_list|)
expr_stmt|;
name|defaultSchemePortMap
operator|.
name|put
argument_list|(
literal|"https"
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
literal|443
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Using the page context, get the base url.      *       * @param pageContext the page context to use      * @return the base url with module name.      */
specifier|public
specifier|static
name|String
name|getBaseURL
parameter_list|(
name|PageContext
name|pageContext
parameter_list|)
block|{
return|return
name|getBaseURL
argument_list|(
name|pageContext
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Using the page context, get the base url and append an optional resource name to the end of the provided url.      *       * @param pageContext the page context to use      * @param resource the resource name (or null if no resource name specified)      * @return the base url with resource name.      */
specifier|public
specifier|static
name|String
name|getBaseURL
parameter_list|(
name|PageContext
name|pageContext
parameter_list|,
name|String
name|resource
parameter_list|)
block|{
name|HttpServletRequest
name|request
init|=
operator|(
name|HttpServletRequest
operator|)
name|pageContext
operator|.
name|getRequest
argument_list|()
decl_stmt|;
return|return
name|getBaseURL
argument_list|(
name|request
argument_list|,
name|resource
argument_list|)
return|;
block|}
comment|/**      * Using the http servlet request, get the base url and append an optional resource name to the end of the url.      *       * @param request the request to use      * @param resource the resource name (or null if not resource name should be appended)      * @return the base url with resource name.      */
specifier|public
specifier|static
name|String
name|getBaseURL
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|resource
parameter_list|)
block|{
name|StringBuilder
name|baseUrl
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|baseUrl
operator|.
name|append
argument_list|(
name|request
operator|.
name|getScheme
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"://"
argument_list|)
expr_stmt|;
name|baseUrl
operator|.
name|append
argument_list|(
name|getServerName
argument_list|(
name|request
argument_list|)
argument_list|)
expr_stmt|;
name|baseUrl
operator|.
name|append
argument_list|(
name|request
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|resource
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|baseUrl
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|baseUrl
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
name|baseUrl
operator|.
name|append
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
return|return
name|baseUrl
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|String
name|getServerName
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|String
name|name
init|=
name|request
operator|.
name|getHeader
argument_list|(
literal|"X-Forwarded-Host"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|request
operator|.
name|getServerName
argument_list|()
expr_stmt|;
name|int
name|portnum
init|=
name|request
operator|.
name|getServerPort
argument_list|()
decl_stmt|;
comment|// Only add port if non-standard.
name|Integer
name|defaultPortnum
init|=
operator|(
name|Integer
operator|)
name|defaultSchemePortMap
operator|.
name|get
argument_list|(
name|request
operator|.
name|getScheme
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|defaultPortnum
operator|==
literal|null
operator|)
operator|||
operator|(
name|defaultPortnum
operator|.
name|intValue
argument_list|()
operator|!=
name|portnum
operator|)
condition|)
block|{
name|name
operator|=
name|name
operator|+
literal|":"
operator|+
name|String
operator|.
name|valueOf
argument_list|(
name|portnum
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
else|else
block|{
comment|// respect chains of proxies, return first one (as it's the outermost visible one)
name|String
index|[]
name|hosts
init|=
name|name
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|name
operator|=
name|hosts
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

