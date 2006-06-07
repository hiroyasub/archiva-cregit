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
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DefaultArtifactRepository
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
name|layout
operator|.
name|ArtifactRepositoryLayout
import|;
end_import

begin_comment
comment|/**  * Class to represent the Proxy repository.  Currently does not provide additional methods from  * DefaultArtifactRepository but is expected to do so like enabled/disabled when a UI is present.  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|ProxyRepository
extends|extends
name|DefaultArtifactRepository
block|{
comment|// zero caches forever
specifier|private
name|long
name|cachePeriod
decl_stmt|;
specifier|private
name|boolean
name|cacheFailures
decl_stmt|;
specifier|private
name|boolean
name|hardfail
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|proxied
decl_stmt|;
specifier|public
name|ProxyRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|url
parameter_list|,
name|ArtifactRepositoryLayout
name|layout
parameter_list|,
name|boolean
name|cacheFailures
parameter_list|,
name|long
name|cachePeriod
parameter_list|)
block|{
name|this
argument_list|(
name|id
argument_list|,
name|url
argument_list|,
name|layout
argument_list|)
expr_stmt|;
name|this
operator|.
name|cacheFailures
operator|=
name|cacheFailures
expr_stmt|;
name|this
operator|.
name|cachePeriod
operator|=
name|cachePeriod
expr_stmt|;
block|}
specifier|public
name|ProxyRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|url
parameter_list|,
name|ArtifactRepositoryLayout
name|layout
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|url
argument_list|,
name|layout
argument_list|)
expr_stmt|;
block|}
specifier|public
name|long
name|getCachePeriod
parameter_list|()
block|{
return|return
name|cachePeriod
return|;
block|}
specifier|public
name|void
name|setCachePeriod
parameter_list|(
name|long
name|cachePeriod
parameter_list|)
block|{
name|this
operator|.
name|cachePeriod
operator|=
name|cachePeriod
expr_stmt|;
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
name|boolean
name|isProxied
parameter_list|()
block|{
return|return
name|proxied
return|;
block|}
specifier|public
name|void
name|setProxied
parameter_list|(
name|boolean
name|proxied
parameter_list|)
block|{
name|this
operator|.
name|proxied
operator|=
name|proxied
expr_stmt|;
block|}
comment|/**      * Checks the repository hardfail setting.      *      * @return true if the hardfail is enabled, otherwise, returns false.      */
specifier|public
name|boolean
name|isHardfail
parameter_list|()
block|{
return|return
name|hardfail
return|;
block|}
comment|/**      * If hardfail is set to true, then any unexpected errors from retrieving files from this repository      * will cause the download to fail.      *      * @param hardfail set to true to enable hard failures      */
specifier|public
name|void
name|setHardfail
parameter_list|(
name|boolean
name|hardfail
parameter_list|)
block|{
name|this
operator|.
name|hardfail
operator|=
name|hardfail
expr_stmt|;
block|}
block|}
end_class

end_unit

