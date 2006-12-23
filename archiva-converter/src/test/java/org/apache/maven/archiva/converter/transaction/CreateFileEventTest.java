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
name|converter
operator|.
name|transaction
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
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
class|class
name|CreateFileEventTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|File
name|testDir
init|=
operator|new
name|File
argument_list|(
name|PlexusTestCase
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target/transaction-tests/create-file"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|testCreateCommitRollback
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"test-file.txt"
argument_list|)
decl_stmt|;
name|CreateFileEvent
name|event
init|=
operator|new
name|CreateFileEvent
argument_list|(
literal|"file contents"
argument_list|,
name|testFile
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Test file is not yet created"
argument_list|,
name|testFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Test file is not yet created"
argument_list|,
name|testFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|rollback
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Test file is has been deleted after rollback"
argument_list|,
name|testFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Test file parent directories has been rolledback too"
argument_list|,
name|testDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"target directory still exists"
argument_list|,
operator|new
name|File
argument_list|(
name|PlexusTestCase
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateCommitRollbackWithBackup
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"test-file.txt"
argument_list|)
decl_stmt|;
name|testFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|testFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|testFile
argument_list|,
literal|"original contents"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|CreateFileEvent
name|event
init|=
operator|new
name|CreateFileEvent
argument_list|(
literal|"modified contents"
argument_list|,
name|testFile
argument_list|)
decl_stmt|;
name|String
name|contents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|testFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Test contents have not changed"
argument_list|,
literal|"original contents"
argument_list|,
name|contents
argument_list|)
expr_stmt|;
name|event
operator|.
name|commit
argument_list|()
expr_stmt|;
name|contents
operator|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|testFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test contents have not changed"
argument_list|,
literal|"modified contents"
argument_list|,
name|contents
argument_list|)
expr_stmt|;
name|event
operator|.
name|rollback
argument_list|()
expr_stmt|;
name|contents
operator|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|testFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test contents have not changed"
argument_list|,
literal|"original contents"
argument_list|,
name|contents
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateRollbackCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"test-file.txt"
argument_list|)
decl_stmt|;
name|CreateFileEvent
name|event
init|=
operator|new
name|CreateFileEvent
argument_list|(
literal|"file contents"
argument_list|,
name|testFile
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Test file is not yet created"
argument_list|,
name|testFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|rollback
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Test file is not yet created"
argument_list|,
name|testFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Test file is not yet created"
argument_list|,
name|testFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|PlexusTestCase
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target/transaction-tests"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

