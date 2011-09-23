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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|model
operator|.
name|ArtifactReference
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
name|repository
operator|.
name|RepositoryContentFactory
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
name|model
operator|.
name|ArtifactTransferRequest
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
name|apache
operator|.
name|commons
operator|.
name|lang
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
name|Test
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|CopyArtifactTest
extends|extends
name|AbstractArchivaRestTest
block|{
specifier|static
specifier|final
name|String
name|TARGET_REPO_ID
init|=
literal|"test-copy-target"
decl_stmt|;
specifier|static
specifier|final
name|String
name|SOURCE_REPO_ID
init|=
literal|"test-origin-repo"
decl_stmt|;
specifier|private
name|void
name|initSourceTargetRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|targetRepo
init|=
operator|new
name|File
argument_list|(
literal|"target/test-repo-copy"
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|targetRepo
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|targetRepo
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|targetRepo
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|TARGET_REPO_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|TARGET_REPO_ID
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|TARGET_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ManagedRepository
name|managedRepository
init|=
name|getTestManagedRepository
argument_list|()
decl_stmt|;
name|managedRepository
operator|.
name|setId
argument_list|(
name|TARGET_REPO_ID
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setLocation
argument_list|(
name|targetRepo
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setCronExpression
argument_list|(
literal|"* * * * * ?"
argument_list|)
expr_stmt|;
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|TARGET_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|originRepo
init|=
operator|new
name|File
argument_list|(
literal|"target/test-origin-repo"
argument_list|)
decl_stmt|;
if|if
condition|(
name|originRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|originRepo
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|originRepo
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/repo-with-osgi"
argument_list|)
argument_list|,
name|originRepo
argument_list|)
expr_stmt|;
if|if
condition|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|SOURCE_REPO_ID
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|managedRepository
operator|=
name|getTestManagedRepository
argument_list|()
expr_stmt|;
name|managedRepository
operator|.
name|setId
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setLocation
argument_list|(
name|originRepo
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|addKnownContentConsumer
argument_list|(
literal|"create-missing-checksums"
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|addKnownContentConsumer
argument_list|(
literal|"metadata-updater"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clean
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|TARGET_REPO_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|TARGET_REPO_ID
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|TARGET_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|deleteManagedRepository
argument_list|(
name|SOURCE_REPO_ID
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|copyToAnEmptyRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|initSourceTargetRepo
argument_list|()
expr_stmt|;
comment|// START SNIPPET: copy-artifact
name|ArtifactTransferRequest
name|artifactTransferRequest
init|=
operator|new
name|ArtifactTransferRequest
argument_list|()
decl_stmt|;
name|artifactTransferRequest
operator|.
name|setGroupId
argument_list|(
literal|"org.apache.karaf.features"
argument_list|)
expr_stmt|;
name|artifactTransferRequest
operator|.
name|setArtifactId
argument_list|(
literal|"org.apache.karaf.features.core"
argument_list|)
expr_stmt|;
name|artifactTransferRequest
operator|.
name|setVersion
argument_list|(
literal|"2.2.2"
argument_list|)
expr_stmt|;
name|artifactTransferRequest
operator|.
name|setRepositoryId
argument_list|(
name|SOURCE_REPO_ID
argument_list|)
expr_stmt|;
name|artifactTransferRequest
operator|.
name|setTargetRepositoryId
argument_list|(
name|TARGET_REPO_ID
argument_list|)
expr_stmt|;
name|Boolean
name|res
init|=
name|getRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|copyArtifact
argument_list|(
name|artifactTransferRequest
argument_list|)
decl_stmt|;
comment|// END SNIPPET: copy-artifact
name|assertTrue
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|String
name|targetRepoPath
init|=
name|getManagedRepositoriesService
argument_list|(
name|authorizationHeader
argument_list|)
operator|.
name|getManagedRepository
argument_list|(
name|TARGET_REPO_ID
argument_list|)
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|File
name|artifact
init|=
operator|new
name|File
argument_list|(
name|targetRepoPath
argument_list|,
literal|"/org/apache/karaf/features/org.apache.karaf.features.core/2.2.2/org.apache.karaf.features.core-2.2.2.jar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|artifact
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|pom
init|=
operator|new
name|File
argument_list|(
name|targetRepoPath
argument_list|,
literal|"/org/apache/karaf/features/org.apache.karaf.features.core/2.2.2/org.apache.karaf.features.core-2.2.2.pom"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"not exists "
operator|+
name|pom
operator|.
name|getPath
argument_list|()
argument_list|,
name|pom
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO find a way to force metadata generation and test it !!
name|clean
argument_list|()
expr_stmt|;
block|}
comment|//@Test
specifier|public
name|void
name|copyToAnExistingRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|initSourceTargetRepo
argument_list|()
expr_stmt|;
name|clean
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

