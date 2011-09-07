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

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"remoteRepository"
argument_list|)
specifier|public
class|class
name|RemoteRepository
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|layout
decl_stmt|;
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|String
name|userName
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|int
name|timeOut
init|=
literal|60
decl_stmt|;
specifier|public
name|RemoteRepository
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|RemoteRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|layout
parameter_list|)
block|{
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
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
block|}
specifier|public
name|RemoteRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|layout
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|,
name|int
name|timeOut
parameter_list|)
block|{
name|this
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|url
argument_list|,
name|layout
argument_list|)
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|timeOut
operator|=
name|timeOut
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|String
name|getLayout
parameter_list|()
block|{
return|return
name|this
operator|.
name|layout
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
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
name|void
name|setLayout
parameter_list|(
name|String
name|layout
parameter_list|)
block|{
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
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
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|17
decl_stmt|;
name|result
operator|=
literal|37
operator|*
name|result
operator|+
operator|(
name|id
operator|!=
literal|null
condition|?
name|id
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|other
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
name|other
operator|instanceof
name|RemoteRepository
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|RemoteRepository
name|that
init|=
operator|(
name|RemoteRepository
operator|)
name|other
decl_stmt|;
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|result
operator|=
name|result
operator|&&
operator|(
name|getId
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getId
argument_list|()
operator|==
literal|null
else|:
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getId
argument_list|()
argument_list|)
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
specifier|public
name|int
name|getTimeOut
parameter_list|()
block|{
return|return
name|timeOut
return|;
block|}
specifier|public
name|void
name|setTimeOut
parameter_list|(
name|int
name|timeOut
parameter_list|)
block|{
name|this
operator|.
name|timeOut
operator|=
name|timeOut
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
literal|"RemoteRepository"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{id='"
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
literal|", url='"
argument_list|)
operator|.
name|append
argument_list|(
name|url
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
literal|", layout='"
argument_list|)
operator|.
name|append
argument_list|(
name|layout
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
literal|", userName='"
argument_list|)
operator|.
name|append
argument_list|(
name|userName
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
literal|", password='"
argument_list|)
operator|.
name|append
argument_list|(
name|password
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
literal|", timeOut="
argument_list|)
operator|.
name|append
argument_list|(
name|timeOut
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

