namespace StaffManageSystemClient
{
    public class User
    {
        public string username;
        public int level;

        public string GetLevelDisplay()
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