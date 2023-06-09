begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|proxy
operator|.
name|mock
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|VersionUtil
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
name|LayoutException
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
name|ItemSelector
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
name|lang3
operator|.
name|StringUtils
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
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"remoteRepositoryContent#mock"
argument_list|)
specifier|public
class|class
name|RemoteRepositoryContentMock
implements|implements
name|RemoteRepositoryContent
block|{
name|RemoteRepository
name|repository
decl_stmt|;
name|RemoteRepositoryContentMock
parameter_list|(
name|RemoteRepository
name|repo
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repo
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|( )
block|{
return|return
name|repository
operator|.
name|getId
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteRepository
name|getRepository
parameter_list|( )
block|{
return|return
name|repository
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setRepository
parameter_list|(
name|RemoteRepository
name|repo
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repo
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toPath
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
block|{
name|String
name|baseVersion
decl_stmt|;
if|if
condition|(
operator|!
name|selector
operator|.
name|hasVersion
argument_list|()
operator|&&
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|selector
operator|.
name|getArtifactVersion
argument_list|()
argument_list|)
condition|)
block|{
name|baseVersion
operator|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|selector
operator|.
name|getArtifactVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|baseVersion
operator|=
name|selector
operator|.
name|getVersion
argument_list|()
expr_stmt|;
block|}
return|return
name|selector
operator|.
name|getNamespace
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|"/"
operator|+
name|selector
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"/"
operator|+
name|baseVersion
operator|+
literal|"/"
operator|+
name|selector
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|selector
operator|.
name|getVersion
argument_list|()
operator|+
operator|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|selector
operator|.
name|getClassifier
argument_list|()
argument_list|)
condition|?
literal|"-"
operator|+
name|selector
operator|.
name|getClassifier
argument_list|()
else|:
literal|""
operator|)
operator|+
literal|"."
operator|+
name|selector
operator|.
name|getType
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ItemSelector
name|toItemSelector
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

