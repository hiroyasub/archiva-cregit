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
name|database
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * ArchivaDAO - The interface for all content within the database.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArchivaDAO
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|ArchivaDAO
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Perform a simple query against the database.      *       * @param constraint the constraint to use.      * @return the List of results.      */
name|List
name|query
parameter_list|(
name|SimpleConstraint
name|constraint
parameter_list|)
function_decl|;
comment|/**      * Perform a simple save of a peristable object to the database.      *       * @param o the serializable (persistable) object to save.      * @return the post-serialized object.      */
name|Object
name|save
parameter_list|(
name|Serializable
name|obj
parameter_list|)
function_decl|;
name|ArtifactDAO
name|getArtifactDAO
parameter_list|()
function_decl|;
name|ProjectModelDAO
name|getProjectModelDAO
parameter_list|()
function_decl|;
name|RepositoryDAO
name|getRepositoryDAO
parameter_list|()
function_decl|;
name|RepositoryProblemDAO
name|getRepositoryProblemDAO
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

