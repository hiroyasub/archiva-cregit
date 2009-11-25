begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|beans
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|xmlrpc
operator|.
name|ServiceBean
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|xmlrpc
operator|.
name|ServiceBeanField
import|;
end_import

begin_class
annotation|@
name|ServiceBean
specifier|public
class|class
name|Artifact
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|repositoryId
decl_stmt|;
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
comment|//private Date whenGathered;
specifier|public
name|Artifact
parameter_list|()
block|{
block|}
specifier|public
name|Artifact
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|type
parameter_list|)
comment|//                     String type, Date whenGathered )
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
comment|//this.whenGathered = whenGathered;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
comment|/*public Date getWhenGathered()     {         return whenGathered;     }*/
annotation|@
name|ServiceBeanField
argument_list|(
literal|"groupId"
argument_list|)
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
annotation|@
name|ServiceBeanField
argument_list|(
literal|"artifactId"
argument_list|)
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
annotation|@
name|ServiceBeanField
argument_list|(
literal|"version"
argument_list|)
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
annotation|@
name|ServiceBeanField
argument_list|(
literal|"type"
argument_list|)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
annotation|@
name|ServiceBeanField
argument_list|(
literal|"repositoryId"
argument_list|)
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
comment|/*@ServiceBeanField( "whenGathered" )     public void setWhenGathered( Date whenGathered )     {         this.whenGathered = whenGathered;     }*/
block|}
end_class

end_unit

