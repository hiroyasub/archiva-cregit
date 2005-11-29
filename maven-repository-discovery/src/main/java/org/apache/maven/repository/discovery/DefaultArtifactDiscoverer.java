begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|discovery
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|factory
operator|.
name|ArtifactFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
comment|/**  * Artifact discoverer for the new repository layout (Maven 2.0+).  *  * @author John Casey  * @author Brett Porter  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArtifactDiscoverer
extends|extends
name|AbstractArtifactDiscoverer
block|{
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|public
name|List
name|discoverArtifacts
parameter_list|(
name|File
name|repositoryBase
parameter_list|,
name|String
name|blacklistedPatterns
parameter_list|,
name|boolean
name|convertSnapshots
parameter_list|)
block|{
name|List
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|String
index|[]
name|artifactPaths
init|=
name|scanForArtifactPaths
argument_list|(
name|repositoryBase
argument_list|,
name|blacklistedPatterns
argument_list|)
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
name|artifactPaths
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|path
init|=
name|artifactPaths
index|[
name|i
index|]
decl_stmt|;
name|Artifact
name|artifact
init|=
name|buildArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|convertSnapshots
operator|||
operator|!
name|artifact
operator|.
name|isSnapshot
argument_list|()
condition|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|artifacts
return|;
block|}
specifier|private
name|Artifact
name|buildArtifact
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|Artifact
name|result
decl_stmt|;
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
if|if
condition|(
name|pathParts
operator|.
name|size
argument_list|()
operator|<
literal|4
condition|)
block|{
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
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
literal|"jar"
argument_list|)
expr_stmt|;
name|String
name|remainingFilename
init|=
name|filename
decl_stmt|;
if|if
condition|(
operator|!
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
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
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
if|if
condition|(
name|result
operator|.
name|isSnapshot
argument_list|()
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
literal|"jar"
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResolvedVersion
argument_list|(
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
literal|4
argument_list|)
argument_list|)
expr_stmt|;
if|if
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
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|result
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

