begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|reports
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
name|facets
operator|.
name|RepositoryProblemFacet
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
name|MetadataRepository
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
name|MetadataRepositoryException
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
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|RepositoryStorageMetadataException
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
name|stereotype
operator|.
name|Service
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

begin_comment
comment|/**  * Process repository management events and respond appropriately.  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryListener#problem-reports"
argument_list|)
specifier|public
class|class
name|RepositoryProblemEventListener
implements|implements
name|RepositoryListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RepositoryProblemEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
comment|// FIXME: move to session
annotation|@
name|Override
specifier|public
name|void
name|deleteArtifact
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|project
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|String
name|name
init|=
name|RepositoryProblemFacet
operator|.
name|createName
argument_list|(
name|namespace
argument_list|,
name|project
argument_list|,
name|version
argument_list|,
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|RepositorySession
name|session
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
name|metadataRepository
operator|.
name|removeMetadataFacet
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|RepositoryProblemFacet
operator|.
name|FACET_ID
argument_list|,
name|name
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
literal|"Unable to remove metadata facet as part of delete event: {}"
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|addArtifact
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
name|ProjectVersionMetadata
name|metadata
parameter_list|)
block|{
comment|// Remove problems associated with this version on successful addition
comment|// TODO: this removes all problems - do we need something that just remove the problems we know are corrected?
name|String
name|name
init|=
name|RepositoryProblemFacet
operator|.
name|createName
argument_list|(
name|namespace
argument_list|,
name|projectId
argument_list|,
name|metadata
operator|.
name|getId
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|metadataRepository
operator|.
name|removeMetadataFacet
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|RepositoryProblemFacet
operator|.
name|FACET_ID
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|session
operator|.
name|markDirty
argument_list|()
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
literal|"Unable to remove repository problem facets for the version being corrected in the repository: {}"
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|addArtifactProblem
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
parameter_list|,
name|RepositoryStorageMetadataException
name|exception
parameter_list|)
block|{
name|RepositoryProblemFacet
name|problem
init|=
operator|new
name|RepositoryProblemFacet
argument_list|()
decl_stmt|;
name|problem
operator|.
name|setMessage
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setProject
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setRepositoryId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setVersion
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setProblem
argument_list|(
name|exception
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|session
operator|.
name|getRepository
argument_list|()
operator|.
name|addMetadataFacet
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|problem
argument_list|)
expr_stmt|;
name|session
operator|.
name|markDirty
argument_list|()
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
literal|"Unable to add repository problem facets for the version being removed: {}"
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
block|}
block|}
end_class

end_unit

