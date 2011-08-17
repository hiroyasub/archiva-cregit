begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|PingService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since TODO  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"pingService#rest"
argument_list|)
specifier|public
class|class
name|DefaultPingService
implements|implements
name|PingService
block|{
specifier|public
name|String
name|ping
parameter_list|()
block|{
return|return
literal|"Yeah Baby It rocks!"
return|;
block|}
block|}
end_class

end_unit

