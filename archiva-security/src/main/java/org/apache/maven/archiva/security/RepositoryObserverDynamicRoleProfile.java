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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|AbstractDynamicRoleProfile
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
comment|/**  * @plexus.component role="org.codehaus.plexus.rbac.profile.DynamicRoleProfile"  * role-hint="archiva-repository-observer"  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryObserverDynamicRoleProfile
extends|extends
name|AbstractDynamicRoleProfile
block|{
comment|/**      * Create the Role name for a Repository Observer, using the provided repository id.      *      * @param repoId the repository id      */
specifier|public
name|String
name|getRoleName
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
return|return
name|ArchivaRoleConstants
operator|.
name|REPOSITORY_OBSERVER_ROLE_PREFIX
operator|+
name|ArchivaRoleConstants
operator|.
name|DELIMITER
operator|+
name|repoId
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
name|OPERATION_REPOSITORY_ACCESS
argument_list|)
expr_stmt|;
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
literal|true
return|;
block|}
block|}
end_class

end_unit

