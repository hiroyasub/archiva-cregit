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
name|model
operator|.
name|ManagedRepository
import|;
end_import

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
name|model
operator|.
name|RemoteRepository
import|;
end_import

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
name|RepositoriesService
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoriesService#rest"
argument_list|)
specifier|public
class|class
name|DefaultRepositoriesService
implements|implements
name|RepositoriesService
block|{
specifier|public
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getManagedRepositories
parameter_list|()
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|getRemoteRepositories
parameter_list|()
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Boolean
name|scanRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|boolean
name|fullScan
parameter_list|)
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
block|}
end_class

end_unit

