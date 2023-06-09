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
operator|.
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * The project is the container for several versions each with different artifacts.  *  *<pre>  * namespace1 +--> project 1 +--> version 11 +--> artifact 111  *            |              |               |  *            |              |               +--> artifact 112  *            |              |  *            |              +--> version 12 +--> artifact 121  *            |                              |  *            |                              +--> artifact 122  *            |                              +--> ...  *            |  *            +--> project 2 +--> version 21 +--> artifact 211  *                           |               +--> ...  *                           +--> version 22 +--> artifact 221  *                                           +--> ...  *</pre>  *  *<p>  * Implementations must provide proper hash and equals methods.  *</p>  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Project
extends|extends
name|ContentItem
block|{
comment|/**      * The namespace of the project      *      * @return the namespace      */
name|Namespace
name|getNamespace
parameter_list|( )
function_decl|;
comment|/**      * The id of the project      *      * @return the project id      */
name|String
name|getId
parameter_list|( )
function_decl|;
block|}
end_interface

end_unit

