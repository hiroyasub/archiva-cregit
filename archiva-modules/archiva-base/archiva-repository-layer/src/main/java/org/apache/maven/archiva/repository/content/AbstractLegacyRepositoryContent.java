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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|ArtifactReference
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
name|repository
operator|.
name|layout
operator|.
name|LayoutException
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
comment|/**  * AbstractLegacyRepositoryContent  *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractLegacyRepositoryContent
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PATH_SEPARATOR
init|=
literal|"/"
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
name|typeToDirectoryMap
decl_stmt|;
static|static
block|{
name|typeToDirectoryMap
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
name|typeToDirectoryMap
operator|.
name|put
argument_list|(
literal|"ejb-client"
argument_list|,
literal|"ejb"
argument_list|)
expr_stmt|;
name|typeToDirectoryMap
operator|.
name|put
argument_list|(
name|ArtifactExtensionMapping
operator|.
name|MAVEN_ONE_PLUGIN
argument_list|,
literal|"plugin"
argument_list|)
expr_stmt|;
name|typeToDirectoryMap
operator|.
name|put
argument_list|(
literal|"distribution-tgz"
argument_list|,
literal|"distribution"
argument_list|)
expr_stmt|;
name|typeToDirectoryMap
operator|.
name|put
argument_list|(
literal|"distribution-zip"
argument_list|,
literal|"distribution"
argument_list|)
expr_stmt|;
name|typeToDirectoryMap
operator|.
name|put
argument_list|(
literal|"javadoc"
argument_list|,
literal|"javadoc.jar"
argument_list|)
expr_stmt|;
block|}
comment|/**      * @plexus.requirement role-hint="legacy"      */
specifier|private
name|PathParser
name|legacyPathParser
decl_stmt|;
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
return|return
name|legacyPathParser
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
return|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|ArchivaArtifact
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Artifact reference cannot be null"
argument_list|)
throw|;
block|}
return|return
name|toPath
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|,
name|reference
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Artifact reference cannot be null"
argument_list|)
throw|;
block|}
return|return
name|toPath
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|,
name|reference
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|toPath
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
name|StringBuffer
name|path
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|groupId
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|getDirectory
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
name|path
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|ArtifactExtensionMapping
operator|.
name|getExtension
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|getDirectory
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|String
name|dirname
init|=
name|typeToDirectoryMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|dirname
operator|!=
literal|null
condition|)
block|{
return|return
name|dirname
operator|+
literal|"s"
return|;
block|}
comment|// Default process.
return|return
name|type
operator|+
literal|"s"
return|;
block|}
specifier|public
name|void
name|setLegacyPathParser
parameter_list|(
name|PathParser
name|parser
parameter_list|)
block|{
name|this
operator|.
name|legacyPathParser
operator|=
name|parser
expr_stmt|;
block|}
block|}
end_class

end_unit

