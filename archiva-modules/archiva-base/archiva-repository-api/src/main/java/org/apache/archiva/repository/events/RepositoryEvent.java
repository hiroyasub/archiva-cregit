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
name|events
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
name|Repository
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryEvent
parameter_list|<
name|O
parameter_list|>
extends|extends
name|Event
argument_list|<
name|O
argument_list|>
block|{
specifier|private
specifier|final
name|Repository
name|repository
decl_stmt|;
specifier|public
name|RepositoryEvent
parameter_list|(
name|EventType
name|type
parameter_list|,
name|O
name|origin
parameter_list|,
name|Repository
name|repository
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|,
name|origin
argument_list|)
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
block|}
end_class

end_unit

