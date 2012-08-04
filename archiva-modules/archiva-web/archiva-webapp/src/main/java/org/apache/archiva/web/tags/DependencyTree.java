begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|ActionContext
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
name|archiva
operator|.
name|dependency
operator|.
name|tree
operator|.
name|maven2
operator|.
name|Maven3DependencyTreeBuilder
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
name|model
operator|.
name|Keys
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
name|security
operator|.
name|ArchivaXworkUser
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
name|security
operator|.
name|UserRepositories
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|graph
operator|.
name|DependencyNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|graph
operator|.
name|DependencyVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|List
import|;
end_import

begin_comment
comment|/**  * DependencyTree  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"dependencyTree"
argument_list|)
specifier|public
class|class
name|DependencyTree
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DependencyTree
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|Maven3DependencyTreeBuilder
name|dependencyTreeBuilder
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UserRepositories
name|userRepositories
decl_stmt|;
specifier|public
specifier|static
class|class
name|TreeEntry
block|{
specifier|private
name|String
name|pre
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|post
init|=
literal|""
decl_stmt|;
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|public
name|void
name|setArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
specifier|public
name|Artifact
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
specifier|public
name|String
name|getPost
parameter_list|()
block|{
return|return
name|post
return|;
block|}
specifier|public
name|String
name|getPre
parameter_list|()
block|{
return|return
name|pre
return|;
block|}
specifier|public
name|void
name|appendPre
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|this
operator|.
name|pre
operator|+=
name|string
expr_stmt|;
block|}
specifier|public
name|void
name|appendPost
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|this
operator|.
name|post
operator|+=
name|string
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|gatherTreeList
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|modelVersion
parameter_list|)
throws|throws
name|ArchivaException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
name|String
name|emsg
init|=
literal|"Error generating dependency tree ["
operator|+
name|Keys
operator|.
name|toKey
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|modelVersion
argument_list|)
operator|+
literal|"]: groupId is blank."
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|emsg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaException
argument_list|(
name|emsg
argument_list|)
throw|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
name|String
name|emsg
init|=
literal|"Error generating dependency tree ["
operator|+
name|Keys
operator|.
name|toKey
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|modelVersion
argument_list|)
operator|+
literal|"]: artifactId is blank."
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|emsg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaException
argument_list|(
name|emsg
argument_list|)
throw|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|modelVersion
argument_list|)
condition|)
block|{
name|String
name|emsg
init|=
literal|"Error generating dependency tree ["
operator|+
name|Keys
operator|.
name|toKey
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|modelVersion
argument_list|)
operator|+
literal|"]: version is blank."
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|emsg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaException
argument_list|(
name|emsg
argument_list|)
throw|;
block|}
comment|// TODO Cache the results to disk, in XML format, in the same place as the artifact is located.
name|TreeListVisitor
name|visitor
init|=
operator|new
name|TreeListVisitor
argument_list|()
decl_stmt|;
try|try
block|{
name|dependencyTreeBuilder
operator|.
name|buildDependencyTree
argument_list|(
name|userRepositories
operator|.
name|getObservableRepositoryIds
argument_list|(
name|getPrincipal
argument_list|()
argument_list|)
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|modelVersion
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaException
argument_list|(
literal|"Unable to build dependency tree: "
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
return|return
name|visitor
operator|.
name|getList
argument_list|()
return|;
block|}
specifier|private
name|String
name|getPrincipal
parameter_list|()
block|{
return|return
name|ArchivaXworkUser
operator|.
name|getActivePrincipal
argument_list|(
name|ActionContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSession
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|TreeListVisitor
implements|implements
name|DependencyVisitor
block|{
specifier|private
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|list
decl_stmt|;
specifier|private
name|TreeEntry
name|currentEntry
decl_stmt|;
name|boolean
name|firstChild
init|=
literal|true
decl_stmt|;
specifier|private
name|DependencyNode
name|firstNode
decl_stmt|;
specifier|public
name|TreeListVisitor
parameter_list|()
block|{
name|this
operator|.
name|list
operator|=
operator|new
name|ArrayList
argument_list|<
name|TreeEntry
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|getList
parameter_list|()
block|{
return|return
name|this
operator|.
name|list
return|;
block|}
specifier|public
name|boolean
name|visitEnter
parameter_list|(
name|DependencyNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|firstNode
operator|==
literal|null
condition|)
block|{
name|firstNode
operator|=
name|node
expr_stmt|;
block|}
name|currentEntry
operator|=
operator|new
name|TreeEntry
argument_list|()
expr_stmt|;
if|if
condition|(
name|firstChild
condition|)
block|{
name|currentEntry
operator|.
name|appendPre
argument_list|(
literal|"<ul>"
argument_list|)
expr_stmt|;
block|}
name|currentEntry
operator|.
name|appendPre
argument_list|(
literal|"<li>"
argument_list|)
expr_stmt|;
name|currentEntry
operator|.
name|setArtifact
argument_list|(
name|node
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
name|currentEntry
operator|.
name|appendPost
argument_list|(
literal|"</li>"
argument_list|)
expr_stmt|;
name|this
operator|.
name|list
operator|.
name|add
argument_list|(
name|currentEntry
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|getChildren
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|firstChild
operator|=
literal|true
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitLeave
parameter_list|(
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|graph
operator|.
name|DependencyNode
name|node
parameter_list|)
block|{
name|firstChild
operator|=
literal|false
expr_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|getChildren
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|currentEntry
operator|.
name|appendPost
argument_list|(
literal|"</ul>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|==
name|firstNode
condition|)
block|{
name|currentEntry
operator|.
name|appendPost
argument_list|(
literal|"</ul>"
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

