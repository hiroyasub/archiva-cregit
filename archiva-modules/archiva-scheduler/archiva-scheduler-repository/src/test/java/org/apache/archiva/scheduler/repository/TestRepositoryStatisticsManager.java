begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
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
name|stats
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
name|RepositoryStatisticsManager
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

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryStatisticsManager#test"
argument_list|)
specifier|public
class|class
name|TestRepositoryStatisticsManager
implements|implements
name|RepositoryStatisticsManager
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
argument_list|>
name|repoStats
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
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
operator|!
name|repoStats
operator|.
name|isEmpty
argument_list|()
return|;
block|}
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
block|{
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|repositoryStatisticsList
init|=
name|getStatsList
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
return|return
operator|!
name|repositoryStatisticsList
operator|.
name|isEmpty
argument_list|()
condition|?
name|repositoryStatisticsList
operator|.
name|get
argument_list|(
name|repositoryStatisticsList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
else|:
literal|null
return|;
block|}
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
block|{
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|stats
init|=
name|getStatsList
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
name|RepositoryStatistics
name|repositoryStatistics
init|=
operator|new
name|RepositoryStatistics
argument_list|()
decl_stmt|;
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
name|setNewFileCount
argument_list|(
name|newFiles
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
name|setRepositoryId
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
name|stats
operator|.
name|add
argument_list|(
name|repositoryStatistics
argument_list|)
expr_stmt|;
block|}
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
block|{
name|repoStats
operator|.
name|remove
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
block|}
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
name|startDate
parameter_list|,
name|Date
name|endDate
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|private
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|getStatsList
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|List
argument_list|<
name|RepositoryStatistics
argument_list|>
name|stats
init|=
name|repoStats
operator|.
name|get
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
if|if
condition|(
name|stats
operator|==
literal|null
condition|)
block|{
name|stats
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|repoStats
operator|.
name|put
argument_list|(
name|repositoryId
argument_list|,
name|stats
argument_list|)
expr_stmt|;
block|}
return|return
name|stats
return|;
block|}
block|}
end_class

end_unit

