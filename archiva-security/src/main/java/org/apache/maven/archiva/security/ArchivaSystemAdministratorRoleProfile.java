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
name|security
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|rbac
operator|.
name|profile
operator|.
name|AbstractRoleProfile
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
comment|/**  * @todo why does this need to be created in the client app?  * @todo composition instead of inheritence?  * @plexus.component role="org.codehaus.plexus.rbac.profile.RoleProfile" role-hint="archiva-system-administrator"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaSystemAdministratorRoleProfile
extends|extends
name|AbstractRoleProfile
block|{
specifier|public
name|String
name|getRoleName
parameter_list|()
block|{
return|return
name|ArchivaRoleConstants
operator|.
name|SYSTEM_ADMINISTRATOR_ROLE
return|;
block|}
specifier|public
name|List
name|getOperations
parameter_list|()
block|{
name|List
name|operations
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|operations
operator|.
name|add
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
expr_stmt|;
name|operations
operator|.
name|add
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_USERS
argument_list|)
expr_stmt|;
name|operations
operator|.
name|add
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_RUN_INDEXER
argument_list|)
expr_stmt|;
name|operations
operator|.
name|add
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_REGENERATE_INDEX
argument_list|)
expr_stmt|;
name|operations
operator|.
name|add
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_ACCESS_REPORT
argument_list|)
expr_stmt|;
comment|// TODO: does this need to be templated?
name|operations
operator|.
name|add
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_ADD_REPOSITORY
argument_list|)
expr_stmt|;
name|operations
operator|.
name|add
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_EDIT_REPOSITORY
argument_list|)
expr_stmt|;
name|operations
operator|.
name|add
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_DELETE_REPOSITORY
argument_list|)
expr_stmt|;
comment|// we don't add access/upload repository operations. This isn't a sys-admin function, and we don't want to
comment|// encourage the use of the sys admin role for such operations. They can grant it as necessary.
return|return
name|operations
return|;
block|}
specifier|public
name|boolean
name|isAssignable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

