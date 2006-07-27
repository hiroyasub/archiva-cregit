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
name|indexing
operator|.
name|record
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
name|project
operator|.
name|MavenProject
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
name|project
operator|.
name|MavenProjectBuilder
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
name|project
operator|.
name|ProjectBuildingException
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
name|indexing
operator|.
name|RepositoryIndexException
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
name|Xpp3Dom
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
name|Xpp3DomBuilder
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
name|InputStreamReader
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
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipFile
import|;
end_import

begin_comment
comment|/**  * An index record type for the standard index.  *  * @author Edwin Punzalan  * @author Brett Porter  * @plexus.component role="org.apache.maven.repository.indexing.record.RepositoryIndexRecordFactory" role-hint="standard"  */
end_comment

begin_class
specifier|public
class|class
name|StandardArtifactIndexRecordFactory
extends|extends
name|AbstractArtifactIndexRecordFactory
block|{
comment|/**      * A list of artifact types to treat as a zip archive.      *      * @todo this should be smarter (perhaps use plexus archiver to look for an unarchiver, and make the ones for zip configurable since sar, par, etc can be added at random.      */
specifier|private
specifier|static
specifier|final
name|Set
name|ARCHIVE_TYPES
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
literal|"zip"
block|,
literal|"ejb"
block|,
literal|"par"
block|,
literal|"sar"
block|,
literal|"war"
block|,
literal|"ear"
block|}
argument_list|)
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MavenProjectBuilder
name|projectBuilder
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PLUGIN_METADATA_NAME
init|=
literal|"META-INF/maven/plugin.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARCHETYPE_METADATA_NAME
init|=
literal|"META-INF/maven/archetype.xml"
decl_stmt|;
specifier|public
name|RepositoryIndexRecord
name|createRecord
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|StandardArtifactIndexRecord
name|record
init|=
literal|null
decl_stmt|;
name|File
name|file
init|=
name|artifact
operator|.
name|getFile
argument_list|()
decl_stmt|;
comment|// TODO: is this condition really a possibility?
if|if
condition|(
name|file
operator|!=
literal|null
operator|&&
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|String
name|md5
init|=
name|readChecksum
argument_list|(
name|file
argument_list|,
name|Digester
operator|.
name|MD5
argument_list|)
decl_stmt|;
name|String
name|sha1
init|=
name|readChecksum
argument_list|(
name|file
argument_list|,
name|Digester
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|List
name|files
init|=
literal|null
decl_stmt|;
try|try
block|{
name|files
operator|=
name|readFilesInArchive
argument_list|(
name|file
argument_list|)
expr_stmt|;
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
name|error
argument_list|(
literal|"Error reading artifact file, omitting from index: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// If it's an archive with no files, don't create a record
if|if
condition|(
operator|!
name|ARCHIVE_TYPES
operator|.
name|contains
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|||
name|files
operator|!=
literal|null
condition|)
block|{
name|record
operator|=
operator|new
name|StandardArtifactIndexRecord
argument_list|()
expr_stmt|;
name|record
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setClassifier
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setType
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setMd5Checksum
argument_list|(
name|md5
argument_list|)
expr_stmt|;
name|record
operator|.
name|setSha1Checksum
argument_list|(
name|sha1
argument_list|)
expr_stmt|;
name|record
operator|.
name|setFilename
argument_list|(
name|artifact
operator|.
name|getRepository
argument_list|()
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
name|record
operator|.
name|setLastModified
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setSize
argument_list|(
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setRepository
argument_list|(
name|artifact
operator|.
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
name|populateArchiveEntries
argument_list|(
name|files
argument_list|,
name|record
argument_list|,
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|"pom"
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
name|Artifact
name|pomArtifact
init|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|pomFile
init|=
operator|new
name|File
argument_list|(
name|artifact
operator|.
name|getRepository
argument_list|()
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|artifact
operator|.
name|getRepository
argument_list|()
operator|.
name|pathOf
argument_list|(
name|pomArtifact
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|pomFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|populatePomEntries
argument_list|(
name|readPom
argument_list|(
name|pomArtifact
argument_list|,
name|artifact
operator|.
name|getRepository
argument_list|()
argument_list|)
argument_list|,
name|record
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Model
name|model
init|=
name|readPom
argument_list|(
name|artifact
argument_list|,
name|artifact
operator|.
name|getRepository
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"pom"
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
comment|// Don't return a record for a POM that is does not belong on its own
name|record
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|populatePomEntries
argument_list|(
name|model
argument_list|,
name|record
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|record
return|;
block|}
specifier|private
name|void
name|populatePomEntries
parameter_list|(
name|Model
name|pom
parameter_list|,
name|StandardArtifactIndexRecord
name|record
parameter_list|)
block|{
name|record
operator|.
name|setPackaging
argument_list|(
name|pom
operator|.
name|getPackaging
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setProjectName
argument_list|(
name|pom
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setProjectDescription
argument_list|(
name|pom
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setInceptionYear
argument_list|(
name|pom
operator|.
name|getInceptionYear
argument_list|()
argument_list|)
expr_stmt|;
comment|/* TODO: fields for later                 indexPlugins( doc, FLD_PLUGINS_BUILD, pom.getBuild().getPlugins().iterator() );                 indexReportPlugins( doc, FLD_PLUGINS_REPORT, pom.getReporting().getPlugins().iterator() );                 record.setDependencies( dependencies );                 record.setLicenses( licenses ); */
block|}
specifier|private
name|Model
name|readPom
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
comment|// TODO: will this pollute with local repo metadata?
name|Model
name|model
decl_stmt|;
try|try
block|{
name|MavenProject
name|project
init|=
name|projectBuilder
operator|.
name|buildFromRepository
argument_list|(
name|artifact
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|model
operator|=
name|project
operator|.
name|getModel
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProjectBuildingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Unable to read project: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|model
return|;
block|}
specifier|private
name|void
name|populateArchiveEntries
parameter_list|(
name|List
name|files
parameter_list|,
name|StandardArtifactIndexRecord
name|record
parameter_list|,
name|File
name|artifactFile
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|StringBuffer
name|classes
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|StringBuffer
name|fileBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|files
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// ignore directories
if|if
condition|(
operator|!
name|name
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|fileBuffer
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isClass
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|classes
operator|.
name|append
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|6
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|PLUGIN_METADATA_NAME
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|populatePluginEntries
argument_list|(
name|readXmlMetadataFileInJar
argument_list|(
name|artifactFile
argument_list|,
name|PLUGIN_METADATA_NAME
argument_list|)
argument_list|,
name|record
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|ARCHETYPE_METADATA_NAME
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|populateArchetypeEntries
argument_list|(
name|record
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|classes
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|record
operator|.
name|setClasses
argument_list|(
name|classes
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fileBuffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|record
operator|.
name|setFiles
argument_list|(
name|fileBuffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|populateArchetypeEntries
parameter_list|(
name|StandardArtifactIndexRecord
name|record
parameter_list|)
block|{
comment|// Typically discovered as a JAR
name|record
operator|.
name|setType
argument_list|(
literal|"maven-archetype"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Xpp3Dom
name|readXmlMetadataFileInJar
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
comment|// TODO: would be more efficient with original ZipEntry still around
name|Xpp3Dom
name|xpp3Dom
decl_stmt|;
name|ZipFile
name|zipFile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|zipFile
operator|=
operator|new
name|ZipFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|ZipEntry
name|entry
init|=
name|zipFile
operator|.
name|getEntry
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|xpp3Dom
operator|=
name|Xpp3DomBuilder
operator|.
name|build
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|zipFile
operator|.
name|getInputStream
argument_list|(
name|entry
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ZipException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Unable to read plugin metadata: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Unable to read plugin metadata: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|XmlPullParserException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryIndexException
argument_list|(
literal|"Unable to read plugin metadata: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closeQuietly
argument_list|(
name|zipFile
argument_list|)
expr_stmt|;
block|}
return|return
name|xpp3Dom
return|;
block|}
specifier|public
name|void
name|populatePluginEntries
parameter_list|(
name|Xpp3Dom
name|metadata
parameter_list|,
name|StandardArtifactIndexRecord
name|record
parameter_list|)
block|{
comment|// Typically discovered as a JAR
name|record
operator|.
name|setType
argument_list|(
literal|"maven-plugin"
argument_list|)
expr_stmt|;
name|Xpp3Dom
name|prefix
init|=
name|metadata
operator|.
name|getChild
argument_list|(
literal|"goalPrefix"
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|record
operator|.
name|setPluginPrefix
argument_list|(
name|prefix
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

