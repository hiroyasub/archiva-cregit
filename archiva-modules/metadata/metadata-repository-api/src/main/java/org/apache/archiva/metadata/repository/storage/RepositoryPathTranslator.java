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
name|storage
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
name|ArtifactMetadata
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
name|storage
operator|.
name|StorageAsset
import|;
end_import

begin_interface
specifier|public
interface|interface
name|RepositoryPathTranslator
block|{
name|char
name|PATH_SEPARATOR
init|=
literal|'/'
decl_stmt|;
name|String
name|toPath
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|String
name|filename
parameter_list|)
function_decl|;
name|String
name|toPath
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
function_decl|;
name|StorageAsset
name|toFile
parameter_list|(
name|StorageAsset
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|String
name|filename
parameter_list|)
function_decl|;
name|StorageAsset
name|toFile
parameter_list|(
name|StorageAsset
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
function_decl|;
name|StorageAsset
name|toFile
parameter_list|(
name|StorageAsset
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|)
function_decl|;
name|StorageAsset
name|toFile
parameter_list|(
name|StorageAsset
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
function_decl|;
name|ArtifactMetadata
name|getArtifactForPath
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|relativePath
parameter_list|)
function_decl|;
name|ArtifactMetadata
name|getArtifactFromId
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|String
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

