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
name|web
operator|.
name|validator
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|validator
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|validator
operator|.
name|ValidatorContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|validator
operator|.
name|validators
operator|.
name|ValidatorSupport
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|IntervalValidator
extends|extends
name|ValidatorSupport
block|{
specifier|public
name|void
name|validate
parameter_list|(
name|Object
name|obj
parameter_list|)
throws|throws
name|ValidationException
block|{
name|String
name|snapshotsPolicy
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"snapshotsPolicy"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
name|String
name|releasesPolicy
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"releasesPolicy"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
name|Integer
name|snapshotsInterval
init|=
operator|(
name|Integer
operator|)
name|getFieldValue
argument_list|(
literal|"snapshotsInterval"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
name|Integer
name|releasesInterval
init|=
operator|(
name|Integer
operator|)
name|getFieldValue
argument_list|(
literal|"releasesInterval"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
name|ValidatorContext
name|ctxt
init|=
name|getValidatorContext
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|snapshotsPolicy
operator|.
name|equals
argument_list|(
literal|"interval"
argument_list|)
condition|)
block|{
if|if
condition|(
name|snapshotsInterval
operator|.
name|intValue
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Snapshots Interval must be set to zero."
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|releasesPolicy
operator|.
name|equals
argument_list|(
literal|"interval"
argument_list|)
condition|)
block|{
if|if
condition|(
name|releasesInterval
operator|.
name|intValue
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Releases Interval must be set to zero."
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ctxt
operator|.
name|hasActionErrors
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
block|}
end_class

end_unit

