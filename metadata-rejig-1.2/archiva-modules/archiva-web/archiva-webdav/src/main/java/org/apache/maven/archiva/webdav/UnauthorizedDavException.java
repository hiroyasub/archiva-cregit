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
name|webdav
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
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavException
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
name|HttpServletResponse
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:james@atlassian.com">James William Dumay</a>  */
end_comment

begin_class
specifier|public
class|class
name|UnauthorizedDavException
extends|extends
name|DavException
block|{
specifier|final
specifier|private
name|String
name|repositoryName
decl_stmt|;
specifier|public
name|UnauthorizedDavException
parameter_list|(
name|String
name|repositoryName
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|repositoryName
operator|=
name|repositoryName
expr_stmt|;
block|}
specifier|public
name|String
name|getRepositoryName
parameter_list|()
block|{
return|return
name|repositoryName
return|;
block|}
block|}
end_class

end_unit

