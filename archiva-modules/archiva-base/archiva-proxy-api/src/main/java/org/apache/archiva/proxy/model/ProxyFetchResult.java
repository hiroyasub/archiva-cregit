begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|model
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
name|repository
operator|.
name|storage
operator|.
name|StorageAsset
import|;
end_import

begin_comment
comment|/**  * A result from a proxy fetch operation.  *  * @since 2.1.2  */
end_comment

begin_class
specifier|public
class|class
name|ProxyFetchResult
block|{
comment|//The file returned
specifier|private
name|StorageAsset
name|file
decl_stmt|;
comment|//Was the local file modified by the fetch?
specifier|private
name|boolean
name|modified
decl_stmt|;
specifier|public
name|ProxyFetchResult
parameter_list|(
name|StorageAsset
name|file
parameter_list|,
name|boolean
name|modified
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
name|this
operator|.
name|modified
operator|=
name|modified
expr_stmt|;
block|}
specifier|public
name|StorageAsset
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
specifier|public
name|boolean
name|isModified
parameter_list|()
block|{
return|return
name|modified
return|;
block|}
block|}
end_class

end_unit

