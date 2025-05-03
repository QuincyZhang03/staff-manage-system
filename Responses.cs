using System.Collections.Generic;
using System.Windows;

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
            return null;
        }
    }
    class BlockChainIntegrity
    {
        public bool isLegal;
        public string metadata;
    }
    class LoginResponse : AbstractResponse
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
    }
    class ChangePasswordResponse : AbstractResponse
    {
        public string message;
    }
    class CheckIntegrityResponse : AbstractResponse
    {
        public BlockChainIntegrity message;
    }
}