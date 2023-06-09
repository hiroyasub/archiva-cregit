begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|repository
operator|.
name|metadata
operator|.
name|storage
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
name|maven
operator|.
name|metadata
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
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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
name|List
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
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryPathTranslator#maven2"
argument_list|)
specifier|public
class|class
name|Maven2RepositoryPathTranslator
implements|implements
name|RepositoryPathTranslator
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Maven2RepositoryPathTranslator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|GROUP_SEPARATOR
init|=
literal|'.'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|TIMESTAMP_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"([0-9]{8}.[0-9]{6})-([0-9]+).*"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|MAVEN_PLUGIN_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(maven-.*-plugin)|(.*-maven-plugin)$"
argument_list|)
decl_stmt|;
comment|/**      *      * see #initialize      */
annotation|@
name|Inject
specifier|private
name|List
argument_list|<
name|ArtifactMappingProvider
argument_list|>
name|artifactMappingProviders
decl_stmt|;
specifier|public
name|Maven2RepositoryPathTranslator
parameter_list|()
block|{
comment|// noop
block|}
specifier|public
name|List
argument_list|<
name|ArtifactMappingProvider
argument_list|>
name|getArtifactMappingProviders
parameter_list|( )
block|{
return|return
name|artifactMappingProviders
return|;
block|}
specifier|public
name|void
name|setArtifactMappingProviders
parameter_list|(
name|List
argument_list|<
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
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
comment|//artifactMappingProviders = new ArrayList<ArtifactMappingProvider>(
comment|//    applicationContext.getBeansOfType( ArtifactMappingProvider.class ).values() );
block|}
specifier|public
name|Maven2RepositoryPathTranslator
parameter_list|(
name|List
argument_list|<
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
name|StorageAsset
name|toFile
parameter_list|(
name|StorageAsset
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|String
name|filename
parameter_list|)
block|{
return|return
name|basedir
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|filename
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|toFile
parameter_list|(
name|StorageAsset
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
return|return
name|basedir
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toPath
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
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
argument_list|()
decl_stmt|;
name|appendNamespaceToProjectVersion
argument_list|(
name|path
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
expr_stmt|;
name|path
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
specifier|private
name|void
name|appendNamespaceToProjectVersion
parameter_list|(
name|StringBuilder
name|path
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
name|appendNamespaceAndProject
argument_list|(
name|path
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|)
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|appendNamespaceToProjectVersion
argument_list|(
name|path
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
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
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|appendNamespace
argument_list|(
name|path
argument_list|,
name|namespace
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
name|toPath
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|appendNamespaceAndProject
argument_list|(
name|path
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|)
expr_stmt|;
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|appendNamespaceAndProject
parameter_list|(
name|StringBuilder
name|path
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
name|appendNamespace
argument_list|(
name|path
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|projectId
argument_list|)
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
name|projectId
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendNamespace
parameter_list|(
name|StringBuilder
name|path
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
name|formatAsDirectory
argument_list|(
name|namespace
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|toFile
parameter_list|(
name|StorageAsset
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
return|return
name|basedir
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|namespace
argument_list|,
name|projectId
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|toFile
parameter_list|(
name|StorageAsset
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
return|return
name|basedir
operator|.
name|resolve
argument_list|(
name|toPath
argument_list|(
name|namespace
argument_list|)
argument_list|)
return|;
block|}
specifier|private
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
annotation|@
name|Override
specifier|public
name|ArtifactMetadata
name|getArtifactForPath
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|relativePath
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|relativePath
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|int
name|len
init|=
name|parts
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|len
operator|<
literal|4
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Not a valid artifact path in a Maven 2 repository, not enough directories: "
operator|+
name|relativePath
argument_list|)
throw|;
block|}
name|String
name|id
init|=
name|parts
index|[
operator|--
name|len
index|]
decl_stmt|;
name|String
name|baseVersion
init|=
name|parts
index|[
operator|--
name|len
index|]
decl_stmt|;
name|String
name|artifactId
init|=
name|parts
index|[
operator|--
name|len
index|]
decl_stmt|;
name|StringBuilder
name|groupIdBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|groupIdBuilder
operator|.
name|append
argument_list|(
name|parts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|groupIdBuilder
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|groupIdBuilder
operator|.
name|append
argument_list|(
name|parts
index|[
name|len
operator|-
literal|1
index|]
argument_list|)
expr_stmt|;
return|return
name|getArtifactFromId
argument_list|(
name|repoId
argument_list|,
name|groupIdBuilder
operator|.
name|toString
argument_list|()
argument_list|,
name|artifactId
argument_list|,
name|baseVersion
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ArtifactMetadata
name|getArtifactFromId
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
operator|!
name|id
operator|.
name|startsWith
argument_list|(
name|projectId
operator|+
literal|"-"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Not a valid artifact path in a Maven 2 repository, filename '"
operator|+
name|id
operator|+
literal|"' doesn't start with artifact ID '"
operator|+
name|projectId
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|MavenArtifactFacet
name|facet
init|=
operator|new
name|MavenArtifactFacet
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|projectId
operator|.
name|length
argument_list|()
operator|+
literal|1
decl_stmt|;
name|String
name|version
decl_stmt|;
name|String
name|idSubStrFromVersion
init|=
name|id
operator|.
name|substring
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|idSubStrFromVersion
operator|.
name|startsWith
argument_list|(
name|projectVersion
argument_list|)
operator|&&
operator|!
name|VersionUtil
operator|.
name|isUniqueSnapshot
argument_list|(
name|projectVersion
argument_list|)
condition|)
block|{
comment|// non-snapshot versions, or non-timestamped snapshot versions
name|version
operator|=
name|projectVersion
expr_stmt|;
block|}
if|else if
condition|(
name|VersionUtil
operator|.
name|isGenericSnapshot
argument_list|(
name|projectVersion
argument_list|)
condition|)
block|{
comment|// timestamped snapshots
try|try
block|{
name|int
name|mainVersionLength
init|=
name|projectVersion
operator|.
name|length
argument_list|()
operator|-
literal|8
decl_stmt|;
comment|// 8 is length of "SNAPSHOT"
if|if
condition|(
name|mainVersionLength
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Timestamped snapshots must contain the main version, filename was '"
operator|+
name|id
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|Matcher
name|m
init|=
name|TIMESTAMP_PATTERN
operator|.
name|matcher
argument_list|(
name|idSubStrFromVersion
operator|.
name|substring
argument_list|(
name|mainVersionLength
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|matches
argument_list|()
expr_stmt|;
name|String
name|timestamp
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|buildNumber
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|facet
operator|.
name|setTimestamp
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
name|facet
operator|.
name|setBuildNumber
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|buildNumber
argument_list|)
argument_list|)
expr_stmt|;
name|version
operator|=
name|idSubStrFromVersion
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|mainVersionLength
argument_list|)
operator|+
name|timestamp
operator|+
literal|"-"
operator|+
name|buildNumber
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Not a valid artifact path in a Maven 2 repository, filename '"
operator|+
name|id
operator|+
literal|"' doesn't contain a timestamped version matching snapshot '"
operator|+
name|projectVersion
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// invalid
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Not a valid artifact path in a Maven 2 repository, filename '"
operator|+
name|id
operator|+
literal|"' doesn't contain version '"
operator|+
name|projectVersion
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|String
name|classifier
decl_stmt|;
name|String
name|ext
decl_stmt|;
name|index
operator|+=
name|version
operator|.
name|length
argument_list|()
expr_stmt|;
if|if
condition|(
name|index
operator|==
name|id
operator|.
name|length
argument_list|()
condition|)
block|{
comment|// no classifier or extension
name|classifier
operator|=
literal|null
expr_stmt|;
name|ext
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|char
name|c
init|=
name|id
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'-'
condition|)
block|{
comment|// classifier up until '.'
name|int
name|extIndex
init|=
name|id
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|,
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|extIndex
operator|>=
literal|0
condition|)
block|{
name|classifier
operator|=
name|id
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|extIndex
argument_list|)
expr_stmt|;
name|ext
operator|=
name|id
operator|.
name|substring
argument_list|(
name|extIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|classifier
operator|=
name|id
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
name|ext
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|c
operator|==
literal|'.'
condition|)
block|{
comment|// rest is the extension
name|classifier
operator|=
literal|null
expr_stmt|;
name|ext
operator|=
name|id
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Not a valid artifact path in a Maven 2 repository, filename '"
operator|+
name|id
operator|+
literal|"' expected classifier or extension but got '"
operator|+
name|id
operator|.
name|substring
argument_list|(
name|index
argument_list|)
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
name|ArtifactMetadata
name|metadata
init|=
operator|new
name|ArtifactMetadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setProject
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setRepositoryId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setProjectVersion
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|facet
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
comment|// we use our own provider here instead of directly accessing Maven's artifact handlers as it has no way
comment|// to select the correct order to apply multiple extensions mappings to a preferred type
comment|// TODO: this won't allow the user to decide order to apply them if there are conflicts or desired changes -
comment|//       perhaps the plugins could register missing entries in configuration, then we just use configuration
comment|//       here?
name|String
name|type
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ArtifactMappingProvider
name|mapping
range|:
name|artifactMappingProviders
control|)
block|{
name|type
operator|=
name|mapping
operator|.
name|mapClassifierAndExtensionToType
argument_list|(
name|classifier
argument_list|,
name|ext
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
comment|// TODO: this is cheating! We should check the POM metadata instead
if|if
condition|(
name|type
operator|==
literal|null
operator|&&
literal|"jar"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|&&
name|isArtifactIdValidMavenPlugin
argument_list|(
name|projectId
argument_list|)
condition|)
block|{
name|type
operator|=
literal|"maven-plugin"
expr_stmt|;
block|}
comment|// use extension as default
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|ext
expr_stmt|;
block|}
comment|// TODO: should we allow this instead?
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Not a valid artifact path in a Maven 2 repository, filename '"
operator|+
name|id
operator|+
literal|"' does not have a type"
argument_list|)
throw|;
block|}
name|facet
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|addFacet
argument_list|(
name|facet
argument_list|)
expr_stmt|;
return|return
name|metadata
return|;
block|}
specifier|public
name|boolean
name|isArtifactIdValidMavenPlugin
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
return|return
name|MAVEN_PLUGIN_PATTERN
operator|.
name|matcher
argument_list|(
name|artifactId
argument_list|)
operator|.
name|matches
argument_list|()
return|;
block|}
block|}
end_class

end_unit

