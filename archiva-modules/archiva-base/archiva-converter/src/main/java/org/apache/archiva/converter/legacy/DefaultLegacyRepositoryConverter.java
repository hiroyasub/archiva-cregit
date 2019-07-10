begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|converter
operator|.
name|legacy
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|filelock
operator|.
name|DefaultFileLockManager
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridgeException
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
name|common
operator|.
name|utils
operator|.
name|PathUtil
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
name|InvalidRepositoryContentConsumer
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
name|converter
operator|.
name|RepositoryConversionException
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
name|BasicManagedRepository
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
name|FilesystemStorage
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
name|maven2
operator|.
name|ManagedDefaultRepositoryContent
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
name|scanner
operator|.
name|RepositoryScanner
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
name|scanner
operator|.
name|RepositoryScannerException
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
name|ArtifactRepositoryFactory
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
name|MavenArtifactRepository
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
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|io
operator|.
name|IOException
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
name|ArrayList
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
name|Collections
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
comment|/**  * DefaultLegacyRepositoryConverter  *  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"legacyRepositoryConverter#default"
argument_list|)
specifier|public
class|class
name|DefaultLegacyRepositoryConverter
implements|implements
name|LegacyRepositoryConverter
block|{
comment|/**      *      */
comment|// private ArtifactRepositoryFactory artifactRepositoryFactory;
comment|/**      *      */
specifier|private
name|ArtifactRepositoryLayout
name|defaultLayout
decl_stmt|;
annotation|@
name|Inject
name|FileTypes
name|fileTypes
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"knownRepositoryContentConsumer#artifact-legacy-to-default-converter"
argument_list|)
specifier|private
name|LegacyConverterArtifactConsumer
name|legacyConverterConsumer
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
specifier|private
name|RepositoryScanner
name|repoScanner
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|DefaultLegacyRepositoryConverter
parameter_list|(
name|PlexusSisuBridge
name|plexusSisuBridge
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
comment|// artifactRepositoryFactory = plexusSisuBridge.lookup( ArtifactRepositoryFactory.class );
name|defaultLayout
operator|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|class
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|convertLegacyRepository
parameter_list|(
name|Path
name|legacyRepositoryDirectory
parameter_list|,
name|Path
name|repositoryDirectory
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|fileExclusionPatterns
parameter_list|)
throws|throws
name|RepositoryConversionException
block|{
try|try
block|{
name|String
name|defaultRepositoryUrl
init|=
name|PathUtil
operator|.
name|toUrl
argument_list|(
name|repositoryDirectory
argument_list|)
decl_stmt|;
name|BasicManagedRepository
name|legacyRepository
init|=
operator|new
name|BasicManagedRepository
argument_list|(
literal|"legacy"
argument_list|,
literal|"Legacy Repository"
argument_list|,
name|repositoryDirectory
operator|.
name|getParent
argument_list|()
argument_list|)
decl_stmt|;
name|legacyRepository
operator|.
name|setLocation
argument_list|(
name|legacyRepositoryDirectory
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|legacyRepository
operator|.
name|setLayout
argument_list|(
literal|"legacy"
argument_list|)
expr_stmt|;
name|DefaultFileLockManager
name|lockManager
init|=
operator|new
name|DefaultFileLockManager
argument_list|()
decl_stmt|;
name|FilesystemStorage
name|storage
init|=
operator|new
name|FilesystemStorage
argument_list|(
name|legacyRepositoryDirectory
argument_list|,
name|lockManager
argument_list|)
decl_stmt|;
name|legacyRepository
operator|.
name|setContent
argument_list|(
operator|new
name|ManagedDefaultRepositoryContent
argument_list|(
name|legacyRepository
argument_list|,
name|fileTypes
argument_list|,
name|lockManager
argument_list|)
argument_list|)
expr_stmt|;
name|ArtifactRepository
name|repository
init|=
operator|new
name|MavenArtifactRepository
argument_list|(
literal|"default"
argument_list|,
name|defaultRepositoryUrl
argument_list|,
name|defaultLayout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|legacyConverterConsumer
operator|.
name|setExcludes
argument_list|(
name|fileExclusionPatterns
argument_list|)
expr_stmt|;
name|legacyConverterConsumer
operator|.
name|setDestinationRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|legacyConverterConsumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ignoredContent
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|RepositoryScanner
operator|.
name|IGNORABLE_CONTENT
argument_list|)
argument_list|)
decl_stmt|;
name|repoScanner
operator|.
name|scan
argument_list|(
name|legacyRepository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|ignoredContent
argument_list|,
name|RepositoryScanner
operator|.
name|FRESH_SCAN
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryScannerException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryConversionException
argument_list|(
literal|"Error convering legacy repository."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

