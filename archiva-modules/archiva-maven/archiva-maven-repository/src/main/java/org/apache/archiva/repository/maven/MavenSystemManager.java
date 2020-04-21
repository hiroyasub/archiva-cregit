begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|maven
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
name|archiva
operator|.
name|repository
operator|.
name|maven
operator|.
name|dependency
operator|.
name|tree
operator|.
name|ArchivaRepositoryConnectorFactory
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
name|repository
operator|.
name|internal
operator|.
name|DefaultArtifactDescriptorReader
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
name|repository
operator|.
name|internal
operator|.
name|DefaultVersionRangeResolver
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
name|repository
operator|.
name|internal
operator|.
name|DefaultVersionResolver
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
name|repository
operator|.
name|internal
operator|.
name|MavenRepositorySystemUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|DefaultRepositorySystemSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|RepositorySystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|RepositorySystemSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|collection
operator|.
name|DependencySelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|impl
operator|.
name|ArtifactDescriptorReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|impl
operator|.
name|DefaultServiceLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|impl
operator|.
name|VersionRangeResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|impl
operator|.
name|VersionResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|internal
operator|.
name|impl
operator|.
name|SimpleLocalRepositoryManagerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|repository
operator|.
name|LocalRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|repository
operator|.
name|LocalRepositoryManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|repository
operator|.
name|NoLocalRepositoryManagerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|spi
operator|.
name|connector
operator|.
name|RepositoryConnectorFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|selector
operator|.
name|AndDependencySelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|selector
operator|.
name|ExclusionDependencySelector
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_comment
comment|/**  * Some static utility methods that are used by different classes.  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"mavenSystemManager"
argument_list|)
specifier|public
class|class
name|MavenSystemManager
block|{
specifier|static
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MavenSystemManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DefaultServiceLocator
name|locator
decl_stmt|;
specifier|private
name|RepositorySystem
name|system
decl_stmt|;
annotation|@
name|PostConstruct
specifier|private
specifier|synchronized
name|void
name|init
parameter_list|()
block|{
name|locator
operator|=
name|newLocator
argument_list|()
expr_stmt|;
name|system
operator|=
name|newRepositorySystem
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new aether repository system session for the given directory and assigns the      * repository to this session.      *      * @param localRepoDir The repository directory      * @return The newly created session object.      */
specifier|public
specifier|static
name|RepositorySystemSession
name|newRepositorySystemSession
parameter_list|(
name|String
name|localRepoDir
parameter_list|)
block|{
name|DefaultRepositorySystemSession
name|session
init|=
name|MavenRepositorySystemUtils
operator|.
name|newSession
argument_list|()
decl_stmt|;
name|LocalRepository
name|repo
init|=
operator|new
name|LocalRepository
argument_list|(
name|localRepoDir
argument_list|)
decl_stmt|;
name|DependencySelector
name|depFilter
init|=
operator|new
name|AndDependencySelector
argument_list|(
operator|new
name|ExclusionDependencySelector
argument_list|()
argument_list|)
decl_stmt|;
name|session
operator|.
name|setDependencySelector
argument_list|(
name|depFilter
argument_list|)
expr_stmt|;
name|SimpleLocalRepositoryManagerFactory
name|repFactory
init|=
operator|new
name|SimpleLocalRepositoryManagerFactory
argument_list|()
decl_stmt|;
try|try
block|{
name|LocalRepositoryManager
name|manager
init|=
name|repFactory
operator|.
name|newInstance
argument_list|(
name|session
argument_list|,
name|repo
argument_list|)
decl_stmt|;
name|session
operator|.
name|setLocalRepositoryManager
argument_list|(
name|manager
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoLocalRepositoryManagerException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not assign the repository manager to the session: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|session
return|;
block|}
specifier|public
name|RepositorySystem
name|getRepositorySystem
parameter_list|()
block|{
return|return
name|system
return|;
block|}
specifier|public
name|DefaultServiceLocator
name|getLocator
parameter_list|()
block|{
return|return
name|locator
return|;
block|}
comment|/**      * Finds the      *      * @return      */
specifier|public
specifier|static
name|RepositorySystem
name|newRepositorySystem
parameter_list|(
name|DefaultServiceLocator
name|locator
parameter_list|)
block|{
return|return
name|locator
operator|.
name|getService
argument_list|(
name|RepositorySystem
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DefaultServiceLocator
name|newLocator
parameter_list|()
block|{
name|DefaultServiceLocator
name|locator
init|=
name|MavenRepositorySystemUtils
operator|.
name|newServiceLocator
argument_list|()
decl_stmt|;
name|locator
operator|.
name|addService
argument_list|(
name|RepositoryConnectorFactory
operator|.
name|class
argument_list|,
name|ArchivaRepositoryConnectorFactory
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// FileRepositoryConnectorFactory.class );
name|locator
operator|.
name|addService
argument_list|(
name|VersionResolver
operator|.
name|class
argument_list|,
name|DefaultVersionResolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|locator
operator|.
name|addService
argument_list|(
name|VersionRangeResolver
operator|.
name|class
argument_list|,
name|DefaultVersionRangeResolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|locator
operator|.
name|addService
argument_list|(
name|ArtifactDescriptorReader
operator|.
name|class
argument_list|,
name|DefaultArtifactDescriptorReader
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|locator
return|;
block|}
block|}
end_class

end_unit
