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

begin_class
specifier|public
class|class
name|ArchivaRoleConstants
block|{
comment|// globalish roles
specifier|public
specifier|static
specifier|final
name|String
name|SYSTEM_ADMINISTRATOR_ROLE
init|=
literal|"System Administrator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_ADMINISTRATOR_ROLE
init|=
literal|"User Administrator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REGISTERED_USER_ROLE
init|=
literal|"Registered User"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GUEST_ROLE
init|=
literal|"Guest"
decl_stmt|;
comment|// operations
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_MANAGE_USERS
init|=
literal|"archiva-manage-users"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_MANAGE_CONFIGURATION
init|=
literal|"archiva-manage-configuration"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_ACTIVE_GUEST
init|=
literal|"archiva-guest"
decl_stmt|;
block|}
end_class

end_unit

