begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|registry
operator|.
name|RegistryException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|registry
operator|.
name|RegistryListener
import|;
end_import

begin_comment
comment|/**  * Configuration holder for the model read from the registry.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArchivaConfiguration
block|{
comment|/**      * Get the configuration.      *      * @return the configuration      */
name|Configuration
name|getConfiguration
parameter_list|()
function_decl|;
comment|/**      * Save any updated configuration.      *      * @param configuration the configuration to save      * @throws org.codehaus.plexus.registry.RegistryException      *          if there is a problem saving the registry data      */
name|void
name|save
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RegistryException
function_decl|;
comment|/**      * Add a change listener so that registry changes are propogated.      *      * @param listener the listener      */
name|void
name|addChangeListener
parameter_list|(
name|RegistryListener
name|listener
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

