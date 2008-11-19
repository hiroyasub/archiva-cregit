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
name|constraints
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
name|Constraint
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

begin_comment
comment|/**  * ArtifactsProcessedConstraint   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactsProcessedConstraint
extends|extends
name|AbstractDeclarativeConstraint
implements|implements
name|Constraint
block|{
specifier|private
name|String
name|whereClause
decl_stmt|;
specifier|public
name|ArtifactsProcessedConstraint
parameter_list|(
name|boolean
name|isProcessed
parameter_list|)
block|{
if|if
condition|(
name|isProcessed
condition|)
block|{
name|whereClause
operator|=
literal|"whenProcessed != null"
expr_stmt|;
block|}
else|else
block|{
name|whereClause
operator|=
literal|"whenProcessed == null"
expr_stmt|;
block|}
block|}
comment|/**      * A Constraint showing artifacts processed since date provided.      * @param since      */
specifier|public
name|ArtifactsProcessedConstraint
parameter_list|(
name|Date
name|since
parameter_list|)
block|{
name|whereClause
operator|=
literal|"whenProcessed> since"
expr_stmt|;
name|declImports
operator|=
operator|new
name|String
index|[]
block|{
literal|"import java.util.Date"
block|}
expr_stmt|;
name|declParams
operator|=
operator|new
name|String
index|[]
block|{
literal|"Date since"
block|}
expr_stmt|;
name|params
operator|=
operator|new
name|Object
index|[]
block|{
name|since
block|}
expr_stmt|;
block|}
specifier|public
name|String
name|getSortColumn
parameter_list|()
block|{
return|return
literal|"groupId"
return|;
block|}
specifier|public
name|String
name|getWhereCondition
parameter_list|()
block|{
return|return
name|whereClause
return|;
block|}
block|}
end_class

end_unit

