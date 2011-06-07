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
name|web
operator|.
name|tags
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
name|archiva
operator|.
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridgeException
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
name|collections
operator|.
name|IteratorUtils
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
name|common
operator|.
name|ArchivaException
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
name|web
operator|.
name|tags
operator|.
name|DependencyTree
operator|.
name|TreeEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|component
operator|.
name|repository
operator|.
name|exception
operator|.
name|ComponentLookupException
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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|JspException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|PageContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|tagext
operator|.
name|IterationTag
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|tagext
operator|.
name|TagSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|tagext
operator|.
name|TryCatchFinally
import|;
end_import

begin_comment
comment|/**  * DependencyTreeTag - just here to output the dependency tree to the browser.  * It was easier to do it this way, vs accessing the dependency graph via a JSP.  *   *<pre>  *<archiva:dependency-tree groupId="org.apache.maven.archiva"   *                            artifactId="archiva-common"   *                            version="1.0"  *                            nodevar="node">  *<b>${node.groupId}</b>:<b>${node.artifactId}</b>:<b>${node.version}</b> (${node.scope})  *</archiva:dependency-tree>  *</pre>  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DependencyTreeTag
extends|extends
name|TagSupport
implements|implements
name|IterationTag
implements|,
name|TryCatchFinally
block|{
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|nodevar
decl_stmt|;
specifier|private
name|Iterator
argument_list|<
name|TreeEntry
argument_list|>
name|treeIterator
decl_stmt|;
specifier|private
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|tree
decl_stmt|;
specifier|private
name|TreeEntry
name|currentTreeEntry
decl_stmt|;
specifier|private
name|String
name|modelVersion
decl_stmt|;
specifier|public
name|int
name|doAfterBody
parameter_list|()
throws|throws
name|JspException
block|{
if|if
condition|(
name|currentTreeEntry
operator|!=
literal|null
condition|)
block|{
name|out
argument_list|(
name|currentTreeEntry
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|treeIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|currentTreeEntry
operator|=
name|treeIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|out
argument_list|(
name|currentTreeEntry
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|exposeVariables
argument_list|()
expr_stmt|;
return|return
name|EVAL_BODY_AGAIN
return|;
block|}
name|out
argument_list|(
literal|"\n</div><!-- end of dependency-graph -->"
argument_list|)
expr_stmt|;
return|return
name|SKIP_BODY
return|;
block|}
specifier|public
name|void
name|doCatch
parameter_list|(
name|Throwable
name|t
parameter_list|)
throws|throws
name|Throwable
block|{
throw|throw
name|t
throw|;
block|}
specifier|public
name|void
name|doFinally
parameter_list|()
block|{
name|unExposeVariables
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|int
name|doStartTag
parameter_list|()
throws|throws
name|JspException
block|{
name|DependencyTree
name|deptree
decl_stmt|;
try|try
block|{
name|deptree
operator|=
operator|(
name|DependencyTree
operator|)
name|PlexusTagUtil
operator|.
name|lookup
argument_list|(
name|pageContext
argument_list|,
name|DependencyTree
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PlexusSisuBridgeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JspException
argument_list|(
literal|"Unable to lookup DependencyTree: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|deptree
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JspException
argument_list|(
literal|"Unable to process dependency tree.  Component not found."
argument_list|)
throw|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|nodevar
argument_list|)
condition|)
block|{
name|nodevar
operator|=
literal|"node"
expr_stmt|;
block|}
name|out
argument_list|(
literal|"<div class=\"dependency-graph\">"
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|tree
operator|=
name|deptree
operator|.
name|gatherTreeList
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|modelVersion
argument_list|)
expr_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|this
operator|.
name|tree
argument_list|)
condition|)
block|{
return|return
name|SKIP_BODY
return|;
block|}
name|treeIterator
operator|=
name|tree
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|currentTreeEntry
operator|=
name|treeIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|out
argument_list|(
name|currentTreeEntry
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|exposeVariables
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaException
name|e
parameter_list|)
block|{
name|treeIterator
operator|=
name|IteratorUtils
operator|.
name|EMPTY_LIST_ITERATOR
expr_stmt|;
name|out
argument_list|(
literal|"<pre>"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|pageContext
operator|.
name|getOut
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
argument_list|(
literal|"</pre>"
argument_list|)
expr_stmt|;
block|}
return|return
name|EVAL_BODY_INCLUDE
return|;
block|}
specifier|public
name|void
name|release
parameter_list|()
block|{
name|groupId
operator|=
literal|""
expr_stmt|;
name|artifactId
operator|=
literal|""
expr_stmt|;
name|version
operator|=
literal|""
expr_stmt|;
name|nodevar
operator|=
literal|""
expr_stmt|;
name|tree
operator|=
literal|null
expr_stmt|;
name|treeIterator
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|void
name|setNodevar
parameter_list|(
name|String
name|nodevar
parameter_list|)
block|{
name|this
operator|.
name|nodevar
operator|=
name|nodevar
expr_stmt|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|void
name|setModelVersion
parameter_list|(
name|String
name|modelVersion
parameter_list|)
block|{
name|this
operator|.
name|modelVersion
operator|=
name|modelVersion
expr_stmt|;
block|}
specifier|private
name|void
name|exposeVariables
parameter_list|()
throws|throws
name|JspException
block|{
if|if
condition|(
name|currentTreeEntry
operator|==
literal|null
condition|)
block|{
name|pageContext
operator|.
name|removeAttribute
argument_list|(
name|nodevar
argument_list|,
name|PageContext
operator|.
name|PAGE_SCOPE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pageContext
operator|.
name|setAttribute
argument_list|(
name|nodevar
argument_list|,
name|currentTreeEntry
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|out
parameter_list|(
name|String
name|msg
parameter_list|)
throws|throws
name|JspException
block|{
try|try
block|{
name|pageContext
operator|.
name|getOut
argument_list|()
operator|.
name|print
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JspException
argument_list|(
literal|"Unable to output to jsp page context."
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|unExposeVariables
parameter_list|()
block|{
name|pageContext
operator|.
name|removeAttribute
argument_list|(
name|nodevar
argument_list|,
name|PageContext
operator|.
name|PAGE_SCOPE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

