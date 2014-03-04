begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_comment
comment|/**  * PathUtil - simple utility methods for path manipulation.  *  *  */
end_comment

begin_class
specifier|public
class|class
name|PathUtil
block|{
specifier|public
specifier|static
name|String
name|toUrl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// Is our work already done for us?
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"file:/"
argument_list|)
condition|)
block|{
return|return
name|path
return|;
block|}
return|return
name|toUrl
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|toUrl
parameter_list|(
name|File
name|file
parameter_list|)
block|{
try|try
block|{
return|return
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|String
name|pathCorrected
init|=
name|StringUtils
operator|.
name|replaceChars
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pathCorrected
operator|.
name|startsWith
argument_list|(
literal|"file:/"
argument_list|)
condition|)
block|{
return|return
name|pathCorrected
return|;
block|}
return|return
literal|"file://"
operator|+
name|pathCorrected
return|;
block|}
block|}
comment|/**      * Given a basedir and a child file, return the relative path to the child.      *      * @param basedir the basedir.      * @param file    the file to get the relative path for.      * @return the relative path to the child. (NOTE: this path will NOT start with a {@link File#separator} character)      */
specifier|public
specifier|static
name|String
name|getRelative
parameter_list|(
name|String
name|basedir
parameter_list|,
name|File
name|file
parameter_list|)
block|{
return|return
name|getRelative
argument_list|(
name|basedir
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Given a basedir and a child file, return the relative path to the child.      *      * @param basedir the basedir.      * @param child   the child path (can be a full path)      * @return the relative path to the child. (NOTE: this path will NOT start with a {@link File#separator} character)      */
specifier|public
specifier|static
name|String
name|getRelative
parameter_list|(
name|String
name|basedir
parameter_list|,
name|String
name|child
parameter_list|)
block|{
if|if
condition|(
name|basedir
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|basedir
operator|.
name|endsWith
argument_list|(
literal|"\\"
argument_list|)
condition|)
block|{
name|basedir
operator|=
name|basedir
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|basedir
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|child
operator|.
name|startsWith
argument_list|(
name|basedir
argument_list|)
condition|)
block|{
comment|// simple solution.
return|return
name|child
operator|.
name|substring
argument_list|(
name|basedir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
return|;
block|}
name|String
name|absoluteBasedir
init|=
operator|new
name|File
argument_list|(
name|basedir
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|startsWith
argument_list|(
name|absoluteBasedir
argument_list|)
condition|)
block|{
comment|// resolved basedir solution.
return|return
name|child
operator|.
name|substring
argument_list|(
name|absoluteBasedir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
return|;
block|}
comment|// File is not within basedir.
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to obtain relative path of file "
operator|+
name|child
operator|+
literal|", it is not within basedir "
operator|+
name|basedir
operator|+
literal|"."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

