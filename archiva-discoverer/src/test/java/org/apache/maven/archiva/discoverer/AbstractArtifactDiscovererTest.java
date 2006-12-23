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
name|discoverer
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|repository
operator|.
name|ArtifactRepository
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
name|repository
operator|.
name|ArtifactRepositoryFactory
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
name|repository
operator|.
name|layout
operator|.
name|ArtifactRepositoryLayout
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractArtifactDiscovererTest
extends|extends
name|PlexusTestCase
block|{
specifier|protected
name|ArtifactDiscoverer
name|discoverer
decl_stmt|;
specifier|private
name|ArtifactFactory
name|factory
decl_stmt|;
specifier|protected
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|protected
specifier|abstract
name|String
name|getLayout
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|File
name|getRepositoryFile
parameter_list|()
function_decl|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|discoverer
operator|=
operator|(
name|ArtifactDiscoverer
operator|)
name|lookup
argument_list|(
name|ArtifactDiscoverer
operator|.
name|ROLE
argument_list|,
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|=
operator|(
name|ArtifactFactory
operator|)
name|lookup
argument_list|(
name|ArtifactFactory
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|repository
operator|=
name|getRepository
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ArtifactRepository
name|getRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|basedir
init|=
name|getRepositoryFile
argument_list|()
decl_stmt|;
name|ArtifactRepositoryFactory
name|factory
init|=
operator|(
name|ArtifactRepositoryFactory
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|ArtifactRepositoryLayout
name|layout
init|=
operator|(
name|ArtifactRepositoryLayout
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|ROLE
argument_list|,
name|getLayout
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createArtifactRepository
argument_list|(
literal|"discoveryRepo"
argument_list|,
literal|"file://"
operator|+
name|basedir
argument_list|,
name|layout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|Artifact
name|createArtifact
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
name|Artifact
name|artifact
init|=
name|factory
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
specifier|protected
name|Artifact
name|createArtifact
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
name|type
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
specifier|protected
name|Artifact
name|createArtifact
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
name|type
parameter_list|,
name|String
name|classifier
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|)
return|;
block|}
block|}
end_class

end_unit

