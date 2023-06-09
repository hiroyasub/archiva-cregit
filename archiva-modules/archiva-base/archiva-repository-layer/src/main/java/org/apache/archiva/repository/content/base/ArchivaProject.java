begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|base
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
name|repository
operator|.
name|ManagedRepositoryContent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|Namespace
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|base
operator|.
name|builder
operator|.
name|ProjectOptBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|base
operator|.
name|builder
operator|.
name|ProjectWithIdBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|base
operator|.
name|builder
operator|.
name|WithAssetBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|base
operator|.
name|builder
operator|.
name|WithNamespaceObjectBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|storage
operator|.
name|StorageAsset
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
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Immutable class, that represents a project.  *<p>  * The namespace and id are required attributes for each instance.  *<p>  * Two project instances are equal if the id, and the namespace are equal and if the base attributes  * repository and asset match.  *  * @author Martin Stockhammer<martin_s@apache.org>  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaProject
extends|extends
name|BaseContentItem
implements|implements
name|Project
block|{
specifier|private
name|Namespace
name|namespace
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
comment|// Setting all setters to private. Builder is the way to go.
specifier|private
name|ArchivaProject
parameter_list|( )
block|{
block|}
comment|/**      * Creates the builder for creating new archiva project instances.      * You have to set all required attributes before you can call the build() method.      *      * @param storageAsset the asset      * @return a builder instance      */
specifier|public
specifier|static
name|WithNamespaceObjectBuilder
name|withAsset
parameter_list|(
name|StorageAsset
name|storageAsset
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|( )
operator|.
name|withAsset
argument_list|(
name|storageAsset
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|WithAssetBuilder
argument_list|<
name|WithNamespaceObjectBuilder
argument_list|>
name|withRepository
parameter_list|(
name|ManagedRepositoryContent
name|repository
parameter_list|)
block|{
return|return
operator|new
name|ArchivaProject
operator|.
name|Builder
argument_list|( )
operator|.
name|withRepository
argument_list|(
name|repository
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Namespace
name|getNamespace
parameter_list|( )
block|{
return|return
name|this
operator|.
name|namespace
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|( )
block|{
return|return
name|this
operator|.
name|id
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
name|ArchivaProject
name|that
init|=
operator|(
name|ArchivaProject
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|repository
operator|.
name|equals
argument_list|(
name|that
operator|.
name|repository
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|namespace
operator|.
name|equals
argument_list|(
name|that
operator|.
name|namespace
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
name|id
operator|.
name|equals
argument_list|(
name|that
operator|.
name|id
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
name|super
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
name|namespace
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
name|id
operator|.
name|hashCode
argument_list|( )
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
return|return
literal|"ArchivaProject{ "
operator|+
name|id
operator|+
literal|", namespace="
operator|+
name|namespace
operator|.
name|toString
argument_list|()
operator|+
literal|"}"
return|;
block|}
comment|/*      * Builder class      */
specifier|public
specifier|static
specifier|final
class|class
name|Builder
extends|extends
name|ContentItemBuilder
argument_list|<
name|ArchivaProject
argument_list|,
name|ProjectOptBuilder
argument_list|,
name|WithNamespaceObjectBuilder
argument_list|>
implements|implements
name|ProjectOptBuilder
implements|,
name|ProjectWithIdBuilder
implements|,
name|WithNamespaceObjectBuilder
block|{
specifier|private
name|Builder
parameter_list|( )
block|{
name|super
argument_list|(
operator|new
name|ArchivaProject
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ProjectOptBuilder
name|getOptBuilder
parameter_list|( )
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|protected
name|WithNamespaceObjectBuilder
name|getNextBuilder
parameter_list|( )
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ProjectOptBuilder
name|withId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null or empty value not allowed for id"
argument_list|)
throw|;
block|}
name|item
operator|.
name|id
operator|=
name|id
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ProjectWithIdBuilder
name|withNamespace
parameter_list|(
name|Namespace
name|namespace
parameter_list|)
block|{
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null value not allowed for namespace"
argument_list|)
throw|;
block|}
name|item
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|super
operator|.
name|setRepository
argument_list|(
name|namespace
operator|.
name|getRepository
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ArchivaProject
name|build
parameter_list|( )
block|{
name|super
operator|.
name|build
argument_list|( )
expr_stmt|;
if|if
condition|(
name|item
operator|.
name|namespace
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Namespace may not be null"
argument_list|)
throw|;
block|}
return|return
name|item
return|;
block|}
block|}
block|}
end_class

end_unit

