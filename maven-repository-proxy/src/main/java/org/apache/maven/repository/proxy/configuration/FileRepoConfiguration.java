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
name|configuration
package|;
end_package

begin_comment
comment|/*  * Copyright 2003-2004 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * Strips file:/// off the front of the configured URL and uses that to find files locally.  *  * @author Ben Walding  */
end_comment

begin_class
specifier|public
class|class
name|FileRepoConfiguration
extends|extends
name|RepoConfiguration
block|{
specifier|private
specifier|final
name|String
name|basePath
decl_stmt|;
specifier|public
name|FileRepoConfiguration
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|description
parameter_list|,
name|boolean
name|copy
parameter_list|,
name|boolean
name|hardFail
parameter_list|,
name|boolean
name|cacheFailures
parameter_list|,
name|long
name|cachePeriod
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|url
argument_list|,
name|description
argument_list|,
name|copy
argument_list|,
name|hardFail
argument_list|,
name|cacheFailures
argument_list|,
name|cachePeriod
argument_list|)
expr_stmt|;
name|basePath
operator|=
name|url
operator|.
name|substring
argument_list|(
literal|8
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getBasePath
parameter_list|()
block|{
return|return
name|basePath
return|;
block|}
comment|/**      * Given a relative path, returns the absolute file in the repository      */
specifier|public
name|File
name|getLocalFile
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|basePath
operator|+
name|path
argument_list|)
return|;
block|}
block|}
end_class

end_unit

