begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
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
name|archiva
operator|.
name|proxy
operator|.
name|model
operator|.
name|ProxyConnector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  * ProxyConnectorOrderComparator   *  *  */
end_comment

begin_class
specifier|public
class|class
name|ProxyConnectorOrderComparator
implements|implements
name|Comparator
argument_list|<
name|ProxyConnector
argument_list|>
block|{
specifier|private
specifier|static
name|ProxyConnectorOrderComparator
name|INSTANCE
init|=
operator|new
name|ProxyConnectorOrderComparator
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|ProxyConnectorOrderComparator
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|ProxyConnector
name|o1
parameter_list|,
name|ProxyConnector
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|==
literal|null
operator|&&
name|o2
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// Ensure null goes to end of list.
if|if
condition|(
name|o1
operator|==
literal|null
operator|&&
name|o2
operator|!=
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|o1
operator|!=
literal|null
operator|&&
name|o2
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// Ensure 0 (unordered) goes to end of list.
if|if
condition|(
name|o1
operator|.
name|getOrder
argument_list|()
operator|==
literal|0
operator|&&
name|o2
operator|.
name|getOrder
argument_list|()
operator|!=
literal|0
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|o1
operator|.
name|getOrder
argument_list|()
operator|!=
literal|0
operator|&&
name|o2
operator|.
name|getOrder
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|o1
operator|.
name|getOrder
argument_list|()
operator|-
name|o2
operator|.
name|getOrder
argument_list|()
return|;
block|}
block|}
end_class

end_unit

