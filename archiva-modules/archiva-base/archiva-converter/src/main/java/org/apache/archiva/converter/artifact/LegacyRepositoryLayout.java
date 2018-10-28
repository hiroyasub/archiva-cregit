begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|converter
operator|.
name|artifact
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
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
name|maven
operator|.
name|artifact
operator|.
name|handler
operator|.
name|ArtifactHandler
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
name|artifact
operator|.
name|metadata
operator|.
name|ArtifactMetadata
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|artifact
operator|.
name|repository
operator|.
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|component
operator|.
name|annotations
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * @author jdcasey  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
name|role
operator|=
name|ArtifactRepositoryLayout
operator|.
name|class
argument_list|,
name|hint
operator|=
literal|"legacy"
argument_list|)
specifier|public
class|class
name|LegacyRepositoryLayout
implements|implements
name|ArtifactRepositoryLayout
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PATH_SEPARATOR
init|=
literal|"/"
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"legacy"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|pathOf
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ArtifactHandler
name|artifactHandler
init|=
name|artifact
operator|.
name|getArtifactHandler
argument_list|()
decl_stmt|;
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|(
literal|128
argument_list|)
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|artifactHandler
operator|.
name|getDirectory
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifact
operator|.
name|hasClassifier
argument_list|()
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifactHandler
operator|.
name|getExtension
argument_list|()
operator|!=
literal|null
operator|&&
name|artifactHandler
operator|.
name|getExtension
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|artifactHandler
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|pathOfLocalRepositoryMetadata
parameter_list|(
name|ArtifactMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
block|{
return|return
name|pathOfRepositoryMetadata
argument_list|(
name|metadata
argument_list|,
name|metadata
operator|.
name|getLocalFilename
argument_list|(
name|repository
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|pathOfRepositoryMetadata
parameter_list|(
name|ArtifactMetadata
name|metadata
parameter_list|,
name|String
name|filename
parameter_list|)
block|{
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|(
literal|128
argument_list|)
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
literal|"poms"
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|filename
argument_list|)
expr_stmt|;
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|pathOfRemoteRepositoryMetadata
parameter_list|(
name|ArtifactMetadata
name|metadata
parameter_list|)
block|{
return|return
name|pathOfRepositoryMetadata
argument_list|(
name|metadata
argument_list|,
name|metadata
operator|.
name|getRemoteFilename
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

