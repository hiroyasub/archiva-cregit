begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|test
operator|.
name|utils
package|;
end_package

begin_comment
comment|/*  * Copyright 2012 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|FrameworkMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|InitializationError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|TestContextManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @author Eric  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaSpringJUnit4ClassRunner
extends|extends
name|SpringJUnit4ClassRunner
block|{
static|static
block|{
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"archiva.user.configFileName"
argument_list|)
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"archiva.user.configFileName"
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|Path
name|file
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
literal|"archiva-test-conf"
argument_list|,
literal|".xml"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"archiva.user.configFileName"
argument_list|,
name|file
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|ArchivaSpringJUnit4ClassRunner
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|InitializationError
block|{
name|super
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|FrameworkMethod
argument_list|>
name|computeTestMethods
parameter_list|()
block|{
return|return
name|ListGenerator
operator|.
name|getShuffleList
argument_list|(
name|super
operator|.
name|computeTestMethods
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Statement
name|withAfterClasses
parameter_list|(
specifier|final
name|Statement
name|statement
parameter_list|)
block|{
specifier|final
name|TestContextManager
name|cm
init|=
name|getTestContextManager
argument_list|( )
decl_stmt|;
return|return
operator|new
name|Statement
argument_list|( )
block|{
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|( )
throws|throws
name|Throwable
block|{
name|statement
operator|.
name|evaluate
argument_list|()
expr_stmt|;
name|cm
operator|.
name|getTestContext
argument_list|( )
operator|.
name|markApplicationContextDirty
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

