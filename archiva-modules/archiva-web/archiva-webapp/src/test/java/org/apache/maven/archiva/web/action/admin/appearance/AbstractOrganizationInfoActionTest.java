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
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|appearance
package|;
end_package

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
name|Configuration
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
name|web
operator|.
name|action
operator|.
name|AbstractWebworkTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOrganizationInfoActionTest
extends|extends
name|AbstractWebworkTestCase
block|{
specifier|protected
name|MockControl
name|archivaConfigurationControl
decl_stmt|;
specifier|protected
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|protected
name|AbstractAppearanceAction
name|action
decl_stmt|;
specifier|protected
name|Configuration
name|config
decl_stmt|;
specifier|protected
specifier|abstract
name|AbstractAppearanceAction
name|getAction
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|config
operator|=
operator|new
name|Configuration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|archivaConfigurationControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|config
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setVoidCallable
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|reloadAction
parameter_list|()
block|{
name|action
operator|=
name|getAction
argument_list|()
expr_stmt|;
name|action
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

