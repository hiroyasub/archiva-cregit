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
name|consumers
operator|.
name|lucene
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
name|maven
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|DatabaseCleanupConsumer
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
name|ArchivaArtifactModel
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
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_comment
comment|/**  * LuceneCleanupRemoveIndexedConsumerTest  *   * @version  */
end_comment

begin_class
specifier|public
class|class
name|LuceneCleanupRemoveIndexedConsumerTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|DatabaseCleanupConsumer
name|luceneCleanupRemoveIndexConsumer
decl_stmt|;
specifier|public
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
name|luceneCleanupRemoveIndexConsumer
operator|=
operator|(
name|DatabaseCleanupConsumer
operator|)
name|lookup
argument_list|(
name|DatabaseCleanupConsumer
operator|.
name|class
argument_list|,
literal|"lucene-cleanup"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIfArtifactExists
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-lucene-cleanup"
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|luceneCleanupRemoveIndexConsumer
operator|.
name|processArchivaArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIfArtifactDoesNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"deleted-artifact"
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|luceneCleanupRemoveIndexConsumer
operator|.
name|processArchivaArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ArchivaArtifact
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
name|ArchivaArtifactModel
name|model
init|=
operator|new
name|ArchivaArtifactModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|model
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|model
operator|.
name|setRepositoryId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ArchivaArtifact
argument_list|(
name|model
argument_list|)
return|;
block|}
block|}
end_class

end_unit

