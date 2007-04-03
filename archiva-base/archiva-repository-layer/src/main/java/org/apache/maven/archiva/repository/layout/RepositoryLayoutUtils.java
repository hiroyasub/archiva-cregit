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
comment|/**  * RepositoryLayoutUtils - utility methods common for most BidirectionalRepositoryLayout implementation.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryLayoutUtils
block|{
comment|/**      * Complex 2+ part extensions.      * Do not include initial "." character in extension names here.      */
specifier|private
specifier|static
specifier|final
name|String
name|ComplexExtensions
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"tar.gz"
block|,
literal|"tar.bz2"
block|}
decl_stmt|;
comment|/**      * These are the version patterns found in the filenames of the various artifact's versions IDs.      * These patterns are all tackling lowercase version IDs.      */
specifier|private
specifier|static
specifier|final
name|String
name|VersionPatterns
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"(snapshot)"
block|,
literal|"([0-9][_.0-9a-z]*)"
block|,
literal|"(g?[_.0-9ab]*(pre|rc|g|m)[_.0-9]*)"
block|,
literal|"(dev[_.0-9]*)"
block|,
literal|"(alpha[_.0-9]*)"
block|,
literal|"(beta[_.0-9]*)"
block|,
literal|"(rc[_.0-9]*)"
block|,
literal|"(test[_.0-9]*)"
block|,
literal|"(debug[_.0-9]*)"
block|,
literal|"(unofficial[_.0-9]*)"
block|,
literal|"(current)"
block|,
literal|"(latest)"
block|,
literal|"(fcs)"
block|,
literal|"(release[_.0-9]*)"
block|,
literal|"(nightly)"
block|,
literal|"(final)"
block|,
literal|"(incubating)"
block|,
literal|"(incubator)"
block|,
literal|"([ab][_.0-9]*)"
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VersionMegaPattern
init|=
name|StringUtils
operator|.
name|join
argument_list|(
name|VersionPatterns
argument_list|,
literal|'|'
argument_list|)
decl_stmt|;
comment|/**      * Filename Parsing Mode - Artifact Id.      */
specifier|private
specifier|static
specifier|final
name|int
name|ARTIFACTID
init|=
literal|1
decl_stmt|;
comment|/**      * Filename Parsing Mode - Version.      */
specifier|private
specifier|static
specifier|final
name|int
name|VERSION
init|=
literal|2
decl_stmt|;
comment|/**      * Filename Parsing Mode - Classifier.      */
specifier|private
specifier|static
specifier|final
name|int
name|CLASSIFIER
init|=
literal|3
decl_stmt|;
comment|/**      * Split the provided filename into 4 String parts.      *       *<pre>      * String part[] = splitFilename( filename );      * artifactId = part[0];      * version    = part[1];      * classifier = part[2];      * extension  = part[3];      *</pre>      *       * @param filename the filename to split.      * @param possibleArtifactId the optional artifactId to aide in splitting the filename.       *                  (null to allow algorithm to calculate one)      * @return the parts of the filename.      * @throws LayoutException       */
specifier|public
specifier|static
name|FilenameParts
name|splitFilename
parameter_list|(
name|String
name|filename
parameter_list|,
name|String
name|possibleArtifactId
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
name|filename
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to split blank filename."
argument_list|)
throw|;
block|}
name|String
name|filestring
init|=
name|filename
operator|.
name|trim
argument_list|()
decl_stmt|;
name|FilenameParts
name|parts
init|=
operator|new
name|FilenameParts
argument_list|()
decl_stmt|;
comment|// I like working backwards.
comment|// Find the extension.
comment|// Work on multipart extensions first.
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|String
name|lowercaseFilename
init|=
name|filestring
operator|.
name|toLowerCase
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
name|ComplexExtensions
operator|.
name|length
operator|&&
operator|!
name|found
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|lowercaseFilename
operator|.
name|endsWith
argument_list|(
literal|"."
operator|+
name|ComplexExtensions
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|parts
operator|.
name|extension
operator|=
name|ComplexExtensions
index|[
name|i
index|]
expr_stmt|;
name|filestring
operator|=
name|filestring
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|filestring
operator|.
name|length
argument_list|()
operator|-
name|ComplexExtensions
index|[
name|i
index|]
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
comment|// Default to 1 part extension.
name|int
name|index
init|=
name|filestring
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|<=
literal|0
condition|)
block|{
comment|// Bad Filename - No Extension
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Unable to determine extension from filename "
operator|+
name|filename
argument_list|)
throw|;
block|}
name|parts
operator|.
name|extension
operator|=
name|filestring
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
name|filestring
operator|=
name|filestring
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
comment|// Work on version string.
if|if
condition|(
operator|(
name|possibleArtifactId
operator|!=
literal|null
operator|)
operator|&&
name|filename
operator|.
name|startsWith
argument_list|(
name|possibleArtifactId
argument_list|)
condition|)
block|{
name|parts
operator|.
name|artifactId
operator|=
name|possibleArtifactId
expr_stmt|;
name|filestring
operator|=
name|filestring
operator|.
name|substring
argument_list|(
name|possibleArtifactId
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|fileParts
index|[]
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|filestring
argument_list|,
literal|'-'
argument_list|)
decl_stmt|;
name|int
name|versionStart
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|versionEnd
init|=
operator|-
literal|1
decl_stmt|;
name|Pattern
name|pat
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|VersionMegaPattern
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
name|Matcher
name|mat
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
name|fileParts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|part
init|=
name|fileParts
index|[
name|i
index|]
decl_stmt|;
name|mat
operator|=
name|pat
operator|.
name|matcher
argument_list|(
name|part
argument_list|)
expr_stmt|;
if|if
condition|(
name|mat
operator|.
name|matches
argument_list|()
condition|)
block|{
comment|// It is a potential verion part.
if|if
condition|(
name|versionStart
operator|<
literal|0
condition|)
block|{
name|versionStart
operator|=
name|i
expr_stmt|;
block|}
name|versionEnd
operator|=
name|i
expr_stmt|;
block|}
block|}
if|if
condition|(
name|versionStart
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Unable to determine version from filename "
operator|+
name|filename
argument_list|)
throw|;
block|}
comment|// Gather up the ArtifactID - Version - Classifier pieces found.
name|int
name|mode
init|=
name|ARTIFACTID
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
name|fileParts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|part
init|=
name|fileParts
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|(
name|mode
operator|==
name|ARTIFACTID
operator|)
operator|&&
operator|(
name|i
operator|>=
name|versionStart
operator|)
condition|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|parts
operator|.
name|artifactId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"No Artifact Id detected."
argument_list|)
throw|;
block|}
name|mode
operator|=
name|VERSION
expr_stmt|;
block|}
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|ARTIFACTID
case|:
name|parts
operator|.
name|appendArtifactId
argument_list|(
name|part
argument_list|)
expr_stmt|;
break|break;
case|case
name|VERSION
case|:
name|parts
operator|.
name|appendVersion
argument_list|(
name|part
argument_list|)
expr_stmt|;
break|break;
case|case
name|CLASSIFIER
case|:
name|parts
operator|.
name|appendClassifier
argument_list|(
name|part
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|i
operator|>=
name|versionEnd
condition|)
block|{
name|mode
operator|=
name|CLASSIFIER
expr_stmt|;
block|}
block|}
return|return
name|parts
return|;
block|}
block|}
end_class

end_unit

