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
name|features
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  *  * The repository feature holds information about specific features. The may not be available by all repository implementations.  * Features should be simple objects for storing additional data, the should not implement too much functionality.  * Additional functionality the uses the information in the feature objects should be implemented in the specific repository  * provider and repository implementations, or in the repository registry if it is generic.  *  * But features may throw events, if it's data is changed.  *  *  * This interface is to get access to a concrete feature by accessing the generic interface.  *  * @param<T> the concrete feature implementation.  *  * @author Martin Stockhammer<martin_s@apache.org>  * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryFeature
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
parameter_list|<
name|T
parameter_list|>
parameter_list|>
block|{
comment|/**      * Unique Identifier of this feature. Each feature implementation has its own unique identifier.      *      * @return the identifier string which should be unique for the implementation class.      */
specifier|default
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * Tells, if this instance is a feature of the given identifier.      *      * @param featureId the feature identifier string to check      * @return true, if this instance is a instance with the feature id, otherwise<code>false</code>      */
specifier|default
name|boolean
name|isFeature
parameter_list|(
name|String
name|featureId
parameter_list|)
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|featureId
argument_list|)
return|;
block|}
comment|/**      * Tells, if the this instance is a feature of the given feature class.      *      * @param clazz The class to check against.      * @param<K> the concrete feature implementation.      * @return      */
specifier|default
parameter_list|<
name|K
extends|extends
name|RepositoryFeature
argument_list|<
name|K
argument_list|>
parameter_list|>
name|boolean
name|isFeature
parameter_list|(
name|Class
argument_list|<
name|K
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
return|;
block|}
comment|/**      * Returns the concrete feature instance.      * @return the feature instance.      */
name|T
name|get
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

