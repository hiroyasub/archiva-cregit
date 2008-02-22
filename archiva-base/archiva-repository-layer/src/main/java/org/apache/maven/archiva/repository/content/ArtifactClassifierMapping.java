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
name|repository
operator|.
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * ArtifactExtensionMapping  *  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactClassifierMapping
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeToClassifierMap
decl_stmt|;
static|static
block|{
name|typeToClassifierMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|typeToClassifierMap
operator|.
name|put
argument_list|(
literal|"java-sources"
argument_list|,
literal|"sources"
argument_list|)
expr_stmt|;
name|typeToClassifierMap
operator|.
name|put
argument_list|(
literal|"javadoc.jars"
argument_list|,
literal|"javadoc"
argument_list|)
expr_stmt|;
name|typeToClassifierMap
operator|.
name|put
argument_list|(
literal|"javadocs"
argument_list|,
literal|"javadoc"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getClassifier
parameter_list|(
name|String
name|type
parameter_list|)
block|{
comment|// Try specialized types first.
if|if
condition|(
name|typeToClassifierMap
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|typeToClassifierMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|// No classifier
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

