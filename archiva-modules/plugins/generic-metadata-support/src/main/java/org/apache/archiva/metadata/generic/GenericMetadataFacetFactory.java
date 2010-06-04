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
name|generic
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
comment|/**  * @plexus.component role="org.apache.archiva.metadata.model.MetadataFacetFactory"  *                   role-hint="org.apache.archiva.metadata.generic"  */
end_comment

begin_class
specifier|public
class|class
name|GenericMetadataFacetFactory
implements|implements
name|MetadataFacetFactory
block|{
specifier|public
name|MetadataFacet
name|createMetadataFacet
parameter_list|()
block|{
return|return
operator|new
name|GenericMetadataFacet
argument_list|()
return|;
block|}
specifier|public
name|MetadataFacet
name|createMetadataFacet
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"There is no valid name for project version facets"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

