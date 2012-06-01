begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
operator|.
name|utils
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
name|metadata
operator|.
name|model
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
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
operator|.
name|MavenArtifactFacet
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
name|commons
operator|.
name|io
operator|.
name|FilenameUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DecimalFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DecimalFormatSymbols
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactDownloadInfoBuilder
block|{
specifier|private
name|ManagedRepositoryContent
name|managedRepositoryContent
decl_stmt|;
specifier|private
name|ArtifactMetadata
name|artifactMetadata
decl_stmt|;
specifier|public
name|ArtifactDownloadInfoBuilder
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ArtifactDownloadInfoBuilder
name|withManagedRepositoryContent
parameter_list|(
name|ManagedRepositoryContent
name|managedRepositoryContent
parameter_list|)
block|{
name|this
operator|.
name|managedRepositoryContent
operator|=
name|managedRepositoryContent
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ArtifactDownloadInfoBuilder
name|forArtifactMetadata
parameter_list|(
name|ArtifactMetadata
name|artifactMetadata
parameter_list|)
block|{
name|this
operator|.
name|artifactMetadata
operator|=
name|artifactMetadata
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Artifact
name|build
parameter_list|()
block|{
name|ArtifactReference
name|ref
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setArtifactId
argument_list|(
name|artifactMetadata
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setGroupId
argument_list|(
name|artifactMetadata
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setVersion
argument_list|(
name|artifactMetadata
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|type
init|=
literal|null
decl_stmt|,
name|classifier
init|=
literal|null
decl_stmt|;
name|MavenArtifactFacet
name|facet
init|=
operator|(
name|MavenArtifactFacet
operator|)
name|artifactMetadata
operator|.
name|getFacet
argument_list|(
name|MavenArtifactFacet
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|facet
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|facet
operator|.
name|getType
argument_list|()
expr_stmt|;
name|classifier
operator|=
name|facet
operator|.
name|getClassifier
argument_list|()
expr_stmt|;
block|}
name|ref
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|managedRepositoryContent
operator|.
name|toFile
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|String
name|extension
init|=
name|FilenameUtils
operator|.
name|getExtension
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|(
name|ref
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|ref
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|ref
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
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
name|artifact
operator|.
name|setFileExtension
argument_list|(
name|extension
argument_list|)
expr_stmt|;
comment|// TODO: find a reusable formatter for this
name|double
name|s
init|=
name|this
operator|.
name|artifactMetadata
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|String
name|symbol
init|=
literal|"b"
decl_stmt|;
if|if
condition|(
name|s
operator|>
literal|1024
condition|)
block|{
name|symbol
operator|=
literal|"K"
expr_stmt|;
name|s
operator|/=
literal|1024
expr_stmt|;
if|if
condition|(
name|s
operator|>
literal|1024
condition|)
block|{
name|symbol
operator|=
literal|"M"
expr_stmt|;
name|s
operator|/=
literal|1024
expr_stmt|;
if|if
condition|(
name|s
operator|>
literal|1024
condition|)
block|{
name|symbol
operator|=
literal|"G"
expr_stmt|;
name|s
operator|/=
literal|1024
expr_stmt|;
block|}
block|}
block|}
name|artifact
operator|.
name|setContext
argument_list|(
name|managedRepositoryContent
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|DecimalFormat
name|df
init|=
operator|new
name|DecimalFormat
argument_list|(
literal|"#,###.##"
argument_list|,
operator|new
name|DecimalFormatSymbols
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setSize
argument_list|(
name|df
operator|.
name|format
argument_list|(
name|s
argument_list|)
operator|+
literal|" "
operator|+
name|symbol
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

