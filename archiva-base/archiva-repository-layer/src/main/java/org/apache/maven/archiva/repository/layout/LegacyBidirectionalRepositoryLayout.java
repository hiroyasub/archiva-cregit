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
name|LegacyArtifactExtensionMapping
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
comment|/**  * LegacyBidirectionalRepositoryLayout - the layout mechanism for use by Maven 1.x repositories.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role-hint="legacy"  */
end_comment

begin_class
specifier|public
class|class
name|LegacyBidirectionalRepositoryLayout
implements|implements
name|BidirectionalRepositoryLayout
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PATH_SEPARATOR
init|=
literal|"/"
decl_stmt|;
specifier|private
name|ArtifactExtensionMapping
name|extensionMapper
init|=
operator|new
name|LegacyArtifactExtensionMapping
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|typeToDirectoryMap
decl_stmt|;
specifier|public
name|LegacyBidirectionalRepositoryLayout
parameter_list|()
block|{
name|typeToDirectoryMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|typeToDirectoryMap
operator|.
name|put
argument_list|(
literal|"ejb-client"
argument_list|,
literal|"ejb"
argument_list|)
expr_stmt|;
name|typeToDirectoryMap
operator|.
name|put
argument_list|(
literal|"distribution-tgz"
argument_list|,
literal|"distribution"
argument_list|)
expr_stmt|;
name|typeToDirectoryMap
operator|.
name|put
argument_list|(
literal|"distribution-zip"
argument_list|,
literal|"distribution"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"legacy"
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
comment|// TODO: Verify type
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
literal|"metadata-xml"
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
name|groupId
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
name|getDirectory
argument_list|(
name|classifier
argument_list|,
name|type
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
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
literal|'-'
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
literal|'-'
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
literal|'.'
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
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|getDirectory
parameter_list|(
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
comment|// Special Cases involving classifiers and type.
if|if
condition|(
literal|"jar"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|&&
literal|"sources"
operator|.
name|equals
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
return|return
literal|"javadoc.jars"
return|;
block|}
comment|// Special Cases involving only type.
name|String
name|dirname
init|=
operator|(
name|String
operator|)
name|typeToDirectoryMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|dirname
operator|!=
literal|null
condition|)
block|{
return|return
name|dirname
operator|+
literal|"s"
return|;
block|}
comment|// Default process.
return|return
name|type
operator|+
literal|"s"
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
name|pathType
decl_stmt|;
specifier|public
name|String
name|type
decl_stmt|;
specifier|public
name|FilenameParts
name|fileParts
decl_stmt|;
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
comment|/* Always 3 parts. (Never more or less)          *           *   path = "commons-lang/jars/commons-lang-2.1.jar"          *   path[0] = "commons-lang";          // The Group ID          *   path[1] = "jars";                  // The Directory Type          *   path[2] = "commons-lang-2.1.jar";  // The Filename.          */
if|if
condition|(
name|pathParts
operator|.
name|length
operator|!=
literal|3
condition|)
block|{
comment|// Illegal Path Parts Length.
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid number of parts to the path ["
operator|+
name|path
operator|+
literal|"] to construct an ArchivaArtifact from. (Required to be 3 parts)"
argument_list|)
throw|;
block|}
comment|// The Group ID.
name|prefs
operator|.
name|groupId
operator|=
name|pathParts
index|[
literal|0
index|]
expr_stmt|;
comment|// The Expected Type.
name|prefs
operator|.
name|pathType
operator|=
name|pathParts
index|[
literal|1
index|]
expr_stmt|;
if|if
condition|(
name|parseFilename
condition|)
block|{
comment|// The Filename.
name|String
name|filename
init|=
name|pathParts
index|[
literal|2
index|]
decl_stmt|;
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
literal|null
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
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Cannot parse legacy paths to a Project Reference."
argument_list|)
throw|;
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
name|fileParts
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
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pathrefs
operator|.
name|fileParts
operator|.
name|extension
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid artifact, no extension."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|pathrefs
operator|.
name|pathType
operator|.
name|equals
argument_list|(
name|pathrefs
operator|.
name|fileParts
operator|.
name|extension
operator|+
literal|"s"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid artifact, mismatch on extension<"
operator|+
name|pathrefs
operator|.
name|fileParts
operator|.
name|extension
operator|+
literal|"> and layout specified type<"
operator|+
name|pathrefs
operator|.
name|pathType
operator|+
literal|">."
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

