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
name|repository
operator|.
name|project
operator|.
name|dependencies
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
name|dependency
operator|.
name|graph
operator|.
name|DependencyGraph
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
name|dependency
operator|.
name|graph
operator|.
name|DependencyGraphBuilder
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
name|dependency
operator|.
name|graph
operator|.
name|DependencyGraphNode
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
name|dependency
operator|.
name|graph
operator|.
name|DependencyGraphUtils
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
name|ArchivaProjectModel
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
name|model
operator|.
name|VersionedReference
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
name|project
operator|.
name|ProjectModelException
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
name|project
operator|.
name|ProjectModelResolverFactory
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
name|project
operator|.
name|filters
operator|.
name|EffectiveProjectModelFilter
import|;
end_import

begin_comment
comment|/**  * ProjectModelBasedGraphBuilder   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component   *              role="org.apache.maven.archiva.dependency.graph.DependencyGraphBuilder"  *              role-hint="project-model"  */
end_comment

begin_class
specifier|public
class|class
name|ProjectModelBasedGraphBuilder
implements|implements
name|DependencyGraphBuilder
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ProjectModelResolverFactory
name|resolverFactory
decl_stmt|;
comment|/**      * @plexus.requirement       *          role="org.apache.maven.archiva.repository.project.ProjectModelFilter"      *          role-hint="effective"      */
specifier|private
name|EffectiveProjectModelFilter
name|effectiveFilter
init|=
operator|new
name|EffectiveProjectModelFilter
argument_list|()
decl_stmt|;
specifier|public
name|DependencyGraph
name|createGraph
parameter_list|(
name|VersionedReference
name|versionedProjectReference
parameter_list|)
block|{
name|String
name|groupId
init|=
name|versionedProjectReference
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
name|String
name|artifactId
init|=
name|versionedProjectReference
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|versionedProjectReference
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|DependencyGraph
name|graph
init|=
operator|new
name|DependencyGraph
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
return|return
name|graph
return|;
block|}
specifier|public
name|void
name|resolveNode
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphNode
name|fromNode
parameter_list|,
name|VersionedReference
name|versionedProjectReference
parameter_list|)
block|{
name|ArchivaProjectModel
name|model
init|=
name|resolveModel
argument_list|(
name|fromNode
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|DependencyGraphUtils
operator|.
name|addNodeFromModel
argument_list|(
name|model
argument_list|,
name|graph
argument_list|,
name|fromNode
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ArchivaProjectModel
name|resolveModel
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
name|VersionedReference
name|projectRef
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|projectRef
operator|.
name|setGroupId
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|projectRef
operator|.
name|setArtifactId
argument_list|(
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|projectRef
operator|.
name|setVersion
argument_list|(
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|resolverFactory
operator|.
name|getCurrentResolverStack
argument_list|()
operator|.
name|findProject
argument_list|(
name|projectRef
argument_list|)
decl_stmt|;
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
return|return
name|createDefaultModel
argument_list|(
name|reference
argument_list|)
return|;
block|}
try|try
block|{
name|ArchivaProjectModel
name|processedModel
init|=
name|effectiveFilter
operator|.
name|filter
argument_list|(
name|model
argument_list|)
decl_stmt|;
return|return
name|processedModel
return|;
block|}
catch|catch
parameter_list|(
name|ProjectModelException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
return|return
name|createDefaultModel
argument_list|(
name|reference
argument_list|)
return|;
block|}
block|}
specifier|private
name|ArchivaProjectModel
name|createDefaultModel
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
name|ArchivaProjectModel
name|model
init|=
operator|new
name|ArchivaProjectModel
argument_list|()
decl_stmt|;
comment|// Create default (dummy) model
name|model
operator|=
operator|new
name|ArchivaProjectModel
argument_list|()
expr_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setPackaging
argument_list|(
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|model
return|;
block|}
block|}
end_class

end_unit

