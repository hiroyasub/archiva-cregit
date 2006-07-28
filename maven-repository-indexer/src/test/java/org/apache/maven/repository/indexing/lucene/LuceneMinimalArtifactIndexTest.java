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
name|analysis
operator|.
name|standard
operator|.
name|StandardAnalyzer
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
name|document
operator|.
name|NumberTools
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
name|index
operator|.
name|IndexReader
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
name|index
operator|.
name|IndexWriter
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
name|indexing
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
name|repository
operator|.
name|indexing
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndexException
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
name|repository
operator|.
name|indexing
operator|.
name|record
operator|.
name|RepositoryIndexRecord
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
name|record
operator|.
name|RepositoryIndexRecordFactory
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_comment
comment|/**  * Test the Lucene implementation of the artifact index.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|LuceneMinimalArtifactIndexTest
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
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIndexExists
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
block|{
name|assertFalse
argument_list|(
literal|"check index doesn't exist"
argument_list|,
name|index
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// create empty directory
name|indexLocation
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check index doesn't exist even if directory does"
argument_list|,
name|index
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// create index, with no records
name|createEmptyIndex
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check index is considered to exist"
argument_list|,
name|index
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test non-directory
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexLocation
argument_list|)
expr_stmt|;
name|indexLocation
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
try|try
block|{
name|index
operator|.
name|exists
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Index operation should fail as the location is not valid"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
comment|// great
block|}
finally|finally
block|{
name|indexLocation
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testAddRecordNoIndex
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
block|{
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"test-jar"
argument_list|)
decl_stmt|;
name|RepositoryIndexRecord
name|record
init|=
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|index
operator|.
name|indexRecords
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|IndexReader
name|reader
init|=
name|IndexReader
operator|.
name|open
argument_list|(
name|indexLocation
argument_list|)
decl_stmt|;
try|try
block|{
name|Document
name|document
init|=
name|reader
operator|.
name|document
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check document"
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|,
name|document
operator|.
name|get
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|FILENAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check index size"
argument_list|,
literal|1
argument_list|,
name|reader
operator|.
name|numDocs
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testAddRecordExistingEmptyIndex
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
block|{
name|createEmptyIndex
argument_list|()
expr_stmt|;
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"test-jar"
argument_list|)
decl_stmt|;
name|RepositoryIndexRecord
name|record
init|=
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|index
operator|.
name|indexRecords
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|IndexReader
name|reader
init|=
name|IndexReader
operator|.
name|open
argument_list|(
name|indexLocation
argument_list|)
decl_stmt|;
try|try
block|{
name|Document
name|document
init|=
name|reader
operator|.
name|document
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertRecord
argument_list|(
name|document
argument_list|,
name|artifact
argument_list|,
literal|"3a0adc365f849366cd8b633cad155cb7"
argument_list|,
literal|"A\nb.B\nb.c.C"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check index size"
argument_list|,
literal|1
argument_list|,
name|reader
operator|.
name|numDocs
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testAddRecordInIndex
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
block|{
name|createEmptyIndex
argument_list|()
expr_stmt|;
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"test-jar"
argument_list|)
decl_stmt|;
name|RepositoryIndexRecord
name|record
init|=
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|index
operator|.
name|indexRecords
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
comment|// Do it again
name|record
operator|=
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|index
operator|.
name|indexRecords
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|IndexReader
name|reader
init|=
name|IndexReader
operator|.
name|open
argument_list|(
name|indexLocation
argument_list|)
decl_stmt|;
try|try
block|{
name|Document
name|document
init|=
name|reader
operator|.
name|document
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertRecord
argument_list|(
name|document
argument_list|,
name|artifact
argument_list|,
literal|"3a0adc365f849366cd8b633cad155cb7"
argument_list|,
literal|"A\nb.B\nb.c.C"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check index size"
argument_list|,
literal|1
argument_list|,
name|reader
operator|.
name|numDocs
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testAddPomRecord
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
block|{
name|createEmptyIndex
argument_list|()
expr_stmt|;
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"test-pom"
argument_list|,
literal|"1.0"
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|RepositoryIndexRecord
name|record
init|=
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|index
operator|.
name|indexRecords
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|IndexReader
name|reader
init|=
name|IndexReader
operator|.
name|open
argument_list|(
name|indexLocation
argument_list|)
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|"No documents"
argument_list|,
literal|0
argument_list|,
name|reader
operator|.
name|numDocs
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testAddPlugin
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
throws|,
name|XmlPullParserException
block|{
name|createEmptyIndex
argument_list|()
expr_stmt|;
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"test-plugin"
argument_list|)
decl_stmt|;
name|RepositoryIndexRecord
name|record
init|=
name|recordFactory
operator|.
name|createRecord
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|index
operator|.
name|indexRecords
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|IndexReader
name|reader
init|=
name|IndexReader
operator|.
name|open
argument_list|(
name|indexLocation
argument_list|)
decl_stmt|;
try|try
block|{
name|Document
name|document
init|=
name|reader
operator|.
name|document
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertRecord
argument_list|(
name|document
argument_list|,
name|artifact
argument_list|,
literal|"06f6fe25e46c4d4fb5be4f56a9bab0ee"
argument_list|,
literal|"org.apache.maven.repository.record.MyMojo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check index size"
argument_list|,
literal|1
argument_list|,
name|reader
operator|.
name|numDocs
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
literal|"org.apache.maven.repository.record"
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
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
specifier|private
name|void
name|createEmptyIndex
parameter_list|()
throws|throws
name|IOException
block|{
name|createIndex
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createIndex
parameter_list|(
name|List
name|docments
parameter_list|)
throws|throws
name|IOException
block|{
name|IndexWriter
name|writer
init|=
operator|new
name|IndexWriter
argument_list|(
name|indexLocation
argument_list|,
operator|new
name|StandardAnalyzer
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|docments
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
name|Document
name|document
init|=
operator|(
name|Document
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|writer
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|assertRecord
parameter_list|(
name|Document
name|document
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|expectedChecksum
parameter_list|,
name|String
name|expectedClasses
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Check document filename"
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|,
name|document
operator|.
name|get
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|FILENAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check document timestamp"
argument_list|,
name|getLastModified
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
argument_list|,
name|document
operator|.
name|get
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|LAST_MODIFIED
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check document checksum"
argument_list|,
name|expectedChecksum
argument_list|,
name|document
operator|.
name|get
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|MD5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check document size"
argument_list|,
name|artifact
operator|.
name|getFile
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
name|NumberTools
operator|.
name|stringToLong
argument_list|(
name|document
operator|.
name|get
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|FILE_SIZE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check document classes"
argument_list|,
name|expectedClasses
argument_list|,
name|document
operator|.
name|get
argument_list|(
name|MinimalIndexRecordFields
operator|.
name|CLASSES
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getLastModified
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|SimpleDateFormat
name|dateFormat
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMddHHmmss"
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
name|dateFormat
operator|.
name|setTimeZone
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|dateFormat
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

