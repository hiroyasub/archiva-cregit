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
name|consumers
operator|.
name|core
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|FileTypes
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
name|consumers
operator|.
name|AbstractMonitoredConsumer
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
name|consumers
operator|.
name|ConsumerException
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|model
operator|.
name|ArchivaRepository
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
name|repository
operator|.
name|layout
operator|.
name|BidirectionalRepositoryLayout
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
name|digest
operator|.
name|ChecksumFile
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
name|digest
operator|.
name|Digester
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
name|digest
operator|.
name|DigesterException
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
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
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryListener
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
comment|/**  * ArtifactMissingChecksumsConsumer - Create missing checksums for the artifact.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  * role-hint="create-missing-checksums"  * instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactMissingChecksumsConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * @plexus.configuration default-value="create-missing-checksums"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Create Missing Checksums (.sha1& .md5)"      */
specifier|private
name|String
name|description
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.repository.layout.BidirectionalRepositoryLayout"      */
specifier|private
name|Map
name|bidirectionalLayoutMap
decl_stmt|;
comment|// TODO: replace with new bidir-repo-layout-factory
comment|/**      * @plexus.requirement role-hint="sha1"      */
specifier|private
name|Digester
name|digestSha1
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="md5";      */
specifier|private
name|Digester
name|digestMd5
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ChecksumFile
name|checksum
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_CHECKSUM_NOT_FILE
init|=
literal|"checksum-bad-not-file"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_CHECKSUM_CANNOT_CALC
init|=
literal|"checksum-calc-failure"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_CHECKSUM_CANNOT_CREATE
init|=
literal|"checksum-create-failure"
decl_stmt|;
specifier|private
name|ArchivaRepository
name|repository
decl_stmt|;
specifier|private
name|File
name|repositoryDir
decl_stmt|;
specifier|private
name|BidirectionalRepositoryLayout
name|layout
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|propertyNameTriggers
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|repositoryDir
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|layoutName
init|=
name|repository
operator|.
name|getModel
argument_list|()
operator|.
name|getLayoutName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|bidirectionalLayoutMap
operator|.
name|containsKey
argument_list|(
name|layoutName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"Unable to process repository with layout ["
operator|+
name|layoutName
operator|+
literal|"] as there is no corresponding "
operator|+
name|BidirectionalRepositoryLayout
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" implementation available."
argument_list|)
throw|;
block|}
name|this
operator|.
name|layout
operator|=
operator|(
name|BidirectionalRepositoryLayout
operator|)
name|bidirectionalLayoutMap
operator|.
name|get
argument_list|(
name|layoutName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|createIfMissing
argument_list|(
name|path
argument_list|,
name|digestSha1
argument_list|)
expr_stmt|;
name|createIfMissing
argument_list|(
name|path
argument_list|,
name|digestMd5
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createIfMissing
parameter_list|(
name|String
name|path
parameter_list|,
name|Digester
name|digester
parameter_list|)
block|{
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
name|path
operator|+
name|digester
operator|.
name|getFilenameExtension
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|checksumFile
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|checksum
operator|.
name|createChecksum
argument_list|(
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
name|path
argument_list|)
argument_list|,
name|digester
argument_list|)
expr_stmt|;
name|triggerConsumerInfo
argument_list|(
literal|"Created missing checksum file "
operator|+
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|TYPE_CHECKSUM_CANNOT_CALC
argument_list|,
literal|"Cannot calculate checksum for file "
operator|+
name|checksumFile
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
name|IOException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|TYPE_CHECKSUM_CANNOT_CREATE
argument_list|,
literal|"Cannot create checksum for file "
operator|+
name|checksumFile
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
block|}
if|else if
condition|(
operator|!
name|checksumFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|triggerConsumerWarning
argument_list|(
name|TYPE_CHECKSUM_NOT_FILE
argument_list|,
literal|"Checksum file "
operator|+
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" is not a file."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|propertyNameTriggers
operator|.
name|contains
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|private
name|void
name|initIncludes
parameter_list|()
block|{
name|includes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|includes
operator|.
name|addAll
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|propertyNameTriggers
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"repositoryScanning"
argument_list|)
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"fileTypes"
argument_list|)
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"fileType"
argument_list|)
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"patterns"
argument_list|)
expr_stmt|;
name|propertyNameTriggers
operator|.
name|add
argument_list|(
literal|"pattern"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

