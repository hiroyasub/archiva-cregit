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
name|xml
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|io
operator|.
name|OutputFormat
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
name|io
operator|.
name|Writer
import|;
end_import

begin_comment
comment|/**  * XMLWriter   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|XMLWriter
block|{
specifier|public
specifier|static
name|void
name|write
parameter_list|(
name|Document
name|doc
parameter_list|,
name|Writer
name|writer
parameter_list|)
throws|throws
name|XMLException
block|{
name|org
operator|.
name|dom4j
operator|.
name|io
operator|.
name|XMLWriter
name|xmlwriter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|OutputFormat
name|outputFormat
init|=
name|OutputFormat
operator|.
name|createPrettyPrint
argument_list|()
decl_stmt|;
name|xmlwriter
operator|=
operator|new
name|org
operator|.
name|dom4j
operator|.
name|io
operator|.
name|XMLWriter
argument_list|(
name|writer
argument_list|,
name|outputFormat
argument_list|)
expr_stmt|;
name|xmlwriter
operator|.
name|write
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|xmlwriter
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"Unable to write xml contents to writer: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|xmlwriter
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|xmlwriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|/* quietly ignore */
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

