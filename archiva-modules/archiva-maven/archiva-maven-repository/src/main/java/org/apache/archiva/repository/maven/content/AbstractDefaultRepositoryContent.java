begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|maven
operator|.
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|RepositoryPathTranslator
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
name|maven
operator|.
name|metadata
operator|.
name|storage
operator|.
name|ArtifactMappingProvider
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
name|maven
operator|.
name|metadata
operator|.
name|storage
operator|.
name|Maven2RepositoryPathTranslator
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
name|ArchivaArtifact
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
name|model
operator|.
name|ProjectReference
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
name|VersionedReference
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
name|RepositoryContent
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
name|ItemSelector
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
name|PathParser
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
comment|/**  * AbstractDefaultRepositoryContent - common methods for working with default (maven 2) layout.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDefaultRepositoryContent
implements|implements
name|RepositoryContent
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAVEN_METADATA
init|=
literal|"maven-metadata.xml"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|char
name|PATH_SEPARATOR
init|=
literal|'/'
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|char
name|GROUP_SEPARATOR
init|=
literal|'.'
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|char
name|ARTIFACT_SEPARATOR
init|=
literal|'-'
decl_stmt|;
specifier|private
name|RepositoryPathTranslator
name|pathTranslator
init|=
operator|new
name|Maven2RepositoryPathTranslator
argument_list|()
decl_stmt|;
specifier|private
name|PathParser
name|defaultPathParser
init|=
operator|new
name|DefaultPathParser
argument_list|()
decl_stmt|;
name|PathParser
name|getPathParser
parameter_list|()
block|{
return|return
name|defaultPathParser
return|;
block|}
comment|/**      *      */
specifier|protected
name|List
argument_list|<
name|?
extends|extends
name|ArtifactMappingProvider
argument_list|>
name|artifactMappingProviders
decl_stmt|;
name|AbstractDefaultRepositoryContent
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|ArtifactMappingProvider
argument_list|>
name|artifactMappingProviders
parameter_list|)
block|{
name|this
operator|.
name|artifactMappingProviders
operator|=
name|artifactMappingProviders
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactMappingProviders
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|ArtifactMappingProvider
argument_list|>
name|artifactMappingProviders
parameter_list|)
block|{
name|this
operator|.
name|artifactMappingProviders
operator|=
name|artifactMappingProviders
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ItemSelector
name|toItemSelector
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
name|defaultPathParser
operator|.
name|toItemSelector
argument_list|(
name|path
argument_list|)
return|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|formatAsDirectory
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
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
name|reference
operator|.
name|getArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|path
operator|.
name|toString
argument_list|( )
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toPath
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
block|{
if|if
condition|(
name|selector
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ItemSelector must not be null."
argument_list|)
throw|;
block|}
name|String
name|projectId
decl_stmt|;
comment|// Initialize the project id if not set
if|if
condition|(
name|selector
operator|.
name|hasProjectId
argument_list|()
condition|)
block|{
name|projectId
operator|=
name|selector
operator|.
name|getProjectId
argument_list|( )
expr_stmt|;
block|}
if|else if
condition|(
name|selector
operator|.
name|hasArtifactId
argument_list|()
condition|)
block|{
comment|// projectId same as artifact id, if set
name|projectId
operator|=
name|selector
operator|.
name|getArtifactId
argument_list|( )
expr_stmt|;
block|}
else|else
block|{
comment|// we arrive here, if projectId&& artifactId not set
return|return
name|pathTranslator
operator|.
name|toPath
argument_list|(
name|selector
operator|.
name|getNamespace
argument_list|()
argument_list|,
literal|""
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|selector
operator|.
name|hasArtifactId
argument_list|( )
condition|)
block|{
return|return
name|pathTranslator
operator|.
name|toPath
argument_list|(
name|selector
operator|.
name|getNamespace
argument_list|( )
argument_list|,
name|projectId
argument_list|)
return|;
block|}
comment|// this part only, if projectId&& artifactId is set
name|String
name|artifactVersion
init|=
literal|""
decl_stmt|;
name|String
name|version
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|selector
operator|.
name|hasVersion
argument_list|()
operator|&&
name|selector
operator|.
name|hasArtifactVersion
argument_list|()
condition|)
block|{
name|artifactVersion
operator|=
name|selector
operator|.
name|getArtifactVersion
argument_list|()
expr_stmt|;
name|version
operator|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|selector
operator|.
name|hasVersion
argument_list|()
operator|&&
name|selector
operator|.
name|hasArtifactVersion
argument_list|()
condition|)
block|{
comment|// we try to retrieve the base version, if artifact version is only set
name|version
operator|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|artifactVersion
operator|=
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
expr_stmt|;
block|}
if|else if
condition|(
name|selector
operator|.
name|hasVersion
argument_list|()
operator|&&
operator|!
name|selector
operator|.
name|hasArtifactVersion
argument_list|()
condition|)
block|{
name|artifactVersion
operator|=
name|selector
operator|.
name|getVersion
argument_list|()
expr_stmt|;
name|version
operator|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
block|}
return|return
name|pathTranslator
operator|.
name|toPath
argument_list|(
name|selector
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|projectId
argument_list|,
name|version
argument_list|,
name|constructId
argument_list|(
name|selector
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifactVersion
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|selector
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|toMetadataPath
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|formatAsDirectory
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
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
name|reference
operator|.
name|getArtifactId
argument_list|()
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
name|MAVEN_METADATA
argument_list|)
expr_stmt|;
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|formatAsDirectory
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|formatAsDirectory
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
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
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
if|if
condition|(
name|reference
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// add the version only if it is present
name|path
operator|.
name|append
argument_list|(
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
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
specifier|public
name|String
name|toMetadataPath
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
block|{
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|formatAsDirectory
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
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
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
if|if
condition|(
name|reference
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// add the version only if it is present
name|path
operator|.
name|append
argument_list|(
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|path
operator|.
name|append
argument_list|(
name|MAVEN_METADATA
argument_list|)
expr_stmt|;
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|ArchivaArtifact
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ArchivaArtifact cannot be null"
argument_list|)
throw|;
block|}
name|String
name|baseVersion
init|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|toPath
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|baseVersion
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|,
name|reference
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Artifact reference cannot be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|reference
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|baseVersion
init|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|toPath
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|baseVersion
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|,
name|reference
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
return|return
name|toPath
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|reference
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|String
name|formatAsDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
return|return
name|directory
operator|.
name|replace
argument_list|(
name|GROUP_SEPARATOR
argument_list|,
name|PATH_SEPARATOR
argument_list|)
return|;
block|}
specifier|private
name|String
name|toPath
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|baseVersion
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|baseVersion
operator|!=
literal|null
condition|)
block|{
return|return
name|pathTranslator
operator|.
name|toPath
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|baseVersion
argument_list|,
name|constructId
argument_list|(
name|artifactId
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|pathTranslator
operator|.
name|toPath
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
return|;
block|}
block|}
comment|// TODO: move into the Maven Artifact facet when refactoring away the caller - the caller will need to have access
comment|//       to the facet or filename (for the original ID)
specifier|private
name|String
name|constructId
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|String
name|ext
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ArtifactMappingProvider
name|provider
range|:
name|artifactMappingProviders
control|)
block|{
name|ext
operator|=
name|provider
operator|.
name|mapTypeToExtension
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|ext
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|ext
operator|==
literal|null
condition|)
block|{
name|ext
operator|=
name|type
expr_stmt|;
block|}
name|StringBuilder
name|id
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|version
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|type
operator|!=
literal|null
operator|)
condition|)
block|{
name|id
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
name|ARTIFACT_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|id
operator|.
name|append
argument_list|(
name|ARTIFACT_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
name|id
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|ext
argument_list|)
expr_stmt|;
block|}
return|return
name|id
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

