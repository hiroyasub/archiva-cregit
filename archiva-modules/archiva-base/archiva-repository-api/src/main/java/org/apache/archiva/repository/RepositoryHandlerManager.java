begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|model
operator|.
name|AbstractRepositoryConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|event
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|event
operator|.
name|EventHandler
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryHandlerManager
extends|extends
name|EventHandler
argument_list|<
name|Event
argument_list|>
block|{
parameter_list|<
name|R
extends|extends
name|Repository
parameter_list|,
name|C
extends|extends
name|AbstractRepositoryConfiguration
parameter_list|>
name|RepositoryHandler
argument_list|<
name|R
argument_list|,
name|C
argument_list|>
name|getHandler
parameter_list|(
name|Class
argument_list|<
name|R
argument_list|>
name|repositoryClazz
parameter_list|,
name|Class
argument_list|<
name|C
argument_list|>
name|configurationClazz
parameter_list|)
function_decl|;
name|void
name|registerHandler
parameter_list|(
name|RepositoryHandler
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|handler
parameter_list|)
function_decl|;
name|boolean
name|isRegisteredId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

