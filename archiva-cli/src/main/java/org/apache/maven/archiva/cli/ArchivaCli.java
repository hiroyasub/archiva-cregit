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
name|cli
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
name|cli
operator|.
name|CommandLine
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
name|cli
operator|.
name|OptionBuilder
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
name|cli
operator|.
name|Options
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|conversion
operator|.
name|LegacyRepositoryConverter
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
name|converter
operator|.
name|RepositoryConversionException
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
name|discoverer
operator|.
name|DiscovererException
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
name|PlexusContainer
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
name|tools
operator|.
name|cli
operator|.
name|AbstractCli
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * @author Jason van Zyl  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaCli
extends|extends
name|AbstractCli
block|{
comment|// ----------------------------------------------------------------------------
comment|// Options
comment|// ----------------------------------------------------------------------------
specifier|public
specifier|static
specifier|final
name|char
name|CONVERT
init|=
literal|'c'
decl_stmt|;
comment|// ----------------------------------------------------------------------------
comment|// Properties controlling Repository conversion
comment|// ----------------------------------------------------------------------------
specifier|public
specifier|static
specifier|final
name|String
name|SOURCE_REPO_PATH
init|=
literal|"sourceRepositoryPath"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TARGET_REPO_PATH
init|=
literal|"targetRepositoryPath"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BLACKLISTED_PATTERNS
init|=
literal|"blacklistPatterns"
decl_stmt|;
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
operator|new
name|ArchivaCli
argument_list|()
operator|.
name|execute
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPomPropertiesPath
parameter_list|()
block|{
return|return
literal|"META-INF/maven/org.apache.maven.archiva/archiva-cli/pom.properties"
return|;
block|}
specifier|public
name|Options
name|buildCliOptions
parameter_list|(
name|Options
name|options
parameter_list|)
block|{
name|options
operator|.
name|addOption
argument_list|(
name|OptionBuilder
operator|.
name|withLongOpt
argument_list|(
literal|"convert"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|withDescription
argument_list|(
literal|"Convert a legacy Maven 1.x repository to a Maven 2.x repository using a properties file to describe the conversion."
argument_list|)
operator|.
name|create
argument_list|(
name|CONVERT
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|options
return|;
block|}
specifier|public
name|void
name|invokePlexusComponent
parameter_list|(
name|CommandLine
name|cli
parameter_list|,
name|PlexusContainer
name|plexus
parameter_list|)
throws|throws
name|Exception
block|{
name|LegacyRepositoryConverter
name|legacyRepositoryConverter
init|=
operator|(
name|LegacyRepositoryConverter
operator|)
name|plexus
operator|.
name|lookup
argument_list|(
name|LegacyRepositoryConverter
operator|.
name|ROLE
argument_list|)
decl_stmt|;
if|if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|CONVERT
argument_list|)
condition|)
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|p
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|cli
operator|.
name|getOptionValue
argument_list|(
name|CONVERT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|showFatalError
argument_list|(
literal|"Cannot find properties file which describes the conversion."
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|File
name|oldRepositoryPath
init|=
operator|new
name|File
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
name|SOURCE_REPO_PATH
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|newRepositoryPath
init|=
operator|new
name|File
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
name|TARGET_REPO_PATH
argument_list|)
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Converting "
operator|+
name|oldRepositoryPath
operator|+
literal|" to "
operator|+
name|newRepositoryPath
argument_list|)
expr_stmt|;
name|List
name|blacklistedPatterns
init|=
literal|null
decl_stmt|;
name|String
name|s
init|=
name|p
operator|.
name|getProperty
argument_list|(
name|BLACKLISTED_PATTERNS
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|blacklistedPatterns
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|StringUtils
operator|.
name|split
argument_list|(
name|s
argument_list|,
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|legacyRepositoryConverter
operator|.
name|convertLegacyRepository
argument_list|(
name|oldRepositoryPath
argument_list|,
name|newRepositoryPath
argument_list|,
name|blacklistedPatterns
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryConversionException
name|e
parameter_list|)
block|{
name|showFatalError
argument_list|(
literal|"Error converting repository."
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DiscovererException
name|e
parameter_list|)
block|{
name|showFatalError
argument_list|(
literal|"Error discovery artifacts to convert."
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

