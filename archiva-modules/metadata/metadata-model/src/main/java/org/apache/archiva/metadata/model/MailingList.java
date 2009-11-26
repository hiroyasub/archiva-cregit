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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_class
specifier|public
class|class
name|MailingList
block|{
specifier|private
name|String
name|mainArchiveUrl
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|otherArchives
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|postAddress
decl_stmt|;
specifier|private
name|String
name|subscribeAddress
decl_stmt|;
specifier|private
name|String
name|unsubscribeAddress
decl_stmt|;
specifier|public
name|void
name|setMainArchiveUrl
parameter_list|(
name|String
name|mainArchiveUrl
parameter_list|)
block|{
name|this
operator|.
name|mainArchiveUrl
operator|=
name|mainArchiveUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getMainArchiveUrl
parameter_list|()
block|{
return|return
name|mainArchiveUrl
return|;
block|}
specifier|public
name|void
name|setOtherArchives
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|otherArchives
parameter_list|)
block|{
name|this
operator|.
name|otherArchives
operator|=
name|otherArchives
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOtherArchives
parameter_list|()
block|{
return|return
name|otherArchives
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setPostAddress
parameter_list|(
name|String
name|postAddress
parameter_list|)
block|{
name|this
operator|.
name|postAddress
operator|=
name|postAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setSubscribeAddress
parameter_list|(
name|String
name|subscribeAddress
parameter_list|)
block|{
name|this
operator|.
name|subscribeAddress
operator|=
name|subscribeAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setUnsubscribeAddress
parameter_list|(
name|String
name|unsubscribeAddress
parameter_list|)
block|{
name|this
operator|.
name|unsubscribeAddress
operator|=
name|unsubscribeAddress
expr_stmt|;
block|}
specifier|public
name|String
name|getSubscribeAddress
parameter_list|()
block|{
return|return
name|subscribeAddress
return|;
block|}
specifier|public
name|String
name|getUnsubscribeAddress
parameter_list|()
block|{
return|return
name|unsubscribeAddress
return|;
block|}
specifier|public
name|String
name|getPostAddress
parameter_list|()
block|{
return|return
name|postAddress
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

