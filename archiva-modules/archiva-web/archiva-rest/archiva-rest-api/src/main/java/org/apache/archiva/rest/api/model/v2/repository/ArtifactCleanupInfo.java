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
name|model
operator|.
name|v2
operator|.
name|repository
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
name|java
operator|.
name|time
operator|.
name|Period
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"ArtifactCleanupInfo"
argument_list|,
name|description
operator|=
literal|"Information for artifact cleanup feature of repository"
argument_list|)
specifier|public
class|class
name|ArtifactCleanupInfo
block|{
name|boolean
name|deleteReleasedSnapshots
decl_stmt|;
name|Period
name|retentionPeriod
decl_stmt|;
name|int
name|retentionCount
decl_stmt|;
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"delete_released_snapshots"
argument_list|,
name|description
operator|=
literal|"True, if snapshots are deleted after a release was published"
argument_list|)
specifier|public
name|boolean
name|isDeleteReleasedSnapshots
parameter_list|( )
block|{
return|return
name|deleteReleasedSnapshots
return|;
block|}
specifier|public
name|void
name|setDeleteReleasedSnapshots
parameter_list|(
name|boolean
name|deleteReleasedSnapshots
parameter_list|)
block|{
name|this
operator|.
name|deleteReleasedSnapshots
operator|=
name|deleteReleasedSnapshots
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"retention_period"
argument_list|,
name|description
operator|=
literal|"Time, after that snapshot artifacts are marked for deletion"
argument_list|)
specifier|public
name|Period
name|getRetentionPeriod
parameter_list|( )
block|{
return|return
name|retentionPeriod
return|;
block|}
specifier|public
name|void
name|setRetentionPeriod
parameter_list|(
name|Period
name|retentionPeriod
parameter_list|)
block|{
name|this
operator|.
name|retentionPeriod
operator|=
name|retentionPeriod
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"retention_count"
argument_list|,
name|description
operator|=
literal|"Maximum number of snapshot artifacts to keep"
argument_list|)
specifier|public
name|int
name|getRetentionCount
parameter_list|( )
block|{
return|return
name|retentionCount
return|;
block|}
specifier|public
name|void
name|setRetentionCount
parameter_list|(
name|int
name|retentionCount
parameter_list|)
block|{
name|this
operator|.
name|retentionCount
operator|=
name|retentionCount
expr_stmt|;
block|}
block|}
end_class

end_unit

