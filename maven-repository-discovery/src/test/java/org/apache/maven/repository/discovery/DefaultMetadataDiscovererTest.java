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
name|discovery
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * This class tests the DefaultMetadataDiscoverer class.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultMetadataDiscovererTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|MetadataDiscoverer
name|discoverer
decl_stmt|;
specifier|private
name|File
name|repositoryLocation
decl_stmt|;
comment|/**      *      */
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
name|discoverer
operator|=
operator|(
name|MetadataDiscoverer
operator|)
name|lookup
argument_list|(
name|MetadataDiscoverer
operator|.
name|ROLE
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|repositoryLocation
operator|=
name|getTestFile
argument_list|(
literal|"src/test/repository"
argument_list|)
expr_stmt|;
block|}
comment|/**      *      */
specifier|public
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
name|discoverer
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Test DefaultMetadataDiscoverer when the all metadata paths are valid.      */
specifier|public
name|void
name|testMetadataDiscovererSuccess
parameter_list|()
block|{
name|List
name|metadataPaths
init|=
name|discoverer
operator|.
name|discoverMetadata
argument_list|(
name|repositoryLocation
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check metadata not null"
argument_list|,
name|metadataPaths
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|metadataPaths
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test if metadata file in wrong directory was added to the kickedOutPaths.      */
specifier|public
name|void
name|testKickoutWrongDirectory
parameter_list|()
block|{
name|discoverer
operator|.
name|discoverMetadata
argument_list|(
name|repositoryLocation
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Iterator
name|iter
init|=
name|discoverer
operator|.
name|getKickedOutPathsIterator
argument_list|()
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|)
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|normalizedDir
init|=
name|dir
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"javax/maven-metadata-repository.xml"
operator|.
name|equals
argument_list|(
name|normalizedDir
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test if blank metadata file was added to the kickedOutPaths.      */
specifier|public
name|void
name|testKickoutBlankMetadata
parameter_list|()
block|{
name|discoverer
operator|.
name|discoverMetadata
argument_list|(
name|repositoryLocation
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Iterator
name|iter
init|=
name|discoverer
operator|.
name|getKickedOutPathsIterator
argument_list|()
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|)
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|normalizedDir
init|=
name|dir
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"org/apache/maven/some-ejb/1.0/maven-metadata-repository.xml"
operator|.
name|equals
argument_list|(
name|normalizedDir
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

