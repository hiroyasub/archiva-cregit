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
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|discoverer
operator|.
name|DiscovererException
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
comment|/**  * @author Jason van Zyl  */
end_comment

begin_interface
specifier|public
interface|interface
name|Archiva
block|{
comment|/**      * Role of the Repository Manager      */
name|String
name|ROLE
init|=
name|Archiva
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Convert a legacy repository to a modern repository. This means a Maven 1.x repository      * using v3 POMs to a Maven 2.x repository using v4.0.0 POMs.      *      * @param legacyRepositoryDirectory      * @param repositoryDirectory      * @throws RepositoryConversionException      */
name|void
name|convertLegacyRepository
parameter_list|(
name|File
name|legacyRepositoryDirectory
parameter_list|,
name|File
name|repositoryDirectory
parameter_list|,
name|List
name|blacklistedPatterns
parameter_list|,
name|boolean
name|includeSnapshots
parameter_list|)
throws|throws
name|RepositoryConversionException
throws|,
name|DiscovererException
function_decl|;
block|}
end_interface

end_unit

