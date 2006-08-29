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
name|indexer
operator|.
name|lucene
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
name|index
operator|.
name|Term
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
name|Query
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
name|TermQuery
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
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryArtifactIndex
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
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryArtifactIndexFactory
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
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryIndexSearchException
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
name|archiva
operator|.
name|indexer
operator|.
name|record
operator|.
name|MinimalIndexRecordFields
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
name|archiva
operator|.
name|indexer
operator|.
name|record
operator|.
name|RepositoryIndexRecordFactory
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

begin_comment
comment|/**  * Test the Lucene implementation of the artifact index search.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @todo would be nice to abstract some of the query away, but for now passing in a Lucene query directly is good enough  */
end_comment

begin_class
specifier|public
class|class
name|LuceneMinimalArtifactIndexSearchTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|RepositoryArtifactIndex
name|index
decl_stmt|;
specifier|private
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
name|File
name|indexLocation
decl_stmt|;
specifier|private
name|RepositoryIndexRecordFactory
name|recordFactory
decl_stmt|;
specifier|private
name|Map
name|records
init|=
operator|new
name|HashMap
argument_list|()
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
name|recordFactory
operator|=
operator|(
name|RepositoryIndexRecordFactory
operator|)
name|lookup
argument_list|(
name|RepositoryIndexRecordFactory
operator|.
name|ROLE
argument_list|,
literal|"minimal"
argument_list|)
expr_stmt|;
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
name|ArtifactRepositoryFactory
name|repositoryFactory
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
name|File
name|file
init|=
name|getTestFile
argument_list|(
literal|"src/test/managed-repository"
argument_list|)
decl_stmt|;
name|repository
operator|=
name|repositoryFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"test"
argument_list|,
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|layout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|RepositoryArtifactIndexFactory
name|factory
init|=
operator|(
name|RepositoryArtifactIndexFactory
operator|)
name|lookup
argument_list|(
name|RepositoryArtifactIndexFactory
operator|.
name|ROLE
argument_list|,
literal|"lucene"
argument_list|)
decl_stmt|;
name|indexLocation
operator|=
name|getTestFile
argument_list|(
literal|"target/test-index"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexLocation
argument_list|)
expr_stmt|;
name|index
operator|=
name|factory
operator|.
name|createMinimalIndex
argument_list|(
name|indexLocation
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-jar"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-jar-jdk14"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-jar"
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|,
literal|"jdk14"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-jar-and-pom"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-jar-and-pom"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-jar-and-pom-jdk14"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-jar-and-pom"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"jar"
argument_list|,
literal|"jdk14"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-child-pom"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-child-pom"
argument_list|,
literal|"1.0-20060728.121314-1"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-archetype"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-archetype"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-plugin"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-plugin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-pom"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-pom"
argument_list|,
literal|"1.0"
argument_list|,
literal|"pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"parent-pom"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"parent-pom"
argument_list|,
literal|"1"
argument_list|,
literal|"pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
literal|"test-dll"
argument_list|,
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|createArtifact
argument_list|(
literal|"test-dll"
argument_list|,
literal|"1.0.1.34"
argument_list|,
literal|"dll"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|index
operator|.
name|indexRecords
argument_list|(
name|records
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchMd5
parameter_list|()
throws|throws
name|RepositoryIndexSearchException
block|{
name|Query
name|query
init|=
name|createExactMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|MD5
argument_list|,
literal|"3a0adc365f849366cd8b633cad155cb7"
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-jdk14"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-and-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-and-pom-jdk14"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-child-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check results size"
argument_list|,
literal|5
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test non-match fails
name|query
operator|=
name|createExactMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|MD5
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|results
operator|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check results size"
argument_list|,
name|results
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchFilename
parameter_list|()
throws|throws
name|RepositoryIndexSearchException
throws|,
name|ParseException
block|{
name|Query
name|query
init|=
name|createMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|FILENAME
argument_list|,
literal|"maven"
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"parent-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-dll"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check results size"
argument_list|,
literal|7
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|=
name|createMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|FILENAME
argument_list|,
literal|"plugin"
argument_list|)
expr_stmt|;
name|results
operator|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-plugin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check results size"
argument_list|,
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|=
name|createMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|FILENAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|results
operator|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"parent-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-dll"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check results size"
argument_list|,
literal|7
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test non-match fails
name|query
operator|=
name|createMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|FILENAME
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|results
operator|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check results size"
argument_list|,
name|results
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchClass
parameter_list|()
throws|throws
name|RepositoryIndexSearchException
throws|,
name|ParseException
block|{
name|Query
name|query
init|=
name|createMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|CLASSES
argument_list|,
literal|"b.c.C"
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-child-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-jdk14"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-and-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-and-pom-jdk14"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check results size"
argument_list|,
literal|5
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|=
name|createMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|CLASSES
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|results
operator|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-child-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-jdk14"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-and-pom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-jar-and-pom-jdk14"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check results size"
argument_list|,
literal|5
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|=
name|createMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|CLASSES
argument_list|,
literal|"MyMojo"
argument_list|)
expr_stmt|;
name|results
operator|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check result"
argument_list|,
name|results
operator|.
name|contains
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|"test-plugin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check results size"
argument_list|,
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test non-match fails
name|query
operator|=
name|createMatchQuery
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|CLASSES
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|results
operator|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check results size"
argument_list|,
name|results
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Query
name|createExactMatchQuery
parameter_list|(
name|String
name|field
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|TermQuery
argument_list|(
operator|new
name|Term
argument_list|(
name|field
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Query
name|createMatchQuery
parameter_list|(
name|String
name|field
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|ParseException
block|{
return|return
operator|new
name|QueryParser
argument_list|(
name|field
argument_list|,
name|LuceneRepositoryArtifactIndex
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
operator|.
name|parse
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|private
name|Artifact
name|createArtifact
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
return|return
name|createArtifact
argument_list|(
name|artifactId
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Artifact
name|createArtifact
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|createArtifact
argument_list|(
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Artifact
name|createArtifact
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|classifier
parameter_list|)
block|{
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createDependencyArtifact
argument_list|(
literal|"org.apache.maven.archiva.record"
argument_list|,
name|artifactId
argument_list|,
name|VersionRange
operator|.
name|createFromVersion
argument_list|(
name|version
argument_list|)
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|,
name|Artifact
operator|.
name|SCOPE_RUNTIME
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|isSnapshot
argument_list|()
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
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

