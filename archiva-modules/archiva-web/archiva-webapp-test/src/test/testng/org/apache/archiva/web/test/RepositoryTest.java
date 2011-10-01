begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|test
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|test
operator|.
name|parent
operator|.
name|AbstractRepositoryTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|annotations
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"repository"
block|}
argument_list|,
name|dependsOnMethods
operator|=
block|{
literal|"testWithCorrectUsernamePassword"
block|}
argument_list|,
name|sequential
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|RepositoryTest
extends|extends
name|AbstractRepositoryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoValidValues
parameter_list|()
block|{
name|goToRepositoriesPage
argument_list|()
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"managedrepo1"
argument_list|,
literal|"Managed Repository Sample 1"
argument_list|,
name|getRepositoryDir
argument_list|()
operator|+
literal|"repository/"
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Save"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Managed Repository Sample 1"
argument_list|)
expr_stmt|;
name|assertRepositoriesPage
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoValidValues"
block|}
argument_list|,
name|enabled
operator|=
literal|false
argument_list|)
specifier|public
name|void
name|testAddManagedRepoInvalidValues
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"<> \\/~+[ ]'\""
argument_list|,
literal|"<>\\~+[]'\""
argument_list|,
literal|"<> ~+[ ]'\""
argument_list|,
literal|"<> ~+[ ]'\""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|""
argument_list|,
literal|"-1"
argument_list|,
literal|"101"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Identifier must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Directory must only contain alphanumeric characters, equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Name must only contain alphanumeric characters, white-spaces(' '), forward-slashes(/), open-parenthesis('('), close-parenthesis(')'), underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Index directory must only contain alphanumeric characters, equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Purge By Retention Count needs to be between 1 and 100."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Purge By Days Older Than needs to be larger than 0."
argument_list|)
expr_stmt|;
comment|// FIXME: broken
name|assertTextPresent
argument_list|(
literal|"Invalid cron expression."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoInvalidIdentifier
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"<> \\/~+[ ]'\""
argument_list|,
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Identifier must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoInvalidRepoName
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"identifier"
argument_list|,
literal|"<>\\~+[]'\""
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Name must only contain alphanumeric characters, white-spaces(' '), forward-slashes(/), open-parenthesis('('), close-parenthesis(')'), underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoInvalidDirectory
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"identifier"
argument_list|,
literal|"name"
argument_list|,
literal|"<> ~+[ ]'\""
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Directory must only contain alphanumeric characters, equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoInvalidIndexDir
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"identifier"
argument_list|,
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"<> ~+[ ]'\""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Index directory must only contain alphanumeric characters, equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoInvalidRetentionCount
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"identifier"
argument_list|,
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"101"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Purge By Retention Count needs to be between 1 and 100."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoInvalidDaysOlder
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"identifier"
argument_list|,
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"-1"
argument_list|,
literal|"1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Purge By Days Older Than needs to be larger than 0."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|enabled
operator|=
literal|false
argument_list|)
specifier|public
name|void
name|testAddManagedRepoBlankValues
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a repository identifier."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a repository name."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a directory."
argument_list|)
expr_stmt|;
comment|// FIXME: broken
name|assertTextPresent
argument_list|(
literal|"Invalid cron expression."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoNoIdentifier
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|""
argument_list|,
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a repository identifier."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoNoRepoName
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"identifier"
argument_list|,
literal|""
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a repository name."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoNoDirectory
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"identifier"
argument_list|,
literal|"name"
argument_list|,
literal|""
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a directory."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|enabled
operator|=
literal|false
argument_list|)
specifier|public
name|void
name|testAddManagedRepoNoCron
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"identifier"
argument_list|,
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// FIXME: broken
name|assertTextPresent
argument_list|(
literal|"Invalid cron expression."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddManagedRepoForEdit
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRepository.action"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
literal|"managedrepo"
argument_list|,
literal|"Managed Repository Sample"
argument_list|,
name|getRepositoryDir
argument_list|()
operator|+
literal|"local-repo/"
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Save"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Managed Repository Sample"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoForEdit"
block|}
argument_list|,
name|enabled
operator|=
literal|false
argument_list|)
specifier|public
name|void
name|testEditManagedRepoInvalidValues
parameter_list|()
block|{
name|editManagedRepository
argument_list|(
literal|"<>\\~+[]'\""
argument_list|,
literal|"<> ~+[ ]'\""
argument_list|,
literal|"<> ~+[ ]'\""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|""
argument_list|,
literal|"-1"
argument_list|,
literal|"101"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Directory must only contain alphanumeric characters, equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Name must only contain alphanumeric characters, white-spaces(' '), forward-slashes(/), open-parenthesis('('), close-parenthesis(')'), underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Index directory must only contain alphanumeric characters, equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Purge By Retention Count needs to be between 1 and 100."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Purge By Days Older Than needs to be larger than 0."
argument_list|)
expr_stmt|;
comment|// FIXME: broken
name|assertTextPresent
argument_list|(
literal|"Invalid cron expression."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoForEdit"
block|}
argument_list|)
specifier|public
name|void
name|testEditManagedRepoInvalidRepoName
parameter_list|()
block|{
name|editManagedRepository
argument_list|(
literal|"<>\\~+[]'\""
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Name must only contain alphanumeric characters, white-spaces(' '), forward-slashes(/), open-parenthesis('('), close-parenthesis(')'), underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoForEdit"
block|}
argument_list|)
specifier|public
name|void
name|testEditManagedRepoInvalidDirectory
parameter_list|()
block|{
name|editManagedRepository
argument_list|(
literal|"name"
argument_list|,
literal|"<> ~+[ ]'\""
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Directory must only contain alphanumeric characters, equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoForEdit"
block|}
argument_list|)
specifier|public
name|void
name|testEditManagedRepoInvalidIndexDir
parameter_list|()
block|{
name|editManagedRepository
argument_list|(
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"<> ~+[ ]'\""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Index directory must only contain alphanumeric characters, equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoForEdit"
block|}
argument_list|,
name|enabled
operator|=
literal|false
argument_list|)
specifier|public
name|void
name|testEditManagedRepoInvalidCron
parameter_list|()
block|{
name|editManagedRepository
argument_list|(
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|""
argument_list|,
literal|"1"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
comment|// FIXME: broken
name|assertTextPresent
argument_list|(
literal|"Invalid cron expression."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoForEdit"
block|}
argument_list|)
specifier|public
name|void
name|testEditManagedRepoInvalidRetentionCount
parameter_list|()
block|{
name|editManagedRepository
argument_list|(
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"1"
argument_list|,
literal|"101"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Purge By Retention Count needs to be between 1 and 100."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoForEdit"
block|}
argument_list|)
specifier|public
name|void
name|testEditManagedRepoInvalidDaysOlder
parameter_list|()
block|{
name|editManagedRepository
argument_list|(
literal|"name"
argument_list|,
literal|"/home"
argument_list|,
literal|"/.index"
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|"-1"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Purge By Days Older Than needs to be larger than 0."
argument_list|)
expr_stmt|;
block|}
comment|// TODO
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddManagedRepoForEdit"
block|}
argument_list|)
specifier|public
name|void
name|testEditManagedRepo
parameter_list|()
block|{
name|editManagedRepository
argument_list|(
literal|"repository.name"
argument_list|,
literal|"Managed Repo"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Managed Repository Sample"
argument_list|)
expr_stmt|;
block|}
comment|// TODO
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testEditManagedRepo"
block|}
argument_list|,
name|enabled
operator|=
literal|false
argument_list|)
specifier|public
name|void
name|testDeleteManageRepo
parameter_list|()
block|{
name|deleteManagedRepository
argument_list|()
expr_stmt|;
comment|// assertTextNotPresent( "managedrepo" );
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddRemoteRepoValidValues"
block|}
argument_list|)
specifier|public
name|void
name|testAddRemoteRepoNullValues
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRemoteRepository.action"
argument_list|)
expr_stmt|;
name|addRemoteRepository
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a repository identifier."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a repository name."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a url."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddRemoteRepositoryNullIdentifier
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRemoteRepository.action"
argument_list|)
expr_stmt|;
name|addRemoteRepository
argument_list|(
literal|""
argument_list|,
literal|"Remote Repository Sample"
argument_list|,
literal|"http://repository.codehaus.org/org/codehaus/mojo/"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a repository identifier."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddRemoteRepositoryNullIdentifier"
block|}
argument_list|)
specifier|public
name|void
name|testAddRemoteRepoNullName
parameter_list|()
block|{
name|addRemoteRepository
argument_list|(
literal|"remoterepo"
argument_list|,
literal|""
argument_list|,
literal|"http://repository.codehaus.org/org/codehaus/mojo/"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a repository name."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddRemoteRepoNullName"
block|}
argument_list|)
specifier|public
name|void
name|testAddRemoteRepoNullURL
parameter_list|()
block|{
name|addRemoteRepository
argument_list|(
literal|"remoterepo"
argument_list|,
literal|"Remote Repository Sample"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a url."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddRemoteRepoNullURL"
block|}
argument_list|)
specifier|public
name|void
name|testAddProxyConnectorValidValues
parameter_list|()
throws|throws
name|Exception
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addProxyConnector.action"
argument_list|)
expr_stmt|;
name|addProxyConnector
argument_list|(
literal|"(direct connection)"
argument_list|,
literal|"internal"
argument_list|,
literal|"remoterepo"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"remoterepo"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Remote Repository Sample"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddRemoteRepoValidValues
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/admin/addRemoteRepository.action"
argument_list|)
expr_stmt|;
name|addRemoteRepository
argument_list|(
literal|"remoterepo"
argument_list|,
literal|"Remote Repository Sample"
argument_list|,
literal|"http://repository.codehaus.org/org/codehaus/mojo/"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Remote Repository Sample"
argument_list|)
expr_stmt|;
block|}
comment|// *** BUNDLED REPOSITORY TEST ***
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testWithCorrectUsernamePassword"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testBundledRepository
parameter_list|()
block|{
name|String
name|repo1
init|=
name|baseUrl
operator|+
literal|"repository/internal/"
decl_stmt|;
name|String
name|repo2
init|=
name|baseUrl
operator|+
literal|"repository/snapshots/"
decl_stmt|;
name|assertRepositoryAccess
argument_list|(
name|repo1
argument_list|)
expr_stmt|;
name|assertRepositoryAccess
argument_list|(
name|repo2
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertRepositoryAccess
parameter_list|(
name|String
name|repo
parameter_list|)
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva"
argument_list|)
expr_stmt|;
name|goToRepositoriesPage
argument_list|()
expr_stmt|;
name|assertLinkPresent
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Collection: /"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Collection: /"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

