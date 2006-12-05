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
comment|/*  * Copyright 2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DefaultRoleProfileManager
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
name|rbac
operator|.
name|profile
operator|.
name|RoleProfileException
import|;
end_import

begin_comment
comment|/**  * Role profile manager.  *  * @author Brett Porter  * @todo composition over inheritence?  * @plexus.component role="org.codehaus.plexus.rbac.profile.RoleProfileManager" role-hint="archiva"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaRoleProfileManager
extends|extends
name|DefaultRoleProfileManager
block|{
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|RoleProfileException
block|{
name|getRole
argument_list|(
literal|"archiva-repository-manager-base"
argument_list|)
expr_stmt|;
name|mergeRoleProfiles
argument_list|(
literal|"system-administrator"
argument_list|,
literal|"archiva-system-administrator"
argument_list|)
expr_stmt|;
name|mergeRoleProfiles
argument_list|(
literal|"user-administrator"
argument_list|,
literal|"archiva-user-administrator"
argument_list|)
expr_stmt|;
name|mergeRoleProfiles
argument_list|(
literal|"guest"
argument_list|,
literal|"archiva-guest"
argument_list|)
expr_stmt|;
name|setInitialized
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|//todo remove the initialization idea from profile managers
block|}
block|}
end_class

end_unit

