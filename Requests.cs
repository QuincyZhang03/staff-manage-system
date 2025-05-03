using System.Net;
using System;
using System.Web.Script.Serialization;
using System.Collections.Generic;
using System.Windows;

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
                string response_json = webClient.UploadString(URL,"POST", NetUtility.SerializeRequest(this));
                return AbstractResponse.ParseResponse(response_json);
            }
            catch (Exception e)
            {
                MessageBox.Show("发送请求失败！错误信息如下：\n"+e.ToString());
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
}
