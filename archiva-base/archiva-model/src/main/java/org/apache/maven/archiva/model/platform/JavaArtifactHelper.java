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
name|model
operator|.
name|platform
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
name|model
operator|.
name|ArchivaArtifact
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
name|model
operator|.
name|ArchivaArtifactJavaDetails
import|;
end_import

begin_comment
comment|/**  * Utility methods for working with java platform specific ArchivaArtifacts.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|JavaArtifactHelper
block|{
specifier|public
specifier|static
name|ArchivaArtifactJavaDetails
name|getJavaDetails
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
block|{
name|ArchivaArtifactJavaDetails
name|javaDetails
init|=
operator|(
name|ArchivaArtifactJavaDetails
operator|)
name|artifact
operator|.
name|getPlatformDetails
argument_list|()
decl_stmt|;
if|if
condition|(
name|javaDetails
operator|==
literal|null
condition|)
block|{
name|javaDetails
operator|=
operator|new
name|ArchivaArtifactJavaDetails
argument_list|()
expr_stmt|;
name|artifact
operator|.
name|setPlatformDetails
argument_list|(
name|javaDetails
argument_list|)
expr_stmt|;
block|}
return|return
name|javaDetails
return|;
block|}
block|}
end_class

end_unit

