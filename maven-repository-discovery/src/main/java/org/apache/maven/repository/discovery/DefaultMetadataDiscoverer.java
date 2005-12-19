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
name|FileNotFoundException
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
name|Arrays
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
name|DefaultArtifact
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
name|handler
operator|.
name|ArtifactHandler
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
name|handler
operator|.
name|DefaultArtifactHandler
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
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|versioning
operator|.
name|VersionRange
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
name|DirectoryScanner
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
name|FileUtils
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

begin_comment
comment|/**  * This class gets all the paths that contain the metadata files.  *   * @author Maria Odea Ching  */
end_comment

begin_class
specifier|public
class|class
name|DefaultMetadataDiscoverer
implements|implements
name|MetadataDiscoverer
block|{
comment|/** 	 * Standard patterns to exclude from discovery as they are not artifacts. 	 */
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|STANDARD_DISCOVERY_EXCLUDES
init|=
block|{
literal|"bin/**"
block|,
literal|"reports/**"
block|,
literal|".maven/**"
block|,
literal|"**/*.md5"
block|,
literal|"**/*.MD5"
block|,
literal|"**/*.sha1"
block|,
literal|"**/*.SHA1"
block|,
literal|"**/*snapshot-version"
block|,
literal|"*/website/**"
block|,
literal|"*/licenses/**"
block|,
literal|"*/licences/**"
block|,
literal|"**/.htaccess"
block|,
literal|"**/*.html"
block|,
literal|"**/*.asc"
block|,
literal|"**/*.txt"
block|,
literal|"**/README*"
block|,
literal|"**/CHANGELOG*"
block|,
literal|"**/KEYS*"
block|}
decl_stmt|;
comment|/** 	 * Standard patterns to include in discovery of metadata files. 	 */
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|STANDARD_DISCOVERY_INCLUDES
init|=
block|{
literal|"**/*-metadata.xml"
block|,
literal|"**/*/*-metadata.xml"
block|,
literal|"**/*/*/*-metadata.xml"
block|,
literal|"**/*-metadata-*.xml"
block|,
literal|"**/*/*-metadata-*.xml"
block|,
literal|"**/*/*/*-metadata-*.xml"
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|EMPTY_STRING_ARRAY
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
specifier|private
name|List
name|excludedPaths
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|List
name|kickedOutPaths
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/** 	 * Search the repository for metadata files. 	 *  	 * @param repositoryBase 	 * @param blacklistedPatterns 	 */
specifier|public
name|List
name|discoverMetadata
parameter_list|(
name|File
name|repositoryBase
parameter_list|,
name|String
name|blacklistedPatterns
parameter_list|)
block|{
name|List
name|metadataFiles
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|String
index|[]
name|metadataPaths
init|=
name|scanForMetadataPaths
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
name|metadataPaths
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|RepositoryMetadata
name|metadata
init|=
name|buildMetadata
argument_list|(
name|repositoryBase
operator|.
name|getPath
argument_list|()
argument_list|,
name|metadataPaths
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadata
operator|!=
literal|null
condition|)
name|metadataFiles
operator|.
name|add
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
else|else
name|kickedOutPaths
operator|.
name|add
argument_list|(
name|metadataPaths
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|metadataFiles
return|;
block|}
comment|/** 	 * Create RepositoryMetadata object. 	 *  	 * @param repo 	 *            The path to the repository. 	 * @param metadataPath 	 *            The path to the metadata file. 	 * @return 	 */
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
block|{
name|RepositoryMetadata
name|metadata
init|=
literal|null
decl_stmt|;
try|try
block|{
name|URL
name|url
init|=
operator|new
name|File
argument_list|(
name|repo
operator|+
literal|"/"
operator|+
name|metadataPath
argument_list|)
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
name|Metadata
name|m
init|=
name|metadataReader
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
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
name|ArtifactHandler
name|handler
init|=
operator|new
name|DefaultArtifactHandler
argument_list|(
literal|"jar"
argument_list|)
decl_stmt|;
name|VersionRange
name|version
init|=
name|VersionRange
operator|.
name|createFromVersion
argument_list|(
name|metaVersion
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|metaGroupId
argument_list|,
name|metaArtifactId
argument_list|,
name|version
argument_list|,
literal|"compile"
argument_list|,
literal|"jar"
argument_list|,
literal|""
argument_list|,
name|handler
argument_list|)
decl_stmt|;
comment|// snapshotMetadata
if|if
condition|(
name|tmpDir
operator|.
name|equals
argument_list|(
name|metaVersion
argument_list|)
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
if|else if
condition|(
name|tmpDir
operator|.
name|equals
argument_list|(
name|metaArtifactId
argument_list|)
condition|)
block|{
comment|// artifactMetadata
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
if|if
condition|(
name|ctr
operator|==
literal|0
condition|)
name|groupDir
operator|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
else|else
name|groupDir
operator|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
operator|+
literal|"."
operator|+
name|groupDir
expr_stmt|;
name|ctr
operator|++
expr_stmt|;
block|}
comment|// groupMetadata
if|if
condition|(
name|metaGroupId
operator|.
name|equals
argument_list|(
name|groupDir
argument_list|)
condition|)
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
catch|catch
parameter_list|(
name|FileNotFoundException
name|fe
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|XmlPullParserException
name|xe
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ie
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|metadata
return|;
block|}
comment|/** 	 * Scan or search for metadata files. 	 *  	 * @param repositoryBase 	 *            The repository directory. 	 * @param blacklistedPatterns 	 *            The patterns to be exluded from the search. 	 * @return 	 */
specifier|private
name|String
index|[]
name|scanForMetadataPaths
parameter_list|(
name|File
name|repositoryBase
parameter_list|,
name|String
name|blacklistedPatterns
parameter_list|)
block|{
name|List
name|allExcludes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|allExcludes
operator|.
name|addAll
argument_list|(
name|FileUtils
operator|.
name|getDefaultExcludesAsList
argument_list|()
argument_list|)
expr_stmt|;
name|allExcludes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|STANDARD_DISCOVERY_EXCLUDES
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|blacklistedPatterns
operator|!=
literal|null
operator|&&
name|blacklistedPatterns
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|allExcludes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|blacklistedPatterns
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|DirectoryScanner
name|scanner
init|=
operator|new
name|DirectoryScanner
argument_list|()
decl_stmt|;
name|scanner
operator|.
name|setBasedir
argument_list|(
name|repositoryBase
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|setIncludes
argument_list|(
name|STANDARD_DISCOVERY_INCLUDES
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|setExcludes
argument_list|(
operator|(
name|String
index|[]
operator|)
name|allExcludes
operator|.
name|toArray
argument_list|(
name|EMPTY_STRING_ARRAY
argument_list|)
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|scan
argument_list|()
expr_stmt|;
name|excludedPaths
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|scanner
operator|.
name|getExcludedFiles
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|scanner
operator|.
name|getIncludedFiles
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getExcludedPathsIterator
parameter_list|()
block|{
return|return
name|excludedPaths
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getKickedOutPathsIterator
parameter_list|()
block|{
return|return
name|kickedOutPaths
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
end_class

end_unit

