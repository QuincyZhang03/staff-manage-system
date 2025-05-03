using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;

namespace StaffManageSystemClient
{

    public partial class StaffManagement : Window
    {
        private readonly User user;
        private readonly ObservableCollection<Staff> staffList = new ObservableCollection<Staff>();
        private readonly ObservableCollection<Department> departmentList = new ObservableCollection<Department>();
        public StaffManagement()
        {
            InitializeComponent();
            UI_TabStaff_ListViewStaff.ItemsSource = staffList;
            UI_TabStaff_ListViewDepartment.ItemsSource = departmentList;
        }
        public StaffManagement(User user) : this() //把用户信息当做参数传入，初始化用户信息
        {
            this.user = user;
            UI_TextUsername.Text = user.username;
            UI_TextUserLevel.Text = user.GetLevelDisplay();
        }

        private void UI_Tool_Search_Click(object sender, RoutedEventArgs e)
        {
            staffList.Clear();
            departmentList.Clear();
            Department.departNames.Clear();
            QueryRequest request = new QueryRequest(user.username);
            QueryResponse response = (QueryResponse)request.CommitRequest();
            if (response == null)
            {
                MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else if (!response.isSuccess)
            {
                MessageBox.Show(this, "服务器拒绝查询！", "查询失败", MessageBoxButton.OK, MessageBoxImage.Exclamation);
            }
            else //查询成功，则读取数据
            {
                foreach (Staff s in response.staff)
                {
                    staffList.Add(s);
                }
                foreach (Department d in response.department)
                {
                    departmentList.Add(d);
                    Department.departNames.Add(d.id, d.name);
                }
            }
        }



        private void UI_ChangePassword_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                UI_ButtonChangePassword_Click(sender, e);
            }
        }

        private void UI_ButtonChangePassword_Click(object sender, RoutedEventArgs e)
        {
            string old_password = UI_TextBoxOldPassword.Password;
            string new_password = UI_TextBoxNewPassword.Password;
            string repeat_new_password = UI_TextBoxRepeatNewPassword.Password;
            if (old_password.Length == 0 || new_password.Length == 0 || repeat_new_password.Length == 0)
            {
                MessageBox.Show("密码不能为空！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else if (old_password.Length < 6 || new_password.Length < 6)
            {
                MessageBox.Show("密码长度不符合要求：至少6字符", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else if (new_password != repeat_new_password)
            {
                MessageBox.Show("两次输入的新密码不一致，请检查", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else//处理更改密码
            {
                ChangePasswordRequest request = new ChangePasswordRequest(user.username, old_password, new_password);
                ChangePasswordResponse response = (ChangePasswordResponse)request.CommitRequest();
                if (response == null)
                {
                    MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                }
                else if (response.message == "WRONG_OLD_PASSWORD")
                {
                    MessageBox.Show(this, "旧密码错误！", "修改密码失败", MessageBoxButton.OK, MessageBoxImage.Warning);
                }
                else if (response.message == "ERROR")
                {
                    MessageBox.Show(this, "服务器端发生错误", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                }
                else if (response.message == "SUCCESS")
                {
                    MessageBox.Show(this, "修改密码成功！", "修改密码成功", MessageBoxButton.OK, MessageBoxImage.Information);
                }
            }
        }


        private void UI_ButtonCheckIntegrity_Click(object sender, RoutedEventArgs e)
        {
            CheckIntegrityRequest request = new CheckIntegrityRequest(user.username);
            CheckIntegrityResponse response = (CheckIntegrityResponse)request.CommitRequest();
            if (response == null)
            {
                MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }else if (response.message == null)
            {
                MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            else if (response.message.isLegal)
            {
                UI_TextIntegrity.Foreground = Brushes.Green;
            }
            else
                UI_TextIntegrity.Foreground = Brushes.Red;
            UI_TextIntegrity.Text = response.message.metadata;
        }
    }
}
