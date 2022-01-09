begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|$package
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
name|components
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
name|apache
operator|.
name|archiva
operator|.
name|components
operator|.
name|registry
operator|.
name|RegistryListener
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
name|configuration
operator|.
name|provider
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
name|archiva
operator|.
name|configuration
operator|.
name|provider
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
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepositoryException
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataResolutionException
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
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
name|content
operator|.
name|BaseRepositoryContentLayout
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
name|content
operator|.
name|LayoutException
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
name|ManagedRepository
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
name|ManagedRepositoryContent
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
name|RepositoryContentFactory
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
name|content
operator|.
name|Artifact
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
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|Date
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
comment|/**  *<code>SimpleArtifactConsumer</code>  *   */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#simple"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|SimpleArtifactConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SimpleArtifactConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * default-value="simple-artifact-consumer"      */
specifier|private
name|String
name|id
init|=
literal|"simple-artifact-consumer"
decl_stmt|;
specifier|private
name|String
name|description
init|=
literal|"Simple consumer to illustrate how to consume the contents of a repository."
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|propertyNameTriggers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** current repository being scanned */
specifier|private
name|ManagedRepository
name|repository
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryContentFactory#default"
argument_list|)
specifier|private
name|RepositoryContentFactory
name|repositoryContentFactory
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
specifier|private
name|RepositorySession
name|repositorySession
decl_stmt|;
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|beginScan
argument_list|(
name|repository
argument_list|,
name|whenGathered
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Beginning scan of repository [{}]"
argument_list|,
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not create repository session {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"Could not create repository session: "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
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
name|processFile
argument_list|(
name|path
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Processing entry [{}] from repository [{}]"
argument_list|,
name|path
argument_list|,
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|ManagedRepositoryContent
name|repositoryContent
init|=
name|repository
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|BaseRepositoryContentLayout
name|layout
init|=
name|repositoryContent
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|repositorySession
operator|.
name|getRepository
argument_list|()
operator|.
name|getArtifacts
argument_list|(
name|repositorySession
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getNamespace
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
decl||
name|MetadataResolutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
name|completeScan
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Finished scan of repository ["
operator|+
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Used by archiva to determine if the consumer wishes to process all of a repository's entries or just those that      * have been modified since the last scan.      *       * @return boolean true if the consumer wishes to process all entries on each scan, false for only those modified      *         since the last scan      */
specifier|public
name|boolean
name|isProcessUnmodified
parameter_list|()
block|{
return|return
name|super
operator|.
name|isProcessUnmodified
argument_list|()
return|;
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|components
operator|.
name|registry
operator|.
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
name|ARTIFACTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|propertyNameTriggers
operator|=
operator|new
name|ArrayList
argument_list|<>
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
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
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
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

