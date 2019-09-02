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
name|stats
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
name|stats
operator|.
name|model
operator|.
name|DefaultRepositoryStatistics
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
name|metadata
operator|.
name|repository
operator|.
name|stats
operator|.
name|model
operator|.
name|RepositoryStatisticsProvider
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
name|RepositoryWalkingStatisticsProvider
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
name|time
operator|.
name|StopWatch
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

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryStatisticsManager#default"
argument_list|)
specifier|public
class|class
name|DefaultRepositoryStatisticsManager
implements|implements
name|RepositoryStatisticsManager
block|{
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
name|DefaultRepositoryStatisticsManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|TimeZone
name|UTC_TIME_ZONE
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
decl_stmt|;
specifier|private
name|RepositoryWalkingStatisticsProvider
name|walkingProvider
init|=
operator|new
name|RepositoryWalkingStatisticsProvider
argument_list|()
decl_stmt|;
annotation|@
name|Inject
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasStatistics
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
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
specifier|final
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|( )
decl_stmt|;
return|return
name|metadataRepository
operator|.
name|hasMetadataFacet
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|DefaultRepositoryStatistics
operator|.
name|FACET_ID
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryStatistics
name|getLastStatistics
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
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
specifier|final
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|( )
decl_stmt|;
comment|// TODO: consider a more efficient implementation that directly gets the last one from the content repository
name|List
argument_list|<
name|String
argument_list|>
name|scans
init|=
name|metadataRepository
operator|.
name|getMetadataFacets
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|DefaultRepositoryStatistics
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|scans
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|scans
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|scans
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|scans
operator|.
name|get
argument_list|(
name|scans
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|RepositoryStatistics
name|repositoryStatistics
init|=
name|RepositoryStatistics
operator|.
name|class
operator|.
name|cast
argument_list|(
name|metadataRepository
operator|.
name|getMetadataFacet
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|RepositoryStatistics
operator|.
name|FACET_ID
argument_list|,
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|stopWatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"time to find last RepositoryStatistics: {} ms"
argument_list|,
name|stopWatch
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|repositoryStatistics
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addStatisticsAfterScan
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|,
name|long
name|totalFiles
parameter_list|,
name|long
name|newFiles
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
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
specifier|final
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|( )
decl_stmt|;
name|DefaultRepositoryStatistics
name|repositoryStatistics
init|=
operator|new
name|DefaultRepositoryStatistics
argument_list|()
decl_stmt|;
name|repositoryStatistics
operator|.
name|setRepositoryId
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
name|repositoryStatistics
operator|.
name|setScanStartTime
argument_list|(
name|startTime
argument_list|)
expr_stmt|;
name|repositoryStatistics
operator|.
name|setScanEndTime
argument_list|(
name|endTime
argument_list|)
expr_stmt|;
name|repositoryStatistics
operator|.
name|setTotalFileCount
argument_list|(
name|totalFiles
argument_list|)
expr_stmt|;
name|repositoryStatistics
operator|.
name|setNewFileCount
argument_list|(
name|newFiles
argument_list|)
expr_stmt|;
comment|// TODO
comment|// In the future, instead of being tied to a scan we might want to record information in the fly based on
comment|// events that are occurring. Even without these totals we could query much of the information on demand based
comment|// on information from the metadata content repository. In the mean time, we lock information in at scan time.
comment|// Note that if new types are later discoverable due to a code change or new plugin, historical stats will not
comment|// be updated and the repository will need to be rescanned.
name|long
name|startGather
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|metadataRepository
operator|instanceof
name|RepositoryStatisticsProvider
condition|)
block|{
operator|(
operator|(
name|RepositoryStatisticsProvider
operator|)
name|metadataRepository
operator|)
operator|.
name|populateStatistics
argument_list|(
name|session
argument_list|,
name|metadataRepository
argument_list|,
name|repositoryId
argument_list|,
name|repositoryStatistics
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|walkingProvider
operator|.
name|populateStatistics
argument_list|(
name|session
argument_list|,
name|metadataRepository
argument_list|,
name|repositoryId
argument_list|,
name|repositoryStatistics
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Gathering statistics executed in {} ms"
argument_list|,
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|startGather
operator|)
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|addMetadataFacet
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|repositoryStatistics
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteStatistics
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
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
specifier|final
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|( )
decl_stmt|;
name|metadataRepository
operator|.
name|removeMetadataFacets
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|DefaultRepositoryStatistics
operator|.
name|FACET_ID
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|getStatisticsInRange
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
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
specifier|final
name|MetadataRepository
name|metadataRepository
init|=
name|session
operator|.
name|getRepository
argument_list|( )
decl_stmt|;
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|metadataRepository
operator|.
name|getMetadataFacets
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|DefaultRepositoryStatistics
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|,
name|Collections
operator|.
name|reverseOrder
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|list
control|)
block|{
try|try
block|{
name|Date
name|date
init|=
name|createNameFormat
argument_list|()
operator|.
name|parse
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|startTime
operator|==
literal|null
operator|||
operator|!
name|date
operator|.
name|before
argument_list|(
name|startTime
argument_list|)
operator|)
operator|&&
operator|(
name|endTime
operator|==
literal|null
operator|||
operator|!
name|date
operator|.
name|after
argument_list|(
name|endTime
argument_list|)
operator|)
condition|)
block|{
name|RepositoryStatistics
name|stats
init|=
operator|(
name|RepositoryStatistics
operator|)
name|metadataRepository
operator|.
name|getMetadataFacet
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|DefaultRepositoryStatistics
operator|.
name|FACET_ID
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|stats
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid scan result found in the metadata repository: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// continue and ignore this one
block|}
block|}
return|return
name|results
return|;
block|}
block|}
specifier|private
specifier|static
name|SimpleDateFormat
name|createNameFormat
parameter_list|()
block|{
name|SimpleDateFormat
name|fmt
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|DefaultRepositoryStatistics
operator|.
name|SCAN_TIMESTAMP_FORMAT
argument_list|)
decl_stmt|;
name|fmt
operator|.
name|setTimeZone
argument_list|(
name|UTC_TIME_ZONE
argument_list|)
expr_stmt|;
return|return
name|fmt
return|;
block|}
specifier|public
name|RepositorySessionFactory
name|getRepositorySessionFactory
parameter_list|( )
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
block|}
end_class

end_unit

