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
name|reporting
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0    *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|GenericMockObject
implements|implements
name|InvocationHandler
block|{
name|Map
name|invocations
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|GenericMockObject
parameter_list|()
block|{
comment|//default constructor
block|}
specifier|public
name|GenericMockObject
parameter_list|(
name|Map
name|returnMap
parameter_list|)
block|{
name|invocations
operator|=
name|returnMap
expr_stmt|;
block|}
specifier|public
name|void
name|setExpectedReturns
parameter_list|(
name|Method
name|method
parameter_list|,
name|List
name|returnList
parameter_list|)
block|{
name|invocations
operator|.
name|put
argument_list|(
name|method
argument_list|,
name|returnList
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
operator|!
name|invocations
operator|.
name|containsKey
argument_list|(
name|method
argument_list|)
condition|)
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"No expected return values defined."
argument_list|)
throw|;
name|List
name|returnList
init|=
operator|(
name|List
operator|)
name|invocations
operator|.
name|get
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnList
operator|.
name|size
argument_list|()
operator|<
literal|1
condition|)
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Too few expected return values defined."
argument_list|)
throw|;
return|return
name|returnList
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
end_class

end_unit

