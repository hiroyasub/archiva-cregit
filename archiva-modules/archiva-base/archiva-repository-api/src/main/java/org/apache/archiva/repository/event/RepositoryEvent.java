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
name|event
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
name|event
operator|.
name|Event
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
name|event
operator|.
name|EventType
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

begin_comment
comment|/**  * A repository event is specific to a repository and holds a reference to the repository that  * is related to this event.  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryEvent
extends|extends
name|Event
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4676673476606414834L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EventType
argument_list|<
name|RepositoryEvent
argument_list|>
name|ANY
init|=
operator|new
name|EventType
argument_list|<>
argument_list|(
name|Event
operator|.
name|ANY
argument_list|,
literal|"REPOSITORY"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Repository
name|repository
decl_stmt|;
specifier|public
name|RepositoryEvent
parameter_list|(
name|EventType
argument_list|<
name|?
extends|extends
name|RepositoryEvent
argument_list|>
name|type
parameter_list|,
name|Object
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
annotation|@
name|Override
specifier|public
name|EventType
argument_list|<
name|?
extends|extends
name|RepositoryEvent
argument_list|>
name|getType
parameter_list|()
block|{
return|return
operator|(
name|EventType
argument_list|<
name|?
extends|extends
name|RepositoryEvent
argument_list|>
operator|)
name|super
operator|.
name|getType
argument_list|()
return|;
block|}
block|}
end_class

end_unit
