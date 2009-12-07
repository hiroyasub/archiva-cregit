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
name|valueOf
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
name|valueOf
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
name|valueOf
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
name|valueOf
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
name|valueOf
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
name|valueOf
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
name|valueOf
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
name|valueOf
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"newFileCount"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

