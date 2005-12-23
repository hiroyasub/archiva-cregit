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
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0    *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
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
name|Collection
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

begin_comment
comment|/**  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactRepositoryIndexer
extends|extends
name|AbstractRepositoryIndexer
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
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
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|FIELDS
init|=
block|{
name|NAME
block|,
name|GROUPID
block|,
name|ARTIFACTID
block|,
name|VERSION
block|,
name|SHA1
block|,
name|MD5
block|,
name|CLASSES
block|,
name|PACKAGES
block|,
name|FILES
block|}
decl_stmt|;
specifier|private
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|private
name|StringBuffer
name|classes
decl_stmt|;
specifier|private
name|StringBuffer
name|packages
decl_stmt|;
specifier|private
name|StringBuffer
name|files
decl_stmt|;
specifier|public
name|ArtifactRepositoryIndexer
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|RepositoryIndexerException
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|indexPath
operator|=
name|path
expr_stmt|;
name|validateIndex
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getIndexFields
parameter_list|()
block|{
return|return
name|FIELDS
return|;
block|}
specifier|public
name|void
name|addObjectIndex
parameter_list|(
name|Object
name|obj
parameter_list|)
throws|throws
name|RepositoryIndexerException
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Artifact
condition|)
block|{
name|addArtifactIndex
argument_list|(
operator|(
name|Artifact
operator|)
name|obj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RepositoryIndexerException
argument_list|(
literal|"This instance of indexer cannot index instances of "
operator|+
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|addArtifactIndex
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|RepositoryIndexerException
block|{
if|if
condition|(
operator|!
name|isOpen
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RepositoryIndexerException
argument_list|(
literal|"Unable to add artifact index on a closed index"
argument_list|)
throw|;
block|}
try|try
block|{
name|getIndexWriter
argument_list|()
expr_stmt|;
name|processArtifactContents
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
comment|//@todo should some of these fields be Keyword instead of Text ?
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
name|Text
argument_list|(
name|NAME
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
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|GROUPID
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
name|ARTIFACTID
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
name|VERSION
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
name|SHA1
argument_list|,
name|getSha1
argument_list|(
name|artifact
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
name|MD5
argument_list|,
name|getMd5
argument_list|(
name|artifact
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
name|CLASSES
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
name|PACKAGES
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
name|FILES
argument_list|,
name|files
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|indexWriter
operator|.
name|addDocument
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|removeBuffers
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexerException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|getSha1
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
throws|,
name|NoSuchAlgorithmException
block|{
name|FileInputStream
name|fIn
init|=
operator|new
name|FileInputStream
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|getChecksum
argument_list|(
name|fIn
argument_list|,
literal|"SHA-1"
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|getMd5
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
throws|,
name|NoSuchAlgorithmException
block|{
name|FileInputStream
name|fIn
init|=
operator|new
name|FileInputStream
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|getChecksum
argument_list|(
name|fIn
argument_list|,
literal|"MD5"
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|byte
index|[]
name|getChecksum
parameter_list|(
name|InputStream
name|inStream
parameter_list|,
name|String
name|algorithm
parameter_list|)
throws|throws
name|IOException
throws|,
name|NoSuchAlgorithmException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|256
index|]
decl_stmt|;
name|MessageDigest
name|complete
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
name|int
name|numRead
decl_stmt|;
do|do
block|{
name|numRead
operator|=
name|inStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
if|if
condition|(
name|numRead
operator|>
literal|0
condition|)
block|{
name|complete
operator|.
name|update
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|numRead
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|numRead
operator|!=
operator|-
literal|1
condition|)
do|;
name|inStream
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|complete
operator|.
name|digest
argument_list|()
return|;
block|}
specifier|private
name|void
name|initBuffers
parameter_list|()
block|{
name|classes
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
name|packages
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
name|files
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|removeBuffers
parameter_list|()
block|{
name|classes
operator|=
literal|null
expr_stmt|;
name|packages
operator|=
literal|null
expr_stmt|;
name|files
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|void
name|processArtifactContents
parameter_list|(
name|File
name|artifact
parameter_list|)
throws|throws
name|IOException
throws|,
name|ZipException
block|{
name|initBuffers
argument_list|()
expr_stmt|;
name|ZipFile
name|jar
init|=
operator|new
name|ZipFile
argument_list|(
name|artifact
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
argument_list|)
condition|)
block|{
name|addClassPackage
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addFile
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|addIfClassEntry
parameter_list|(
name|ZipEntry
name|entry
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
name|idx
operator|=
literal|0
expr_stmt|;
name|String
name|classname
init|=
name|name
operator|.
name|substring
argument_list|(
name|idx
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
name|boolean
name|addClassPackage
parameter_list|(
name|String
name|name
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
specifier|private
name|boolean
name|addFile
parameter_list|(
name|ZipEntry
name|entry
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

