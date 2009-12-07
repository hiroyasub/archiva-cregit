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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|util
operator|.
name|Collection
import|;
end_import

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
name|List
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
name|MailingList
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
name|ProjectVersionMetadata
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
name|io
operator|.
name|FileUtils
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
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_class
specifier|public
class|class
name|FileMetadataRepositoryTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|FileMetadataRepository
name|repository
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"test"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_PROJECT
init|=
literal|"projectId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_NAMESPACE
init|=
literal|"namespace"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_PROJECT_VERSION
init|=
literal|"1.0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_FACET_ID
init|=
literal|"test-facet-id"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_NAME
init|=
literal|"test-name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VALUE
init|=
literal|"test-value"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|UNKNOWN
init|=
literal|"unknown"
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|repository
operator|=
operator|new
name|FileMetadataRepository
argument_list|()
expr_stmt|;
name|File
name|directory
init|=
name|getTestFile
argument_list|(
literal|"target/test-repository"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setMetadataFacetFactories
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|MetadataFacetFactory
operator|>
name|singletonMap
argument_list|(
name|TEST_FACET_ID
argument_list|,
operator|new
name|MetadataFacetFactory
argument_list|()
block|{
specifier|public
name|MetadataFacet
name|createMetadataFacet
parameter_list|()
block|{
return|return
operator|new
name|TestMetadataFacet
argument_list|(
literal|"test-metadata"
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRootNamespaceWithNoMetadataRepository
parameter_list|()
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
name|repository
operator|.
name|getRootNamespaces
argument_list|(
name|TEST_REPO_ID
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateProjectVersionMetadataWithNoOtherArchives
parameter_list|()
block|{
name|ProjectVersionMetadata
name|metadata
init|=
operator|new
name|ProjectVersionMetadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setId
argument_list|(
name|TEST_PROJECT_VERSION
argument_list|)
expr_stmt|;
name|MailingList
name|mailingList
init|=
operator|new
name|MailingList
argument_list|()
decl_stmt|;
name|mailingList
operator|.
name|setName
argument_list|(
literal|"Foo List"
argument_list|)
expr_stmt|;
name|mailingList
operator|.
name|setOtherArchives
argument_list|(
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setMailingLists
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|mailingList
argument_list|)
argument_list|)
expr_stmt|;
name|repository
operator|.
name|updateProjectVersion
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_NAMESPACE
argument_list|,
name|TEST_PROJECT
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateProjectVersionMetadataWithExistingFacets
parameter_list|()
block|{
name|ProjectVersionMetadata
name|metadata
init|=
operator|new
name|ProjectVersionMetadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setId
argument_list|(
name|TEST_PROJECT_VERSION
argument_list|)
expr_stmt|;
name|MetadataFacet
name|facet
init|=
operator|new
name|TestMetadataFacet
argument_list|(
literal|"baz"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addFacet
argument_list|(
name|facet
argument_list|)
expr_stmt|;
name|repository
operator|.
name|updateProjectVersion
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_NAMESPACE
argument_list|,
name|TEST_PROJECT
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
name|metadata
operator|=
name|repository
operator|.
name|getProjectVersion
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_NAMESPACE
argument_list|,
name|TEST_PROJECT
argument_list|,
name|TEST_PROJECT_VERSION
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|TEST_FACET_ID
argument_list|)
argument_list|,
name|metadata
operator|.
name|getFacetIds
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|=
operator|new
name|ProjectVersionMetadata
argument_list|()
expr_stmt|;
name|metadata
operator|.
name|setId
argument_list|(
name|TEST_PROJECT_VERSION
argument_list|)
expr_stmt|;
name|repository
operator|.
name|updateProjectVersion
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_NAMESPACE
argument_list|,
name|TEST_PROJECT
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
name|metadata
operator|=
name|repository
operator|.
name|getProjectVersion
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_NAMESPACE
argument_list|,
name|TEST_PROJECT
argument_list|,
name|TEST_PROJECT_VERSION
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|TEST_FACET_ID
argument_list|)
argument_list|,
name|metadata
operator|.
name|getFacetIds
argument_list|()
argument_list|)
expr_stmt|;
name|TestMetadataFacet
name|testFacet
init|=
operator|(
name|TestMetadataFacet
operator|)
name|metadata
operator|.
name|getFacet
argument_list|(
name|TEST_FACET_ID
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
name|testFacet
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMetadataFacet
parameter_list|()
block|{
name|repository
operator|.
name|addMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|,
name|TEST_NAME
argument_list|,
operator|new
name|TestMetadataFacet
argument_list|(
name|TEST_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|TestMetadataFacet
argument_list|(
name|TEST_VALUE
argument_list|)
argument_list|,
name|repository
operator|.
name|getMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|,
name|TEST_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMetadataFacetWhenEmpty
parameter_list|()
block|{
name|assertNull
argument_list|(
name|repository
operator|.
name|getMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|,
name|TEST_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMetadataFacetWhenUnknownName
parameter_list|()
block|{
name|repository
operator|.
name|addMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|,
name|TEST_NAME
argument_list|,
operator|new
name|TestMetadataFacet
argument_list|(
name|TEST_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|repository
operator|.
name|getMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|,
name|UNKNOWN
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMetadataFacetWhenDefaultValue
parameter_list|()
block|{
name|repository
operator|.
name|addMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|,
name|TEST_NAME
argument_list|,
operator|new
name|TestMetadataFacet
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|TestMetadataFacet
argument_list|(
literal|"test-metadata"
argument_list|)
argument_list|,
name|repository
operator|.
name|getMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|,
name|TEST_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMetadataFacetWhenUnknownFacetId
parameter_list|()
block|{
name|repository
operator|.
name|addMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|UNKNOWN
argument_list|,
name|TEST_NAME
argument_list|,
operator|new
name|TestMetadataFacet
argument_list|(
name|TEST_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|repository
operator|.
name|getMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|UNKNOWN
argument_list|,
name|TEST_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMetadataFacets
parameter_list|()
block|{
name|repository
operator|.
name|addMetadataFacet
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|,
name|TEST_NAME
argument_list|,
operator|new
name|TestMetadataFacet
argument_list|(
name|TEST_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|TEST_NAME
argument_list|)
argument_list|,
name|repository
operator|.
name|getMetadataFacets
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMetadataFacetsWhenEmpty
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|facets
init|=
name|repository
operator|.
name|getMetadataFacets
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_FACET_ID
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|facets
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|TestMetadataFacet
implements|implements
name|MetadataFacet
block|{
specifier|private
name|TestMetadataFacet
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|private
name|String
name|value
decl_stmt|;
specifier|public
name|String
name|getFacetId
parameter_list|()
block|{
return|return
name|TEST_FACET_ID
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toProperties
parameter_list|()
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|TEST_FACET_ID
operator|+
literal|":foo"
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|fromProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|String
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|TEST_FACET_ID
operator|+
literal|":foo"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
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
literal|"TestMetadataFacet{"
operator|+
literal|"value='"
operator|+
name|value
operator|+
literal|'\''
operator|+
literal|'}'
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
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|TestMetadataFacet
name|that
init|=
operator|(
name|TestMetadataFacet
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|?
operator|!
name|value
operator|.
name|equals
argument_list|(
name|that
operator|.
name|value
argument_list|)
else|:
name|that
operator|.
name|value
operator|!=
literal|null
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
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|hashCode
argument_list|()
else|:
literal|0
return|;
block|}
block|}
block|}
end_class

end_unit

