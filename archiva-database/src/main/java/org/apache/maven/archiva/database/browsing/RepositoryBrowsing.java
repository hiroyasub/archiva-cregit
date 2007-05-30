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
operator|.
name|browsing
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|ObjectNotFoundException
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
name|model
operator|.
name|ArchivaProjectModel
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
comment|/**  * Repository Browsing component   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryBrowsing
block|{
comment|/**      * Get the {@link BrowsingResults} for the root of the repository.      *       * @return the root browsing results.      */
specifier|public
name|BrowsingResults
name|getRoot
parameter_list|()
function_decl|;
comment|/**      * Get the {@link BrowsingResults} for the selected groupId.      *       * @param groupId the groupId to select.      * @return the {@link BrowsingResults} for the specified groupId.      */
specifier|public
name|BrowsingResults
name|selectGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
function_decl|;
comment|/**      * Get the {@link BrowsingResults} for the selected groupId& artifactId.      *       * @param groupId the groupId selected      * @param artifactId the artifactId selected      * @return the {@link BrowsingResults} for the specified groupId / artifactId combo.      */
specifier|public
name|BrowsingResults
name|selectArtifactId
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
function_decl|;
comment|/**      * Get the {@link ArchivaProjectModel} for the selected groupId / artifactId / version combo.      *       * @param groupId the groupId selected      * @param artifactId the artifactId selected      * @param version the version selected      * @return the {@link ArchivaProjectModel} for the selected groupId / artifactId / version combo.      * @throws ObjectNotFoundException if the artifact object or project object isn't found in the database.      * @throws ArchivaDatabaseException if there is a fundamental database error.      */
specifier|public
name|ArchivaProjectModel
name|selectVersion
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
function_decl|;
comment|/**      * Get the {@link List} of {@link ArchivaProjectModel} that are used by the provided      * groupId, artifactId, and version specified.      *       * @param groupId the groupId selected      * @param artifactId the artifactId selected      * @param version the version selected      * @return the {@link List} of {@link ArchivaProjectModel} objects. (never null, but can be empty)      * @throws ArchivaDatabaseException if there is a fundamental database error.      */
specifier|public
name|List
name|getUsedBy
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|ArchivaDatabaseException
function_decl|;
block|}
end_interface

end_unit

