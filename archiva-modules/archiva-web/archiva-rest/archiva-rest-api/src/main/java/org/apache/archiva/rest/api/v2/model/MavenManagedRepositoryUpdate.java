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
name|v2
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
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

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"MavenManagedRepositoryUpdate"
argument_list|,
name|description
operator|=
literal|"Data object for updating maven managed repositories"
argument_list|)
specifier|public
class|class
name|MavenManagedRepositoryUpdate
extends|extends
name|MavenManagedRepository
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|9181643343284109862L
decl_stmt|;
specifier|private
name|boolean
name|resetStats
init|=
literal|false
decl_stmt|;
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"reset_stats"
argument_list|,
name|description
operator|=
literal|"True, if statistics should be reset after update"
argument_list|)
specifier|public
name|boolean
name|isResetStats
parameter_list|( )
block|{
return|return
name|resetStats
return|;
block|}
specifier|public
name|void
name|setResetStats
parameter_list|(
name|boolean
name|resetStats
parameter_list|)
block|{
name|this
operator|.
name|resetStats
operator|=
name|resetStats
expr_stmt|;
block|}
block|}
end_class

end_unit

