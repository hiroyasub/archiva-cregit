begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"repositoryGroup"
argument_list|)
specifier|public
class|class
name|RepositoryGroup
implements|implements
name|Serializable
block|{
comment|/**      * repository group Id      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * repositories ids      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|repositories
decl_stmt|;
comment|/**      * The path of the merged index.      */
specifier|private
name|String
name|mergedIndexPath
init|=
literal|"/.indexer"
decl_stmt|;
comment|/**      * The TTL (time to live) of the repo group's merged index.      */
specifier|private
name|int
name|mergedIndexTtl
init|=
literal|30
decl_stmt|;
comment|/**      * default model value is empty so none      * @since 2.0.0      */
specifier|private
name|String
name|cronExpression
decl_stmt|;
specifier|public
name|RepositoryGroup
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|RepositoryGroup
parameter_list|(
name|String
name|id
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|repositories
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|repositories
operator|=
name|repositories
expr_stmt|;
block|}
comment|/**      * Method addRepository.      *      * @param string      */
specifier|public
name|void
name|addRepository
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getRepositories
argument_list|()
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the id of the repository group.      *      * @return String      */
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
comment|/**      * Method getRepositories.      *      * @return List      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRepositories
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|repositories
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|repositories
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|repositories
return|;
block|}
comment|/**      * Method removeRepository.      *      * @param string      */
specifier|public
name|void
name|removeRepository
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getRepositories
argument_list|()
operator|.
name|remove
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the id of the repository group.      *      * @param id      */
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Set the list of repository ids under the group.      *      * @param repositories      */
specifier|public
name|void
name|setRepositories
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositories
parameter_list|)
block|{
name|this
operator|.
name|repositories
operator|=
name|repositories
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
comment|/**      * Set the TTL of the repo group's merged index.      *      * @param mergedIndexTtl      */
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
name|RepositoryGroup
name|mergedIndexPath
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
return|return
name|this
return|;
block|}
specifier|public
name|RepositoryGroup
name|mergedIndexTtl
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
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getCronExpression
parameter_list|()
block|{
return|return
name|cronExpression
return|;
block|}
specifier|public
name|void
name|setCronExpression
parameter_list|(
name|String
name|cronExpression
parameter_list|)
block|{
name|this
operator|.
name|cronExpression
operator|=
name|cronExpression
expr_stmt|;
block|}
specifier|public
name|RepositoryGroup
name|mergedIndexCronExpression
parameter_list|(
name|String
name|mergedIndexCronExpression
parameter_list|)
block|{
name|this
operator|.
name|cronExpression
operator|=
name|mergedIndexCronExpression
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|other
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|other
operator|instanceof
name|RepositoryGroup
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|RepositoryGroup
name|that
init|=
operator|(
name|RepositoryGroup
operator|)
name|other
decl_stmt|;
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|result
operator|=
name|result
operator|&&
operator|(
name|getId
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getId
argument_list|()
operator|==
literal|null
else|:
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getId
argument_list|()
argument_list|)
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|17
decl_stmt|;
name|result
operator|=
literal|37
operator|*
name|result
operator|+
operator|(
name|id
operator|!=
literal|null
condition|?
name|id
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
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
literal|"RepositoryGroup{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"id='"
argument_list|)
operator|.
name|append
argument_list|(
name|id
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
literal|", repositories="
argument_list|)
operator|.
name|append
argument_list|(
name|repositories
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
literal|", cronExpression='"
argument_list|)
operator|.
name|append
argument_list|(
name|cronExpression
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
block|}
end_class

end_unit

