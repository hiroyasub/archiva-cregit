begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|checksum
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Status of checksum update for specific algorithm.  */
end_comment

begin_class
specifier|public
class|class
name|UpdateStatus
block|{
comment|/**      * Checksum file did not exist before and was created      */
specifier|public
specifier|static
specifier|final
name|int
name|CREATED
init|=
literal|1
decl_stmt|;
comment|/**      * Checksum file existed, but content differed      */
specifier|public
specifier|static
specifier|final
name|int
name|UPDATED
init|=
literal|2
decl_stmt|;
comment|/**      * Nothing changed      */
specifier|public
specifier|static
specifier|final
name|int
name|NONE
init|=
literal|0
decl_stmt|;
comment|/**      * Error occured during update/creation of the checksum file      */
specifier|public
specifier|static
specifier|final
name|int
name|ERROR
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
specifier|final
name|ChecksumAlgorithm
name|algorithm
decl_stmt|;
specifier|private
specifier|final
name|int
name|status
decl_stmt|;
specifier|private
specifier|final
name|Throwable
name|error
decl_stmt|;
specifier|public
name|UpdateStatus
parameter_list|(
name|ChecksumAlgorithm
name|algorithm
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
name|status
operator|=
name|NONE
expr_stmt|;
name|error
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|UpdateStatus
parameter_list|(
name|ChecksumAlgorithm
name|algorithm
parameter_list|,
name|int
name|status
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
name|error
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|UpdateStatus
parameter_list|(
name|ChecksumAlgorithm
name|algorithm
parameter_list|,
name|Throwable
name|error
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|ERROR
expr_stmt|;
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
comment|/**      * Return the status value.      * @return The value      */
specifier|public
name|int
name|getValue
parameter_list|()
block|{
return|return
name|status
return|;
block|}
comment|/**      * Return error, if exists, otherwise<code>null</code> will be returned.      * @return      */
specifier|public
name|Throwable
name|getError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
comment|/**      * Return the algorithm, this status is assigned to.      * @return The checksum algorithm      */
specifier|public
name|ChecksumAlgorithm
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algorithm
return|;
block|}
block|}
end_class

end_unit

