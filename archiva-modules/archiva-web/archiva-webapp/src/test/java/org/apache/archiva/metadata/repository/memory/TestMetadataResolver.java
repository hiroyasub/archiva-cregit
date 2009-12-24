begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|memory
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|model
operator|.
name|ArtifactMetadata
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
name|model
operator|.
name|ProjectMetadata
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
name|model
operator|.
name|ProjectVersionMetadata
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
name|model
operator|.
name|ProjectVersionReference
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
name|MetadataResolver
import|;
end_import

begin_class
specifier|public
class|class
name|TestMetadataResolver
implements|implements
name|MetadataResolver
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ProjectVersionMetadata
argument_list|>
name|projectVersions
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ProjectVersionMetadata
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|>
name|artifacts
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProjectVersionReference
argument_list|>
argument_list|>
name|references
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProjectVersionReference
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|namespaces
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|projectsInNamespace
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|versionsInProject
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ProjectMetadata
name|getProject
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
name|ProjectMetadata
name|metadata
init|=
operator|new
name|ProjectMetadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setId
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
return|return
name|metadata
return|;
block|}
specifier|public
name|ProjectVersionMetadata
name|getProjectVersion
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
return|return
name|projectVersions
operator|.
name|get
argument_list|(
name|createMapKey
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getArtifactVersions
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Collection
argument_list|<
name|ProjectVersionReference
argument_list|>
name|getProjectReferences
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
return|return
name|references
operator|.
name|get
argument_list|(
name|createMapKey
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getRootNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
return|return
name|getNamespaces
argument_list|(
name|repoId
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|baseNamespace
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|fromIndex
init|=
name|baseNamespace
operator|!=
literal|null
condition|?
name|baseNamespace
operator|.
name|length
argument_list|()
operator|+
literal|1
else|:
literal|0
decl_stmt|;
for|for
control|(
name|String
name|namespace
range|:
name|this
operator|.
name|namespaces
operator|.
name|get
argument_list|(
name|repoId
argument_list|)
control|)
block|{
if|if
condition|(
name|baseNamespace
operator|==
literal|null
operator|||
name|namespace
operator|.
name|startsWith
argument_list|(
name|baseNamespace
operator|+
literal|"."
argument_list|)
condition|)
block|{
name|int
name|i
init|=
name|namespace
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|,
name|fromIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>=
literal|0
condition|)
block|{
name|namespaces
operator|.
name|add
argument_list|(
name|namespace
operator|.
name|substring
argument_list|(
name|fromIndex
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|namespaces
operator|.
name|add
argument_list|(
name|namespace
operator|.
name|substring
argument_list|(
name|fromIndex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|namespaces
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjects
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|list
init|=
name|projectsInNamespace
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
return|return
name|list
operator|!=
literal|null
condition|?
name|list
else|:
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjectVersions
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|list
init|=
name|versionsInProject
operator|.
name|get
argument_list|(
name|namespace
operator|+
literal|":"
operator|+
name|projectId
argument_list|)
decl_stmt|;
return|return
name|list
operator|!=
literal|null
condition|?
name|list
else|:
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
init|=
name|this
operator|.
name|artifacts
operator|.
name|get
argument_list|(
name|createMapKey
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|artifacts
operator|!=
literal|null
condition|?
name|artifacts
else|:
name|Collections
operator|.
expr|<
name|ArtifactMetadata
operator|>
name|emptyList
argument_list|()
operator|)
return|;
block|}
specifier|public
name|void
name|setProjectVersion
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|ProjectVersionMetadata
name|versionMetadata
parameter_list|)
block|{
name|projectVersions
operator|.
name|put
argument_list|(
name|createMapKey
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|versionMetadata
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|versionMetadata
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|projects
init|=
name|projectsInNamespace
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|projects
operator|==
literal|null
condition|)
block|{
name|projects
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|projectsInNamespace
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|projects
argument_list|)
expr_stmt|;
block|}
name|projects
operator|.
name|add
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|namespace
operator|+
literal|":"
operator|+
name|projectId
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|versions
init|=
name|versionsInProject
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|versions
operator|==
literal|null
condition|)
block|{
name|versions
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|versionsInProject
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
name|versions
operator|.
name|add
argument_list|(
name|versionMetadata
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifacts
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
parameter_list|)
block|{
name|this
operator|.
name|artifacts
operator|.
name|put
argument_list|(
name|createMapKey
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|createMapKey
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
return|return
name|repoId
operator|+
literal|":"
operator|+
name|namespace
operator|+
literal|":"
operator|+
name|projectId
operator|+
literal|":"
operator|+
name|projectVersion
return|;
block|}
specifier|public
name|void
name|setProjectReferences
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|List
argument_list|<
name|ProjectVersionReference
argument_list|>
name|references
parameter_list|)
block|{
name|this
operator|.
name|references
operator|.
name|put
argument_list|(
name|createMapKey
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
argument_list|,
name|references
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|this
operator|.
name|namespaces
operator|.
name|put
argument_list|(
name|repoId
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

