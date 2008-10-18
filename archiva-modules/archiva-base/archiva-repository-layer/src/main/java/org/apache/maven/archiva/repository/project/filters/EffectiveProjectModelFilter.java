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
name|filters
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
name|collections
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaModelCloner
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
name|Dependency
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
name|ProjectModelFilter
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
name|ProjectModelMerge
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
name|codehaus
operator|.
name|plexus
operator|.
name|cache
operator|.
name|Cache
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Builder for the Effective Project Model.    *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.repository.project.ProjectModelFilter"   *                   role-hint="effective"   */
end_comment

begin_class
specifier|public
class|class
name|EffectiveProjectModelFilter
implements|implements
name|ProjectModelFilter
block|{
specifier|private
name|ProjectModelFilter
name|expressionFilter
init|=
operator|new
name|ProjectModelExpressionFilter
argument_list|()
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ProjectModelResolverFactory
name|resolverFactory
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="effective-project-cache"      */
specifier|private
name|Cache
name|effectiveProjectCache
decl_stmt|;
comment|/**      * Take the provided {@link ArchivaProjectModel} and build the effective {@link ArchivaProjectModel}.      *       * Steps:      * 1) Expand any expressions / properties.      * 2) Walk the parent project references and merge.      * 3) Apply dependency management settings.      *       * @param project the project to create the effective {@link ArchivaProjectModel} from.      * @return a the effective {@link ArchivaProjectModel}.      * @throws ProjectModelException if there was a problem building the effective pom.      */
specifier|public
name|ArchivaProjectModel
name|filter
parameter_list|(
specifier|final
name|ArchivaProjectModel
name|project
parameter_list|)
throws|throws
name|ProjectModelException
block|{
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|resolverFactory
operator|.
name|getCurrentResolverStack
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to build effective pom with no project model resolvers defined."
argument_list|)
throw|;
block|}
name|ArchivaProjectModel
name|effectiveProject
decl_stmt|;
name|String
name|projectKey
init|=
name|toProjectKey
argument_list|(
name|project
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|effectiveProjectCache
init|)
block|{
if|if
condition|(
name|effectiveProjectCache
operator|.
name|hasKey
argument_list|(
name|projectKey
argument_list|)
condition|)
block|{
name|DEBUG
argument_list|(
literal|"Fetching (from cache/projectKey): "
operator|+
name|projectKey
argument_list|)
expr_stmt|;
name|effectiveProject
operator|=
operator|(
name|ArchivaProjectModel
operator|)
name|effectiveProjectCache
operator|.
name|get
argument_list|(
name|projectKey
argument_list|)
expr_stmt|;
return|return
name|effectiveProject
return|;
block|}
block|}
comment|// Clone submitted project (so that we don't mess with it)
name|effectiveProject
operator|=
name|ArchivaModelCloner
operator|.
name|clone
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|DEBUG
argument_list|(
literal|"Starting build of effective with: "
operator|+
name|effectiveProject
argument_list|)
expr_stmt|;
comment|// Merge in all the parent poms.
name|effectiveProject
operator|=
name|mergeParent
argument_list|(
name|effectiveProject
argument_list|)
expr_stmt|;
comment|// Setup Expression Evaluation pieces.
name|effectiveProject
operator|=
name|expressionFilter
operator|.
name|filter
argument_list|(
name|effectiveProject
argument_list|)
expr_stmt|;
comment|// Resolve dependency versions from dependency management.
name|applyDependencyManagement
argument_list|(
name|effectiveProject
argument_list|)
expr_stmt|;
comment|// groupId or version could be updated by parent or expressions
name|projectKey
operator|=
name|toProjectKey
argument_list|(
name|effectiveProject
argument_list|)
expr_stmt|;
comment|// Do not add project into cache if it contains no groupId and
comment|// version information
if|if
condition|(
name|effectiveProject
operator|.
name|getGroupId
argument_list|()
operator|!=
literal|null
operator|&&
name|effectiveProject
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|effectiveProjectCache
init|)
block|{
name|DEBUG
argument_list|(
literal|"Putting (to cache/projectKey): "
operator|+
name|projectKey
argument_list|)
expr_stmt|;
name|effectiveProjectCache
operator|.
name|put
argument_list|(
name|projectKey
argument_list|,
name|effectiveProject
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Return what we got.
return|return
name|effectiveProject
return|;
block|}
specifier|private
name|void
name|applyDependencyManagement
parameter_list|(
name|ArchivaProjectModel
name|pom
parameter_list|)
block|{
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|pom
operator|.
name|getDependencyManagement
argument_list|()
argument_list|)
operator|||
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|pom
operator|.
name|getDependencies
argument_list|()
argument_list|)
condition|)
block|{
comment|// Nothing to do. All done!
return|return;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Dependency
argument_list|>
name|managedDependencies
init|=
name|createDependencyMap
argument_list|(
name|pom
operator|.
name|getDependencyManagement
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Dependency
argument_list|>
name|it
init|=
name|pom
operator|.
name|getDependencies
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
name|Dependency
name|dep
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|toVersionlessDependencyKey
argument_list|(
name|dep
argument_list|)
decl_stmt|;
comment|// Do we need to do anything?
if|if
condition|(
name|managedDependencies
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Dependency
name|mgmtDep
init|=
operator|(
name|Dependency
operator|)
name|managedDependencies
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|dep
operator|.
name|setVersion
argument_list|(
name|mgmtDep
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|dep
operator|.
name|setScope
argument_list|(
name|mgmtDep
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|dep
operator|.
name|setExclusions
argument_list|(
name|ProjectModelMerge
operator|.
name|mergeExclusions
argument_list|(
name|dep
operator|.
name|getExclusions
argument_list|()
argument_list|,
name|mgmtDep
operator|.
name|getExclusions
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|ArchivaProjectModel
name|mergeParent
parameter_list|(
name|ArchivaProjectModel
name|pom
parameter_list|)
throws|throws
name|ProjectModelException
block|{
name|ArchivaProjectModel
name|mixedProject
decl_stmt|;
name|DEBUG
argument_list|(
literal|"Project: "
operator|+
name|toProjectKey
argument_list|(
name|pom
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|pom
operator|.
name|getParentProject
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Use parent reference.
name|VersionedReference
name|parentRef
init|=
name|pom
operator|.
name|getParentProject
argument_list|()
decl_stmt|;
name|String
name|parentKey
init|=
name|VersionedReference
operator|.
name|toKey
argument_list|(
name|parentRef
argument_list|)
decl_stmt|;
name|DEBUG
argument_list|(
literal|"Has parent: "
operator|+
name|parentKey
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|parentProject
decl_stmt|;
synchronized|synchronized
init|(
name|effectiveProjectCache
init|)
block|{
comment|// is the pre-merged parent in the cache?
if|if
condition|(
name|effectiveProjectCache
operator|.
name|hasKey
argument_list|(
name|parentKey
argument_list|)
condition|)
block|{
name|DEBUG
argument_list|(
literal|"Fetching (from cache/parentKey): "
operator|+
name|parentKey
argument_list|)
expr_stmt|;
comment|// Use the one from the cache.
name|parentProject
operator|=
operator|(
name|ArchivaProjectModel
operator|)
name|effectiveProjectCache
operator|.
name|get
argument_list|(
name|parentKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Look it up, using resolvers.
name|parentProject
operator|=
name|this
operator|.
name|resolverFactory
operator|.
name|getCurrentResolverStack
argument_list|()
operator|.
name|findProject
argument_list|(
name|parentRef
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|parentProject
operator|!=
literal|null
condition|)
block|{
comment|// Merge the pom with the parent pom.
name|parentProject
operator|=
name|mergeParent
argument_list|(
name|parentProject
argument_list|)
expr_stmt|;
name|parentProject
operator|=
name|expressionFilter
operator|.
name|filter
argument_list|(
name|parentProject
argument_list|)
expr_stmt|;
comment|// Cache the pre-merged parent.
synchronized|synchronized
init|(
name|effectiveProjectCache
init|)
block|{
name|DEBUG
argument_list|(
literal|"Putting (to cache/parentKey/merged): "
operator|+
name|parentKey
argument_list|)
expr_stmt|;
comment|// Add the merged parent pom to the cache.
name|effectiveProjectCache
operator|.
name|put
argument_list|(
name|parentKey
argument_list|,
name|parentProject
argument_list|)
expr_stmt|;
block|}
comment|// Now merge the parent with the current
name|mixedProject
operator|=
name|ProjectModelMerge
operator|.
name|merge
argument_list|(
name|pom
argument_list|,
name|parentProject
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Shortcircuit due to missing parent pom.
comment|// TODO: Document this via a monitor.
name|mixedProject
operator|=
name|mixinSuperPom
argument_list|(
name|pom
argument_list|)
expr_stmt|;
comment|// Cache the non-existant parent.
synchronized|synchronized
init|(
name|effectiveProjectCache
init|)
block|{
name|DEBUG
argument_list|(
literal|"Putting (to cache/parentKey/basicPom): "
operator|+
name|parentKey
argument_list|)
expr_stmt|;
comment|// Add the basic pom to cache.
name|effectiveProjectCache
operator|.
name|put
argument_list|(
name|parentKey
argument_list|,
name|createBasicPom
argument_list|(
name|parentRef
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|DEBUG
argument_list|(
literal|"No parent found"
argument_list|)
expr_stmt|;
comment|/* Mix in the super-pom.              *               * Super POM from maven/components contains many things.              * However, for purposes of archiva, only the<repositories>              * and<pluginRepositories> sections are of any value.              */
name|mixedProject
operator|=
name|mixinSuperPom
argument_list|(
name|pom
argument_list|)
expr_stmt|;
block|}
return|return
name|mixedProject
return|;
block|}
specifier|private
name|ArchivaProjectModel
name|createBasicPom
parameter_list|(
name|VersionedReference
name|ref
parameter_list|)
block|{
name|ArchivaProjectModel
name|model
init|=
operator|new
name|ArchivaProjectModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|ref
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|ref
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|ref
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setPackaging
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
return|return
name|model
return|;
block|}
comment|/**      * Super POM from maven/components contains many things.      * However, for purposes of archiva, only the<repositories>      * and<pluginRepositories> sections are of any value.      *       * @param pom      * @return      */
specifier|private
name|ArchivaProjectModel
name|mixinSuperPom
parameter_list|(
name|ArchivaProjectModel
name|pom
parameter_list|)
block|{
comment|// TODO: add super pom repositories.
name|DEBUG
argument_list|(
literal|"Mix in Super POM: "
operator|+
name|pom
argument_list|)
expr_stmt|;
return|return
name|pom
return|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Dependency
argument_list|>
name|createDependencyMap
parameter_list|(
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Dependency
argument_list|>
name|ret
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Dependency
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Dependency
argument_list|>
name|it
init|=
name|dependencies
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
name|Dependency
name|dep
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|toVersionlessDependencyKey
argument_list|(
name|dep
argument_list|)
decl_stmt|;
name|ret
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|dep
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
specifier|static
name|String
name|toVersionlessDependencyKey
parameter_list|(
name|Dependency
name|dep
parameter_list|)
block|{
name|StringBuffer
name|key
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
operator|.
name|append
argument_list|(
name|dep
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|dep
operator|.
name|getClassifier
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|dep
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|toProjectKey
parameter_list|(
name|ArchivaProjectModel
name|project
parameter_list|)
block|{
name|StringBuffer
name|key
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|DEBUG
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
comment|// Used in debugging of this object.
comment|// System.out.println( "[EffectiveProjectModelFilter] " + msg );
block|}
block|}
end_class

end_unit

