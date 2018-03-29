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
name|Repository
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
name|RepositoryEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_class
specifier|public
class|class
name|IndexCreationEvent
extends|extends
name|RepositoryEvent
argument_list|<
name|URI
argument_list|>
block|{
specifier|public
enum|enum
name|Index
implements|implements
name|EventType
block|{
name|INDEX_URI_CHANGE
block|,
name|PACKED_INDEX_URI_CHANGE
block|}
name|IndexCreationEvent
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|URI
name|oldValue
parameter_list|,
name|URI
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|Index
operator|.
name|INDEX_URI_CHANGE
argument_list|,
name|repo
argument_list|,
name|oldValue
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|IndexCreationEvent
parameter_list|(
name|Index
name|type
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|URI
name|oldValue
parameter_list|,
name|URI
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|,
name|repo
argument_list|,
name|oldValue
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
specifier|final
name|IndexCreationEvent
name|indexUriChange
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|URI
name|oldValue
parameter_list|,
name|URI
name|newValue
parameter_list|)
block|{
return|return
operator|new
name|IndexCreationEvent
argument_list|(
name|Index
operator|.
name|INDEX_URI_CHANGE
argument_list|,
name|repo
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
return|;
block|}
specifier|public
specifier|static
specifier|final
name|IndexCreationEvent
name|packedIndexUriChange
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|URI
name|oldValue
parameter_list|,
name|URI
name|newValue
parameter_list|)
block|{
return|return
operator|new
name|IndexCreationEvent
argument_list|(
name|Index
operator|.
name|PACKED_INDEX_URI_CHANGE
argument_list|,
name|repo
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
return|;
block|}
block|}
end_class

end_unit

