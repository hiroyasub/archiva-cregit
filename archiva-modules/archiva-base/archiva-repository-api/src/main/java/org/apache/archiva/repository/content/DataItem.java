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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  *  * A data item is a item that is not a real artifact because it does not have  * a version, but is normally file based.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataItem
extends|extends
name|ContentItem
block|{
comment|/**      * Returns the identifier of the data item.      * @return the identifier string      */
name|String
name|getId
parameter_list|( )
function_decl|;
comment|/**      * Returns the extension of the file. This method should always return the extension string after the last      * '.'-character.      *      * @return the file name extension      */
specifier|default
name|String
name|getExtension
parameter_list|( )
block|{
specifier|final
name|String
name|name
init|=
name|getAsset
argument_list|( )
operator|.
name|getName
argument_list|( )
decl_stmt|;
specifier|final
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
comment|/**      * Should return the mime type of the artifact.      *      * @return the mime type of the artifact.      */
name|String
name|getContentType
parameter_list|( )
function_decl|;
comment|/**      * Short cut for the file name. Should always return the same value as the artifact name.      *      * @return the name of the file      */
specifier|default
name|String
name|getFileName
parameter_list|( )
block|{
return|return
name|getAsset
argument_list|( )
operator|.
name|getName
argument_list|( )
return|;
block|}
comment|/**      * Returns the      * @return the type of the item      */
name|DataItemType
name|getDataType
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

