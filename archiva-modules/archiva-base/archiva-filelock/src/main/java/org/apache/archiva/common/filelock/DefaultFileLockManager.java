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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|time
operator|.
name|StopWatch
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
name|stereotype
operator|.
name|Service
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
name|FileNotFoundException
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
name|io
operator|.
name|RandomAccessFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|ClosedChannelException
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
name|ConcurrentHashMap
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
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 2.0.0  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"fileLockManager#default"
argument_list|)
specifier|public
class|class
name|DefaultFileLockManager
implements|implements
name|FileLockManager
block|{
comment|// TODO currently we create lock for read and write!!
comment|// the idea could be to store lock here with various clients read/write
comment|// only read could be a more simple lock and acquire a write lock means waiting the end of all reading threads
specifier|private
specifier|static
specifier|final
name|ConcurrentMap
argument_list|<
name|File
argument_list|,
name|Lock
argument_list|>
name|lockFiles
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|File
argument_list|,
name|Lock
argument_list|>
argument_list|(
literal|64
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|skipLocking
init|=
literal|true
decl_stmt|;
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|int
name|timeout
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Lock
name|readFileLock
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
block|{
if|if
condition|(
name|skipLocking
condition|)
block|{
return|return
operator|new
name|Lock
argument_list|(
name|file
argument_list|)
return|;
block|}
name|StopWatch
name|stopWatch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|boolean
name|acquired
init|=
literal|false
decl_stmt|;
name|mkdirs
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
name|Lock
name|lock
init|=
literal|null
decl_stmt|;
name|stopWatch
operator|.
name|start
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|acquired
condition|)
block|{
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|long
name|delta
init|=
name|stopWatch
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"delta {}, timeout {}"
argument_list|,
name|delta
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
if|if
condition|(
name|delta
operator|>
name|timeout
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot acquire read lock within {} millis. Will skip the file: {}"
argument_list|,
name|timeout
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// we could not get the lock within the timeout period, so  throw  FileLockTimeoutException
throw|throw
operator|new
name|FileLockTimeoutException
argument_list|()
throw|;
block|}
block|}
name|Lock
name|current
init|=
name|lockFiles
operator|.
name|get
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"read lock file exist continue wait"
argument_list|)
expr_stmt|;
continue|continue;
block|}
try|try
block|{
name|lock
operator|=
operator|new
name|Lock
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|createNewFileQuietly
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|lock
operator|.
name|openLock
argument_list|(
literal|false
argument_list|,
name|timeout
operator|>
literal|0
argument_list|)
expr_stmt|;
name|acquired
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
comment|// can happen if an other thread has deleted the file
comment|// close RandomAccessFile!!!
if|if
condition|(
name|lock
operator|!=
literal|null
condition|)
block|{
name|closeQuietly
argument_list|(
name|lock
operator|.
name|getRandomAccessFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"read Lock skip: {} try to create file"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|createNewFileQuietly
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FileLockException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"openLock {}:{}"
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Lock
name|current
init|=
name|lockFiles
operator|.
name|putIfAbsent
argument_list|(
name|file
argument_list|,
name|lock
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|lock
operator|=
name|current
expr_stmt|;
block|}
return|return
name|lock
return|;
block|}
annotation|@
name|Override
specifier|public
name|Lock
name|writeFileLock
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileLockException
throws|,
name|FileLockTimeoutException
block|{
if|if
condition|(
name|skipLocking
condition|)
block|{
return|return
operator|new
name|Lock
argument_list|(
name|file
argument_list|)
return|;
block|}
name|mkdirs
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
name|StopWatch
name|stopWatch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|boolean
name|acquired
init|=
literal|false
decl_stmt|;
name|Lock
name|lock
init|=
literal|null
decl_stmt|;
name|stopWatch
operator|.
name|start
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|acquired
condition|)
block|{
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|long
name|delta
init|=
name|stopWatch
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"delta {}, timeout {}"
argument_list|,
name|delta
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
if|if
condition|(
name|delta
operator|>
name|timeout
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot acquire read lock within {} millis. Will skip the file: {}"
argument_list|,
name|timeout
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// we could not get the lock within the timeout period, so throw FileLockTimeoutException
throw|throw
operator|new
name|FileLockTimeoutException
argument_list|()
throw|;
block|}
block|}
name|Lock
name|current
init|=
name|lockFiles
operator|.
name|get
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"write lock file exist continue wait"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|lock
operator|=
operator|new
name|Lock
argument_list|(
name|file
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|createNewFileQuietly
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|lock
operator|.
name|openLock
argument_list|(
literal|true
argument_list|,
name|timeout
operator|>
literal|0
argument_list|)
expr_stmt|;
name|acquired
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
comment|// can happen if an other thread has deleted the file
comment|// close RandomAccessFile!!!
if|if
condition|(
name|lock
operator|!=
literal|null
condition|)
block|{
name|closeQuietly
argument_list|(
name|lock
operator|.
name|getRandomAccessFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"write Lock skip: {} try to create file"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|createNewFileQuietly
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FileLockException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"openLock {}:{}"
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Lock
name|current
init|=
name|lockFiles
operator|.
name|putIfAbsent
argument_list|(
name|file
argument_list|,
name|lock
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|lock
operator|=
name|current
expr_stmt|;
block|}
return|return
name|lock
return|;
block|}
specifier|private
name|void
name|closeQuietly
parameter_list|(
name|RandomAccessFile
name|randomAccessFile
parameter_list|)
block|{
if|if
condition|(
name|randomAccessFile
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|randomAccessFile
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|private
name|void
name|createNewFileQuietly
parameter_list|(
name|File
name|file
parameter_list|)
block|{
try|try
block|{
name|file
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// skip that
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|release
parameter_list|(
name|Lock
name|lock
parameter_list|)
throws|throws
name|FileLockException
block|{
if|if
condition|(
name|lock
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"skip releasing null"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|skipLocking
condition|)
block|{
return|return;
block|}
try|try
block|{
name|lockFiles
operator|.
name|remove
argument_list|(
name|lock
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClosedChannelException
name|e
parameter_list|)
block|{
comment|// skip this one
name|log
operator|.
name|debug
argument_list|(
literal|"ignore ClosedChannelException: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FileLockException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearLockFiles
parameter_list|()
block|{
name|lockFiles
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|mkdirs
parameter_list|(
name|File
name|directory
parameter_list|)
block|{
return|return
name|directory
operator|.
name|mkdirs
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSkipLocking
parameter_list|()
block|{
return|return
name|skipLocking
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setSkipLocking
parameter_list|(
name|boolean
name|skipLocking
parameter_list|)
block|{
name|this
operator|.
name|skipLocking
operator|=
name|skipLocking
expr_stmt|;
block|}
block|}
end_class

end_unit

