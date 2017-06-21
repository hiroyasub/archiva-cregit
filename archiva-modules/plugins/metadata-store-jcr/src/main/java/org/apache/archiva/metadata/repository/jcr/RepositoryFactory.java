begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|jcr
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
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
name|JcrConstants
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
name|oak
operator|.
name|Oak
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
name|oak
operator|.
name|api
operator|.
name|Type
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
name|oak
operator|.
name|jcr
operator|.
name|Jcr
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|IndexConstants
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|IndexUtils
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|ExtractedTextCache
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|IndexCopier
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|IndexTracker
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|LuceneIndexEditorProvider
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|LuceneIndexProvider
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|hybrid
operator|.
name|DocumentQueue
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|hybrid
operator|.
name|ExternalObserverBuilder
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|hybrid
operator|.
name|LocalIndexObserver
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|hybrid
operator|.
name|NRTIndexFactory
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|reader
operator|.
name|DefaultIndexReaderFactory
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
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|util
operator|.
name|LuceneInitializerHelper
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
name|oak
operator|.
name|segment
operator|.
name|SegmentNodeStore
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
name|oak
operator|.
name|segment
operator|.
name|SegmentNodeStoreBuilders
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
name|oak
operator|.
name|segment
operator|.
name|file
operator|.
name|FileStore
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
name|oak
operator|.
name|segment
operator|.
name|file
operator|.
name|FileStoreBuilder
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
name|oak
operator|.
name|segment
operator|.
name|file
operator|.
name|InvalidFileStoreVersionException
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
name|oak
operator|.
name|spi
operator|.
name|commit
operator|.
name|Observer
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
name|oak
operator|.
name|spi
operator|.
name|lifecycle
operator|.
name|RepositoryInitializer
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
name|oak
operator|.
name|spi
operator|.
name|mount
operator|.
name|MountInfoProvider
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
name|oak
operator|.
name|spi
operator|.
name|mount
operator|.
name|Mounts
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
name|oak
operator|.
name|spi
operator|.
name|query
operator|.
name|QueryIndexProvider
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
name|oak
operator|.
name|spi
operator|.
name|state
operator|.
name|NodeBuilder
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
name|oak
operator|.
name|spi
operator|.
name|state
operator|.
name|NodeStore
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
name|oak
operator|.
name|stats
operator|.
name|StatisticsProvider
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
name|javax
operator|.
name|annotation
operator|.
name|Nonnull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
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
name|util
operator|.
name|concurrent
operator|.
name|Executor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|IndexConstants
operator|.
name|INDEX_DEFINITIONS_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|oak
operator|.
name|plugins
operator|.
name|index
operator|.
name|lucene
operator|.
name|LuceneIndexConstants
operator|.
name|INCLUDE_PROPERTY_TYPES
import|;
end_import

