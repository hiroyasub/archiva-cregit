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
name|web
operator|.
name|action
operator|.
name|admin
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|AbstractRepositoryConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ProxiedRepositoryConfiguration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Configures the application repositories.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="deleteProxiedRepositoryAction"  */
end_comment

begin_class
specifier|public
class|class
name|DeleteProxiedRepositoryAction
extends|extends
name|AbstractDeleteRepositoryAction
block|{
specifier|protected
name|AbstractRepositoryConfiguration
name|getRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
return|return
name|configuration
operator|.
name|getProxiedRepositoryById
argument_list|(
name|repoId
argument_list|)
return|;
block|}
specifier|protected
name|void
name|removeRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|AbstractRepositoryConfiguration
name|existingRepository
parameter_list|)
block|{
name|configuration
operator|.
name|removeProxiedRepository
argument_list|(
operator|(
name|ProxiedRepositoryConfiguration
operator|)
name|existingRepository
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeContents
parameter_list|(
name|AbstractRepositoryConfiguration
name|existingRepository
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TODO! delete it
block|}
block|}
end_class

end_unit

