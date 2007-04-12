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

begin_comment
comment|/**  * {@link PreDownloadPolicy} to apply for released versions.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.policies.download.PreDownloadPolicy"  *                   role-hint="releases"  */
end_comment

begin_class
specifier|public
class|class
name|ReleasesPolicy
extends|extends
name|AbstractUpdatePolicy
implements|implements
name|PreDownloadPolicy
block|{
specifier|public
name|String
name|getDefaultPolicySetting
parameter_list|()
block|{
return|return
name|AbstractUpdatePolicy
operator|.
name|IGNORED
return|;
block|}
specifier|protected
name|boolean
name|isSnapshotPolicy
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

