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
name|maven2
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
name|maven
operator|.
name|model
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
name|repository
operator|.
name|BaseRepositoryContentLayout
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
name|repository
operator|.
name|LayoutException
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
name|content
operator|.
name|base
operator|.
name|ArchivaItemSelector
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
name|storage
operator|.
name|StorageAsset
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
name|storage
operator|.
name|util
operator|.
name|StorageUtil
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
name|lang3
operator|.
name|StringUtils
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactBuilder
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
name|ArtifactBuilder
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ArtifactBuilder
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
name|ArtifactBuilder
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
name|ArchivaItemSelector
operator|.
name|Builder
name|selectorBuilder
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withNamespace
argument_list|(
name|artifactMetadata
operator|.
name|getNamespace
argument_list|( )
argument_list|)
operator|.
name|withProjectId
argument_list|(
name|artifactMetadata
operator|.
name|getProject
argument_list|( )
argument_list|)
operator|.
name|withVersion
argument_list|(
name|artifactMetadata
operator|.
name|getProjectVersion
argument_list|( )
argument_list|)
operator|.
name|withArtifactId
argument_list|(
name|artifactMetadata
operator|.
name|getProject
argument_list|( )
argument_list|)
operator|.
name|withArtifactVersion
argument_list|(
name|artifactMetadata
operator|.
name|getVersion
argument_list|( )
argument_list|)
decl_stmt|;
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
name|selectorBuilder
operator|.
name|withType
argument_list|(
name|type
argument_list|)
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
name|selectorBuilder
operator|.
name|withClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
name|BaseRepositoryContentLayout
name|layout
decl_stmt|;
try|try
block|{
name|layout
operator|=
name|managedRepositoryContent
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not convert to layout "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
throw|;
block|}
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|Artifact
name|repoArtifact
init|=
name|layout
operator|.
name|getArtifact
argument_list|(
name|selectorBuilder
operator|.
name|build
argument_list|( )
argument_list|)
decl_stmt|;
name|String
name|extension
init|=
name|repoArtifact
operator|.
name|getExtension
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|(
name|repoArtifact
operator|.
name|getVersion
argument_list|( )
operator|.
name|getProject
argument_list|( )
operator|.
name|getNamespace
argument_list|( )
operator|.
name|getId
argument_list|( )
argument_list|,
name|repoArtifact
operator|.
name|getId
argument_list|( )
argument_list|,
name|repoArtifact
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setRepositoryId
argument_list|(
name|artifactMetadata
operator|.
name|getRepositoryId
argument_list|()
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
name|artifact
operator|.
name|setType
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
name|artifact
operator|.
name|setPath
argument_list|(
name|managedRepositoryContent
operator|.
name|toPath
argument_list|(
name|repoArtifact
argument_list|)
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
name|artifact
operator|.
name|setId
argument_list|(
name|repoArtifact
operator|.
name|getId
argument_list|()
operator|+
literal|"-"
operator|+
name|repoArtifact
operator|.
name|getArtifactVersion
argument_list|()
operator|+
literal|"."
operator|+
name|repoArtifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
comment|/**      * Extract file extension      */
name|String
name|getExtensionFromFile
parameter_list|(
name|StorageAsset
name|file
parameter_list|)
block|{
comment|// we are just interested in the section after the last -
name|String
index|[]
name|parts
init|=
name|file
operator|.
name|getName
argument_list|()
operator|.
name|split
argument_list|(
literal|"-"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|// get anything after a dot followed by a letter a-z, including other dots
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\.([a-z]+[a-z0-9\\.]*)"
argument_list|)
decl_stmt|;
name|Matcher
name|m
init|=
name|p
operator|.
name|matcher
argument_list|(
name|parts
index|[
name|parts
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
comment|// just in case
return|return
name|StorageUtil
operator|.
name|getExtension
argument_list|(
name|file
argument_list|)
return|;
block|}
block|}
end_class

end_unit

