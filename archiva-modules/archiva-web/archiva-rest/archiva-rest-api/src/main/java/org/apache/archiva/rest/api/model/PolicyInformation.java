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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|policies
operator|.
name|PolicyOption
import|;
end_import

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
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"policyInformation"
argument_list|)
specifier|public
class|class
name|PolicyInformation
implements|implements
name|Serializable
block|{
specifier|private
name|List
argument_list|<
name|PolicyOption
argument_list|>
name|options
decl_stmt|;
specifier|private
name|PolicyOption
name|defaultOption
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|public
name|PolicyInformation
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|PolicyInformation
parameter_list|(
name|List
argument_list|<
name|PolicyOption
argument_list|>
name|options
parameter_list|,
name|PolicyOption
name|defaultOption
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
name|this
operator|.
name|defaultOption
operator|=
name|defaultOption
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|PolicyOption
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
specifier|public
name|void
name|setOptions
parameter_list|(
name|List
argument_list|<
name|PolicyOption
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
specifier|public
name|PolicyOption
name|getDefaultOption
parameter_list|()
block|{
return|return
name|defaultOption
return|;
block|}
specifier|public
name|void
name|setDefaultOption
parameter_list|(
name|PolicyOption
name|defaultOption
parameter_list|)
block|{
name|this
operator|.
name|defaultOption
operator|=
name|defaultOption
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
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
literal|"PolicyInformation"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{options="
argument_list|)
operator|.
name|append
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", defaultOption='"
argument_list|)
operator|.
name|append
argument_list|(
name|defaultOption
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
literal|", id='"
argument_list|)
operator|.
name|append
argument_list|(
name|id
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
literal|", name='"
argument_list|)
operator|.
name|append
argument_list|(
name|name
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
block|}
end_class

end_unit

