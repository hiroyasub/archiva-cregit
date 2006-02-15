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
operator|.
name|configuration
package|;
end_package

begin_comment
comment|/**  * @author Ben Walding  */
end_comment

begin_class
specifier|public
class|class
name|GlobalRepoConfiguration
extends|extends
name|FileRepoConfiguration
block|{
specifier|public
name|GlobalRepoConfiguration
parameter_list|(
name|String
name|basePath
parameter_list|)
block|{
name|super
argument_list|(
literal|"global"
argument_list|,
literal|"file:///"
operator|+
name|basePath
argument_list|,
literal|"Global Repository"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

