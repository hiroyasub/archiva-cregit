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
name|DeclarativeConstraint
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
name|ArchivaArtifact
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
name|Dependency
import|;
end_import

begin_comment
comment|/**  * ProjectsByArtifactUsageConstraint   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ProjectsByArtifactUsageConstraint
extends|extends
name|AbstractDeclarativeConstraint
implements|implements
name|DeclarativeConstraint
block|{
specifier|private
name|String
name|filter
decl_stmt|;
specifier|public
name|ProjectsByArtifactUsageConstraint
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
block|{
name|this
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ProjectsByArtifactUsageConstraint
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|super
operator|.
name|declImports
operator|=
operator|new
name|String
index|[]
block|{
literal|"import "
operator|+
name|Dependency
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
expr_stmt|;
name|super
operator|.
name|variables
operator|=
operator|new
name|String
index|[]
block|{
literal|"Dependency dep"
block|}
expr_stmt|;
name|super
operator|.
name|declParams
operator|=
operator|new
name|String
index|[]
block|{
literal|"String selectedGroupId"
block|,
literal|"String selectedArtifactId"
block|,
literal|"String selectedVersion"
block|}
expr_stmt|;
name|filter
operator|=
literal|"dependencies.contains( dep )&& "
operator|+
literal|"dep.groupId == selectedGroupId&& "
operator|+
literal|"dep.artifactId == selectedArtifactId&& "
operator|+
literal|"dep.version == selectedVersion"
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
block|,
name|version
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
literal|null
return|;
block|}
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
block|}
end_class

end_unit

