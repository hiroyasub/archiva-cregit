begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|merger
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|IndexMergerRequest
block|{
comment|/**      * repositories Ids to merge content      */
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoriesIds
decl_stmt|;
comment|/**      * will generate a downloadable index      */
specifier|private
name|boolean
name|packIndex
decl_stmt|;
comment|/**      * original groupId (repositoryGroup id)      */
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|mergedIndexPath
init|=
literal|".indexer"
decl_stmt|;
specifier|private
name|int
name|mergedIndexTtl
decl_stmt|;
specifier|private
name|Path
name|mergedIndexDirectory
decl_stmt|;
specifier|private
name|boolean
name|temporary
decl_stmt|;
specifier|public
name|IndexMergerRequest
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoriesIds
parameter_list|,
name|boolean
name|packIndex
parameter_list|,
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|repositoriesIds
operator|=
name|repositoriesIds
expr_stmt|;
name|this
operator|.
name|packIndex
operator|=
name|packIndex
expr_stmt|;
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
comment|/**      * @since 1.4-M4      */
specifier|public
name|IndexMergerRequest
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoriesIds
parameter_list|,
name|boolean
name|packIndex
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|mergedIndexPath
parameter_list|,
name|int
name|mergedIndexTtl
parameter_list|)
block|{
name|this
operator|.
name|repositoriesIds
operator|=
name|repositoriesIds
expr_stmt|;
name|this
operator|.
name|packIndex
operator|=
name|packIndex
expr_stmt|;
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
name|this
operator|.
name|mergedIndexPath
operator|=
name|mergedIndexPath
expr_stmt|;
name|this
operator|.
name|mergedIndexTtl
operator|=
name|mergedIndexTtl
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getRepositoriesIds
parameter_list|()
block|{
return|return
name|repositoriesIds
return|;
block|}
specifier|public
name|void
name|setRepositoriesIds
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoriesIds
parameter_list|)
block|{
name|this
operator|.
name|repositoriesIds
operator|=
name|repositoriesIds
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPackIndex
parameter_list|()
block|{
return|return
name|packIndex
return|;
block|}
specifier|public
name|void
name|setPackIndex
parameter_list|(
name|boolean
name|packIndex
parameter_list|)
block|{
name|this
operator|.
name|packIndex
operator|=
name|packIndex
expr_stmt|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|String
name|getMergedIndexPath
parameter_list|()
block|{
return|return
name|mergedIndexPath
return|;
block|}
specifier|public
name|void
name|setMergedIndexPath
parameter_list|(
name|String
name|mergedIndexPath
parameter_list|)
block|{
name|this
operator|.
name|mergedIndexPath
operator|=
name|mergedIndexPath
expr_stmt|;
block|}
specifier|public
name|int
name|getMergedIndexTtl
parameter_list|()
block|{
return|return
name|mergedIndexTtl
return|;
block|}
specifier|public
name|void
name|setMergedIndexTtl
parameter_list|(
name|int
name|mergedIndexTtl
parameter_list|)
block|{
name|this
operator|.
name|mergedIndexTtl
operator|=
name|mergedIndexTtl
expr_stmt|;
block|}
specifier|public
name|Path
name|getMergedIndexDirectory
parameter_list|()
block|{
return|return
name|mergedIndexDirectory
return|;
block|}
specifier|public
name|void
name|setMergedIndexDirectory
parameter_list|(
name|Path
name|mergedIndexDirectory
parameter_list|)
block|{
name|this
operator|.
name|mergedIndexDirectory
operator|=
name|mergedIndexDirectory
expr_stmt|;
block|}
specifier|public
name|IndexMergerRequest
name|mergedIndexDirectory
parameter_list|(
name|Path
name|mergedIndexDirectory
parameter_list|)
block|{
name|this
operator|.
name|mergedIndexDirectory
operator|=
name|mergedIndexDirectory
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isTemporary
parameter_list|()
block|{
return|return
name|temporary
return|;
block|}
specifier|public
name|void
name|setTemporary
parameter_list|(
name|boolean
name|temporary
parameter_list|)
block|{
name|this
operator|.
name|temporary
operator|=
name|temporary
expr_stmt|;
block|}
specifier|public
name|IndexMergerRequest
name|temporary
parameter_list|(
name|boolean
name|temporary
parameter_list|)
block|{
name|this
operator|.
name|temporary
operator|=
name|temporary
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"IndexMergerRequest{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"repositoriesIds="
argument_list|)
operator|.
name|append
argument_list|(
name|repositoriesIds
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", packIndex="
argument_list|)
operator|.
name|append
argument_list|(
name|packIndex
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", groupId='"
argument_list|)
operator|.
name|append
argument_list|(
name|groupId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", mergedIndexPath='"
argument_list|)
operator|.
name|append
argument_list|(
name|mergedIndexPath
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", mergedIndexTtl="
argument_list|)
operator|.
name|append
argument_list|(
name|mergedIndexTtl
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", mergedIndexDirectory="
argument_list|)
operator|.
name|append
argument_list|(
name|mergedIndexDirectory
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", temporary="
argument_list|)
operator|.
name|append
argument_list|(
name|temporary
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|IndexMergerRequest
name|that
init|=
operator|(
name|IndexMergerRequest
operator|)
name|o
decl_stmt|;
return|return
name|groupId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|groupId
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|groupId
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit
