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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Capability implementation.  */
end_comment

begin_class
specifier|public
class|class
name|StandardCapabilities
implements|implements
name|RepositoryCapabilities
block|{
specifier|private
specifier|final
name|Set
argument_list|<
name|ReleaseScheme
argument_list|>
name|supportedReleaseSchemes
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|ReleaseScheme
argument_list|>
name|uSupportedReleaseSchemes
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|supportedLayouts
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|uSupportedLayouts
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|customCapabilities
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|uCustomCapabilities
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|supportedFeatures
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|uSupportedFeatures
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|indexable
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|fileBased
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|canBlockRedeployments
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|scannable
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|allowsFailover
decl_stmt|;
specifier|public
name|StandardCapabilities
parameter_list|(
name|ReleaseScheme
index|[]
name|supportedReleaseSchemes
parameter_list|,
name|String
index|[]
name|supportedLayouts
parameter_list|,
name|String
index|[]
name|customCapabilities
parameter_list|,
name|String
index|[]
name|supportedFeatures
parameter_list|,
name|boolean
name|indexable
parameter_list|,
name|boolean
name|fileBased
parameter_list|,
name|boolean
name|canBlockRedeployments
parameter_list|,
name|boolean
name|scannable
parameter_list|,
name|boolean
name|allowsFailover
parameter_list|)
block|{
name|this
operator|.
name|supportedReleaseSchemes
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|ReleaseScheme
name|scheme
range|:
name|supportedReleaseSchemes
control|)
block|{
name|this
operator|.
name|supportedReleaseSchemes
operator|.
name|add
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|uSupportedReleaseSchemes
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|this
operator|.
name|supportedReleaseSchemes
argument_list|)
expr_stmt|;
name|this
operator|.
name|supportedLayouts
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(  )
expr_stmt|;
for|for
control|(
name|String
name|layout
range|:
name|supportedLayouts
control|)
block|{
name|this
operator|.
name|supportedLayouts
operator|.
name|add
argument_list|(
name|layout
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|uSupportedLayouts
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|this
operator|.
name|supportedLayouts
argument_list|)
expr_stmt|;
name|this
operator|.
name|customCapabilities
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(  )
expr_stmt|;
for|for
control|(
name|String
name|cap
range|:
name|customCapabilities
control|)
block|{
name|this
operator|.
name|customCapabilities
operator|.
name|add
argument_list|(
name|cap
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|uCustomCapabilities
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|this
operator|.
name|customCapabilities
argument_list|)
expr_stmt|;
name|this
operator|.
name|supportedFeatures
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(  )
expr_stmt|;
for|for
control|(
name|String
name|feature
range|:
name|supportedFeatures
control|)
block|{
name|this
operator|.
name|supportedFeatures
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|uSupportedFeatures
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|this
operator|.
name|supportedFeatures
argument_list|)
expr_stmt|;
name|this
operator|.
name|indexable
operator|=
name|indexable
expr_stmt|;
name|this
operator|.
name|fileBased
operator|=
name|fileBased
expr_stmt|;
name|this
operator|.
name|canBlockRedeployments
operator|=
name|canBlockRedeployments
expr_stmt|;
name|this
operator|.
name|scannable
operator|=
name|scannable
expr_stmt|;
name|this
operator|.
name|allowsFailover
operator|=
name|allowsFailover
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|ReleaseScheme
argument_list|>
name|supportedReleaseSchemes
parameter_list|( )
block|{
return|return
name|uSupportedReleaseSchemes
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|supportedLayouts
parameter_list|( )
block|{
return|return
name|uSupportedLayouts
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|customCapabilities
parameter_list|( )
block|{
return|return
name|uCustomCapabilities
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|supportedFeatures
parameter_list|( )
block|{
return|return
name|uSupportedFeatures
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isIndexable
parameter_list|( )
block|{
return|return
name|indexable
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isFileBased
parameter_list|( )
block|{
return|return
name|fileBased
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canBlockRedeployments
parameter_list|( )
block|{
return|return
name|canBlockRedeployments
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isScannable
parameter_list|( )
block|{
return|return
name|scannable
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|allowsFailover
parameter_list|( )
block|{
return|return
name|allowsFailover
return|;
block|}
block|}
end_class

end_unit

