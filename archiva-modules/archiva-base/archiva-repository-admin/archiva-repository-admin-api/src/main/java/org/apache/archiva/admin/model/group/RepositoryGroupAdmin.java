begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|group
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
name|admin
operator|.
name|model
operator|.
name|AuditInformation
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
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|RepositoryGroup
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryGroupAdmin
block|{
name|List
argument_list|<
name|RepositoryGroup
argument_list|>
name|getRepositoriesGroups
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
name|RepositoryGroup
name|getRepositoryGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
name|Boolean
name|addRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
name|Boolean
name|updateRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
name|Boolean
name|deleteRepositoryGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
name|Boolean
name|addRepositoryToGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
name|Boolean
name|deleteRepositoryFromGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * @return Map with key repoGroupId and value repoGroup      * @throws RepositoryAdminException      */
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|getRepositoryGroupsAsMap
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * @return Map with key repoGroupId and value List of ManagedRepositories      * @throws RepositoryAdminException      */
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getGroupToRepositoryMap
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * @return Map with key managedRepo id and value List of repositoryGroup ids where the repo is      * @throws RepositoryAdminException      */
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getRepositoryToGroupMap
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
name|Path
name|getMergedIndexDirectory
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

