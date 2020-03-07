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
name|maven
operator|.
name|metadata
operator|.
name|storage
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArtifactMappingProvider
block|{
name|String
name|mapClassifierAndExtensionToType
parameter_list|(
name|String
name|classifier
parameter_list|,
name|String
name|ext
parameter_list|)
function_decl|;
name|String
name|mapTypeToExtension
parameter_list|(
name|String
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

