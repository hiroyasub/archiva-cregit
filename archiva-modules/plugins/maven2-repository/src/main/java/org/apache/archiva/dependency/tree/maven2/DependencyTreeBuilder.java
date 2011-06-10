begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|dependency
operator|.
name|tree
operator|.
name|maven2
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeBuilderException
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|traversal
operator|.
name|DependencyNodeVisitor
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
comment|/**  * Builds a tree of dependencies for a given Maven project. Customized wrapper for maven-dependency-tree to use  * maven-model-builder instead of maven-project.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyTreeBuilder
block|{
comment|/**      * Builds a tree of dependencies for the specified Maven project.      *      * @param repositoryIds the list of repositories to search for metadata      * @param groupId       the project groupId to build the tree for      * @param artifactId    the project artifactId to build the tree for      * @param version       the project version to build the tree for      * @param nodeVisitor   visitor to apply to all nodes discovered      * @throws DependencyTreeBuilderException if the dependency tree cannot be resolved      */
name|void
name|buildDependencyTree
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|DependencyNodeVisitor
name|nodeVisitor
parameter_list|)
throws|throws
name|DependencyTreeBuilderException
function_decl|;
block|}
end_interface

end_unit

