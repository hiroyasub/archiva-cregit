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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|rbac
operator|.
name|RbacObjectNotFoundException
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
name|redback
operator|.
name|rbac
operator|.
name|RbacManagerException
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
comment|/**  * UserRepositories   *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|UserRepositories
block|{
comment|/**      * Get the list of observable repository ids for the user specified.      *       * @param principal the principle to obtain the observable repository ids from.      * @return the list of observable repository ids.      * @throws PrincipalNotFoundException      * @throws AccessDeniedException      * @throws ArchivaSecurityException      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getObservableRepositoryIds
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|PrincipalNotFoundException
throws|,
name|AccessDeniedException
throws|,
name|ArchivaSecurityException
function_decl|;
comment|/**      * Get the list of writable repository ids for the user specified.      *       * @param principal the principle to obtain the observable repository ids from.      * @return the list of observable repository ids.      * @throws PrincipalNotFoundException      * @throws AccessDeniedException      * @throws ArchivaSecurityException      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getManagableRepositoryIds
parameter_list|(
name|String
name|principal
parameter_list|)
throws|throws
name|PrincipalNotFoundException
throws|,
name|AccessDeniedException
throws|,
name|ArchivaSecurityException
function_decl|;
comment|/**      * Create any missing repository roles for the provided repository id.      *       * @param repoId the repository id to work off of.      * @throws ArchivaSecurityException if there was a problem creating the repository roles.      */
specifier|public
name|void
name|createMissingRepositoryRoles
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|ArchivaSecurityException
function_decl|;
comment|/**      * Check if user is authorized to upload artifacts in the repository.      *       * @param principal      * @param repoId      * @return      * @throws PrincipalNotFoundException      * @throws ArchivaSecurityException      */
specifier|public
name|boolean
name|isAuthorizedToUploadArtifacts
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|PrincipalNotFoundException
throws|,
name|ArchivaSecurityException
function_decl|;
comment|/**      * Check if user is authorized to delete artifacts in the repository.      *       * @param principal      * @param repoId      * @return      * @throws RbacManagerException      * @throws RbacObjectNotFoundException      */
specifier|public
name|boolean
name|isAuthorizedToDeleteArtifacts
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|RbacManagerException
throws|,
name|RbacObjectNotFoundException
function_decl|;
block|}
end_interface

end_unit

