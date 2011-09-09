begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
operator|.
name|admin
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
name|Serializable
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryScanning
implements|implements
name|Serializable
block|{
comment|/**      * Field fileTypes.      */
specifier|private
name|List
argument_list|<
name|FileType
argument_list|>
name|fileTypes
decl_stmt|;
comment|/**      * Field knownContentConsumers.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
decl_stmt|;
comment|/**      * Field invalidContentConsumers.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|invalidContentConsumers
decl_stmt|;
specifier|public
name|RepositoryScanning
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|RepositoryScanning
parameter_list|(
name|List
argument_list|<
name|FileType
argument_list|>
name|fileTypes
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|invalidContentConsumers
parameter_list|)
block|{
name|this
operator|.
name|fileTypes
operator|=
name|fileTypes
expr_stmt|;
name|this
operator|.
name|knownContentConsumers
operator|=
name|knownContentConsumers
expr_stmt|;
name|this
operator|.
name|invalidContentConsumers
operator|=
name|invalidContentConsumers
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|FileType
argument_list|>
name|getFileTypes
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|fileTypes
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|fileTypes
operator|=
operator|new
name|ArrayList
argument_list|<
name|FileType
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|fileTypes
return|;
block|}
specifier|public
name|void
name|setFileTypes
parameter_list|(
name|List
argument_list|<
name|FileType
argument_list|>
name|fileTypes
parameter_list|)
block|{
name|this
operator|.
name|fileTypes
operator|=
name|fileTypes
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getKnownContentConsumers
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|knownContentConsumers
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|knownContentConsumers
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|knownContentConsumers
return|;
block|}
specifier|public
name|void
name|setKnownContentConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
parameter_list|)
block|{
name|this
operator|.
name|knownContentConsumers
operator|=
name|knownContentConsumers
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getInvalidContentConsumers
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|invalidContentConsumers
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|invalidContentConsumers
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|invalidContentConsumers
return|;
block|}
specifier|public
name|void
name|setInvalidContentConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|invalidContentConsumers
parameter_list|)
block|{
name|this
operator|.
name|invalidContentConsumers
operator|=
name|invalidContentConsumers
expr_stmt|;
block|}
block|}
end_class

end_unit

