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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
import|;
end_import

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
comment|/**  * Create an artifact repository from the given configuration.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ConfiguredRepositoryFactory
block|{
name|String
name|ROLE
init|=
name|ConfiguredRepositoryFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Create an artifact repository from the given configuration.      *      * @param configuration the configuration      * @return the artifact repository      */
name|ArtifactRepository
name|createRepository
parameter_list|(
name|RepositoryConfiguration
name|configuration
parameter_list|)
function_decl|;
comment|/**      * Create artifact repositories from the given configuration.      *      * @param configuration the configuration containing the repositories      * @return the artifact repositories      */
name|List
name|createRepositories
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
function_decl|;
comment|/**      * Create a local repository from the given configuration.      *      * @param configuration the configuration      * @return the local artifact repository      */
name|ArtifactRepository
name|createLocalRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
function_decl|;
comment|/**      * Create an artifact repository from the given proxy repository configuration.      *      * @param configuration the configuration      * @return the artifact repository      */
name|ArtifactRepository
name|createProxiedRepository
parameter_list|(
name|ProxiedRepositoryConfiguration
name|configuration
parameter_list|)
function_decl|;
comment|/**      * Create artifact repositories from the given proxy repository configurations.      *      * @param configuration the configuration containing the repositories      * @return the artifact repositories      */
name|List
name|createProxiedRepositories
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

