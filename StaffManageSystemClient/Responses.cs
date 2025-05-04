using System.Collections.Generic;

namespace StaffManageSystemClient
{

    class AbstractResponse
    {
        public string response_type;
        public static AbstractResponse ParseResponse(string json)
        {
            Dictionary<string, object> dict = NetUtility.DeserializeJson(json);
            if (dict == null) return null;
            string response_type = (string)dict["response_type"];

            if (response_type == "LOGIN")
                return NetUtility.serilizer.Deserialize<LoginResponse>(json);
            else if (response_type == "REGISTER")
                return NetUtility.serilizer.Deserialize<RegisterResponse>(json);
            else if (response_type == "QUERY")
                return NetUtility.serilizer.Deserialize<QueryResponse>(json);
            else if (response_type == "CHANGE_PASSWORD")
                return NetUtility.serilizer.Deserialize<ChangePasswordResponse>(json);
            else if (response_type == "CHECK_INTEGRITY")
                return NetUtility.serilizer.Deserialize<CheckIntegrityResponse>(json);
            else if (response_type == "RESET_USER")
                return NetUtility.serilizer.Deserialize<ResetUserResponse>(json);
            else if (response_type == "DELETE_USER")
                return NetUtility.serilizer.Deserialize<DeleteUserResponse>(json);
            else if (response_type == "DELETE")
                return NetUtility.serilizer.Deserialize<DeleteResponse>(json);
            else if (response_type == "MODIFY_USER_LEVEL")
                return NetUtility.serilizer.Deserialize<ModifyUserLevelResponse>(json);
            else if (response_type == "CREATE")
                return NetUtility.serilizer.Deserialize<CreateResponse>(json);
            else if (response_type == "UPDATE")
                return NetUtility.serilizer.Deserialize<UpdateResponse>(json);
            return null;
        }
    }
    class BlockChainIntegrity
    {
        public bool isLegal;
        public string metadata;
    }
    class LoginResponse : AbstractResponse //注意这些请求的属性名必须与json中的一致
    {
        public string message;
        public User user_info;
    }
    class RegisterResponse : AbstractResponse
    {
        public string message;
    }
    class QueryResponse : AbstractResponse
    {
        public bool isSuccess;
        public Staff[] staff;
        public Department[] department;
        public UserData[] user;
    }
    class ChangePasswordResponse : AbstractResponse
    {
        public string message;
    }
    class CheckIntegrityResponse : AbstractResponse
    {
        public BlockChainIntegrity message;
    }
    class ResetUserResponse : AbstractResponse
    {
        public string message;
    }
    class DeleteUserResponse : AbstractResponse
    {
        public string message;
    }
    class DeleteResponse : AbstractResponse
    {
        public string message;
    }
    class ModifyUserLevelResponse : AbstractResponse
    {
        public string message;
    }
    class CreateResponse : AbstractResponse
    {
        public string message;
    }
    class UpdateResponse : AbstractResponse
    {
        public string message;
    }
}