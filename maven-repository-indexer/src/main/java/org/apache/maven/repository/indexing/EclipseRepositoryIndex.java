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
name|document
operator|.
name|Field
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
name|FileInputStream
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
name|FileOutputStream
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
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|List
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
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipException
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
name|ZipFile
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
name|ZipOutputStream
import|;
end_import

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|EclipseRepositoryIndex
extends|extends
name|AbstractRepositoryIndex
block|{
specifier|private
specifier|static
specifier|final
name|String
name|JAR_NAME
init|=
literal|"j"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JAR_SIZE
init|=
literal|"s"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JAR_DATE
init|=
literal|"d"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAMES
init|=
literal|"c"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MD5
init|=
literal|"m"
decl_stmt|;
specifier|private
name|Digester
name|digester
decl_stmt|;
comment|/**      * Class constructor      *      * @param indexPath  the path where the lucene index will be created/updated.      * @param repository the repository where the indexed artifacts are located      * @param digester   the digester object to generate the checksum strings      */
specifier|public
name|EclipseRepositoryIndex
parameter_list|(
name|File
name|indexPath
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|Digester
name|digester
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|super
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|this
operator|.
name|digester
operator|=
name|digester
expr_stmt|;
block|}
comment|/**      * @see AbstractRepositoryIndex#getAnalyzer()      */
specifier|public
name|Analyzer
name|getAnalyzer
parameter_list|()
block|{
return|return
operator|new
name|EclipseIndexAnalyzer
argument_list|(
operator|new
name|SimpleAnalyzer
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|indexArtifacts
parameter_list|(
name|List
name|artifactList
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|List
name|docs
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|artifacts
init|=
name|artifactList
operator|.
name|iterator
argument_list|()
init|;
name|artifacts
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
name|artifacts
operator|.
name|next
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|createDocument
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|docs
operator|.
name|add
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
name|addDocuments
argument_list|(
name|docs
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method to index a given artifact for use with the eclipse plugin      *      * @param artifact the Artifact object to be indexed      * @throws RepositoryIndexException      */
specifier|public
name|void
name|indexArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|Document
name|doc
init|=
name|createDocument
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|addDocuments
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Document
name|createDocument
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|Document
name|doc
init|=
literal|null
decl_stmt|;
name|File
name|artifactFile
init|=
name|artifact
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifactFile
operator|!=
literal|null
operator|&&
name|artifactFile
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
operator|&&
name|artifactFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|String
name|md5
decl_stmt|;
try|try
block|{
name|md5
operator|=
name|digester
operator|.
name|createChecksum
argument_list|(
name|artifactFile
argument_list|,
literal|"MD5"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Unable to compute checksum."
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Unable to compute checksum."
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
name|RepositoryIndexException
argument_list|(
literal|"Unable to compute checksum."
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|StringBuffer
name|classes
decl_stmt|;
try|try
block|{
comment|// TODO: improve
name|classes
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"jar"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|ZipFile
name|jar
init|=
operator|new
name|ZipFile
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Enumeration
name|entries
init|=
name|jar
operator|.
name|entries
argument_list|()
init|;
name|entries
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|ZipEntry
name|entry
init|=
operator|(
name|ZipEntry
operator|)
name|entries
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|addIfClassEntry
argument_list|(
name|entry
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ZipException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Error reading from artifact file: "
operator|+
name|artifact
operator|.
name|getFile
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
name|RepositoryIndexException
argument_list|(
literal|"Error reading from artifact file"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|doc
operator|=
operator|new
name|Document
argument_list|()
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|MD5
argument_list|,
name|md5
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|JAR_NAME
argument_list|,
name|artifactFile
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|JAR_DATE
argument_list|,
name|DateField
operator|.
name|timeToString
argument_list|(
name|artifactFile
operator|.
name|lastModified
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|JAR_SIZE
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|artifactFile
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|NAMES
argument_list|,
name|classes
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|doc
return|;
block|}
comment|/**      * method to create an archived copy of the index contents      *      * @return File object to the archive      * @throws IOException      */
specifier|public
name|File
name|getCompressedCopy
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|indexPath
init|=
name|getIndexPath
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|indexPath
operator|.
name|getName
argument_list|()
decl_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|indexPath
operator|.
name|getParent
argument_list|()
argument_list|,
name|name
operator|+
literal|".zip"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|fileDelete
argument_list|(
name|outputFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|ZipOutputStream
name|zos
init|=
operator|new
name|ZipOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outputFile
argument_list|)
argument_list|)
decl_stmt|;
name|zos
operator|.
name|setLevel
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|File
index|[]
name|files
init|=
name|indexPath
operator|.
name|listFiles
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|writeFile
argument_list|(
name|zos
argument_list|,
name|files
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|zos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|outputFile
return|;
block|}
specifier|private
specifier|static
name|void
name|writeFile
parameter_list|(
name|ZipOutputStream
name|zos
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|ZipEntry
name|e
init|=
operator|new
name|ZipEntry
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|zos
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|FileInputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|IOUtil
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|zos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|zos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|zos
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
block|}
comment|/**      * Class used to analyze the lucene index      */
specifier|private
specifier|static
class|class
name|EclipseIndexAnalyzer
extends|extends
name|Analyzer
block|{
specifier|private
name|Analyzer
name|defaultAnalyzer
decl_stmt|;
comment|/**          * constructor to for this analyzer          *          * @param defaultAnalyzer the analyzer to use as default for the general fields of the artifact indeces          */
name|EclipseIndexAnalyzer
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
literal|"s"
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
name|EclipseIndexTokenizer
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
block|}
comment|/**      * Class used to tokenize the eclipse index      */
specifier|private
specifier|static
class|class
name|EclipseIndexTokenizer
extends|extends
name|CharTokenizer
block|{
comment|/**          * Constructor with the required reader to the index stream          *          * @param reader the Reader object of the index stream          */
name|EclipseIndexTokenizer
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
comment|/**          * method that lucene calls to check tokenization of a stream character          *          * @param character char currently being processed          * @return true if the char is a token, false if the char is a stop char          */
specifier|protected
name|boolean
name|isTokenChar
parameter_list|(
name|char
name|character
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

