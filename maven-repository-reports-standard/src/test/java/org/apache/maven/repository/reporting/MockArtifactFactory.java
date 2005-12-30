begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|reporting
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Artifact
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
name|artifact
operator|.
name|factory
operator|.
name|ArtifactFactory
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
name|artifact
operator|.
name|versioning
operator|.
name|VersionRange
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|MockArtifactFactory
implements|implements
name|ArtifactFactory
block|{
specifier|public
name|Artifact
name|createArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|,
name|String
name|s3
parameter_list|,
name|String
name|s4
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createArtifactWithClassifier
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|,
name|String
name|s3
parameter_list|,
name|String
name|s4
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createDependencyArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|VersionRange
name|versionRange
parameter_list|,
name|String
name|s2
parameter_list|,
name|String
name|s3
parameter_list|,
name|String
name|s4
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createDependencyArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|VersionRange
name|versionRange
parameter_list|,
name|String
name|s2
parameter_list|,
name|String
name|s3
parameter_list|,
name|String
name|s4
parameter_list|,
name|String
name|s5
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createDependencyArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|VersionRange
name|versionRange
parameter_list|,
name|String
name|s2
parameter_list|,
name|String
name|s3
parameter_list|,
name|String
name|s4
parameter_list|,
name|String
name|s5
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createBuildArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|,
name|String
name|s3
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createProjectArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createParentArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createPluginArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|VersionRange
name|versionRange
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createProjectArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|,
name|String
name|s3
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|createExtensionArtifact
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|s1
parameter_list|,
name|VersionRange
name|versionRange
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

