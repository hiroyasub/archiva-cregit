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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * DeclarativeConstraint   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|DeclarativeConstraint
extends|extends
name|Constraint
block|{
comment|/**      * Get the declared imports used for this query. (optional)      *       * NOTE: This is DAO implementation specific.      *       * @return the imports. (can be null)      */
specifier|public
specifier|abstract
name|String
index|[]
name|getDeclaredImports
parameter_list|()
function_decl|;
comment|/**      * Get the declared parameters used for this query. (optional)      *       * NOTE: This is DAO implementation specific.      *       * @return the parameters. (can be null)      */
specifier|public
specifier|abstract
name|String
index|[]
name|getDeclaredParameters
parameter_list|()
function_decl|;
comment|/**      * The JDOQL filter to apply to the query. (optional)      *       * NOTE: This is DAO implementation specific.      *       * @return the filter to apply. (can be null)      */
specifier|public
specifier|abstract
name|String
name|getFilter
parameter_list|()
function_decl|;
comment|/**      * Get the parameters used for this query. (required if using {@link #getDeclaredParameters()} )      *       * NOTE: This is DAO implementation specific.      *       * @return the parameters. (can be null)      */
specifier|public
specifier|abstract
name|Object
index|[]
name|getParameters
parameter_list|()
function_decl|;
comment|/**      * Get the sort direction name.      *       * @return the sort direction name. ("ASC" or "DESC") (only valid if {@link #getSortColumn()} is specified.)      */
specifier|public
specifier|abstract
name|String
name|getSortDirection
parameter_list|()
function_decl|;
comment|/**      * Get the sort column name.      *       * @return the sort column name. (can be null)      */
specifier|public
specifier|abstract
name|String
name|getSortColumn
parameter_list|()
function_decl|;
comment|/**      * Get the variables used within the query.      *       * NOTE: This is DAO implementation specific.      *       * @return the variables used within the query.      */
specifier|public
specifier|abstract
name|String
index|[]
name|getVariables
parameter_list|()
function_decl|;
comment|/**      * Get the SELECT WHERE (condition) value for the constraint.      *       * @return the equivalent of the SELECT WHERE (condition) value for this constraint. (can be null)      */
specifier|public
specifier|abstract
name|String
name|getWhereCondition
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

