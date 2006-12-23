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
name|discoverer
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
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * Artifact discoverer for the new repository layout (Maven 2.0+).  *  * @author John Casey  * @author Brett Porter  * @plexus.component role="org.apache.maven.archiva.discoverer.ArtifactDiscoverer" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArtifactDiscoverer
extends|extends
name|AbstractArtifactDiscoverer
block|{
comment|/**      * @see org.apache.maven.archiva.discoverer.ArtifactDiscoverer#buildArtifact(String)      */
specifier|public
name|Artifact
name|buildArtifact
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|DiscovererException
block|{
name|List
name|pathParts
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|path
argument_list|,
literal|"/\\"
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|pathParts
operator|.
name|add
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|reverse
argument_list|(
name|pathParts
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
decl_stmt|;
if|if
condition|(
name|pathParts
operator|.
name|size
argument_list|()
operator|>=
literal|4
condition|)
block|{
comment|// maven 2.x path
comment|// the actual artifact filename.
name|String
name|filename
init|=
operator|(
name|String
operator|)
name|pathParts
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// the next one is the version.
name|String
name|version
init|=
operator|(
name|String
operator|)
name|pathParts
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// the next one is the artifactId.
name|String
name|artifactId
init|=
operator|(
name|String
operator|)
name|pathParts
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// the remaining are the groupId.
name|Collections
operator|.
name|reverse
argument_list|(
name|pathParts
argument_list|)
expr_stmt|;
name|String
name|groupId
init|=
name|StringUtils
operator|.
name|join
argument_list|(
name|pathParts
operator|.
name|iterator
argument_list|()
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|String
name|remainingFilename
init|=
name|filename
decl_stmt|;
if|if
condition|(
name|remainingFilename
operator|.
name|startsWith
argument_list|(
name|artifactId
operator|+
literal|"-"
argument_list|)
condition|)
block|{
name|remainingFilename
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
name|artifactId
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
comment|// TODO: use artifact handler, share with legacy discoverer
name|String
name|type
decl_stmt|;
if|if
condition|(
name|remainingFilename
operator|.
name|endsWith
argument_list|(
literal|".tar.gz"
argument_list|)
condition|)
block|{
name|type
operator|=
literal|"distribution-tgz"
expr_stmt|;
name|remainingFilename
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|remainingFilename
operator|.
name|length
argument_list|()
operator|-
literal|".tar.gz"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|remainingFilename
operator|.
name|endsWith
argument_list|(
literal|".zip"
argument_list|)
condition|)
block|{
name|type
operator|=
literal|"distribution-zip"
expr_stmt|;
name|remainingFilename
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|remainingFilename
operator|.
name|length
argument_list|()
operator|-
literal|".zip"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|remainingFilename
operator|.
name|endsWith
argument_list|(
literal|"-test-sources.jar"
argument_list|)
condition|)
block|{
name|type
operator|=
literal|"java-source"
expr_stmt|;
name|classifier
operator|=
literal|"test-sources"
expr_stmt|;
name|remainingFilename
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|remainingFilename
operator|.
name|length
argument_list|()
operator|-
literal|"-test-sources.jar"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|remainingFilename
operator|.
name|endsWith
argument_list|(
literal|"-sources.jar"
argument_list|)
condition|)
block|{
name|type
operator|=
literal|"java-source"
expr_stmt|;
name|classifier
operator|=
literal|"sources"
expr_stmt|;
name|remainingFilename
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|remainingFilename
operator|.
name|length
argument_list|()
operator|-
literal|"-sources.jar"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|index
init|=
name|remainingFilename
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|type
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
name|remainingFilename
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Path filename does not have an extension"
argument_list|)
throw|;
block|}
block|}
name|Artifact
name|result
decl_stmt|;
if|if
condition|(
name|classifier
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|artifactFactory
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|Artifact
operator|.
name|SCOPE_RUNTIME
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|artifactFactory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|.
name|isSnapshot
argument_list|()
condition|)
block|{
comment|// version is *-SNAPSHOT, filename is *-yyyyMMdd.hhmmss-b
name|int
name|classifierIndex
init|=
name|remainingFilename
operator|.
name|indexOf
argument_list|(
literal|'-'
argument_list|,
name|version
operator|.
name|length
argument_list|()
operator|+
literal|8
argument_list|)
decl_stmt|;
if|if
condition|(
name|classifierIndex
operator|>=
literal|0
condition|)
block|{
name|classifier
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
name|classifierIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|remainingFilename
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|classifierIndex
argument_list|)
expr_stmt|;
name|result
operator|=
name|artifactFactory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|remainingFilename
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|artifactFactory
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|remainingFilename
argument_list|,
name|Artifact
operator|.
name|SCOPE_RUNTIME
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|// poor encapsulation requires we do this to populate base version
if|if
condition|(
operator|!
name|result
operator|.
name|isSnapshot
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Failed to create a snapshot artifact: "
operator|+
name|result
argument_list|)
throw|;
block|}
if|else if
condition|(
operator|!
name|result
operator|.
name|getBaseVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Built snapshot artifact base version does not match path version: "
operator|+
name|result
operator|+
literal|"; should have been version: "
operator|+
name|version
argument_list|)
throw|;
block|}
else|else
block|{
name|artifact
operator|=
name|result
expr_stmt|;
block|}
block|}
if|else if
condition|(
operator|!
name|remainingFilename
operator|.
name|startsWith
argument_list|(
name|version
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Built artifact version does not match path version"
argument_list|)
throw|;
block|}
if|else if
condition|(
operator|!
name|remainingFilename
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
if|if
condition|(
name|remainingFilename
operator|.
name|charAt
argument_list|(
name|version
operator|.
name|length
argument_list|()
argument_list|)
operator|==
literal|'-'
condition|)
block|{
name|classifier
operator|=
name|remainingFilename
operator|.
name|substring
argument_list|(
name|version
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|artifactFactory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Path version does not corresspond to an artifact version"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|artifact
operator|=
name|result
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Path filename does not correspond to an artifact"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Path is too short to build an artifact from"
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

