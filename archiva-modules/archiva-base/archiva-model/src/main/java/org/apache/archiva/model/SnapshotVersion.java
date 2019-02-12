begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * The Snapshot Version.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|SnapshotVersion
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
comment|/**      *       *             The unique timestamp for the snapshot version.      *                 */
specifier|private
name|String
name|timestamp
decl_stmt|;
comment|/**      * The incremental build number of the snapshot.      */
specifier|private
name|int
name|buildNumber
init|=
literal|0
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the incremental build number of the snapshot.      *       * @return int      */
specifier|public
name|int
name|getBuildNumber
parameter_list|()
block|{
return|return
name|this
operator|.
name|buildNumber
return|;
block|}
comment|//-- int getBuildNumber()
comment|/**      * Get the unique timestamp for the snapshot version.      *       * @return String      */
specifier|public
name|String
name|getTimestamp
parameter_list|()
block|{
return|return
name|this
operator|.
name|timestamp
return|;
block|}
comment|//-- String getTimestamp()
comment|/**      * Set the incremental build number of the snapshot.      *       * @param buildNumber      */
specifier|public
name|void
name|setBuildNumber
parameter_list|(
name|int
name|buildNumber
parameter_list|)
block|{
name|this
operator|.
name|buildNumber
operator|=
name|buildNumber
expr_stmt|;
block|}
comment|//-- void setBuildNumber( int )
comment|/**      * Set the unique timestamp for the snapshot version.      *       * @param timestamp      */
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|String
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
comment|//-- void setTimestamp( String )
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|1251466956496493405L
decl_stmt|;
block|}
end_class

end_unit

