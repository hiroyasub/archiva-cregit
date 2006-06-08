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
name|indexing
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
name|lucene
operator|.
name|document
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|queryParser
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Hits
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|IndexSearcher
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
name|indexing
operator|.
name|query
operator|.
name|Query
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
name|IOException
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
name|HashMap
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
name|Map
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
comment|/**  * Implementation Class for searching through the index.  *  * @plexus.component role="org.apache.maven.repository.indexing.RepositoryIndexSearcher"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRepositoryIndexSearcher
extends|extends
name|AbstractLogEnabled
implements|implements
name|RepositoryIndexSearcher
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|factory
decl_stmt|;
specifier|public
name|List
name|search
parameter_list|(
name|Query
name|query
parameter_list|,
name|RepositoryIndex
name|index
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
block|{
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Query
name|luceneQuery
decl_stmt|;
try|try
block|{
name|luceneQuery
operator|=
name|query
operator|.
name|createLuceneQuery
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to construct query: "
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
name|IndexSearcher
name|searcher
decl_stmt|;
try|try
block|{
name|searcher
operator|=
operator|new
name|IndexSearcher
argument_list|(
name|index
operator|.
name|getIndexPath
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to open index: "
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
name|List
name|docs
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
try|try
block|{
name|Hits
name|hits
init|=
name|searcher
operator|.
name|search
argument_list|(
name|luceneQuery
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
name|hits
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Document
name|doc
init|=
name|hits
operator|.
name|doc
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|docs
operator|.
name|add
argument_list|(
name|createSearchedObjectFromIndexDocument
argument_list|(
name|doc
argument_list|,
name|index
operator|.
name|getRepository
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to search index: "
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
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to search index: "
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
finally|finally
block|{
try|try
block|{
name|searcher
operator|.
name|close
argument_list|()
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
literal|"Unable to close index searcher"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|docs
return|;
block|}
comment|/**      * Method for creating the object to be returned for the search      *      * @param doc        the index document where the object field values will be retrieved from      * @param repository      * @return Object      */
specifier|protected
name|RepositoryIndexSearchHit
name|createSearchedObjectFromIndexDocument
parameter_list|(
name|Document
name|doc
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
block|{
name|RepositoryIndexSearchHit
name|searchHit
init|=
literal|null
decl_stmt|;
comment|// the document is of type artifact
name|String
name|groupId
init|=
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_GROUPID
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_ARTIFACTID
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_VERSION
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_DOCTYPE
argument_list|)
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|ARTIFACT
argument_list|)
condition|)
block|{
name|String
name|packaging
init|=
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PACKAGING
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|factory
operator|.
name|createBuildArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|packaging
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RepositoryIndex
operator|.
name|ARTIFACT
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_CLASSES
argument_list|,
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_CLASSES
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PACKAGES
argument_list|,
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PACKAGES
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_FILES
argument_list|,
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_FILES
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_MD5
argument_list|,
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_MD5
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_SHA1
argument_list|,
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_SHA1
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PACKAGING
argument_list|,
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PACKAGING
argument_list|)
argument_list|)
expr_stmt|;
name|searchHit
operator|=
operator|new
name|RepositoryIndexSearchHit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|searchHit
operator|.
name|setObject
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|// the document is of type model
if|else if
condition|(
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_DOCTYPE
argument_list|)
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|POM
argument_list|)
condition|)
block|{
name|Artifact
name|pomArtifact
init|=
name|factory
operator|.
name|createProjectArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|searchHit
operator|=
operator|new
name|RepositoryIndexSearchHit
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|searchHit
operator|.
name|setObject
argument_list|(
name|readPom
argument_list|(
name|pomArtifact
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// the document is of type metadata
if|else if
condition|(
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_DOCTYPE
argument_list|)
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|METADATA
argument_list|)
condition|)
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
name|doc
operator|.
name|get
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_NAME
argument_list|)
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
name|String
name|tmpDir
init|=
operator|(
name|String
operator|)
name|pathParts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|RepositoryMetadata
name|repoMetadata
decl_stmt|;
if|if
condition|(
name|tmpDir
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|repoMetadata
operator|=
operator|new
name|SnapshotArtifactRepositoryMetadata
argument_list|(
name|factory
operator|.
name|createBuildArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|tmpDir
operator|.
name|equals
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
name|repoMetadata
operator|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|factory
operator|.
name|createBuildArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|repoMetadata
operator|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
block|}
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|readMetadata
argument_list|(
name|repoMetadata
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
name|searchHit
operator|=
operator|new
name|RepositoryIndexSearchHit
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|searchHit
operator|.
name|setObject
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
block|}
return|return
name|searchHit
return|;
block|}
comment|/**      * Create RepositoryMetadata object.      *      * @return RepositoryMetadata      */
specifier|private
name|Metadata
name|readMetadata
parameter_list|(
name|RepositoryMetadata
name|repoMetadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
block|{
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
name|repository
operator|.
name|pathOfRemoteRepositoryMetadata
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
decl_stmt|;
name|MetadataXpp3Reader
name|metadataReader
init|=
operator|new
name|MetadataXpp3Reader
argument_list|()
decl_stmt|;
name|FileReader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reader
operator|=
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|metadataReader
operator|.
name|read
argument_list|(
name|reader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to find metadata file: "
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
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to read metadata file: "
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
name|XmlPullParserException
name|xe
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to parse metadata file: "
operator|+
name|xe
operator|.
name|getMessage
argument_list|()
argument_list|,
name|xe
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create RepositoryMetadata object.      *      * @return RepositoryMetadata      */
specifier|private
name|Model
name|readPom
parameter_list|(
name|Artifact
name|pomArtifact
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
block|{
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
name|repository
operator|.
name|pathOf
argument_list|(
name|pomArtifact
argument_list|)
argument_list|)
decl_stmt|;
name|MavenXpp3Reader
name|r
init|=
operator|new
name|MavenXpp3Reader
argument_list|()
decl_stmt|;
name|FileReader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reader
operator|=
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|read
argument_list|(
name|reader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to find requested POM: "
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
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to read POM: "
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
name|XmlPullParserException
name|xe
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexSearchException
argument_list|(
literal|"Unable to parse POM: "
operator|+
name|xe
operator|.
name|getMessage
argument_list|()
argument_list|,
name|xe
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

