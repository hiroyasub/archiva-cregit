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
name|resolvers
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|ConfigurationNames
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
name|RepositoryConfiguration
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
name|functors
operator|.
name|LocalRepositoryPredicate
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
name|ArchivaRepository
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
name|ArchivaConfigurationAdaptor
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
name|RepositoryException
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
name|BidirectionalRepositoryLayout
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
name|BidirectionalRepositoryLayoutFactory
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
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
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
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryListener
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
comment|/**  * Factory for ProjectModelResolver objects   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.repository.project.resolvers.RepositoryProjectModelResolverFactory"  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryProjectModelResolverFactory
extends|extends
name|AbstractLogEnabled
implements|implements
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|BidirectionalRepositoryLayoutFactory
name|layoutFactory
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="model400"      */
specifier|private
name|ProjectModelReader
name|project400Reader
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="model300"      */
specifier|private
name|ProjectModelReader
name|project300Reader
decl_stmt|;
comment|/**      * Get the {@link ProjectModelResolver} for the specific archiva repository.      *       * @param repo the repository to base resolver on.      * @return return the resolver for the archiva repository provided.      * @throws RepositoryException if unable to create a resolver for the provided {@link ArchivaRepository}      */
specifier|public
name|ProjectModelResolver
name|getResolver
parameter_list|(
name|ArchivaRepository
name|repo
parameter_list|)
throws|throws
name|RepositoryException
block|{
if|if
condition|(
name|resolverMap
operator|.
name|containsKey
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|ProjectModelResolver
operator|)
name|this
operator|.
name|resolverMap
operator|.
name|get
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
name|ProjectModelResolver
name|resolver
init|=
name|toResolver
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|resolverMap
operator|.
name|put
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
return|return
name|resolver
return|;
block|}
comment|/**      * Get the {@link ProjectModelResolver} for the specific archiva repository based on repository id.      *       * @param repoid the repository id to get the resolver for.      * @return the {@link ProjectModelResolver} if found, or null if repository is not found.      */
specifier|public
name|ProjectModelResolver
name|getResolver
parameter_list|(
name|String
name|repoid
parameter_list|)
block|{
return|return
operator|(
name|ProjectModelResolver
operator|)
name|this
operator|.
name|resolverMap
operator|.
name|get
argument_list|(
name|repoid
argument_list|)
return|;
block|}
comment|/**      * Get the {@link List} of {@link ProjectModelResolver} for       * the {@link List} of {@link ArchivaRepository} objects provided.      *       * @param repositoryList the {@link List} of {@link ArchivaRepository} objects to       *             get {@link ProjectModelResolver} for.      * @return the {@link List} of {@link ProjectModelResolver} objects.      * @throws RepositoryException if unable to convert any of the provided {@link ArchivaRepository} objects into      *             a {@link ProjectModelResolver} object.      */
specifier|public
name|List
name|getResolverList
parameter_list|(
name|List
name|repositoryList
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryList
argument_list|)
condition|)
block|{
return|return
name|ret
return|;
block|}
name|Iterator
name|it
init|=
name|repositoryList
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
name|ArchivaRepository
name|repo
init|=
operator|(
name|ArchivaRepository
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|getResolver
argument_list|(
name|repo
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
comment|/**      * Get the entire {@link List} of {@link ProjectModelResolver} that the factory is tracking.      *        * @return the entire list of {@link ProjectModelResolver} that is being tracked.      */
specifier|public
name|List
name|getAllResolvers
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ret
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|resolverMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|ConfigurationNames
operator|.
name|isRepositories
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|update
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|Map
name|resolverMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|void
name|update
parameter_list|()
block|{
synchronized|synchronized
init|(
name|resolverMap
init|)
block|{
name|resolverMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|List
name|configRepos
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|CollectionUtils
operator|.
name|filter
argument_list|(
name|configRepos
argument_list|,
name|LocalRepositoryPredicate
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|configRepos
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
name|RepositoryConfiguration
name|repoconfig
init|=
operator|(
name|RepositoryConfiguration
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ArchivaRepository
name|repo
init|=
name|ArchivaConfigurationAdaptor
operator|.
name|toArchivaRepository
argument_list|(
name|repoconfig
argument_list|)
decl_stmt|;
try|try
block|{
name|RepositoryProjectResolver
name|resolver
init|=
name|toResolver
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|resolverMap
operator|.
name|put
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
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
block|}
block|}
specifier|private
name|RepositoryProjectResolver
name|toResolver
parameter_list|(
name|ArchivaRepository
name|repo
parameter_list|)
throws|throws
name|RepositoryException
block|{
if|if
condition|(
operator|!
name|repo
operator|.
name|isManaged
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"Unable to create RepositoryProjectResolver from non-managed repository: "
operator|+
name|repo
argument_list|)
throw|;
block|}
try|try
block|{
name|BidirectionalRepositoryLayout
name|layout
init|=
name|layoutFactory
operator|.
name|getLayout
argument_list|(
name|repo
operator|.
name|getLayoutType
argument_list|()
argument_list|)
decl_stmt|;
name|ProjectModelReader
name|reader
init|=
name|project400Reader
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
literal|"legacy"
argument_list|,
name|repo
operator|.
name|getLayoutType
argument_list|()
argument_list|)
condition|)
block|{
name|reader
operator|=
name|project300Reader
expr_stmt|;
block|}
name|RepositoryProjectResolver
name|resolver
init|=
operator|new
name|RepositoryProjectResolver
argument_list|(
name|repo
argument_list|,
name|reader
argument_list|,
name|layout
argument_list|)
decl_stmt|;
return|return
name|resolver
return|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"Unable to create RepositoryProjectResolver due to invalid layout spec: "
operator|+
name|repo
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|update
argument_list|()
expr_stmt|;
name|archivaConfiguration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

