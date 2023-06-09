begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|facets
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|MetadataFacet
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
name|metadata
operator|.
name|model
operator|.
name|MetadataFacetFactory
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMetadataFacetFactory
parameter_list|<
name|T
extends|extends
name|MetadataFacet
parameter_list|>
implements|implements
name|MetadataFacetFactory
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|final
name|String
name|facetId
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|facetClazz
decl_stmt|;
specifier|protected
name|AbstractMetadataFacetFactory
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|facetClazz
parameter_list|,
name|String
name|facetId
parameter_list|)
block|{
name|this
operator|.
name|facetId
operator|=
name|facetId
expr_stmt|;
name|this
operator|.
name|facetClazz
operator|=
name|facetClazz
expr_stmt|;
block|}
specifier|protected
name|AbstractMetadataFacetFactory
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|facetClazz
parameter_list|)
block|{
name|this
operator|.
name|facetClazz
operator|=
name|facetClazz
expr_stmt|;
try|try
block|{
name|this
operator|.
name|facetId
operator|=
operator|(
name|String
operator|)
name|this
operator|.
name|facetClazz
operator|.
name|getField
argument_list|(
literal|"FACET_ID"
argument_list|)
operator|.
name|get
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"There is no FACET_ID static public field on the class "
operator|+
name|facetClazz
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|T
name|createMetadataFacet
parameter_list|( )
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|T
name|createMetadataFacet
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|T
argument_list|>
name|getFacetClass
parameter_list|( )
block|{
return|return
name|facetClazz
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getFacetId
parameter_list|( )
block|{
return|return
name|facetId
return|;
block|}
block|}
end_class

end_unit

