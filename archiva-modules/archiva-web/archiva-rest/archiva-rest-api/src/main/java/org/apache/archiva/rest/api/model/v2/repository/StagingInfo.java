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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
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
literal|"StagingInfo"
argument_list|,
name|description
operator|=
literal|"Information about staging feature of a repository"
argument_list|)
specifier|public
class|class
name|StagingInfo
block|{
specifier|private
name|String
name|stagingRepository
decl_stmt|;
specifier|private
name|boolean
name|stageRepoNeeded
decl_stmt|;
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"staging_repo"
argument_list|,
name|description
operator|=
literal|"The repository id of the staging repository"
argument_list|)
specifier|public
name|String
name|getStagingRepository
parameter_list|( )
block|{
return|return
name|stagingRepository
return|;
block|}
specifier|public
name|void
name|setStagingRepository
parameter_list|(
name|String
name|stagingRepository
parameter_list|)
block|{
name|this
operator|.
name|stagingRepository
operator|=
name|stagingRepository
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"stage_repo_needed"
argument_list|,
name|description
operator|=
literal|"True, if this repository needs a staging repository"
argument_list|)
specifier|public
name|boolean
name|isStageRepoNeeded
parameter_list|( )
block|{
return|return
name|stageRepoNeeded
return|;
block|}
specifier|public
name|void
name|setStageRepoNeeded
parameter_list|(
name|boolean
name|stageRepoNeeded
parameter_list|)
block|{
name|this
operator|.
name|stageRepoNeeded
operator|=
name|stageRepoNeeded
expr_stmt|;
block|}
block|}
end_class

end_unit

