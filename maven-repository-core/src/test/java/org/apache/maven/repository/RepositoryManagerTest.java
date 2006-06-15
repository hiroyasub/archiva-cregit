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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
import|;
end_import

begin_comment
comment|/**  * @author Jason van Zyl  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryManagerTest
extends|extends
name|PlexusTestCase
block|{
specifier|public
name|void
name|testLegacyRepositoryConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryManager
name|rm
init|=
operator|(
name|RepositoryManager
operator|)
name|lookup
argument_list|(
name|RepositoryManager
operator|.
name|ROLE
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

