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
name|proxy
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepositoryFactory
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
name|artifact
operator|.
name|repository
operator|.
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|wagon
operator|.
name|TransferFailedException
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
name|wagon
operator|.
name|Wagon
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
name|wagon
operator|.
name|authorization
operator|.
name|AuthorizationException
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
name|PlexusTestCase
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
name|util
operator|.
name|FileUtils
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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

begin_comment
comment|/**  * @author Brett Porter  * @todo! tests to do vvv  * @todo test when failure should be cached but caching is disabled  * @todo test snapshots - general  * @todo test snapshots - newer version on repo2 is pulled down  * @todo test snapshots - older version on repo2 is skipped  * @todo test snapshots - update interval  * @todo test metadata - general  * @todo test metadata - multiple repos are merged  * @todo test metadata - update interval  * @todo test metadata - looking for an update and file has been removed remotely  * @todo test when managed repo is m1 layout (proxy is m2), including metadata  * @todo test when one proxied repo is m1 layout (managed is m2), including metadata  * @todo test when one proxied repo is m1 layout (managed is m1), including metadata  * @todo test get always  * @todo test get always when resource is present locally but not in any proxied repos (should fail)  * @todo test remote checksum only md5  * @todo test remote checksum only sha1  * @todo test remote checksum missing  * @todo test remote checksum present and correct  * @todo test remote checksum present and incorrect  * @todo test remote checksum transfer failed  * @todo test when failure is cached but cache period is over (and check failure is cleared)  */
end_comment

