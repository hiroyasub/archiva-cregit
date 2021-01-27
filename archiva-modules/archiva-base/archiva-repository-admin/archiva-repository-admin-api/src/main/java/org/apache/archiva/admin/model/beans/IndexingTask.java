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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * Information about index update tasks running on a repository.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|IndexingTask
extends|extends
name|RepositoryTaskInfo
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|1947200162602613310L
decl_stmt|;
comment|/**      *<code>true</code>, if this task is just updating the existing index.      */
specifier|private
name|boolean
name|updateOnly
decl_stmt|;
specifier|public
name|boolean
name|isUpdateOnly
parameter_list|( )
block|{
return|return
name|updateOnly
return|;
block|}
specifier|public
name|void
name|setUpdateOnly
parameter_list|(
name|boolean
name|updateOnly
parameter_list|)
block|{
name|this
operator|.
name|updateOnly
operator|=
name|updateOnly
expr_stmt|;
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
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|( )
operator|!=
name|o
operator|.
name|getClass
argument_list|( )
condition|)
return|return
literal|false
return|;
name|IndexingTask
name|that
init|=
operator|(
name|IndexingTask
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|isFullScan
argument_list|( )
operator|!=
name|that
operator|.
name|fullScan
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|updateOnly
operator|!=
name|that
operator|.
name|updateOnly
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|isRunning
argument_list|( )
operator|!=
name|that
operator|.
name|isRunning
argument_list|( )
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|getMaxExecutionTimeMs
argument_list|( )
operator|!=
name|that
operator|.
name|getMaxExecutionTimeMs
argument_list|( )
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|getRepositoryId
argument_list|( )
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getRepositoryId
argument_list|( )
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
name|getResource
argument_list|( )
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getResource
argument_list|( )
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
name|int
name|result
init|=
name|getRepositoryId
argument_list|( )
operator|.
name|hashCode
argument_list|( )
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|isFullScan
argument_list|( )
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|updateOnly
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|getResource
argument_list|( )
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|isRunning
argument_list|( )
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|int
operator|)
operator|(
name|getMaxExecutionTimeMs
argument_list|( )
operator|^
operator|(
name|getMaxExecutionTimeMs
argument_list|( )
operator|>>>
literal|32
operator|)
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
parameter_list|( )
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"IndexingTask{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"repositoryId='"
argument_list|)
operator|.
name|append
argument_list|(
name|getRepositoryId
argument_list|( )
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
literal|", fullRepository="
argument_list|)
operator|.
name|append
argument_list|(
name|isFullScan
argument_list|( )
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", updateOnly="
argument_list|)
operator|.
name|append
argument_list|(
name|updateOnly
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", resource='"
argument_list|)
operator|.
name|append
argument_list|(
name|getResource
argument_list|( )
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
literal|", running="
argument_list|)
operator|.
name|append
argument_list|(
name|isRunning
argument_list|( )
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", maxExecutionTimeMs="
argument_list|)
operator|.
name|append
argument_list|(
name|getMaxExecutionTimeMs
argument_list|( )
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
argument_list|( )
return|;
block|}
block|}
end_class

end_unit

