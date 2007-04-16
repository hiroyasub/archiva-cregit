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
name|policies
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|archiva
operator|.
name|policies
operator|.
name|urlcache
operator|.
name|UrlFailureCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * {@link PreDownloadPolicy} to check if the requested url has failed before.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.policies.PreDownloadPolicy"  *                   role-hint="cache-failures"  */
end_comment

begin_class
specifier|public
class|class
name|CachedFailuresPolicy
extends|extends
name|AbstractLogEnabled
implements|implements
name|PreDownloadPolicy
block|{
comment|/**      * The CACHED policy indicates that if the URL provided exists in the      * cached failures pool, then the policy fails, and the download isn't even       * attempted.      */
specifier|public
specifier|static
specifier|final
name|String
name|CACHED
init|=
literal|"cached"
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|UrlFailureCache
name|urlFailureCache
decl_stmt|;
specifier|private
name|Set
name|validPolicyCodes
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
specifier|public
name|CachedFailuresPolicy
parameter_list|()
block|{
name|validPolicyCodes
operator|.
name|add
argument_list|(
name|IGNORED
argument_list|)
expr_stmt|;
name|validPolicyCodes
operator|.
name|add
argument_list|(
name|CACHED
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|applyPolicy
parameter_list|(
name|String
name|policySetting
parameter_list|,
name|Properties
name|request
parameter_list|,
name|File
name|localFile
parameter_list|)
block|{
if|if
condition|(
operator|!
name|validPolicyCodes
operator|.
name|contains
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// No valid code? false it is then.
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unknown checksum policyCode ["
operator|+
name|policySetting
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|IGNORED
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Ignore.
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"OK to fetch, check-failures policy set to IGNORED."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|String
name|url
init|=
name|request
operator|.
name|getProperty
argument_list|(
literal|"url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|url
argument_list|)
condition|)
block|{
if|if
condition|(
name|urlFailureCache
operator|.
name|hasFailedBefore
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"NO to fetch, check-failures detected previous failure on url: "
operator|+
name|url
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"OK to fetch, check-failures detected no issues."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getDefaultPolicySetting
parameter_list|()
block|{
return|return
name|IGNORED
return|;
block|}
block|}
end_class

end_unit

