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
name|apache
operator|.
name|maven
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
name|codehaus
operator|.
name|plexus
operator|.
name|spring
operator|.
name|PlexusToSpringUtils
import|;
end_import

begin_class
specifier|public
class|class
name|DavResourceTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|DavSession
name|session
decl_stmt|;
specifier|private
name|MimeTypes
name|mimeTypes
decl_stmt|;
specifier|private
name|ArchivaDavResourceLocator
name|resourceLocator
decl_stmt|;
specifier|private
name|File
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
name|File
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
annotation|@
name|Override
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
name|session
operator|=
operator|new
name|ArchivaDavSession
argument_list|()
expr_stmt|;
name|mimeTypes
operator|=
operator|(
name|MimeTypes
operator|)
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|MimeTypes
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|baseDir
operator|=
name|getTestFile
argument_list|(
literal|"target/DavResourceTest"
argument_list|)
expr_stmt|;
name|baseDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|myResource
operator|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"myresource.jar"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Could not create "
operator|+
name|myResource
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|myResource
operator|.
name|createNewFile
argument_list|()
argument_list|)
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
name|REPOPATH
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
name|release
argument_list|(
name|mimeTypes
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|baseDir
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DavResource
name|getDavResource
parameter_list|(
name|String
name|logicalPath
parameter_list|,
name|File
name|file
parameter_list|)
block|{
return|return
operator|new
name|ArchivaDavResource
argument_list|(
name|logicalPath
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|mimeTypes
argument_list|,
name|session
argument_list|,
name|resourceLocator
argument_list|,
literal|null
argument_list|)
return|;
block|}
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
comment|//Simple lock manager will die
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
comment|//Lock should exist
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
comment|//Lock should not exist
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
block|}
end_class

end_unit

