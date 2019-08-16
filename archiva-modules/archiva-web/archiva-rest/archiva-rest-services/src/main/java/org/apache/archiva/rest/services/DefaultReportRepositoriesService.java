begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
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
name|stats
operator|.
name|model
operator|.
name|RepositoryStatistics
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
name|stats
operator|.
name|model
operator|.
name|RepositoryStatisticsManager
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ReportRepositoriesService
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
comment|/**  * DefaultReportRepositoriesService  *  * @author Adrien Lecharpentier&lt;adrien.lecharpentier@zenika.com&gt;  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"reportRepositoriesService#rest"
argument_list|)
specifier|public
class|class
name|DefaultReportRepositoriesService
extends|extends
name|AbstractRestService
implements|implements
name|ReportRepositoriesService
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ALL_REPOSITORIES
init|=
literal|"all"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|getStatisticsReport
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositoriesId
parameter_list|,
name|int
name|rowCount
parameter_list|,
name|Date
name|startDate
parameter_list|,
name|Date
name|endDate
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
switch|switch
condition|(
name|repositoriesId
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
literal|"report.statistics.report.missing-repositories"
argument_list|,
literal|null
argument_list|)
throw|;
case|case
literal|1
case|:
return|return
name|getUniqueRepositoryReport
argument_list|(
name|repositoriesId
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|rowCount
argument_list|,
name|startDate
argument_list|,
name|endDate
argument_list|)
return|;
default|default:
return|return
name|getMultipleRepositoriesReport
argument_list|(
name|repositoriesId
argument_list|,
name|rowCount
argument_list|)
return|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|getMultipleRepositoriesReport
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositoriesId
parameter_list|,
name|int
name|rowCount
parameter_list|)
block|{
name|RepositorySession
name|repositorySession
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
try|try
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|stats
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repo
range|:
name|repositoriesId
control|)
block|{
try|try
block|{
name|stats
operator|.
name|add
argument_list|(
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|repo
argument_list|)
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
literal|"Unable to retrieve stats, assuming is empty: {}"
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
return|return
name|stats
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|size
argument_list|()
operator|>
name|rowCount
condition|?
name|rowCount
else|:
name|stats
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|getUniqueRepositoryReport
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|int
name|rowCount
parameter_list|,
name|Date
name|startDate
parameter_list|,
name|Date
name|endDate
parameter_list|)
block|{
name|RepositorySession
name|repositorySession
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
try|try
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|stats
init|=
literal|null
decl_stmt|;
try|try
block|{
name|stats
operator|=
name|repositoryStatisticsManager
operator|.
name|getStatisticsInRange
argument_list|(
name|repositoryId
argument_list|,
name|startDate
argument_list|,
name|endDate
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
literal|"Unable to retrieve stats, assuming is empty: {}"
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
if|if
condition|(
name|stats
operator|==
literal|null
operator|||
name|stats
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
expr|<
name|RepositoryStatistics
operator|>
name|emptyList
argument_list|()
return|;
block|}
return|return
name|stats
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|size
argument_list|()
operator|>
name|rowCount
condition|?
name|rowCount
else|:
name|stats
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|RepositoryProblemFacet
argument_list|>
name|getHealthReport
parameter_list|(
name|String
name|repository
parameter_list|,
name|String
name|groupId
parameter_list|,
name|int
name|rowCount
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|RepositorySession
name|repositorySession
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositories
init|=
name|getObservableRepos
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ALL_REPOSITORIES
operator|.
name|equals
argument_list|(
name|repository
argument_list|)
operator|&&
operator|!
name|observableRepositories
operator|.
name|contains
argument_list|(
name|repository
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
literal|"${$.i18n.prop('report.repository.illegal-access', "
operator|+
name|repository
operator|+
literal|")}"
argument_list|,
literal|"repositoryId"
argument_list|,
operator|new
name|IllegalAccessException
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|ALL_REPOSITORIES
operator|.
name|equals
argument_list|(
name|repository
argument_list|)
condition|)
block|{
name|observableRepositories
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|RepositoryProblemFacet
argument_list|>
name|problemArtifacts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|observableRepositories
control|)
block|{
for|for
control|(
name|String
name|name
range|:
name|metadataRepository
operator|.
name|getMetadataFacets
argument_list|(
name|repositorySession
argument_list|,
name|repoId
argument_list|,
name|RepositoryProblemFacet
operator|.
name|FACET_ID
argument_list|)
control|)
block|{
name|RepositoryProblemFacet
name|metadataFacet
init|=
operator|(
name|RepositoryProblemFacet
operator|)
name|metadataRepository
operator|.
name|getMetadataFacet
argument_list|(
name|repositorySession
argument_list|,
name|repoId
argument_list|,
name|RepositoryProblemFacet
operator|.
name|FACET_ID
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|groupId
argument_list|)
operator|||
name|groupId
operator|.
name|equals
argument_list|(
name|metadataFacet
operator|.
name|getNamespace
argument_list|()
argument_list|)
condition|)
block|{
name|problemArtifacts
operator|.
name|add
argument_list|(
name|metadataFacet
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|problemArtifacts
return|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
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
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

