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
name|converter
operator|.
name|artifact
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|io
operator|.
name|IOException
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
name|io
operator|.
name|FileUtils
import|;
end_import

begin_comment
comment|/**  * AsciiFileUtil - conveinence utility for reading / writing ascii files.  *   * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @todo switch to commons-lang and use their high-performance versions of these utility methods.  */
end_comment

begin_class
specifier|public
class|class
name|AsciiFileUtil
block|{
comment|/**      * Read a file into a {@link String} and return it.      *       * @param file the file to read      * @return the {@link String} contents of the file.      * @throws IOException if there was a problem performing this operation.      */
specifier|public
specifier|static
name|String
name|readFile
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|file
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Write the contents of a {@link String} to a file.      *        * @param file the file to write to      * @param content the {@link String} contents to write.      * @throws IOException if there was a problem performing this operation.      */
specifier|public
specifier|static
name|void
name|writeFile
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|IOException
block|{
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|file
argument_list|,
name|content
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

