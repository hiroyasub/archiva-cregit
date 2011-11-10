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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Preparable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Validateable
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
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|audit
operator|.
name|Auditable
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
name|checksum
operator|.
name|ChecksumAlgorithm
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
name|common
operator|.
name|utils
operator|.
name|VersionUtil
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|Artifact
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|RepositoriesService
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
name|security
operator|.
name|AccessDeniedException
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
name|security
operator|.
name|ArchivaSecurityException
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
name|security
operator|.
name|PrincipalNotFoundException
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
name|security
operator|.
name|UserRepositories
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|RedbackAuthenticationThreadLocal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
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
name|Controller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
comment|/**  * Delete an artifact. Metadata will be updated if one exists, otherwise it would be created.  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"deleteArtifactAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|DeleteArtifactAction
extends|extends
name|AbstractActionSupport
implements|implements
name|Validateable
implements|,
name|Preparable
implements|,
name|Auditable
block|{
comment|/**      * The groupId of the artifact to be deleted.      */
specifier|private
name|String
name|groupId
decl_stmt|;
comment|/**      * The artifactId of the artifact to be deleted.      */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|/**      * The version of the artifact to be deleted.      */
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      * @since 1.4-M2      *        The classifier of the artifact to be deleted (optionnal)      */
specifier|private
name|String
name|classifier
decl_stmt|;
comment|/**      * @since 1.4-M2      *        The type of the artifact to be deleted (optionnal) (default jar)      */
specifier|private
name|String
name|type
decl_stmt|;
comment|/**      * The repository where the artifact is to be deleted.      */
specifier|private
name|String
name|repositoryId
decl_stmt|;
comment|/**      * List of managed repositories to delete from.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|managedRepos
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UserRepositories
name|userRepositories
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoriesService
name|repositoriesService
decl_stmt|;
specifier|private
name|ChecksumAlgorithm
index|[]
name|algorithms
init|=
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|SHA1
block|,
name|ChecksumAlgorithm
operator|.
name|MD5
block|}
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|super
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getManagedRepos
parameter_list|()
block|{
return|return
name|managedRepos
return|;
block|}
specifier|public
name|void
name|setManagedRepos
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|managedRepos
parameter_list|)
block|{
name|this
operator|.
name|managedRepos
operator|=
name|managedRepos
expr_stmt|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|managedRepos
operator|=
name|getManagableRepos
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|input
parameter_list|()
block|{
return|return
name|INPUT
return|;
block|}
specifier|private
name|void
name|reset
parameter_list|()
block|{
comment|// reset the fields so the form is clear when
comment|// the action returns to the jsp page
name|groupId
operator|=
literal|""
expr_stmt|;
name|artifactId
operator|=
literal|""
expr_stmt|;
name|version
operator|=
literal|""
expr_stmt|;
name|repositoryId
operator|=
literal|""
expr_stmt|;
name|classifier
operator|=
literal|""
expr_stmt|;
name|type
operator|=
literal|""
expr_stmt|;
block|}
specifier|public
name|String
name|doDelete
parameter_list|()
block|{
comment|// services need a ThreadLocal variable to test karma
name|RedbackAuthenticationThreadLocal
operator|.
name|set
argument_list|(
name|getRedbackRequestInformation
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setPackaging
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|repositoriesService
operator|.
name|deleteArtifact
argument_list|(
name|artifact
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaRestServiceException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"ArchivaRestServiceException exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
finally|finally
block|{
name|RedbackAuthenticationThreadLocal
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Artifact \'"
argument_list|)
operator|.
name|append
argument_list|(
name|groupId
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
literal|"' was successfully deleted from repository '"
argument_list|)
operator|.
name|append
argument_list|(
name|repositoryId
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|!
name|userRepositories
operator|.
name|isAuthorizedToDeleteArtifacts
argument_list|(
name|getPrincipal
argument_list|()
argument_list|,
name|repositoryId
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"User is not authorized to delete artifacts in repository '"
operator|+
name|repositoryId
operator|+
literal|"'."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|version
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
operator|&&
operator|(
operator|!
name|VersionUtil
operator|.
name|isVersion
argument_list|(
name|version
argument_list|)
operator|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Invalid version."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaSecurityException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// trims all request parameter values, since the trailing/leading white-spaces are ignored during validation.
name|trimAllRequestParameterValues
argument_list|()
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getManagableRepos
parameter_list|()
block|{
try|try
block|{
return|return
name|userRepositories
operator|.
name|getManagableRepositoryIds
argument_list|(
name|getPrincipal
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PrincipalNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// TODO: pass this onto the screen.
block|}
catch|catch
parameter_list|(
name|ArchivaSecurityException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|private
name|void
name|trimAllRequestParameterValues
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
name|groupId
operator|=
name|groupId
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
name|artifactId
operator|=
name|artifactId
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|version
operator|=
name|version
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
name|repositoryId
operator|=
name|repositoryId
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|ManagedRepositoryAdmin
name|getManagedRepositoryAdmin
parameter_list|()
block|{
return|return
name|managedRepositoryAdmin
return|;
block|}
specifier|public
name|void
name|setManagedRepositoryAdmin
parameter_list|(
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
parameter_list|)
block|{
name|this
operator|.
name|managedRepositoryAdmin
operator|=
name|managedRepositoryAdmin
expr_stmt|;
block|}
specifier|public
name|RepositoriesService
name|getRepositoriesService
parameter_list|()
block|{
return|return
name|repositoriesService
return|;
block|}
specifier|public
name|void
name|setRepositoriesService
parameter_list|(
name|RepositoriesService
name|repositoriesService
parameter_list|)
block|{
name|this
operator|.
name|repositoriesService
operator|=
name|repositoriesService
expr_stmt|;
block|}
block|}
end_class

end_unit

