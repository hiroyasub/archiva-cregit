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
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|ManagedRepositoryConfiguration
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
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|GetMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|PutMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
import|;
end_import

begin_comment
comment|/**  * RepositoryServletRepositoryGroupTest  *   * Test Case 1.  Accessing a valid repository group root url (e.g. http://machine.com/repository/repository-group/) returns a Bad Request (HTTP 400)  * Test Case 2.  Accessing an invalid repository group root url is forwarded to managed repository checking (this is not covered here)  * Test Case 3.  Accessing an artifact in a valid repository group will iterate over the managed repositories in the repository group  *     Test Case 3.a.  If an invalid managed repository is encountered (managed repository doesn't exist),  *                     a Not Found (HTTP 404) is returned and the iteration is broken  *     Test Case 3.b.  If an artifact is not found in a valid managed repository (after proxying, etc.),  *                     a Not Found (HTTP 404) is set but not returned yet, the iteration continues to the next managed repository.  *                     The Not Found (HTTP 404) is returned after exhausting all valid managed repositories  *     Test Case 3.c.  If an artifact is found in a valid managed repository,  *                     the artifact is returned, the iteration is broken and any Not Found (HTTP 404) is disregarded  * Test Case 4.  Accessing a valid repository group with any http write method returns a Bad Request (HTTP 400)  *                       * @author   *  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletRepositoryGroupTest
extends|extends
name|AbstractRepositoryServletTestCase
block|{
specifier|protected
name|File
name|repoRootFirst
decl_stmt|;
specifier|protected
name|File
name|repoRootLast
decl_stmt|;
specifier|protected
name|File
name|repoRootInvalid
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|MANAGED_REPO_FIRST
init|=
literal|"first"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|MANAGED_REPO_LAST
init|=
literal|"last"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|MANAGED_REPO_INVALID
init|=
literal|"invalid"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPO_GROUP_WITH_VALID_REPOS
init|=
literal|"group-with-valid-repos"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPO_GROUP_WITH_INVALID_REPOS
init|=
literal|"group-with-invalid-repos"
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
name|String
name|appserverBase
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"appserver.base"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|repoRootFirst
operator|=
operator|new
name|File
argument_list|(
name|appserverBase
argument_list|,
literal|"data/repositories/"
operator|+
name|MANAGED_REPO_FIRST
argument_list|)
expr_stmt|;
name|repoRootLast
operator|=
operator|new
name|File
argument_list|(
name|appserverBase
argument_list|,
literal|"data/repositories/"
operator|+
name|MANAGED_REPO_LAST
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
name|MANAGED_REPO_FIRST
argument_list|,
literal|"First Test Repo"
argument_list|,
name|repoRootFirst
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
name|MANAGED_REPO_LAST
argument_list|,
literal|"Last Test Repo"
argument_list|,
name|repoRootLast
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|managedRepoIds
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|managedRepoIds
operator|.
name|add
argument_list|(
name|MANAGED_REPO_FIRST
argument_list|)
expr_stmt|;
name|managedRepoIds
operator|.
name|add
argument_list|(
name|MANAGED_REPO_LAST
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addRepositoryGroup
argument_list|(
name|createRepositoryGroup
argument_list|(
name|REPO_GROUP_WITH_VALID_REPOS
argument_list|,
name|managedRepoIds
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create the repository group with an invalid managed repository
name|repoRootInvalid
operator|=
operator|new
name|File
argument_list|(
name|appserverBase
argument_list|,
literal|"data/repositories/"
operator|+
name|MANAGED_REPO_INVALID
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|managedRepositoryConfiguration
init|=
name|createManagedRepository
argument_list|(
name|MANAGED_REPO_INVALID
argument_list|,
literal|"Invalid Test Repo"
argument_list|,
name|repoRootInvalid
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
name|MANAGED_REPO_FIRST
argument_list|,
literal|"First Test Repo"
argument_list|,
name|repoRootFirst
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|managedRepositoryConfiguration
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
name|MANAGED_REPO_LAST
argument_list|,
literal|"Last Test Repo"
argument_list|,
name|repoRootLast
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|invalidManagedRepoIds
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|invalidManagedRepoIds
operator|.
name|add
argument_list|(
name|MANAGED_REPO_FIRST
argument_list|)
expr_stmt|;
name|invalidManagedRepoIds
operator|.
name|add
argument_list|(
name|MANAGED_REPO_INVALID
argument_list|)
expr_stmt|;
name|invalidManagedRepoIds
operator|.
name|add
argument_list|(
name|MANAGED_REPO_LAST
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addRepositoryGroup
argument_list|(
name|createRepositoryGroup
argument_list|(
name|REPO_GROUP_WITH_INVALID_REPOS
argument_list|,
name|invalidManagedRepoIds
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|removeManagedRepository
argument_list|(
name|managedRepositoryConfiguration
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoRootInvalid
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootFirst
argument_list|)
expr_stmt|;
name|setupCleanRepo
argument_list|(
name|repoRootLast
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
comment|/*      * Test Case 3.c      */
specifier|public
name|void
name|testGetFromFirstManagedRepositoryReturnOk
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|resourceName
init|=
literal|"dummy/dummy-first-resource/1.0/dummy-first-resource-1.0.txt"
decl_stmt|;
name|File
name|dummyInternalResourceFile
init|=
operator|new
name|File
argument_list|(
name|repoRootFirst
argument_list|,
name|resourceName
argument_list|)
decl_stmt|;
name|dummyInternalResourceFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|dummyInternalResourceFile
argument_list|,
literal|"first"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/"
operator|+
name|REPO_GROUP_WITH_VALID_REPOS
operator|+
literal|"/"
operator|+
name|resourceName
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
literal|"first"
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*      * Test Case 3.c      */
specifier|public
name|void
name|testGetFromLastManagedRepositoryReturnOk
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|resourceName
init|=
literal|"dummy/dummy-last-resource/1.0/dummy-last-resource-1.0.txt"
decl_stmt|;
name|File
name|dummyReleasesResourceFile
init|=
operator|new
name|File
argument_list|(
name|repoRootLast
argument_list|,
name|resourceName
argument_list|)
decl_stmt|;
name|dummyReleasesResourceFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|dummyReleasesResourceFile
argument_list|,
literal|"last"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/"
operator|+
name|REPO_GROUP_WITH_VALID_REPOS
operator|+
literal|"/"
operator|+
name|resourceName
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
literal|"last"
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*      * Test Case 3.b      */
specifier|public
name|void
name|testGetFromValidRepositoryGroupReturnNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|resourceName
init|=
literal|"dummy/dummy-no-resource/1.0/dummy-no-resource-1.0.txt"
decl_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/"
operator|+
name|REPO_GROUP_WITH_VALID_REPOS
operator|+
literal|"/"
operator|+
name|resourceName
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseNotFound
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * Test Case 3.a      */
specifier|public
name|void
name|testGetInvalidManagedRepositoryInGroupReturnNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|resourceName
init|=
literal|"dummy/dummy-no-resource/1.0/dummy-no-resource-1.0.txt"
decl_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/"
operator|+
name|REPO_GROUP_WITH_INVALID_REPOS
operator|+
literal|"/"
operator|+
name|resourceName
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseNotFound
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * Test Case 4      */
specifier|public
name|void
name|testPutValidRepositoryGroupReturnBadRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|resourceName
init|=
literal|"dummy/dummy-put-resource/1.0/dummy-put-resource-1.0.txt"
decl_stmt|;
name|String
name|putUrl
init|=
literal|"http://machine.com/repository/"
operator|+
name|REPO_GROUP_WITH_VALID_REPOS
operator|+
literal|"/"
operator|+
name|resourceName
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
decl_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"text/plain"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseMethodNotAllowed
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBrowseRepositoryGroup
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/"
operator|+
name|REPO_GROUP_WITH_VALID_REPOS
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have received a response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have been an 401 response code."
argument_list|,
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|response
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertResponseMethodNotAllowed
parameter_list|(
name|WebResponse
name|response
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Should have recieved a response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have been an 405/Method Not Allowed response code."
argument_list|,
name|HttpServletResponse
operator|.
name|SC_METHOD_NOT_ALLOWED
argument_list|,
name|response
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|RepositoryGroupConfiguration
name|createRepositoryGroup
parameter_list|(
name|String
name|id
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|repositories
parameter_list|)
block|{
name|RepositoryGroupConfiguration
name|repoGroupConfiguration
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|()
decl_stmt|;
name|repoGroupConfiguration
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repoGroupConfiguration
operator|.
name|setRepositories
argument_list|(
name|repositories
argument_list|)
expr_stmt|;
return|return
name|repoGroupConfiguration
return|;
block|}
block|}
end_class

end_unit

