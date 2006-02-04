begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
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
name|artifact
operator|.
name|factory
operator|.
name|ArtifactFactory
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
name|artifact
operator|.
name|manager
operator|.
name|ChecksumFailedException
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
name|artifact
operator|.
name|manager
operator|.
name|WagonManager
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|repository
operator|.
name|ArtifactUtils
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
name|repository
operator|.
name|digest
operator|.
name|DefaultDigester
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
name|repository
operator|.
name|digest
operator|.
name|Digester
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
name|repository
operator|.
name|proxy
operator|.
name|configuration
operator|.
name|ProxyConfiguration
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
name|repository
operator|.
name|proxy
operator|.
name|repository
operator|.
name|ProxyRepository
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
name|wagon
operator|.
name|ConnectionException
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
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|wagon
operator|.
name|TransferFailedException
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
name|wagon
operator|.
name|UnsupportedProtocolException
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
name|wagon
operator|.
name|Wagon
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
name|wagon
operator|.
name|authentication
operator|.
name|AuthenticationException
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
name|wagon
operator|.
name|authorization
operator|.
name|AuthorizationException
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
name|wagon
operator|.
name|observers
operator|.
name|ChecksumObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
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
name|IOException
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
name|Iterator
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

begin_comment
comment|/**  * @author Edwin Punzalan  * @plexus.component role="org.apache.maven.repository.proxy.ProxyManager"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultProxyManager
extends|extends
name|AbstractLogEnabled
implements|implements
name|ProxyManager
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|WagonManager
name|wagon
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
name|ProxyConfiguration
name|config
decl_stmt|;
specifier|public
name|DefaultProxyManager
parameter_list|(
name|ProxyConfiguration
name|configuration
parameter_list|)
block|{
name|config
operator|=
name|configuration
expr_stmt|;
block|}
specifier|public
name|File
name|get
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
block|{
comment|//@todo use wagon for cache use file:// as URL
name|String
name|cachePath
init|=
name|config
operator|.
name|getRepositoryCachePath
argument_list|()
decl_stmt|;
name|File
name|cachedFile
init|=
operator|new
name|File
argument_list|(
name|cachePath
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cachedFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|getRemoteFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
return|return
name|cachedFile
return|;
block|}
specifier|public
name|File
name|getRemoteFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
block|{
try|try
block|{
name|Artifact
name|artifact
init|=
name|ArtifactUtils
operator|.
name|buildArtifact
argument_list|(
name|path
argument_list|,
name|artifactFactory
argument_list|)
decl_stmt|;
name|File
name|remoteFile
decl_stmt|;
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|remoteFile
operator|=
name|getArtifactFile
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|".md5"
argument_list|)
operator|||
name|path
operator|.
name|endsWith
argument_list|(
literal|".sha1"
argument_list|)
condition|)
block|{
name|remoteFile
operator|=
name|getRepositoryFile
argument_list|(
name|path
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// as of now, only metadata fits here
name|remoteFile
operator|=
name|getRepositoryFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
return|return
name|remoteFile
return|;
block|}
catch|catch
parameter_list|(
name|TransferFailedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|File
name|getArtifactFile
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
block|{
name|ArtifactRepository
name|repoCache
init|=
name|config
operator|.
name|getRepositoryCache
argument_list|()
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repoCache
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repoCache
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|wagon
operator|.
name|getArtifact
argument_list|(
name|artifact
argument_list|,
name|config
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|artifactFile
operator|=
name|artifact
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
return|return
name|artifactFile
return|;
block|}
specifier|private
name|File
name|getRepositoryFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
block|{
return|return
name|getRepositoryFile
argument_list|(
name|path
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
name|File
name|getRepositoryFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|useChecksum
parameter_list|)
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
block|{
name|ArtifactRepository
name|cache
init|=
name|config
operator|.
name|getRepositoryCache
argument_list|()
decl_stmt|;
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|cache
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|repositories
init|=
name|config
operator|.
name|getRepositories
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|repositories
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ProxyRepository
name|repository
init|=
operator|(
name|ProxyRepository
operator|)
name|repositories
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|Wagon
name|wagon
init|=
name|this
operator|.
name|wagon
operator|.
name|getWagon
argument_list|(
name|repository
operator|.
name|getProtocol
argument_list|()
argument_list|)
decl_stmt|;
comment|//@todo configure wagon
name|Map
name|checksums
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|useChecksum
condition|)
block|{
name|checksums
operator|=
name|prepareChecksums
argument_list|(
name|wagon
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|connectToRepository
argument_list|(
name|wagon
argument_list|,
name|repository
argument_list|)
condition|)
block|{
name|File
name|temp
init|=
operator|new
name|File
argument_list|(
name|target
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".tmp"
argument_list|)
decl_stmt|;
name|temp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|int
name|tries
init|=
literal|0
decl_stmt|;
name|boolean
name|success
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|success
condition|)
block|{
name|tries
operator|++
expr_stmt|;
name|wagon
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|temp
argument_list|)
expr_stmt|;
if|if
condition|(
name|useChecksum
condition|)
block|{
name|releaseChecksums
argument_list|(
name|wagon
argument_list|,
name|checksums
argument_list|)
expr_stmt|;
name|success
operator|=
name|doChecksumCheck
argument_list|(
name|checksums
argument_list|,
name|path
argument_list|,
name|wagon
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|success
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|tries
operator|>
literal|1
operator|&&
operator|!
name|success
condition|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
literal|"Checksum failures occurred while downloading "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
name|disconnectWagon
argument_list|(
name|wagon
argument_list|)
expr_stmt|;
return|return
name|target
return|;
block|}
comment|//try next repository
block|}
catch|catch
parameter_list|(
name|TransferFailedException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Skipping repository "
operator|+
name|repository
operator|.
name|getUrl
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
comment|//do nothing, file not found in this repository
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Skipping repository "
operator|+
name|repository
operator|.
name|getUrl
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedProtocolException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Skipping repository "
operator|+
name|repository
operator|.
name|getUrl
argument_list|()
operator|+
literal|": no wagon configured for protocol "
operator|+
name|repository
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
throw|throw
operator|new
name|ResourceDoesNotExistException
argument_list|(
literal|"Could not find "
operator|+
name|path
operator|+
literal|" in any of the repositories."
argument_list|)
throw|;
block|}
specifier|private
name|Map
name|prepareChecksums
parameter_list|(
name|Wagon
name|wagon
parameter_list|)
block|{
name|Map
name|checksums
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
try|try
block|{
name|ChecksumObserver
name|checksum
init|=
operator|new
name|ChecksumObserver
argument_list|(
literal|"SHA-1"
argument_list|)
decl_stmt|;
name|wagon
operator|.
name|addTransferListener
argument_list|(
name|checksum
argument_list|)
expr_stmt|;
name|checksums
operator|.
name|put
argument_list|(
literal|"sha1"
argument_list|,
name|checksum
argument_list|)
expr_stmt|;
name|checksum
operator|=
operator|new
name|ChecksumObserver
argument_list|(
literal|"MD5"
argument_list|)
expr_stmt|;
name|wagon
operator|.
name|addTransferListener
argument_list|(
name|checksum
argument_list|)
expr_stmt|;
name|checksums
operator|.
name|put
argument_list|(
literal|"md5"
argument_list|,
name|checksum
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"An error occurred while preparing checksum observers"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|checksums
return|;
block|}
specifier|private
name|void
name|releaseChecksums
parameter_list|(
name|Wagon
name|wagon
parameter_list|,
name|Map
name|checksumMap
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|checksums
init|=
name|checksumMap
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|checksums
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ChecksumObserver
name|listener
init|=
operator|(
name|ChecksumObserver
operator|)
name|checksums
operator|.
name|next
argument_list|()
decl_stmt|;
name|wagon
operator|.
name|removeTransferListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|connectToRepository
parameter_list|(
name|Wagon
name|wagon
parameter_list|,
name|ProxyRepository
name|repository
parameter_list|)
block|{
name|boolean
name|connected
init|=
literal|false
decl_stmt|;
try|try
block|{
name|wagon
operator|.
name|connect
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|connected
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConnectionException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Could not connect to "
operator|+
name|repository
operator|.
name|getId
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthenticationException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Could not connect to "
operator|+
name|repository
operator|.
name|getId
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|connected
return|;
block|}
specifier|private
name|boolean
name|doChecksumCheck
parameter_list|(
name|Map
name|checksumMap
parameter_list|,
name|String
name|path
parameter_list|,
name|Wagon
name|wagon
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|checksums
init|=
name|checksumMap
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|checksums
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|checksumExt
init|=
operator|(
name|String
operator|)
name|checksums
operator|.
name|next
argument_list|()
decl_stmt|;
name|ChecksumObserver
name|checksum
init|=
operator|(
name|ChecksumObserver
operator|)
name|checksumMap
operator|.
name|get
argument_list|(
name|checksumExt
argument_list|)
decl_stmt|;
name|String
name|remotePath
init|=
name|path
operator|+
literal|"."
operator|+
name|checksumExt
decl_stmt|;
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|config
operator|.
name|getRepositoryCache
argument_list|()
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|remotePath
argument_list|)
decl_stmt|;
try|try
block|{
name|File
name|tempChecksumFile
init|=
operator|new
name|File
argument_list|(
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"."
operator|+
name|checksumExt
argument_list|)
decl_stmt|;
name|wagon
operator|.
name|get
argument_list|(
name|remotePath
operator|+
literal|"."
operator|+
name|checksumExt
argument_list|,
name|tempChecksumFile
argument_list|)
expr_stmt|;
name|String
name|algorithm
decl_stmt|;
if|if
condition|(
literal|"md5"
operator|.
name|equals
argument_list|(
name|checksumExt
argument_list|)
condition|)
block|{
name|algorithm
operator|=
literal|"MD5"
expr_stmt|;
block|}
else|else
block|{
name|algorithm
operator|=
literal|"SHA-1"
expr_stmt|;
block|}
name|Digester
name|digester
init|=
operator|new
name|DefaultDigester
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|digester
operator|.
name|verifyChecksum
argument_list|(
name|tempChecksumFile
argument_list|,
name|checksum
operator|.
name|getActualChecksum
argument_list|()
argument_list|,
name|algorithm
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Failed to initialize checksum: "
operator|+
name|algorithm
operator|+
literal|"\n  "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Failed to verify checksum: "
operator|+
name|algorithm
operator|+
literal|"\n  "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ChecksumFailedException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|TransferFailedException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"An error occurred during the download of "
operator|+
name|remotePath
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// do nothing try the next checksum
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"An error occurred during the download of "
operator|+
name|remotePath
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// do nothing try the next checksum
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"An error occurred during the download of "
operator|+
name|remotePath
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// do nothing try the next checksum
block|}
block|}
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Skipping checksum validation for "
operator|+
name|path
operator|+
literal|": No remote checksums available."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|disconnectWagon
parameter_list|(
name|Wagon
name|wagon
parameter_list|)
block|{
try|try
block|{
name|wagon
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConnectionException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Problem disconnecting from wagon - ignoring: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

