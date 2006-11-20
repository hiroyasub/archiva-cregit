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
name|web
operator|.
name|action
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|Term
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|TermQuery
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
name|Configuration
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
name|ConfigurationStore
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
name|ConfigurationStoreException
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
name|ConfiguredRepositoryFactory
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
name|RepositoryArtifactIndex
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
name|RepositoryArtifactIndexFactory
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
name|RepositoryIndexSearchException
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
name|lucene
operator|.
name|LuceneQuery
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
name|record
operator|.
name|StandardArtifactIndexRecord
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
name|web
operator|.
name|util
operator|.
name|VersionMerger
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
name|metadata
operator|.
name|ArtifactMetadataSource
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
name|resolver
operator|.
name|ArtifactCollector
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
name|resolver
operator|.
name|ArtifactResolutionException
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
name|versioning
operator|.
name|DefaultArtifactVersion
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|project
operator|.
name|artifact
operator|.
name|InvalidDependencyVersionException
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyNode
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTree
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeBuilder
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeBuilderException
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
name|util
operator|.
name|StringUtils
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
name|util
operator|.
name|xml
operator|.
name|pull
operator|.
name|XmlPullParserException
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
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
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
name|HashSet
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Browse the repository.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="showArtifactAction"  */
end_comment

begin_class
specifier|public
class|class
name|ShowArtifactAction
extends|extends
name|PlexusActionSupport
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfiguredRepositoryFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MavenProjectBuilder
name|projectBuilder
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryArtifactIndexFactory
name|factory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactMetadataSource
name|artifactMetadataSource
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactCollector
name|collector
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|DependencyTreeBuilder
name|dependencyTreeBuilder
decl_stmt|;
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|Model
name|model
decl_stmt|;
specifier|private
name|Collection
name|dependencies
decl_stmt|;
specifier|private
name|List
name|dependencyTree
decl_stmt|;
specifier|public
name|String
name|artifact
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|IOException
throws|,
name|XmlPullParserException
throws|,
name|ProjectBuildingException
block|{
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|MavenProject
name|project
init|=
name|readProject
argument_list|()
decl_stmt|;
name|model
operator|=
name|project
operator|.
name|getModel
argument_list|()
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|dependencies
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|IOException
throws|,
name|XmlPullParserException
throws|,
name|ProjectBuildingException
block|{
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|MavenProject
name|project
init|=
name|readProject
argument_list|()
decl_stmt|;
name|model
operator|=
name|project
operator|.
name|getModel
argument_list|()
expr_stmt|;
comment|// TODO: should this be the whole set of artifacts, and be more like the maven dependencies report?
name|this
operator|.
name|dependencies
operator|=
name|VersionMerger
operator|.
name|wrap
argument_list|(
name|project
operator|.
name|getModel
argument_list|()
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|dependees
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|IOException
throws|,
name|XmlPullParserException
throws|,
name|ProjectBuildingException
throws|,
name|RepositoryIndexException
throws|,
name|RepositoryIndexSearchException
block|{
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|MavenProject
name|project
init|=
name|readProject
argument_list|()
decl_stmt|;
name|model
operator|=
name|project
operator|.
name|getModel
argument_list|()
expr_stmt|;
name|RepositoryArtifactIndex
name|index
init|=
name|getIndex
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|createId
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|List
name|records
init|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
operator|new
name|TermQuery
argument_list|(
operator|new
name|Term
argument_list|(
literal|"dependencies"
argument_list|,
name|id
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|dependencies
operator|=
name|VersionMerger
operator|.
name|merge
argument_list|(
name|records
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|dependencyTree
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|ProjectBuildingException
throws|,
name|InvalidDependencyVersionException
throws|,
name|ArtifactResolutionException
block|{
if|if
condition|(
operator|!
name|checkParameters
argument_list|()
condition|)
block|{
return|return
name|ERROR
return|;
block|}
name|Configuration
name|configuration
init|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
decl_stmt|;
name|List
name|repositories
init|=
name|repositoryFactory
operator|.
name|createRepositories
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
comment|// TODO: maybe we can decouple the assembly parts of the project builder from the repository handling to get rid of the temp repo
name|ArtifactRepository
name|localRepository
init|=
name|repositoryFactory
operator|.
name|createLocalRepository
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|MavenProject
name|project
init|=
name|projectBuilder
operator|.
name|buildFromRepository
argument_list|(
name|artifact
argument_list|,
name|repositories
argument_list|,
name|localRepository
argument_list|)
decl_stmt|;
name|model
operator|=
name|project
operator|.
name|getModel
argument_list|()
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|" processing : "
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
argument_list|)
expr_stmt|;
name|DependencyTree
name|dependencies
init|=
name|collectDependencies
argument_list|(
name|project
argument_list|,
name|artifact
argument_list|,
name|localRepository
argument_list|,
name|repositories
argument_list|)
decl_stmt|;
name|this
operator|.
name|dependencyTree
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|populateFlatTreeList
argument_list|(
name|dependencies
operator|.
name|getRootNode
argument_list|()
argument_list|,
name|dependencyTree
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|void
name|populateFlatTreeList
parameter_list|(
name|DependencyNode
name|currentNode
parameter_list|,
name|List
name|dependencyList
parameter_list|)
block|{
name|DependencyNode
name|childNode
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|currentNode
operator|.
name|getChildren
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|childNode
operator|=
operator|(
name|DependencyNode
operator|)
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|dependencyList
operator|.
name|add
argument_list|(
name|childNode
argument_list|)
expr_stmt|;
name|populateFlatTreeList
argument_list|(
name|childNode
argument_list|,
name|dependencyList
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|DependencyTree
name|collectDependencies
parameter_list|(
name|MavenProject
name|project
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ArtifactRepository
name|localRepository
parameter_list|,
name|List
name|repositories
parameter_list|)
throws|throws
name|ArtifactResolutionException
throws|,
name|ProjectBuildingException
throws|,
name|InvalidDependencyVersionException
throws|,
name|ConfigurationStoreException
block|{
try|try
block|{
return|return
name|dependencyTreeBuilder
operator|.
name|buildDependencyTree
argument_list|(
name|project
argument_list|,
name|localRepository
argument_list|,
name|artifactFactory
argument_list|,
name|artifactMetadataSource
argument_list|,
name|collector
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|DependencyTreeBuilderException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unable to build dependency tree."
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|createId
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
return|;
block|}
specifier|private
name|RepositoryArtifactIndex
name|getIndex
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|RepositoryIndexException
block|{
name|Configuration
name|configuration
init|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
decl_stmt|;
name|File
name|indexPath
init|=
operator|new
name|File
argument_list|(
name|configuration
operator|.
name|getIndexPath
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createStandardIndex
argument_list|(
name|indexPath
argument_list|)
return|;
block|}
specifier|private
name|MavenProject
name|readProject
parameter_list|()
throws|throws
name|ConfigurationStoreException
throws|,
name|ProjectBuildingException
block|{
name|Configuration
name|configuration
init|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
decl_stmt|;
name|List
name|repositories
init|=
name|repositoryFactory
operator|.
name|createRepositories
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
comment|// TODO: maybe we can decouple the assembly parts of the project builder from the repository handling to get rid of the temp repo
name|ArtifactRepository
name|localRepository
init|=
name|repositoryFactory
operator|.
name|createLocalRepository
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
return|return
name|projectBuilder
operator|.
name|buildFromRepository
argument_list|(
name|artifact
argument_list|,
name|repositories
argument_list|,
name|localRepository
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|checkParameters
parameter_list|()
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
comment|// TODO: i18n
name|addActionError
argument_list|(
literal|"You must specify a group ID to browse"
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
if|else if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
comment|// TODO: i18n
name|addActionError
argument_list|(
literal|"You must specify a artifact ID to browse"
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
if|else if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|version
argument_list|)
condition|)
block|{
comment|// TODO: i18n
name|addActionError
argument_list|(
literal|"You must specify a version to browse"
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|Model
name|getModel
parameter_list|()
block|{
return|return
name|model
return|;
block|}
specifier|public
name|Collection
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|List
name|getDependencyTree
parameter_list|()
block|{
return|return
name|dependencyTree
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|DependencyWrapper
block|{
specifier|private
specifier|final
name|String
name|groupId
decl_stmt|;
specifier|private
specifier|final
name|String
name|artifactId
decl_stmt|;
comment|/**          * Versions added. We ignore duplicates since you might add those with varying classifiers.          */
specifier|private
name|Set
name|versions
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|scope
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
specifier|public
name|DependencyWrapper
parameter_list|(
name|StandardArtifactIndexRecord
name|record
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|record
operator|.
name|getGroupId
argument_list|()
expr_stmt|;
name|this
operator|.
name|artifactId
operator|=
name|record
operator|.
name|getArtifactId
argument_list|()
expr_stmt|;
name|addVersion
argument_list|(
name|record
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DependencyWrapper
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|dependency
operator|.
name|getGroupId
argument_list|()
expr_stmt|;
name|this
operator|.
name|artifactId
operator|=
name|dependency
operator|.
name|getArtifactId
argument_list|()
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|dependency
operator|.
name|getScope
argument_list|()
expr_stmt|;
name|this
operator|.
name|classifier
operator|=
name|dependency
operator|.
name|getClassifier
argument_list|()
expr_stmt|;
name|addVersion
argument_list|(
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|void
name|addVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
comment|// We use DefaultArtifactVersion to get the correct sorting order later, however it does not have
comment|// hashCode properly implemented, so we add it here.
comment|// TODO: add these methods to the actual DefaultArtifactVersion and use that.
name|versions
operator|.
name|add
argument_list|(
operator|new
name|DefaultArtifactVersion
argument_list|(
name|version
argument_list|)
block|{
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
decl_stmt|;
name|result
operator|=
name|getBuildNumber
argument_list|()
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|getMajorVersion
argument_list|()
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|getMinorVersion
argument_list|()
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|getIncrementalVersion
argument_list|()
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|getQualifier
argument_list|()
operator|!=
literal|null
condition|?
name|getQualifier
argument_list|()
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DefaultArtifactVersion
name|that
init|=
operator|(
name|DefaultArtifactVersion
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|getBuildNumber
argument_list|()
operator|!=
name|that
operator|.
name|getBuildNumber
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getIncrementalVersion
argument_list|()
operator|!=
name|that
operator|.
name|getIncrementalVersion
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getMajorVersion
argument_list|()
operator|!=
name|that
operator|.
name|getMajorVersion
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getMinorVersion
argument_list|()
operator|!=
name|that
operator|.
name|getMinorVersion
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getQualifier
argument_list|()
operator|!=
literal|null
condition|?
operator|!
name|getQualifier
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getQualifier
argument_list|()
argument_list|)
else|:
name|that
operator|.
name|getQualifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|versions
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|version
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|List
name|getVersions
parameter_list|()
block|{
name|List
name|versions
init|=
operator|new
name|ArrayList
argument_list|(
name|this
operator|.
name|versions
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|versions
argument_list|)
expr_stmt|;
return|return
name|versions
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
block|}
block|}
end_class

end_unit

