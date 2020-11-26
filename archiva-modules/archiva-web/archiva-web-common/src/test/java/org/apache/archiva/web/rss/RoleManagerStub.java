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
name|rss
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
name|redback
operator|.
name|role
operator|.
name|RoleManager
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
name|redback
operator|.
name|role
operator|.
name|RoleManagerException
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
name|redback
operator|.
name|role
operator|.
name|model
operator|.
name|RedbackRoleModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"RoleManagerStub"
argument_list|)
specifier|public
class|class
name|RoleManagerStub
implements|implements
name|RoleManager
block|{
annotation|@
name|Override
specifier|public
name|void
name|loadRoleModel
parameter_list|(
name|URL
name|resourceLocation
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|loadRoleModel
parameter_list|(
name|RedbackRoleModel
name|model
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|String
name|createTemplatedRole
parameter_list|(
name|String
name|templateId
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeTemplatedRole
parameter_list|(
name|String
name|templateId
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|String
name|moveTemplatedRole
parameter_list|(
name|String
name|templateId
parameter_list|,
name|String
name|oldResource
parameter_list|,
name|String
name|newResource
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|assignRole
parameter_list|(
name|String
name|roleId
parameter_list|,
name|String
name|principal
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|assignRoleByName
parameter_list|(
name|String
name|roleName
parameter_list|,
name|String
name|principal
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|assignTemplatedRole
parameter_list|(
name|String
name|templateId
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|principal
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|unassignRole
parameter_list|(
name|String
name|roleId
parameter_list|,
name|String
name|principal
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|unassignRoleByName
parameter_list|(
name|String
name|roleName
parameter_list|,
name|String
name|principal
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|roleExists
parameter_list|(
name|String
name|roleId
parameter_list|)
throws|throws
name|RoleManagerException
block|{
return|return
literal|false
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|templatedRoleExists
parameter_list|(
name|String
name|templateId
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|RoleManagerException
block|{
return|return
literal|false
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|RedbackRoleModel
name|getModel
parameter_list|()
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|verifyTemplatedRole
parameter_list|(
name|String
name|templateID
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|RoleManagerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|()
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
block|}
end_class

end_unit

