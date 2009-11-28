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
name|repository
operator|.
name|filter
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
specifier|public
class|class
name|ExcludesFilter
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Filter
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|Collection
argument_list|<
name|T
argument_list|>
name|excludes
decl_stmt|;
specifier|public
name|ExcludesFilter
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|excludes
parameter_list|)
block|{
name|this
operator|.
name|excludes
operator|=
name|excludes
expr_stmt|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|T
name|value
parameter_list|)
block|{
return|return
operator|!
name|excludes
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

