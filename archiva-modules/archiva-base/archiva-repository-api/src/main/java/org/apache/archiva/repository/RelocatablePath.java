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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|RelocatablePath
block|{
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
specifier|private
specifier|final
name|String
name|originPath
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|relocated
decl_stmt|;
name|RelocatablePath
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|originPath
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|originPath
operator|=
name|originPath
expr_stmt|;
name|this
operator|.
name|relocated
operator|=
operator|!
name|path
operator|.
name|equals
argument_list|(
name|originPath
argument_list|)
expr_stmt|;
block|}
name|RelocatablePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|originPath
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|relocated
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|String
name|getPath
parameter_list|( )
block|{
return|return
name|path
return|;
block|}
specifier|public
name|String
name|getOriginPath
parameter_list|( )
block|{
return|return
name|originPath
return|;
block|}
specifier|public
name|boolean
name|isRelocated
parameter_list|( )
block|{
return|return
name|relocated
return|;
block|}
block|}
end_class

end_unit

