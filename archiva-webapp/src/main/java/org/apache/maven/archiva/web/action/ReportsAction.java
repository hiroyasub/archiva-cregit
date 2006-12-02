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
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|configuration
operator|.
name|Configuration
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
name|ConfigurationStore
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
name|ConfiguredRepositoryFactory
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
name|discoverer
operator|.
name|DiscovererException
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
name|discoverer
operator|.
name|filter
operator|.
name|AcceptAllArtifactFilter
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
name|discoverer
operator|.
name|filter
operator|.
name|SnapshotArtifactFilter
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
name|reporting
operator|.
name|executor
operator|.
name|ReportExecutor
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
name|reporting
operator|.
name|group
operator|.
name|ReportGroup
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
name|reporting
operator|.
name|database
operator|.
name|ReportingDatabase
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
name|reporting
operator|.
name|store
operator|.
name|ReportingStoreException
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
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|artifact
operator|.
name|resolver
operator|.
name|filter
operator|.
name|ArtifactFilter
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
name|security
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
name|security
operator|.
name|ui
operator|.
name|web
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
name|security
operator|.
name|ui
operator|.
name|web
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
name|security
operator|.
name|ui
operator|.
name|web
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
comment|/**  * Repository reporting.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="reportsAction"  * @todo split report access and report generation  */
end_comment

begin_class
specifier|public
class|class
name|ReportsAction
extends|extends
name|PlexusActionSupport
implements|implements
name|Preparable
implements|,
name|SecureAction
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfiguredRepositoryFactory
name|factory
decl_stmt|;
specifier|private
name|List
name|databases
decl_stmt|;
specifier|private
name|String
name|repositoryId
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ReportExecutor
name|executor
decl_stmt|;
specifier|private
name|Configuration
name|configuration
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.reporting.group.ReportGroup"      */
specifier|private
name|Map
name|reports
decl_stmt|;
specifier|private
name|String
name|reportGroup
init|=
name|DEFAULT_REPORT_GROUP
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_REPORT_GROUP
init|=
literal|"health"
decl_stmt|;
specifier|private
name|String
name|filter
decl_stmt|;
specifier|public
name|String
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|ReportGroup
name|reportGroup
init|=
operator|(
name|ReportGroup
operator|)
name|reports
operator|.
name|get
argument_list|(
name|this
operator|.
name|reportGroup
argument_list|)
decl_stmt|;
name|databases
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
if|if
condition|(
name|repositoryId
operator|!=
literal|null
operator|&&
operator|!
name|repositoryId
operator|.
name|equals
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|RepositoryConfiguration
name|repositoryConfiguration
init|=
name|configuration
operator|.
name|getRepositoryById
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
name|getReport
argument_list|(
name|repositoryConfiguration
argument_list|,
name|reportGroup
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|configuration
operator|.
name|getRepositories
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryConfiguration
name|repositoryConfiguration
init|=
operator|(
name|RepositoryConfiguration
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|getReport
argument_list|(
name|repositoryConfiguration
argument_list|,
name|reportGroup
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|void
name|getReport
parameter_list|(
name|RepositoryConfiguration
name|repositoryConfiguration
parameter_list|,
name|ReportGroup
name|reportGroup
parameter_list|)
throws|throws
name|ReportingStoreException
block|{
name|ArtifactRepository
name|repository
init|=
name|factory
operator|.
name|createRepository
argument_list|(
name|repositoryConfiguration
argument_list|)
decl_stmt|;
name|ReportingDatabase
name|database
init|=
name|executor
operator|.
name|getReportDatabase
argument_list|(
name|repository
argument_list|,
name|reportGroup
argument_list|)
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
operator|&&
operator|!
name|filter
operator|.
name|equals
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|database
operator|=
name|database
operator|.
name|getFilteredDatabase
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|databases
operator|.
name|add
argument_list|(
name|database
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|runReport
parameter_list|()
throws|throws
name|Exception
block|{
name|ReportGroup
name|reportGroup
init|=
operator|(
name|ReportGroup
operator|)
name|reports
operator|.
name|get
argument_list|(
name|this
operator|.
name|reportGroup
argument_list|)
decl_stmt|;
name|RepositoryConfiguration
name|repositoryConfiguration
init|=
name|configuration
operator|.
name|getRepositoryById
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
name|ArtifactRepository
name|repository
init|=
name|factory
operator|.
name|createRepository
argument_list|(
name|repositoryConfiguration
argument_list|)
decl_stmt|;
name|ReportingDatabase
name|database
init|=
name|executor
operator|.
name|getReportDatabase
argument_list|(
name|repository
argument_list|,
name|reportGroup
argument_list|)
decl_stmt|;
if|if
condition|(
name|database
operator|.
name|isInProgress
argument_list|()
condition|)
block|{
return|return
name|SUCCESS
return|;
block|}
name|generateReport
argument_list|(
name|database
argument_list|,
name|repositoryConfiguration
argument_list|,
name|reportGroup
argument_list|,
name|repository
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
specifier|private
name|void
name|generateReport
parameter_list|(
name|ReportingDatabase
name|database
parameter_list|,
name|RepositoryConfiguration
name|repositoryConfiguration
parameter_list|,
name|ReportGroup
name|reportGroup
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|DiscovererException
throws|,
name|ReportingStoreException
block|{
name|database
operator|.
name|setInProgress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
name|blacklistedPatterns
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
if|if
condition|(
name|repositoryConfiguration
operator|.
name|getBlackListPatterns
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|blacklistedPatterns
operator|.
name|addAll
argument_list|(
name|repositoryConfiguration
operator|.
name|getBlackListPatterns
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getGlobalBlackListPatterns
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|blacklistedPatterns
operator|.
name|addAll
argument_list|(
name|configuration
operator|.
name|getGlobalBlackListPatterns
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ArtifactFilter
name|filter
decl_stmt|;
if|if
condition|(
name|repositoryConfiguration
operator|.
name|isIncludeSnapshots
argument_list|()
condition|)
block|{
name|filter
operator|=
operator|new
name|AcceptAllArtifactFilter
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|filter
operator|=
operator|new
name|SnapshotArtifactFilter
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|executor
operator|.
name|runReports
argument_list|(
name|reportGroup
argument_list|,
name|repository
argument_list|,
name|blacklistedPatterns
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|database
operator|.
name|setInProgress
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setReportGroup
parameter_list|(
name|String
name|reportGroup
parameter_list|)
block|{
name|this
operator|.
name|reportGroup
operator|=
name|reportGroup
expr_stmt|;
block|}
specifier|public
name|String
name|getReportGroup
parameter_list|()
block|{
return|return
name|reportGroup
return|;
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
name|List
name|getDatabases
parameter_list|()
block|{
return|return
name|databases
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
name|configuration
operator|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
specifier|public
name|Map
name|getReports
parameter_list|()
block|{
return|return
name|reports
return|;
block|}
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
specifier|public
name|void
name|setFilter
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
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

