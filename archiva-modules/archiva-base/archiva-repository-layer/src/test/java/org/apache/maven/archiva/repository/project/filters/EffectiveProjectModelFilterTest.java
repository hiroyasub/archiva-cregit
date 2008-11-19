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
name|maven
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|VersionUtil
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
name|Individual
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
name|AbstractRepositoryLayerTestCase
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
name|ProjectModelReader
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
name|ProjectModelResolver
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
name|readers
operator|.
name|ProjectModel400Reader
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
name|resolvers
operator|.
name|ManagedRepositoryProjectResolver
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
comment|/**  * EffectiveProjectModelFilterTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|EffectiveProjectModelFilterTest
extends|extends
name|AbstractRepositoryLayerTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_REPOSITORY
init|=
literal|"src/test/repositories/default-repository"
decl_stmt|;
specifier|private
name|EffectiveProjectModelFilter
name|lookupEffective
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|(
name|EffectiveProjectModelFilter
operator|)
name|lookup
argument_list|(
name|ProjectModelFilter
operator|.
name|class
argument_list|,
literal|"effective"
argument_list|)
return|;
block|}
specifier|private
name|ArchivaProjectModel
name|createArchivaProjectModel
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ProjectModelException
block|{
name|ProjectModelReader
name|reader
init|=
operator|new
name|ProjectModel400Reader
argument_list|()
decl_stmt|;
name|File
name|pomFile
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
return|return
name|reader
operator|.
name|read
argument_list|(
name|pomFile
argument_list|)
return|;
block|}
specifier|private
name|ProjectModelResolver
name|createDefaultRepositoryResolver
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|defaultRepoDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
name|DEFAULT_REPOSITORY
argument_list|)
decl_stmt|;
name|ManagedRepositoryContent
name|repo
init|=
name|createManagedRepositoryContent
argument_list|(
literal|"defaultTestRepo"
argument_list|,
literal|"Default Test Repo"
argument_list|,
name|defaultRepoDir
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
name|ProjectModelReader
name|reader
init|=
operator|new
name|ProjectModel400Reader
argument_list|()
decl_stmt|;
name|ManagedRepositoryProjectResolver
name|resolver
init|=
operator|new
name|ManagedRepositoryProjectResolver
argument_list|(
name|repo
argument_list|,
name|reader
argument_list|)
decl_stmt|;
return|return
name|resolver
return|;
block|}
specifier|public
name|void
name|testBuildEffectiveProject
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEffectiveProject
argument_list|(
literal|"/org/apache/maven/archiva/archiva-model/1.0-SNAPSHOT/archiva-model-1.0-SNAPSHOT.pom"
argument_list|,
literal|"/archiva-model-effective.pom"
argument_list|)
expr_stmt|;
name|assertEffectiveProject
argument_list|(
literal|"/test-project/test-project-endpoint-ejb/2.4.4/test-project-endpoint-ejb-2.4.4.pom"
argument_list|,
literal|"/test-project-model-effective.pom"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertEffectiveProject
parameter_list|(
name|String
name|pomFile
parameter_list|,
name|String
name|effectivePomFile
parameter_list|)
throws|throws
name|Exception
throws|,
name|ProjectModelException
block|{
name|initTestResolverFactory
argument_list|()
expr_stmt|;
name|EffectiveProjectModelFilter
name|filter
init|=
name|lookupEffective
argument_list|()
decl_stmt|;
name|ArchivaProjectModel
name|startModel
init|=
name|createArchivaProjectModel
argument_list|(
name|DEFAULT_REPOSITORY
operator|+
name|pomFile
argument_list|)
decl_stmt|;
name|ArchivaProjectModel
name|effectiveModel
init|=
name|filter
operator|.
name|filter
argument_list|(
name|startModel
argument_list|)
decl_stmt|;
name|ArchivaProjectModel
name|expectedModel
init|=
name|createArchivaProjectModel
argument_list|(
literal|"src/test/expected-poms/"
operator|+
name|effectivePomFile
argument_list|)
decl_stmt|;
name|assertModel
argument_list|(
name|expectedModel
argument_list|,
name|effectiveModel
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-510] In Repository Browse, the first unique snapshot version clicked is getting persisted in the       * request resulting to 'version does not match' error      *       * The purpose of this test is ensure that timestamped SNAPSHOTS do not cache improperly, and each timestamped      * pom can be loaded through the effective project filter correctly.      */
specifier|public
name|void
name|testBuildEffectiveSnapshotProject
parameter_list|()
throws|throws
name|Exception
block|{
name|initTestResolverFactory
argument_list|()
expr_stmt|;
name|EffectiveProjectModelFilter
name|filter
init|=
name|lookupEffective
argument_list|()
decl_stmt|;
name|String
name|axisVersions
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"1.3-20070725.210059-1"
block|,
literal|"1.3-20070725.232304-2"
block|,
literal|"1.3-20070726.053327-3"
block|,
literal|"1.3-20070726.173653-5"
block|,
literal|"1.3-20070727.113106-7"
block|,
literal|"1.3-20070728.053229-10"
block|,
literal|"1.3-20070728.112043-11"
block|,
literal|"1.3-20070729.171937-16"
block|,
literal|"1.3-20070730.232112-20"
block|,
literal|"1.3-20070731.113304-21"
block|,
literal|"1.3-20070731.172936-22"
block|,
literal|"1.3-20070802.113139-29"
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|axisVersions
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
literal|"Version should be a unique snapshot."
argument_list|,
name|VersionUtil
operator|.
name|isUniqueSnapshot
argument_list|(
name|axisVersions
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|initialModel
init|=
name|createArchivaProjectModel
argument_list|(
name|DEFAULT_REPOSITORY
operator|+
literal|"/org/apache/axis2/axis2/1.3-SNAPSHOT/axis2-"
operator|+
name|axisVersions
index|[
name|i
index|]
operator|+
literal|".pom"
argument_list|)
decl_stmt|;
comment|// This is the process that ProjectModelToDatabaseConsumer uses, so we mimic it here.
comment|// This logic is related to the MRM-510 jira.
name|String
name|baseVersion
init|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|axisVersions
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Base Version<"
operator|+
name|baseVersion
operator|+
literal|"> of filename<"
operator|+
name|axisVersions
index|[
name|i
index|]
operator|+
literal|"> should be equal to what is in model."
argument_list|,
name|initialModel
operator|.
name|getVersion
argument_list|()
argument_list|,
name|baseVersion
argument_list|)
expr_stmt|;
name|initialModel
operator|.
name|setVersion
argument_list|(
name|axisVersions
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unique snapshot versions of initial model should be equal."
argument_list|,
name|axisVersions
index|[
name|i
index|]
argument_list|,
name|initialModel
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|effectiveModel
init|=
name|filter
operator|.
name|filter
argument_list|(
name|initialModel
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unique snapshot versions of initial model should be equal."
argument_list|,
name|axisVersions
index|[
name|i
index|]
argument_list|,
name|initialModel
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unique snapshot versions of filtered/effective model should be equal."
argument_list|,
name|axisVersions
index|[
name|i
index|]
argument_list|,
name|effectiveModel
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|ProjectModelResolverFactory
name|initTestResolverFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectModelResolverFactory
name|resolverFactory
init|=
operator|(
name|ProjectModelResolverFactory
operator|)
name|lookup
argument_list|(
name|ProjectModelResolverFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|resolverFactory
operator|.
name|getCurrentResolverStack
argument_list|()
operator|.
name|clearResolvers
argument_list|()
expr_stmt|;
name|resolverFactory
operator|.
name|getCurrentResolverStack
argument_list|()
operator|.
name|addProjectModelResolver
argument_list|(
name|createDefaultRepositoryResolver
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|resolverFactory
return|;
block|}
specifier|private
name|void
name|assertModel
parameter_list|(
name|ArchivaProjectModel
name|expectedModel
parameter_list|,
name|ArchivaProjectModel
name|effectiveModel
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Equivalent Models"
argument_list|,
name|expectedModel
argument_list|,
name|effectiveModel
argument_list|)
expr_stmt|;
name|assertContainsSameIndividuals
argument_list|(
literal|"Individuals"
argument_list|,
name|expectedModel
operator|.
name|getIndividuals
argument_list|()
argument_list|,
name|effectiveModel
operator|.
name|getIndividuals
argument_list|()
argument_list|)
expr_stmt|;
name|dumpDependencyList
argument_list|(
literal|"Expected"
argument_list|,
name|expectedModel
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|dumpDependencyList
argument_list|(
literal|"Effective"
argument_list|,
name|effectiveModel
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertContainsSameDependencies
argument_list|(
literal|"Dependencies"
argument_list|,
name|expectedModel
operator|.
name|getDependencies
argument_list|()
argument_list|,
name|effectiveModel
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertContainsSameDependencies
argument_list|(
literal|"DependencyManagement"
argument_list|,
name|expectedModel
operator|.
name|getDependencyManagement
argument_list|()
argument_list|,
name|effectiveModel
operator|.
name|getDependencyManagement
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|dumpDependencyList
parameter_list|(
name|String
name|type
parameter_list|,
name|List
argument_list|<
name|Dependency
argument_list|>
name|deps
parameter_list|)
block|{
if|if
condition|(
name|deps
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" Dependencies ["
operator|+
name|type
operator|+
literal|"] is null."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|deps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" Dependencies ["
operator|+
name|type
operator|+
literal|"] dependency list is empty."
argument_list|)
expr_stmt|;
return|return;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|".\\ ["
operator|+
name|type
operator|+
literal|"] Dependency List (size:"
operator|+
name|deps
operator|.
name|size
argument_list|()
operator|+
literal|") \\.________________"
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Dependency
argument_list|>
name|it
init|=
name|deps
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|Dependency
operator|.
name|toKey
argument_list|(
name|dep
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertEquivalentLists
parameter_list|(
name|String
name|listId
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|expectedList
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|effectiveList
parameter_list|)
block|{
if|if
condition|(
operator|(
name|expectedList
operator|==
literal|null
operator|)
operator|&&
operator|(
name|effectiveList
operator|==
literal|null
operator|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|(
name|expectedList
operator|==
literal|null
operator|)
operator|&&
operator|(
name|effectiveList
operator|!=
literal|null
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Effective ["
operator|+
name|listId
operator|+
literal|"] List is instantiated, while expected List is null."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|expectedList
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|effectiveList
operator|==
literal|null
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Effective ["
operator|+
name|listId
operator|+
literal|"] List is null, while expected List is instantiated."
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"["
operator|+
name|listId
operator|+
literal|"] List Size"
argument_list|,
name|expectedList
operator|.
name|size
argument_list|()
argument_list|,
name|expectedList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertContainsSameIndividuals
parameter_list|(
name|String
name|listId
parameter_list|,
name|List
argument_list|<
name|Individual
argument_list|>
name|expectedList
parameter_list|,
name|List
argument_list|<
name|Individual
argument_list|>
name|effectiveList
parameter_list|)
block|{
name|assertEquivalentLists
argument_list|(
name|listId
argument_list|,
name|expectedList
argument_list|,
name|effectiveList
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Individual
argument_list|>
name|expectedMap
init|=
name|getIndividualsMap
argument_list|(
name|expectedList
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Individual
argument_list|>
name|effectiveMap
init|=
name|getIndividualsMap
argument_list|(
name|effectiveList
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|expectedMap
operator|.
name|keySet
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
name|String
name|key
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should exist in Effective ["
operator|+
name|listId
operator|+
literal|"] list: "
operator|+
name|key
argument_list|,
name|effectiveMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertContainsSameDependencies
parameter_list|(
name|String
name|listId
parameter_list|,
name|List
argument_list|<
name|Dependency
argument_list|>
name|expectedList
parameter_list|,
name|List
argument_list|<
name|Dependency
argument_list|>
name|effectiveList
parameter_list|)
block|{
name|assertEquivalentLists
argument_list|(
name|listId
argument_list|,
name|expectedList
argument_list|,
name|effectiveList
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Dependency
argument_list|>
name|expectedMap
init|=
name|getDependencyMap
argument_list|(
name|expectedList
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Dependency
argument_list|>
name|effectiveMap
init|=
name|getDependencyMap
argument_list|(
name|effectiveList
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|expectedMap
operator|.
name|keySet
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
name|String
name|key
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should exist in Effective ["
operator|+
name|listId
operator|+
literal|"] list: "
operator|+
name|key
argument_list|,
name|effectiveMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Individual
argument_list|>
name|getIndividualsMap
parameter_list|(
name|List
argument_list|<
name|Individual
argument_list|>
name|individuals
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Individual
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Individual
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Individual
argument_list|>
name|it
init|=
name|individuals
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
name|Individual
name|individual
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|individual
operator|.
name|getEmail
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|individual
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Dependency
argument_list|>
name|getDependencyMap
parameter_list|(
name|List
argument_list|<
name|Dependency
argument_list|>
name|deps
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Dependency
argument_list|>
name|map
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
name|deps
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
name|Dependency
operator|.
name|toKey
argument_list|(
name|dep
argument_list|)
decl_stmt|;
name|map
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
name|map
return|;
block|}
block|}
end_class

end_unit

