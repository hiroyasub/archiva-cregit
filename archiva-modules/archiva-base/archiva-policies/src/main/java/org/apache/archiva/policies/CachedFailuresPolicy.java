begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_comment
comment|/**  * {@link PreDownloadPolicy} to check if the requested url has failed before.  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"preDownloadPolicy#cache-failures"
argument_list|)
specifier|public
class|class
name|CachedFailuresPolicy
implements|implements
name|PreDownloadPolicy
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CachedFailuresPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The NO policy setting means that the the existence of old failures is<strong>not</strong> checked.      * All resource requests are allowed thru to the remote repo.      */
specifier|public
specifier|static
specifier|final
name|String
name|NO
init|=
literal|"no"
decl_stmt|;
comment|/**      * The YES policy setting means that the existence of old failures is checked, and will      * prevent the request from being performed against the remote repo.      */
specifier|public
specifier|static
specifier|final
name|String
name|YES
init|=
literal|"yes"
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
specifier|private
name|UrlFailureCache
name|urlFailureCache
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|public
name|CachedFailuresPolicy
parameter_list|()
block|{
name|options
operator|.
name|add
argument_list|(
name|NO
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|YES
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
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
throws|throws
name|PolicyViolationException
throws|,
name|PolicyConfigurationException
block|{
if|if
condition|(
operator|!
name|options
operator|.
name|contains
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Not a valid code.
throw|throw
operator|new
name|PolicyConfigurationException
argument_list|(
literal|"Unknown cache-failues policy setting ["
operator|+
name|policySetting
operator|+
literal|"], valid settings are ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|options
operator|.
name|iterator
argument_list|()
argument_list|,
literal|","
argument_list|)
operator|+
literal|"]"
argument_list|)
throw|;
block|}
if|if
condition|(
name|NO
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Skip.
name|log
operator|.
name|debug
argument_list|(
literal|"OK to fetch, check-failures policy set to NO."
argument_list|)
expr_stmt|;
return|return;
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
throw|throw
operator|new
name|PolicyViolationException
argument_list|(
literal|"NO to fetch, check-failures detected previous failure on url: "
operator|+
name|url
argument_list|)
throw|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"OK to fetch, check-failures detected no issues."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDefaultOption
parameter_list|()
block|{
return|return
name|NO
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"cache-failures"
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Cache failures"
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

