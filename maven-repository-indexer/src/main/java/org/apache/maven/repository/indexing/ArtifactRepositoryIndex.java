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
comment|/**  * Class used to index Artifact objects in a specified repository  *  * @author Edwin Punzalan  * @plexus.component role="org.apache.maven.repository.indexing.RepositoryIndex" role-hint="artifact" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactRepositoryIndex
extends|extends
name|AbstractRepositoryIndex
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
name|Analyzer
name|analyzer
decl_stmt|;
comment|/** @plexus.requirement */
specifier|private
name|Digester
name|digester
decl_stmt|;
comment|/**      * method to get the Analyzer used to create indices      *      * @return the Analyzer object used to create the artifact indices      */
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
comment|/**      * method for collecting the available index fields usable for searching      *      * @return index field names      */
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
comment|/**      * generic method for indexing      *      * @param obj the object to be indexed by this indexer      */
specifier|public
name|void
name|index
parameter_list|(
name|Object
name|obj
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Artifact
condition|)
block|{
name|indexArtifact
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
name|RepositoryIndexException
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
comment|/**      * method to index a given artifact      *      * @param artifact the Artifact object to be indexed      */
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
if|if
condition|(
operator|!
name|isOpen
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Unable to add artifact index on a closed index"
argument_list|)
throw|;
block|}
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
name|ZipFile
name|jar
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
name|jar
operator|=
operator|new
name|ZipFile
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
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
name|ZipException
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
name|MD5
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
try|try
block|{
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

