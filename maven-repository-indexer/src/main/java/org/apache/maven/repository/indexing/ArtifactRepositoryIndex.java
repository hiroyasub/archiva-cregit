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
name|IOException
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

begin_comment
comment|/**  * Class used to index Artifact objects in a specific repository  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactRepositoryIndex
extends|extends
name|AbstractRepositoryIndex
block|{
specifier|private
name|Digester
name|digester
decl_stmt|;
comment|/**      * Class constructor      *      * @param indexPath  the path where the lucene index will be created/updated.      * @param repository the repository where the indexed artifacts are located      * @param digester   the digester object to generate the checksum strings      * @throws RepositoryIndexException      */
specifier|public
name|ArtifactRepositoryIndex
parameter_list|(
name|String
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
comment|/**      * @see AbstractRepositoryIndex#deleteIfIndexed(Object)      */
specifier|public
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
block|{
if|if
condition|(
name|object
operator|instanceof
name|Artifact
condition|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|indexExists
argument_list|()
condition|)
block|{
name|validateIndex
argument_list|(
name|FIELDS
argument_list|)
expr_stmt|;
name|deleteDocument
argument_list|(
name|FLD_ID
argument_list|,
name|ARTIFACT
operator|+
literal|":"
operator|+
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Object is not of type artifact."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Method to index a given artifact      *      * @param artifact the Artifact object to be indexed      * @throws RepositoryIndexException      */
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
name|StringBuffer
name|classes
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|StringBuffer
name|packages
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|StringBuffer
name|files
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|sha1sum
decl_stmt|;
name|String
name|md5sum
decl_stmt|;
try|try
block|{
name|sha1sum
operator|=
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
expr_stmt|;
name|md5sum
operator|=
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
expr_stmt|;
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
literal|"Unable to create a checksum"
argument_list|,
name|e
argument_list|)
throw|;
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
literal|"Error reading from artifact file"
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
try|try
block|{
comment|// TODO: improve
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
if|if
condition|(
name|addIfClassEntry
argument_list|(
name|entry
argument_list|,
name|classes
argument_list|)
condition|)
block|{
name|addClassPackage
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|,
name|packages
argument_list|)
expr_stmt|;
block|}
name|addFile
argument_list|(
name|entry
argument_list|,
name|files
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
name|Document
name|doc
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Keyword
argument_list|(
name|FLD_ID
argument_list|,
name|ARTIFACT
operator|+
literal|":"
operator|+
name|artifact
operator|.
name|getId
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
name|FLD_NAME
argument_list|,
name|artifact
operator|.
name|getFile
argument_list|()
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
name|FLD_GROUPID
argument_list|,
name|artifact
operator|.
name|getGroupId
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
name|FLD_ARTIFACTID
argument_list|,
name|artifact
operator|.
name|getArtifactId
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
name|FLD_VERSION
argument_list|,
name|artifact
operator|.
name|getVersion
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
name|FLD_SHA1
argument_list|,
name|sha1sum
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
name|FLD_MD5
argument_list|,
name|md5sum
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
name|FLD_CLASSES
argument_list|,
name|classes
operator|.
name|toString
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
name|FLD_PACKAGES
argument_list|,
name|packages
operator|.
name|toString
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
name|FLD_FILES
argument_list|,
name|files
operator|.
name|toString
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
name|UnIndexed
argument_list|(
name|FLD_DOCTYPE
argument_list|,
name|ARTIFACT
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
name|FLD_LASTUPDATE
argument_list|,
literal|""
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
name|FLD_PLUGINPREFIX
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Keyword
argument_list|(
name|FLD_LICENSE_URLS
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Keyword
argument_list|(
name|FLD_DEPENDENCIES
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Keyword
argument_list|(
name|FLD_PLUGINS_REPORT
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Keyword
argument_list|(
name|FLD_PLUGINS_BUILD
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Keyword
argument_list|(
name|FLD_PLUGINS_ALL
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|i
init|=
name|artifact
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|FLD_PACKAGING
argument_list|,
name|artifact
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|deleteIfIndexed
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isOpen
argument_list|()
condition|)
block|{
name|open
argument_list|()
expr_stmt|;
block|}
name|getIndexWriter
argument_list|()
operator|.
name|addDocument
argument_list|(
name|doc
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
literal|"Error opening index"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Method to test a zip entry if it is a java class, and adds it to the classes buffer      *      * @param entry   the zip entry to test for java class      * @param classes the String buffer to add the java class if the test result as true      * @return true if the zip entry is a java class and was successfully added to the buffer      */
specifier|private
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
comment|/**      * Method to add a class package to the buffer of packages      *      * @param name     the complete path name of the class      * @param packages the packages buffer      * @return true if the package is successfully added      */
specifier|private
name|boolean
name|addClassPackage
parameter_list|(
name|String
name|name
parameter_list|,
name|StringBuffer
name|packages
parameter_list|)
block|{
name|boolean
name|isAdded
init|=
literal|false
decl_stmt|;
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
operator|>
literal|0
condition|)
block|{
name|String
name|packageName
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
operator|+
literal|"\n"
decl_stmt|;
if|if
condition|(
name|packages
operator|.
name|indexOf
argument_list|(
name|packageName
argument_list|)
operator|<
literal|0
condition|)
block|{
name|packages
operator|.
name|append
argument_list|(
name|packageName
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|isAdded
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|isAdded
return|;
block|}
comment|/**      * Method to add the zip entry as a file list      *      * @param entry the zip entry to be added      * @param files the buffer of files to update      * @return true if the file was successfully added      */
specifier|private
name|boolean
name|addFile
parameter_list|(
name|ZipEntry
name|entry
parameter_list|,
name|StringBuffer
name|files
parameter_list|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getName
argument_list|()
decl_stmt|;
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
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isAdded
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|files
operator|.
name|indexOf
argument_list|(
name|name
operator|+
literal|"\n"
argument_list|)
operator|<
literal|0
condition|)
block|{
name|files
operator|.
name|append
argument_list|(
name|name
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
return|return
name|isAdded
return|;
block|}
block|}
end_class

end_unit

