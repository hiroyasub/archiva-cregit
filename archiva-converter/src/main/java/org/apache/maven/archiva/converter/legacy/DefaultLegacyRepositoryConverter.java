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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|converter
operator|.
name|ConversionListener
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
name|archiva
operator|.
name|discoverer
operator|.
name|Discoverer
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
name|discoverer
operator|.
name|DiscovererException
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
name|net
operator|.
name|MalformedURLException
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
comment|/**  * @author Jason van Zyl  * @plexus.component  * @todo turn this into a general conversion component and hide all this crap here.  * @todo it should be possible to move this to the converter module without causing it to gain additional dependencies  */
end_comment

begin_class
specifier|public
class|class
name|DefaultLegacyRepositoryConverter
implements|implements
name|LegacyRepositoryConverter
block|{
comment|/**      * @plexus.requirement role-hint="legacy"      */
specifier|private
name|ArtifactRepositoryLayout
name|legacyLayout
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|ArtifactRepositoryLayout
name|defaultLayout
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactRepositoryFactory
name|artifactRepositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|Discoverer
name|discoverer
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.common.consumers.Consumer" role-hint="legacy-converter"      */
specifier|private
name|LegacyConverterArtifactConsumer
name|legacyConverterConsumer
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
parameter_list|,
name|boolean
name|includeSnapshots
parameter_list|)
throws|throws
name|RepositoryConversionException
block|{
name|ArtifactRepository
name|legacyRepository
decl_stmt|;
name|ArtifactRepository
name|repository
decl_stmt|;
try|try
block|{
name|String
name|legacyRepositoryDir
init|=
name|legacyRepositoryDirectory
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|repositoryDir
init|=
name|repositoryDirectory
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//workaround for spaces non converted by PathUtils in wagon
comment|//TODO: remove it when PathUtils will be fixed
if|if
condition|(
name|legacyRepositoryDir
operator|.
name|indexOf
argument_list|(
literal|"%20"
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|legacyRepositoryDir
operator|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|legacyRepositoryDir
argument_list|,
literal|"%20"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|repositoryDir
operator|.
name|indexOf
argument_list|(
literal|"%20"
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|repositoryDir
operator|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|repositoryDir
argument_list|,
literal|"%20"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
block|}
name|legacyRepository
operator|=
name|artifactRepositoryFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"legacy"
argument_list|,
name|legacyRepositoryDir
argument_list|,
name|legacyLayout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|repository
operator|=
name|artifactRepositoryFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"default"
argument_list|,
name|repositoryDir
argument_list|,
name|defaultLayout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
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
try|try
block|{
name|List
name|consumers
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|legacyConverterConsumer
operator|.
name|setDestinationRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|add
argument_list|(
name|legacyConverterConsumer
argument_list|)
expr_stmt|;
name|discoverer
operator|.
name|walkRepository
argument_list|(
name|legacyRepository
argument_list|,
name|consumers
argument_list|,
name|includeSnapshots
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DiscovererException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryConversionException
argument_list|(
literal|"Unable to convert repository due to discoverer error:"
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
comment|/**      * Add a listener to the conversion process.      *      * @param listener the listener to add.      */
specifier|public
name|void
name|addConversionListener
parameter_list|(
name|ConversionListener
name|listener
parameter_list|)
block|{
name|legacyConverterConsumer
operator|.
name|addConversionListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Remove a listener from the conversion process.      *      * @param listener the listener to remove.      */
specifier|public
name|void
name|removeConversionListener
parameter_list|(
name|ConversionListener
name|listener
parameter_list|)
block|{
name|legacyConverterConsumer
operator|.
name|removeConversionListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

