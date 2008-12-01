begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactMetadata
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|Date
name|updated
decl_stmt|;
specifier|private
name|long
name|size
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|Date
name|getUpdated
parameter_list|()
block|{
return|return
name|updated
return|;
block|}
specifier|public
name|void
name|setUpdated
parameter_list|(
name|Date
name|updated
parameter_list|)
block|{
name|this
operator|.
name|updated
operator|=
name|updated
expr_stmt|;
block|}
specifier|public
name|void
name|setUpdated
parameter_list|(
name|long
name|updated
parameter_list|)
block|{
name|this
operator|.
name|updated
operator|=
operator|new
name|Date
argument_list|(
name|updated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|long
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
specifier|public
name|void
name|setSize
parameter_list|(
name|long
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
block|}
end_class

end_unit

