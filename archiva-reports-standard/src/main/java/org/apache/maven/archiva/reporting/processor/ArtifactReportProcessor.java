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
name|reporting
operator|.
name|processor
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
name|model
operator|.
name|Model
import|;
end_import

begin_comment
comment|/**  * This interface will be called by the main system for each artifact as it is discovered. This is how each of the  * different types of reports are implemented.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArtifactReportProcessor
block|{
name|String
name|ROLE
init|=
name|ArtifactReportProcessor
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|void
name|processArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Model
name|model
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

