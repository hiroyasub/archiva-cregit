begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|base
operator|.
name|builder
operator|.
name|OptBuilder
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
name|mock
operator|.
name|ManagedRepositoryContentMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
class|class
name|ArchivaProjectTest
extends|extends
name|ContentItemTest
block|{
name|Namespace
name|namespace
decl_stmt|;
annotation|@
name|BeforeEach
name|void
name|init
parameter_list|()
block|{
name|namespace
operator|=
name|ArchivaNamespace
operator|.
name|withRepository
argument_list|(
name|repository
argument_list|)
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
literal|"test.namespace.123"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|OptBuilder
name|getBuilder
parameter_list|( )
block|{
return|return
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
name|namespace
argument_list|)
operator|.
name|withId
argument_list|(
literal|"abcde"
argument_list|)
return|;
block|}
annotation|@
name|Test
name|void
name|withNamespaceAndId
parameter_list|()
block|{
name|ArchivaProject
name|item
init|=
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
name|namespace
argument_list|)
operator|.
name|withId
argument_list|(
literal|"abcdefg"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|assertNotNull
argument_list|(
name|item
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abcdefg"
argument_list|,
name|item
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|item
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|namespace
argument_list|,
name|item
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|item
operator|.
name|getRepository
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|repository
argument_list|,
name|item
operator|.
name|getRepository
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|item
operator|.
name|getAsset
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|asset
argument_list|,
name|item
operator|.
name|getAsset
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|illegalNamespace
parameter_list|()
block|{
name|WithNamespaceObjectBuilder
name|builder
init|=
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
decl_stmt|;
name|assertThrows
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
parameter_list|( )
lambda|->
name|builder
operator|.
name|withNamespace
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|illegalId
parameter_list|()
block|{
name|ProjectWithIdBuilder
name|builder
init|=
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
name|assertThrows
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
parameter_list|( )
lambda|->
name|builder
operator|.
name|withId
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertThrows
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
parameter_list|( )
lambda|->
name|builder
operator|.
name|withId
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|equalityTests
parameter_list|()
block|{
name|ArchivaProject
name|item1
init|=
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
name|namespace
argument_list|)
operator|.
name|withId
argument_list|(
literal|"abcdefgtest1"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|ArchivaProject
name|item2
init|=
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
name|namespace
argument_list|)
operator|.
name|withId
argument_list|(
literal|"abcdefgtest2"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|ArchivaProject
name|item3
init|=
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
name|namespace
argument_list|)
operator|.
name|withId
argument_list|(
literal|"abcdefgtest1"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|ArchivaNamespace
name|ns2
init|=
name|ArchivaNamespace
operator|.
name|withRepository
argument_list|(
name|repository
argument_list|)
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
literal|"test.namespace.123"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|ArchivaProject
name|item4
init|=
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
name|ns2
argument_list|)
operator|.
name|withId
argument_list|(
literal|"abcdefgtest1"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|ArchivaNamespace
name|ns3
init|=
name|ArchivaNamespace
operator|.
name|withRepository
argument_list|(
name|repository
argument_list|)
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
literal|"test.namespace.1234"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|ArchivaProject
name|item5
init|=
name|ArchivaProject
operator|.
name|withAsset
argument_list|(
name|asset
argument_list|)
operator|.
name|withNamespace
argument_list|(
name|ns3
argument_list|)
operator|.
name|withId
argument_list|(
literal|"abcdefgtest1"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|assertNotEquals
argument_list|(
name|item1
argument_list|,
name|item2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|item1
argument_list|,
name|item3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|item1
argument_list|,
name|item4
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|item1
argument_list|,
name|item5
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

