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

begin_comment
comment|/**  * DefaultPathParser is a parser for maven 2 (default layout) paths to ArtifactReference.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *  * @plexus.component role="org.apache.maven.archiva.repository.content.PathParser" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultPathParser
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
comment|/**      * {@inheritDoc}      * @see org.apache.maven.archiva.repository.content.PathParser#toArtifactReference(java.lang.String)      */
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
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|path
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Unable to convert blank path."
argument_list|)
throw|;
block|}
name|ArtifactReference
name|artifact
init|=
operator|new
name|ArtifactReference
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
comment|/* Minimum parts.          *          *   path = "commons-lang/commons-lang/2.1/commons-lang-2.1.jar"          *   path[0] = "commons-lang";        // The Group ID          *   path[1] = "commons-lang";        // The Artifact ID          *   path[2] = "2.1";                 // The Version          *   path[3] = "commons-lang-2.1.jar" // The filename.          */
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
name|int
name|filenamePos
init|=
name|partCount
operator|-
literal|1
decl_stmt|;
name|int
name|baseVersionPos
init|=
name|partCount
operator|-
literal|2
decl_stmt|;
name|int
name|artifactIdPos
init|=
name|partCount
operator|-
literal|3
decl_stmt|;
name|int
name|groupIdPos
init|=
name|partCount
operator|-
literal|4
decl_stmt|;
comment|// Second to last is the baseVersion (the directory version)
name|String
name|baseVersion
init|=
name|pathParts
index|[
name|baseVersionPos
index|]
decl_stmt|;
comment|// Third to last is the artifact Id.
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|pathParts
index|[
name|artifactIdPos
index|]
argument_list|)
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
name|groupIdPos
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|artifact
operator|.
name|setGroupId
argument_list|(
name|pathParts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|artifact
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|"."
operator|+
name|pathParts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
comment|// Last part is the filename
name|String
name|filename
init|=
name|pathParts
index|[
name|filenamePos
index|]
decl_stmt|;
comment|// Now we need to parse the filename to get the artifact version Id.
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|filename
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
literal|"Unable to split blank filename."
argument_list|)
throw|;
block|}
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
name|filename
argument_list|)
decl_stmt|;
comment|// Expect the filename to start with the artifactId.
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|parser
operator|.
name|expect
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
literal|"filename format is invalid, "
operator|+
literal|"should start with artifactId as stated in path."
argument_list|)
throw|;
block|}
comment|// Process the version.
name|artifact
operator|.
name|setVersion
argument_list|(
name|parser
operator|.
name|expect
argument_list|(
name|baseVersion
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getVersion
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// We working with a snapshot?
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|baseVersion
argument_list|)
condition|)
block|{
name|artifact
operator|.
name|setVersion
argument_list|(
name|parser
operator|.
name|nextVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|VersionUtil
operator|.
name|isUniqueSnapshot
argument_list|(
name|artifact
operator|.
name|getVersion
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
literal|"filename format is invalid,"
operator|+
literal|"expected timestamp format in filename."
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
name|INVALID_ARTIFACT_PATH
operator|+
literal|"filename format is invalid, "
operator|+
literal|"expected version as stated in path."
argument_list|)
throw|;
block|}
block|}
comment|// Do we have a classifier?
switch|switch
condition|(
name|parser
operator|.
name|seperator
argument_list|()
condition|)
block|{
case|case
literal|'-'
case|:
comment|// Definately a classifier.
name|artifact
operator|.
name|setClassifier
argument_list|(
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set the type.
name|artifact
operator|.
name|setType
argument_list|(
name|ArtifactExtensionMapping
operator|.
name|mapExtensionAndClassifierToType
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'.'
case|:
comment|// We have an dual extension possibility.
name|String
name|extension
init|=
name|parser
operator|.
name|remaining
argument_list|()
operator|+
literal|'.'
operator|+
name|parser
operator|.
name|getExtension
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setType
argument_list|(
name|extension
argument_list|)
expr_stmt|;
break|break;
case|case
literal|0
case|:
comment|// End of the filename, only a simple extension left. - Set the type.
name|String
name|type
init|=
name|ArtifactExtensionMapping
operator|.
name|mapExtensionToType
argument_list|(
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid artifact: no type was specified"
argument_list|)
throw|;
block|}
name|artifact
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// Special case for maven plugins
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
name|ArtifactExtensionMapping
operator|.
name|isMavenPlugin
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
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
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
comment|// Sanity Checks.
comment|// Do we have a snapshot version?
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
comment|// Rules are different for SNAPSHOTS
if|if
condition|(
operator|!
name|VersionUtil
operator|.
name|isGenericSnapshot
argument_list|(
name|baseVersion
argument_list|)
condition|)
block|{
name|String
name|filenameBaseVersion
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
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid snapshot artifact location, version directory should be "
operator|+
name|filenameBaseVersion
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// Non SNAPSHOT rules.
comment|// Do we pass the simple test?
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|baseVersion
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid artifact: version declared in directory path does"
operator|+
literal|" not match what was found in the artifact filename."
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

