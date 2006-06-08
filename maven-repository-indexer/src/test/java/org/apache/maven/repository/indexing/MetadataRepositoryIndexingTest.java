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
name|ArtifactRepositoryFactory
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
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|Plugin
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
name|Versioning
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
name|RangeQuery
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
name|SinglePhraseQuery
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
name|PlexusTestCase
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

begin_comment
comment|/**  * This class tests the MetadataRepositoryIndex.  */
end_comment

begin_class
specifier|public
class|class
name|MetadataRepositoryIndexingTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|private
name|String
name|indexPath
decl_stmt|;
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * Set up.      *      * @throws Exception      */
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|File
name|repositoryDirectory
init|=
name|getTestFile
argument_list|(
literal|"src/test/repository"
argument_list|)
decl_stmt|;
name|String
name|repoDir
init|=
name|repositoryDirectory
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ArtifactRepositoryLayout
name|layout
init|=
operator|(
name|ArtifactRepositoryLayout
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|ROLE
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
name|ArtifactRepositoryFactory
name|repoFactory
init|=
operator|(
name|ArtifactRepositoryFactory
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|repository
operator|=
name|repoFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"test"
argument_list|,
name|repoDir
argument_list|,
name|layout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|indexPath
operator|=
literal|"target/index"
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexPath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tear down.      *      * @throws Exception      */
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|repository
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create the test index.      * Indexing process: check if the object was already indexed [ checkIfIndexed(Object) ], open the index [ open() ],      * index the object [ index(Object) ], optimize the index [ optimize() ] and close the index [ close() ].      *      * @throws Exception      */
specifier|private
name|void
name|createTestIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryIndexingFactory
name|factory
init|=
operator|(
name|RepositoryIndexingFactory
operator|)
name|lookup
argument_list|(
name|RepositoryIndexingFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|MetadataRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createMetadataRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|RepositoryMetadata
name|repoMetadata
init|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
literal|"org.apache.maven"
argument_list|)
decl_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|readMetadata
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|getArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.1"
argument_list|)
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|readMetadata
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|SnapshotArtifactRepositoryMetadata
argument_list|(
name|getArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.1"
argument_list|)
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|readMetadata
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|readMetadata
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test the ArtifactRepositoryIndex using a single-phrase search.      *      * @throws Exception      */
specifier|public
name|void
name|testSearch
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestIndex
argument_list|()
expr_stmt|;
name|RepositoryIndexingFactory
name|factory
init|=
operator|(
name|RepositoryIndexingFactory
operator|)
name|lookup
argument_list|(
name|RepositoryIndexingFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|RepositoryIndexSearchLayer
name|repoSearchLayer
init|=
operator|(
name|RepositoryIndexSearchLayer
operator|)
name|lookup
argument_list|(
name|RepositoryIndexSearchLayer
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|MetadataRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createMetadataRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
comment|// search last update
name|Query
name|qry
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_LASTUPDATE
argument_list|,
literal|"20051212044643"
argument_list|)
decl_stmt|;
name|List
name|metadataList
init|=
name|repoSearchLayer
operator|.
name|searchAdvanced
argument_list|(
name|qry
argument_list|,
name|indexer
argument_list|)
decl_stmt|;
comment|//assertEquals( 1, metadataList.size() );
for|for
control|(
name|Iterator
name|iter
init|=
name|metadataList
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryIndexSearchHit
name|hit
init|=
operator|(
name|RepositoryIndexSearchHit
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|hit
operator|.
name|isMetadata
argument_list|()
condition|)
block|{
name|RepositoryMetadata
name|repoMetadata
init|=
operator|(
name|RepositoryMetadata
operator|)
name|hit
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
name|repoMetadata
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
name|Versioning
name|versioning
init|=
name|metadata
operator|.
name|getVersioning
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"20051212044643"
argument_list|,
name|versioning
operator|.
name|getLastUpdated
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// search plugin prefix
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PLUGINPREFIX
argument_list|,
literal|"org.apache.maven"
argument_list|)
expr_stmt|;
name|metadataList
operator|=
name|repoSearchLayer
operator|.
name|searchAdvanced
argument_list|(
name|qry
argument_list|,
name|indexer
argument_list|)
expr_stmt|;
comment|//assertEquals( 1, metadataList.size() );
for|for
control|(
name|Iterator
name|iter
init|=
name|metadataList
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryIndexSearchHit
name|hit
init|=
operator|(
name|RepositoryIndexSearchHit
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|hit
operator|.
name|isMetadata
argument_list|()
condition|)
block|{
name|RepositoryMetadata
name|repoMetadata
init|=
operator|(
name|RepositoryMetadata
operator|)
name|hit
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
name|repoMetadata
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
name|List
name|plugins
init|=
name|metadata
operator|.
name|getPlugins
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|plugins
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
name|Plugin
name|plugin
init|=
operator|(
name|Plugin
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven"
argument_list|,
name|plugin
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// search last update using INCLUSIVE Range Query
name|Query
name|qry1
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_LASTUPDATE
argument_list|,
literal|"20051212000000"
argument_list|)
decl_stmt|;
name|Query
name|qry2
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_LASTUPDATE
argument_list|,
literal|"20051212235959"
argument_list|)
decl_stmt|;
name|RangeQuery
name|rQry
init|=
operator|new
name|RangeQuery
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|rQry
operator|.
name|addQuery
argument_list|(
name|qry1
argument_list|)
expr_stmt|;
name|rQry
operator|.
name|addQuery
argument_list|(
name|qry2
argument_list|)
expr_stmt|;
name|metadataList
operator|=
name|repoSearchLayer
operator|.
name|searchAdvanced
argument_list|(
name|rQry
argument_list|,
name|indexer
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|metadataList
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryIndexSearchHit
name|hit
init|=
operator|(
name|RepositoryIndexSearchHit
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|hit
operator|.
name|isMetadata
argument_list|()
condition|)
block|{
name|RepositoryMetadata
name|repoMetadata
init|=
operator|(
name|RepositoryMetadata
operator|)
name|hit
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
name|repoMetadata
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
name|Versioning
name|versioning
init|=
name|metadata
operator|.
name|getVersioning
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"20051212044643"
argument_list|,
name|versioning
operator|.
name|getLastUpdated
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// search last update using EXCLUSIVE Range Query
name|qry1
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_LASTUPDATE
argument_list|,
literal|"20051212000000"
argument_list|)
expr_stmt|;
name|qry2
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_LASTUPDATE
argument_list|,
literal|"20051212044643"
argument_list|)
expr_stmt|;
name|rQry
operator|=
operator|new
name|RangeQuery
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rQry
operator|.
name|addQuery
argument_list|(
name|qry1
argument_list|)
expr_stmt|;
name|rQry
operator|.
name|addQuery
argument_list|(
name|qry2
argument_list|)
expr_stmt|;
name|metadataList
operator|=
name|repoSearchLayer
operator|.
name|searchAdvanced
argument_list|(
name|rQry
argument_list|,
name|indexer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|metadataList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test the exceptions thrown by MetadataRepositoryIndex.      *      * @throws Exception      */
specifier|public
name|void
name|testExceptions
parameter_list|()
throws|throws
name|Exception
block|{
comment|//test when the object passed in the index(..) method is not a RepositoryMetadata instance
name|RepositoryIndexingFactory
name|factory
init|=
operator|(
name|RepositoryIndexingFactory
operator|)
name|lookup
argument_list|(
name|RepositoryIndexingFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|MetadataRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createMetadataRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
try|try
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.1"
argument_list|)
decl_stmt|;
name|indexer
operator|.
name|index
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must throw exception when the passed object is not a RepositoryMetadata object."
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|indexer
operator|.
name|deleteIfIndexed
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must throw exception when the passed object is not of type metadata."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test delete of document from metadata index.      *      * @throws Exception      */
specifier|public
name|void
name|testDeleteMetadataDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestIndex
argument_list|()
expr_stmt|;
name|RepositoryIndexingFactory
name|factory
init|=
operator|(
name|RepositoryIndexingFactory
operator|)
name|lookup
argument_list|(
name|RepositoryIndexingFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|RepositoryIndexSearcher
name|repoSearcher
init|=
operator|(
name|RepositoryIndexSearcher
operator|)
name|lookup
argument_list|(
name|RepositoryIndexSearcher
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|MetadataRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createMetadataRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|RepositoryMetadata
name|repoMetadata
init|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
literal|"org.apache.maven"
argument_list|)
decl_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|readMetadata
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|deleteDocument
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_ID
argument_list|,
operator|(
name|String
operator|)
name|repoMetadata
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|Query
name|qry
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_ID
argument_list|,
operator|(
name|String
operator|)
name|repoMetadata
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|List
name|metadataList
init|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|,
name|indexer
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|metadataList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create artifact object.      *      * @param groupId    the groupId of the artifact      * @param artifactId the artifactId of the artifact      * @param version    the version of the artifact      * @return Artifact      * @throws Exception      */
specifier|private
name|Artifact
name|getArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|artifactFactory
operator|==
literal|null
condition|)
block|{
name|artifactFactory
operator|=
operator|(
name|ArtifactFactory
operator|)
name|lookup
argument_list|(
name|ArtifactFactory
operator|.
name|ROLE
argument_list|)
expr_stmt|;
block|}
return|return
name|artifactFactory
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
return|;
block|}
comment|/**      * Create RepositoryMetadata object.      *      * @return RepositoryMetadata      */
specifier|private
name|Metadata
name|readMetadata
parameter_list|(
name|RepositoryMetadata
name|repoMetadata
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
block|}
end_class

end_unit

