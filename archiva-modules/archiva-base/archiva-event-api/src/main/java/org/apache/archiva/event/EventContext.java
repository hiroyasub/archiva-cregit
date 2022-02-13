begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|event
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Context information about a specific event.  * This is used to provide specific information about the event context by using the generic  * event interface.  * Some event handler may need information about the underlying event but have no access to the  * API classes that represent the event.  *  * Context information is always string based and should not depend on external classes apart from JDK classes.  *  * @author Martin Schreier<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|EventContext
block|{
comment|/**      * Returns the id which is also used as prefix for keys in the repository data map.      * @return the identifier of this context      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Returns the context data as map of strings. Each entry key is prefixed with      * the unique prefix of this context.      *      * @return the map of key value pairs stored in this context      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getData
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

