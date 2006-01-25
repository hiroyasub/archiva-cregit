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
name|CompoundQuery
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
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactRepositoryIndexingTest
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
specifier|static
specifier|final
name|String
name|ARTIFACT_TYPE
init|=
literal|"ARTIFACT"
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
literal|"target/index/jar"
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexPath
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIndexerExceptions
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
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|notIndexDir
argument_list|,
name|repository
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
comment|// expected
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
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|notIndexDir
argument_list|,
name|repository
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
comment|// expected
block|}
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
try|try
block|{
name|indexer
operator|.
name|isIndexed
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
comment|// expected
block|}
block|}
comment|/**      * Create an index that will be used for testing.      * Indexing process: check if the object was already indexed [ checkIfIndexed(Object) ], open the index [ open() ],      * index the object [ index(Object) ], optimize the index [ optimize() ] and close the index [ close() ].      *      * @throws Exception      */
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
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
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
block|}
comment|/**      * Test the ArtifactRepositoryIndexSearcher using a single-phrase search.      *      * @throws Exception      */
specifier|public
name|void
name|testSearchSingle
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
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|RepositoryIndexSearcher
name|repoSearcher
init|=
name|factory
operator|.
name|createArtifactRepositoryIndexSearcher
argument_list|(
name|indexer
argument_list|)
decl_stmt|;
comment|// search version
name|Query
name|qry
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_VERSION
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|List
name|artifacts
init|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// search classes
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_CLASSES
argument_list|,
literal|"App"
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test-artifactId"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// search packages
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_PACKAGES
argument_list|,
literal|"groupId"
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test-artifactId"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// search files
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_FILES
argument_list|,
literal|"pom.xml"
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|iter
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test-artifactId"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// search group id
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_GROUPID
argument_list|,
literal|"org.apache.maven"
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|iter
operator|=
name|artifacts
operator|.
name|iterator
argument_list|()
expr_stmt|;
if|if
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// search artifact id
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_ARTIFACTID
argument_list|,
literal|"maven-artifact"
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|iter
operator|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-artifact"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// search version
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_VERSION
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|iter
operator|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"2"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// search sha1 checksum
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"2.0"
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
name|String
name|sha1
init|=
name|digester
operator|.
name|createChecksum
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|,
name|Digester
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_SHA1
argument_list|,
name|sha1
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|iter
operator|=
name|artifacts
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
name|Artifact
name|artifact2
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|sha1Tmp
init|=
name|digester
operator|.
name|createChecksum
argument_list|(
name|artifact2
operator|.
name|getFile
argument_list|()
argument_list|,
name|Digester
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|sha1
argument_list|,
name|sha1Tmp
argument_list|)
expr_stmt|;
block|}
comment|// search md5 checksum
name|String
name|md5
init|=
name|digester
operator|.
name|createChecksum
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|,
name|Digester
operator|.
name|MD5
argument_list|)
decl_stmt|;
name|qry
operator|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_MD5
argument_list|,
name|md5
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|iter
operator|=
name|artifacts
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
name|Artifact
name|artifact2
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|md5Tmp
init|=
name|digester
operator|.
name|createChecksum
argument_list|(
name|artifact2
operator|.
name|getFile
argument_list|()
argument_list|,
name|Digester
operator|.
name|MD5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|md5
argument_list|,
name|md5Tmp
argument_list|)
expr_stmt|;
block|}
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test the ArtifactRepositoryIndexSearcher using compound search (AND, OR).      *      * @throws Exception      */
specifier|public
name|void
name|testSearchCompound
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
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|RepositoryIndexSearcher
name|repoSearcher
init|=
name|factory
operator|.
name|createArtifactRepositoryIndexSearcher
argument_list|(
name|indexer
argument_list|)
decl_stmt|;
comment|// Criteria 1: required query
comment|// ex. artifactId=maven-artifact AND groupId=org.apache.maven
name|Query
name|qry1
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_ARTIFACTID
argument_list|,
literal|"maven-artifact"
argument_list|)
decl_stmt|;
name|Query
name|qry2
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_GROUPID
argument_list|,
literal|"org.apache.maven"
argument_list|)
decl_stmt|;
name|CompoundQuery
name|rQry
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|rQry
operator|.
name|and
argument_list|(
name|qry1
argument_list|)
expr_stmt|;
name|rQry
operator|.
name|and
argument_list|(
name|qry2
argument_list|)
expr_stmt|;
name|List
name|artifacts
init|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|rQry
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-artifact"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Criteria 2: nested required query
comment|// ex. (artifactId=maven-artifact AND groupId=org.apache.maven) OR
comment|// version=2.0.3
name|Query
name|qry3
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_VERSION
argument_list|,
literal|"2.0.3"
argument_list|)
decl_stmt|;
name|CompoundQuery
name|oQry
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|oQry
operator|.
name|or
argument_list|(
name|rQry
argument_list|)
expr_stmt|;
name|oQry
operator|.
name|or
argument_list|(
name|qry3
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|oQry
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-artifact"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Criteria 3: nested required query
comment|// ex. (artifactId=maven-artifact AND groupId=org.apache.maven) AND
comment|// (version=2.0.3 OR version=2.0.1)
comment|// AND (name=maven-artifact-2.0.1.jar OR name=maven-artifact)
name|Query
name|qry4
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_VERSION
argument_list|,
literal|"2.0.1"
argument_list|)
decl_stmt|;
name|oQry
operator|=
operator|new
name|CompoundQuery
argument_list|()
expr_stmt|;
name|oQry
operator|.
name|or
argument_list|(
name|qry3
argument_list|)
expr_stmt|;
name|oQry
operator|.
name|or
argument_list|(
name|qry4
argument_list|)
expr_stmt|;
name|CompoundQuery
name|oQry5
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|Query
name|qry9
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_NAME
argument_list|,
literal|"maven-artifact-2.0.1.jar"
argument_list|)
decl_stmt|;
name|Query
name|qry10
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_NAME
argument_list|,
literal|"maven-artifact"
argument_list|)
decl_stmt|;
name|oQry5
operator|.
name|or
argument_list|(
name|qry9
argument_list|)
expr_stmt|;
name|oQry5
operator|.
name|or
argument_list|(
name|qry10
argument_list|)
expr_stmt|;
name|CompoundQuery
name|rQry2
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|rQry2
operator|.
name|or
argument_list|(
name|oQry
argument_list|)
expr_stmt|;
name|rQry2
operator|.
name|and
argument_list|(
name|rQry
argument_list|)
expr_stmt|;
name|rQry2
operator|.
name|or
argument_list|(
name|oQry5
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|rQry2
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-artifact"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0.1"
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Criteria 4: nested required query
comment|// ex. [(artifactId=maven-artifact AND groupId=org.apache.maven) AND
comment|// (version=2.0.3 OR version=2.0.1)
comment|// AND (name=maven-artifact-2.0.1.jar OR name=maven-artifact)]
comment|// OR [(artifactId=sample AND groupId=test)]
name|CompoundQuery
name|rQry3
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|Query
name|qry5
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_ARTIFACTID
argument_list|,
literal|"sample"
argument_list|)
decl_stmt|;
name|Query
name|qry6
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_GROUPID
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|rQry3
operator|.
name|and
argument_list|(
name|qry5
argument_list|)
expr_stmt|;
name|rQry3
operator|.
name|and
argument_list|(
name|qry6
argument_list|)
expr_stmt|;
name|CompoundQuery
name|oQry2
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|oQry2
operator|.
name|and
argument_list|(
name|rQry2
argument_list|)
expr_stmt|;
name|oQry2
operator|.
name|and
argument_list|(
name|rQry3
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|oQry2
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-artifact"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0.1"
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Criteria 4: nested required query
comment|// ex. [(artifactId=maven-artifact AND groupId=org.apache.maven) AND
comment|// (version=2.0.3 OR version=2.0.1)
comment|// AND (name=maven-artifact-2.0.1.jar OR name=maven-artifact)] OR
comment|// [(artifactId=sample AND groupId=test)] OR
comment|// [(artifactId=sample2 AND groupId=test)]
name|CompoundQuery
name|rQry4
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|Query
name|qry7
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_ARTIFACTID
argument_list|,
literal|"sample2"
argument_list|)
decl_stmt|;
name|Query
name|qry8
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_GROUPID
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|rQry4
operator|.
name|and
argument_list|(
name|qry7
argument_list|)
expr_stmt|;
name|rQry4
operator|.
name|and
argument_list|(
name|qry8
argument_list|)
expr_stmt|;
name|oQry2
operator|.
name|and
argument_list|(
name|rQry4
argument_list|)
expr_stmt|;
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|oQry2
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-artifact"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test delete of document from the artifact index.      *      * @throws Exception      */
specifier|public
name|void
name|testDeleteArtifactDocument
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
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
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
name|indexer
operator|.
name|deleteDocument
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_ID
argument_list|,
name|ARTIFACT_TYPE
operator|+
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryIndexSearcher
name|repoSearcher
init|=
name|factory
operator|.
name|createArtifactRepositoryIndexSearcher
argument_list|(
name|indexer
argument_list|)
decl_stmt|;
name|Query
name|qry
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
name|ArtifactRepositoryIndex
operator|.
name|FLD_ID
argument_list|,
name|ARTIFACT_TYPE
operator|+
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|List
name|artifacts
init|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|qry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|artifacts
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
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

