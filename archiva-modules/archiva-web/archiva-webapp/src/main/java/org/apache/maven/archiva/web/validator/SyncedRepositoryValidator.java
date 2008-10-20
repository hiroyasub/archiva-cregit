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
comment|/**  * Validator for synced repository form. The values to be validated depends on the  * selected sync method to be used.  *  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  */
end_comment

begin_class
specifier|public
class|class
name|SyncedRepositoryValidator
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
name|method
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"method"
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
name|method
operator|.
name|equals
argument_list|(
literal|"rsync"
argument_list|)
condition|)
block|{
name|String
name|rsyncHost
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"rsyncHost"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|rsyncHost
operator|==
literal|null
operator|||
name|rsyncHost
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Rsync host is required."
argument_list|)
expr_stmt|;
block|}
name|String
name|rsyncDirectory
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"rsyncDirectory"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|rsyncDirectory
operator|==
literal|null
operator|||
name|rsyncDirectory
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Rsync directory is required."
argument_list|)
expr_stmt|;
block|}
name|String
name|rsyncMethod
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"rsyncMethod"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|rsyncMethod
operator|==
literal|null
operator|||
name|rsyncMethod
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Rsync method is required."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|rsyncMethod
operator|.
name|equals
argument_list|(
literal|"anonymous"
argument_list|)
operator|&&
operator|!
name|rsyncMethod
operator|.
name|equals
argument_list|(
literal|"ssh"
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Invalid rsync method"
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|username
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"username"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|==
literal|null
operator|||
name|username
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Username is required."
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|method
operator|.
name|equals
argument_list|(
literal|"svn"
argument_list|)
condition|)
block|{
name|String
name|svnUrl
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"svnUrl"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|svnUrl
operator|==
literal|null
operator|||
name|svnUrl
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"SVN url is required."
argument_list|)
expr_stmt|;
block|}
name|String
name|username
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"username"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|==
literal|null
operator|||
name|username
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Username is required."
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|method
operator|.
name|equals
argument_list|(
literal|"cvs"
argument_list|)
condition|)
block|{
name|String
name|cvsRoot
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"cvsRoot"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|cvsRoot
operator|==
literal|null
operator|||
name|cvsRoot
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"CVS root is required."
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|method
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
name|String
name|directory
init|=
operator|(
name|String
operator|)
name|getFieldValue
argument_list|(
literal|"directory"
argument_list|,
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|directory
operator|==
literal|null
operator|||
name|directory
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|ctxt
operator|.
name|addActionError
argument_list|(
literal|"Directory is required."
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

