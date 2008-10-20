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

begin_comment
comment|/**  * ArtifactVersionsConstraint  *   * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactVersionsConstraint
extends|extends
name|AbstractDeclarativeConstraint
implements|implements
name|Constraint
block|{
specifier|private
name|String
name|whereClause
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|sortColumn
init|=
literal|"repositoryId"
decl_stmt|;
specifier|public
name|ArtifactVersionsConstraint
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|boolean
name|includeWhenGathered
parameter_list|)
block|{
if|if
condition|(
name|repoId
operator|!=
literal|null
condition|)
block|{
name|whereClause
operator|=
literal|"repositoryId.equals(selectedRepoId)&& groupId.equals(selectedGroupId)&& artifactId.equals(selectedArtifactId) "
operator|+
operator|(
name|includeWhenGathered
condition|?
literal|"&& whenGathered != null"
else|:
literal|""
operator|)
expr_stmt|;
name|declParams
operator|=
operator|new
name|String
index|[]
block|{
literal|"String selectedRepoId"
block|,
literal|"String selectedGroupId"
block|,
literal|"String selectedArtifactId"
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
name|groupId
block|,
name|artifactId
block|}
expr_stmt|;
block|}
else|else
block|{
name|whereClause
operator|=
literal|"groupId.equals(selectedGroupId)&& artifactId.equals(selectedArtifactId) "
operator|+
operator|(
name|includeWhenGathered
condition|?
literal|"&& whenGathered != null"
else|:
literal|""
operator|)
expr_stmt|;
name|declParams
operator|=
operator|new
name|String
index|[]
block|{
literal|"String selectedGroupId"
block|,
literal|"String selectedArtifactId"
block|}
expr_stmt|;
name|params
operator|=
operator|new
name|Object
index|[]
block|{
name|groupId
block|,
name|artifactId
block|}
expr_stmt|;
block|}
block|}
specifier|public
name|ArtifactVersionsConstraint
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|sortColumn
parameter_list|)
block|{
name|this
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
literal|true
argument_list|)
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

