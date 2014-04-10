begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"artifactMappingProvider#default"
argument_list|)
specifier|public
class|class
name|DefaultArtifactMappingProvider
implements|implements
name|ArtifactMappingProvider
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|classifierAndExtensionToTypeMap
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeToExtensionMap
decl_stmt|;
specifier|public
name|DefaultArtifactMappingProvider
parameter_list|()
block|{
name|classifierAndExtensionToTypeMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
comment|// Maven 2.2.1 supplied types (excluding defaults where extension == type and no classifier)
name|classifierAndExtensionToTypeMap
operator|.
name|put
argument_list|(
literal|"client:jar"
argument_list|,
literal|"ejb-client"
argument_list|)
expr_stmt|;
name|classifierAndExtensionToTypeMap
operator|.
name|put
argument_list|(
literal|"sources:jar"
argument_list|,
literal|"java-source"
argument_list|)
expr_stmt|;
name|classifierAndExtensionToTypeMap
operator|.
name|put
argument_list|(
literal|"javadoc:jar"
argument_list|,
literal|"javadoc"
argument_list|)
expr_stmt|;
name|classifierAndExtensionToTypeMap
operator|.
name|put
argument_list|(
literal|"tests:jar"
argument_list|,
literal|"test-jar"
argument_list|)
expr_stmt|;
name|typeToExtensionMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
comment|// Maven 2.2.1 supplied types (excluding defaults where extension == type and no classifier)
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
literal|"maven-plugin"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
comment|// Additional type
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"maven-archetype"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
comment|// TODO: move to maven 1 plugin - but note that it won't have the interface type and might need to reproduce the
comment|//       same thing
name|typeToExtensionMap
operator|.
name|put
argument_list|(
literal|"maven-one-plugin"
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
name|String
name|mapClassifierAndExtensionToType
parameter_list|(
name|String
name|classifier
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
if|if
condition|(
name|classifier
operator|==
literal|null
condition|)
block|{
name|classifier
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|ext
operator|==
literal|null
condition|)
block|{
name|ext
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|classifierAndExtensionToTypeMap
operator|.
name|get
argument_list|(
name|classifier
operator|+
literal|":"
operator|+
name|ext
argument_list|)
return|;
block|}
specifier|public
name|String
name|mapTypeToExtension
parameter_list|(
name|String
name|type
parameter_list|)
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
block|}
end_class

end_unit

