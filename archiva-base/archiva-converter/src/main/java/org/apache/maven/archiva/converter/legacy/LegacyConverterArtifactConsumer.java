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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|converter
operator|.
name|artifact
operator|.
name|ArtifactConversionException
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
name|artifact
operator|.
name|ArtifactConverter
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
name|ArtifactReference
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
name|ManagedRepositoryContent
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
name|content
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

begin_comment
comment|/**  * LegacyConverterArtifactConsumer - convert artifacts as they are found  * into the destination repository.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  *     role-hint="artifact-legacy-to-default-converter"  *     instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|LegacyConverterArtifactConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LegacyConverterArtifactConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="legacy-to-default"      */
specifier|private
name|ArtifactConverter
name|artifactConverter
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
name|ManagedRepositoryContent
name|managedRepository
decl_stmt|;
specifier|private
name|ArtifactRepository
name|destinationRepository
decl_stmt|;
specifier|private
name|List
name|includes
decl_stmt|;
specifier|private
name|List
name|excludes
decl_stmt|;
specifier|public
name|LegacyConverterArtifactConsumer
parameter_list|()
block|{
name|includes
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.jar"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.ear"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.war"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|this
operator|.
name|managedRepository
operator|=
operator|new
name|ManagedDefaultRepositoryContent
argument_list|()
expr_stmt|;
name|this
operator|.
name|managedRepository
operator|.
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
block|}
specifier|public
name|List
name|getExcludes
parameter_list|()
block|{
return|return
name|excludes
return|;
block|}
specifier|public
name|List
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
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
try|try
block|{
name|ArtifactReference
name|reference
init|=
name|managedRepository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createArtifact
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|,
name|reference
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|artifactConverter
operator|.
name|convert
argument_list|(
name|artifact
argument_list|,
name|destinationRepository
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to convert artifact: "
operator|+
name|path
operator|+
literal|" : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactConversionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to convert artifact: "
operator|+
name|path
operator|+
literal|" : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Legacy Artifact to Default Artifact Converter"
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"artifact-legacy-to-default-converter"
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
name|void
name|setExcludes
parameter_list|(
name|List
name|excludes
parameter_list|)
block|{
name|this
operator|.
name|excludes
operator|=
name|excludes
expr_stmt|;
block|}
specifier|public
name|void
name|setIncludes
parameter_list|(
name|List
name|includes
parameter_list|)
block|{
name|this
operator|.
name|includes
operator|=
name|includes
expr_stmt|;
block|}
specifier|public
name|ArtifactRepository
name|getDestinationRepository
parameter_list|()
block|{
return|return
name|destinationRepository
return|;
block|}
specifier|public
name|void
name|setDestinationRepository
parameter_list|(
name|ArtifactRepository
name|destinationRepository
parameter_list|)
block|{
name|this
operator|.
name|destinationRepository
operator|=
name|destinationRepository
expr_stmt|;
block|}
block|}
end_class

end_unit

