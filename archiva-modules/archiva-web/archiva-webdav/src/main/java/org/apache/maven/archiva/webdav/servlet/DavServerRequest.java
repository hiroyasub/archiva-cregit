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
operator|.
name|servlet
package|;
end_package

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
name|webdav
operator|.
name|util
operator|.
name|WrappedRepositoryRequest
import|;
end_import

begin_comment
comment|/**  * DavServerRequest  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id: DavServerRequest.java 7073 2007-11-22 04:04:50Z brett $  */
end_comment

begin_interface
specifier|public
interface|interface
name|DavServerRequest
block|{
specifier|public
name|String
name|getPrefix
parameter_list|()
function_decl|;
specifier|public
name|String
name|getLogicalResource
parameter_list|()
function_decl|;
specifier|public
name|void
name|setLogicalResource
parameter_list|(
name|String
name|logicalResource
parameter_list|)
function_decl|;
specifier|public
name|WrappedRepositoryRequest
name|getRequest
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

