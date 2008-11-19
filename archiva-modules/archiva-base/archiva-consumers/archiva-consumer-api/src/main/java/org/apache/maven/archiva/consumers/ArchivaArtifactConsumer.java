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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaArtifact
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
comment|/**  * ArchivaArtifactConsumer - consumer for ArchivaArtifact objects.   *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArchivaArtifactConsumer
extends|extends
name|Consumer
block|{
comment|/**      * Get the list of included file patterns for this consumer.      *       * @return the list of ({@link String}) artifact types to process.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludedTypes
parameter_list|()
function_decl|;
comment|/**      *<p>      * Event that triggers at the beginning of a scan.      *</p>      *       *<p>      * NOTE: This would be a good place to initialize the consumer, to lock any resources, and to      * generally start tracking the scan as a whole.      *</p>      */
specifier|public
name|void
name|beginScan
parameter_list|()
function_decl|;
comment|/**      *<p>      * Event indicating an {@link ArchivaArtifact} is to be processed by this consumer.      *</p>       *       *<p>      * NOTE: The consumer does not need to process the artifact immediately, can can opt to queue and/or track      * the artifact to be processed in batch.  Just be sure to complete the processing by the {@link #completeScan()}       * event.       *</p>      *       * @param file the file to process.      * @throws ConsumerException if there was a problem processing this file.      */
specifier|public
name|void
name|processArchivaArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ConsumerException
function_decl|;
comment|/**      *<p>      * Event that triggers on the completion of a scan.      *</p>      *       *<p>      * NOTE: If the consumer opted to batch up processing requests in the       * {@link #processArchivaArtifact(ArchivaArtifact)} event this would be the last opportunity to drain       * any processing queue's.      *</p>      */
specifier|public
name|void
name|completeScan
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

