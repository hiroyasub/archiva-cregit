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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
operator|.
name|ArtifactMappingProvider
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
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
operator|.
name|DefaultArtifactMappingProvider
import|;
end_import

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
comment|/**  * ArtifactExtensionMapping  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactExtensionMapping
block|{
specifier|public
specifier|static
specifier|final
name|String
name|MAVEN_ARCHETYPE
init|=
literal|"maven-archetype"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAVEN_PLUGIN
init|=
literal|"maven-plugin"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAVEN_ONE_PLUGIN
init|=
literal|"maven-one-plugin"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeToExtensionMap
decl_stmt|;
comment|// TODO: won't support extensions - need to refactor away this class
specifier|private
specifier|static
specifier|final
name|ArtifactMappingProvider
name|mapping
init|=
operator|new
name|DefaultArtifactMappingProvider
argument_list|()
decl_stmt|;
static|static
block|{
name|typeToExtensionMap
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
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"ejb-client"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"ejb"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"java-source"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"javadoc"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"test-jar"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
name|MAVEN_PLUGIN
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
name|MAVEN_ARCHETYPE
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
comment|// TODO: move to maven 1 plugin
name|typeToExtensionMap
operator|.
name|put
argument_list|(
name|MAVEN_ONE_PLUGIN
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"javadoc.jar"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"uberjar"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"distribution-tgz"
argument_list|,
literal|"tar.gz"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"distribution-zip"
argument_list|,
literal|"zip"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"aspect"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getExtension
parameter_list|(
name|String
name|type
parameter_list|)
block|{
comment|// Try specialized types first.
if|if
condition|(
name|typeToExtensionMap
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|typeToExtensionMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|// Return type
return|return
name|type
return|;
block|}
specifier|public
specifier|static
name|String
name|mapExtensionAndClassifierToType
parameter_list|(
name|String
name|classifier
parameter_list|,
name|String
name|extension
parameter_list|)
block|{
return|return
name|mapExtensionAndClassifierToType
argument_list|(
name|classifier
argument_list|,
name|extension
argument_list|,
name|extension
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|mapExtensionAndClassifierToType
parameter_list|(
name|String
name|classifier
parameter_list|,
name|String
name|extension
parameter_list|,
name|String
name|defaultExtension
parameter_list|)
block|{
name|String
name|value
init|=
name|mapping
operator|.
name|mapClassifierAndExtensionToType
argument_list|(
name|classifier
argument_list|,
name|extension
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|mapToMaven1Type
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
return|return
name|value
operator|!=
literal|null
condition|?
name|value
else|:
name|defaultExtension
return|;
block|}
specifier|public
specifier|static
name|String
name|mapExtensionToType
parameter_list|(
name|String
name|extension
parameter_list|)
block|{
name|String
name|value
init|=
name|mapToMaven1Type
argument_list|(
name|extension
argument_list|)
decl_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
else|:
name|extension
return|;
block|}
specifier|private
specifier|static
name|String
name|mapToMaven1Type
parameter_list|(
name|String
name|extension
parameter_list|)
block|{
comment|// TODO: Maven 1 plugin
name|String
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"tar.gz"
operator|.
name|equals
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|value
operator|=
literal|"distribution-tgz"
expr_stmt|;
block|}
if|else  if
condition|(
literal|"tar.bz2"
operator|.
name|equals
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|value
operator|=
literal|"distribution-bzip"
expr_stmt|;
block|}
if|else  if
condition|(
literal|"zip"
operator|.
name|equals
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|value
operator|=
literal|"distribution-zip"
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

