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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|common
operator|.
name|filelock
operator|.
name|FileLockManager
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
name|common
operator|.
name|utils
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
name|archiva
operator|.
name|repository
operator|.
name|LayoutException
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
name|RepositoryRegistry
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
name|storage
operator|.
name|FilesystemAsset
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
name|events
operator|.
name|AuditListener
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
name|maven2
operator|.
name|MavenManagedRepository
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
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
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
name|webdav
operator|.
name|util
operator|.
name|MimeTypes
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
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavResourceFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavResourceLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavServletRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavServletResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|lock
operator|.
name|ActiveLock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|lock
operator|.
name|LockInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|lock
operator|.
name|LockManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|lock
operator|.
name|Scope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|lock
operator|.
name|SimpleLockManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|lock
operator|.
name|Type
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
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath*:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|DavResourceTest
extends|extends
name|TestCase
block|{
specifier|private
name|DavSession
name|session
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|MimeTypes
name|mimeTypes
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|FileLockManager
name|fileLockManager
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
specifier|private
name|ArchivaDavResourceLocator
name|resourceLocator
decl_stmt|;
specifier|private
name|DavResourceFactory
name|resourceFactory
decl_stmt|;
specifier|private
name|Path
name|baseDir
decl_stmt|;
specifier|private
specifier|final
name|String
name|REPOPATH
init|=
literal|"myresource.jar"
decl_stmt|;
specifier|private
name|Path
name|myResource
decl_stmt|;
specifier|private
name|DavResource
name|resource
decl_stmt|;
specifier|private
name|LockManager
name|lockManager
decl_stmt|;
specifier|private
name|MavenManagedRepository
name|repository
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
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
name|session
operator|=
operator|new
name|ArchivaDavSession
argument_list|()
expr_stmt|;
name|baseDir
operator|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/DavResourceTest"
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|baseDir
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"conf"
argument_list|)
argument_list|)
expr_stmt|;
name|repository
operator|=
name|MavenManagedRepository
operator|.
name|newLocalInstance
argument_list|(
literal|"repo001"
argument_list|,
literal|"repo001"
argument_list|,
name|baseDir
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|putRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|myResource
operator|=
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"myresource.jar"
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createFile
argument_list|(
name|myResource
argument_list|)
expr_stmt|;
name|resourceFactory
operator|=
operator|new
name|RootContextDavResourceFactory
argument_list|()
expr_stmt|;
name|resourceLocator
operator|=
operator|(
name|ArchivaDavResourceLocator
operator|)
operator|new
name|ArchivaDavLocatorFactory
argument_list|()
operator|.
name|createResourceLocator
argument_list|(
literal|"/"
argument_list|,
name|REPOPATH
argument_list|)
expr_stmt|;
name|resource
operator|=
name|getDavResource
argument_list|(
name|resourceLocator
operator|.
name|getHref
argument_list|(
literal|false
argument_list|)
argument_list|,
name|myResource
argument_list|)
expr_stmt|;
name|lockManager
operator|=
operator|new
name|SimpleLockManager
argument_list|()
expr_stmt|;
name|resource
operator|.
name|addLockManager
argument_list|(
name|lockManager
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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|baseDir
argument_list|)
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
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|appserverBase
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|appserverBase
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|DavResource
name|getDavResource
parameter_list|(
name|String
name|logicalPath
parameter_list|,
name|Path
name|file
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
operator|new
name|ArchivaDavResource
argument_list|(
operator|new
name|FilesystemAsset
argument_list|(
name|repository
argument_list|,
name|logicalPath
argument_list|,
name|file
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
argument_list|,
name|logicalPath
argument_list|,
name|repository
argument_list|,
name|session
argument_list|,
name|resourceLocator
argument_list|,
name|resourceFactory
argument_list|,
name|mimeTypes
argument_list|,
name|Collections
operator|.
expr|<
name|AuditListener
operator|>
name|emptyList
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteNonExistantResourceShould404
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|dir
init|=
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"testdir"
argument_list|)
decl_stmt|;
try|try
block|{
name|DavResource
name|directoryResource
init|=
name|getDavResource
argument_list|(
literal|"/testdir"
argument_list|,
name|dir
argument_list|)
decl_stmt|;
name|directoryResource
operator|.
name|getCollection
argument_list|()
operator|.
name|removeMember
argument_list|(
name|directoryResource
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw DavException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DavException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|DavServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|,
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|dir
init|=
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"testdir"
argument_list|)
decl_stmt|;
try|try
block|{
name|assertNotNull
argument_list|(
name|Files
operator|.
name|createDirectories
argument_list|(
name|dir
argument_list|)
argument_list|)
expr_stmt|;
name|DavResource
name|directoryResource
init|=
name|getDavResource
argument_list|(
literal|"/testdir"
argument_list|,
name|dir
argument_list|)
decl_stmt|;
name|directoryResource
operator|.
name|getCollection
argument_list|()
operator|.
name|removeMember
argument_list|(
name|directoryResource
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|dir
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteResource
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|myResource
argument_list|)
argument_list|)
expr_stmt|;
name|resource
operator|.
name|getCollection
argument_list|()
operator|.
name|removeMember
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|myResource
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsLockable
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|resource
operator|.
name|isLockable
argument_list|(
name|Type
operator|.
name|WRITE
argument_list|,
name|Scope
operator|.
name|EXCLUSIVE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|resource
operator|.
name|isLockable
argument_list|(
name|Type
operator|.
name|WRITE
argument_list|,
name|Scope
operator|.
name|SHARED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLock
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|LockInfo
name|info
init|=
operator|new
name|LockInfo
argument_list|(
name|Scope
operator|.
name|EXCLUSIVE
argument_list|,
name|Type
operator|.
name|WRITE
argument_list|,
literal|"/"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|lockManager
operator|.
name|createLock
argument_list|(
name|info
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLockIfResourceUnlockable
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|LockInfo
name|info
init|=
operator|new
name|LockInfo
argument_list|(
name|Scope
operator|.
name|SHARED
argument_list|,
name|Type
operator|.
name|WRITE
argument_list|,
literal|"/"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
try|try
block|{
name|lockManager
operator|.
name|createLock
argument_list|(
name|info
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw dav exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Simple lock manager will die
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetLock
parameter_list|()
throws|throws
name|Exception
block|{
name|LockInfo
name|info
init|=
operator|new
name|LockInfo
argument_list|(
name|Scope
operator|.
name|EXCLUSIVE
argument_list|,
name|Type
operator|.
name|WRITE
argument_list|,
literal|"/"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|lockManager
operator|.
name|createLock
argument_list|(
name|info
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// Lock should exist
name|assertNotNull
argument_list|(
name|resource
operator|.
name|getLock
argument_list|(
name|Type
operator|.
name|WRITE
argument_list|,
name|Scope
operator|.
name|EXCLUSIVE
argument_list|)
argument_list|)
expr_stmt|;
comment|// Lock should not exist
name|assertNull
argument_list|(
name|resource
operator|.
name|getLock
argument_list|(
name|Type
operator|.
name|WRITE
argument_list|,
name|Scope
operator|.
name|SHARED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRefreshLockThrowsExceptionIfNoLockIsPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|LockInfo
name|info
init|=
operator|new
name|LockInfo
argument_list|(
name|Scope
operator|.
name|EXCLUSIVE
argument_list|,
name|Type
operator|.
name|WRITE
argument_list|,
literal|"/"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
try|try
block|{
name|lockManager
operator|.
name|refreshLock
argument_list|(
name|info
argument_list|,
literal|"notoken"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw dav exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DavException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|DavServletResponse
operator|.
name|SC_PRECONDITION_FAILED
argument_list|,
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRefreshLock
parameter_list|()
throws|throws
name|Exception
block|{
name|LockInfo
name|info
init|=
operator|new
name|LockInfo
argument_list|(
name|Scope
operator|.
name|EXCLUSIVE
argument_list|,
name|Type
operator|.
name|WRITE
argument_list|,
literal|"/"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|lockManager
operator|.
name|createLock
argument_list|(
name|info
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|ActiveLock
name|lock
init|=
name|resource
operator|.
name|getLocks
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|lockManager
operator|.
name|refreshLock
argument_list|(
name|info
argument_list|,
name|lock
operator|.
name|getToken
argument_list|()
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnlock
parameter_list|()
throws|throws
name|Exception
block|{
name|LockInfo
name|info
init|=
operator|new
name|LockInfo
argument_list|(
name|Scope
operator|.
name|EXCLUSIVE
argument_list|,
name|Type
operator|.
name|WRITE
argument_list|,
literal|"/"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|lockManager
operator|.
name|createLock
argument_list|(
name|info
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|ActiveLock
name|lock
init|=
name|resource
operator|.
name|getLocks
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|lockManager
operator|.
name|releaseLock
argument_list|(
name|lock
operator|.
name|getToken
argument_list|()
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnlockThrowsDavExceptionIfNotLocked
parameter_list|()
throws|throws
name|Exception
block|{
name|LockInfo
name|info
init|=
operator|new
name|LockInfo
argument_list|(
name|Scope
operator|.
name|EXCLUSIVE
argument_list|,
name|Type
operator|.
name|WRITE
argument_list|,
literal|"/"
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|lockManager
operator|.
name|createLock
argument_list|(
name|info
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
try|try
block|{
name|lockManager
operator|.
name|releaseLock
argument_list|(
literal|"BLAH"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw DavException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DavException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|DavServletResponse
operator|.
name|SC_LOCKED
argument_list|,
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnlockThrowsDavExceptionIfResourceNotLocked
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
try|try
block|{
name|lockManager
operator|.
name|releaseLock
argument_list|(
literal|"BLAH"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw DavException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DavException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|DavServletResponse
operator|.
name|SC_PRECONDITION_FAILED
argument_list|,
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|getLocks
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|RootContextDavResourceFactory
implements|implements
name|DavResourceFactory
block|{
annotation|@
name|Override
specifier|public
name|DavResource
name|createResource
parameter_list|(
name|DavResourceLocator
name|locator
parameter_list|,
name|DavServletRequest
name|request
parameter_list|,
name|DavServletResponse
name|response
parameter_list|)
throws|throws
name|DavException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|DavResource
name|createResource
parameter_list|(
name|DavResourceLocator
name|locator
parameter_list|,
name|DavSession
name|session
parameter_list|)
throws|throws
name|DavException
block|{
try|try
block|{
return|return
operator|new
name|ArchivaDavResource
argument_list|(
operator|new
name|FilesystemAsset
argument_list|(
name|repository
argument_list|,
literal|"/"
argument_list|,
name|baseDir
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
argument_list|,
literal|"/"
argument_list|,
name|repository
argument_list|,
name|session
argument_list|,
name|resourceLocator
argument_list|,
name|resourceFactory
argument_list|,
name|mimeTypes
argument_list|,
name|Collections
operator|.
expr|<
name|AuditListener
operator|>
name|emptyList
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DavException
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

