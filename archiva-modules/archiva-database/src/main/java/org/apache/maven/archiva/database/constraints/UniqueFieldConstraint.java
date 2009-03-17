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
comment|/**  * UniqueFieldConstraint  */
end_comment

begin_class
specifier|public
class|class
name|UniqueFieldConstraint
extends|extends
name|AbstractSimpleConstraint
implements|implements
name|Constraint
block|{
specifier|private
name|String
name|sql
decl_stmt|;
specifier|public
name|UniqueFieldConstraint
parameter_list|(
name|String
name|className
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
name|sql
operator|=
literal|"SELECT "
operator|+
name|fieldName
operator|+
literal|" FROM "
operator|+
name|className
operator|+
literal|" GROUP BY "
operator|+
name|fieldName
operator|+
literal|" ORDER BY "
operator|+
name|fieldName
operator|+
literal|" ASCENDING"
expr_stmt|;
block|}
specifier|public
name|UniqueFieldConstraint
parameter_list|(
name|String
name|className
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|fieldNamePrefix
parameter_list|)
block|{
name|sql
operator|=
literal|"SELECT "
operator|+
name|fieldName
operator|+
literal|" FROM "
operator|+
name|className
operator|+
literal|" WHERE "
operator|+
name|fieldName
operator|+
literal|".startsWith( fieldPrefix ) PARAMETERS String fieldPrefix GROUP BY "
operator|+
name|fieldName
operator|+
literal|" ORDER BY "
operator|+
name|fieldName
operator|+
literal|" ASCENDING"
expr_stmt|;
name|super
operator|.
name|params
operator|=
operator|new
name|Object
index|[]
block|{
name|fieldNamePrefix
block|}
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultClass
parameter_list|()
block|{
return|return
name|String
operator|.
name|class
return|;
block|}
specifier|public
name|String
name|getSelectSql
parameter_list|()
block|{
return|return
name|sql
return|;
block|}
block|}
end_class

end_unit

