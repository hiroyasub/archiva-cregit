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
name|common
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
name|common
operator|.
name|utils
operator|.
name|BaseFile
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * DiscovererConsumer   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|Consumer
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|Consumer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * This is the human readable name for the discoverer.      *       * @return the human readable discoverer name.      */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * This is used to initialize any internals in the consumer before it is used.      *       * This method is called by the internals of archiva and is not meant to be used by other developers.      * This method is called once per repository.      *       * @param repository the repository to initialize the consumer against.      * @return true if the repository is valid for this consumer. false will result in consumer being disabled       *      for the provided repository.      */
specifier|public
name|boolean
name|init
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Get the List of excluded file patterns for this consumer.      *       * @return the list of excluded file patterns for this consumer.      */
specifier|public
name|List
name|getExcludePatterns
parameter_list|()
function_decl|;
comment|/**      * Get the List of included file patterns for this consumer.      *       * @return the list of included file patterns for this consumer.      */
specifier|public
name|List
name|getIncludePatterns
parameter_list|()
function_decl|;
comment|/**      * Called by archiva framework to indicate that there is a file suitable for consuming,       * This method will only be called if the {@link #init(ArtifactRepository)} and {@link #getExcludePatterns()}      * and {@link #getIncludePatterns()} all pass for this consumer.      *       * @param file the file to process.      * @throws ConsumerException if there was a problem processing this file.      */
specifier|public
name|void
name|processFile
parameter_list|(
name|BaseFile
name|file
parameter_list|)
throws|throws
name|ConsumerException
function_decl|;
comment|/**      * Called by archiva framework to indicate that there has been a problem detected      * on a specific file.      *       * NOTE: It is very possible for 1 file to have more than 1 problem associated with it.      *       * @param file the file to process.      * @param message the message describing the problem.      */
specifier|public
name|void
name|processFileProblem
parameter_list|(
name|BaseFile
name|file
parameter_list|,
name|String
name|message
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

