begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"networkConfiguration"
argument_list|)
specifier|public
class|class
name|NetworkConfiguration
implements|implements
name|Serializable
block|{
comment|/**      * maximum total external http connections.      */
specifier|private
name|int
name|maxTotal
init|=
literal|30
decl_stmt|;
comment|/**      * maximum total external http connections per host.      */
specifier|private
name|int
name|maxTotalPerHost
init|=
literal|30
decl_stmt|;
specifier|private
name|boolean
name|usePooling
init|=
literal|true
decl_stmt|;
specifier|public
name|NetworkConfiguration
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|NetworkConfiguration
parameter_list|(
name|int
name|maxTotal
parameter_list|,
name|int
name|maxTotalPerHost
parameter_list|,
name|boolean
name|usePooling
parameter_list|)
block|{
name|this
operator|.
name|maxTotal
operator|=
name|maxTotal
expr_stmt|;
name|this
operator|.
name|maxTotalPerHost
operator|=
name|maxTotalPerHost
expr_stmt|;
name|this
operator|.
name|usePooling
operator|=
name|usePooling
expr_stmt|;
block|}
specifier|public
name|int
name|getMaxTotal
parameter_list|()
block|{
return|return
name|maxTotal
return|;
block|}
specifier|public
name|void
name|setMaxTotal
parameter_list|(
name|int
name|maxTotal
parameter_list|)
block|{
name|this
operator|.
name|maxTotal
operator|=
name|maxTotal
expr_stmt|;
block|}
specifier|public
name|int
name|getMaxTotalPerHost
parameter_list|()
block|{
return|return
name|maxTotalPerHost
return|;
block|}
specifier|public
name|void
name|setMaxTotalPerHost
parameter_list|(
name|int
name|maxTotalPerHost
parameter_list|)
block|{
name|this
operator|.
name|maxTotalPerHost
operator|=
name|maxTotalPerHost
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUsePooling
parameter_list|()
block|{
return|return
name|usePooling
return|;
block|}
specifier|public
name|void
name|setUsePooling
parameter_list|(
name|boolean
name|usePooling
parameter_list|)
block|{
name|this
operator|.
name|usePooling
operator|=
name|usePooling
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
literal|"NetworkConfiguration"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{maxTotal="
argument_list|)
operator|.
name|append
argument_list|(
name|maxTotal
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", maxTotalPerHost="
argument_list|)
operator|.
name|append
argument_list|(
name|maxTotalPerHost
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", usePooling="
argument_list|)
operator|.
name|append
argument_list|(
name|usePooling
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

