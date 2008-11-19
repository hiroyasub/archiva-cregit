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
name|Constraint
import|;
end_import

begin_comment
comment|/**  * ArchivaRepositoryByUrlConstraint  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaRepositoryByUrlConstraint
extends|extends
name|AbstractDeclarativeConstraint
implements|implements
name|Constraint
block|{
specifier|private
name|String
name|whereCondition
decl_stmt|;
specifier|public
name|ArchivaRepositoryByUrlConstraint
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|whereCondition
operator|=
literal|"this.url == '"
operator|+
name|url
operator|+
literal|"'"
expr_stmt|;
block|}
specifier|public
name|String
name|getWhereCondition
parameter_list|()
block|{
return|return
name|whereCondition
return|;
block|}
specifier|public
name|String
name|getSortColumn
parameter_list|()
block|{
return|return
literal|"url"
return|;
block|}
block|}
end_class

end_unit

