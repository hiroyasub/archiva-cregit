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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * ArtifactExtensionMapping  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
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
specifier|private
specifier|static
specifier|final
name|Pattern
name|mavenPluginPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(maven-.*-plugin)|(.*-maven-plugin)$"
argument_list|)
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
literal|"java-source"
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
literal|"javadoc"
argument_list|,
literal|"jar"
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
operator|(
name|String
operator|)
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
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'.'
argument_list|)
return|;
block|}
comment|/**      * Determine if a given artifact Id conforms to the naming scheme for a maven plugin.      *      * @param artifactId the artifactId to test.      * @return true if this artifactId conforms to the naming scheme for a maven plugin.      */
specifier|public
specifier|static
name|boolean
name|isMavenPlugin
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
return|return
name|mavenPluginPattern
operator|.
name|matcher
argument_list|(
name|artifactId
argument_list|)
operator|.
name|matches
argument_list|()
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
if|if
condition|(
literal|"sources"
operator|.
name|equals
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
return|return
literal|"java-source"
return|;
block|}
if|else if
condition|(
literal|"javadoc"
operator|.
name|equals
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
return|return
literal|"javadoc"
return|;
block|}
return|return
name|mapExtensionToType
argument_list|(
name|extension
argument_list|)
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
if|if
condition|(
name|extension
operator|.
name|equals
argument_list|(
literal|"tar.gz"
argument_list|)
condition|)
block|{
return|return
literal|"distribution-tgz"
return|;
block|}
if|else  if
condition|(
name|extension
operator|.
name|equals
argument_list|(
literal|"tar.bz2"
argument_list|)
condition|)
block|{
return|return
literal|"distribution-bzip"
return|;
block|}
if|else  if
condition|(
name|extension
operator|.
name|equals
argument_list|(
literal|"zip"
argument_list|)
condition|)
block|{
return|return
literal|"distribution-zip"
return|;
block|}
return|return
name|extension
return|;
block|}
block|}
end_class

end_unit

