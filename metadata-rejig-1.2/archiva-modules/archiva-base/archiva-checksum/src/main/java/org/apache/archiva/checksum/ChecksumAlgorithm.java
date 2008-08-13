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
name|FilenameUtils
import|;
end_import

begin_comment
comment|/**  * Enumeration of available ChecksumAlgorithm techniques.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_enum
specifier|public
enum|enum
name|ChecksumAlgorithm
block|{
name|SHA1
argument_list|(
literal|"SHA-1"
argument_list|,
literal|"sha1"
argument_list|,
literal|"SHA1"
argument_list|)
block|,
name|MD5
argument_list|(
literal|"MD5"
argument_list|,
literal|"md5"
argument_list|,
literal|"MD5"
argument_list|)
block|;
specifier|public
specifier|static
name|ChecksumAlgorithm
name|getByExtension
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|ext
init|=
name|FilenameUtils
operator|.
name|getExtension
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|ChecksumAlgorithm
operator|.
name|SHA1
operator|.
name|getExt
argument_list|()
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
condition|)
block|{
return|return
name|ChecksumAlgorithm
operator|.
name|SHA1
return|;
block|}
if|else if
condition|(
name|ChecksumAlgorithm
operator|.
name|MD5
operator|.
name|getExt
argument_list|()
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
condition|)
block|{
return|return
name|ChecksumAlgorithm
operator|.
name|MD5
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Filename "
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|" has no associated extension."
argument_list|)
throw|;
block|}
comment|/**      * The MessageDigest algorithm for this hash.      */
specifier|private
name|String
name|algorithm
decl_stmt|;
comment|/**      * The file extension for this ChecksumAlgorithm.      */
specifier|private
name|String
name|ext
decl_stmt|;
comment|/**      * The checksum type, the key that you see in checksum files.      */
specifier|private
name|String
name|type
decl_stmt|;
comment|/**      * Construct a ChecksumAlgorithm      *       * @param algorithm the MessageDigest algorithm      * @param ext the file extension.      * @param type the checksum type.      */
specifier|private
name|ChecksumAlgorithm
parameter_list|(
name|String
name|algorithm
parameter_list|,
name|String
name|ext
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
name|this
operator|.
name|ext
operator|=
name|ext
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algorithm
return|;
block|}
specifier|public
name|String
name|getExt
parameter_list|()
block|{
return|return
name|ext
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_enum

end_unit

