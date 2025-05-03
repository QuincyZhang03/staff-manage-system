using System.Web.Script.Serialization;

namespace StaffManageSystemClient
{
    public class User
    {
        public string username;
        public int level;

        [ScriptIgnore]
        public const int DEFAULT_USER = 0;
        [ScriptIgnore]
        public const int OPERATOR = 1;
        [ScriptIgnore]
        public const int ADMIN = 2;

        public User() { }//反序列化需要有一个无参构造方法
        
        public User(string username, int level)
        {
            this.username = username;
            this.level = level;
        }

        public static string GetLevelDisplay(int level)
        {
            if (level == 0)
                return "普通用户";
            else if (level == 1)
                return "管理员";
            else if (level == 2)
                return "超级管理员";
            return "未知等级：" + level;
        }
    }
}