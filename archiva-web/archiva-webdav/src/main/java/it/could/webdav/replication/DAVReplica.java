begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* ========================================================================== *  *         Copyright (C) 2004-2006, Pier Fumagalli<http://could.it/>         *  *                            All rights reserved.                            *  * ========================================================================== *  *                                                                            *  * Licensed under the  Apache License, Version 2.0  (the "License").  You may *  * not use this file except in compliance with the License.  You may obtain a *  * copy of the License at<http://www.apache.org/licenses/LICENSE-2.0>.       *  *                                                                            *  * Unless  required  by applicable  law or  agreed  to  in writing,  software *  * distributed under the License is distributed on an  "AS IS" BASIS, WITHOUT *  * WARRANTIES OR  CONDITIONS OF ANY KIND, either express or implied.  See the *  * License for the  specific language  governing permissions  and limitations *  * under the License.                                                         *  *                                                                            *  * ========================================================================== */
end_comment

begin_package
package|package
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|replication
package|;
end_package

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|util
operator|.
name|StreamTools
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|util
operator|.
name|http
operator|.
name|WebDavClient
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|util
operator|.
name|location
operator|.
name|Location
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVListener
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVLogger
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVRepository
import|;
end_import

begin_import
import|import
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVResource
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
name|OutputStream
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
import|;
end_import

begin_comment
comment|/**  *<p>TODO: Document this class.</p>  *  * @author<a href="http://could.it/">Pier Fumagalli</a>  */
end_comment

