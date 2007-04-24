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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|index
operator|.
name|IndexModifier
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
name|index
operator|.
name|TermEnum
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
name|lucene
operator|.
name|search
operator|.
name|MatchAllDocsQuery
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
name|RepositoryContentIndex
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
name|query
operator|.
name|Query
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
name|ParseException
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
name|Collection
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
comment|/**  * Lucene implementation of a repository index.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|LuceneRepositoryContentIndex
implements|implements
name|RepositoryContentIndex
block|{
comment|/**      * The max field length for a field in a document.      */
specifier|private
specifier|static
specifier|final
name|int
name|MAX_FIELD_LENGTH
init|=
literal|40000
decl_stmt|;
comment|/**      * The location of the index on the file system.      */
specifier|private
name|File
name|indexLocation
decl_stmt|;
comment|/**      * The Lucene Index Handlers      */
specifier|private
name|LuceneIndexHandlers
name|indexHandlers
decl_stmt|;
specifier|public
name|LuceneRepositoryContentIndex
parameter_list|(
name|File
name|indexDir
parameter_list|,
name|LuceneIndexHandlers
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|indexLocation
operator|=
name|indexDir
expr_stmt|;
name|this
operator|.
name|indexHandlers
operator|=
name|handlers
expr_stmt|;
block|}
specifier|public
name|void
name|indexRecords
parameter_list|(
name|Collection
name|records
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|deleteRecords
argument_list|(
name|records
argument_list|)
expr_stmt|;
name|addRecords
argument_list|(
name|records
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|modifyRecords
parameter_list|(
name|Collection
name|records
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|IndexModifier
name|indexModifier
init|=
literal|null
decl_stmt|;
try|try
block|{
name|indexModifier
operator|=
operator|new
name|IndexModifier
argument_list|(
name|indexLocation
argument_list|,
name|indexHandlers
operator|.
name|getAnalyzer
argument_list|()
argument_list|,
operator|!
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|indexModifier
operator|.
name|setMaxFieldLength
argument_list|(
name|MAX_FIELD_LENGTH
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|records
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
name|LuceneRepositoryContentRecord
name|record
init|=
operator|(
name|LuceneRepositoryContentRecord
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|record
operator|!=
literal|null
condition|)
block|{
name|Term
name|term
init|=
operator|new
name|Term
argument_list|(
name|LuceneDocumentMaker
operator|.
name|PRIMARY_KEY
argument_list|,
name|record
operator|.
name|getPrimaryKey
argument_list|()
argument_list|)
decl_stmt|;
name|indexModifier
operator|.
name|deleteDocuments
argument_list|(
name|term
argument_list|)
expr_stmt|;
name|Document
name|document
init|=
name|indexHandlers
operator|.
name|getConverter
argument_list|()
operator|.
name|convert
argument_list|(
name|record
argument_list|)
decl_stmt|;
name|indexModifier
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
block|}
name|indexModifier
operator|.
name|optimize
argument_list|()
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
name|RepositoryIndexException
argument_list|(
literal|"Error updating index: "
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
name|closeQuietly
argument_list|(
name|indexModifier
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|modifyRecord
parameter_list|(
name|LuceneRepositoryContentRecord
name|record
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|IndexModifier
name|indexModifier
init|=
literal|null
decl_stmt|;
try|try
block|{
name|indexModifier
operator|=
operator|new
name|IndexModifier
argument_list|(
name|indexLocation
argument_list|,
name|indexHandlers
operator|.
name|getAnalyzer
argument_list|()
argument_list|,
operator|!
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|indexModifier
operator|.
name|setMaxFieldLength
argument_list|(
name|MAX_FIELD_LENGTH
argument_list|)
expr_stmt|;
if|if
condition|(
name|record
operator|!=
literal|null
condition|)
block|{
name|Term
name|term
init|=
operator|new
name|Term
argument_list|(
name|LuceneDocumentMaker
operator|.
name|PRIMARY_KEY
argument_list|,
name|record
operator|.
name|getPrimaryKey
argument_list|()
argument_list|)
decl_stmt|;
name|indexModifier
operator|.
name|deleteDocuments
argument_list|(
name|term
argument_list|)
expr_stmt|;
name|Document
name|document
init|=
name|indexHandlers
operator|.
name|getConverter
argument_list|()
operator|.
name|convert
argument_list|(
name|record
argument_list|)
decl_stmt|;
name|indexModifier
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
name|indexModifier
operator|.
name|optimize
argument_list|()
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
name|RepositoryIndexException
argument_list|(
literal|"Error updating index: "
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
name|closeQuietly
argument_list|(
name|indexModifier
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addRecords
parameter_list|(
name|Collection
name|records
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|IndexWriter
name|indexWriter
decl_stmt|;
try|try
block|{
name|indexWriter
operator|=
operator|new
name|IndexWriter
argument_list|(
name|indexLocation
argument_list|,
name|indexHandlers
operator|.
name|getAnalyzer
argument_list|()
argument_list|,
operator|!
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|indexWriter
operator|.
name|setMaxFieldLength
argument_list|(
name|MAX_FIELD_LENGTH
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
name|RepositoryIndexException
argument_list|(
literal|"Unable to open index"
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|records
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
name|LuceneRepositoryContentRecord
name|record
init|=
operator|(
name|LuceneRepositoryContentRecord
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|record
operator|!=
literal|null
condition|)
block|{
name|Document
name|document
init|=
name|indexHandlers
operator|.
name|getConverter
argument_list|()
operator|.
name|convert
argument_list|(
name|record
argument_list|)
decl_stmt|;
name|indexWriter
operator|.
name|addDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
block|}
name|indexWriter
operator|.
name|optimize
argument_list|()
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
name|RepositoryIndexException
argument_list|(
literal|"Failed to add an index document"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closeQuietly
argument_list|(
name|indexWriter
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|deleteRecords
parameter_list|(
name|Collection
name|records
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
if|if
condition|(
name|exists
argument_list|()
condition|)
block|{
name|IndexReader
name|indexReader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|indexReader
operator|=
name|IndexReader
operator|.
name|open
argument_list|(
name|indexLocation
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|records
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
name|LuceneRepositoryContentRecord
name|record
init|=
operator|(
name|LuceneRepositoryContentRecord
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|record
operator|!=
literal|null
condition|)
block|{
name|Term
name|term
init|=
operator|new
name|Term
argument_list|(
name|LuceneDocumentMaker
operator|.
name|PRIMARY_KEY
argument_list|,
name|record
operator|.
name|getPrimaryKey
argument_list|()
argument_list|)
decl_stmt|;
name|indexReader
operator|.
name|deleteDocuments
argument_list|(
name|term
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Error deleting document: "
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
name|closeQuietly
argument_list|(
name|indexReader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Collection
name|getAllRecords
parameter_list|()
throws|throws
name|RepositoryIndexSearchException
block|{
return|return
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
operator|new
name|MatchAllDocsQuery
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Collection
name|getAllRecordKeys
parameter_list|()
throws|throws
name|RepositoryIndexException
block|{
return|return
name|getAllFieldValues
argument_list|(
name|LuceneDocumentMaker
operator|.
name|PRIMARY_KEY
argument_list|)
return|;
block|}
specifier|private
name|List
name|getAllFieldValues
parameter_list|(
name|String
name|fieldName
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|List
name|keys
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
if|if
condition|(
name|exists
argument_list|()
condition|)
block|{
name|IndexReader
name|indexReader
init|=
literal|null
decl_stmt|;
name|TermEnum
name|terms
init|=
literal|null
decl_stmt|;
try|try
block|{
name|indexReader
operator|=
name|IndexReader
operator|.
name|open
argument_list|(
name|indexLocation
argument_list|)
expr_stmt|;
name|terms
operator|=
name|indexReader
operator|.
name|terms
argument_list|(
operator|new
name|Term
argument_list|(
name|fieldName
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
while|while
condition|(
name|fieldName
operator|.
name|equals
argument_list|(
name|terms
operator|.
name|term
argument_list|()
operator|.
name|field
argument_list|()
argument_list|)
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|terms
operator|.
name|term
argument_list|()
operator|.
name|text
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|terms
operator|.
name|next
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Error deleting document: "
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
name|closeQuietly
argument_list|(
name|indexReader
argument_list|)
expr_stmt|;
name|closeQuietly
argument_list|(
name|terms
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|keys
return|;
block|}
comment|//    public List getAllGroupIds() throws RepositoryIndexException
comment|//    {
comment|//        return getAllFieldValues( StandardIndexRecordFields.GROUPID_EXACT );
comment|//    }
comment|//
comment|//    public List getArtifactIds( String groupId ) throws RepositoryIndexSearchException
comment|//    {
comment|//        return searchField( new TermQuery( new Term( StandardIndexRecordFields.GROUPID_EXACT, groupId ) ),
comment|//                            StandardIndexRecordFields.ARTIFACTID );
comment|//    }
comment|//
comment|//    public List getVersions( String groupId, String artifactId ) throws RepositoryIndexSearchException
comment|//    {
comment|//        BooleanQuery query = new BooleanQuery();
comment|//        query.add( new TermQuery( new Term( StandardIndexRecordFields.GROUPID_EXACT, groupId ) ),
comment|//                   BooleanClause.Occur.MUST );
comment|//        query.add( new TermQuery( new Term( StandardIndexRecordFields.ARTIFACTID_EXACT, artifactId ) ),
comment|//                   BooleanClause.Occur.MUST );
comment|//
comment|//        return searchField( query, StandardIndexRecordFields.VERSION );
comment|//    }
comment|//    private List searchField( org.apache.lucene.search.Query luceneQuery, String fieldName )
comment|//        throws RepositoryIndexSearchException
comment|//    {
comment|//        Set results = new LinkedHashSet();
comment|//
comment|//        IndexSearcher searcher;
comment|//        try
comment|//        {
comment|//            searcher = new IndexSearcher( indexLocation.getAbsolutePath() );
comment|//        }
comment|//        catch ( IOException e )
comment|//        {
comment|//            throw new RepositoryIndexSearchException( "Unable to open index: " + e.getMessage(), e );
comment|//        }
comment|//
comment|//        try
comment|//        {
comment|//            Hits hits = searcher.search( luceneQuery );
comment|//            for ( int i = 0; i< hits.length(); i++ )
comment|//            {
comment|//                Document doc = hits.doc( i );
comment|//
comment|//                results.add( doc.get( fieldName ) );
comment|//            }
comment|//        }
comment|//        catch ( IOException e )
comment|//        {
comment|//            throw new RepositoryIndexSearchException( "Unable to search index: " + e.getMessage(), e );
comment|//        }
comment|//        finally
comment|//        {
comment|//            closeQuietly( searcher );
comment|//        }
comment|//        return new ArrayList( results );
comment|//    }
specifier|public
name|boolean
name|exists
parameter_list|()
throws|throws
name|RepositoryIndexException
block|{
if|if
condition|(
name|IndexReader
operator|.
name|indexExists
argument_list|(
name|indexLocation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
operator|!
name|indexLocation
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|else if
condition|(
name|indexLocation
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|indexLocation
operator|.
name|listFiles
argument_list|()
operator|.
name|length
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
name|indexLocation
operator|+
literal|" is not a valid index directory."
argument_list|)
throw|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
name|indexLocation
operator|+
literal|" is not a directory."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|List
name|search
parameter_list|(
name|Query
name|query
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
block|{
name|LuceneQuery
name|lQuery
init|=
operator|(
name|LuceneQuery
operator|)
name|query
decl_stmt|;
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
init|=
name|lQuery
operator|.
name|getLuceneQuery
argument_list|()
decl_stmt|;
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
name|indexLocation
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
name|records
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
name|records
operator|.
name|add
argument_list|(
name|indexHandlers
operator|.
name|getConverter
argument_list|()
operator|.
name|convert
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|closeQuietly
argument_list|(
name|searcher
argument_list|)
expr_stmt|;
block|}
return|return
name|records
return|;
block|}
specifier|public
name|QueryParser
name|getQueryParser
parameter_list|()
block|{
return|return
name|this
operator|.
name|indexHandlers
operator|.
name|getQueryParser
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|closeQuietly
parameter_list|(
name|IndexSearcher
name|searcher
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|searcher
operator|!=
literal|null
condition|)
block|{
name|searcher
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|private
specifier|static
name|void
name|closeQuietly
parameter_list|(
name|TermEnum
name|terms
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
if|if
condition|(
name|terms
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|terms
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
comment|// ignore
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|closeQuietly
parameter_list|(
name|IndexWriter
name|indexWriter
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
try|try
block|{
if|if
condition|(
name|indexWriter
operator|!=
literal|null
condition|)
block|{
name|indexWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// write should compain if it can't be closed, data probably not persisted
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|void
name|closeQuietly
parameter_list|(
name|IndexModifier
name|indexModifier
parameter_list|)
block|{
if|if
condition|(
name|indexModifier
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|indexModifier
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
comment|// ignore
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|closeQuietly
parameter_list|(
name|IndexReader
name|reader
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|public
name|File
name|getIndexDirectory
parameter_list|()
block|{
return|return
name|this
operator|.
name|indexLocation
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|indexHandlers
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
end_class

end_unit

