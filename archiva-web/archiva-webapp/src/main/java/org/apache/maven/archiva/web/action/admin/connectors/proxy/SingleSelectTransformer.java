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
name|action
operator|.
name|admin
operator|.
name|connectors
operator|.
name|proxy
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
name|commons
operator|.
name|collections
operator|.
name|Transformer
import|;
end_import

begin_comment
comment|/**  * Ensure that input strings are never arrays.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|SingleSelectTransformer
implements|implements
name|Transformer
block|{
specifier|private
specifier|static
name|Transformer
name|INSTANCE
init|=
operator|new
name|SingleSelectTransformer
argument_list|()
decl_stmt|;
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|instanceof
name|String
condition|)
block|{
if|if
condition|(
name|input
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|String
name|arr
index|[]
init|=
operator|(
name|String
index|[]
operator|)
name|input
decl_stmt|;
if|if
condition|(
name|arr
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|arr
index|[
literal|0
index|]
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Transformer
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
block|}
end_class

end_unit

