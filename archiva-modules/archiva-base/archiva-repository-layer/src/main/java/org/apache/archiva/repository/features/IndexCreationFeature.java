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
name|features
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  *  * This feature provides some information about index creation.  *  */
end_comment

begin_class
specifier|public
class|class
name|IndexCreationFeature
implements|implements
name|RepositoryFeature
argument_list|<
name|IndexCreationFeature
argument_list|>
block|{
specifier|private
name|boolean
name|skipPackedIndexCreation
init|=
literal|false
decl_stmt|;
specifier|public
name|IndexCreationFeature
parameter_list|(
name|boolean
name|skipPackedIndexCreation
parameter_list|)
block|{
name|this
operator|.
name|skipPackedIndexCreation
operator|=
name|skipPackedIndexCreation
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|IndexCreationFeature
name|getFeature
parameter_list|()
block|{
return|return
name|this
return|;
block|}
comment|/**      * Returns true, if no packed index files should be created.      * @return True, if no packed index files are created, otherwise false.      */
specifier|public
name|boolean
name|isSkipPackedIndexCreation
parameter_list|()
block|{
return|return
name|skipPackedIndexCreation
return|;
block|}
comment|/**      * Sets the flag for packed index creation.      *      * @param skipPackedIndexCreation      */
specifier|public
name|void
name|setSkipPackedIndexCreation
parameter_list|(
name|boolean
name|skipPackedIndexCreation
parameter_list|)
block|{
name|this
operator|.
name|skipPackedIndexCreation
operator|=
name|skipPackedIndexCreation
expr_stmt|;
block|}
block|}
end_class

end_unit

