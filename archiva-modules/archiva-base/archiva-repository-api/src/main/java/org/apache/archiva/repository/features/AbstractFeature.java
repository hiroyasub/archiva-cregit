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
name|features
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
name|RepositoryEvent
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
name|RepositoryEventListener
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
name|Collection
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

begin_class
specifier|public
class|class
name|AbstractFeature
block|{
specifier|private
name|List
argument_list|<
name|RepositoryEventListener
argument_list|>
name|listener
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|AbstractFeature
parameter_list|()
block|{
block|}
name|AbstractFeature
parameter_list|(
name|RepositoryEventListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|listener
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
name|AbstractFeature
parameter_list|(
name|Collection
argument_list|<
name|RepositoryEventListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
operator|.
name|listener
operator|.
name|addAll
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addListener
parameter_list|(
name|RepositoryEventListener
name|listener
parameter_list|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|listener
operator|.
name|contains
argument_list|(
name|listener
argument_list|)
condition|)
block|{
name|this
operator|.
name|listener
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|listener
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeListener
parameter_list|(
name|RepositoryEventListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|listener
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearListeners
parameter_list|()
block|{
name|this
operator|.
name|listener
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|void
name|raiseEvent
parameter_list|(
name|RepositoryEvent
argument_list|<
name|T
argument_list|>
name|event
parameter_list|)
block|{
for|for
control|(
name|RepositoryEventListener
name|listr
range|:
name|listener
control|)
block|{
name|listr
operator|.
name|raise
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
