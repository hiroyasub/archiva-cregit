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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
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
name|util
operator|.
name|IOUtil
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
name|NoSuchAlgorithmException
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
name|List
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
comment|/**  * @author Edwin Punzalan  * @plexus.component role="org.apache.maven.repository.proxy.ProxyManager" role-hint="default"  */
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
name|wagonManager
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
name|void
name|setConfiguration
parameter_list|(
name|ProxyConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
specifier|public
name|ProxyConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|config
return|;
block|}
comment|/**      * @see org.apache.maven.repository.proxy.ProxyManager#get(String)      */
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
name|checkConfiguration
argument_list|()
expr_stmt|;
comment|//@todo use wagonManager for cache use file:// as URL
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
else|else
block|{
name|List
name|repos
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|confRepos
init|=
name|config
operator|.
name|getRepositories
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|confRepos
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ProxyRepository
name|repo
init|=
operator|(
name|ProxyRepository
operator|)
name|confRepos
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|repo
operator|.
name|getCachePeriod
argument_list|()
operator|>
literal|0
operator|&&
operator|(
name|repo
operator|.
name|getCachePeriod
argument_list|()
operator|*
literal|1000
operator|)
operator|+
name|cachedFile
operator|.
name|lastModified
argument_list|()
operator|<
name|System
operator|.
name|currentTimeMillis
argument_list|()
condition|)
block|{
name|repos
operator|.
name|add
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|repos
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|getRemoteFile
argument_list|(
name|path
argument_list|,
name|repos
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cachedFile
return|;
block|}
comment|/**      * @see org.apache.maven.repository.proxy.ProxyManager#getRemoteFile(String)      */
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
name|checkConfiguration
argument_list|()
expr_stmt|;
return|return
name|getRemoteFile
argument_list|(
name|path
argument_list|,
name|config
operator|.
name|getRepositories
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|File
name|getRemoteFile
parameter_list|(
name|String
name|path
parameter_list|,
name|List
name|repositories
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
block|{
name|checkConfiguration
argument_list|()
expr_stmt|;
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
argument_list|,
name|repositories
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
name|repositories
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
argument_list|,
name|repositories
argument_list|)
expr_stmt|;
block|}
return|return
name|remoteFile
return|;
block|}
comment|/**      * Used to download an artifact object from the remote repositories.      *      * @param artifact     the artifact object to be downloaded from a remote repository      * @param repositories the list of ProxyRepositories to retrieve the artifact from      * @return File object representing the remote artifact in the repository cache      * @throws ProxyException                when an error occurred during retrieval of the requested artifact      * @throws ResourceDoesNotExistException when the requested artifact cannot be found in any of the      *                                       configured repositories      */
specifier|private
name|File
name|getArtifactFile
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|List
name|repositories
parameter_list|)
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
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
name|artifact
operator|.
name|setFile
argument_list|(
name|artifactFile
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|artifactFile
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
comment|//@todo usage of repository cache period
name|wagonManager
operator|.
name|getArtifact
argument_list|(
name|artifact
argument_list|,
name|repositories
argument_list|)
expr_stmt|;
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
comment|/**      * Used to retrieve a remote file from the remote repositories.  This method is used only when the requested      * path cannot be resolved into a repository object, for example, an Artifact.      *      * @param path         the remote path to use to search for the requested file      * @param repositories the list of repositories to retrieve the file from      * @return File object representing the remote file in the repository cache      * @throws ResourceDoesNotExistException when the requested path cannot be found in any of the configured      *                                       repositories.      * @throws ProxyException                when an error occurred during the retrieval of the requested path      */
specifier|private
name|File
name|getRepositoryFile
parameter_list|(
name|String
name|path
parameter_list|,
name|List
name|repositories
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
name|repositories
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Used to retrieve a remote file from the remote repositories.  This method is used only when the requested      * path cannot be resolved into a repository object, for example, an Artifact.      *      * @param path         the remote path to use to search for the requested file      * @param repositories the list of repositories to retrieve the file from      * @param useChecksum  forces the download to whether use a checksum (if present in the remote repository) or not      * @return File object representing the remote file in the repository cache      * @throws ResourceDoesNotExistException when the requested path cannot be found in any of the configured      *                                       repositories.      * @throws ProxyException                when an error occurred during the retrieval of the requested path      */
specifier|private
name|File
name|getRepositoryFile
parameter_list|(
name|String
name|path
parameter_list|,
name|List
name|repositories
parameter_list|,
name|boolean
name|useChecksum
parameter_list|)
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
block|{
name|Map
name|checksums
init|=
literal|null
decl_stmt|;
name|Wagon
name|wagon
init|=
literal|null
decl_stmt|;
name|boolean
name|connected
init|=
literal|false
decl_stmt|;
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
name|repos
init|=
name|repositories
operator|.
name|iterator
argument_list|()
init|;
name|repos
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
name|repos
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|wagon
operator|=
name|wagonManager
operator|.
name|getWagon
argument_list|(
name|repository
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
comment|//@todo configure wagonManager
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
name|connected
operator|=
name|connectToRepository
argument_list|(
name|wagon
argument_list|,
name|repository
argument_list|)
expr_stmt|;
if|if
condition|(
name|connected
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
name|int
name|tries
init|=
literal|0
decl_stmt|;
name|boolean
name|success
init|=
literal|true
decl_stmt|;
do|do
block|{
name|tries
operator|++
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Trying "
operator|+
name|path
operator|+
literal|" from "
operator|+
name|repository
operator|.
name|getId
argument_list|()
operator|+
literal|"..."
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|target
operator|.
name|exists
argument_list|()
condition|)
block|{
name|wagon
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|temp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|long
name|repoTimestamp
init|=
name|target
operator|.
name|lastModified
argument_list|()
operator|+
name|repository
operator|.
name|getCachePeriod
argument_list|()
operator|*
literal|1000
decl_stmt|;
name|wagon
operator|.
name|getIfNewer
argument_list|(
name|path
argument_list|,
name|temp
argument_list|,
name|repoTimestamp
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useChecksum
condition|)
block|{
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
do|while
condition|(
operator|!
name|success
condition|)
do|;
name|disconnectWagon
argument_list|(
name|wagon
argument_list|)
expr_stmt|;
name|copyTempToTarget
argument_list|(
name|temp
argument_list|,
name|target
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
comment|//@todo usage for cacheFailure
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
literal|": no wagonManager configured "
operator|+
literal|"for protocol "
operator|+
name|repository
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|wagon
operator|!=
literal|null
operator|&&
name|checksums
operator|!=
literal|null
condition|)
block|{
name|releaseChecksums
argument_list|(
name|wagon
argument_list|,
name|checksums
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|connected
condition|)
block|{
name|disconnectWagon
argument_list|(
name|wagon
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Used to add checksum observers as transfer listeners to the wagonManager object      *      * @param wagon the wagonManager object to use the checksum with      * @return map of ChecksumObservers added into the wagonManager transfer listeners      */
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
comment|/**      * Used to remove the ChecksumObservers from the wagonManager object      *      * @param wagon       the wagonManager object to remote the ChecksumObservers from      * @param checksumMap the map representing the list of ChecksumObservers added to the wagonManager object      */
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
comment|/**      * Used to request the wagonManager object to connect to a repository      *      * @param wagon      the wagonManager object that will be used to connect to the repository      * @param repository the repository object to connect the wagonManager to      * @return true when the wagonManager is able to connect to the repository      */
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
comment|/**      * Used to verify the checksum during a wagonManager download      *      * @param checksumMap the map of ChecksumObservers present in the wagonManager as transferlisteners      * @param path        path of the remote object whose checksum is to be verified      * @param wagon       the wagonManager object used to download the requested path      * @return true when the checksum succeeds and false when the checksum failed.      */
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
throws|throws
name|ProxyException
block|{
name|releaseChecksums
argument_list|(
name|wagon
argument_list|,
name|checksumMap
argument_list|)
expr_stmt|;
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
name|checksumPath
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
name|checksumPath
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
literal|".tmp"
argument_list|)
decl_stmt|;
name|wagon
operator|.
name|get
argument_list|(
name|checksumPath
argument_list|,
name|tempChecksumFile
argument_list|)
expr_stmt|;
name|String
name|remoteChecksum
init|=
name|readTextFile
argument_list|(
name|tempChecksumFile
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|remoteChecksum
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
operator|>
literal|0
condition|)
block|{
name|remoteChecksum
operator|=
name|remoteChecksum
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|remoteChecksum
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|boolean
name|checksumCheck
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|remoteChecksum
operator|.
name|toUpperCase
argument_list|()
operator|.
name|equals
argument_list|(
name|checksum
operator|.
name|getActualChecksum
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
condition|)
block|{
name|copyTempToTarget
argument_list|(
name|tempChecksumFile
argument_list|,
name|checksumFile
argument_list|)
expr_stmt|;
name|checksumCheck
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|checksumCheck
return|;
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
name|debug
argument_list|(
literal|"An error occurred during the download of "
operator|+
name|checksumPath
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
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
name|debug
argument_list|(
literal|"An error occurred during the download of "
operator|+
name|checksumPath
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
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
name|debug
argument_list|(
literal|"An error occurred during the download of "
operator|+
name|checksumPath
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// do nothing try the next checksum
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
name|debug
argument_list|(
literal|"An error occurred while reading the temporary checksum file."
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|getLogger
argument_list|()
operator|.
name|debug
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
comment|/**      * Used to ensure that this proxy instance is running with a valid configuration instance.      *      * @throws ProxyException      */
specifier|private
name|void
name|checkConfiguration
parameter_list|()
throws|throws
name|ProxyException
block|{
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
literal|"No proxy configuration defined."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Used to read text file contents for use with the checksum validation      *      * @param file The file to be read      * @return The String content of the file parameter      * @throws IOException when an error occurred while reading the file contents      */
specifier|private
name|String
name|readTextFile
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|text
init|=
literal|""
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
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|64
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
name|text
operator|+=
operator|new
name|String
argument_list|(
name|buffer
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
name|IOUtil
operator|.
name|close
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
return|return
name|text
return|;
block|}
comment|/**      * Used to move the temporary file to its real destination.  This is patterned from the way WagonManager handles      * its downloaded files.      *      * @param temp   The completed download file      * @param target The final location of the downloaded file      * @throws ProxyException when the temp file cannot replace the target file      */
specifier|private
name|void
name|copyTempToTarget
parameter_list|(
name|File
name|temp
parameter_list|,
name|File
name|target
parameter_list|)
throws|throws
name|ProxyException
block|{
if|if
condition|(
name|target
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|target
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
literal|"Unable to overwrite existing target file: "
operator|+
name|target
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|temp
operator|.
name|renameTo
argument_list|(
name|target
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to rename tmp file to its final name... resorting to copy command."
argument_list|)
expr_stmt|;
try|try
block|{
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|temp
argument_list|,
name|target
argument_list|)
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
name|ProxyException
argument_list|(
literal|"Cannot copy tmp file to its final location"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|temp
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Used to disconnect the wagonManager from its repository      *      * @param wagon the connected wagonManager object      */
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
literal|"Problem disconnecting from wagonManager - ignoring: "
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

