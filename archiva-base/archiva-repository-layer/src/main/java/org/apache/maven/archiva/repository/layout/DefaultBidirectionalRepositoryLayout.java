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
name|repository
operator|.
name|layout
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
name|maven
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
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|ArtifactExtensionMapping
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
name|content
operator|.
name|DefaultArtifactExtensionMapping
import|;
end_import

begin_comment
comment|/**  * DefaultBidirectionalRepositoryLayout - the layout mechanism for use by Maven 2.x repositories.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultBidirectionalRepositoryLayout
implements|implements
name|BidirectionalRepositoryLayout
block|{
specifier|private
specifier|static
specifier|final
name|char
name|PATH_SEPARATOR
init|=
literal|'/'
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
name|char
name|ARTIFACT_SEPARATOR
init|=
literal|'-'
decl_stmt|;
specifier|private
name|ArtifactExtensionMapping
name|extensionMapper
init|=
operator|new
name|DefaultArtifactExtensionMapping
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"default"
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
name|reference
operator|.
name|getBaseVersion
argument_list|()
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
specifier|public
name|String
name|toPath
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
block|{
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
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|artifact
parameter_list|)
block|{
name|String
name|baseVersion
init|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|toPath
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|baseVersion
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
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
name|StringBuffer
name|path
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|formatAsDirectory
argument_list|(
name|groupId
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
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
if|if
condition|(
name|baseVersion
operator|!=
literal|null
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
name|baseVersion
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
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
name|path
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
name|path
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
name|path
operator|.
name|append
argument_list|(
name|GROUP_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|extensionMapper
operator|.
name|getExtension
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|path
operator|.
name|toString
argument_list|()
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
class|class
name|PathReferences
block|{
specifier|public
name|String
name|groupId
decl_stmt|;
specifier|public
name|String
name|artifactId
decl_stmt|;
specifier|public
name|String
name|baseVersion
decl_stmt|;
specifier|public
name|String
name|type
decl_stmt|;
specifier|public
name|FilenameParts
name|fileParts
decl_stmt|;
specifier|public
name|void
name|appendGroupId
parameter_list|(
name|String
name|part
parameter_list|)
block|{
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
name|groupId
operator|=
name|part
expr_stmt|;
return|return;
block|}
name|groupId
operator|+=
literal|"."
operator|+
name|part
expr_stmt|;
block|}
block|}
specifier|private
name|PathReferences
name|toPathReferences
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|parseFilename
parameter_list|)
throws|throws
name|LayoutException
block|{
name|PathReferences
name|prefs
init|=
operator|new
name|PathReferences
argument_list|()
decl_stmt|;
name|String
name|normalizedPath
init|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|path
argument_list|,
literal|"\\"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|pathParts
index|[]
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|normalizedPath
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
comment|/* Minimum parts.          *           *   path = "commons-lang/commons-lang/2.1/commons-lang-2.1.jar"          *   path[0] = "commons-lang";        // The Group ID          *   path[1] = "commons-lang";        // The Artifact ID          *   path[2] = "2.1";                 // The Version          *   path[3] = "commons-lang-2.1.jar" // The filename.          */
if|if
condition|(
name|pathParts
operator|.
name|length
operator|<
literal|4
condition|)
block|{
comment|// Illegal Path Parts Length.
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Not enough parts to the path ["
operator|+
name|path
operator|+
literal|"] to construct an ArchivaArtifact from. (Requires at least 4 parts)"
argument_list|)
throw|;
block|}
comment|// Maven 2.x path.
name|int
name|partCount
init|=
name|pathParts
operator|.
name|length
decl_stmt|;
comment|// Second to last is the baseVersion (the directory version)
name|prefs
operator|.
name|baseVersion
operator|=
name|pathParts
index|[
name|partCount
operator|-
literal|2
index|]
expr_stmt|;
comment|// Third to last is the artifact Id.
name|prefs
operator|.
name|artifactId
operator|=
name|pathParts
index|[
name|partCount
operator|-
literal|3
index|]
expr_stmt|;
comment|// Remaining pieces are the groupId.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
name|partCount
operator|-
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|prefs
operator|.
name|appendGroupId
argument_list|(
name|pathParts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parseFilename
condition|)
block|{
comment|// Last part is the filename
name|String
name|filename
init|=
name|pathParts
index|[
name|partCount
operator|-
literal|1
index|]
decl_stmt|;
comment|// Now we need to parse the filename to get the artifact version Id.
name|prefs
operator|.
name|fileParts
operator|=
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
name|filename
argument_list|,
name|prefs
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|type
operator|=
name|extensionMapper
operator|.
name|getType
argument_list|(
name|filename
argument_list|)
expr_stmt|;
block|}
return|return
name|prefs
return|;
block|}
specifier|public
name|ProjectReference
name|toProjectReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
name|PathReferences
name|pathrefs
init|=
name|toPathReferences
argument_list|(
name|path
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ProjectReference
name|reference
init|=
operator|new
name|ProjectReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
name|pathrefs
operator|.
name|groupId
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|pathrefs
operator|.
name|artifactId
argument_list|)
expr_stmt|;
return|return
name|reference
return|;
block|}
specifier|public
name|ArchivaArtifact
name|toArtifact
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
name|PathReferences
name|pathrefs
init|=
name|toPathReferences
argument_list|(
name|path
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ArchivaArtifact
name|artifact
init|=
operator|new
name|ArchivaArtifact
argument_list|(
name|pathrefs
operator|.
name|groupId
argument_list|,
name|pathrefs
operator|.
name|artifactId
argument_list|,
name|pathrefs
operator|.
name|fileParts
operator|.
name|version
argument_list|,
name|pathrefs
operator|.
name|fileParts
operator|.
name|classifier
argument_list|,
name|pathrefs
operator|.
name|type
argument_list|)
decl_stmt|;
comment|// Sanity Checks.
name|String
name|artifactBaseVersion
init|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|pathrefs
operator|.
name|fileParts
operator|.
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactBaseVersion
operator|.
name|equals
argument_list|(
name|pathrefs
operator|.
name|baseVersion
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid artifact location, version directory and filename mismatch."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|pathrefs
operator|.
name|artifactId
operator|.
name|equals
argument_list|(
name|pathrefs
operator|.
name|fileParts
operator|.
name|artifactId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid artifact Id"
argument_list|)
throw|;
block|}
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

