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
name|DateField
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
name|QueryParser
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
name|repository
operator|.
name|digest
operator|.
name|DefaultDigester
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
name|digest
operator|.
name|Digester
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|EclipseRepositoryIndexTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|private
name|String
name|indexPath
decl_stmt|;
specifier|private
name|Digester
name|digester
decl_stmt|;
specifier|private
name|long
name|artifactFileTime
decl_stmt|;
specifier|protected
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
name|digester
operator|=
operator|new
name|DefaultDigester
argument_list|()
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
comment|/**      * Create an index that will be used for testing.      * Indexing process: check if the object was already indexed [ checkIfIndexed(Object) ], open the index [ open() ],      * index the object [ index(Object) ], optimize the index [ optimize() ] and close the index [ close() ].      *      * @throws Exception      */
specifier|private
name|EclipseRepositoryIndex
name|createTestIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|EclipseRepositoryIndex
name|indexer
init|=
operator|new
name|EclipseRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|,
operator|new
name|DefaultDigester
argument_list|()
argument_list|)
decl_stmt|;
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
name|artifactFileTime
operator|=
name|artifact
operator|.
name|getFile
argument_list|()
operator|.
name|lastModified
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
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
name|long
name|historicTime
init|=
name|artifactFileTime
operator|-
literal|10000L
decl_stmt|;
name|artifact
operator|=
name|getArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
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
name|artifact
operator|.
name|getFile
argument_list|()
operator|.
name|setLastModified
argument_list|(
name|historicTime
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
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
name|artifact
operator|=
name|getArtifact
argument_list|(
literal|"test"
argument_list|,
literal|"test-artifactId"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
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
name|artifact
operator|.
name|getFile
argument_list|()
operator|.
name|setLastModified
argument_list|(
name|historicTime
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
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
name|artifact
operator|=
name|getArtifact
argument_list|(
literal|"test"
argument_list|,
literal|"test-artifactId"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
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
name|artifact
operator|.
name|getFile
argument_list|()
operator|.
name|setLastModified
argument_list|(
name|historicTime
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
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
return|return
name|indexer
return|;
block|}
comment|/**      * Method for testing the exceptions thrown by ArtifactRepositoryIndex      *      * @throws Exception      */
specifier|public
name|void
name|testIndexerExceptions
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"test"
argument_list|,
literal|"test-artifactId"
argument_list|,
literal|"1.0"
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
try|try
block|{
name|String
name|notIndexDir
init|=
operator|new
name|File
argument_list|(
literal|"pom.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|EclipseRepositoryIndex
name|indexer
init|=
operator|new
name|EclipseRepositoryIndex
argument_list|(
name|notIndexDir
argument_list|,
name|repository
argument_list|,
name|digester
argument_list|)
decl_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must throw exception on non-directory index directory"
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
try|try
block|{
name|String
name|notIndexDir
init|=
operator|new
name|File
argument_list|(
literal|""
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|EclipseRepositoryIndex
name|indexer
init|=
operator|new
name|EclipseRepositoryIndex
argument_list|(
name|notIndexDir
argument_list|,
name|repository
argument_list|,
name|digester
argument_list|)
decl_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must throw an exception on a non-index directory"
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
name|EclipseRepositoryIndex
name|indexer
init|=
operator|new
name|EclipseRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|,
name|digester
argument_list|)
decl_stmt|;
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
literal|"Must throw exception on object not of type artifact."
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
comment|/**      * Test the ArtifactRepositoryIndex using a single-phrase search.      *      * @throws Exception      */
specifier|public
name|void
name|testSearch
parameter_list|()
throws|throws
name|Exception
block|{
name|EclipseRepositoryIndex
name|index
init|=
name|createTestIndex
argument_list|()
decl_stmt|;
name|IndexSearcher
name|searcher
init|=
operator|new
name|IndexSearcher
argument_list|(
name|index
operator|.
name|getIndexPath
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|QueryParser
name|parser
init|=
operator|new
name|QueryParser
argument_list|(
literal|"j"
argument_list|,
name|index
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
decl_stmt|;
name|Hits
name|hits
init|=
name|searcher
operator|.
name|search
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
literal|"maven-artifact-2.0.1.jar"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Total hits"
argument_list|,
literal|1
argument_list|,
name|hits
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|hits
operator|.
name|doc
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check jar name"
argument_list|,
literal|"maven-artifact-2.0.1.jar"
argument_list|,
name|doc
operator|.
name|get
argument_list|(
literal|"j"
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|QueryParser
argument_list|(
literal|"s"
argument_list|,
name|index
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
name|hits
operator|=
name|searcher
operator|.
name|search
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
literal|"78377"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Total hits"
argument_list|,
literal|1
argument_list|,
name|hits
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|=
name|hits
operator|.
name|doc
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check jar name"
argument_list|,
literal|"maven-artifact-2.0.1.jar"
argument_list|,
name|doc
operator|.
name|get
argument_list|(
literal|"j"
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|QueryParser
argument_list|(
literal|"d"
argument_list|,
name|index
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
name|hits
operator|=
name|searcher
operator|.
name|search
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
name|DateField
operator|.
name|timeToString
argument_list|(
name|artifactFileTime
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Total hits"
argument_list|,
literal|1
argument_list|,
name|hits
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|=
name|hits
operator|.
name|doc
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check jar name"
argument_list|,
literal|"maven-artifact-2.0.1.jar"
argument_list|,
name|doc
operator|.
name|get
argument_list|(
literal|"j"
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|QueryParser
argument_list|(
literal|"m"
argument_list|,
name|index
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
name|hits
operator|=
name|searcher
operator|.
name|search
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
literal|"AE55D9B5720E11B6CF19FE1E31A42E51"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Total hits"
argument_list|,
literal|1
argument_list|,
name|hits
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|=
name|hits
operator|.
name|doc
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check jar name"
argument_list|,
literal|"maven-artifact-2.0.1.jar"
argument_list|,
name|doc
operator|.
name|get
argument_list|(
literal|"j"
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|QueryParser
argument_list|(
literal|"c"
argument_list|,
name|index
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
name|hits
operator|=
name|searcher
operator|.
name|search
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
literal|"MavenXpp3Reader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Total hits"
argument_list|,
literal|1
argument_list|,
name|hits
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|=
name|hits
operator|.
name|doc
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check jar name"
argument_list|,
literal|"maven-model-2.0.jar"
argument_list|,
name|doc
operator|.
name|get
argument_list|(
literal|"j"
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|QueryParser
argument_list|(
literal|"j"
argument_list|,
name|index
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
name|hits
operator|=
name|searcher
operator|.
name|search
argument_list|(
name|parser
operator|.
name|parse
argument_list|(
literal|"maven"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Total hits"
argument_list|,
literal|2
argument_list|,
name|hits
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|searcher
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Method for creating artifact object      *      * @param groupId    the groupId of the artifact to be created      * @param artifactId the artifactId of the artifact to be created      * @param version    the version of the artifact to be created      * @return Artifact object      * @throws Exception      */
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
specifier|protected
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
block|}
end_class

end_unit

