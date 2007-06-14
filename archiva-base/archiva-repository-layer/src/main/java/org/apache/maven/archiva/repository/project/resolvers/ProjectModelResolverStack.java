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
name|repository
operator|.
name|project
operator|.
name|resolvers
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
name|collections
operator|.
name|CollectionUtils
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
name|ArchivaProjectModel
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
name|VersionedReference
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
name|repository
operator|.
name|project
operator|.
name|ProjectModelException
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
name|repository
operator|.
name|project
operator|.
name|ProjectModelResolver
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
name|Iterator
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
comment|/**  * Represents a stack of {@link ProjectModelResolver} resolvers for  * finding/resolving an ArchivaProjectModel from multiple sources.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ProjectModelResolverStack
block|{
specifier|private
name|List
name|resolvers
decl_stmt|;
specifier|private
name|List
name|listeners
decl_stmt|;
specifier|public
name|ProjectModelResolverStack
parameter_list|()
block|{
name|this
operator|.
name|resolvers
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addListener
parameter_list|(
name|ProjectModelResolutionListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addProjectModelResolver
parameter_list|(
name|ProjectModelResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|resolvers
operator|.
name|add
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearResolvers
parameter_list|()
block|{
name|this
operator|.
name|resolvers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ArchivaProjectModel
name|findProject
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|)
block|{
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|this
operator|.
name|resolvers
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No resolvers have been defined."
argument_list|)
throw|;
block|}
name|triggerResolutionStart
argument_list|(
name|projectRef
argument_list|,
name|this
operator|.
name|resolvers
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|this
operator|.
name|resolvers
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectModelResolver
name|resolver
init|=
operator|(
name|ProjectModelResolver
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|triggerResolutionAttempting
argument_list|(
name|projectRef
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|resolver
operator|.
name|resolveProjectModel
argument_list|(
name|projectRef
argument_list|)
decl_stmt|;
if|if
condition|(
name|model
operator|!=
literal|null
condition|)
block|{
comment|// Project was found.
name|triggerResolutionSuccess
argument_list|(
name|projectRef
argument_list|,
name|resolver
argument_list|,
name|model
argument_list|)
expr_stmt|;
return|return
name|model
return|;
block|}
name|triggerResolutionMiss
argument_list|(
name|projectRef
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProjectModelException
name|e
parameter_list|)
block|{
name|triggerResolutionError
argument_list|(
name|projectRef
argument_list|,
name|resolver
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Project was not found in entire resolver list.
name|triggerResolutionNotFound
argument_list|(
name|projectRef
argument_list|,
name|this
operator|.
name|resolvers
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|this
operator|.
name|resolvers
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|void
name|removeListener
parameter_list|(
name|ProjectModelResolutionListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeResolver
parameter_list|(
name|ProjectModelResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolvers
operator|.
name|remove
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|triggerResolutionAttempting
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|this
operator|.
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectModelResolutionListener
name|listener
init|=
operator|(
name|ProjectModelResolutionListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|resolutionAttempting
argument_list|(
name|projectRef
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing with exception.
block|}
block|}
block|}
specifier|private
name|void
name|triggerResolutionError
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|,
name|Exception
name|cause
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|this
operator|.
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectModelResolutionListener
name|listener
init|=
operator|(
name|ProjectModelResolutionListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|resolutionError
argument_list|(
name|projectRef
argument_list|,
name|resolver
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing with exception.
block|}
block|}
block|}
specifier|private
name|void
name|triggerResolutionMiss
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|this
operator|.
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectModelResolutionListener
name|listener
init|=
operator|(
name|ProjectModelResolutionListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|resolutionMiss
argument_list|(
name|projectRef
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing with exception.
block|}
block|}
block|}
specifier|private
name|void
name|triggerResolutionNotFound
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|List
name|resolvers
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|this
operator|.
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectModelResolutionListener
name|listener
init|=
operator|(
name|ProjectModelResolutionListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|resolutionNotFound
argument_list|(
name|projectRef
argument_list|,
name|resolvers
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing with exception.
block|}
block|}
block|}
specifier|private
name|void
name|triggerResolutionStart
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|List
name|resolvers
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|this
operator|.
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectModelResolutionListener
name|listener
init|=
operator|(
name|ProjectModelResolutionListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|resolutionStart
argument_list|(
name|projectRef
argument_list|,
name|resolvers
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing with exception.
block|}
block|}
block|}
specifier|private
name|void
name|triggerResolutionSuccess
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|,
name|ArchivaProjectModel
name|model
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|this
operator|.
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectModelResolutionListener
name|listener
init|=
operator|(
name|ProjectModelResolutionListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|resolutionSuccess
argument_list|(
name|projectRef
argument_list|,
name|resolver
argument_list|,
name|model
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing with exception.
block|}
block|}
block|}
block|}
end_class

end_unit

