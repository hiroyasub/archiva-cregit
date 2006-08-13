begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepositoryPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A proxied artifact repository - contains the artifact repository and additional information about  * the proxied repository.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|ProxiedArtifactRepository
block|{
comment|/**      * Whether to cache failures or not.      */
specifier|private
name|boolean
name|cacheFailures
decl_stmt|;
comment|/**      * Whether failures on this repository cause the whole group to fail.      */
specifier|private
name|boolean
name|hardFail
decl_stmt|;
comment|/**      * Whether to use the network proxy for any requests.      */
specifier|private
name|boolean
name|useNetworkProxy
decl_stmt|;
comment|/**      * The artifact repository on the other end of the proxy.      */
specifier|private
specifier|final
name|ArtifactRepository
name|repository
decl_stmt|;
comment|/**      * Cache of failures that have already occurred, containing paths from the repository root. The value given      * specifies when the failure should expire.      */
specifier|private
name|Map
comment|/*<String,Long>*/
name|failureCache
init|=
operator|new
name|HashMap
comment|/*<String,Long>*/
argument_list|()
decl_stmt|;
comment|/**      * A user friendly name for the repository.      */
specifier|private
name|String
name|name
decl_stmt|;
specifier|public
name|ProxiedArtifactRepository
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHardFail
parameter_list|()
block|{
return|return
name|hardFail
return|;
block|}
specifier|public
name|boolean
name|isUseNetworkProxy
parameter_list|()
block|{
return|return
name|useNetworkProxy
return|;
block|}
specifier|public
name|boolean
name|isCacheFailures
parameter_list|()
block|{
return|return
name|cacheFailures
return|;
block|}
specifier|public
name|ArtifactRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
comment|/**      * Check if there is a previously cached failure for requesting the given path.      *      * @param path the path      * @return whether there is a failure      */
specifier|public
name|boolean
name|isCachedFailure
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|boolean
name|failed
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|cacheFailures
condition|)
block|{
name|Long
name|time
init|=
operator|(
name|Long
operator|)
name|failureCache
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|time
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|<
name|time
operator|.
name|longValue
argument_list|()
condition|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|clearFailure
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|failed
return|;
block|}
comment|/**      * Add a failure to the cache.      *      * @param path   the path that failed      * @param policy the policy for when the failure should expire      */
specifier|public
name|void
name|addFailure
parameter_list|(
name|String
name|path
parameter_list|,
name|ArtifactRepositoryPolicy
name|policy
parameter_list|)
block|{
name|failureCache
operator|.
name|put
argument_list|(
name|path
argument_list|,
operator|new
name|Long
argument_list|(
name|calculateExpiryTime
argument_list|(
name|policy
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|long
name|calculateExpiryTime
parameter_list|(
name|ArtifactRepositoryPolicy
name|policy
parameter_list|)
block|{
name|String
name|updatePolicy
init|=
name|policy
operator|.
name|getUpdatePolicy
argument_list|()
decl_stmt|;
name|long
name|time
decl_stmt|;
if|if
condition|(
name|ArtifactRepositoryPolicy
operator|.
name|UPDATE_POLICY_ALWAYS
operator|.
name|equals
argument_list|(
name|updatePolicy
argument_list|)
condition|)
block|{
name|time
operator|=
literal|0
expr_stmt|;
block|}
if|else if
condition|(
name|ArtifactRepositoryPolicy
operator|.
name|UPDATE_POLICY_DAILY
operator|.
name|equals
argument_list|(
name|updatePolicy
argument_list|)
condition|)
block|{
comment|// Get midnight boundary
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|time
operator|=
name|cal
operator|.
name|getTime
argument_list|()
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|updatePolicy
operator|.
name|startsWith
argument_list|(
name|ArtifactRepositoryPolicy
operator|.
name|UPDATE_POLICY_INTERVAL
argument_list|)
condition|)
block|{
name|String
name|s
init|=
name|updatePolicy
operator|.
name|substring
argument_list|(
name|ArtifactRepositoryPolicy
operator|.
name|UPDATE_POLICY_INTERVAL
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|minutes
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|,
name|minutes
argument_list|)
expr_stmt|;
name|time
operator|=
name|cal
operator|.
name|getTime
argument_list|()
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// else assume "never"
name|time
operator|=
name|Long
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
return|return
name|time
return|;
block|}
comment|/**      * Remove a failure.      *      * @param path the path that had previously failed      */
specifier|public
name|void
name|clearFailure
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|failureCache
operator|.
name|remove
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setCacheFailures
parameter_list|(
name|boolean
name|cacheFailures
parameter_list|)
block|{
name|this
operator|.
name|cacheFailures
operator|=
name|cacheFailures
expr_stmt|;
block|}
specifier|public
name|void
name|setHardFail
parameter_list|(
name|boolean
name|hardFail
parameter_list|)
block|{
name|this
operator|.
name|hardFail
operator|=
name|hardFail
expr_stmt|;
block|}
specifier|public
name|void
name|setUseNetworkProxy
parameter_list|(
name|boolean
name|useNetworkProxy
parameter_list|)
block|{
name|this
operator|.
name|useNetworkProxy
operator|=
name|useNetworkProxy
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

