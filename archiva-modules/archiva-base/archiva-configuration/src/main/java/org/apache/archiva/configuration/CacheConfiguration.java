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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Cache configuration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|CacheConfiguration
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      * TimeToIdleSeconds.      */
specifier|private
name|int
name|timeToIdleSeconds
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * TimeToLiveSeconds.      */
specifier|private
name|int
name|timeToLiveSeconds
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * max elements in memory.      */
specifier|private
name|int
name|maxElementsInMemory
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * max elements on disk.      */
specifier|private
name|int
name|maxElementsOnDisk
init|=
operator|-
literal|1
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get max elements in memory.      *       * @return int      */
specifier|public
name|int
name|getMaxElementsInMemory
parameter_list|()
block|{
return|return
name|this
operator|.
name|maxElementsInMemory
return|;
block|}
comment|//-- int getMaxElementsInMemory()
comment|/**      * Get max elements on disk.      *       * @return int      */
specifier|public
name|int
name|getMaxElementsOnDisk
parameter_list|()
block|{
return|return
name|this
operator|.
name|maxElementsOnDisk
return|;
block|}
comment|//-- int getMaxElementsOnDisk()
comment|/**      * Get timeToIdleSeconds.      *       * @return int      */
specifier|public
name|int
name|getTimeToIdleSeconds
parameter_list|()
block|{
return|return
name|this
operator|.
name|timeToIdleSeconds
return|;
block|}
comment|//-- int getTimeToIdleSeconds()
comment|/**      * Get timeToLiveSeconds.      *       * @return int      */
specifier|public
name|int
name|getTimeToLiveSeconds
parameter_list|()
block|{
return|return
name|this
operator|.
name|timeToLiveSeconds
return|;
block|}
comment|//-- int getTimeToLiveSeconds()
comment|/**      * Set max elements in memory.      *       * @param maxElementsInMemory      */
specifier|public
name|void
name|setMaxElementsInMemory
parameter_list|(
name|int
name|maxElementsInMemory
parameter_list|)
block|{
name|this
operator|.
name|maxElementsInMemory
operator|=
name|maxElementsInMemory
expr_stmt|;
block|}
comment|//-- void setMaxElementsInMemory( int )
comment|/**      * Set max elements on disk.      *       * @param maxElementsOnDisk      */
specifier|public
name|void
name|setMaxElementsOnDisk
parameter_list|(
name|int
name|maxElementsOnDisk
parameter_list|)
block|{
name|this
operator|.
name|maxElementsOnDisk
operator|=
name|maxElementsOnDisk
expr_stmt|;
block|}
comment|//-- void setMaxElementsOnDisk( int )
comment|/**      * Set timeToIdleSeconds.      *       * @param timeToIdleSeconds      */
specifier|public
name|void
name|setTimeToIdleSeconds
parameter_list|(
name|int
name|timeToIdleSeconds
parameter_list|)
block|{
name|this
operator|.
name|timeToIdleSeconds
operator|=
name|timeToIdleSeconds
expr_stmt|;
block|}
comment|//-- void setTimeToIdleSeconds( int )
comment|/**      * Set timeToLiveSeconds.      *       * @param timeToLiveSeconds      */
specifier|public
name|void
name|setTimeToLiveSeconds
parameter_list|(
name|int
name|timeToLiveSeconds
parameter_list|)
block|{
name|this
operator|.
name|timeToLiveSeconds
operator|=
name|timeToLiveSeconds
expr_stmt|;
block|}
comment|//-- void setTimeToLiveSeconds( int )
block|}
end_class

end_unit

