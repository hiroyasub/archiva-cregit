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
name|com
operator|.
name|opensymphony
operator|.
name|webwork
operator|.
name|views
operator|.
name|jsp
operator|.
name|TagUtils
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
name|tagext
operator|.
name|TagSupport
import|;
end_import

begin_comment
comment|/**  * DownloadArtifactTag   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DownloadArtifactTag
extends|extends
name|TagSupport
block|{
specifier|private
name|String
name|groupId_
decl_stmt|;
comment|// stores EL-based groupId property
specifier|private
name|String
name|groupId
decl_stmt|;
comment|// stores the evaluated groupId object.
specifier|private
name|String
name|artifactId_
decl_stmt|;
comment|// stores EL-based artifactId property
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|// stores the evaluated artifactId object.
specifier|private
name|String
name|version_
decl_stmt|;
comment|// stores EL-based version property
specifier|private
name|String
name|version
decl_stmt|;
comment|// stores the evaluated version object.
specifier|private
name|String
name|mini_
decl_stmt|;
comment|// stores EL-based mini property
specifier|private
name|boolean
name|mini
decl_stmt|;
comment|// stores the evaluated mini object.
specifier|public
name|int
name|doEndTag
parameter_list|()
throws|throws
name|JspException
block|{
name|evaluateExpressions
argument_list|()
expr_stmt|;
name|DownloadArtifact
name|download
init|=
operator|new
name|DownloadArtifact
argument_list|(
name|TagUtils
operator|.
name|getStack
argument_list|(
name|pageContext
argument_list|)
argument_list|,
name|pageContext
argument_list|)
decl_stmt|;
name|download
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|download
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|download
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|download
operator|.
name|setMini
argument_list|(
name|mini
argument_list|)
expr_stmt|;
name|download
operator|.
name|end
argument_list|(
name|pageContext
operator|.
name|getOut
argument_list|()
argument_list|,
literal|""
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|doEndTag
argument_list|()
return|;
block|}
specifier|private
name|void
name|evaluateExpressions
parameter_list|()
throws|throws
name|JspException
block|{
name|ExpressionTool
name|exprTool
init|=
operator|new
name|ExpressionTool
argument_list|(
name|pageContext
argument_list|,
name|this
argument_list|,
literal|"download"
argument_list|)
decl_stmt|;
comment|// Handle required properties.
name|groupId
operator|=
name|exprTool
operator|.
name|requiredString
argument_list|(
literal|"groupId"
argument_list|,
name|groupId_
argument_list|)
expr_stmt|;
name|artifactId
operator|=
name|exprTool
operator|.
name|requiredString
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId_
argument_list|)
expr_stmt|;
name|version
operator|=
name|exprTool
operator|.
name|requiredString
argument_list|(
literal|"version"
argument_list|,
name|version_
argument_list|)
expr_stmt|;
comment|// Handle optional properties
name|mini
operator|=
name|exprTool
operator|.
name|optionalBoolean
argument_list|(
literal|"mini"
argument_list|,
name|mini_
argument_list|,
literal|false
argument_list|)
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
name|artifactId_
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
name|groupId_
operator|=
name|groupId
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
name|version_
operator|=
name|version
expr_stmt|;
block|}
block|}
end_class

end_unit

