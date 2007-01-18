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
name|org
operator|.
name|apache
operator|.
name|taglibs
operator|.
name|standard
operator|.
name|tag
operator|.
name|common
operator|.
name|core
operator|.
name|NullAttributeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|taglibs
operator|.
name|standard
operator|.
name|tag
operator|.
name|el
operator|.
name|core
operator|.
name|ExpressionUtil
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
comment|/**  * GroupIdLink   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|GroupIdLinkTag
extends|extends
name|TagSupport
block|{
specifier|private
name|String
name|var_
decl_stmt|;
comment|// stores EL-based property
specifier|private
name|Object
name|var
decl_stmt|;
comment|// stores the evaluated object.
specifier|private
name|boolean
name|includeTop
init|=
literal|false
decl_stmt|;
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
name|gidlink
init|=
operator|new
name|GroupIdLink
argument_list|(
name|TagUtils
operator|.
name|getStack
argument_list|(
name|pageContext
argument_list|)
argument_list|,
operator|(
name|HttpServletRequest
operator|)
name|pageContext
operator|.
name|getRequest
argument_list|()
argument_list|,
operator|(
name|HttpServletResponse
operator|)
name|pageContext
operator|.
name|getResponse
argument_list|()
argument_list|)
decl_stmt|;
name|gidlink
operator|.
name|setGroupId
argument_list|(
name|var
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|gidlink
operator|.
name|setIncludeTop
argument_list|(
name|includeTop
argument_list|)
expr_stmt|;
name|gidlink
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
try|try
block|{
name|var
operator|=
name|ExpressionUtil
operator|.
name|evalNotNull
argument_list|(
literal|"groupIdLink"
argument_list|,
literal|"var"
argument_list|,
name|var_
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|this
argument_list|,
name|pageContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullAttributeException
name|e
parameter_list|)
block|{
name|log
argument_list|(
literal|"groupIdLink var is null!"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|var
operator|=
literal|""
expr_stmt|;
block|}
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
specifier|private
name|void
name|log
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|pageContext
operator|.
name|getServletContext
argument_list|()
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
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

