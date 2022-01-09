begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Generic interface for mapping DTOs  *  * @author Martin Schreier<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ModelMapper
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
block|{
comment|/**      * Converts the source instance to a new instance of the target type.      * @param source the source instance      * @return a new instance of the target type      */
name|T
name|map
parameter_list|(
name|S
name|source
parameter_list|)
function_decl|;
comment|/**      * Updates the target instance based on the source instance      * @param source the source instance      * @param target the target instance      */
name|void
name|update
parameter_list|(
name|S
name|source
parameter_list|,
name|T
name|target
parameter_list|)
function_decl|;
comment|/**      * Converts the target instance back to the source type      * @param target the target instance      * @return a new instance of the source type      */
name|S
name|reverseMap
parameter_list|(
name|T
name|target
parameter_list|)
function_decl|;
comment|/**      * Updates the source instance based on the target instance      * @param target the target instance      * @param source the source instance      */
name|void
name|reverseUpdate
parameter_list|(
name|T
name|target
parameter_list|,
name|S
name|source
parameter_list|)
function_decl|;
comment|/**      * Returns the class name of the source type      * @return the source type      */
name|Class
argument_list|<
name|S
argument_list|>
name|getSourceType
parameter_list|()
function_decl|;
comment|/**      * Returns the class name of the target type      * @return the target type      */
name|Class
argument_list|<
name|T
argument_list|>
name|getTargetType
parameter_list|()
function_decl|;
comment|/**      * Returns<code>true</code>, if the given type are the same or supertype of the mapping types.      * @param sourceType      * @param targetType      * @param<S>      * @param<T>      * @return      */
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|boolean
name|supports
parameter_list|(
name|Class
argument_list|<
name|S
argument_list|>
name|sourceType
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|targetType
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

