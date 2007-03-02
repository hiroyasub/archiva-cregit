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
name|maven
operator|.
name|archiva
operator|.
name|common
operator|.
name|consumers
operator|.
name|GenericArtifactConsumer
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
name|BaseFile
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
name|reporting
operator|.
name|database
operator|.
name|ArtifactResultsDatabase
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
name|reporting
operator|.
name|group
operator|.
name|ReportGroup
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
name|InvalidArtifactRTException
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
name|project
operator|.
name|MavenProject
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
name|project
operator|.
name|MavenProjectBuilder
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
name|project
operator|.
name|ProjectBuildingException
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

begin_comment
comment|/**  * ArtifactHealthConsumer   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *  * @plexus.component role="org.apache.maven.archiva.common.consumers.Consumer"  *     role-hint="artifact-health"  *     instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactHealthConsumer
extends|extends
name|GenericArtifactConsumer
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactResultsDatabase
name|database
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="health"      */
specifier|private
name|ReportGroup
name|health
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MavenProjectBuilder
name|projectBuilder
decl_stmt|;
specifier|public
name|void
name|processArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|BaseFile
name|file
parameter_list|)
block|{
name|Model
name|model
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Artifact
name|pomArtifact
init|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|MavenProject
name|project
init|=
name|projectBuilder
operator|.
name|buildFromRepository
argument_list|(
name|pomArtifact
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|model
operator|=
name|project
operator|.
name|getModel
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidArtifactRTException
name|e
parameter_list|)
block|{
name|database
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"health"
argument_list|,
literal|"invalid"
argument_list|,
literal|"Invalid artifact ["
operator|+
name|artifact
operator|+
literal|"] : "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProjectBuildingException
name|e
parameter_list|)
block|{
name|database
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"health"
argument_list|,
literal|"project-build"
argument_list|,
literal|"Error reading project model: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
name|database
operator|.
name|remove
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|health
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|processFileProblem
parameter_list|(
name|BaseFile
name|path
parameter_list|,
name|String
name|message
parameter_list|)
block|{
comment|/* do nothing here (yet) */
comment|// TODO: store build failure into database?
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Artifact Health Consumer"
return|;
block|}
block|}
end_class

end_unit

