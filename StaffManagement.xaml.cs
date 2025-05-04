using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;

namespace StaffManageSystemClient
{

    public partial class StaffManagement : Window, INotifyPropertyChanged
    {

        public event PropertyChangedEventHandler PropertyChanged;

        private readonly User user;
        private readonly ObservableCollection<Staff> staffList = new ObservableCollection<Staff>();
        private readonly ObservableCollection<Department> departmentList = new ObservableCollection<Department>();
        private readonly ObservableCollection<UserData> userDataList = new ObservableCollection<UserData>();

        private bool _canCreate;
        public bool CanCreate
        {
            get => _canCreate;
            set
            {
                _canCreate = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(CanCreate)));
            }
        }

        private bool _canDelete;
        public bool CanDelete
        {
            get => _canDelete;
            set
            {
                _canDelete = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(CanDelete)));
            }
        }

        private bool _canEdit;
        public bool CanEdit
        {
            get => _canEdit;
            set
            {
                _canEdit = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(CanEdit)));
            }
        }

        private bool _userSelected;
        public bool UserSelected
        {
            get => _userSelected;
            set
            {
                _userSelected = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(UserSelected)));
            }
        }


        public StaffManagement()
        {
            InitializeComponent();
            UI_TabStaff_ListViewStaff.ItemsSource = staffList;
            UI_TabStaff_ListViewDepartment.ItemsSource = departmentList;
            UI_TabStaff_ListViewUser.ItemsSource = userDataList;
            UI_ToolBar.DataContext = this;
        }
        public StaffManagement(User user) : this() //把用户信息当做参数传入，初始化用户信息
        {
            this.user = user;
            UI_TextUsername.Text = user.username;
            UI_TextUserLevel.Text = User.GetLevelDisplay(user.level);
            UI_Tool_Create.Visibility = user.level >= User.OPERATOR ? Visibility.Visible : Visibility.Collapsed;
            UI_Tool_Edit.Visibility = user.level >= User.OPERATOR ? Visibility.Visible : Visibility.Collapsed;
            UI_Tool_Delete.Visibility = user.level >= User.OPERATOR ? Visibility.Visible : Visibility.Collapsed;
            UI_Tool_ResetUser.Visibility = user.level >= User.OPERATOR ? Visibility.Visible : Visibility.Collapsed;
            UI_Tool_ModifyUser.Visibility = user.level >= User.ADMIN ? Visibility.Visible : Visibility.Collapsed;
            UI_TabUser.Visibility = user.level >= User.OPERATOR ? Visibility.Visible : Visibility.Collapsed;
            UI_TabCheckIntegrity.Visibility = user.level >= User.ADMIN ? Visibility.Visible : Visibility.Collapsed;
        }
        private void UI_Tool_Search_Click(object sender, RoutedEventArgs e)
        {
            staffList.Clear();
            departmentList.Clear();
            userDataList.Clear();
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
                if (response.user != null)
                {
                    foreach (UserData u in response.user)
                    {
                        userDataList.Add(u);
                    }
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
            }
            else if (response.message == null)
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

        private void UI_Tool_Logout_Click(object sender, RoutedEventArgs e)//退出登录按钮
        {
            new LoginWindow().Show();
            Close();
        }

        private void UI_Tool_ResetUser_Click(object sender, RoutedEventArgs e)//重置密码按钮
        {
            UserData userData = (UserData)UI_TabStaff_ListViewUser.SelectedItem;
            if (userData != null)
            {
                string to_reset = userData.Username;
                MessageBoxResult confirm = MessageBox.Show(this, "确定将用户 " + to_reset + " 的密码重置吗？\n重置后，该用户的密码将变为123456。", "重置确认", MessageBoxButton.YesNo, MessageBoxImage.Question);
                if (confirm == MessageBoxResult.Yes)
                {
                    ResetUserRequest request = new ResetUserRequest(user.username, to_reset);
                    ResetUserResponse response = (ResetUserResponse)request.CommitRequest();
                    if (response == null)
                    {
                        MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                    }
                    else if (response.message == null)
                    {
                        MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                    else
                    {
                        MessageBox.Show(this, response.message, "重置结果", MessageBoxButton.OK, MessageBoxImage.Information);
                        UI_Tool_Search_Click(sender, e);//重置结束后重新查询
                    }
                }
            }
        }

        private void UI_Tool_Delete_Click(object sender, RoutedEventArgs e)
        {
            if (UI_Tab.SelectedItem == UI_TabStaff || UI_Tab.SelectedItem == UI_TabDepartment)
            { //删除数据（员工、部门）操作
                string type;
                ListView source;//数据源
                if (UI_Tab.SelectedItem == UI_TabStaff)
                {
                    type = "staff";
                    source = UI_TabStaff_ListViewStaff;
                }
                else
                {
                    type = "department";
                    source = UI_TabStaff_ListViewDepartment;
                }
                int count = source.SelectedItems.Count;
                MessageBoxResult confirm = MessageBox.Show(this, "确定删除选中的" + count + "条数据吗？", "删除确认", MessageBoxButton.YesNo, MessageBoxImage.Question);
                if (confirm == MessageBoxResult.Yes)
                {
                    List<string> dataList = new List<string>();//待删除数据列表，注意是json文本列表，不是json对象列表
                    foreach (object item in source.SelectedItems)
                        dataList.Add(NetUtility.serilizer.Serialize(item));
                    DeleteRequest request = new DeleteRequest(user.username, type, dataList);
                    DeleteResponse response = (DeleteResponse)request.CommitRequest();
                    if (response == null)
                    {
                        MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                    }
                    else if (response.message == null)
                    {
                        MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                    else
                    {
                        MessageBox.Show(this, response.message, "删除结果", MessageBoxButton.OK, MessageBoxImage.Information);
                        UI_Tool_Search_Click(sender, e);//重置结束后重新查询
                    }
                }
            }
            else if (UI_Tab.SelectedItem == UI_TabUser)
            { //删除用户操作
                UserData to_delete = (UserData)UI_TabStaff_ListViewUser.SelectedItem;
                if (to_delete != null)
                { //理论上不可能为null
                    MessageBoxResult confirm = MessageBox.Show(this, "确定删除" + to_delete.Level + " " + to_delete.Username + " 的用户信息吗？\n注意：本操作不可撤销，请谨慎！", "删除确认", MessageBoxButton.YesNo, MessageBoxImage.Question);
                    if (confirm == MessageBoxResult.Yes)
                    {
                        DeleteUserRequest request = new DeleteUserRequest(user.username, to_delete.Username);
                        DeleteUserResponse response = (DeleteUserResponse)request.CommitRequest();
                        if (response == null)
                        {
                            MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                        }
                        else if (response.message == null)
                        {
                            MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                        }
                        else
                        {
                            MessageBox.Show(this, response.message, "删除结果", MessageBoxButton.OK, MessageBoxImage.Information);
                            UI_Tool_Search_Click(sender, e);//重置结束后重新查询
                        }
                    }
                }
            }
        }

        private void UI_Tool_ModifyUser_Click(object sender, RoutedEventArgs e)
        {
            UserData selectedUser = (UserData)UI_TabStaff_ListViewUser.SelectedItem;
            if (selectedUser != null)
            {
                LevelSelect levelSelect = new LevelSelect(this, selectedUser.Username, selectedUser.level);
                levelSelect.ShowDialog();
                if (levelSelect.changed)
                {
                    int newLevel = levelSelect.selectedLevel;
                    string key = levelSelect.key;
                    ModifyUserLevelRequest request = new ModifyUserLevelRequest(user.username, selectedUser.Username, newLevel, key);
                    ModifyUserLevelResponse response = (ModifyUserLevelResponse)request.CommitRequest();
                    if (response == null)
                    {
                        MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                    }
                    else if (response.message == null)
                    {
                        MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                    else
                    {
                        MessageBox.Show(this, response.message, "删除结果", MessageBoxButton.OK, MessageBoxImage.Information);
                        UI_Tool_Search_Click(sender, e);//删除结束后重新查询
                    }
                }
            }
        }

        private void UI_Tab_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            //此处更改选中Tab以及更改列表选中项都会发送这个事件，其中列表选中项更改事件会向上传递。
            //可以利用e.Source的不同来判断是谁发送的事件。
            int selectedStaffCount = UI_TabStaff_ListViewStaff.SelectedItems.Count;
            int selectedDepartmentCount = UI_TabStaff_ListViewDepartment.SelectedItems.Count;
            bool selectedUser = UI_TabStaff_ListViewUser.SelectedItem != null;

            CanCreate = UI_Tab.SelectedItem == UI_TabStaff || UI_Tab.SelectedItem == UI_TabDepartment;
            CanDelete =
                (UI_Tab.SelectedItem == UI_TabStaff && selectedStaffCount > 0) ||
                (UI_Tab.SelectedItem == UI_TabDepartment && selectedDepartmentCount > 0) ||
                (UI_Tab.SelectedItem == UI_TabUser && selectedUser);
            CanEdit = UI_Tab.SelectedItem == UI_TabStaff || UI_Tab.SelectedItem == UI_TabDepartment;
            UserSelected = UI_Tab.SelectedItem == UI_TabUser && selectedUser;
        }

        private void UI_Tool_Create_Click(object sender, RoutedEventArgs e)
        {
            if (UI_Tab.SelectedItem == UI_TabStaff)//添加员工
            {
                CreateStaffWindow createWindow = new CreateStaffWindow(this, departmentList);
                if ((bool)!createWindow.ShowDialog()) return;
                int count = createWindow.staffList.Count;
                if (count > 0)
                {
                    List<string> dataList = new List<string>();
                    foreach (Staff staff in createWindow.staffList)
                    {
                        dataList.Add(NetUtility.serilizer.Serialize(staff));
                    }
                    CreateRequest request = new CreateRequest(user.username, "staff", dataList);
                    CreateResponse response = (CreateResponse)request.CommitRequest();
                    if (response == null)
                    {
                        MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                    }
                    else if (response.message == null)
                    {
                        MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                    else
                    {
                        MessageBox.Show(this, response.message, "创建结果", MessageBoxButton.OK, MessageBoxImage.Information);
                        UI_Tool_Search_Click(sender, e);//创建结束后重新查询
                    }
                }
            }
            else if (UI_Tab.SelectedItem == UI_TabDepartment)//添加部门
            {
                CreateDepartmentWindow createWindow = new CreateDepartmentWindow(this);
                if ((bool)!createWindow.ShowDialog()) return;
                int count = createWindow.departmentList.Count;
                if (count > 0)
                {
                    List<string> dataList = new List<string>();
                    foreach (Department department in createWindow.departmentList)
                    {
                        dataList.Add(NetUtility.serilizer.Serialize(department));
                    }
                    CreateRequest request = new CreateRequest(user.username, "department", dataList);
                    CreateResponse response = (CreateResponse)request.CommitRequest();
                    if (response == null)
                    {
                        MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                    }
                    else if (response.message == null)
                    {
                        MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                    else
                    {
                        MessageBox.Show(this, response.message, "创建结果", MessageBoxButton.OK, MessageBoxImage.Information);
                        UI_Tool_Search_Click(sender, e);//创建结束后重新查询
                    }
                }
            }
        }

        private void UI_Tool_Edit_Click(object sender, RoutedEventArgs e)
        {
            if (UI_Tab.SelectedItem == UI_TabStaff)//编辑员工
            {
                ModifyStaffWindow modifyWindow = new ModifyStaffWindow(this, staffList, departmentList);
                if ((bool)!modifyWindow.ShowDialog()) return;
                int count = modifyWindow.edits.Count;
                if (count > 0)
                {
                    List<string[]> dataList = new List<string[]>();
                    foreach (Staff[] pair in modifyWindow.edits)
                    {
                        dataList.Add(new string[]
                        {
                            NetUtility.serilizer.Serialize(pair[0]),
                            NetUtility.serilizer.Serialize(pair[1])
                        });
                    }
                    UpdateRequest request = new UpdateRequest(user.username, "staff", dataList);
                    UpdateResponse response = (UpdateResponse)request.CommitRequest();
                    if (response == null)
                    {
                        MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                    }
                    else if (response.message == null)
                    {
                        MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                    else
                    {
                        MessageBox.Show(this, response.message, "创建结果", MessageBoxButton.OK, MessageBoxImage.Information);
                        UI_Tool_Search_Click(sender, e);//创建结束后重新查询
                    }
                }

            }
            else if (UI_Tab.SelectedItem == UI_TabDepartment)//编辑部门
            {
                ModifyDepartmentWindow modifyWindow = new ModifyDepartmentWindow(this, departmentList);
                if ((bool)!modifyWindow.ShowDialog()) return;
                int count = modifyWindow.edits.Count;
                if (count > 0)
                {
                    List<string[]> dataList = new List<string[]>();
                    foreach (Department[] pairs in modifyWindow.edits)
                    {
                        dataList.Add(new string[]
                        {
                            NetUtility.serilizer.Serialize(pairs[0]),
                            NetUtility.serilizer.Serialize(pairs[1])
                        });
                    }
                    UpdateRequest request = new UpdateRequest(user.username, "department", dataList);
                    UpdateResponse response = (UpdateResponse)request.CommitRequest();
                    if (response == null)
                    {
                        MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                    }
                    else if (response.message == null)
                    {
                        MessageBox.Show(this, "响应异常", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                    else
                    {
                        MessageBox.Show(this, response.message, "创建结果", MessageBoxButton.OK, MessageBoxImage.Information);
                        UI_Tool_Search_Click(sender, e);//创建结束后重新查询
                    }
                }
            }
        }

        private void UI_MainWindow_Loaded(object sender, RoutedEventArgs e)
        {
            UI_Tool_Search_Click(sender, e);//加载完成后自动查询
        }

        private void UI_Tool_About_Click(object sender, RoutedEventArgs e)
        {
            MessageBox.Show(this, "本套软件开发者为南昌大学计算机科学与技术211班张健\n（学号6109121013），用于本人毕业设计。","关于",MessageBoxButton.OK,MessageBoxImage.Information);
        }

        private void UI_ToolBar_Loaded(object sender, RoutedEventArgs e)//移除溢出小箭头
        {
            FrameworkElement overflowGrid=UI_ToolBar.Template.FindName("OverflowGrid",UI_ToolBar) as FrameworkElement;
            if(overflowGrid != null)
            {
                overflowGrid.Visibility = Visibility.Collapsed;
            }
        }
    }
}
