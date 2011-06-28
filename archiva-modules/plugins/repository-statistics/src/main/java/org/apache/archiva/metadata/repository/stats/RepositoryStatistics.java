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
name|MetadataFacet
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

begin_class
specifier|public
class|class
name|RepositoryStatistics
implements|implements
name|MetadataFacet
block|{
specifier|private
name|Date
name|scanEndTime
decl_stmt|;
specifier|private
name|Date
name|scanStartTime
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
specifier|public
specifier|static
name|String
name|FACET_ID
init|=
literal|"org.apache.archiva.metadata.repository.stats"
decl_stmt|;
specifier|static
specifier|final
name|String
name|SCAN_TIMESTAMP_FORMAT
init|=
literal|"yyyy/MM/dd/HHmmss.SSS"
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
name|ZeroForNullHashMap
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
argument_list|()
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
specifier|public
name|Date
name|getScanEndTime
parameter_list|()
block|{
return|return
name|scanEndTime
return|;
block|}
specifier|public
name|void
name|setScanEndTime
parameter_list|(
name|Date
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
specifier|public
name|Date
name|getScanStartTime
parameter_list|()
block|{
return|return
name|scanStartTime
return|;
block|}
specifier|public
name|void
name|setScanStartTime
parameter_list|(
name|Date
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
specifier|public
name|long
name|getTotalArtifactCount
parameter_list|()
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
specifier|public
name|long
name|getTotalArtifactFileSize
parameter_list|()
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
specifier|public
name|long
name|getTotalFileCount
parameter_list|()
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
specifier|public
name|long
name|getTotalGroupCount
parameter_list|()
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
specifier|public
name|long
name|getTotalProjectCount
parameter_list|()
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
specifier|public
name|long
name|getNewFileCount
parameter_list|()
block|{
return|return
name|newFileCount
return|;
block|}
specifier|public
name|long
name|getDuration
parameter_list|()
block|{
return|return
name|scanEndTime
operator|.
name|getTime
argument_list|()
operator|-
name|scanStartTime
operator|.
name|getTime
argument_list|()
return|;
block|}
specifier|public
name|String
name|getFacetId
parameter_list|()
block|{
return|return
name|FACET_ID
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|createNameFormat
argument_list|()
operator|.
name|format
argument_list|(
name|scanStartTime
argument_list|)
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toProperties
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"scanEndTime"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|scanEndTime
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"scanStartTime"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|scanStartTime
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"totalArtifactCount"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|totalArtifactCount
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"totalArtifactFileSize"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|totalArtifactFileSize
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"totalFileCount"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|totalFileCount
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"totalGroupCount"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|totalGroupCount
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"totalProjectCount"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|totalProjectCount
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"newFileCount"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|newFileCount
argument_list|)
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
name|Long
argument_list|>
name|entry
range|:
name|totalCountForType
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"count-"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
specifier|public
name|void
name|fromProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|scanEndTime
operator|=
operator|new
name|Date
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"scanEndTime"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|scanStartTime
operator|=
operator|new
name|Date
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"scanStartTime"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|totalArtifactCount
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"totalArtifactCount"
argument_list|)
argument_list|)
expr_stmt|;
name|totalArtifactFileSize
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"totalArtifactFileSize"
argument_list|)
argument_list|)
expr_stmt|;
name|totalFileCount
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"totalFileCount"
argument_list|)
argument_list|)
expr_stmt|;
name|totalGroupCount
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"totalGroupCount"
argument_list|)
argument_list|)
expr_stmt|;
name|totalProjectCount
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"totalProjectCount"
argument_list|)
argument_list|)
expr_stmt|;
name|newFileCount
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"newFileCount"
argument_list|)
argument_list|)
expr_stmt|;
name|totalCountForType
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"count-"
argument_list|)
condition|)
block|{
name|totalCountForType
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|newFileCount
operator|!=
name|that
operator|.
name|newFileCount
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|totalArtifactCount
operator|!=
name|that
operator|.
name|totalArtifactCount
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|totalArtifactFileSize
operator|!=
name|that
operator|.
name|totalArtifactFileSize
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|totalFileCount
operator|!=
name|that
operator|.
name|totalFileCount
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|totalGroupCount
operator|!=
name|that
operator|.
name|totalGroupCount
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|totalProjectCount
operator|!=
name|that
operator|.
name|totalProjectCount
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|scanEndTime
operator|.
name|equals
argument_list|(
name|that
operator|.
name|scanEndTime
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|scanStartTime
operator|.
name|equals
argument_list|(
name|that
operator|.
name|scanStartTime
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
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
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|scanEndTime
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|scanStartTime
operator|.
name|hashCode
argument_list|()
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
argument_list|()
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
parameter_list|()
block|{
return|return
literal|"RepositoryStatistics{"
operator|+
literal|"scanEndTime="
operator|+
name|scanEndTime
operator|+
literal|", scanStartTime="
operator|+
name|scanStartTime
operator|+
literal|", totalArtifactCount="
operator|+
name|totalArtifactCount
operator|+
literal|", totalArtifactFileSize="
operator|+
name|totalArtifactFileSize
operator|+
literal|", totalFileCount="
operator|+
name|totalFileCount
operator|+
literal|", totalGroupCount="
operator|+
name|totalGroupCount
operator|+
literal|", totalProjectCount="
operator|+
name|totalProjectCount
operator|+
literal|", newFileCount="
operator|+
name|newFileCount
operator|+
literal|", totalCountForType="
operator|+
name|totalCountForType
operator|+
literal|'}'
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|getTotalCountForType
parameter_list|()
block|{
return|return
name|totalCountForType
return|;
block|}
specifier|public
name|long
name|getTotalCountForType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
name|totalCountForType
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTotalCountForType
parameter_list|(
name|String
name|type
parameter_list|,
name|long
name|count
parameter_list|)
block|{
name|totalCountForType
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|ZeroForNullHashMap
parameter_list|<
name|K
parameter_list|,
name|V
extends|extends
name|Long
parameter_list|>
extends|extends
name|HashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|V
name|value
init|=
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
else|:
operator|(
name|V
operator|)
name|Long
operator|.
name|valueOf
argument_list|(
literal|0L
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

