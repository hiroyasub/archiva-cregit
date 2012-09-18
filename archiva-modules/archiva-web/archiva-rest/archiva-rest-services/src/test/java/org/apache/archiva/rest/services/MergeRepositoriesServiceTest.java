begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
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
name|maven2
operator|.
name|model
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|BrowseService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|MergeRepositoriesService
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
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fest
operator|.
name|assertions
operator|.
name|api
operator|.
name|Assertions
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
name|io
operator|.
name|File
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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|MergeRepositoriesServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
annotation|@
name|Override
annotation|@
name|Before
specifier|public
name|void
name|startServer
parameter_list|()
throws|throws
name|Exception
block|{
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
argument_list|,
literal|"src/test/repo-with-osgi"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"builddir"
argument_list|)
argument_list|,
literal|"test-repository"
argument_list|)
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
argument_list|,
literal|"src/test/repo-with-osgi-stage"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"builddir"
argument_list|)
argument_list|,
literal|"test-repository-stage"
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|startServer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
specifier|public
name|void
name|stopServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO delete repositories
name|super
operator|.
name|stopServer
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"builddir"
argument_list|)
argument_list|,
literal|"test-repository"
argument_list|)
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"builddir"
argument_list|)
argument_list|,
literal|"test-repository-stage"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|mergeConflictedArtifacts
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|testRepoId
init|=
literal|"test-repository"
decl_stmt|;
name|createStagedNeededRepo
argument_list|(
name|testRepoId
argument_list|,
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"builddir"
argument_list|)
argument_list|,
literal|"test-repository"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// force jcr data population !
name|BrowseService
name|browseService
init|=
name|getBrowseService
argument_list|(
name|authorizationHeader
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|browseService
operator|.
name|getRootGroups
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
name|browseService
operator|.
name|getRootGroups
argument_list|(
name|testRepoId
operator|+
literal|"-stage"
argument_list|)
expr_stmt|;
name|MergeRepositoriesService
name|service
init|=
name|getMergeRepositoriesService
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifactMetadatas
init|=
name|service
operator|.
name|getMergeConflictedArtifacts
argument_list|(
name|testRepoId
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"conflicts: {}"
argument_list|,
name|artifactMetadatas
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|artifactMetadatas
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|deleteTestRepo
argument_list|(
name|testRepoId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
end_class

end_unit

