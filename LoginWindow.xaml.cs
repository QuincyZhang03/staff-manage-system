using System.Windows;
using System.Windows.Input;


namespace StaffManageSystemClient
{

    public partial class LoginWindow : Window
    {
        public LoginWindow()
        {
            InitializeComponent();
            UI_TextboxLoginUsername.Focus();
        }

        private void UI_ButtonReg_Click(object sender, RoutedEventArgs e)//注册
        {
            string username = UI_TextboxRegUsername.Text;
            string password = UI_TextboxRegPassword.Password;
            string repeat_password = UI_TextboxRegRepeatPassword.Password;
            if (username.Length == 0 || password.Length == 0 || repeat_password.Length == 0)
            {
                MessageBox.Show("用户名或密码不能为空！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else if (username.Length > 15 || username.Length < 3)
            {
                MessageBox.Show("用户名长度不符合要求：3-15字符", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else if (password.Length < 6)
            {
                MessageBox.Show("密码长度不符合要求：至少6字符", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else if (password != repeat_password)
            {
                MessageBox.Show("两次输入的密码不一致，请检查！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else //处理注册
            {
                RegisterRequest request = new RegisterRequest(username, password);
                RegisterResponse response = (RegisterResponse)request.CommitRequest();
                if (response == null)
                {
                    MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                }
                else if (response.message == "USER_ALREADY_EXISTS")
                {
                    MessageBox.Show(this, "用户名已存在，请直接登录！", "登录失败", MessageBoxButton.OK, MessageBoxImage.Warning);
                    UI_TabLogin.SelectedItem = UI_TabLogin_TabItemLogin;
                }
                else if (response.message == "ERROR")
                {
                    MessageBox.Show(this, "服务器端发生错误", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                }
                else if (response.message == "SUCCESS")
                {
                    MessageBox.Show(this, "注册成功！欢迎使用，" + username, "注册成功", MessageBoxButton.OK, MessageBoxImage.Information);
                    User user = new User(username, 0);
                    new StaffManagement(user).Show();
                    Close();//把该干的干完了再Close
                }
                else
                {
                    MessageBox.Show(this, "未知响应：" + response.response_type, "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }
        }

        private void UI_ButtonLogin_Click(object sender, RoutedEventArgs e)//登录
        {
            string username = UI_TextboxLoginUsername.Text;
            string password = UI_TextboxLoginPassword.Password;
            if (username.Length == 0 || password.Length == 0)
            {
                MessageBox.Show("用户名或密码不能为空！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else //处理登录
            {
                LoginRequest request = new LoginRequest(username, password);
                LoginResponse response = (LoginResponse)request.CommitRequest();
                if (response == null)
                {
                    MessageBox.Show(this, "向服务器发送请求失败！", "错误", MessageBoxButton.OK, MessageBoxImage.Warning);
                }
                else if (response.message == "NO_SUCH_USER")
                {
                    MessageBox.Show(this, "用户名不存在！", "登录失败", MessageBoxButton.OK, MessageBoxImage.Warning);
                }
                else if (response.message == "WRONG_PASSWORD")
                {
                    MessageBox.Show(this, "密码错误！", "登录失败", MessageBoxButton.OK, MessageBoxImage.Warning);
                }
                else if (response.message == "ERROR")
                {
                    MessageBox.Show(this, "服务器端发生错误", "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                }
                else if (response.message == "VERIFIED")
                {
                    User user = response.user_info;
                    MessageBox.Show(this, "登录成功！欢迎您，" + user.username, "登录成功", MessageBoxButton.OK, MessageBoxImage.Information);
                    new StaffManagement(user).Show();
                    Close();//把该干的干完了再Close
                }
                else
                {
                    MessageBox.Show(this, "未知响应：" + response.response_type, "错误", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }
        }

        private void UI_Login_KeyDown(object sender, KeyEventArgs e)
        {
            var key = e.Key;
            if (key == Key.F7)//debug
            {
                UI_TextboxLoginUsername.Text = "quincyzhang";
                UI_TextboxLoginPassword.Password = "Zhangjian5e";
                UI_ButtonLogin_Click(sender, e);
            }
            if (key == Key.Enter)
            {
                UI_ButtonLogin_Click(sender, e);
            }
        }
        private void UI_Register_KeyDown(object sender, KeyEventArgs e)
        {
            var key = e.Key;
            if (key == Key.Enter)
            {
                UI_ButtonReg_Click(sender, e);
            }
        }
    }
}
