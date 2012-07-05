begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|model
operator|.
name|ArtifactMetadata
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
name|RepositoryPathTranslator
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
name|Maven2RepositoryPathTranslator
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
name|MavenArtifactFacet
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
name|AbstractRepositoryLayerTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * ArtifactExtensionMappingTest  *  *  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactExtensionMappingTest
extends|extends
name|AbstractRepositoryLayerTestCase
block|{
specifier|private
name|RepositoryPathTranslator
name|pathTranslator
init|=
operator|new
name|Maven2RepositoryPathTranslator
argument_list|(
name|Collections
operator|.
expr|<
name|ArtifactMappingProvider
operator|>
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testIsMavenPlugin
parameter_list|()
block|{
name|assertMavenPlugin
argument_list|(
literal|"maven-test-plugin"
argument_list|)
expr_stmt|;
name|assertMavenPlugin
argument_list|(
literal|"maven-clean-plugin"
argument_list|)
expr_stmt|;
name|assertMavenPlugin
argument_list|(
literal|"cobertura-maven-plugin"
argument_list|)
expr_stmt|;
name|assertMavenPlugin
argument_list|(
literal|"maven-project-info-reports-plugin"
argument_list|)
expr_stmt|;
name|assertMavenPlugin
argument_list|(
literal|"silly-name-for-a-maven-plugin"
argument_list|)
expr_stmt|;
name|assertNotMavenPlugin
argument_list|(
literal|"maven-plugin-api"
argument_list|)
expr_stmt|;
name|assertNotMavenPlugin
argument_list|(
literal|"foo-lib"
argument_list|)
expr_stmt|;
name|assertNotMavenPlugin
argument_list|(
literal|"another-maven-plugin-api"
argument_list|)
expr_stmt|;
name|assertNotMavenPlugin
argument_list|(
literal|"secret-maven-plugin-2"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertMavenPlugin
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Should be detected as maven plugin:<"
operator|+
name|artifactId
operator|+
literal|">"
argument_list|,
literal|"maven-plugin"
argument_list|,
name|getTypeFromArtifactId
argument_list|(
name|artifactId
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getTypeFromArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|ArtifactMetadata
name|artifact
init|=
name|pathTranslator
operator|.
name|getArtifactFromId
argument_list|(
literal|null
argument_list|,
literal|"groupId"
argument_list|,
name|artifactId
argument_list|,
literal|"1.0"
argument_list|,
name|artifactId
operator|+
literal|"-1.0.jar"
argument_list|)
decl_stmt|;
name|MavenArtifactFacet
name|facet
init|=
operator|(
name|MavenArtifactFacet
operator|)
name|artifact
operator|.
name|getFacet
argument_list|(
name|MavenArtifactFacet
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
return|return
name|facet
operator|.
name|getType
argument_list|()
return|;
block|}
specifier|private
name|void
name|assertNotMavenPlugin
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"Should NOT be detected as maven plugin:<"
operator|+
name|artifactId
operator|+
literal|">"
argument_list|,
literal|"maven-plugin"
operator|.
name|equals
argument_list|(
name|getTypeFromArtifactId
argument_list|(
name|artifactId
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

