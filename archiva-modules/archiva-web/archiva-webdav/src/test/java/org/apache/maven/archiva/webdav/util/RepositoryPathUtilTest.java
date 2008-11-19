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
name|webdav
operator|.
name|util
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryPathUtilTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGetRepositoryId
parameter_list|()
block|{
name|String
name|href
init|=
literal|"/path/to/my/resource"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"to"
argument_list|,
name|RepositoryPathUtil
operator|.
name|getRepositoryName
argument_list|(
name|href
argument_list|)
argument_list|)
expr_stmt|;
name|href
operator|=
literal|"path/to/my/resource"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"to"
argument_list|,
name|RepositoryPathUtil
operator|.
name|getRepositoryName
argument_list|(
name|href
argument_list|)
argument_list|)
expr_stmt|;
name|href
operator|=
literal|"mypath"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|RepositoryPathUtil
operator|.
name|getLogicalResource
argument_list|(
name|href
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetLogicalPath
parameter_list|()
block|{
name|String
name|href
init|=
literal|"/repository/internal/org/apache/maven/someartifact.jar"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/org/apache/maven/someartifact.jar"
argument_list|,
name|RepositoryPathUtil
operator|.
name|getLogicalResource
argument_list|(
name|href
argument_list|)
argument_list|)
expr_stmt|;
name|href
operator|=
literal|"repository/internal/org/apache/maven/someartifact.jar"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/org/apache/maven/someartifact.jar"
argument_list|,
name|RepositoryPathUtil
operator|.
name|getLogicalResource
argument_list|(
name|href
argument_list|)
argument_list|)
expr_stmt|;
name|href
operator|=
literal|"repository/internal/org/apache/maven/"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/org/apache/maven/"
argument_list|,
name|RepositoryPathUtil
operator|.
name|getLogicalResource
argument_list|(
name|href
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

