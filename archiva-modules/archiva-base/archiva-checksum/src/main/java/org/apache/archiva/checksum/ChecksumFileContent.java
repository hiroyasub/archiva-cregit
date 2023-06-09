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

begin_comment
comment|/**  * Simple POJO for storing the data parsed from a checksum file.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|ChecksumFileContent
block|{
name|String
name|checksum
decl_stmt|;
name|String
name|fileReference
decl_stmt|;
name|boolean
name|formatMatch
init|=
literal|false
decl_stmt|;
specifier|public
name|ChecksumFileContent
parameter_list|()
block|{
block|}
specifier|public
name|ChecksumFileContent
parameter_list|(
name|String
name|checksum
parameter_list|,
name|String
name|fileReference
parameter_list|,
name|boolean
name|formatMatch
parameter_list|)
block|{
name|this
operator|.
name|checksum
operator|=
name|checksum
expr_stmt|;
name|this
operator|.
name|fileReference
operator|=
name|fileReference
expr_stmt|;
name|this
operator|.
name|formatMatch
operator|=
name|formatMatch
expr_stmt|;
block|}
comment|/**      * The checksum as hex string.      *      * @return      */
specifier|public
name|String
name|getChecksum
parameter_list|( )
block|{
return|return
name|checksum
return|;
block|}
specifier|public
name|void
name|setChecksum
parameter_list|(
name|String
name|checksum
parameter_list|)
block|{
name|this
operator|.
name|checksum
operator|=
name|checksum
expr_stmt|;
block|}
comment|/**      * The name of the reference file as stored in the checksum file.      * @return      */
specifier|public
name|String
name|getFileReference
parameter_list|( )
block|{
return|return
name|fileReference
return|;
block|}
specifier|public
name|void
name|setFileReference
parameter_list|(
name|String
name|fileReference
parameter_list|)
block|{
name|this
operator|.
name|fileReference
operator|=
name|fileReference
expr_stmt|;
block|}
comment|/**      * True, if the file content matches a known format      * @return      */
specifier|public
name|boolean
name|isFormatMatch
parameter_list|( )
block|{
return|return
name|formatMatch
return|;
block|}
specifier|public
name|void
name|setFormatMatch
parameter_list|(
name|boolean
name|formatMatch
parameter_list|)
block|{
name|this
operator|.
name|formatMatch
operator|=
name|formatMatch
expr_stmt|;
block|}
block|}
end_class

end_unit

