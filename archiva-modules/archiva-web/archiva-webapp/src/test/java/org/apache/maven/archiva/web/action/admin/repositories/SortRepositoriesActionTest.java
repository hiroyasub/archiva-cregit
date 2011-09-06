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
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
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
name|apache
operator|.
name|struts2
operator|.
name|StrutsSpringTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|integration
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
name|redback
operator|.
name|integration
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
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_comment
comment|/**  * SortRepositoriesActionTest  */
end_comment

begin_class
specifier|public
class|class
name|SortRepositoriesActionTest
extends|extends
name|StrutsSpringTestCase
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
specifier|static
specifier|final
name|String
name|REPO1_ID
init|=
literal|"managed-repo-ident-1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPO2_ID
init|=
literal|"managed-repo-ident-2"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPO3_ID
init|=
literal|"managed-repo-ident-3"
decl_stmt|;
specifier|private
name|MockControl
name|archivaConfigurationControl
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|originalArchivaConfiguration
decl_stmt|;
specifier|private
name|SortRepositoriesAction
name|action
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getContextLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath*:/spring-context.xml"
block|}
return|;
block|}
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
name|SortRepositoriesAction
operator|)
name|getActionProxy
argument_list|(
literal|"/admin/sortDownRepositoryFromGroup.action"
argument_list|)
operator|.
name|getAction
argument_list|()
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
name|originalArchivaConfiguration
operator|=
name|action
operator|.
name|archivaConfiguration
expr_stmt|;
name|action
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|action
operator|.
name|archivaConfiguration
operator|=
name|originalArchivaConfiguration
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
name|testSortDownFirstRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|createInitialConfiguration
argument_list|()
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
literal|4
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
name|RepositoryGroupConfiguration
name|repoGroup
init|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
name|repoGroup
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|repositories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO1_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO2_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO3_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// sort down first repo
name|action
operator|.
name|setRepoGroupId
argument_list|(
name|repoGroup
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTargetRepo
argument_list|(
name|REPO1_ID
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|sortDown
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|repoGroup
operator|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|repositories
operator|=
name|repoGroup
operator|.
name|getRepositories
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|repositories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO2_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO1_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO3_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSortDownLastRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|createInitialConfiguration
argument_list|()
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
literal|4
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
name|RepositoryGroupConfiguration
name|repoGroup
init|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
name|repoGroup
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|repositories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO1_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO2_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO3_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// sort down last repo
name|action
operator|.
name|setRepoGroupId
argument_list|(
name|repoGroup
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTargetRepo
argument_list|(
name|REPO3_ID
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|sortDown
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|repoGroup
operator|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|repositories
operator|=
name|repoGroup
operator|.
name|getRepositories
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|repositories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO1_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO2_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO3_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSortUpLastRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|createInitialConfiguration
argument_list|()
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
literal|4
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
name|RepositoryGroupConfiguration
name|repoGroup
init|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
name|repoGroup
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|repositories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO1_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO2_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO3_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// sort up last repo
name|action
operator|.
name|setRepoGroupId
argument_list|(
name|repoGroup
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTargetRepo
argument_list|(
name|REPO3_ID
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|sortUp
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|repoGroup
operator|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|repositories
operator|=
name|repoGroup
operator|.
name|getRepositories
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|repositories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO1_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO3_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO2_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSortUpFirstRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|configuration
init|=
name|createInitialConfiguration
argument_list|()
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
literal|4
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
name|RepositoryGroupConfiguration
name|repoGroup
init|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
name|repoGroup
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|repositories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO1_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO2_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO3_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// sort up first repo
name|action
operator|.
name|setRepoGroupId
argument_list|(
name|repoGroup
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTargetRepo
argument_list|(
name|REPO1_ID
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|sortUp
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|repoGroup
operator|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|repositories
operator|=
name|repoGroup
operator|.
name|getRepositories
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|repositories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO1_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO2_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO3_ID
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Configuration
name|createInitialConfiguration
parameter_list|()
block|{
name|Configuration
name|config
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
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
name|repoGroup
operator|.
name|addRepository
argument_list|(
name|REPO1_ID
argument_list|)
expr_stmt|;
name|repoGroup
operator|.
name|addRepository
argument_list|(
name|REPO2_ID
argument_list|)
expr_stmt|;
name|repoGroup
operator|.
name|addRepository
argument_list|(
name|REPO3_ID
argument_list|)
expr_stmt|;
name|config
operator|.
name|addRepositoryGroup
argument_list|(
name|repoGroup
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

