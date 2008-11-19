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
name|ArchivaProjectModel
import|;
end_import

begin_comment
comment|/**  * Generic Filtering interface for {@link ArchivaProjectModel} objects.    *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProjectModelFilter
block|{
comment|/**      * Filter a model and return the results of the filtering.      *       * @param model the model to filter.      * @return a new model representing the filtered state of the model.      * @throws ProjectModelException if there was a problem executing the filter.      */
specifier|public
name|ArchivaProjectModel
name|filter
parameter_list|(
specifier|final
name|ArchivaProjectModel
name|model
parameter_list|)
throws|throws
name|ProjectModelException
function_decl|;
block|}
end_interface

end_unit

