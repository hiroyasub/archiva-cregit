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
name|metadata
operator|.
name|Metadata
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
name|metadata
operator|.
name|Plugin
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
name|metadata
operator|.
name|RepositoryMetadata
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
name|metadata
operator|.
name|Versioning
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
name|Collections
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
comment|/**  * This class indexes the metadata in the repository.  */
end_comment

begin_class
specifier|public
class|class
name|MetadataRepositoryIndex
extends|extends
name|AbstractRepositoryIndex
block|{
comment|/**      * Class Constructor      *      * @param indexPath  the path to the index      * @param repository the repository where the metadata to be indexed is located      */
specifier|public
name|MetadataRepositoryIndex
parameter_list|(
name|File
name|indexPath
parameter_list|,
name|ArtifactRepository
name|repository
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
block|}
comment|/**      * Index the contents of the specified RepositoryMetadata parameter object      *      * @param repoMetadata the metadata object to be indexed      * @throws RepositoryIndexException      */
specifier|public
name|void
name|indexMetadata
parameter_list|(
name|RepositoryMetadata
name|repoMetadata
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|indexMetadata
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Index the metadata found within the provided list.  Deletes existing entries in the index first before      * proceeding with the index additions.      *      * @param metadataList      * @throws RepositoryIndexException      */
specifier|public
name|void
name|indexMetadata
parameter_list|(
name|List
name|metadataList
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
try|try
block|{
name|deleteDocuments
argument_list|(
name|getTermList
argument_list|(
name|metadataList
argument_list|)
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
literal|"Failed to delete an index document"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|addDocuments
argument_list|(
name|getDocumentList
argument_list|(
name|metadataList
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a list of Lucene Term object used in index deletion      *      * @param metadataList      * @return List of Term object      */
specifier|private
name|List
name|getTermList
parameter_list|(
name|List
name|metadataList
parameter_list|)
block|{
name|List
name|terms
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|metadata
init|=
name|metadataList
operator|.
name|iterator
argument_list|()
init|;
name|metadata
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryMetadata
name|repoMetadata
init|=
operator|(
name|RepositoryMetadata
operator|)
name|metadata
operator|.
name|next
argument_list|()
decl_stmt|;
name|terms
operator|.
name|add
argument_list|(
operator|new
name|Term
argument_list|(
name|FLD_ID
argument_list|,
operator|(
name|String
operator|)
name|repoMetadata
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|terms
return|;
block|}
comment|/**      * Creates a list of Lucene documents      *      * @param metadataList      * @return List of Lucene Documents      */
specifier|private
name|List
name|getDocumentList
parameter_list|(
name|List
name|metadataList
parameter_list|)
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
name|metadata
init|=
name|metadataList
operator|.
name|iterator
argument_list|()
init|;
name|metadata
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryMetadata
name|repoMetadata
init|=
operator|(
name|RepositoryMetadata
operator|)
name|metadata
operator|.
name|next
argument_list|()
decl_stmt|;
name|docs
operator|.
name|add
argument_list|(
name|createDocument
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|docs
return|;
block|}
comment|/**      * Creates a Lucene Document from a RepositoryMetadata; used for index additions      *      * @param repoMetadata      * @return Lucene Document      */
specifier|private
name|Document
name|createDocument
parameter_list|(
name|RepositoryMetadata
name|repoMetadata
parameter_list|)
block|{
comment|//get lastUpdated from Versioning (specified in Metadata object)
comment|//get pluginPrefixes from Plugin (spcified in Metadata object) -----> concatenate/append???
comment|//get the metadatapath: check where metadata is located, then concatenate the groupId,
comment|// artifactId, version based on its location
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
name|createKeywordField
argument_list|(
name|FLD_ID
argument_list|,
operator|(
name|String
operator|)
name|repoMetadata
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Metadata
name|metadata
init|=
name|repoMetadata
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_NAME
argument_list|,
name|repository
operator|.
name|pathOfRemoteRepositoryMetadata
argument_list|(
name|repoMetadata
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Versioning
name|versioning
init|=
name|metadata
operator|.
name|getVersioning
argument_list|()
decl_stmt|;
if|if
condition|(
name|versioning
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_LASTUPDATE
argument_list|,
name|versioning
operator|.
name|getLastUpdated
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_LASTUPDATE
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
name|plugins
init|=
name|metadata
operator|.
name|getPlugins
argument_list|()
decl_stmt|;
name|String
name|pluginAppended
init|=
literal|""
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|plugins
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
name|Plugin
name|plugin
init|=
operator|(
name|Plugin
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|plugin
operator|.
name|getPrefix
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|plugin
operator|.
name|getPrefix
argument_list|()
argument_list|)
condition|)
block|{
name|pluginAppended
operator|=
name|plugin
operator|.
name|getPrefix
argument_list|()
operator|+
literal|"\n"
expr_stmt|;
block|}
block|}
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_PLUGINPREFIX
argument_list|,
name|pluginAppended
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|metadata
operator|.
name|getGroupId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_GROUPID
argument_list|,
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_GROUPID
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|metadata
operator|.
name|getArtifactId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_ARTIFACTID
argument_list|,
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_ARTIFACTID
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|metadata
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_VERSION
argument_list|,
name|metadata
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_VERSION
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO: do we need to add all these empty fields?
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_DOCTYPE
argument_list|,
name|METADATA
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|createKeywordField
argument_list|(
name|FLD_PACKAGING
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_SHA1
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_MD5
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_CLASSES
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_PACKAGES
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|createTextField
argument_list|(
name|FLD_FILES
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|createKeywordField
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
name|createKeywordField
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
name|createKeywordField
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
name|createKeywordField
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
name|createKeywordField
argument_list|(
name|FLD_PLUGINS_ALL
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
specifier|private
specifier|static
name|Field
name|createTextField
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|Field
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|Field
operator|.
name|Store
operator|.
name|YES
argument_list|,
name|Field
operator|.
name|Index
operator|.
name|TOKENIZED
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Field
name|createKeywordField
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|Field
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|Field
operator|.
name|Store
operator|.
name|YES
argument_list|,
name|Field
operator|.
name|Index
operator|.
name|UN_TOKENIZED
argument_list|)
return|;
block|}
block|}
end_class

end_unit

