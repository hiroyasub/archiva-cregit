begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|xml
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|test
operator|.
name|utils
operator|.
name|ArchivaBlockJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_comment
comment|/**  * AbstractArchivaXmlTestCase   *  *  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractArchivaXmlTestCase
extends|extends
name|TestCase
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|OSLASH
init|=
literal|"\u00f8"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|TRYGVIS
init|=
literal|"Trygve Laugst"
operator|+
name|OSLASH
operator|+
literal|"l"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|INFIN
init|=
literal|"\u221e"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|INFINITE_ARCHIVA
init|=
literal|"The "
operator|+
name|INFIN
operator|+
literal|" Archiva"
decl_stmt|;
specifier|protected
name|Path
name|getExampleXml
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|Path
name|examplesDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/examples"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|examplesDir
argument_list|)
condition|)
block|{
name|fail
argument_list|(
literal|"Missing the examples directory: "
operator|+
name|examplesDir
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Path
name|exampleFile
init|=
name|examplesDir
operator|.
name|resolve
argument_list|(
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|exampleFile
argument_list|)
condition|)
block|{
name|fail
argument_list|(
literal|"Missing the example xml file: "
operator|+
name|exampleFile
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exampleFile
return|;
block|}
block|}
end_class

end_unit

