begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|lucene
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
name|commons
operator|.
name|io
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ArchivaConfiguration
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
name|archiva
operator|.
name|configuration
operator|.
name|FileTypes
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
name|archiva
operator|.
name|consumers
operator|.
name|AbstractMonitoredConsumer
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
name|archiva
operator|.
name|consumers
operator|.
name|ConsumerException
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
name|archiva
operator|.
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryContentIndex
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
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryContentIndexFactory
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
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryIndexException
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
name|archiva
operator|.
name|indexer
operator|.
name|filecontent
operator|.
name|FileContentRecord
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
name|archiva
operator|.
name|model
operator|.
name|ArchivaArtifact
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
name|archiva
operator|.
name|model
operator|.
name|ArchivaRepository
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
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|BidirectionalRepositoryLayout
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
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|BidirectionalRepositoryLayoutFactory
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
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|LayoutException
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
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
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryListener
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
name|List
import|;
end_import

begin_comment
comment|/**  * IndexContentConsumer - generic full file content indexing consumer.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  *                   role-hint="index-content"  *                   instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|IndexContentConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
implements|,
name|Initializable
block|{
specifier|private
specifier|static
specifier|final
name|String
name|READ_CONTENT
init|=
literal|"read_content"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INDEX_ERROR
init|=
literal|"indexing_error"
decl_stmt|;
comment|/**      * @plexus.configuration default-value="index-content"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Text and XML file contents indexing"      */
specifier|private
name|String
name|description
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="lucene"      */
specifier|private
name|RepositoryContentIndexFactory
name|indexFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|BidirectionalRepositoryLayoutFactory
name|layoutFactory
decl_stmt|;
specifier|private
name|List
name|propertyNameTriggers
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|List
name|includes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|RepositoryContentIndex
name|index
decl_stmt|;
specifier|private
name|ArchivaRepository
name|repository
decl_stmt|;
specifier|private
name|File
name|repositoryDir
decl_stmt|;
specifier|private
name|BidirectionalRepositoryLayout
name|repositoryLayout
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|List
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
name|getIncludes
parameter_list|()
block|{
return|return
name|this
operator|.
name|includes
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|)
throws|throws
name|ConsumerException
block|{
if|if
condition|(
operator|!
name|repository
operator|.
name|isManaged
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"Consumer requires managed repository."
argument_list|)
throw|;
block|}
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|repositoryDir
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|indexFactory
operator|.
name|createFileContentIndex
argument_list|(
name|repository
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|repositoryLayout
operator|=
name|layoutFactory
operator|.
name|getLayout
argument_list|(
name|this
operator|.
name|repository
operator|.
name|getLayoutType
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"Unable to initialize consumer due to unknown repository layout: "
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
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|FileContentRecord
name|record
init|=
operator|new
name|FileContentRecord
argument_list|()
decl_stmt|;
try|try
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repositoryDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|record
operator|.
name|setRepositoryId
argument_list|(
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setFilename
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|record
operator|.
name|setContents
argument_list|(
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|file
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test for possible artifact reference syntax.
try|try
block|{
name|ArchivaArtifact
name|artifact
init|=
name|this
operator|.
name|repositoryLayout
operator|.
name|toArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|record
operator|.
name|setArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// Not an artifact.
block|}
name|index
operator|.
name|modifyRecord
argument_list|(
name|record
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|READ_CONTENT
argument_list|,
literal|"Unable to read file contents: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|INDEX_ERROR
argument_list|,
literal|"Unable to index file contents: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|propertyNameTriggers
operator|.
name|contains
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|private
name|void
name|initIncludes
parameter_list|()
block|{
name|includes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|includes
operator|.
name|addAll
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|INDEXABLE_CONTENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|propertyNameTriggers
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"repositoryScanning"
argument_list|)
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"fileTypes"
argument_list|)
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"fileType"
argument_list|)
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"patterns"
argument_list|)
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"pattern"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

