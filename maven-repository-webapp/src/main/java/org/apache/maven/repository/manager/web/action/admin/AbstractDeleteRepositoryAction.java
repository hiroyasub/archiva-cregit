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
name|manager
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
name|repository
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
name|repository
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
name|repository
operator|.
name|configuration
operator|.
name|ConfigurationChangeException
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
name|repository
operator|.
name|configuration
operator|.
name|ConfigurationStore
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
name|repository
operator|.
name|configuration
operator|.
name|ConfigurationStoreException
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
name|repository
operator|.
name|configuration
operator|.
name|InvalidConfigurationException
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
name|repository
operator|.
name|configuration
operator|.
name|RepositoryConfiguration
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
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
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
comment|/**  * Base action for repository removal actions.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDeleteRepositoryAction
extends|extends
name|PlexusActionSupport
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
comment|/**      * The repository ID to lookup when editing a repository.      */
specifier|protected
name|String
name|repoId
decl_stmt|;
comment|/**      * Which operation to select.      */
specifier|private
name|String
name|operation
init|=
literal|"unmodified"
decl_stmt|;
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|IOException
throws|,
name|InvalidConfigurationException
throws|,
name|ConfigurationChangeException
block|{
comment|// TODO: if this didn't come from the form, go to configure.action instead of going through with re-saving what was just loaded
if|if
condition|(
literal|"delete-entry"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
operator|||
literal|"delete-contents"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|Configuration
name|configuration
init|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
decl_stmt|;
name|AbstractRepositoryConfiguration
name|existingRepository
init|=
name|getRepository
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingRepository
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"A repository with that id does not exist"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
comment|// TODO: remove from index too!
name|removeRepository
argument_list|(
name|configuration
argument_list|,
name|existingRepository
argument_list|)
expr_stmt|;
name|configurationStore
operator|.
name|storeConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"delete-contents"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|removeContents
argument_list|(
name|existingRepository
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|removeContents
parameter_list|(
name|AbstractRepositoryConfiguration
name|existingRepository
parameter_list|)
throws|throws
name|IOException
function_decl|;
specifier|protected
specifier|abstract
name|AbstractRepositoryConfiguration
name|getRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|void
name|removeRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|AbstractRepositoryConfiguration
name|existingRepository
parameter_list|)
function_decl|;
specifier|public
name|String
name|input
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|getRepoId
parameter_list|()
block|{
return|return
name|repoId
return|;
block|}
specifier|public
name|void
name|setRepoId
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
name|this
operator|.
name|repoId
operator|=
name|repoId
expr_stmt|;
block|}
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
block|}
end_class

end_unit

