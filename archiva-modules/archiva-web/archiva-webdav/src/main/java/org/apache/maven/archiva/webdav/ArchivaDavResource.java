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
name|jackrabbit
operator|.
name|webdav
operator|.
name|*
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
name|property
operator|.
name|DavPropertySet
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
name|property
operator|.
name|DavPropertyNameSet
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
name|property
operator|.
name|DavProperty
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
name|property
operator|.
name|DavPropertyName
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
name|InputContext
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
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|lock
operator|.
name|*
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
name|util
operator|.
name|Text
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
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
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
operator|.
name|MimeTypes
import|;
end_import

begin_import
import|import
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
operator|.
name|IndexWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:james@atlassian.com">James William Dumay</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaDavResource
implements|implements
name|DavResource
block|{
specifier|private
specifier|final
name|MimeTypes
name|mimeTypes
decl_stmt|;
specifier|private
specifier|final
name|DavResourceLocator
name|locator
decl_stmt|;
specifier|private
specifier|final
name|DavResourceFactory
name|factory
decl_stmt|;
specifier|private
specifier|final
name|DavSession
name|session
decl_stmt|;
specifier|private
specifier|final
name|File
name|localResource
decl_stmt|;
specifier|private
specifier|final
name|String
name|logicalResource
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|METHODS
init|=
literal|"OPTIONS, GET, HEAD, POST, TRACE, PROPFIND, PROPPATCH, MKCOL, COPY, PUT, DELETE, MOVE"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMPLIANCE_CLASS
init|=
literal|"1"
decl_stmt|;
specifier|private
name|DavPropertySet
name|properties
decl_stmt|;
specifier|public
name|ArchivaDavResource
parameter_list|(
name|String
name|localResource
parameter_list|,
name|String
name|logicalResource
parameter_list|,
name|MimeTypes
name|mimeTypes
parameter_list|,
name|DavResourceLocator
name|locator
parameter_list|,
name|DavResourceFactory
name|factory
parameter_list|,
name|DavSession
name|session
parameter_list|)
block|{
name|this
operator|.
name|mimeTypes
operator|=
name|mimeTypes
expr_stmt|;
name|this
operator|.
name|localResource
operator|=
operator|new
name|File
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
name|locator
operator|=
name|locator
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|properties
operator|=
operator|new
name|DavPropertySet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|mimeTypes
operator|.
name|getMimeType
argument_list|(
name|localResource
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getComplianceClass
parameter_list|()
block|{
return|return
name|COMPLIANCE_CLASS
return|;
block|}
specifier|public
name|String
name|getSupportedMethods
parameter_list|()
block|{
return|return
name|METHODS
return|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
return|return
name|localResource
operator|.
name|exists
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isCollection
parameter_list|()
block|{
return|return
name|localResource
operator|.
name|isDirectory
argument_list|()
return|;
block|}
specifier|public
name|String
name|getDisplayName
parameter_list|()
block|{
name|String
name|resPath
init|=
name|getResourcePath
argument_list|()
decl_stmt|;
return|return
operator|(
name|resPath
operator|!=
literal|null
operator|)
condition|?
name|Text
operator|.
name|getName
argument_list|(
name|resPath
argument_list|)
else|:
name|resPath
return|;
block|}
specifier|public
name|DavResourceLocator
name|getLocator
parameter_list|()
block|{
return|return
name|locator
return|;
block|}
specifier|public
name|String
name|getResourcePath
parameter_list|()
block|{
return|return
name|locator
operator|.
name|getResourcePath
argument_list|()
return|;
block|}
specifier|public
name|String
name|getHref
parameter_list|()
block|{
return|return
name|locator
operator|.
name|getHref
argument_list|(
name|isCollection
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|long
name|getModificationTime
parameter_list|()
block|{
return|return
name|localResource
operator|.
name|lastModified
argument_list|()
return|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|localResource
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|void
name|spool
parameter_list|(
name|OutputContext
name|outputContext
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|isCollection
argument_list|()
condition|)
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|localResource
argument_list|)
argument_list|,
name|outputContext
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|IndexWriter
name|writer
init|=
operator|new
name|IndexWriter
argument_list|(
name|this
argument_list|,
name|localResource
argument_list|,
name|logicalResource
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|outputContext
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|DavPropertyName
index|[]
name|getPropertyNames
parameter_list|()
block|{
return|return
operator|new
name|DavPropertyName
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|DavProperty
name|getProperty
parameter_list|(
name|DavPropertyName
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|DavPropertySet
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|DavProperty
name|property
parameter_list|)
throws|throws
name|DavException
block|{
block|}
specifier|public
name|void
name|removeProperty
parameter_list|(
name|DavPropertyName
name|propertyName
parameter_list|)
throws|throws
name|DavException
block|{
block|}
specifier|public
name|MultiStatusResponse
name|alterProperties
parameter_list|(
name|DavPropertySet
name|setProperties
parameter_list|,
name|DavPropertyNameSet
name|removePropertyNames
parameter_list|)
throws|throws
name|DavException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|MultiStatusResponse
name|alterProperties
parameter_list|(
name|List
name|changeList
parameter_list|)
throws|throws
name|DavException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|DavResource
name|getCollection
parameter_list|()
block|{
name|DavResource
name|parent
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getResourcePath
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getResourcePath
argument_list|()
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|String
name|parentPath
init|=
name|Text
operator|.
name|getRelativeParent
argument_list|(
name|getResourcePath
argument_list|()
argument_list|,
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentPath
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|parentPath
operator|=
literal|"/"
expr_stmt|;
block|}
name|DavResourceLocator
name|parentloc
init|=
name|locator
operator|.
name|getFactory
argument_list|()
operator|.
name|createResourceLocator
argument_list|(
name|locator
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|locator
operator|.
name|getWorkspacePath
argument_list|()
argument_list|,
name|parentPath
argument_list|)
decl_stmt|;
try|try
block|{
name|parent
operator|=
name|factory
operator|.
name|createResource
argument_list|(
name|parentloc
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DavException
name|e
parameter_list|)
block|{
comment|// should not occur
block|}
block|}
return|return
name|parent
return|;
block|}
specifier|public
name|void
name|addMember
parameter_list|(
name|DavResource
name|resource
parameter_list|,
name|InputContext
name|inputContext
parameter_list|)
throws|throws
name|DavException
block|{
name|File
name|localFile
init|=
operator|new
name|File
argument_list|(
name|localResource
argument_list|,
name|resource
operator|.
name|getDisplayName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|resource
operator|.
name|isCollection
argument_list|()
operator|&&
name|isCollection
argument_list|()
operator|&&
name|inputContext
operator|.
name|hasStream
argument_list|()
condition|)
comment|//New File
block|{
name|boolean
name|deleteFile
init|=
literal|false
decl_stmt|;
name|FileOutputStream
name|stream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|stream
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|localFile
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|inputContext
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|stream
argument_list|)
expr_stmt|;
if|if
condition|(
name|inputContext
operator|.
name|getContentLength
argument_list|()
operator|!=
name|localFile
operator|.
name|length
argument_list|()
condition|)
block|{
name|deleteFile
operator|=
literal|true
expr_stmt|;
throw|throw
operator|new
name|DavException
argument_list|(
name|HttpServletResponse
operator|.
name|SC_BAD_REQUEST
argument_list|,
literal|"Content Header length was "
operator|+
name|inputContext
operator|.
name|getContentLength
argument_list|()
operator|+
literal|" but was "
operator|+
name|localFile
operator|.
name|length
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DavException
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|stream
argument_list|)
expr_stmt|;
if|if
condition|(
name|deleteFile
condition|)
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|localFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|resource
operator|.
name|isCollection
argument_list|()
operator|&&
name|isCollection
argument_list|()
condition|)
comment|//New directory
block|{
name|localFile
operator|.
name|mkdir
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|DavException
argument_list|(
name|HttpServletResponse
operator|.
name|SC_BAD_REQUEST
argument_list|,
literal|"Could not write member "
operator|+
name|resource
operator|.
name|getResourcePath
argument_list|()
operator|+
literal|" at "
operator|+
name|getResourcePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|DavResourceIterator
name|getMembers
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|removeMember
parameter_list|(
name|DavResource
name|member
parameter_list|)
throws|throws
name|DavException
block|{
block|}
specifier|public
name|void
name|move
parameter_list|(
name|DavResource
name|destination
parameter_list|)
throws|throws
name|DavException
block|{
block|}
specifier|public
name|void
name|copy
parameter_list|(
name|DavResource
name|destination
parameter_list|,
name|boolean
name|shallow
parameter_list|)
throws|throws
name|DavException
block|{
block|}
specifier|public
name|boolean
name|isLockable
parameter_list|(
name|Type
name|type
parameter_list|,
name|Scope
name|scope
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|hasLock
parameter_list|(
name|Type
name|type
parameter_list|,
name|Scope
name|scope
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|ActiveLock
name|getLock
parameter_list|(
name|Type
name|type
parameter_list|,
name|Scope
name|scope
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ActiveLock
index|[]
name|getLocks
parameter_list|()
block|{
return|return
operator|new
name|ActiveLock
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|ActiveLock
name|lock
parameter_list|(
name|LockInfo
name|reqLockInfo
parameter_list|)
throws|throws
name|DavException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ActiveLock
name|refreshLock
parameter_list|(
name|LockInfo
name|reqLockInfo
parameter_list|,
name|String
name|lockToken
parameter_list|)
throws|throws
name|DavException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|unlock
parameter_list|(
name|String
name|lockToken
parameter_list|)
throws|throws
name|DavException
block|{
block|}
specifier|public
name|void
name|addLockManager
parameter_list|(
name|LockManager
name|lockmgr
parameter_list|)
block|{
block|}
specifier|public
name|DavResourceFactory
name|getFactory
parameter_list|()
block|{
return|return
name|factory
return|;
block|}
specifier|public
name|DavSession
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
block|}
end_class

end_unit

