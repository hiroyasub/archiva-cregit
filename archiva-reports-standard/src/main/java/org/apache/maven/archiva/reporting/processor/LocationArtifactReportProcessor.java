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
name|reporting
operator|.
name|processor
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|maven
operator|.
name|archiva
operator|.
name|reporting
operator|.
name|database
operator|.
name|ArtifactResultsDatabase
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
name|handler
operator|.
name|DefaultArtifactHandler
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
name|model
operator|.
name|Model
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
name|model
operator|.
name|io
operator|.
name|xpp3
operator|.
name|MavenXpp3Reader
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
name|xml
operator|.
name|pull
operator|.
name|XmlPullParserException
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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_comment
comment|/**  * Validate the location of the artifact based on the values indicated  * in its pom (both the pom packaged with the artifact& the pom in the  * file system).  *  * @plexus.component role="org.apache.maven.archiva.reporting.processor.ArtifactReportProcessor" role-hint="artifact-location"  */
end_comment

begin_class
specifier|public
class|class
name|LocationArtifactReportProcessor
implements|implements
name|ArtifactReportProcessor
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|// TODO: share with other code with the same
specifier|private
specifier|static
specifier|final
name|Set
name|JAR_FILE_TYPES
init|=
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"jar"
block|,
literal|"war"
block|,
literal|"par"
block|,
literal|"ejb"
block|,
literal|"ear"
block|,
literal|"rar"
block|,
literal|"sar"
block|}
argument_list|)
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactResultsDatabase
name|database
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POM
init|=
literal|"pom"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ROLE_HINT
init|=
literal|"artifact-location"
decl_stmt|;
comment|/**      * Check whether the artifact is in its proper location. The location of the artifact      * is validated first against the groupId, artifactId and versionId in the specified model      * object (pom in the file system). Then unpack the artifact (jar file) and get the model (pom)      * included in the package. If a model exists inside the package, then check if the artifact's      * location is valid based on the location specified in the pom. Check if the both the location      * specified in the file system pom and in the pom included in the package is the same.      */
specifier|public
name|void
name|processArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Model
name|model
parameter_list|)
block|{
name|ArtifactRepository
name|repository
init|=
name|artifact
operator|.
name|getRepository
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"file"
operator|.
name|equals
argument_list|(
name|repository
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
comment|// We can't check other types of URLs yet. Need to use Wagon, with an exists() method.
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Can't process repository '"
operator|+
name|repository
operator|.
name|getUrl
argument_list|()
operator|+
literal|"'. Only file based repositories are supported"
argument_list|)
throw|;
block|}
name|adjustDistributionArtifactHandler
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|String
name|artifactPath
init|=
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|model
operator|!=
literal|null
condition|)
block|{
comment|// only check if it is a standalone POM, or an artifact other than a POM
comment|// ie, don't check the location of the POM for another artifact matches that of the artifact
if|if
condition|(
operator|!
name|POM
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|||
name|POM
operator|.
name|equals
argument_list|(
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
condition|)
block|{
comment|//check if the artifact is located in its proper location based on the info
comment|//specified in the model object/pom
name|Artifact
name|modelArtifact
init|=
name|artifactFactory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|model
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|,
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
decl_stmt|;
name|adjustDistributionArtifactHandler
argument_list|(
name|modelArtifact
argument_list|)
expr_stmt|;
name|String
name|modelPath
init|=
name|repository
operator|.
name|pathOf
argument_list|(
name|modelArtifact
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|modelPath
operator|.
name|equals
argument_list|(
name|artifactPath
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"repository-pom-location"
argument_list|,
literal|"The artifact is out of place. It does not match the specified location in the repository pom: "
operator|+
name|modelPath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// get the location of the artifact itself
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|artifactPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|JAR_FILE_TYPES
operator|.
name|contains
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
comment|//unpack the artifact (using the groupId, artifactId& version specified in the artifact object itself
comment|//check if the pom is included in the package
name|Model
name|extractedModel
init|=
name|readArtifactModel
argument_list|(
name|file
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|extractedModel
operator|!=
literal|null
condition|)
block|{
name|Artifact
name|extractedArtifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
name|extractedModel
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|extractedModel
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|extractedModel
operator|.
name|getVersion
argument_list|()
argument_list|,
name|extractedModel
operator|.
name|getPackaging
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|repository
operator|.
name|pathOf
argument_list|(
name|extractedArtifact
argument_list|)
operator|.
name|equals
argument_list|(
name|artifactPath
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"packaged-pom-location"
argument_list|,
literal|"The artifact is out of place. It does not match the specified location in the packaged pom."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"missing-artifact"
argument_list|,
literal|"The artifact file ["
operator|+
name|file
operator|+
literal|"] cannot be found for metadata."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addFailure
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|problem
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
comment|// TODO: reason could be an i18n key derived from the processor and the problem ID and the
name|database
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
name|ROLE_HINT
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|adjustDistributionArtifactHandler
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
comment|// need to tweak these as they aren't currently in the known type converters. TODO - add them in Maven
if|if
condition|(
literal|"distribution-zip"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|artifact
operator|.
name|setArtifactHandler
argument_list|(
operator|new
name|DefaultArtifactHandler
argument_list|(
literal|"zip"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"distribution-tgz"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|artifact
operator|.
name|setArtifactHandler
argument_list|(
operator|new
name|DefaultArtifactHandler
argument_list|(
literal|"tar.gz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Model
name|readArtifactModel
parameter_list|(
name|File
name|file
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
name|Model
name|model
init|=
literal|null
decl_stmt|;
name|JarFile
name|jar
init|=
literal|null
decl_stmt|;
try|try
block|{
name|jar
operator|=
operator|new
name|JarFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
comment|//Get the entry and its input stream.
name|JarEntry
name|entry
init|=
name|jar
operator|.
name|getJarEntry
argument_list|(
literal|"META-INF/maven/"
operator|+
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|"/"
operator|+
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"/pom.xml"
argument_list|)
decl_stmt|;
comment|// If the entry is not null, extract it.
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|model
operator|=
name|readModel
argument_list|(
name|jar
operator|.
name|getInputStream
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|model
operator|.
name|getGroupId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|model
operator|.
name|setGroupId
argument_list|(
name|model
operator|.
name|getParent
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|model
operator|.
name|getVersion
argument_list|()
operator|==
literal|null
condition|)
block|{
name|model
operator|.
name|setVersion
argument_list|(
name|model
operator|.
name|getParent
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"Unable to read artifact to extract model: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XmlPullParserException
name|e
parameter_list|)
block|{
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"Unable to parse extracted model: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|jar
operator|!=
literal|null
condition|)
block|{
comment|//noinspection UnusedCatchParameter
try|try
block|{
name|jar
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
comment|// ignore
block|}
block|}
block|}
return|return
name|model
return|;
block|}
specifier|private
name|void
name|addWarning
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
comment|// TODO: reason could be an i18n key derived from the processor and the problem ID and the
name|database
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
name|ROLE_HINT
argument_list|,
literal|"bad-location"
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Model
name|readModel
parameter_list|(
name|InputStream
name|entryStream
parameter_list|)
throws|throws
name|IOException
throws|,
name|XmlPullParserException
block|{
name|Reader
name|isReader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|entryStream
argument_list|)
decl_stmt|;
name|Model
name|model
decl_stmt|;
try|try
block|{
name|MavenXpp3Reader
name|pomReader
init|=
operator|new
name|MavenXpp3Reader
argument_list|()
decl_stmt|;
name|model
operator|=
name|pomReader
operator|.
name|read
argument_list|(
name|isReader
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|isReader
argument_list|)
expr_stmt|;
block|}
return|return
name|model
return|;
block|}
block|}
end_class

end_unit