begin_class
specifier|public
class|class
name|ProxyRequestHandlerTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ProxyRequestHandler
name|requestHandler
decl_stmt|;
specifier|private
name|List
name|proxiedRepositories
decl_stmt|;
specifier|private
name|ArtifactRepository
name|defaultManagedRepository
decl_stmt|;
specifier|private
name|ArtifactRepository
name|proxiedRepository1
decl_stmt|;
specifier|private
name|ArtifactRepository
name|proxiedRepository2
decl_stmt|;
specifier|private
name|ArtifactRepositoryLayout
name|defaultLayout
decl_stmt|;
specifier|private
name|ArtifactRepositoryFactory
name|factory
decl_stmt|;
specifier|private
name|MockControl
name|wagonMockControl
decl_stmt|;
specifier|private
name|Wagon
name|wagonMock
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
name|requestHandler
operator|=
operator|(
name|ProxyRequestHandler
operator|)
name|lookup
argument_list|(
name|ProxyRequestHandler
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|File
name|repoLocation
init|=
name|getTestFile
argument_list|(
literal|"target/test-repository/managed"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|copyDirectoryStructure
argument_list|(
name|getTestFile
argument_list|(
literal|"src/test/repositories/managed"
argument_list|)
argument_list|,
name|repoLocation
argument_list|)
expr_stmt|;
name|defaultLayout
operator|=
operator|(
name|ArtifactRepositoryLayout
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|ROLE
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|factory
operator|=
operator|(
name|ArtifactRepositoryFactory
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryFactory
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|defaultManagedRepository
operator|=
name|createRepository
argument_list|(
literal|"managed-repository"
argument_list|,
name|repoLocation
argument_list|)
expr_stmt|;
name|File
name|location
init|=
name|getTestFile
argument_list|(
literal|"src/test/repositories/proxied1"
argument_list|)
decl_stmt|;
name|proxiedRepository1
operator|=
name|createRepository
argument_list|(
literal|"proxied1"
argument_list|,
name|location
argument_list|)
expr_stmt|;
name|location
operator|=
name|getTestFile
argument_list|(
literal|"src/test/repositories/proxied2"
argument_list|)
expr_stmt|;
name|proxiedRepository2
operator|=
name|createRepository
argument_list|(
literal|"proxied2"
argument_list|,
name|location
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|=
operator|new
name|ArrayList
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|createProxiedRepository
argument_list|(
name|proxiedRepository1
argument_list|)
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|createProxiedRepository
argument_list|(
name|proxiedRepository2
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|=
name|MockControl
operator|.
name|createNiceControl
argument_list|(
name|Wagon
operator|.
name|class
argument_list|)
expr_stmt|;
name|wagonMock
operator|=
operator|(
name|Wagon
operator|)
name|wagonMockControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|WagonDelegate
name|delegate
init|=
operator|(
name|WagonDelegate
operator|)
name|lookup
argument_list|(
name|Wagon
operator|.
name|ROLE
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|delegate
operator|.
name|setDelegate
argument_list|(
name|wagonMock
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetDefaultLayoutNotPresent
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout/1.0/get-default-layout-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file matches"
argument_list|,
name|expectedFile
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file created"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|proxiedRepository1
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|expectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|proxiedFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file contents"
argument_list|,
name|expectedContents
argument_list|,
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: timestamp preservation requires support for that in wagon
comment|//        assertEquals( "Check file timestamp", proxiedFile.lastModified(), file.lastModified() );
block|}
specifier|public
name|void
name|testGetDefaultLayoutAlreadyPresent
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|expectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|expectedFile
argument_list|)
decl_stmt|;
name|long
name|originalModificationTime
init|=
name|expectedFile
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file matches"
argument_list|,
name|expectedFile
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file created"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check file contents"
argument_list|,
name|expectedContents
argument_list|,
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|proxiedRepository1
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|unexpectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|proxiedFile
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check file contents"
argument_list|,
name|unexpectedContents
operator|.
name|equals
argument_list|(
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check file timestamp is not that of proxy"
argument_list|,
name|proxiedFile
operator|.
name|lastModified
argument_list|()
operator|==
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check file timestamp is that of original managed file"
argument_list|,
name|originalModificationTime
argument_list|,
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetWhenInBothProxiedRepos
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-both-proxies/1.0/get-in-both-proxies-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file matches"
argument_list|,
name|expectedFile
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file created"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|proxiedRepository1
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|expectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|proxiedFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file contents"
argument_list|,
name|expectedContents
argument_list|,
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|proxiedFile
operator|=
operator|new
name|File
argument_list|(
name|proxiedRepository2
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|String
name|unexpectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|proxiedFile
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check file contents"
argument_list|,
name|unexpectedContents
operator|.
name|equals
argument_list|(
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetInSecondProxiedRepo
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-second-proxy/1.0/get-in-second-proxy-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file matches"
argument_list|,
name|expectedFile
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file created"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|proxiedRepository2
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|expectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|proxiedFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file contents"
argument_list|,
name|expectedContents
argument_list|,
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotFoundInAnyProxies
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/does-not-exist/1.0/does-not-exist-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"File returned was: "
operator|+
name|file
operator|+
literal|"; should have got a not found exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
comment|// expected, but check file was not created
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testGetInSecondProxiedRepoFirstFails
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
throws|,
name|TransferFailedException
throws|,
name|AuthorizationException
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-second-proxy/1.0/get-in-second-proxy-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|proxiedRepository1
operator|=
name|createRepository
argument_list|(
literal|"proxied1"
argument_list|,
literal|"test://..."
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ProxiedArtifactRepository
name|proxiedArtifactRepository
init|=
name|createProxiedRepository
argument_list|(
name|proxiedRepository1
argument_list|)
decl_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|proxiedArtifactRepository
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|createProxiedRepository
argument_list|(
name|proxiedRepository2
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|expectedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".tmp"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check file matches"
argument_list|,
name|expectedFile
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file created"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|proxiedRepository2
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|expectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|proxiedFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file contents"
argument_list|,
name|expectedContents
argument_list|,
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check failure"
argument_list|,
name|proxiedArtifactRepository
operator|.
name|isCachedFailure
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetButAllRepositoriesFail
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
throws|,
name|TransferFailedException
throws|,
name|AuthorizationException
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-second-proxy/1.0/get-in-second-proxy-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|proxiedRepository1
operator|=
name|createRepository
argument_list|(
literal|"proxied1"
argument_list|,
literal|"test://..."
argument_list|)
expr_stmt|;
name|proxiedRepository2
operator|=
name|createRepository
argument_list|(
literal|"proxied2"
argument_list|,
literal|"test://..."
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ProxiedArtifactRepository
name|proxiedArtifactRepository1
init|=
name|createProxiedRepository
argument_list|(
name|proxiedRepository1
argument_list|)
decl_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|proxiedArtifactRepository1
argument_list|)
expr_stmt|;
name|ProxiedArtifactRepository
name|proxiedArtifactRepository2
init|=
name|createProxiedRepository
argument_list|(
name|proxiedRepository2
argument_list|)
decl_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|proxiedArtifactRepository2
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|expectedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".tmp"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|expectedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".tmp"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Found file: "
operator|+
name|file
operator|+
literal|"; but was expecting a failure"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
comment|// as expected
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check failure"
argument_list|,
name|proxiedArtifactRepository1
operator|.
name|isCachedFailure
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check failure"
argument_list|,
name|proxiedArtifactRepository2
operator|.
name|isCachedFailure
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: do we really want failures to present as a not found?
comment|// TODO: How much information on each failure should we pass back to the user vs. logging in the proxy?
block|}
block|}
specifier|public
name|void
name|testGetInSecondProxiedRepoFirstHardFails
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
throws|,
name|TransferFailedException
throws|,
name|AuthorizationException
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-second-proxy/1.0/get-in-second-proxy-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|proxiedRepository1
operator|=
name|createRepository
argument_list|(
literal|"proxied1"
argument_list|,
literal|"test://..."
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ProxiedArtifactRepository
name|proxiedArtifactRepository
init|=
name|createHardFailProxiedRepository
argument_list|(
name|proxiedRepository1
argument_list|)
decl_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|proxiedArtifactRepository
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|createProxiedRepository
argument_list|(
name|proxiedRepository2
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|expectedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".tmp"
argument_list|)
argument_list|)
expr_stmt|;
name|TransferFailedException
name|failedException
init|=
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
decl_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
name|failedException
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Found file: "
operator|+
name|file
operator|+
literal|"; but was expecting a failure"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
comment|// expect a failure
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check cause"
argument_list|,
name|failedException
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check failure"
argument_list|,
name|proxiedArtifactRepository
operator|.
name|isCachedFailure
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testGetInSecondProxiedRepoFirstFailsFromCache
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
throws|,
name|TransferFailedException
throws|,
name|AuthorizationException
block|{
comment|// fail from the cache, even though it is in the first repo now
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-both-proxies/1.0/get-in-both-proxies-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ProxiedArtifactRepository
name|proxiedArtifactRepository
init|=
name|createProxiedRepository
argument_list|(
name|proxiedRepository1
argument_list|)
decl_stmt|;
name|proxiedArtifactRepository
operator|.
name|addFailure
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|proxiedArtifactRepository
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|createProxiedRepository
argument_list|(
name|proxiedRepository2
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file matches"
argument_list|,
name|expectedFile
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file created"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|proxiedRepository2
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|expectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|proxiedFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file contents"
argument_list|,
name|expectedContents
argument_list|,
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|proxiedFile
operator|=
operator|new
name|File
argument_list|(
name|proxiedRepository1
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|String
name|unexpectedContents
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|proxiedFile
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check file contents"
argument_list|,
name|unexpectedContents
operator|.
name|equals
argument_list|(
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetInSecondProxiedRepoFirstHardFailsFromCache
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
throws|,
name|TransferFailedException
throws|,
name|AuthorizationException
block|{
comment|// fail from the cache, even though it is in the first repo now
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-both-proxies/1.0/get-in-both-proxies-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|defaultManagedRepository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ProxiedArtifactRepository
name|proxiedArtifactRepository
init|=
name|createHardFailProxiedRepository
argument_list|(
name|proxiedRepository1
argument_list|)
decl_stmt|;
name|proxiedArtifactRepository
operator|.
name|addFailure
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|proxiedArtifactRepository
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|createProxiedRepository
argument_list|(
name|proxiedRepository2
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|File
name|file
init|=
name|requestHandler
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|proxiedRepositories
argument_list|,
name|defaultManagedRepository
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Found file: "
operator|+
name|file
operator|+
literal|"; but was expecting a failure"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
comment|// expect a failure
name|assertTrue
argument_list|(
literal|"Check failure"
argument_list|,
name|proxiedArtifactRepository
operator|.
name|isCachedFailure
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A faster recursive copy that omits .svn directories.      *      * @param sourceDirectory the source directory to copy      * @param destDirectory   the target location      * @throws java.io.IOException if there is a copying problem      * @todo get back into plexus-utils, share with indexing module      */
specifier|private
specifier|static
name|void
name|copyDirectoryStructure
parameter_list|(
name|File
name|sourceDirectory
parameter_list|,
name|File
name|destDirectory
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|sourceDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Source directory doesn't exists ("
operator|+
name|sourceDirectory
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|")."
argument_list|)
throw|;
block|}
name|File
index|[]
name|files
init|=
name|sourceDirectory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|String
name|sourcePath
init|=
name|sourceDirectory
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|file
init|=
name|files
index|[
name|i
index|]
decl_stmt|;
name|String
name|dest
init|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|dest
operator|=
name|dest
operator|.
name|substring
argument_list|(
name|sourcePath
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|File
name|destination
init|=
operator|new
name|File
argument_list|(
name|destDirectory
argument_list|,
name|dest
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|destination
operator|=
name|destination
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|copyFileToDirectory
argument_list|(
name|file
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
literal|".svn"
operator|.
name|equals
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|destination
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|destination
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not create destination directory '"
operator|+
name|destination
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"'."
argument_list|)
throw|;
block|}
name|copyDirectoryStructure
argument_list|(
name|file
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown file type: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
specifier|static
name|ProxiedArtifactRepository
name|createProxiedRepository
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
block|{
name|ProxiedArtifactRepository
name|proxiedArtifactRepository
init|=
operator|new
name|ProxiedArtifactRepository
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|proxiedArtifactRepository
operator|.
name|setName
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|proxiedArtifactRepository
operator|.
name|setCacheFailures
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|proxiedArtifactRepository
return|;
block|}
specifier|private
specifier|static
name|ProxiedArtifactRepository
name|createHardFailProxiedRepository
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
block|{
name|ProxiedArtifactRepository
name|proxiedArtifactRepository
init|=
name|createProxiedRepository
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|proxiedArtifactRepository
operator|.
name|setHardFail
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|proxiedArtifactRepository
return|;
block|}
specifier|private
name|ArtifactRepository
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|File
name|repoLocation
parameter_list|)
throws|throws
name|MalformedURLException
block|{
return|return
name|createRepository
argument_list|(
name|id
argument_list|,
name|repoLocation
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|ArtifactRepository
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|url
parameter_list|)
block|{
return|return
name|createRepository
argument_list|(
name|id
argument_list|,
name|url
argument_list|,
name|defaultLayout
argument_list|)
return|;
block|}
specifier|private
name|ArtifactRepository
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|url
parameter_list|,
name|ArtifactRepositoryLayout
name|repositoryLayout
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createArtifactRepository
argument_list|(
name|id
argument_list|,
name|url
argument_list|,
name|repositoryLayout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

