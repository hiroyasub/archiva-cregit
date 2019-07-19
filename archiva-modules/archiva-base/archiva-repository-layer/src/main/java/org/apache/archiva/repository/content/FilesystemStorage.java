begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
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
name|archiva
operator|.
name|common
operator|.
name|filelock
operator|.
name|FileLockException
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
name|filelock
operator|.
name|FileLockTimeoutException
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
name|Lock
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|FileChannel
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
name|ReadableByteChannel
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
name|WritableByteChannel
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
name|CopyOption
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
name|nio
operator|.
name|file
operator|.
name|StandardCopyOption
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
name|StandardOpenOption
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_comment
comment|/**  * Implementation of<code>{@link RepositoryStorage}</code> where data is stored in the filesystem.  *  * All files are relative to a given base path. Path values are separated by '/', '..' is allowed to navigate  * to a parent directory, but navigation out of the base path will lead to a exception.  */
end_comment

begin_class
specifier|public
class|class
name|FilesystemStorage
implements|implements
name|RepositoryStorage
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FilesystemStorage
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Path
name|basePath
decl_stmt|;
specifier|private
specifier|final
name|FileLockManager
name|fileLockManager
decl_stmt|;
specifier|public
name|FilesystemStorage
parameter_list|(
name|Path
name|basePath
parameter_list|,
name|FileLockManager
name|fileLockManager
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|basePath
operator|=
name|basePath
operator|.
name|normalize
argument_list|()
operator|.
name|toRealPath
argument_list|()
expr_stmt|;
name|this
operator|.
name|fileLockManager
operator|=
name|fileLockManager
expr_stmt|;
block|}
specifier|private
name|Path
name|normalize
parameter_list|(
specifier|final
name|String
name|path
parameter_list|)
block|{
name|String
name|nPath
init|=
name|path
decl_stmt|;
while|while
condition|(
name|nPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|nPath
operator|=
name|nPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|Paths
operator|.
name|get
argument_list|(
name|nPath
argument_list|)
return|;
block|}
specifier|private
name|Path
name|getAssetPath
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|assetPath
init|=
name|basePath
operator|.
name|resolve
argument_list|(
name|normalize
argument_list|(
name|path
argument_list|)
argument_list|)
operator|.
name|normalize
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|assetPath
operator|.
name|startsWith
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Path navigation out of allowed scope: "
operator|+
name|path
argument_list|)
throw|;
block|}
return|return
name|assetPath
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|consumeData
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|InputStream
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|readLock
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|path
init|=
name|asset
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|readLock
condition|)
block|{
name|consumeDataLocked
argument_list|(
name|path
argument_list|,
name|consumerFunction
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|path
argument_list|)
init|)
block|{
name|consumerFunction
operator|.
name|accept
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not read the input stream from file {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Runtime exception during data consume from artifact {}. Error: {}"
argument_list|,
name|path
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|consumeDataFromChannel
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|ReadableByteChannel
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|readLock
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|path
init|=
name|asset
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|readLock
condition|)
block|{
name|consumeDataFromChannelLocked
argument_list|(
name|path
argument_list|,
name|consumerFunction
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
init|(
name|FileChannel
name|is
init|=
name|FileChannel
operator|.
name|open
argument_list|(
name|path
argument_list|,
name|StandardOpenOption
operator|.
name|READ
argument_list|)
init|)
block|{
name|consumerFunction
operator|.
name|accept
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not read the input stream from file {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Runtime exception during data consume from artifact {}. Error: {}"
argument_list|,
name|path
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeData
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|OutputStream
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|writeLock
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|path
init|=
name|asset
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|writeLock
condition|)
block|{
name|writeDataLocked
argument_list|(
name|path
argument_list|,
name|consumerFunction
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
init|(
name|OutputStream
name|is
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|path
argument_list|)
init|)
block|{
name|consumerFunction
operator|.
name|accept
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not write the output stream to file {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Runtime exception during data consume from artifact {}. Error: {}"
argument_list|,
name|path
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeDataToChannel
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|WritableByteChannel
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|writeLock
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|path
init|=
name|asset
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|writeLock
condition|)
block|{
name|writeDataToChannelLocked
argument_list|(
name|path
argument_list|,
name|consumerFunction
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
init|(
name|FileChannel
name|os
init|=
name|FileChannel
operator|.
name|open
argument_list|(
name|path
argument_list|,
name|StandardOpenOption
operator|.
name|WRITE
argument_list|,
name|StandardOpenOption
operator|.
name|TRUNCATE_EXISTING
argument_list|,
name|StandardOpenOption
operator|.
name|CREATE
argument_list|)
init|)
block|{
name|consumerFunction
operator|.
name|accept
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not write the data to file {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Runtime exception during data consume from artifact {}. Error: {}"
argument_list|,
name|path
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|consumeDataLocked
parameter_list|(
name|Path
name|file
parameter_list|,
name|Consumer
argument_list|<
name|InputStream
argument_list|>
name|consumerFunction
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Lock
name|lock
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|fileLockManager
operator|.
name|readFileLock
argument_list|(
name|file
argument_list|)
expr_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|lock
operator|.
name|getFile
argument_list|()
argument_list|)
init|)
block|{
name|consumerFunction
operator|.
name|accept
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not read the input stream from file {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
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
block|}
catch|catch
parameter_list|(
name|FileLockException
decl||
name|FileNotFoundException
decl||
name|FileLockTimeoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Locking error on file {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|consumeDataFromChannelLocked
parameter_list|(
name|Path
name|file
parameter_list|,
name|Consumer
argument_list|<
name|ReadableByteChannel
argument_list|>
name|consumerFunction
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Lock
name|lock
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|fileLockManager
operator|.
name|readFileLock
argument_list|(
name|file
argument_list|)
expr_stmt|;
try|try
init|(
name|FileChannel
name|is
init|=
name|FileChannel
operator|.
name|open
argument_list|(
name|lock
operator|.
name|getFile
argument_list|( )
argument_list|,
name|StandardOpenOption
operator|.
name|READ
argument_list|)
init|)
block|{
name|consumerFunction
operator|.
name|accept
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not read the input stream from file {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
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
block|}
catch|catch
parameter_list|(
name|FileLockException
decl||
name|FileNotFoundException
decl||
name|FileLockTimeoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Locking error on file {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|writeDataLocked
parameter_list|(
name|Path
name|file
parameter_list|,
name|Consumer
argument_list|<
name|OutputStream
argument_list|>
name|consumerFunction
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Lock
name|lock
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|fileLockManager
operator|.
name|writeFileLock
argument_list|(
name|file
argument_list|)
expr_stmt|;
try|try
init|(
name|OutputStream
name|is
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|lock
operator|.
name|getFile
argument_list|()
argument_list|)
init|)
block|{
name|consumerFunction
operator|.
name|accept
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not write the output stream to file {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
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
block|}
catch|catch
parameter_list|(
name|FileLockException
decl||
name|FileNotFoundException
decl||
name|FileLockTimeoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Locking error on file {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|writeDataToChannelLocked
parameter_list|(
name|Path
name|file
parameter_list|,
name|Consumer
argument_list|<
name|WritableByteChannel
argument_list|>
name|consumerFunction
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Lock
name|lock
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|fileLockManager
operator|.
name|writeFileLock
argument_list|(
name|file
argument_list|)
expr_stmt|;
try|try
init|(
name|FileChannel
name|is
init|=
name|FileChannel
operator|.
name|open
argument_list|(
name|lock
operator|.
name|getFile
argument_list|( )
argument_list|,
name|StandardOpenOption
operator|.
name|WRITE
argument_list|,
name|StandardOpenOption
operator|.
name|TRUNCATE_EXISTING
argument_list|,
name|StandardOpenOption
operator|.
name|CREATE
argument_list|)
init|)
block|{
name|consumerFunction
operator|.
name|accept
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not write to file {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
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
block|}
catch|catch
parameter_list|(
name|FileLockException
decl||
name|FileNotFoundException
decl||
name|FileLockTimeoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Locking error on file {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|getAsset
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|FilesystemAsset
argument_list|(
name|this
argument_list|,
name|path
argument_list|,
name|getAssetPath
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Path navigates outside of base directory "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|addAsset
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|container
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|FilesystemAsset
argument_list|(
name|this
argument_list|,
name|path
argument_list|,
name|getAssetPath
argument_list|(
name|path
argument_list|)
argument_list|,
name|basePath
argument_list|,
name|container
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Path navigates outside of base directory "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAsset
parameter_list|(
name|StorageAsset
name|asset
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|delete
argument_list|(
name|asset
operator|.
name|getFilePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|moveAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|String
name|destination
parameter_list|,
name|CopyOption
modifier|...
name|copyOptions
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|container
init|=
name|origin
operator|.
name|isContainer
argument_list|()
decl_stmt|;
name|FilesystemAsset
name|newAsset
init|=
operator|new
name|FilesystemAsset
argument_list|(
name|this
argument_list|,
name|destination
argument_list|,
name|getAssetPath
argument_list|(
name|destination
argument_list|)
argument_list|,
name|basePath
argument_list|,
name|container
argument_list|)
decl_stmt|;
name|moveAsset
argument_list|(
name|origin
argument_list|,
name|newAsset
argument_list|,
name|copyOptions
argument_list|)
expr_stmt|;
return|return
name|newAsset
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|moveAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|StorageAsset
name|destination
parameter_list|,
name|CopyOption
modifier|...
name|copyOptions
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|move
argument_list|(
name|origin
operator|.
name|getFilePath
argument_list|()
argument_list|,
name|destination
operator|.
name|getFilePath
argument_list|()
argument_list|,
name|copyOptions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|copyAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|String
name|destination
parameter_list|,
name|CopyOption
modifier|...
name|copyOptions
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|container
init|=
name|origin
operator|.
name|isContainer
argument_list|()
decl_stmt|;
name|FilesystemAsset
name|newAsset
init|=
operator|new
name|FilesystemAsset
argument_list|(
name|this
argument_list|,
name|destination
argument_list|,
name|getAssetPath
argument_list|(
name|destination
argument_list|)
argument_list|,
name|basePath
argument_list|,
name|container
argument_list|)
decl_stmt|;
name|copyAsset
argument_list|(
name|origin
argument_list|,
name|newAsset
argument_list|,
name|copyOptions
argument_list|)
expr_stmt|;
return|return
name|newAsset
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|copyAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|StorageAsset
name|destination
parameter_list|,
name|CopyOption
modifier|...
name|copyOptions
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|destinationPath
init|=
name|destination
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
name|boolean
name|overwrite
init|=
literal|false
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
name|copyOptions
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|copyOptions
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|StandardCopyOption
operator|.
name|REPLACE_EXISTING
argument_list|)
condition|)
block|{
name|overwrite
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|destinationPath
argument_list|)
operator|&&
operator|!
name|overwrite
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Destination file exists already "
operator|+
name|destinationPath
argument_list|)
throw|;
block|}
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|origin
operator|.
name|getFilePath
argument_list|()
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|origin
operator|.
name|getFilePath
argument_list|( )
operator|.
name|toFile
argument_list|()
argument_list|,
name|destinationPath
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|Files
operator|.
name|isRegularFile
argument_list|(
name|origin
operator|.
name|getFilePath
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|destinationPath
argument_list|)
condition|)
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|destinationPath
argument_list|)
expr_stmt|;
block|}
name|Files
operator|.
name|copy
argument_list|(
name|origin
operator|.
name|getFilePath
argument_list|( )
argument_list|,
name|destinationPath
argument_list|,
name|copyOptions
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|FileLockManager
name|getFileLockManager
parameter_list|()
block|{
return|return
name|fileLockManager
return|;
block|}
block|}
end_class

end_unit

