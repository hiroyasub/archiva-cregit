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
operator|.
name|reports
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|webwork
operator|.
name|interceptor
operator|.
name|ServletRequestAware
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|Preparable
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
name|database
operator|.
name|ArchivaDAO
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
name|database
operator|.
name|Constraint
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
name|database
operator|.
name|constraints
operator|.
name|RangeConstraint
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
name|database
operator|.
name|constraints
operator|.
name|RepositoryProblemByGroupIdConstraint
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
name|database
operator|.
name|constraints
operator|.
name|RepositoryProblemByRepositoryIdConstraint
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
name|database
operator|.
name|constraints
operator|.
name|RepositoryProblemConstraint
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
name|database
operator|.
name|constraints
operator|.
name|UniqueFieldConstraint
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
name|RepositoryProblem
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
name|RepositoryProblemReport
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
name|security
operator|.
name|ArchivaRoleConstants
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
name|redback
operator|.
name|rbac
operator|.
name|Resource
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
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureAction
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
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionBundle
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
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionException
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
comment|/**  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="generateReport"  */
end_comment

begin_class
specifier|public
class|class
name|GenerateReportAction
extends|extends
name|PlexusActionSupport
implements|implements
name|SecureAction
implements|,
name|ServletRequestAware
implements|,
name|Preparable
block|{
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|protected
name|ArchivaDAO
name|dao
decl_stmt|;
specifier|protected
name|Constraint
name|constraint
decl_stmt|;
specifier|protected
name|HttpServletRequest
name|request
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|RepositoryProblemReport
argument_list|>
name|reports
init|=
operator|new
name|ArrayList
argument_list|<
name|RepositoryProblemReport
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|String
name|groupId
decl_stmt|;
specifier|protected
name|String
name|repositoryId
decl_stmt|;
specifier|protected
name|String
name|prev
decl_stmt|;
specifier|protected
name|String
name|next
decl_stmt|;
specifier|protected
name|int
index|[]
name|range
init|=
operator|new
name|int
index|[
literal|2
index|]
decl_stmt|;
specifier|protected
name|int
name|page
init|=
literal|1
decl_stmt|;
specifier|protected
name|int
name|rowCount
init|=
literal|100
decl_stmt|;
specifier|protected
name|boolean
name|isLastPage
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BLANK
init|=
literal|"blank"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BASIC
init|=
literal|"basic"
decl_stmt|;
specifier|private
specifier|static
name|Boolean
name|jasperPresent
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoryIds
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALL_REPOSITORIES
init|=
literal|"All Repositories"
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|repositoryIds
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|repositoryIds
operator|.
name|add
argument_list|(
name|ALL_REPOSITORIES
argument_list|)
expr_stmt|;
comment|// comes first to be first in the list
name|repositoryIds
operator|.
name|addAll
argument_list|(
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueFieldConstraint
argument_list|(
name|RepositoryProblem
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"repositoryId"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getRepositoryIds
parameter_list|()
block|{
return|return
name|repositoryIds
return|;
block|}
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RepositoryProblem
argument_list|>
name|problemArtifacts
init|=
name|dao
operator|.
name|getRepositoryProblemDAO
argument_list|()
operator|.
name|queryRepositoryProblems
argument_list|(
name|configureConstraint
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|contextPath
init|=
name|request
operator|.
name|getRequestURL
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|request
operator|.
name|getRequestURL
argument_list|()
operator|.
name|indexOf
argument_list|(
name|request
operator|.
name|getRequestURI
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|RepositoryProblem
name|problemArtifact
decl_stmt|;
name|RepositoryProblemReport
name|problemArtifactReport
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
name|problemArtifacts
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|problemArtifact
operator|=
operator|(
name|RepositoryProblem
operator|)
name|problemArtifacts
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|problemArtifactReport
operator|=
operator|new
name|RepositoryProblemReport
argument_list|(
name|problemArtifact
argument_list|)
expr_stmt|;
name|problemArtifactReport
operator|.
name|setGroupURL
argument_list|(
name|contextPath
operator|+
literal|"/browse/"
operator|+
name|problemArtifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|problemArtifactReport
operator|.
name|setArtifactURL
argument_list|(
name|contextPath
operator|+
literal|"/browse/"
operator|+
name|problemArtifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|"/"
operator|+
name|problemArtifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|reports
operator|.
name|add
argument_list|(
name|problemArtifactReport
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reports
operator|.
name|size
argument_list|()
operator|<=
name|rowCount
condition|)
block|{
name|isLastPage
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|reports
operator|.
name|remove
argument_list|(
name|rowCount
argument_list|)
expr_stmt|;
block|}
name|prev
operator|=
name|request
operator|.
name|getRequestURL
argument_list|()
operator|+
literal|"?page="
operator|+
operator|(
name|page
operator|-
literal|1
operator|)
operator|+
literal|"&rowCount="
operator|+
name|rowCount
operator|+
literal|"&groupId="
operator|+
name|groupId
operator|+
literal|"&repositoryId="
operator|+
name|repositoryId
expr_stmt|;
name|next
operator|=
name|request
operator|.
name|getRequestURL
argument_list|()
operator|+
literal|"?page="
operator|+
operator|(
name|page
operator|+
literal|1
operator|)
operator|+
literal|"&rowCount="
operator|+
name|rowCount
operator|+
literal|"&groupId="
operator|+
name|groupId
operator|+
literal|"&repositoryId="
operator|+
name|repositoryId
expr_stmt|;
if|if
condition|(
name|reports
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|page
operator|==
literal|1
condition|)
block|{
return|return
name|BLANK
return|;
block|}
if|else if
condition|(
name|isJasperPresent
argument_list|()
condition|)
block|{
return|return
literal|"jasper"
return|;
block|}
else|else
block|{
return|return
name|SUCCESS
return|;
block|}
block|}
specifier|private
specifier|static
name|boolean
name|isJasperPresent
parameter_list|()
block|{
if|if
condition|(
name|jasperPresent
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
literal|"net.sf.jasperreports.engine.JRExporterParameter"
argument_list|)
expr_stmt|;
name|jasperPresent
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
name|jasperPresent
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|jasperPresent
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
block|}
return|return
name|jasperPresent
operator|.
name|booleanValue
argument_list|()
return|;
block|}
specifier|private
name|Constraint
name|configureConstraint
parameter_list|()
block|{
name|Constraint
name|constraint
decl_stmt|;
name|range
index|[
literal|0
index|]
operator|=
operator|(
name|page
operator|-
literal|1
operator|)
operator|*
name|rowCount
expr_stmt|;
name|range
index|[
literal|1
index|]
operator|=
operator|(
name|page
operator|*
name|rowCount
operator|)
operator|+
literal|1
expr_stmt|;
comment|// Add 1 to check if it's the last page or not.
if|if
condition|(
name|groupId
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|groupId
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|)
condition|)
block|{
if|if
condition|(
name|repositoryId
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|repositoryId
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|&&
operator|!
name|repositoryId
operator|.
name|equals
argument_list|(
name|ALL_REPOSITORIES
argument_list|)
operator|)
condition|)
block|{
name|constraint
operator|=
operator|new
name|RepositoryProblemConstraint
argument_list|(
name|range
argument_list|,
name|groupId
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|constraint
operator|=
operator|new
name|RepositoryProblemByGroupIdConstraint
argument_list|(
name|range
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|repositoryId
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|repositoryId
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|&&
operator|!
name|repositoryId
operator|.
name|equals
argument_list|(
name|ALL_REPOSITORIES
argument_list|)
operator|)
condition|)
block|{
name|constraint
operator|=
operator|new
name|RepositoryProblemByRepositoryIdConstraint
argument_list|(
name|range
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|constraint
operator|=
operator|new
name|RangeConstraint
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
return|return
name|constraint
return|;
block|}
specifier|public
name|void
name|setServletRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|RepositoryProblemReport
argument_list|>
name|getReports
parameter_list|()
block|{
return|return
name|reports
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
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|String
name|getPrev
parameter_list|()
block|{
return|return
name|prev
return|;
block|}
specifier|public
name|String
name|getNext
parameter_list|()
block|{
return|return
name|next
return|;
block|}
specifier|public
name|int
name|getPage
parameter_list|()
block|{
return|return
name|page
return|;
block|}
specifier|public
name|void
name|setPage
parameter_list|(
name|int
name|page
parameter_list|)
block|{
name|this
operator|.
name|page
operator|=
name|page
expr_stmt|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
name|rowCount
return|;
block|}
specifier|public
name|void
name|setRowCount
parameter_list|(
name|int
name|rowCount
parameter_list|)
block|{
name|this
operator|.
name|rowCount
operator|=
name|rowCount
expr_stmt|;
block|}
specifier|public
name|boolean
name|getIsLastPage
parameter_list|()
block|{
return|return
name|isLastPage
return|;
block|}
specifier|public
name|SecureActionBundle
name|getSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|SecureActionBundle
name|bundle
init|=
operator|new
name|SecureActionBundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|setRequiresAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|addRequiredAuthorization
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_ACCESS_REPORT
argument_list|,
name|Resource
operator|.
name|GLOBAL
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
block|}
end_class

end_unit

