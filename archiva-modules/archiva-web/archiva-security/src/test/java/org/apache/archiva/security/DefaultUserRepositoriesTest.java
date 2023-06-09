begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|security
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|security
operator|.
name|common
operator|.
name|ArchivaRoleConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
comment|/**  * DefaultUserRepositoriesTest  *  *  */
end_comment

begin_class
specifier|public
class|class
name|DefaultUserRepositoriesTest
extends|extends
name|AbstractSecurityTest
block|{
annotation|@
name|Before
annotation|@
name|Override
specifier|public
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
name|restoreGuestInitialValues
argument_list|(
name|USER_ALPACA
argument_list|)
expr_stmt|;
name|restoreGuestInitialValues
argument_list|(
name|USER_GUEST
argument_list|)
expr_stmt|;
name|restoreGuestInitialValues
argument_list|(
name|USER_ADMIN
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetObservableRepositoryIds
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create some users.
name|createUser
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"Al 'Archiva' Paca"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected users"
argument_list|,
literal|3
argument_list|,
name|securitySystem
operator|.
name|getUserManager
argument_list|()
operator|.
name|getUsers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// some unassigned repo observer roles.
name|setupRepository
argument_list|(
literal|"central"
argument_list|)
expr_stmt|;
name|setupRepository
argument_list|(
literal|"corporate"
argument_list|)
expr_stmt|;
name|setupRepository
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|setupRepository
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|setupRepository
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
comment|// some assigned repo observer roles.
name|assignRepositoryObserverRole
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
name|assignRepositoryObserverRole
argument_list|(
name|USER_ALPACA
argument_list|,
literal|"central"
argument_list|)
expr_stmt|;
name|assignRepositoryObserverRole
argument_list|(
name|USER_GUEST
argument_list|,
literal|"corporate"
argument_list|)
expr_stmt|;
comment|// the global repo observer role.
name|assignGlobalRepositoryObserverRole
argument_list|(
name|USER_ADMIN
argument_list|)
expr_stmt|;
name|assertRepoIds
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"central"
block|,
literal|"corporate"
block|}
argument_list|,
name|userRepos
operator|.
name|getObservableRepositoryIds
argument_list|(
name|USER_ALPACA
argument_list|)
argument_list|)
expr_stmt|;
name|assertRepoIds
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"coporate"
block|}
argument_list|,
name|userRepos
operator|.
name|getObservableRepositoryIds
argument_list|(
name|USER_GUEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertRepoIds
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"central"
block|,
literal|"internal"
block|,
literal|"corporate"
block|,
literal|"snapshots"
block|,
literal|"secret"
block|}
argument_list|,
name|userRepos
operator|.
name|getObservableRepositoryIds
argument_list|(
name|USER_ADMIN
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
specifier|public
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
name|restoreGuestInitialValues
argument_list|(
name|USER_ALPACA
argument_list|)
expr_stmt|;
name|restoreGuestInitialValues
argument_list|(
name|USER_GUEST
argument_list|)
expr_stmt|;
name|restoreGuestInitialValues
argument_list|(
name|USER_ADMIN
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertRepoIds
parameter_list|(
name|String
index|[]
name|expectedRepoIds
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Observable Repository Ids cannot be null."
argument_list|,
name|observableRepositoryIds
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedRepoIds
operator|.
name|length
operator|!=
name|observableRepositoryIds
operator|.
name|size
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Size of Observable Repository Ids wrong, expected<"
operator|+
name|expectedRepoIds
operator|.
name|length
operator|+
literal|"> but got<"
operator|+
name|observableRepositoryIds
operator|.
name|size
argument_list|()
operator|+
literal|"> instead. \nExpected: ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|expectedRepoIds
argument_list|,
literal|","
argument_list|)
operator|+
literal|"]\nActual: ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|observableRepositoryIds
operator|.
name|iterator
argument_list|()
argument_list|,
literal|","
argument_list|)
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assignGlobalRepositoryObserverRole
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|Exception
block|{
name|roleManager
operator|.
name|assignRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_GLOBAL_REPOSITORY_OBSERVER
argument_list|,
name|principal
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

