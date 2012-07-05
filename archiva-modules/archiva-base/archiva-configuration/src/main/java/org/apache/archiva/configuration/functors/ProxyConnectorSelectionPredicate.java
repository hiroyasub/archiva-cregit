begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|functors
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
name|configuration
operator|.
name|ProxyConnectorConfiguration
import|;
end_import

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
name|Predicate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * ProxyConnectorPredicate  *  *  */
end_comment

begin_class
specifier|public
class|class
name|ProxyConnectorSelectionPredicate
implements|implements
name|Predicate
block|{
specifier|private
name|String
name|sourceId
decl_stmt|;
specifier|private
name|String
name|targetId
decl_stmt|;
specifier|public
name|ProxyConnectorSelectionPredicate
parameter_list|(
name|String
name|sourceId
parameter_list|,
name|String
name|targetId
parameter_list|)
block|{
name|this
operator|.
name|sourceId
operator|=
name|sourceId
expr_stmt|;
name|this
operator|.
name|targetId
operator|=
name|targetId
expr_stmt|;
block|}
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|boolean
name|satisfies
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|ProxyConnectorConfiguration
condition|)
block|{
name|ProxyConnectorConfiguration
name|connector
init|=
operator|(
name|ProxyConnectorConfiguration
operator|)
name|object
decl_stmt|;
return|return
operator|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|sourceId
argument_list|,
name|connector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|targetId
argument_list|,
name|connector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
operator|)
return|;
block|}
return|return
name|satisfies
return|;
block|}
block|}
end_class

end_unit

