begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|legacy
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|RemoteRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArtifactReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
operator|.
name|RepositoryURL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|RemoteRepositoryContent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|legacy
operator|.
name|AbstractLegacyRepositoryContent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|LayoutException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
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

begin_comment
comment|/**  * RemoteLegacyRepositoryContent  *  *  * TODO no need to be a component once legacy path parser is not  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"remoteRepositoryContent#legacy"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|RemoteLegacyRepositoryContent
extends|extends
name|AbstractLegacyRepositoryContent
implements|implements
name|RemoteRepositoryContent
block|{
specifier|private
name|RemoteRepository
name|repository
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|repository
operator|.
name|getId
argument_list|()
return|;
block|}
specifier|public
name|RemoteRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|RepositoryURL
name|getURL
parameter_list|()
block|{
return|return
operator|new
name|RepositoryURL
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|RemoteRepository
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
comment|/**      * Convert a path to an artifact reference.      *      * @param path the path to convert. (relative or full url path)      * @throws org.apache.archiva.repository.layout.LayoutException if the path cannot be converted to an artifact reference.      */
annotation|@
name|Override
specifier|public
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|toArtifactReference
argument_list|(
name|path
operator|.
name|substring
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
return|;
block|}
specifier|public
name|RepositoryURL
name|toURL
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
name|String
name|url
init|=
name|repository
operator|.
name|getUrl
argument_list|()
operator|+
name|toPath
argument_list|(
name|reference
argument_list|)
decl_stmt|;
return|return
operator|new
name|RepositoryURL
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
end_class

end_unit

