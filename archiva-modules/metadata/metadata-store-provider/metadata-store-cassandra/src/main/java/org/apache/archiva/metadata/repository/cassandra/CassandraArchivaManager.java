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
name|cassandra
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|oss
operator|.
name|driver
operator|.
name|api
operator|.
name|core
operator|.
name|CqlSession
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|oss
operator|.
name|driver
operator|.
name|api
operator|.
name|core
operator|.
name|CqlSessionBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|oss
operator|.
name|driver
operator|.
name|api
operator|.
name|core
operator|.
name|config
operator|.
name|DriverConfigLoader
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 2.0.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|CassandraArchivaManager
block|{
name|String
name|DEFAULT_PRIMARY_KEY
init|=
literal|"key"
decl_stmt|;
name|void
name|start
parameter_list|()
function_decl|;
name|void
name|shutdown
parameter_list|()
function_decl|;
name|boolean
name|started
parameter_list|()
function_decl|;
name|String
name|getRepositoryFamilyName
parameter_list|()
function_decl|;
name|String
name|getNamespaceFamilyName
parameter_list|()
function_decl|;
name|String
name|getProjectFamilyName
parameter_list|()
function_decl|;
name|String
name|getProjectVersionMetadataFamilyName
parameter_list|()
function_decl|;
name|String
index|[]
name|getProjectVersionMetadataColumns
parameter_list|()
function_decl|;
name|String
name|getArtifactMetadataFamilyName
parameter_list|()
function_decl|;
name|String
name|getMetadataFacetFamilyName
parameter_list|()
function_decl|;
name|String
name|getMailingListFamilyName
parameter_list|()
function_decl|;
name|String
name|getLicenseFamilyName
parameter_list|()
function_decl|;
name|String
name|getDependencyFamilyName
parameter_list|()
function_decl|;
name|String
name|getChecksumFamilyName
parameter_list|()
function_decl|;
name|DriverConfigLoader
name|getConfigLoader
parameter_list|()
function_decl|;
name|CqlSessionBuilder
name|getSessionBuilder
parameter_list|( )
function_decl|;
name|CqlSession
name|getSession
parameter_list|()
function_decl|;
name|String
name|getKeyspaceName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

