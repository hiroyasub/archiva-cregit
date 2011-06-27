begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|test
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
name|archiva
operator|.
name|web
operator|.
name|test
operator|.
name|parent
operator|.
name|AbstractArchivaTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|annotations
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"findartifact"
block|}
argument_list|,
name|dependsOnGroups
operator|=
block|{
literal|"about"
block|}
argument_list|,
name|sequential
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|FindArtifactTest
extends|extends
name|AbstractArchivaTest
block|{
specifier|public
name|void
name|testFindArtifactNullValues
parameter_list|()
block|{
name|goToFindArtifactPage
argument_list|()
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must select a file, or enter the checksum. If the file was given and you receive this message, there may have been an error generating the checksum."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFindArtifactUsingChecksum
parameter_list|()
block|{
name|goToFindArtifactPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"checksumSearch_q"
argument_list|,
literal|"8e896baea663a45d7bd2737f8e464481"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Search"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"No results found"
argument_list|)
expr_stmt|;
block|}
comment|// TODO: test using file upload on Firefox versions that support getAsBinary (ie, no applet)
block|}
end_class

end_unit

