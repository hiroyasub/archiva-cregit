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
name|Version
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

begin_class
specifier|public
class|class
name|ArchivaVersion
extends|extends
name|ArchivaContentItem
implements|implements
name|Version
block|{
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|StorageAsset
name|asset
decl_stmt|;
specifier|private
name|Project
name|project
decl_stmt|;
specifier|private
name|ArchivaVersion
parameter_list|()
block|{
block|}
specifier|public
specifier|static
name|ProjectBuilder
name|withVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|( )
operator|.
name|withVersion
argument_list|(
name|version
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getVersion
parameter_list|( )
block|{
return|return
name|version
return|;
block|}
annotation|@
name|Override
specifier|public
name|StorageAsset
name|getAsset
parameter_list|( )
block|{
return|return
name|asset
return|;
block|}
annotation|@
name|Override
specifier|public
name|Project
name|getProject
parameter_list|( )
block|{
return|return
name|project
return|;
block|}
specifier|public
interface|interface
name|ProjectBuilder
block|{
name|Builder
name|withProject
parameter_list|(
name|Project
name|project
parameter_list|)
function_decl|;
block|}
specifier|public
specifier|static
specifier|final
class|class
name|Builder
implements|implements
name|ProjectBuilder
block|{
specifier|private
name|ArchivaVersion
name|version
init|=
operator|new
name|ArchivaVersion
argument_list|()
decl_stmt|;
name|ProjectBuilder
name|withVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|version
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Version parameter must not be empty or null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|version
operator|.
name|version
operator|=
name|version
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|withProject
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|this
operator|.
name|version
operator|.
name|project
operator|=
name|project
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withAsset
parameter_list|(
name|StorageAsset
name|asset
parameter_list|)
block|{
name|this
operator|.
name|version
operator|.
name|asset
operator|=
name|asset
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withAttribute
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|version
operator|.
name|putAttribute
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ArchivaVersion
name|build
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|version
operator|.
name|asset
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|version
operator|.
name|project
operator|.
name|getRepository
argument_list|( )
operator|.
name|getRepository
argument_list|( )
operator|.
name|getAsset
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|version
return|;
block|}
block|}
block|}
end_class

end_unit

