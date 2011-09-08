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
operator|.
name|legacy
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|repository
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
name|repository
operator|.
name|admin
operator|.
name|ArchivaAdministration
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
name|repository
operator|.
name|admin
operator|.
name|LegacyArtifactPath
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArtifactReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepositoryContent
import|;
end_import

begin_import
import|import
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
name|AbstractActionSupport
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_comment
comment|/**  * Add a LegacyArtifactPath to archiva configuration  *  * @since 1.1  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"addLegacyArtifactPathAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|AddLegacyArtifactPathAction
extends|extends
name|AbstractActionSupport
implements|implements
name|Preparable
implements|,
name|Validateable
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaAdministration
name|archivaAdministration
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"managedRepositoryContent#legacy"
argument_list|)
specifier|private
name|ManagedRepositoryContent
name|repositoryContent
decl_stmt|;
specifier|private
name|LegacyArtifactPath
name|legacyArtifactPath
decl_stmt|;
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|this
operator|.
name|legacyArtifactPath
operator|=
operator|new
name|LegacyArtifactPath
argument_list|()
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
specifier|public
name|String
name|commit
parameter_list|()
block|{
name|this
operator|.
name|legacyArtifactPath
operator|.
name|setArtifact
argument_list|(
name|this
operator|.
name|groupId
operator|+
literal|":"
operator|+
name|this
operator|.
name|artifactId
operator|+
literal|":"
operator|+
name|this
operator|.
name|version
operator|+
literal|":"
operator|+
name|this
operator|.
name|classifier
operator|+
literal|":"
operator|+
name|this
operator|.
name|type
argument_list|)
expr_stmt|;
comment|// Check the proposed Artifact macthes the path
name|ArtifactReference
name|artifact
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
name|this
operator|.
name|groupId
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|this
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setClassifier
argument_list|(
name|this
operator|.
name|classifier
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|this
operator|.
name|version
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setType
argument_list|(
name|this
operator|.
name|type
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|repositoryContent
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|equals
argument_list|(
name|this
operator|.
name|legacyArtifactPath
operator|.
name|getPath
argument_list|()
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"artifact reference does not match the initial path : "
operator|+
name|path
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
try|try
block|{
name|getArchivaAdministration
argument_list|()
operator|.
name|addLegacyArtifactPath
argument_list|(
name|legacyArtifactPath
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"Error occured "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|LegacyArtifactPath
name|getLegacyArtifactPath
parameter_list|()
block|{
return|return
name|legacyArtifactPath
return|;
block|}
specifier|public
name|void
name|setLegacyArtifactPath
parameter_list|(
name|LegacyArtifactPath
name|legacyArtifactPath
parameter_list|)
block|{
name|this
operator|.
name|legacyArtifactPath
operator|=
name|legacyArtifactPath
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
comment|// trim all unecessary trailing/leading white-spaces; always put this statement before the closing braces(after all validation).
name|trimAllRequestParameterValues
argument_list|()
expr_stmt|;
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
name|legacyArtifactPath
operator|.
name|getPath
argument_list|()
argument_list|)
condition|)
block|{
name|legacyArtifactPath
operator|.
name|setPath
argument_list|(
name|legacyArtifactPath
operator|.
name|getPath
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|classifier
argument_list|)
condition|)
block|{
name|classifier
operator|=
name|classifier
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
name|type
argument_list|)
condition|)
block|{
name|type
operator|=
name|type
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
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
name|ArchivaAdministration
name|getArchivaAdministration
parameter_list|()
block|{
return|return
name|archivaAdministration
return|;
block|}
specifier|public
name|void
name|setArchivaAdministration
parameter_list|(
name|ArchivaAdministration
name|archivaAdministration
parameter_list|)
block|{
name|this
operator|.
name|archivaAdministration
operator|=
name|archivaAdministration
expr_stmt|;
block|}
block|}
end_class

end_unit

