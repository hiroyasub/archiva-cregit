begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rss
operator|.
name|processor
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|rometools
operator|.
name|rome
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndFeed
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rometools
operator|.
name|rome
operator|.
name|io
operator|.
name|FeedException
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
comment|/**  * Retrieve and process the data that will be fed into the RssFeedGenerator.  */
end_comment

begin_interface
specifier|public
interface|interface
name|RssFeedProcessor
block|{
name|String
name|KEY_REPO_ID
init|=
literal|"repoId"
decl_stmt|;
name|String
name|KEY_GROUP_ID
init|=
literal|"groupId"
decl_stmt|;
name|String
name|KEY_ARTIFACT_ID
init|=
literal|"artifactId"
decl_stmt|;
name|SyndFeed
name|process
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reqParams
parameter_list|)
throws|throws
name|FeedException
function_decl|;
block|}
end_interface

end_unit

