begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Arrays
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

begin_comment
comment|/**  * VersionComparatorTest   *  *  */
end_comment

begin_class
specifier|public
class|class
name|VersionComparatorTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testComparator
parameter_list|()
block|{
comment|/* Sort order is oldest to newest */
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.0"
block|,
literal|"3.0"
block|,
literal|"2.0"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0"
block|,
literal|"2.0"
block|,
literal|"3.0"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.5"
block|,
literal|"1.2"
block|,
literal|"1.0"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0"
block|,
literal|"1.2"
block|,
literal|"1.5"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.5-SNAPSHOT"
block|,
literal|"1.2"
block|,
literal|"1.20"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.2"
block|,
literal|"1.5-SNAPSHOT"
block|,
literal|"1.20"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.1"
block|,
literal|"1.0-SNAPSHOT"
block|,
literal|"1.1-m6"
block|,
literal|"1.1-rc1"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0-SNAPSHOT"
block|,
literal|"1.1-rc1"
block|,
literal|"1.1-m6"
block|,
literal|"1.1"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.1-m6"
block|,
literal|"1.0-SNAPSHOT"
block|,
literal|"1.1-rc1"
block|,
literal|"1.1"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0-SNAPSHOT"
block|,
literal|"1.1-rc1"
block|,
literal|"1.1-m6"
block|,
literal|"1.1"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"2.0.5"
block|,
literal|"2.0.4-SNAPSHOT"
block|,
literal|"2.0"
block|,
literal|"2.0-rc1"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"2.0-rc1"
block|,
literal|"2.0"
block|,
literal|"2.0.4-SNAPSHOT"
block|,
literal|"2.0.5"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.0-alpha-1"
block|,
literal|"1.0-alpha-22"
block|,
literal|"1.0-alpha-10"
block|,
literal|"1.0-alpha-9"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0-alpha-1"
block|,
literal|"1.0-alpha-9"
block|,
literal|"1.0-alpha-10"
block|,
literal|"1.0-alpha-22"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.0-alpha1"
block|,
literal|"1.0-alpha22"
block|,
literal|"1.0-alpha10"
block|,
literal|"1.0-alpha9"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0-alpha1"
block|,
literal|"1.0-alpha9"
block|,
literal|"1.0-alpha10"
block|,
literal|"1.0-alpha22"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.0-1"
block|,
literal|"1.0-22"
block|,
literal|"1.0-10"
block|,
literal|"1.0-9"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0-1"
block|,
literal|"1.0-9"
block|,
literal|"1.0-10"
block|,
literal|"1.0-22"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"alpha-1"
block|,
literal|"alpha-22"
block|,
literal|"alpha-10"
block|,
literal|"alpha-9"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"alpha-1"
block|,
literal|"alpha-9"
block|,
literal|"alpha-10"
block|,
literal|"alpha-22"
block|}
argument_list|)
expr_stmt|;
name|assertSort
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.0.1"
block|,
literal|"1.0.22"
block|,
literal|"1.0.10"
block|,
literal|"1.0.9"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0.1"
block|,
literal|"1.0.9"
block|,
literal|"1.0.10"
block|,
literal|"1.0.22"
block|}
argument_list|)
expr_stmt|;
comment|// TODO: write more unit tests.
block|}
specifier|private
name|void
name|assertSort
parameter_list|(
name|String
index|[]
name|rawVersions
parameter_list|,
name|String
index|[]
name|expectedSort
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|versions
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|rawVersions
argument_list|)
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|versions
argument_list|,
name|VersionComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Versions.size()"
argument_list|,
name|expectedSort
operator|.
name|length
argument_list|,
name|versions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedSort
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Sorted Versions["
operator|+
name|i
operator|+
literal|"]"
argument_list|,
name|expectedSort
index|[
name|i
index|]
argument_list|,
operator|(
name|String
operator|)
name|versions
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testToParts
parameter_list|()
block|{
name|assertParts
argument_list|(
literal|"1.0"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1"
block|,
literal|"0"
block|}
argument_list|)
expr_stmt|;
name|assertParts
argument_list|(
literal|"1.0-alpha-1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1"
block|,
literal|"0"
block|,
literal|"alpha"
block|,
literal|"1"
block|}
argument_list|)
expr_stmt|;
name|assertParts
argument_list|(
literal|"2.0-rc2"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"2"
block|,
literal|"0"
block|,
literal|"rc"
block|,
literal|"2"
block|}
argument_list|)
expr_stmt|;
name|assertParts
argument_list|(
literal|"1.3-m6"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1"
block|,
literal|"3"
block|,
literal|"m"
block|,
literal|"6"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertParts
parameter_list|(
name|String
name|version
parameter_list|,
name|String
index|[]
name|expectedParts
parameter_list|)
block|{
name|String
name|actualParts
index|[]
init|=
name|VersionComparator
operator|.
name|toParts
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parts.length"
argument_list|,
name|expectedParts
operator|.
name|length
argument_list|,
name|actualParts
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedParts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"parts["
operator|+
name|i
operator|+
literal|"]"
argument_list|,
name|expectedParts
index|[
name|i
index|]
argument_list|,
name|actualParts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

