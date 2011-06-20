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
operator|.
name|parent
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractBrowseTest
extends|extends
name|AbstractArchivaTest
block|{
comment|// Browse
specifier|public
name|void
name|goToBrowsePage
parameter_list|()
block|{
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
literal|"/archiva/browse"
argument_list|)
expr_stmt|;
name|assertBrowsePage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertBrowsePage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Browse Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Browse Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Groups"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

