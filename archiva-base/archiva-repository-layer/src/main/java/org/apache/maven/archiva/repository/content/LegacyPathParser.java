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
name|content
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
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
name|layout
operator|.
name|LayoutException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * LegacyPathParser is a parser for maven 1 (legacy layout) paths to  * ArtifactReference.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.repository.content.PathParser"  * role-hint="legacy"  */
end_comment

begin_class
specifier|public
class|class
name|LegacyPathParser
implements|implements
name|PathParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INVALID_ARTIFACT_PATH
init|=
literal|"Invalid path to Artifact: "
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|protected
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      * {@inheritDoc}      *      * @see org.apache.maven.archiva.repository.content.PathParser#toArtifactReference(java.lang.String)      */
specifier|public
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
name|ArtifactReference
name|artifact
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
comment|// First, look if a custom resolution rule has been set for this artifact
name|Collection
name|legacy
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getLegacyArtifactPaths
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|legacy
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|LegacyArtifactPath
name|legacyPath
init|=
operator|(
name|LegacyArtifactPath
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|legacyPath
operator|.
name|match
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|artifact
operator|.
name|setGroupId
argument_list|(
name|legacyPath
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|legacyPath
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setClassifier
argument_list|(
name|legacyPath
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|legacyPath
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setType
argument_list|(
name|legacyPath
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
block|}
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
name|INVALID_ARTIFACT_PATH
operator|+
literal|"legacy paths should only have 3 parts [groupId]/[type]s/[artifactId]-[version].[type], found "
operator|+
name|pathParts
operator|.
name|length
operator|+
literal|" instead."
argument_list|)
throw|;
block|}
comment|// The Group ID.
name|artifact
operator|.
name|setGroupId
argument_list|(
name|pathParts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
comment|// The Expected Type.
name|String
name|expectedType
init|=
name|pathParts
index|[
literal|1
index|]
decl_stmt|;
comment|// Sanity Check: expectedType should end in "s".
if|if
condition|(
operator|!
name|expectedType
operator|.
name|endsWith
argument_list|(
literal|"s"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
literal|"legacy paths should have an expected type ending in [s] in the second part of the path."
argument_list|)
throw|;
block|}
comment|// The Filename.
name|String
name|filename
init|=
name|pathParts
index|[
literal|2
index|]
decl_stmt|;
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|parser
operator|.
name|nextNonVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// Sanity Check: does it have an artifact id?
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
comment|// Special Case: The filename might start with a version id (like "test-arch-1.0.jar").
name|int
name|idx
init|=
name|filename
operator|.
name|indexOf
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|parser
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// Take the first section regardless of content.
name|String
name|artifactId
init|=
name|parser
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// Is there anything more that is considered not a version id?
name|String
name|moreArtifactId
init|=
name|parser
operator|.
name|nextNonVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|moreArtifactId
argument_list|)
condition|)
block|{
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|artifactId
operator|+
literal|"-"
operator|+
name|moreArtifactId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Sanity Check: still no artifact id?
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
literal|"no artifact id present."
argument_list|)
throw|;
block|}
block|}
name|artifact
operator|.
name|setVersion
argument_list|(
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
comment|// Sanity Check: does it have a version?
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
comment|// Special Case: use last section of artifactId as version.
name|String
name|artifactId
init|=
name|artifact
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|artifactId
operator|.
name|lastIndexOf
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|artifact
operator|.
name|setVersion
argument_list|(
name|artifactId
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|artifactId
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
literal|"no version found."
argument_list|)
throw|;
block|}
block|}
name|String
name|classifier
init|=
name|ArtifactClassifierMapping
operator|.
name|getClassifier
argument_list|(
name|expectedType
argument_list|)
decl_stmt|;
if|if
condition|(
name|classifier
operator|!=
literal|null
condition|)
block|{
name|String
name|version
init|=
name|artifact
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|version
operator|.
name|endsWith
argument_list|(
literal|"-"
operator|+
name|classifier
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
name|expectedType
operator|+
literal|" artifacts must use the classifier "
operator|+
name|classifier
argument_list|)
throw|;
block|}
name|version
operator|=
name|version
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|version
operator|.
name|length
argument_list|()
operator|-
name|classifier
operator|.
name|length
argument_list|()
operator|-
literal|1
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
block|}
name|String
name|extension
init|=
name|parser
operator|.
name|getExtension
argument_list|()
decl_stmt|;
comment|// Set Type
name|String
name|defaultExtension
init|=
name|expectedType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|expectedType
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setType
argument_list|(
name|ArtifactExtensionMapping
operator|.
name|mapExtensionAndClassifierToType
argument_list|(
name|classifier
argument_list|,
name|extension
argument_list|,
name|defaultExtension
argument_list|)
argument_list|)
expr_stmt|;
comment|// Sanity Check: does it have an extension?
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
literal|"no extension found."
argument_list|)
throw|;
block|}
comment|// Special Case with Maven Plugins
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|,
name|extension
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
literal|"plugins"
argument_list|,
name|expectedType
argument_list|)
condition|)
block|{
name|artifact
operator|.
name|setType
argument_list|(
name|ArtifactExtensionMapping
operator|.
name|MAVEN_PLUGIN
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Sanity Check: does extension match pathType on path?
name|String
name|expectedExtension
init|=
name|ArtifactExtensionMapping
operator|.
name|getExtension
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|expectedExtension
operator|.
name|equals
argument_list|(
name|extension
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
literal|"mismatch on extension ["
operator|+
name|extension
operator|+
literal|"] and layout specified type ["
operator|+
name|artifact
operator|.
name|getType
argument_list|()
operator|+
literal|"] (which maps to extension: ["
operator|+
name|expectedExtension
operator|+
literal|"]) on path ["
operator|+
name|path
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

