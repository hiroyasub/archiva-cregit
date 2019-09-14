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
name|index
operator|.
name|mock
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
name|DefaultFileLockManager
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
name|indexer
operator|.
name|ArchivaIndexingContext
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
name|Repository
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
name|FilesystemStorage
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
name|StorageAsset
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
name|index
operator|.
name|context
operator|.
name|IndexingContext
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
name|NoSuchFileException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
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

begin_comment
comment|/**  * Maven implementation of index context  */
end_comment

begin_class
specifier|public
class|class
name|MavenIndexContextMock
implements|implements
name|ArchivaIndexingContext
block|{
specifier|private
name|boolean
name|open
init|=
literal|true
decl_stmt|;
specifier|private
name|IndexingContext
name|delegate
decl_stmt|;
specifier|private
name|Repository
name|repository
decl_stmt|;
specifier|private
name|FilesystemStorage
name|indexStorage
decl_stmt|;
name|MavenIndexContextMock
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|IndexingContext
name|delegate
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|indexStorage
operator|=
operator|new
name|FilesystemStorage
argument_list|(
name|delegate
operator|.
name|getIndexDirectoryFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|,
operator|new
name|DefaultFileLockManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getId
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|getPath
parameter_list|()
block|{
return|return
name|indexStorage
operator|.
name|getAsset
argument_list|(
literal|""
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|Files
operator|.
name|list
argument_list|(
name|delegate
operator|.
name|getIndexDirectoryFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
operator|.
name|count
argument_list|()
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|commit
parameter_list|()
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|rollback
parameter_list|()
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|optimize
parameter_list|()
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|optimize
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|(
name|boolean
name|deleteFiles
parameter_list|)
throws|throws
name|IOException
block|{
name|open
operator|=
literal|false
expr_stmt|;
try|try
block|{
name|delegate
operator|.
name|close
argument_list|(
name|deleteFiles
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchFileException
name|e
parameter_list|)
block|{
comment|// Ignore missing directory
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|open
operator|=
literal|false
expr_stmt|;
try|try
block|{
name|delegate
operator|.
name|close
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchFileException
name|e
parameter_list|)
block|{
comment|// Ignore missing directory
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
name|open
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|purge
parameter_list|()
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|purge
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supports
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|IndexingContext
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBaseContext
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|UnsupportedOperationException
block|{
if|if
condition|(
name|IndexingContext
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|delegate
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The class "
operator|+
name|clazz
operator|+
literal|" is not supported by the maven indexer"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getGroups
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getAllGroups
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateTimestamp
parameter_list|(
name|boolean
name|save
parameter_list|)
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|updateTimestamp
argument_list|(
name|save
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateTimestamp
parameter_list|(
name|boolean
name|save
parameter_list|,
name|ZonedDateTime
name|time
parameter_list|)
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|updateTimestamp
argument_list|(
name|save
argument_list|,
name|Date
operator|.
name|from
argument_list|(
name|time
operator|.
name|toInstant
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

