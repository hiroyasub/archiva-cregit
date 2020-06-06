begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * The namespace represents some kind of hierarchical coordinate where artifacts are stored.  * The syntax of the namespace (e.g. the separator like '.' or '/') is dependent on the repository type.  *  *<pre>  * namespace1 +--> project 1 +--> version 11 +--> artifact 111  *            |              |               |  *            |              |               +--> artifact 112  *            |              |  *            |              +--> version 12 +--> artifact 121  *            |                              |  *            |                              +--> artifact 122  *            |                              +--> ...  *            |  *            +--> project 2 +--> version 21 +--> artifact 211  *                           |               +--> ...  *                           +--> version 22 +--> artifact 221  *                                           +--> ...  *</pre>  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Namespace
extends|extends
name|ContentItem
block|{
comment|/**      * Return the namespace string that identifies the current namespace.      * Namespaces are hierarchical and have a separator that separates the path elements. Default      * separator is '.'. But this may depend on the repository type.      *      * A namespace may be empty which is equal to the root.      *      * @return the unique name of the namespace      */
name|String
name|getId
parameter_list|( )
function_decl|;
comment|/**      * Returns the elements that represent the path to the namespace.      *      * @return the list of path elements      */
name|List
argument_list|<
name|String
argument_list|>
name|getNamespacePath
parameter_list|( )
function_decl|;
block|}
end_interface

end_unit

