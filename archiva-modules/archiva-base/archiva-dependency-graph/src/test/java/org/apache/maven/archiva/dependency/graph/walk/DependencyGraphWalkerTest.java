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
operator|.
name|walk
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|graph
operator|.
name|DependencyGraph
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
name|graph
operator|.
name|DependencyGraphEdge
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
name|graph
operator|.
name|DependencyGraphKeys
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
name|graph
operator|.
name|DependencyGraphNode
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
name|graph
operator|.
name|tasks
operator|.
name|FlagCyclicEdgesTask
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
name|ArtifactReference
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * DependencyGraphWalkerTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DependencyGraphWalkerTest
extends|extends
name|TestCase
block|{
comment|/**      *<pre>      *  [foo-util] ---&gt; [foo-common]      *      \      *       ---------&gt; [foo-xml] ---&gt; [xercesImpl] ---&gt; [xmlParserAPIs]      *                        \  \      *                         \  ---&gt; [jdom] ----+      *                          \                 |      *                           ----&gt; [jaxen]&lt;--+      *</pre>      */
specifier|public
name|void
name|testModerateWalk
parameter_list|()
block|{
name|DependencyGraph
name|graph
init|=
operator|new
name|DependencyGraph
argument_list|(
literal|"org.foo"
argument_list|,
literal|"foo-util"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|String
name|rootKey
init|=
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|graph
operator|.
name|getRootNode
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
name|rootKey
argument_list|,
literal|"org.foo:foo-common:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
name|rootKey
argument_list|,
literal|"org.foo:foo-xml:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"org.foo:foo-xml:1.0::jar"
argument_list|,
literal|"xerces:xercesImpl:2.2.1::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"xerces:xercesImpl:2.2.1::jar"
argument_list|,
literal|"xerces:xmlParserAPIs:2.2.1::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"org.foo:foo-xml:1.0::jar"
argument_list|,
literal|"jdom:jdom:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"org.foo:foo-xml:1.0::jar"
argument_list|,
literal|"jaxen:jaxen:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"jdom:jdom:1.0::jar"
argument_list|,
literal|"jaxen:jaxen:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyGraphWalker
name|walker
init|=
operator|new
name|WalkDepthFirstSearch
argument_list|()
decl_stmt|;
name|WalkCollector
name|walkCollector
init|=
operator|new
name|WalkCollector
argument_list|()
decl_stmt|;
name|walker
operator|.
name|visit
argument_list|(
name|graph
argument_list|,
name|walkCollector
argument_list|)
expr_stmt|;
name|String
name|expectedPath
index|[]
init|=
operator|new
name|String
index|[]
block|{
name|rootKey
block|,
literal|"org.foo:foo-common:1.0::jar"
block|,
literal|"org.foo:foo-xml:1.0::jar"
block|,
literal|"jaxen:jaxen:1.0::jar"
block|,
literal|"xerces:xercesImpl:2.2.1::jar"
block|,
literal|"xerces:xmlParserAPIs:2.2.1::jar"
block|,
literal|"jdom:jdom:1.0::jar"
block|}
decl_stmt|;
name|assertVisitor
argument_list|(
name|walkCollector
argument_list|,
literal|1
argument_list|,
literal|7
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|assertPath
argument_list|(
name|expectedPath
argument_list|,
name|walkCollector
operator|.
name|getCollectedPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      *<pre>      *  [foo-util] ---&gt; [foo-common]      *      \      *       ---------&gt; [foo-xml] ---&gt; [xercesImpl] ---&gt; [xmlParserAPIs]      *</pre>      */
specifier|public
name|void
name|testSimpleWalk
parameter_list|()
block|{
name|DependencyGraph
name|graph
init|=
operator|new
name|DependencyGraph
argument_list|(
literal|"org.foo"
argument_list|,
literal|"foo-util"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|String
name|rootKey
init|=
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|graph
operator|.
name|getRootNode
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
name|rootKey
argument_list|,
literal|"org.foo:foo-common:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
name|rootKey
argument_list|,
literal|"org.foo:foo-xml:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"org.foo:foo-xml:1.0::jar"
argument_list|,
literal|"xerces:xercesImpl:2.2.1::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"xerces:xercesImpl:2.2.1::jar"
argument_list|,
literal|"xerces:xmlParserAPIs:2.2.1::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyGraphWalker
name|walker
init|=
operator|new
name|WalkDepthFirstSearch
argument_list|()
decl_stmt|;
name|WalkCollector
name|walkCollector
init|=
operator|new
name|WalkCollector
argument_list|()
decl_stmt|;
name|walker
operator|.
name|visit
argument_list|(
name|graph
argument_list|,
name|walkCollector
argument_list|)
expr_stmt|;
name|String
name|expectedPath
index|[]
init|=
operator|new
name|String
index|[]
block|{
name|rootKey
block|,
literal|"org.foo:foo-common:1.0::jar"
block|,
literal|"org.foo:foo-xml:1.0::jar"
block|,
literal|"xerces:xercesImpl:2.2.1::jar"
block|,
literal|"xerces:xmlParserAPIs:2.2.1::jar"
block|}
decl_stmt|;
name|assertVisitor
argument_list|(
name|walkCollector
argument_list|,
literal|1
argument_list|,
literal|5
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertPath
argument_list|(
name|expectedPath
argument_list|,
name|walkCollector
operator|.
name|getCollectedPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      *<pre>      *  [foo-util] ---&gt; [foo-common]      *      \      *       \              +----------------------------------------+      *        \             v                                        |      *         -------&gt; [foo-xml] ---&gt; [xercesImpl] ---&gt; [xmlParserAPIs]      *                        \  \      *                         \  ---&gt; [jdom] ----+      *                          \                 |      *                           ----&gt; [jaxen]&lt;--+      *</pre>      */
specifier|public
name|void
name|testDeepNodeWalk
parameter_list|()
block|{
name|DependencyGraph
name|graph
init|=
operator|new
name|DependencyGraph
argument_list|(
literal|"org.foo"
argument_list|,
literal|"foo-util"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|String
name|rootKey
init|=
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|graph
operator|.
name|getRootNode
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
name|rootKey
argument_list|,
literal|"org.foo:foo-common:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
name|rootKey
argument_list|,
literal|"org.foo:foo-xml:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"org.foo:foo-xml:1.0::jar"
argument_list|,
literal|"xerces:xercesImpl:2.2.1::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"xerces:xercesImpl:2.2.1::jar"
argument_list|,
literal|"xerces:xmlParserAPIs:2.2.1::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"org.foo:foo-xml:1.0::jar"
argument_list|,
literal|"jdom:jdom:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"org.foo:foo-xml:1.0::jar"
argument_list|,
literal|"jaxen:jaxen:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"jdom:jdom:1.0::jar"
argument_list|,
literal|"jaxen:jaxen:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
comment|// introduce cyclic dep. intentional. should only result in walking to foo-xml once.
name|addEdgeAndNodes
argument_list|(
name|graph
argument_list|,
name|toEdge
argument_list|(
literal|"xerces:xmlParserAPIs:2.2.1::jar"
argument_list|,
literal|"org.foo:foo-xml:1.0::jar"
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|FlagCyclicEdgesTask
argument_list|()
operator|.
name|executeTask
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|DependencyGraphWalker
name|walker
init|=
operator|new
name|WalkDepthFirstSearch
argument_list|()
decl_stmt|;
name|WalkCollector
name|walkCollector
init|=
operator|new
name|WalkCollector
argument_list|()
decl_stmt|;
name|ArtifactReference
name|startRef
init|=
name|toArtifactReference
argument_list|(
literal|"org.foo:foo-xml:1.0::jar"
argument_list|)
decl_stmt|;
name|DependencyGraphNode
name|startNode
init|=
operator|new
name|DependencyGraphNode
argument_list|(
name|startRef
argument_list|)
decl_stmt|;
name|walker
operator|.
name|visit
argument_list|(
name|graph
argument_list|,
name|startNode
argument_list|,
name|walkCollector
argument_list|)
expr_stmt|;
name|String
name|expectedPath
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"org.foo:foo-xml:1.0::jar"
block|,
literal|"jaxen:jaxen:1.0::jar"
block|,
literal|"xerces:xercesImpl:2.2.1::jar"
block|,
literal|"xerces:xmlParserAPIs:2.2.1::jar"
block|,
literal|"jdom:jdom:1.0::jar"
block|}
decl_stmt|;
name|assertVisitor
argument_list|(
name|walkCollector
argument_list|,
literal|1
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|assertPath
argument_list|(
name|expectedPath
argument_list|,
name|walkCollector
operator|.
name|getCollectedPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addEdgeAndNodes
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphEdge
name|edge
parameter_list|)
block|{
name|ensureNodeExists
argument_list|(
name|graph
argument_list|,
name|edge
operator|.
name|getNodeFrom
argument_list|()
argument_list|)
expr_stmt|;
name|ensureNodeExists
argument_list|(
name|graph
argument_list|,
name|edge
operator|.
name|getNodeTo
argument_list|()
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|ensureNodeExists
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|ArtifactReference
name|artifact
parameter_list|)
block|{
name|DependencyGraphNode
name|node
init|=
name|graph
operator|.
name|getNode
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
name|node
operator|=
operator|new
name|DependencyGraphNode
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertPath
parameter_list|(
name|String
index|[]
name|expectedPath
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|collectedPath
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Path.length"
argument_list|,
name|expectedPath
operator|.
name|length
argument_list|,
name|collectedPath
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
name|expectedPath
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Walk path["
operator|+
name|i
operator|+
literal|"]"
argument_list|,
name|expectedPath
index|[
name|i
index|]
argument_list|,
name|collectedPath
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertVisitor
parameter_list|(
name|WalkCollector
name|walkCollector
parameter_list|,
name|int
name|countGraphs
parameter_list|,
name|int
name|countNodes
parameter_list|,
name|int
name|countEdges
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Count of graph discovery."
argument_list|,
name|countGraphs
argument_list|,
name|walkCollector
operator|.
name|getCountDiscoverGraph
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Count of graph finished."
argument_list|,
name|countGraphs
argument_list|,
name|walkCollector
operator|.
name|getCountFinishGraph
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Discover - Finish = 0 (on graph counts)"
argument_list|,
literal|0
argument_list|,
operator|(
name|walkCollector
operator|.
name|getCountDiscoverGraph
argument_list|()
operator|-
name|walkCollector
operator|.
name|getCountFinishGraph
argument_list|()
operator|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Count of node discovery."
argument_list|,
name|countNodes
argument_list|,
name|walkCollector
operator|.
name|getCountDiscoverNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Count of node finished."
argument_list|,
name|countNodes
argument_list|,
name|walkCollector
operator|.
name|getCountFinishNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Discover - Finish = 0 (on node counts)"
argument_list|,
literal|0
argument_list|,
operator|(
name|walkCollector
operator|.
name|getCountDiscoverNode
argument_list|()
operator|-
name|walkCollector
operator|.
name|getCountFinishNode
argument_list|()
operator|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Count of edge discovery."
argument_list|,
name|countEdges
argument_list|,
name|walkCollector
operator|.
name|getCountDiscoverEdge
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Count of edge finished."
argument_list|,
name|countEdges
argument_list|,
name|walkCollector
operator|.
name|getCountFinishEdge
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Discover - Finish = 0 (on edge counts)"
argument_list|,
literal|0
argument_list|,
operator|(
name|walkCollector
operator|.
name|getCountDiscoverEdge
argument_list|()
operator|-
name|walkCollector
operator|.
name|getCountFinishEdge
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DependencyGraphEdge
name|toEdge
parameter_list|(
name|String
name|fromKey
parameter_list|,
name|String
name|toKey
parameter_list|)
block|{
name|ArtifactReference
name|nodeFrom
init|=
name|toArtifactReference
argument_list|(
name|fromKey
argument_list|)
decl_stmt|;
name|ArtifactReference
name|nodeTo
init|=
name|toArtifactReference
argument_list|(
name|toKey
argument_list|)
decl_stmt|;
return|return
operator|new
name|DependencyGraphEdge
argument_list|(
name|nodeFrom
argument_list|,
name|nodeTo
argument_list|)
return|;
block|}
specifier|private
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|String
name|parts
index|[]
init|=
name|StringUtils
operator|.
name|splitPreserveAllTokens
argument_list|(
name|key
argument_list|,
literal|':'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ArtifactReference ["
operator|+
name|key
operator|+
literal|"] parts should equal 5"
argument_list|,
literal|5
argument_list|,
name|parts
operator|.
name|length
argument_list|)
expr_stmt|;
name|ArtifactReference
name|artifact
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|parts
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setClassifier
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setType
argument_list|(
name|parts
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

