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
name|indexer
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * Obtain an index instance.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryArtifactIndexFactory
block|{
comment|/**      * Plexus role.      */
name|String
name|ROLE
init|=
name|RepositoryArtifactIndexFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Method to create an instance of the standard index.      *      * @param indexPath the path where the index will be created/updated      * @return the index instance      */
name|RepositoryArtifactIndex
name|createStandardIndex
parameter_list|(
name|File
name|indexPath
parameter_list|)
function_decl|;
comment|/**      * Method to create an instance of the minimal index.      *      * @param indexPath the path where the index will be created/updated      * @return the index instance      */
name|RepositoryArtifactIndex
name|createMinimalIndex
parameter_list|(
name|File
name|indexPath
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

