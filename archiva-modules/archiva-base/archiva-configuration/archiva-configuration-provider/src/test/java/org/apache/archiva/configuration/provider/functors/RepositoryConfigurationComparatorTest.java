begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|provider
operator|.
name|functors
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|model
operator|.
name|AbstractRepositoryConfiguration
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
name|model
operator|.
name|ManagedRepositoryConfiguration
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
name|model
operator|.
name|functors
operator|.
name|RepositoryConfigurationComparator
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
name|test
operator|.
name|utils
operator|.
name|ArchivaBlockJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Test the repositry comparator.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|RepositoryConfigurationComparatorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testComparator
parameter_list|()
block|{
name|Comparator
argument_list|<
name|AbstractRepositoryConfiguration
argument_list|>
name|comparator
init|=
operator|new
name|RepositoryConfigurationComparator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
name|createRepository
argument_list|(
literal|"id"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
literal|null
argument_list|,
name|createRepository
argument_list|(
literal|"id"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
name|createRepository
argument_list|(
literal|"id1"
argument_list|)
argument_list|,
name|createRepository
argument_list|(
literal|"id1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
name|createRepository
argument_list|(
literal|"id1"
argument_list|)
argument_list|,
name|createRepository
argument_list|(
literal|"id2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
name|createRepository
argument_list|(
literal|"id2"
argument_list|)
argument_list|,
name|createRepository
argument_list|(
literal|"id1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|repo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
block|}
end_class

end_unit

