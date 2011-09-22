begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
operator|.
name|util
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|security
operator|.
name|common
operator|.
name|ArchivaRoleConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * WebdavMethodUtil  *  * @version $Id: WebdavMethodUtil.java 5412 2007-01-13 01:18:47Z joakime $  */
end_comment

begin_class
specifier|public
class|class
name|WebdavMethodUtil
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|READ_METHODS
decl_stmt|;
static|static
block|{
name|READ_METHODS
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|READ_METHODS
operator|.
name|add
argument_list|(
literal|"HEAD"
argument_list|)
expr_stmt|;
name|READ_METHODS
operator|.
name|add
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|READ_METHODS
operator|.
name|add
argument_list|(
literal|"PROPFIND"
argument_list|)
expr_stmt|;
name|READ_METHODS
operator|.
name|add
argument_list|(
literal|"OPTIONS"
argument_list|)
expr_stmt|;
name|READ_METHODS
operator|.
name|add
argument_list|(
literal|"REPORT"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getMethodPermission
parameter_list|(
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|method
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"WebDAV method is empty"
argument_list|)
throw|;
block|}
if|if
condition|(
name|READ_METHODS
operator|.
name|contains
argument_list|(
name|method
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
return|;
block|}
if|else if
condition|(
literal|"DELETE"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_DELETE
return|;
block|}
else|else
block|{
return|return
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
return|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isReadMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|method
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"WebDAV method is empty"
argument_list|)
throw|;
block|}
return|return
name|READ_METHODS
operator|.
name|contains
argument_list|(
name|method
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

