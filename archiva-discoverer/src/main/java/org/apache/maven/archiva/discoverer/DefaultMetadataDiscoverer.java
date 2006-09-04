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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|ArtifactRepositoryMetadata
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
name|metadata
operator|.
name|GroupRepositoryMetadata
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
name|metadata
operator|.
name|Metadata
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
name|metadata
operator|.
name|RepositoryMetadata
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
name|metadata
operator|.
name|SnapshotArtifactRepositoryMetadata
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
name|metadata
operator|.
name|io
operator|.
name|xpp3
operator|.
name|MetadataXpp3Reader
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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|xml
operator|.
name|pull
operator|.
name|XmlPullParserException
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Iterator
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
comment|/**  * This class gets all the paths that contain the metadata files.  *  * @plexus.component role="org.apache.maven.archiva.discoverer.MetadataDiscoverer" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultMetadataDiscoverer
extends|extends
name|AbstractDiscoverer
implements|implements
name|MetadataDiscoverer
block|{
comment|/**      * Standard patterns to include in discovery of metadata files.      *      * @todo Note that only the remote format is supported at this time: you cannot search local repository metadata due      * to the way it is later loaded in the searchers. Review code using pathOfRemoteMetadata. IS there any value in      * searching the local metadata in the first place though?      */
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|STANDARD_DISCOVERY_INCLUDES
init|=
block|{
literal|"**/maven-metadata.xml"
block|}
decl_stmt|;
specifier|public
name|List
name|discoverMetadata
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|operation
parameter_list|,
name|List
name|blacklistedPatterns
parameter_list|)
throws|throws
name|DiscovererException
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
name|List
name|metadataFiles
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|List
name|metadataPaths
init|=
name|scanForArtifactPaths
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|)
argument_list|,
name|blacklistedPatterns
argument_list|,
name|STANDARD_DISCOVERY_INCLUDES
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|metadataPaths
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|metadataPath
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|RepositoryMetadata
name|metadata
init|=
name|buildMetadata
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|metadataPath
argument_list|)
decl_stmt|;
name|metadataFiles
operator|.
name|add
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DiscovererException
name|e
parameter_list|)
block|{
name|addKickedOutPath
argument_list|(
name|metadataPath
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|metadataFiles
return|;
block|}
specifier|private
name|RepositoryMetadata
name|buildMetadata
parameter_list|(
name|String
name|repo
parameter_list|,
name|String
name|metadataPath
parameter_list|)
throws|throws
name|DiscovererException
block|{
name|Metadata
name|m
decl_stmt|;
name|String
name|repoPath
init|=
name|repo
operator|+
literal|"/"
operator|+
name|metadataPath
decl_stmt|;
try|try
block|{
name|URL
name|url
init|=
operator|new
name|File
argument_list|(
name|repoPath
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|Reader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|MetadataXpp3Reader
name|metadataReader
init|=
operator|new
name|MetadataXpp3Reader
argument_list|()
decl_stmt|;
name|m
operator|=
name|metadataReader
operator|.
name|read
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XmlPullParserException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Error parsing metadata file '"
operator|+
name|repoPath
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|// shouldn't happen
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Error constructing metadata file '"
operator|+
name|repoPath
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Error reading metadata file '"
operator|+
name|repoPath
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|RepositoryMetadata
name|repositoryMetadata
init|=
name|buildMetadata
argument_list|(
name|m
argument_list|,
name|metadataPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|repositoryMetadata
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DiscovererException
argument_list|(
literal|"Unable to build a repository metadata from path"
argument_list|)
throw|;
block|}
return|return
name|repositoryMetadata
return|;
block|}
comment|/**      * Builds a RepositoryMetadata object from a Metadata object and its path.      *      * @param m            Metadata      * @param metadataPath path      * @return RepositoryMetadata if the parameters represent one; null if not      * @todo should we just be using the path information, and loading it later when it is needed? (for reporting, etc)      */
specifier|private
name|RepositoryMetadata
name|buildMetadata
parameter_list|(
name|Metadata
name|m
parameter_list|,
name|String
name|metadataPath
parameter_list|)
block|{
name|String
name|metaGroupId
init|=
name|m
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
name|String
name|metaArtifactId
init|=
name|m
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|String
name|metaVersion
init|=
name|m
operator|.
name|getVersion
argument_list|()
decl_stmt|;
comment|// check if the groupId, artifactId and version is in the
comment|// metadataPath
comment|// parse the path, in reverse order
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
name|metadataPath
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
comment|// remove the metadata file
name|pathParts
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|pathParts
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|tmpDir
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|metaVersion
argument_list|)
condition|)
block|{
name|artifact
operator|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|metaGroupId
argument_list|,
name|metaArtifactId
argument_list|,
name|metaVersion
argument_list|)
expr_stmt|;
block|}
comment|// snapshotMetadata
name|RepositoryMetadata
name|metadata
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tmpDir
operator|!=
literal|null
operator|&&
name|tmpDir
operator|.
name|equals
argument_list|(
name|metaVersion
argument_list|)
condition|)
block|{
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|metadata
operator|=
operator|new
name|SnapshotArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|tmpDir
operator|!=
literal|null
operator|&&
name|tmpDir
operator|.
name|equals
argument_list|(
name|metaArtifactId
argument_list|)
condition|)
block|{
comment|// artifactMetadata
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|metadata
operator|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|artifact
operator|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|metaGroupId
argument_list|,
name|metaArtifactId
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|metadata
operator|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|groupDir
init|=
literal|""
decl_stmt|;
name|int
name|ctr
init|=
literal|0
decl_stmt|;
for|for
control|(
name|it
operator|=
name|pathParts
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|path
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctr
operator|==
literal|0
condition|)
block|{
name|groupDir
operator|=
name|path
expr_stmt|;
block|}
else|else
block|{
name|groupDir
operator|=
name|path
operator|+
literal|"."
operator|+
name|groupDir
expr_stmt|;
block|}
name|ctr
operator|++
expr_stmt|;
block|}
comment|// groupMetadata
if|if
condition|(
name|metaGroupId
operator|!=
literal|null
operator|&&
name|metaGroupId
operator|.
name|equals
argument_list|(
name|groupDir
argument_list|)
condition|)
block|{
name|metadata
operator|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
name|metaGroupId
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|metadata
return|;
block|}
block|}
end_class

end_unit

