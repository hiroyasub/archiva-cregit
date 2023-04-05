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
comment|/**  * @author Martin Schreier<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMapper
parameter_list|<
name|B
parameter_list|,
name|T
parameter_list|,
name|R
parameter_list|>
implements|implements
name|MultiModelMapper
argument_list|<
name|B
argument_list|,
name|T
argument_list|,
name|R
argument_list|>
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|S2
parameter_list|,
name|T2
parameter_list|,
name|R2
parameter_list|>
name|boolean
name|supports
parameter_list|(
name|Class
argument_list|<
name|S2
argument_list|>
name|baseType
parameter_list|,
name|Class
argument_list|<
name|T2
argument_list|>
name|destinationType
parameter_list|,
name|Class
argument_list|<
name|R2
argument_list|>
name|reverseSourceType
parameter_list|)
block|{
return|return
operator|(
name|baseType
operator|.
name|isAssignableFrom
argument_list|(
name|getBaseType
argument_list|( )
argument_list|)
operator|&&
name|destinationType
operator|.
name|isAssignableFrom
argument_list|(
name|getDestinationType
argument_list|( )
argument_list|)
operator|&&
name|reverseSourceType
operator|.
name|isAssignableFrom
argument_list|(
name|getReverseSourceType
argument_list|( )
argument_list|)
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
return|return
name|getHash
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|super
operator|.
name|hashCode
argument_list|( )
operator|==
name|obj
operator|.
name|hashCode
argument_list|( )
return|;
block|}
block|}
end_class

end_unit
