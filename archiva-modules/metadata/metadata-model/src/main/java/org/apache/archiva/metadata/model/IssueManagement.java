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

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Information about the issue management system used by the project.  *  * @todo considering moving this to a facet - avoid referring to it externally  */
end_comment

begin_class
specifier|public
class|class
name|IssueManagement
block|{
comment|/**      * A simple identifier for the type of issue management server used, eg<tt>jira</tt>,<tt>bugzilla</tt>, etc.      */
specifier|private
name|String
name|system
decl_stmt|;
comment|/**      * The base URL of the issue management system.      */
specifier|private
name|String
name|url
decl_stmt|;
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getSystem
parameter_list|()
block|{
return|return
name|system
return|;
block|}
specifier|public
name|void
name|setSystem
parameter_list|(
name|String
name|system
parameter_list|)
block|{
name|this
operator|.
name|system
operator|=
name|system
expr_stmt|;
block|}
block|}
end_class

end_unit

