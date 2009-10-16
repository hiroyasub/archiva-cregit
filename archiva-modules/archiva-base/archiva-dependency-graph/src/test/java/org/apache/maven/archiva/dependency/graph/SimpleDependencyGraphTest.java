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
name|dependency
operator|.
name|graph
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|List
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
name|dependency
operator|.
name|DependencyGraphFactory
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
name|model
operator|.
name|DependencyScope
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
name|model
operator|.
name|VersionedReference
import|;
end_import

begin_comment
comment|/**  * SimpleDependencyGraphTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|SimpleDependencyGraphTest
extends|extends
name|AbstractDependencyGraphFactoryTestCase
block|{
specifier|public
name|void
name|testResolveDependenciesBasic
parameter_list|()
throws|throws
name|GraphTaskException
block|{
name|MemoryRepositoryDependencyGraphBuilder
name|graphBuilder
init|=
operator|new
name|MemoryRepositoryDependencyGraphBuilder
argument_list|()
decl_stmt|;
name|MemoryRepository
name|repository
init|=
operator|new
name|SimpleMemoryRepository
argument_list|()
decl_stmt|;
name|graphBuilder
operator|.
name|setMemoryRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
comment|// Create the factory, and add the test resolver.
name|DependencyGraphFactory
name|factory
init|=
operator|new
name|DependencyGraphFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setGraphBuilder
argument_list|(
name|graphBuilder
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setDesiredScope
argument_list|(
name|DependencyScope
operator|.
name|TEST
argument_list|)
expr_stmt|;
comment|// Get the model to resolve from
name|VersionedReference
name|rootRef
init|=
name|toVersionedReference
argument_list|(
literal|"org.apache.maven.archiva:archiva-commons:1.0"
argument_list|)
decl_stmt|;
comment|// Perform the resolution.
name|DependencyGraph
name|graph
init|=
name|factory
operator|.
name|getGraph
argument_list|(
name|rootRef
argument_list|)
decl_stmt|;
comment|// Test the results.
name|assertNotNull
argument_list|(
literal|"Graph shouldn't be null."
argument_list|,
name|graph
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expectedNodes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expectedNodes
operator|.
name|add
argument_list|(
literal|"org.apache.maven.archiva:archiva-commons:1.0::pom"
argument_list|)
expr_stmt|;
name|expectedNodes
operator|.
name|add
argument_list|(
literal|"org.codehaus.plexus:plexus-digest:1.0::jar"
argument_list|)
expr_stmt|;
name|expectedNodes
operator|.
name|add
argument_list|(
literal|"junit:junit:3.8.1::jar"
argument_list|)
expr_stmt|;
name|assertNodes
argument_list|(
name|graph
argument_list|,
name|expectedNodes
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ExpectedEdge
argument_list|>
name|expectedEdges
init|=
operator|new
name|ArrayList
argument_list|<
name|ExpectedEdge
argument_list|>
argument_list|()
decl_stmt|;
name|expectedEdges
operator|.
name|add
argument_list|(
operator|new
name|ExpectedEdge
argument_list|(
literal|"org.apache.maven.archiva:archiva-commons:1.0::pom"
argument_list|,
literal|"org.codehaus.plexus:plexus-digest:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedEdges
operator|.
name|add
argument_list|(
operator|new
name|ExpectedEdge
argument_list|(
literal|"org.codehaus.plexus:plexus-digest:1.0::jar"
argument_list|,
literal|"junit:junit:3.8.1::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEdges
argument_list|(
name|graph
argument_list|,
name|expectedEdges
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

