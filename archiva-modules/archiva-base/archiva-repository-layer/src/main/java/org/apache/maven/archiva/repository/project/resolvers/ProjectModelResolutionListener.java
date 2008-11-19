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
name|repository
operator|.
name|project
operator|.
name|resolvers
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
name|model
operator|.
name|ArchivaProjectModel
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
name|VersionedReference
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
name|repository
operator|.
name|project
operator|.
name|ProjectModelResolver
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
comment|/**  * ProjectModelResolutionListener   *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProjectModelResolutionListener
block|{
comment|/**      * Indicates that the resolution process has started for a specific project.      *       * @param projectRef the project reference.      * @param resolverList the {@link List} of {@link ProjectModelResolver}'s that will be searched.      * @see #resolutionSuccess(VersionedReference, ProjectModelResolver, ArchivaProjectModel)      * @see #resolutionNotFound(VersionedReference, List)      */
specifier|public
name|void
name|resolutionStart
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|List
name|resolverList
parameter_list|)
function_decl|;
comment|/**      * Indicates that a resolution against a specific resolver is about       * to occur.      *       * @param projectRef the project reference.      * @param resolver the resolver to attempt resolution on.      */
specifier|public
name|void
name|resolutionAttempting
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Indicates that a resolution against a specific resolver resulted      * in in a missed resolution.      *       * "Miss" in this case refers to an attempt against a resolver, and that      * resolver essentially responds with a "not found here" response.      *       * @param projectRef the project reference.      * @param resolver the resolver the attempt was made on.      */
specifier|public
name|void
name|resolutionMiss
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Indicates that a resolution against the specific resolver has      * caused an error.      *       * @param projectRef the project reference.      * @param resolver the (optional) resolver on which the error occured.      * @param cause the cause of the error.      */
specifier|public
name|void
name|resolutionError
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|,
name|Exception
name|cause
parameter_list|)
function_decl|;
comment|/**      * Indicates that a resolution process has finished, and the requested      * projectRef has been found.       *       * @param projectRef the project reference.      * @param resolver the resolver on which success occured.      * @param model the resolved model.       * @see #resolutionStart(VersionedReference, List)      */
specifier|public
name|void
name|resolutionSuccess
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|,
name|ArchivaProjectModel
name|model
parameter_list|)
function_decl|;
comment|/**      * Indicates that the resolution process has finished, and the requested      * projectRef could not be found.      *       * @param projectRef the project reference.      * @param resolverList the {@link List} of {@link ProjectModelResolver}'s that was be searched.      * @see #resolutionStart(VersionedReference, List)      */
specifier|public
name|void
name|resolutionNotFound
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|List
name|resolverList
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

