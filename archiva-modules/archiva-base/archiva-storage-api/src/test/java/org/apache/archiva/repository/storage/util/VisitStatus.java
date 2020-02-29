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
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
class|class
name|VisitStatus
block|{
name|LinkedList
argument_list|<
name|StorageAsset
argument_list|>
name|visited
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|( )
decl_stmt|;
name|VisitStatus
parameter_list|( )
block|{
block|}
specifier|public
name|void
name|add
parameter_list|(
name|StorageAsset
name|asset
parameter_list|)
block|{
comment|// System.out.println( "Adding " + asset.getPath( ) );
name|visited
operator|.
name|addLast
argument_list|(
name|asset
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StorageAsset
name|getLast
parameter_list|( )
block|{
return|return
name|visited
operator|.
name|getLast
argument_list|( )
return|;
block|}
specifier|public
name|StorageAsset
name|getFirst
parameter_list|()
block|{
return|return
name|visited
operator|.
name|getFirst
argument_list|( )
return|;
block|}
specifier|public
name|List
argument_list|<
name|StorageAsset
argument_list|>
name|getVisited
parameter_list|( )
block|{
return|return
name|visited
return|;
block|}
specifier|public
name|int
name|size
parameter_list|( )
block|{
return|return
name|visited
operator|.
name|size
argument_list|( )
return|;
block|}
block|}
end_class

end_unit

