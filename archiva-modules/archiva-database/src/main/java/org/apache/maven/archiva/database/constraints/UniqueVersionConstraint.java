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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|ArchivaArtifactModel
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
comment|/**  * Obtain the list of version's for specific GroupId and ArtifactId.  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|UniqueVersionConstraint
extends|extends
name|AbstractSimpleConstraint
implements|implements
name|Constraint
block|{
specifier|private
name|StringBuffer
name|sql
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
comment|/**      * Obtain the list of version's for specific GroupId and ArtifactId.      *       * @param selectedRepositoryIds the selected repository ids.      * @param groupId the selected groupId.      * @param artifactId the selected artifactId.      */
specifier|public
name|UniqueVersionConstraint
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepositoryIds
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A blank groupId is not allowed."
argument_list|)
throw|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A blank artifactId is not allowed."
argument_list|)
throw|;
block|}
name|appendSelect
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|sql
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
expr_stmt|;
name|SqlBuilder
operator|.
name|appendWhereSelectedRepositories
argument_list|(
name|sql
argument_list|,
literal|"repositoryId"
argument_list|,
name|selectedRepositoryIds
argument_list|)
expr_stmt|;
name|sql
operator|.
name|append
argument_list|(
literal|"&& "
argument_list|)
expr_stmt|;
name|appendWhereSelectedGroupIdArtifactId
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|appendGroupBy
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|super
operator|.
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
comment|/**      * Obtain the list of version's for specific GroupId and ArtifactId.      *       * @param groupId the selected groupId.      * @param artifactId the selected artifactId.      */
specifier|public
name|UniqueVersionConstraint
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A blank groupId is not allowed."
argument_list|)
throw|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A blank artifactId is not allowed."
argument_list|)
throw|;
block|}
name|appendSelect
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|sql
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
expr_stmt|;
name|appendWhereSelectedGroupIdArtifactId
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|appendGroupBy
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|super
operator|.
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Class
name|getResultClass
parameter_list|()
block|{
return|return
name|String
operator|.
name|class
return|;
block|}
specifier|public
name|String
name|getSelectSql
parameter_list|()
block|{
return|return
name|sql
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|appendGroupBy
parameter_list|(
name|StringBuffer
name|buf
parameter_list|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" GROUP BY version ORDER BY version ASCENDING"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendSelect
parameter_list|(
name|StringBuffer
name|buf
parameter_list|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"SELECT version FROM "
argument_list|)
operator|.
name|append
argument_list|(
name|ArchivaArtifactModel
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendWhereSelectedGroupIdArtifactId
parameter_list|(
name|StringBuffer
name|buf
parameter_list|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" groupId == selectedGroupId&& artifactId == selectedArtifactId"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" PARAMETERS String selectedGroupId, String selectedArtifactId"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

