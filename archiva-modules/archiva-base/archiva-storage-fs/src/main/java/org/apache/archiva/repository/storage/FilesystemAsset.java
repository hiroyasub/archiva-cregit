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
name|storage
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
name|StringUtils
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
name|*
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
name|attribute
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * Implementation of an asset that is stored on the filesystem.  *<p>  * The implementation does not check the given paths. Caller should normalize the asset path  * and check, if the base path is a parent of the resulting path.  *<p>  * The file must not exist for all operations.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|FilesystemAsset
implements|implements
name|StorageAsset
implements|,
name|Comparable
block|{
specifier|private
specifier|final
specifier|static
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FilesystemAsset
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
name|Path
name|assetPath
decl_stmt|;
specifier|private
specifier|final
name|String
name|relativePath
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_POSIX_FILE_PERMS
init|=
literal|"rw-rw----"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_POSIX_DIR_PERMS
init|=
literal|"rwxrwx---"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|DEFAULT_POSIX_FILE_PERMISSIONS
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|DEFAULT_POSIX_DIR_PERMISSIONS
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|AclEntryPermission
index|[]
name|DEFAULT_ACL_FILE_PERMISSIONS
init|=
operator|new
name|AclEntryPermission
index|[]
block|{
name|AclEntryPermission
operator|.
name|DELETE
block|,
name|AclEntryPermission
operator|.
name|READ_ACL
block|,
name|AclEntryPermission
operator|.
name|READ_ATTRIBUTES
block|,
name|AclEntryPermission
operator|.
name|READ_DATA
block|,
name|AclEntryPermission
operator|.
name|WRITE_ACL
block|,
name|AclEntryPermission
operator|.
name|WRITE_ATTRIBUTES
block|,
name|AclEntryPermission
operator|.
name|WRITE_DATA
block|,
name|AclEntryPermission
operator|.
name|APPEND_DATA
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|AclEntryPermission
index|[]
name|DEFAULT_ACL_DIR_PERMISSIONS
init|=
operator|new
name|AclEntryPermission
index|[]
block|{
name|AclEntryPermission
operator|.
name|ADD_FILE
block|,
name|AclEntryPermission
operator|.
name|ADD_SUBDIRECTORY
block|,
name|AclEntryPermission
operator|.
name|DELETE_CHILD
block|,
name|AclEntryPermission
operator|.
name|DELETE
block|,
name|AclEntryPermission
operator|.
name|READ_ACL
block|,
name|AclEntryPermission
operator|.
name|READ_ATTRIBUTES
block|,
name|AclEntryPermission
operator|.
name|READ_DATA
block|,
name|AclEntryPermission
operator|.
name|WRITE_ACL
block|,
name|AclEntryPermission
operator|.
name|WRITE_ATTRIBUTES
block|,
name|AclEntryPermission
operator|.
name|WRITE_DATA
block|,
name|AclEntryPermission
operator|.
name|APPEND_DATA
block|}
decl_stmt|;
static|static
block|{
name|DEFAULT_POSIX_FILE_PERMISSIONS
operator|=
name|PosixFilePermissions
operator|.
name|fromString
argument_list|(
name|DEFAULT_POSIX_FILE_PERMS
argument_list|)
expr_stmt|;
name|DEFAULT_POSIX_DIR_PERMISSIONS
operator|=
name|PosixFilePermissions
operator|.
name|fromString
argument_list|(
name|DEFAULT_POSIX_DIR_PERMS
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|defaultPosixFilePermissions
init|=
name|DEFAULT_POSIX_FILE_PERMISSIONS
decl_stmt|;
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|defaultPosixDirectoryPermissions
init|=
name|DEFAULT_POSIX_DIR_PERMISSIONS
decl_stmt|;
name|List
argument_list|<
name|AclEntry
argument_list|>
name|defaultFileAcls
decl_stmt|;
name|List
argument_list|<
name|AclEntry
argument_list|>
name|defaultDirectoryAcls
decl_stmt|;
name|boolean
name|supportsAcl
init|=
literal|false
decl_stmt|;
name|boolean
name|supportsPosix
init|=
literal|false
decl_stmt|;
specifier|final
name|boolean
name|setPermissionsForNew
decl_stmt|;
specifier|final
name|RepositoryStorage
name|storage
decl_stmt|;
name|boolean
name|directoryHint
init|=
literal|false
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|OpenOption
index|[]
name|REPLACE_OPTIONS
init|=
operator|new
name|OpenOption
index|[]
block|{
name|StandardOpenOption
operator|.
name|TRUNCATE_EXISTING
block|,
name|StandardOpenOption
operator|.
name|CREATE
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|OpenOption
index|[]
name|APPEND_OPTIONS
init|=
operator|new
name|OpenOption
index|[]
block|{
name|StandardOpenOption
operator|.
name|APPEND
block|}
decl_stmt|;
name|FilesystemAsset
parameter_list|(
name|RepositoryStorage
name|storage
parameter_list|,
name|String
name|path
parameter_list|,
name|Path
name|assetPath
parameter_list|,
name|Path
name|basePath
parameter_list|)
block|{
name|this
operator|.
name|assetPath
operator|=
name|assetPath
expr_stmt|;
name|this
operator|.
name|relativePath
operator|=
name|normalizePath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPermissionsForNew
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates an asset for the given path. The given paths are not checked.      * The base path should be an absolute path.      *      * @param path The logical path for the asset relative to the repository.      * @param assetPath The asset path.      */
specifier|public
name|FilesystemAsset
parameter_list|(
name|RepositoryStorage
name|storage
parameter_list|,
name|String
name|path
parameter_list|,
name|Path
name|assetPath
parameter_list|)
block|{
name|this
operator|.
name|assetPath
operator|=
name|assetPath
expr_stmt|;
name|this
operator|.
name|relativePath
operator|=
name|normalizePath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPermissionsForNew
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|basePath
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates an asset for the given path. The given paths are not checked.      * The base path should be an absolute path.      *      * @param path The logical path for the asset relative to the repository      * @param assetPath The asset path.      * @param directory This is only relevant, if the represented file or directory does not exist yet and      *                  is a hint.      */
specifier|public
name|FilesystemAsset
parameter_list|(
name|RepositoryStorage
name|storage
parameter_list|,
name|String
name|path
parameter_list|,
name|Path
name|assetPath
parameter_list|,
name|Path
name|basePath
parameter_list|,
name|boolean
name|directory
parameter_list|)
block|{
name|this
operator|.
name|assetPath
operator|=
name|assetPath
expr_stmt|;
name|this
operator|.
name|relativePath
operator|=
name|normalizePath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|this
operator|.
name|directoryHint
operator|=
name|directory
expr_stmt|;
name|this
operator|.
name|setPermissionsForNew
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates an asset for the given path. The given paths are not checked.      * The base path should be an absolute path.      *      * @param path The logical path for the asset relative to the repository      * @param assetPath The asset path.      * @param directory This is only relevant, if the represented file or directory does not exist yet and      *                  is a hint.      */
specifier|public
name|FilesystemAsset
parameter_list|(
name|RepositoryStorage
name|storage
parameter_list|,
name|String
name|path
parameter_list|,
name|Path
name|assetPath
parameter_list|,
name|Path
name|basePath
parameter_list|,
name|boolean
name|directory
parameter_list|,
name|boolean
name|setPermissionsForNew
parameter_list|)
block|{
name|this
operator|.
name|assetPath
operator|=
name|assetPath
expr_stmt|;
name|this
operator|.
name|relativePath
operator|=
name|normalizePath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|this
operator|.
name|directoryHint
operator|=
name|directory
expr_stmt|;
name|this
operator|.
name|setPermissionsForNew
operator|=
name|setPermissionsForNew
expr_stmt|;
name|this
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|normalizePath
parameter_list|(
specifier|final
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
return|return
literal|"/"
operator|+
name|path
return|;
block|}
else|else
block|{
name|String
name|tmpPath
init|=
name|path
decl_stmt|;
while|while
condition|(
name|tmpPath
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|tmpPath
operator|=
name|tmpPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|tmpPath
return|;
block|}
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|setPermissionsForNew
condition|)
block|{
try|try
block|{
name|supportsAcl
operator|=
name|Files
operator|.
name|getFileStore
argument_list|(
name|assetPath
operator|.
name|getRoot
argument_list|()
argument_list|)
operator|.
name|supportsFileAttributeView
argument_list|(
name|AclFileAttributeView
operator|.
name|class
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
literal|"Could not check filesystem capabilities {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|supportsPosix
operator|=
name|Files
operator|.
name|getFileStore
argument_list|(
name|assetPath
operator|.
name|getRoot
argument_list|()
argument_list|)
operator|.
name|supportsFileAttributeView
argument_list|(
name|PosixFileAttributeView
operator|.
name|class
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
literal|"Could not check filesystem capabilities {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|supportsAcl
condition|)
block|{
name|AclFileAttributeView
name|aclView
init|=
name|Files
operator|.
name|getFileAttributeView
argument_list|(
name|assetPath
operator|.
name|getParent
argument_list|()
argument_list|,
name|AclFileAttributeView
operator|.
name|class
argument_list|)
decl_stmt|;
name|UserPrincipal
name|owner
init|=
literal|null
decl_stmt|;
try|try
block|{
name|owner
operator|=
name|aclView
operator|.
name|getOwner
argument_list|()
expr_stmt|;
name|setDefaultFileAcls
argument_list|(
name|processPermissions
argument_list|(
name|owner
argument_list|,
name|DEFAULT_ACL_FILE_PERMISSIONS
argument_list|)
argument_list|)
expr_stmt|;
name|setDefaultDirectoryAcls
argument_list|(
name|processPermissions
argument_list|(
name|owner
argument_list|,
name|DEFAULT_ACL_DIR_PERMISSIONS
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|supportsAcl
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|List
argument_list|<
name|AclEntry
argument_list|>
name|processPermissions
parameter_list|(
name|UserPrincipal
name|owner
parameter_list|,
name|AclEntryPermission
index|[]
name|defaultAclFilePermissions
parameter_list|)
block|{
name|AclEntry
operator|.
name|Builder
name|aclBuilder
init|=
name|AclEntry
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
name|aclBuilder
operator|.
name|setPermissions
argument_list|(
name|defaultAclFilePermissions
argument_list|)
expr_stmt|;
name|aclBuilder
operator|.
name|setType
argument_list|(
name|AclEntryType
operator|.
name|ALLOW
argument_list|)
expr_stmt|;
name|aclBuilder
operator|.
name|setPrincipal
argument_list|(
name|owner
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|AclEntry
argument_list|>
name|aclList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|aclList
operator|.
name|add
argument_list|(
name|aclBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|aclList
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryStorage
name|getStorage
parameter_list|( )
block|{
return|return
name|storage
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|relativePath
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|assetPath
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Instant
name|getModificationTime
parameter_list|()
block|{
try|try
block|{
return|return
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|assetPath
argument_list|)
operator|.
name|toInstant
argument_list|()
return|;
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
literal|"Could not read modification time of {}"
argument_list|,
name|assetPath
argument_list|)
expr_stmt|;
return|return
name|Instant
operator|.
name|now
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns true, if the path of this asset points to a directory      *      * @return      */
annotation|@
name|Override
specifier|public
name|boolean
name|isContainer
parameter_list|()
block|{
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|assetPath
argument_list|)
condition|)
block|{
return|return
name|Files
operator|.
name|isDirectory
argument_list|(
name|assetPath
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|directoryHint
return|;
block|}
block|}
comment|/**      * Returns the list of directory entries, if this asset represents a directory.      * Otherwise a empty list will be returned.      *      * @return The list of entries in the directory, if it exists.      */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|StorageAsset
argument_list|>
name|list
parameter_list|()
block|{
try|try
block|{
return|return
name|Files
operator|.
name|list
argument_list|(
name|assetPath
argument_list|)
operator|.
name|map
argument_list|(
name|p
lambda|->
operator|new
name|FilesystemAsset
argument_list|(
name|storage
argument_list|,
name|relativePath
operator|+
literal|"/"
operator|+
name|p
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|assetPath
operator|.
name|resolve
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
block|}
comment|/**      * Returns the size of the represented file. If it cannot be determined, -1 is returned.      *      * @return      */
annotation|@
name|Override
specifier|public
name|long
name|getSize
parameter_list|()
block|{
try|try
block|{
return|return
name|Files
operator|.
name|size
argument_list|(
name|assetPath
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
comment|/**      * Returns a input stream to the underlying file, if it exists. The caller has to make sure, that      * the stream is closed after it was used.      *      * @return      * @throws IOException      */
annotation|@
name|Override
specifier|public
name|InputStream
name|getReadStream
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|isContainer
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Can not create input stream for container"
argument_list|)
throw|;
block|}
return|return
name|Files
operator|.
name|newInputStream
argument_list|(
name|assetPath
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ReadableByteChannel
name|getReadChannel
parameter_list|( )
throws|throws
name|IOException
block|{
return|return
name|FileChannel
operator|.
name|open
argument_list|(
name|assetPath
argument_list|,
name|StandardOpenOption
operator|.
name|READ
argument_list|)
return|;
block|}
specifier|private
name|OpenOption
index|[]
name|getOpenOptions
parameter_list|(
name|boolean
name|replace
parameter_list|)
block|{
return|return
name|replace
condition|?
name|REPLACE_OPTIONS
else|:
name|APPEND_OPTIONS
return|;
block|}
annotation|@
name|Override
specifier|public
name|OutputStream
name|getWriteStream
parameter_list|(
name|boolean
name|replace
parameter_list|)
throws|throws
name|IOException
block|{
name|OpenOption
index|[]
name|options
init|=
name|getOpenOptions
argument_list|(
name|replace
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|assetPath
argument_list|)
condition|)
block|{
name|create
argument_list|()
expr_stmt|;
block|}
return|return
name|Files
operator|.
name|newOutputStream
argument_list|(
name|assetPath
argument_list|,
name|options
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WritableByteChannel
name|getWriteChannel
parameter_list|(
name|boolean
name|replace
parameter_list|)
throws|throws
name|IOException
block|{
name|OpenOption
index|[]
name|options
init|=
name|getOpenOptions
argument_list|(
name|replace
argument_list|)
decl_stmt|;
return|return
name|FileChannel
operator|.
name|open
argument_list|(
name|assetPath
argument_list|,
name|options
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|replaceDataFromFile
parameter_list|(
name|Path
name|newData
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|boolean
name|createNew
init|=
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|assetPath
argument_list|)
decl_stmt|;
name|Path
name|backup
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|createNew
condition|)
block|{
name|backup
operator|=
name|findBackupFile
argument_list|(
name|assetPath
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
operator|!
name|createNew
condition|)
block|{
name|Files
operator|.
name|move
argument_list|(
name|assetPath
argument_list|,
name|backup
argument_list|)
expr_stmt|;
block|}
name|Files
operator|.
name|move
argument_list|(
name|newData
argument_list|,
name|assetPath
argument_list|,
name|StandardCopyOption
operator|.
name|REPLACE_EXISTING
argument_list|)
expr_stmt|;
name|applyDefaultPermissions
argument_list|(
name|assetPath
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
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
literal|"Could not overwrite file {}"
argument_list|,
name|assetPath
argument_list|)
expr_stmt|;
comment|// Revert if possible
if|if
condition|(
name|backup
operator|!=
literal|null
operator|&&
name|Files
operator|.
name|exists
argument_list|(
name|backup
argument_list|)
condition|)
block|{
name|Files
operator|.
name|move
argument_list|(
name|backup
argument_list|,
name|assetPath
argument_list|,
name|StandardCopyOption
operator|.
name|REPLACE_EXISTING
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|backup
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|backup
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
literal|"Could not delete backup file {}"
argument_list|,
name|backup
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|applyDefaultPermissions
parameter_list|(
name|Path
name|filePath
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|supportsPosix
condition|)
block|{
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|perms
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|filePath
argument_list|)
condition|)
block|{
name|perms
operator|=
name|defaultPosixFilePermissions
expr_stmt|;
block|}
else|else
block|{
name|perms
operator|=
name|defaultPosixDirectoryPermissions
expr_stmt|;
block|}
name|Files
operator|.
name|setPosixFilePermissions
argument_list|(
name|filePath
argument_list|,
name|perms
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|supportsAcl
condition|)
block|{
name|List
argument_list|<
name|AclEntry
argument_list|>
name|perms
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|filePath
argument_list|)
condition|)
block|{
name|perms
operator|=
name|getDefaultDirectoryAcls
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|perms
operator|=
name|getDefaultFileAcls
argument_list|()
expr_stmt|;
block|}
name|AclFileAttributeView
name|aclAttr
init|=
name|Files
operator|.
name|getFileAttributeView
argument_list|(
name|filePath
argument_list|,
name|AclFileAttributeView
operator|.
name|class
argument_list|)
decl_stmt|;
name|aclAttr
operator|.
name|setAcl
argument_list|(
name|perms
argument_list|)
expr_stmt|;
block|}
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
literal|"Could not set permissions for {}: {}"
argument_list|,
name|filePath
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Path
name|findBackupFile
parameter_list|(
name|Path
name|file
parameter_list|)
block|{
name|String
name|ext
init|=
literal|".bak"
decl_stmt|;
name|Path
name|backupPath
init|=
name|file
operator|.
name|getParent
argument_list|()
operator|.
name|resolve
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
name|ext
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|backupPath
argument_list|)
condition|)
block|{
name|backupPath
operator|=
name|file
operator|.
name|getParent
argument_list|()
operator|.
name|resolve
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
name|ext
operator|+
name|idx
operator|++
argument_list|)
expr_stmt|;
block|}
return|return
name|backupPath
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
return|return
name|Files
operator|.
name|exists
argument_list|(
name|assetPath
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Path
name|getFilePath
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
name|assetPath
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isFileBased
parameter_list|( )
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasParent
parameter_list|( )
block|{
if|if
condition|(
name|basePath
operator|!=
literal|null
operator|&&
name|assetPath
operator|.
name|equals
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|assetPath
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|getParent
parameter_list|( )
block|{
name|Path
name|parentPath
decl_stmt|;
if|if
condition|(
name|basePath
operator|!=
literal|null
operator|&&
name|assetPath
operator|.
name|equals
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|parentPath
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|parentPath
operator|=
name|assetPath
operator|.
name|getParent
argument_list|( )
expr_stmt|;
block|}
name|String
name|relativeParent
init|=
name|StringUtils
operator|.
name|substringBeforeLast
argument_list|(
name|relativePath
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentPath
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|FilesystemAsset
argument_list|(
name|storage
argument_list|,
name|relativeParent
argument_list|,
name|parentPath
argument_list|,
name|basePath
argument_list|,
literal|true
argument_list|,
name|setPermissionsForNew
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|resolve
parameter_list|(
name|String
name|toPath
parameter_list|)
block|{
return|return
name|storage
operator|.
name|getAsset
argument_list|(
name|this
operator|.
name|getPath
argument_list|()
operator|+
literal|"/"
operator|+
name|toPath
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDefaultFileAcls
parameter_list|(
name|List
argument_list|<
name|AclEntry
argument_list|>
name|acl
parameter_list|)
block|{
name|defaultFileAcls
operator|=
name|acl
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|AclEntry
argument_list|>
name|getDefaultFileAcls
parameter_list|()
block|{
return|return
name|defaultFileAcls
return|;
block|}
specifier|public
name|void
name|setDefaultPosixFilePermissions
parameter_list|(
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|perms
parameter_list|)
block|{
name|defaultPosixFilePermissions
operator|=
name|perms
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|getDefaultPosixFilePermissions
parameter_list|()
block|{
return|return
name|defaultPosixFilePermissions
return|;
block|}
specifier|public
name|void
name|setDefaultDirectoryAcls
parameter_list|(
name|List
argument_list|<
name|AclEntry
argument_list|>
name|acl
parameter_list|)
block|{
name|defaultDirectoryAcls
operator|=
name|acl
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|AclEntry
argument_list|>
name|getDefaultDirectoryAcls
parameter_list|()
block|{
return|return
name|defaultDirectoryAcls
return|;
block|}
specifier|public
name|void
name|setDefaultPosixDirectoryPermissions
parameter_list|(
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|perms
parameter_list|)
block|{
name|defaultPosixDirectoryPermissions
operator|=
name|perms
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|getDefaultPosixDirectoryPermissions
parameter_list|()
block|{
return|return
name|defaultPosixDirectoryPermissions
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|create
parameter_list|()
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
name|assetPath
argument_list|)
condition|)
block|{
if|if
condition|(
name|directoryHint
condition|)
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|assetPath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|assetPath
operator|.
name|getParent
argument_list|()
argument_list|)
condition|)
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|assetPath
operator|.
name|getParent
argument_list|( )
argument_list|)
expr_stmt|;
block|}
name|Files
operator|.
name|createFile
argument_list|(
name|assetPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|setPermissionsForNew
condition|)
block|{
name|applyDefaultPermissions
argument_list|(
name|assetPath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|relativePath
operator|+
literal|":"
operator|+
name|assetPath
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|FilesystemAsset
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|getPath
argument_list|()
operator|.
name|compareTo
argument_list|(
operator|(
operator|(
name|FilesystemAsset
operator|)
name|o
operator|)
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit
