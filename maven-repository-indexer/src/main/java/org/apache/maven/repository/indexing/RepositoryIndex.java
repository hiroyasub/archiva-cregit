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
name|util
operator|.
name|Arrays
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
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryIndex
block|{
specifier|static
specifier|final
name|String
name|POM
init|=
literal|"POM"
decl_stmt|;
specifier|static
specifier|final
name|String
name|METADATA
init|=
literal|"METADATA"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ARTIFACT
init|=
literal|"ARTIFACT"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_ID
init|=
literal|"id"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_NAME
init|=
literal|"name"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_DOCTYPE
init|=
literal|"doctype"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_GROUPID
init|=
literal|"groupId"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_ARTIFACTID
init|=
literal|"artifactId"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_VERSION
init|=
literal|"version"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_PACKAGING
init|=
literal|"packaging"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_SHA1
init|=
literal|"sha1"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_MD5
init|=
literal|"md5"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_LASTUPDATE
init|=
literal|"last update"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_PLUGINPREFIX
init|=
literal|"plugin prefix"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_CLASSES
init|=
literal|"class"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_PACKAGES
init|=
literal|"package"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_FILES
init|=
literal|"file"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_LICENSE_URLS
init|=
literal|"license url"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_DEPENDENCIES
init|=
literal|"dependency"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_PLUGINS_BUILD
init|=
literal|"build plugin"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_PLUGINS_REPORT
init|=
literal|"report plugin"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLD_PLUGINS_ALL
init|=
literal|"plugins_all"
decl_stmt|;
specifier|static
specifier|final
name|String
index|[]
name|FIELDS
init|=
block|{
name|FLD_ID
block|,
name|FLD_NAME
block|,
name|FLD_DOCTYPE
block|,
name|FLD_GROUPID
block|,
name|FLD_ARTIFACTID
block|,
name|FLD_VERSION
block|,
name|FLD_PACKAGING
block|,
name|FLD_SHA1
block|,
name|FLD_MD5
block|,
name|FLD_LASTUPDATE
block|,
name|FLD_PLUGINPREFIX
block|,
name|FLD_CLASSES
block|,
name|FLD_PACKAGES
block|,
name|FLD_FILES
block|,
name|FLD_LICENSE_URLS
block|,
name|FLD_DEPENDENCIES
block|,
name|FLD_PLUGINS_BUILD
block|,
name|FLD_PLUGINS_REPORT
block|,
name|FLD_PLUGINS_ALL
block|}
decl_stmt|;
specifier|static
specifier|final
name|List
name|KEYWORD_FIELDS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|FLD_ID
block|,
name|FLD_PACKAGING
block|,
name|FLD_LICENSE_URLS
block|,
name|FLD_DEPENDENCIES
block|,
name|FLD_PLUGINS_BUILD
block|,
name|FLD_PLUGINS_REPORT
block|,
name|FLD_PLUGINS_ALL
block|}
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
index|[]
name|MODEL_FIELDS
init|=
block|{
name|FLD_PACKAGING
block|,
name|FLD_LICENSE_URLS
block|,
name|FLD_DEPENDENCIES
block|,
name|FLD_PLUGINS_BUILD
block|,
name|FLD_PLUGINS_REPORT
block|}
decl_stmt|;
name|ArtifactRepository
name|getRepository
parameter_list|()
function_decl|;
comment|/**      * Method to encapsulate the optimize() method for lucene      */
name|void
name|optimize
parameter_list|()
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Method to retrieve the lucene analyzer object used in creating the document fields for this index      *      * @return lucene Analyzer object used in creating the index fields      */
name|Analyzer
name|getAnalyzer
parameter_list|()
function_decl|;
comment|/**      * Method to retrieve the path where the index is made available      *      * @return the path where the index resides      */
name|File
name|getIndexPath
parameter_list|()
function_decl|;
comment|/**      * Tests an index field if it is a keyword field      *      * @param field the name of the index field to test      * @return true if the index field passed is a keyword, otherwise its false      */
name|boolean
name|isKeywordField
parameter_list|(
name|String
name|field
parameter_list|)
function_decl|;
comment|/**      * method for validating an index directory      *      * @throws RepositoryIndexException if the given indexPath is not valid for this type of RepositoryIndex      */
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|RepositoryIndexException
throws|,
name|IOException
function_decl|;
comment|/**      * Opens the lucene index and add all the lucene documents inside the list into the index.      * Closes the index at the end.      *      * @param docList List of Lucene Documents      * @throws RepositoryIndexException when an error occurred during the indexing of the documents      */
specifier|public
name|void
name|addDocuments
parameter_list|(
name|List
name|docList
parameter_list|)
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Delete from the index matching the list of lucene Terms      *      * @param termList List of Lucene Term      * @throws RepositoryIndexException      * @throws IOException      */
specifier|public
name|void
name|deleteDocuments
parameter_list|(
name|List
name|termList
parameter_list|)
throws|throws
name|RepositoryIndexException
throws|,
name|IOException
function_decl|;
block|}
end_interface

end_unit

