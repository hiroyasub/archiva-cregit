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
name|Tag
import|;
end_import

begin_comment
comment|/**  * ExpressionTool   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ExpressionTool
block|{
specifier|private
name|PageContext
name|pageContext
decl_stmt|;
specifier|private
name|Tag
name|tag
decl_stmt|;
specifier|private
name|String
name|tagName
decl_stmt|;
specifier|public
name|ExpressionTool
parameter_list|(
name|PageContext
name|pageContext
parameter_list|,
name|Tag
name|tag
parameter_list|,
name|String
name|tagName
parameter_list|)
block|{
name|this
operator|.
name|pageContext
operator|=
name|pageContext
expr_stmt|;
name|this
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
name|this
operator|.
name|tagName
operator|=
name|tagName
expr_stmt|;
block|}
specifier|public
name|boolean
name|optionalBoolean
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|String
name|expression
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
throws|throws
name|JspException
block|{
try|try
block|{
name|Boolean
name|ret
init|=
operator|(
name|Boolean
operator|)
name|ExpressionUtil
operator|.
name|evalNotNull
argument_list|(
name|this
operator|.
name|tagName
argument_list|,
name|propertyName
argument_list|,
name|expression
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|this
operator|.
name|tag
argument_list|,
name|this
operator|.
name|pageContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|ret
operator|.
name|booleanValue
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NullAttributeException
name|e
parameter_list|)
block|{
return|return
name|defaultValue
return|;
block|}
block|}
specifier|public
name|String
name|optionalString
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|String
name|expression
parameter_list|,
name|String
name|defaultValue
parameter_list|)
throws|throws
name|JspException
block|{
try|try
block|{
name|String
name|ret
init|=
operator|(
name|String
operator|)
name|ExpressionUtil
operator|.
name|evalNotNull
argument_list|(
name|this
operator|.
name|tagName
argument_list|,
name|propertyName
argument_list|,
name|expression
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|this
operator|.
name|tag
argument_list|,
name|this
operator|.
name|pageContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|ret
return|;
block|}
catch|catch
parameter_list|(
name|NullAttributeException
name|e
parameter_list|)
block|{
return|return
name|defaultValue
return|;
block|}
block|}
specifier|public
name|String
name|requiredString
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|String
name|expression
parameter_list|)
throws|throws
name|JspException
block|{
try|try
block|{
name|String
name|ret
init|=
operator|(
name|String
operator|)
name|ExpressionUtil
operator|.
name|evalNotNull
argument_list|(
name|this
operator|.
name|tagName
argument_list|,
name|propertyName
argument_list|,
name|expression
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|this
operator|.
name|tag
argument_list|,
name|this
operator|.
name|pageContext
argument_list|)
decl_stmt|;
return|return
name|ret
return|;
block|}
catch|catch
parameter_list|(
name|NullAttributeException
name|e
parameter_list|)
block|{
name|String
name|emsg
init|=
literal|"Required "
operator|+
name|this
operator|.
name|tagName
operator|+
literal|" property ["
operator|+
name|propertyName
operator|+
literal|"] is null!"
decl_stmt|;
name|log
argument_list|(
name|emsg
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JspException
argument_list|(
name|emsg
argument_list|)
throw|;
block|}
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
block|}
end_class

end_unit

