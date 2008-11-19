begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|lucene
operator|.
name|stubs
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
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
name|maven
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryContentIndex
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|RepositoryContentIndexFactory
import|;
end_import

begin_comment
comment|/**  * LuceneRepositoryContenIndexFactoryStub  *   * @version  */
end_comment

begin_class
specifier|public
class|class
name|LuceneRepositoryContentIndexFactoryStub
implements|implements
name|RepositoryContentIndexFactory
block|{
specifier|public
name|RepositoryContentIndex
name|createBytecodeIndex
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
operator|new
name|LuceneRepositoryContentIndexStub
argument_list|()
return|;
block|}
specifier|public
name|RepositoryContentIndex
name|createFileContentIndex
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
operator|new
name|LuceneRepositoryContentIndexStub
argument_list|()
return|;
block|}
specifier|public
name|RepositoryContentIndex
name|createHashcodeIndex
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
operator|new
name|LuceneRepositoryContentIndexStub
argument_list|()
return|;
block|}
block|}
end_class

end_unit

