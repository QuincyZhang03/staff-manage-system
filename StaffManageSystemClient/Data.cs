using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Web.Script.Serialization;

namespace StaffManageSystemClient
{
    public class Staff : INotifyPropertyChanged
    {

        public event PropertyChangedEventHandler PropertyChanged;

        public int id;//此处仍然使用public以便反序列化
        public string name;
        public string gender;
        public string birthday;
        public string telephone;
        public int department;
        public string position;
        public double salary;

        public Staff() { }//反序列化需要有一个无参构造方法
        public Staff(int id, string name, string gender, string birthday, string telephone, int department, string position, double salary)
        {
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.birthday = birthday;
            this.telephone = telephone;
            this.department = department;
            this.position = position;
            this.salary = salary;
        }

        //以下使用属性，便于在列表中显示
        [ScriptIgnore]
        public int Id
        {
            get => id; set
            {
                id = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Id)));
            }
        }
        [ScriptIgnore]
        public string Name
        {
            get => name; set
            {
                name = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Name)));
            }
        }
        [ScriptIgnore]
        public string Gender
        {
            get => gender; set
            {
                gender = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Gender)));
            }
        }
        [ScriptIgnore]
        public string Birthday
        {
            get => birthday; set
            {
                birthday = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Birthday)));
            }
        }
        [ScriptIgnore]
        public string Telephone
        {
            get => telephone; set
            {
                telephone = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Telephone)));
            }
        }
        [ScriptIgnore]
        public int Department
        {
            get => department; 
            set
            {
                department = Convert.ToInt32(value);
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Id)));
            }
        }
        [ScriptIgnore]
        public string DepartmentDisplayName//在前端显示的是这个
        {
            get => StaffManageSystemClient.Department.GetDepartmentName(department);
        }
        [ScriptIgnore]
        public string Position { get => position; set
            {
                position = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Position)));
            }
        }
        [ScriptIgnore]
        public double Salary { get => salary; set
            {
                salary = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Salary)));
            }
        }

        private bool _modified;
        [ScriptIgnore]
        public bool Modified //该属性用于标识本数据是否修改过，用于在“修改数据”窗口中突出显示修改过的数据
        {
            get => _modified;
            set { 
                _modified = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Modified)));
            }
        }

        public Staff Clone()
        {
            return new Staff(id, name, gender, birthday, telephone, department, position, salary);
        }
    }

    public class Department : INotifyPropertyChanged
    {
        public int id;
        public string name;
        public event PropertyChangedEventHandler PropertyChanged;

        [ScriptIgnore]
        public int Id
        {
            get => id; 
            set
            {
                id = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Id)));
            }
        }
        [ScriptIgnore]
        public string Name { get => name; set
            {
                name = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Name)));
            }
        }

        private bool _modified;
        [ScriptIgnore]
        public bool Modified //该属性用于标识本数据是否修改过，用于在“修改数据”窗口中突出显示修改过的数据
        {
            get => _modified;
            set
            {
                _modified = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Modified)));
            }
        }
        [ScriptIgnore]//序列化时忽略该属性
        public static readonly Dictionary<int, string> departNames = new Dictionary<int, string>();


        public Department() { }

        public Department(int id, string name)
        {
            this.id = id;
            this.name = name;
        }

        public Department Clone()
        {
            return new Department(id, name);
        }
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
