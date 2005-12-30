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
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactRepositoryIndexingTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|GROUPID
init|=
literal|"groupId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACTID
init|=
literal|"artifactId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SHA1
init|=
literal|"sha1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MD5
init|=
literal|"md5"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLASSES
init|=
literal|"classes"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PACKAGES
init|=
literal|"packages"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FILES
init|=
literal|"files"
decl_stmt|;
specifier|protected
name|ArtifactRepositoryIndex
name|indexer
decl_stmt|;
specifier|protected
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|protected
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|protected
name|String
name|indexPath
decl_stmt|;
specifier|private
name|RepositoryIndexSearcher
name|repoSearcher
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
specifier|public
name|void
name|testIndexerExceptions
parameter_list|()
throws|throws
name|Exception
block|{
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
name|indexer
operator|=
operator|(
name|ArtifactRepositoryIndex
operator|)
name|lookup
argument_list|(
name|RepositoryIndex
operator|.
name|ROLE
argument_list|,
literal|"artifact"
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|open
argument_list|(
name|notIndexDir
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
comment|//expected
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
name|indexer
operator|=
operator|(
name|ArtifactRepositoryIndex
operator|)
name|lookup
argument_list|(
name|RepositoryIndex
operator|.
name|ROLE
argument_list|,
literal|"artifact"
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|open
argument_list|(
name|notIndexDir
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
comment|//expected
block|}
comment|//indexer = (ArtifactRepositoryIndex) factory.getArtifactRepositoryIndexer( indexPath, repository );
comment|//indexer.close();
name|indexer
operator|=
operator|(
name|ArtifactRepositoryIndex
operator|)
name|lookup
argument_list|(
name|RepositoryIndex
operator|.
name|ROLE
argument_list|,
literal|"artifact"
argument_list|)
expr_stmt|;
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
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must throw exception on add index with closed index."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
comment|//expected
block|}
try|try
block|{
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Must throw exception on optimize index with closed index."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
comment|//expected
block|}
name|indexer
operator|.
name|open
argument_list|(
name|indexPath
argument_list|)
expr_stmt|;
try|try
block|{
name|indexer
operator|.
name|index
argument_list|(
literal|"should fail"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must throw exception on add non-Artifact object."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
comment|//expected
block|}
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testIndex
parameter_list|()
throws|throws
name|Exception
block|{
comment|//indexer = (ArtifactRepositoryIndex) factory.getArtifactRepositoryIndexer( indexPath, repository );
name|indexer
operator|=
operator|(
name|ArtifactRepositoryIndex
operator|)
name|lookup
argument_list|(
name|RepositoryIndex
operator|.
name|ROLE
argument_list|,
literal|"artifact"
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|open
argument_list|(
name|indexPath
argument_list|)
expr_stmt|;
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
name|indexer
operator|.
name|open
argument_list|(
name|indexPath
argument_list|)
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
name|index
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSearch
parameter_list|()
throws|throws
name|Exception
block|{
name|indexer
operator|=
operator|(
name|ArtifactRepositoryIndex
operator|)
name|lookup
argument_list|(
name|RepositoryIndex
operator|.
name|ROLE
argument_list|,
literal|"artifact"
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|open
argument_list|(
name|getTestPath
argument_list|(
literal|"src/test/index"
argument_list|)
argument_list|)
expr_stmt|;
comment|//repoSearcher = new ArtifactRepositoryIndexSearcher( indexer, indexPath, repository );
name|repoSearcher
operator|=
operator|(
name|RepositoryIndexSearcher
operator|)
name|lookup
argument_list|(
name|RepositoryIndexSearcher
operator|.
name|ROLE
argument_list|,
literal|"artifact"
argument_list|)
expr_stmt|;
name|List
name|artifacts
init|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"test"
argument_list|,
name|GROUPID
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
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"test"
argument_list|,
name|ARTIFACTID
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
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"1.0"
argument_list|,
name|VERSION
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
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"App"
argument_list|,
name|CLASSES
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
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"groupId"
argument_list|,
name|PACKAGES
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
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"pom.xml"
argument_list|,
name|FILES
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
name|File
name|f
init|=
name|artifact
operator|.
name|getFile
argument_list|()
decl_stmt|;
comment|//assertNotNull( f );
comment|//assertTrue( f.exists() );
block|}
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"org.apache.maven"
argument_list|,
name|GROUPID
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
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"maven-artifact"
argument_list|,
name|ARTIFACTID
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
name|artifacts
operator|=
name|repoSearcher
operator|.
name|search
argument_list|(
name|indexer
argument_list|,
literal|"2"
argument_list|,
name|VERSION
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
block|}
specifier|protected
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
block|}
end_class

end_unit

