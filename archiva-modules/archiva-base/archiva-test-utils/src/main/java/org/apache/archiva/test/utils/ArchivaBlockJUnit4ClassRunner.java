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
name|BlockJUnit4ClassRunner
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
name|ArchivaBlockJUnit4ClassRunner
extends|extends
name|BlockJUnit4ClassRunner
block|{
specifier|public
name|ArchivaBlockJUnit4ClassRunner
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|klass
parameter_list|)
throws|throws
name|InitializationError
block|{
name|super
argument_list|(
name|klass
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
block|}
end_class

end_unit

