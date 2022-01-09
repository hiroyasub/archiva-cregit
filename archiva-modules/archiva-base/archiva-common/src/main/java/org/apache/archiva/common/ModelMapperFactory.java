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
comment|/**  * Interface that returns a given DTO mapper.  *  * @author Martin Schreier<martin_s@apache.org>  *  * @param<SB> The base source type for the model mapper  * @param<TB> The base target type for the model mapper  */
end_comment

begin_interface
specifier|public
interface|interface
name|ModelMapperFactory
parameter_list|<
name|SB
parameter_list|,
name|TB
parameter_list|>
block|{
comment|/**      * Returns a mapper for the given source and target type. If no mapper is registered for this combination,      * it will throw a {@link IllegalArgumentException}      * @param sourceType the source type for the mapping      * @param targetType the destination type      * @param<S> source type      * @param<T> destination type      * @return the mapper instance      */
parameter_list|<
name|S
extends|extends
name|SB
parameter_list|,
name|T
extends|extends
name|TB
parameter_list|>
name|ModelMapper
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|getMapper
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
throws|throws
name|IllegalArgumentException
function_decl|;
block|}
end_interface

end_unit

