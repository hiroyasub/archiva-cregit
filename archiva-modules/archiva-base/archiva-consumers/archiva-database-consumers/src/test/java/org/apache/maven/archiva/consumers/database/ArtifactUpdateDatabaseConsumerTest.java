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
name|database
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|common
operator|.
name|utils
operator|.
name|BaseFile
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|FileType
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
name|configuration
operator|.
name|FileTypes
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|consumers
operator|.
name|functors
operator|.
name|ConsumerWantsFilePredicate
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

begin_class
specifier|public
class|class
name|ArtifactUpdateDatabaseConsumerTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|File
name|repoLocation
decl_stmt|;
specifier|protected
name|KnownRepositoryContentConsumer
name|consumer
decl_stmt|;
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
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|FileType
name|fileType
init|=
operator|(
name|FileType
operator|)
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getFileTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|,
name|fileType
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|fileType
operator|.
name|addPattern
argument_list|(
literal|"**/*.xml"
argument_list|)
expr_stmt|;
name|repoLocation
operator|=
name|getTestFile
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/test-repo"
argument_list|)
expr_stmt|;
name|consumer
operator|=
operator|(
name|KnownRepositoryContentConsumer
operator|)
name|lookup
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|,
literal|"update-db-artifact"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConsumption
parameter_list|()
block|{
name|File
name|localFile
init|=
operator|new
name|File
argument_list|(
name|repoLocation
argument_list|,
literal|"org/apache/maven/plugins/maven-plugin-plugin/2.4.1/maven-metadata.xml"
argument_list|)
decl_stmt|;
name|ConsumerWantsFilePredicate
name|predicate
init|=
operator|new
name|ConsumerWantsFilePredicate
argument_list|()
decl_stmt|;
name|BaseFile
name|baseFile
init|=
operator|new
name|BaseFile
argument_list|(
name|repoLocation
argument_list|,
name|localFile
argument_list|)
decl_stmt|;
name|predicate
operator|.
name|setBasefile
argument_list|(
name|baseFile
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|predicate
operator|.
name|evaluate
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConsumptionOfOtherMetadata
parameter_list|()
block|{
name|File
name|localFile
init|=
operator|new
name|File
argument_list|(
name|repoLocation
argument_list|,
literal|"org/apache/maven/plugins/maven-plugin-plugin/2.4.1/maven-metadata-central.xml"
argument_list|)
decl_stmt|;
name|ConsumerWantsFilePredicate
name|predicate
init|=
operator|new
name|ConsumerWantsFilePredicate
argument_list|()
decl_stmt|;
name|BaseFile
name|baseFile
init|=
operator|new
name|BaseFile
argument_list|(
name|repoLocation
argument_list|,
name|localFile
argument_list|)
decl_stmt|;
name|predicate
operator|.
name|setBasefile
argument_list|(
name|baseFile
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|predicate
operator|.
name|evaluate
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

