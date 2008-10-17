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
name|xwork2
operator|.
name|util
operator|.
name|ValueStack
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|components
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|views
operator|.
name|jsp
operator|.
name|ComponentTagSupport
import|;
end_import

begin_comment
comment|/**  * GroupIdLink   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|GroupIdLinkTag
extends|extends
name|ComponentTagSupport
block|{
specifier|private
name|String
name|var_
decl_stmt|;
comment|// stores EL-based property
specifier|private
name|String
name|var
decl_stmt|;
comment|// stores the evaluated object.
specifier|private
name|boolean
name|includeTop
init|=
literal|false
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Component
name|getBean
parameter_list|(
name|ValueStack
name|valueStack
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
return|return
operator|new
name|GroupIdLink
argument_list|(
name|valueStack
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|release
parameter_list|()
block|{
name|var_
operator|=
literal|null
expr_stmt|;
name|var
operator|=
literal|null
expr_stmt|;
name|includeTop
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|GroupIdLink
name|groupIdLink
init|=
operator|(
name|GroupIdLink
operator|)
name|component
decl_stmt|;
name|groupIdLink
operator|.
name|setGroupId
argument_list|(
name|var
argument_list|)
expr_stmt|;
name|groupIdLink
operator|.
name|setIncludeTop
argument_list|(
name|includeTop
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
literal|"groupIdLink"
argument_list|)
decl_stmt|;
name|var
operator|=
name|exprTool
operator|.
name|optionalString
argument_list|(
literal|"var"
argument_list|,
name|var_
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setVar
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|var_
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|setIncludeTop
parameter_list|(
name|boolean
name|includeTop
parameter_list|)
block|{
name|this
operator|.
name|includeTop
operator|=
name|includeTop
expr_stmt|;
block|}
block|}
end_class

end_unit

