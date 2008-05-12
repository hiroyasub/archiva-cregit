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
operator|.
name|repositories
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Action
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
name|ArchivaConfiguration
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
name|RepositoryGroupConfiguration
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
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionBundle
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
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionException
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
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_comment
comment|/**  * DeleteRepositoryGroupActionTest  *   * @author  * @version  */
end_comment

begin_class
specifier|public
class|class
name|DeleteRepositoryGroupActionTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|REPO_GROUP_ID
init|=
literal|"repo-group-ident"
decl_stmt|;
specifier|private
name|DeleteRepositoryGroupAction
name|action
decl_stmt|;
specifier|private
name|MockControl
name|archivaConfigurationControl
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|action
operator|=
operator|(
name|DeleteRepositoryGroupAction
operator|)
name|lookup
argument_list|(
name|Action
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"deleteRepositoryGroupAction"
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|archivaConfigurationControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|action
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
operator|new
name|Configuration
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|SecureActionBundle
name|bundle
init|=
name|action
operator|.
name|getSecureActionBundle
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|bundle
operator|.
name|requiresAuthentication
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bundle
operator|.
name|getAuthorizationTuples
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRepositoryGroupConfirmation
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryGroupConfiguration
name|origRepoGroup
init|=
name|createRepositoryGroup
argument_list|()
decl_stmt|;
name|Configuration
name|configuration
init|=
name|createConfigurationForEditing
argument_list|(
name|origRepoGroup
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepoGroupId
argument_list|(
name|REPO_GROUP_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO_GROUP_ID
argument_list|,
name|action
operator|.
name|getRepoGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryGroupConfiguration
name|repoGroup
init|=
name|action
operator|.
name|getRepositoryGroup
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repoGroup
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|repoGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|action
operator|.
name|getRepoGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|origRepoGroup
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRepositoryGroup
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|createConfigurationForEditing
argument_list|(
name|createRepositoryGroup
argument_list|()
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepoGroupId
argument_list|(
name|REPO_GROUP_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO_GROUP_ID
argument_list|,
name|action
operator|.
name|getRepoGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryGroupConfiguration
name|repoGroup
init|=
name|action
operator|.
name|getRepositoryGroup
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repoGroup
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|repoGroup
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|delete
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRepositoryGroupCancelled
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryGroupConfiguration
name|origRepoGroup
init|=
name|createRepositoryGroup
argument_list|()
decl_stmt|;
name|Configuration
name|configuration
init|=
name|createConfigurationForEditing
argument_list|(
name|origRepoGroup
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepoGroupId
argument_list|(
name|REPO_GROUP_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO_GROUP_ID
argument_list|,
name|action
operator|.
name|getRepoGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryGroupConfiguration
name|repoGroup
init|=
name|action
operator|.
name|getRepositoryGroup
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repoGroup
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|execute
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|repoGroup
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Configuration
name|createConfigurationForEditing
parameter_list|(
name|RepositoryGroupConfiguration
name|repoGroup
parameter_list|)
block|{
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|addRepositoryGroup
argument_list|(
name|repoGroup
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
specifier|private
name|RepositoryGroupConfiguration
name|createRepositoryGroup
parameter_list|()
block|{
name|RepositoryGroupConfiguration
name|repoGroup
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|()
decl_stmt|;
name|repoGroup
operator|.
name|setId
argument_list|(
name|REPO_GROUP_ID
argument_list|)
expr_stmt|;
return|return
name|repoGroup
return|;
block|}
block|}
end_class

end_unit

