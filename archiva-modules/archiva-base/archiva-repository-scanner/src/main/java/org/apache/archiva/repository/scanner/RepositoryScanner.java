begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
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
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepository
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
name|consumers
operator|.
name|InvalidRepositoryContentConsumer
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * RepositoryScanner  *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryScanner
block|{
comment|/**      * The value to pass to {@link #scan(ManagedRepository, long)} to have the scan      * operate in a fresh fashion, with no check on changes based on timestamp.      */
specifier|public
specifier|static
specifier|final
name|long
name|FRESH_SCAN
init|=
literal|0
decl_stmt|;
comment|/**      *<p>      * Typical Ignorable Content patterns.      *</p>      *<p/>      *<p><strong>      * NOTE: Do not use for normal webapp or task driven repository scanning.      *</strong></p>      *<p/>      *<p>      * These patterns are only valid for archiva-cli and archiva-converter use.      *</p>      */
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|IGNORABLE_CONTENT
init|=
block|{
literal|"bin/**"
block|,
literal|"reports/**"
block|,
literal|".index"
block|,
literal|".reports/**"
block|,
literal|".maven/**"
block|,
literal|"**/.svn/**"
block|,
literal|"**/*snapshot-version"
block|,
literal|"*/website/**"
block|,
literal|"*/licences/**"
block|,
literal|"**/.htaccess"
block|,
literal|"**/*.html"
block|,
literal|"**/*.txt"
block|,
literal|"**/README*"
block|,
literal|"**/CHANGELOG*"
block|,
literal|"**/KEYS*"
block|}
decl_stmt|;
comment|/**      * Scan the repository for content changes.      *<p/>      * Internally, this will use the as-configured known and invalid consumer lists.      *      * @param repository   the repository to change.      * @param changesSince the timestamp to use as a threshold on what is considered new or changed.      *                     (To have all content be taken into consideration regardless of timestamp,      *                     use the {@link #FRESH_SCAN} constant)      * @return the statistics for this scan.      * @throws RepositoryScannerException if there was a fundamental problem with getting the discoverer started.      */
name|RepositoryScanStatistics
name|scan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|long
name|changesSince
parameter_list|)
throws|throws
name|RepositoryScannerException
function_decl|;
comment|/**      * Scan the repository for content changes.      *<p/>      * Internally, this will use the as-configured known and invalid consumer lists.      *      * @param repository              the repository to change.      * @param knownContentConsumers   the list of consumers that follow the {@link KnownRepositoryContentConsumer}      *                                interface that should be used for this scan.      * @param invalidContentConsumers the list of consumers that follow the {@link InvalidRepositoryContentConsumer}      *                                interface that should be used for this scan.      * @param ignoredContentPatterns  list of patterns that should be ignored and not sent to any consumer.      * @param changesSince            the timestamp to use as a threshold on what is considered new or changed.      *                                (To have all content be taken into consideration regardless of timestamp,      *                                use the {@link #FRESH_SCAN} constant)      * @return the statistics for this scan.      * @throws RepositoryScannerException if there was a fundamental problem with getting the discoverer started.      */
name|RepositoryScanStatistics
name|scan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownContentConsumers
parameter_list|,
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidContentConsumers
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|ignoredContentPatterns
parameter_list|,
name|long
name|changesSince
parameter_list|)
throws|throws
name|RepositoryScannerException
function_decl|;
name|Set
argument_list|<
name|RepositoryScannerInstance
argument_list|>
name|getInProgressScans
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

