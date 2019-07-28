begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|metadata
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaRepositoryMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
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
name|archiva
operator|.
name|repository
operator|.
name|storage
operator|.
name|StorageAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|xml
operator|.
name|XMLException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|xml
operator|.
name|XMLWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections4
operator|.
name|CollectionUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|DocumentHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|Comparator
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
comment|/**  * RepositoryMetadataWriter  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryMetadataWriter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RepositoryMetadataWriter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|void
name|write
parameter_list|(
name|ArchivaRepositoryMetadata
name|metadata
parameter_list|,
name|StorageAsset
name|outputFile
parameter_list|)
throws|throws
name|RepositoryMetadataException
block|{
name|boolean
name|thrown
init|=
literal|false
decl_stmt|;
try|try
init|(
name|OutputStreamWriter
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|outputFile
operator|.
name|getWriteStream
argument_list|(
literal|true
argument_list|)
argument_list|)
init|)
block|{
name|write
argument_list|(
name|metadata
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|thrown
operator|=
literal|true
expr_stmt|;
throw|throw
operator|new
name|RepositoryMetadataException
argument_list|(
literal|"Unable to write metadata file: "
operator|+
name|outputFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" - "
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
if|if
condition|(
name|thrown
condition|)
block|{
try|try
block|{
name|outputFile
operator|.
name|getStorage
argument_list|()
operator|.
name|removeAsset
argument_list|(
name|outputFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not remove asset {}"
argument_list|,
name|outputFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|write
parameter_list|(
name|ArchivaRepositoryMetadata
name|metadata
parameter_list|,
name|Writer
name|writer
parameter_list|)
throws|throws
name|RepositoryMetadataException
block|{
name|Document
name|doc
init|=
name|DocumentHelper
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|root
init|=
name|DocumentHelper
operator|.
name|createElement
argument_list|(
literal|"metadata"
argument_list|)
decl_stmt|;
name|doc
operator|.
name|setRootElement
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|addOptionalElementText
argument_list|(
name|root
argument_list|,
literal|"groupId"
argument_list|,
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|addOptionalElementText
argument_list|(
name|root
argument_list|,
literal|"artifactId"
argument_list|,
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|addOptionalElementText
argument_list|(
name|root
argument_list|,
literal|"version"
argument_list|,
name|metadata
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|metadata
operator|.
name|getPlugins
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|plugins
init|=
name|root
operator|.
name|addElement
argument_list|(
literal|"plugins"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Plugin
argument_list|>
name|pluginList
init|=
name|metadata
operator|.
name|getPlugins
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|pluginList
argument_list|,
name|PluginComparator
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
for|for
control|(
name|Plugin
name|plugin
range|:
name|metadata
operator|.
name|getPlugins
argument_list|()
control|)
block|{
name|Element
name|p
init|=
name|plugins
operator|.
name|addElement
argument_list|(
literal|"plugin"
argument_list|)
decl_stmt|;
name|p
operator|.
name|addElement
argument_list|(
literal|"prefix"
argument_list|)
operator|.
name|setText
argument_list|(
name|plugin
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|addElement
argument_list|(
literal|"artifactId"
argument_list|)
operator|.
name|setText
argument_list|(
name|plugin
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|addOptionalElementText
argument_list|(
name|p
argument_list|,
literal|"name"
argument_list|,
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
argument_list|)
comment|//
operator|||
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|metadata
operator|.
name|getReleasedVersion
argument_list|()
argument_list|)
comment|//
operator|||
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|metadata
operator|.
name|getLatestVersion
argument_list|()
argument_list|)
comment|//
operator|||
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|metadata
operator|.
name|getLastUpdated
argument_list|()
argument_list|)
comment|//
operator|||
operator|(
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
name|Element
name|versioning
init|=
name|root
operator|.
name|addElement
argument_list|(
literal|"versioning"
argument_list|)
decl_stmt|;
name|addOptionalElementText
argument_list|(
name|versioning
argument_list|,
literal|"latest"
argument_list|,
name|metadata
operator|.
name|getLatestVersion
argument_list|()
argument_list|)
expr_stmt|;
name|addOptionalElementText
argument_list|(
name|versioning
argument_list|,
literal|"release"
argument_list|,
name|metadata
operator|.
name|getReleasedVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Element
name|snapshot
init|=
name|versioning
operator|.
name|addElement
argument_list|(
literal|"snapshot"
argument_list|)
decl_stmt|;
name|String
name|bnum
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|.
name|getBuildNumber
argument_list|()
argument_list|)
decl_stmt|;
name|addOptionalElementText
argument_list|(
name|snapshot
argument_list|,
literal|"buildNumber"
argument_list|,
name|bnum
argument_list|)
expr_stmt|;
name|addOptionalElementText
argument_list|(
name|snapshot
argument_list|,
literal|"timestamp"
argument_list|,
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|versions
init|=
name|versioning
operator|.
name|addElement
argument_list|(
literal|"versions"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|metadata
operator|.
name|getAvailableVersions
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|version
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|versions
operator|.
name|addElement
argument_list|(
literal|"version"
argument_list|)
operator|.
name|setText
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
block|}
name|addOptionalElementText
argument_list|(
name|versioning
argument_list|,
literal|"lastUpdated"
argument_list|,
name|metadata
operator|.
name|getLastUpdated
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|XMLWriter
operator|.
name|write
argument_list|(
name|doc
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryMetadataException
argument_list|(
literal|"Unable to write xml contents to writer: "
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
block|}
specifier|private
specifier|static
name|void
name|addOptionalElementText
parameter_list|(
name|Element
name|elem
parameter_list|,
name|String
name|elemName
parameter_list|,
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return;
block|}
name|elem
operator|.
name|addElement
argument_list|(
name|elemName
argument_list|)
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|PluginComparator
implements|implements
name|Comparator
argument_list|<
name|Plugin
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|PluginComparator
name|INSTANCE
init|=
operator|new
name|PluginComparator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Plugin
name|plugin
parameter_list|,
name|Plugin
name|plugin2
parameter_list|)
block|{
if|if
condition|(
name|plugin
operator|.
name|getPrefix
argument_list|()
operator|!=
literal|null
operator|&&
name|plugin2
operator|.
name|getPrefix
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|plugin
operator|.
name|getPrefix
argument_list|()
operator|.
name|compareTo
argument_list|(
name|plugin2
operator|.
name|getPrefix
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|plugin
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
name|plugin2
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|plugin
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|plugin2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|// we assume artifactId is not null which sounds good :-)
return|return
name|plugin
operator|.
name|getArtifactId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|plugin2
operator|.
name|getArtifactId
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

