begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
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
name|repository
operator|.
name|ManagedRepository
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
comment|/**  * A consumer of content (files) in the repository.  *  * olamy: TODO/FIXME we must review this api, in the current situation we use prototype beans rather than singletons  * this is a bit memory consuming the better will be to ConsumerContext bean to transport repository context etc...  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryContentConsumer
extends|extends
name|Consumer
block|{
comment|/**      * Get the list of included file patterns for this consumer.      *      * @return the list of {@link String} patterns. (example:<code>&quot;**&#47;*.pom&quot;</code>)      */
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
function_decl|;
comment|/**      * Get the list of excluded file patterns for this consumer.      *      * @return the list of {@link String} patterns. (example:<code>&quot;**&#47;*.pom&quot;</code>) - (can be null for no exclusions)      */
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
function_decl|;
comment|/**      *<p>      * Event that triggers at the beginning of a scan.      *</p>      *<p>      * NOTE: This would be a good place to initialize the consumer, to lock any resources, and to      * generally start tracking the scan as a whole.      *</p>      *      * @param repository   the repository that this consumer is being used for.      * @param whenGathered the start of the repository scan      * @throws ConsumerException if there was a problem with using the provided repository with the consumer.      */
name|void
name|beginScan
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
function_decl|;
comment|/**      *<p>      * Event that triggers at the beginning of a scan, where you can also indicate whether the consumers will be      * executed on an entire repository or on a specific resource.      *</p>      *      * @param repository          the repository that this consumer is being used for.      * @param whenGathered        the start of the repository scan      * @param executeOnEntireRepo flags whether the consumer will be executed on an entire repository or just on a specific resource      * @throws ConsumerException if there was a problem with using the provided repository with the consumer.      * @see RepositoryContentConsumer#beginScan(ManagedRepository, java.util.Date)      */
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
function_decl|;
comment|/**      *<p>      * Event indicating a file is to be processed by this consumer.      *</p>      *<p>      * NOTE: The consumer does not need to process the file immediately, can can opt to queue and/or track      * the files to be processed in batch.  Just be sure to complete the processing by the {@link #completeScan()}      * event.      *</p>      *      * @param path the relative file path (in the repository) to process.      * @throws ConsumerException if there was a problem processing this file.      */
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
function_decl|;
comment|/**      * @param path the relative file path (in the repository) to process.      * @param executeOnEntireRepo flags whether the consumer will be executed on an entire repository or just on a specific resource      * @throws Exception if there was a problem processing this file.      */
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      *<p>      * Event that triggers on the completion of a scan.      *</p>      *<p>      * NOTE: If the consumer opted to batch up processing requests in the {@link #processFile(String)} event      * this would be the last opportunity to drain any processing queue's.      *</p>      */
name|void
name|completeScan
parameter_list|()
function_decl|;
comment|/**      * @param executeOnEntireRepo flags whether the consumer will be executed on an entire repository or just on a specific resource      */
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
function_decl|;
comment|/**      * Whether the consumer should process files that have not been modified since the time passed in to the scan      * method.      *      * @return whether to process the unmodified files      */
name|boolean
name|isProcessUnmodified
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

