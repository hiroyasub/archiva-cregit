begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|configuration
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * A component for loading the configuration into the model.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @todo this is something that could possibly be generalised into Modello.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ConfigurationStore
block|{
comment|/**      * The Plexus role for the component.      */
name|String
name|ROLE
init|=
name|ConfigurationStore
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Get the configuration from the store. A cached version may be used.      *      * @return the configuration      * @throws ConfigurationStoreException if there is a problem loading the configuration      */
name|Configuration
name|getConfigurationFromStore
parameter_list|()
throws|throws
name|ConfigurationStoreException
function_decl|;
comment|/**      * Save the configuration to the store.      *      * @param configuration the configuration to store      */
name|void
name|storeConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|ConfigurationStoreException
throws|,
name|InvalidConfigurationException
throws|,
name|ConfigurationChangeException
function_decl|;
comment|/**      * Add a configuration change listener.      *      * @param listener the listener      */
name|void
name|addChangeListener
parameter_list|(
name|ConfigurationChangeListener
name|listener
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

