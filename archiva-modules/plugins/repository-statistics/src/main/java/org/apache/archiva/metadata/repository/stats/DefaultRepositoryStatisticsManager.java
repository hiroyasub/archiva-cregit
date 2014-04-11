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
name|maven2
operator|.
name|MavenArtifactFacet
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
name|MetadataResolutionException
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
name|commons
operator|.
name|JcrUtils
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
name|Collection
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
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Node
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
name|javax
operator|.
name|jcr
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|query
operator|.
name|QueryManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|query
operator|.
name|QueryResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|query
operator|.
name|Row
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
annotation|@
name|Override
specifier|public
name|boolean
name|hasStatistics
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
name|metadataRepository
operator|.
name|hasMetadataFacet
argument_list|(
name|repositoryId
argument_list|,
name|RepositoryStatistics
operator|.
name|FACET_ID
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryStatistics
name|getLastStatistics
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
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
name|repositoryId
argument_list|,
name|RepositoryStatistics
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
specifier|private
name|void
name|walkRepository
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|RepositoryStatistics
name|stats
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
for|for
control|(
name|String
name|namespace
range|:
name|metadataRepository
operator|.
name|getNamespaces
argument_list|(
name|repositoryId
argument_list|,
name|ns
argument_list|)
control|)
block|{
name|walkRepository
argument_list|(
name|metadataRepository
argument_list|,
name|stats
argument_list|,
name|repositoryId
argument_list|,
name|ns
operator|+
literal|"."
operator|+
name|namespace
argument_list|)
expr_stmt|;
block|}
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
name|repositoryId
argument_list|,
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|projects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|stats
operator|.
name|setTotalGroupCount
argument_list|(
name|stats
operator|.
name|getTotalGroupCount
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalProjectCount
argument_list|(
name|stats
operator|.
name|getTotalProjectCount
argument_list|()
operator|+
name|projects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|project
range|:
name|projects
control|)
block|{
for|for
control|(
name|String
name|version
range|:
name|metadataRepository
operator|.
name|getProjectVersions
argument_list|(
name|repositoryId
argument_list|,
name|ns
argument_list|,
name|project
argument_list|)
control|)
block|{
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repositoryId
argument_list|,
name|ns
argument_list|,
name|project
argument_list|,
name|version
argument_list|)
control|)
block|{
name|stats
operator|.
name|setTotalArtifactCount
argument_list|(
name|stats
operator|.
name|getTotalArtifactCount
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalArtifactFileSize
argument_list|(
name|stats
operator|.
name|getTotalArtifactFileSize
argument_list|()
operator|+
name|artifact
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|MavenArtifactFacet
name|facet
init|=
operator|(
name|MavenArtifactFacet
operator|)
name|artifact
operator|.
name|getFacet
argument_list|(
name|MavenArtifactFacet
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|facet
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
name|facet
operator|.
name|getType
argument_list|()
decl_stmt|;
name|stats
operator|.
name|setTotalCountForType
argument_list|(
name|type
argument_list|,
name|stats
operator|.
name|getTotalCountForType
argument_list|(
name|type
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addStatisticsAfterScan
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
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
name|RepositoryStatistics
name|repositoryStatistics
init|=
operator|new
name|RepositoryStatistics
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
comment|// FIXME what about other implementations ?
if|if
condition|(
name|metadataRepository
operator|.
name|canObtainAccess
argument_list|(
name|Session
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// TODO: this is currently very raw and susceptible to changes in content structure. Should we instead
comment|//   depend directly on the plugin and interrogate the JCR repository's knowledge of the structure?
name|populateStatisticsFromJcr
argument_list|(
operator|(
name|Session
operator|)
name|metadataRepository
operator|.
name|obtainAccess
argument_list|(
name|Session
operator|.
name|class
argument_list|)
argument_list|,
name|repositoryId
argument_list|,
name|repositoryStatistics
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO:
comment|//   if the file repository is used more permanently, we may seek a more efficient mechanism - e.g. we could
comment|//   build an index, or store the aggregate information and update it on the fly. We can perhaps even walk
comment|//   but retrieve less information to speed it up. In the mean time, we walk the repository using the
comment|//   standard APIs
name|populateStatisticsFromRepositoryWalk
argument_list|(
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
name|repositoryId
argument_list|,
name|repositoryStatistics
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|populateStatisticsFromJcr
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|RepositoryStatistics
name|repositoryStatistics
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
comment|// TODO: these may be best as running totals, maintained by observations on the properties in JCR
try|try
block|{
name|QueryManager
name|queryManager
init|=
name|session
operator|.
name|getWorkspace
argument_list|()
operator|.
name|getQueryManager
argument_list|()
decl_stmt|;
comment|// TODO: JCR-SQL2 query will not complete on a large repo in Jackrabbit 2.2.0 - see JCR-2835
comment|//    Using the JCR-SQL2 variants gives
comment|//      "org.apache.lucene.search.BooleanQuery$TooManyClauses: maxClauseCount is set to 1024"
comment|//            String whereClause = "WHERE ISDESCENDANTNODE([/repositories/" + repositoryId + "/content])";
comment|//            Query query = queryManager.createQuery( "SELECT size FROM [archiva:artifact] " + whereClause,
comment|//                                                    Query.JCR_SQL2 );
name|String
name|whereClause
init|=
literal|"WHERE jcr:path LIKE '/repositories/"
operator|+
name|repositoryId
operator|+
literal|"/content/%'"
decl_stmt|;
name|Query
name|query
init|=
name|queryManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT size FROM archiva:artifact "
operator|+
name|whereClause
argument_list|,
name|Query
operator|.
name|SQL
argument_list|)
decl_stmt|;
name|QueryResult
name|queryResult
init|=
name|query
operator|.
name|execute
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|totalByType
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|long
name|totalSize
init|=
literal|0
decl_stmt|,
name|totalArtifacts
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|JcrUtils
operator|.
name|getRows
argument_list|(
name|queryResult
argument_list|)
control|)
block|{
name|Node
name|n
init|=
name|row
operator|.
name|getNode
argument_list|()
decl_stmt|;
name|totalSize
operator|+=
name|row
operator|.
name|getValue
argument_list|(
literal|"size"
argument_list|)
operator|.
name|getLong
argument_list|()
expr_stmt|;
name|String
name|type
decl_stmt|;
if|if
condition|(
name|n
operator|.
name|hasNode
argument_list|(
name|MavenArtifactFacet
operator|.
name|FACET_ID
argument_list|)
condition|)
block|{
name|Node
name|facetNode
init|=
name|n
operator|.
name|getNode
argument_list|(
name|MavenArtifactFacet
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
name|type
operator|=
name|facetNode
operator|.
name|getProperty
argument_list|(
literal|"type"
argument_list|)
operator|.
name|getString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
literal|"Other"
expr_stmt|;
block|}
name|Integer
name|prev
init|=
name|totalByType
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|totalByType
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|prev
operator|!=
literal|null
condition|?
name|prev
operator|+
literal|1
else|:
literal|1
argument_list|)
expr_stmt|;
name|totalArtifacts
operator|++
expr_stmt|;
block|}
name|repositoryStatistics
operator|.
name|setTotalArtifactCount
argument_list|(
name|totalArtifacts
argument_list|)
expr_stmt|;
name|repositoryStatistics
operator|.
name|setTotalArtifactFileSize
argument_list|(
name|totalSize
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|totalByType
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|repositoryStatistics
operator|.
name|setTotalCountForType
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
comment|// The query ordering is a trick to ensure that the size is correct, otherwise due to lazy init it will be -1
comment|//            query = queryManager.createQuery( "SELECT * FROM [archiva:project] " + whereClause, Query.JCR_SQL2 );
name|query
operator|=
name|queryManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT * FROM archiva:project "
operator|+
name|whereClause
operator|+
literal|" ORDER BY jcr:score"
argument_list|,
name|Query
operator|.
name|SQL
argument_list|)
expr_stmt|;
name|repositoryStatistics
operator|.
name|setTotalProjectCount
argument_list|(
name|query
operator|.
name|execute
argument_list|()
operator|.
name|getRows
argument_list|()
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
comment|//            query = queryManager.createQuery(
comment|//                "SELECT * FROM [archiva:namespace] " + whereClause + " AND namespace IS NOT NULL", Query.JCR_SQL2 );
name|query
operator|=
name|queryManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT * FROM archiva:namespace "
operator|+
name|whereClause
operator|+
literal|" AND namespace IS NOT NULL ORDER BY jcr:score"
argument_list|,
name|Query
operator|.
name|SQL
argument_list|)
expr_stmt|;
name|repositoryStatistics
operator|.
name|setTotalGroupCount
argument_list|(
name|query
operator|.
name|execute
argument_list|()
operator|.
name|getRows
argument_list|()
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MetadataRepositoryException
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
block|}
specifier|private
name|void
name|populateStatisticsFromRepositoryWalk
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|RepositoryStatistics
name|repositoryStatistics
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
try|try
block|{
for|for
control|(
name|String
name|ns
range|:
name|metadataRepository
operator|.
name|getRootNamespaces
argument_list|(
name|repositoryId
argument_list|)
control|)
block|{
name|walkRepository
argument_list|(
name|metadataRepository
argument_list|,
name|repositoryStatistics
argument_list|,
name|repositoryId
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MetadataResolutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MetadataRepositoryException
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteStatistics
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
name|metadataRepository
operator|.
name|removeMetadataFacets
argument_list|(
name|repositoryId
argument_list|,
name|RepositoryStatistics
operator|.
name|FACET_ID
argument_list|)
expr_stmt|;
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
name|MetadataRepository
name|metadataRepository
parameter_list|,
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
name|repositoryId
argument_list|,
name|RepositoryStatistics
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
name|repositoryId
argument_list|,
name|RepositoryStatistics
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
literal|"Invalid scan result found in the metadata repository: "
operator|+
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
name|RepositoryStatistics
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
block|}
end_class

end_unit

