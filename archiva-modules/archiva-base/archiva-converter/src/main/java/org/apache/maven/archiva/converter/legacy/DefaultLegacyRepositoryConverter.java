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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|layout
operator|.
name|ArtifactRepositoryLayout
import|;
end_import

begin_comment
comment|/**  * DefaultLegacyRepositoryConverter   *  * @version $Id$  * @plexus.component   */
end_comment

begin_class
specifier|public
class|class
name|DefaultLegacyRepositoryConverter
implements|implements
name|LegacyRepositoryConverter
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactRepositoryFactory
name|artifactRepositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|ArtifactRepositoryLayout
name|defaultLayout
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"       *                     role-hint="artifact-legacy-to-default-converter"      */
specifier|private
name|LegacyConverterArtifactConsumer
name|legacyConverterConsumer
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryScanner
name|repoScanner
decl_stmt|;
specifier|public
name|void
name|convertLegacyRepository
parameter_list|(
name|File
name|legacyRepositoryDirectory
parameter_list|,
name|File
name|repositoryDirectory
parameter_list|,
name|List
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
name|ManagedRepositoryConfiguration
name|legacyRepository
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|legacyRepository
operator|.
name|setId
argument_list|(
literal|"legacy"
argument_list|)
expr_stmt|;
name|legacyRepository
operator|.
name|setName
argument_list|(
literal|"Legacy Repository"
argument_list|)
expr_stmt|;
name|legacyRepository
operator|.
name|setLocation
argument_list|(
name|legacyRepositoryDirectory
operator|.
name|getAbsolutePath
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
name|ArtifactRepository
name|repository
init|=
name|artifactRepositoryFactory
operator|.
name|createArtifactRepository
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
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|legacyConverterConsumer
argument_list|)
expr_stmt|;
name|List
name|invalidConsumers
init|=
name|Collections
operator|.
name|EMPTY_LIST
decl_stmt|;
name|List
name|ignoredContent
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ignoredContent
operator|.
name|addAll
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
expr_stmt|;
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

