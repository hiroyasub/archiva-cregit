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
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
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
name|IOUtil
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
name|Xpp3Dom
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
name|Xpp3DomBuilder
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
name|Xpp3DomWriter
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
name|FileNotFoundException
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
name|io
operator|.
name|FileWriter
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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Locale
import|;
end_import

begin_comment
comment|/**  * Base class for the artifact and metadata discoverers.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDiscoverer
extends|extends
name|AbstractLogEnabled
implements|implements
name|Discoverer
block|{
specifier|private
name|List
name|kickedOutPaths
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|protected
name|ArtifactFactory
name|artifactFactory
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
comment|/**      * @plexus.configuration default-value="60000"      */
specifier|private
name|int
name|blackoutPeriod
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|DATE_FMT
init|=
literal|"yyyyMMddHHmmss"
decl_stmt|;
comment|/**      * Add a path to the list of files that were kicked out due to being invalid.      *      * @param path   the path to add      * @param reason the reason why the path is being kicked out      */
specifier|protected
name|void
name|addKickedOutPath
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|kickedOutPaths
operator|.
name|add
argument_list|(
operator|new
name|DiscovererPath
argument_list|(
name|path
argument_list|,
name|reason
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns an iterator for the list if DiscovererPaths that were found to not represent a searched object      *      * @return Iterator for the DiscovererPath List      */
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
specifier|protected
name|List
name|scanForArtifactPaths
parameter_list|(
name|File
name|repositoryBase
parameter_list|,
name|List
name|blacklistedPatterns
parameter_list|,
name|String
index|[]
name|includes
parameter_list|,
name|String
index|[]
name|excludes
parameter_list|,
name|long
name|comparisonTimestamp
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
if|if
condition|(
name|excludes
operator|!=
literal|null
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
name|excludes
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|blacklistedPatterns
operator|!=
literal|null
condition|)
block|{
name|allExcludes
operator|.
name|addAll
argument_list|(
name|blacklistedPatterns
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
if|if
condition|(
name|includes
operator|!=
literal|null
condition|)
block|{
name|scanner
operator|.
name|setIncludes
argument_list|(
name|includes
argument_list|)
expr_stmt|;
block|}
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
comment|// TODO: Correct for extremely large repositories (artifact counts over 200,000 entries)
name|scanner
operator|.
name|scan
argument_list|()
expr_stmt|;
for|for
control|(
name|Iterator
name|files
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|scanner
operator|.
name|getExcludedFiles
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
init|;
name|files
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|path
init|=
name|files
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|excludedPaths
operator|.
name|add
argument_list|(
operator|new
name|DiscovererPath
argument_list|(
name|path
argument_list|,
literal|"Artifact was in the specified list of exclusions"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO: this could be a part of the scanner
name|List
name|includedPaths
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|files
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|scanner
operator|.
name|getIncludedFiles
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
init|;
name|files
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|path
init|=
name|files
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|long
name|modTime
init|=
operator|new
name|File
argument_list|(
name|repositoryBase
argument_list|,
name|path
argument_list|)
operator|.
name|lastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|modTime
operator|<
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|blackoutPeriod
condition|)
block|{
if|if
condition|(
name|modTime
operator|>
name|comparisonTimestamp
condition|)
block|{
name|includedPaths
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|includedPaths
return|;
block|}
comment|/**      * Returns an iterator for the list if DiscovererPaths that were not processed because they are explicitly excluded      *      * @return Iterator for the DiscovererPath List      */
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
specifier|protected
name|long
name|readComparisonTimestamp
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|operation
parameter_list|,
name|Xpp3Dom
name|dom
parameter_list|)
block|{
name|Xpp3Dom
name|entry
init|=
name|dom
operator|.
name|getChild
argument_list|(
name|operation
argument_list|)
decl_stmt|;
name|long
name|comparisonTimestamp
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|comparisonTimestamp
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
name|DATE_FMT
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|parse
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Timestamp was invalid: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|"; ignoring"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|comparisonTimestamp
return|;
block|}
specifier|protected
name|Xpp3Dom
name|readDom
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|Xpp3Dom
name|dom
decl_stmt|;
name|FileReader
name|fileReader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fileReader
operator|=
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|dom
operator|=
name|Xpp3DomBuilder
operator|.
name|build
argument_list|(
name|fileReader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
comment|// Safe to ignore
name|dom
operator|=
operator|new
name|Xpp3Dom
argument_list|(
literal|"metadata"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XmlPullParserException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Error reading metadata (ignoring and recreating): "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|dom
operator|=
operator|new
name|Xpp3Dom
argument_list|(
literal|"metadata"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Error reading metadata (ignoring and recreating): "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|dom
operator|=
operator|new
name|Xpp3Dom
argument_list|(
literal|"metadata"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|fileReader
argument_list|)
expr_stmt|;
block|}
return|return
name|dom
return|;
block|}
specifier|protected
name|Xpp3Dom
name|getLastArtifactDiscoveryDom
parameter_list|(
name|Xpp3Dom
name|dom
parameter_list|)
block|{
name|Xpp3Dom
name|lastDiscoveryDom
init|=
name|dom
operator|.
name|getChild
argument_list|(
literal|"lastArtifactDiscovery"
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastDiscoveryDom
operator|==
literal|null
condition|)
block|{
name|dom
operator|.
name|addChild
argument_list|(
operator|new
name|Xpp3Dom
argument_list|(
literal|"lastArtifactDiscovery"
argument_list|)
argument_list|)
expr_stmt|;
name|lastDiscoveryDom
operator|=
name|dom
operator|.
name|getChild
argument_list|(
literal|"lastArtifactDiscovery"
argument_list|)
expr_stmt|;
block|}
return|return
name|lastDiscoveryDom
return|;
block|}
specifier|protected
name|Xpp3Dom
name|getLastMetadataDiscoveryDom
parameter_list|(
name|Xpp3Dom
name|dom
parameter_list|)
block|{
name|Xpp3Dom
name|lastDiscoveryDom
init|=
name|dom
operator|.
name|getChild
argument_list|(
literal|"lastMetadataDiscovery"
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastDiscoveryDom
operator|==
literal|null
condition|)
block|{
name|dom
operator|.
name|addChild
argument_list|(
operator|new
name|Xpp3Dom
argument_list|(
literal|"lastMetadataDiscovery"
argument_list|)
argument_list|)
expr_stmt|;
name|lastDiscoveryDom
operator|=
name|dom
operator|.
name|getChild
argument_list|(
literal|"lastMetadataDiscovery"
argument_list|)
expr_stmt|;
block|}
return|return
name|lastDiscoveryDom
return|;
block|}
specifier|public
name|void
name|resetLastCheckedTime
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|operation
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TODO: get these changes into maven-metadata.xml and migrate towards that. The model is further diverging to a different layout at each level so submodels might be a good idea.
comment|// TODO: maven-artifact probably needs an improved pathOfMetadata to cope with top level metadata
comment|// TODO: might we need to write this as maven-metadata-local in some circumstances? merge others? Probably best to keep it simple and just use this format at the root. No need to merge anything that I can see
comment|// TODO: since this metadata isn't meant to be shared, perhaps another file is called for after all.
comment|// Format is:<repository><lastDiscovery><KEY>yyyyMMddHHmmss</KEY></lastDiscovery></repository> (ie, flat properties)
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"maven-metadata.xml"
argument_list|)
decl_stmt|;
name|Xpp3Dom
name|dom
init|=
name|readDom
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|removeEntry
argument_list|(
name|getLastArtifactDiscoveryDom
argument_list|(
name|dom
argument_list|)
argument_list|,
name|operation
argument_list|)
condition|)
block|{
name|changed
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|removeEntry
argument_list|(
name|getLastMetadataDiscoveryDom
argument_list|(
name|dom
argument_list|)
argument_list|,
name|operation
argument_list|)
condition|)
block|{
name|changed
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|changed
condition|)
block|{
name|saveDom
argument_list|(
name|file
argument_list|,
name|dom
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|removeEntry
parameter_list|(
name|Xpp3Dom
name|lastDiscoveryDom
parameter_list|,
name|String
name|operation
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
comment|// do this in reverse so that removing doesn't affect counter
name|Xpp3Dom
index|[]
name|children
init|=
name|lastDiscoveryDom
operator|.
name|getChildren
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|lastDiscoveryDom
operator|.
name|getChildCount
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
if|if
condition|(
name|children
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|changed
operator|=
literal|true
expr_stmt|;
name|lastDiscoveryDom
operator|.
name|removeChild
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|changed
return|;
block|}
specifier|protected
name|void
name|saveDom
parameter_list|(
name|File
name|file
parameter_list|,
name|Xpp3Dom
name|dom
parameter_list|)
throws|throws
name|IOException
block|{
name|FileWriter
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
decl_stmt|;
comment|// save metadata
try|try
block|{
name|Xpp3DomWriter
operator|.
name|write
argument_list|(
name|writer
argument_list|,
name|dom
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setEntry
parameter_list|(
name|Xpp3Dom
name|lastDiscoveryDom
parameter_list|,
name|String
name|operation
parameter_list|,
name|String
name|dateString
parameter_list|)
block|{
name|Xpp3Dom
name|entry
init|=
name|lastDiscoveryDom
operator|.
name|getChild
argument_list|(
name|operation
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
name|entry
operator|=
operator|new
name|Xpp3Dom
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|lastDiscoveryDom
operator|.
name|addChild
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|entry
operator|.
name|setValue
argument_list|(
name|dateString
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Xpp3Dom
name|readRepositoryMetadataDom
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
block|{
return|return
name|readDom
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"maven-metadata.xml"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

