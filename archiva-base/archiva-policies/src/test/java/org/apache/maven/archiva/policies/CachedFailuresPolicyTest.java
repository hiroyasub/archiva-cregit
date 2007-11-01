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
name|PlexusTestCase
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
name|Properties
import|;
end_import

begin_comment
comment|/**  * CachedFailuresPolicyTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|CachedFailuresPolicyTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|DownloadPolicy
name|lookupPolicy
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|(
name|DownloadPolicy
operator|)
name|lookup
argument_list|(
name|PreDownloadPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"cache-failures"
argument_list|)
return|;
block|}
specifier|private
name|UrlFailureCache
name|lookupUrlFailureCache
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|(
name|UrlFailureCache
operator|)
name|lookup
argument_list|(
name|UrlFailureCache
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"default"
argument_list|)
return|;
block|}
specifier|private
name|File
name|getFile
parameter_list|()
block|{
return|return
operator|new
name|File
argument_list|(
literal|"target/cache-failures/"
operator|+
name|getName
argument_list|()
operator|+
literal|".txt"
argument_list|)
return|;
block|}
specifier|private
name|Properties
name|createRequest
parameter_list|()
block|{
name|Properties
name|request
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
return|return
name|request
return|;
block|}
specifier|public
name|void
name|testPolicyNo
parameter_list|()
throws|throws
name|Exception
block|{
name|DownloadPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|File
name|localFile
init|=
name|getFile
argument_list|()
decl_stmt|;
name|Properties
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setProperty
argument_list|(
literal|"url"
argument_list|,
literal|"http://a.bad.hostname.maven.org/path/to/resource.txt"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|applyPolicy
argument_list|(
name|CachedFailuresPolicy
operator|.
name|NO
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPolicyYesNotInCache
parameter_list|()
throws|throws
name|Exception
block|{
name|DownloadPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|File
name|localFile
init|=
name|getFile
argument_list|()
decl_stmt|;
name|Properties
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setProperty
argument_list|(
literal|"url"
argument_list|,
literal|"http://a.bad.hostname.maven.org/path/to/resource.txt"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|applyPolicy
argument_list|(
name|CachedFailuresPolicy
operator|.
name|YES
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPolicyYesInCache
parameter_list|()
throws|throws
name|Exception
block|{
name|UrlFailureCache
name|urlFailureCache
init|=
name|lookupUrlFailureCache
argument_list|()
decl_stmt|;
name|DownloadPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|File
name|localFile
init|=
name|getFile
argument_list|()
decl_stmt|;
name|Properties
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|String
name|url
init|=
literal|"http://a.bad.hostname.maven.org/path/to/resource.txt"
decl_stmt|;
name|urlFailureCache
operator|.
name|cacheFailure
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|request
operator|.
name|setProperty
argument_list|(
literal|"url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
try|try
block|{
name|policy
operator|.
name|applyPolicy
argument_list|(
name|CachedFailuresPolicy
operator|.
name|YES
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected a PolicyViolationException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyViolationException
name|e
parameter_list|)
block|{
comment|// expected path.
block|}
block|}
block|}
end_class

end_unit

