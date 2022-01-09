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
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * File Locking configuration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|FileLockConfiguration
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
comment|/**      * skipping the locking mechanism.      */
specifier|private
name|boolean
name|skipLocking
init|=
literal|true
decl_stmt|;
comment|/**      * maximum time to wait to get the file lock (0 infinite).      */
specifier|private
name|int
name|lockingTimeout
init|=
literal|0
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get maximum time to wait to get the file lock (0 infinite).      *       * @return int      */
specifier|public
name|int
name|getLockingTimeout
parameter_list|()
block|{
return|return
name|this
operator|.
name|lockingTimeout
return|;
block|}
comment|//-- int getLockingTimeout()
comment|/**      * Get skipping the locking mechanism.      *       * @return boolean      */
specifier|public
name|boolean
name|isSkipLocking
parameter_list|()
block|{
return|return
name|this
operator|.
name|skipLocking
return|;
block|}
comment|//-- boolean isSkipLocking()
comment|/**      * Set maximum time to wait to get the file lock (0 infinite).      *       * @param lockingTimeout      */
specifier|public
name|void
name|setLockingTimeout
parameter_list|(
name|int
name|lockingTimeout
parameter_list|)
block|{
name|this
operator|.
name|lockingTimeout
operator|=
name|lockingTimeout
expr_stmt|;
block|}
comment|//-- void setLockingTimeout( int )
comment|/**      * Set skipping the locking mechanism.      *       * @param skipLocking      */
specifier|public
name|void
name|setSkipLocking
parameter_list|(
name|boolean
name|skipLocking
parameter_list|)
block|{
name|this
operator|.
name|skipLocking
operator|=
name|skipLocking
expr_stmt|;
block|}
comment|//-- void setSkipLocking( boolean )
block|}
end_class

end_unit

