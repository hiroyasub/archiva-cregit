begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|core
operator|.
name|repository
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
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|archiva
operator|.
name|configuration
operator|.
name|FileTypes
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
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
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
name|RepositorySessionFactory
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
name|redback
operator|.
name|components
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
name|apache
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
name|archiva
operator|.
name|repository
operator|.
name|RepositoryContentFactory
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
name|archiva
operator|.
name|repository
operator|.
name|RepositoryNotFoundException
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
name|repository
operator|.
name|events
operator|.
name|RepositoryListener
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
name|repository
operator|.
name|metadata
operator|.
name|MetadataTools
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
name|redback
operator|.
name|components
operator|.
name|registry
operator|.
name|RegistryListener
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
name|annotation
operator|.
name|Scope
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
name|inject
operator|.
name|Named
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_comment
comment|/**  * Consumer for removing old snapshots in the repository based on the criteria  * specified by the user.  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#repository-purge"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|RepositoryPurgeConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
block|{
comment|/**      * default-value="repository-purge"      */
specifier|private
name|String
name|id
init|=
literal|"repository-purge"
decl_stmt|;
comment|/**      * default-value="Purge repository of old snapshots"      */
specifier|private
name|String
name|description
init|=
literal|"Purge repository of old snapshots"
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration#default"
argument_list|)
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryContentFactory#default"
argument_list|)
specifier|private
name|RepositoryContentFactory
name|repositoryContentFactory
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|MetadataTools
name|metadataTools
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"fileTypes"
argument_list|)
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|RepositoryPurge
name|repoPurge
decl_stmt|;
specifier|private
name|RepositoryPurge
name|cleanUp
decl_stmt|;
specifier|private
name|boolean
name|deleteReleasedSnapshots
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
specifier|private
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
comment|/**      * FIXME: this could be multiple implementations and needs to be configured.      */
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
specifier|private
name|RepositorySession
name|repositorySession
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
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
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
name|getDefaultArtifactExclusions
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|this
operator|.
name|includes
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|ManagedRepositoryContent
name|repositoryContent
decl_stmt|;
try|try
block|{
name|repositoryContent
operator|=
name|repositoryContentFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"Can't run repository purge: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"Can't run repository purge: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
if|if
condition|(
name|repository
operator|.
name|getDaysOlder
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|repoPurge
operator|=
operator|new
name|DaysOldRepositoryPurge
argument_list|(
name|repositoryContent
argument_list|,
name|repository
operator|.
name|getDaysOlder
argument_list|()
argument_list|,
name|repository
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repositorySession
argument_list|,
name|listeners
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|repoPurge
operator|=
operator|new
name|RetentionCountRepositoryPurge
argument_list|(
name|repositoryContent
argument_list|,
name|repository
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repositorySession
argument_list|,
name|listeners
argument_list|)
expr_stmt|;
block|}
name|cleanUp
operator|=
operator|new
name|CleanupReleasedSnapshotsRepositoryPurge
argument_list|(
name|repositoryContent
argument_list|,
name|metadataTools
argument_list|,
name|managedRepositoryAdmin
argument_list|,
name|repositoryContentFactory
argument_list|,
name|repositorySession
argument_list|,
name|listeners
argument_list|)
expr_stmt|;
name|deleteReleasedSnapshots
operator|=
name|repository
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|beginScan
argument_list|(
name|repository
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|deleteReleasedSnapshots
condition|)
block|{
name|cleanUp
operator|.
name|process
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
name|repoPurge
operator|.
name|process
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryPurgeException
name|rpe
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
name|rpe
operator|.
name|getMessage
argument_list|()
argument_list|,
name|rpe
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|Exception
block|{
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|completeScan
argument_list|()
expr_stmt|;
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
name|isRepositoryScanning
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initIncludes
argument_list|()
expr_stmt|;
block|}
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
specifier|private
name|void
name|initIncludes
parameter_list|()
block|{
name|includes
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isProcessUnmodified
parameter_list|()
block|{
comment|// we need to check all files for deletion, especially if not modified
return|return
literal|true
return|;
block|}
specifier|public
name|ArchivaConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ArchivaConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
specifier|public
name|RepositoryContentFactory
name|getRepositoryContentFactory
parameter_list|()
block|{
return|return
name|repositoryContentFactory
return|;
block|}
specifier|public
name|void
name|setRepositoryContentFactory
parameter_list|(
name|RepositoryContentFactory
name|repositoryContentFactory
parameter_list|)
block|{
name|this
operator|.
name|repositoryContentFactory
operator|=
name|repositoryContentFactory
expr_stmt|;
block|}
specifier|public
name|MetadataTools
name|getMetadataTools
parameter_list|()
block|{
return|return
name|metadataTools
return|;
block|}
specifier|public
name|void
name|setMetadataTools
parameter_list|(
name|MetadataTools
name|metadataTools
parameter_list|)
block|{
name|this
operator|.
name|metadataTools
operator|=
name|metadataTools
expr_stmt|;
block|}
specifier|public
name|FileTypes
name|getFiletypes
parameter_list|()
block|{
return|return
name|filetypes
return|;
block|}
specifier|public
name|void
name|setFiletypes
parameter_list|(
name|FileTypes
name|filetypes
parameter_list|)
block|{
name|this
operator|.
name|filetypes
operator|=
name|filetypes
expr_stmt|;
block|}
specifier|public
name|RepositoryPurge
name|getRepoPurge
parameter_list|()
block|{
return|return
name|repoPurge
return|;
block|}
specifier|public
name|void
name|setRepoPurge
parameter_list|(
name|RepositoryPurge
name|repoPurge
parameter_list|)
block|{
name|this
operator|.
name|repoPurge
operator|=
name|repoPurge
expr_stmt|;
block|}
specifier|public
name|RepositoryPurge
name|getCleanUp
parameter_list|()
block|{
return|return
name|cleanUp
return|;
block|}
specifier|public
name|void
name|setCleanUp
parameter_list|(
name|RepositoryPurge
name|cleanUp
parameter_list|)
block|{
name|this
operator|.
name|cleanUp
operator|=
name|cleanUp
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDeleteReleasedSnapshots
parameter_list|()
block|{
return|return
name|deleteReleasedSnapshots
return|;
block|}
specifier|public
name|void
name|setDeleteReleasedSnapshots
parameter_list|(
name|boolean
name|deleteReleasedSnapshots
parameter_list|)
block|{
name|this
operator|.
name|deleteReleasedSnapshots
operator|=
name|deleteReleasedSnapshots
expr_stmt|;
block|}
specifier|public
name|RepositorySessionFactory
name|getRepositorySessionFactory
parameter_list|()
block|{
return|return
name|repositorySessionFactory
return|;
block|}
specifier|public
name|void
name|setRepositorySessionFactory
parameter_list|(
name|RepositorySessionFactory
name|repositorySessionFactory
parameter_list|)
block|{
name|this
operator|.
name|repositorySessionFactory
operator|=
name|repositorySessionFactory
expr_stmt|;
block|}
specifier|public
name|RepositorySession
name|getRepositorySession
parameter_list|()
block|{
return|return
name|repositorySession
return|;
block|}
specifier|public
name|void
name|setRepositorySession
parameter_list|(
name|RepositorySession
name|repositorySession
parameter_list|)
block|{
name|this
operator|.
name|repositorySession
operator|=
name|repositorySession
expr_stmt|;
block|}
block|}
end_class

end_unit

