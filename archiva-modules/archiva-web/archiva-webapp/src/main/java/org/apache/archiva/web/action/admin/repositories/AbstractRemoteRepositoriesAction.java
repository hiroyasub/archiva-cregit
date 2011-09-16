begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|repositories
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|remote
operator|.
name|RemoteRepositoryAdmin
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * AbstractRemoteRepositoriesAction  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|AbstractRemoteRepositoriesAction
extends|extends
name|AbstractRepositoriesAdminAction
block|{
annotation|@
name|Inject
specifier|private
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
decl_stmt|;
specifier|public
name|RemoteRepositoryAdmin
name|getRemoteRepositoryAdmin
parameter_list|()
block|{
return|return
name|remoteRepositoryAdmin
return|;
block|}
specifier|public
name|void
name|setRemoteRepositoryAdmin
parameter_list|(
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
parameter_list|)
block|{
name|this
operator|.
name|remoteRepositoryAdmin
operator|=
name|remoteRepositoryAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

