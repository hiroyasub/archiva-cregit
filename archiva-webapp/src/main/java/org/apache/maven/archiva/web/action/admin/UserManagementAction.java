begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|admin
package|;
end_package

begin_comment
comment|/*  * Copyright 2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|ModelDriven
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|Preparable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|security
operator|.
name|rbac
operator|.
name|RBACManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|security
operator|.
name|user
operator|.
name|User
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|security
operator|.
name|user
operator|.
name|UserManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
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
name|List
import|;
end_import

begin_comment
comment|/**  * LoginAction:  *  * @author Jesse McConnell<jmcconnell@apache.org>  * @version $Id:$  * @plexus.component role="com.opensymphony.xwork.Action"  * role-hint="userManagement"  */
end_comment

begin_class
specifier|public
class|class
name|UserManagementAction
extends|extends
name|PlexusActionSupport
implements|implements
name|ModelDriven
implements|,
name|Preparable
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|UserManager
name|userManager
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RBACManager
name|rbacManager
decl_stmt|;
specifier|private
name|User
name|user
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|String
name|principal
decl_stmt|;
specifier|private
name|List
name|availableRoles
decl_stmt|;
specifier|private
name|List
name|assignedRoles
decl_stmt|;
specifier|private
name|List
name|resources
decl_stmt|;
specifier|private
name|String
name|resourceName
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|username
operator|=
operator|(
operator|(
name|User
operator|)
name|session
operator|.
name|get
argument_list|(
literal|"user"
argument_list|)
operator|)
operator|.
name|getUsername
argument_list|()
expr_stmt|;
name|user
operator|=
name|userManager
operator|.
name|findUser
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|user
operator|=
name|userManager
operator|.
name|findUser
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
name|resources
operator|=
name|rbacManager
operator|.
name|getAllResources
argument_list|()
expr_stmt|;
name|availableRoles
operator|=
name|rbacManager
operator|.
name|getAllAssignableRoles
argument_list|()
expr_stmt|;
name|principal
operator|=
operator|(
operator|(
name|User
operator|)
name|session
operator|.
name|get
argument_list|(
literal|"user"
argument_list|)
operator|)
operator|.
name|getPrincipal
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
if|if
condition|(
name|principal
operator|!=
literal|null
operator|&&
name|rbacManager
operator|.
name|userAssignmentExists
argument_list|(
name|principal
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"recovering assigned roles"
argument_list|)
expr_stmt|;
name|assignedRoles
operator|=
operator|new
name|ArrayList
argument_list|(
name|rbacManager
operator|.
name|getAssignedRoles
argument_list|(
name|principal
argument_list|)
argument_list|)
expr_stmt|;
name|availableRoles
operator|=
operator|new
name|ArrayList
argument_list|(
name|rbacManager
operator|.
name|getUnassignedRoles
argument_list|(
name|principal
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"new assigned roles"
argument_list|)
expr_stmt|;
name|assignedRoles
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|availableRoles
operator|=
name|rbacManager
operator|.
name|getAllAssignableRoles
argument_list|()
expr_stmt|;
block|}
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"assigned roles: "
operator|+
name|assignedRoles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"available roles: "
operator|+
name|availableRoles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|save
parameter_list|()
throws|throws
name|Exception
block|{
name|User
name|temp
init|=
name|userManager
operator|.
name|findUser
argument_list|(
name|username
argument_list|)
decl_stmt|;
name|temp
operator|.
name|setEmail
argument_list|(
name|user
operator|.
name|getEmail
argument_list|()
argument_list|)
expr_stmt|;
name|temp
operator|.
name|setFullName
argument_list|(
name|user
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
name|temp
operator|.
name|setLocked
argument_list|(
name|user
operator|.
name|isLocked
argument_list|()
argument_list|)
expr_stmt|;
name|userManager
operator|.
name|updateUser
argument_list|(
name|temp
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|Object
name|getModel
parameter_list|()
block|{
return|return
name|user
return|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
specifier|public
name|User
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
specifier|public
name|String
name|getPrincipal
parameter_list|()
block|{
return|return
name|principal
return|;
block|}
specifier|public
name|void
name|setPrincipal
parameter_list|(
name|String
name|principal
parameter_list|)
block|{
name|this
operator|.
name|principal
operator|=
name|principal
expr_stmt|;
block|}
specifier|public
name|List
name|getAvailableRoles
parameter_list|()
block|{
return|return
name|availableRoles
return|;
block|}
specifier|public
name|void
name|setAvailableRoles
parameter_list|(
name|List
name|availableRoles
parameter_list|)
block|{
name|this
operator|.
name|availableRoles
operator|=
name|availableRoles
expr_stmt|;
block|}
specifier|public
name|List
name|getAssignedRoles
parameter_list|()
block|{
return|return
name|assignedRoles
return|;
block|}
specifier|public
name|void
name|setAssignedRoles
parameter_list|(
name|List
name|assignedRoles
parameter_list|)
block|{
name|this
operator|.
name|assignedRoles
operator|=
name|assignedRoles
expr_stmt|;
block|}
specifier|public
name|List
name|getResources
parameter_list|()
block|{
return|return
name|resources
return|;
block|}
specifier|public
name|void
name|setResources
parameter_list|(
name|List
name|resources
parameter_list|)
block|{
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
block|}
specifier|public
name|String
name|getResourceName
parameter_list|()
block|{
return|return
name|resourceName
return|;
block|}
specifier|public
name|void
name|setResourceName
parameter_list|(
name|String
name|resourceName
parameter_list|)
block|{
name|this
operator|.
name|resourceName
operator|=
name|resourceName
expr_stmt|;
block|}
block|}
end_class

end_unit

