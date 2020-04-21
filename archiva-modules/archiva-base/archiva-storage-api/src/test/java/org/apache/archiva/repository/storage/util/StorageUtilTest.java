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
name|storage
operator|.
name|util
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
name|archiva
operator|.
name|repository
operator|.
name|storage
operator|.
name|mock
operator|.
name|MockAsset
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
name|mock
operator|.
name|MockStorage
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
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|StorageUtilTest
block|{
specifier|private
specifier|static
name|int
name|LEVEL1
init|=
literal|12
decl_stmt|;
specifier|private
specifier|static
name|int
name|LEVEL2
init|=
literal|13
decl_stmt|;
specifier|private
specifier|static
name|int
name|LEVEL3
init|=
literal|6
decl_stmt|;
specifier|private
name|MockAsset
name|createTree
parameter_list|()
block|{
return|return
name|createTree
argument_list|(
name|LEVEL1
argument_list|,
name|LEVEL2
argument_list|,
name|LEVEL3
argument_list|)
return|;
block|}
specifier|private
name|MockAsset
name|createTree
parameter_list|(
name|int
modifier|...
name|levelElements
parameter_list|)
block|{
name|MockAsset
name|root
init|=
operator|new
name|MockAsset
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|recurseSubTree
argument_list|(
name|root
argument_list|,
literal|0
argument_list|,
name|levelElements
argument_list|)
expr_stmt|;
return|return
name|root
return|;
block|}
specifier|private
name|void
name|recurseSubTree
parameter_list|(
name|MockAsset
name|parent
parameter_list|,
name|int
name|level
parameter_list|,
name|int
index|[]
name|levelElements
parameter_list|)
block|{
if|if
condition|(
name|level
operator|<
name|levelElements
operator|.
name|length
condition|)
block|{
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|levelElements
index|[
name|level
index|]
condition|;
name|k
operator|++
control|)
block|{
name|String
name|name
init|=
name|parent
operator|.
name|getName
argument_list|( )
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"%03d"
argument_list|,
name|k
argument_list|)
decl_stmt|;
name|MockAsset
name|asset
init|=
operator|new
name|MockAsset
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|recurseSubTree
argument_list|(
name|asset
argument_list|,
name|level
operator|+
literal|1
argument_list|,
name|levelElements
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
name|void
name|testWalkFromRoot
parameter_list|()
block|{
name|StorageAsset
name|root
init|=
name|createTree
argument_list|( )
decl_stmt|;
name|ConsumeVisitStatus
name|status
init|=
operator|new
name|ConsumeVisitStatus
argument_list|( )
decl_stmt|;
name|StorageUtil
operator|.
name|walk
argument_list|(
name|root
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
name|LEVEL1
operator|*
name|LEVEL2
operator|*
name|LEVEL3
operator|+
name|LEVEL1
operator|*
name|LEVEL2
operator|+
name|LEVEL1
operator|+
literal|1
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|status
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|StorageAsset
name|first
init|=
name|root
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|list
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|first
argument_list|,
name|status
operator|.
name|getFirst
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|root
argument_list|,
name|status
operator|.
name|getLast
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testWalkFromChild
parameter_list|()
block|{
name|StorageAsset
name|root
init|=
name|createTree
argument_list|( )
decl_stmt|;
name|ConsumeVisitStatus
name|status
init|=
operator|new
name|ConsumeVisitStatus
argument_list|( )
decl_stmt|;
name|StorageAsset
name|testRoot
init|=
name|root
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|StorageUtil
operator|.
name|walk
argument_list|(
name|testRoot
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
name|LEVEL2
operator|*
name|LEVEL3
operator|+
name|LEVEL2
operator|+
literal|1
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|status
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|StorageAsset
name|first
init|=
name|root
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|list
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|first
argument_list|,
name|status
operator|.
name|getFirst
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testRoot
argument_list|,
name|status
operator|.
name|getLast
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testWalkFromRootWithCondition
parameter_list|()
block|{
name|StorageAsset
name|root
init|=
name|createTree
argument_list|( )
decl_stmt|;
name|StopVisitStatus
name|status
init|=
operator|new
name|StopVisitStatus
argument_list|( )
decl_stmt|;
name|status
operator|.
name|setStopCondition
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"001002003"
argument_list|)
argument_list|)
expr_stmt|;
name|StorageUtil
operator|.
name|walk
argument_list|(
name|root
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"001002003"
argument_list|,
name|status
operator|.
name|getLast
argument_list|( )
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
name|LEVEL2
operator|*
name|LEVEL3
operator|+
name|LEVEL2
operator|+
literal|2
operator|*
name|LEVEL3
operator|+
literal|1
operator|+
literal|1
operator|+
literal|1
operator|+
literal|4
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|status
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testStream
parameter_list|()
block|{
name|StorageAsset
name|root
init|=
name|createTree
argument_list|( )
decl_stmt|;
name|ConsumeVisitStatus
name|status
init|=
operator|new
name|ConsumeVisitStatus
argument_list|( )
decl_stmt|;
name|List
argument_list|<
name|StorageAsset
argument_list|>
name|result
decl_stmt|;
try|try
init|(
name|Stream
argument_list|<
name|StorageAsset
argument_list|>
name|stream
init|=
name|StorageUtil
operator|.
name|newAssetStream
argument_list|(
name|root
argument_list|,
literal|false
argument_list|)
init|)
block|{
name|result
operator|=
name|stream
operator|.
name|filter
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getName
argument_list|( )
operator|.
name|startsWith
argument_list|(
literal|"001"
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|expected
init|=
name|LEVEL2
operator|*
name|LEVEL3
operator|+
name|LEVEL2
operator|+
literal|1
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|result
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"001"
argument_list|,
name|result
operator|.
name|get
argument_list|(
name|result
operator|.
name|size
argument_list|( )
operator|-
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"001012"
argument_list|,
name|result
operator|.
name|get
argument_list|(
name|result
operator|.
name|size
argument_list|( )
operator|-
literal|2
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testStreamParallel
parameter_list|()
block|{
name|StorageAsset
name|root
init|=
name|createTree
argument_list|( )
decl_stmt|;
name|ConsumeVisitStatus
name|status
init|=
operator|new
name|ConsumeVisitStatus
argument_list|( )
decl_stmt|;
name|List
argument_list|<
name|StorageAsset
argument_list|>
name|result
decl_stmt|;
try|try
init|(
name|Stream
argument_list|<
name|StorageAsset
argument_list|>
name|stream
init|=
name|StorageUtil
operator|.
name|newAssetStream
argument_list|(
name|root
argument_list|,
literal|true
argument_list|)
init|)
block|{
name|result
operator|=
name|stream
operator|.
name|filter
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getName
argument_list|( )
operator|.
name|startsWith
argument_list|(
literal|"001"
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|expected
init|=
name|LEVEL2
operator|*
name|LEVEL3
operator|+
name|LEVEL2
operator|+
literal|1
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|result
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testDelete
parameter_list|()
throws|throws
name|IOException
block|{
name|MockAsset
name|root
init|=
name|createTree
argument_list|( )
decl_stmt|;
name|MockStorage
name|storage
init|=
operator|new
name|MockStorage
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|StorageUtil
operator|.
name|deleteRecursively
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
name|LEVEL1
operator|*
name|LEVEL2
operator|*
name|LEVEL3
operator|+
name|LEVEL1
operator|*
name|LEVEL2
operator|+
name|LEVEL1
operator|+
literal|1
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|storage
operator|.
name|getStatus
argument_list|( )
operator|.
name|size
argument_list|(
name|MockStorage
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testDeleteWithException
parameter_list|()
throws|throws
name|IOException
block|{
name|MockAsset
name|root
init|=
name|createTree
argument_list|( )
decl_stmt|;
name|MockStorage
name|storage
init|=
operator|new
name|MockStorage
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|root
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|setThrowException
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|StorageUtil
operator|.
name|deleteRecursively
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
name|LEVEL1
operator|*
name|LEVEL2
operator|*
name|LEVEL3
operator|+
name|LEVEL1
operator|*
name|LEVEL2
operator|+
name|LEVEL1
operator|+
literal|1
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|storage
operator|.
name|getStatus
argument_list|( )
operator|.
name|size
argument_list|(
name|MockStorage
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|testDeleteWithExceptionFailFast
parameter_list|()
throws|throws
name|IOException
block|{
name|MockAsset
name|root
init|=
name|createTree
argument_list|( )
decl_stmt|;
name|MockStorage
name|storage
init|=
operator|new
name|MockStorage
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|root
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|list
argument_list|( )
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|setThrowException
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|StorageUtil
operator|.
name|deleteRecursively
argument_list|(
name|root
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
literal|113
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|storage
operator|.
name|getStatus
argument_list|( )
operator|.
name|size
argument_list|(
name|MockStorage
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
