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
comment|/**  * ArtifactsBySha1ChecksumConstraint   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactsBySha1ChecksumConstraint
extends|extends
name|AbstractConstraint
implements|implements
name|Constraint
block|{
specifier|private
name|String
name|whereClause
decl_stmt|;
specifier|public
name|ArtifactsBySha1ChecksumConstraint
parameter_list|(
name|String
name|desiredChecksum
parameter_list|)
block|{
name|whereClause
operator|=
literal|"this.checksumSHA1 == desiredChecksum"
expr_stmt|;
name|declParams
operator|=
operator|new
name|String
index|[]
block|{
literal|"String desiredChecksum"
block|}
expr_stmt|;
name|params
operator|=
operator|new
name|Object
index|[]
block|{
name|desiredChecksum
block|}
expr_stmt|;
block|}
specifier|public
name|String
name|getSortColumn
parameter_list|()
block|{
return|return
literal|"groupId"
return|;
block|}
specifier|public
name|String
name|getWhereCondition
parameter_list|()
block|{
return|return
name|whereClause
return|;
block|}
block|}
end_class

end_unit