begin_class
specifier|public
class|class
name|DAVReplica
extends|extends
name|Thread
implements|implements
name|DAVListener
block|{
specifier|private
specifier|static
specifier|final
name|int
name|SYNCHRONIZE
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
specifier|final
name|DAVRepository
name|repository
decl_stmt|;
specifier|private
specifier|final
name|DAVLogger
name|logger
decl_stmt|;
specifier|private
specifier|final
name|Location
name|location
decl_stmt|;
specifier|private
specifier|final
name|List
name|actions
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|public
name|DAVReplica
parameter_list|(
name|DAVRepository
name|repository
parameter_list|,
name|Location
name|location
parameter_list|,
name|DAVLogger
name|logger
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|location
operator|=
operator|new
name|WebDavClient
argument_list|(
name|location
argument_list|)
operator|.
name|getLocation
argument_list|()
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|synchronize
parameter_list|()
throws|throws
name|IOException
block|{
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Scheduling full synchronization"
argument_list|)
expr_stmt|;
name|this
operator|.
name|notify
argument_list|(
name|this
operator|.
name|repository
operator|.
name|getResource
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|,
name|SYNCHRONIZE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|notify
parameter_list|(
name|DAVResource
name|resource
parameter_list|,
name|int
name|event
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Event for \""
operator|+
name|resource
operator|.
name|getRelativePath
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|resource
operator|.
name|getRepository
argument_list|()
operator|!=
name|this
operator|.
name|repository
condition|)
return|return;
synchronized|synchronized
init|(
name|this
operator|.
name|actions
init|)
block|{
name|this
operator|.
name|actions
operator|.
name|add
argument_list|(
operator|new
name|Action
argument_list|(
name|resource
argument_list|,
name|event
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|actions
operator|.
name|notify
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Starting background replica thread on "
operator|+
name|location
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
try|try
block|{
specifier|final
name|DAVReplica
operator|.
name|Action
name|array
index|[]
decl_stmt|;
synchronized|synchronized
init|(
name|this
operator|.
name|actions
init|)
block|{
try|try
block|{
if|if
condition|(
name|this
operator|.
name|actions
operator|.
name|isEmpty
argument_list|()
condition|)
name|this
operator|.
name|actions
operator|.
name|wait
argument_list|()
expr_stmt|;
specifier|final
name|int
name|s
init|=
name|this
operator|.
name|actions
operator|.
name|size
argument_list|()
decl_stmt|;
name|array
operator|=
operator|(
name|Action
index|[]
operator|)
name|this
operator|.
name|actions
operator|.
name|toArray
argument_list|(
operator|new
name|Action
index|[
name|s
index|]
argument_list|)
expr_stmt|;
name|this
operator|.
name|actions
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|exception
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Exiting background replica thread"
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|array
operator|.
name|length
condition|;
name|x
operator|++
control|)
try|try
block|{
name|this
operator|.
name|replicate
argument_list|(
name|array
index|[
name|x
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
specifier|final
name|String
name|path
init|=
name|array
index|[
name|x
index|]
operator|.
name|resource
operator|.
name|getRelativePath
argument_list|()
decl_stmt|;
specifier|final
name|String
name|message
init|=
literal|"Error synchronizing resource "
operator|+
name|path
decl_stmt|;
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Replica thread attempted suicide"
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|replicate
parameter_list|(
name|DAVReplica
operator|.
name|Action
name|action
parameter_list|)
block|{
specifier|final
name|DAVResource
name|resource
init|=
name|action
operator|.
name|resource
decl_stmt|;
if|if
condition|(
name|action
operator|.
name|event
operator|==
name|SYNCHRONIZE
condition|)
block|{
name|this
operator|.
name|synchronize
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
else|else
try|try
block|{
specifier|final
name|String
name|path
init|=
name|resource
operator|.
name|getParent
argument_list|()
operator|.
name|getRelativePath
argument_list|()
decl_stmt|;
specifier|final
name|Location
name|location
init|=
name|this
operator|.
name|location
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
specifier|final
name|WebDavClient
name|client
init|=
operator|new
name|WebDavClient
argument_list|(
name|location
argument_list|)
decl_stmt|;
specifier|final
name|String
name|child
init|=
name|resource
operator|.
name|getName
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|action
operator|.
name|event
condition|)
block|{
case|case
name|RESOURCE_CREATED
case|:
case|case
name|RESOURCE_MODIFIED
case|:
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Putting resource "
operator|+
name|path
argument_list|)
expr_stmt|;
name|this
operator|.
name|put
argument_list|(
name|resource
argument_list|,
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|RESOURCE_REMOVED
case|:
case|case
name|COLLECTION_REMOVED
case|:
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Deleting resource "
operator|+
name|path
argument_list|)
expr_stmt|;
name|client
operator|.
name|delete
argument_list|(
name|child
argument_list|)
expr_stmt|;
break|break;
case|case
name|COLLECTION_CREATED
case|:
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Creating collection "
operator|+
name|path
argument_list|)
expr_stmt|;
name|client
operator|.
name|mkcol
argument_list|(
name|child
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|String
name|message
init|=
literal|"Error replicating "
operator|+
name|resource
operator|.
name|getRelativePath
argument_list|()
decl_stmt|;
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
name|message
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|put
parameter_list|(
name|DAVResource
name|resource
parameter_list|,
name|WebDavClient
name|client
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|String
name|name
init|=
name|resource
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|long
name|length
init|=
name|resource
operator|.
name|getContentLength
argument_list|()
operator|.
name|longValue
argument_list|()
decl_stmt|;
specifier|final
name|OutputStream
name|output
init|=
name|client
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|length
argument_list|)
decl_stmt|;
specifier|final
name|InputStream
name|input
init|=
name|resource
operator|.
name|read
argument_list|()
decl_stmt|;
name|StreamTools
operator|.
name|copy
argument_list|(
name|input
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|synchronize
parameter_list|(
name|DAVResource
name|resource
parameter_list|)
block|{
comment|/* Figure out the path of the resource */
specifier|final
name|String
name|path
init|=
name|resource
operator|.
name|getRelativePath
argument_list|()
decl_stmt|;
comment|/* If it's a file or null, just skip the whole thing */
if|if
condition|(
operator|!
name|resource
operator|.
name|isCollection
argument_list|()
condition|)
block|{
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Synchronization on non-collection "
operator|+
name|path
argument_list|)
expr_stmt|;
return|return;
block|}
comment|/* Open a webdav client to the collection to synchronize */
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Synchronizing collection "
operator|+
name|path
argument_list|)
expr_stmt|;
specifier|final
name|WebDavClient
name|client
decl_stmt|;
try|try
block|{
specifier|final
name|Location
name|location
init|=
name|this
operator|.
name|location
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|client
operator|=
operator|new
name|WebDavClient
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Error creating WebDAV client"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
return|return;
block|}
comment|/* Create a list of all children from the DAV client */
specifier|final
name|Set
name|children
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|client
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
name|children
operator|.
name|add
argument_list|(
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
comment|/* Process all resource children one by one and ensure they exist */
for|for
control|(
name|Iterator
name|iter
init|=
name|resource
operator|.
name|getChildren
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
specifier|final
name|DAVResource
name|child
init|=
operator|(
name|DAVResource
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|child
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/* Remove this from the resources that will be removed later */
name|children
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
comment|/* If the client doesn't have this child, add it to the replica */
if|if
condition|(
operator|!
name|client
operator|.
name|hasChild
argument_list|(
name|name
argument_list|)
condition|)
try|try
block|{
if|if
condition|(
name|child
operator|.
name|isCollection
argument_list|()
condition|)
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Client doesn't have collection "
operator|+
name|name
argument_list|)
expr_stmt|;
name|client
operator|.
name|mkcol
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|synchronize
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Client doesn't have resource "
operator|+
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|put
argument_list|(
name|child
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Error creating new child "
operator|+
name|name
argument_list|,
name|exception
argument_list|)
expr_stmt|;
comment|/* If this child is a collection, it must be a collection on dav */
block|}
if|else if
condition|(
name|child
operator|.
name|isCollection
argument_list|()
condition|)
try|try
block|{
if|if
condition|(
operator|!
name|client
operator|.
name|isCollection
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Recreating collection "
operator|+
name|name
argument_list|)
expr_stmt|;
name|client
operator|.
name|delete
argument_list|(
name|name
argument_list|)
operator|.
name|mkcol
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|synchronize
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Error creating collection "
operator|+
name|name
argument_list|,
name|exception
argument_list|)
expr_stmt|;
comment|/* Ok, the resource is a normal one, verify size and timestamp */
block|}
else|else
try|try
block|{
specifier|final
name|Date
name|rlast
init|=
name|child
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
specifier|final
name|Date
name|dlast
init|=
name|client
operator|.
name|getLastModified
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|rlast
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|rlast
operator|.
name|equals
argument_list|(
name|dlast
argument_list|)
operator|)
condition|)
block|{
specifier|final
name|Long
name|rlen
init|=
name|child
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
specifier|final
name|long
name|dlen
init|=
name|client
operator|.
name|getContentLength
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|rlen
operator|==
literal|null
operator|)
operator|||
operator|(
name|rlen
operator|.
name|longValue
argument_list|()
operator|!=
name|dlen
operator|)
condition|)
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Resending resource "
operator|+
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|put
argument_list|(
name|child
argument_list|,
name|client
operator|.
name|delete
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Error resending resource "
operator|+
name|name
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* Any other child that was not removed above, will go away now! */
for|for
control|(
name|Iterator
name|iter
init|=
name|children
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
specifier|final
name|String
name|name
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Removing leftovers "
operator|+
name|name
argument_list|)
expr_stmt|;
name|client
operator|.
name|delete
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Error removing left over "
operator|+
name|name
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|Action
block|{
specifier|final
name|DAVResource
name|resource
decl_stmt|;
specifier|final
name|int
name|event
decl_stmt|;
specifier|private
name|Action
parameter_list|(
name|DAVResource
name|resource
parameter_list|,
name|int
name|event
parameter_list|)
block|{
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
name|this
operator|.
name|event
operator|=
name|event
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

