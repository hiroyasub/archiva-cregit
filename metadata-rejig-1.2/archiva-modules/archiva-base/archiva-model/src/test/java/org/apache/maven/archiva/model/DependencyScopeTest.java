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
name|model
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

begin_comment
comment|/**  * DependencyScopeTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DependencyScopeTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testIsWithinScope
parameter_list|()
block|{
comment|// Test on blank / empty desired scopes.
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"compile"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"test"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"runtime"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"provided"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"compile"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"test"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"runtime"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"provided"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
comment|// Tests on blank / empty actual scopes.
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|""
argument_list|,
name|DependencyScope
operator|.
name|COMPILE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|null
argument_list|,
name|DependencyScope
operator|.
name|COMPILE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|""
argument_list|,
name|DependencyScope
operator|.
name|TEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|null
argument_list|,
name|DependencyScope
operator|.
name|TEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|""
argument_list|,
name|DependencyScope
operator|.
name|PROVIDED
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|null
argument_list|,
name|DependencyScope
operator|.
name|PROVIDED
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|""
argument_list|,
name|DependencyScope
operator|.
name|RUNTIME
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|null
argument_list|,
name|DependencyScope
operator|.
name|RUNTIME
argument_list|)
argument_list|)
expr_stmt|;
comment|// Tests on compile desired scopes.
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"compile"
argument_list|,
name|DependencyScope
operator|.
name|COMPILE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"test"
argument_list|,
name|DependencyScope
operator|.
name|COMPILE
argument_list|)
argument_list|)
expr_stmt|;
comment|// Tests on test desired scopes.
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"compile"
argument_list|,
name|DependencyScope
operator|.
name|TEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"test"
argument_list|,
name|DependencyScope
operator|.
name|TEST
argument_list|)
argument_list|)
expr_stmt|;
comment|// Tests on oddball scopes.
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"compile"
argument_list|,
name|DependencyScope
operator|.
name|PROVIDED
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"test"
argument_list|,
name|DependencyScope
operator|.
name|PROVIDED
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"provided"
argument_list|,
name|DependencyScope
operator|.
name|PROVIDED
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"compile"
argument_list|,
name|DependencyScope
operator|.
name|RUNTIME
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"test"
argument_list|,
name|DependencyScope
operator|.
name|RUNTIME
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"provided"
argument_list|,
name|DependencyScope
operator|.
name|RUNTIME
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DependencyScope
operator|.
name|isWithinScope
argument_list|(
literal|"runtime"
argument_list|,
name|DependencyScope
operator|.
name|RUNTIME
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

