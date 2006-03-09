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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|model
operator|.
name|Model
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
name|model
operator|.
name|io
operator|.
name|xpp3
operator|.
name|MavenXpp3Reader
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
name|repository
operator|.
name|ArtifactUtils
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
name|io
operator|.
name|FileReader
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
comment|/**  * Artifact discoverer for the new repository layout (Maven 2.0+).  *  * @author John Casey  * @author Brett Porter  * @plexus.component role="org.apache.maven.repository.discovery.ArtifactDiscoverer" role-hint="org.apache.maven.repository.discovery.DefaultArtifactDiscoverer"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArtifactDiscoverer
extends|extends
name|AbstractArtifactDiscoverer
implements|implements
name|ArtifactDiscoverer
block|{
specifier|private
specifier|final
specifier|static
name|String
name|POM
init|=
literal|".pom"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|DELIM
init|=
literal|"\\"
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|public
name|List
name|discoverArtifacts
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|blacklistedPatterns
parameter_list|,
name|boolean
name|includeSnapshots
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"file"
operator|.
name|equals
argument_list|(
name|repository
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Only filesystem repositories are supported"
argument_list|)
throw|;
block|}
name|File
name|repositoryBase
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|)
decl_stmt|;
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
name|ArtifactUtils
operator|.
name|buildArtifact
argument_list|(
name|repositoryBase
argument_list|,
name|path
argument_list|,
name|repository
argument_list|,
name|artifactFactory
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
name|includeSnapshots
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
else|else
block|{
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|artifacts
return|;
block|}
specifier|public
name|List
name|discoverStandalonePoms
parameter_list|(
name|ArtifactRepository
name|repository
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
name|File
name|repositoryBase
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|)
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
if|if
condition|(
name|path
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
name|POM
argument_list|)
condition|)
block|{
name|Artifact
name|pomArtifact
init|=
name|buildArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|MavenXpp3Reader
name|mavenReader
init|=
operator|new
name|MavenXpp3Reader
argument_list|()
decl_stmt|;
name|String
name|filename
init|=
name|repositoryBase
operator|.
name|getAbsolutePath
argument_list|()
operator|+
name|DELIM
operator|+
name|path
decl_stmt|;
try|try
block|{
name|Model
name|model
init|=
name|mavenReader
operator|.
name|read
argument_list|(
operator|new
name|FileReader
argument_list|(
name|filename
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|model
operator|!=
literal|null
operator|)
operator|&&
operator|(
literal|"pom"
operator|.
name|equals
argument_list|(
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
comment|/*if ( ( pomArtifact != null )&& ( "pom".equals( model.getPackaging() ) ) )                     {                         if ( convertSnapshots || !pomArtifact.isSnapshot() )                         {                             artifacts.add( pomArtifact );                         }                     }                     */
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"error reading file: "
operator|+
name|filename
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
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
name|finalResult
init|=
literal|null
decl_stmt|;
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
block|}
else|else
block|{
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
block|}
else|else
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
init|=
literal|null
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
operator|<
literal|0
condition|)
block|{
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
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
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
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
comment|// version is XXX-SNAPSHOT, filename is XXX-yyyyMMdd.hhmmss-b
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
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
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
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|finalResult
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
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
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
operator|!=
literal|'-'
condition|)
block|{
name|addKickedOutPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
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
name|finalResult
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
block|}
else|else
block|{
name|finalResult
operator|=
name|result
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|finalResult
operator|!=
literal|null
condition|)
block|{
name|finalResult
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
block|}
return|return
name|finalResult
return|;
block|}
block|}
end_class

end_unit

