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
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|repositories
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
name|List
import|;
end_import

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
name|configuration
operator|.
name|Configuration
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
name|configuration
operator|.
name|RepositoryGroupConfiguration
import|;
end_import

begin_comment
comment|/**  * SortRepositoriesAction  *   * @version  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="sortRepositoriesAction"  */
end_comment

begin_class
specifier|public
class|class
name|SortRepositoriesAction
extends|extends
name|AbstractRepositoriesAdminAction
block|{
specifier|private
name|String
name|repoGroupId
decl_stmt|;
specifier|private
name|String
name|targetRepo
decl_stmt|;
specifier|public
name|String
name|sortDown
parameter_list|()
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
name|getRepositoriesFromGroup
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|findTargetRepository
argument_list|(
name|repositories
argument_list|,
name|targetRepo
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
operator|&&
name|validIndex
argument_list|(
name|repositories
argument_list|,
name|idx
operator|+
literal|1
argument_list|)
condition|)
block|{
name|repositories
operator|.
name|remove
argument_list|(
name|idx
argument_list|)
expr_stmt|;
name|repositories
operator|.
name|add
argument_list|(
name|idx
operator|+
literal|1
argument_list|,
name|targetRepo
argument_list|)
expr_stmt|;
block|}
return|return
name|saveConfiguration
argument_list|(
name|config
argument_list|)
return|;
block|}
specifier|public
name|String
name|sortUp
parameter_list|()
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
name|getRepositoriesFromGroup
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|findTargetRepository
argument_list|(
name|repositories
argument_list|,
name|targetRepo
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
operator|&&
name|validIndex
argument_list|(
name|repositories
argument_list|,
name|idx
operator|-
literal|1
argument_list|)
condition|)
block|{
name|repositories
operator|.
name|remove
argument_list|(
name|idx
argument_list|)
expr_stmt|;
name|repositories
operator|.
name|add
argument_list|(
name|idx
operator|-
literal|1
argument_list|,
name|targetRepo
argument_list|)
expr_stmt|;
block|}
return|return
name|saveConfiguration
argument_list|(
name|config
argument_list|)
return|;
block|}
specifier|public
name|String
name|getRepoGroupId
parameter_list|()
block|{
return|return
name|repoGroupId
return|;
block|}
specifier|public
name|void
name|setRepoGroupId
parameter_list|(
name|String
name|repoGroupId
parameter_list|)
block|{
name|this
operator|.
name|repoGroupId
operator|=
name|repoGroupId
expr_stmt|;
block|}
specifier|public
name|String
name|getTargetRepo
parameter_list|()
block|{
return|return
name|targetRepo
return|;
block|}
specifier|public
name|void
name|setTargetRepo
parameter_list|(
name|String
name|targetRepo
parameter_list|)
block|{
name|this
operator|.
name|targetRepo
operator|=
name|targetRepo
expr_stmt|;
block|}
specifier|private
name|int
name|findTargetRepository
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositories
parameter_list|,
name|String
name|targetRepository
parameter_list|)
block|{
name|int
name|idx
init|=
operator|(
operator|-
literal|1
operator|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|repositories
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|targetRepository
argument_list|,
name|repositories
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|idx
operator|=
name|i
expr_stmt|;
break|break;
block|}
block|}
return|return
name|idx
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getRepositoriesFromGroup
parameter_list|()
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|RepositoryGroupConfiguration
name|repoGroup
init|=
name|config
operator|.
name|findRepositoryGroupById
argument_list|(
name|repoGroupId
argument_list|)
decl_stmt|;
return|return
name|repoGroup
operator|.
name|getRepositories
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|validIndex
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositories
parameter_list|,
name|int
name|idx
parameter_list|)
block|{
return|return
operator|(
name|idx
operator|>=
literal|0
operator|)
operator|&&
operator|(
name|idx
operator|<
name|repositories
operator|.
name|size
argument_list|()
operator|)
return|;
block|}
block|}
end_class

end_unit

