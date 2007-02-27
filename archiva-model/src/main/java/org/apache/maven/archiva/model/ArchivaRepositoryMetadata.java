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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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

begin_comment
comment|/**  * ArchivaRepositoryMetadata   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaRepositoryMetadata
implements|implements
name|RepositoryContent
block|{
specifier|private
name|List
name|availableVersions
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|RepositoryContentKey
name|key
decl_stmt|;
specifier|private
name|String
name|releasedVersion
decl_stmt|;
specifier|public
name|ArchivaRepositoryMetadata
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
operator|new
name|RepositoryContentKey
argument_list|(
name|repository
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getAvailableVersions
parameter_list|()
block|{
return|return
name|availableVersions
return|;
block|}
specifier|public
name|String
name|getReleasedVersion
parameter_list|()
block|{
return|return
name|releasedVersion
return|;
block|}
specifier|public
name|RepositoryContentKey
name|getRepositoryContentKey
parameter_list|()
block|{
return|return
name|this
operator|.
name|key
return|;
block|}
specifier|public
name|void
name|setAvailableVersions
parameter_list|(
name|List
name|availableVersions
parameter_list|)
block|{
name|this
operator|.
name|availableVersions
operator|=
name|availableVersions
expr_stmt|;
block|}
specifier|public
name|void
name|setReleasedVersion
parameter_list|(
name|String
name|releasedVersion
parameter_list|)
block|{
name|this
operator|.
name|releasedVersion
operator|=
name|releasedVersion
expr_stmt|;
block|}
specifier|public
name|void
name|setRepositoryContentKey
parameter_list|(
name|RepositoryContentKey
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
block|}
end_class

end_unit

