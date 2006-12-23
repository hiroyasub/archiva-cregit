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
name|applet
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|applet
operator|.
name|Applet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|*
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_comment
comment|/**  * Applet that takes a file on the local filesystem and checksums it for sending to the server.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|ChecksumApplet
extends|extends
name|Applet
block|{
specifier|private
specifier|static
specifier|final
name|int
name|CHECKSUM_BUFFER_SIZE
init|=
literal|8192
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BYTE_MASK
init|=
literal|0xFF
decl_stmt|;
specifier|private
name|JProgressBar
name|progressBar
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|()
block|{
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|progressBar
operator|=
operator|new
name|JProgressBar
argument_list|()
expr_stmt|;
name|progressBar
operator|.
name|setStringPainted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|progressBar
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|JLabel
name|label
init|=
operator|new
name|JLabel
argument_list|(
literal|"Checksum progress: "
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|label
argument_list|,
name|BorderLayout
operator|.
name|WEST
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|generateMd5
parameter_list|(
specifier|final
name|String
name|file
parameter_list|)
throws|throws
name|IOException
throws|,
name|NoSuchAlgorithmException
block|{
name|Object
name|o
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|()
block|{
specifier|public
name|Object
name|run
parameter_list|()
block|{
try|try
block|{
return|return
name|checksumFile
argument_list|(
name|file
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
return|return
literal|"Error checksumming file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|"Couldn't find the file. "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|"Error reading file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
return|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
return|return
operator|(
name|String
operator|)
name|o
return|;
block|}
specifier|protected
name|String
name|checksumFile
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|NoSuchAlgorithmException
throws|,
name|IOException
block|{
name|MessageDigest
name|digest
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"MD5"
argument_list|)
decl_stmt|;
name|long
name|total
init|=
operator|new
name|File
argument_list|(
name|file
argument_list|)
operator|.
name|length
argument_list|()
decl_stmt|;
name|InputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|long
name|totalRead
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|CHECKSUM_BUFFER_SIZE
index|]
decl_stmt|;
name|int
name|numRead
decl_stmt|;
do|do
block|{
name|numRead
operator|=
name|fis
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
if|if
condition|(
name|numRead
operator|>
literal|0
condition|)
block|{
name|digest
operator|.
name|update
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|numRead
argument_list|)
expr_stmt|;
name|totalRead
operator|+=
name|numRead
expr_stmt|;
name|progressBar
operator|.
name|setValue
argument_list|(
operator|(
name|int
operator|)
operator|(
name|totalRead
operator|*
name|progressBar
operator|.
name|getMaximum
argument_list|()
operator|/
name|total
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|numRead
operator|!=
operator|-
literal|1
condition|)
do|;
block|}
finally|finally
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|byteArrayToHexStr
argument_list|(
name|digest
operator|.
name|digest
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|String
name|byteArrayToHexStr
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|String
name|output
init|=
literal|""
decl_stmt|;
for|for
control|(
name|int
name|cnt
init|=
literal|0
init|;
name|cnt
operator|<
name|data
operator|.
name|length
condition|;
name|cnt
operator|++
control|)
block|{
comment|//Deposit a byte into the 8 lsb of an int.
name|int
name|tempInt
init|=
name|data
index|[
name|cnt
index|]
operator|&
name|BYTE_MASK
decl_stmt|;
comment|//Get hex representation of the int as a string.
name|String
name|tempStr
init|=
name|Integer
operator|.
name|toHexString
argument_list|(
name|tempInt
argument_list|)
decl_stmt|;
comment|//Append a leading 0 if necessary so that each hex string will contain 2 characters.
if|if
condition|(
name|tempStr
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
name|tempStr
operator|=
literal|"0"
operator|+
name|tempStr
expr_stmt|;
block|}
comment|//Concatenate the two characters to the output string.
name|output
operator|=
name|output
operator|+
name|tempStr
expr_stmt|;
block|}
return|return
name|output
operator|.
name|toUpperCase
argument_list|()
return|;
block|}
block|}
end_class

end_unit

