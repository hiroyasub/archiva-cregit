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
name|maven
operator|.
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|LayoutException
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
name|fail
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractManagedRepositoryContentTest
extends|extends
name|AbstractRepositoryContentTest
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertBadPathCi
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|super
operator|.
name|assertBadPathCi
argument_list|(
name|path
argument_list|,
name|reason
argument_list|)
expr_stmt|;
try|try
block|{
name|getManaged
argument_list|()
operator|.
name|toItem
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException on the invalid path ["
operator|+
name|path
operator|+
literal|"] because of ["
operator|+
name|reason
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
block|}
end_class

end_unit

