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
comment|/**  * Repository value events are used for providing information about repository attribute changes.  * The value event gives information of the attribute value before and after the change.  *  * @param<V> The type of the changed attribute  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryValueEvent
parameter_list|<
name|V
parameter_list|>
extends|extends
name|RepositoryEvent
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4176597620699304794L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EventType
argument_list|<
name|RepositoryValueEvent
argument_list|<
name|?
argument_list|>
argument_list|>
name|ANY
init|=
operator|new
name|EventType
argument_list|(
name|RepositoryEvent
operator|.
name|ANY
argument_list|,
literal|"REPOSITORY.VALUE"
argument_list|)
decl_stmt|;
specifier|final
name|V
name|value
decl_stmt|;
specifier|final
name|V
name|oldValue
decl_stmt|;
specifier|final
name|String
name|attributeName
decl_stmt|;
specifier|public
name|RepositoryValueEvent
parameter_list|(
name|EventType
argument_list|<
name|?
extends|extends
name|RepositoryValueEvent
argument_list|<
name|V
argument_list|>
argument_list|>
name|type
parameter_list|,
name|Object
name|origin
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|V
name|oldValue
parameter_list|,
name|V
name|value
parameter_list|,
name|String
name|attributeName
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|,
name|origin
argument_list|,
name|repo
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|oldValue
operator|=
name|oldValue
expr_stmt|;
name|this
operator|.
name|attributeName
operator|=
name|attributeName
expr_stmt|;
block|}
specifier|public
name|V
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|V
name|getOldValue
parameter_list|()
block|{
return|return
name|oldValue
return|;
block|}
specifier|public
name|String
name|getAttributeName
parameter_list|()
block|{
return|return
name|attributeName
return|;
block|}
block|}
end_class

end_unit

