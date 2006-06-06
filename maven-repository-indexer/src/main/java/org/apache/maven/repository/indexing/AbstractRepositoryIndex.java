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
name|analysis
operator|.
name|Analyzer
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
name|analysis
operator|.
name|CharTokenizer
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
name|analysis
operator|.
name|SimpleAnalyzer
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
name|analysis
operator|.
name|TokenStream
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
name|io
operator|.
name|Reader
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
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_comment
comment|/**  * Abstract class for RepositoryIndexers  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryIndex
implements|implements
name|RepositoryIndex
block|{
specifier|private
name|String
name|indexPath
decl_stmt|;
specifier|private
name|boolean
name|indexOpen
decl_stmt|;
specifier|private
name|IndexWriter
name|indexWriter
decl_stmt|;
specifier|protected
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|private
name|Analyzer
name|analyzer
decl_stmt|;
comment|/**      * Class constructor      *      * @param indexPath      * @param repository      * @throws RepositoryIndexException      */
specifier|protected
name|AbstractRepositoryIndex
parameter_list|(
name|String
name|indexPath
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|indexPath
operator|=
name|indexPath
expr_stmt|;
block|}
comment|/**      * Method to open the IndexWriter      *      * @throws RepositoryIndexException      */
specifier|public
name|void
name|open
parameter_list|()
throws|throws
name|RepositoryIndexException
block|{
try|try
block|{
if|if
condition|(
name|indexExists
argument_list|()
condition|)
block|{
name|indexWriter
operator|=
operator|new
name|IndexWriter
argument_list|(
name|indexPath
argument_list|,
name|getAnalyzer
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|indexWriter
operator|=
operator|new
name|IndexWriter
argument_list|(
name|indexPath
argument_list|,
name|getAnalyzer
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ie
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
name|ie
argument_list|)
throw|;
block|}
name|indexOpen
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * @see org.apache.maven.repository.indexing.RepositoryIndex#optimize()      */
specifier|public
name|void
name|optimize
parameter_list|()
throws|throws
name|RepositoryIndexException
block|{
if|if
condition|(
operator|!
name|indexOpen
condition|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Unable to optimize index on a closed index"
argument_list|)
throw|;
block|}
try|try
block|{
name|indexWriter
operator|.
name|optimize
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Failed to optimize index"
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
block|}
comment|/**      * @see org.apache.maven.repository.indexing.RepositoryIndex#isOpen()      */
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
name|indexOpen
return|;
block|}
comment|/**      * @see org.apache.maven.repository.indexing.RepositoryIndex#close()      */
specifier|public
name|void
name|close
parameter_list|()
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
name|indexWriter
operator|=
literal|null
expr_stmt|;
block|}
name|indexOpen
operator|=
literal|false
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
comment|/**      * @see org.apache.maven.repository.indexing.RepositoryIndex#getIndexPath()      */
specifier|public
name|String
name|getIndexPath
parameter_list|()
block|{
return|return
name|indexPath
return|;
block|}
comment|/**      * Method to retrieve the lucene IndexWriter used in creating/updating the index      *      * @return the lucene IndexWriter object used to update the index      * @throws IOException      */
specifier|protected
name|IndexWriter
name|getIndexWriter
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|indexWriter
operator|==
literal|null
condition|)
block|{
name|indexWriter
operator|=
operator|new
name|IndexWriter
argument_list|(
name|indexPath
argument_list|,
name|getAnalyzer
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|indexWriter
return|;
block|}
comment|/**      * method for validating an index directory      *      * @param indexFields      * @throws RepositoryIndexException if the given indexPath is not valid for this type of RepositoryIndex      */
specifier|protected
name|void
name|validateIndex
parameter_list|(
name|String
index|[]
name|indexFields
parameter_list|)
throws|throws
name|RepositoryIndexException
throws|,
name|IOException
block|{
name|IndexReader
name|indexReader
init|=
name|IndexReader
operator|.
name|open
argument_list|(
name|indexPath
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|indexReader
operator|.
name|numDocs
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Collection
name|fields
init|=
name|indexReader
operator|.
name|getFieldNames
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
name|indexFields
operator|.
name|length
condition|;
name|idx
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|fields
operator|.
name|contains
argument_list|(
name|indexFields
index|[
name|idx
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"The Field "
operator|+
name|indexFields
index|[
name|idx
index|]
operator|+
literal|" does not exist in index "
operator|+
name|indexPath
operator|+
literal|"."
argument_list|)
throw|;
block|}
block|}
block|}
block|}
finally|finally
block|{
name|indexReader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @see org.apache.maven.repository.indexing.RepositoryIndex#getRepository()      */
specifier|public
name|ArtifactRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
comment|/**      * Delete the document(s) that contains the specified value on the specified field.      *      * @param field      * @param value      * @throws RepositoryIndexException      * @throws IOException      */
specifier|protected
name|void
name|deleteDocument
parameter_list|(
name|String
name|field
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|RepositoryIndexException
throws|,
name|IOException
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
name|indexPath
argument_list|)
expr_stmt|;
name|indexReader
operator|.
name|delete
argument_list|(
operator|new
name|Term
argument_list|(
name|field
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ie
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
name|indexPath
operator|+
literal|"is not a valid directory."
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|indexReader
operator|!=
literal|null
condition|)
block|{
name|indexReader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Check if the index already exists.      *      * @return true if the index already exists      * @throws IOException      * @throws RepositoryIndexException      */
specifier|protected
name|boolean
name|indexExists
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryIndexException
block|{
name|File
name|indexDir
init|=
operator|new
name|File
argument_list|(
name|indexPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|IndexReader
operator|.
name|indexExists
argument_list|(
name|indexDir
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
name|indexDir
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
name|indexDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|indexDir
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
name|indexPath
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
name|indexPath
operator|+
literal|" is not a directory."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Checks if the object has already been indexed and deletes it if it is.      *      * @param object the object to be indexed.      * @throws RepositoryIndexException      * @throws IOException      */
specifier|abstract
name|void
name|deleteIfIndexed
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|RepositoryIndexException
throws|,
name|IOException
function_decl|;
comment|/**      * @see org.apache.maven.repository.indexing.RepositoryIndex#getAnalyzer()      */
specifier|public
name|Analyzer
name|getAnalyzer
parameter_list|()
block|{
if|if
condition|(
name|analyzer
operator|==
literal|null
condition|)
block|{
name|analyzer
operator|=
operator|new
name|ArtifactRepositoryIndexAnalyzer
argument_list|(
operator|new
name|SimpleAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|analyzer
return|;
block|}
comment|/**      * @see RepositoryIndex#isKeywordField(String)      */
specifier|public
name|boolean
name|isKeywordField
parameter_list|(
name|String
name|field
parameter_list|)
block|{
return|return
name|KEYWORD_FIELDS
operator|.
name|contains
argument_list|(
name|field
argument_list|)
return|;
block|}
comment|/**      * Method to test a zip entry if it is a java class, and adds it to the classes buffer      *      * @param entry   the zip entry to test for java class      * @param classes the String buffer to add the java class if the test result as true      * @return true if the zip entry is a java class and was successfully added to the buffer      */
specifier|protected
name|boolean
name|addIfClassEntry
parameter_list|(
name|ZipEntry
name|entry
parameter_list|,
name|StringBuffer
name|classes
parameter_list|)
block|{
name|boolean
name|isAdded
init|=
literal|false
decl_stmt|;
name|String
name|name
init|=
name|entry
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
comment|// TODO verify if class is public or protected
if|if
condition|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"$"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|idx
operator|=
literal|0
expr_stmt|;
block|}
name|String
name|classname
init|=
name|name
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|6
argument_list|)
decl_stmt|;
name|classes
operator|.
name|append
argument_list|(
name|classname
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|isAdded
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|isAdded
return|;
block|}
specifier|private
class|class
name|ArtifactRepositoryIndexAnalyzer
extends|extends
name|Analyzer
block|{
specifier|private
name|Analyzer
name|defaultAnalyzer
decl_stmt|;
comment|/**          * constructor to for this analyzer          *          * @param defaultAnalyzer the analyzer to use as default for the general fields of the artifact indeces          */
specifier|public
name|ArtifactRepositoryIndexAnalyzer
parameter_list|(
name|Analyzer
name|defaultAnalyzer
parameter_list|)
block|{
name|this
operator|.
name|defaultAnalyzer
operator|=
name|defaultAnalyzer
expr_stmt|;
block|}
comment|/**          * Method called by lucence during indexing operations          *          * @param fieldName the field name that the lucene object is currently processing          * @param reader    a Reader object to the index stream          * @return an analyzer to specific to the field name or the default analyzer if none is present          */
specifier|public
name|TokenStream
name|tokenStream
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|Reader
name|reader
parameter_list|)
block|{
name|TokenStream
name|tokenStream
decl_stmt|;
if|if
condition|(
name|RepositoryIndex
operator|.
name|FLD_VERSION
operator|.
name|equals
argument_list|(
name|fieldName
argument_list|)
operator|||
name|RepositoryIndex
operator|.
name|FLD_LASTUPDATE
operator|.
name|equals
argument_list|(
name|fieldName
argument_list|)
condition|)
block|{
name|tokenStream
operator|=
operator|new
name|VersionTokenizer
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tokenStream
operator|=
name|defaultAnalyzer
operator|.
name|tokenStream
argument_list|(
name|fieldName
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
return|return
name|tokenStream
return|;
block|}
comment|/**          * Class used to tokenize an artifact's version.          */
specifier|private
class|class
name|VersionTokenizer
extends|extends
name|CharTokenizer
block|{
comment|/**              * Constructor with the required reader to the index stream              *              * @param reader the Reader object of the index stream              */
name|VersionTokenizer
parameter_list|(
name|Reader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
comment|/**              * method that lucene calls to check tokenization of a stream character              *              * @param character char currently being processed              * @return true if the char is a token, false if the char is a stop char              */
specifier|protected
name|boolean
name|isTokenChar
parameter_list|(
name|char
name|character
parameter_list|)
block|{
return|return
name|character
operator|!=
literal|'.'
operator|&&
name|character
operator|!=
literal|'-'
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

