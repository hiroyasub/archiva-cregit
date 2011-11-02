begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|indexing
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
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|index
operator|.
name|NexusIndexer
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
name|index
operator|.
name|context
operator|.
name|IndexCreator
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
name|index
operator|.
name|context
operator|.
name|IndexingContext
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
name|index
operator|.
name|context
operator|.
name|UnsupportedExistingLuceneIndexException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|taskqueue
operator|.
name|Task
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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

begin_class
specifier|public
class|class
name|ArtifactIndexingTask
implements|implements
name|Task
block|{
specifier|public
enum|enum
name|Action
block|{
name|ADD
block|,
name|DELETE
block|,
name|FINISH
block|}
specifier|private
specifier|final
name|ManagedRepository
name|repository
decl_stmt|;
specifier|private
specifier|final
name|File
name|resourceFile
decl_stmt|;
specifier|private
specifier|final
name|Action
name|action
decl_stmt|;
specifier|private
specifier|final
name|IndexingContext
name|context
decl_stmt|;
specifier|private
name|boolean
name|executeOnEntireRepo
init|=
literal|true
decl_stmt|;
comment|/**      * @since 1.4-M1      */
specifier|private
name|boolean
name|onlyUpdate
init|=
literal|false
decl_stmt|;
specifier|public
name|ArtifactIndexingTask
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|File
name|resourceFile
parameter_list|,
name|Action
name|action
parameter_list|,
name|IndexingContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|resourceFile
operator|=
name|resourceFile
expr_stmt|;
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|ArtifactIndexingTask
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|File
name|resourceFile
parameter_list|,
name|Action
name|action
parameter_list|,
name|IndexingContext
name|context
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|this
argument_list|(
name|repository
argument_list|,
name|resourceFile
argument_list|,
name|action
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|executeOnEntireRepo
operator|=
name|executeOnEntireRepo
expr_stmt|;
block|}
specifier|public
name|ArtifactIndexingTask
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|File
name|resourceFile
parameter_list|,
name|Action
name|action
parameter_list|,
name|IndexingContext
name|context
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|,
name|boolean
name|onlyUpdate
parameter_list|)
block|{
name|this
argument_list|(
name|repository
argument_list|,
name|resourceFile
argument_list|,
name|action
argument_list|,
name|context
argument_list|,
name|executeOnEntireRepo
argument_list|)
expr_stmt|;
name|this
operator|.
name|onlyUpdate
operator|=
name|onlyUpdate
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExecuteOnEntireRepo
parameter_list|()
block|{
return|return
name|executeOnEntireRepo
return|;
block|}
specifier|public
name|void
name|setExecuteOnEntireRepo
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|this
operator|.
name|executeOnEntireRepo
operator|=
name|executeOnEntireRepo
expr_stmt|;
block|}
specifier|public
name|long
name|getMaxExecutionTime
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|File
name|getResourceFile
parameter_list|()
block|{
return|return
name|resourceFile
return|;
block|}
specifier|public
name|Action
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
specifier|public
name|ManagedRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|IndexingContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|boolean
name|isOnlyUpdate
parameter_list|()
block|{
return|return
name|onlyUpdate
return|;
block|}
specifier|public
name|void
name|setOnlyUpdate
parameter_list|(
name|boolean
name|onlyUpdate
parameter_list|)
block|{
name|this
operator|.
name|onlyUpdate
operator|=
name|onlyUpdate
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|action
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|repository
operator|.
name|getId
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|resourceFile
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|resourceFile
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ArtifactIndexingTask
name|other
init|=
operator|(
name|ArtifactIndexingTask
operator|)
name|obj
decl_stmt|;
if|if
condition|(
operator|!
name|action
operator|.
name|equals
argument_list|(
name|other
operator|.
name|action
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|repository
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|resourceFile
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|resourceFile
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|resourceFile
operator|.
name|equals
argument_list|(
name|other
operator|.
name|resourceFile
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ArtifactIndexingTask [action="
operator|+
name|action
operator|+
literal|", repositoryId="
operator|+
name|repository
operator|.
name|getId
argument_list|()
operator|+
literal|", resourceFile="
operator|+
name|resourceFile
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

