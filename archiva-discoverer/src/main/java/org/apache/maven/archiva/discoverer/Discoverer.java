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
name|discoverer
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
name|repository
operator|.
name|ArtifactRepository
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
comment|/**  * Discoverer - generic discoverer of content in an ArtifactRepository.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|Discoverer
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|Discoverer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Walk the repository, and report to the consumers the files found.      *       * Report changes to the appropriate Consumer.      *       * This is just a convenience method to {@link #walkRepository(ArtifactRepository, List, boolean, long, List, List)}      * equivalent to calling<code>walkRepository( repository, consumers, includeSnapshots, 0, null, null );</code>      *       * @param repository the repository to change.      * @param consumers use the provided list of consumers.      * @param includeSnapshots true to include snapshots in the walking of this repository.      * @return the statistics for this scan.      * @throws DiscovererException if there was a fundamental problem with getting the discoverer started.      */
specifier|public
name|DiscovererStatistics
name|walkRepository
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|List
name|consumers
parameter_list|,
name|boolean
name|includeSnapshots
parameter_list|)
throws|throws
name|DiscovererException
function_decl|;
comment|/**      * Walk the repository, and report to the consumers the files found.      *       * Report changes to the appropriate Consumer.      *       * @param repository the repository to change.      * @param consumers use the provided list of consumers.      * @param includeSnapshots true to include snapshots in the scanning of this repository.      * @param onlyModifiedAfterTimestamp Only report to the consumers, files that have a {@link File#lastModified()})       *          after the provided timestamp.      * @param extraFileExclusions an optional list of file exclusions on the walk.      * @param extraFileInclusions an optional list of file inclusions on the walk.      * @return the statistics for this scan.      * @throws DiscovererException if there was a fundamental problem with getting the discoverer started.       */
specifier|public
name|DiscovererStatistics
name|walkRepository
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|List
name|consumers
parameter_list|,
name|boolean
name|includeSnapshots
parameter_list|,
name|long
name|onlyModifiedAfterTimestamp
parameter_list|,
name|List
name|extraFileExclusions
parameter_list|,
name|List
name|extraFileInclusions
parameter_list|)
throws|throws
name|DiscovererException
function_decl|;
block|}
end_interface

end_unit

