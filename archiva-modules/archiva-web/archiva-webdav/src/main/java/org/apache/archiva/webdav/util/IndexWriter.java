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
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|io
operator|.
name|OutputContext
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|IndexWriter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|IndexWriter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|logicalResource
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Path
argument_list|>
name|localResources
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isVirtual
decl_stmt|;
specifier|public
name|IndexWriter
parameter_list|(
name|DavResource
name|resource
parameter_list|,
name|Path
name|localResource
parameter_list|,
name|String
name|logicalResource
parameter_list|)
block|{
name|this
operator|.
name|localResources
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|localResources
operator|.
name|add
argument_list|(
name|localResource
argument_list|)
expr_stmt|;
name|this
operator|.
name|logicalResource
operator|=
name|logicalResource
expr_stmt|;
name|this
operator|.
name|isVirtual
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|IndexWriter
parameter_list|(
name|DavResource
name|resource
parameter_list|,
name|List
argument_list|<
name|Path
argument_list|>
name|localResources
parameter_list|,
name|String
name|logicalResource
parameter_list|)
block|{
name|this
operator|.
name|logicalResource
operator|=
name|logicalResource
expr_stmt|;
name|this
operator|.
name|localResources
operator|=
name|localResources
expr_stmt|;
name|this
operator|.
name|isVirtual
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|OutputContext
name|outputContext
parameter_list|)
block|{
name|outputContext
operator|.
name|setModificationTime
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|outputContext
operator|.
name|setContentType
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
name|outputContext
operator|.
name|setETag
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// skygo ETag MRM-1127 seems to be fixed
if|if
condition|(
name|outputContext
operator|.
name|hasStream
argument_list|()
condition|)
block|{
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
name|outputContext
operator|.
name|getOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|writeDocumentStart
argument_list|(
name|writer
argument_list|)
expr_stmt|;
try|try
block|{
name|writeHyperlinks
argument_list|(
name|writer
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
literal|"Could not write hyperlinks {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|writeDocumentEnd
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeDocumentStart
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<!DOCTYPE html>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<html>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<head>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<title>Collection: /"
operator|+
name|logicalResource
operator|+
literal|"</title>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<style type=\"text/css\">"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"ul{list-style:none;}"
argument_list|)
expr_stmt|;
name|StringBuilder
name|relative
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"../../"
argument_list|)
decl_stmt|;
if|if
condition|(
name|logicalResource
operator|!=
literal|null
operator|&&
name|logicalResource
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|tmpRelative
init|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|logicalResource
argument_list|,
literal|"\\"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tmpRelative
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|relative
operator|.
name|append
argument_list|(
literal|"../"
argument_list|)
expr_stmt|;
block|}
block|}
name|writer
operator|.
name|println
argument_list|(
literal|".file{background:url("
operator|+
name|relative
operator|.
name|toString
argument_list|()
operator|+
literal|"images/package-x-generic.png) no-repeat scroll 0 0 transparent;}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|".folder{background:url("
operator|+
name|relative
operator|.
name|toString
argument_list|()
operator|+
literal|"images/folder.png) no-repeat scroll 0 0 transparent;}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"a{color:#0088CC;text-decoration: none;padding-left:20px;}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|".collection tr:nth-child(odd){background-color:#fafafa;}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"tr td:nth-child(2){width:150px;color:#cc8800;text-align:right;}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"tr td:nth-child(3){width:150px;color:#0000cc;text-align:center;}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"th td:nth-child(2){width:150px;}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"th td:nth-child(3){width:150px;}"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</style>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<link rel=\"shortcut icon\" href=\"../../favicon.ico\"/>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</head>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<body>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<h3>Collection: /"
operator|+
name|logicalResource
operator|+
literal|"</h3>"
argument_list|)
expr_stmt|;
comment|//Check if not root
if|if
condition|(
name|logicalResource
operator|!=
literal|null
operator|&&
name|logicalResource
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Path
name|file
init|=
name|Paths
operator|.
name|get
argument_list|(
name|logicalResource
argument_list|)
decl_stmt|;
name|String
name|parentName
init|=
name|file
operator|.
name|getParent
argument_list|()
operator|==
literal|null
condition|?
literal|"/"
else|:
name|file
operator|.
name|getParent
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//convert to unix path in case archiva is hosted on windows
name|parentName
operator|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|parentName
argument_list|,
literal|"\\"
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<ul>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<li><a class=\"folder\" href=\"../\">"
operator|+
name|parentName
operator|+
literal|"</a><i><small>(Parent)</small></i></li>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</ul>"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"<table class=\"collection\">"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<tr><th>Name</th><th>Size (Bytes)</th><th>Last Modified</th></tr>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeDocumentEnd
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"</table>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</body>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</html>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeHyperlinks
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|isVirtual
condition|)
block|{
for|for
control|(
name|Path
name|localResource
range|:
name|localResources
control|)
block|{
name|List
argument_list|<
name|Path
argument_list|>
name|files
init|=
name|Files
operator|.
name|list
argument_list|(
name|localResource
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|( )
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|files
argument_list|)
expr_stmt|;
for|for
control|(
name|Path
name|file
range|:
name|files
control|)
block|{
name|writeHyperlink
argument_list|(
name|writer
argument_list|,
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|file
argument_list|)
operator|.
name|toMillis
argument_list|()
argument_list|,
name|Files
operator|.
name|size
argument_list|(
name|file
argument_list|)
argument_list|,
name|Files
operator|.
name|isDirectory
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// virtual repository - filter unique directories
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|uniqueChildFiles
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|sortedList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Path
name|resource
range|:
name|localResources
control|)
block|{
name|List
argument_list|<
name|Path
argument_list|>
name|files
init|=
name|Files
operator|.
name|list
argument_list|(
name|resource
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Path
name|file
range|:
name|files
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|mergedChildFiles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|uniqueChildFiles
operator|.
name|get
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|mergedChildFiles
operator|.
name|add
argument_list|(
name|file
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mergedChildFiles
operator|=
name|uniqueChildFiles
operator|.
name|get
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|mergedChildFiles
operator|.
name|contains
argument_list|(
name|file
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|mergedChildFiles
operator|.
name|add
argument_list|(
name|file
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|uniqueChildFiles
operator|.
name|put
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|mergedChildFiles
argument_list|)
expr_stmt|;
name|sortedList
operator|.
name|add
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|sortedList
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|written
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|fileName
range|:
name|sortedList
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|childFilesFromMap
init|=
name|uniqueChildFiles
operator|.
name|get
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|childFilePath
range|:
name|childFilesFromMap
control|)
block|{
name|Path
name|childFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|childFilePath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|written
operator|.
name|contains
argument_list|(
name|childFile
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|written
operator|.
name|add
argument_list|(
name|childFile
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|writeHyperlink
argument_list|(
name|writer
argument_list|,
name|fileName
argument_list|,
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|childFile
argument_list|)
operator|.
name|toMillis
argument_list|()
argument_list|,
name|Files
operator|.
name|size
argument_list|(
name|childFile
argument_list|)
argument_list|,
name|Files
operator|.
name|isDirectory
argument_list|(
name|childFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|String
name|fileDateFormat
parameter_list|(
name|long
name|date
parameter_list|)
block|{
name|DateFormat
name|dateFormatter
init|=
name|DateFormat
operator|.
name|getDateTimeInstance
argument_list|(
name|DateFormat
operator|.
name|SHORT
argument_list|,
name|DateFormat
operator|.
name|SHORT
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
decl_stmt|;
name|Date
name|aDate
init|=
operator|new
name|Date
argument_list|(
name|date
argument_list|)
decl_stmt|;
return|return
name|dateFormatter
operator|.
name|format
argument_list|(
name|aDate
argument_list|)
return|;
block|}
specifier|private
name|void
name|writeHyperlink
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|resourceName
parameter_list|,
name|long
name|lastModified
parameter_list|,
name|long
name|fileSize
parameter_list|,
name|boolean
name|directory
parameter_list|)
block|{
if|if
condition|(
name|directory
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<tr><td><a class=\"folder\" href=\""
operator|+
name|resourceName
operator|+
literal|"/\">"
operator|+
name|resourceName
operator|+
literal|"</a></td><td>&nbsp;</td><td>&nbsp;</td></tr>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<tr><td><a class=\"file\" href=\""
operator|+
name|resourceName
operator|+
literal|"\">"
operator|+
name|resourceName
operator|+
literal|"</a></td><td class=\"size\">"
operator|+
name|fileSize
operator|+
literal|"&nbsp;&nbsp;</td><td class=\"date\">"
operator|+
name|fileDateFormat
argument_list|(
name|lastModified
argument_list|)
operator|+
literal|"</td></tr>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

