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
name|database
operator|.
name|updater
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
name|database
operator|.
name|AbstractArchivaDatabaseTestCase
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
name|database
operator|.
name|ArtifactDAO
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
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * DatabaseUpdaterTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DatabaseUpdaterTest
extends|extends
name|AbstractArchivaDatabaseTestCase
block|{
specifier|private
name|DatabaseUpdater
name|dbupdater
decl_stmt|;
specifier|public
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
name|whenProcessed
parameter_list|)
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Artifact should not be null."
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|Date
name|dateWhenProcessed
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|whenProcessed
operator|!=
literal|null
condition|)
block|{
name|dateWhenProcessed
operator|=
name|toDate
argument_list|(
name|whenProcessed
argument_list|)
expr_stmt|;
block|}
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenProcessed
argument_list|(
name|dateWhenProcessed
argument_list|)
expr_stmt|;
comment|// Satisfy table / column requirements.
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setLastModified
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
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
name|ArtifactDAO
name|adao
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Artifact DAO should not be null."
argument_list|,
name|adao
argument_list|)
expr_stmt|;
name|adao
operator|.
name|saveArtifact
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-common"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|adao
operator|.
name|saveArtifact
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-utils"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|adao
operator|.
name|saveArtifact
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-old"
argument_list|,
literal|"0.1"
argument_list|,
literal|"2004/02/15 9:01:00"
argument_list|)
argument_list|)
expr_stmt|;
name|adao
operator|.
name|saveArtifact
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-database"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|dbupdater
operator|=
operator|(
name|DatabaseUpdater
operator|)
name|lookup
argument_list|(
name|DatabaseUpdater
operator|.
name|class
argument_list|,
literal|"jdo"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"DatabaseUpdater should not be null."
argument_list|,
name|dbupdater
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateUnprocessed
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|groupId
init|=
literal|"org.apache.maven.archiva"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"archiva-utils"
decl_stmt|;
name|String
name|version
init|=
literal|"1.0-SNAPSHOT"
decl_stmt|;
name|String
name|classifier
init|=
literal|""
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|TestDatabaseUnprocessedConsumer
name|consumer
init|=
name|lookupTestUnprocessedConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|resetCount
argument_list|()
expr_stmt|;
comment|// Check the state of the artifact in the DB.
name|ArchivaArtifact
name|savedArtifact
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Artifact should not be considered processed (yet)."
argument_list|,
name|savedArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|isProcessed
argument_list|()
argument_list|)
expr_stmt|;
comment|// Update the artifact
name|dbupdater
operator|.
name|updateUnprocessed
argument_list|(
name|savedArtifact
argument_list|)
expr_stmt|;
comment|// Check the update.
name|ArchivaArtifact
name|processed
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Artifact should be flagged as processed."
argument_list|,
name|processed
operator|.
name|getModel
argument_list|()
operator|.
name|isProcessed
argument_list|()
argument_list|)
expr_stmt|;
comment|// Did the unprocessed consumer do it's thing?
name|assertEquals
argument_list|(
literal|"Processed Count."
argument_list|,
literal|1
argument_list|,
name|consumer
operator|.
name|getCountProcessed
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

