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
comment|/**  * Each artifact is attached to exactly one version.  *<p>  * Implementations must provide proper hash and equals methods.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Version
extends|extends
name|ContentItem
block|{
comment|/**      * Returns the version string.      *      * @return the version string      */
name|String
name|getVersion
parameter_list|( )
function_decl|;
comment|/**      * Returns the version segments. E.g. for 1.3.4 it will return ["1","3"."4"]      *      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getVersionSegments
parameter_list|( )
function_decl|;
comment|/**      * Returns the project this version is attached to.      *      * @return the project instance. Will never return<code>null</code>      */
name|Project
name|getProject
parameter_list|( )
function_decl|;
block|}
end_interface

end_unit

