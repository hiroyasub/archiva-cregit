begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|filelock
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|edu
operator|.
name|umd
operator|.
name|cs
operator|.
name|mtc
operator|.
name|MultithreadedTestCase
import|;
end_import

begin_import
import|import
name|edu
operator|.
name|umd
operator|.
name|cs
operator|.
name|mtc
operator|.
name|TestFramework
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
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
name|inject
operator|.
name|Named
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
name|FileOutputStream
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
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_comment
comment|//import org.apache.commons.io.IOUtils;
end_comment

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
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
block|}
argument_list|)
specifier|public
class|class
name|DefaultFileLockManagerTest
block|{
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"fileLockManager#default"
argument_list|)
name|FileLockManager
name|fileLockManager
decl_stmt|;
class|class
name|ConcurrentFileWrite
extends|extends
name|MultithreadedTestCase
block|{
name|AtomicInteger
name|success
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|FileLockManager
name|fileLockManager
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"buildDirectory"
argument_list|)
argument_list|,
literal|"foo.txt"
argument_list|)
decl_stmt|;
name|File
name|largeJar
init|=
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
literal|"src/test/cassandra-all-2.0.3.jar"
argument_list|)
decl_stmt|;
name|ConcurrentFileWrite
parameter_list|(
name|FileLockManager
name|fileLockManager
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|fileLockManager
operator|=
name|fileLockManager
expr_stmt|;
comment|//file.createNewFile();
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|()
block|{
block|}
specifier|public
name|void
name|thread1
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread1"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|writeFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|delete
argument_list|()
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|largeJar
operator|.
name|toPath
argument_list|()
argument_list|,
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread1 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread2
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread2"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|writeFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|delete
argument_list|()
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|largeJar
operator|.
name|toPath
argument_list|()
argument_list|,
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread2 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread3
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread3"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|readFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|copy
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|File
operator|.
name|createTempFile
argument_list|(
literal|"foo"
argument_list|,
literal|".jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread3 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread4
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread4"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|writeFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|delete
argument_list|()
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|largeJar
operator|.
name|toPath
argument_list|()
argument_list|,
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread4 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread5
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread5"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|writeFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|delete
argument_list|()
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|largeJar
operator|.
name|toPath
argument_list|()
argument_list|,
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread5 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread6
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread6"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|readFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|copy
argument_list|(
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|File
operator|.
name|createTempFile
argument_list|(
literal|"foo"
argument_list|,
literal|".jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread6 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread7
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread7"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|writeFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|delete
argument_list|()
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|largeJar
operator|.
name|toPath
argument_list|()
argument_list|,
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread7 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread8
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread8"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|readFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|copy
argument_list|(
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|File
operator|.
name|createTempFile
argument_list|(
literal|"foo"
argument_list|,
literal|".jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread8 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread9
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread7"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|writeFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|delete
argument_list|()
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|largeJar
operator|.
name|toPath
argument_list|()
argument_list|,
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread9 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|thread10
parameter_list|()
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
throws|,
name|IOException
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"thread10"
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
name|fileLockManager
operator|.
name|readFileLock
argument_list|(
name|this
operator|.
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|copy
argument_list|(
name|lock
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|File
operator|.
name|createTempFile
argument_list|(
literal|"foo"
argument_list|,
literal|".jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fileLockManager
operator|.
name|release
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"thread8 ok"
argument_list|)
expr_stmt|;
name|success
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Before
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|fileLockManager
operator|.
name|setSkipLocking
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fileLockManager
operator|.
name|clearLockFiles
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWrite
parameter_list|()
throws|throws
name|Throwable
block|{
name|ConcurrentFileWrite
name|concurrentFileWrite
init|=
operator|new
name|ConcurrentFileWrite
argument_list|(
name|fileLockManager
argument_list|)
decl_stmt|;
comment|//concurrentFileWrite.setTrace( true );
name|TestFramework
operator|.
name|runOnce
argument_list|(
name|concurrentFileWrite
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"success: {}"
argument_list|,
name|concurrentFileWrite
operator|.
name|success
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|concurrentFileWrite
operator|.
name|success
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

