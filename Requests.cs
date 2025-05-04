using System.Net;
using System;
using System.Web.Script.Serialization;
using System.Collections.Generic;
using System.Windows;
using System.Text;

namespace StaffManageSystemClient
{
    class NetUtility
    {
        public static readonly JavaScriptSerializer serilizer = new JavaScriptSerializer();
        public static string SerializeRequest(AbstractRequest request)
        {
            return serilizer.Serialize(request);
        }
        public static Dictionary<string, object> DeserializeJson(string json)
        {
            var dict = serilizer.DeserializeObject(json);
            if (dict is Dictionary<string, object>)
                return (Dictionary<string, object>)dict;
            return null;
        }
    }
    abstract class AbstractRequest
    {
        private static readonly WebClient webClient = new WebClient();
        private const string URL = "http://localhost:23456/api/staff";
        public AbstractResponse CommitRequest()//向服务器提交请求
        {
            webClient.Headers.Set(HttpRequestHeader.ContentType, "application/json; charset=UTF-8");
            try
            {
                string rawjson=NetUtility.SerializeRequest(this);
                byte[] bytesToSend=Encoding.UTF8.GetBytes(rawjson);
                byte[] response_data = webClient.UploadData(URL, "POST", bytesToSend);
                string response_json=Encoding.UTF8.GetString(response_data);
                return AbstractResponse.ParseResponse(response_json);
            }
            catch (Exception e)
            {
                MessageBox.Show("发送请求失败！错误信息如下：\n" + e.ToString());
            }
            return null;
        }
    }
    class LoginRequest : AbstractRequest
    {
        public readonly string request_type = "LOGIN";
        public readonly string username;
        public readonly string input_password;

        public LoginRequest(string username, string input_password)
        {
            this.username = username;
            this.input_password = input_password;
        }
    }
    class RegisterRequest : AbstractRequest
    {
        public readonly string request_type = "REGISTER";
        public readonly string username;
        public readonly string input_password;

        public RegisterRequest(string username, string input_password)
        {
            this.username = username;
            this.input_password = input_password;
        }
    }
    class QueryRequest : AbstractRequest
    {
        public readonly string request_type = "QUERY";
        public readonly string username;

        public QueryRequest(string username)
        {
            this.username = username;
        }
    }
    class ChangePasswordRequest : AbstractRequest
    {
        public readonly string request_type = "CHANGE_PASSWORD";
        public readonly string username;
        public readonly string old_password;
        public readonly string new_password;

        public ChangePasswordRequest(string username, string old_password, string new_password)
        {
            this.username = username;
            this.old_password = old_password;
            this.new_password = new_password;
        }
    }
    class CheckIntegrityRequest : AbstractRequest
    {
        public readonly string request_type = "CHECK_INTEGRITY";
        public readonly string username;

        public CheckIntegrityRequest(string username)
        {
            this.username = username;
        }
    }
    class ResetUserRequest : AbstractRequest
    {
        public readonly string request_type = "RESET_USER";
        public readonly string username;
        public readonly string to_reset;

        public ResetUserRequest(string username, string to_reset)
        {
            this.username = username;
            this.to_reset = to_reset;
        }
    }
    class DeleteUserRequest : AbstractRequest
    {
        public readonly string request_type = "DELETE_USER";
        public readonly string username;
        public readonly string to_delete;

        public DeleteUserRequest(string username, string to_delete)
        {
            this.username = username;
            this.to_delete = to_delete;
        }
    }
    class DeleteRequest : AbstractRequest
    {
        public readonly string request_type = "DELETE";
        public readonly string username;
        public readonly string type;//可以为"staff"或"department"
        public readonly List<string> dataList; //一定是json文本列表，不是json对象列表

        public DeleteRequest(string username, string type, List<string> dataList)
        {
            this.username = username;
            this.type = type;
            this.dataList = dataList;
        }
    }
    class ModifyUserLevelRequest : AbstractRequest
    {
        public readonly string request_type = "MODIFY_USER_LEVEL";
        public readonly string username;
        public readonly string to_modify;
        public readonly int new_level;
        public readonly string key;

        public ModifyUserLevelRequest(string username, string to_modify, int new_level, string key)
        {
            this.username = username;
            this.to_modify = to_modify;
            this.new_level = new_level;
            this.key = key;
        }
    }
    class CreateRequest : AbstractRequest
    {
        public readonly string request_type = "CREATE";
        public readonly string username;
        public readonly string type;//可以为"staff"或"department"
        public readonly List<string> dataList; //一定是json文本列表，不是json对象列表

        public CreateRequest(string username, string type, List<string> dataList)
        {
            this.username = username;
            this.type = type;
            this.dataList = dataList;
        }
    }
    class UpdateRequest : AbstractRequest
    {
        public readonly string request_type = "UPDATE";
        public readonly string username;
        public readonly string type;//可以为"staff"或"department"
        public readonly List<string[]> dataList; //一定是json文本列表，不是json对象列表

        public UpdateRequest(string username, string type, List<string[]> dataList)
        {
            this.username = username;
            this.type = type;
            this.dataList = dataList;
        }
    }
}
