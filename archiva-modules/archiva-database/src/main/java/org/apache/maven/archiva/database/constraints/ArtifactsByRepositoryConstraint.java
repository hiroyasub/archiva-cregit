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
name|java
operator|.
name|util
operator|.
name|Date
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
name|Constraint
import|;
end_import

begin_comment
comment|/**  * ArtifactsByRepositoryConstraint  *   * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactsByRepositoryConstraint
extends|extends
name|AbstractDeclarativeConstraint
implements|implements
name|Constraint
block|{
specifier|private
name|String
name|whereClause
decl_stmt|;
specifier|private
name|String
name|sortColumn
init|=
literal|"groupId"
decl_stmt|;
specifier|public
name|ArtifactsByRepositoryConstraint
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
name|whereClause
operator|=
literal|"repositoryId == repoId"
expr_stmt|;
name|declParams
operator|=
operator|new
name|String
index|[]
block|{
literal|"String repoId"
block|}
expr_stmt|;
name|params
operator|=
operator|new
name|Object
index|[]
block|{
name|repoId
block|}
expr_stmt|;
block|}
specifier|public
name|ArtifactsByRepositoryConstraint
parameter_list|(
name|String
name|repoId
parameter_list|,
name|Date
name|targetWhenGathered
parameter_list|,
name|String
name|sortColumn
parameter_list|,
name|boolean
name|isBefore
parameter_list|)
block|{
name|String
name|condition
init|=
name|isBefore
condition|?
literal|"<="
else|:
literal|">="
decl_stmt|;
name|declImports
operator|=
operator|new
name|String
index|[]
block|{
literal|"import java.util.Date"
block|}
expr_stmt|;
name|whereClause
operator|=
literal|"this.repositoryId == repoId&& this.whenGathered "
operator|+
name|condition
operator|+
literal|" targetWhenGathered"
expr_stmt|;
name|declParams
operator|=
operator|new
name|String
index|[]
block|{
literal|"String repoId"
block|,
literal|"Date targetWhenGathered"
block|}
expr_stmt|;
name|params
operator|=
operator|new
name|Object
index|[]
block|{
name|repoId
block|,
name|targetWhenGathered
block|}
expr_stmt|;
name|this
operator|.
name|sortColumn
operator|=
name|sortColumn
expr_stmt|;
block|}
specifier|public
name|ArtifactsByRepositoryConstraint
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|type
parameter_list|,
name|Date
name|before
parameter_list|,
name|String
name|sortColumn
parameter_list|)
block|{
name|declImports
operator|=
operator|new
name|String
index|[]
block|{
literal|"import java.util.Date"
block|}
expr_stmt|;
name|whereClause
operator|=
literal|"this.repositoryId == repoId&& this.type == type&& this.whenGathered<= before"
expr_stmt|;
name|declParams
operator|=
operator|new
name|String
index|[]
block|{
literal|"String repoId"
block|,
literal|"String type"
block|,
literal|"Date before"
block|}
expr_stmt|;
name|params
operator|=
operator|new
name|Object
index|[]
block|{
name|repoId
block|,
name|type
block|,
name|before
block|}
expr_stmt|;
name|this
operator|.
name|sortColumn
operator|=
name|sortColumn
expr_stmt|;
block|}
specifier|public
name|String
name|getSortColumn
parameter_list|()
block|{
return|return
name|sortColumn
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

