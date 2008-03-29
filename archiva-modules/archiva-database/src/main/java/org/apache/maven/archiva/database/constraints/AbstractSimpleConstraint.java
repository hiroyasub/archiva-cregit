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
name|database
operator|.
name|constraints
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
name|database
operator|.
name|SimpleConstraint
import|;
end_import

begin_comment
comment|/**  * Simple Constraint abstract for working with nearly-raw SQL strings.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSimpleConstraint
implements|implements
name|SimpleConstraint
block|{
specifier|protected
name|Object
index|[]
name|params
decl_stmt|;
specifier|public
name|Object
index|[]
name|getParameters
parameter_list|()
block|{
return|return
name|params
return|;
block|}
specifier|public
name|String
name|getFetchLimits
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isResultsPersistable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

