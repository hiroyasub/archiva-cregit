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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|ActionSupport
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|ModelDriven
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|Preparable
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
name|indexing
operator|.
name|RepositoryIndexException
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
name|indexing
operator|.
name|RepositoryIndexSearchException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
comment|/**  * Configures the application.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="configureAction"  */
end_comment

begin_class
specifier|public
class|class
name|ConfigureAction
extends|extends
name|ActionSupport
implements|implements
name|ModelDriven
implements|,
name|Preparable
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
comment|/**      * The configuration.      */
specifier|private
name|Configuration
name|configuration
decl_stmt|;
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
throws|,
name|RepositoryIndexSearchException
throws|,
name|ConfigurationStoreException
throws|,
name|InvalidConfigurationException
throws|,
name|ConfigurationChangeException
block|{
comment|// TODO: if this didn't come from the form, go to configure.action instead of going through with re-saving what was just loaded
comment|// TODO: if this is changed, do we move the index or recreate it?
comment|// Normalize the path
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|configuration
operator|.
name|getIndexPath
argument_list|()
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setIndexPath
argument_list|(
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
comment|// TODO: error handling when this fails, or is not a directory
block|}
comment|// Just double checking that our validation routines line up with what is expected in the configuration
assert|assert
name|configuration
operator|.
name|isValid
argument_list|()
assert|;
name|configurationStore
operator|.
name|storeConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
comment|// TODO: if the repository has changed, we need to check if indexing is needed
name|addActionMessage
argument_list|(
literal|"Successfully saved configuration"
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
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
name|Object
name|getModel
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|ConfigurationStoreException
block|{
name|configuration
operator|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

