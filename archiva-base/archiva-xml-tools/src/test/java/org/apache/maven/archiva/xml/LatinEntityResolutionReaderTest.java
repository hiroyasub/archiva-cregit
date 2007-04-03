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
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|io
operator|.
name|FileReader
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
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_comment
comment|/**  * LatinEntityResolutionReaderTest   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|LatinEntityResolutionReaderTest
extends|extends
name|AbstractArchivaXmlTestCase
block|{
comment|/**      * A method to obtain the content of a reader as a String,      * while allowing for specifing the buffer size of the operation.      *       * This method is only really useful for testing a Reader implementation.      *       * @param input the reader to get the input from.      * @param bufsize the buffer size to use.      * @return the contents of the reader as a String.      * @throws IOException if there was an I/O error.      */
specifier|private
name|String
name|toStringFromReader
parameter_list|(
name|Reader
name|input
parameter_list|,
name|int
name|bufsize
parameter_list|)
throws|throws
name|IOException
block|{
name|StringWriter
name|output
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
specifier|final
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
name|bufsize
index|]
decl_stmt|;
name|int
name|n
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
operator|(
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|output
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * This reads a text file from the src/test/examples directory,      * normalizes the end of lines, and returns the contents as a big String.      *       * @param examplePath the name of the file in the src/test/examples directory.      * @return the contents of the provided file      * @throws IOException if there was an I/O error.      */
specifier|private
name|String
name|toStringFromExample
parameter_list|(
name|String
name|examplePath
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|exampleFile
init|=
name|getExampleXml
argument_list|(
name|examplePath
argument_list|)
decl_stmt|;
name|FileReader
name|fileReader
init|=
operator|new
name|FileReader
argument_list|(
name|exampleFile
argument_list|)
decl_stmt|;
name|BufferedReader
name|lineReader
init|=
operator|new
name|BufferedReader
argument_list|(
name|fileReader
argument_list|)
decl_stmt|;
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|boolean
name|hasContent
init|=
literal|false
decl_stmt|;
name|String
name|line
init|=
name|lineReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|hasContent
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|hasContent
operator|=
literal|true
expr_stmt|;
name|line
operator|=
name|lineReader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|assertProperRead
parameter_list|(
name|String
name|sourcePath
parameter_list|,
name|String
name|expectedPath
parameter_list|,
name|int
name|bufsize
parameter_list|)
block|{
try|try
block|{
name|File
name|inputFile
init|=
name|getExampleXml
argument_list|(
name|sourcePath
argument_list|)
decl_stmt|;
name|FileReader
name|fileReader
init|=
operator|new
name|FileReader
argument_list|(
name|inputFile
argument_list|)
decl_stmt|;
name|LatinEntityResolutionReader
name|testReader
init|=
operator|new
name|LatinEntityResolutionReader
argument_list|(
name|fileReader
argument_list|)
decl_stmt|;
name|String
name|actualOutput
init|=
name|toStringFromReader
argument_list|(
name|testReader
argument_list|,
name|bufsize
argument_list|)
decl_stmt|;
name|String
name|expectedOutput
init|=
name|toStringFromExample
argument_list|(
name|expectedPath
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedOutput
argument_list|,
name|actualOutput
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"IOException: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testReaderNormalBufsize
parameter_list|()
throws|throws
name|IOException
block|{
name|assertProperRead
argument_list|(
literal|"no-prolog-with-entities.xml"
argument_list|,
literal|"no-prolog-with-entities.xml-resolved"
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReaderSmallBufsize
parameter_list|()
throws|throws
name|IOException
block|{
name|assertProperRead
argument_list|(
literal|"no-prolog-with-entities.xml"
argument_list|,
literal|"no-prolog-with-entities.xml-resolved"
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReaderRediculouslyTinyBufsize
parameter_list|()
throws|throws
name|IOException
block|{
name|assertProperRead
argument_list|(
literal|"no-prolog-with-entities.xml"
argument_list|,
literal|"no-prolog-with-entities.xml-resolved"
argument_list|,
literal|32
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReaderHugeBufsize
parameter_list|()
throws|throws
name|IOException
block|{
name|assertProperRead
argument_list|(
literal|"no-prolog-with-entities.xml"
argument_list|,
literal|"no-prolog-with-entities.xml-resolved"
argument_list|,
literal|409600
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoLatinEntitiesHugeLine
parameter_list|()
block|{
name|assertProperRead
argument_list|(
literal|"commons-codec-1.2.pom"
argument_list|,
literal|"commons-codec-1.2.pom"
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

