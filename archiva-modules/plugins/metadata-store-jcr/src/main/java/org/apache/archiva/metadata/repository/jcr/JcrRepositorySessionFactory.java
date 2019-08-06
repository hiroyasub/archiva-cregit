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
name|jcr
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|MetadataFacetFactory
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
name|*
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
name|commons
operator|.
name|lang
operator|.
name|time
operator|.
name|StopWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|oak
operator|.
name|segment
operator|.
name|file
operator|.
name|InvalidFileStoreVersionException
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
name|context
operator|.
name|ApplicationContext
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
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|RepositoryException
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
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Map
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositorySessionFactory#jcr"
argument_list|)
specifier|public
class|class
name|JcrRepositorySessionFactory
extends|extends
name|AbstractRepositorySessionFactory
implements|implements
name|RepositorySessionFactory
block|{
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|metadataFacetFactories
decl_stmt|;
specifier|private
name|Repository
name|repository
decl_stmt|;
comment|// Lazy evaluation to avoid problems with circular dependencies during initialization
specifier|private
name|MetadataResolver
name|metadataResolver
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactoryBean
name|repositorySessionFactoryBean
decl_stmt|;
specifier|private
name|RepositoryFactory
name|repositoryFactory
decl_stmt|;
specifier|private
name|JcrMetadataRepository
name|jcrMetadataRepository
decl_stmt|;
annotation|@
name|Override
specifier|public
name|RepositorySession
name|createSession
parameter_list|()
throws|throws
name|MetadataRepositoryException
block|{
try|try
block|{
return|return
operator|new
name|JcrSession
argument_list|(
name|jcrMetadataRepository
argument_list|,
name|getMetadataResolver
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
comment|// FIXME: a custom exception requires refactoring for callers to handle it
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|// Lazy evaluation to avoid problems with circular dependencies during initialization
specifier|private
name|MetadataResolver
name|getMetadataResolver
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|metadataResolver
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|metadataResolver
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|MetadataResolver
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|metadataResolver
return|;
block|}
specifier|protected
name|void
name|initialize
parameter_list|()
block|{
comment|// skip initialisation if not jcr
if|if
condition|(
name|repositorySessionFactoryBean
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|repositorySessionFactoryBean
operator|.
name|getId
argument_list|()
argument_list|,
literal|"jcr"
argument_list|)
condition|)
block|{
return|return;
block|}
name|StopWatch
name|stopWatch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|stopWatch
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|metadataFacetFactories
operator|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|MetadataFacetFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// olamy with spring the "id" is now "metadataFacetFactory#hint"
comment|// whereas was only hint with plexus so let remove  metadataFacetFactory#
name|Map
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|cleanedMetadataFacetFactories
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|metadataFacetFactories
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|entry
range|:
name|metadataFacetFactories
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|contains
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|cleanedMetadataFacetFactories
operator|.
name|put
argument_list|(
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"#"
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cleanedMetadataFacetFactories
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|metadataFacetFactories
operator|=
name|cleanedMetadataFacetFactories
expr_stmt|;
try|try
block|{
name|repositoryFactory
operator|=
operator|new
name|RepositoryFactory
argument_list|()
expr_stmt|;
comment|// FIXME this need to be configurable
name|Path
name|directoryPath
init|=
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"appserver.base"
argument_list|)
argument_list|,
literal|"data/jcr"
argument_list|)
decl_stmt|;
name|repositoryFactory
operator|.
name|setRepositoryPath
argument_list|(
name|directoryPath
argument_list|)
expr_stmt|;
try|try
block|{
name|repository
operator|=
name|repositoryFactory
operator|.
name|createRepository
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidFileStoreVersionException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Repository creation failed {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Fatal error. Could not create metadata repository."
argument_list|)
throw|;
block|}
name|jcrMetadataRepository
operator|=
operator|new
name|JcrMetadataRepository
argument_list|(
name|metadataFacetFactories
argument_list|,
name|repository
argument_list|)
expr_stmt|;
try|try
init|(
name|JcrSession
name|session
init|=
operator|new
name|JcrSession
argument_list|(
name|jcrMetadataRepository
argument_list|,
name|metadataResolver
argument_list|)
init|)
block|{
name|JcrMetadataRepository
operator|.
name|initializeNodeTypes
argument_list|(
name|session
operator|.
name|getJcrSession
argument_list|()
argument_list|)
expr_stmt|;
comment|// Saves automatically with close
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|stopWatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"time to initialize JcrRepositorySessionFactory: {}"
argument_list|,
name|stopWatch
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|shutdown
parameter_list|()
block|{
name|repositoryFactory
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|close
parameter_list|()
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setMetadataResolver
parameter_list|(
name|MetadataResolver
name|metadataResolver
parameter_list|)
block|{
name|this
operator|.
name|metadataResolver
operator|=
name|metadataResolver
expr_stmt|;
block|}
specifier|public
name|JcrMetadataRepository
name|getMetadataRepository
parameter_list|()
block|{
return|return
name|jcrMetadataRepository
return|;
block|}
specifier|public
name|void
name|setMetadataFacetFactories
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|metadataFacetFactories
parameter_list|)
block|{
name|this
operator|.
name|metadataFacetFactories
operator|=
name|metadataFacetFactories
expr_stmt|;
block|}
block|}
end_class

end_unit

