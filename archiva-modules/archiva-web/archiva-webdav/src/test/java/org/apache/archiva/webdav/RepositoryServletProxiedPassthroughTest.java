begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
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
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebResponse
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

begin_comment
comment|/**  * RepositoryServlet Tests, Proxied, Get of resources that are not artifacts or metadata, with varying policy settings.  *   */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletProxiedPassthroughTest
extends|extends
name|AbstractRepositoryServletProxiedTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_SHA1
init|=
literal|"2aab0a51c04c9023636852f3e63a68034ba10142"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATH_SHA1
init|=
literal|"org/apache/archiva/test/1.0/test-1.0.jar.sha1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_ASC
init|=
literal|"-----BEGIN PGP SIGNATURE-----\n"
operator|+
literal|"Version: GnuPG v1.4.8 (Darwin)\n"
operator|+
literal|"\n"
operator|+
literal|"iEYEABECAAYFAkiAIVgACgkQxbsDNW2stZZjyACeK3LW+ZDeawCyJj4XgvUaJkNh\n"
operator|+
literal|"qIEAoIUiijY4Iw82RWOT75Rt3yZuY6ZI\n"
operator|+
literal|"=WLkm\n"
operator|+
literal|"-----END PGP SIGNATURE-----\n"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATH_ASC
init|=
literal|"org/apache/archiva/test/1.0/test-1.0.jar.asc"
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedManagedNewerSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|NEWER
operator|*
name|OVER_ONE_DAY
operator|)
argument_list|,
name|PATH_SHA1
argument_list|,
name|CONTENT_SHA1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedManagedOlderSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|OLDER
operator|*
name|OVER_ONE_DAY
operator|)
argument_list|,
name|PATH_SHA1
argument_list|,
name|CONTENT_SHA1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedNoManagedContentSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|NO_MANAGED_COPY
argument_list|,
name|PATH_SHA1
argument_list|,
name|CONTENT_SHA1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedEqualSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
name|PATH_SHA1
argument_list|,
name|CONTENT_SHA1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedManagedNewerAsc
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|NEWER
operator|*
name|OVER_ONE_DAY
operator|)
argument_list|,
name|PATH_ASC
argument_list|,
name|CONTENT_ASC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedManagedOlderAsc
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|OLDER
operator|*
name|OVER_ONE_DAY
operator|)
argument_list|,
name|PATH_ASC
argument_list|,
name|CONTENT_ASC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedNoManagedContentAsc
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|NO_MANAGED_COPY
argument_list|,
name|PATH_ASC
argument_list|,
name|CONTENT_ASC
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedEqualAsc
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
name|PATH_ASC
argument_list|,
name|CONTENT_ASC
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertGetProxiedResource
parameter_list|(
name|int
name|expectation
parameter_list|,
name|boolean
name|hasManagedCopy
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|Exception
block|{
name|assertGetProxiedResource
argument_list|(
name|expectation
argument_list|,
name|hasManagedCopy
argument_list|,
literal|0
argument_list|,
name|path
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertGetProxiedResource
parameter_list|(
name|int
name|expectation
parameter_list|,
name|boolean
name|hasManagedCopy
parameter_list|,
name|long
name|deltaManagedToRemoteTimestamp
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|contents
parameter_list|)
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupCentralRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|expectedRemoteContents
init|=
name|contents
decl_stmt|;
name|String
name|expectedManagedContents
init|=
literal|null
decl_stmt|;
name|File
name|remoteFile
init|=
name|populateRepo
argument_list|(
name|remoteCentral
argument_list|,
name|path
argument_list|,
name|expectedRemoteContents
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasManagedCopy
condition|)
block|{
name|expectedManagedContents
operator|=
name|contents
expr_stmt|;
name|File
name|managedFile
init|=
name|populateRepo
argument_list|(
name|repoRootInternal
argument_list|,
name|path
argument_list|,
name|expectedManagedContents
argument_list|)
decl_stmt|;
name|managedFile
operator|.
name|setLastModified
argument_list|(
name|remoteFile
operator|.
name|lastModified
argument_list|()
operator|+
name|deltaManagedToRemoteTimestamp
argument_list|)
expr_stmt|;
block|}
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteCentral
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
comment|// --- Execution
comment|// process the response code later, not via an exception.
comment|//HttpUnitOptions.setExceptionsThrownOnErrorStatus( false );
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|path
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
comment|// --- Verification
switch|switch
condition|(
name|expectation
condition|)
block|{
case|case
name|EXPECT_MANAGED_CONTENTS
case|:
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Invalid Test Case: Can't expect managed contents with "
operator|+
literal|"test that doesn't have a managed copy in the first place."
argument_list|,
name|hasManagedCopy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected managed file contents"
argument_list|,
name|expectedManagedContents
argument_list|,
name|response
operator|.
name|getContentAsString
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXPECT_REMOTE_CONTENTS
case|:
name|assertResponseOK
argument_list|(
name|response
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected remote file contents"
argument_list|,
name|expectedRemoteContents
argument_list|,
name|response
operator|.
name|getContentAsString
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXPECT_NOT_FOUND
case|:
name|assertResponseNotFound
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertManagedFileNotExists
argument_list|(
name|repoRootInternal
argument_list|,
name|path
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

