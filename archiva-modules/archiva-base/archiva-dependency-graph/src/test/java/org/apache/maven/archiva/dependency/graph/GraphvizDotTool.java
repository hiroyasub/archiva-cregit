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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|CollectionUtils
import|;
end_import

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
name|StringEscapeUtils
import|;
end_import

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
comment|/**  * GraphvizDotTool - testing utility to help understand the graph.   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|GraphvizDotTool
implements|implements
name|GraphListener
block|{
specifier|private
name|int
name|phaseNumber
init|=
literal|0
decl_stmt|;
specifier|protected
name|VersionedReference
name|toVersionedReference
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
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Versioned Reference ["
operator|+
name|key
operator|+
literal|"] part count."
argument_list|,
literal|3
argument_list|,
name|parts
operator|.
name|length
argument_list|)
expr_stmt|;
name|VersionedReference
name|ref
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setGroupId
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setArtifactId
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setVersion
argument_list|(
name|parts
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
return|return
name|ref
return|;
block|}
specifier|private
name|DependencyGraph
name|getDependencyGraph
parameter_list|(
name|MemoryRepository
name|repository
parameter_list|,
name|String
name|rootRefKey
parameter_list|)
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
name|factory
operator|.
name|addGraphListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// Get the model to resolve from
name|VersionedReference
name|rootRef
init|=
name|toVersionedReference
argument_list|(
name|rootRefKey
argument_list|)
decl_stmt|;
comment|// Perform the resolution.
name|phaseNumber
operator|=
literal|0
expr_stmt|;
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
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Graph shouldn't be null."
argument_list|,
name|graph
argument_list|)
expr_stmt|;
return|return
name|graph
return|;
block|}
specifier|public
name|void
name|testGenerateDots
parameter_list|()
throws|throws
name|GraphTaskException
block|{
name|getDependencyGraph
argument_list|(
operator|new
name|ArchivaWebappMemoryRepository
argument_list|()
argument_list|,
literal|"org.apache.maven.archiva:archiva-webapp:1.0-alpha-2-SNAPSHOT"
argument_list|)
expr_stmt|;
comment|//        getDependencyGraph( new ArchivaCommonMemoryRepository(),
comment|//                            "org.apache.maven.archiva:archiva-common:1.0-alpha-2-SNAPSHOT" );
comment|//
comment|//        getDependencyGraph( new ArchivaXmlToolsMemoryRepository(),
comment|//                            "org.apache.maven.archiva:archiva-xml-tools:1.0-alpha-2-SNAPSHOT" );
comment|//
comment|//        getDependencyGraph( new ContinuumStoreMemoryRepository(),
comment|//                            "org.apache.maven.continuum:continuum-store:1.1-SNAPSHOT" );
comment|//
comment|//        getDependencyGraph( new MavenProjectInfoReportsPluginMemoryRepository(),
comment|//                            "org.apache.maven.plugins:maven-project-info-reports-plugin:2.1-SNAPSHOT" );
comment|//
comment|//        getDependencyGraph( new WagonManagerMemoryRepository(), "org.apache.maven.wagon:wagon-manager:2.0-SNAPSHOT" );
name|getDependencyGraph
argument_list|(
operator|new
name|DepManDeepVersionMemoryRepository
argument_list|()
argument_list|,
literal|"net.example.depman.deepversion:A:1.0"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dependencyResolutionEvent
parameter_list|(
name|DependencyResolutionEvent
name|event
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|graphError
parameter_list|(
name|GraphTaskException
name|e
parameter_list|,
name|DependencyGraph
name|currentGraph
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|graphPhaseEvent
parameter_list|(
name|GraphPhaseEvent
name|event
parameter_list|)
block|{
name|String
name|graphId
init|=
name|event
operator|.
name|getGraph
argument_list|()
operator|.
name|getRootNode
argument_list|()
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|String
name|title
init|=
literal|"Graph: "
operator|+
name|graphId
decl_stmt|;
switch|switch
condition|(
name|event
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|GraphPhaseEvent
operator|.
name|GRAPH_TASK_POST
case|:
name|phaseNumber
operator|++
expr_stmt|;
name|title
operator|+=
literal|" - Phase: "
operator|+
name|phaseNumber
operator|+
literal|" - Task: "
operator|+
name|event
operator|.
name|getTask
argument_list|()
operator|.
name|getTaskId
argument_list|()
expr_stmt|;
name|writeDot
argument_list|(
literal|"target/graph_"
operator|+
name|graphId
operator|+
literal|"_"
operator|+
name|phaseNumber
operator|+
literal|"_"
operator|+
name|event
operator|.
name|getTask
argument_list|()
operator|.
name|getTaskId
argument_list|()
operator|+
literal|".dot"
argument_list|,
name|event
operator|.
name|getGraph
argument_list|()
argument_list|,
name|title
argument_list|)
expr_stmt|;
break|break;
case|case
name|GraphPhaseEvent
operator|.
name|GRAPH_DONE
case|:
name|title
operator|+=
literal|" FINISHED"
expr_stmt|;
name|writeDot
argument_list|(
literal|"target/graph_"
operator|+
name|graphId
operator|+
literal|".dot"
argument_list|,
name|event
operator|.
name|getGraph
argument_list|()
argument_list|,
name|title
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
specifier|private
name|void
name|writeDot
parameter_list|(
name|String
name|outputFilename
parameter_list|,
name|DependencyGraph
name|graph
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Writing Graphviz output: "
operator|+
name|outputFilename
argument_list|)
expr_stmt|;
try|try
block|{
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|outputFilename
argument_list|)
decl_stmt|;
name|FileWriter
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|outputFile
argument_list|)
decl_stmt|;
name|PrintWriter
name|dot
init|=
operator|new
name|PrintWriter
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"// Auto generated dot file from plexus-graph-visualizer-graphviz."
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"digraph example {"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  // Graph Defaults"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  graph ["
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    bgcolor=\"#ffffff\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontname=\"Helvetica\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontsize=\"11\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    label=\""
operator|+
name|title
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    labeljust=\"l\""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    rankdir=\"LR\""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  ];"
argument_list|)
expr_stmt|;
comment|// Node Defaults.
name|dot
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  // Node Defaults."
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  node ["
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontname=\"Helvetica\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontsize=\"11\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    shape=\"box\""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  ];"
argument_list|)
expr_stmt|;
comment|// Edge Defaults.
name|dot
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  // Edge Defaults."
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  edge ["
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    arrowsize=\"0.8\""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontsize=\"11\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  ];"
argument_list|)
expr_stmt|;
for|for
control|(
name|DependencyGraphNode
name|node
range|:
name|graph
operator|.
name|getNodes
argument_list|()
control|)
block|{
name|writeNode
argument_list|(
name|dot
argument_list|,
name|graph
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DependencyGraphEdge
name|edge
range|:
name|graph
operator|.
name|getEdges
argument_list|()
control|)
block|{
name|DependencyGraphNode
name|from
init|=
name|graph
operator|.
name|getNode
argument_list|(
name|edge
operator|.
name|getNodeFrom
argument_list|()
argument_list|)
decl_stmt|;
name|DependencyGraphNode
name|to
init|=
name|graph
operator|.
name|getNode
argument_list|(
name|edge
operator|.
name|getNodeTo
argument_list|()
argument_list|)
decl_stmt|;
name|writeEdge
argument_list|(
name|dot
argument_list|,
name|edge
argument_list|,
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
name|dot
operator|.
name|println
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|flush
argument_list|()
expr_stmt|;
name|dot
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unable to write GraphViz file "
operator|+
name|outputFilename
operator|+
literal|" : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|toLabel
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|StringBuffer
name|lbl
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|lbl
operator|.
name|append
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|lbl
operator|.
name|append
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|lbl
operator|.
name|append
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|StringEscapeUtils
operator|.
name|escapeJava
argument_list|(
name|lbl
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|toId
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|StringBuffer
name|id
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|raw
init|=
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|raw
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|raw
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isLetterOrDigit
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|id
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
operator|(
name|c
operator|==
literal|'-'
operator|)
operator|||
operator|(
name|c
operator|==
literal|'_'
operator|)
condition|)
block|{
name|id
operator|.
name|append
argument_list|(
literal|"_"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|id
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|writeNode
parameter_list|(
name|PrintWriter
name|dot
parameter_list|,
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|dot
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  // Node"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  \""
operator|+
name|toId
argument_list|(
name|node
argument_list|)
operator|+
literal|"\" ["
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    label=\""
operator|+
name|toLabel
argument_list|(
name|node
argument_list|)
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|boolean
name|orphan
init|=
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|graph
operator|.
name|getEdgesTo
argument_list|(
name|node
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|isFromParent
argument_list|()
condition|)
block|{
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#FF0000\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    shape=ellipse,"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dot
operator|.
name|println
argument_list|(
literal|"    shape=box,"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|.
name|isConflicted
argument_list|()
condition|)
block|{
comment|// dot.println( "    fontcolor=\"#FF88FF\"," );
name|dot
operator|.
name|println
argument_list|(
literal|"    style=filled,"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fillcolor=\"#88FF88\","
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|orphan
condition|)
block|{
name|dot
operator|.
name|println
argument_list|(
literal|"    style=filled,"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fillcolor=\"#8888FF\","
argument_list|)
expr_stmt|;
block|}
name|dot
operator|.
name|println
argument_list|(
literal|"  ];"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeEdge
parameter_list|(
name|PrintWriter
name|dot
parameter_list|,
name|DependencyGraphEdge
name|edge
parameter_list|,
name|DependencyGraphNode
name|from
parameter_list|,
name|DependencyGraphNode
name|to
parameter_list|)
block|{
name|dot
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  // Edge"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  \""
operator|+
name|toId
argument_list|(
name|from
argument_list|)
operator|+
literal|"\" -> \""
operator|+
name|toId
argument_list|(
name|to
argument_list|)
operator|+
literal|"\" ["
argument_list|)
expr_stmt|;
if|if
condition|(
name|edge
operator|.
name|isDisabled
argument_list|()
condition|)
block|{
switch|switch
condition|(
name|edge
operator|.
name|getDisabledType
argument_list|()
condition|)
block|{
case|case
name|DependencyGraph
operator|.
name|DISABLED_CYCLIC
case|:
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#FF0000\","
argument_list|)
expr_stmt|;
break|break;
case|case
name|DependencyGraph
operator|.
name|DISABLED_OPTIONAL
case|:
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#FF00FF\","
argument_list|)
expr_stmt|;
break|break;
case|case
name|DependencyGraph
operator|.
name|DISABLED_NEARER_DEP
case|:
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#00FF00\","
argument_list|)
expr_stmt|;
break|break;
case|case
name|DependencyGraph
operator|.
name|DISABLED_NEARER_EDGE
case|:
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#88FF88\","
argument_list|)
expr_stmt|;
break|break;
default|default:
case|case
name|DependencyGraph
operator|.
name|DISABLED_EXCLUDED
case|:
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#0000FF\","
argument_list|)
expr_stmt|;
break|break;
block|}
name|dot
operator|.
name|println
argument_list|(
literal|"    label=\""
operator|+
name|edge
operator|.
name|getDisabledReason
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontsize=\"8\","
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|DependencyScope
operator|.
name|TEST
operator|.
name|equals
argument_list|(
name|edge
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|dot
operator|.
name|println
argument_list|(
literal|"    style=\"dashed\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#DDDDDD\","
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|DependencyScope
operator|.
name|RUNTIME
operator|.
name|equals
argument_list|(
name|edge
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|dot
operator|.
name|println
argument_list|(
literal|"    style=\"dashed\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#DDFFDD\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    label=\"runtime\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontsize=\"8\","
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|DependencyScope
operator|.
name|PROVIDED
operator|.
name|equals
argument_list|(
name|edge
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|dot
operator|.
name|println
argument_list|(
literal|"    style=\"dashed\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#DDDDFF\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    label=\"provided\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontsize=\"8\","
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|DependencyScope
operator|.
name|SYSTEM
operator|.
name|equals
argument_list|(
name|edge
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|dot
operator|.
name|println
argument_list|(
literal|"    style=\"dashed\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    color=\"#FFDDDD\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    label=\"system\","
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    fontsize=\"8\","
argument_list|)
expr_stmt|;
block|}
name|dot
operator|.
name|println
argument_list|(
literal|"    arrowtail=none,"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"    arrowhead=normal"
argument_list|)
expr_stmt|;
name|dot
operator|.
name|println
argument_list|(
literal|"  ];"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

