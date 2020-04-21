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
name|maven
operator|.
name|MavenMetadataReader
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
name|ArchivaRepositoryMetadata
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
name|SnapshotVersion
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
name|metadata
operator|.
name|RepositoryMetadataException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
comment|/**  * Helper class that contains certain maven specific methods  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"MavenContentHelper"
argument_list|)
specifier|public
class|class
name|MavenContentHelper
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
name|MavenContentHelper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Pattern
name|UNIQUE_SNAPSHOT_NUMBER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^([0-9]{8}\\.[0-9]{6}-[0-9]+)(.*)"
argument_list|)
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"metadataReader#maven"
argument_list|)
name|MavenMetadataReader
name|metadataReader
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|METADATA_FILENAME
init|=
literal|"maven-metadata.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|METADATA_REPOSITORY_FILENAME
init|=
literal|"maven-metadata-repository.xml"
decl_stmt|;
specifier|public
name|MavenContentHelper
parameter_list|()
block|{
block|}
specifier|public
name|void
name|setMetadataReader
parameter_list|(
name|MavenMetadataReader
name|metadataReader
parameter_list|)
block|{
name|this
operator|.
name|metadataReader
operator|=
name|metadataReader
expr_stmt|;
block|}
comment|/**      * Returns the namespace string for a given path in the repository      *      * @param namespacePath the path to the namespace in the directory      * @return the namespace string that matches the given path.      */
specifier|public
specifier|static
name|String
name|getNamespaceFromNamespacePath
parameter_list|(
specifier|final
name|StorageAsset
name|namespacePath
parameter_list|)
block|{
name|LinkedList
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|( )
decl_stmt|;
name|StorageAsset
name|current
init|=
name|namespacePath
decl_stmt|;
while|while
condition|(
name|current
operator|.
name|hasParent
argument_list|()
condition|)
block|{
name|names
operator|.
name|addFirst
argument_list|(
name|current
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|current
operator|=
name|current
operator|.
name|getParent
argument_list|( )
expr_stmt|;
block|}
return|return
name|String
operator|.
name|join
argument_list|(
literal|"."
argument_list|,
name|names
argument_list|)
return|;
block|}
comment|/**      * Returns the artifact version for the given artifact directory and the item selector      */
specifier|public
name|String
name|getArtifactVersion
parameter_list|(
name|StorageAsset
name|artifactDir
parameter_list|,
name|ItemSelector
name|selector
parameter_list|)
block|{
if|if
condition|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|()
condition|)
block|{
return|return
name|selector
operator|.
name|getArtifactVersion
argument_list|()
return|;
block|}
if|else if
condition|(
name|selector
operator|.
name|hasVersion
argument_list|()
condition|)
block|{
if|if
condition|(
name|VersionUtil
operator|.
name|isGenericSnapshot
argument_list|(
name|selector
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|getLatestArtifactSnapshotVersion
argument_list|(
name|artifactDir
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|selector
operator|.
name|getVersion
argument_list|( )
return|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No version set on the selector "
argument_list|)
throw|;
block|}
block|}
comment|/**      *      * Returns the latest snapshot version that is referenced by the metadata file.      *      * @param artifactDir the directory of the artifact      * @param snapshotVersion the generic snapshot version (must end with '-SNAPSHOT')      * @return the real version from the metadata      */
specifier|public
name|String
name|getLatestArtifactSnapshotVersion
parameter_list|(
name|StorageAsset
name|artifactDir
parameter_list|,
name|String
name|snapshotVersion
parameter_list|)
block|{
specifier|final
name|StorageAsset
name|metadataFile
init|=
name|artifactDir
operator|.
name|resolve
argument_list|(
name|METADATA_FILENAME
argument_list|)
decl_stmt|;
name|StringBuilder
name|version
init|=
operator|new
name|StringBuilder
argument_list|( )
decl_stmt|;
try|try
block|{
name|ArchivaRepositoryMetadata
name|metadata
init|=
name|metadataReader
operator|.
name|read
argument_list|(
name|metadataFile
argument_list|)
decl_stmt|;
comment|// re-adjust to timestamp if present, otherwise retain the original -SNAPSHOT filename
name|SnapshotVersion
name|metadataVersion
init|=
name|metadata
operator|.
name|getSnapshotVersion
argument_list|( )
decl_stmt|;
if|if
condition|(
name|metadataVersion
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|metadataVersion
operator|.
name|getTimestamp
argument_list|( )
argument_list|)
condition|)
block|{
name|version
operator|.
name|append
argument_list|(
name|snapshotVersion
argument_list|,
literal|0
argument_list|,
name|snapshotVersion
operator|.
name|length
argument_list|( )
operator|-
literal|8
argument_list|)
expr_stmt|;
comment|// remove SNAPSHOT from end
name|version
operator|.
name|append
argument_list|(
name|metadataVersion
operator|.
name|getTimestamp
argument_list|( )
argument_list|)
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
operator|.
name|append
argument_list|(
name|metadataVersion
operator|.
name|getBuildNumber
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|version
operator|.
name|toString
argument_list|( )
return|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
comment|// unable to parse metadata - LOGGER it, and continue with the version as the original SNAPSHOT version
name|log
operator|.
name|warn
argument_list|(
literal|"Invalid metadata: {} - {}"
argument_list|,
name|metadataFile
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|baseVersion
init|=
name|StringUtils
operator|.
name|removeEnd
argument_list|(
name|snapshotVersion
argument_list|,
literal|"-SNAPSHOT"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|prefix
init|=
name|metadataFile
operator|.
name|getParent
argument_list|( )
operator|.
name|getParent
argument_list|( )
operator|.
name|getName
argument_list|( )
operator|+
literal|"-"
operator|+
name|baseVersion
operator|+
literal|"-"
decl_stmt|;
return|return
name|artifactDir
operator|.
name|list
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getName
argument_list|( )
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|a
lambda|->
name|StringUtils
operator|.
name|removeStart
argument_list|(
name|a
operator|.
name|getName
argument_list|( )
argument_list|,
name|prefix
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|n
lambda|->
name|UNIQUE_SNAPSHOT_NUMBER_PATTERN
operator|.
name|matcher
argument_list|(
name|n
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|m
lambda|->
name|m
operator|.
name|matches
argument_list|( )
argument_list|)
operator|.
name|map
argument_list|(
name|m
lambda|->
name|baseVersion
operator|+
literal|"-"
operator|+
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|sorted
argument_list|(
name|Comparator
operator|.
name|reverseOrder
argument_list|()
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|orElse
argument_list|(
name|snapshotVersion
argument_list|)
return|;
block|}
comment|/**      * Returns a artifact filename that corresponds to the given data.      * @param artifactId the selector data      * @param artifactVersion the artifactVersion      * @param classifier the artifact classifier      * @param extension the file extension      */
specifier|static
name|String
name|getArtifactFileName
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|artifactVersion
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|extension
parameter_list|)
block|{
name|StringBuilder
name|fileName
init|=
operator|new
name|StringBuilder
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
decl_stmt|;
name|fileName
operator|.
name|append
argument_list|(
name|artifactVersion
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|fileName
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
name|fileName
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|extension
argument_list|)
expr_stmt|;
return|return
name|fileName
operator|.
name|toString
argument_list|( )
return|;
block|}
comment|/**      * Returns the classifier for a given selector. If the selector has no classifier, but      * a type set. The classifier is generated from the type.      *      * @param selector the artifact selector      * @return the classifier or empty string if no classifier was found      */
specifier|static
name|String
name|getClassifier
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
block|{
if|if
condition|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
condition|)
block|{
return|return
name|selector
operator|.
name|getClassifier
argument_list|( )
return|;
block|}
if|else if
condition|(
name|selector
operator|.
name|hasType
argument_list|( )
condition|)
block|{
return|return
name|getClassifierFromType
argument_list|(
name|selector
operator|.
name|getType
argument_list|( )
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
comment|/**      * Returns a classifier for a given type. It returns only classifier for the maven default types      * that are known.      *      * @param type the type of the artifact      * @return the classifier if one was found, otherwise a empty string      */
specifier|static
name|String
name|getClassifierFromType
parameter_list|(
specifier|final
name|String
name|type
parameter_list|)
block|{
name|String
name|testType
init|=
name|type
operator|.
name|trim
argument_list|( )
operator|.
name|toLowerCase
argument_list|( )
decl_stmt|;
switch|switch
condition|(
name|testType
operator|.
name|length
argument_list|( )
condition|)
block|{
case|case
literal|7
case|:
if|if
condition|(
literal|"javadoc"
operator|.
name|equals
argument_list|(
name|testType
argument_list|)
condition|)
block|{
return|return
literal|"javadoc"
return|;
block|}
case|case
literal|8
case|:
if|if
condition|(
literal|"test-jar"
operator|.
name|equals
argument_list|(
name|testType
argument_list|)
condition|)
block|{
return|return
literal|"tests"
return|;
block|}
case|case
literal|10
case|:
if|if
condition|(
literal|"ejb-client"
operator|.
name|equals
argument_list|(
name|testType
argument_list|)
condition|)
block|{
return|return
literal|"client"
return|;
block|}
case|case
literal|11
case|:
if|if
condition|(
literal|"java-source"
operator|.
name|equals
argument_list|(
name|testType
argument_list|)
condition|)
block|{
return|return
literal|"sources"
return|;
block|}
default|default:
return|return
literal|""
return|;
block|}
block|}
comment|/**      * Returns the type that matches the given classifier and extension      *      * @param classifierArg the classifier      * @param extensionArg the extension      * @return the type that matches the combination of classifier and extension      */
specifier|static
name|String
name|getTypeFromClassifierAndExtension
parameter_list|(
name|String
name|classifierArg
parameter_list|,
name|String
name|extensionArg
parameter_list|)
block|{
name|String
name|extension
init|=
name|extensionArg
operator|.
name|toLowerCase
argument_list|( )
operator|.
name|trim
argument_list|( )
decl_stmt|;
name|String
name|classifier
init|=
name|classifierArg
operator|.
name|toLowerCase
argument_list|( )
operator|.
name|trim
argument_list|( )
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|extension
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
if|else if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
return|return
name|extension
return|;
block|}
if|else if
condition|(
name|classifier
operator|.
name|equals
argument_list|(
literal|"tests"
argument_list|)
operator|&&
name|extension
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
return|return
literal|"test-jar"
return|;
block|}
if|else if
condition|(
name|classifier
operator|.
name|equals
argument_list|(
literal|"client"
argument_list|)
operator|&&
name|extension
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
return|return
literal|"ejb-client"
return|;
block|}
if|else if
condition|(
name|classifier
operator|.
name|equals
argument_list|(
literal|"sources"
argument_list|)
operator|&&
name|extension
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
return|return
literal|"java-source"
return|;
block|}
if|else if
condition|(
name|classifier
operator|.
name|equals
argument_list|(
literal|"javadoc"
argument_list|)
operator|&&
name|extension
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
return|return
literal|"javadoc"
return|;
block|}
else|else
block|{
return|return
name|extension
return|;
block|}
block|}
comment|/**      * If the selector defines a type and no extension, the extension can be derived from      * the type.      *      * @param selector the item selector      * @return the extension that matches the type or the default extension "jar" if the type is not known      */
specifier|static
name|String
name|getArtifactExtension
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
block|{
if|if
condition|(
name|selector
operator|.
name|hasExtension
argument_list|( )
condition|)
block|{
return|return
name|selector
operator|.
name|getExtension
argument_list|( )
return|;
block|}
if|else if
condition|(
name|selector
operator|.
name|hasType
argument_list|( )
condition|)
block|{
specifier|final
name|String
name|type
init|=
name|selector
operator|.
name|getType
argument_list|( )
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|( )
decl_stmt|;
switch|switch
condition|(
name|type
operator|.
name|length
argument_list|()
condition|)
block|{
case|case
literal|3
case|:
if|if
condition|(
literal|"pom"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
literal|"war"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
literal|"ear"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
literal|"rar"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|type
return|;
block|}
default|default:
return|return
literal|"jar"
return|;
block|}
block|}
else|else
block|{
return|return
literal|"jar"
return|;
block|}
block|}
block|}
end_class

end_unit
