begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Keys - utility methods for converting common objects into string keys.   *  *  */
end_comment

begin_class
specifier|public
class|class
name|Keys
block|{
specifier|public
specifier|static
name|String
name|toKey
parameter_list|(
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
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
name|groupId
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|classifier
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|toKey
parameter_list|(
name|ArtifactReference
name|ref
parameter_list|)
block|{
return|return
name|toKey
argument_list|(
name|ref
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|ref
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|ref
operator|.
name|getVersion
argument_list|()
argument_list|,
name|ref
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|ref
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|toKey
parameter_list|(
name|ProjectReference
name|ref
parameter_list|)
block|{
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
name|ref
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|ref
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|toKey
parameter_list|(
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
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
name|groupId
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|version
argument_list|)
expr_stmt|;
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|toKey
parameter_list|(
name|VersionedReference
name|ref
parameter_list|)
block|{
return|return
name|toKey
argument_list|(
name|ref
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|ref
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|ref
operator|.
name|getVersion
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