begin_comment
comment|/**  * Created by martin on 14.06.17.  *  * @author Martin Stockhammer  * @since 3.0.0  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryFactory
block|{
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RepositoryFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SEGMENT_FILE_TYPE
init|=
literal|"oak-segment-tar"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IN_MEMORY_TYPE
init|=
literal|"oak-memory"
decl_stmt|;
name|String
name|storeType
init|=
name|SEGMENT_FILE_TYPE
decl_stmt|;
name|Path
name|repositoryPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"repository"
argument_list|)
decl_stmt|;
specifier|public
name|Repository
name|createRepository
parameter_list|( )
throws|throws
name|IOException
throws|,
name|InvalidFileStoreVersionException
block|{
name|NodeStore
name|nodeStore
decl_stmt|;
if|if
condition|(
name|SEGMENT_FILE_TYPE
operator|.
name|equals
argument_list|(
name|storeType
argument_list|)
condition|)
block|{
name|FileStore
name|fs
init|=
name|FileStoreBuilder
operator|.
name|fileStoreBuilder
argument_list|(
name|repositoryPath
operator|.
name|toFile
argument_list|( )
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|nodeStore
operator|=
name|SegmentNodeStoreBuilders
operator|.
name|builder
argument_list|(
name|fs
argument_list|)
operator|.
name|build
argument_list|( )
expr_stmt|;
block|}
if|else if
condition|(
name|IN_MEMORY_TYPE
operator|.
name|equals
argument_list|(
name|storeType
argument_list|)
condition|)
block|{
name|nodeStore
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Store type "
operator|+
name|storeType
operator|+
literal|" not recognized"
argument_list|)
throw|;
block|}
name|Oak
name|oak
init|=
name|nodeStore
operator|==
literal|null
condition|?
operator|new
name|Oak
argument_list|()
else|:
operator|new
name|Oak
argument_list|(
name|nodeStore
argument_list|)
decl_stmt|;
name|oak
operator|.
name|with
argument_list|(
operator|new
name|RepositoryInitializer
argument_list|( )
block|{
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
annotation|@
name|Nonnull
name|NodeBuilder
name|root
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Creating index "
argument_list|)
expr_stmt|;
name|NodeBuilder
name|lucene
init|=
name|IndexUtils
operator|.
name|getOrCreateOakIndex
argument_list|(
name|root
argument_list|)
operator|.
name|child
argument_list|(
literal|"lucene"
argument_list|)
decl_stmt|;
name|lucene
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
literal|"oak:QueryIndexDefinition"
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
expr_stmt|;
name|lucene
operator|.
name|setProperty
argument_list|(
literal|"compatVersion"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|lucene
operator|.
name|setProperty
argument_list|(
literal|"type"
argument_list|,
literal|"lucene"
argument_list|)
expr_stmt|;
comment|// lucene.setProperty("async", "async");
name|lucene
operator|.
name|setProperty
argument_list|(
name|INCLUDE_PROPERTY_TYPES
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"String"
argument_list|)
argument_list|,
name|Type
operator|.
name|STRINGS
argument_list|)
expr_stmt|;
comment|// lucene.setProperty("refresh",true);
name|lucene
operator|.
name|setProperty
argument_list|(
literal|"async"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"async"
argument_list|,
literal|"sync"
argument_list|)
argument_list|,
name|Type
operator|.
name|STRINGS
argument_list|)
expr_stmt|;
name|NodeBuilder
name|rules
init|=
name|lucene
operator|.
name|child
argument_list|(
literal|"indexRules"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
name|JcrConstants
operator|.
name|NT_UNSTRUCTURED
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
decl_stmt|;
name|rules
operator|.
name|setProperty
argument_list|(
literal|":childOrder"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"archiva:projectVersion"
argument_list|,
literal|"archiva:artifact"
argument_list|,
literal|"archiva:facet"
argument_list|,
literal|"archiva:namespace"
argument_list|,
literal|"archiva:project"
argument_list|)
argument_list|,
name|Type
operator|.
name|STRINGS
argument_list|)
expr_stmt|;
name|NodeBuilder
name|allProps
init|=
name|rules
operator|.
name|child
argument_list|(
literal|"archiva:projectVersion"
argument_list|)
operator|.
name|child
argument_list|(
literal|"properties"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
literal|"nt:unstructured"
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|":childOrder"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"allProps"
argument_list|)
argument_list|,
name|Type
operator|.
name|STRINGS
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"indexNodeName"
argument_list|,
literal|true
argument_list|)
operator|.
name|child
argument_list|(
literal|"allProps"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
name|JcrConstants
operator|.
name|NT_UNSTRUCTURED
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
decl_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"name"
argument_list|,
literal|".*"
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"isRegexp"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"nodeScopeIndex"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"index"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"analyzed"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// allProps.setProperty("propertyIndex",true);
name|allProps
operator|=
name|rules
operator|.
name|child
argument_list|(
literal|"archiva:artifact"
argument_list|)
operator|.
name|child
argument_list|(
literal|"properties"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
literal|"nt:unstructured"
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|":childOrder"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"allProps"
argument_list|)
argument_list|,
name|Type
operator|.
name|STRINGS
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"indexNodeName"
argument_list|,
literal|true
argument_list|)
operator|.
name|child
argument_list|(
literal|"allProps"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
name|JcrConstants
operator|.
name|NT_UNSTRUCTURED
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"name"
argument_list|,
literal|".*"
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"isRegexp"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"nodeScopeIndex"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"index"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"analyzed"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|=
name|rules
operator|.
name|child
argument_list|(
literal|"archiva:facet"
argument_list|)
operator|.
name|child
argument_list|(
literal|"properties"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
literal|"nt:unstructured"
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|":childOrder"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"allProps"
argument_list|)
argument_list|,
name|Type
operator|.
name|STRINGS
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"indexNodeName"
argument_list|,
literal|true
argument_list|)
operator|.
name|child
argument_list|(
literal|"allProps"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
name|JcrConstants
operator|.
name|NT_UNSTRUCTURED
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"name"
argument_list|,
literal|".*"
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"isRegexp"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"nodeScopeIndex"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"index"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"analyzed"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|=
name|rules
operator|.
name|child
argument_list|(
literal|"archiva:namespace"
argument_list|)
operator|.
name|child
argument_list|(
literal|"properties"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
literal|"nt:unstructured"
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|":childOrder"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"allProps"
argument_list|)
argument_list|,
name|Type
operator|.
name|STRINGS
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"indexNodeName"
argument_list|,
literal|true
argument_list|)
operator|.
name|child
argument_list|(
literal|"allProps"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
name|JcrConstants
operator|.
name|NT_UNSTRUCTURED
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"name"
argument_list|,
literal|".*"
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"isRegexp"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"nodeScopeIndex"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"index"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"analyzed"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|=
name|rules
operator|.
name|child
argument_list|(
literal|"archiva:project"
argument_list|)
operator|.
name|child
argument_list|(
literal|"properties"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
literal|"nt:unstructured"
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|":childOrder"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"allProps"
argument_list|)
argument_list|,
name|Type
operator|.
name|STRINGS
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"indexNodeName"
argument_list|,
literal|true
argument_list|)
operator|.
name|child
argument_list|(
literal|"allProps"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_PRIMARYTYPE
argument_list|,
name|JcrConstants
operator|.
name|NT_UNSTRUCTURED
argument_list|,
name|Type
operator|.
name|NAME
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"name"
argument_list|,
literal|".*"
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"isRegexp"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"nodeScopeIndex"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"index"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|allProps
operator|.
name|setProperty
argument_list|(
literal|"analyzed"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Index: "
operator|+
name|lucene
operator|+
literal|" myIndex "
operator|+
name|lucene
operator|.
name|getChildNode
argument_list|(
literal|"myIndex"
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"myIndex "
operator|+
name|lucene
operator|.
name|getChildNode
argument_list|(
literal|"myIndex"
argument_list|)
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
comment|// IndexUtils.createIndexDefinition(  )
block|}
block|}
argument_list|)
expr_stmt|;
name|ExecutorService
name|executorService
init|=
name|createExecutor
argument_list|()
decl_stmt|;
name|StatisticsProvider
name|statsProvider
init|=
name|StatisticsProvider
operator|.
name|NOOP
decl_stmt|;
name|int
name|queueSize
init|=
name|Integer
operator|.
name|getInteger
argument_list|(
literal|"queueSize"
argument_list|,
literal|10000
argument_list|)
decl_stmt|;
name|File
name|indexDir
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"archiva_index"
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Queue Index "
operator|+
name|indexDir
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|IndexCopier
name|indexCopier
init|=
operator|new
name|IndexCopier
argument_list|(
name|executorService
argument_list|,
name|indexDir
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|NRTIndexFactory
name|nrtIndexFactory
init|=
operator|new
name|NRTIndexFactory
argument_list|(
name|indexCopier
argument_list|,
name|statsProvider
argument_list|)
decl_stmt|;
name|MountInfoProvider
name|mountInfoProvider
init|=
name|Mounts
operator|.
name|defaultMountInfoProvider
argument_list|( )
decl_stmt|;
name|IndexTracker
name|tracker
init|=
operator|new
name|IndexTracker
argument_list|(
operator|new
name|DefaultIndexReaderFactory
argument_list|(
name|mountInfoProvider
argument_list|,
name|indexCopier
argument_list|)
argument_list|,
name|nrtIndexFactory
argument_list|)
decl_stmt|;
name|DocumentQueue
name|queue
init|=
operator|new
name|DocumentQueue
argument_list|(
name|queueSize
argument_list|,
name|tracker
argument_list|,
name|executorService
argument_list|,
name|statsProvider
argument_list|)
decl_stmt|;
name|LocalIndexObserver
name|localIndexObserver
init|=
operator|new
name|LocalIndexObserver
argument_list|(
name|queue
argument_list|,
name|statsProvider
argument_list|)
decl_stmt|;
name|LuceneIndexProvider
name|provider
init|=
operator|new
name|LuceneIndexProvider
argument_list|(
name|tracker
argument_list|)
decl_stmt|;
comment|//        ExternalObserverBuilder builder = new ExternalObserverBuilder(queue, tracker, statsProvider,
comment|//            executorService, queueSize);
comment|//        Observer observer = builder.build();
comment|//        builder.getBackgroundObserver();
name|LuceneIndexEditorProvider
name|editorProvider
init|=
operator|new
name|LuceneIndexEditorProvider
argument_list|(
literal|null
argument_list|,
name|tracker
argument_list|,
operator|new
name|ExtractedTextCache
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|null
argument_list|,
name|mountInfoProvider
argument_list|)
decl_stmt|;
name|editorProvider
operator|.
name|setIndexingQueue
argument_list|(
name|queue
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Oak: "
operator|+
name|oak
operator|+
literal|" with nodeStore "
operator|+
name|nodeStore
argument_list|)
expr_stmt|;
name|Jcr
name|jcr
init|=
operator|new
name|Jcr
argument_list|(
name|oak
argument_list|)
operator|.
name|with
argument_list|(
name|editorProvider
argument_list|)
operator|.
name|with
argument_list|(
operator|(
name|Observer
operator|)
name|provider
argument_list|)
operator|.
name|with
argument_list|(
name|localIndexObserver
argument_list|)
comment|// .with(observer)
operator|.
name|with
argument_list|(
operator|(
name|QueryIndexProvider
operator|)
name|provider
argument_list|)
operator|.
name|withAsyncIndexing
argument_list|(
literal|"async"
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|Repository
name|r
init|=
name|jcr
operator|.
name|createRepository
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
specifier|public
name|String
name|getStoreType
parameter_list|( )
block|{
return|return
name|storeType
return|;
block|}
specifier|public
name|void
name|setStoreType
parameter_list|(
name|String
name|storeType
parameter_list|)
block|{
name|this
operator|.
name|storeType
operator|=
name|storeType
expr_stmt|;
block|}
specifier|public
name|Path
name|getRepositoryPath
parameter_list|( )
block|{
return|return
name|repositoryPath
return|;
block|}
specifier|public
name|void
name|setRepositoryPath
parameter_list|(
name|Path
name|repositoryPath
parameter_list|)
block|{
name|this
operator|.
name|repositoryPath
operator|=
name|repositoryPath
expr_stmt|;
block|}
specifier|public
name|void
name|setRepositoryPath
parameter_list|(
name|String
name|repositoryPath
parameter_list|)
block|{
name|this
operator|.
name|repositoryPath
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|repositoryPath
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|this
operator|.
name|repositoryPath
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|this
operator|.
name|repositoryPath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|ExecutorService
name|createExecutor
parameter_list|()
block|{
name|ThreadPoolExecutor
name|executor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|,
literal|60L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ThreadFactory
argument_list|()
block|{
specifier|private
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Thread
operator|.
name|UncaughtExceptionHandler
name|handler
init|=
operator|new
name|Thread
operator|.
name|UncaughtExceptionHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|uncaughtException
parameter_list|(
name|Thread
name|t
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error occurred in asynchronous processing "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Thread
name|newThread
parameter_list|(
annotation|@
name|Nonnull
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|r
argument_list|,
name|createName
argument_list|()
argument_list|)
decl_stmt|;
name|thread
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setPriority
argument_list|(
name|Thread
operator|.
name|MIN_PRIORITY
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setUncaughtExceptionHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|thread
return|;
block|}
specifier|private
name|String
name|createName
parameter_list|()
block|{
return|return
literal|"oak-lucene-"
operator|+
name|counter
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|executor
operator|.
name|setKeepAliveTime
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
name|executor
operator|.
name|allowCoreThreadTimeOut
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|executor
return|;
block|}
block|}
end_class

end_unit

