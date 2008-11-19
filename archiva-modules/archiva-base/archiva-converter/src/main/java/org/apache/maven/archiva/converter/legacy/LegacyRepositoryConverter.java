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
name|converter
operator|.
name|legacy
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
name|archiva
operator|.
name|converter
operator|.
name|RepositoryConversionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
comment|/**  * Convert an entire repository.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|LegacyRepositoryConverter
block|{
name|String
name|ROLE
init|=
name|LegacyRepositoryConverter
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Convert a legacy repository to a modern repository. This means a Maven 1.x repository      * using v3 POMs to a Maven 2.x repository using v4.0.0 POMs.      *      * @param legacyRepositoryDirectory the directory of the legacy repository.       * @param destinationRepositoryDirectory the directory of the modern repository.      * @param fileExclusionPatterns the list of patterns to exclude from the conversion.      * @throws RepositoryConversionException       */
name|void
name|convertLegacyRepository
parameter_list|(
name|File
name|legacyRepositoryDirectory
parameter_list|,
name|File
name|destinationRepositoryDirectory
parameter_list|,
name|List
name|fileExclusionPatterns
parameter_list|)
throws|throws
name|RepositoryConversionException
function_decl|;
block|}
end_interface

end_unit

