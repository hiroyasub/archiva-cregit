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
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * MimeTypes  *  * @version $Id: MimeTypes.java 7010 2007-10-25 23:35:02Z joakime $  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"mimeTpes"
argument_list|)
specifier|public
class|class
name|MimeTypes
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_MIME_TYPE
init|=
literal|"application/octet-stream"
decl_stmt|;
specifier|private
name|String
name|resource
init|=
literal|"org/apache/maven/archiva/webdav/util/mime.types"
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mimeMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MimeTypes
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Get the Mime Type for the provided filename.      *      * @param filename the filename to obtain the mime type for.      * @return a mime type String, or null if filename is null, has no extension, or no mime type is associated with it.      */
specifier|public
name|String
name|getMimeType
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|String
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|filename
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|filename
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|value
operator|=
operator|(
name|String
operator|)
name|mimeMap
operator|.
name|get
argument_list|(
name|filename
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|DEFAULT_MIME_TYPE
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|load
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|load
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|file
operator|.
name|isFile
argument_list|()
operator|||
operator|!
name|file
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to load mime types from file "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" : not a readable file."
argument_list|)
expr_stmt|;
return|return;
block|}
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to load mime types from file "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|load
parameter_list|(
name|String
name|resourceName
parameter_list|)
block|{
name|ClassLoader
name|cloader
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
comment|/* Load up the mime types table */
name|URL
name|mimeURL
init|=
name|cloader
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|mimeURL
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to find resource "
operator|+
name|resourceName
argument_list|)
throw|;
block|}
name|InputStream
name|mimeStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|mimeStream
operator|=
name|mimeURL
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|load
argument_list|(
name|mimeStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to load mime map "
operator|+
name|resourceName
operator|+
literal|" : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|mimeStream
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|load
parameter_list|(
name|InputStream
name|mimeStream
parameter_list|)
block|{
name|mimeMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|InputStreamReader
name|reader
init|=
literal|null
decl_stmt|;
name|BufferedReader
name|buf
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reader
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|mimeStream
argument_list|)
expr_stmt|;
name|buf
operator|=
operator|new
name|BufferedReader
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|buf
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// empty line. skip it
continue|continue;
block|}
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
comment|// Comment. skip it
continue|continue;
block|}
name|StringTokenizer
name|tokenizer
init|=
operator|new
name|StringTokenizer
argument_list|(
name|line
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenizer
operator|.
name|countTokens
argument_list|()
operator|>
literal|1
condition|)
block|{
name|String
name|type
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
while|while
condition|(
name|tokenizer
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|extension
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|this
operator|.
name|mimeMap
operator|.
name|put
argument_list|(
name|extension
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to read mime types from input stream : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getResource
parameter_list|()
block|{
return|return
name|resource
return|;
block|}
specifier|public
name|void
name|setResource
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
block|}
block|}
end_class

end_unit

