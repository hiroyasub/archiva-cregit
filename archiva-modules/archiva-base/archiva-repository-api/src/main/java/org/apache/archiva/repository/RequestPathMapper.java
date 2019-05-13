begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
package|;
end_package

begin_comment
comment|/**  *  * Maps request paths to native repository paths. Normally HTTP requests and the path in the repository  * storage should be identically.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RequestPathMapper
block|{
comment|/**      * Maps a request path to a repository path. The request path should be relative      * to the repository. The resulting path should always start with a '/'.      * The returned object contains additional information, if this request      *      * @param requestPath      * @return      */
name|RelocatablePath
name|relocatableRequestToRepository
parameter_list|(
name|String
name|requestPath
parameter_list|)
function_decl|;
name|String
name|requestToRepository
parameter_list|(
name|String
name|requestPath
parameter_list|)
function_decl|;
comment|/**      * Maps a repository path to a request path. The repository path is relative to the      * repository. The resulting path should always start with a '/'.      *      * @param repositoryPath      * @return      */
name|String
name|repositoryToRequest
parameter_list|(
name|String
name|repositoryPath
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

