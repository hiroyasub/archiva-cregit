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
name|audit
operator|.
name|AuditEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|ArgumentsMatcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
specifier|public
class|class
name|AuditEventArgumentsMatcher
implements|implements
name|ArgumentsMatcher
block|{
specifier|public
name|boolean
name|matches
parameter_list|(
name|Object
index|[]
name|objects
parameter_list|,
name|Object
index|[]
name|objects1
parameter_list|)
block|{
if|if
condition|(
name|objects
operator|.
name|length
operator|!=
literal|1
operator|||
name|objects1
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|AuditEvent
name|o1
init|=
operator|(
name|AuditEvent
operator|)
name|objects
index|[
literal|0
index|]
decl_stmt|;
name|AuditEvent
name|o2
init|=
operator|(
name|AuditEvent
operator|)
name|objects1
index|[
literal|0
index|]
decl_stmt|;
name|o2
operator|.
name|setTimestamp
argument_list|(
name|o1
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
comment|// effectively ignore the timestamp
return|return
name|o1
operator|.
name|equals
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|(
name|Object
index|[]
name|objects
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|objects
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

