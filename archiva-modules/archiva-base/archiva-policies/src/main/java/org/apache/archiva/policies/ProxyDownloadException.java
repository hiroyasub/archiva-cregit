begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|policies
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|ArchivaException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * One or more exceptions occurred downloading from a remote repository during the proxy phase.  */
end_comment

begin_class
specifier|public
class|class
name|ProxyDownloadException
extends|extends
name|ArchivaException
block|{
comment|/**      * A list of failures keyed by repository ID.      */
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|failures
decl_stmt|;
specifier|public
name|ProxyDownloadException
parameter_list|(
name|String
name|message
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|Exception
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|constructMessage
argument_list|(
name|message
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
name|repositoryId
argument_list|,
name|cause
argument_list|)
argument_list|)
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|failures
operator|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|repositoryId
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ProxyDownloadException
parameter_list|(
name|String
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|failures
parameter_list|)
block|{
name|super
argument_list|(
name|constructMessage
argument_list|(
name|message
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|failures
operator|=
name|failures
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|constructMessage
parameter_list|(
name|String
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|failures
parameter_list|)
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|entry
range|:
name|failures
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"\n\t"
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|msg
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|getFailures
parameter_list|()
block|{
return|return
name|failures
return|;
block|}
block|}
end_class

end_unit

