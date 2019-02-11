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
comment|/**  * Class ProxyConnectorConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|ProxyConnectorConfiguration
extends|extends
name|AbstractRepositoryConnectorConfiguration
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
comment|/**      *       *             The order of the proxy connectors. (0 means no      * order specified)      *           .      */
specifier|private
name|int
name|order
init|=
literal|0
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the order of the proxy connectors. (0 means no order      * specified).      *       * @return int      */
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
return|return
name|this
operator|.
name|order
return|;
block|}
comment|//-- int getOrder()
comment|/**      * Set the order of the proxy connectors. (0 means no order      * specified).      *       * @param order      */
specifier|public
name|void
name|setOrder
parameter_list|(
name|int
name|order
parameter_list|)
block|{
name|this
operator|.
name|order
operator|=
name|order
expr_stmt|;
block|}
comment|//-- void setOrder( int )
comment|/**      * The order id for UNORDERED      */
specifier|public
specifier|static
specifier|final
name|int
name|UNORDERED
init|=
literal|0
decl_stmt|;
comment|/**      * The policy key {@link #getPolicies()} for error handling.      * See {@link org.apache.archiva.policies.DownloadErrorPolicy}      * for details on potential values to this policy key.      */
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_PROPAGATE_ERRORS
init|=
literal|"propagate-errors"
decl_stmt|;
comment|/**      * The policy key {@link #getPolicies()} for error handling when an artifact is present.      * See {@link org.apache.archiva.policies.DownloadErrorPolicy}      * for details on potential values to this policy key.      */
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_PROPAGATE_ERRORS_ON_UPDATE
init|=
literal|"propagate-errors-on-update"
decl_stmt|;
comment|/**      * The policy key {@link #getPolicies()} for snapshot handling.      * See {@link org.apache.archiva.policies.SnapshotsPolicy}      * for details on potential values to this policy key.      */
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_SNAPSHOTS
init|=
literal|"snapshots"
decl_stmt|;
comment|/**      * The policy key {@link #getPolicies()} for releases handling.      * See {@link org.apache.archiva.policies.ReleasesPolicy}      * for details on potential values to this policy key.      */
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_RELEASES
init|=
literal|"releases"
decl_stmt|;
comment|/**      * The policy key {@link #getPolicies()} for checksum handling.      * See {@link org.apache.archiva.policies.ChecksumPolicy}      * for details on potential values to this policy key.      */
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_CHECKSUM
init|=
literal|"checksum"
decl_stmt|;
comment|/**      * The policy key {@link #getPolicies()} for cache-failures handling.      * See {@link org.apache.archiva.policies.CachedFailuresPolicy}      * for details on potential values to this policy key.      */
specifier|public
specifier|static
specifier|final
name|String
name|POLICY_CACHE_FAILURES
init|=
literal|"cache-failures"
decl_stmt|;
block|}
end_class

end_unit

