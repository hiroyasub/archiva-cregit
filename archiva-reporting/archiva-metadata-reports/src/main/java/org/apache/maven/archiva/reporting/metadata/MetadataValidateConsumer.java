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
name|metadata
package|;
end_package

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
comment|/**  * MetadataValidateConsumer   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * TODO: whoops, how do we consumer metadata?    */
end_comment

begin_class
specifier|public
class|class
name|MetadataValidateConsumer
block|{
comment|//    /**
comment|//     * Process the metadata encountered in the repository and report all errors found, if any.
comment|//     *
comment|//     * @param metadata   the metadata to be processed.
comment|//     * @param repository the repository where the metadata was encountered
comment|//     * @param reporter   the ReportingDatabase to receive processing results
comment|//     */
comment|//    public void processMetadata( RepositoryMetadata metadata, ArtifactRepository repository )
comment|//    {
comment|//        if ( metadata.storedInGroupDirectory() )
comment|//        {
comment|//            try
comment|//            {
comment|//                checkPluginMetadata( metadata, repository );
comment|//            }
comment|//            catch ( IOException e )
comment|//            {
comment|//                addWarning( metadata, null, "Error getting plugin artifact directories versions: " + e );
comment|//            }
comment|//        }
comment|//        else
comment|//        {
comment|//            Versioning versioning = metadata.getMetadata().getVersioning();
comment|//            boolean found = false;
comment|//            if ( versioning != null )
comment|//            {
comment|//                String lastUpdated = versioning.getLastUpdated();
comment|//                if ( lastUpdated != null&& lastUpdated.length() != 0 )
comment|//                {
comment|//                    found = true;
comment|//                }
comment|//            }
comment|//            if ( !found )
comment|//            {
comment|//                addFailure( metadata, "missing-last-updated", "Missing lastUpdated element inside the metadata." );
comment|//            }
comment|//
comment|//            if ( metadata.storedInArtifactVersionDirectory() )
comment|//            {
comment|//                checkSnapshotMetadata( metadata, repository );
comment|//            }
comment|//            else
comment|//            {
comment|//                checkMetadataVersions( metadata, repository );
comment|//
comment|//                try
comment|//                {
comment|//                    checkRepositoryVersions( metadata, repository );
comment|//                }
comment|//                catch ( IOException e )
comment|//                {
comment|//                    String reason = "Error getting plugin artifact directories versions: " + e;
comment|//                    addWarning( metadata, null, reason );
comment|//                }
comment|//            }
comment|//        }
comment|//    }
comment|//
comment|//    private void addWarning( RepositoryMetadata metadata, String problem, String reason )
comment|//    {
comment|//        // TODO: reason could be an i18n key derived from the processor and the problem ID and the
comment|//        database.addWarning( metadata, ROLE_HINT, problem, reason );
comment|//    }
comment|//
comment|//    /**
comment|//     * Method for processing a GroupRepositoryMetadata
comment|//     *
comment|//     * @param metadata   the metadata to be processed.
comment|//     * @param repository the repository where the metadata was encountered
comment|//     * @param reporter   the ReportingDatabase to receive processing results
comment|//     */
comment|//    private void checkPluginMetadata( RepositoryMetadata metadata, ArtifactRepository repository )
comment|//        throws IOException
comment|//    {
comment|//        File metadataDir = new File( repository.getBasedir(), repository.pathOfRemoteRepositoryMetadata( metadata ) )
comment|//            .getParentFile();
comment|//        List pluginDirs = getArtifactIdFiles( metadataDir );
comment|//
comment|//        Map prefixes = new HashMap();
comment|//        for ( Iterator plugins = metadata.getMetadata().getPlugins().iterator(); plugins.hasNext(); )
comment|//        {
comment|//            Plugin plugin = (Plugin) plugins.next();
comment|//
comment|//            String artifactId = plugin.getArtifactId();
comment|//            if ( artifactId == null || artifactId.length() == 0 )
comment|//            {
comment|//                addFailure( metadata, "missing-artifact-id:" + plugin.getPrefix(),
comment|//                            "Missing or empty artifactId in group metadata for plugin " + plugin.getPrefix() );
comment|//            }
comment|//
comment|//            String prefix = plugin.getPrefix();
comment|//            if ( prefix == null || prefix.length() == 0 )
comment|//            {
comment|//                addFailure( metadata, "missing-plugin-prefix:" + artifactId,
comment|//                            "Missing or empty plugin prefix for artifactId " + artifactId + "." );
comment|//            }
comment|//            else
comment|//            {
comment|//                if ( prefixes.containsKey( prefix ) )
comment|//                {
comment|//                    addFailure( metadata, "duplicate-plugin-prefix:" + prefix, "Duplicate plugin prefix found: "
comment|//                        + prefix + "." );
comment|//                }
comment|//                else
comment|//                {
comment|//                    prefixes.put( prefix, plugin );
comment|//                }
comment|//            }
comment|//
comment|//            if ( artifactId != null&& artifactId.length()> 0 )
comment|//            {
comment|//                File pluginDir = new File( metadataDir, artifactId );
comment|//                if ( !pluginDirs.contains( pluginDir ) )
comment|//                {
comment|//                    addFailure( metadata, "missing-plugin-from-repository:" + artifactId, "Metadata plugin "
comment|//                        + artifactId + " not found in the repository" );
comment|//                }
comment|//                else
comment|//                {
comment|//                    pluginDirs.remove( pluginDir );
comment|//                }
comment|//            }
comment|//        }
comment|//
comment|//        if ( pluginDirs.size()> 0 )
comment|//        {
comment|//            for ( Iterator plugins = pluginDirs.iterator(); plugins.hasNext(); )
comment|//            {
comment|//                File plugin = (File) plugins.next();
comment|//                addFailure( metadata, "missing-plugin-from-metadata:" + plugin.getName(), "Plugin " + plugin.getName()
comment|//                    + " is present in the repository but " + "missing in the metadata." );
comment|//            }
comment|//        }
comment|//    }
comment|//
comment|//    /**
comment|//     * Method for processing a SnapshotArtifactRepository
comment|//     *
comment|//     * @param metadata   the metadata to be processed.
comment|//     * @param repository the repository where the metadata was encountered
comment|//     * @param reporter   the ReportingDatabase to receive processing results
comment|//     */
comment|//    private void checkSnapshotMetadata( RepositoryMetadata metadata, ArtifactRepository repository )
comment|//    {
comment|//        RepositoryQueryLayer repositoryQueryLayer = repositoryQueryLayerFactory.createRepositoryQueryLayer( repository );
comment|//
comment|//        Versioning versioning = metadata.getMetadata().getVersioning();
comment|//        if ( versioning != null )
comment|//        {
comment|//            Snapshot snapshot = versioning.getSnapshot();
comment|//
comment|//            String version = StringUtils.replace( metadata.getBaseVersion(), Artifact.SNAPSHOT_VERSION, snapshot
comment|//                .getTimestamp()
comment|//                + "-" + snapshot.getBuildNumber() );
comment|//            Artifact artifact = artifactFactory.createProjectArtifact( metadata.getGroupId(), metadata.getArtifactId(),
comment|//                                                                       version );
comment|//            artifact.isSnapshot(); // trigger baseVersion correction
comment|//
comment|//            if ( !repositoryQueryLayer.containsArtifact( artifact ) )
comment|//            {
comment|//                addFailure( metadata, "missing-snapshot-artifact-from-repository:" + version, "Snapshot artifact "
comment|//                    + version + " does not exist." );
comment|//            }
comment|//        }
comment|//    }
comment|//
comment|//    /**
comment|//     * Method for validating the versions declared inside an ArtifactRepositoryMetadata
comment|//     *
comment|//     * @param metadata   the metadata to be processed.
comment|//     * @param repository the repository where the metadata was encountered
comment|//     * @param reporter   the ReportingDatabase to receive processing results
comment|//     */
comment|//    private void checkMetadataVersions( RepositoryMetadata metadata, ArtifactRepository repository )
comment|//    {
comment|//        RepositoryQueryLayer repositoryQueryLayer = repositoryQueryLayerFactory.createRepositoryQueryLayer( repository );
comment|//
comment|//        Versioning versioning = metadata.getMetadata().getVersioning();
comment|//        if ( versioning != null )
comment|//        {
comment|//            for ( Iterator versions = versioning.getVersions().iterator(); versions.hasNext(); )
comment|//            {
comment|//                String version = (String) versions.next();
comment|//
comment|//                Artifact artifact = artifactFactory.createProjectArtifact( metadata.getGroupId(), metadata
comment|//                    .getArtifactId(), version );
comment|//
comment|//                if ( !repositoryQueryLayer.containsArtifact( artifact ) )
comment|//                {
comment|//                    addFailure( metadata, "missing-artifact-from-repository:" + version, "Artifact version " + version
comment|//                        + " is present in metadata but " + "missing in the repository." );
comment|//                }
comment|//            }
comment|//        }
comment|//    }
comment|//
comment|//    /**
comment|//     * Searches the artifact repository directory for all versions and verifies that all of them are listed in the
comment|//     * ArtifactRepositoryMetadata
comment|//     *
comment|//     * @param metadata   the metadata to be processed.
comment|//     * @param repository the repository where the metadata was encountered
comment|//     * @param reporter   the ReportingDatabase to receive processing results
comment|//     * @throws java.io.IOException if there is a problem reading from the file system
comment|//     */
comment|//    private void checkRepositoryVersions( RepositoryMetadata metadata, ArtifactRepository repository )
comment|//        throws IOException
comment|//    {
comment|//        Versioning versioning = metadata.getMetadata().getVersioning();
comment|//        List metadataVersions = versioning != null ? versioning.getVersions() : Collections.EMPTY_LIST;
comment|//        File versionsDir = new File( repository.getBasedir(), repository.pathOfRemoteRepositoryMetadata( metadata ) )
comment|//            .getParentFile();
comment|//
comment|//        // TODO: I don't know how this condition can happen, but it was seen on the main repository.
comment|//        // Avoid hard failure
comment|//        if ( versionsDir.exists() )
comment|//        {
comment|//            List versions = FileUtils.getFileNames( versionsDir, "*/*.pom", null, false );
comment|//            for ( Iterator i = versions.iterator(); i.hasNext(); )
comment|//            {
comment|//                File path = new File( (String) i.next() );
comment|//                String version = path.getParentFile().getName();
comment|//                if ( !metadataVersions.contains( version ) )
comment|//                {
comment|//                    addFailure( metadata, "missing-artifact-from-metadata:" + version, "Artifact version " + version
comment|//                        + " found in the repository but " + "missing in the metadata." );
comment|//                }
comment|//            }
comment|//        }
comment|//        else
comment|//        {
comment|//            addFailure( metadata, null, "Metadata's directory did not exist: " + versionsDir );
comment|//        }
comment|//    }
comment|//
comment|//    /**
comment|//     * Used to gather artifactIds from a groupId directory.
comment|//     *
comment|//     * @param groupIdDir the directory of the group
comment|//     * @return the list of artifact ID File objects for each directory
comment|//     * @throws IOException if there was a failure to read the directories
comment|//     */
comment|//    private List getArtifactIdFiles( File groupIdDir )
comment|//        throws IOException
comment|//    {
comment|//        List artifactIdFiles = new ArrayList();
comment|//
comment|//        File[] files = groupIdDir.listFiles();
comment|//        if ( files != null )
comment|//        {
comment|//            for ( Iterator i = Arrays.asList( files ).iterator(); i.hasNext(); )
comment|//            {
comment|//                File artifactDir = (File) i.next();
comment|//
comment|//                if ( artifactDir.isDirectory() )
comment|//                {
comment|//                    List versions = FileUtils.getFileNames( artifactDir, "*/*.pom", null, false );
comment|//                    if ( versions.size()> 0 )
comment|//                    {
comment|//                        artifactIdFiles.add( artifactDir );
comment|//                    }
comment|//                }
comment|//            }
comment|//        }
comment|//
comment|//        return artifactIdFiles;
comment|//    }
comment|//
comment|//    private void addFailure( RepositoryMetadata metadata, String problem, String reason )
comment|//    {
comment|//        // TODO: reason could be an i18n key derived from the processor and the problem ID and the
comment|//        database.addFailure( metadata, ROLE_HINT, problem, reason );
comment|//    }
block|}
end_class

end_unit

