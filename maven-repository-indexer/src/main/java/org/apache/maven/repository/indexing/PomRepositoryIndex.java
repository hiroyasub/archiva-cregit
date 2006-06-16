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
name|model
operator|.
name|Dependency
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
name|model
operator|.
name|License
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
name|model
operator|.
name|Model
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
name|model
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
name|model
operator|.
name|ReportPlugin
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
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|digest
operator|.
name|DigesterException
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
name|StringUtils
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
comment|/**  * Class to create index entries for a given pom in a repository  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|PomRepositoryIndex
extends|extends
name|AbstractRepositoryIndex
block|{
specifier|private
name|Digester
name|digester
decl_stmt|;
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * Class Constructor      *      * @param indexPath       the path where the index is available or will be made available      * @param repository      the repository where objects indexed by this class resides      * @param digester        the digester to be used for generating checksums      * @param artifactFactory the factory for building artifact objects      */
specifier|public
name|PomRepositoryIndex
parameter_list|(
name|File
name|indexPath
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|Digester
name|digester
parameter_list|,
name|ArtifactFactory
name|artifactFactory
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
name|this
operator|.
name|artifactFactory
operator|=
name|artifactFactory
expr_stmt|;
block|}
comment|/**      * Method to create the index fields for a Model object into the index      *      * @param pom the Model object to be indexed      * @throws RepositoryIndexException      */
specifier|public
name|void
name|indexPom
parameter_list|(
name|Model
name|pom
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|indexPoms
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|pom
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Index the Models within the supplied List.  Deletes existing index values before adding them to the list.      *      * @param pomList      * @throws RepositoryIndexException      */
specifier|public
name|void
name|indexPoms
parameter_list|(
name|List
name|pomList
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
name|pomList
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
name|pomList
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a list of Lucene Term object used in index deletion      *      * @param pomList      * @return List of Term object      */
specifier|private
name|List
name|getTermList
parameter_list|(
name|List
name|pomList
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
name|poms
init|=
name|pomList
operator|.
name|iterator
argument_list|()
init|;
name|poms
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Model
name|pom
init|=
operator|(
name|Model
operator|)
name|poms
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
name|POM
operator|+
literal|":"
operator|+
name|pom
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|terms
return|;
block|}
comment|/**      * Creates a list of Lucene documents      *      * @param pomList      * @return      * @throws RepositoryIndexException      */
specifier|private
name|List
name|getDocumentList
parameter_list|(
name|List
name|pomList
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
name|poms
init|=
name|pomList
operator|.
name|iterator
argument_list|()
init|;
name|poms
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Model
name|pom
init|=
operator|(
name|Model
operator|)
name|poms
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
name|pom
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|docs
return|;
block|}
comment|/**      * Creates a Lucene Document from a Model; used for index additions      *      * @param pom      * @return      * @throws RepositoryIndexException      */
specifier|private
name|Document
name|createDocument
parameter_list|(
name|Model
name|pom
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
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
name|POM
operator|+
literal|":"
operator|+
name|pom
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
name|FLD_GROUPID
argument_list|,
name|pom
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
name|pom
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
name|pom
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
name|Keyword
argument_list|(
name|FLD_PACKAGING
argument_list|,
name|pom
operator|.
name|getPackaging
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
name|pom
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|pom
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|pom
operator|.
name|getVersion
argument_list|()
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|File
name|pomFile
init|=
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
decl_stmt|;
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
name|getChecksum
argument_list|(
name|Digester
operator|.
name|SHA1
argument_list|,
name|pomFile
operator|.
name|getAbsolutePath
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
name|FLD_MD5
argument_list|,
name|getChecksum
argument_list|(
name|Digester
operator|.
name|MD5
argument_list|,
name|pomFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|indexLicenseUrls
argument_list|(
name|doc
argument_list|,
name|pom
argument_list|)
expr_stmt|;
name|indexDependencies
argument_list|(
name|doc
argument_list|,
name|pom
argument_list|)
expr_stmt|;
name|boolean
name|hasPlugins
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|pom
operator|.
name|getBuild
argument_list|()
operator|!=
literal|null
operator|&&
name|pom
operator|.
name|getBuild
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|!=
literal|null
operator|&&
name|pom
operator|.
name|getBuild
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|hasPlugins
operator|=
literal|true
expr_stmt|;
name|indexPlugins
argument_list|(
name|doc
argument_list|,
name|FLD_PLUGINS_BUILD
argument_list|,
name|pom
operator|.
name|getBuild
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|indexPlugins
argument_list|(
name|doc
argument_list|,
name|FLD_PLUGINS_ALL
argument_list|,
name|pom
operator|.
name|getBuild
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|FLD_PLUGINS_BUILD
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pom
operator|.
name|getReporting
argument_list|()
operator|!=
literal|null
operator|&&
name|pom
operator|.
name|getReporting
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|!=
literal|null
operator|&&
name|pom
operator|.
name|getReporting
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|hasPlugins
operator|=
literal|true
expr_stmt|;
name|indexReportPlugins
argument_list|(
name|doc
argument_list|,
name|FLD_PLUGINS_REPORT
argument_list|,
name|pom
operator|.
name|getReporting
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|indexReportPlugins
argument_list|(
name|doc
argument_list|,
name|FLD_PLUGINS_ALL
argument_list|,
name|pom
operator|.
name|getReporting
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|FLD_PLUGINS_REPORT
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasPlugins
condition|)
block|{
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Text
argument_list|(
name|FLD_PLUGINS_ALL
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|POM
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: do we need to add all these empty fields?
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
name|FLD_NAME
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
name|Field
operator|.
name|Keyword
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
name|Field
operator|.
name|Text
argument_list|(
name|FLD_FILES
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
comment|/**      * Method to index license urls found inside the passed pom      *      * @param doc the index object to create the fields for the license urls      * @param pom the Model object to be indexed      */
specifier|private
name|void
name|indexLicenseUrls
parameter_list|(
name|Document
name|doc
parameter_list|,
name|Model
name|pom
parameter_list|)
block|{
name|List
name|licenseList
init|=
name|pom
operator|.
name|getLicenses
argument_list|()
decl_stmt|;
if|if
condition|(
name|licenseList
operator|!=
literal|null
operator|&&
name|licenseList
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Iterator
name|licenses
init|=
name|licenseList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|licenses
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|License
name|license
init|=
operator|(
name|License
operator|)
name|licenses
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|url
init|=
name|license
operator|.
name|getUrl
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|url
argument_list|)
condition|)
block|{
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
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
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
block|}
block|}
comment|/**      * Method to index declared dependencies found inside the passed pom      *      * @param doc the index object to create the fields for the dependencies      * @param pom the Model object to be indexed      */
specifier|private
name|void
name|indexDependencies
parameter_list|(
name|Document
name|doc
parameter_list|,
name|Model
name|pom
parameter_list|)
block|{
name|List
name|dependencyList
init|=
name|pom
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
if|if
condition|(
name|dependencyList
operator|!=
literal|null
operator|&&
name|dependencyList
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Iterator
name|dependencies
init|=
name|dependencyList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dependencies
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Dependency
name|dep
init|=
operator|(
name|Dependency
operator|)
name|dependencies
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|getId
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dep
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
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
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
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
block|}
block|}
comment|/**      * Method to index plugins to a specified index field      *      * @param doc     the index object to create the fields for the plugins      * @param field   the index field to store the passed plugin      * @param plugins the iterator to the list of plugins to be indexed      */
specifier|private
name|void
name|indexPlugins
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|field
parameter_list|,
name|Iterator
name|plugins
parameter_list|)
block|{
while|while
condition|(
name|plugins
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Plugin
name|plugin
init|=
operator|(
name|Plugin
operator|)
name|plugins
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|getId
argument_list|(
name|plugin
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|plugin
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|plugin
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Keyword
argument_list|(
name|field
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method to index report plugins to a specified index field      *      * @param doc     the index object to create the fields for the report plugins      * @param field   the index field to store the passed report plugin      * @param plugins the iterator to the list of report plugins to be indexed      */
specifier|private
name|void
name|indexReportPlugins
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|field
parameter_list|,
name|Iterator
name|plugins
parameter_list|)
block|{
while|while
condition|(
name|plugins
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ReportPlugin
name|plugin
init|=
operator|(
name|ReportPlugin
operator|)
name|plugins
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|getId
argument_list|(
name|plugin
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|plugin
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|plugin
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
name|Field
operator|.
name|Keyword
argument_list|(
name|field
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method to generate the computed checksum of an existing file using the specified algorithm.      *      * @param algorithm the algorithm to be used to generate the checksum      * @param file      the file to match the generated checksum      * @return a string representing the checksum      * @throws RepositoryIndexException      */
specifier|private
name|String
name|getChecksum
parameter_list|(
name|String
name|algorithm
parameter_list|,
name|String
name|file
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
try|try
block|{
return|return
name|digester
operator|.
name|createChecksum
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|,
name|algorithm
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Failed to create checksum"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Method to create the unique artifact id to represent the artifact in the repository      *      * @param groupId    the artifact groupId      * @param artifactId the artifact artifactId      * @param version    the artifact version      * @return the String id to uniquely represent the artifact      */
specifier|private
name|String
name|getId
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
return|;
block|}
block|}
end_class

end_unit

