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
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|PomRepositoryIndex
extends|extends
name|AbstractRepositoryIndex
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_GROUPID
init|=
literal|"groupId"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_ARTIFACTID
init|=
literal|"artifactId"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_VERSION
init|=
literal|"version"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_PACKAGING
init|=
literal|"packaging"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_LICENSE_URLS
init|=
literal|"license_urls"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_DEPENDENCIES
init|=
literal|"dependencies"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_PLUGINS_BUILD
init|=
literal|"plugins_build"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_PLUGINS_REPORT
init|=
literal|"plugins_report"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_PLUGINS_ALL
init|=
literal|"plugins_all"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_SHA1
init|=
literal|"sha1"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|FLD_MD5
init|=
literal|"md5"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|FIELDS
init|=
block|{
name|FLD_GROUPID
block|,
name|FLD_ARTIFACTID
block|,
name|FLD_VERSION
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
decl_stmt|;
specifier|private
name|Analyzer
name|analyzer
decl_stmt|;
specifier|private
name|Digester
name|digester
decl_stmt|;
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|public
name|PomRepositoryIndex
parameter_list|(
name|String
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
name|Model
condition|)
block|{
name|indexPom
argument_list|(
operator|(
name|Model
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
name|indexPom
parameter_list|(
name|Model
name|pom
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
literal|"Unable to add pom index on a closed index"
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
specifier|public
name|boolean
name|isKeywordField
parameter_list|(
name|String
name|field
parameter_list|)
block|{
name|boolean
name|keyword
decl_stmt|;
if|if
condition|(
name|field
operator|.
name|equals
argument_list|(
name|PomRepositoryIndex
operator|.
name|FLD_LICENSE_URLS
argument_list|)
condition|)
block|{
name|keyword
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|field
operator|.
name|equals
argument_list|(
name|PomRepositoryIndex
operator|.
name|FLD_DEPENDENCIES
argument_list|)
condition|)
block|{
name|keyword
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|field
operator|.
name|equals
argument_list|(
name|PomRepositoryIndex
operator|.
name|FLD_PLUGINS_BUILD
argument_list|)
condition|)
block|{
name|keyword
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|field
operator|.
name|equals
argument_list|(
name|PomRepositoryIndex
operator|.
name|FLD_PLUGINS_REPORT
argument_list|)
condition|)
block|{
name|keyword
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|field
operator|.
name|equals
argument_list|(
name|PomRepositoryIndex
operator|.
name|FLD_PLUGINS_ALL
argument_list|)
condition|)
block|{
name|keyword
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|keyword
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|keyword
return|;
block|}
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

