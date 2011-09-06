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
name|filter
operator|.
name|ExcludesFilter
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
name|storage
operator|.
name|RepositoryStorage
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
name|storage
operator|.
name|RepositoryStorageMetadataInvalidException
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
name|storage
operator|.
name|RepositoryStorageMetadataNotFoundException
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
name|Collection
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
comment|/**  * Default implementation of the metadata resolver API. At present it will handle updating the content repository  * from new or changed information in the model and artifacts from the repository storage.  *<p/>  * This is a singleton component to allow an alternate implementation to be provided. It is intended to be the same  * system-wide for the whole content repository instead of on a per-managed-repository basis. Therefore, the session is  * passed in as an argument to obtain any necessary resources, rather than the class being instantiated within the  * session in the context of a single managed repository's resolution needs.  *<p/>  * Note that the caller is responsible for the session, such as closing and saving (which is implied by the resolver  * being obtained from within the session). The {@link RepositorySession#markDirty()} method is used as a hint to ensure  * that the session knows we've made changes at close. We cannot ensure the changes will be persisted if the caller  * chooses to revert first. This is preferable to storing the metadata immediately - a separate session would require  * having a bi-directional link with the session factory, and saving the existing session might save other changes  * unknowingly by the caller.  *<p/>  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"metadataResolver#default"
argument_list|)
specifier|public
class|class
name|DefaultMetadataResolver
implements|implements
name|MetadataResolver
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultMetadataResolver
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * FIXME: this needs to be configurable based on storage type - and could also be instantiated per repo. Change to a      * factory, and perhaps retrieve from the session. We should avoid creating one per request, however.      *<p/>      * TODO: Also need to accommodate availability of proxy module      * ... could be a different type since we need methods to modify the storage metadata, which would also allow more      * appropriate methods to pass in the already determined repository configuration, for example, instead of the ID      *      *      */
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryStorage#maven2"
argument_list|)
specifier|private
name|RepositoryStorage
name|repositoryStorage
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
decl_stmt|;
annotation|@
name|PostConstruct
specifier|private
name|void
name|initialize
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ProjectVersionMetadata
name|resolveProjectVersion
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throws|throws
name|MetadataResolutionException
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|ProjectVersionMetadata
name|metadata
init|=
name|metadataRepository
operator|.
name|getProjectVersion
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
decl_stmt|;
comment|// TODO: do we want to detect changes as well by comparing timestamps? isProjectVersionNewerThan(updated)
comment|//       in such cases we might also remove/update stale metadata, including adjusting plugin-based facets
comment|//       This would also be better than checking for completeness - we can then refresh only when fixed (though
comment|//       sometimes this has an additional dependency - such as a parent - requesting the user to force an update
comment|//       may then work here and be more efficient than always trying again)
if|if
condition|(
name|metadata
operator|==
literal|null
operator|||
name|metadata
operator|.
name|isIncomplete
argument_list|()
condition|)
block|{
try|try
block|{
name|metadata
operator|=
name|repositoryStorage
operator|.
name|readProjectVersionMetadata
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved project version metadata from storage: "
operator|+
name|metadata
argument_list|)
expr_stmt|;
block|}
comment|// FIXME: make this a more generic post-processing that plugins can take advantage of
comment|//       eg. maven projects should be able to process parent here
if|if
condition|(
operator|!
name|metadata
operator|.
name|getDependencies
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ProjectVersionReference
name|ref
init|=
operator|new
name|ProjectVersionReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setProjectId
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setProjectVersion
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setReferenceType
argument_list|(
name|ProjectVersionReference
operator|.
name|ReferenceType
operator|.
name|DEPENDENCY
argument_list|)
expr_stmt|;
block|}
try|try
block|{
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|addArtifact
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
name|metadataRepository
operator|.
name|updateProjectVersion
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to persist resolved information: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|markDirty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryStorageMetadataInvalidException
name|e
parameter_list|)
block|{
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|addArtifactProblem
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|MetadataResolutionException
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
catch|catch
parameter_list|(
name|RepositoryStorageMetadataNotFoundException
name|e
parameter_list|)
block|{
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|addArtifactProblem
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// no need to rethrow - return null
block|}
block|}
return|return
name|metadata
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|ProjectVersionReference
argument_list|>
name|resolveProjectReferences
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throws|throws
name|MetadataResolutionException
block|{
comment|// TODO: is this assumption correct? could a storage mech. actually know all references in a non-Maven scenario?
comment|// not passed to the storage mechanism as resolving references would require iterating all artifacts
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|()
decl_stmt|;
return|return
name|metadataRepository
operator|.
name|getProjectReferences
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|resolveRootNamespaces
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
name|metadataRepository
operator|.
name|getRootNamespaces
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|storageNamespaces
init|=
name|repositoryStorage
operator|.
name|listRootNamespaces
argument_list|(
name|repoId
argument_list|,
operator|new
name|ExcludesFilter
argument_list|<
name|String
argument_list|>
argument_list|(
name|namespaces
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|storageNamespaces
operator|!=
literal|null
operator|&&
operator|!
name|storageNamespaces
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved root namespaces from storage: "
operator|+
name|storageNamespaces
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|n
range|:
name|storageNamespaces
control|)
block|{
try|try
block|{
name|metadataRepository
operator|.
name|updateNamespace
argument_list|(
name|repoId
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to persist resolved information: "
operator|+
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
name|session
operator|.
name|markDirty
argument_list|()
expr_stmt|;
name|namespaces
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|addAll
argument_list|(
name|storageNamespaces
argument_list|)
expr_stmt|;
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
name|resolveNamespaces
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
name|metadataRepository
operator|.
name|getNamespaces
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|exclusions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|namespaces
argument_list|)
decl_stmt|;
name|exclusions
operator|.
name|addAll
argument_list|(
name|metadataRepository
operator|.
name|getProjects
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|storageNamespaces
init|=
name|repositoryStorage
operator|.
name|listNamespaces
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
operator|new
name|ExcludesFilter
argument_list|<
name|String
argument_list|>
argument_list|(
name|exclusions
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|storageNamespaces
operator|!=
literal|null
operator|&&
operator|!
name|storageNamespaces
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved namespaces from storage: "
operator|+
name|storageNamespaces
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|n
range|:
name|storageNamespaces
control|)
block|{
try|try
block|{
name|metadataRepository
operator|.
name|updateNamespace
argument_list|(
name|repoId
argument_list|,
name|namespace
operator|+
literal|"."
operator|+
name|n
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to persist resolved information: "
operator|+
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
name|session
operator|.
name|markDirty
argument_list|()
expr_stmt|;
name|namespaces
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|addAll
argument_list|(
name|storageNamespaces
argument_list|)
expr_stmt|;
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
name|resolveProjects
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|projects
init|=
name|metadataRepository
operator|.
name|getProjects
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|exclusions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|projects
argument_list|)
decl_stmt|;
name|exclusions
operator|.
name|addAll
argument_list|(
name|metadataRepository
operator|.
name|getNamespaces
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|storageProjects
init|=
name|repositoryStorage
operator|.
name|listProjects
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
operator|new
name|ExcludesFilter
argument_list|<
name|String
argument_list|>
argument_list|(
name|exclusions
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|storageProjects
operator|!=
literal|null
operator|&&
operator|!
name|storageProjects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved projects from storage: "
operator|+
name|storageProjects
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|projectId
range|:
name|storageProjects
control|)
block|{
name|ProjectMetadata
name|projectMetadata
init|=
name|repositoryStorage
operator|.
name|readProjectMetadata
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectMetadata
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|metadataRepository
operator|.
name|updateProject
argument_list|(
name|repoId
argument_list|,
name|projectMetadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to persist resolved information: "
operator|+
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
name|session
operator|.
name|markDirty
argument_list|()
expr_stmt|;
name|projects
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|projects
argument_list|)
expr_stmt|;
name|projects
operator|.
name|addAll
argument_list|(
name|storageProjects
argument_list|)
expr_stmt|;
block|}
return|return
name|projects
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|resolveProjectVersions
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|projectVersions
init|=
name|metadataRepository
operator|.
name|getProjectVersions
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|storageProjectVersions
init|=
name|repositoryStorage
operator|.
name|listProjectVersions
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
operator|new
name|ExcludesFilter
argument_list|<
name|String
argument_list|>
argument_list|(
name|projectVersions
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|storageProjectVersions
operator|!=
literal|null
operator|&&
operator|!
name|storageProjectVersions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved project versions from storage: "
operator|+
name|storageProjectVersions
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|projectVersion
range|:
name|storageProjectVersions
control|)
block|{
try|try
block|{
name|ProjectVersionMetadata
name|versionMetadata
init|=
name|repositoryStorage
operator|.
name|readProjectVersionMetadata
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
decl_stmt|;
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|addArtifact
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|versionMetadata
argument_list|)
expr_stmt|;
block|}
name|metadataRepository
operator|.
name|updateProjectVersion
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|versionMetadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to persist resolved information: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryStorageMetadataInvalidException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Not update project in metadata repository due to an error resolving it from storage: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|addArtifactProblem
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryStorageMetadataNotFoundException
name|e
parameter_list|)
block|{
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|addArtifactProblem
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|session
operator|.
name|markDirty
argument_list|()
expr_stmt|;
name|projectVersions
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|projectVersions
argument_list|)
expr_stmt|;
name|projectVersions
operator|.
name|addAll
argument_list|(
name|storageProjectVersions
argument_list|)
expr_stmt|;
block|}
return|return
name|projectVersions
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|resolveArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throws|throws
name|MetadataResolutionException
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
init|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
decl_stmt|;
name|ExcludesFilter
argument_list|<
name|String
argument_list|>
name|filter
init|=
operator|new
name|ExcludesFilter
argument_list|<
name|String
argument_list|>
argument_list|(
name|createArtifactIdList
argument_list|(
name|artifacts
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|storageArtifacts
init|=
name|repositoryStorage
operator|.
name|readArtifactsMetadata
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|filter
argument_list|)
decl_stmt|;
if|if
condition|(
name|storageArtifacts
operator|!=
literal|null
operator|&&
operator|!
name|storageArtifacts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved artifacts from storage: "
operator|+
name|storageArtifacts
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|storageArtifacts
control|)
block|{
try|try
block|{
name|metadataRepository
operator|.
name|updateArtifact
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to persist resolved information: "
operator|+
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
name|session
operator|.
name|markDirty
argument_list|()
expr_stmt|;
name|artifacts
operator|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|addAll
argument_list|(
name|storageArtifacts
argument_list|)
expr_stmt|;
block|}
return|return
name|artifacts
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|createArtifactIdList
parameter_list|(
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
parameter_list|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|artifactIds
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|artifacts
control|)
block|{
name|artifactIds
operator|.
name|add
argument_list|(
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|artifactIds
return|;
block|}
block|}
end_class

end_unit

