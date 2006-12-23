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
name|layer
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
name|maven
operator|.
name|artifact
operator|.
name|Artifact
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * The transitive and metadata validation reports will need to query the repository for artifacts.  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryQueryLayer
block|{
name|String
name|ROLE
init|=
name|RepositoryQueryLayer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|containsArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
function_decl|;
name|List
name|getVersions
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|RepositoryQueryLayerException
function_decl|;
name|ArtifactRepository
name|getRepository
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

