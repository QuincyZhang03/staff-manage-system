using System;
using System.Collections.Generic;
using System.Web.Script.Serialization;

namespace StaffManageSystemClient
{
    public class Staff
    {
        public int id;//此处仍然使用public以便反序列化
        public string name;
        public string gender;
        public DateTime birthday;
        public string telephone;
        public int department;
        public string position;
        public double salary;

        //以下使用属性，便于在列表中显示
        public int Id { get => id; }
        public string Name { get => name; }
        public string Gender { get => gender; }
        public string Birthday { get => birthday.ToString("yyyy-MM-dd"); }
        public string Telephone { get => telephone; }
        public string Department { get => StaffManageSystemClient.Department.GetDepartmentName(department); }
        public string Position { get => position; }
        public double Salary { get => salary; }
    }

    public class Department
    {
        public int id;
        public string name;

        public int Id { get => id; set => id = value; }
        public string Name { get => name; set => name = value; }

        [ScriptIgnore]//序列化时忽略该属性
        public static readonly Dictionary<int, string> departNames = new Dictionary<int, string>();


        public static string GetDepartmentName(int id)
        {
            return departNames[id];
        }
    }
    public class UserData
    {
        public readonly string username;
        public readonly int level;
        public readonly string password;

        public string Username { get => username; }
        public string Level { get => User.GetLevelDisplay(level); }
        public string Password { get => password; }

    }

}
