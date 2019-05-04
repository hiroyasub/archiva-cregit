begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|checksum
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|MappedByteBuffer
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
name|StandardOpenOption
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_comment
comment|/**  * Checksum - simple checksum hashing routines.  */
end_comment

begin_class
specifier|public
class|class
name|Checksum
block|{
specifier|private
name|byte
index|[]
name|result
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
specifier|private
specifier|final
name|MessageDigest
name|md
decl_stmt|;
specifier|private
name|ChecksumAlgorithm
name|checksumAlgorithm
decl_stmt|;
specifier|public
name|Checksum
parameter_list|(
name|ChecksumAlgorithm
name|checksumAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|checksumAlgorithm
operator|=
name|checksumAlgorithm
expr_stmt|;
try|try
block|{
name|md
operator|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|checksumAlgorithm
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
comment|// Not really possible, but here none-the-less
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to initialize MessageDigest algorithm "
operator|+
name|checksumAlgorithm
operator|.
name|getAlgorithm
argument_list|()
operator|+
literal|" : "
operator|+
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
specifier|public
name|String
name|getChecksum
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|result
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|finish
argument_list|()
expr_stmt|;
block|}
return|return
name|Hex
operator|.
name|encode
argument_list|(
name|this
operator|.
name|result
argument_list|)
return|;
block|}
specifier|public
name|byte
index|[]
name|getChecksumBytes
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|result
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|finish
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|result
return|;
block|}
specifier|public
name|ChecksumAlgorithm
name|getAlgorithm
parameter_list|()
block|{
return|return
name|this
operator|.
name|checksumAlgorithm
return|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|md
operator|.
name|reset
argument_list|()
expr_stmt|;
name|this
operator|.
name|result
operator|=
operator|new
name|byte
index|[
literal|0
index|]
expr_stmt|;
block|}
specifier|public
name|Checksum
name|update
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|result
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
name|reset
argument_list|()
expr_stmt|;
block|}
name|md
operator|.
name|update
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Checksum
name|update
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|result
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
name|reset
argument_list|()
expr_stmt|;
block|}
name|md
operator|.
name|update
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Checksum
name|finish
parameter_list|()
block|{
name|this
operator|.
name|result
operator|=
name|md
operator|.
name|digest
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|compare
parameter_list|(
name|byte
index|[]
name|cmp
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|result
operator|==
literal|null
operator|||
name|this
operator|.
name|result
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|finish
argument_list|()
expr_stmt|;
block|}
return|return
name|md
operator|.
name|isEqual
argument_list|(
name|this
operator|.
name|result
argument_list|,
name|cmp
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|compare
parameter_list|(
name|String
name|hexString
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|result
operator|==
literal|null
operator|||
name|this
operator|.
name|result
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|finish
argument_list|()
expr_stmt|;
block|}
return|return
name|md
operator|.
name|isEqual
argument_list|(
name|this
operator|.
name|result
argument_list|,
name|Hex
operator|.
name|decode
argument_list|(
name|hexString
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

