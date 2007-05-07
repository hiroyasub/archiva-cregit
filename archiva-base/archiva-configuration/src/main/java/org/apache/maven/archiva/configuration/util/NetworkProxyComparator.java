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
name|configuration
operator|.
name|util
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|NetworkProxyConfiguration
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
comment|/**  * NetworkProxyComparator   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|NetworkProxyComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
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
if|if
condition|(
operator|(
name|o1
operator|instanceof
name|NetworkProxyConfiguration
operator|)
operator|&&
operator|(
name|o2
operator|instanceof
name|NetworkProxyConfiguration
operator|)
condition|)
block|{
name|String
name|id1
init|=
operator|(
operator|(
name|NetworkProxyConfiguration
operator|)
name|o1
operator|)
operator|.
name|getId
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
operator|(
operator|(
name|NetworkProxyConfiguration
operator|)
name|o2
operator|)
operator|.
name|getId
argument_list|()
decl_stmt|;
return|return
name|id1
operator|.
name|compareToIgnoreCase
argument_list|(
name|id2
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

