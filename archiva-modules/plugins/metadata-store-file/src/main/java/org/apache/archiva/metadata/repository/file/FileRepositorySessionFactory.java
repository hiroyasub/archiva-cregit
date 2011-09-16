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
name|repository
operator|.
name|file
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|MetadataFacetFactory
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
name|repository
operator|.
name|MetadataRepository
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
name|repository
operator|.
name|MetadataResolver
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
name|repository
operator|.
name|RepositorySession
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
name|repository
operator|.
name|RepositorySessionFactory
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
name|lang
operator|.
name|StringUtils
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
name|configuration
operator|.
name|ArchivaConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositorySessionFactory#file"
argument_list|)
specifier|public
class|class
name|FileRepositorySessionFactory
implements|implements
name|RepositorySessionFactory
block|{
comment|/**      *      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|metadataFacetFactories
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration#default"
argument_list|)
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
specifier|private
name|MetadataResolver
name|metadataResolver
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|tmpMetadataFacetFactories
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|MetadataFacetFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// olamy with spring the "id" is now "metadataFacetFactory#hint"
comment|// whereas was only hint with plexus so let remove  metadataFacetFactory#
name|metadataFacetFactories
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
argument_list|(
name|tmpMetadataFacetFactories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|entry
range|:
name|tmpMetadataFacetFactories
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|metadataFacetFactories
operator|.
name|put
argument_list|(
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"#"
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|RepositorySession
name|createSession
parameter_list|()
block|{
name|MetadataRepository
name|metadataRepository
init|=
operator|new
name|FileMetadataRepository
argument_list|(
name|metadataFacetFactories
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
return|return
operator|new
name|RepositorySession
argument_list|(
name|metadataRepository
argument_list|,
name|metadataResolver
argument_list|)
return|;
block|}
block|}
end_class

end_unit

