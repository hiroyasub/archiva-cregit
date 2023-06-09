begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|common
operator|.
name|proxy
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
name|maven
operator|.
name|wagon
operator|.
name|Wagon
import|;
end_import

begin_comment
comment|/**  * Create a Wagon instance for the given protocol.  */
end_comment

begin_interface
specifier|public
interface|interface
name|WagonFactory
block|{
comment|/**      * Create a new Wagon instance for the given protocol.      *      * @param wagonFactoryRequest      *      * @return the Wagon instance      */
name|Wagon
name|getWagon
parameter_list|(
name|WagonFactoryRequest
name|wagonFactoryRequest
parameter_list|)
throws|throws
name|WagonFactoryException
function_decl|;
block|}
end_interface

end_unit

