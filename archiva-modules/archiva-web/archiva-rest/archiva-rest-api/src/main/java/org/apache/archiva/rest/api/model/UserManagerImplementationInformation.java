begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M4  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"userManagerImplementationInformation"
argument_list|)
specifier|public
class|class
name|UserManagerImplementationInformation
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|beanId
decl_stmt|;
specifier|private
name|String
name|descriptionKey
decl_stmt|;
specifier|public
name|UserManagerImplementationInformation
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|UserManagerImplementationInformation
parameter_list|(
name|String
name|beanId
parameter_list|,
name|String
name|descriptionKey
parameter_list|)
block|{
name|this
operator|.
name|beanId
operator|=
name|beanId
expr_stmt|;
name|this
operator|.
name|descriptionKey
operator|=
name|descriptionKey
expr_stmt|;
block|}
specifier|public
name|String
name|getBeanId
parameter_list|()
block|{
return|return
name|beanId
return|;
block|}
specifier|public
name|void
name|setBeanId
parameter_list|(
name|String
name|beanId
parameter_list|)
block|{
name|this
operator|.
name|beanId
operator|=
name|beanId
expr_stmt|;
block|}
specifier|public
name|String
name|getDescriptionKey
parameter_list|()
block|{
return|return
name|descriptionKey
return|;
block|}
specifier|public
name|void
name|setDescriptionKey
parameter_list|(
name|String
name|descriptionKey
parameter_list|)
block|{
name|this
operator|.
name|descriptionKey
operator|=
name|descriptionKey
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"UserManagerImplementationInformation"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{beanId='"
argument_list|)
operator|.
name|append
argument_list|(
name|beanId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", descriptionKey='"
argument_list|)
operator|.
name|append
argument_list|(
name|descriptionKey
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|UserManagerImplementationInformation
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|UserManagerImplementationInformation
name|that
init|=
operator|(
name|UserManagerImplementationInformation
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|beanId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|beanId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|beanId
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

