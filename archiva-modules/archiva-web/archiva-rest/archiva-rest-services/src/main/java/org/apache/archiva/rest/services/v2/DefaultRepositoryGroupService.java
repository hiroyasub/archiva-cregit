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
operator|.
name|v2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|EntityExistsException
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
name|EntityNotFoundException
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
name|RepositoryAdminException
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
name|group
operator|.
name|RepositoryGroupAdmin
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
name|components
operator|.
name|rest
operator|.
name|model
operator|.
name|PagedResult
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
name|components
operator|.
name|rest
operator|.
name|util
operator|.
name|QueryHelper
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
name|model
operator|.
name|v2
operator|.
name|RepositoryGroup
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
name|v2
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
name|v2
operator|.
name|ErrorKeys
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
name|v2
operator|.
name|ErrorMessage
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
name|v2
operator|.
name|RepositoryGroupService
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
name|lang3
operator|.
name|StringUtils
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriInfo
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
name|Comparator
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
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
operator|.
name|utils
operator|.
name|AuditHelper
operator|.
name|getAuditData
import|;
end_import

begin_comment
comment|/**  * REST V2 Implementation for repository groups.  *  * @author Martin Stockhammer<martin_s@apache.org>  * @see RepositoryGroupService  * @since 3.0  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"v2.repositoryGroupService#rest"
argument_list|)
specifier|public
class|class
name|DefaultRepositoryGroupService
implements|implements
name|RepositoryGroupService
block|{
annotation|@
name|Context
name|HttpServletResponse
name|httpServletResponse
decl_stmt|;
annotation|@
name|Context
name|UriInfo
name|uriInfo
decl_stmt|;
specifier|final
specifier|private
name|RepositoryGroupAdmin
name|repositoryGroupAdmin
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultRepositoryGroupService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QueryHelper
argument_list|<
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
name|RepositoryGroup
argument_list|>
name|QUERY_HELPER
init|=
operator|new
name|QueryHelper
argument_list|<>
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"id"
block|}
argument_list|)
decl_stmt|;
static|static
block|{
name|QUERY_HELPER
operator|.
name|addStringFilter
argument_list|(
literal|"id"
argument_list|,
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
name|RepositoryGroup
operator|::
name|getId
argument_list|)
expr_stmt|;
name|QUERY_HELPER
operator|.
name|addNullsafeFieldComparator
argument_list|(
literal|"id"
argument_list|,
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
name|RepositoryGroup
operator|::
name|getId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DefaultRepositoryGroupService
parameter_list|(
name|RepositoryGroupAdmin
name|repositoryGroupAdmin
parameter_list|)
block|{
name|this
operator|.
name|repositoryGroupAdmin
operator|=
name|repositoryGroupAdmin
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PagedResult
argument_list|<
name|RepositoryGroup
argument_list|>
name|getRepositoriesGroups
parameter_list|(
name|String
name|searchTerm
parameter_list|,
name|Integer
name|offset
parameter_list|,
name|Integer
name|limit
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|orderBy
parameter_list|,
name|String
name|order
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|Predicate
argument_list|<
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
name|RepositoryGroup
argument_list|>
name|filter
init|=
name|QUERY_HELPER
operator|.
name|getQueryFilter
argument_list|(
name|searchTerm
argument_list|)
decl_stmt|;
name|Comparator
argument_list|<
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
name|RepositoryGroup
argument_list|>
name|ordering
init|=
name|QUERY_HELPER
operator|.
name|getComparator
argument_list|(
name|orderBy
argument_list|,
name|QUERY_HELPER
operator|.
name|isAscending
argument_list|(
name|order
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|totalCount
init|=
name|Math
operator|.
name|toIntExact
argument_list|(
name|repositoryGroupAdmin
operator|.
name|getRepositoriesGroups
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|filter
argument_list|)
operator|.
name|count
argument_list|( )
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RepositoryGroup
argument_list|>
name|result
init|=
name|repositoryGroupAdmin
operator|.
name|getRepositoriesGroups
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|filter
argument_list|)
operator|.
name|sorted
argument_list|(
name|ordering
argument_list|)
operator|.
name|skip
argument_list|(
name|offset
argument_list|)
operator|.
name|limit
argument_list|(
name|limit
argument_list|)
operator|.
name|map
argument_list|(
name|RepositoryGroup
operator|::
name|of
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|( )
argument_list|)
decl_stmt|;
return|return
operator|new
name|PagedResult
argument_list|<>
argument_list|(
name|totalCount
argument_list|,
name|offset
argument_list|,
name|limit
argument_list|,
name|result
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Repository admin error: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_ADMIN_ERROR
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not convert total count: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|INVALID_RESULT_SET_ERROR
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|getRepositoryGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryGroupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
literal|""
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
try|try
block|{
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
name|RepositoryGroup
name|group
init|=
name|repositoryGroupAdmin
operator|.
name|getRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|)
decl_stmt|;
return|return
name|RepositoryGroup
operator|.
name|of
argument_list|(
name|group
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|EntityNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
name|repositoryGroupId
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_ADMIN_ERROR
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
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
name|RepositoryGroup
name|toModel
parameter_list|(
name|RepositoryGroup
name|group
parameter_list|)
block|{
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
name|RepositoryGroup
name|result
init|=
operator|new
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
name|RepositoryGroup
argument_list|( )
decl_stmt|;
name|result
operator|.
name|setId
argument_list|(
name|group
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|result
operator|.
name|setLocation
argument_list|(
name|group
operator|.
name|getLocation
argument_list|( )
argument_list|)
expr_stmt|;
name|result
operator|.
name|setRepositories
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|group
operator|.
name|getRepositories
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setMergedIndexPath
argument_list|(
name|group
operator|.
name|getMergeConfiguration
argument_list|( )
operator|.
name|getMergedIndexPath
argument_list|( )
argument_list|)
expr_stmt|;
name|result
operator|.
name|setMergedIndexTtl
argument_list|(
name|group
operator|.
name|getMergeConfiguration
argument_list|( )
operator|.
name|getMergedIndexTtlMinutes
argument_list|( )
argument_list|)
expr_stmt|;
name|result
operator|.
name|setCronExpression
argument_list|(
name|group
operator|.
name|getMergeConfiguration
argument_list|( )
operator|.
name|getIndexMergeSchedule
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|addRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|Boolean
name|result
init|=
name|repositoryGroupAdmin
operator|.
name|addRepositoryGroup
argument_list|(
name|toModel
argument_list|(
name|repositoryGroup
argument_list|)
argument_list|,
name|getAuditData
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
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
name|RepositoryGroup
name|newGroup
init|=
name|repositoryGroupAdmin
operator|.
name|getRepositoryGroup
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|( )
argument_list|)
decl_stmt|;
if|if
condition|(
name|newGroup
operator|!=
literal|null
condition|)
block|{
return|return
name|RepositoryGroup
operator|.
name|of
argument_list|(
name|newGroup
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_ADD_FAILED
argument_list|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_ADD_FAILED
argument_list|)
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|EntityExistsException
name|e
parameter_list|)
block|{
name|httpServletResponse
operator|.
name|setHeader
argument_list|(
literal|"Location"
argument_list|,
name|uriInfo
operator|.
name|getAbsolutePathBuilder
argument_list|( )
operator|.
name|path
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|( )
argument_list|)
operator|.
name|build
argument_list|( )
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_EXIST
argument_list|,
name|repositoryGroup
operator|.
name|getId
argument_list|( )
argument_list|)
argument_list|,
literal|303
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
return|return
name|handleAdminException
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
specifier|private
name|RepositoryGroup
name|handleAdminException
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Repository admin error: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|,
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|.
name|keyExists
argument_list|( )
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|PREFIX
operator|+
name|e
operator|.
name|getKey
argument_list|( )
argument_list|,
name|e
operator|.
name|getParameters
argument_list|( )
argument_list|)
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_ADMIN_ERROR
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|updateRepositoryGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryGroupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
literal|""
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
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
name|RepositoryGroup
name|updateGroup
init|=
name|toModel
argument_list|(
name|repositoryGroup
argument_list|)
decl_stmt|;
try|try
block|{
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
name|RepositoryGroup
name|originGroup
init|=
name|repositoryGroupAdmin
operator|.
name|getRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|updateGroup
operator|.
name|getId
argument_list|( )
argument_list|)
condition|)
block|{
name|updateGroup
operator|.
name|setId
argument_list|(
name|repositoryGroupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|updateGroup
operator|.
name|getLocation
argument_list|( )
argument_list|)
condition|)
block|{
name|updateGroup
operator|.
name|setLocation
argument_list|(
name|originGroup
operator|.
name|getLocation
argument_list|( )
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|updateGroup
operator|.
name|getMergedIndexPath
argument_list|( )
argument_list|)
condition|)
block|{
name|updateGroup
operator|.
name|setMergedIndexPath
argument_list|(
name|originGroup
operator|.
name|getMergedIndexPath
argument_list|( )
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|updateGroup
operator|.
name|getCronExpression
argument_list|( )
operator|==
literal|null
condition|)
block|{
name|updateGroup
operator|.
name|setCronExpression
argument_list|(
name|originGroup
operator|.
name|getCronExpression
argument_list|( )
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|updateGroup
operator|.
name|getRepositories
argument_list|( )
operator|==
literal|null
operator|||
name|updateGroup
operator|.
name|getRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
operator|==
literal|0
condition|)
block|{
name|updateGroup
operator|.
name|setRepositories
argument_list|(
name|originGroup
operator|.
name|getRepositories
argument_list|( )
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|updateGroup
operator|.
name|getMergedIndexTtl
argument_list|( )
operator|<=
literal|0
condition|)
block|{
name|updateGroup
operator|.
name|setMergedIndexTtl
argument_list|(
name|originGroup
operator|.
name|getMergedIndexTtl
argument_list|( )
argument_list|)
expr_stmt|;
block|}
name|repositoryGroupAdmin
operator|.
name|updateRepositoryGroup
argument_list|(
name|updateGroup
argument_list|,
name|getAuditData
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|RepositoryGroup
operator|.
name|of
argument_list|(
name|repositoryGroupAdmin
operator|.
name|getRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|EntityNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
name|repositoryGroupId
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
return|return
name|handleAdminException
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Response
name|deleteRepositoryGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryGroupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
literal|""
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
try|try
block|{
name|Boolean
name|deleted
init|=
name|repositoryGroupAdmin
operator|.
name|deleteRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|,
name|getAuditData
argument_list|( )
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|deleted
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_DELETE_FAILED
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|( )
operator|.
name|build
argument_list|( )
return|;
block|}
catch|catch
parameter_list|(
name|EntityNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
name|repositoryGroupId
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|handleAdminException
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// cannot happen:
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|addRepositoryToGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryGroupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
literal|""
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_NOT_FOUND
argument_list|,
literal|""
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
try|try
block|{
name|repositoryGroupAdmin
operator|.
name|addRepositoryToGroup
argument_list|(
name|repositoryGroupId
argument_list|,
name|repositoryId
argument_list|,
name|getAuditData
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|RepositoryGroup
operator|.
name|of
argument_list|(
name|repositoryGroupAdmin
operator|.
name|getRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|EntityNotFoundException
name|e
parameter_list|)
block|{
return|return
name|handleNotFoundException
argument_list|(
name|repositoryGroupId
argument_list|,
name|repositoryId
argument_list|,
name|e
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|EntityExistsException
name|e
parameter_list|)
block|{
comment|// This is thrown, if the repositoryId is already assigned to the group. We ignore this for the PUT action (nothing to do).
try|try
block|{
return|return
name|RepositoryGroup
operator|.
name|of
argument_list|(
name|repositoryGroupAdmin
operator|.
name|getRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|repositoryAdminException
parameter_list|)
block|{
return|return
name|handleAdminException
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
return|return
name|handleAdminException
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|deleteRepositoryFromGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
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
name|v2
operator|.
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryGroupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
literal|""
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_NOT_FOUND
argument_list|,
literal|""
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
try|try
block|{
name|repositoryGroupAdmin
operator|.
name|deleteRepositoryFromGroup
argument_list|(
name|repositoryGroupId
argument_list|,
name|repositoryId
argument_list|,
name|getAuditData
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|RepositoryGroup
operator|.
name|of
argument_list|(
name|repositoryGroupAdmin
operator|.
name|getRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|EntityNotFoundException
name|e
parameter_list|)
block|{
return|return
name|handleNotFoundException
argument_list|(
name|repositoryGroupId
argument_list|,
name|repositoryId
argument_list|,
name|e
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
return|return
name|handleAdminException
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
specifier|protected
name|RepositoryGroup
name|handleNotFoundException
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|EntityNotFoundException
name|e
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
if|if
condition|(
name|e
operator|.
name|getParameters
argument_list|( )
operator|.
name|length
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|repositoryGroupId
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getParameters
argument_list|( )
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
name|repositoryGroupId
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
if|else if
condition|(
name|repositoryId
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getParameters
argument_list|( )
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_NOT_FOUND
argument_list|,
name|repositoryGroupId
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
block|}
name|log
operator|.
name|warn
argument_list|(
literal|"Entity not found but neither group nor repo set in exception"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|ErrorMessage
operator|.
name|of
argument_list|(
name|ErrorKeys
operator|.
name|REPOSITORY_GROUP_NOT_FOUND
argument_list|,
name|repositoryGroupId
argument_list|)
argument_list|,
literal|404
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

