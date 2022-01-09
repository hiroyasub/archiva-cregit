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
name|api
operator|.
name|v2
operator|.
name|model
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
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Schema
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
name|scanner
operator|.
name|RepositoryScanStatistics
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|OffsetDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
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
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  * @since 3.0  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"repositoryStatistics"
argument_list|)
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"RepositoryStatistics"
argument_list|,
name|description
operator|=
literal|"Statistics data"
argument_list|)
specifier|public
class|class
name|RepositoryStatistics
implements|implements
name|Serializable
implements|,
name|RestModel
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7943367882738452531L
decl_stmt|;
specifier|private
name|OffsetDateTime
name|scanEndTime
decl_stmt|;
specifier|private
name|OffsetDateTime
name|scanStartTime
decl_stmt|;
specifier|private
name|long
name|scanDurationMs
decl_stmt|;
specifier|private
name|long
name|totalArtifactCount
decl_stmt|;
specifier|private
name|long
name|totalArtifactFileSize
decl_stmt|;
specifier|private
name|long
name|totalFileCount
decl_stmt|;
specifier|private
name|long
name|totalGroupCount
decl_stmt|;
specifier|private
name|long
name|totalProjectCount
decl_stmt|;
specifier|private
name|long
name|newFileCount
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|totalCountForType
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|customValues
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|public
name|RepositoryStatistics
parameter_list|( )
block|{
block|}
specifier|public
specifier|static
name|RepositoryStatistics
name|of
parameter_list|(
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
name|modelStats
parameter_list|)
block|{
name|RepositoryStatistics
name|newStats
init|=
operator|new
name|RepositoryStatistics
argument_list|( )
decl_stmt|;
name|newStats
operator|.
name|setScanStartTime
argument_list|(
name|modelStats
operator|.
name|getScanStartTime
argument_list|()
operator|.
name|toInstant
argument_list|()
operator|.
name|atOffset
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setScanEndTime
argument_list|(
name|modelStats
operator|.
name|getScanEndTime
argument_list|( )
operator|.
name|toInstant
argument_list|( )
operator|.
name|atOffset
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setNewFileCount
argument_list|(
name|modelStats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setScanDurationMs
argument_list|(
name|Duration
operator|.
name|between
argument_list|(
name|newStats
operator|.
name|scanStartTime
argument_list|,
name|newStats
operator|.
name|scanEndTime
argument_list|)
operator|.
name|toMillis
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalArtifactCount
argument_list|(
name|modelStats
operator|.
name|getTotalArtifactCount
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalArtifactFileSize
argument_list|(
name|modelStats
operator|.
name|getTotalArtifactFileSize
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalCountForType
argument_list|(
name|modelStats
operator|.
name|getTotalCountForType
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalFileCount
argument_list|(
name|modelStats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalGroupCount
argument_list|(
name|modelStats
operator|.
name|getTotalGroupCount
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalProjectCount
argument_list|(
name|modelStats
operator|.
name|getTotalProjectCount
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|modelStats
operator|.
name|getAvailableCustomValues
argument_list|()
control|)
block|{
name|newStats
operator|.
name|addCustomValue
argument_list|(
name|key
argument_list|,
name|modelStats
operator|.
name|getCustomValue
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|newStats
return|;
block|}
specifier|public
specifier|static
name|RepositoryStatistics
name|of
parameter_list|(
name|RepositoryScanStatistics
name|scanStatistics
parameter_list|)
block|{
name|RepositoryStatistics
name|newStats
init|=
operator|new
name|RepositoryStatistics
argument_list|( )
decl_stmt|;
name|newStats
operator|.
name|setScanStartTime
argument_list|(
name|scanStatistics
operator|.
name|getWhenGathered
argument_list|()
operator|.
name|toInstant
argument_list|()
operator|.
name|atOffset
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setScanEndTime
argument_list|(
name|OffsetDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setNewFileCount
argument_list|(
name|scanStatistics
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setScanDurationMs
argument_list|(
name|Duration
operator|.
name|between
argument_list|(
name|newStats
operator|.
name|scanStartTime
argument_list|,
name|newStats
operator|.
name|scanEndTime
argument_list|)
operator|.
name|toMillis
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalArtifactCount
argument_list|(
name|scanStatistics
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalArtifactFileSize
argument_list|(
name|scanStatistics
operator|.
name|getTotalSize
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalFileCount
argument_list|(
name|scanStatistics
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalGroupCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|newStats
operator|.
name|setTotalProjectCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|newStats
return|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"scan_end_time"
argument_list|,
name|description
operator|=
literal|"Date and time when the last scan finished"
argument_list|)
specifier|public
name|OffsetDateTime
name|getScanEndTime
parameter_list|( )
block|{
return|return
name|scanEndTime
return|;
block|}
specifier|public
name|void
name|setScanEndTime
parameter_list|(
name|OffsetDateTime
name|scanEndTime
parameter_list|)
block|{
name|this
operator|.
name|scanEndTime
operator|=
name|scanEndTime
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"scan_start_time"
argument_list|,
name|description
operator|=
literal|"Date and time when the last scan started"
argument_list|)
specifier|public
name|OffsetDateTime
name|getScanStartTime
parameter_list|( )
block|{
return|return
name|scanStartTime
return|;
block|}
specifier|public
name|void
name|setScanStartTime
parameter_list|(
name|OffsetDateTime
name|scanStartTime
parameter_list|)
block|{
name|this
operator|.
name|scanStartTime
operator|=
name|scanStartTime
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"total_artifact_count"
argument_list|,
name|description
operator|=
literal|"The number of artifacts scanned"
argument_list|)
specifier|public
name|long
name|getTotalArtifactCount
parameter_list|( )
block|{
return|return
name|totalArtifactCount
return|;
block|}
specifier|public
name|void
name|setTotalArtifactCount
parameter_list|(
name|long
name|totalArtifactCount
parameter_list|)
block|{
name|this
operator|.
name|totalArtifactCount
operator|=
name|totalArtifactCount
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"total_artifact_file_size"
argument_list|,
name|description
operator|=
literal|"The cumulative size of all files scanned"
argument_list|)
specifier|public
name|long
name|getTotalArtifactFileSize
parameter_list|( )
block|{
return|return
name|totalArtifactFileSize
return|;
block|}
specifier|public
name|void
name|setTotalArtifactFileSize
parameter_list|(
name|long
name|totalArtifactFileSize
parameter_list|)
block|{
name|this
operator|.
name|totalArtifactFileSize
operator|=
name|totalArtifactFileSize
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"total_file_count"
argument_list|,
name|description
operator|=
literal|"The total number of files scanned"
argument_list|)
specifier|public
name|long
name|getTotalFileCount
parameter_list|( )
block|{
return|return
name|totalFileCount
return|;
block|}
specifier|public
name|void
name|setTotalFileCount
parameter_list|(
name|long
name|totalFileCount
parameter_list|)
block|{
name|this
operator|.
name|totalFileCount
operator|=
name|totalFileCount
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"total_group_count"
argument_list|,
name|description
operator|=
literal|"The number of groups scanned"
argument_list|)
specifier|public
name|long
name|getTotalGroupCount
parameter_list|( )
block|{
return|return
name|totalGroupCount
return|;
block|}
specifier|public
name|void
name|setTotalGroupCount
parameter_list|(
name|long
name|totalGroupCount
parameter_list|)
block|{
name|this
operator|.
name|totalGroupCount
operator|=
name|totalGroupCount
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"total_project_count"
argument_list|,
name|description
operator|=
literal|"The number of projects scanned"
argument_list|)
specifier|public
name|long
name|getTotalProjectCount
parameter_list|( )
block|{
return|return
name|totalProjectCount
return|;
block|}
specifier|public
name|void
name|setTotalProjectCount
parameter_list|(
name|long
name|totalProjectCount
parameter_list|)
block|{
name|this
operator|.
name|totalProjectCount
operator|=
name|totalProjectCount
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"new_file_count"
argument_list|,
name|description
operator|=
literal|"Number of files registered as new"
argument_list|)
specifier|public
name|long
name|getNewFileCount
parameter_list|( )
block|{
return|return
name|newFileCount
return|;
block|}
specifier|public
name|void
name|setNewFileCount
parameter_list|(
name|long
name|newFileCount
parameter_list|)
block|{
name|this
operator|.
name|newFileCount
operator|=
name|newFileCount
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"scan_duration_ms"
argument_list|,
name|description
operator|=
literal|"The duration of the last scan in ms"
argument_list|)
specifier|public
name|long
name|getScanDurationMs
parameter_list|( )
block|{
return|return
name|scanDurationMs
return|;
block|}
specifier|public
name|void
name|setScanDurationMs
parameter_list|(
name|long
name|scanDurationMs
parameter_list|)
block|{
name|this
operator|.
name|scanDurationMs
operator|=
name|scanDurationMs
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"total_count_for_type"
argument_list|,
name|description
operator|=
literal|"File counts partitioned by file types"
argument_list|)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|getTotalCountForType
parameter_list|( )
block|{
return|return
name|totalCountForType
return|;
block|}
specifier|public
name|void
name|setTotalCountForType
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|totalCountForType
parameter_list|)
block|{
name|this
operator|.
name|totalCountForType
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|totalCountForType
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addTotalCountForType
parameter_list|(
name|String
name|type
parameter_list|,
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|totalCountForType
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"custom_values"
argument_list|,
name|description
operator|=
literal|"Custom statistic values"
argument_list|)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|getCustomValues
parameter_list|( )
block|{
return|return
name|customValues
return|;
block|}
specifier|public
name|void
name|setCustomValues
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|customValues
parameter_list|)
block|{
name|this
operator|.
name|customValues
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|customValues
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addCustomValue
parameter_list|(
name|String
name|type
parameter_list|,
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|customValues
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|( )
operator|!=
name|o
operator|.
name|getClass
argument_list|( )
condition|)
return|return
literal|false
return|;
name|RepositoryStatistics
name|that
init|=
operator|(
name|RepositoryStatistics
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|scanDurationMs
operator|!=
name|that
operator|.
name|scanDurationMs
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|totalArtifactCount
operator|!=
name|that
operator|.
name|totalArtifactCount
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|totalArtifactFileSize
operator|!=
name|that
operator|.
name|totalArtifactFileSize
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|totalFileCount
operator|!=
name|that
operator|.
name|totalFileCount
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|totalGroupCount
operator|!=
name|that
operator|.
name|totalGroupCount
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|totalProjectCount
operator|!=
name|that
operator|.
name|totalProjectCount
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|newFileCount
operator|!=
name|that
operator|.
name|newFileCount
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|scanEndTime
operator|!=
literal|null
condition|?
operator|!
name|scanEndTime
operator|.
name|equals
argument_list|(
name|that
operator|.
name|scanEndTime
argument_list|)
else|:
name|that
operator|.
name|scanEndTime
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|scanStartTime
operator|!=
literal|null
condition|?
operator|!
name|scanStartTime
operator|.
name|equals
argument_list|(
name|that
operator|.
name|scanStartTime
argument_list|)
else|:
name|that
operator|.
name|scanStartTime
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|totalCountForType
operator|.
name|equals
argument_list|(
name|that
operator|.
name|totalCountForType
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
name|customValues
operator|.
name|equals
argument_list|(
name|that
operator|.
name|customValues
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
name|int
name|result
init|=
name|scanEndTime
operator|!=
literal|null
condition|?
name|scanEndTime
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|scanStartTime
operator|!=
literal|null
condition|?
name|scanStartTime
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|int
operator|)
operator|(
name|scanDurationMs
operator|^
operator|(
name|scanDurationMs
operator|>>>
literal|32
operator|)
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|int
operator|)
operator|(
name|totalArtifactCount
operator|^
operator|(
name|totalArtifactCount
operator|>>>
literal|32
operator|)
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|int
operator|)
operator|(
name|totalArtifactFileSize
operator|^
operator|(
name|totalArtifactFileSize
operator|>>>
literal|32
operator|)
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|int
operator|)
operator|(
name|totalFileCount
operator|^
operator|(
name|totalFileCount
operator|>>>
literal|32
operator|)
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|int
operator|)
operator|(
name|totalGroupCount
operator|^
operator|(
name|totalGroupCount
operator|>>>
literal|32
operator|)
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|int
operator|)
operator|(
name|totalProjectCount
operator|^
operator|(
name|totalProjectCount
operator|>>>
literal|32
operator|)
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|int
operator|)
operator|(
name|newFileCount
operator|^
operator|(
name|newFileCount
operator|>>>
literal|32
operator|)
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|totalCountForType
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|customValues
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|( )
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"RepositoryStatistics{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"scanEndTime="
argument_list|)
operator|.
name|append
argument_list|(
name|scanEndTime
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", scanStartTime="
argument_list|)
operator|.
name|append
argument_list|(
name|scanStartTime
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", scanDurationMs="
argument_list|)
operator|.
name|append
argument_list|(
name|scanDurationMs
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalArtifactCount="
argument_list|)
operator|.
name|append
argument_list|(
name|totalArtifactCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalArtifactFileSize="
argument_list|)
operator|.
name|append
argument_list|(
name|totalArtifactFileSize
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalFileCount="
argument_list|)
operator|.
name|append
argument_list|(
name|totalFileCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalGroupCount="
argument_list|)
operator|.
name|append
argument_list|(
name|totalGroupCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalProjectCount="
argument_list|)
operator|.
name|append
argument_list|(
name|totalProjectCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", newFileCount="
argument_list|)
operator|.
name|append
argument_list|(
name|newFileCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|( )
return|;
block|}
block|}
end_class

end_unit

