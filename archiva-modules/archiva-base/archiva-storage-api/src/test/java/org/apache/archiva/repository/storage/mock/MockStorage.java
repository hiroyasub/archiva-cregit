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
operator|.
name|mock
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|RepositoryStorage
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
name|archiva
operator|.
name|repository
operator|.
name|storage
operator|.
name|util
operator|.
name|VisitStatus
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
name|net
operator|.
name|URI
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
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|MockStorage
implements|implements
name|RepositoryStorage
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ADD
init|=
literal|"ADD"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REMOVE
init|=
literal|"REMOVE"
decl_stmt|;
specifier|private
name|MockAsset
name|root
decl_stmt|;
specifier|private
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|MockAsset
argument_list|>
name|assets
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|( )
decl_stmt|;
specifier|private
name|VisitStatus
name|status
init|=
operator|new
name|VisitStatus
argument_list|( )
decl_stmt|;
specifier|public
name|MockStorage
parameter_list|(
name|MockAsset
name|root
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
name|root
operator|.
name|setStorage
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MockStorage
parameter_list|()
block|{
name|this
operator|.
name|root
operator|=
operator|new
name|MockAsset
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|this
operator|.
name|root
operator|.
name|setStorage
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|VisitStatus
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|getLocation
parameter_list|( )
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateLocation
parameter_list|(
name|URI
name|newLocation
parameter_list|)
throws|throws
name|IOException
block|{
block|}
specifier|private
name|String
index|[]
name|splitPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
else|else
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
return|return
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|path
operator|.
name|length
argument_list|( )
argument_list|)
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
return|;
block|}
return|return
name|path
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
return|;
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
if|if
condition|(
name|assets
operator|.
name|containsKey
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|assets
operator|.
name|get
argument_list|(
name|path
argument_list|)
return|;
block|}
name|String
index|[]
name|pathArr
init|=
name|splitPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|StorageAsset
name|parent
init|=
name|root
decl_stmt|;
for|for
control|(
name|String
name|pathElement
range|:
name|pathArr
control|)
block|{
name|Optional
argument_list|<
name|?
extends|extends
name|StorageAsset
argument_list|>
name|next
init|=
name|parent
operator|.
name|list
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getName
argument_list|( )
operator|.
name|equals
argument_list|(
name|pathElement
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|( )
decl_stmt|;
if|if
condition|(
name|next
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|parent
operator|=
name|next
operator|.
name|get
argument_list|( )
expr_stmt|;
block|}
else|else
block|{
name|MockAsset
name|asset
init|=
operator|new
name|MockAsset
argument_list|(
operator|(
name|MockAsset
operator|)
name|parent
argument_list|,
name|pathElement
argument_list|)
decl_stmt|;
name|assets
operator|.
name|put
argument_list|(
name|asset
operator|.
name|getPath
argument_list|( )
argument_list|,
name|asset
argument_list|)
expr_stmt|;
name|parent
operator|=
name|asset
expr_stmt|;
block|}
block|}
return|return
name|parent
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|getRoot
parameter_list|( )
block|{
return|return
name|root
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
name|String
index|[]
name|pathArr
init|=
name|splitPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|StorageAsset
name|parent
init|=
name|root
decl_stmt|;
for|for
control|(
name|String
name|pathElement
range|:
name|pathArr
control|)
block|{
name|Optional
argument_list|<
name|?
extends|extends
name|StorageAsset
argument_list|>
name|next
init|=
name|parent
operator|.
name|list
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getName
argument_list|( )
operator|.
name|equals
argument_list|(
name|pathElement
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|( )
decl_stmt|;
if|if
condition|(
name|next
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|parent
operator|=
name|next
operator|.
name|get
argument_list|( )
expr_stmt|;
block|}
else|else
block|{
name|MockAsset
name|asset
init|=
operator|new
name|MockAsset
argument_list|(
operator|(
name|MockAsset
operator|)
name|parent
argument_list|,
name|pathElement
argument_list|)
decl_stmt|;
name|assets
operator|.
name|put
argument_list|(
name|asset
operator|.
name|getPath
argument_list|( )
argument_list|,
name|asset
argument_list|)
expr_stmt|;
name|parent
operator|=
name|asset
expr_stmt|;
block|}
block|}
name|status
operator|.
name|add
argument_list|(
name|ADD
argument_list|,
name|parent
argument_list|)
expr_stmt|;
return|return
name|parent
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAsset
parameter_list|(
name|StorageAsset
name|assetArg
parameter_list|)
throws|throws
name|IOException
block|{
name|MockAsset
name|asset
init|=
operator|(
name|MockAsset
operator|)
name|assetArg
decl_stmt|;
if|if
condition|(
name|asset
operator|.
name|hasParent
argument_list|()
condition|)
block|{
name|asset
operator|.
name|getParent
argument_list|( )
operator|.
name|unregisterChild
argument_list|(
name|asset
argument_list|)
expr_stmt|;
block|}
name|assets
operator|.
name|remove
argument_list|(
name|asset
operator|.
name|getPath
argument_list|( )
argument_list|)
expr_stmt|;
name|status
operator|.
name|add
argument_list|(
name|REMOVE
argument_list|,
name|asset
argument_list|)
expr_stmt|;
if|if
condition|(
name|asset
operator|.
name|isThrowException
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Mocked IOException for "
operator|+
name|asset
operator|.
name|getPath
argument_list|( )
argument_list|)
throw|;
block|}
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
return|return
literal|null
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
return|return
literal|null
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
block|}
block|}
end_class

end_unit

