begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"browseGroupIdResult"
argument_list|)
specifier|public
class|class
name|BrowseGroupIdResult
block|{
specifier|private
name|List
argument_list|<
name|BrowseGroupIdEntry
argument_list|>
name|browseGroupIdEntries
decl_stmt|;
specifier|public
name|BrowseGroupIdResult
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|BrowseGroupIdResult
parameter_list|(
name|List
argument_list|<
name|BrowseGroupIdEntry
argument_list|>
name|browseGroupIdEntries
parameter_list|)
block|{
comment|// no op
block|}
specifier|public
name|List
argument_list|<
name|BrowseGroupIdEntry
argument_list|>
name|getBrowseGroupIdEntries
parameter_list|()
block|{
return|return
name|browseGroupIdEntries
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|BrowseGroupIdEntry
operator|>
name|emptyList
argument_list|()
else|:
name|browseGroupIdEntries
return|;
block|}
specifier|public
name|void
name|setBrowseGroupIdEntries
parameter_list|(
name|List
argument_list|<
name|BrowseGroupIdEntry
argument_list|>
name|browseGroupIdEntries
parameter_list|)
block|{
name|this
operator|.
name|browseGroupIdEntries
operator|=
name|browseGroupIdEntries
expr_stmt|;
block|}
block|}
end_class

end_unit

